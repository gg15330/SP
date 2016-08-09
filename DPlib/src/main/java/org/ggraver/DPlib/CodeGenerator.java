package org.ggraver.DPlib;

import com.github.javaparser.Position;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.EmptyStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by george on 09/08/16.
 */
public class CodeGenerator
{

    public String generate(Model model)
    {
        MethodDeclaration main = model.getCallingMethod();
        MethodDeclaration methodTemplate = new MethodDeclaration();

        MethodDeclaration modelMethod = model.getMethodToAnalyse();
        methodTemplate.setName(modelMethod.getName());
        methodTemplate.setTypeParameters(modelMethod.getTypeParameters());
        methodTemplate.setType(modelMethod.getType());
        methodTemplate.setModifiers(modelMethod.getModifiers());
        methodTemplate.setParameters(modelMethod.getParameters());
        methodTemplate.setThrows(modelMethod.getThrows());

        BlockStmt blockStmt = new BlockStmt();
        Comment comment = new LineComment("Your code here");
        blockStmt.addOrphanComment(comment);
        methodTemplate.setBody(blockStmt);

        List<BodyDeclaration> members = new ArrayList<>();
        members.add(methodTemplate);
        members.add(main);

        ClassOrInterfaceDeclaration classDeclaration = new ClassOrInterfaceDeclaration();
        classDeclaration.setMembers(members);
        classDeclaration.setName(model.getMethodToAnalyse().getName());

        List<TypeDeclaration> types = new ArrayList<>();
        types.add(classDeclaration);

        CompilationUnit compilationUnit = new CompilationUnit();
        compilationUnit.setTypes(types);
        System.out.println("COMPILATION UNIT:\n\n" + compilationUnit.toString());
        return compilationUnit.toString();
    }

}
