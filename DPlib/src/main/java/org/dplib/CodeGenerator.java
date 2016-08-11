package org.dplib;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.stmt.BlockStmt;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by george on 10/08/16.
 */
public class CodeGenerator
{
    public String generate(Model model)
    {
//        method template
        Comment comment = new LineComment("Your code here");

        BlockStmt blockStmt = new BlockStmt();
        blockStmt.addOrphanComment(comment);

        MethodDeclaration modelMethod = model.getMethodToAnalyse();
        MethodDeclaration methodTemplate = new MethodDeclaration();
        methodTemplate.setName(modelMethod.getName());
        methodTemplate.setTypeParameters(modelMethod.getTypeParameters());
        methodTemplate.setType(modelMethod.getType());
        methodTemplate.setModifiers(modelMethod.getModifiers());
        methodTemplate.setParameters(modelMethod.getParameters());
        methodTemplate.setThrows(modelMethod.getThrows());
        methodTemplate.setBody(blockStmt);

//        main method
        MethodDeclaration main = model.getCallingMethod();

//        method list
        List<BodyDeclaration> members = new ArrayList<>();
        members.add(methodTemplate);
        members.add(main);

//        class declaration
        ClassOrInterfaceDeclaration classDeclaration = new ClassOrInterfaceDeclaration();
        classDeclaration.setMembers(members);
        classDeclaration.setName(model.getMethodToAnalyse().getName());

//        type list
        List<TypeDeclaration> types = new ArrayList<>();
        types.add(classDeclaration);

//        compilation unit
        CompilationUnit compilationUnit = new CompilationUnit();
        compilationUnit.setTypes(types);
        compilationUnit.setComment(new BlockComment(model.getDescription()));

        return compilationUnit.toString();
    }

}
