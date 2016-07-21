package org.ggraver.DPlib;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.ggraver.DPlib.Exception.AnalysisException;
import org.ggraver.DPlib.Exception.CompileException;

import java.io.File;
import java.io.IOException;

/**
 * Unit test for simple App.
 */
public class ClassAnalyserTest
    extends TestCase
{
    private static ClassAnalyser ca;
    private static File testJavaFile;
    private static File testClassFile;
    private static File testInvalidClassFile;

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
        return new TestSuite( org.ggraver.DPlib.ClassAnalyserTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    private static void compileSetup() {
        System.out.println("Setting up for test_compile()...");
        ca = new ClassAnalyser();

        testJavaFile = new File("src/test/resources/dfib.java");
        if(!testJavaFile.exists()) {
            throw new Error("Test .class file does not exist.");
        }

        testClassFile = new File("src/test/resources/dfib.class");
        if(!testClassFile.exists()) {
            throw new Error("Test .class file does not exist.");
        }

        System.out.println("Setup complete.");
    }

    public static void test_compile() {
        compileSetup();
        try {
            assertEquals(testClassFile, ca.compile(testJavaFile));
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    public static void test_CompileException() {
        Throwable t = null;
        try {
            ca.compile(null);
        } catch (Throwable ex) {
            t = ex;
        }
        assertTrue(t instanceof CompileException);
    }

    private static void analyseSetup() {
        System.out.println("Setting up for test_analyse()...");
//        testInvalidClassFile = new File("src/test/resources/invalid.class");
//        try {
//            if (!testInvalidClassFile.createNewFile()) {
//                throw new Error();
//            }
//        } catch (IOException e) {
//            throw new Error(e);
//        }
//
//        if(testInvalidClassFile == null) {
//            throw new Error("testInvalidClassFile is null.");
//        }

        try {
            ca.analyse(testClassFile);
        } catch (Exception e) {
            throw new Error(e);
        }
        System.out.println("Setup complete.");
    }

    public static void test_InstructionCountGreaterThan0() {
        analyseSetup();
        assertTrue(0 < ca.getInstructionCount());
    }

    public static void test_ExecutionTimeGreaterThan0() {
        assertTrue(0 < ca.getExecutionTime());
    }

//    public static void test_AnalysisException() {
//        Throwable t = null;
//        try {
//            ca.analyse(testInvalidClassFile);
//        } catch(Exception e) {
//            t = e;
//        }
//        assertEquals(t.getClass(), AnalysisException.class);
//    }

}
