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

    Model() {}

    void setMethodToAnalyse(MethodDeclaration methodToAnalyse)
    {
        this.methodToAnalyse = methodToAnalyse;
    }

    void setOutput(String output)
    {
        this.output = output;
    }

    public void setINSTRUCTION_COUNT_MARGIN(int INSTRUCTION_COUNT_MARGIN)
    {
        this.INSTRUCTION_COUNT_MARGIN = INSTRUCTION_COUNT_MARGIN;
    }

    public void setEXECUTION_TIME_MARGIN(int EXECUTION_TIME_MARGIN)
    {
        this.EXECUTION_TIME_MARGIN = EXECUTION_TIME_MARGIN;
    }

    double getINSTRUCTION_COUNT_MARGIN()
    {
        return INSTRUCTION_COUNT_MARGIN;
    }

    double getEXECUTION_TIME_MARGIN()
    {
        return EXECUTION_TIME_MARGIN;
    }

    void setExecutionTime(long executionTime)
    {
        this.executionTime = executionTime;
    }

    void setInstructionCount(long instructionCount)
    {
        this.instructionCount = instructionCount;
    }

    MethodDeclaration getMethodToAnalyse()
    {
        return methodToAnalyse;
    }

    String getOutput()
    {
        return output;
    }

    long getExecutionTime()
    {
        return executionTime;
    }

    long getInstructionCount()
    {
        return instructionCount;
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
