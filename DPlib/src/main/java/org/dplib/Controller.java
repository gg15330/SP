package org.dplib;

import com.github.javaparser.ParseException;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.dplib.analyse.*;
import org.dplib.compile.CompileException;
import org.dplib.compile.SourceCompiler;
import org.dplib.display.View;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.xml.transform.Source;
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
public class Controller
implements PropertyChangeListener
{
    private String command;
    private String javaFilePath;
    private String inputFilePath;
    private String modelFilePath;
    private String methodName;

    private final IO io = new IO();
    private View view;
    private final FileHandler fileHandler = new FileHandler();
    private Model model;
    private Solver solver;
    private File modelFile;

    public static void main(String[] args)
    {
        Controller guiController = new Controller();
        guiController.processArgs(args);
        guiController.run();
    }

    private void run()
    {
        try
        {
            switch (command) {
                case "model":
                    System.out.println("Creating model file...");

                    File sourceFile = fileHandler.locateFile(javaFilePath, "java");
                    File inputFile = fileHandler.locateFile(inputFilePath, "txt");
                    model = new Modeler().model(sourceFile, inputFile, methodName, fileHandler);

                    fileHandler.serializeModel(model, sourceFile.getParentFile());
                    System.out.println("Model file created.");
                    io.displayResults(model.getResults());
                    break;
                case "solve":
                    modelFile = fileHandler.locateFile(modelFilePath, "mod");
                    model = fileHandler.deserializeModelFile(modelFile);
                    startGUI();
                    break;
                default: throw new Error("Invalid command: " + command);
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

    private void processArgs(String[] args)
    {
        if(args.length == 4)
        {
            if(!args[0].equals("model"))
            {
                io.usage();
                System.exit(1);
            }
            command = args[0];
            javaFilePath = args[1];
            inputFilePath = args[2];
            methodName = args[3];
        }
        else if(args.length == 1)
        {
            command = "solve";
            modelFilePath = args[0];
        }
        else
        {
            io.usage();
            System.exit(1);
        }
    }

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
            solver.addPropertyChangeListener(Controller.this);
            solver.execute();
        }

    }

}
