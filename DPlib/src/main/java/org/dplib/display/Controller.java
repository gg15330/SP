package org.dplib.display;

import org.dplib.*;
import org.dplib.exception.AnalysisException;
import org.dplib.exception.SolvingException;

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
        System.out.println("Model data:" +
        "\nDescription - " + model.getDescription() +
        "\nType - " + model.getType() +
        "\nRequired classes - " + model.getRequiredClasses() +
        "\nOutput" + model.getOutput() +
        "\nExecution time - " + model.getExecutionTime() +
        "\nInstruction count - " + model.getInstructionCount());
        view.setEditorText(new CodeGenerator().generate(model));
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
        throws AnalysisException, IOException, SolvingException
        {
            File compiledJavaFile;
            compiledJavaFile = fileHandler.createJavaFile(view.getEditorText());
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
                if(e.getCause() instanceof Exception)
                {
                    io.errorMsg((Exception) e.getCause());
                }
                else {
                    throw new Error(e);
                }
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

    }

}
