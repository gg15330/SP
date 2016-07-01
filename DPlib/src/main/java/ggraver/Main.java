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
import org.objectweb.asm.tree.MethodNode;

import java.io.File;
import java.io.FileInputStream;
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
        MethodNode cse = new MethodNode(Opcodes.ASM5);
        System.out.println("Signature: " + cse.signature);
    }

    private void compile() {
        try {
            ProcessBuilder build = new ProcessBuilder("javac", "../sample/FileToParse.java");
            build.redirectErrorStream(true);
            Process p = build.start();
        } catch(Exception e) {
            e.printStackTrace();
        }
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

    private class MethodVis extends VoidVisitorAdapter<Object> {

        @Override
        public void visit(MethodDeclaration n, Object arg) {
            // here you can access the attributes of the method.
            // this method will be called for all methods in this
            // CompilationUnit, including inner class methods
            System.out.println("NAME: " + n.getName());
            System.out.println("DECL: " + n.getDeclarationAsString());
            BlockStmt bs = n.getBody();
            for(Statement s : bs.getStmts()) {
                process(s);
            }
            super.visit(n, arg);
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
