package org.ggraver.DPlib;

import org.ggraver.DPlib.Exception.AnalysisException;
import org.ggraver.DPlib.Exception.ModelingException;

import java.io.IOException;

import static javafx.application.Application.launch;

// overall program control
public class Launcher

{
    public static void main(String args[])
    {
        try
        {
            Class.forName("javafx.application.Application", false, javafx.application.Application.class.getClassLoader());
            launch(GUIApp.class, args);
        }
        catch (ClassNotFoundException cnfe)
        {
            System.err.println("Could not launch JavaFX Application - " +
                                "reverting to command line interface...\n");
            try
            {
                new CommandLineApp().start(args);
            }
            catch (IOException | AnalysisException | ModelingException ex)
            {
                new IO().exit(ex);
                System.exit(1);
            }
        }


    }

}
