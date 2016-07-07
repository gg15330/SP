package ggraver;

import java.io.File;
import java.lang.Process;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;

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
        MethodNode mn = new MethodNode(Opcodes.ASM5);
        System.out.println("Signature: " + mn.signature);

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
            File f = new File("main/java/ggraver/ClassAnalyser.java");
            System.out.println("File path: " + f.getPath());

            Path currentRelativePath = Paths.get("");
            String s = currentRelativePath.toAbsolutePath().toString();
            System.out.println("Current relative path is: " + s);

            ProcessBuilder build = new ProcessBuilder("javac -cp /home/george/Documents/sp/DPlib/target/DPlib-1.0-SNAPSHOT.jar ggraver/annotations/Dynamic.java", file.getName());
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

        System.out.println("FileToParse.java compiled successfully.");

    }

}
