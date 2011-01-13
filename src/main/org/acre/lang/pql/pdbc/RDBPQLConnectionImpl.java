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

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * @author Deepak.Alur@Sun.com
 *         Date: Sep 27, 2004
 *         Time: 2:35:12 PM
 */
public class RDBPQLConnectionImpl implements PQLConnection {
    boolean open=false;
    String connectionName;

    Properties env = null;
    PQL pql;

    private static final String dbDriver = "org.gjt.mm.mysql.Driver";
    private boolean valid=true;

    public RDBPQLConnectionImpl(String driver, String dbUrl, String dbUser, String dbPassword)
            throws PQLException {

        connectionName = dbUrl + driver + dbUser + dbPassword + System.currentTimeMillis();

        PQLErrors errors = new PQLErrors();

        if (dbUrl == null) {
            errors.addError(PQLErrors.PQL_DB_DBURL, "Database URL is null or invalid");
        }
        if (dbUser == null) {
            errors.addError(PQLErrors.PQL_DB_USERID, "Database User Id is null or invalid");
        }
        if (dbPassword== null) {
            errors.addError(PQLErrors.PQL_DB_PASSWORD, "Database Password is null or invalid");
        }
        if (driver == null) {
            errors.addError(PQLErrors.PQL_DB_DRIVER, "JDBC Driver is null or invalid");
        }

        if (errors.size() != 0) {
            throw new PQLException(errors);
        }

        pql = PQL.createDatabasePQL(
                this.connectionName, driver,
                dbUrl, dbUser, dbPassword);

        connect();

    }

    public RDBPQLConnectionImpl(String dbUrl, String dbUser, String dbPassword)
        throws PQLException {
            this(dbDriver, dbUrl, dbUser, dbPassword);
    }

    private void connect() throws PQLException {
        if (!isValid())
            throw new PQLException("Connection invalid");

        try {
            pql.getAdaptor().connect();
        } catch (Throwable t) {
            valid = false;
            open = false;
            pql.removeDatabasePQL(this.connectionName);
            throw new PQLException("Failed to connect", t);
        }
        open = true;
        valid=true;
    }

    public void close() throws PQLException {

        checkOpenValid();

        pql.getAdaptor().disconnect();
        pql.removeDatabasePQL(this.connectionName);
        open = false;
        valid=false;
    }

    public boolean isClosed() throws PQLException {
        return !open;
    }

    public PQLStatement createStatement() throws PQLException {
        checkOpenValid();

        return new PQLStatementImpl(this);
    }

    public PQLPreparedStatement prepareStatement(String query) throws PQLException  {
        checkOpenValid();

        //Prepend the environment vairable intialization
        String prefix = getEnvironmentDeclaration();
        if (prefix != null) {
            query = prefix + query;
        }
        return new PQLPreparedStatementImpl(this,  query);
    }

    public PQL getPQL()
            throws PQLException {
        return pql;
    }

    public static void main(String args[]) {
        String dbUrl = "jdbc:mysql://localhost:3306/salsa";
        String dbUser = "salsa";
        String dbPassword = "salsa";
        try {
            PQLConnection conn = PQLConnectionFactory.createPQLConnectionToRDB(dbUrl, dbUser, dbPassword);
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

    public boolean isValid() {
        return valid;
    }

    public void fetchComplete(PQLValueHolder data)
            throws PQLException {
        checkOpenValid();

        pql.fetch(data);
    }

    public Map executePQL(String query) throws PQLException {
        checkOpenValid();

        return pql.executePQL(query);
    }

    private void checkOpenValid()
            throws PQLException {
        if (isClosed() || (!isValid())) {
            throw new PQLException("Connection is closed or invalid. Cannot perform operation");
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
        StringBuffer buffer = new StringBuffer();
        for (Iterator iter = env.entrySet().iterator(); iter.hasNext();) {
            Map.Entry prop = (Map.Entry) iter.next();
            String key = (String) prop.getKey();
            String value = (String) prop.getValue();
            buffer.append("DEFINE @").append(key).append(" AS ").append(value).append(";\n");
        }
        return buffer.toString();
    }
    
}


