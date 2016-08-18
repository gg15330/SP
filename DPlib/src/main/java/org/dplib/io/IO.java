package org.dplib.io;

import org.dplib.analyse.ProblemType;
import org.dplib.analyse.Result;
import org.dplib.analyse.AnalysisException;
import org.dplib.ModelingException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

// IO handling
public class IO
{

    private String usage = "\nUsage:\n" +
            "\nTo model a problem:\n" +
            "\njava -jar DPLib-1.0-SNAPSHOT.jar " +
            "model <path/to/file_to_model.java> <method_to_analyse>\n" +
            "\nTo solve:\n" +
            "\njava -jar DPLib-1.0-SNAPSHOT.jar " +
            "<path/to/file_to_solve.java>\n";

    public void usage() { System.err.println(usage); }

    public void exceptionMsg(Throwable t)
    {
        if (!(t instanceof AnalysisException)
            && !(t instanceof ModelingException)
            && !(t instanceof IOException))
        { throw new Error("Expected ModelingException, AnalysisException or IOException."); }

        System.err.println(t.getMessage() + "\n");
    }

    public void displayResults(List<Result> resultList)
    {
        final int width = 82;
        System.out.println("\nRESULTS:\n");
        System.out.format("%4s\t%-40s\t%15s\t%11s", "", "INPUT", "OUTPUT", "TIME[ms]\n");
        for(int i = 0; i < width; i++) { System.out.print("-"); }
        System.out.println();
        for(int j = 0; j < resultList.size(); j++)
        {
            System.out.format("%4s\t%-40s\t%15s\t%10d\n",
                              ("[" + String.valueOf(j) + "]"),
                              Arrays.toString(resultList.get(j).getInput()),
                              resultList.get(j).getOutput(),
                              resultList.get(j).getExecutionTime());
        }
        System.out.println();
    }

    public void pass() { System.out.println("\nPASS\n"); }

    public void fail(String userOutput, String tutorOutput)
    {
        System.out.println("\nFAIL: output \"" + userOutput + "\"" +
                                   "does not match expected output \"" + tutorOutput + "\".\n");
    }

    public void fail(ProblemType expectedType, ProblemType actualType)
    {
        System.out.println("\nFAIL: submitted solution is type: " + actualType + ". Required: " + expectedType + ".\n");
    }
}
