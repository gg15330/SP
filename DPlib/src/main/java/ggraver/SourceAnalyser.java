package ggraver;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.ArrayList;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

// analyses source code for invalid solutions/errors
public class SourceAnalyser {

    private File file;
    private NormalAnnotationExpr methodAnnotation;
    private List<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();

    // construct a new SourceAnalyser with references to the .java file
    // and the annotation for the method to be analysed
    public SourceAnalyser(File file, String annotation) {

        this.file = file;
        this.methodAnnotation = new NormalAnnotationExpr();
        methodAnnotation.setName(new NameExpr(annotation));

    }

    // analyse the user-submitted .java file for code errors/invalid solutions (e.g. recursion)
    public void analyse() throws Exception {

        FileInputStream fis = new FileInputStream(file);
        CompilationUnit cu = JavaParser.parse(fis);
        fis.close();

        MV mv = new MV();
        mv.visit(cu, null);

        try {
            MethodDeclaration md = findMethod();
            System.out.println(md.getBody());
        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }

    // check that the correct annotation exists for a method in the source file
    // and that it is unique
    private MethodDeclaration findMethod() throws Exception {
        System.out.println(methodAnnotation.getName());

        boolean found = false;
        MethodDeclaration methodDeclaration = null;

        for(MethodDeclaration md : methods) {
            for(AnnotationExpr ae : md.getAnnotations()) {
                if(ae instanceof MarkerAnnotationExpr
                && ae.getName().equals(methodAnnotation.getName())) {
                    System.out.println("MarkerAnnotation: " + ae.getName());
                    if(found == true) {
                        throw new Exception("Annotation already exists.");
                    }
                    else {
                        methodDeclaration = md;
                        found = true;
                    }
                }
            }
        }

        if(found == true) {
            return methodDeclaration;
        }
        else {
            throw new Exception("Required annotation \"" + methodAnnotation.getName() + "\" not present in source file.");
        }

    }

    // private class to extract method data for source file and insert it into the method list
    private class MV extends VoidVisitorAdapter<Object> {

        @Override
        public void visit(MethodDeclaration n, Object arg) {

            methods.add(n);

        }

    }

}
