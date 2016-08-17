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
        int indent = 1;
        StringBuilder sb = new StringBuilder();

        sb.append(declaration + " ");
        String temp = body.replaceAll("    ", "");
        System.out.println(temp);
        String[] strings = temp.split("\n");
        for(String s : strings)
        {
            System.out.println("LINE: " + s);
            if(s.contains("}")) { indent--; }
            for(int i = 0; i < indent; i++) { sb.append(indentString); }
            if(s.contains("{")) { indent++; }
            sb.append(s + "\n");
        }

        System.out.println(sb.toString());
        return sb.toString();
    }

}
