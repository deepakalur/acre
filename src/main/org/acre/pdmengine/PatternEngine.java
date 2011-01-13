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

import org.acre.pdm.PDMType;
import org.acre.dao.PatternQueryRepository;
import org.acre.dao.PatternRepository;
import org.acre.lang.pql.pdbc.PQLConnection;
import org.acre.pdmengine.model.Artifact;
import org.acre.pdmengine.model.ArtifactLink;
import org.acre.pdmengine.model.PatternResult;
import org.acre.pdmengine.model.RoleResult;

import java.util.List;
import java.util.Map;

/**
 * @author Deepak.Alur@Sun.com
 *         rajmohan@sun.com  
 *         Date: Aug 3, 2004
 *         Time: 11:12:27 AM
 *
 * PatternEngine component is responsible for searching and discovering patterns.
 */
public interface PatternEngine {
    int COARSE = SearchContext.COARSE;
    int EXACT = SearchContext.EXACT;

    /**
 *  Execute pattern search for the given pattern.
 * Scope of search is global (i.e., across all systems) using latest version for each system
  * @param pdm
 * @return PatternResult with artifacts & relationships
 * @throws PatternEngineException
 */
    PatternResult execute(PDMType pdm) throws PatternEngineException;

/**
 *  Execute pattern search for the given pattern name in the global scope
 * Scope of search is global (i.e., across all systems) using latest version for each system
  * @param patternName
 * @return PatternResult with artifacts & relationships
 * @throws PatternEngineException
 */
    PatternResult execute(String patternName) throws PatternEngineException;

    PatternResult execute(String patternName, int searchType);

/**
 * Pattern search across different scopes(system, time, version) using search specification object.
 * @param patternName
 * @param params
 * @param searchCtx
 * @return
 * @throws PatternEngineException
 */
    PatternResult[] execute(String patternName, Map params, SearchContext searchCtx)
            throws PatternEngineException;

/**
 *  Execute pattern search for the given pattern name using specified parameters
 * Scope of search is global (i.e., across all systems) using latest version for each system
  * @param patternName
 * @param params input parameters for pattern search
 * @return PatternResult with artifacts & relationships
 * @throws PatternEngineException
 */
    PatternResult execute(String patternName, Map params) throws PatternEngineException;


    // setCurrentSystem(), setCurrentVersion(), setCurrentTimestamp()
    
    PatternRepository getPatternRepository();

    void setPatternRepository(PatternRepository patternRepository) ;

    PatternQueryRepository getPatternQueryRepository();

    void setPatternQueryRepository(PatternQueryRepository patternQueryRepository) ;

    /**
     * @deprecated Clients do not have to set this connection, PatternEngine handles
     * PQL connection management.
     */
    void setPqlConnection(PQLConnection connection);

    void refresh(String patternName);

    void refreshAll();

    /**
     *  find all artifacts outside of the given pattern that "calls"
     *  (i.e., has a relationship) with the given role
     * @param patternName
     * @param roleName
     * @param relationship
     * @return
     */
    ArtifactLink[] findRelcos(String patternName, String roleName, String relationship);

    Artifact[] findRelcos(String patternName, String roleName, String relationship ,
                                 String artifactName);

    ArtifactLink[] findRelcos(RoleResult roleResult, String relationship);

    Artifact[] findRelcos(RoleResult roleResult, String relationship, Artifact artifact);

    /**
     * find all artifacts outside of the Pattern that the given role
     * "calls"(i.e., has a relationship) with
     * @param patternName
     * @param roleName
     * @param relationship
     * @return
     */
    ArtifactLink[] findInvRelcos(String patternName, String roleName, String relationship);

    Artifact[] findInvRelcos(String patternName, String roleName, String relationship,
                                    String artifactName);

    ArtifactLink[] findInvRelcos(RoleResult roleResult, String relationship);

    Artifact[] findInvRelcos(RoleResult roleResult, String relationship, Artifact artifact);



    // find PDMs containing the given artifact
    String[] findPatterns(SearchContext searchContext, String orphanArtifactName);

    String[] findPatterns(SearchContext searchContext, Artifact orphanArtifact);

    // find PDMs, each containing all the given artifacts
    String[] findPatterns(SearchContext searchContext, String[] orphanArtifacts);

    String[] findPatterns(SearchContext searchContext, Artifact[] orphanArtifacts);

    /**
     * Discover inter-PDM relationships
     * @param patterns
     * @param relationshiptypes
     * @return
     */
    // List<RelationshipResult>
    List discoverRelationships(String patterns[], String relationshiptypes[]);

    List discoverRelationships(PatternResult pResults[], String relationshiptypes[]);

    /**
     * Discover inter-PDM relationships and compose a new PDM composed of newly
     * discovered relationships.
     *
     * @param dynamicPatternName
     * @param patternNames
     * @param relationshipTypes
     * @return
     */
    PatternResult discoverPattern(String dynamicPatternName, String patternNames[], String relationshipTypes[]);

    PatternResult discoverPattern(String dynamicPatternName, PatternResult patternResults[], String relationshipTypes[]);

    /**
     * Discover intra-Role relationships within a given PDM
     * @param pattern
     * @param relationshipTypes
     * @return
     */
   //List<RelationshipResult>
    List discoverRoleRelationships(String pattern, String relationshipTypes[]);

    // loads all patterns for global + latest version        
    void loadAllPatterns(boolean async, PatternEngineEventListener listener);

    // loads all patterns for specific search context
    void loadAllPatterns(SearchContext context, boolean async, PatternEngineEventListener listener);

    void shutdown();

}
