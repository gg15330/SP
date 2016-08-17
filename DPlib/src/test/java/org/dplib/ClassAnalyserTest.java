package org.dplib;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.dplib.exception.AnalysisException;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Unit test for ClassAnalyser class.
 */
public class ClassAnalyserTest
    extends TestCase
{
    private static ClassAnalyser ca = new ClassAnalyser();
    private static File testJavaFile;
    private static File testClassFile;
    private static File testEmptyClassFile;
    private static File testClassFileRuntimeException;
    private static String[][] testInputs;
    private static String[][] testInvalidInputs;

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ClassAnalyserTest(String testName )
    {
        super( testName );

        testJavaFile = new File("src/test/resources/FibonacciDP.java");
        if(!testJavaFile.exists()) { throw new Error("Test .java file does not exist."); }

        testClassFile = new File("src/test/resources/testClassFile.class");
        if(!testClassFile.exists()) { throw new Error("Test .class file does not exist."); }

        testEmptyClassFile = new File("src/test/resources/testEmptyClassFile.class");
        if(!testEmptyClassFile.exists()) { throw new Error("Test empty .class file does not exist."); }

        testClassFileRuntimeException = new File("src/test/resources/testJavaFileRuntimeException.class");
        if(!testClassFileRuntimeException.exists()) { throw new Error("Test .java file does not exist."); }

        File testInputFile = new File("src/test/resources/testInputFile.txt");
        if(!testInputFile.exists()) { throw new Error("Test input file does not exist."); }
        try { testInputs = new FileHandler(testInputFile.getPath(), "txt").parseInputTextFile(testInputFile); }
        catch (IOException e) { throw new Error(e); }
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
    public static void test_analyse_exceptions() {
//        test nonexistent class file
        try{ ca.analyse(new File(""), new String[0][0]); throw new Error("Expected AnalysisException."); }
        catch (AnalysisException ae) { assertEquals(".class file does not exist.", ae.getMessage()); }
        catch (Exception e) { throw new Error(e); }

//        test empty class file
        try{ ca.analyse(testEmptyClassFile, new String[0][0]); throw new Error("Expected AnalysisException."); }
        catch (AnalysisException ae) { assertEquals(".class file is empty.", ae.getMessage()); }
        catch (Exception e) { throw new Error(e); }

//        test empty input array
        try{ ca.analyse(testClassFile, new String[0][0]); throw new Error("Expected AnalysisException."); }
        catch (AnalysisException ae) { assertEquals("Input array is empty.", ae.getMessage()); }
        catch (Exception e) { throw new Error(e); }

//        test class file with runtime exception
        try{ ca.analyse(testClassFileRuntimeException, testInputs); throw new Error("Expected AnalysisException."); }
        catch (AnalysisException ae) { assertEquals("Program did not execute correctly - check code (and inputs if modeling a problem).", ae.getMessage()); }
        catch (Exception e) { throw new Error(e); }
    }

    public static void test_analyse() {
        List<Result> results;
        try{ results = ca.analyse(testClassFile, testInputs); } catch (AnalysisException e) { throw new Error(e); }

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
