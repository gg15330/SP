package org.ggraver.DPlib.Display;

import org.ggraver.DPlib.*;
import org.ggraver.DPlib.Exception.AnalysisException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by george on 07/08/16.
 */
public class Controller
{

    private IO io = new IO();
    private FileHandler fileHandler;
    private Solver solver = new Solver();
    private View view = new View();
    private Model model;

    public Controller(String filePath)
    throws IOException
    {
        view.addSolveBtnListener(new solveBtnListener());
        this.fileHandler = new FileHandler(filePath, "java");
    }

    public void start()
    throws IOException, AnalysisException
    {
        model = fileHandler.parseXML();
        view.setEditorText(fileHandler.getFileAsString());
        SwingUtilities.invokeLater(view::createAndShowGUI);
    }

    private class solveBtnListener
    implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            new SolverWorker().execute();
        }

    }

    //    solves problem in background thread
    private class SolverWorker
    extends SwingWorker<Result, Void>
    {

        @Override
        public Result doInBackground()
        throws AnalysisException
        {
            File compiledJavaFile;
            try
            {
                compiledJavaFile = fileHandler.createJavaFile(view.getEditorText());
            }
            catch (IOException e)
            {
                throw new AnalysisException(e);
            }
            return solver.solve(model, compiledJavaFile);
        }

        @Override
        protected void done()
        {
            try
            {
                Result result = get();
                view.setExecutionTimeGraph(result.getModelExecutionTime(), result.getUserExecutionTime());
                view.setInstructionCountGraph(result.getModelInstructionCount(), result.getUserInstructionCount());
            }
            catch (ExecutionException e)
            {
                if (!(e.getCause() instanceof AnalysisException))
                {
                    e.printStackTrace();
                    throw new Error("Expected AnalysisException, got: " + e.getCause().getClass());
                }
                io.errorMsg((AnalysisException) e.getCause());
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

    }

}
