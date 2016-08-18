package org.dplib;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.dplib.display.View;
import org.dplib.exception.AnalysisException;
import org.dplib.exception.ModelingException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Unit test for ClassAnalyser class.
 */
public class SolverTest
extends TestCase
{
    private static Solver s;
    private static View testView = new View();
    private static Model testInvalidModel = new Model();
    private static final IO io = new IO();
    private static File testJavaFile;

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public SolverTest(String testName )
    {
        super( testName );

        testJavaFile = new File("src/test/resources/FibonacciDP.java");
        if(!testJavaFile.exists()) { throw new Error("Test .java file does not exist."); }

        testInvalidModel.setCallingMethodBody("test");
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
    public static void test_solve_exceptions() {
//        test invalid model
//        List<Result> results;
//        s = new Solver(testInvalidModel, testJavaFile, testView, io); s.execute();
//        try { results = s.get(); throw new Error("Expected exception."); }
//        catch (Exception e)
//        {
//            assertEquals(ExecutionException.class, e.getClass());
//            assertEquals(AnalysisException.class, e.getCause().getClass());
//            assertEquals("Submitted calling method \"public static void main(String[] args)\" does not match modelled calling method \"null\".", e.getCause().getMessage());
//        }
    }

    public static void test_solve() {
//        setup
        File testInputFile = new File("src/test/resources/testInputFile.txt");
        if(!testInputFile.exists()) { throw new Error("Test input .txt file does not exist."); }

        String[][] testInputs;
        try { testInputs = new FileHandler(testJavaFile.getPath(), "java").parseInputTextFile(testInputFile); }
        catch (IOException e) { throw new Error(e); }

        Model testModel;
        try { testModel = new Modeler().model(testJavaFile, "fibDP", testInputs); }
        catch (ModelingException e) { throw new Error(e); }

        testView.setEditorText(new CodeGenerator().generate(testModel.getClassName(),
                                                            testModel.getCallingMethodDeclaration(),
                                                            testModel.getCallingMethodBody(),
                                                            testModel.getMethodToAnalyseDeclaration()));

//        test
        s = new Solver(testModel, testJavaFile, testView, io); s.execute();
        Analysis analysis;
        try { analysis = s.get(); } catch (Exception e) { throw new Error(e); }

        assertEquals("1", analysis.getResults().get(0).getInput()[0]);
        assertEquals("1", analysis.getResults().get(0).getOutput());
        assertTrue(0 < analysis.getResults().get(0).getExecutionTime());

        assertEquals("5", analysis.getResults().get(1).getInput()[0]);
        assertEquals("5", analysis.getResults().get(1).getOutput());
        assertTrue(0 < analysis.getResults().get(1).getExecutionTime());

        assertEquals("10", analysis.getResults().get(2).getInput()[0]);
        assertEquals("55", analysis.getResults().get(2).getOutput());
        assertTrue(0 < analysis.getResults().get(2).getExecutionTime());

        assertEquals("15", analysis.getResults().get(3).getInput()[0]);
        assertEquals("610", analysis.getResults().get(3).getOutput());
        assertTrue(0 < analysis.getResults().get(3).getExecutionTime());
    }

}
