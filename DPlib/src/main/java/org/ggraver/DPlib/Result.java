package org.ggraver.DPlib;

/**
 * Created by george on 02/08/16.
 */
class Result
{

    private String output;
    private long executionTime;
    private long instructionCount;

    Result(String output, long executionTime, long instructionCount)
    {
        this.output = output;
        this.executionTime = executionTime;
        this.instructionCount = instructionCount;
    }

    void setExecutionTime(long executionTime)
    {
        this.executionTime = executionTime;
    }

    public long getExecutionTime()
    {
        return executionTime;
    }

    public String getOutput()
    {
        return output;
    }

    public void setOutput(String output)
    {
        this.output = output;
    }

    public long getInstructionCount()
    {
        return instructionCount;
    }

    public void setInstructionCount(long instructionCount)
    {
        this.instructionCount = instructionCount;
    }

}
