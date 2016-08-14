package org.ggraver.DPlib.Display;

import org.ggraver.DPlib.*;
import org.ggraver.DPlib.Exception.AnalysisException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
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
        this.fileHandler = new FileHandler(filePath, "mod");
    }

    public void start()
    throws IOException, AnalysisException
    {
        model = fileHandler.deserializeModelFile();

        for(Result result : model.getResults())
        {
            System.out.println("Results:" +
            "\n" + result.getInput() + "\n" + result.getOutput() + "\n" + result.getExecutionTime());
        }

        view.setEditorText(new CodeGenerator().generate(model.getClassName(),
                                                        model.getCallingMethodDeclaration(),
                                                        model.getCallingMethodBody(),
                                                        model.getMethodToAnalyseDeclaration()));
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
    extends SwingWorker<List<Result>, Void>
    {

        @Override
        public List<Result> doInBackground()
        throws AnalysisException, IOException
        {
            File compiledJavaFile;
            compiledJavaFile = fileHandler.createJavaFile(view.getEditorText());
            return solver.solve(model, compiledJavaFile);
        }

        @Override
        protected void done()
        {
            List<Result> results;
            try
            {
                results = get();
//                view.setExecutionTimeGraph(result.getModelExecutionTime(), result.getUserExecutionTime());
//                view.setInstructionCountGraph(result.getModelInstructionCount(), result.getUserInstructionCount());
            }
            catch (ExecutionException e)
            {
                io.errorMsg(e.getCause());
                return;
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
                return;
            }

            for(int i = 0; i < results.size(); i++)
            {
                String userOutput = results.get(i).getOutput();
                String tutorOutput = model.getResults().get(i).getOutput();

                if(!userOutput.equals(tutorOutput))
                {
                    io.fail(userOutput, tutorOutput);
                    return;
                }
            }
            io.pass();
        }

    }

}
