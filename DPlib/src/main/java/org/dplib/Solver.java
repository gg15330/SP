package org.dplib;

import com.github.javaparser.ParseException;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.dplib.display.View;
import org.dplib.exception.AnalysisException;
import org.dplib.exception.CompileException;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by george on 01/08/16.
 */
public class Solver
extends SwingWorker<Analysis, Void>
{
    private final Model model;
    private final File javaFile;
    private final View view;
    private final IO io;
    private SourceAnalyser sa;
    private ProblemType problemType;
    private List<Result> results;

    public Solver(Model model, File javaFile, View view, IO io)
    {
        this.model = model;
        this.javaFile = javaFile;
        this.view = view;
        this.io = io;
    }

    private void solve(Model model, File file)
    throws AnalysisException
    {

        try { sa = new SourceAnalyser(file, model.getMethodToAnalyseName()); }
        catch (ParseException | IOException e) { throw new AnalysisException(e); }

        MethodDeclaration userCallingMethod = sa.findMethod("main");
        String userCallingMethodBody = userCallingMethod.getBody().toString();

        checkMatchingMethodBodies(userCallingMethodBody,
                                  model.getCallingMethodBody(),
                                  userCallingMethod.getDeclarationAsString(),
                                  model.getCallingMethodDeclaration());

        System.out.println("Analysing...");
        sa.analyse();
        problemType = sa.determineProblemType();

        File classFile;

        try { classFile = new SourceCompiler().compile(file, sa.getClassName()); }
        catch (CompileException e) { throw new AnalysisException(e); }
        results = new ClassAnalyser().analyse(classFile, fetchInputArray(model.getResults()));

        System.out.println("Analysis complete.");
    }

    private String[][] fetchInputArray(List<Result> results)
    {
        String[][] inputs = new String[results.size()][];
        for(int i = 0; i < results.size(); i++)
        {
            inputs[i] = results.get(i).getInput();
        }
        return inputs;
    }

    private void checkMatchingMethodBodies(String userCallingMethod,
                                           String tutorCallingMethod,
                                           String userMethodDeclaration,
                                           String modelMethodDeclaration)
    throws AnalysisException
    {
        if (!userCallingMethod.equals(tutorCallingMethod))
        {
            throw new AnalysisException("Submitted calling method \"" + userMethodDeclaration +
                                                "\" does not match modelled calling method \"" + modelMethodDeclaration + "\".");
        }
    }

    @Override
    public Analysis doInBackground()
    throws AnalysisException, IOException
    {
        solve(model, javaFile);
        Analysis analysis = new Analysis();
        analysis.setProblemType(problemType);
        analysis.setResults(results);
        return analysis;
    }

    @Override
    public void done()
    {
        Analysis analysis;
        try
        {
            analysis = get();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
            return;
        }
        catch (ExecutionException e)
        {
            io.exceptionMsg(new AnalysisException(e.getCause()));
            return;
        }

        updateGraphs(analysis.getResults());
        showResult(analysis);
    }

    private void updateGraphs(List<Result> results)
    {
        DefaultCategoryDataset studentTimeDataset = new DefaultCategoryDataset();
        DefaultCategoryDataset studentOutputDataset = new DefaultCategoryDataset();

        for (int i = 0; i < results.size(); i++)
        {
            Result studentResult = results.get(i);
            studentTimeDataset.addValue(studentResult.getExecutionTime(), "Your code", String.valueOf(i));
            studentOutputDataset.addValue(Integer.parseInt(studentResult.getOutput()), "Your code", String.valueOf(i));
        }

        view.setExecutionTimeGraph(studentTimeDataset);
        view.setOutputGraph(studentOutputDataset);
    }

    private void showResult(Analysis analysis)
    {
        if(!analysis.getProblemType().equals(model.getProblemType()))
        {
            io.fail(model.getProblemType(), analysis.getProblemType());
            return;
        }

        for(int i = 0; i < analysis.getResults().size(); i++)
        {
            Result studentResult = analysis.getResults().get(i);
            Result tutorResult = model.getResults().get(i);

            if(!studentResult.getOutput().equals(tutorResult.getOutput()))
            {
                io.fail(studentResult.getOutput(), tutorResult.getOutput());
                return;
            }
        }

        io.pass();
    }

}