package org.acre.lang.test;

/**/

import org.acre.pdmengine.PatternEngineEngineImplTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class TestPDM {

    /**
     * Test suite for otago.agent classes.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(PatternEngineEngineImplTestCase.suite());
        return suite;
    }

    /**
     * Execute all tests.
     */
    public static void main(String[] args) {
        TestRunner.run(TestPDM.suite());
    }
}