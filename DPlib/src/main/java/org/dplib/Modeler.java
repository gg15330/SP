package org.dplib;

import com.github.javaparser.ParseException;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.dplib.analyse.*;
import org.dplib.compile.CompileException;
import org.dplib.compile.SourceCompiler;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by george on 22/07/16.
 */

// constructs a new Model object based on analysis of source code and class file
class Modeler
{

    public Model model(File sourceFile,
                       File inputFile,
                       String methodName,
                       FileHandler fileHandler)
    throws ModelingException
    {
        MethodDeclaration methodToAnalyse;
        MethodDeclaration callingMethod;
        SourceAnalyser sa = new SourceAnalyser();
        List<Result> results;
        try
        {
            sa.parse(sourceFile);

            methodToAnalyse = sa.locateMethod(methodName);
            callingMethod = sa.locateMethod("main");

            File classFile = new SourceCompiler().compile(sourceFile, sa.getClassName());
            String[][] inputs = fileHandler.parseInputTextFile(inputFile);
            results = new ClassAnalyser().analyse(classFile, inputs);
        }
        catch (IOException | ParseException | CompileException | AnalysisException e)
        {
            throw new ModelingException(e);
        }

        Model model = new Model();
        model.setClassName(sa.getClassName());
        model.setMethodToAnalyseName(methodToAnalyse.getName());
        model.setMethodToAnalyseDeclaration(methodToAnalyse.getDeclarationAsString());
        model.setCallingMethodDeclaration(callingMethod.getDeclarationAsString());
        model.setCallingMethodBody(callingMethod.getBody().toString());
        model.setProblemType(sa.determineProblemType(methodToAnalyse));
        model.setResults(results);
        return model;
    }

}
