package ggraver;

import java.util.List;
import java.util.ArrayList;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;


// analyses method in-depth and checks for incorrect solutions to problem
public class MethodAnalyser {

    private MethodDeclaration method;
    private boolean recursion;

    public MethodAnalyser(MethodDeclaration method) {

        this.method = method;

        if(this.method == null) {
            throw new Error("this.method should not be null.");
        }

    }

    public void analyse() {

        System.out.println("Recursion: " + recursive(method));
        
        // try {
        //     recursiveWithException(method);
        //     recursion = false;
        // }
        // catch(Exception e) {
        //     System.out.println(e.getMessage());
        //     recursion = true;
        // }
        // finally {
        //     System.out.println("Recursive: " + recursion);
        // }

    }

    // recursively check all nodes within the method for recursive function calls
    private boolean recursive(Node node) {

        if(node instanceof MethodCallExpr) {

            MethodCallExpr mce = (MethodCallExpr)node;

            if(mce.getName().equals(method.getName())) {
                // throw new Exception("Recursive method call \"" + mce.getName() + "\" found at line " + mce.getBeginLine());
                System.out.println("Recursive method call \"" + mce.getName() + "\" found at line " + mce.getBeginLine());
                return true;
            }

        }

        for (Node n : node.getChildrenNodes()){
            if(recursive(n) == true) {
                return true;
            }
        }

        return false;

    }

    // recursively check all nodes within the method for recursive function calls,
    // throw an exception if one is found
    private void recursiveWithException(Node node) throws Exception {

        if(node instanceof MethodCallExpr) {

            MethodCallExpr mce = (MethodCallExpr)node;

            if(mce.getName().equals(method.getName())) {
                throw new Exception("Recursive method call \"" + mce.getName() + "\" found at line " + mce.getBeginLine());
            }

        }

        for (Node n : node.getChildrenNodes()){
            recursiveWithException(n);
        }

    }

}
