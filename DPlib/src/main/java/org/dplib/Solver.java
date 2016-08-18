package org.dplib;

import com.github.javaparser.ParseException;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.dplib.analyse.*;
import org.dplib.compile.SourceCompiler;
import org.dplib.display.View;
import org.dplib.analyse.AnalysisException;
import org.dplib.compile.CompileException;
import org.dplib.io.IO;
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
    private final String sourceCode;
    private final SourceAnalyser sa = new SourceAnalyser();
    private ProblemType problemType;
    private List<Result> results;
    private final FileHandler fileHandler;

    public Solver(Model model, String sourceCode, FileHandler fileHandler)
    {
        this.model = model;
        this.sourceCode = sourceCode;
        this.fileHandler = fileHandler;
    }

    @Override
    public Analysis doInBackground()
    throws AnalysisException, IOException
    {

        System.out.println("Analysing...\n");

        try { sa.parse(sourceCode); } catch (ParseException e) { throw new AnalysisException(e); }

        MethodDeclaration methodToAnalyse = sa.locateMethod(model.getMethodToAnalyseName());
        problemType = sa.determineProblemType(methodToAnalyse);

        MethodDeclaration callingMethod = sa.locateMethod("main");
        String callingMethodBody = callingMethod.getBody().toString();

        checkMatchingMethodBodies(callingMethodBody,
                                  model.getCallingMethodBody(),
                                  callingMethod.getDeclarationAsString(),
                                  model.getCallingMethodDeclaration());

        File tempJavaFile = fileHandler.createTempJavaFile(sourceCode);
        File classFile;

        try { classFile = new SourceCompiler().compile(tempJavaFile, sa.getClassName()); }
        catch (CompileException e) { throw new AnalysisException(e); }
        results = new ClassAnalyser().analyse(classFile, fetchInputArray(model.getResults()));

        System.out.println("Analysis complete.\n");


        Analysis analysis = new Analysis();
        analysis.setProblemType(problemType);
        analysis.setResults(results);
        return analysis;
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

}