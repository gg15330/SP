package org.ggraver.DPlib;

import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;

import java.io.File;
import java.io.IOException;

/**
 * Created by george on 22/07/16.
 */
// constructs a new Problem object which contains one method to analyse in the Solver
class Modeler {

    private final Enum<ProblemType> problemType;
    private String className;

//    store output data in xml file for comparison with student solution
//    private OutputFile xml;
//
//    input for sample .java file
//    private Object input;

//    analyse source file and make sure the method is recursive
//    private SourceAnalyser sourceAnalyser = new SourceAnalyser();

//    compute answers based on input and get performance analysis
//    private ClassAnalyser classAnalyser;

    Modeler() {
        this.problemType = ProblemType.NONE;
        this.className = "Test";
    }

    public Modeler(Enum<ProblemType> problemType) { this.problemType = problemType; }

    public void model(File sourceFile) {

        SourceAnalyser sourceAnalyser = new SourceAnalyser();

        try {
            CompilationUnit compilationUnit = sourceAnalyser.parse(sourceFile);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



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
