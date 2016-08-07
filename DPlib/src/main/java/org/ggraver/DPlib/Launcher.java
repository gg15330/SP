package org.ggraver.DPlib;

import org.ggraver.DPlib.Display.SwingController;
import org.ggraver.DPlib.Display.SwingView;
import org.ggraver.DPlib.Exception.AnalysisException;
import org.ggraver.DPlib.Exception.ModelingException;

import java.io.IOException;

import javax.swing.*;

import static javafx.application.Application.launch;

// overall program control
public class Launcher
{

    public static void main(String[] args)
    {
//        if (System.getProperty("java.runtime.name").equals("Java(TM) SE Runtime Environment"))
//        {
//            launch(FXView.class, args);
//        }
//        else
//        {
        SwingView swingView = new SwingView();
        IO io = new IO();
        FileHandler fileHandler;
        try
        {
            io.processArgs(args);
            fileHandler = new FileHandler(io.getFilePath(), "java");
            switch (io.getCommand()) {
                case "model":
                    Model model = new Modeler().model(fileHandler.getFile(), io.getMethodName());
                    fileHandler.generateXML(model);
                    break;
                case "solve":
                    SwingController swingController = new SwingController(swingView, io);
                    javax.swing.SwingUtilities.invokeLater(swingView::createAndShowGUI);
                    System.out.println("Launcher EDT: " + SwingUtilities.isEventDispatchThread());

                    break;
                default: throw new Error("Invalid command: " + io.getCommand());
            }
        }
        catch (IOException | ModelingException | AnalysisException e)
        {
            io.exit(e);
            System.exit(1);
        }


//        }
    }

}
