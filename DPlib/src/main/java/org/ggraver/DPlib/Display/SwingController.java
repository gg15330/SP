package org.ggraver.DPlib.Display;

import org.ggraver.DPlib.CommandLineApp;
import org.ggraver.DPlib.Exception.AnalysisException;
import org.ggraver.DPlib.Exception.ModelingException;
import org.ggraver.DPlib.IO;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by george on 07/08/16.
 */
public class SwingController
{

    SwingView swingView;

    public SwingController(SwingView swingView)
    {
        this.swingView = swingView;
        this.swingView.addSolveBtnListener(new solveBtnListener());
        System.out.println("SwingController constructor EDT: " + SwingUtilities.isEventDispatchThread());
    }

    public void start()
    {
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

    private class solveBtnListener
    implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            System.out.println("EDT: " + SwingUtilities.isEventDispatchThread());
            swingView.setEditorText("MVC BABByYYY!!!");
        }

    }

}
