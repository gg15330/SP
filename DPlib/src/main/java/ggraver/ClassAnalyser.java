package ggraver;

import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.IOException;

import java.lang.Process;
import java.lang.ProcessBuilder.Redirect;
import java.lang.NumberFormatException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.MemoryType;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.FileUtils;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.CodeSizeEvaluator;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.ClassReader;

import ggraver.Exception.CompileException;

// generates .class files and analyses performance of user solution
public class ClassAnalyser {

    private final int PERF_INSTR_LINE = 5;
    private final int INSTR_WHITESPACE_INDENT = 7;
    private final String TEMP_FILENAME = "log.txt";

    private File javaFile;
    private String methodName;

    public ClassAnalyser() {}

    public ClassAnalyser(File javaFile, String methodName) {

        this.javaFile = javaFile;
        this.methodName = methodName;

    }

    // analyse the compiled .class file for performance
    public void analyse() {

        try {
            compile(javaFile);
        }
        catch(CompileException ce) {
            ce.printStackTrace();
            System.exit(1);
        }

        String fname = FilenameUtils.removeExtension(javaFile.getName());
        File dir, log = null;

        try {
            dir = new File(javaFile.getParent());
            log = tempFile(TEMP_FILENAME, dir);
        }
        catch(IOException ioe) {
            throw new Error(ioe);
        }

        ProcessBuilder build = new ProcessBuilder("perf", "stat", "-e", "instructions:u", "-o", log.getName(), "java", fname);
        build.directory(dir);
        build.redirectErrorStream(true);

        Process p = null;

        try {
            p = build.start();
            // remember to put timeout in waitFor()
            p.waitFor();
        }
        catch(Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        long i = 0;

        try {
            i = fetchInstructionCount(log, PERF_INSTR_LINE);
        }
        catch(Exception e) {
            throw new Error(e);
        }

        System.out.println("i = " + i);

    }

    private File tempFile(String fileName, File dir) throws IOException {

        File temp = new File(dir + "/" + fileName);
        System.out.println("Temp file name: " + temp.getPath());
        temp.deleteOnExit();
        temp.createNewFile();

        if(!temp.exists()) {
            throw new IOException("Temp file does not exist.");
        }

        return temp;

    }

    // get the line containing the instruction count from the perf stat output stream and return it as a long
    private long fetchInstructionCount(File f, int lineNum) throws IOException, NumberFormatException {

        String line = null;
        String instructionsString = null;
        long instructions = 0;

        line = FileUtils.readLines(f).get(lineNum);
        instructionsString = line.split(" ", 0)[INSTR_WHITESPACE_INDENT];
        instructions = Long.parseLong(instructionsString.replaceAll(",", ""));

        return instructions;

    }

    // compile the user-submitted .java file for performance analysis
    private void compile(File file) throws CompileException {

        // found at https://stackoverflow.com/questions/8496494/running-command-line-in-java
        Process p = null;
        System.out.println("Compiling file: " + file.getName());
        int result = 0;

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

    public void setFile(File javaFile) {
        this.javaFile = javaFile;
    }

}
