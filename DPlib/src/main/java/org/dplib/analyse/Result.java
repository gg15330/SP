package org.dplib.analyse;

import java.io.Serializable;

/**
 * Created by george on 12/08/16.
 */

// individual result of program run
public class Result
implements Serializable
{
    private String[] input;
    private String output;
    private long executionTime;

    public Result(String[] input, String output, long executionTime)
    {
        this.input = input;
        this.output = output;
        this.executionTime = executionTime;
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

}
