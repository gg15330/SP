package org.dplib;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.dplib.exception.CompileException;

import java.io.File;

/**
 * Unit test for ClassAnalyser class.
 */
public class SourceCompilerTest
    extends TestCase
{
    private static SourceCompiler sc = new SourceCompiler();
    private static File testJavaFile;
    private static File testClassFile;
    private static File testInvalidJavaFile;

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public SourceCompilerTest(String testName )
    {
        super( testName );

        testJavaFile = new File("src/test/resources/FibonacciDP.java");
        if(!testJavaFile.exists()) { throw new Error("Test .java file does not exist."); }

        testInvalidJavaFile = new File("src/test/resources/testInvalidJavaFile.java");
        if(!testJavaFile.exists()) { throw new Error("Test invalid .java file does not exist."); }
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( SourceCompilerTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public static void test_compile_exception() {
//        test invalid class file
        try { File invalid = sc.compile(testInvalidJavaFile, "test"); throw new Error("Expected CompileException."); }
        catch (CompileException e) { assertEquals("Could not compile .java file. Please ensure your .java file is valid.", e.getMessage()); }
        catch (Exception e) { throw new Error(e); }

//        test invalid class name
        try { File invalid = sc.compile(testJavaFile, "INVALID"); throw new Error("Expected CompileException."); }
        catch (CompileException e) { assertEquals("Could not find .class file: INVALID", e.getMessage()); }
        catch (Exception e) { throw new Error(e); }
    }

    public static void test_compile() {
        try { testClassFile = sc.compile(testJavaFile, "FibonacciDP"); } catch (Exception e) { throw new Error(e); }
        assertTrue(testClassFile.exists());
    }

}
