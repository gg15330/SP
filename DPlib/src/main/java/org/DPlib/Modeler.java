package org.DPlib;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.Statement;
import org.DPlib.Exception.ModelingException;
import org.DPlib.Exception.AnalysisException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by george on 22/07/16.
 */
// constructs a new Model object which contains one method to analyse in the Solver
class Modeler
{

    Model model(File sourceFile, String methodName, String[] inputs)
    throws ModelingException
    {
        Model model = new Model();
        if(!sourceFile.exists()) { throw new Error("Source file does not exist."); }
        System.out.println("Source file path: " + sourceFile.getPath());
        try
        {
            SourceAnalyser sa = new SourceAnalyser(sourceFile, methodName);

            model.setClassName(sa.getClassName());

            MethodDeclaration methodToAnalyse = sa.findMethod(methodName);
            model.setMethodToAnalyseDeclaration(methodToAnalyse.getDeclarationAsString());

            MethodDeclaration callingMethod = sa.findMethod("main");
            model.setCallingMethodDeclaration(callingMethod.getDeclarationAsString());

            List<Statement> statements = new ArrayList<>(callingMethod.getBody().getStmts());
            String[] callingMethodStatements = toStringArray(statements);
            model.setCallingMethodBody(callingMethodStatements);

            ClassAnalyser ca = new ClassAnalyser(sourceFile, sa.getClassName());
            for(String input : inputs)
            {
                Result result = ca.analyse(input);
                model.addResult(result);
            }
        }
        catch (AnalysisException e)
        {
            throw new ModelingException(e);
        }
        return model;
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
