package org.ggraver.DPlib;

import com.github.javaparser.ParseException;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.apache.commons.io.FilenameUtils;
import org.ggraver.DPlib.Exception.AnalysisException;
import org.ggraver.DPlib.Exception.CompileException;
import org.ggraver.DPlib.Exception.ModelingException;

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
    private String methodName;

//    analyse source file and make sure the method is recursive
//    private SourceAnalyser sourceAnalyser;

//    compute answers based on input and get performance analysis
//    private ClassAnalyser classAnalyser;

    Modeler(File sourceFile, String methodName) throws IOException {

        this.problemType = ProblemType.NONE;
        this.className = "Test";
        this.methodName = methodName;
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

//    remember to handle input parameterList
    void model() throws ModelingException {
//
//        SourceAnalyser sourceAnalyser;
//        MethodDeclaration md;
//
////         parse input file, check method is not recursive
//        try {
//            sourceAnalyser = new SourceAnalyser(sourceFile, methodName);
//            md = sourceAnalyser.findMethod(methodName);
//        } catch (AnalysisException e) {
//            throw new ModelingException(e);
//        }
//
//        if(sourceAnalyser.isRecursive(md)) {
//            throw new ModelingException("Recursive function call found at line: " + sourceAnalyser.getRecursiveCallLineNo());
//        }
//
////        compile source
//        try {
//            ClassAnalyser ca =  new ClassAnalyser(sourceFile);
//            ca.analyse();
//        } catch (AnalysisException e) {
//            throw new ModelingException(e);
//        }

//        run program, record stats in XML file



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

    void setMethod(String methodName) {

    }

    void setInputs(String... inputs) {

    }

    void methodToAnalyse(String s) {

    }

    void methodParameters() {

    }

}
