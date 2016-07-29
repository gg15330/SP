package org.ggraver.DPlib;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.io.FilenameUtils;
import org.ggraver.DPlib.Exception.AnalysisException;
import org.ggraver.DPlib.Exception.ModelingException;

import java.io.*;

/**
 * Created by george on 22/07/16.
 */
// constructs a new Problem object which contains one method to analyse in the Solver
class Modeler
{

    private File sourceFile;
    private String methodName;

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

    //    remember to handle input parameterList
    Model model()
    throws ModelingException
    {
        Model model = new Model();
        try
        {
            SourceAnalyser sa = new SourceAnalyser(sourceFile, methodName);
            MethodDeclaration main = sa.findMethod("main");
            model.setMethodToAnalyse(new MethodDeclaration());
            model.setCallingMethod(main);

            ClassAnalyser ca = new ClassAnalyser(sourceFile);
            ca.analyse();
            model.setExpectedOutput(ca.getOutput());
            model.setExecutionTime(ca.getExecutionTime());
            model.setInstructionCount(ca.getInstructionCount() + 100000); // margin of error - temporary
            generateXML(model);

            //get Model object back from XML file
            XStream xStream = new XStream();
            File getBack = new File(sourceFile.getParentFile(), "model.xml");
            FileInputStream fis = new FileInputStream(getBack);
            Model newModel = new Model();
            xStream.fromXML(fis, newModel);
        }
        catch (FileNotFoundException | AnalysisException e)
        {
            throw new ModelingException(e);
        }
        return model;
    }

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

}
