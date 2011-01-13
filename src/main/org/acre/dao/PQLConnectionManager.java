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
package org.acre.dao;

import org.acre.config.ConfigData;
import org.acre.config.ConfigListener;
import org.acre.config.ConfigService;
import org.acre.lang.pql.pdbc.PQLConnection;
import org.acre.lang.pql.pdbc.PQLConnectionFactory;
import org.acre.lang.pql.pdbc.PQLException;
import org.acre.lang.pql.pdbc.PQLStatement;

import java.util.Map;

public class PQLConnectionManager implements ConfigListener {
    private static PQLConnectionManager ourInstance = new PQLConnectionManager();
    private static PQLConnection globalConnection=null;
    private boolean updateRequired=false;
    private boolean busy=false;

    public static PQLConnectionManager getInstance() {
        return ourInstance;
    }

    private PQLConnectionManager() {
    }

    public synchronized PQLConnection getGlobalConnection() throws PQLException {
        checkBusy();

        if (globalConnection == null) {
            createGlobalConnection();
        }
        else if (updateRequired) {
            resetGlobalConnection();
            updateRequired = false;            
        }
        setBusy();
        return globalConnection;
    }

    private void setBusy() {
        busy=true;
    }

    public void checkBusy() throws PQLException {
        if (busy)
            throw new PQLException("Connection Busy. Must be released before reusing");
    }

    public synchronized void release() {
        busy = false;
    }

    public synchronized void createGlobalConnection() throws PQLException {
        checkBusy();

        ConfigData configData = ConfigService.getInstance().getConfigData();
        globalConnection = PQLConnectionFactory.createConnectionFromConfig(configData);
    }

    public synchronized void resetGlobalConnection() throws PQLException {
        checkBusy();

        createGlobalConnection();
    }

    public void tdbUpdated() {
        updateRequired = true;
    }

    public void rdbUpdated() {
        updateRequired = true;
    }

    public void debugUpdated() {
        updateRequired = true;
    }

    public void repositoryUpdated() {
        updateRequired = true;
    }

    public static Map execute(String s) throws PQLException {

        Map result = null;
        try {
            PQLConnection conn = PQLConnectionManager.getInstance().getGlobalConnection();
            PQLStatement stmt = conn.createStatement();
            result = stmt.executeQuery(s);
        } catch (PQLException e) {
            throw e;  
        } finally {
            PQLConnectionManager.getInstance().release();
        }

        return result;
    }

    public static void cancelExecute() {
        // todo - cancel PQL Execute        
    }
}
