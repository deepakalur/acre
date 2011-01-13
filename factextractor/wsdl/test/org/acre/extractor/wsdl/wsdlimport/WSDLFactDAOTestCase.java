package org.acre.extractor.wsdl.wsdlimport;

import junit.framework.TestCase;

/**
 * User: rajmohan@sun.com
 * Date: Oct 18, 2004
 * Time: 4:59:26 PM
 */
public class WSDLFactDAOTestCase extends TestCase {

    private WSDLFactDAO factDAO;
    public WSDLFactDAOTestCase() {
        super();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public WSDLFactDAOTestCase(String s) {
        super(s);    //To change body of overridden methods use File | Settings | File Templates.
    }


    protected void setUp() throws Exception {
        super.setUp();
        factDAO = new WSDLFactDAO("test.sql");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        factDAO.finish();
    }

    public void testCreateInsert() {
        String insert = factDAO.buildInsertStatement("TABLE1", new String[]{"col1, col2"},
                new String[]{"value1, value2"});
        String reference = "REPLACE INTO  TABLE1 ( col1, col2 )  VALUES ( value1, value2 );";
        assertEquals(reference, insert.trim());

        insert = factDAO.buildInsertStatement("TABLE2", new String[]{"col1, col2, col3, " +
                "col4, col5, col6, col7, col8, col9, col10"},
                new String[]{"value1, value2, value3, value4, value5, value6, value7, value8, value9, value10"});
        reference = "REPLACE INTO  TABLE2 ( col1, col2, col3, col4, col5, col6, col7, col8, col9, col10 )  " +
                "VALUES ( value1, value2, value3, value4, value5, value6, value7, value8, value9, value10 );";

        assertEquals(reference, insert.trim());

    }
}
