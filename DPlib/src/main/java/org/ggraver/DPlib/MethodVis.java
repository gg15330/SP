// This class is deprecated - has been incorporated into the SourceAnalyser class

package org.ggraver.DPlib;

import java.util.Map;
import java.util.HashMap;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

// goes through source code of a particular method and identifies each statement
// adapted from https://github.com/javaparser/javaparser/wiki/Manual
public class MethodVis extends VoidVisitorAdapter<Object> {

    private Map<String, BlockStmt> methodList;

    public MethodVis() {

        methodList = new HashMap<String, BlockStmt>();

    }

    // get information from all getMethods in the source file
    @Override
    public void visit(MethodDeclaration n, Object arg) {

        methodList.put(n.getName(), n.getBody());

        // if(n.getName().equals(method)) {
        //     System.out.println("NAME: " + n.getName());
        //     System.out.println("DECL: " + n.getDeclarationAsString());
        //     BlockStmt bs = n.getBody();
        //
        //     for(Statement s : bs.getStmts()) {
        //         process(s);
        //     }
        //
        // }

    }

    // checks that the method to be analysed exists in the source file
    public void checkMethodExists(String method) throws Exception {

        // for(String m : methodList) {
        //     if(m.equals(method)) {
        //         return;
        //     }
        // }

        throw new Exception("Required method \"" + method + "\" does not exist. " +
        "\nPlease check source file.\n");

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

    public Map<String, BlockStmt> getMethods() {
        return methodList;
    }

}
