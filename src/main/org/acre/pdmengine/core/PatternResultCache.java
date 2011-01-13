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

import org.acre.common.AcreStringUtil;
import org.acre.config.ConfigService;
import org.acre.pdmengine.model.PatternResult;
import org.acre.pdmengine.util.InputSpec;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PatternResultCache {
    protected static final int DEFAULT_SIZE = 7919;

    private Map cache = Collections.synchronizedMap(new HashMap(DEFAULT_SIZE));

    private Logger logger = ConfigService.getInstance().getLogger(PatternResultCache.class);
    private long cacheRequests;
    private long cacheHits;

    public PatternResultCache() {
    }

    public PatternResult get(InputSpec inputSpec) {
        cacheRequests++;
        PatternResult result =  (PatternResult)cache.get(inputSpec);
        if ( result != null ) {
            cacheHits++;
            if ( logger.isLoggable(Level.INFO) ) {
                logger.info("Cache Hit : " + inputSpec.getPatternName());
                logger.info(toString());
            }
        }
        return result;
    }

    // List<PatternResult>
    public Collection getCachedPDMResults() {
             return Collections.unmodifiableCollection(cache.values());
    }

    public void put(InputSpec inputSpec, PatternResult result)  {
        cache.put(inputSpec, result);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(200);
        sb.append("PatternResult ")
                .append(" Total Requests : " + cacheRequests).
                append(" Positive Hits : " + cacheHits)
                .append(this.cache.keySet());
        return sb.toString();
    }


    public void remove(String patternName) {
        if ( AcreStringUtil.isEmpty(patternName))
            return;

        Iterator keys = cache.keySet().iterator();
        while ( keys.hasNext()) {
            InputSpec is = (InputSpec)keys;
            if ( patternName.equalsIgnoreCase(is.getPatternName()) ) {
                cache.remove(is);
            }
        }
    }

    public void clear() {
        cache.clear();        
    }
}
