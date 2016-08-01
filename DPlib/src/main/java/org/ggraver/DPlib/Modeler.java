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
    {
        this.methodName = methodName;
        this.sourceFile = sourceFile;
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
            File modelFile = new File(sourceFile.getParentFile(), "model.xml");
            FileInputStream fis = new FileInputStream(modelFile);
            Model newModel = (Model) xStream.fromXML(fis);
        }
        catch (FileNotFoundException | AnalysisException e)
        {
            throw new ModelingException(e);
        }
        return model;
    }


}
