package org.ggraver.DPlib;

import com.github.javaparser.ast.body.MethodDeclaration;
import org.ggraver.DPlib.Exception.AnalysisException;

import java.io.File;

/**
 * Created by george on 01/08/16.
 */
public class Solver
{

    public Result solve(Model model, File file)
    throws AnalysisException
    {
        SourceAnalyser sa = new SourceAnalyser(file, model.getMethodToAnalyse().getName());
        MethodDeclaration userCallingMethod = sa.findMethod("main");

        if(!userCallingMethod.equals(model.getCallingMethod()))
        {
            throw new AnalysisException("Submitted calling method \"" +
                                                userCallingMethod.getName() +
                                                "\" does not match modelled calling method \"" +
                                                model.getCallingMethod().getName() + "\".");
        }

        ClassAnalyser ca = new ClassAnalyser(file, sa.getClassName());
//        ca.analyse(input);

        Result result = new Result();
        result.setOutput(model.getOutput(), ca.getOutput());
//        result.setExecutionTime(model.getExecutionTime(),
//                                ca.getExecutionTime(),
//                                model.getEXECUTION_TIME_MARGIN());
//        result.setInstructionCount(model.getInstructionCount(),
//                                   ca.getInstructionCount(),
//                                   model.getINSTRUCTION_COUNT_MARGIN());

        return result;
    }

}
