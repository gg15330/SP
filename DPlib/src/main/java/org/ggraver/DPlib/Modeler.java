package org.ggraver.DPlib;

import com.sun.codemodel.*;

import java.io.File;
import java.io.IOException;

/**
 * Created by george on 22/07/16.
 */
// constructs a new Problem object which contains one method to analyse in the Solver
public class Modeler {

    private final Enum<ProblemType> problemType;
    private String className;

    Modeler() {
        this.problemType = ProblemType.NONE;
        this.className = "Test";
    }

    public Modeler(Enum<ProblemType> problemType) {
        this.problemType = problemType;
    }

    // found at http://sookocheff.com/post/java/generating-java-with-jcodemodel/
    void generate() {

        Problem prob = new Problem();

        try {
            JCodeModel jcm = prob.generateSourceCode();
            jcm.build(new File("sample/test/"));
        } catch (JClassAlreadyExistsException jcaee) {
            jcaee.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    void methodToAnalyse(String s) {

    }

    void methodParameters() {

    }

}
