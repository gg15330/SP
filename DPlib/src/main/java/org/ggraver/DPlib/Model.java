package org.ggraver.DPlib;

import com.github.javaparser.ast.body.MethodDeclaration;

/**
 * Created by george on 28/07/16.
 */
public class Model
{

    private MethodDeclaration methodToAnalyse;
    private MethodDeclaration callingMethod;
    private String output;
    private long executionTime;
    private long instructionCount;
    private double INSTRUCTION_COUNT_MARGIN = 1.2;
    private double EXECUTION_TIME_MARGIN = 1.2;

    double getINSTRUCTION_COUNT_MARGIN()
    {
        return INSTRUCTION_COUNT_MARGIN;
    }

    public void setINSTRUCTION_COUNT_MARGIN(int INSTRUCTION_COUNT_MARGIN)
    {
        this.INSTRUCTION_COUNT_MARGIN = INSTRUCTION_COUNT_MARGIN;
    }

    double getEXECUTION_TIME_MARGIN()
    {
        return EXECUTION_TIME_MARGIN;
    }

    public void setEXECUTION_TIME_MARGIN(int EXECUTION_TIME_MARGIN)
    {
        this.EXECUTION_TIME_MARGIN = EXECUTION_TIME_MARGIN;
    }

    MethodDeclaration getMethodToAnalyse()
    {
        return methodToAnalyse;
    }

    void setMethodToAnalyse(MethodDeclaration methodToAnalyse)
    {
        this.methodToAnalyse = methodToAnalyse;
    }

    String getOutput()
    {
        return output;
    }

    void setOutput(String output)
    {
        this.output = output;
    }

    long getExecutionTime()
    {
        return executionTime;
    }

    void setExecutionTime(long executionTime)
    {
        this.executionTime = executionTime;
    }

    long getInstructionCount()
    {
        return instructionCount;
    }

    void setInstructionCount(long instructionCount)
    {
        this.instructionCount = instructionCount;
    }

    MethodDeclaration getCallingMethod()
    {
        return callingMethod;
    }

    void setCallingMethod(MethodDeclaration callingMethod)
    {
        this.callingMethod = callingMethod;
    }
}
