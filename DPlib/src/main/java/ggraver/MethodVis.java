package ggraver;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

// goes through source code of a particular method and identifies each statement
// adapted from https://github.com/javaparser/javaparser/wiki/Manual
public class MethodVis extends VoidVisitorAdapter<Object> {

    @Override
    public void visit(MethodDeclaration n, Object arg) {

        System.out.println("NAME: " + n.getName());
        System.out.println("DECL: " + n.getDeclarationAsString());
        BlockStmt bs = n.getBody();

        for(Statement s : bs.getStmts()) {
            process(s);
        }

        // this doesn't seem to do anything but it was in the manual
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
