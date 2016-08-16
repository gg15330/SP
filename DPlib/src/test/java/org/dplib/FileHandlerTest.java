package org.dplib;

/**
 * Created by george on 21/07/16.
 */

import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.ModifierVisitorAdapter;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.dplib.exception.AnalysisException;

import java.io.File;
import java.io.IOException;

/**
 * Unit test for SourceAnalyser class.
 */
public class FileHandlerTest
        extends TestCase
{
    private static FileHandler fh;
    private static File testJavaFile;
    private static File invalidJavaFile;
    private static File testModelFile;
    private static Model testModel;

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

        invalidJavaFile = new File("src/test/resources/invalid.java");
        if(!invalidJavaFile.exists()) {
            throw new Error("Test .java file (invalid) does not exist.");
        }

        testModelFile = new File("src/test/resources/testModelFile.mod");
        if(!testModelFile.exists()) {
            throw new Error("Test .mod file does not exist.");
        }

        testModel = new Model();
        testModel.setClassName("Test");
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

    public static void test_constructor_exceptions()
    {
//        test invalid name
        try { fh = new FileHandler("INVALID", "java"); }
        catch (Exception e) { assert(e.getMessage().equals("Could not find file: INVALID.java")); }
//        test invalid extension
        try { fh = new FileHandler(testJavaFile.getPath(), "INVALID"); }
        catch (Exception e) { assert(e.getMessage().equals("Input file \"src/test/resources/FibonacciDP.java\" does not match required file extension \"INVALID\".")); }
    }

    public static void test_constructor()
    {
        try { fh = new FileHandler(testJavaFile.getPath(), "java"); }
        catch (IOException e) { throw new Error(e); }
        assertEquals(testJavaFile, fh.getFile());
    }

    public static void test_serializeModel()
    {
        try { fh = new FileHandler(testJavaFile.getPath(), "java"); }
        catch (IOException e) { throw new Error(e); }
        assertEquals(0, fh.serializeModel(testModel));
        File f = new File("src/test/resources/FibonacciDP.mod");
        assertTrue(f.exists()); f.delete();
    }

    public static void test_deserializeModelFile()
    {
        try { fh = new FileHandler(testModelFile.getPath(), "mod"); }
        catch (IOException e) { throw new Error(e); }
        assertEquals("Test", fh.deserializeModelFile().getClassName());
    }

}
