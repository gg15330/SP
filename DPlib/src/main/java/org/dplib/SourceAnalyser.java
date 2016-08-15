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
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.dplib.exception.AnalysisException;

// this class analyses source code for invalid solutions/errors
// the analysis can be different depending on whether the .java file
// inputStream submitted by the tutor or the student
class SourceAnalyser
{

    private String methodName;
    private CompilationUnit cu;
    private String[] requiredClasses;

    private boolean isRecursive;
    private boolean containsArray;
    private boolean containsRequiredClass;

    private int recursiveCallLineNo;

    // construct a new SourceAnalyser with a method declaration to check against the source file
    SourceAnalyser(File file, String methodName)
    throws IOException, ParseException
    {
        FileInputStream fis = new FileInputStream(file);
        this.cu = JavaParser.parse(fis);
        fis.close();
        this.methodName = methodName;
        requiredClasses = null;
    }

    SourceAnalyser(File file, String methodName, String... requiredClasses)
    throws IOException, ParseException
    {
        this(file, methodName);
        this.requiredClasses = requiredClasses;;
    }

    void analyse(MethodDeclaration methodDeclaration)
    throws AnalysisException
    {
        System.out.println("Analysing...");
        methodDeclaration = findMethod(methodName);
        isRecursive = isRecursive(methodDeclaration);
        containsArray = containsArray(methodDeclaration);
        if (requiredClasses != null)
        {
            containsRequiredClass = containsRequiredClass(methodDeclaration);
        }

        System.out.println("Results of analysis:" +
                                   "\nMethod declaration: " + methodDeclaration.getName() +
                                   "\nRecursive: " + isRecursive +
                                   "\nContains array: " + containsArray +
                                   "\nContains required class: " + containsRequiredClass);
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

    private boolean containsRequiredClass(Node node)
    {
        if(node instanceof ClassOrInterfaceType)
        {
            String classType = ((ClassOrInterfaceType) node).getName();
            for(String requiredClass : requiredClasses)
            {
                if(classType.equals(requiredClass)) return true;
            }
        }
        for(Node n : node.getChildrenNodes())
        {
            if(containsRequiredClass(n)) return true;
        }
        return false;
    }

    private boolean containsArray(Node node)
    {
        if(node instanceof ArrayCreationExpr) return true;
        for(Node n : node.getChildrenNodes())
        {
            if(containsArray(n)) return true;
        }
        return false;
    }

    ProblemType determineProblemType() {
        if(isRecursive)
        {
            if(containsArray || containsRequiredClass) { return ProblemType.MEMOIZED; }
            else { return ProblemType.RECURSIVE; }
        }
        else if(!isRecursive)
        {
            if(containsArray || containsRequiredClass) { return ProblemType.ITERATIVE; }
        }
        return ProblemType.UNDEFINED;
    }

    public String[] StatementsToStringArray(List<Statement> statements)
    {
        String[] strings = new String[statements.size()];

        for(int i = 0; i < statements.size(); i++)
        {
            strings[i] = statements.get(i).toString();
        }
        return strings;
    }

    CompilationUnit getCompilationUnit()
    {
        return cu;
    }

    String getClassName()
    {
        return cu.getTypes().get(0).getName();
    }

    public int getRecursiveCallLineNo()
    {
        return recursiveCallLineNo;
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
