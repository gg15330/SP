package org.dplib;

import org.dplib.exception.CompileException;

import java.io.File;
import java.io.IOException;

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
        if (sourceFile == null) { throw new CompileException(".java file should not be null."); }

        Process p;
        int result;

        try
        {
            ProcessBuilder build = new ProcessBuilder("javac", sourceFile.getPath());
            p = build.start();
//            new ClassAnalyser.SubProcessInputStream(p.getInputStream()).start();
//            new ClassAnalyser.SubProcessInputStream(p.getErrorStream()).start();
            result = p.waitFor();
        }
        catch (IOException | InterruptedException e) { throw new Error(e); }

        if (result != 0)
        {
            throw new CompileException("Could not compile .java file. Please ensure your .java file is valid.");
        }

        File classFile = new File(sourceFile.getParent() + "/" + className + ".class");

        if (!classFile.exists())
        {
            throw new CompileException("Could not find .class file.");
        }
        System.out.println(sourceFile.getName() + " compiled successfully.");

        return classFile;
    }

}
