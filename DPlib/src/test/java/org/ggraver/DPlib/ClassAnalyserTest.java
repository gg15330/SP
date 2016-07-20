package org.ggraver.DPlib;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.File;

/**
 * Unit test for simple App.
 */
public class ClassAnalyserTest
    extends TestCase
{
    private static ClassAnalyser ca;
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
    }

    static void runTests() {
        test_tempFile();
    }

    public static void test_tempFile() {
        File f = new File("test.txt", "src/test/java/org/ggraver/DPlib");
        assertEquals(true, true);
    }

}
