package org.ggraver.DPlib;

import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by george on 22/07/16.
 */
// constructs a new Problem object which contains one method to analyse in the Solver
class Modeler {

    private final Enum<ProblemType> problemType;
    private File sourceFile;
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

    Modeler(File sourceFile) throws IOException {

        this.problemType = ProblemType.NONE;
        this.className = "Test";
        this.sourceFile = sourceFile;

        if(
                !sourceFile.exists()
                || sourceFile.isDirectory()
                || !FilenameUtils.getExtension(sourceFile.getPath()).equals("java")
                ) {
            throw new IOException("Could not model input file: " + sourceFile.getPath());
        }

    }

    public Modeler(Enum<ProblemType> problemType) { this.problemType = problemType; }

    void model(File sourceFile) {

//        SourceAnalyser sourceAnalyser = new SourceAnalyser();
//
//        try {
//            CompilationUnit compilationUnit = sourceAnalyser.parse(sourceFile);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    void model() {

        SourceAnalyser sourceAnalyser;
        try {
            sourceAnalyser = new SourceAnalyser(sourceFile);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        sourceAnalyser.checkForRecursion();

    }

    void generateSourceCode() {

        Problem prob = new Problem();

        try {
            prob.generateSourceCode();
//            jcm.build(new File("sample/code_generation_test/"));
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }

    }

    void methodToAnalyse(String s) {

    }

    void methodParameters() {

    }

}
