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
            SwingController swingController = new SwingController(swingView);
            swingController.start();
            javax.swing.SwingUtilities.invokeLater(swingView::createAndShowGUI);
            System.out.println("Launcher EDT: " + SwingUtilities.isEventDispatchThread());
//        }
    }

}
