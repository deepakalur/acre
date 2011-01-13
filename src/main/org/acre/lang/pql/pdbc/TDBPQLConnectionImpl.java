/**
 *   Copyright 2004-2005 Sun Microsystems, Inc.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.acre.lang.pql.pdbc;

import org.acre.lang.runtime.PQL;

import java.util.Map;
import java.util.Properties;

/**
 * @author Deepak.Alur@Sun.com
 *         Date: Oct 12, 2004
 *         Time: 10:50:08 PM
 */
public class TDBPQLConnectionImpl implements PQLConnection{
    boolean isOpen=false;
    PQL pql;
    Properties env = null;

    public TDBPQLConnectionImpl(String factdbpath)
        throws PQLException {

        pql = PQL.createQLPQL(factdbpath);
        pql.getAdaptor().connect();
        isOpen = true;
    }

    public void close()  throws PQLException {
        // todo
        pql.getAdaptor().disconnect();
        isOpen = false;
    }

    public boolean isClosed()  throws PQLException {
        return !isOpen;
    }

    public PQLStatement createStatement()  throws PQLException {
        checkOpenValid();
        return new PQLStatementImpl(this);
    }

    private void checkOpenValid()
            throws PQLException {
        if (isClosed()) {
            throw new PQLException("Connection is closed or invalid. Cannot perform operation");
        }
    }

    public PQLPreparedStatement prepareStatement(String query) throws PQLException {
        checkOpenValid();
        return new PQLPreparedStatementImpl(this, query);
    }

    public PQL getPQL()
            throws PQLException {
        return pql;
    }

    public void fetchComplete(PQLValueHolder data)
            throws PQLException {
        checkOpenValid();
        pql.fetch(data);
    }

    public Map executePQL(String query)
            throws PQLException {
        checkOpenValid();
        return pql.executePQL(query);
    }

    public static void main(String args[]) {
        String dbUrl = "/dev/salsadevcvs/salsa/PSA/factDatabase.ta";

        try {
            PQLConnection conn = PQLConnectionFactory.createPQLConnectionToTDB(dbUrl);

            PQLStatement stmt = conn.createStatement();
            Map resultsMap = stmt.executeQuery("return select c from classes c;");
            conn.close();
            System.out.println("Got result = " + resultsMap.values().size());
            if (resultsMap.size() > 0) {
                Object resultObj = resultsMap.entrySet().iterator().next();
                if (resultObj instanceof PQLResultSet) {
                    PQLResultSet pqlRes = (PQLResultSet) resultObj;
                    System.out.println("Number of rows = " + pqlRes.getMetaData().getRowCount());
                }
            }
        } catch (PQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
    
    /**
     * Returns the environment variables for PQLConnection
     * @return environment vairables
     */
    public Properties getEnvironment() {
        return env;
    }

    /**
     * Sets the environment variables for PQLConnection
     * @param env
     */
    public void setEnvironment(Properties env) {
        this.env = env;
    }
    
    public String getEnvironmentDeclaration() {
        if (env == null || env.size() == 0) {
            return null;
        }
        throw new IllegalAccessError("getEnvironmentDeclaration not implemented for TDBPQLConnectionImpl");
    }
    
}
