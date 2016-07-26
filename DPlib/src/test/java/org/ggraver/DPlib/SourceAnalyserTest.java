package org.ggraver.DPlib;

/**
 * Created by george on 21/07/16.
 */

import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.expr.NameExpr;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.BeforeClass;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Unit test for SourceAnalyser class.
 */
public class SourceAnalyserTest
        extends TestCase
{
    private static SourceAnalyser sa;
    private static File testJavaFile;
    private static File testInvalidJavaFile;

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public SourceAnalyserTest(String testName)
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( org.ggraver.DPlib.SourceAnalyserTest.class );
    }

    /**
     * Rigourous Test :-)
     */

//    not set up correctly - wrong constructor
    private static void parseSetup() {
        System.out.println("Setting up for test_compile()...");

        testJavaFile = new File("src/test/resources/FibonacciDP.java");
        if(!testJavaFile.exists()) {
            throw new Error("Test .java file does not exist.");
        }

        testInvalidJavaFile = new File("src/test/resources/invalid.java");
        if(!testInvalidJavaFile.exists()) {
            throw new Error("Test .java file (invalid) does not exist.");
        }

        try {
            sa = new SourceAnalyser(testJavaFile, null);
        } catch (IOException e) {
            throw new Error(e);
        } catch (ParseException e) {
            throw new Error(e);
        }

        System.out.println("Setup complete.");
    }

    public static void test_parse() {
        parseSetup();
        try {
            CompilationUnit cu = sa.getCompilationUnit();
            assertEquals("List", cu.getImports().get(0).getName().getName());
            assertEquals("ManagementFactory", cu.getImports().get(1).getName().getName());
            assertEquals("MemoryPoolMXBean", cu.getImports().get(2).getName().getName());
            assertEquals("MemoryUsage", cu.getImports().get(3).getName().getName());
            assertEquals("MemoryType", cu.getImports().get(4).getName().getName());
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    public static void test_parse_ParseException() {
        parseSetup();
        Throwable t;
        try {
            sa = new SourceAnalyser(testInvalidJavaFile, null);
            throw new Error("Expected ParseException.");
        } catch (Throwable ex) {
            t = ex;
        }
        assertTrue(t instanceof ParseException);
    }

    public static void test_parse_IOException() {
        parseSetup();
        Throwable t;
        try {
            sa = new SourceAnalyser(new File("src/"), null);
            throw new Error("Expected ParseException.");
        } catch (Throwable ex) {
            t = ex;
        }
        assertTrue(t instanceof IOException);
    }

}
