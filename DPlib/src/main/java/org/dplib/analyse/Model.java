package org.dplib.analyse;

import java.io.Serializable;

/**
 * Created by george on 28/07/16.
 */

// this class contains all the information required to fully model a DP problem for solving
public class Model
extends Analysis
implements Serializable
{

    private String description = "Problem description";
    private String className;
    private String methodToAnalyseName;
    private String methodToAnalyseDeclaration;
    private String callingMethodDeclaration;
    private String callingMethodBody;

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getMethodToAnalyseName() { return methodToAnalyseName; }
    public void setMethodToAnalyseName(String methodToAnalyseName) { this.methodToAnalyseName = methodToAnalyseName; }

    public String getMethodToAnalyseDeclaration() { return methodToAnalyseDeclaration; }
    public void setMethodToAnalyseDeclaration(String name) { this.methodToAnalyseDeclaration = name; }

    public String getCallingMethodDeclaration() { return callingMethodDeclaration; }
    public void setCallingMethodDeclaration(String callingMethodDeclaration) { this.callingMethodDeclaration = callingMethodDeclaration; }

    public String getCallingMethodBody() { return callingMethodBody; }
    public void setCallingMethodBody(String callingMethodBody) { this.callingMethodBody = callingMethodBody; }

}
