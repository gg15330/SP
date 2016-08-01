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
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.ggraver.DPlib.Exception.AnalysisException;

// this class analyses source code for invalid solutions/errors
// the analysis can be different depending on whether the .java file
// is submitted by the tutor or the student
class SourceAnalyser
{

    private String methodName;
    private int recursiveCallLineNo;
    private CompilationUnit cu;
    private MethodDeclaration methodDeclaration;
    private MethodCallExpr methodCall;
    private List<Object> methodInputValues;

    // construct a new SourceAnalyser with a method declaration to check against the source file
    SourceAnalyser(File file, String methodName)
    throws AnalysisException
    {

        try
        {
            parse(file);
        }
        catch (ParseException e)
        {
            throw new AnalysisException("Could not parse .java file.");
        }
        catch (IOException e)
        {
            throw new AnalysisException(e);
        }

        this.methodName = methodName;

    }

    private void parse(File file)
    throws ParseException, IOException
    {

        FileInputStream fis = new FileInputStream(file);
        cu = JavaParser.parse(fis);
        fis.close();

    }

    //    no parameterList - expected method name/list of expected method parameterList is generated
    void analyse()
    throws AnalysisException
    {

    }


    //    list of expected method parameterList is supplied - for checking student-submitted files
    void analyse(List<Parameter> expectedParams)
    throws AnalysisException
    {

    }

    MethodDeclaration findMethod(String methodName)
    throws AnalysisException
    {
        MethodDeclarationVisitor methodDeclarationVisitor = new MethodDeclarationVisitor();
        methodDeclarationVisitor.visit(cu, null);
        for (MethodDeclaration md : methodDeclarationVisitor.getMethods())
        {
            if (md.getName().equals(methodName))
            {
                return md;
            }
        }
        throw new AnalysisException("expected MethodDeclaration \"" + methodName
                                            + "\" does not exist in source file.");
    }

    MethodCallExpr findMethodCall(MethodDeclaration parentMethod, String methodCallName)
    throws AnalysisException
    {
        MethodCallExprVisitor methodCallExprVisitor = new MethodCallExprVisitor();
        methodCallExprVisitor.visit(parentMethod, null);
        for (MethodCallExpr mce : methodCallExprVisitor.getMethodCalls())
        {
            if (mce.getName().equals(methodCallName))
            {
                return mce;
            }
        }
        throw new AnalysisException("expected MethodDeclaration \"" + methodName
                                            + "\" does not exist in source file.");
    }

    // checks method properties to ensure the submitted method is the same as the tutor-defined template
    void checkMethodProperties(MethodDeclaration expected, MethodDeclaration actual)
    throws AnalysisException
    {

        if (!actual.getType().equals(expected.getType()))
        {
            throw new AnalysisException("Wrong method Type." +
                                                "\nExpected: " + expected.getType() +
                                                " Actual: " + actual.getType());
        }

        if (!actual.getParameters().equals(expected.getParameters()))
        {
            throw new AnalysisException("Method parameters do not match." +
                                                "\nExpected: " + expected.getParameters() +
                                                "\nActual: " + actual.getParameters());
        }

    }

    boolean isRecursive(Node node)
    {
        if (node instanceof MethodCallExpr)
        {
            MethodCallExpr mce = (MethodCallExpr) node;
            if (mce.getName().equals(methodName))
            {
                recursiveCallLineNo = mce.getBeginLine();
                return true;
            }
        }
        for (Node child : node.getChildrenNodes())
        {
            if (isRecursive(child))
            {
                return true;
            }
        }
        return false;
    }

    // To do: convert to check for duplicate method names/parameterList/type
    private boolean duplicateAnnotations(MarkerAnnotationExpr methodAnnotation, List<AnnotationExpr> annotations)
    {

        int count = 0;

        for (AnnotationExpr ae : annotations)
        {

            if (ae.getName().equals(methodAnnotation.getName()))
            {
                count++;

                if (count > 1)
                {
                    return true;
                }

            }

        }

        return false;

    }

    public CompilationUnit getCompilationUnit()
    {
        return cu;
    }

    public int getRecursiveCallLineNo()
    {
        return recursiveCallLineNo;
    }

    public MethodDeclaration getMethodDeclaration()
    {
        return methodDeclaration;
    }

    public List<Object> getMethodInputValues()
    {
        return methodInputValues;
    }

    public MethodCallExpr getMethodCall()
    {
        return methodCall;
    }

    // private visitor class to extract method data for source file and insert it into the method list
    private class MethodDeclarationVisitor extends VoidVisitorAdapter<Object>
    {

        private List<MethodDeclaration> methods = new ArrayList<>();

        @Override
        public void visit(MethodDeclaration n, Object arg)
        {
            methods.add(n);
        }

        List<MethodDeclaration> getMethods()
        {
            return methods;
        }

    }

    // private visitor class to extract method data for source file and insert it into the method list
    private class MethodCallExprVisitor extends VoidVisitorAdapter<Object>
    {

        private List<MethodCallExpr> methodCalls = new ArrayList<>();

        @Override
        public void visit(MethodCallExpr n, Object arg)
        {
            methodCalls.add(n);
            List<Expression> args = n.getArgs();
            for(Expression e : args) {
                if(e instanceof MethodCallExpr) {
                    MethodCallExpr mce = (MethodCallExpr) e;
                    visit(mce, null);
                }
            }
        }

        List<MethodCallExpr> getMethodCalls()
        {
            return methodCalls;
        }

    }

}
