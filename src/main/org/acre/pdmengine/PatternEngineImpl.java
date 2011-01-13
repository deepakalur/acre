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


import org.acre.common.AcreStringUtil;
import org.acre.config.ConfigService;
import org.acre.dao.*;
import org.acre.lang.pql.pdbc.PQLConnection;
import org.acre.lang.pql.pdbc.PQLException;
import org.acre.pdmengine.analyzer.DesignAnalyzer;
import org.acre.pdmengine.analyzer.PatternAggregator;
import org.acre.pdmengine.core.PatternSearchEngine;
import org.acre.pdmengine.model.*;
import org.acre.pdmengine.model.impl.PDMModelUtil;
import org.acre.pdmengine.model.impl.PatternResultImpl;
import org.acre.pdmengine.model.impl.RoleResultImpl;
import org.acre.pdmengine.pqe.PQLEngineFacade;
import org.acre.pdmengine.pqe.PQLEngineFacadeFactory;
import org.acre.pdmengine.util.InputSpec;
import org.acre.pdm.PDMType;
import org.acre.pdm.RolesType;
import org.acre.pdm.RoleType;
import org.acre.pdm.RelationshipType;

import java.util.*;
import java.util.logging.Logger;


/**
 * @author Deepak.Alur@Sun.com
 *         rajmohan@sun.com
 *         Date: Aug 3, 2004
 *         Time: 11:14:13 AM
 *
 * PatternEngine is responsible for searching and discovering patterns.
 *
 * This component is implemented as a "Stateful Session Component"
 * i.e., component has user-affinity & maintains user state
 *
 */
public class PatternEngineImpl implements PatternEngine {


    static Logger log = ConfigService.getInstance().getLogger(PatternEngineImpl.class);

    // per-user state
    private PatternRepository patternRepository;
    private PatternQueryRepository queryRepository;
    private PQLEngineFacade pqlEngineFacade;

    private PatternSearchEngine patternSearchEngine;
    private PatternAggregator patternAggregator;
    private DesignAnalyzer designAnalyzer;

    public PatternEngineImpl() {
        initialize();
    }

    
    public PatternEngineImpl(PQLEngineFacade pqlEngineFacade, 
                             PatternRepository patternRepository,
                             PatternQueryRepository patternQueryRepository,
                             PatternSearchEngine patternSearchEngine
                             ) {
        this.pqlEngineFacade = pqlEngineFacade;
        this.patternRepository = patternRepository;
        this.queryRepository = patternQueryRepository;
        this.patternSearchEngine = patternSearchEngine;

        designAnalyzer = new DesignAnalyzer(pqlEngineFacade);
        patternAggregator = new PatternAggregator(patternRepository, patternSearchEngine);

    }

    protected void initialize() {

        pqlEngineFacade = PQLEngineFacadeFactory.getInstance().createPQLEngineFacade();
        patternRepository = DAOFactory.getPatternRepository();
        queryRepository = DAOFactory.getPatternQueryRepository();

        patternSearchEngine = new PatternSearchEngine(pqlEngineFacade, patternRepository, queryRepository);

        designAnalyzer = new DesignAnalyzer(pqlEngineFacade);
        patternAggregator = new PatternAggregator(patternRepository, patternSearchEngine);

    }


    public PatternResult execute(String patternName) throws PatternEngineException {
        return execute(patternName, null);
    }

    public PatternResult execute(String patternName, int searchType) throws PatternEngineException {
        InputSpec inputSpec =  new InputSpec(patternName);
        inputSpec.setSearchType(searchType);
        return patternSearchEngine.execute(inputSpec, null);
    }

    public PatternResult execute(String patternName, Map params) throws PatternEngineException {
        InputSpec inputSpec = new InputSpec(patternName);
        return patternSearchEngine.execute(inputSpec, params);
    }

    public PatternResult[] execute(String patternName, Map params, SearchContext searchCtx)
            throws PatternEngineException {
        return patternSearchEngine.execute(patternName, params, searchCtx);
    }

    public PatternResult execute(PDMType pdm) throws PatternEngineException {
        return patternSearchEngine.execute(pdm, null, new InputSpec(pdm.getName()));
    }

    public PatternResult execute(PDMType pdm, Map params) throws PatternEngineException {
        return patternSearchEngine.execute(pdm, params, new InputSpec(pdm.getName()));
    }

    public PatternRepository getPatternRepository() {
        return patternRepository;
    }

    public void setPatternRepository(PatternRepository patternRepository) {
        this.patternRepository = patternRepository;
    }

    public PatternQueryRepository getPatternQueryRepository() {
        return queryRepository;
    }

    public void setPatternQueryRepository(PatternQueryRepository patternQueryRepository) {
        this.queryRepository = patternQueryRepository;
    }

    public void refresh(String patternName) {
        patternSearchEngine.refresh(patternName);
    }

    public void refreshAll() {
        patternSearchEngine.refreshAll();
    }

    // find all artifacts outside of the PDM that "calls"(i.e., has a relationship) with the given role
      public ArtifactLink[] findRelcos(String patternName, String roleName, String relationship) {


        PatternResultImpl patternResult = (PatternResultImpl)execute(patternName);

        return findRelcos(patternResult, roleName, relationship);
    }

    // find all artifacts outside of the PDM that "calls"(i.e., has a relationship) with the given role
      private ArtifactLink[] findRelcos(PatternResult patternResult, String roleName, String relationship) {

        setSearchScope(patternResult);

        ArtifactLink links[] = designAnalyzer.findRelcos((PatternResultImpl)patternResult, roleName,
                   relationship);
        return links;
    }

    // find all artifacts outside of the PDM that "calls"(i.e., has a relationship) with the given role
      public Artifact[] findRelcos(String patternName, String roleName, String relationship ,
                                   String artifactName) {

        // validation aspect
        validatePdmName(patternName);
//        validateRelationshipType(relationshipTypes);
        validateArtifactName(artifactName);

        PatternResultImpl pdmResult = (PatternResultImpl)execute(patternName);

        return findRelcos(pdmResult, roleName, relationship, artifactName);
    }

    // find all artifacts outside of the PDM that "calls"(i.e., has a relationship) with the given role
      private Artifact[] findRelcos(PatternResult patternResult, String roleName, String relationship ,
                                   String artifactName) {

        setSearchScope(patternResult);

        Artifact[] artifacts = designAnalyzer.findRelcos((PatternResultImpl)patternResult,
                   roleName, relationship, artifactName);
        return artifacts;
    }

    private void setSearchScope(PatternResult patternResult) {
        pqlEngineFacade.setSearchScope(patternResult.getSystem(),
                patternResult.getVersion(), patternResult.getTimestamp());
    }


    public ArtifactLink[] findRelcos(RoleResult roleResult, String relationship) {

        return findRelcos(getRoot(roleResult), roleResult.getName(), relationship);
    }


    public Artifact[] findRelcos(RoleResult roleResult, String relationship, Artifact artifact) {
        PatternResult root = getRoot(roleResult);

        return findRelcos(root, roleResult.getName(), relationship, artifact.getName());
    }

    // find all artifacts outside of the PDM that the given role "calls"(i.e., has a relationship) with
    public ArtifactLink[] findInvRelcos(String patternName, String roleName, String relationship) {
        PatternResult patternResult = execute(patternName);

        return findInvRelcos(patternResult, roleName , relationship);
    }

    public Artifact[] findInvRelcos(String patternName, String roleName, String relationship,
                                        String artifactName) {
        PatternResult patternResult = execute(patternName);

        return findInvRelcos(patternResult, roleName , relationship, artifactName);
    }


    // find all artifacts outside of the PDM that the given role "calls"(i.e., has a relationship) with
    public ArtifactLink[] findInvRelcos(RoleResult role, String relationship) {
        PatternResult root = getRoot(role);

        return findInvRelcos(root, role.getName(), relationship);
    }

    public Artifact[] findInvRelcos(RoleResult roleResult, String relationship, Artifact artifact) {
        PatternResult root = getRoot(roleResult);

        return findInvRelcos(root, roleResult.getName(), relationship, artifact.getName());
    }

    // find all artifacts outside of the PDM that the given role "calls"(i.e., has a relationship) with
     private ArtifactLink[] findInvRelcos(PatternResult patternResult, String roleName, String relationship) {
        setSearchScope(patternResult);
        ArtifactLink links[] = designAnalyzer.findInvRelcos((PatternResultImpl)patternResult,
                                        roleName, relationship);


        return links;

    }

    // find all artifacts outside of the PDM that the given role "calls"(i.e., has a relationship) with
      private Artifact[] findInvRelcos(PatternResult patternResult, String roleName, String relationship,
                                   String artifactName) {


        setSearchScope(patternResult);

       Artifact[] artifacts = designAnalyzer.findInvRelcos((PatternResultImpl)patternResult,
               roleName, relationship, artifactName);

        return artifacts;
    }


    public void setPqlConnection(PQLConnection pqlConnection) {
        // no-op  - This method to be deprecated.
        // PQLConnection management is delegated to PatternEngine, instead of
        // PatternEngine's client handling it.

//        executionContext.setPqlConnection(pqlConnection);
    }

    private PatternResult getRoot(RoleResult roleResult) {
        PatternResultImpl parent;
        parent = (PatternResultImpl)roleResult.getParent();
        return parent.getRoot();
    }

    public String[] findPatterns(SearchContext searchContext, String orphanArtifact) {
        if ( AcreStringUtil.isEmpty(orphanArtifact))
            return new String[0];

        return patternAggregator.findPatterns(searchContext, new String[]{orphanArtifact});
    }

    public String[] findPatterns(SearchContext searchContext, Artifact orphanArtifact) {
        if ( orphanArtifact == null )
            return new String[0];
        return patternAggregator.findPatterns(searchContext, new String[]{orphanArtifact.getName()});
    }

    public String[]  findPatterns(SearchContext searchContext, String orphanArtifacts[]) {
        return patternAggregator.findPatterns(searchContext, orphanArtifacts);
    }

    public String[]  findPatterns(SearchContext searchContext, Artifact orphanArtifacts[]) {
        if ( orphanArtifacts == null || orphanArtifacts.length==0)
            return new String[0];

        String artifactNames[] = new String[orphanArtifacts.length];

        for ( int i=0; i < orphanArtifacts.length; i++) {
            artifactNames[i] = orphanArtifacts[i].getName();
        }

        return patternAggregator.findPatterns(searchContext, artifactNames);
    }


    /**
     * Discover inter-role relationships between given PDMs
     * @param patterns
     * @param relationshipTypes
     * @return
     */
    // List<RelationshipResult>
    public List discoverRelationships(String patterns[], String [] relationshipTypes) {
            List resultsList = new ArrayList();

            if ( patterns.length == 0 || relationshipTypes.length == 0)
                return resultsList;

            for ( int idx=0; idx < patterns.length-1; idx++) {
                String pattern1 = patterns[idx];

                for ( int idx2=idx+1; idx2 < patterns.length; idx2++ ) {
                    String pattern2 = patterns[idx2];

                    PatternResult sourcePatternResult, targetPDMResult;
                    sourcePatternResult = execute(pattern1);
                    targetPDMResult = execute(pattern2);

                    RelationshipResult[] results   = designAnalyzer.discoverRelationships(sourcePatternResult, targetPDMResult, relationshipTypes);
                    resultsList.addAll(Arrays.asList(results));
                }
            }
            return resultsList;
    }


    public List discoverRelationships(PatternResult pResults[], String relationshiptypes[]) {
        return designAnalyzer.discoverRelationships(pResults, relationshiptypes);
    }

    // Discover relationship among given PDMs, and create a new PDM hosting the given PDMs and
    // discovered relationships
    public PatternResult discoverPattern(String dynamicPatternName, String patternNames[], String relationshipTypes[]) {

        PatternResult patternResults[] = new PatternResult[patternNames.length];
        for ( int i=0; i < patternNames.length; i++) {
            patternResults[i] = execute(patternNames[i]);
        }

        List discoveredRelationshipResults;

        // discover relationship among the given PDMs
        discoveredRelationshipResults = discoverRelationships(patternResults, relationshipTypes);

        return createPattern(dynamicPatternName, patternResults, discoveredRelationshipResults);

    }

    // Discover relationship among given PDMs, and create a new PDM hosting the given PDMs and
    // discovered relationships
    public PatternResult discoverPattern(String dynamicPatternName, PatternResult pResults[], String relationshipTypes[]) {
        List discoveredRelationshipResults;

        // discover relationship among the given PDMs
        discoveredRelationshipResults = discoverRelationships(pResults, relationshipTypes);


        return createPattern(dynamicPatternName, pResults, discoveredRelationshipResults);

    }

    private PatternResult createPattern(String dynamicPatternName,
                                        PatternResult[] subPatternResults,
                                        List relationshipResults) {

        // create new root PDMType
        PDMType newPDMType;
        newPDMType = patternRepository.createNewPatternModel();
        newPDMType.setName(dynamicPatternName);

        PatternResultImpl newPDMResult = new PatternResultImpl(newPDMType);

        RolesType rolesType;
        rolesType = newPDMType.getRoles();

        // create dynamic PDM roles
        for ( int idx =0; idx < subPatternResults.length; idx++ ) {
            PatternResult result = subPatternResults[idx];

            PDMType pdmType = result.getPdm();
            RoleType roleType = patternRepository.createRole(pdmType.getName(),   // name
                    PDMXMLConstants.ROLE_TYPE_PDM,   // type
                    Integer.toString(idx+1), // sequence
                    pdmType.getName(), // typeReferenceName
                    null, // queryName
                    null, // returnVariableName
                    null ); // arguments

            rolesType.getRole().add(roleType);

            RoleResult roleResult = new RoleResultImpl(newPDMResult);
            roleResult.setRole(roleType);
            roleResult.setRoleResult(result);

            newPDMResult.getRoles().add(roleResult);
        }

        newPDMResult.setRelationships(relationshipResults);
        Iterator relIterator = relationshipResults.iterator();
        while ( relIterator.hasNext() ) {
            RelationshipResult relResult = (RelationshipResult)relIterator.next();
            RelationshipType relType = relResult.getRelationship();

            newPDMType.getRelationships().getRelationship().add(relType);
        }

        return newPDMResult;
    }

    /**
     * Discover intra-role relationships for a given PDM
     * @param pattern
     * @param relationshipTypes
     * @return
     */
    public List discoverRoleRelationships(String pattern, String relationshipTypes[]) {
        PatternResult patternResult;
        patternResult = execute(pattern);

        return designAnalyzer.discoverRoleRelationships(patternResult, relationshipTypes);
    }

    public void loadAllPatterns(boolean async, final PatternEngineEventListener listener) {
        loadAllPatterns(SearchContext.GLOBAL_SEARCH, async, listener);
    }

    public void loadAllPatterns(final SearchContext searchCtx, boolean async, final PatternEngineEventListener listener) {
        patternSearchEngine.loadAllPatterns(searchCtx, async, listener);
    }

    public void shutdown() {
        pqlEngineFacade.shutdown();
    }


    // validation aspect
    private void validatePdmName(String pdmName) {
        if ( AcreStringUtil.isEmpty(pdmName))
            throw new PatternEngineException("Error : PDM name specified is null or empty");
    }

    private void validateRelationshipName(String relationshipName) {
        if ( AcreStringUtil.isEmpty(relationshipName))
            throw new PatternEngineException("Error : Relationship name specified is null or empty");
    }

    private void validateRelationshipType(String relationshipType[]) {
        //TODO - Check for valid types also i.e., calls, creates, etc
        for ( int i=0; i < relationshipType.length; i++ ) {
            if ( AcreStringUtil.isEmpty(relationshipType[i]))
                throw new PatternEngineException("Error : Relationship Type specified is null or empty");
        }
    }

    private void validateArtifactName(String artifactName) {
        if ( AcreStringUtil. isEmpty(artifactName))
            throw new PatternEngineException("Error : artifact name is null or empty");
    }


    public static void main (String args[]) {
        PatternRepository facade = null;
        PatternQueryRepository patternQueryRepository = null;
        PQLConnection pqlConnection = null;

        ConfigService config = ConfigService.getInstance();
        log.info("Got Config. Check preferences below.");
        log.info("Config Data = : " + config.getConfigData());
        log.info("Config determines what type of PQLConnection is obtained.");

        facade = DAOFactory.getPatternRepository();
        patternQueryRepository = DAOFactory.getPatternQueryRepository();

        try {
            //
            pqlConnection = PQLConnectionManager.getInstance().getGlobalConnection();
//                    PQLConnectionFactory.createPQLConnectionToRDB(
//                    "jdbc:mysql://localhost:3306/salsa", // dburl
//                    "salsa", "salsa"); // userid, password
        } catch (PQLException e) {
            e.printStackTrace();
        }

        facade.printPatternModels();
        patternQueryRepository.printGlobalQueryList();

        PDMType pdm;

        PatternEngine engine;
        engine = PatternEngineFactory.getInstance().getEngine();
        engine.setPatternRepository(facade);
        engine.setPatternQueryRepository(patternQueryRepository);
        engine.setPqlConnection(pqlConnection);

        PatternResult model=null;
        try {
            pdm = facade.getGlobalPatternModel("IF-intercepts-FC");
            model = engine.execute(pdm);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        log.info("Model=" +  model);
        log.info("DOT for Model=\ndigraph G{\n" +
                PDMModelUtil.generateDOTModel(model) + "\n}\n");
    }

    public PQLEngineFacade getPqlEngineFacade() {
        return pqlEngineFacade;
    }
}
