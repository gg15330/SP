package org.dplib;

/**
 * Created by george on 21/07/16.
 */

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.dplib.analyse.Model;

import java.io.File;
import java.io.IOException;

/**
 * Unit test for SourceAnalyser class.
 */
public class FileHandlerTest
        extends TestCase
{
    private static FileHandler fh = new FileHandler();
    private static File testJavaFile;
    private static File testInvalidJavaFile;
    private static File testModelFile;
    private static File testInputFile;
    private static Model testModel = new Model();

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public FileHandlerTest(String testName)
    {
        super( testName );

        testJavaFile = new File("src/test/resources/FibonacciDP.java");
        if(!testJavaFile.exists()) {
            throw new Error("Test .java file does not exist.");
        }

        testInvalidJavaFile = new File("src/test/resources/testInvalidJavaFile.java");
        if(!testInvalidJavaFile.exists()) {
            throw new Error("Test .java file (invalid) does not exist.");
        }

        testInputFile = new File("src/test/resources/testInputFile.txt");
        if(!testInputFile.exists()) {
            throw new Error("Test input .txt file does not exist.");
        }

        testModelFile = new File("src/test/resources/testModelFile.mod");
//        if(!testModelFile.exists()) {
//            throw new Error("Test .mod file does not exist.");
//        }

        testModel.setClassName("testModelFile");
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( FileHandlerTest.class );
    }

    /**
     * Rigourous Test :-)
     */

    public static void test_locateFile_exceptions()
    {
//        test invalid name
        try { File invalid = fh.locateFile("INVALID", "java"); throw new Error("Expected Exception."); }
        catch (Exception e) { assertEquals("Could not find file: INVALID", e.getMessage()); }
//        test invalid extension
        try { File invalidExt = fh.locateFile(testJavaFile.getPath(), "INVALID"); throw new Error("Expected Exception."); }
        catch (Exception e) { assertEquals("Input file \"src/test/resources/FibonacciDP.java\" does not match required file extension \"INVALID\".", e.getMessage()); }
    }

    public static void test_locateFile()
    {
        File test;
        try { test = fh.locateFile(testJavaFile.getPath(), "java"); }
        catch (IOException e) { throw new Error(e); }
        assertEquals(testJavaFile, test);
    }

    public static void test_createTempJavaFile()
    {
        File test;
        try { test = fh.createTempJavaFile(new File("src/test/resources"), "test"); }
        catch (IOException e) { throw new Error(e); }
        assertTrue(test.exists());
    }

    public static void test_parseInputTextFile_exceptions()
    {
        File invalid = new File("INVALID");
        try { String[][] inputs = fh.parseInputTextFile(invalid); throw new Error("Expected Exception."); }
        catch (Exception e) { assertTrue(e instanceof IOException); }
    }

    public static void test_parseInputTextFile()
    {
        String[][] inputs;
        try { inputs = fh.parseInputTextFile(testInputFile); }
        catch (Exception e) { throw new Error(e); }
        assertEquals("1", inputs[0][0]);
        assertEquals("5", inputs[1][0]);
        assertEquals("10", inputs[2][0]);
        assertEquals("15", inputs[3][0]);
    }

    public static void test_serializeModel()
    {
        testModelFile = fh.serializeModel(testModel, new File("src/test/resources"));
        assertTrue(testModelFile.exists());
    }

    public static void test_deserializeModelFile()
    {
        assertEquals("testModelFile", fh.deserializeModelFile(testModelFile).getClassName());
    }

}
