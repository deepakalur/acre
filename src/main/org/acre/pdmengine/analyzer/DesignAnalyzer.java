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
package org.acre.pdmengine.analyzer;

import org.acre.config.ConfigService;
import org.acre.lang.pql.pdbc.PQLResultSet;
import org.acre.pdmengine.PatternEngineException;
import org.acre.pdmengine.core.RelationshipExecutor;
import org.acre.pdmengine.model.*;
import org.acre.pdmengine.model.impl.FilteredLinks;
import org.acre.pdmengine.model.impl.PatternResultImpl;
import org.acre.pdmengine.model.impl.QueryResultImpl;
import org.acre.pdmengine.pqe.InvRelcoCommand;
import org.acre.pdmengine.pqe.PQLEngineFacade;
import org.acre.pdmengine.pqe.RelcoCommand;
import org.acre.pdmengine.util.BidiLinksMap;
import org.acre.pdmengine.util.LinkMapBuilder;
import org.acre.pdmengine.util.PatternEngineUtil;
import sun.misc.SoftCache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DesignAnalyzer {

    private PQLEngineFacade pqlEngineFacade;

    public DesignAnalyzer(PQLEngineFacade pqlEngineFacade) {
         this.pqlEngineFacade = pqlEngineFacade;
    }


    /**
     * Discover inter-role relationships between given PDMs
     * @param pdms
     * @param relationshipTypes
     * @return
     */
    // List<RelationshipResult>
    public List discoverRelationships(PatternResult pdms[], String [] relationshipTypes) {
        List resultsList = new ArrayList();

        if ( pdms.length == 0 || relationshipTypes.length == 0)
            return resultsList;

        for ( int idx=0; idx < pdms.length-1; idx++) {
            PatternResult pdm1 = pdms[idx];
            for ( int idx2=idx+1; idx2 < pdms.length; idx2++ ) {
                PatternResult pdm2 = pdms[idx2];
                RelationshipResult[] results = discoverRelationships(pdm1, pdm2, relationshipTypes);
                resultsList.addAll(Arrays.asList(results));
            }
        }
        return resultsList;
    }

    public List discoverRoleRelationships(PatternResult pattern, String relationshipType[]) {

        RelationshipExecutor relationshipExecutor = new RelationshipExecutor(pqlEngineFacade);
        List resultList = relationshipExecutor.discoverRelationships(pattern, pattern, relationshipType);

        return resultList;

    }



    public RelationshipResult[] discoverRelationships(PatternResult sourcePattern, PatternResult targetPattern,
                                                      String[] relationshipType) {

        RelationshipExecutor relationshipExecutor = new RelationshipExecutor(pqlEngineFacade);

        List results1 = relationshipExecutor.discoverRelationships(sourcePattern, targetPattern, relationshipType);
        List results2 = relationshipExecutor.discoverRelationships(targetPattern, sourcePattern, relationshipType);
        results2.addAll(results1);

        RelationshipResult[] results = new RelationshipResult[results2.size()];
        results2.toArray(results);
        return results;
    }


    /**
        Relco: Relative Complement functionality.
        Relative Complement will be available for Relationships between Roles.
        So a user will be able to select a connector between two Roles and
        say "find Relative Complement" or "find Inverse Relative Complement"

        Relco of p
        p is defined as x calls y
        RelCo(P) = Universal set of classes calls y Artifacts - x Artifacts
     * @param roleName
     * @param relationshipType
     * @param artifactName
     * @return
     */
        public Artifact[] findRelcos(PatternResultImpl pdmResult, String roleName,
                                     String relationshipType, String artifactName) {

            BidiLinksMap bidiLinksMap;
            bidiLinksMap = findRelcoArtifacts(pdmResult, roleName, relationshipType);

            // Map<String /*ArtifactName*/ , ArtifactLink>
            Map relcosMap;
            relcosMap = bidiLinksMap.getReverseLinksMap();

            ArtifactLink result = (ArtifactLink)relcosMap.get(artifactName);
            if ( result != null )
                return result.getTargetArtifacts();
            return null;
        }
/**
 * find Relco of (pdmResults.roles calls roleName) i.e.,
 * all artifacts not in the "current scene"(represented by pdmResult) that call roleName.
 *
 * @param pdmResult
 * @param roleName
 * @param relationshipType
 * @return
 */
        public ArtifactLink[] findRelcos(PatternResultImpl pdmResult, String roleName,
                                         String relationshipType) {
            // Map<String /*ArtifactName*/ , ArtifactLink>
            Map relcosMap;
            BidiLinksMap bidiLinksMap;
            bidiLinksMap = findRelcoArtifacts(pdmResult, roleName, relationshipType);

            relcosMap = bidiLinksMap.getLinksMap();

            ArtifactLink links[];
            links = new ArtifactLink[relcosMap.values().size()];
            relcosMap.values().toArray(links);
            return links;

        }


        public BidiLinksMap findRelcoArtifacts(PatternResultImpl pdmResult, String roleName,
                                    String relationshipType) {
            BidiLinksMap bidiLinksMap;
            String cacheKey = roleName + "_" + relationshipType;
            bidiLinksMap = (BidiLinksMap)roleRelcoCache.get(cacheKey);
            if ( bidiLinksMap != null )
                return bidiLinksMap;


            // Step 1 :
            // find all roles associated with "roleName" for the given relationshipType (SetX)

            // List<RoleResult> associatedRoles;
            List associatedRoles;
//            associatedRoles = pdmResult.findAssociatedFromRoles(roleName, relationshipType);
            associatedRoles = pdmResult.findAssociatedRoles(roleName, relationshipType);

            logger.info("# of associated roles - " + associatedRoles.size());

            // Step 2 : find roleName's roleResult (SetY)
            RoleResult targetRoleResult = pdmResult.searchRole(roleName);
            if ( targetRoleResult == null ) {
                throw new PatternEngineException("PDMRelco execution error - Reference role not found : "
                        + roleName);
            }

            // Step 3: Relco(SetX calls SetY)
            String targetEntity = targetRoleResult.getVariableName();
            String resultEntity = "relco_" + PatternEngineUtil.generateTempName() + " /* " + pdmResult.getName() + " */";

            RelcoCommand relcoCommand = new RelcoCommand();

            String assocRoles[] = new String[associatedRoles.size()];
            for ( int iRole=0; iRole < associatedRoles.size(); iRole++ ) {
                RoleResult roleResult = (RoleResult)associatedRoles.get(iRole);
                assocRoles[iRole] = roleResult.getVariableName();
            }

            Map relcoResult;

            // raw relco-links
            relcoResult = relcoCommand.execute(pqlEngineFacade, resultEntity, assocRoles,
                    targetEntity, relationshipType);

            // filter raw relco-links against only 'valid pattern artifacts'
            PQLResultSet rs = (PQLResultSet)relcoResult.values().iterator().next();
            FilteredLinks fLinks = new FilteredLinks(rs);
            fLinks.intersectTargets(((QueryResultImpl)targetRoleResult.getRoleResult()).getArtifacts());
            bidiLinksMap = LinkMapBuilder.buildLinkMap(fLinks.getLinks());

            roleRelcoCache.put(cacheKey, bidiLinksMap);
            return bidiLinksMap;
        }

        public Artifact[] findInvRelcos(PatternResultImpl pdmResult, String roleName,
                                       String relationshipType, String artifactName) {

            BidiLinksMap bidiLinksMap;
            bidiLinksMap = findInvRelcoArtifacts(pdmResult, roleName, relationshipType);

            // Map<String /*ArtifactName*/ , ArtifactLink>
            Map invRelcosMap;
            invRelcosMap = bidiLinksMap.getLinksMap();  // Cache FIX

            ArtifactLink result = (ArtifactLink)invRelcosMap.get(artifactName);
            if ( result != null )
                return result.getTargetArtifacts();
            return null;
        }

        public ArtifactLink[] findInvRelcos(PatternResultImpl pdmResult, String roleName, String relationshipType) {
            // Map<String /*ArtifactName*/ , ArtifactLink>
            BidiLinksMap bidiLinksMap;
            bidiLinksMap = findInvRelcoArtifacts(pdmResult, roleName, relationshipType);

            Map invRelcosMap;
            invRelcosMap = bidiLinksMap.getLinksMap();

            ArtifactLink links[];
            links = new ArtifactLink[invRelcosMap.values().size()];
            invRelcosMap.values().toArray(links);
            return links;

        }

        public BidiLinksMap findInvRelcoArtifacts(PatternResultImpl pdmResult, String roleName,
                                                  String relationshipType) {
            BidiLinksMap bidiLinksMap;
            String cacheKey = roleName + "_" + relationshipType;
            bidiLinksMap = (BidiLinksMap)roleInvRelcoCache.get(cacheKey);
            if ( bidiLinksMap != null )
                return bidiLinksMap;


            // Step 1 :
            // find all roles associated with "roleName" for the given relationshipType (SetX)

            // List<RoleResult> associatedRoles;
            List associatedRoles;
//            associatedRoles = pdmResult.findAssociatedToRoles(roleName, relationshipType);
            associatedRoles = pdmResult.findAssociatedRoles(roleName, relationshipType);

            logger.info("# of associated roles - " + associatedRoles.size());

            // Step 2 : find roleName's roleResult (SetY)
            RoleResult sourceRoleResult = pdmResult.searchRole(roleName);
            if ( sourceRoleResult == null ) {
                throw new PatternEngineException("PDMInvRelco execution error - Reference role not found : "
                        + roleName);
            }

            // Step 3: InvRelco(SetX calls SetY)
            String sourceEntity = sourceRoleResult.getVariableName();
            String resultEntity = "invrelco_" + PatternEngineUtil.generateTempName() + " /* " + pdmResult.getName() + " */";

            InvRelcoCommand invRelcoOp = new InvRelcoCommand();

            String assocRoles[] = new String[associatedRoles.size()];
            for ( int iRole=0; iRole < associatedRoles.size(); iRole++ ) {
                RoleResult roleResult = (RoleResult)associatedRoles.get(iRole);
                assocRoles[iRole] = roleResult.getVariableName();
            }

            Map relcoResult;

            // raw inv-relco links
            relcoResult = invRelcoOp.execute(pqlEngineFacade, resultEntity, sourceEntity,
                    assocRoles, relationshipType);

            // filter raw invrelco-links against only 'valid pattern artifacts'
            PQLResultSet rs = (PQLResultSet)relcoResult.values().iterator().next();
            FilteredLinks fLinks = new FilteredLinks(rs);
            fLinks.intersectSources(((QueryResultImpl)sourceRoleResult.getRoleResult()).getArtifacts());
            bidiLinksMap = LinkMapBuilder.buildLinkMap(fLinks.getLinks());

            roleInvRelcoCache.put(cacheKey, bidiLinksMap);
            return bidiLinksMap;
        }

        private static Logger logger = ConfigService.getInstance().getLogger(PatternResultImpl.class);

        private ResultCache roleRelcoCache = new ResultCache();
        private ResultCache roleInvRelcoCache = new ResultCache();

}

// ResultCache<type>
class ResultCache extends SoftCache {
    protected static final int DEFAULT_SIZE = 7919;
    private Logger logger = ConfigService.getInstance().getLogger(ResultCache.class);
    private long cacheRequests;
    private long cacheHits;

    public ResultCache() {
        super(DEFAULT_SIZE);
    }

    public Object get(String name) {
        cacheRequests++;
        Object result =  super.get(name.toLowerCase());
        if ( result != null ) {
            cacheHits++;
            if ( logger.isLoggable(Level.INFO) ) {
                logger.info("Cache Hit : " + name);
                logger.info(toString());
            }
        }
        return result;
    }

    public void put(String name, Object result) {
        super.put(name.toLowerCase(), result);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(200);
        sb.append("PatternResult ")
                .append(" Total Requests : " + cacheRequests).
                append(" Positive Hits : " + cacheHits)
                .append(this.keySet());
        return sb.toString();
    }
}
