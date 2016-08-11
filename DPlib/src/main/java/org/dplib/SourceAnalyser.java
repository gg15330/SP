package org.dplib;

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
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.dplib.enums.ProblemType;
import org.dplib.exception.AnalysisException;

class SourceAnalyser
{

    private String methodName;
    private int recursiveCallLineNo;
    private CompilationUnit cu;
    private MethodDeclaration methodDeclaration;
    private ProblemType problemType;

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
            throw new AnalysisException("\nCould not parse .java file: " + e.getMessage());
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

    void analyse()
    throws AnalysisException
    {
        methodDeclaration = findMethod(methodName);
    }

//    boolean containsRequiredClass(Node node, String className)
//    {
//        System.out.println("Class: " + node.getClass());
//        System.out.println("Node: " + node.toString());
//        System.out.println();
//        for(Node n : node.getChildrenNodes())
//        {
//            containsRequiredClass(n, className);
//        }
//        return true;
//    }

    boolean containsArray(Node node)
    {
        if(node instanceof ArrayCreationExpr) return true;
        for(Node n : node.getChildrenNodes())
        {
            if(containsArray(n)) return true;
        }
        return false;
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

    CompilationUnit getCompilationUnit()
    {
        return cu;
    }

    String getClassName()
    {
        System.out.println("Class name: " + cu.getTypes().get(0).getName());
        return cu.getTypes().get(0).getName();
    }

    public int getRecursiveCallLineNo()
    {
        return recursiveCallLineNo;
    }

    ProblemType getProblemType()
    {
        return problemType;
    }

    public MethodDeclaration getMethodDeclaration()
    {
        return methodDeclaration;
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
