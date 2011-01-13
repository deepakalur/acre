package org.acre.lang.runtime;

import java.util.Map;

import org.acre.lang.pql.pdbc.PQLResultSet;

import junit.framework.TestCase;


/**
 * @author Syed Ali
 *
 */
public class CheckSystemSQL extends TestCase {

    private DatabaseAdapter da;
    public static void main(String[] args) {
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        da = new DatabaseAdapter(null);
        da.connect();
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        da.disconnect();
    }
    
    public void testGetLatestSystemId() {
        int latestSystemId = da.getLatestSystemId();
        System.out.println(latestSystemId);
        assertTrue(latestSystemId > DatabaseAdapter.SYSTEM_NOT_INITIALIZED);
    }
    public int callGetSystemId(String system, String version, String timestamp) {
        int theSystemId;
        theSystemId = da.getSystemId(system, version, timestamp);
        System.out.println(theSystemId);
        return theSystemId;
    }
    public String callGetSystemIds(String system, String version, String timestamp) {
        String systemIds;
        systemIds = da.getSystemIds(system, version, timestamp);
        System.out.println("da.getSystemIds("+system+", "+version+", "+timestamp+"): "+systemIds);
        return systemIds;
    }
    public void testFindSystem() {
        int theSystemId;
        theSystemId = callGetSystemId("SALSA", null, null);
        assertTrue(theSystemId > DatabaseAdapter.SYSTEM_UNDEFINED);
        theSystemId = callGetSystemId(null, "V1.0", null);
        assertTrue(theSystemId > DatabaseAdapter.SYSTEM_UNDEFINED);
        theSystemId = callGetSystemId(null, null, "2005-02-04 14:33:32");
        assertTrue(theSystemId > DatabaseAdapter.SYSTEM_UNDEFINED);
        theSystemId = callGetSystemId("SALSA", "V1.0", null);
        assertTrue(theSystemId > DatabaseAdapter.SYSTEM_UNDEFINED);
        theSystemId = callGetSystemId("SALSA", "V1.0", "2005-02-04 14:33:32");
        assertTrue(theSystemId > DatabaseAdapter.SYSTEM_UNDEFINED);
    }
    public void testDoNotFindSystem() {
        int theSystemId;
        theSystemId = callGetSystemId(null, null, null);
        assertEquals(theSystemId, DatabaseAdapter.SYSTEM_UNDEFINED);
        theSystemId = callGetSystemId("DASL", null, null);
        assertEquals(theSystemId, DatabaseAdapter.SYSTEM_UNDEFINED);
        theSystemId = callGetSystemId(null, "V111", null);
        assertEquals(theSystemId, DatabaseAdapter.SYSTEM_UNDEFINED);
        theSystemId = callGetSystemId(null, null, "2005-02-04 14:33:31");
        assertEquals(theSystemId, DatabaseAdapter.SYSTEM_UNDEFINED);
        theSystemId = callGetSystemId("SALSA1", "V1.0", "2005-02-04 14:33:32");
        assertEquals(theSystemId, DatabaseAdapter.SYSTEM_UNDEFINED);
    }
    
    public void testFindSystems() {
        String systemIds;
        systemIds = callGetSystemIds("SALSA", null, null);
        assertNotNull(systemIds);
        systemIds = callGetSystemIds(null, "V1.0", null);
        assertNotNull(systemIds);
        systemIds = callGetSystemIds(null, null, "2005-02-04 14:33:32");
        assertNotNull(systemIds);
        systemIds = callGetSystemIds("SALSA", "V1.0", null);
        assertNotNull(systemIds);
        systemIds = callGetSystemIds("SALSA", "V1.0", "2005-02-04 14:33:32");
        assertNotNull(systemIds);
    }
    public void testDoNotFindSystems() {
        String systemIds;
        systemIds = callGetSystemIds(null, null, null);
        assertNull(systemIds);
        systemIds = callGetSystemIds("DASL", null, null);
        assertNull(systemIds);
        systemIds = callGetSystemIds(null, "V111", null);
        assertNull(systemIds);
        systemIds = callGetSystemIds(null, null, "2005-02-04 14:33:31");
        assertNull(systemIds);
        systemIds = callGetSystemIds("SALSA1", "V1.0", "2005-02-04 14:33:32");
        assertNull(systemIds);
    }
}
