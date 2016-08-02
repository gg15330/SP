package org.ggraver.DPlib;

import org.ggraver.DPlib.Exception.AnalysisException;
import org.ggraver.DPlib.Exception.ModelingException;
import org.ggraver.DPlib.Result.Result;

import java.io.IOException;

// overall program control
public class Main
{

    private IO io;
    private FileHandler fileHandler;

    private Main(String[] args)
    throws IOException
    {
        io = new IO(args);
        fileHandler = new FileHandler(io.getFilePath(), "java");
    }

    public static void main(String[] args)
    {
        Main program = null;
        try
        {
            program = new Main(args);
            program.run();
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    private void run()
    {
        try
        {
            switch (io.getCommand()) {
                case "model":
                    Model model = new Modeler().model(fileHandler.getFile(), io.getMethodName());
                    fileHandler.generateXML(model);
                    break;
                case "solve":
                    Model modelToSolve = fileHandler.parseXML();
                    Result result = new Solver().solve(modelToSolve, fileHandler.getFile());
                    processResult(result);
                    break;
                default: throw new Error("Invalid command: " + io.getCommand());
            }
        }
        catch (ModelingException | IOException e)
        {
            io.exit(e);
            System.exit(1);
        }
        catch (AnalysisException e)
        {
            io.exit(e);
        }
    }

    private void processResult(Result result)
    {
        switch (result.getResultType())
        {
            case OUTPUT_FAILURE:
                io.fail();
                break;
            case EXECUTION_TIME_FAILURE:
                io.fail();
                break;
            case INSTRUCTION_COUNT_FAILURE:
                io.fail();
                break;
            case PASS:
                io.success();
                break;
            default:
                throw new Error("Could not interpret Result type.");
        }
    }

}
