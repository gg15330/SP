package org.ggraver.DPlib;

import java.io.File;
import java.io.IOException;

import java.lang.Process;
import java.lang.NumberFormatException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.FileUtils;

import org.ggraver.DPlib.Exception.CompileException;

// generates .class files and analyses performance of user solution
class ClassAnalyser {

    private final int PERF_INSTR_LINE = 5;
    private final int INSTR_WHITESPACE_INDENT = 7;
    private final String TEMP_FILENAME = "log.txt";

    private File javaFile;

    public ClassAnalyser() {}

    ClassAnalyser(File javaFile, String methodName) {

        this.javaFile = javaFile;

    }

    boolean returnsTrue() { return true; }

    // analyse the compiled .class file for performance
    void analyse() {

        try {
            compile(javaFile);
        }
        catch(CompileException ce) {
            ce.printStackTrace();
            System.exit(1);
        }

        String fname = FilenameUtils.removeExtension(javaFile.getName());
        File dir;
        File log;

        try {
            dir = new File(javaFile.getParent());
            log = tempFile(TEMP_FILENAME, dir);
        }
        catch(IOException ioe) {
            throw new Error(ioe);
        }

        ProcessBuilder build = new ProcessBuilder("perf", "stat", "-e", "instructions:u", "-o", log.getName(), "java", fname);
        build.directory(dir);
        build.inheritIO();
        build.redirectErrorStream(true);

        Process p;
        long start = 0;
        long end = 0;

        try {
            p = build.start();
            // remember to put timeout in waitFor()
            start = System.currentTimeMillis();
            p.waitFor();
            end = System.currentTimeMillis();
        }
        catch(Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        long numOfInstructions;

        try {
            numOfInstructions = fetchInstructionCount(log, PERF_INSTR_LINE);
        }
        catch(Exception e) {
            throw new Error(e);
        }

        System.out.println("[INSTRUCTIONS] " + numOfInstructions);
        System.out.println("[TIME] " + (end - start) + "ms");

    }

    private File tempFile(String fileName, File dir) throws IOException {

        File temp = new File(dir + "/" + fileName);
        System.out.println("Temp file name: " + temp.getPath());
        temp.deleteOnExit();

        if(!temp.createNewFile()) {
            throw new IOException("Temp file \"" + TEMP_FILENAME + "\" already exists.");
        }

        if(!temp.exists()) {
            throw new IOException("Temp file \"" + TEMP_FILENAME + "\" was not created.");
        }

        return temp;

    }

    // get the line containing the instruction count from the perf stat output stream and return it as a long
    private long fetchInstructionCount(File f, int lineNum) throws IOException, NumberFormatException {

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
    private void compile(File file) throws CompileException {

        // found at https://stackoverflow.com/questions/8496494/running-command-line-in-java
        Process p;
        System.out.println("Compiling file: " + file.getName());
        int result;

        try {
            ProcessBuilder build = new ProcessBuilder("javac", file.getPath());
            build.inheritIO();
            p = build.start();
            result = p.waitFor();
        }
        catch(Exception e) {
            throw new Error(e);
        }

        if(result != 0) {
            throw new CompileException("Could not compile .java file. Please ensure your .java file is valid.");
        }

        System.out.println(file.getName() + " compiled successfully.");

    }

}
