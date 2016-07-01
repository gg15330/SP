package ggraver;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.body.BodyDeclaration;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.MethodNode;

import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.Runtime;
import java.lang.Process;

public class Main {

    public static void main(String[] args) throws Exception {

        try {
            Main cp = new Main();
            cp.run();
        }
        catch(Exception e) {
            System.err.println("Class not found.");
        }

    }

    private void run() {
        // analyseSource();
        analyseClass();
    }

    private void analyseClass() {
        compile();
        MethodNode mn = new MethodNode(Opcodes.ASM5);
        System.out.println("Signature: " + mn.signature);
    }

    private void compile() {
        // found at https://stackoverflow.com/questions/8496494/running-command-line-in-java
        Process p = null;
        try {
            System.out.println(new File(".").getCanonicalPath());
            ProcessBuilder build = new ProcessBuilder("javac", "FileToParse.java");
            File dir = new File("./sample");
            build.directory(dir);
            // build.redirectErrorStream(true);
            build.inheritIO();

            p = build.start();

            // found somewhere else
            // redirect output
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line=null;
            while((line=input.readLine()) != null) {
                System.out.println(line);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        int result = 0;
        try {
            result = p.waitFor();
        } catch(Exception e) {
            System.out.println("Interrupted: ");
            e.printStackTrace();
        }
        if(result != 0) {
            throw new Error("Could not compile .java file. Please ensure your .java file is valid.");
        }
        System.out.println(".java file compiled successfully.");
    }

    private void analyseSource() {
        // creates an input stream for the file to be parsed
        FileInputStream in;
        try {
            in = new FileInputStream("../sample/FileToParse.java");
        }
        catch(Exception e) {
            in = null;
            e.printStackTrace();
        }

        CompilationUnit cu;
        // parse the file
        try {
            cu = JavaParser.parse(in);
            in.close();
        } catch(Exception e) {
            cu = null;
            System.out.println("JavaParser class not found for some reason.");
            System.exit(1);
        }

        MethodVis mv = new MethodVis();
        mv.visit(cu, null);
    }

    // https://github.com/javaparser/javaparser/wiki/Manual
    private class MethodVis extends VoidVisitorAdapter<Object> {

        @Override
        public void visit(MethodDeclaration n, Object arg) {
            here you can access the attributes of the method.
            this method will be called for all methods in this
            CompilationUnit, including inner class methods
            System.out.println("NAME: " + n.getName());
            System.out.println("DECL: " + n.getDeclarationAsString());
            BlockStmt bs = n.getBody();
            for(Statement s : bs.getStmts()) {
                process(s);
            }
            // super.visit(n, arg);
        }

        private void process(Statement s) {
            if(s instanceof ForStmt) {
                System.out.println("FOR STATEMENT: ");
                System.out.println("INIT: " + ((ForStmt) s).getInit());
                System.out.println("COMPARE: " + ((ForStmt) s).getCompare());
                System.out.println("UPDATE: " + ((ForStmt) s).getUpdate());
                System.out.println(
                        "STATEMENT: \n" +
                                "Begin line: " + s.getBeginLine() + "\n" +
                                s + "\n" +
                                "End line: " + s.getEndLine()
                );
            }
            else {
                System.out.println(
                        "STATEMENT: \n" +
                                "Begin line: " + s.getBeginLine() + "\n" +
                                s + "\n" +
                                "End line: " + s.getEndLine()
                );
            }
        }
    }
}
