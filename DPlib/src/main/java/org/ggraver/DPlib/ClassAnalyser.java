package org.ggraver.DPlib;

import java.io.File;
import java.io.IOException;

import java.lang.Process;
import java.lang.NumberFormatException;

import org.apache.commons.io.FileUtils;

import static org.apache.commons.io.FilenameUtils.removeExtension;

import org.ggraver.DPlib.Exception.CompileException;
import org.ggraver.DPlib.Exception.AnalysisException;


// generates .class files and analyses performance of user solution
class ClassAnalyser {

    private final int PERF_INSTR_LINE = 5;
    private final int INSTR_WHITESPACE_INDENT = 7;
    private final String TEMP_FILENAME = "log.txt";

    private File javaFile;
    private long instructionCount;

    private long executionTime;

    public ClassAnalyser() {}

    ClassAnalyser(File javaFile, String methodName) {

        this.javaFile = javaFile;

    }

    // analyse the compiled .class file for performance
    void analyse(File classFile) throws AnalysisException {

        File dir;
        File log;

        try {
            dir = new File(classFile.getParent());
            log = tempFile(TEMP_FILENAME, dir);
        } catch (IOException ioe) {
            throw new AnalysisException(ioe);
        }

        ProcessBuilder perfStat = buildPerfStat(
                log.getName(),
                removeExtension(classFile.getName()),
                dir
        );
        Process p;
        long start = 0;
        long end = 0;

        try {
            // remember to put timeout in waitFor()
            start = System.currentTimeMillis();
            p = perfStat.start();
            p.waitFor();
            end = System.currentTimeMillis();
        } catch (Exception e) {
            throw new AnalysisException(e);
        }

        try {
            instructionCount = fetchInstructionCount(log, PERF_INSTR_LINE);
        } catch (Exception e) {
            throw new Error(e);
        }

        executionTime = end - start;

        if(executionTime < 0) {
            throw new Error("Execution time should not be less than 0.");
        }

        System.out.println("[INSTRUCTIONS] " + instructionCount);
        System.out.println("[TIME] " + executionTime + "ms");

    }

    private ProcessBuilder buildPerfStat(String logFileName, String classFileName, File dir) {

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

    private File tempFile(String fileName, File dir) throws IOException {

        File temp = new File(dir + "/" + fileName);
        System.out.println("Temp file name: " + temp.getPath());
        temp.deleteOnExit();

        if (!temp.createNewFile()) {
            throw new IOException("Temp file \"" + TEMP_FILENAME + "\" already exists.");
        }

        if (!temp.exists()) {
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
    File compile(File file) throws CompileException {

        System.out.println("Compiling file: " + file.getName());

        // found at https://stackoverflow.com/questions/8496494/running-command-line-in-java
        Process p;
        int result;

        try {
            ProcessBuilder build = new ProcessBuilder("javac", file.getPath());
            build.inheritIO();
            p = build.start();
            result = p.waitFor();
        } catch (Exception e) {
            throw new CompileException(e);
        }

        if (result != 0) {
            throw new CompileException("Could not compile .java file. Please ensure your .java file is valid.");
        }

        System.out.println(file.getName() + " compiled successfully.");
        File classFile;

        try {
            classFile = new File(file.getParent() + "/" + removeExtension(javaFile.getName()) + ".class");
        } catch (NullPointerException npe) {
            throw new CompileException(npe);
        }

        if (!classFile.exists()) {
            throw new CompileException("Could not find .class file.");
        }

        return classFile;

    }

    public long getInstructionCount() {
        return instructionCount;
    }

    public long getExecutionTime() {
        return executionTime;
    }

}
