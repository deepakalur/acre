/*
 * Copyright (c) 2004
 * Sun Microsystems, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Sun
 * Microsystems, Inc. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Sun.
 *
 * SUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. SUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 */
package org.acre.lang.pql.pdbc;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.HashSet;

/**
 * PQLPreparedStatementImpl Tester.
 *
 * @author <Authors name>
 * @since <pre>10/15/2004</pre>
 * @version 1.0
 */
public class PQLPreparedStatementImplTest extends TestCase
{
    PQLPreparedStatementImpl stmt;

    private static String param = "names";
    private static String query = "select c from classes c where c.name in (:"+ param + ")";

    public PQLPreparedStatementImplTest(String name)
    {
        super(name);
    }

    public void setUp() throws Exception
    {
        super.setUp();
        stmt = new PQLPreparedStatementImpl(null, query);
    }

    public void tearDown() throws Exception
    {
        super.tearDown();
    }

    public void testSetParameter() throws Exception
    {
        stmt.setParameter(param, "Hello");
    }

    public void testSetParameter1() throws Exception
    {
        stmt.setParameter(param, 1);
        assertTrue(stmt.getParameter(param) instanceof Integer);
        assertTrue(((Integer)stmt.getParameter(param)).intValue() == 1);
    }

    public void testSetParameter2() throws Exception
    {
        stmt.setParameter(param, 2.5f);
        assertTrue(stmt.getParameter(param) instanceof Float);
        assertTrue(((Float)stmt.getParameter(param)).floatValue() == 2.5f);
    }

    public void testSetParameter3() throws Exception
    {
        stmt.setParameter(param, true);
        assertTrue(stmt.getParameter(param) instanceof Boolean);
        assertTrue(((Boolean)stmt.getParameter(param)).booleanValue() == true);
    }

    public void testSetParameter4() throws Exception
    {
        ArrayList vals = new ArrayList();
        vals.add("com.sun.classA");
        vals.add("com.sun.classB");
        stmt.setParameter(param, vals);
        assertTrue(stmt.getParameter(param) instanceof Collection);
        Collection c = (Collection) stmt.getParameter(param);
        assertTrue(!c.isEmpty());
        assertTrue(c.size() == vals.size());
    }

    public void testGetParameter() throws Exception
    {
        stmt.setParameter(param, true);
        assertTrue(stmt.getParameter(param) instanceof Boolean);
    }

    public void testClearParameters() throws Exception
    {
        stmt.clearParameters();
        assertTrue(stmt.getParameters().size()==0);
    }

    public void testGetProcessedQuery() throws Exception
    {
        ArrayList vals = new ArrayList();
        vals.add("com.sun.classA");
        vals.add("com.sun.classB");
        stmt.setParameter(param, vals);
        assertTrue(stmt.getProcessedQuery() != null);
    }

    public void testExecuteQuery() throws Exception
    {
        //TODO: Test goes here...
    }

    public void testGetParameters() throws Exception
    {
        // add 2 parameters and see what comes back
        stmt.setParameter(param, "Hello");
        stmt.setParameter(param, "World");
        assertTrue(stmt.getParameters() != null);
        assertTrue(stmt.getParameters().size() == 2);
    }

    public void testToString() throws Exception
    {
        assertTrue(stmt.toString() != null);
    }

    public void testGetReturnVariableNames() throws PQLException {
        String query =  " include BusinessDelegate; " +
                        "define cls1 as select c from classes c; " +
                        "define cls2 as select c from classes c where c.isInterface=\"true\" ; " +
                        " return cls1, cls2; ";
        PQLPreparedStatement stmt = new PQLPreparedStatementImpl(null, query);
        String rets[] = stmt.getReturnVariableNames();

        Set ref = new HashSet();
        ref.add("cls1");
        ref.add("cls2");
        assertTrue(rets.length==2);
        for ( int i=0; i<rets.length; i++) {
            assertTrue(ref.contains(rets[i]));
            System.out.println(rets[i]);
        }


    }

    public void testGetReturnVariableNames2() throws PQLException {
        String query =  "include BusinessDelegate; " +
                        "include Singleton; " +
                        "define cls3 as select c from singletons c; " +
                        "include SessionFacade; " +
                        "define cls1 as select c from classes c; " +
                        "define cls2 as select c from classes c where c.isInterface=\"true\" ; " +
                        " return cls1, cls2, cls3; ";
        PQLPreparedStatement stmt = new PQLPreparedStatementImpl(null, query);
        String rets[] = stmt.getReturnVariableNames();

        Set ref = new HashSet();
        ref.add("cls1");
        ref.add("cls2");
        ref.add("cls3");
        assertTrue(rets.length == 3);
        for ( int i=0; i<rets.length; i++) {
            assertTrue(ref.contains(rets[i]));
            System.out.println(rets[i]);
        }

    }


    public void testGetReturnVariableNames3() throws PQLException {
        String query =  "include BusinessDelegate; " +
                        "include Singleton; " +
                        "define cls3 as select c from singletons c; " +
                        "include SessionFacade; " +
                        "return SFBeans, SFInterfaces, SFHomes; ";
        PQLPreparedStatement stmt = new PQLPreparedStatementImpl(null, query);
        String rets[] = stmt.getReturnVariableNames();

        Set ref = new HashSet();
        ref.add("SFBeans");
        ref.add("SFInterfaces");
        ref.add("SFHomes");
        assertTrue(rets.length==3);
        for ( int i=0; i<rets.length; i++) {
            assertTrue(ref.contains(rets[i]));
            System.out.println(rets[i]);
        }

    }



    public static Test suite()
    {
        return new TestSuite(PQLPreparedStatementImplTest.class);
    }

    public static void main(String args[]) {
        junit.textui.TestRunner.run(suite());
    }

    /**
     * Test of main method, of class org.acre.lang.pql.pdbc.PQLPreparedStatementImpl.
     */
    public void testMain() {

        System.out.println("testMain");

        // TODO add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
}
