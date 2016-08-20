package org.dplib;

import org.dplib.analyse.AnalysisException;
import org.dplib.analyse.ProblemType;
import org.dplib.analyse.Result;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

// IO handling
class IO
{

    private String usage = "\nUsage:\n" +
            "\nTo model a problem:\n" +
            "\njava -jar DPLib-1.0-SNAPSHOT.jar " +
            "model <path/to/file_to_model.java> <method_to_analyse>\n" +
            "\nTo solve:\n" +
            "\njava -jar DPLib-1.0-SNAPSHOT.jar " +
            "<path/to/file_to_solve.java>\n";

    void usage() { System.err.println(usage); }

    void exceptionMsg(Throwable t)
    {
        if (!(t instanceof AnalysisException)
            && !(t instanceof ModelingException)
            && !(t instanceof IOException))
        { throw new Error("Expected ModelingException, AnalysisException or IOException."); }

        System.err.println(t.getMessage() + "\n");
    }

    void displayResults(List<Result> resultList)
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

    void pass() { System.out.println("\nPASS\n"); }

    void fail(String userOutput, String tutorOutput)
    {
        System.out.println("\nFAIL: output \"" + userOutput + "\"" +
                                   "does not match expected output \"" + tutorOutput + "\".\n");
    }

    void fail(ProblemType expectedType, ProblemType actualType)
    {
        System.out.println("\nFAIL: submitted solution is type: " + actualType + ". Required: " + expectedType + ".\n");
    }
}
