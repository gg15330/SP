package org.ggraver.DPlib;

import com.github.javaparser.ast.body.MethodDeclaration;

import java.util.List;

/**
 * Created by george on 28/07/16.
 */
class Model
{

    private MethodDeclaration methodToAnalyse;
    private MethodDeclaration callingMethod;
    private String expectedOutput;
    private long executionTime;
    private long instructionCount;

    Model() {}

    public void setMethodToAnalyse(MethodDeclaration methodToAnalyse)
    {
        this.methodToAnalyse = methodToAnalyse;
    }

    public void setExpectedOutput(String expectedOutputs)
    {
        this.expectedOutput = expectedOutputs;
    }

    public void setExecutionTime(long executionTime)
    {
        this.executionTime = executionTime;
    }

    public void setInstructionCount(long instructionCount)
    {
        this.instructionCount = instructionCount;
    }

    public MethodDeclaration getMethodToAnalyse()
    {
        return methodToAnalyse;
    }

    public String getExpectedOutputs()
    {
        return expectedOutput;
    }

    public long getExecutionTime()
    {
        return executionTime;
    }

    public long getInstructionCount()
    {
        return instructionCount;
    }

    public MethodDeclaration getCallingMethod()
    {
        return callingMethod;
    }

    public void setCallingMethod(MethodDeclaration callingMethod)
    {
        this.callingMethod = callingMethod;
    }
}
