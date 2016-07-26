package org.ggraver.DPlib;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.ggraver.DPlib.Exception.AnalysisException;

// analyses source code for invalid solutions/errors
class SourceAnalyser {

    private String methodName;
    private int recursiveCallLineNo;
    private CompilationUnit cu;

    // construct a new SourceAnalyser with a method declaration to check against the source file
    SourceAnalyser(File file, String methodName) throws IOException, ParseException {

        this.cu = parse(file);
        this.methodName = methodName;

    }

    private CompilationUnit parse(File file) throws ParseException, IOException {

        // put FileInputStream in Main module -
        // can control file stream for both modules that way
        FileInputStream fis = new FileInputStream(file);
        CompilationUnit compilationUnit = JavaParser.parse(fis);
        fis.close();

        return compilationUnit;

    }

//     analyse the user-submitted .java file for code errors/invalid solutions (e.g. recursion)
    void analyse(CompilationUnit cu) throws AnalysisException {

//        MethodDeclaration methodToAnalyse = findMethod();
//        checkMethodProperties(methodToAnalyse);

    }

    MethodDeclaration findMethod(String methodName) throws AnalysisException {

        MV mv = new MV();
        mv.visit(cu, null);

        for (MethodDeclaration md : mv.methods()) {
            if (md.getName().equals(methodName)) {
                return md;
            }
        }

        throw new AnalysisException("expected MethodDeclaration \"" + methodName
                + "\" does not exist in source file.");

    }

    // checks method properties to ensure the submitted method is the same as the template
    void checkMethodProperties(MethodDeclaration expected, MethodDeclaration actual) throws AnalysisException {

        if (!actual.getType().equals(expected.getType())) {
            throw new AnalysisException("Wrong method Type." +
                    "\nExpected: " + expected.getType() +
                    " Actual: " + actual.getType());
        }

        if (!actual.getParameters().equals(expected.getParameters())) {
            throw new AnalysisException("Wrong method Parameters." +
                    "\nExpected: " + expected.getParameters() +
                    " Actual: " + actual.getParameters());
        }

    }

    // recursively check all nodes within the method for recursive function calls,
    // throw an exception if one is found
    void isRecursive(Node node, String name) throws AnalysisException {

        if (node instanceof MethodCallExpr) {

            MethodCallExpr mce = (MethodCallExpr) node;

            if (mce.getName().equals(name)) {
                throw new AnalysisException("Recursive method call \"" + mce.getName() +
                        "\" found at line " + mce.getBeginLine());
            }

        }

        for (Node n : node.getChildrenNodes()) {
            isRecursive(n, name);
        }

    }

    public boolean isRecursive(Node node) {

        if(node instanceof MethodCallExpr) {

            MethodCallExpr mce = (MethodCallExpr) node;

            if(mce.getName().equals(methodName)) {
                recursiveCallLineNo = mce.getBeginLine();
                return true;
            }

        }

        for(Node child : node.getChildrenNodes()) {
            if(isRecursive(child)) {
                return true;
            }
        }

        return false;

    }

    // To do: convert to check for duplicate method names/parameters/type
    private boolean duplicateAnnotations(MarkerAnnotationExpr methodAnnotation, List<AnnotationExpr> annotations) {

        int count = 0;

        for (AnnotationExpr ae : annotations) {

            if (ae.getName().equals(methodAnnotation.getName())) {
                count++;

                if (count > 1) {
                    return true;
                }

            }

        }

        return false;

    }

    public CompilationUnit getCompilationUnit() {
        return cu;
    }

    public int getRecursiveCallLineNo() {
        return recursiveCallLineNo;
    }

    // private visitor class to extract method data for source file and insert it into the method list
    private class MV extends VoidVisitorAdapter<Object> {

        private List<MethodDeclaration> methods = new ArrayList<>();

        @Override
        public void visit(MethodDeclaration n, Object arg) {

            methods.add(n);

        }

        List<MethodDeclaration> methods() {
            return methods;
        }

    }

}
