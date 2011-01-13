package org.acre.lang.pql.pdbc;

import java.util.*;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * RDBPQLConnectionImpl Tester.
 *
 * @author <Authors name>
 * @since <pre>10/13/2004</pre>
 * @version 1.0
 */
public class RDBPQLConnectionImplTest extends TestCase
{
    private RDBPQLConnectionImpl connection;
    String dbUrl = "jdbc:mysql://localhost:3306/salsa";
    String dbUser = "salsa";
    String dbPassword = "salsa";

    public RDBPQLConnectionImplTest(String name)
    {
        super(name);
    }

    public void setUp() throws Exception
    {
        super.setUp();
        connection = new RDBPQLConnectionImpl(dbUrl, dbUser, dbPassword);
    }

    public void tearDown() throws Exception
    {
        super.tearDown();
    }

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

    public void testPrepareStatement() throws Exception
    {
        //assertTrue(connection.prepareStatement()!=null);
    }


    public void testMain() throws Exception
    {
        //TODO: Test goes here...
    }

    public static Test suite()
    {
        return new TestSuite(RDBPQLConnectionImplTest.class);
    }

     /**
     * Runs the test case.
     */
    public static void main(String args[]) {
        junit.textui.TestRunner.run(suite());
    }

    /**
     * Test of isValid method, of class org.acre.lang.pql.pdbc.RDBPQLConnectionImpl.
     */
    public void testIsValid()
            throws PQLException {
        assertTrue(connection.isValid());
        connection.close();
        assertTrue(!connection.isValid());
    }
    /**
     * Test creating statement and running queries with environment variables {system="PSA",version="v1.0"}
     */
    public void testCreateStatementEnvironment1()
            throws PQLException {
        Properties p = new Properties();
        p.put("system", "\"PSA\"");
        p.put("version", "\"v1.0\"");
        runStatementQuery(p);
    }
    /**
     * Test creating statement and running queries with environment variables {system="PSA",version=LATEST}
     */
    public void testCreateStatementEnvironment2()
            throws PQLException {
        Properties p = new Properties();
        p.put("system", "\"PSA\"");
        p.put("version", "LATEST");
        runStatementQuery(p);
    }

    /**
     * Test creating statement and running queries with environment variables{system=NULL,version=NULL}
     */
    public void testCreateStatementNullEnvironment()
            throws PQLException {
        Properties p = new Properties();
        p.put("system", "NULL");
        p.put("version", "NULL");
        runStatementQuery(p);
    }
    /**
     * Test creating statement and running queries with NO environment variables {}
     */
    public void testCreateStatementNoEnvironment1()
            throws PQLException {
        Properties p = new Properties();
        runStatementQuery(p);
    }
    /**
     * Test creating statement and running queries with NULL environment variables null
     */
    public void testCreateStatementNoEnvironment2()
            throws PQLException {
        runStatementQuery(null);
    }


    private void runStatementQuery(Properties p) throws PQLException {
        connection.setEnvironment(p);
        PQLStatement st = connection.createStatement();
        Map m = st.executeQuery("return SELECT c FROM JClass c WHERE shortName LIKE \"H%\";");
        assertTrue(m != null);
        connection.close();
    }
    
    /**
     * Test preparing statement and running queries with environment variables {system="PSA",version="v1.0"}
     */
    public void testPrepareStatementEnvironment1()
            throws PQLException {
        Properties p = new Properties();
        p.put("system", "\"PSA\"");
        p.put("version", "\"v1.0\"");
        runPreparedStatementQuery(p);
    }
    /**
     * Test preparing statement and running queries with environment variables {system="PSA",version=LATEST}
     */
    public void testPrepareStatementEnvironment2()
            throws PQLException {
        Properties p = new Properties();
        p.put("system", "\"PSA\"");
        p.put("version", "LATEST");
        runPreparedStatementQuery(p);
    }

    /**
     * Test preparing statement and running queries with environment variables{system=NULL,version=NULL}
     */
    public void testPrepareStatementNullEnvironment()
            throws PQLException {
        Properties p = new Properties();
        p.put("system", "NULL");
        p.put("version", "NULL");
        runPreparedStatementQuery(p);
    }
    /**
     * Test preparing statement and running queries with NO environment variables {}
     */
    public void testPrepareStatementNoEnvironment1()
            throws PQLException {
        Properties p = new Properties();
        runPreparedStatementQuery(p);
    }
    /**
     * Test preparing statement and running queries with NULL environment variables null
     */
    public void testPrepareStatementNoEnvironment2()
            throws PQLException {
        runPreparedStatementQuery(null);
    }

    
    private void runPreparedStatementQuery(Properties p) throws PQLException {
        connection.setEnvironment(p);
        PQLPreparedStatement st = connection.prepareStatement("return SELECT c FROM JClass c WHERE shortName LIKE \"H%\";");
        Map m = st.executeQuery();
        assertTrue(m != null);
        connection.close();
    }
}
