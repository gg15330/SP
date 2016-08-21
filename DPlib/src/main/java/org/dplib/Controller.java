package org.dplib;

import org.dplib.analyse.*;
import org.dplib.display.View;
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

// overall program control - uses MVC structure to pass/retrieve information to/from GUI
public class Controller
implements PropertyChangeListener
{

    private final IO io = new IO();
    private final FileHandler fileHandler = new FileHandler();
    private View view;
    private Solver solver;
    private Model model;

    private File modelFile;

    public static void main(String[] args)
    {
        Controller guiController = new Controller();
        guiController.run(args);
    }

    private void run(String[] args)
    {
        io.processArgs(args);
        try
        {
            switch (io.getCommand()) {
                case "model":
                    File sourceFile = fileHandler.locateFile(io.getJavaFilePath(), "java");
                    io.filePath("Source", sourceFile.getPath());
                    File inputFile = fileHandler.locateFile(io.getInputFilePath(), "txt");
                    io.filePath("Input", inputFile.getPath());

                    io.createModelMsg();
                    model = new Modeler().model(sourceFile, inputFile, io.getMethodName(), fileHandler);
                    io.modelCreatedMsg();

                    io.createModFileMsg();
                    fileHandler.serializeModel(model, sourceFile.getParentFile());
                    io.modFileCreatedMsg();

                    io.printModel(model);
                    break;

                case "solve":
                    modelFile = fileHandler.locateFile(io.getModelFilePath(), "mod");
                    io.filePath("Model", modelFile.getPath());
                    model = fileHandler.deserializeModelFile(modelFile);
                    startGUI();
                    break;

                default: throw new Error("Invalid command: " + io.getCommand());
            }
        }
        catch (IOException | ModelingException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void startGUI()
    {
        DefaultCategoryDataset tutorTimeDataset = createDataset(model, "time");
        DefaultCategoryDataset tutorOutputDataset = createDataset(model, "output");

        String editorText = new CodeGenerator().generate(model.getClassName(),
                                                         model.getCallingMethodDeclaration(),
                                                         model.getCallingMethodBody(),
                                                         model.getMethodToAnalyseDeclaration());

        view = new View(editorText);
        view.addSolveBtnListener(new solveBtnListener());
        view.setTutorTimeData(tutorTimeDataset);
        view.setTutorOutputData(tutorOutputDataset);

        SwingUtilities.invokeLater(view::createAndShowGUI);
    }

    private DefaultCategoryDataset createDataset(Model model, String type)
    {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for(int i = 0; i < model.getResults().size(); i++)
        {
            Result r = model.getResults().get(i);

            if("time".equals(type))
            {
                dataset.addValue(r.getExecutionTime(), "Tutor", String.valueOf(i));
            }
            else if("output".equals(type))
            {
                dataset.addValue(Long.parseLong(r.getOutput()), "Tutor", String.valueOf(i));
            }
        }

        return dataset;
    }

//    updates the graphs and presents results of analysis upon completion of Solver background thread
    public void propertyChange(PropertyChangeEvent pce) {

        if (pce.getNewValue().equals(SwingWorker.StateValue.DONE)) {
            Analysis analysis;

            try { analysis = solver.get(); }
            catch (InterruptedException e) { e.printStackTrace(); return; }
            catch (ExecutionException e)
            {
                if(!(e.getCause() instanceof AnalysisException)
                || !(e.getCause()instanceof IOException))
                {
                    io.exceptionMsg(new AnalysisException(e.getCause()));
                }
                else
                {
                    io.exceptionMsg(e.getCause());
                }
                return;
            }

            updateGraphs(analysis.getResults());
            presentAnalysis(analysis);
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

//    displays either pass or fail depending on whether the submitted solution matches the model
    private void presentAnalysis(Analysis analysis)
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
            solver = new Solver(model, view.getEditorText(), fileHandler, modelFile.getParentFile());
            solver.addPropertyChangeListener(Controller.this);
            solver.execute();
        }

    }

}
