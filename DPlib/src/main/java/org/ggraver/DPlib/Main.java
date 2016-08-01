package org.ggraver.DPlib;

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
                Modeler modeler = new Modeler(fileHandler.getSourceFile(), io.getMethodName());
                Model model = null;
                try
                {
                    model = modeler.model();
                }
                catch (ModelingException e)
                {
                    io.fail(e);
                    System.exit(1);
                }
                fileHandler.generateXML(model);
                break;
            case "solve": break;
            default: throw new Error("Invalid command: " + io.getCommand());
        }
    }

    private void solve()
    {
//        try
//        {
////             this part will eventually go in a solver/marker module
//            SourceAnalyser sa = new SourceAnalyser(studentFile, methodName);
//            MethodDeclaration studentMain = sa.findMethod("main");
//
//                    if (!model.getCallingMethod().getBody().getStmts()
//                              .equals(studentMain.getBody().getStmts()))
//                    {
//                        throw new Error("Main methods do not match.");
//                    }
//        }
//        catch (AnalysisException e)
//        {
//            io.fail(e);
//        }
    }

}
