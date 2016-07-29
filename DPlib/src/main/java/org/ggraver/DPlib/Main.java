package org.ggraver.DPlib;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.Statement;
import org.apache.commons.io.FilenameUtils;
import org.ggraver.DPlib.Exception.AnalysisException;
import org.ggraver.DPlib.Exception.ModelingException;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

//overall program control
public class Main
{

    private File tutorFile;
    private File studentFile;
    private String methodName;

    private Main(String file, String methodName)
    {

        this.tutorFile = new File(file);
        this.studentFile = new File("sample/fib/FibonacciDPStudent.java");

        if (!this.tutorFile.exists()
                || this.tutorFile.isDirectory()
                || !FilenameUtils.getExtension(this.tutorFile.getName()).equals("java")
                || !this.studentFile.exists()
                || this.studentFile.isDirectory()
                || !FilenameUtils.getExtension(this.studentFile.getName()).equals("java"))
        {
            System.err.println("\nInvalid tutorFile name: " + file + "\n");
            System.exit(1);
        }
        this.methodName = methodName;
    }

    public static void main(String[] args)
    {
        if (args.length != 2)
        {
            System.out.println("\nUsage: java -jar DPLib-1.0-SNAPSHOT.jar <path/to.tutorFile.java>\n");
            System.exit(1);
        }

        Main main = new Main(args[0], args[1]);
        main.run();
    }

    private void run()
    {
        try
        {
            Modeler modeler = new Modeler(tutorFile, methodName);
            Model model = modeler.model();
            SourceAnalyser sa = new SourceAnalyser(studentFile, methodName);
            MethodDeclaration studentMain = sa.findMethod("main");

            for (Statement s : model.getCallingMethod().getBody().getStmts())
            {
                System.out.println("[TUTOR] " + s);
            }
            for (Statement s : studentMain.getBody().getStmts())
            {
                System.out.println("[STUDENT] " + s);
            }

            if (!model.getCallingMethod().getBody().getStmts()
                      .equals(studentMain.getBody().getStmts()))
            {
                throw new Error("Main methods do not match.");
            }
            ClassAnalyser ca = new ClassAnalyser(tutorFile);
            ca.analyse();
            System.out.println("Time: " + ca.getExecutionTime());
            System.out.println("Instructions: " + NumberFormat.getNumberInstance(Locale.UK).format(ca.getInstructionCount()));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (ModelingException | AnalysisException e)
        {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }


////             create method properties
//            List<Parameter> parameterList = new ArrayList<>();
//
//            Parameter param = new Parameter();
//            param.setType(new PrimitiveType(PrimitiveType.Primitive.Int));
//            param.setId(new VariableDeclaratorId("n"));
//            parameterList.add(param);
//
////             create expected method
//            MethodDeclaration expected = new MethodDeclaration();
//            expected.setName(methodName);
//            expected.setType(new PrimitiveType(PrimitiveType.Primitive.Int));
//            expected.setParameters(parameterList);
//
//            studentFileAnalyser.checkMethodProperties(expected, studentFileAnalyser.getMethodDeclaration());
//
//        } catch (AnalysisException e) {
//            e.printStackTrace();
//            System.err.println(e.getMessage());
//            System.exit(1);
//        }
    }

}
