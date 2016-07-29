package org.ggraver.DPlib;

import org.apache.commons.io.FileUtils;
import org.ggraver.DPlib.Exception.AnalysisException;
import org.ggraver.DPlib.Exception.CompileException;

import java.io.File;
import java.io.IOException;

import static org.apache.commons.io.FilenameUtils.removeExtension;


// generates .class files and analyses performance of user solution
class ClassAnalyser
{

    private final int PERF_INSTR_LINE = 5;
    private final String TEMP_FILENAME = "temp.txt";
    private File classFile;
    private String output = "test_output";
    private long instructionCount;
    private long executionTime;

    ClassAnalyser(File sourceFile)
    throws AnalysisException
    {
        try
        {
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
            executionTime = execute(perfStat);
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
    }

    private long execute(ProcessBuilder pb)
    throws IOException, InterruptedException
    {
        // remember to put timeout in waitFor()
        Process p;
        long start, end;
        start = System.currentTimeMillis();
        p = pb.start();
        p.waitFor();
        end = System.currentTimeMillis();
        executionTime = end - start;
        if (executionTime < 0)
        {
            throw new Error("Execution time should not be less than 0.");
        }
        return executionTime;
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
        build.redirectErrorStream(true);
        return build;
    }

    private File tempFile(String fileName, File dir)
    throws IOException
    {
        File temp = new File(dir + "/" + fileName);
        System.out.println("Temp file name: " + temp.getPath());
        temp.deleteOnExit();
        return temp;
    }

    // get the line containing the instruction count from the perf stat output stream and return it as a long
    private long fetchInstructionCount(File f, int lineNum)
    throws IOException, NumberFormatException
    {

        String instructionsString;
        long instructions;

        instructionsString = FileUtils.readLines(f, "UTF-8").get(lineNum)
                                      .replaceAll(" ", "")
                                      .replaceAll("instructions:u", "")
                                      .replaceAll(",", "");

        instructions = Long.parseLong(instructionsString);
        return instructions;

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
            build.inheritIO();
            p = build.start();
            result = p.waitFor();
        }
        catch (IOException | InterruptedException e)
        {
            throw new CompileException(e);
        }
        if (result != 0)
        {
            throw new CompileException("Could not compile .java file. Please ensure your .java file is valid.");
        }
        System.out.println(sourceFile.getName() + " compiled successfully.");
        try
        {
            this.classFile = new File(sourceFile.getParent() + "/" + removeExtension(sourceFile.getName()) + ".class");
        }
        catch (NullPointerException npe)
        {
            throw new CompileException("Could not create new file.");
        }
        if (!this.classFile.exists())
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

}
