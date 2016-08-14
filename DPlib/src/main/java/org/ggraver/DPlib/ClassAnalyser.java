package org.ggraver.DPlib;

import org.apache.commons.io.FileUtils;
import org.ggraver.DPlib.Exception.AnalysisException;
import org.ggraver.DPlib.Exception.CompileException;

import java.io.*;


// generates .class files and analyses performance of user solution
class ClassAnalyser
{

    private String className;
    private File classFile;

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
    Result analyse(String input)
    throws AnalysisException
    {
        if (!classFile.exists() || classFile.length() == 0)
        {
            throw new AnalysisException(".class file does not exist or is empty.");
        }

        ProcessBuilder build = new ProcessBuilder("java", className, input);
        build.directory(classFile.getParentFile());

        // remember to put timeout in waitFor()
        Process p;
        String output;
        long executionTime;

        try
        {
            long start = System.currentTimeMillis();
            p = build.start();
            p.waitFor();
            long end = System.currentTimeMillis();
            executionTime = end - start;
            if ((executionTime) < 0) { throw new Error("Execution time should not be less than 0."); }
            output = fetchOutput(p.getInputStream());
        }
        catch (IOException | InterruptedException e)
        {
            throw new AnalysisException(e);
        }

        Result result = new Result();
        result.setInput(input);
        result.setOutput(output);
        result.setExecutionTime(executionTime);

        return result;
    }

    private String fetchOutput(InputStream inputStream)
    throws IOException
    {
        InputStreamReader isr = new InputStreamReader(inputStream);
        BufferedReader br = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null)
        {
            sb.append(line);
        }

        if(sb.length() == 0)
        {
            throw new IOException("Output is empty.");
        }

        return sb.toString();
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
            throw new CompileException("Could not compile .java file. Please ensure your .java file is valid.");
        }

        System.out.println(sourceFile.getName() + " compiled successfully.");
        classFile = new File(sourceFile.getParent() + "/" + className + ".class");

        if (!classFile.exists())
        {
            throw new CompileException("Could not find .class file.");
        }
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
