package org.dplib;

import com.github.javaparser.ast.body.MethodDeclaration;
import org.dplib.analyse.*;

import java.util.List;

/**
 * Created by george on 22/07/16.
 */
// constructs a new Model object which contains one method to analyse in the Solver
public class Modeler
{

    public Model model(String className,
                       MethodDeclaration methodDeclaration,
                       MethodDeclaration callingMethodDeclaration,
                       ProblemType problemType,
                       List<Result> results)
    {
        Model model = new Model();
        model.setClassName(className);
        model.setMethodToAnalyseName(methodDeclaration.getName());
        model.setMethodToAnalyseDeclaration(methodDeclaration.getDeclarationAsString());
        model.setCallingMethodDeclaration(callingMethodDeclaration.getDeclarationAsString());
        model.setCallingMethodBody(callingMethodDeclaration.getBody().toString());
        model.setProblemType(problemType);
        model.setResults(results);
        return model;
    }

}
