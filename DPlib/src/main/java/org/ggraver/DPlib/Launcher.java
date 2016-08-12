package org.ggraver.DPlib;

import org.ggraver.DPlib.Display.Controller;
import org.ggraver.DPlib.Exception.AnalysisException;
import org.ggraver.DPlib.Exception.ModelingException;

import java.io.File;
import java.io.IOException;

import static javafx.application.Application.launch;

// overall program control
public class Launcher
{

    private String command;
    private String javaFilePath;
    private String methodName;
    private String inputFilePath;

    public static void main(String[] args)
    {
//        if (System.getProperty("java.runtime.name").equals("Java(TM) SE Runtime Environment"))
//        {
//            launch(FXView.class, args);
//        }
//        else
//        {
        Launcher launcher = new Launcher();
        launcher.processArgs(args);
        launcher.start();
        //        }
    }

    private void start()
    {
        try
        {
            Controller controller = new Controller(javaFilePath);
            switch (command) {
                case "model":
                    FileHandler fileHandler = new FileHandler(javaFilePath, "java");
                    FileHandler inputFileHandler = new FileHandler(inputFilePath, "txt");
                    String[] input = inputFileHandler.parseInputTextFile(inputFileHandler.getFile());
                    for(String s : input)
                    {
                        System.out.println(s);
                    }
                    Model model = new Modeler().model(fileHandler.getFile(), methodName, input);
//                    fileHandler.generateXML(model);
                    break;
                case "solve":
                    controller.start();
                    break;
                default: throw new Error("Invalid command: " + command);
            }
        }
        catch (IOException | ModelingException | AnalysisException e)
        {
            new IO().errorMsg(e);
            System.exit(1);
        }
    }

    private void processArgs(String[] args)
    {
        IO io = new IO();
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
            javaFilePath = args[0];
        }
        else
        {
            io.usage();
            System.exit(1);
        }
    }

}
