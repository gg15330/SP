package org.ggraver.DPlib;

import com.github.javaparser.ast.body.MethodDeclaration;
import org.ggraver.DPlib.Exception.AnalysisException;

import java.io.File;

/**
 * Created by george on 01/08/16.
 */
public class Solver
{

    void solve(Model model, File file)
    throws AnalysisException
    {
        System.out.println("Method: " + model.getMethodToAnalyse());
        System.out.println("Calling method: " + model.getCallingMethod());
        System.out.println("Outputs: " + model.getExpectedOutputs());
        System.out.println("Instructions: " + model.getInstructionCount());
        System.out.println("Time: " + model.getInstructionCount());

        SourceAnalyser sa = new SourceAnalyser(file, model.getMethodToAnalyse().getName());
        MethodDeclaration studentMain = sa.findMethod("main");


//                            if (!model.getCallingMethod().getBody().getStmts()
//                                      .equals(studentMain.getBody().getStmts()))
//                            {
//                                throw new Error("Main getMethods do not match.");
//                            }

    }

}
