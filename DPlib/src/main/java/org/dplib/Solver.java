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
extends SwingWorker<List<Result>, Void>
{
    private final Model model;
    private final File javaFile;
    private final View view;
    private final IO io;

    public Solver(Model model, File javaFile, View view, IO io)
    {
        this.model = model;
        this.javaFile = javaFile;
        this.view = view;
        this.io = io;
    }

    private List<Result> solve(Model model, File file)
    throws AnalysisException
    {
        SourceAnalyser sa;

        try { sa = new SourceAnalyser(file, model.getMethodToAnalyseDeclaration()); }
        catch (ParseException | IOException e) { throw new AnalysisException(e); }

        MethodDeclaration userCallingMethod = sa.findMethod("main");
        String userCallingMethodBody = userCallingMethod.getBody().toString();

        checkMatchingMethodBodies(userCallingMethodBody,
                                  model.getCallingMethodBody(),
                                  userCallingMethod.getDeclarationAsString(),
                                  model.getCallingMethodDeclaration());

        System.out.println("Analysing...");
        File classFile;

        try { classFile = new SourceCompiler().compile(file, sa.getClassName()); }
        catch (CompileException e) { throw new AnalysisException(e); }
        List<Result> results = new ClassAnalyser().analyse(classFile, fetchInputArray(model.getResults()));

        System.out.println("Analysis complete.");

        return results;
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
    public List<Result> doInBackground()
    throws AnalysisException, IOException
    {
        return solve(model, javaFile);
    }

    @Override
    public void done()
    {
        List<Result> results;
        try
        {
            results = get();
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

        updateGraphs(results);
        showResult(results);
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

    private void showResult(List<Result> results)
    {
        for(int i = 0; i < results.size(); i++)
        {
            Result studentResult = results.get(i);
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