package org.dplib;

import com.github.javaparser.ast.body.MethodDeclaration;
import org.dplib.exception.AnalysisException;
import org.dplib.exception.ModelingException;

import java.io.*;

/**
 * Created by george on 22/07/16.
 */
// constructs a new Model object which contains one method to analyse in the Solver
class Modeler
{

    Model model(File sourceFile, String methodName)
    throws ModelingException
    {
        Model model = new Model();
        try
        {
            SourceAnalyser sa = new SourceAnalyser(sourceFile, methodName);
            sa.analyse(sa.getCompilationUnit());
            MethodDeclaration main = sa.findMethod("main");
            MethodDeclaration methodToAnalyse = sa.findMethod(methodName);
            model.setMethodToAnalyse(methodToAnalyse);
            model.setCallingMethod(main);
            model.setType(sa.getProblemType());

            ClassAnalyser ca = new ClassAnalyser(sourceFile, sa.getClassName());
            ca.analyse();
            model.setOutput(ca.getOutput());
            model.setExecutionTime(ca.getExecutionTime());
            model.setInstructionCount(ca.getInstructionCount() + 100000); // margin of error - temporary
        }
        catch (AnalysisException e)
        {
            throw new ModelingException(e);
        }
        return model;
    }

}
