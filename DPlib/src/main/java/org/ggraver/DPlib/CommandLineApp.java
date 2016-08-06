package org.ggraver.DPlib;

import org.ggraver.DPlib.Exception.AnalysisException;
import org.ggraver.DPlib.Exception.ModelingException;

import java.io.IOException;

/**
 * Created by george on 05/08/16.
 */
public class CommandLineApp
{

    private Result result;

    void start(String[] args)
    throws IOException, ModelingException, AnalysisException
    {
        IO io = new IO();
        io.processArgs(args);
        FileHandler fileHandler = new FileHandler(io.getFilePath(), "java");

        switch (io.getCommand()) {
            case "model":
                Model model = new Modeler().model(fileHandler.getFile(), io.getMethodName());
                fileHandler.generateXML(model);
                break;
            case "solve":
                model = fileHandler.parseXML();
                Solver solver = new Solver();
                result = solver.solve(model, fileHandler.getFile());
                io.displayResult(result);
                break;
            default: throw new Error("Invalid command: " + io.getCommand());
        }
    }

    public Result getResult()
    {
        return result;
    }

}
