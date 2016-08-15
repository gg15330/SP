package org.dplib;

import com.github.javaparser.ParseException;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.Statement;
import org.dplib.exception.ModelingException;
import org.dplib.exception.AnalysisException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by george on 22/07/16.
 */
// constructs a new Model object which contains one method to analyse in the Solver
class Modeler
{

    private SourceAnalyser sa;

    Model model(File sourceFile, String methodName, String[][] inputs)
    throws ModelingException
    {
        Model model = new Model();
        if(!sourceFile.exists()) { throw new Error("Source file does not exist."); }

        try
        {
            sa = new SourceAnalyser(sourceFile, methodName);

            model.setClassName(sa.getClassName());

//            set declaration string for method to analyse
            MethodDeclaration methodToAnalyse = sa.findMethod(methodName);
            model.setMethodToAnalyseDeclaration(methodToAnalyse.getDeclarationAsString());

//            set declaration string for calling method
            MethodDeclaration callingMethod = sa.findMethod("main");
            model.setCallingMethodDeclaration(callingMethod.getDeclarationAsString());

//            set method body for calling method as String array
            List<Statement> statements = new ArrayList<>(callingMethod.getBody().getStmts());
            String[] callingMethodStatements = sa.StatementsToStringArray(statements);
            model.setCallingMethodBody(callingMethodStatements);

//            set problem type
            sa.analyse();
            model.setType(sa.determineProblemType());
            System.out.println("Problem type: " + sa.determineProblemType());

//            create result set
            ClassAnalyser ca = new ClassAnalyser(sourceFile, sa.getClassName());
            System.out.println("Analysing...");

            for(String[] args : inputs)
            {
                Result result = ca.analyse(args);
                model.addResult(result);
            }

            System.out.println("Analysis complete.");
        }
        catch (AnalysisException | ParseException | IOException e)
        {
            throw new ModelingException(e);
        }

        return model;
    }

}
