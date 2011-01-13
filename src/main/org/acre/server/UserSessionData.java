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
package org.acre.server;

import org.acre.analytics.PatternAnalyticsEngine;
import org.acre.analytics.AcreContainer;
import org.acre.pdmengine.PatternEngine;

/**
 * @author john.crupi@Sun.com
 *         rajmohan@sun.com
 */
public class UserSessionData {
    private UserContextObject userContext;
    private AcreContainer acreContainer;
    private long lastaccessed;

    UserSessionData( UserContextObject userContext, AcreContainer acreContainer) {
        this.userContext = userContext;
        this.acreContainer = acreContainer;
        touch();
    }

    UserContextObject getUserContext() {
        touch();
        return userContext;
    }

    String getName() {
        touch();
        return userContext.getName();
    }

    AcreContainer getAcreContainer() {
        touch();
        return acreContainer;
    }

    PatternEngine getPatternEngine() {
        return getAcreContainer().getPatternEngine();
    }


    PatternAnalyticsEngine getAnalyticsEngine() {
        return getAcreContainer().getAnalyticsEngine();
    }

    public long getLastaccessed() {
        return lastaccessed;
    }

    void touch() {
        lastaccessed = System.currentTimeMillis();
    }

    public void cleanup() {
        getPatternEngine().shutdown();
    }
}
