package org.dplib;

import com.github.javaparser.ParseException;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.dplib.analyse.*;
import org.dplib.compile.CompileException;
import org.dplib.compile.SourceCompiler;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by george on 01/08/16.
 */

// analyses source code and class file on a SwingWorker thread
// then returns an Analysis with ProblemType and List<Result>
class Solver
extends SwingWorker<Analysis, Void>
{
    private final SourceAnalyser sa = new SourceAnalyser();
    private final FileHandler fileHandler;
    private final Model model;
    private final String sourceCode;
    private final File dir;

    Solver(Model model, String sourceCode, FileHandler fileHandler, File dir)
    {
        this.model = model;
        this.sourceCode = sourceCode;
        this.fileHandler = fileHandler;
        this.dir = dir;
    }

    @Override
    public Analysis doInBackground()
    throws AnalysisException
    {
        try { sa.parse(sourceCode); }
        catch (ParseException e) { throw new AnalysisException(e); }

        checkValidSource();

        Analysis analysis = new Analysis();
        MethodDeclaration methodToAnalyse = sa.locateMethod(model.getMethodToAnalyseName());

        analysis.setProblemType(sa.determineProblemType(methodToAnalyse));

        File classFile;
        try {
            File tempJavaFile = fileHandler.createTempJavaFile(dir, sourceCode);
            classFile = new SourceCompiler().compile(tempJavaFile, sa.getClassName());
        }
        catch (IOException | CompileException e) { throw new AnalysisException(e); }

        String[][] inputs = fetchInputArray(model.getResults());
        List<Result> results = new ClassAnalyser().analyse(classFile, inputs);

        analysis.setResults(results);

        return analysis;
    }

    private void checkValidSource()
    throws AnalysisException
    {
        MethodDeclaration callingMethod = sa.locateMethod("main");

        String callingMethodBody = callingMethod.getBody().toString();
        if(!checkMatchingMethodBodies(callingMethodBody, model.getCallingMethodBody()))
        {
            throw new AnalysisException("Submitted calling method \"" +
                                                callingMethod.getDeclarationAsString() +
                                                "\" does not match modelled calling method \"" +
                                                model.getCallingMethodDeclaration() + "\".");
        }
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

    private boolean checkMatchingMethodBodies(String userCallingMethod,
                                           String tutorCallingMethod)
    {
        return userCallingMethod.equals(tutorCallingMethod);
    }

}