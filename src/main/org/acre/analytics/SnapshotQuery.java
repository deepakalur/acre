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
import java.util.Date;
import java.util.List;

/**
 * Query specification for snapshots
 * using system, version, startdate, enddate, patternname
 * - {system=PSA, startDate=dt1, endDate=dt2}
 * - {system=PSA, system=Commons, system=Finance, startDate=dt1, endDate=dt2}
 * - {system=global, version=REL_21}
 * - {system=global, version=REL_21, version=REL_22, version=REL_23}
 * - {system=global, pattern=singleton, startDate=dt1, endDate=dt2}
 * - {system=global, version=REL_EA2, pattern=dynamicProxy}
 * - {system=PSA, pattern=smartProxy}
 *
 */
public class SnapshotQuery {

    public SnapshotQuery() {
    }

    public SnapshotQuery(String name, Date startDate, Date endDate) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public SnapshotQuery(String name, Date startDate, Date endDate, String versionName, String systemName, String patternName) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        addVersionFilter(versionName);
        addSystemFilter(systemName);
        addPatternFilter(patternName);
    }

    String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    List getSystemsFilter() {
        return systems;
    }

    public void addSystemFilter(String system) {
        systems.add(system);
    }

    List getVersionsFilter() {
        return versions;
    }

    public void addVersionFilter(String version) {
        versions.add(version);
    }

    List getPatternsFilter() {
        return patterns;
    }

    public void addPatternFilter(String pattern) {
        patterns.add(pattern);
    }


    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof SnapshotQuery))
            return false;

        final SnapshotQuery that = (SnapshotQuery) o;

        if ( !name.equals(that.name) )
            return false;

        if ( !isSameDate(startDate, that.startDate))
            return false;

        if ( !isSameDate(endDate, that.endDate))
            return false;

        if ( !systems.equals(that.systems))
            return false;

        if ( !patterns.equals(that.patterns))
            return false;

        if ( !versions.equals(that.versions))
            return false;

        return true;
    }

    private boolean isSameDate(Date dt1, Date dt2) {

        if ( dt1 != null )
            return dt1.equals(dt2);

        if ( dt2 != null )
            return dt2.equals(dt1);

        return true; // both are null
    }

    public int hashCode() {
        int result;
        result = name.hashCode();
        result = 29 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 29 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 29 * result + (versions != null ? versions.hashCode() : 0);
        result = 29 * result + (systems != null ? systems.hashCode() : 0);
        result = 29 * result + (patterns != null ? patterns.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "SnapshotQuery{" +
                "name='" + name + "'" +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", versionName='" + versions + "'" +
                ", systemName='" + systems + "'" +
                ", patternName='" + patterns + "'" +
                "}";
    }


    private String name = "";
    private Date startDate;
    private Date endDate;

    // List<String>
    private List systems = new ArrayList();

    // List<String>
    private List versions = new ArrayList();

    // List<String>
    private List patterns = new ArrayList();
}
