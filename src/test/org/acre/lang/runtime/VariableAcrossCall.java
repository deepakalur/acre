package org.acre.lang.runtime;

import java.util.Map;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.acre.lang.pql.pdbc.*;
import org.acre.lang.pql.pdbc.PQLResultSet;
import org.acre.lang.pql.pdbc.RDBPQLConnectionImpl;


/**
 * @author Syed Ali
 *
 */
public class VariableAcrossCall extends TestCase {

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testVariablesAcrossCalls() {
        String s;
        Map r;
        PQL pql = PQL.createDatabasePQL();
        pql.getAdaptor().connect();
        s = "define a as select c from classes c where c.shortName LIKE \"A%\";\n"
            +"define ab as select c from a c where c.shortName LIKE \"%b%\";\n"
            +"define b as select c from classes c where c.shortName LIKE \"B%\";\n"
            +"return a, ab, b;";
        Map result1 = pql.executePQL(s);
        Map result2 = pql.executePQL("return select ab from ab");
        PQLResultSet as1 = (PQLResultSet) result1.get("ab");
        PQLResultSet as2 = (PQLResultSet) result2.get("__result__");
        pql.getAdaptor().disconnect();
        assertEquals(as1.getMetaData().getRowCount(), as2.getMetaData().getRowCount());
    }

    public void testVariablesAcrossCalls1() {
        Map map;
        String dbUrl = "jdbc:mysql://localhost:3306/salsa";
        String dbUser = "salsa";
        String dbPassword = "salsa";
        try {
            PQLConnection connection = new RDBPQLConnectionImpl(dbUrl, dbUser, dbPassword);
            PQLStatement statement = connection.createStatement();
            map = statement.executeQuery("define basefilters as select c from classes c; return basefilters;");
            assertTrue(map.size() == 1);
            map = statement.executeQuery("define frontcontrollers as select c  from classes c; return frontcontrollers;");
            assertTrue(map.size() == 1);
            map = statement.executeQuery("select c  from frontcontrollers c;");
            assertTrue(map.size() == 0);
            map = statement.executeQuery("select c  from basefilters c;");
            assertTrue(map.size() == 0);
            connection.close();
        } catch (PQLException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }    
    }
    public void testVariablesAcrossCalls2() {
        Map map;
        String dbUrl = "jdbc:mysql://localhost:3306/salsa";
        String dbUser = "salsa";
        String dbPassword = "salsa";
        try {
            PQLConnection connection = new RDBPQLConnectionImpl(dbUrl, dbUser, dbPassword);
            PQLStatement statement = connection.createStatement();

            map = statement.executeQuery("define basefilters as select c from classes c; return basefilters;");
            assertTrue(map.size() == 1);
            map = statement.executeQuery("return select c from basefilters c;");
            assertTrue(map.size() == 1);

            map = statement.executeQuery("define baseFilters as select c from classes c; return baseFilters;");
            assertTrue(map.size() == 1);
            map = statement.executeQuery("return select c from baseFilters c;");         
            assertTrue(map.size() == 1);

            connection.close();
        } catch (PQLException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }    
    }

    public static Test suite() {
        return new TestSuite(VariableAcrossCall.class);
    }

    public static void main(String args[]) {
        junit.textui.TestRunner.run(suite());
    }
}
