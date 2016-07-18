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

// remember to include file checks/exceptions

// generates .class files and analyses performance of user solution
public class ClassAnalyser {

    private File file;
    private String methodName;

    public ClassAnalyser() {}

    public ClassAnalyser(File file, String methodName) {
        this.file = file;
        this.methodName = methodName;
    }

    // analyse the compiled .class file for performance
    public void analyse(File file, String methodName) {

        System.out.println("---[" + file.getName().toUpperCase() + "]---");
        compile(file);
        // ClassReader cr = null;
        //
        // try {
        //     File classFile = new File("sample/fib/fib.class");
        //     FileInputStream fis = new FileInputStream(classFile);
        //     cr = new ClassReader(fis);
        //     fis.close();
        // }
        // catch(Exception e) {
        //     e.printStackTrace();
        // }

        // System.out.println("Class name: " + cr.getClassName());
        // ClassNode cn = new ClassNode();
        // cr.accept(cn, 0);
        //
        // ArrayList<MethodNode> mnList = new ArrayList<MethodNode>(cn.methods);
        // System.out.println("File: " + file.getName());
        // for(MethodNode mn : mnList) {
        //     if(mn.name.equals(methodName)) {
        //         System.out.println("mn.maxStack: " + mn.maxStack);
        //         CodeSizeEvaluator cse = new CodeSizeEvaluator(null);
        //         mn.accept(cse);
        //         System.out.println("Max size: " + cse.getMaxSize());
        //     }
        // }

        Process p = null;
        String f = FilenameUtils.removeExtension(file.getName());

        try {

            File dir = new File("sample/fib");
            File temp = new File(dir + "/temp.txt");
            temp.deleteOnExit();
            System.out.println("Temp file name: " + temp.getPath());
            temp.createNewFile();

            if(!temp.exists()) {
                throw new Error("temp file does not exist.");
            }

            ProcessBuilder build = new ProcessBuilder("perf", "stat", "-e", "instructions:u", "-o", temp.getName(), "java", f);
            build.directory(dir);
            build.redirectErrorStream(true);

            System.out.println("Executing: " + build.command());
            p = build.start();
            p.waitFor();

            long i = fetchInstructionCount(temp, 5);
            System.out.println("i = " + i);

        }
        catch(Exception e) {
            e.printStackTrace();
        }

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

        String instructionsString = line.split(" ", 0)[7];

        long instructions = 0;

        try {
            instructions = Long.parseLong(instructionsString.replaceAll(",", ""));
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return instructions;
    }

    private File tempFile(String prefix, String suffix) {

        File file = null;

        try {
            file = new File(prefix + suffix);
            // file.deleteOnExit();
        }
        catch(Exception e) {
            throw new Error("Could not create temp file.");
        }
        System.out.println("Returning file: " + file.getPath());
        return file;

    }

    // compile the user-submitted .java file for performance analysis
    private void compile(File file) {

        // found at https://stackoverflow.com/questions/8496494/running-command-line-in-java
        Process p = null;
        System.out.println("Compiling file: " + file.getName());

        try {
            ProcessBuilder build = new ProcessBuilder("javac", file.getPath());
            build.inheritIO();
            p = build.start();
        } catch(Exception e) {
            e.printStackTrace();
        }

        int result = 0;

        try {
            result = p.waitFor();
        } catch(Exception e) {
            e.printStackTrace();
        }

        if(result != 0) {
            throw new Error("Could not compile .java file. Please ensure your .java file is valid.");
        }

        System.out.println(file.getName() + " compiled successfully.");

    }

    public void setFile(File file) {
        this.file = file;
    }

}
