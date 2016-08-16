package org.dplib;

import org.dplib.exception.CompileException;

import java.io.*;
import java.net.Inet4Address;

/**
 * Created by george on 16/08/16.
 */
public class SourceCompiler
implements SubProcess
{

    // compile the user-submitted .java file for performance analysis
    File compile(File sourceFile, String className)
    throws CompileException
    {
        System.out.println("Compiling file: " + sourceFile.getPath());

        Process p;
        try
        {
            p = subProcess(sourceFile.getParentFile(), "javac", sourceFile.getName());
            new SubProcessInputStream(p.getInputStream()).start();
            new SubProcessInputStream(p.getErrorStream()).start();
            if (p.waitFor() != 0) { throw new CompileException("Could not compile .java file. Please ensure your .java file is valid."); }
        }
        catch (IOException | InterruptedException e) { throw new CompileException(e); }

        File classFile = new File(sourceFile.getParent() + "/" + className + ".class");
        if (!classFile.exists()) { throw new CompileException("Could not find .class file."); }
        System.out.println(sourceFile.getName() + " compiled successfully.");

        return classFile;
    }

    //    adapted from http://www.javaworld.com/article/2071275/core-java/when-runtime-exec---won-t.html?page=2
    private class SubProcessInputStream extends Thread
    {
        private InputStream inputStream;

        SubProcessInputStream(InputStream inputStream)
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
