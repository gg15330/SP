package org.dplib;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import org.dplib.enums.ProblemType;
import org.dplib.exception.AnalysisException;
import org.dplib.exception.ModelingException;

import java.io.*;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by george on 22/07/16.
 */
// constructs a new Model object which contains one method to contains in the Solver
class Modeler
{

    Model model(File sourceFile, String methodName)
    throws ModelingException
    {
        Model model = new Model(ProblemType.ITERATIVE);
        try
        {
            SourceAnalyser sa = new SourceAnalyser(sourceFile, methodName);
            sa.analyse();
            System.out.println("Contains array: " + sa.containsArray(sa.getMethodDeclaration()));
            System.out.println();
            if(model.getRequiredClasses().length > 0)
            {

            }

//            sa.containsVariable(sa.getMethodDeclaration(), Collection.class);
            System.out.println("Collection simple name: " + Collection.class.getSimpleName());
            System.out.println("Collection type name: " + Collection.class.getTypeName());
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
