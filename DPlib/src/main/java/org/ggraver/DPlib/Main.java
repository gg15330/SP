package org.ggraver.DPlib;

import com.github.javaparser.ast.body.MethodDeclaration;
import org.ggraver.DPlib.Exception.AnalysisException;
import org.ggraver.DPlib.Exception.ModelingException;

import java.io.IOException;

// overall program control
public class Main
{

    public static void main(String[] args)
    {
        IO io = null;
        FileHandler fileHandler = null;
        try
        {
            io = new IO(args);
            fileHandler = new FileHandler(io.getFilePath());
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        switch (io.getCommand()) {
            case "model":
                try
                {
                    Model model = new Modeler().model(fileHandler.getSourceFile(), io.getMethodName());
                    fileHandler.generateXML(model);
                }
                catch (ModelingException e)
                {
                    io.fail(e);
                    System.exit(1);
                }
                break;
            case "solve":
                try
                {
//                     this part will eventually go in a solver/marker module
                    SourceAnalyser sa = new SourceAnalyser(fileHandler.getSourceFile(), io.getMethodName());
                    MethodDeclaration studentMain = sa.findMethod("main");
//                            if (!model.getCallingMethod().getBody().getStmts()
//                                      .equals(studentMain.getBody().getStmts()))
//                            {
//                                throw new Error("Main getMethods do not match.");
//                            }
                }
                catch (AnalysisException e)
                {
                    io.fail(e);
                }
                break;
            default: throw new Error("Invalid command: " + io.getCommand());
        }
    }

}
