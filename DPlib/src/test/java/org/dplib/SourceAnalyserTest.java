package org.dplib;

/**
 * Created by george on 21/07/16.
 */

import com.github.javaparser.ParseException;
import com.github.javaparser.StreamProvider;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.dplib.exception.AnalysisException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Unit test for SourceAnalyser class.
 */
public class SourceAnalyserTest
        extends TestCase
{
    private static SourceAnalyser sa;
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
    public static void test_constructor_exceptions()
    {
        try { sa = new SourceAnalyser(null, null); throw new Error("Expected Exception."); }
        catch (Exception e) { assertEquals(NullPointerException.class, e.getClass()); }
        try { sa = new SourceAnalyser(new File("INVALID"), null); throw new Error("Expected Exception."); }
        catch (Exception e) { assertEquals(FileNotFoundException.class, e.getClass()); }
        try { sa = new SourceAnalyser(testInvalidJavaFile, null); throw new Error("Expected Exception."); }
        catch (Exception e) { assertEquals(ParseException.class, e.getClass()); }
    }

    public static void test_constructor()
    {
        try { sa = new SourceAnalyser(testIterativeJavaFile, "test"); } catch (Exception e) { throw new Error(e); }
        assertEquals("test", sa.getMethodName());
        assertEquals("FibonacciDP", sa.getCompilationUnit().getTypes().get(0).getName());
    }

    public static void test_findMethod_exceptions()
    {
        try { sa = new SourceAnalyser(testIterativeJavaFile, "test"); } catch (Exception e) { throw new Error(e); }
        try { MethodDeclaration md = sa.findMethod("INVALID"); throw new Error("Expected AnalysisException."); }
        catch (AnalysisException e) { assertEquals("expected MethodDeclaration \"INVALID\" does not exist in source.", e.getMessage()); }
    }

    public static void test_findMethod()
    {
        try { sa = new SourceAnalyser(testIterativeJavaFile, "fibDP"); } catch (Exception e) { throw new Error(e); }
        MethodDeclaration md;
        try { md = sa.findMethod("fibDP"); } catch (AnalysisException e) { throw new Error(e); }
        assertEquals("fibDP", md.getName());
    }

    public static void test_determineProblemType()
    {
//        test recursive type
        try { sa = new SourceAnalyser(testRecursiveJavaFile, "fibRec"); } catch (Exception e) { throw new Error(e); }
        try { sa.analyse(); } catch (AnalysisException e) { throw new Error(e); }
        assertEquals(ProblemType.RECURSIVE, sa.determineProblemType());

//        test memoized type
        try { sa = new SourceAnalyser(testMemoizedJavaFile, "fibMem"); } catch (Exception e) { throw new Error(e); }
        try { sa.analyse(); } catch (AnalysisException e) { throw new Error(e); }
        assertEquals(ProblemType.MEMOIZED, sa.determineProblemType());

//        test iterative type
        try { sa = new SourceAnalyser(testIterativeJavaFile, "fibDP"); } catch (Exception e) { throw new Error(e); }
        try { sa.analyse(); } catch (AnalysisException e) { throw new Error(e); }
        assertEquals(ProblemType.ITERATIVE, sa.determineProblemType());

//        test undefined type
        try { sa = new SourceAnalyser(testUndefinedJavaFile, "fibUndefined"); }
        catch (Exception e)
        {
            throw new Error(e);
        }
        try { sa.analyse(); } catch (AnalysisException e) { throw new Error(e); }
        assertEquals(ProblemType.UNDEFINED, sa.determineProblemType());
    }

    public static void test_statementsToStringArray()
    {
        MethodDeclaration md;
        try { sa = new SourceAnalyser(testIterativeJavaFile, "fibDP"); } catch (Exception e) { throw new Error(e); }
        try { md = sa.findMethod("main"); } catch (AnalysisException e) { throw new Error(e); }
        List<Statement> statements = new ArrayList<>(md.getBody().getStmts());
        assertEquals("int n = Integer.parseInt(args[0]);", sa.statementsToStringArray(statements)[0]);
        assertEquals("System.out.println(fibDP(n));", sa.statementsToStringArray(statements)[1]);
    }

}
