package org.ggraver.DPlib;

import com.github.javaparser.ParseException;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import org.apache.commons.io.FilenameUtils;
import org.ggraver.DPlib.Exception.AnalysisException;
import org.ggraver.DPlib.Exception.CompileException;
import org.ggraver.DPlib.Exception.ModelingException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by george on 22/07/16.
 */
// constructs a new Problem object which contains one method to analyse in the Solver
class Modeler
{

    private File sourceFile;
    private String methodName;
    private Result result;
    private List<Parameter> inputs = new ArrayList<Parameter>();
    private List<Parameter> outputs = new ArrayList<Parameter>();

    Modeler(File sourceFile, String methodName)
    throws IOException
    {

        this.methodName = methodName;
        this.sourceFile = sourceFile;

        if (!sourceFile.exists()
                || sourceFile.isDirectory()
                || !FilenameUtils.getExtension(sourceFile.getPath()).equals("java"))
        {
            throw new IOException("Could not model input file: " + sourceFile.getPath());
        }

    }

    void model(File sourceFile)
    {
    }

    //    remember to handle input parameterList
    void model()
    throws ModelingException
    {
        result = new Result();
        result.setMethod(new MethodDeclaration());
        result.setInputs(new ArrayList<Object>());
        result.setOutputs(new ArrayList<Object>());
        result.setExecutionTime(100);
        result.setInstructionCount(5000);

        try
        {
            generateXML();
        }
        catch (ParserConfigurationException | TransformerException e)
        {
            throw new ModelingException(e);
        }
    }

    //    template sourced from http://www.mkyong.com/java/how-to-create-xml-file-in-java-dom/
    private void generateXML()
    throws ParserConfigurationException, TransformerException
    {
        File XML = new File(sourceFile.getParentFile(), "result.xml");

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();

        Document document = db.newDocument();
        Element root = document.createElement("root");
        document.appendChild(root);

        Element input = document.createElement("input");
        root.appendChild(input);
        for (Object o : inputs) { addInputToXML(o, input); }

        Element output = document.createElement("output");
        root.appendChild(output);
        for (Object o : outputs) { addOutputToXML(o, output); }

        Element performance = document.createElement("performance");
        root.appendChild(performance);

        Element instructions = document.createElement("instructions");
        instructions.appendChild(document.createTextNode(String.valueOf(result.getInstructionCount())));
        performance.appendChild(instructions);

        Element time = document.createElement("time");
        time.appendChild(document.createTextNode(String.valueOf(result.getExecutionTime())));
        performance.appendChild(time);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        DOMSource source = new DOMSource(document);
        StreamResult streamResult = new StreamResult(XML);
        transformer.transform(source, streamResult);

        if (!XML.exists())
        {
            throw new Error("XML file does not exist.");
        }
    }

    private void addInputToXML(Object o, Element input)
    {

    }

    private void addOutputToXML(Object o, Element output)
    {

    }

    void setMethod(String methodName)
    {

    }

    void setInputs(String... inputs)
    {

    }

    void methodToAnalyse(String s)
    {

    }

    void methodParameters()
    {

    }

}
