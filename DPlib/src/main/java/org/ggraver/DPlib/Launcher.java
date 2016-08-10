package org.ggraver.DPlib;

import org.ggraver.DPlib.Display.Controller;
import org.ggraver.DPlib.Exception.AnalysisException;
import org.ggraver.DPlib.Exception.ModelingException;

import java.io.IOException;

import static javafx.application.Application.launch;

// overall program control
public class Launcher
{

    private String command;
    private String filePath;
    private String methodName;

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
            Controller controller = new Controller(filePath);
            switch (command) {
                case "model":
                    FileHandler fileHandler = new FileHandler(filePath, "java");
                    Model model = new Modeler().model(fileHandler.getFile(), methodName);
                    fileHandler.generateXML(model);
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
        if(args.length == 3)
        {
            command = args[0];
            filePath = args[1];
            methodName = args[2];
            if(!command.equals("model"))
            {
                io.usage();
                System.exit(1);
            }
        }
        else if(args.length == 2)
        {
            command = args[0];
            filePath = args[1];
            if(!command.equals("solve"))
            {
                io.usage();
                System.exit(1);
            }
        }
        else
        {
            io.usage();
            System.exit(1);
        }
    }

}
