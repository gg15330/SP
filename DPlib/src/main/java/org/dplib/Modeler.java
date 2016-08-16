package org.dplib;

import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.Statement;
import org.dplib.exception.CompileException;
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
            System.out.println("Analysing source...");
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
            String[] callingMethodStatements = sa.statementsToStringArray(statements);
            model.setCallingMethodBody(callingMethodStatements);

//            set problem type
            sa.analyse();
            model.setType(sa.determineProblemType());

//            create result set
            System.out.println("Analysing class...");

            File classFile = new SourceCompiler().compile(sourceFile, sa.getClassName());
            List<Result> results = new ClassAnalyser().analyse(classFile, inputs);
            model.setResults(results);

            System.out.println("Analysis complete.");
        }
        catch (CompileException | AnalysisException | ParseException | IOException e)
        {
            throw new ModelingException(e);
        }

        return model;
    }

}
