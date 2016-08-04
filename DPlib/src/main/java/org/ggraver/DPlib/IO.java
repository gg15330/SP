package org.ggraver.DPlib;

import org.ggraver.DPlib.Exception.AnalysisException;
import org.ggraver.DPlib.Exception.ModelingException;

import java.io.IOException;

// IO handling
class IO
{

    private String command;
    private String filePath;
    private String methodName;

    void processArgs(String[] args)
    throws IOException
    {
        if(args.length == 3)
        {
            command = args[0];
            filePath = args[1];
            methodName = args[2];
            if(!command.equals("model"))
            {
                throw new IOException(usage);
            }
        }
        else if(args.length == 2)
        {
            command = args[0];
            filePath = args[1];
            if(!command.equals("solve"))
            {
                throw new IOException(usage);
            }
        }
        else
        {
            throw new IOException(usage);
        }
    }

    void exit(Exception e)
    {
        if (!(e instanceof AnalysisException)
                && !(e instanceof ModelingException)
                && !(e instanceof IOException))
        {
            e.printStackTrace();
            throw new Error("Expected ModelingException, AnalysisException or IOException.");
        }
        System.err.println(e.getMessage());
    }

    void displayResult(Result result)
    {
        String[][] resultsTable = new String[4][4];

        resultsTable[0] = new String[] {"", "MODEL", "USER", "RESULT"};
        resultsTable[1] = new String[] {
                "OUTPUT",
                result.getModelOutput(),
                result.getUserOutput(),
                pass(result.getOutputPass())
        };
        resultsTable[2] = new String[] {
                "EXECUTION TIME",
                String.valueOf(result.getModelExecutionTime()),
                String.valueOf(result.getUserExecutionTime()),
                pass(result.getExecutionTimePass())
        };
        resultsTable[3] = new String[] {
                "INSTRUCTION COUNT",
                String.valueOf(result.getModelInstructionCount()),
                String.valueOf(result.getUserInstructionCount()),
                pass(result.getInstructionCountPass())
        };

        System.out.println();
        for(String[] row : resultsTable)
        {
            System.out.format("%-20s%-20s%-20s%-20s\n", row[0], row[1], row[2], row[3]);
        }
        System.out.println();
    }

    private String pass(boolean b)
    {
        if(b)
        {
            return "PASS";
        }
        else
        {
            return "FAIL";
        }
    }

    String getMethodName()
    {
        return methodName;
    }

    String getFilePath()
    {
        return filePath;
    }

    private String usage = "\nUsage:\n" +
            "\nTo model a problem:\n" +
            "\njava -jar DPLib-1.0-SNAPSHOT.jar " +
            "model <path/to/file_to_model.java> <method_to_analyse>\n" +
            "\nTo solve:\n" +
            "\njava -jar DPLib-1.0-SNAPSHOT.jar " +
            "solve <path/to/file_to_solve.java>\n";

    private String analysisReport;


    String getCommand()
    {
        return command;
    }

}
