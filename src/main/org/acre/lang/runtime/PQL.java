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
package org.acre.lang.runtime;

import org.acre.lang.analyser.ScriptModel;
import org.acre.lang.pql.translator.Translate;

import java.util.HashMap;
import java.util.Map;

/**
 * Proxy from Groovy/Java to PQL
 * It also contains Java APIs to PQL
 *
 * @author Yury Kamen
 *         TODO: add docs
 */
public class PQL extends PQLSupport {

    private static Map sqlConnCache = new HashMap();
    private static Map qlConnCache = new HashMap();
    
    private PQL(String database) {
        super(new QLAdaptor(), true);
        scriptModel = new ScriptModel();
        adapter.evaluateNative(Translate.getImport(database));
        initQL();
    }
    
    private PQL(String dbDriver, String dbUrl, String dbUser, String dbPassword) {
        super(new DatabaseAdapter(new ScriptModel()), true);
        scriptModel = ((DatabaseAdapter) adapter).getScriptModel();
        DatabaseAdapter dbAdapter = (DatabaseAdapter) adapter;
        if (dbDriver != null) {
            dbAdapter.setDbDriver(dbDriver);
        }
        if (dbUrl != null) {
            dbAdapter.setDbUrl(dbUrl);
        }
        if (dbUser != null) {
            dbAdapter.setDbUser(dbUser);
        }
        if (dbPassword != null) {
            dbAdapter.setDbPassword(dbPassword);
        }
    }
    
    private void initQL() {
        for (int i = 0; i < Translate.PREQL_PROLOGUE.length; i++) {
            adapter.evaluateNative(Translate.PREQL_PROLOGUE[i]);
        }
    }
    
    public static PQL createQLPQL(String database) {
        PQL result = (PQL) qlConnCache.get(database); 
        if (result != null) {
            return result;
        }
        result = new PQL(database);
        qlConnCache.put(database, result);
        return result;
        
    }
    
    public static PQL createDatabasePQL() {
        return createDatabasePQL(null, null, null, null);
    }
    
    public static PQL createDatabasePQL(String name) {
        return createDatabasePQL(name, null, null, null, null);
    }
    
    public static PQL createDatabasePQL(String dbDriver, String dbUrl, String dbUser, String dbPassword) {
        return createDatabasePQL("default", dbDriver, dbUrl, dbUser, dbPassword);
    }
    
    public static PQL createDatabasePQL(String name, String dbDriver, String dbUrl, String dbUser, String dbPassword) {
        PQL result = (PQL) sqlConnCache.get(name); 
        if (result != null) {
            return result;
        }
//        if (!(dbDriver == null && dbUrl == null && dbUser == null && dbPassword == null)) { 
            result = new PQL(dbDriver, dbUrl, dbUser, dbPassword);
            sqlConnCache.put(name, result);
//        }
        return result;
    }


    /**
     * when you create a new Connection, a PQL instance is returned even if the JDBC Driver
     * or user id or password is invalid. By this time, the newly created PQL instance is put
     * into the sqlConnCache. But when a connect() method is invoked on that pql instance, it
     * throws an exception.
     * So this method was introduced to be explicitly called to remove the connection instance
     * from the cache
      * @param name
     */

    public static void removeDatabasePQL(String name) {
        PQL result = (PQL) sqlConnCache.get(name);
        if (result != null) {
            sqlConnCache.remove(name);
            result = null;
        }
    }
}

