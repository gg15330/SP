package org.ggraver.DPlib;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.Statement;
import org.ggraver.DPlib.Exception.AnalysisException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by george on 01/08/16.
 */
public class Solver
{

    public List<Result2> solve(Model model, File file)
    throws AnalysisException
    {
        SourceAnalyser sa = new SourceAnalyser(file, model.getMethodToAnalyseDeclaration());
        MethodDeclaration userCallingMethod = sa.findMethod("main");

        String[] userCallingMethodBody = toStringArray(userCallingMethod.getBody().getStmts());

        System.out.println("user main method length: " + userCallingMethodBody.length);
        System.out.println("model main method length: " + model.getCallingMethodBody().length);
        if(userCallingMethodBody.length != model.getCallingMethodBody().length)
        {
            throw new Error("method body lengths do not match.");
        }

        for(int i = 0; i < userCallingMethodBody.length; i++)
        {
            System.out.println("[USER STMT] " + userCallingMethodBody[i]);
            System.out.println("[MODL STMT] " + model.getCallingMethodBody()[i]);
            if(!userCallingMethodBody[i].equals(model.getCallingMethodBody()[i]))
            {
                throw new AnalysisException("Submitted calling method \"" +
                                                    userCallingMethod.getDeclarationAsString() +
                                                    "\" does not match modelled calling method \"" +
                                                    model.getCallingMethodDeclaration() + "\".");
            }
        }




        ClassAnalyser ca = new ClassAnalyser(file, sa.getClassName());
        List<Result2> results = new ArrayList<>();

        for(Result2 result2 : model.getResults())
        {
            Result2 result = ca.analyse(result2.getInput());
            results.add(result);
        }

        return results;
    }

    private String[] toStringArray(List<Statement> statements)
    {
        String[] strings = new String[statements.size()];

        for(int i = 0; i < statements.size(); i++)
        {
            strings[i] = statements.get(i).toString();
        }
        return strings;
    }
}
