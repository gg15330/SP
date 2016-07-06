package ggraver;

import java.util.List;
import java.util.ArrayList;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;


// analyses method in-depth and checks for incorrect solutions to problem
public class MethodAnalyser {

    public void analyse(MethodDeclaration md) {

    }

    public void process(Node node){

        for (Node n : node.getChildrenNodes()){
            System.out.println("Node: " + n.toString());

            if(n instanceof MethodCallExpr) {
                System.out.println("[METHOD CALL]");
            }

        process(n);

        }

    }

    public boolean isRecursive(MethodDeclaration md) {

        System.out.println("Method name: " + md.getName());
        BlockStmt body = md.getBody();
        List<Statement> stmts = body.getStmts();

        MethodCallVisitor mcv = new MethodCallVisitor();

        // mcv.visit(body, null);

        for(Statement s : stmts) {
            System.out.println("Statement");

            if(s instanceof ExpressionStmt) {
                System.out.println("Expression statement found");
                Expression expr = ((ExpressionStmt)s).getExpression();
                if(expr instanceof MethodCallExpr) {
                    System.out.println("Method call expression: " + expr);
                }
                else {
                    System.out.println("Expression: " + expr);
                }
            }
            else if(s instanceof ReturnStmt) {
                System.out.println("Return statement found");
                Expression expr = ((ReturnStmt)s).getExpr();
                if(expr instanceof MethodCallExpr) {
                    System.out.println("Method call expression: " + expr);
                }
                else {
                    System.out.println("Expression: " + expr);
                    System.out.println("Class: " + expr.getClass());
                }
            }

        }

        return false;

    }

    private class MethodCallVisitor extends VoidVisitorAdapter<Object> {

        @Override
        public void visit(BlockStmt n, Object arg) {

            System.out.println("Visiting...");
            System.out.println("");

        }

    }

}
