package org.ggraver.DPlib;

import java.util.List;
import java.util.ArrayList;

import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.BlockStmt;

public class Method {

    private String name;
    private List<AnnotationExpr> annotations;
    private BlockStmt statements;

    public Method(String name, List<AnnotationExpr> annotations, List<Statement> statements) {

        this.name = name;
        this.annotations = new ArrayList<AnnotationExpr>();

    }

    public String name() {
        return name;
    }

    public List<AnnotationExpr> annotations() {
        return annotations;
    }

    public BlockStmt statements() {
        return statements;
    }

}
