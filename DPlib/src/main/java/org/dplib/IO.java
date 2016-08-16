package org.dplib;

import org.dplib.exception.AnalysisException;
import org.dplib.exception.ModelingException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

// IO handling
public class IO
{

    public void errorMsg(Throwable t)
    {
        if (!(t instanceof AnalysisException)
                && !(t instanceof ModelingException)
                && !(t instanceof IOException))
        {
            t.printStackTrace();
            throw new Error("Expected ModelingException, AnalysisException or IOException.");
        }
        System.err.println(t.getMessage() + "\n");
    }

    void usage()
    {
        System.err.println(usage);
    }

    void fail(String userOutput, String tutorOutput)
    {
        System.out.println("\nFAIL: output \"" + userOutput + "\"" +
                           "does not match expected output \"" + tutorOutput + "\".\n");
    }

    void displayResults(List<Result> resultList)
    {
        int i = 0;
        final int width = 82;
        System.out.println("\nRESULTS:\n");
        System.out.format("%4s\t%-40s\t%15s\t%11s", "", "INPUT", "OUTPUT", "TIME[ms]\n");
        for(int j = 0; j < width; j++) { System.out.print("-"); }
        System.out.println();
        for(Result r : resultList)
        {
            System.out.format("%4s\t%-40s\t%15s\t%10d\n",
                              ("[" + String.valueOf(i) + "]"),
                              Arrays.toString(r.getInput()),
                              r.getOutput(),
                              r.getExecutionTime());
            i++;
        }
        System.out.println();
    }

    void pass()
    {
        System.out.println("\nPASS\n");
    }

    private String usage = "\nUsage:\n" +
            "\nTo model a problem:\n" +
            "\njava -jar DPLib-1.0-SNAPSHOT.jar " +
            "model <path/to/file_to_model.java> <method_to_analyse>\n" +
            "\nTo solve:\n" +
            "\njava -jar DPLib-1.0-SNAPSHOT.jar " +
            "<path/to/file_to_solve.java>\n";

}
