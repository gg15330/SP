package ggraver;

import java.io.File;
import java.lang.Process;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;

// generates .class files and analyses performance of user solution
public class ClassAnalyser {

    // analyse the compiled .class file for performance
    private void analyseClass() {

        compile();
        MethodNode mn = new MethodNode(Opcodes.ASM5);
        System.out.println("Signature: " + mn.signature);

    }

    // compile the user-submitted .java file for performance analysis
    private void compile() {

        // found at https://stackoverflow.com/questions/8496494/running-command-line-in-java
        Process p = null;

        try {
            ProcessBuilder build = new ProcessBuilder("javac", "FileToParse.java");
            File dir = new File("./sample");
            build.directory(dir);
            build.inheritIO();
            p = build.start();
        } catch(Exception e) {
            e.printStackTrace();
        }

        int result = 0;

        try {
            result = p.waitFor();
        } catch(Exception e) {
            System.out.println("Interrupted: \n");
            e.printStackTrace();
        }

        if(result != 0) {
            throw new Error("Could not compile .java file. Please ensure your .java file is valid.");
        }

        System.out.println("FileToParse.java compiled successfully.");

    }

}
