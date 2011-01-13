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
import org.acre.common.AcreStringUtil;
import org.acre.config.ConfigService;
import org.acre.dao.PDMValidator;
import org.acre.dao.PatternQueryRepository;
import org.acre.dao.PatternRepository;
import org.acre.dao.AcreDbDAO;
import org.acre.pdmengine.PatternEngineEventListener;
import org.acre.pdmengine.PatternEngineException;
import org.acre.pdmengine.SearchContext;
import org.acre.pdmengine.model.PatternResult;
import org.acre.pdmengine.model.impl.PatternResultImpl;
import org.acre.pdmengine.pqe.PQLEngineFacade;
import org.acre.pdmengine.util.InputSpec;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author rajmohan@Sun.com
 */
public class PatternSearchEngine {

    static Logger log = ConfigService.getInstance().getLogger(PatternSearchEngine.class);

    // per-user state
    private PatternResultCache patternResultCache = new PatternResultCache();

    private Set disabledPatterns = new HashSet();

    private PQLEngineFacade pqlEngineFacade;
    private PatternRepository patternRepository;
    private PatternQueryRepository queryRepository;

    public PatternSearchEngine(PQLEngineFacade pqlEngineFacade, PatternRepository patternRepository,
                               PatternQueryRepository queryRepository) {
        this.pqlEngineFacade = pqlEngineFacade;
        this.patternRepository = patternRepository;
        this.queryRepository = queryRepository;
    }

    public PatternResult[] execute(String patternName, Map params, SearchContext searchCtx)
            throws PatternEngineException {

        if ( AcreStringUtil.isEmpty(patternName) )
            throw new IllegalArgumentException("Specify a valid pattern name. ");


        if ( searchCtx == null )
            return execute(patternName, params, null, (String[])null, SearchContext.EXACT);

        List systemsList = searchCtx.getSystems();
        List versionsList = searchCtx.getVersions();
        List timestampsList = searchCtx.getTimestamps();

        String []systems = new String[systemsList.size()];
        systemsList.toArray(systems);

        String []versions = new String[versionsList.size()];
        versionsList.toArray(versions);

        Date []timestamps = new Date[timestampsList.size()];
        timestampsList.toArray(timestamps);

        // search for patterns using
        // systems & versions;
        // systems & timestamps
        // systems & startdate/enddate
        PatternResult[] result = null;
        if ( timestamps.length > 0) {
            result = execute(patternName, params, systems, timestamps,
                    searchCtx.getSearchType());
        }
        else if ( searchCtx.getStartDate() != null || searchCtx.getEndDate() != null ) {
            result = execute(patternName, params, systems,
                    searchCtx.getStartDate(), searchCtx.getEndDate(),
                    searchCtx.getSearchType());
        }
        else
            result = execute(patternName, params, systems, versions,
                    searchCtx.getSearchType());
        return result;
    }

    public PatternResult[] execute(String patternName, Map params,
                                   String systems[], String versions[],
                                   int searchType) {

        if ( AcreStringUtil.isEmpty(systems))
            systems = new String[]{SearchContext.GLOBAL_SYSTEM};

        if ( AcreStringUtil.isEmpty(versions) )
            versions = new String[]{SearchContext.LATEST_VERSION};

        List results = new ArrayList();
        for (int systemIdx=0; systemIdx < systems.length; systemIdx++ ) {
            for ( int versionIdx=0; versionIdx < versions.length; versionIdx++) {
                PatternResult pr;
                InputSpec inputSpec = new InputSpec(patternName, systems[systemIdx],
                        versions[versionIdx], null);
                inputSpec.setSearchType(searchType);
                pr = execute(inputSpec, params);
                results.add(pr);
            }
        }

        PatternResult[] prs = new PatternResult[results.size()];
        results.toArray(prs);
        return prs;
    }

    
    public PatternResult[] execute(String patternName, Map params, String system[],
                                   Date startDate, Date endDate, int searchType) {

        Date timeStamps[] = null;

        AcreDbDAO dao = new AcreDbDAO();
        timeStamps = dao.getCharExtractTimeStamps(startDate, endDate);

        return execute(patternName, params, system, timeStamps, searchType);
    }

    public PatternResult[] execute(String patternName, Map params, String system[],
                                   Date time[], int searchType) {
        if ( AcreStringUtil.isEmpty(system) )
            system = new String[]{"global"};

        if ( time==null || time.length==0 )
            throw new IllegalArgumentException("Specify a valid extraction timestamp.");

        List results = new ArrayList();

        for (int systemIdx=0; systemIdx < system.length; systemIdx++ ) {
            for ( int timeIdx=0; timeIdx< time.length; timeIdx++) {
                PatternResult pr;
                InputSpec inputSpec = new InputSpec(patternName, system[systemIdx], null,
                                                            time[timeIdx]);
                inputSpec.setSearchType(searchType);
                pr = execute(inputSpec, params);
                results.add(pr);
            }
        }

        PatternResult[] prs = new PatternResult[results.size()];
        results.toArray(prs);
        return prs;
    }

    /**
 * Search the characterstics database for the given design-pattern spec.
  * @param inputSpec - design-pattern specification to be grokked for.
 * @param params - paramterized customization for patterns to be searched
 * @return - result design artifacts matching the queried for patterns
 * @throws PatternEngineException
 */
    public PatternResult execute(InputSpec inputSpec, Map params)
            throws PatternEngineException {
        String patternName = inputSpec.getPatternName();

        PDMType pdm = patternRepository.getGlobalPatternModel(patternName);
        if ( pdm == null ) {
            throw new PatternEngineException("PDM name not found : " + patternName +
                    " - verify PDM name and check acreRootDirectory setting ");
        }
        PatternResult result = null;

        try {
            result = execute(pdm, params, inputSpec);
        } catch (PatternEngineException e) {
            log.log(Level.SEVERE,"Error executing <" + patternName +  "> PDM ---> " + e.getMessage());
            throw e;
        }
        catch (Throwable e) {
            String msg = "Error executing PDM : " + patternName + " - " +  e.getMessage();
            log.log(Level.SEVERE, msg, e);
            throw new PatternEngineException(msg, e);
        }

        return result;
    }

    public PatternResult execute(PDMType pdm, Map params, InputSpec inputSpec ) throws PatternEngineException {
        log.info("Creating PDM Model: " + pdm.getName());

        PatternResult patternResult;
        boolean isCacheablePDM = true; // Cache only non-parameterized PDMs

        // Don't cache parameterized PDM's
        if ( (params != null && (params.size() > 0)) ) {
            isCacheablePDM = false;
        }

        if ( isCacheablePDM ) {
            patternResult = patternResultCache.get(inputSpec);
            if ( patternResult != null )
                return patternResult;
        }

        setSearchScope(inputSpec);

        if ( isScriptedPDM(pdm) ) {
                ScriptedPatternExecutor scriptedPattern = new ScriptedPatternExecutor(patternRepository,
                        queryRepository, pqlEngineFacade);
                patternResult = scriptedPattern.execute(pdm, params, inputSpec);
        }
        else {
            ModelPatternExecutor modelPattern = new ModelPatternExecutor(patternRepository,
                    queryRepository, pqlEngineFacade, this);
            patternResult = modelPattern.executeModelPDM(pdm, params, inputSpec);
        }

        if ( inputSpec.isExactSearch()) {
                patternResult = refinePatternResults(patternResult);
        }

/**
        PatternResultProxy proxyClass = new PatternResultProxy(patternResult, filteredPattern);

        PatternResult proxy =
                (PatternResult)Proxy.newProxyInstance(PatternResult.class.getClassLoader(),
                        new Class[]{PatternResult.class},
                        proxyClass);
**/
        if ( isCacheablePDM ) {
            patternResultCache.put(inputSpec, patternResult);
            log.info("Caching PDM - " + pdm.getName());
        }
        return patternResult;
    }

    private PatternResult refinePatternResults(PatternResult patternResult) {
        PatternResultImpl refinedResult = new PatternResultImpl(patternResult, null);
        refinedResult.setPatternConformance(SearchContext.EXACT);

        // Process raw PatternResult and build a patterns-compliant only PatternResult
        ((PatternResultImpl)refinedResult).filterAndJoinLinks();

        patternResult = refinedResult;
        return patternResult;
    }

    private String quote(String s) {
        return "\"" + s + "\"";
    }

    private void setSearchScope(InputSpec inputSpec) {
        pqlEngineFacade.setSearchScope(inputSpec.getSystem(),
                 inputSpec.getVersion(), inputSpec.getTimestamp());
    }


    private boolean isScriptedPDM(PDMType pdm) {
        return PDMValidator.getInstance().isScriptedPDM(pdm);
    }

    public PatternResultCache getPDMResultCache() {
        return patternResultCache;
    }

    public void refresh(String patternName) {
        // just remove from cache.
        // Will execute the PDM lazily
        patternResultCache.remove(patternName);
    }

    public void refreshAll() {
        // just remove from cache.
        // Will execute the PDM lazily
        patternResultCache.clear();
    }


    public void loadAllPatterns(final SearchContext searchCtx, boolean async, final PatternEngineEventListener listener) {
        if ( async ) {
            Thread worker = new Thread(new Runnable() {

                public void run() {
                    executeAllPatterns(searchCtx);
                    if ( listener != null )
                        listener.patternsSearchComplete();

                }
            });

            worker.setDaemon(true);
            worker.start();
        }
        else
            executeAllPatterns(searchCtx);

    }

    private void executeAllPatterns(SearchContext searchCtx) {
        Iterator patternNames = patternRepository.getGlobalPatternModelNames().iterator();

        while (patternNames.hasNext()) {
            String pattern = (String)patternNames.next();
            try {
                if ( !disabledPatterns.contains(pattern) )
                    execute(pattern, null, searchCtx);
            }
            catch(Throwable t) {
                // ignore & continue for now
                disabledPatterns.add(pattern);

                log.severe("Error searching pattern : " + pattern + " - " + t.getMessage());
            }
        }
    }

}
