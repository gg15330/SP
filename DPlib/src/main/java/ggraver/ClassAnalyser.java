package ggraver;

import java.io.File;
import java.io.FileInputStream;
import java.lang.Process;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.ClassReader;

// generates .class files and analyses performance of user solution
public class ClassAnalyser {

    private File file;

    public ClassAnalyser() {}

    public ClassAnalyser(File file) {
        this.file = file;
    }

    // analyse the compiled .class file for performance
    public void analyse(File file) {

        compile(file);
        ClassReader cr = null;

        try {
            File classFile = new File("sample/fib/fib.class");
            FileInputStream fis = new FileInputStream(classFile);
            cr = new ClassReader(fis);
            fis.close();
        }
        catch(Exception e) {
            System.out.println("Problem with file.");
            e.printStackTrace();
        }
        System.out.println("Class name: " + cr.getClassName());

    }

    // compile the user-submitted .java file for performance analysis
    private void compile(File file) {

        // found at https://stackoverflow.com/questions/8496494/running-command-line-in-java
        Process p = null;

        System.out.println("Compiling file: " + file.getName());

// -classpath ../../target/DPlib-1.0-SNAPSHOT.jar ggraver/annotations/Dynamic.java

        try {
            System.out.println("File name: " + file.getName());
            System.out.println("File path: " + file.getPath());

            ProcessBuilder build = new ProcessBuilder("javac", file.getPath());
            // File dir = new File("sample/fib");
            // build.directory(dir);
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

}
