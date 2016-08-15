package org.DPlib;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.Statement;
import org.DPlib.Display.View;
import org.DPlib.Exception.AnalysisException;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

    public List<Result> solve(Model model, File file)
    throws AnalysisException
    {
        SourceAnalyser sa = new SourceAnalyser(file, model.getMethodToAnalyseDeclaration());
        MethodDeclaration userCallingMethod = sa.findMethod("main");
        String[] userCallingMethodBody = toStringArray(userCallingMethod.getBody().getStmts());

        checkMatchingMethodLengths(userCallingMethodBody, model.getCallingMethodBody());
        checkMatchingMethodBodies(userCallingMethodBody,
                                  model.getCallingMethodBody(),
                                  userCallingMethod.getDeclarationAsString(),
                                  model.getCallingMethodDeclaration());

        ClassAnalyser ca = new ClassAnalyser(file, sa.getClassName());
        List<Result> results = new ArrayList<>();

        for (Result r : model.getResults())
        {
            results.add(ca.analyse(r.getInput()));
        }

        return results;
    }

    private void checkMatchingMethodLengths(String[] userCallingMethod, String[] tutorCallingMethod)
    throws AnalysisException
    {
        if (userCallingMethod.length != tutorCallingMethod.length)
        {
            throw new AnalysisException("Method body lengths do not match: " +
                                                userCallingMethod.length + " != " +
                                                tutorCallingMethod.length);
        }
    }

    private void checkMatchingMethodBodies(String[] userCallingMethod,
                                           String[] tutorCallingMethod,
                                           String userMethodDeclaration,
                                           String modelMethodDeclaration)
    throws AnalysisException
    {
        for (int i = 0; i < userCallingMethod.length; i++)
        {
            if (!userCallingMethod[i].equals(tutorCallingMethod[i]))
            {
                throw new AnalysisException("Submitted calling method \"" + userMethodDeclaration +
                                            "\" does not match modelled calling method \"" + modelMethodDeclaration + "\".");
            }
        }
    }

    private String[] toStringArray(List<Statement> statements)
    {
        String[] strings = new String[statements.size()];

        for (int i = 0; i < statements.size(); i++)
        {
            strings[i] = statements.get(i).toString();
        }
        return strings;
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
            io.errorMsg(e.getCause());
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