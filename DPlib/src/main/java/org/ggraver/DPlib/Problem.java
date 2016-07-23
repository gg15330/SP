package org.ggraver.DPlib;

import com.sun.codemodel.*;

import java.io.File;
import java.io.IOException;

/**
 * Created by george on 22/07/16.
 */
class Problem {

    Problem(String className, Enum<ProblemType> problemType) {}

    public Problem() {

    }

    JCodeModel generateSourceCode() throws IOException, JClassAlreadyExistsException {

        // Instantiate a new JCodeModel
        JCodeModel codeModel = new JCodeModel();

        // Create a new class
        JDefinedClass jc = codeModel._class("GeneratedClass");

        // Add Javadoc
        jc.javadoc().add("A JCodeModel example.");

        // Add default constructor
        jc.constructor(JMod.PUBLIC);

        // Add constant serializable id
        jc.field(JMod.STATIC | JMod.FINAL, Long.class, "serialVersionUID", JExpr.lit(1L));

        // Add private variable
        JFieldVar quantity = jc.field(JMod.PRIVATE, Integer.class, "quantity");

        // Add get method
        JMethod getter = jc.method(JMod.PUBLIC, quantity.type(), "getQuantity");
        getter.body()._return(quantity);
        getter.javadoc().add("Returns the quantity.");
        getter.javadoc().addReturn().add(quantity.name());

        JMethod dfib = jc.method(JMod.NONE, codeModel.INT, "FibonnaciDP");
//        FibonnaciDP.param();

        // Add set method
        JMethod setter = jc.method(JMod.PUBLIC, codeModel.VOID, "setQuantity");
        setter.param(quantity.type(), quantity.name());
        setter.body().assign(JExpr._this().ref(quantity.name()), JExpr.ref(quantity.name()));
        setter.javadoc().add("Set the quantity.");
        setter.javadoc().addParam(quantity.name()).add("the new quantity");

        // Generate the code
//        codeModel.build(new File("sample/test/"));
        return codeModel;
    }

}
