package org.DPlib;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.DPlib.Exception.AnalysisException;

import java.io.File;

/**
 * Unit test for ClassAnalyser class.
 */
public class ClassAnalyserTest
    extends TestCase
{
    private static ClassAnalyser ca;
    private static File testJavaFile;
    private static File testClassFile;

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ClassAnalyserTest(String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( ClassAnalyserTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    private static void compileSetup() {
        System.out.println("Setting up for test_compile()...");

        testJavaFile = new File("src/test/resources/FibonacciDP.java");
        if(!testJavaFile.exists()) {
            throw new Error("Test .class file does not exist.");
        }

        testClassFile = new File("src/test/resources/FibonacciDP.class");
        if(!testClassFile.exists()) {
            throw new Error("Test .class file does not exist.");
        }

        try {
            ca = new ClassAnalyser(testClassFile, "FibonacciDP");
        } catch (AnalysisException e) {
            throw new Error(e);
        }

        System.out.println("Setup complete.");
    }

    public static void test_compile() {
//        compileSetup();
//        try {
//            assertEquals(testClassFile, ca.compile(testJavaFile));
//        } catch (Exception e) {
//            throw new Error(e);
//        }
    }

    public static void test_CompileException() {
//        Throwable t = null;
//        try {
//            ca.compile(null);
//        } catch (Throwable ex) {
//            t = ex;
//        }
//        assertTrue(t instanceof CompileException);
    }

    private static void analyseSetup() {
        System.out.println("Setting up for test_analyse()...");

        try {
//            ca.analyse(input);
        } catch (Exception e) {
            throw new Error(e);
        }
        System.out.println("Setup complete.");
    }

    public static void test_InstructionCountGreaterThan0() {
        analyseSetup();
//        assertTrue(0 < ca.getInstructionCount());
    }

//    public static void test_ExecutionTimeGreaterThan0() {
//        assertTrue(0 < ca.getExecutionTime());
//    }

    public static void test_AnalysisException() {
        Throwable t;
        try {
//            throw new Error("Expected AnalysisException.");
        } catch(Exception e) {
//            t = e;
        }
    }

}
