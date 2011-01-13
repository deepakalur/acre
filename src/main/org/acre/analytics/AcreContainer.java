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

import org.acre.dao.DAOFactory;
import org.acre.dao.PatternQueryRepository;
import org.acre.dao.PatternRepository;
import org.acre.pdmengine.PatternBuilder;
import org.acre.pdmengine.PatternEngine;
import org.acre.pdmengine.PatternEngineImpl;
import org.acre.pdmengine.core.PatternBuilderImpl;
import org.acre.pdmengine.core.PatternSearchEngine;
import org.acre.pdmengine.pqe.PQLEngineFacade;
import org.acre.pdmengine.pqe.PQLEngineFacadeFactory;

/**
 * @author rajmohan@Sun.com
 * AcreContainer acts as a Custom IoC container for Acre.
 * It is responsible for creating and assembling various component services,
 * thus promoting looser-coupling(& DIP principle) among various Acre engine components.
 *
 */
public class AcreContainer {

    private PatternEngine patternEngine;
    private PatternAnalyticsEngine analyticsEngine;
    private PatternBuilder patternBuilder;
    private PQLEngineFacade pqlEngineFacade;
    private PatternSearchEngine patternSearchEngine;
    private PatternRepository patternRepository;
    private PatternQueryRepository patternQueryRepository;

    public AcreContainer() {
        // instantiate and assemble Component services
        patternRepository = DAOFactory.getPatternRepository();
        patternQueryRepository = DAOFactory.getPatternQueryRepository();
        pqlEngineFacade = PQLEngineFacadeFactory.getInstance().createPQLEngineFacade();

        patternSearchEngine = new PatternSearchEngine(pqlEngineFacade,
                patternRepository,
                patternQueryRepository);

        patternEngine = new PatternEngineImpl(pqlEngineFacade,
                patternRepository,
                patternQueryRepository,
                patternSearchEngine);

        analyticsEngine = new PatternAnalyticsEngineImpl(patternEngine);

        patternBuilder = new PatternBuilderImpl(pqlEngineFacade);
    }

    public PatternAnalyticsEngine getAnalyticsEngine() {
        return analyticsEngine;
    }

    public PatternEngine getPatternEngine() {
        return patternEngine;
    }

    public PatternBuilder getPatternBuilder() {
        return patternBuilder;
    }
}
