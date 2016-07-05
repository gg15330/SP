package ggraver;

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
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

// analyses source code for invalid solutions/errors
public class SourceAnalyser {

    private File file;
    private MarkerAnnotationExpr methodAnnotation;
    private List<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();

    // construct a new SourceAnalyser with references to the .java file
    // and the annotation for the method to be analysed
    public SourceAnalyser(File file, String annotation) {

        this.file = file;
        this.methodAnnotation = new MarkerAnnotationExpr();
        methodAnnotation.setName(new NameExpr(annotation));

    }

    // analyse the user-submitted .java file for code errors/invalid solutions (e.g. recursion)
    public void analyse() throws Exception {

        FileInputStream fis = new FileInputStream(file);
        CompilationUnit cu = JavaParser.parse(fis);
        fis.close();

        MV mv = new MV();
        mv.visit(cu, null);

        MethodDeclaration md = findMethod();
        System.out.println(md.getBody());

    }

    // return the method to be analysed
    private MethodDeclaration findMethod() throws Exception {

        boolean found = false;
        MethodDeclaration methodDeclaration = null;

        for(MethodDeclaration md : methods) {

            for(AnnotationExpr ae : md.getAnnotations()) {

                if(ae instanceof MarkerAnnotationExpr
                && ae.getName().equals(methodAnnotation.getName())) {

                    if(found == true) {
                        throw new Exception("Required annotation \"@" + methodAnnotation.getName() +
                        "\" exists for multiple methods. Please amend source file.");
                    }

                    methodDeclaration = md;
                    found = true;
                }

            }
            
        }

        if(found == false) {
            throw new Exception("Method with required annotation \"@" + methodAnnotation.getName() + "\" not found in source file.");
        }

        if(methodDeclaration == null) {
            throw new Error("methodDeclaration should not be null.");
        }

        return methodDeclaration;

    }

    // private class to extract method data for source file and insert it into the method list
    private class MV extends VoidVisitorAdapter<Object> {

        @Override
        public void visit(MethodDeclaration n, Object arg) {

            methods.add(n);

        }

    }

}
