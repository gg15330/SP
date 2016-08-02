package org.ggraver.DPlib;

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
            fileHandler = new FileHandler(io.getFilePath(), "java");
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        try
        {
            switch (io.getCommand()) {
                case "model":
                    Model model = new Modeler().model(fileHandler.getFile(), io.getMethodName());
                    fileHandler.generateXML(model);
                    break;
                case "solve":
                    Model modelToSolve = fileHandler.parseXML();
                    new Solver().solve(modelToSolve, fileHandler.getFile());
                    break;
                default: throw new Error("Invalid command: " + io.getCommand());
            }
        }
        catch (ModelingException | IOException e)
        {
            io.fail(e);
            System.exit(1);
        }
        catch (AnalysisException e)
        {
            e.printStackTrace();
        }
    }

}
