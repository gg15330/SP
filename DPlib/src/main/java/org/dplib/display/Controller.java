package org.dplib.display;

import org.dplib.*;
import org.dplib.exception.AnalysisException;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * Created by george on 07/08/16.
 */
public class Controller
{

    private final IO io = new IO();
    private FileHandler fileHandler;
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

    private class solveBtnListener
    implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            try
            {
                File javaFile = fileHandler.createTempJavaFile(view.getEditorText());
                new Solver(model, javaFile, view, io).execute();
            }
            catch (IOException ioe)
            {
                io.errorMsg(ioe);
            }
        }

    }

}
