package org.ggraver.DPlib;

import com.github.javaparser.ast.body.MethodDeclaration;


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

         try {

             recursion = false;
         }
         catch(Exception e) {
             System.out.println(e.getMessage());
             recursion = true;
         }
         finally {
             System.out.println("Recursive: " + recursion);
         }

    }

}
