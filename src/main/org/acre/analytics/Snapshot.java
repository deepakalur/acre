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

import java.util.*;

/**
 * @author rajmohan@Sun.com
 */
public class Snapshot {
    public Snapshot(String system, String version, Date timestamp) {
        this.system = system;
        this.version = version;
        this.timestamp = timestamp;
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

    // Collection<PatternMetrics>
    public Collection getPatternMetrics() {
        return patternMetricsMap.values();
    }

    public PatternMetrics getPatternMetrics(String name) {
        return (PatternMetrics)patternMetricsMap.get(name);
    }

    public void addPatternMetrics(PatternMetrics patternMetrics) {
        patternMetrics.setSnapshot(this);
        patternMetricsMap.put(patternMetrics.getPatternName(), patternMetrics);
    }

    public boolean equals(Object obj) {
        if ( obj == this )
            return true;

        if ( obj == null )
            return false;

        if ( obj instanceof Snapshot) {
            Snapshot that = (Snapshot)obj;

            if ( isIdentical(getSystem(), that.getSystem()) &&
                 isIdentical(getVersion(), that.getVersion()) &&
                    isIdentical(getTimestamp(), that.getTimestamp()))
                    return true;
        }
        return false;
    }

    private boolean isIdentical(Object o1, Object o2) {
        if ( o1 == o2 ) // even if both are null
            return true;

        if ( o1==null || o2==null)
            return false;

        if ( o1 instanceof String)
            return ((String)o1).equalsIgnoreCase((String)o2);
        return o1.equals(o2);
    }

    public int hashCode() {
        return
                (system != null ? system.hashCode() : 0) +
                (version != null ? version.hashCode() : 0) +
                (timestamp != null ? timestamp.hashCode() : 0) ;
    }

    void setVersion(String version) {
        this.version = version;
    }

    void setSystem(String system) {
        this.system = system;
    }

    void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    private String version;
    private String system;
    private Date timestamp;
    private Map patternMetricsMap = Collections.synchronizedMap(new HashMap());

}
