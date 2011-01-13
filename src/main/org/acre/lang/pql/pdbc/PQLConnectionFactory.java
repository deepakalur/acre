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

import org.acre.config.ConfigData;
import org.acre.config.AcreConfigException;


/**
 * @author Deepak.Alur@Sun.com
 *         Date: Sep 27, 2004
 *         Time: 2:36:23 PM
 */
public class PQLConnectionFactory {

    private String defaultJdbcDriver = "org.gjt.mm.mysql.Driver";
    private String defaultdbUrl = "jdbc:mysql://localhost:3306/salsa";
    private String defaultdbUser = "salsa";
    private String defaultdbPassword = "salsa";

    /**
     * This method creates a new PQL Connection to an RDBMS provided
     * that the parameters are supplied with valid values
     * @param dbDriver RDBMS Driver to use
     * @param factdburl RDBMS URL
     * @param userid RDBMS User Id
     * @param password RBDMS User Password
     * @return a PQLConnection instance connected to an RDBMS
     * @throws PQLException when connection cannot be created
     */
    public static PQLConnection createPQLConnectionToRDB(
            String dbDriver,
            String factdburl,
            String userid,
            String password)
            throws PQLException {

        PQLConnection conn = new RDBPQLConnectionImpl(dbDriver, factdburl, userid, password);
        return conn;
    }

    /** This method creates a new PQL Connection to an RDBMS provided
     * that the parameters are supplied with valid values
     * @param factdburl RDBMS URL
     * @param userid RDBMS User Id
     * @param password RBDMS User Password
     * @return a PQLConnection instance connected to an RDBMS
     * @throws PQLException when connection cannot be created
     */
    public static PQLConnection createPQLConnectionToRDB(
            String factdburl,
            String userid,
            String password)
            throws PQLException {

        PQLConnection conn = new RDBPQLConnectionImpl(factdburl, userid, password);
        return conn;
    }

    /**
     * This method creates a new PQL Connection to a TDB Fileprovided
     * that the parameters are supplied with valid values
     * @param factdburl File Path URL for the Facts File
     * @return a PQLConnection instance connected to the TDB File
     * @throws PQLException when connection cannot be created
     */
    public static PQLConnection createPQLConnectionToTDB(
            String factdburl) throws PQLException {

        PQLConnection conn = new TDBPQLConnectionImpl(factdburl);
        return conn;
    }

    /**
     * Use this method only if the Configuration has been properly set.
     * @see org.acre.config.ConfigService for details
     *  
     * This creates a new connection depending on the settings in the ConfigData
     * provided as argument.
     * If configData.isDBTypeRDBMS() is true, then it returns a connection to the RDBMS
     * by obtaining the driver, url, username and password from the configData argument.
     * If configData.isDBTypeTDB() is true, it returns a connection to the TDB File
     * by obtaining the TDB file Path URL from the configData argument.
     * @param configData
     * @return
     * @throws PQLException
     */

    public static PQLConnection createConnectionFromConfig(ConfigData configData)
    throws PQLException
    {
        try {
            configData.validate();

            if (configData.isDBTypeTDB()) {
                return createPQLConnectionToTDB(configData.getTdbFilePath());
            } else if (configData.isDBTypeRDBMS()) {
                return createPQLConnectionToRDB(
                        configData.getRdbmsJDBCDriver(),
                        configData.getRdbmsURL(),
                        configData.getRdbmsUserId(),
                        configData.getRdbmsUserPassword()
                        );
            } else {
                throw new PQLException("Unknown DB Type specified in Configuration : "
                    + configData);
            }
        } catch (AcreConfigException e) {
            throw new PQLException("Configuration Error, see nested AcreConfigException for details",
                    e);
        }
    }
}
