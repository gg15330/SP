package org.ggraver.DPlib;

import java.util.AbstractMap;
import java.util.prefs.PreferenceChangeEvent;

/**
 * Created by george on 02/08/16.
 */
public class Result
{
    private AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<String, String>, Boolean> output;
    private AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<Long, Long>, Boolean> executionTime;
    private AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<Long, Long>, Boolean> instructionCount;

    private boolean pass(String expected, String actual)
    {
        return expected.equals(actual);
    }

    private boolean pass(long expected, long actual, double margin)
    {
        return actual < (Math.round(expected * margin));
    }

    void setOutput(String expected, String actual)
    {
        AbstractMap.SimpleEntry<String, String> outputs = new AbstractMap.SimpleEntry<>(expected, actual);
        output = new AbstractMap.SimpleEntry<>(outputs, pass(expected, actual));
    }

    void setExecutionTime(long expected, long actual, double margin)
    {
        AbstractMap.SimpleEntry<Long, Long> times = new AbstractMap.SimpleEntry<>(expected, actual);
        executionTime = new AbstractMap.SimpleEntry<>(times, pass(expected, actual, margin));
    }

    void setInstructionCount(long expected, long actual, double margin)
    {
        AbstractMap.SimpleEntry<Long, Long> instructions = new AbstractMap.SimpleEntry<>(expected, actual);
        instructionCount = new AbstractMap.SimpleEntry<>(instructions, pass(expected, actual, margin));
    }

    String getModelOutput()
    {
        return output.getKey().getKey();
    }

    String getUserOutput()
    {
        return output.getKey().getValue();
    }

    boolean getOutputPass()
    {
        return output.getValue();
    }

    public long getModelExecutionTime()
    {
        return executionTime.getKey().getKey();
    }

    public long getUserExecutionTime()
    {
        return executionTime.getKey().getValue();
    }

    boolean getExecutionTimePass()
    {
        return executionTime.getValue();
    }

    public long getModelInstructionCount()
    {
        return instructionCount.getKey().getKey();
    }

    public long getUserInstructionCount()
    {
        return instructionCount.getKey().getValue();
    }

    boolean getInstructionCountPass()
    {
        return instructionCount.getValue();
    }
}

