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
package org.acre.pdmengine;

import org.acre.dao.PatternQueryRepository;
import org.acre.lang.pql.pdbc.PQLConnection;
import org.acre.lang.runtime.lib.PDMComponent;
import org.acre.lang.runtime.lib.PDMComponentFactory;
import org.acre.pdmengine.pqe.PQLEngineFacade;

import java.io.File;
import java.util.Map;

public class ScriptEngineFacade {

    private PQLEngineFacade pqlEngineFacade;

    public ScriptEngineFacade(PQLEngineFacade pqlEngineFacade) {
        this.pqlEngineFacade = pqlEngineFacade;
    }

    public Map executeGroovyScript(String queryName, Map params,
                                          PatternQueryRepository patternQueryRepository) {
        Map pqlResults = null;
        String fileName = patternQueryRepository.getGlobalQueryFileName(queryName);
        try {
            PDMComponent pdmComponent = PDMComponentFactory.getPDMComponent(new File(fileName));
            PQLConnection pqlConnection = pqlEngineFacade.getPQLConnection();
            pdmComponent.setPql(pqlConnection.getPQL());
            Object result = pdmComponent.execute(params);
            if (!(result instanceof Map)) {
                throw new PatternEngineException("Groovy Script return type must be a Map interface :" + queryName);
            }
            pqlResults = (Map) result;
        } catch (Exception e) {
            throw new PatternEngineException("Groovy script execution error :" + queryName, e);
        }
        return pqlResults;
    }

    public Map executeGroovyString(String cmd, Map params) {
        return executeGroovyString(cmd, params, pqlEngineFacade.getPQLConnection());
    }

    public static Map executeGroovyString(String cmd, Map params, PQLConnection pqlConnection) {

        Map pqlResults = null;
        try {
            PDMComponent pdmComponent = PDMComponentFactory.getPDMComponent(cmd);
            pdmComponent.setPql(pqlConnection.getPQL());
            Object result = pdmComponent.execute(params);
            if (!(result instanceof Map)) {
                throw new PatternEngineException("Groovy Script return type must be a Map interface :" + cmd);
            }
            pqlResults = (Map) result;
        } catch (Exception e) {
            throw new PatternEngineException("Groovy script execution error :" + cmd, e);
        }
        return pqlResults;
    }
}
