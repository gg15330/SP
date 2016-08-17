package org.dplib;

import org.dplib.exception.CompileException;

import java.io.*;

/**
 * Created by george on 16/08/16.
 */
class SourceCompiler
extends SubProcess
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
            redirectInputStream(p.getInputStream());
            redirectInputStream(p.getErrorStream());
            if (p.waitFor() != 0) { throw new CompileException("Could not compile .java file. Please ensure your .java file is valid."); }
        }
        catch (IOException | InterruptedException e) { throw new CompileException(e); }

        File classFile = new File(sourceFile.getParent() + "/" + className + ".class");
        if (!classFile.exists()) { throw new CompileException("Could not find .class file: " + className); }
        System.out.println(sourceFile.getName() + " compiled successfully.");

        return classFile;
    }

}
