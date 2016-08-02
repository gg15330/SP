package org.ggraver.DPlib;

import org.ggraver.DPlib.Exception.AnalysisException;
import org.ggraver.DPlib.Exception.ModelingException;

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
        Main program;
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
                    Solver solver = new Solver();
                    Result result = solver.solve(modelToSolve, fileHandler.getFile());

                    boolean output = solver.pass(modelToSolve.getOutput(), result.getOutput());

                    boolean executionTime = solver.pass(modelToSolve.getExecutionTime(),
                                                        result.getExecutionTime(),
                                                        solver.getEXECUTION_TIME_MARGIN());

                    boolean instructionCount = solver.pass(modelToSolve.getInstructionCount(),
                                                           result.getInstructionCount(),
                                                           solver.getINSTRUCTION_COUNT_MARGIN());

                    io.displayResult(output, executionTime, instructionCount);
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

}
