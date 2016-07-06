package ggraver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

// analyses source code for invalid solutions/errors
public class SourceAnalyser {

    CompilationUnit cu;
    private MarkerAnnotationExpr methodAnnotation;
    private List<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();

    // construct a new SourceAnalyser with default annotation "Dynamic"
    public SourceAnalyser() {

        this.methodAnnotation = new MarkerAnnotationExpr();
        methodAnnotation.setName(new NameExpr("Dynamic"));

    }

    // construct a new SourceAnalyser with references to the .java file
    // and a custom annotation for the method to be analysed
    public SourceAnalyser(String annotation) {

        this.methodAnnotation = new MarkerAnnotationExpr();
        methodAnnotation.setName(new NameExpr(annotation));

    }

    public void parse(File file) throws Exception {

        FileInputStream fis = new FileInputStream(file);
        cu = JavaParser.parse(fis);
        fis.close();

    }

    // analyse the user-submitted .java file for code errors/invalid solutions (e.g. recursion)
    public void analyse() {

        MV mv = new MV();
        mv.visit(cu, null);

        try {
            checkAnnotation(methodAnnotation);
        }
        catch(Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        MethodAnalyser ma = new MethodAnalyser();
        MethodDeclaration md = assignMethod(methodAnnotation);
        
        if(ma.isRecursive(md)) {
            System.out.println("Recursion detected!");
        }

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

    // checks annotation for method to be analysed exists and is unique
    private void checkAnnotation(MarkerAnnotationExpr methodAnnotation) throws Exception {

        List<AnnotationExpr> annotations = new ArrayList<AnnotationExpr>();

        for(MethodDeclaration md : methods) {
            for(AnnotationExpr ae : md.getAnnotations()) {
                annotations.add(ae);
            }
        }

        if(!annotations.contains(methodAnnotation)) {
            throw new Exception("Method with required annotation \"@" + methodAnnotation.getName() + "\" not found in source file.");
        }

        if(duplicateAnnotations(methodAnnotation, annotations)) {
            throw new Exception("Required annotation \"@" + methodAnnotation.getName() +
                                "\" exists for multiple methods. Please amend source file.");
        }

    }

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
