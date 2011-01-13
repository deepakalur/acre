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
package org.acre.analytics;

import org.acre.config.ConfigService;
import org.acre.dao.DAOFactory;
import org.acre.dao.PatternRepository;
import org.acre.dao.AcreDbDAO;
import org.acre.pdmengine.PatternEngine;
import org.acre.pdmengine.SearchContext;
import org.acre.pdmengine.model.*;

import java.util.*;
import java.util.logging.Logger;

/**
 * @author rajmohan@Sun.com
 */
class PatternAnalyticsEngineImpl implements PatternAnalyticsEngine {

    private static Logger logger = ConfigService.getInstance().getLogger(PatternAnalyticsEngineImpl.class);

    PatternEngine patternEngine;

    // TODO
//    static private Snapshots snapshotsCache ;

    public PatternAnalyticsEngineImpl(PatternEngine patternEngine) {
        this.patternEngine = patternEngine;

    }

    public Snapshots getSnapshots(Date startDate, Date endDate) {
        SnapshotQuery query = new SnapshotQuery();
        query.setStartDate(startDate);
        query.setEndDate(endDate);
        return getSnapshots(query);
    }

    public Snapshots getSnapshots(SnapshotQuery query) {

        List patterns = query.getPatternsFilter();
        if ( patterns.isEmpty())
            patterns = getCatalogPatterns();

        List systems = query.getSystemsFilter();
        if ( systems.isEmpty() ) {
            systems.add("global");
        }

        if ( !query.getVersionsFilter().isEmpty() ) {
            return getSnapshots(systems, query.getVersionsFilter(), patterns);
        }

        Date startDate = query.getStartDate();
        Date endDate = query.getEndDate();

        if ( endDate == null ) // default to current
            endDate = Calendar.getInstance().getTime();

        if ( startDate == null ) { // default to 1 year prior to endDate
            Calendar cal = Calendar.getInstance();
            cal.setTime(endDate);
            cal.add(Calendar.YEAR, -1);
            startDate = cal.getTime();
        }


        return getSnapshots(systems, startDate, endDate, patterns);
    }


    private Snapshots getSnapshots(List systems, Date startDate, Date endDate, List patterns) {
        Snapshots snapshots = new Snapshots();

        AcreDbDAO dao = new AcreDbDAO();
        // TODO - Use system name also
        Date extractionTimes[] = dao.getCharExtractTimeStamps(startDate, endDate);

        Iterator systemsItr = systems.iterator();
        while ( systemsItr.hasNext()) {
            String system = (String)systemsItr.next();
            for ( int i=0; i < extractionTimes.length; i++) {
                Snapshot snapshot = getSnapshotUsingExtractionTime(system, extractionTimes[i], patterns);
                if ( snapshot != null )
                    snapshots.addSnapshot(snapshot);
            }
        }
        return snapshots;
    }

    private Snapshots getSnapshots(List systems, List versions, List patterns) {

        Snapshots snapshots = new Snapshots();

        Iterator systemsItr = systems.iterator();
        while ( systemsItr.hasNext() ) {
            String system = (String)systemsItr.next();
            Iterator versionsItr = versions.iterator();
            while ( versionsItr.hasNext() ) {
                String version = (String)versionsItr.next();
                Snapshot snapshot = getSnapshotUsingVersion(system, version, patterns);
                snapshots.addSnapshot(snapshot);
            }
        }
        return snapshots;
    }


    private List getCatalogPatterns() {
        PatternRepository patternRepository;
        patternRepository = DAOFactory.getPatternRepository();

        return patternRepository.getGlobalPatternModelNames();
    }

    private Snapshot getSnapshotUsingExtractionTime(String system, Date extractionTime, List patterns) {
        return getSnapshot(system, null, extractionTime, patterns);
    }

    private Snapshot getSnapshotUsingVersion(String system, String version, List patterns) {
        return getSnapshot(system, version, null, patterns);
    }

    private Snapshot getSnapshot(String system, String version, Date extractionTime, List patterns) {

        Snapshot snapshot = new Snapshot(system, version, extractionTime);

        // execute all patterns
        // for each PatternResult compute design metrics
        Iterator patternNamesItr = patterns.iterator();
        while (patternNamesItr.hasNext()) {
            String patternName = (String)patternNamesItr.next();
            try {

                PatternResult patternResult;

                if ( version != null ) {
                    SearchContext ctx = new SearchContext();
                    ctx.addSystem(system);
                    ctx.addVersion(version);
                    patternResult = patternEngine.execute(patternName, null, ctx)[0];
                    // set timestamp when searching with version
                    snapshot.setTimestamp(patternResult.getTimestamp());

                }
                else {
                    SearchContext ctx = new SearchContext();
                    ctx.addSystem(system);
                    ctx.addTimestamp(extractionTime);
                    patternResult = patternEngine.execute(patternName, null, ctx)[0];
                    // set version when searching with timestamp
                    snapshot.setVersion(patternResult.getVersion());
                }

                PatternMetrics patternMetrics;
                patternMetrics = buildPatternMetrics(patternResult);

                snapshot.addPatternMetrics(patternMetrics);
            }
            catch(Throwable t) {
                // ignore & continue 
                logger.severe("Error searching pattern : " +  patternName +
                         " - " + t.getMessage());
            }
        }
        return snapshot;
    }


    public PatternMetrics getPatternMetrics(String patternName) {
        PatternResult patternResult = patternEngine.execute(patternName);

        PatternMetrics patternMetrics;
        patternMetrics = buildPatternMetrics(patternResult);

        return patternMetrics;
    }

    private PatternMetrics buildPatternMetrics(PatternResult patternResult) {
        PatternMetrics metrics = new PatternMetrics(patternResult.getName());

        int hits = getPatternHitCount(patternResult);
        metrics.setHitCount(hits);


        Iterator roles = patternResult.getRoles().iterator();
        while ( roles.hasNext()) {
            RoleResult roleResult = (RoleResult) roles.next();
            PatternMetrics subMetrics;
            if ( roleResult.getRoleResult() instanceof PatternResult) {
                subMetrics = buildPatternMetrics((PatternResult) roleResult.getRoleResult());
            }
            else {
                subMetrics = new PatternMetrics(roleResult.getName());
                QueryResult queryResult = (QueryResult) roleResult.getRoleResult();
                subMetrics.setHitCount(queryResult.getArtifacts().length);
            }
            metrics.getRoles().add(subMetrics);
        }
        return metrics;
    }



    /**
     * Check for pattern compliance by traversing the artifact-links in all relationships
     * @param patternResult
     * @return # of successful pattern hits
     */
    private int getPatternHitCount(PatternResult patternResult) {

        List relationships = getOrderedRelationships(patternResult);

        int hits = 0;
        if ( relationships.size() == 0 ) {
            Iterator roles = patternResult.getRoles().iterator();
            while ( roles.hasNext() ) {
                RoleResult roleResult = (RoleResult)roles.next();
                hits = Math.max(roleResult.getHitCount(), hits);
            }

        }
        else if ( relationships.size() == 1 ) {

            RelationshipResult relation = (RelationshipResult)relationships.get(0);
            ArtifactLink links[] = relation.getLinks();

            for ( int i=0; i < links.length; i++) {
                ArtifactLink link = links[i];
                Artifact targetArtifacts[] = link.getTargetArtifacts();
                hits +=  targetArtifacts.length;
            }
        }
        else {
            RelationshipResult startRelation = (RelationshipResult) relationships.get(0);

            List chainedRelations = relationships.subList(1, relationships.size());

            ArtifactLink [] links = startRelation.getLinks();

            for ( int l=0; l<links.length; l++) {
                Artifact sourceArtifact = links[l].getSourceArtifact();
                Artifact [] targetArtifacts = links[l].getTargetArtifacts();

                for ( int t=0; t < targetArtifacts.length; t++) {
                    hits += getCallPaths(targetArtifacts[t], chainedRelations, 0);
                }
            }
        }
        return hits;
    }

    private int getCallPaths(Artifact artifact, List relations, int relIdx) {
        RelationshipResult relationship = (RelationshipResult)relations.get(relIdx);
        int linkCount = 0;
        ArtifactLink chainLink = relationship.getLink(artifact);
        if ( chainLink != null ) {
            if ( relIdx < relations.size()-1 ) {
                relIdx++;
                Artifact [] targetArtifacts = chainLink.getTargetArtifacts();
                for ( int t=0; t < targetArtifacts.length; t++ ) {
                    linkCount += getCallPaths(targetArtifacts[t], relations, relIdx);
                }
            }
            else  // end of chain...get artifact count
                linkCount = chainLink.getTargetArtifacts().length;
        }
        return linkCount;

    }


    List getOrderedRelationships(PatternResult patternResult) {
            List allRelationships = new ArrayList();
            collectRelationships(patternResult, "Calls", allRelationships);
            return allRelationships;
    }



    private void collectRelationships(PatternResult patternResult, String type, List result) {

        Iterator relationships;
        relationships = patternResult.getRelationships().iterator();
        while ( relationships.hasNext()) {
            RelationshipResult relationship = (RelationshipResult)relationships.next();
            if ( relationship.getRelationship().getType().equalsIgnoreCase(type))
                result.add(relationship);
        }

        Iterator roles;
        roles = patternResult.getRoles().iterator();
        while ( roles.hasNext()) {
            RoleResult role = (RoleResult)roles.next();
            if ( role.getRoleResult() instanceof PatternResult) {
                collectRelationships((PatternResult)role.getRoleResult(), type, result);
            }
        }

    }

}
