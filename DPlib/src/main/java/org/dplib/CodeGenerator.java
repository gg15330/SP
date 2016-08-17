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
                " {\n\n" + createMethod(methodToAnalyseDeclaration) +
                "\n\n" + createMethod(callingMethodDeclaration, callingMethodBody) +
                "\n\n}";

        return string;
    }

    private String createMethod(String declaration)
    {
        return (indentString + declaration + " {" +
                "\n" + indentString + indentString + "// Your code here" +
                "\n" + indentString + "}");
    }

    private String createMethod(String declaration, String body)
    {
        int indent = 1;
        StringBuilder sb = new StringBuilder();

        sb.append(indentString + declaration + "\n");

        String[] strings = body.split("\n");

        for(String s : strings)
        {
            String trimmed = s.trim();

            if(trimmed.contains("}")) { indent--; }
            for(int i = 0; i < indent; i++) { sb.append(indentString); }
            if(trimmed.contains("{")) { indent++; }
            sb.append(trimmed + "\n");
        }

        System.out.println(sb.toString());
        return sb.toString();
    }

}
