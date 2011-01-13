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

import org.acre.common.AcreStringUtil;
import org.acre.config.ConfigService;
import org.acre.dao.PatternRepository;
import org.acre.pdmengine.SearchContext;
import org.acre.pdmengine.core.PatternResultCache;
import org.acre.pdmengine.core.PatternSearchEngine;
import org.acre.pdmengine.model.PatternResult;
import org.acre.pdmengine.model.QueryResult;
import org.acre.pdmengine.model.Result;
import org.acre.pdmengine.model.RoleResult;

import java.util.*;
import java.util.logging.Logger;

/**
 * @author rajmohan@Sun.com
 * @version Dec 14, 2004 2:42:27 PM
 */
public class PatternAggregator {
    PatternSearchEngine engine;
    PatternRepository patternRepository;

    Logger logger = ConfigService.getInstance().getLogger(PatternAggregator.class);

    public PatternAggregator(PatternRepository patternRepository, PatternSearchEngine engine) {
        this.patternRepository = patternRepository;
        this.engine = engine;
    }

    public String[] findPatterns(SearchContext searchContext, String[] artifactNames) {
        if ( AcreStringUtil.isEmpty(artifactNames))
            return new String[0];

        loadAndCachePatternArtifacts(searchContext);

        String system = (String)searchContext.getSystems().get(0);
        String version = (String)searchContext.getVersions().get(0);

        String[] patterns = findPatternsInSystem(artifactNames, system, version);
        return patterns;
    }

    private String[] findPatternsInSystem(String[] artifactNames, String system, String version) {
        // Identify & return only patterns that contain
        // ALL given artifacts
        Set commonPatterns;
        commonPatterns = systemArtifactCache.getPatterns(artifactNames[0], system, version);

        for ( int i=1; i < artifactNames.length; i++ ) {
            Set patterns = systemArtifactCache.getPatterns(artifactNames[i], system, version);
            if ( patterns == null ) {
                commonPatterns.clear();
                break;
            }
            commonPatterns.retainAll(patterns);
        }
        String resultPatternNames[] = new String[commonPatterns.size()];
        commonPatterns.toArray(resultPatternNames);
        return resultPatternNames;
    }

    private void loadAndCachePatternArtifacts(SearchContext searchContext) {

        engine.loadAllPatterns(searchContext, false, null);
        buildArtifactsMap();
        return;
    }

    private void buildArtifactsMap() {
        PatternResultCache patternResultCache = engine.getPDMResultCache();

        Iterator patternResultItr = patternResultCache.getCachedPDMResults().iterator();

        while ( patternResultItr.hasNext() ) {
            PatternResult patternResult = (PatternResult)patternResultItr.next();

            RoleResult[] roleResults = new RoleResult[patternResult.getRoles().size()];
            patternResult.getRoles().toArray(roleResults);

            for ( int iRole = 0 ; iRole < roleResults.length; iRole++ ) {
                RoleResult roleResult = roleResults[iRole];
                Result result = roleResult.getRoleResult();
                if ( result instanceof QueryResult) {
                    buildQueryResultArtifactsMap(patternResult, (QueryResult)result,
                            roleResult.getVariableName());
                }
            }
        }
    }

    private void buildQueryResultArtifactsMap(PatternResult pResult, QueryResult queryResult, String variable) {
        logger.info("BuildQueryResultArtifactsMap : " + pResult.getName() + ":" + variable);
        String artifactNames[] = queryResult.getArtifactNames(variable);

        for ( int i=0; i<artifactNames.length; i++) {
            String artifactName = artifactNames[i];
            systemArtifactCache.add(artifactName, pResult.getName(), pResult.getSystem(), pResult.getVersion());
        }
    }

    /** TODO - clarify requirements regarding parent vs ancestor PDMs for orphaned artifacts
    public String[] findAncestorPDMs(String pdmName) {
        Set ancestors = new HashSet();
        Iterator pdms = pdmType.values().iterator();
        while ( pdms.hasNext() ) {
            PDM pdm = (PDM)pdms.next();
            if ( isDescendantPDM(pdm, pdmName) ) {
                ancestors.add(pdm.getName());
            }
        }

        String ancestorNames[] = new String[ancestors.size()];
        ancestors.toArray(ancestorNames);
        return ancestorNames;
    }
    private boolean isDescendantPDM(PDM pdm, String pdmName) {
        Iterator roles = pdm.getRoles().getRole().iterator();
        while ( roles.hasNext() ) {
            Role role = (Role)roles.next();
            if ( PDMXMLConstants.ROLE_TYPE_PDM.equalsIgnoreCase(role.getType()) ) {
                String name = role.getTypeReferenceName();
                if ( AcreStringUtil.isEmpty(name) ) {
                    name = role.getName();
                }
                if ( name.equalsIgnoreCase(pdmName)) {
                    return true;
                }
                return isDescendantPDM(role.)
            }
        }
        return false;
    }
*/
/**
// TODO - To be Enabled for multiple-System searches
// clearer understanding of use-cases required
    List getSystemVersions(SearchContext searchContext) {
        List sysVersionList = new ArrayList();
        if  ( isGlobalSearch(searchContext) ) {
            AcreDbDAO dao = new AcreDbDAO();
            String[] systems = dao.findAllSystems();

            for ( int i=0; i < systems.length; i++) {
                String system = systems[i];

                String latestVersion = dao.findVersion(system, null);

                SystemVersion sysVersion = new SystemVersion(system, latestVersion);
                sysVersionList.add(sysVersion);
            }
        }
        else {
            List systems = searchContext.getSystems();
            List versions = searchContext.getVersions();

            for ( int i=0; i < systems.size(); i++) {
                String system = (String)systems.get(i);
                for ( int j=0; j < versions.size(); j++) {
                    String version = (String)versions.get(i);
                    SystemVersion sysVersion = new SystemVersion(system, version);
                    sysVersionList.add(sysVersion);
                }
            }
        }
        return sysVersionList;
    }

    private boolean isGlobalSearch(SearchContext searchContext) {
        String system = (String)searchContext.getSystems().get(0);
        String version = (String)searchContext.getVersions().get(0);
        return SearchContext.GLOBAL_SYSTEM.equalsIgnoreCase(system) &&
                SearchContext.LATEST_VERSION.equalsIgnoreCase(version);
    }

**/
    private class SystemArtifactCache {
        // (system,version) -> ArtifactCache
        private Map systemCache = new HashMap(7919);


        Set getPatterns(String artifactName, String system, String version) {
            ArtifactCache artifactCache = getArtifactCache(system, version);
            return artifactCache.getPatterns(artifactName.toLowerCase());
        }

        private ArtifactCache getArtifactCache(String system, String version) {
            String key = system.toLowerCase() + ":" + version.toLowerCase();
            ArtifactCache artifactCache = (ArtifactCache)systemCache.get(key);
            if ( artifactCache == null ) {
                artifactCache = new ArtifactCache();
                systemCache.put(key, artifactCache);
            }
            return artifactCache;
        }



        void add(String artifactName, String patternName, String system, String version) {
            ArtifactCache artifactCache = getArtifactCache(system, version);
            artifactCache.addArtifact(artifactName, patternName);
        }

        public String toString() {
            StringBuffer sb = new StringBuffer(10*1024);
            Iterator entries = systemCache.entrySet().iterator();
            while ( entries.hasNext() ) {
                Map.Entry entry = (Map.Entry)entries.next();
                sb.append(entry.getKey() + " : " + entry.getValue());
                sb.append("\n");
            }
            return sb.toString();
        }


        private class ArtifactCache {
            private Map artifactCache = new HashMap(7919);

            void addArtifact(String artifactName, String patternName) {
                Set patterns = (Set)artifactCache.get(artifactName.toLowerCase());
                if ( patterns == null ) {
                    patterns = new HashSet();
                    artifactCache.put(artifactName.toLowerCase(), patterns);
                }

                if ( !patterns.contains(patternName))
                    patterns.add(patternName);
            }

            Set getPatterns(String artifactName) {
                Set patterns = (Set)artifactCache.get(artifactName.toLowerCase());
                if ( patterns == null ) {
                    patterns = new HashSet();
                    artifactCache.put(artifactName.toLowerCase(), patterns);
                }
                return patterns;
           }

            public String toString() {
                StringBuffer sb = new StringBuffer(10*1024);
                Iterator entries = artifactCache.entrySet().iterator();
                while ( entries.hasNext() ) {
                    Map.Entry entry = (Map.Entry)entries.next();
                    sb.append(entry.getKey() + " : " + entry.getValue());
                    sb.append("\n");
                }
                return sb.toString();
            }
        }

    }

    private SystemArtifactCache systemArtifactCache = new SystemArtifactCache();

    private Map pdmType = new HashMap(997);
    boolean loaded = false;
}
