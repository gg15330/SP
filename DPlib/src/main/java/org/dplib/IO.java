package org.dplib;

import org.dplib.analyse.AnalysisException;
import org.dplib.analyse.Model;
import org.dplib.analyse.ProblemType;
import org.dplib.analyse.Result;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

// IO handling
class IO
{

    private String command;
    private String javaFilePath;
    private String inputFilePath;
    private String modelFilePath;
    private String methodName;

    void processArgs(String[] args)
    {
        if(args.length == 4)
        {
            if(!args[0].equals("model"))
            {
                usage();
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
            usage();
            System.exit(1);
        }
    }

    private void usage() {
        String usage = "\nUsage:\n" +
                "\nTo model a problem:\n" +
                "\njava -jar DPLib-1.0-SNAPSHOT.jar " +
                "model <path/to/file_to_model.java> <method_to_analyse>\n" +
                "\nTo solve:\n" +
                "\njava -jar DPLib-1.0-SNAPSHOT.jar " +
                "<path/to/file_to_solve.java>\n";

        System.err.println(usage);
    }

    void exceptionMsg(Throwable t)
    {
        if (!(t instanceof AnalysisException)
            && !(t instanceof ModelingException)
            && !(t instanceof IOException))
        { throw new Error("Expected ModelingException, AnalysisException or IOException."); }

        System.err.println(t.getMessage() + "\n");
    }

    void filePath(String type, String path)
    {
        System.out.println(type + " file: " + path);
    }

    void createModelMsg()
    {
        System.out.println("Creating problem model...");
    }

    void modelCreatedMsg()
    {
        System.out.println("Problem modeled.");
    }

    void createModFileMsg()
    {
        System.out.println("Creating .mod file...");
    }

    void printModel(Model model)
    {
        final int width = 82;
        System.out.println("\n\nPROBLEM MODEL\n");
        System.out.println("Class name: " + model.getClassName());
        System.out.println("Calling method: " + model.getCallingMethodDeclaration());
        System.out.println("Method to analyse: " + model.getMethodToAnalyseDeclaration());
        System.out.println("Problem type: " + model.getProblemType());
        System.out.println("Results:");
        System.out.format("\n%4s\t%-40s\t%15s\t%11s", "", "INPUT", "OUTPUT", "TIME[ms]\n");
        for(int i = 0; i < width; i++) { System.out.print("-"); }
        System.out.println();
        for(int j = 0; j < model.getResults().size(); j++)
        {
            System.out.format("%4s\t%-40s\t%15s\t%10d\n",
                              ("[" + String.valueOf(j) + "]"),
                              Arrays.toString(model.getResults().get(j).getInput()),
                              model.getResults().get(j).getOutput(),
                              model.getResults().get(j).getExecutionTime());
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

    public String getCommand()
    {
        return command;
    }

    public String getJavaFilePath()
    {
        return javaFilePath;
    }

    public String getInputFilePath()
    {
        return inputFilePath;
    }

    public String getModelFilePath()
    {
        return modelFilePath;
    }

    public String getMethodName()
    {
        return methodName;
    }

    public void modFileCreatedMsg()
    {
        System.out.println(".mod file created.");
    }
}
