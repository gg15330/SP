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
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.dplib.exception.AnalysisException;

// this class analyses source code for invalid solutions/errors
// the analysis can be different depending on whether the .java file
// is submitted by the tutor or the student
class SourceAnalyser
{

    private String methodName;
    private CompilationUnit cu;
    private boolean isRecursive;
    private boolean containsArray;
    private int recursiveCallLineNo;

    // construct a new SourceAnalyser with a method declaration to check against the source file
    SourceAnalyser(File file, String methodName)
    throws IOException, ParseException
    {
        FileInputStream fis = new FileInputStream(file);
        this.cu = JavaParser.parse(fis);
        fis.close();
        this.methodName = methodName;
    }

    void analyse()
    throws AnalysisException
    {
        MethodDeclaration methodDeclaration = findMethod(methodName);
        isRecursive = isRecursive(methodDeclaration);
        containsArray = containsArray(methodDeclaration);
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
        throw new AnalysisException("expected MethodDeclaration \"" + methodName +
                                    "\" does not exist in source file.");
    }

    private boolean isRecursive(Node node)
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
            if(containsArray) { return ProblemType.MEMOIZED; }
            else { return ProblemType.RECURSIVE; }
        }
        else if(!isRecursive)
        {
            if(containsArray) { return ProblemType.ITERATIVE; }
        }
        return ProblemType.UNDEFINED;
    }

    public String[] statementsToStringArray(List<Statement> statements)
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

    String getMethodName() { return methodName; }

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
