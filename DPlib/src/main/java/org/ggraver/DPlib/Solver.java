package org.ggraver.DPlib;

import com.github.javaparser.ast.body.MethodDeclaration;
import org.ggraver.DPlib.Exception.AnalysisException;

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

        return new Result(ca.getOutput(),
                ca.getExecutionTime(),
                ca.getInstructionCount());
    }

//    pass test for program output
    boolean pass(String expected, String actual)
    {
        return expected.equals(actual);
    }

//    pass test for execution time/instruction count
    boolean pass(long expected, long actual, double margin)
    {
        return actual < Math.round(expected * margin);
    }

    public void setINSTRUCTION_COUNT_MARGIN(int INSTRUCTION_COUNT_MARGIN)
    {
        this.INSTRUCTION_COUNT_MARGIN = INSTRUCTION_COUNT_MARGIN;
    }

    public void setEXECUTION_TIME_MARGIN(int EXECUTION_TIME_MARGIN)
    {
        this.EXECUTION_TIME_MARGIN = EXECUTION_TIME_MARGIN;
    }

    public double getINSTRUCTION_COUNT_MARGIN()
    {
        return INSTRUCTION_COUNT_MARGIN;
    }

    public double getEXECUTION_TIME_MARGIN()
    {
        return EXECUTION_TIME_MARGIN;
    }
}