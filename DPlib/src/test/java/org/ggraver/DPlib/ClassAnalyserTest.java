package org.ggraver.DPlib;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.ggraver.DPlib.Exception.CompileException;
import org.junit.Rule;

import java.io.File;

/**
 * Unit test for simple App.
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
        return new TestSuite( org.ggraver.DPlib.ClassAnalyserTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testClassAnalyser()
    {
        setup();
        runTests();
    }

    static void setup() {
        System.out.println("Setting up...");
        ca = new ClassAnalyser();
        System.out.println("file path: " + System.getProperty("user.dir"));
        testClassFile = new File("src/test/resources/dfib.class");

        if(!testClassFile.exists()) {
            throw new Error("Test .class file does not exist.");
        }

        testJavaFile = new File("src/test/resources/dfib.java");

        if(!testJavaFile.exists()) {
            throw new Error("Test .class file does not exist.");
        }
    }

    static void runTests() {
        compileTest();
        compileExceptionTest();
    }

    static void compileTest() {
        try {
            assertEquals(testClassFile, ca.compile(testJavaFile));
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    @org.junit.Test(expected = CompileException.class)
    static void compileExceptionTest() {
        try {
            assertEquals(testClassFile, ca.compile(null));
        } catch (Exception e) {
            assertEquals(CompileException.class, e.getClass());
//            assertEquals();
        }
    }

}
