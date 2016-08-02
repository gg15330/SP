package org.ggraver.DPlib;

import com.github.javaparser.ast.body.MethodDeclaration;
import org.ggraver.DPlib.Exception.AnalysisException;

import java.io.File;

/**
 * Created by george on 01/08/16.
 */
class Solver
{

    private Result result;

    void solve(Model model, File file)
    throws AnalysisException
    {

        SourceAnalyser sa = new SourceAnalyser(file, model.getMethodToAnalyse().getName());
        MethodDeclaration studentCallingMethod = sa.findMethod("main");
        MethodDeclaration studentMethodToAnalyse = sa.findMethod(model.getMethodToAnalyse().getName());

        if(!studentCallingMethod.equals(model.getCallingMethod()))
        {
            throw new AnalysisException("Submitted calling method \"" +
            studentMethodToAnalyse.getName() +
            "\" does not match modelled calling method \"" +
            model.getCallingMethod() + "\".");
        }

        ClassAnalyser ca = new ClassAnalyser(file);
        ca.analyse();

        System.out.println("Model Method: " + model.getMethodToAnalyse().getName());
        System.out.println("Model Calling method: " + model.getCallingMethod().getName());
        System.out.println("Model Output: " + model.getExpectedOutputs());
        System.out.println("Model Instructions: " + model.getInstructionCount());
        System.out.println("Model Time: " + model.getExecutionTime());
        System.out.println();

        System.out.println("Submitted Method: " + sa.findMethod(model.getMethodToAnalyse().getName()).getName());
        System.out.println("Submitted Calling method: " + sa.findMethod(model.getCallingMethod().getName()).getName());
        System.out.println("Submitted Output: " + ca.getOutput());
        System.out.println("Submitted Instructions: " + ca.getInstructionCount());
        System.out.println("Submitted Time: " + ca.getExecutionTime());

        if(!model.getExpectedOutputs().equals(ca.getOutput()))
        {
            String incorrectOutput = "Incorrect output: " +
            "\n\nExpected: " + model.getExpectedOutputs() +
            "\nActual: " + ca.getOutput() + "." +
            "\nPlease check your implementation of the Dynamic Programming algorithm.\n";
            System.out.println(incorrectOutput);
        }

//                            if (!model.getCallingMethod().getBody().getStmts()
//                                      .equals(studentMain.getBody().getStmts()))
//                            {
//                                throw new Error("Main getMethods do not match.");
//                            }

    }

    Result getResult()
    {
        return result;
    }

}
