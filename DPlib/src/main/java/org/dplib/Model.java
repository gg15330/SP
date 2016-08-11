package org.dplib;

import com.github.javaparser.ast.body.MethodDeclaration;
import org.dplib.enums.ProblemType;

import java.util.List;

/**
 * Created by george on 28/07/16.
 */
public class Model
{

    private ProblemType type;
    private MethodDeclaration methodToAnalyse;
    private MethodDeclaration callingMethod;
    private boolean recursionAllowed;
    private String[] requiredClasses;
    private String description = "Problem description";
    private String output;
    private long executionTime;
    private long instructionCount;
    private double INSTRUCTION_COUNT_MARGIN = 1.2;
    private double EXECUTION_TIME_MARGIN = 1.2;

    Model(ProblemType type, String... requiredClasses)
    {
        this.type = type;
        switch (type)
        {
            case RECURSIVE:
                this.requiredClasses = null;
                recursionAllowed = true;
                break;
            case MEMOIZED:
                this.requiredClasses = requiredClasses;
                recursionAllowed = true;
                break;
            case ITERATIVE:
                this.requiredClasses = requiredClasses;
                recursionAllowed = false;
                break;
            default: break;
        }
    }

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

    public String getDescription()
    {
        return description;
    }

    public ProblemType getType()
    {
        return type;
    }

    public void setType(ProblemType type)
    {
        this.type = type;
    }

    public String[] getRequiredClasses()
    {
        return requiredClasses;
    }
}
