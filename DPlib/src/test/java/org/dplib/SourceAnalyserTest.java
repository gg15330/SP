package org.dplib;

/**
 * Created by george on 21/07/16.
 */

import com.github.javaparser.ParseException;
import com.github.javaparser.ast.body.MethodDeclaration;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.dplib.analyse.AnalysisException;
import org.dplib.analyse.ProblemType;
import org.dplib.analyse.SourceAnalyser;

import java.io.File;
import java.io.IOException;

/**
 * Unit test for SourceAnalyser class.
 */
public class SourceAnalyserTest
        extends TestCase
{
    private static SourceAnalyser sa = new SourceAnalyser();
    private static File testRecursiveJavaFile;
    private static File testMemoizedJavaFile;
    private static File testIterativeJavaFile;
    private static File testUndefinedJavaFile;
    private static File testInvalidJavaFile;

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public SourceAnalyserTest(String testName)
    {
        super(testName);

        testRecursiveJavaFile = new File("src/test/resources/FibonacciRecursive.java");
        if (!testRecursiveJavaFile.exists()) { throw new Error("Test recursive .java file does not exist."); }

        testMemoizedJavaFile = new File("src/test/resources/FibonacciMemo.java");
        if (!testMemoizedJavaFile.exists()) { throw new Error("Test memoized .java file does not exist."); }

        testIterativeJavaFile = new File("src/test/resources/FibonacciDP.java");
        if (!testIterativeJavaFile.exists()) { throw new Error("Test iterative .java file does not exist."); }

        testUndefinedJavaFile = new File("src/test/resources/FibonacciUndefined.java");
        if (!testUndefinedJavaFile.exists()) { throw new Error("Test undefined .java file does not exist."); }

        testInvalidJavaFile = new File("src/test/resources/testInvalidJavaFile.java");
        if (!testInvalidJavaFile.exists()) { throw new Error("Test .java file (invalid) does not exist."); }
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite(SourceAnalyserTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public static void test_parse_exceptions()
    {
        try { sa.parse(new File("")); throw new Error("Expected Exception."); }
        catch (Exception e) { assertEquals(ParseException.class, e.getClass()); }

        try { sa.parse(testInvalidJavaFile); throw new Error("Expected Exception."); }
        catch (Exception e) { assertEquals(ParseException.class, e.getClass()); }
    }

    public static void test_parse()
    {
        try { sa.parse(testIterativeJavaFile); }
        catch (Exception e) { throw new Error(e); }
        assertEquals("FibonacciDP", sa.getClassName());
    }

    public static void test_locateMethod_exceptions()
    {
        try { sa.parse(testIterativeJavaFile); }
        catch (IOException | ParseException e) { throw new Error(e); }
        try { MethodDeclaration md = sa.locateMethod("INVALID"); throw new Error("Expected AnalysisException."); }
        catch (AnalysisException e) { assertEquals("expected MethodDeclaration \"INVALID\" does not exist in source.", e.getMessage()); }
    }

    public static void test_locateMethod()
    {
        try { sa.parse(testIterativeJavaFile); }
        catch (IOException | ParseException e) { throw new Error(e); }
        MethodDeclaration md;
        try { md = sa.locateMethod("fibDP"); }
        catch (Exception e) { throw new Error(e); }
        assertEquals("fibDP", md.getName());
    }

    public static void test_determineProblemType()
    {
        MethodDeclaration md;

//        test recursive type
        try { sa.parse(testRecursiveJavaFile); } catch (Exception e) { throw new Error(e); }
        try { md = sa.locateMethod("fibRec"); } catch (Exception e) { throw new Error(e); }
        assertEquals(ProblemType.RECURSIVE, sa.determineProblemType(md));

//        test memoized type
        try { sa.parse(testMemoizedJavaFile); } catch (Exception e) { throw new Error(e); }
        try { md = sa.locateMethod("fibMem"); } catch (Exception e) { throw new Error(e); }
        assertEquals(ProblemType.MEMOIZED, sa.determineProblemType(md));

//        test iterative type
        try { sa.parse(testIterativeJavaFile); } catch (Exception e) { throw new Error(e); }
        try { md = sa.locateMethod("fibDP"); } catch (Exception e) { throw new Error(e); }
        assertEquals(ProblemType.ITERATIVE, sa.determineProblemType(md));

//        test undefined type
        try { sa.parse(testUndefinedJavaFile); } catch (Exception e) { throw new Error(e); }
        try { md = sa.locateMethod("fibUndefined"); } catch (Exception e) { throw new Error(e); }
        assertEquals(ProblemType.UNDEFINED, sa.determineProblemType(md));
    }

}
