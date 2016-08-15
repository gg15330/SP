package org.DPlib;

import org.DPlib.Exception.AnalysisException;
import org.DPlib.Exception.CompileException;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


// generates .class files and analyses performance
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
    Result analyse(String[] args)
    throws AnalysisException
    {
        if (!classFile.exists() || classFile.length() == 0)
        {
            throw new AnalysisException(".class file does not exist or is empty.");
        }

        List<String> commands = new ArrayList<>();
        commands.add("java");
        commands.add(className);
        commands.addAll(Arrays.asList(args));

        ProcessBuilder build = new ProcessBuilder(commands);
        build.directory(classFile.getParentFile());
        build.redirectErrorStream(true);
//        build.inheritIO();

        String output;
        long start, end;

        try
        {
            start = System.currentTimeMillis();
            Process p = build.start();
            p.waitFor();
            end = System.currentTimeMillis();
            if ((end - start) < 0) { throw new Error("Execution time should not be less than 0."); }
            output = fetchOutput(p.getInputStream());
        }
        catch (IOException | InterruptedException e)
        {
            throw new AnalysisException(e);
        }

        Result result = new Result();
        result.setInput(args);
        result.setOutput(output);
        result.setExecutionTime(end - start);

        return result;
    }

    private String fetchOutput(InputStream inputStream)
    throws IOException
    {
        InputStreamReader isr = new InputStreamReader(inputStream);
        BufferedReader br = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null) { sb.append(line); }
        if(sb.length() == 0) { throw new IOException("Output is empty."); }

        isr.close();
        br.close();

        return sb.toString();
    }

    // compile the user-submitted .java file for performance analysis
    private void compile(File sourceFile)
    throws CompileException
    {
        if (sourceFile == null) { throw new CompileException(".java file should not be null."); }
        System.out.println("Compiling file: " + sourceFile.getPath());
        Process p;
        int result;

        try
        {
            ProcessBuilder build = new ProcessBuilder("javac", sourceFile.getPath());
            p = build.start();
            new CompileStream(p.getInputStream()).start();
            new CompileStream(p.getErrorStream()).start();
            result = p.waitFor();
        }
        catch (IOException | InterruptedException e) { throw new Error(e); }

        if (result != 0)
        {
            throw new CompileException("Could not compile .java file. Please ensure your .java file is valid.");
        }

        classFile = new File(sourceFile.getParent() + "/" + className + ".class");

        if (!classFile.exists())
        {
            throw new CompileException("Could not find .class file.");
        }
        System.out.println(sourceFile.getName() + " compiled successfully.");
    }

//    adapted from http://www.javaworld.com/article/2071275/core-java/when-runtime-exec---won-t.html?page=2
    private class CompileStream extends Thread
    {
        private InputStream inputStream;

        CompileStream(InputStream inputStream)
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

                br.close();
            }
            catch (IOException e) { throw new Error(e); }
        }

    }

}
