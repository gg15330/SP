package org.ggraver.DPlib;

import org.ggraver.DPlib.Exception.AnalysisException;
import org.ggraver.DPlib.Exception.ModelingException;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

import static javafx.application.Application.launch;

// overall program control
public class Launcher
{

//    public static void main(String args[])
//    {
//        launch(GUIApp.class, args);
//

//    }

    public static void main(String[] args)
    {
//        String jre = System.getProperty("java.runtime.name");
//        if (jre.equals("Java(TM) SE Runtime Environment"))
//        {
//            launch(GUIApp.class, args);
//        }
//        else if (jre.equals("OpenJDK Runtime Environment"))
//        {
//
//        }

        SwingDisplay swingDisplay = new SwingDisplay();
        javax.swing.SwingUtilities.invokeLater(swingDisplay::createAndShowGUI);
        try
        {
            new CommandLineApp().start(args);
            System.out.println("Main EDT: " + SwingUtilities.isEventDispatchThread());
        }
        catch (IOException | AnalysisException | ModelingException ex)
        {
            new IO().exit(ex);
            System.exit(1);
        }
    }

}
