package org.dplib.analyse;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.ArrayCreationExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

// Checks source code for errors, identifies which method to analyse
// and determines the type of algorithm used (recursive, memoized or iterative)
public class SourceAnalyser
{

    private CompilationUnit cu;
    private int recursiveCallLineNo;

    public void parse(File sourceFile)
    throws IOException, ParseException
    {
        this.cu = JavaParser.parse(sourceFile);
    }

    public void parse(String sourceCode)
    throws ParseException
    {
        StringReader sr = new StringReader(sourceCode);
        this.cu = JavaParser.parse(sr);
        sr.close();
    }

//    locate the method to be analysed within the CompilationUnit,
//    throw an AnalysisException if not found
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

//    analyse the Nodes of the given method and determine the ProblemType (DP algorithm)
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

//    recursively check all nodes within the method for a recursive function call
    private boolean isRecursive(Node node, String methodName)
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
            if (isRecursive(child, methodName))
            {
                return true;
            }
        }
        return false;
    }

//    recursively search for creation of a new array within the given method
    private boolean containsArray(Node node)
    {
        if(node instanceof ArrayCreationExpr) return true;
        for(Node n : node.getChildrenNodes())
        {
            if(containsArray(n)) return true;
        }
        return false;
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

}
