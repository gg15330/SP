package org.ggraver.DPlib.Display;

import org.ggraver.DPlib.*;
import org.ggraver.DPlib.Exception.AnalysisException;
import org.ggraver.DPlib.Exception.ModelingException;

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
    Solver solver;
    IO io;

    public SwingController(SwingView swingView, IO io)
    throws AnalysisException, IOException
    {
        this.swingView = swingView;
        this.swingView.addSolveBtnListener(new solveBtnListener());
        this.io = io;

        FileHandler fileHandler = new FileHandler();
        Model model = fileHandler.parseXML();
        Result result = solver.solve(model, fileHandler.getFile());
        io.displayResult(result);

        SwingUtilities.invokeLater(swingView::createAndShowGUI);
    }

    private class solveBtnListener
    implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            swingView.setEditorText("MVC BABByYYY!!!");
        }

    }

}
