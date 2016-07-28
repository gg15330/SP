package org.ggraver.DPlib;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Created by george on 28/07/16.
 */
class Result
{

    private MethodDeclaration method;
    private List<Object> inputs;
    private List<Object> outputs;
    private long executionTime;
    private long instructionCount;

    Result() {}

    Result(File XMLFile)
    {
        parseXML(XMLFile);
    }

//    interpret XML file into analysable data
    private void parseXML(File f) throws NullPointerException
    {
        if (f == null)
        {
            throw new NullPointerException("XML result file should not be null.");
        }
    }

    public void setMethod(MethodDeclaration method)
    {
        this.method = method;
    }

    public void setInputs(List<Object> inputs)
    {
        this.inputs = inputs;
    }

    public void setOutputs(List<Object> outputs)
    {
        this.outputs = outputs;
    }

    public void setExecutionTime(long executionTime)
    {
        this.executionTime = executionTime;
    }

    public void setInstructionCount(long instructionCount)
    {
        this.instructionCount = instructionCount;
    }

    public MethodDeclaration getMethod()
    {
        return method;
    }

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
