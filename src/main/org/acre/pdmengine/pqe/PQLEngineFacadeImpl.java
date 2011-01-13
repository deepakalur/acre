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
package org.acre.pdmengine.pqe;

import org.acre.config.ConfigData;
import org.acre.config.ConfigService;
import org.acre.lang.pql.pdbc.PQLConnection;
import org.acre.lang.pql.pdbc.PQLConnectionFactory;
import org.acre.lang.pql.pdbc.PQLException;
import org.acre.lang.pql.pdbc.PQLStatement;
import org.acre.pdmengine.PatternEngineException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author rajmohan@Sun.com
 */
class PQLEngineFacadeImpl implements PQLEngineFacade {
    private PQLConnection pqlConnection;

    private static Logger logger = ConfigService.getInstance().getLogger(PQLEngineFacadeImpl.class);

    public PQLEngineFacadeImpl() {
        this.pqlConnection = createPQLConnection();
    }

    public void setSearchScope(String system, String version, Date timestamp) {
        Properties properties = new Properties();
        properties.put("system", quote(system));
        properties.put("version", version == null ? "null" : quote(version));

        properties.put("timestamp", timestamp == null ? "null" :
                    quote(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timestamp)));
        pqlConnection.setEnvironment(properties);

    }

    public Map executeQuery(String query) {
        Map pqlResult;
        try {
            long startTS = System.currentTimeMillis();

            PQLStatement statement = pqlConnection.createStatement();

            // execute base dynamic query
            pqlResult = statement.executeQuery(query);
            logger.severe("PQL Query Execution Time : " + (System.currentTimeMillis() - startTS)/1000 );
            return pqlResult;
        } catch (PQLException e) {
            throw new PatternEngineException("Internal error - executing PQL query : "
                    + query + "\n" + e.getMessage(), e);
        }
    }

    public void shutdown() {
        try {
            pqlConnection.close();
        } catch (PQLException e) {
            throw new PatternEngineException("Error shutting down Pattern Query Engine - "
                + e.getMessage());
        }

    }

    public PQLConnection getPQLConnection() {
        return pqlConnection;
    }

    private PQLConnection createPQLConnection() {
        try {
            ConfigData configData = ConfigService.getInstance().getConfigData();
            PQLConnection conn = PQLConnectionFactory.createConnectionFromConfig(configData);
            return conn;
        } catch (PQLException e) {
            throw new PatternEngineException("Internal Error - initializing PQLConnection : "
                + e.getMessage());
        }
    }

    private String quote(String s) {
        return "\"" + s + "\"";
    }


}
