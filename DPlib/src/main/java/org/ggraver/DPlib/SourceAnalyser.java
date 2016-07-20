package org.ggraver.DPlib;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.ArrayList;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

// analyses source code for invalid solutions/errors
class SourceAnalyser {

    private CompilationUnit cu;

    private MethodDeclaration methodDeclaration;
    private MarkerAnnotationExpr methodAnnotation;
    private List<MethodDeclaration> methods = new ArrayList<>();

    // construct a new SourceAnalyser with default annotation "Dynamic"
    SourceAnalyser() {

        this.methodAnnotation = new MarkerAnnotationExpr();
        methodAnnotation.setName(new NameExpr("Dynamic"));

    }

    // construct a new SourceAnalyser with a method declaration to check against the source file
    public SourceAnalyser(String methodName) {

        this.methodDeclaration = new MethodDeclaration();
        this.methodDeclaration.setName(methodName);
        // this.methodDeclaration.setType();
        // this.methodDeclaration.setParameters();

    }

    void parse(File file) throws Exception {

        // put FileInputStream in Main module - can control file stream for
        // both modules that way
        FileInputStream fis = new FileInputStream(file);
        cu = JavaParser.parse(fis);
        fis.close();

    }

    // analyse the user-submitted .java file for code errors/invalid solutions (e.g. recursion)
    void analyse() {

        MV mv = new MV();
        mv.visit(cu, null);

        try {
//            checkAnnotation(methodAnnotation);
        }
        catch(Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

//        MethodDeclaration md = assignMethod(methodAnnotation);
//        MethodAnalyser ma = new MethodAnalyser(md);
//        ma.analyse();

    }

    private MethodDeclaration assignMethod(MarkerAnnotationExpr methodAnnotation) {

        MethodDeclaration methodDeclaration = null;

        for(MethodDeclaration md : methods) {

            List<AnnotationExpr> annotations = md.getAnnotations();

            if(!annotations.isEmpty()) {
                AnnotationExpr ae = annotations.get(0);

                if(ae.getName().equals(methodAnnotation.getName())) {
                    methodDeclaration = md;
                }

            }

        }

        if(methodDeclaration == null) {
            throw new Error("methodDeclaration should not be null.");
        }

        return methodDeclaration;

    }

    // To do: convert this method to check method name, parameters, type
    // checks annotation for method to be analysed exists and is unique
    private void checkAnnotation(MarkerAnnotationExpr methodAnnotation) throws Exception {

        List<AnnotationExpr> annotations = new ArrayList<>();

        for(MethodDeclaration md : methods) {
            annotations.addAll(md.getAnnotations());
        }

        if(!annotations.contains(methodAnnotation)) {
            throw new Exception("Method with required annotation \"@" + methodAnnotation.getName() + "\" not found in source file.");
        }

        if(duplicateAnnotations(methodAnnotation, annotations)) {
            throw new Exception("Required annotation \"@" + methodAnnotation.getName() +
                                "\" exists for multiple methods. Please amend source file.");
        }

    }

    // To do: convert to check for duplicate method names/parameters/type
    private boolean duplicateAnnotations(MarkerAnnotationExpr methodAnnotation, List<AnnotationExpr> annotations) {

        int count = 0;

        for(AnnotationExpr ae : annotations) {

            if(ae.getName().equals(methodAnnotation.getName())) {
                count++;

                if(count > 1) {
                    return true;
                }

            }

        }

        return false;

    }

    // private visitor class to extract method data for source file and insert it into the method list
    private class MV extends VoidVisitorAdapter<Object> {

        @Override
        public void visit(MethodDeclaration n, Object arg) {

            methods.add(n);

        }

    }

}
