package org.ggraver.DPlib;

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
//            launch(FXDisplay.class, args);
//        }
//        else
//        {
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
//        }
    }

}
