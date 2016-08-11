package org.dplib;

import com.github.javaparser.ParseException;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import org.dplib.enums.ProblemType;
import org.dplib.exception.AnalysisException;
import org.dplib.exception.ModelingException;

import javax.xml.transform.Source;
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
        SourceAnalyser sa;
        ClassAnalyser ca;
        MethodDeclaration main;
        MethodDeclaration methodToAnalyse;

        try
        {
            sa = new SourceAnalyser(sourceFile, methodName);
            sa.analyse();

            main = sa.findMethod("main");
            methodToAnalyse = sa.findMethod(methodName);

            ca = new ClassAnalyser(sourceFile, sa.getClassName());
            ca.analyse();
        }
        catch (AnalysisException | IOException e)
        {
            throw new ModelingException(e);
        }
        catch (ParseException e)
        {
            throw new ModelingException("\nCould not parse .java file: " + e.getMessage());
        }

        Model model = new Model(ProblemType.ITERATIVE);
        model.setType(sa.getProblemType());
        model.setCallingMethod(main);
        model.setMethodToAnalyse(methodToAnalyse);
        model.setOutput(ca.getOutput());
        model.setExecutionTime(ca.getExecutionTime());
        model.setInstructionCount(ca.getInstructionCount() + 100000); // margin of error - temporary

        return model;
    }

}
