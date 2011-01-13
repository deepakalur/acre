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
package org.acre.visualizer.grappa;

import org.acre.analytics.AcreContainer;
import org.acre.analytics.AcreContainerFactory;
import org.acre.lang.pql.pdbc.PQLException;
import org.acre.pdmengine.PatternEngine;
import org.acre.pdmengine.SearchContext;
import org.acre.pdmengine.model.PatternResult;

import java.util.logging.Logger;

/**
 * Connects to SALSA fact database, and executes a given PDM
 *
 * @author Yury Kamen
 */
public class PDMExecutor {
    Logger log = Logger.getLogger("PDMExecutor");

    private PatternEngine patternEngine;
    private AcreContainer acreContainer;

    public PDMExecutor() {
        acreContainer = AcreContainerFactory.getInstance().getSalsaContainer();
        patternEngine = acreContainer.getPatternEngine();
    }

    public void setUp() throws Exception {
    }


    public void tearDown() throws Exception {
    }


    public PatternResult execute(String pdmName) throws PQLException {
        return patternEngine.execute(pdmName, SearchContext.COARSE);
    }

    public PatternResult execute (String pdmName, String systemName, String versionName) throws PQLException {
        SearchContext ctx = new SearchContext();
        ctx.setSearchType(SearchContext.COARSE);
        ctx.addSystem(systemName);
        ctx.addVersion(versionName);

        // return shoudl be an array for the given system/version
        PatternResult [] res = patternEngine.execute(pdmName, null, ctx);
        if ((res != null) && (res.length != 0))
            return res[0];
        else
            return null;
    }

//    public void testApplicationControllerPDM() throws PQLException {
//        PatternResult pdmResult = execute("ApplicationController");
//        Iterator relationships = pdmResult.getRelationships().iterator();
//        log.info(pdmResult.toString());
//    }
}
