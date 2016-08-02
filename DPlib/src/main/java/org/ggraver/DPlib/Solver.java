package org.ggraver.DPlib;

import com.github.javaparser.ast.body.MethodDeclaration;
import org.ggraver.DPlib.Exception.AnalysisException;
import org.ggraver.DPlib.Result.FailureResult;
import org.ggraver.DPlib.Result.PassResult;
import org.ggraver.DPlib.Result.Result;
import org.ggraver.DPlib.Result.ResultType;

import java.io.File;

/**
 * Created by george on 01/08/16.
 */
class Solver
{

    private double INSTRUCTION_COUNT_MARGIN = 1.2;
    private double EXECUTION_TIME_MARGIN = 1.2;

    Result solve(Model model, File file)
    throws AnalysisException
    {
        SourceAnalyser sa = new SourceAnalyser(file, model.getMethodToAnalyse().getName());
        MethodDeclaration studentCallingMethod = sa.findMethod("main");

        if(!studentCallingMethod.equals(model.getCallingMethod()))
        {
            throw new AnalysisException("Submitted calling method \"" +
            studentCallingMethod.getName() +
            "\" does not match modelled calling method \"" +
            model.getCallingMethod() + "\".");
        }

        ClassAnalyser ca = new ClassAnalyser(file);
        ca.analyse();

        if(!model.getOutput().equals(ca.getOutput()))
        {
            return new FailureResult(ResultType.OUTPUT_FAILURE,
                                            model.getOutput(),
                                            ca.getOutput());
        }
        else if(ca.getExecutionTime() > (Math.round(model.getExecutionTime() * EXECUTION_TIME_MARGIN)))
        {
            return new FailureResult(ResultType.EXECUTION_TIME_FAILURE,
                              String.valueOf(model.getExecutionTime()),
                              String.valueOf(ca.getExecutionTime()));
        }
        else if(ca.getInstructionCount() > (Math.round(model.getInstructionCount() * INSTRUCTION_COUNT_MARGIN)))
        {
            return new FailureResult(ResultType.INSTRUCTION_COUNT_FAILURE,
                              String.valueOf(model.getInstructionCount()),
                              String.valueOf(ca.getInstructionCount()));
        }
        else
        {
            return new PassResult();
        }
    }

    public void setINSTRUCTION_COUNT_MARGIN(int INSTRUCTION_COUNT_MARGIN)
    {
        this.INSTRUCTION_COUNT_MARGIN = INSTRUCTION_COUNT_MARGIN;
    }

    public void setEXECUTION_TIME_MARGIN(int EXECUTION_TIME_MARGIN)
    {
        this.EXECUTION_TIME_MARGIN = EXECUTION_TIME_MARGIN;
    }

}