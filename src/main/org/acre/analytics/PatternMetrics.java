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

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author rajmohan@Sun.com
 */
public class PatternMetrics {

    private String patternName;
    private int hitCount;

    // private Collection<PatternMetrics> roles;
    private Collection roles = new ArrayList();

    public Collection getRoles() {
        return roles;
    }

    public void setRoles(Collection roles) {
        this.roles = roles;
    }

    public PatternMetrics() {
    }

    public PatternMetrics(String name) {
        setPatternName(name);
    }

    public int getHitCount() {
        return hitCount;
    }

    public void setHitCount(int hitCount) {
        this.hitCount = hitCount;
    }

    public String getPatternName() {
        return patternName;
    }

    public void setPatternName(String patternName) {
        this.patternName = patternName;
    }

    public Snapshot getSnapshot() {
        return snapshot;
    }

    void setSnapshot(Snapshot snapshot) {
        this.snapshot = snapshot;
    }

    private Snapshot snapshot;

}
