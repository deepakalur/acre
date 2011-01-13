/*
 * TDBPQLConnectionImplTest.java
 * JUnit based test
 *
 * Created on October 26, 2004, 1:33 PM
 */

package org.acre.lang.pql.pdbc;

import junit.framework.TestCase;

/**
 *
 * @author Administrator
 */
public class TDBPQLConnectionImplTest extends TestCase {

    private TDBPQLConnectionImpl connection;
    String factDB = "src/main/AcreRepository/Global/ApplicationData/FactDB/PSA/psaFacts.ta";

    public TDBPQLConnectionImplTest(String testName) {
        super(testName);
    }

    protected void setUp() throws java.lang.Exception {
        super.setUp();
        connection = new TDBPQLConnectionImpl(factDB);
    }

    protected void tearDown() throws java.lang.Exception {
        super.tearDown();
    }

    public static junit.framework.Test suite() {

        junit.framework.TestSuite suite = new junit.framework.TestSuite(TDBPQLConnectionImplTest.class);
        
        return suite;
    }

    /**
     * Test of close method, of class org.acre.lang.pql.pdbc.TDBPQLConnectionImpl.
     */

    public void testClose() throws Exception
    {
        connection.close();
        assertTrue(connection.isClosed());
    }

    public void testIsClosed() throws Exception
    {
        assertFalse(connection.isClosed());
        connection.close();
        assertTrue(connection.isClosed());
    }

    public void testCreateStatement() throws Exception
    {
        assertTrue(connection.createStatement()!=null);
    }

    /**
     * Test of prepareStatement method, of class org.acre.lang.pql.pdbc.TDBPQLConnectionImpl.
     */
    public void testPrepareStatement() {

    }


    /**
    * Runs the test case.
    */
   public static void main(String args[]) {
       junit.textui.TestRunner.run(suite());
   }

    // TODO add test methods here. The name must begin with 'test'. For example:
    // public void testHello() {}
    
}
