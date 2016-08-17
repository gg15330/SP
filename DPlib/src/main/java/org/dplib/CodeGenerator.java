package org.dplib;

/**
 * Created by george on 10/08/16.
 */
public class CodeGenerator
{
    private final String indentString = "||||";

    public String generate(String className,
                           String callingMethodDeclaration,
                           String callingMethodBody,
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

    private String createMethod(String declaration, String body)
    {
        int indent = 0;
        StringBuilder sb = new StringBuilder();
        sb.append(declaration + "\n");
        for(int j = 0; j < indent; j++) { sb.append(indentString); }
        for(int i = 2; i < body.length(); i++)
        {
            if(body.charAt(i-1) == '\n'
            && body.charAt(i-2) == '{')
            {
                for(int j = 0; j < ++indent; j++) { sb.append(indentString); }
            }
            sb.append(body.charAt(i));
            if(body.charAt(i-1) == '{') { indent++; }
            if(body.charAt(i-1) == '}') { indent--; }
        }
        System.out.println(body);
        System.out.println(sb.toString());
        return sb.toString();
    }

}
