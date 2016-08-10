package org.ggraver.DPlib;

import org.apache.commons.io.FileUtils;
import org.ggraver.DPlib.Exception.AnalysisException;
import org.ggraver.DPlib.Exception.CompileException;

import java.io.*;

import static org.apache.commons.io.FilenameUtils.removeExtension;


// generates .class files and analyses performance of user solution
class ClassAnalyser
{

    private final int PERF_INSTR_LINE = 5;
    private final String TEMP_FILENAME = "temp.txt";
    private String className;
    private File classFile;
    private String output;
    private long executionTime;
    private long instructionCount;

    ClassAnalyser(File sourceFile, String className)
    throws AnalysisException
    {
        try
        {
            this.className = className;
            compile(sourceFile);
        }
        catch (CompileException e)
        {
            throw new AnalysisException(e);
        }
    }

    // analyse the compiled .class file for performance
    void analyse()
    throws AnalysisException
    {
        if (!classFile.exists() || classFile.length() == 0)
        {
            throw new AnalysisException(".class file does not exist or is empty.");
        }
        File dir, log;
        try
        {
            dir = new File(classFile.getParent());
            log = tempFile(TEMP_FILENAME, dir);
        }
        catch (IOException ioe)
        {
            throw new AnalysisException(ioe);
        }
        ProcessBuilder perfStat = buildPerfStat(
                log.getName(),
                removeExtension(classFile.getName()),
                dir);

        try
        {
            execute(perfStat);
        }
        catch (Exception e)
        {
            throw new AnalysisException(e);
        }
        try
        {
            instructionCount = fetchInstructionCount(log, PERF_INSTR_LINE);
        }
        catch (Exception e)
        {
            throw new Error(e);
        }
        if (!log.delete())
        {
            throw new Error("temp log file not successfully deleted.");
        }
    }

    private void execute(ProcessBuilder pb)
    throws IOException, InterruptedException, AnalysisException
    {
        // remember to put timeout in waitFor()
        long start = System.currentTimeMillis();
        Process p = pb.start();
        p.waitFor();
        long end = System.currentTimeMillis();

        if ((end - start) < 0)
        {
            throw new Error("Execution time should not be less than 0.");
        }

        executionTime = end - start;

        InputStreamReader isr = new InputStreamReader(p.getInputStream());
        BufferedReader br = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String line;
        int lineCount = 0;

        while ((line = br.readLine()) != null)
        {
            sb.append(line);
            lineCount++;
            if (lineCount > 1)
            {
                throw new AnalysisException("Program produced more than 1 line of output.");
            }
        }

        output = sb.toString();
    }

    private ProcessBuilder buildPerfStat(String logFileName, String classFileName, File dir)
    {
        ProcessBuilder build = new ProcessBuilder(
                "perf", "stat",
                "-e", "instructions:u",
                "-o", logFileName,
                "java", classFileName
        );
        build.directory(dir);
        build.inheritIO();
        return build;
    }

    private File tempFile(String fileName, File dir)
    throws IOException
    {
        File temp = new File(dir + "/" + fileName);

        if (temp.exists())
        {
            System.err.println("Deleting pre-existing temp file - should not exist...");
            if (!temp.delete())
            {
                throw new Error("Could not delete pre-existing temp file.");
            }
        }
        if (!temp.createNewFile())
        {
            throw new Error("Could not create new temp file.");
        }

        return temp;
    }

    // get the line containing the instruction count from the perf stat output stream and return it as a long
    private long fetchInstructionCount(File f, int lineNum)
    throws IOException, NumberFormatException
    {
        String instructionsString = FileUtils.readLines(f, "UTF-8").get(lineNum)
                                             .replaceAll(" ", "")
                                             .replaceAll("instructions:u", "")
                                             .replaceAll(",", "");
        return Long.parseLong(instructionsString);
    }

    // compile the user-submitted .java file for performance analysis
    private void compile(File sourceFile)
    throws CompileException
    {
        if (sourceFile == null)
        {
            throw new CompileException(".java file should not be null.");
        }
        System.out.println("Compiling file: " + sourceFile.getPath());
        Process p;
        int result;
        try
        {
            ProcessBuilder build = new ProcessBuilder("javac", sourceFile.getPath());
            p = build.start();
            new SubProcessStream(p.getInputStream()).start();
            new SubProcessStream(p.getErrorStream()).start();
            result = p.waitFor();
        }
        catch (IOException | InterruptedException e)
        {
            throw new Error(e);
        }
        if (result != 0)
        {
            throw new CompileException("Could not compile .java file. Please ensure your .java file inputStream valid.");
        }

        System.out.println(sourceFile.getName() + " compiled successfully.");
        classFile = new File(sourceFile.getParent() + "/" + className + ".class");

        if (!classFile.exists())
        {
            throw new CompileException("Could not find .class file.");
        }
    }

    long getInstructionCount()
    {
        return instructionCount;
    }

    long getExecutionTime()
    {
        return executionTime;
    }

    public String getOutput()
    {
        return output;
    }

//    adapted from http://www.javaworld.com/article/2071275/core-java/when-runtime-exec---won-t.html?page=2
    private class SubProcessStream extends Thread
    {
        InputStream inputStream;

        SubProcessStream(InputStream inputStream)
        {
            this.inputStream = inputStream;
        }

        @Override
        public void run()
        {
            try
            {
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = br.readLine()) != null)
                {
                    System.out.println(line);
                }
            }
            catch (IOException e)
            {
                throw new Error(e);
            }
        }
    }
}
