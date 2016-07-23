package org.ggraver.DPlib;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.body.VariableDeclaratorId;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.Type;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.corba.se.impl.orb.ParserTable;
import com.sun.javafx.sg.prism.NGShape;
import org.ggraver.DPlib.Exception.AnalysisException;

import javax.xml.transform.Source;

//overall program control
public class Main {

    private File file;
    private String methodName;

    private Main(String file, String methodName) {

        this.file = new File(file);

        if (!this.file.exists() || this.file.isDirectory()) {
            System.err.println("\nInvalid file name: " + file + "\n");
            System.exit(1);
        }

        this.methodName = methodName;

    }

    // handle command line arguments
    public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println("\nUsage: java -jar DPLib-1.0-SNAPSHOT.jar <path/to.file.java>\n");
            System.exit(1);
        }

        Main main = new Main(args[0], args[1]);
        main.run();

    }

    private void run() {

//        Modeler modeler = new Modeler();
//        modeler.methodToAnalyse("");
//        modeler.methodParameters();
//        modeler.generate();

//         create method properties
        PrimitiveType pt = new PrimitiveType(PrimitiveType.Primitive.Int);
        List<Parameter> parameters = new ArrayList<>();
        Parameter param = new Parameter();
        param.setType(pt);
        param.setId(new VariableDeclaratorId("n"));
        parameters.add(param);

//         create expected method
        MethodDeclaration expected = new MethodDeclaration();
        expected.setName(methodName);
        expected.setType(pt);
        expected.setParameters(parameters);

        SourceAnalyser sa = null;

        try {
            sa = new SourceAnalyser(file);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
            System.err.println("\nFile not found.\n");
            System.exit(1);
        } catch (ParseException pe) {
            pe.printStackTrace();
            System.err.println("\nFile could not be parsed, please ensure file is a valid .java class.\n");
            System.exit(1);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(1);
        }

        MethodDeclaration actual;

//         check submitted method matches template name/type/parameters
//         and check that it is not recursive
        try {
            actual = sa.findMethod(expected.getName());
            sa.checkMethodProperties(expected, actual);
            sa.checkForRecursion(actual, actual.getName());
        } catch (AnalysisException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }





//        try {
//            ClassAnalyser ca = new ClassAnalyser();
//            File f = ca.compile(file);
//            ca.analyse(f);
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.err.println(e.getMessage());
//            System.exit(1);
//        }

    }

}
