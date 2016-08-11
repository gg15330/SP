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

    private SourceAnalyser sa;
    private ClassAnalyser ca;

    Model model(File sourceFile, String methodName)
    throws ModelingException
    {
        Model model = new Model();

        try
        {
            sa = new SourceAnalyser(sourceFile, methodName);
            sa.analyse();
            ca = new ClassAnalyser(sourceFile, sa.getClassName());
            ca.analyse();

            model.setCallingMethod(sa.findMethod("main"));
            model.setMethodToAnalyse(sa.findMethod(methodName));
        }
        catch (AnalysisException | IOException e)
        {
            throw new ModelingException(e);
        }
        catch (ParseException e)
        {
            throw new ModelingException("\nCould not parse .java file: " + e.getMessage());
        }

        model.setType(determineProblemType());
        System.out.println(determineProblemType());
        model.setOutput(ca.getOutput());
        model.setExecutionTime(ca.getExecutionTime());
        model.setInstructionCount(ca.getInstructionCount() + 100000); // margin of error - temporary

        return model;
    }

    private ProblemType determineProblemType() {
        if(sa.isRecursive())
        {
            if(sa.containsArray() || sa.containsRequiredClass())
            {
                return ProblemType.MEMOIZED;
            }
            else
            {
                return ProblemType.RECURSIVE;
            }
        }
        else if(!sa.isRecursive())
        {
            if(sa.containsArray() || sa.containsRequiredClass())
            {
                return ProblemType.ITERATIVE;
            }
        }
        return ProblemType.NONE;
    }

}
