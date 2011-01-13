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
package org.acre.pdmengine.core;

import org.acre.pdm.PDMType;
import org.acre.pdm.RoleType;
import org.acre.pdmqueries.QueryType;
import org.acre.common.AcreStringUtil;
import org.acre.config.ConfigService;
import org.acre.dao.PDMXMLConstants;
import org.acre.dao.PatternQueryRepository;
import org.acre.dao.PatternRepository;
import org.acre.pdmengine.PatternEngineException;
import org.acre.pdmengine.ScriptEngineFacade;
import org.acre.pdmengine.model.PatternResult;
import org.acre.pdmengine.model.RoleResult;
import org.acre.pdmengine.model.impl.PatternResultImpl;
import org.acre.pdmengine.model.impl.QueryResultImpl;
import org.acre.pdmengine.model.impl.RoleResultImpl;
import org.acre.pdmengine.pqe.PQLEngineFacade;
import org.acre.pdmengine.util.InputSpec;

import java.util.*;
import java.util.logging.Logger;


class ScriptedPatternExecutor {
    private PatternRepository patternRepository;
    private PatternQueryRepository queryRepository;
    private PQLEngineFacade pqlEngineFacade;
    
    static Logger logger = ConfigService.getInstance().getLogger(ScriptedPatternExecutor.class);

    public ScriptedPatternExecutor( PatternRepository patternRepository,
                                    PatternQueryRepository queryRepository,
                                    PQLEngineFacade pqlEngineFacade) {
        this.patternRepository = patternRepository;
        this.queryRepository = queryRepository;
        this.pqlEngineFacade = pqlEngineFacade;
    }

    public PatternResult execute(PDMType pdmType, Map params, InputSpec inputSpec) {

        PatternResult patternResult = new PatternResultImpl(pdmType, inputSpec);

        String script = pdmType.getScriptedPDMPath();

        if ( script == null || (script.length()==0) ) {
            throw new PatternEngineException("Script not specified for PQLPDM type: " + pdmType.getName());
        }

        // role is PQL, get PQL and execute it
        QueryType query = queryRepository.getGlobalQuery(script);
        if ( query == null ) {
            throw new PatternEngineException("Script not found : " + script + " in " + pdmType.getName());
        }

        Map pqlResults;

        if ( PDMXMLConstants.QUERY_LANGUAGE_GROOVY.equalsIgnoreCase(query.getLanguage())) {
            ScriptEngineFacade scriptFacade = new ScriptEngineFacade(pqlEngineFacade);
            pqlResults = scriptFacade.executeGroovyScript (query.getName(), params, queryRepository);
        }
        else {
            String pql = queryRepository.getGlobalQueryFile(query.getName());
            pqlResults = pqlEngineFacade.executeQuery(pql);
        }
        // get roles
        List roles;
        roles = pdmType.getRoles().getRole();

        List roleResults;

        roleResults = bindRoleVariablesToScriptResult(patternResult, roles, query, pqlResults);

        // add roles to the patternResult
        patternResult.setRoles(roleResults);


        // evaluate relationships in the given PDM
        RelationshipExecutor relationshipExecutor = new RelationshipExecutor(pqlEngineFacade);
        relationshipExecutor.executeRelationships(pdmType, patternResult);

        return patternResult;
    }

    private List bindRoleVariablesToScriptResult(PatternResult parentPatternResult, List roles, QueryType query,
                                                 Map scriptResult) {

        List roleResults;
        roleResults = new ArrayList();


        Map lcScriptResult = getMapWithLowerCaseKeys(scriptResult);

//        for (Object o: roles) {
        for (Iterator iter = roles.iterator(); iter.hasNext();) {
            Object o = iter.next();
            RoleType roleType = (RoleType) o;

            RoleResult roleResult = new RoleResultImpl(parentPatternResult);
            roleResult.setRole(roleType);

            QueryResultImpl roleQueryResult = null;

            Map roleResultMap = new HashMap();

            // case-insensitive variableNames binding
            String returnVariableName = roleType.getReturnVariableName();
            if ( AcreStringUtil.isEmpty(returnVariableName)) {
                if ( roles.size() > 1 ) {
                    logger.warning("No binding variable name specified for ScriptedPDM : " +
                            query.getName() + " - " + roleType.getName() );
                }
                else {
                    // provide default binding for 1-role PDMs
                    roleResultMap = lcScriptResult;
                }
            }
            else {
                Object value = lcScriptResult.get(returnVariableName.toLowerCase());
                roleResultMap.put(returnVariableName, value);
            }

            roleQueryResult = new QueryResultImpl(roleResultMap);
            roleQueryResult.setQuery(query);

            roleResult.setRoleResult(roleQueryResult);
            roleResults.add(roleResult);
        }
        return roleResults;
    }

    private Map getMapWithLowerCaseKeys(Map scriptResult) {
        // create case-insensitive map key
        Map lcScriptResult = new HashMap(scriptResult.size());
        Iterator keys = scriptResult.keySet().iterator();
        while ( keys.hasNext()) {
            String key = (String)keys.next();
            lcScriptResult.put(key.toLowerCase(), scriptResult.get(key));
        }
        return lcScriptResult;
    }

}
