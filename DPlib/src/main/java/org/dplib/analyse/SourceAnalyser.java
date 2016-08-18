package org.dplib.analyse;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
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

// this class analyses source code for invalid solutions/errors
// the analysis can be different depending on whether the .java file
// is submitted by the tutor or the student
public class SourceAnalyser
{

    private CompilationUnit cu;
    private int recursiveCallLineNo;

    public void parse(String sourceCode)
    throws ParseException
    {
        StringReader sr = new StringReader(sourceCode);
        this.cu = JavaParser.parse(sr);
        sr.close();
    }

    public MethodDeclaration locateMethod(String methodName)
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
                                    "\" does not exist in source.");
    }

    public ProblemType determineProblemType(MethodDeclaration methodDeclaration) {
        if(isRecursive(methodDeclaration, methodDeclaration.getName()))
        {
            if(containsArray(methodDeclaration)) { return ProblemType.MEMOIZED; }
            else { return ProblemType.RECURSIVE; }
        }
        else
        {
            if(containsArray(methodDeclaration)) { return ProblemType.ITERATIVE; }
        }
        return ProblemType.UNDEFINED;
    }

    private boolean isRecursive(Node node, String methodName)
    {
        if (node instanceof MethodCallExpr)
        {
            MethodCallExpr mce = (MethodCallExpr) node;
            if (mce.getName().equals(methodName))
            {
                System.out.println("Recursive function call found.");
                recursiveCallLineNo = mce.getBeginLine();
                return true;
            }
        }
        for (Node child : node.getChildrenNodes())
        {
            if (isRecursive(child, methodName))
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

    public CompilationUnit getCompilationUnit()
    {
        return cu;
    }

    public String getClassName()
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
