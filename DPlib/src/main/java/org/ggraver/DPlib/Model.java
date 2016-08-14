package org.ggraver.DPlib;

import com.github.javaparser.ast.body.MethodDeclaration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by george on 28/07/16.
 */
public class Model
implements Serializable
{

    private String description = "Problem description";
    private String className;
    private String methodToAnalyseDeclaration;
    private String callingMethodDeclaration;
    private String[] callingMethodBody;
    private List<Result2> results = new ArrayList<>();

//    private long instructionCount;
//    private double EXECUTION_TIME_MARGIN = 1.2;
//    private double INSTRUCTION_COUNT_MARGIN = 1.2;

    public String getDescription() { return description; }
    public void addResult(Result2 result2) { results.add(result2); }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public void setDescription(String description) { this.description = description; }
    public void setMethodToAnalyseDeclaration(String name) { this.methodToAnalyseDeclaration = name; }

    public String getMethodToAnalyseDeclaration() { return methodToAnalyseDeclaration; }
    public void setCallingMethodDeclaration(String callingMethodDeclaration) { this.callingMethodDeclaration = callingMethodDeclaration; }

    public String getCallingMethodDeclaration() { return callingMethodDeclaration; }
    public void setCallingMethodBody(String[] callingMethodBody) { this.callingMethodBody = callingMethodBody; }

    public String[] getCallingMethodBody() { return callingMethodBody; }

    public List<Result2> getResults() { return results; }


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
