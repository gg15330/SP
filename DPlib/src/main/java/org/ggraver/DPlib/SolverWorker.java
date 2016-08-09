package org.ggraver.DPlib;

import org.ggraver.DPlib.Exception.AnalysisException;

import javax.swing.*;
import java.io.File;
import java.util.concurrent.ExecutionException;

/**
 * Created by george on 09/08/16.
 */
public class SolverWorker
extends SwingWorker<Result, Void>
{
    private final File javaFile;
    private final Model model;

    public SolverWorker(Model model, File javaFile)
    {
        this.model = model;
        this.javaFile = javaFile;
    }

    @Override
    public Result doInBackground()
    throws AnalysisException
    {
        System.out.println("doInBackground EDT: " + SwingUtilities.isEventDispatchThread());
//        return new Solver().solve(model, javaFile);
        return null;
    }

    @Override
    protected void done() {
        System.out.println("done EDT: " + SwingUtilities.isEventDispatchThread());
        try {
            System.out.println("Done");
            Result result = get();

//            swingView.setExecutionTimeGraph(result.getModelExecutionTime(), result.getUserExecutionTime());
//            swingView.setInstructionCountGraph(result.getModelInstructionCount(), result.getUserInstructionCount());
        } catch (ExecutionException e) {
            e.getCause().printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}