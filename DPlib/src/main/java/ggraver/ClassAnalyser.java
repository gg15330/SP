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

// remember to include file checks/exceptions

// generates .class files and analyses performance of user solution
public class ClassAnalyser {

    private final int PERF_INSTR_LINE = 5;
    private final int INSTR_WHITESPACE_INDENT = 7;

    private File file;
    private String methodName;

    public ClassAnalyser() {}

    public ClassAnalyser(File file, String methodName) {
        this.file = file;
        this.methodName = methodName;
    }

    // analyse the compiled .class file for performance
    public void analyse() {

        try {
            compile(file);
        }
        catch(CompileException ce) {
            ce.printStackTrace();
        }

        String fname = FilenameUtils.removeExtension(file.getName());
        File dir, temp = null;

        try {
            dir = new File("sample/fib");
            temp = tempFile("temp.txt", dir);
        }
        catch(IOException ioe) {
            throw new Error(ioe);
        }

        ProcessBuilder build = new ProcessBuilder("perf", "stat", "-e", "instructions:u", "-o", temp.getName(), "java", fname);
        build.directory(dir);
        build.redirectErrorStream(true);

        Process p = null;

        try {
            p = build.start();
            p.waitFor();
        }
        catch(Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        long i = fetchInstructionCount(temp, PERF_INSTR_LINE);
        System.out.println("i = " + i);

    }

    private File tempFile(String fileName, File dir) throws IOException {

        File temp = new File(dir + "/" + fileName);
        System.out.println("Temp file name: " + temp.getPath());
        temp.deleteOnExit();
        temp.createNewFile();

        if(!temp.exists()) {
            throw new Error("temp file does not exist.");
        }

        return temp;

    }

    // get the line containing the instruction count from the perf stat output stream and return it as a long
    private long fetchInstructionCount(File f, int lineNum) {

        String line = null;

        try {
            line = FileUtils.readLines(f).get(lineNum);
        }
        catch(IOException ioe) {
            throw new Error(ioe);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        String instructionsString = line.split(" ", 0)[INSTR_WHITESPACE_INDENT];
        long instructions = 0;

        try {
            instructions = Long.parseLong(instructionsString.replaceAll(",", ""));
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return instructions;

    }

    // compile the user-submitted .java file for performance analysis
    private void compile(File file) throws CompileException {

        // found at https://stackoverflow.com/questions/8496494/running-command-line-in-java
        Process p = null;
        System.out.println("Compiling file: " + file.getName());

        try {
            ProcessBuilder build = new ProcessBuilder("javac", file.getPath());
            build.inheritIO();
            p = build.start();
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        int result = 0;

        try {
            result = p.waitFor();
        }
        catch(Exception e) {
            e.printStackTrace();
            result = 1;
        }

        if(result != 0) {
            throw new CompileException("Could not compile .java file. Please ensure your .java file is valid.");
        }

        System.out.println(file.getName() + " compiled successfully.");

    }

    public void setFile(File file) {
        this.file = file;
    }

}
