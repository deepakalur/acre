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
 * This is the main interface that represents a connection to the backend
 * PQL Engine. This connection can be either to an RDBMS or to a TDB File
 * @see org.acre.lang.pql.pdbc.RDBPQLConnectionImpl
 * @see org.acre.lang.pql.pdbc.TDBPQLConnectionImpl
 * @see org.acre.lang.pql.pdbc.PQLConnectionFactory
 *
 * @author Deepak.Alur@Sun.com
 *         Date: Sep 27, 2004
 *         Time: 2:32:20 PM
 */
public interface PQLConnection {
    /**
     * closes an open PQLConnection instance.
     * A connection cannot be used after it is closed.
     * @throws PQLException
     */
   public void close() throws PQLException;

  /**
   * checks if the connection is closed or not.
   * @return false if the connection is open, true if connection is closed
   * @throws PQLException
   */
   public boolean isClosed() throws PQLException;

   /**
    * Creates a new PQLStatement instance
    * @see org.acre.lang.pql.pdbc.PQLStatement
    * @return an instance of PQLStatement
    * @throws PQLException
    */
   public PQLStatement createStatement() throws PQLException;

   /**
    * Creates a new PQLStatement instance
    * @see org.acre.lang.pql.pdbc.PQLPreparedStatement
    * @param query - a String representing the query for the prepared statement
    * @return an instance of PQLPreparedStatement
    * @throws PQLException
    */
   public PQLPreparedStatement prepareStatement(String query) throws PQLException;

   /**
    * used to pass handle to the backend PQL engine instance
    * @deprecated
    * @return
    * @throws PQLException
    */
   PQL getPQL() throws PQLException;

   /**
    * populates (lazy loads) the given instance of a valid PQLValueHolder
    * instance.
    * @param data
    * @throws PQLException
    */
   public void fetchComplete(PQLValueHolder data) throws PQLException;


    /**
     * Executes a given PQL query string and returns the results Map instance
     * @param query
     * @return a Map instance containing PQLResultSet instances
     * @throws PQLException
     */
   public Map executePQL(String query) throws PQLException;

   /**
    * Returns the environment variables for PQLConnection
    * @return environment vairables
    */
   public Properties getEnvironment();

   /**
    * Sets the environment variables for PQLConnection
    * @param env
    */
   public void setEnvironment(Properties env);

   /**
    * Returns the string to be prepended to the query for initializing 
    * environment for running queries on the PQLConnection
    * @return environment vairables
    */
   public String getEnvironmentDeclaration();

}
