package ggraver;

import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.ForStmt;

// adapted from https://github.com/javaparser/javaparser/wiki/Manual
public class MethodVis extends VoidVisitorAdapter<Object> {

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

        // super.visit(n, arg);

    }

    // process each Statement depending on type
    private void process(Statement s) {

        System.out.println(
                        "STATEMENT: \n" +
                        "Begin line: " + s.getBeginLine() + "\n" +
                        s + "\n" +
                        "End line: " + s.getEndLine()
        );

        if(s instanceof ForStmt) {
            System.out.println("FOR STATEMENT: ");
            System.out.println("INIT: " + ((ForStmt) s).getInit());
            System.out.println("COMPARE: " + ((ForStmt) s).getCompare());
            System.out.println("UPDATE: " + ((ForStmt) s).getUpdate());
        }

    }

}
