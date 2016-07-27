package org.ggraver.DPlib;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclaratorId;
import com.github.javaparser.ast.type.PrimitiveType;
import org.apache.commons.io.FilenameUtils;
import org.ggraver.DPlib.Exception.AnalysisException;
import org.ggraver.DPlib.Exception.ModelingException;

import javax.xml.transform.Source;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//overall program control
public class Main {

    private File sourceFile;
    private String methodName;

    private Main(String file, String methodName) {

        this.sourceFile = new File(file);

        if (!this.sourceFile.exists()
                || this.sourceFile.isDirectory()
                || !FilenameUtils.getExtension(this.sourceFile.getName()).equals("java")) {
            System.err.println("\nInvalid sourceFile name: " + file + "\n");
            System.exit(1);
        }

        this.methodName = methodName;

    }

    public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println("\nUsage: java -jar DPLib-1.0-SNAPSHOT.jar <path/to.sourceFile.java>\n");
            System.exit(1);
        }

        Main main = new Main(args[0], args[1]);
        main.run();

    }

    private void run() {

        try {
            Modeler modeler = new Modeler(sourceFile, methodName);
            modeler.model();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ModelingException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }

        SourceAnalyser studentFileAnalyser;

        try {
            studentFileAnalyser = new SourceAnalyser(sourceFile, methodName);
            studentFileAnalyser.findMethod(methodName);
            if(!studentFileAnalyser.isRecursive(studentFileAnalyser.getMethodDeclaration())) {
                System.out.println("Method is not recursive.");
            }
            else {
                System.out.println("Method IS recursive.");
            }

//             create method properties
            List<Parameter> parameterList = new ArrayList<>();

            Parameter param = new Parameter();
            param.setType(new PrimitiveType(PrimitiveType.Primitive.Int));
            param.setId(new VariableDeclaratorId("n"));
            parameterList.add(param);

//             create expected method
            MethodDeclaration expected = new MethodDeclaration();
            expected.setName(methodName);
            expected.setType(new PrimitiveType(PrimitiveType.Primitive.Int));
            expected.setParameters(parameterList);

            studentFileAnalyser.checkMethodProperties(expected, studentFileAnalyser.getMethodDeclaration());

        } catch (AnalysisException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            System.exit(1);
        }



















//         check submitted method matches template name/type/parameterList
//         and check that it is not recursive
/*        try {
            actual = sa.findMethod(expected.getName());
            sa.checkMethodProperties(expected, actual);
            sa.isRecursive(actual, actual.getName());
        } catch (AnalysisException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }*/

//        try {
//            ClassAnalyser ca = new ClassAnalyser();
//            File f = ca.compile(sourceFile);
//            ca.analyse(f);
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.err.println(e.getMessage());
//            System.exit(1);
//        }

    }

}
