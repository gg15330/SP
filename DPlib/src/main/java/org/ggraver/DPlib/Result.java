package org.ggraver.DPlib;

import java.lang.reflect.AccessibleObject;
import java.util.AbstractMap;
import java.util.Hashtable;

/**
 * Created by george on 02/08/16.
 */
class Result
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

    AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<Long,Long>, Boolean> getExecutionTime()
    {
        return executionTime;
    }

    AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<String,String>, Boolean> getOutput()
    {
        return output;
    }

    AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<Long,Long>, Boolean> getInstructionCount() { return instructionCount; }

}
