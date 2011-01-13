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
import org.acre.pdm.RolesType;
import org.acre.pdmqueries.QueryType;
import org.acre.config.ConfigService;
import org.acre.dao.PDMXMLConstants;
import org.acre.dao.PatternQueryRepository;
import org.acre.dao.PatternRepository;
import org.acre.pdmengine.PatternEngineException;
import org.acre.pdmengine.ScriptEngineFacade;
import org.acre.pdmengine.model.*;
import org.acre.pdmengine.model.impl.AnnotatedResultImpl;
import org.acre.pdmengine.model.impl.PatternResultImpl;
import org.acre.pdmengine.model.impl.QueryResultImpl;
import org.acre.pdmengine.model.impl.RoleResultImpl;
import org.acre.pdmengine.pqe.PQLEngineFacade;
import org.acre.pdmengine.util.InputSpec;

import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.*;
import java.util.logging.Logger;



/**
 * User: deepak.alur@sun.com
 *       rajmohan@sun.com
 * Date: Nov 8, 2004
 * Time: 11:47:13 PM
 */
public class ModelPatternExecutor {
    Logger log = ConfigService.getInstance().getLogger(ModelPatternExecutor.class);

    private PatternSearchEngine patternSearchEngine;

    private PatternRepository patternRepository;
    private PatternQueryRepository patternQueryRepository;
    private PQLEngineFacade pqlEngineFacade;


    private String DYNAMIC_ROLE_MARKER = "$";
    
    public ModelPatternExecutor(PatternRepository patternRepository,
                                PatternQueryRepository patternQueryRepository,
                                PQLEngineFacade pqlEngineFacade,
                                PatternSearchEngine patternSearchEngine) {
        this.patternRepository = patternRepository;
        this.patternQueryRepository = patternQueryRepository;
        this.pqlEngineFacade = pqlEngineFacade;
        this.patternSearchEngine = patternSearchEngine;

    }

    public PatternResult executeModelPDM(PDMType pdm, Map params, InputSpec inputSpec) {

        PDMType clonePDM =  patternRepository.clonePatternModel(pdm);

        PatternResult patternResult = new PatternResultImpl(clonePDM, inputSpec);

        evaluateDynamicRoles(clonePDM);

        evaluateStaticRoles(clonePDM, patternResult, params, inputSpec);


        // evaluate relationships
        RelationshipExecutor relationshipExecutor = new RelationshipExecutor(pqlEngineFacade);
        relationshipExecutor.executeRelationships(clonePDM, patternResult);

        return patternResult;
    }

    private void evaluateStaticRoles(PDMType pdm, PatternResult patternResult,
                                     Map params, InputSpec inputSpec) {
        // evaluate Static roles
        List roles;
        roles = pdm.getRoles().getRole();

        List roleResults;
        roleResults = new ArrayList();

//        for (Object o: roles) {
        for (Iterator iter = roles.iterator(); iter.hasNext();) {
            Object o = iter.next();
            RoleType role = (RoleType) o;

            RoleResult result = evaluateRole(patternResult, role, params, inputSpec);

            roleResults.add(result);
        }

        // add roles to the patternResult
        patternResult.setRoles(roleResults);
    }

    private RoleResult evaluateRole(PatternResult parentResult, RoleType role,
                                    Map params, InputSpec inputSpec) {

        // create role pdmResult
        RoleResultImpl roleResult = new RoleResultImpl(parentResult);
        roleResult.setRole(role);

        // if role is PDM, get that PDM Model
        if (PDMXMLConstants.ROLE_TYPE_PDM.equalsIgnoreCase(role.getType())) {

            evaluatePDMRole(role, parentResult, roleResult, inputSpec);

        } else if (PDMXMLConstants.ROLE_TYPE_QUERY.equalsIgnoreCase(role.getType())) {
            if (params == null) {
                params = new HashMap();
            }
            //add implicit param : TODO - streamline this code
            params.put("rolename", role.getName());
            params.put("scope", roleResult.getScope()) ;

            QueryResult queryResult = evaluteQuery(role.getQueryName(), params);

            roleResult.setRoleResult(queryResult);

            // Dynamic Return variable-name binding
            String variableName = roleResult.getVariableName();
            if ( "$result".equalsIgnoreCase(variableName)) {
                String queryReturnVariableName = queryResult.getVariableKeys()[0];
                roleResult.setVariableName(queryReturnVariableName);
            }
        } else if (PDMXMLConstants.ROLE_TYPE_ANNOTATED.equalsIgnoreCase(role.getType())) {
               String fileName = role.getRepository();
               String pdmAnnotationFile = ConfigService.getInstance().getPDMAnnotationsFilePath(fileName);
            Properties artifactsRepository;
            try {
                InputStream is = new FileInputStream(pdmAnnotationFile);
                artifactsRepository = new Properties();
                artifactsRepository.load(is);
            } catch (Exception e) {
                throw new PatternEngineException("Error accessing PDM Annotations repository " + pdmAnnotationFile
                        , e);
            }

            Enumeration propertyName = artifactsRepository.propertyNames();
            while ( propertyName.hasMoreElements()) {
                String artifactType = (String)propertyName.nextElement();

                String artifactIds = artifactsRepository.getProperty(artifactType);

                StringBuffer sb = new StringBuffer(1000);
                sb.append("return select e from " + artifactType + " e where e.id in ( ");
                sb.append(artifactIds);
                sb.append(" );");

                String dynamicQuery = sb.toString();


                Result annotatedResult;
                Map pqlResults;
                pqlResults = pqlEngineFacade.executeQuery(dynamicQuery);

                annotatedResult = new AnnotatedResultImpl(pqlResults, roleResult);

                roleResult.setRoleResult(annotatedResult);
            }
        }
        return  roleResult;
    }

    private void evaluatePDMRole(RoleType role, PatternResult parentResult,
                                 RoleResult roleResult, InputSpec inputSpec) {
        Map callParams = null;
        String pdmReferenceName = role.getTypeReferenceName();
        if ( pdmReferenceName == null )
            pdmReferenceName = role.getName();

        if ( pdmReferenceName.equalsIgnoreCase(parentResult.getName())) {
            // composite pattern: detecting role PDM reference to its parent PDM
            roleResult.setRoleResult(parentResult);
        }
        else {
            PDMType rolePDM = patternRepository.getGlobalPatternModel(pdmReferenceName);
            if ( rolePDM == null ) {
                throw new PatternEngineException("Role reference to PDM not found : " + pdmReferenceName);
            }
            log.info("Role = " + role.getName() + " - Executing PDM:" + rolePDM.getName());
            PatternResult results;

            InputSpec roleInputSpec = new InputSpec(rolePDM.getName(), inputSpec);

            results = patternSearchEngine.execute(rolePDM, callParams, roleInputSpec);

            roleResult.setRoleResult(results);
        }
    }


    private QueryResult evaluteQuery(String queryName, Map params) {
        // role is PQL, get PQL and execute it
        QueryType query = patternQueryRepository.getGlobalQuery(queryName);
        if ( query == null ) {
            String msg = "Referenced Query not found : " + queryName;
            throw new PatternEngineException(msg);
        }       


        QueryResult queryResult = null;


        log.info("Executing Role.Query:" + query.getName());

        if ( PDMXMLConstants.QUERY_LANGUAGE_GROOVY.equalsIgnoreCase(query.getLanguage())) {
            ScriptEngineFacade groovyFacade = new ScriptEngineFacade(pqlEngineFacade);

            Map pqlResults = groovyFacade.executeGroovyScript(query.getName(),
                    params, patternQueryRepository);

            queryResult = new QueryResultImpl(pqlResults);
            queryResult.setQuery(query);
        } else {
            String pql = patternQueryRepository.getGlobalQueryFile(query.getName());

            Map pqlResults;
            pqlResults = pqlEngineFacade.executeQuery(pql);

            queryResult = new QueryResultImpl(pqlResults);
            queryResult.setQuery(query);
            queryResult.setQueryStatement(pql);
        }
        return queryResult;
    }



    private void evaluateDynamicRoles(PDMType rootPDMType) {

        RolesType rolesType = rootPDMType.getRoles();
        if ( PDMXMLConstants.ROLES_TYPE_DYNAMIC.equalsIgnoreCase(rolesType.getType()) == false) {
            return;
        }

        String queryName;
        queryName = rolesType.getQueryName();
        QueryResult queryResult = evaluteQuery(queryName, null);
        Artifact roleArtifacts[] = queryResult.getArtifacts(0);

        ListIterator roles;
        roles = rolesType.getRole().listIterator();
        if ( roles.hasNext() == false) {
            throw new PatternEngineException("Dynamic Roles specified with missing Role definition" +
                    rootPDMType.getName());
        }
        RoleType metaRole = (RoleType)roles.next();

        // remove metaRole definition from the list
        roles.remove();

        int sequence = 0;
        // Bind above query result-set with # of static roles
        for ( int i = 0; i < roleArtifacts.length; i++ ) {
            RoleType newRole = patternRepository.cloneRole(metaRole);
            String newRoleName = roleArtifacts[i].getName();
            newRole.setName(newRoleName);
            newRole.setSequence(new BigInteger(Integer.toString(sequence++)));
            roles.add(newRole);
        }

        return;
    }
}
