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
package org.acre.pdmengine.util;

import org.acre.pdmengine.PatternEngineException;
import org.acre.pdmengine.SearchContext;

import java.util.Date;

/**
 * @author rajmohan@Sun.com
 */
public class InputSpec {
    String patternName;
    int searchType;
    String system;
    String version;
    Date timestamp;

    public InputSpec(String patternName) {
        this(patternName, SearchContext.GLOBAL_SYSTEM, SearchContext.LATEST_VERSION, null);
    }

    public InputSpec(String patternName, InputSpec is) {
        this(patternName, is.getSystem(), is.getVersion(), is.getTimestamp());
        setSearchType(is.getSearchType());
    }

    public InputSpec(String patternName, String system, String version, Date time) {
        if ( (patternName ==null) || (system==null) ) {
            throw new PatternEngineException("Insufficient input parameters(patternName, system) specified");
        }
        this.patternName = patternName.toLowerCase();
        this.system = system.toLowerCase();
        this.version = version == null ? null : version.toLowerCase();
        this.timestamp = time;
        searchType = SearchContext.EXACT;
    }

    public String getPatternName() {
        return patternName;
    }

    public String getSystem() {
        return system;
    }

    public String getVersion() {
        return version;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public int hashCode() {
        return  patternName.hashCode() +
                system.hashCode() +
                (version != null ? version.hashCode() : 0 )+
                (timestamp != null  ? timestamp.hashCode() : 0 );
    }

    public boolean equals(Object obj) {
        if ( this == obj )
            return true;

        if ( obj instanceof InputSpec) {
            InputSpec that = (InputSpec)obj;
            if (    patternName.equalsIgnoreCase(that.patternName) &&
                    system.equalsIgnoreCase(that.system) &&
                    isIdentical(version, that.version) &&
                    isIdentical(timestamp, that.timestamp) &&
                    searchType == that.searchType
                    )
                return true;

        }
        return false;
    }

    private boolean isIdentical(String str1, String str2) {
        if ( str1 == str2 )  // if both are null, they are identical too.
            return true;

        if ( (str1 == null) || (str2==null) )
            return false;

        return str1.equalsIgnoreCase(str2);
    }

    private boolean isIdentical(Object obj1, Object obj2) {
        if ( obj1 == obj2 )  // if both are null, they are identical too.
            return true;

        if ( (obj1 == null) || (obj2==null) )
            return false;

        return obj1.equals(obj2);
    }

    public int getSearchType() {
        return searchType;
    }

    public void setSearchType(int searchType) {
        if ( searchType != SearchContext.COARSE &&
                searchType != SearchContext.EXACT )
            throw new IllegalArgumentException("PatternConformance can only be COARSE or EXACT");
        this.searchType = searchType;
    }

    public boolean isExactSearch() {
        return searchType == SearchContext.EXACT;
    }

    public boolean isCoarseSearch() {
        return searchType == SearchContext.COARSE;
    }
}
