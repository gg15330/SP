package org.ggraver.DPlib;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import org.apache.commons.io.FilenameUtils;
import org.ggraver.DPlib.Exception.AnalysisException;
import org.ggraver.DPlib.Exception.ModelingException;

import java.io.File;
import java.io.IOException;
import java.util.List;

//overall program control
public class Main
{

    private File sourceFile;
    private String methodName;

    private Main(String file, String methodName)
    {

        this.sourceFile = new File(file);

        if (!this.sourceFile.exists()
                || this.sourceFile.isDirectory()
                || !FilenameUtils.getExtension(this.sourceFile.getName()).equals("java"))
        {
            System.err.println("\nInvalid sourceFile name: " + file + "\n");
            System.exit(1);
        }

        this.methodName = methodName;

    }

    public static void main(String[] args)
    {

        if (args.length != 2)
        {
            System.out.println("\nUsage: java -jar DPLib-1.0-SNAPSHOT.jar <path/to.sourceFile.java>\n");
            System.exit(1);
        }

        Main main = new Main(args[0], args[1]);
        main.run();

    }

    private void run()
    {
        try
        {
            Modeler modeler = new Modeler(sourceFile, methodName);
            modeler.model();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (ModelingException e)
        {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }

        try
        {
            SourceAnalyser sa = new SourceAnalyser(sourceFile, methodName);
            sa.findMethodCall(sa.getCompilationUnit(), methodName);
            MethodCallExpr call = sa.getMethodCall();

            System.out.println("------------------------------------------");
            System.out.println("[" + call.getBeginLine() + "] Method call: " + call.getName());
            List<Expression> args = call.getArgs();
            for(Expression e : args) {
                System.out.println("Arg: " + e);
                System.out.println("Arg class: " + e.getClass());
                System.out.println("Arg parent: " + e.getParentNode());
                System.out.println("Arg parent class: " + e.getParentNode().getClass());
                System.out.println("Arg parent data: " + e.getParentNode().getData());
            }
            System.out.println("Scope: " + call.getScope());

        }
        catch (AnalysisException e)
        {
            e.printStackTrace();
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
