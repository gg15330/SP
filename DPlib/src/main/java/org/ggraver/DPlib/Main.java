package org.ggraver.DPlib;

import com.github.javaparser.ast.body.MethodDeclaration;
import org.ggraver.DPlib.Exception.AnalysisException;
import org.ggraver.DPlib.Exception.ModelingException;

import java.io.File;
import java.io.IOException;

// overall program control
public class Main
{

    private IO io = new IO();
    private File file;
    private String methodName;

    private Main(String file, String methodName)
    {
        this.file = new File(file);
        this.methodName = methodName;
    }

    public static void main(String[] args)
    {
        Main main = new Main(args[0], args[1]);
        main.run(args);
    }

    private void run(String[] args)
    {
        try
        {
            io.checkArgs(args);
            io.checkValidSourceFile(file, "java");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        switch (args[0]) {
            case "model": model(); break;
            case "solve": solve(); break;
            default: io.fail(new IOException("Invalid command line")); break;
        }
    }

    private void model()
    {
        try
        {
            Modeler modeler = new Modeler(file, methodName);
            Model model = modeler.model();
        }
        catch (ModelingException e)
        {
            io.fail(e);
        }
    }

    private void solve()
    {
        try
        {
//             this part will eventually go in a solver/marker module
            SourceAnalyser sa = new SourceAnalyser(studentFile, methodName);
            MethodDeclaration studentMain = sa.findMethod("main");

//                    if (!model.getCallingMethod().getBody().getStmts()
//                              .equals(studentMain.getBody().getStmts()))
//                    {
//                        throw new Error("Main methods do not match.");
//                    }
        }
        catch (AnalysisException e)
        {
            io.fail(e);
        }
    }

}
