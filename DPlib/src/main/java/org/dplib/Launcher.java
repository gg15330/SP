package org.dplib;

import org.dplib.display.Controller;
import org.dplib.exception.AnalysisException;
import org.dplib.exception.ModelingException;

import java.io.IOException;

import static javafx.application.Application.launch;

// overall program control
public class Launcher
{

    private String command;
    private String javaFilePath;
    private String inputFilePath;
    private String modelFilePath;
    private String methodName;
    private final IO io = new IO();

    public static void main(String[] args)
    {
        Launcher launcher = new Launcher();
        launcher.processArgs(args);
        launcher.start();
    }

    private void start()
    {
        try
        {
            switch (command) {
                case "model":
                    FileHandler fileHandler = new FileHandler(javaFilePath, "java");
                    FileHandler inputFileHandler = new FileHandler(inputFilePath, "txt");
                    String[][] inputs = inputFileHandler.parseInputTextFile(inputFileHandler.getFile());
                    Model model = new Modeler().model(fileHandler.getFile(), methodName, inputs);
                    io.displayResults(model.getResults());
                    System.out.println("Creating model file...");
                    fileHandler.serializeModel(model);
                    System.out.println("Model file created.");
                    break;
                case "solve":
                    new Controller(modelFilePath).start();
                    break;
                default: throw new Error("Invalid command: " + command);
            }
        }
        catch (IOException | ModelingException | AnalysisException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void processArgs(String[] args)
    {
        if(args.length == 4)
        {
            command = args[0];
            javaFilePath = args[1];
            inputFilePath = args[2];
            methodName = args[3];
            if(!command.equals("model"))
            {
                io.usage();
                System.exit(1);
            }
        }
        else if(args.length == 1)
        {
            command = "solve";
            modelFilePath = args[0];
        }
        else
        {
            io.usage();
            System.exit(1);
        }
    }

}
