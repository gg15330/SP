package org.dplib;

/**
 * Created by george on 10/08/16.
 */
public class CodeGenerator
{
    public String generate(String className, String callingMethodDeclaration, String[] callingMethodBody,
                           String methodToAnalyseDeclaration)
    {
        String string = "class " + className +
                " {\n\n    " + createMethod(methodToAnalyseDeclaration) +
                "\n\n    " + createMethod(callingMethodDeclaration, callingMethodBody) +
                "\n\n}";

        return string;
    }

    private String createMethod(String declaration)
    {
        return (declaration + " {" +
                "\n        // Your code here" +
                "\n    }");
    }

    private String createMethod(String declaration, String[] body)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(declaration + " {");

        for(String s : body)
        {
            sb.append("\n        " + s);
        }

        sb.append("\n    }");

        return sb.toString();
    }

}
