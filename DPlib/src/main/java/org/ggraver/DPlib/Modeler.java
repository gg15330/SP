package org.ggraver.DPlib;

import com.github.javaparser.ast.body.MethodDeclaration;
import org.ggraver.DPlib.Exception.AnalysisException;
import org.ggraver.DPlib.Exception.ModelingException;

import java.io.*;

/**
 * Created by george on 22/07/16.
 */
// constructs a new Model object which contains one method to analyse in the Solver
class Modeler
{

    Model model(File sourceFile, String methodName, String[] inputs)
    throws ModelingException
    {
        Model model = new Model();
        try
        {
            SourceAnalyser sa = new SourceAnalyser(sourceFile, methodName);
            MethodDeclaration main = sa.findMethod("main");
            MethodDeclaration methodToAnalyse = sa.findMethod(methodName);
            model.setMethodToAnalyse(methodToAnalyse);
            model.setCallingMethod(main);

            ClassAnalyser ca = new ClassAnalyser(sourceFile, sa.getClassName());
            for(String input : inputs)
            {
                Result2 result2 = ca.analyse(input);
                model.addResult(result2);
            }
            model.setOutput(ca.getOutput());
            model.setExecutionTime(ca.getExecutionTime());
//            model.setInstructionCount(ca.getInstructionCount()); // margin of error - temporary
        }
        catch (AnalysisException e)
        {
            throw new ModelingException(e);
        }
        return model;
    }

}
