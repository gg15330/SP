package org.dplib.display;

import org.dplib.*;
import org.dplib.analyse.Analysis;
import org.dplib.analyse.Model;
import org.dplib.analyse.Result;
import org.dplib.analyse.AnalysisException;
import org.dplib.io.CodeGenerator;
import org.dplib.io.IO;
import org.dplib.Solver;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by george on 07/08/16.
 */
public class GUIController
implements PropertyChangeListener
{

    private final View view = new View();
    private final IO io = new IO();
    private final FileHandler fileHandler;
    private Model model;
    private Solver solver;

    public GUIController(String filePath)
    throws IOException
    {
        view.addSolveBtnListener(new solveBtnListener());
        this.fileHandler = new FileHandler(filePath, "mod");
    }

    public void start()
    throws IOException, AnalysisException
    {
        model = fileHandler.deserializeModelFile();
        DefaultCategoryDataset tutorTimeDataset = new DefaultCategoryDataset();
        DefaultCategoryDataset tutorOutputDataset = new DefaultCategoryDataset();

        for(int i = 0; i < model.getResults().size(); i++)
        {
            Result r = model.getResults().get(i);
            tutorTimeDataset.addValue(r.getExecutionTime(), "Tutor", String.valueOf(i));
            tutorOutputDataset.addValue(Long.parseLong(r.getOutput()), "Tutor", String.valueOf(i));
        }

        view.setTutorTimeData(tutorTimeDataset);
        view.setTutorOutputData(tutorOutputDataset);
        view.setEditorText(new CodeGenerator().generate(model.getClassName(),
                                                        model.getCallingMethodDeclaration(),
                                                        model.getCallingMethodBody(),
                                                        model.getMethodToAnalyseDeclaration()));

        SwingUtilities.invokeLater(view::createAndShowGUI);
    }

    public void propertyChange(PropertyChangeEvent pce) {

        if (pce.getNewValue().equals(SwingWorker.StateValue.DONE)) {
            Analysis analysis;
            try
            {
                analysis = solver.get();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
                return;
            }
            catch (ExecutionException e)
            {
                io.exceptionMsg(new AnalysisException(e.getCause()));
                return;
            }

            updateGraphs(analysis.getResults());
            showResult(analysis);
        }

    }

    private void updateGraphs(List<Result> results)
    {
        DefaultCategoryDataset studentTimeDataset = new DefaultCategoryDataset();
        DefaultCategoryDataset studentOutputDataset = new DefaultCategoryDataset();

        for (int i = 0; i < results.size(); i++)
        {
            Result studentResult = results.get(i);
            studentTimeDataset.addValue(studentResult.getExecutionTime(), "Your code", String.valueOf(i));
            studentOutputDataset.addValue(Integer.parseInt(studentResult.getOutput()), "Your code", String.valueOf(i));
        }

        view.setExecutionTimeGraph(studentTimeDataset);
        view.setOutputGraph(studentOutputDataset);
    }

    private void showResult(Analysis analysis)
    {
        if(!analysis.getProblemType().equals(model.getProblemType()))
        {
            io.fail(model.getProblemType(), analysis.getProblemType());
            return;
        }

        for(int i = 0; i < analysis.getResults().size(); i++)
        {
            Result studentResult = analysis.getResults().get(i);
            Result tutorResult = model.getResults().get(i);

            if(!studentResult.getOutput().equals(tutorResult.getOutput()))
            {
                io.fail(studentResult.getOutput(), tutorResult.getOutput());
                return;
            }
        }

        io.pass();
    }

    private class solveBtnListener
    implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            solver = new Solver(model, view.getEditorText(), fileHandler);
            solver.addPropertyChangeListener(GUIController.this);
            solver.execute();
        }

    }

}
