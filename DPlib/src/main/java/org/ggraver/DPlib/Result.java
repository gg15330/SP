package org.ggraver.DPlib;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;

import java.io.File;
import java.util.List;

/**
 * Created by george on 28/07/16.
 */
public class Result
{

    private List<Object> inputs;
    private List<Object> outputs;
    private long executionTime;
    private long instructionCount;

    private Result(File XMLFile)
    {
        if (XMLFile == null)
        {
            throw new NullPointerException("XML result file should not be null.");
        }
        parse(XMLFile);
    }

//    interpret XML file into analysable data
    private void parse(File f)
    {

    }

    public MethodDeclaration getMethod()
    {
        return method;
    }

    private MethodDeclaration method;

    public List getInputs()
    {
        return inputs;
    }

    public List getOutputs()
    {
        return outputs;
    }

    public long getExecutionTime()
    {
        return executionTime;
    }

    public long getInstructionCount()
    {
        return instructionCount;
    }

}
