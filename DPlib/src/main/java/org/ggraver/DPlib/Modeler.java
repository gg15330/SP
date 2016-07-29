package org.ggraver.DPlib;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.ggraver.DPlib.Exception.AnalysisException;
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
import javax.xml.ws.Service;
import java.io.*;
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
    private Model model;
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
        try
        {
            SourceAnalyser sa = new SourceAnalyser(sourceFile, methodName);
            MethodDeclaration main = sa.findMethod("main");
            String body = main.toString();
//            System.out.println("Main body:\n" + body);
            String comparison = "public static void main(String[] args) {\n" +
                    "    int n = 45;\n" +
                    "    System.out.println(fibDP(n));\n" +
                    "}";
//            System.out.println("Comparison:\n" + comparison);
            if (!body.equals(comparison))
            {
                throw new ModelingException("Main body does not match.");
            }
        }
        catch (AnalysisException e)
        {
            e.printStackTrace();
        }

        model = new Model();
        model.setMethod(new MethodDeclaration());
        model.setInputs(new ArrayList<Object>());
        model.setOutputs(new ArrayList<Object>());
        model.setExecutionTime(100);
        model.setInstructionCount(5000);
        generateXML(model);

        try
        {
            XStream xStream = new XStream();
            File getBack = new File(sourceFile.getParentFile(), "model.xml");
            FileInputStream fis = new FileInputStream(getBack);
            Model getModel = new Model();
            xStream.fromXML(fis, getModel);
            System.out.println("Model:\n" + getModel.getInstructionCount() +
                                       "\n" + getModel.getExecutionTime());
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    //    template sourced from http://www.mkyong.com/java/how-to-create-xml-file-in-java-dom/
    private void generateXML(Model m)
    {
        File XML = new File(sourceFile.getParentFile(), "model.xml");
        XStream xstream = new XStream();
        FileOutputStream fos;
        try
        {
            fos = new FileOutputStream(XML);
            xstream.toXML(m, fos);
            fos.close();
        }
        catch (IOException e)
        {
            throw new Error("Could not serialize Model object to XML.");
        }
        if (!XML.exists())
        {
            throw new Error("XML file does not exist.");
        }
        System.out.println("Model file generated");
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
