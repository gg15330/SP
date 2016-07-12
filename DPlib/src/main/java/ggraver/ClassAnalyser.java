package ggraver;

import java.io.File;
import java.io.FileInputStream;
import java.lang.Process;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FilenameUtils;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.MemoryType;

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
            ProcessBuilder build = new ProcessBuilder("perf stat -e instructions:u java", f);
            File dir = new File("sample/fib");
            build.directory(dir);
            build.inheritIO();
            build.redirectErrorStream(true);
            p = build.start();
            p.waitFor();

            // List<MemoryPoolMXBean> memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();
            //
            // for(MemoryPoolMXBean mp : memoryPoolMXBeans) {
            //     System.out.println("Bean name: " + mp.getName() + "\nType: " + mp.getType().toString());
            //     MemoryUsage pu = mp.getUsage();
            //     System.out.println("[USAGE] " + pu.getUsed());
            // }

            // Runtime runtime = Runtime.getRuntime();
            // // Run the garbage collector
            // // runtime.gc();
            // // Calculate the used memory
            // long memory = runtime.totalMemory() - runtime.freeMemory();
            // System.out.println("Used memory is bytes: " + memory);
            //

            //
            // Runtime runtime2 = Runtime.getRuntime();
            // // Run the garbage collector
            // // runtime2.gc();
            // // Calculate the used memory
            // long memory2 = runtime2.totalMemory() - runtime2.freeMemory();
            // System.out.println("Used memory is bytes: " + memory2);

        } catch(Exception e) {
            e.printStackTrace();
        }


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
