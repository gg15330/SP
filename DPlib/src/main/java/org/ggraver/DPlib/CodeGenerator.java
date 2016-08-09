package org.ggraver.DPlib;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by george on 09/08/16.
 */
public class CodeGenerator
{

    public String generate(Model model)
    {
        System.out.println("Generating code...");
        MethodDeclaration main = model.getCallingMethod();
        List<BodyDeclaration> members = new ArrayList<>();
        members.add(main);
        ClassOrInterfaceDeclaration classDeclaration = new ClassOrInterfaceDeclaration();
        classDeclaration.setMembers(members);
        classDeclaration.setName(model.getMethodToAnalyse().getName());

        List<TypeDeclaration> types = new ArrayList<>();
        types.add(classDeclaration);
        CompilationUnit compilationUnit = new CompilationUnit();
        compilationUnit.setTypes(types);
        System.out.println("COMPILATION UNIT:\n\n" + compilationUnit.toString());
        System.out.println("Code generated.");
        return compilationUnit.toString();
    }

}
