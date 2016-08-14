package org.ggraver.DPlib;

import com.github.javaparser.ast.body.MethodDeclaration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by george on 28/07/16.
 */
public class Model
{

    private String description = "Problem description";
    private String methodToAnalyseDeclaration;
    private String callingMethodDeclaration;
    private String[] callingMethodBody;
    private String output;
    private List<Result2> results = new ArrayList<>();
    private long executionTime;

//    private long instructionCount;
//    private double EXECUTION_TIME_MARGIN = 1.2;
//    private double INSTRUCTION_COUNT_MARGIN = 1.2;

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public void setMethodToAnalyseDeclaration(String name) { this.methodToAnalyseDeclaration = name; }
    public String getMethodToAnalyseDeclaration() { return methodToAnalyseDeclaration; }

    public void setCallingMethodDeclaration(String callingMethodDeclaration) { this.callingMethodDeclaration = callingMethodDeclaration; }
    public String getCallingMethodDeclaration() { return callingMethodDeclaration; }

    public void setCallingMethodBody(String[] callingMethodBody) { this.callingMethodBody = callingMethodBody; }
    public String[] getCallingMethodBody() { return callingMethodBody; }

    public void setOutput(String output) { this.output = output; }
    public String getOutput() { return output; }

    public void setExecutionTime(long executionTime) { this.executionTime = executionTime; }

    public void addResult(Result2 result2) { results.add(result2); }
    public List<Result2> getResults() { return results; }

    public long getExecutionTime()
    {
        return executionTime;
    }



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
