package org.dplib;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.dplib.analyse.Model;
import org.dplib.analyse.ProblemType;
import org.dplib.analyse.Result;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Unit test for ClassAnalyser class.
 */
public class ModelerTest
extends TestCase
{

    private static final FileHandler fh = new FileHandler();
    private static File testJavaFile;
    private static File testInputFile;

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ModelerTest(String testName )
    {
        super( testName );

        testJavaFile = new File("src/test/resources/FibonacciDP.java");
        if(!testJavaFile.exists()) { throw new Error("Test .java file does not exist."); }

        testInputFile = new File("src/test/resources/testInputFile.txt");
        if(!testInputFile.exists()) { throw new Error("Test input .txt file does not exist."); }
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( ModelerTest.class );
    }

    /**
     * Rigourous Test :-)
     */

    public static void test_model() {
        Model model;
        try { model = new Modeler().model(testJavaFile, testInputFile, "fibDP", fh); }
        catch (ModelingException e) { throw new Error(e); }

        assertEquals("FibonacciDP", model.getClassName());
        assertEquals("Problem description", model.getDescription());
        assertEquals("public static void main(String[] args)", model.getCallingMethodDeclaration());
        assertEquals("static int fibDP(int n)", model.getMethodToAnalyseDeclaration());
        assertEquals("int n = Integer.parseInt(args[0]);", model.getCallingMethodBody().substring(6, 40));
        assertEquals(ProblemType.ITERATIVE, model.getProblemType());

        List<Result> results = model.getResults();
        assertEquals("1", results.get(0).getInput()[0]);
        assertEquals("1", results.get(0).getOutput());
        assertTrue(0 < results.get(0).getExecutionTime());

        assertEquals("5", results.get(1).getInput()[0]);
        assertEquals("5", results.get(1).getOutput());
        assertTrue(0 < results.get(1).getExecutionTime());

        assertEquals("10", results.get(2).getInput()[0]);
        assertEquals("55", results.get(2).getOutput());
        assertTrue(0 < results.get(2).getExecutionTime());

        assertEquals("15", results.get(3).getInput()[0]);
        assertEquals("610", results.get(3).getOutput());
        assertTrue(0 < results.get(3).getExecutionTime());
    }

}
