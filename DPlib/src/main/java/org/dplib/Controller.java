package org.dplib;

import com.github.javaparser.ParseException;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.dplib.analyse.*;
import org.dplib.compile.CompileException;
import org.dplib.compile.SourceCompiler;
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
public class Controller
implements PropertyChangeListener
{
    private String command;
    private String javaFilePath;
    private String inputFilePath;
    private String modelFilePath;
    private String methodName;

    private final IO io = new IO();
    private final View view = new View();
    private FileHandler fileHandler;
    private Model model;
    private Solver solver;

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
                    fileHandler = new FileHandler(javaFilePath, "java");
                    modelProblem();
                    break;
                case "solve":
                    fileHandler = new FileHandler(modelFilePath, "mod");
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

    private void modelProblem()
    throws ModelingException
    {
        SourceAnalyser sa;
        MethodDeclaration methodToAnalyse;
        MethodDeclaration callingMethod;
        List<Result> results;
        try
        {
            String sourceCode = fileHandler.parseSourceFile(fileHandler.getFile());

            sa = new SourceAnalyser();
            sa.parse(sourceCode);

            methodToAnalyse = sa.locateMethod(methodName);
            callingMethod = sa.locateMethod("main");

            File classFile = new SourceCompiler().compile(fileHandler.getFile(), sa.getClassName());
            FileHandler inputFileHandler = new FileHandler(inputFilePath, "txt");
            String[][] inputs = inputFileHandler.parseInputTextFile(inputFileHandler.getFile());
            results = new ClassAnalyser().analyse(classFile, inputs);
        }
        catch (IOException | ParseException | CompileException | AnalysisException e)
        {
            throw new ModelingException(e);
        }

        System.out.println("Creating model file...");
        Model model = new Modeler().model(sa.getClassName(),
                                          methodToAnalyse,
                                          callingMethod,
                                          sa.determineProblemType(methodToAnalyse),
                                          results);

        fileHandler.serializeModel(model);
        System.out.println("Model file created.");

        io.displayResults(model.getResults());
    }

    private void startGUI()
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

        view.addSolveBtnListener(new solveBtnListener());
        view.setTutorTimeData(tutorTimeDataset);
        view.setTutorOutputData(tutorOutputDataset);
        view.setEditorText(new CodeGenerator().generate(model.getClassName(),
                                                        model.getCallingMethodDeclaration(),
                                                        model.getCallingMethodBody(),
                                                        model.getMethodToAnalyseDeclaration()));

        SwingUtilities.invokeLater(view::createAndShowGUI);
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
