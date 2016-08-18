package org.dplib.analyse;

import org.dplib.analyse.Analysis;

import java.io.Serializable;

/**
 * Created by george on 28/07/16.
 */
public class Model
extends Analysis
implements Serializable
{

    private String description = "Problem description";
    private String className;
    private String methodToAnalyseName;
    private String methodToAnalyseDeclaration;
    private String callingMethodDeclaration;
    private String callingMethodBody;

//    private long instructionCount;
//    private double EXECUTION_TIME_MARGIN = 1.2;
//    private double INSTRUCTION_COUNT_MARGIN = 1.2;

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getMethodToAnalyseName() { return methodToAnalyseName; }
    public void setMethodToAnalyseName(String methodToAnalyseName) { this.methodToAnalyseName = methodToAnalyseName; }

    public String getMethodToAnalyseDeclaration() { return methodToAnalyseDeclaration; }
    public void setMethodToAnalyseDeclaration(String name) { this.methodToAnalyseDeclaration = name; }

    public String getCallingMethodDeclaration() { return callingMethodDeclaration; }
    public void setCallingMethodDeclaration(String callingMethodDeclaration) { this.callingMethodDeclaration = callingMethodDeclaration; }

    public String getCallingMethodBody() { return callingMethodBody; }
    public void setCallingMethodBody(String callingMethodBody) { this.callingMethodBody = callingMethodBody; }

//    void setInstructionCount(long instructionCount)
//    {
//        this.instructionCount = instructionCount;
//    }
//
//    long getInstructionCount()
//    {
//        return instructionCount;
//    }
//
//    public void setINSTRUCTION_COUNT_MARGIN(int INSTRUCTION_COUNT_MARGIN)
//    {
//        this.INSTRUCTION_COUNT_MARGIN = INSTRUCTION_COUNT_MARGIN;
//    }
//
//    public void setEXECUTION_TIME_MARGIN(int EXECUTION_TIME_MARGIN)
//    {
//        this.EXECUTION_TIME_MARGIN = EXECUTION_TIME_MARGIN;
//    }
//
//    double getINSTRUCTION_COUNT_MARGIN()
//    {
//        return INSTRUCTION_COUNT_MARGIN;
//    }
//
//    double getEXECUTION_TIME_MARGIN()
//    {
//        return EXECUTION_TIME_MARGIN;
//    }

}
