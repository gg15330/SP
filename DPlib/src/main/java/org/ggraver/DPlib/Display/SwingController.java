package org.ggraver.DPlib.Display;

import org.ggraver.DPlib.*;
import org.ggraver.DPlib.Exception.AnalysisException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by george on 07/08/16.
 */
public class SwingController
{

    private Model model;
    private SwingView swingView = new SwingView();
    private Solver solver = new Solver();
    private IO io;
    private FileHandler fileHandler;
    private Result result;

    public SwingController(IO io, FileHandler fileHandler)
    throws IOException
    {
        swingView.addSolveBtnListener(new solveBtnListener());
        this.io = io;
        this.fileHandler = fileHandler;
    }

    public void start()
    throws IOException, AnalysisException
    {
        model = fileHandler.parseXML();
        result = solver.solve(model, fileHandler.getFile());
        SwingUtilities.invokeLater(swingView::createAndShowGUI);
        System.out.println("SwingController EDT: " + SwingUtilities.isEventDispatchThread());
    }

    private class solveBtnListener
    implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            try
            {
                model = fileHandler.parseXML();
                result = solver.solve(model, fileHandler.getFile());
                System.out.println("actionPerformed EDT: " + SwingUtilities.isEventDispatchThread());
            }
            catch (IOException | AnalysisException ex)
            {
                io.errorMsg(ex);
            }
            swingView.setExecutionTimeGraph(result.getModelExecutionTime(), result.getUserExecutionTime());
        }

    }

}
