package org.dplib;

import com.github.javaparser.ParseException;
import com.github.javaparser.TokenMgrException;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.dplib.exception.AnalysisException;
import org.dplib.exception.SolvingException;

import java.io.File;
import java.io.IOException;

/**
 * Created by george on 01/08/16.
 */
public class Solver
{

    public Result solve(Model model, File file)
    throws AnalysisException, SolvingException
    {
        SourceAnalyser sa;
        try
        {
            sa = new SourceAnalyser(file, model.getMethodToAnalyse().getName());
        }
        catch (IOException | ParseException e)
        {
            throw new SolvingException(e);
        }
        catch (TokenMgrException e)
        {
            throw new SolvingException(e.getMessage());
        }

        MethodDeclaration userCallingMethod = sa.findMethod("main");

        if(!userCallingMethod.equals(model.getCallingMethod()))
        {
            throw new AnalysisException("Submitted calling method \"" +
                                                userCallingMethod.getName() +
                                                "\" does not match modelled calling method \"" +
                                                model.getCallingMethod().getName() + "\".");
        }

        ClassAnalyser ca = new ClassAnalyser(file, sa.getClassName());
        ca.analyse();

        Result result = new Result();
        result.setOutput(model.getOutput(), ca.getOutput());
        result.setExecutionTime(model.getExecutionTime(),
                                ca.getExecutionTime(),
                                model.getEXECUTION_TIME_MARGIN());
        result.setInstructionCount(model.getInstructionCount(),
                                   ca.getInstructionCount(),
                                   model.getINSTRUCTION_COUNT_MARGIN());

        return result;
    }

}
