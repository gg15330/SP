package org.DPlib;

import java.io.Serializable;

/**
 * Created by george on 12/08/16.
 */
public class Result
implements Serializable
{
    private String[] input;
    private String output;
    private long executionTime;

    public void setInput(String[] input)
    {
        this.input = input;
    }

    public void setOutput(String output)
    {
        this.output = output;
    }

    public String[] getInput()
    {
        return input;
    }

    public String getOutput()
    {
        return output;
    }

    public long getExecutionTime()
    {
        return executionTime;
    }

    public void setExecutionTime(long executionTime)
    {
        this.executionTime = executionTime;
    }
}
