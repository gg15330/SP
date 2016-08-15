package org.ggraver.DPlib;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.Statement;
import org.ggraver.DPlib.Display.View;
import org.ggraver.DPlib.Exception.AnalysisException;
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

        if (userCallingMethodBody.length != model.getCallingMethodBody().length)
        {
            throw new Error("method body lengths do not match.");
        }

        for (int i = 0; i < userCallingMethodBody.length; i++)
        {
            if (!userCallingMethodBody[i].equals(model.getCallingMethodBody()[i]))
            {
                throw new AnalysisException("Submitted calling method \"" +
                                                    userCallingMethod.getDeclarationAsString() +
                                                    "\" does not match modelled calling method \"" +
                                                    model.getCallingMethodDeclaration() + "\".");
            }
        }

        ClassAnalyser ca = new ClassAnalyser(file, sa.getClassName());
        List<Result> results = new ArrayList<>();

        for (Result result2 : model.getResults())
        {
            Result result = ca.analyse(result2.getInput());
            results.add(result);
        }

        return results;
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
        List<Result> results = null;
        try
        {
            results = get();
        }
        catch (InterruptedException e)
        {
            io.errorMsg(e.getCause());
            return;
        }
        catch (ExecutionException e) { e.printStackTrace(); }

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

        showResult(results);
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