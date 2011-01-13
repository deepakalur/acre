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
package org.acre.pdmengine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author rajmohan@Sun.com
 */
public class SearchContext implements Serializable {
    private List<String> systems = new ArrayList<String>();



    private List timestamps = new ArrayList();
    private List versions = new ArrayList();
    private int searchType = COARSE; // default search Type
    private Date startDate;
    private Date endDate;

    static public final int COARSE = 1;
    static public final int EXACT= 2;

    static public final String LATEST_VERSION = "Latest";
    static public final String GLOBAL_SYSTEM = "Global";

    static SearchContext GLOBAL_SEARCH = new SearchContext(GLOBAL_SYSTEM, LATEST_VERSION);

    public SearchContext() {
    }

    public SearchContext(String system, String version) {
        addSystem(system);
        addVersion(version);
    }

    public SearchContext(String system, String version, int searchType) {
        addSystem(system);
        addVersion(version);
        setSearchType(searchType);
    }

    public void addSystem(String system) {
        systems.add(system);
    }

    public void addTimestamp(Date date) {
        timestamps.add(date);
    }

    public void addVersion(String version) {
        versions.add(version);
    }

    public List getSystems() {
        return systems;
    }

    public List getTimestamps() {
        return timestamps;
    }

    public List getVersions() {
        return versions;
    }

    public int getSearchType() {
        return searchType;
    }

    public void setSearchType(int searchType) {
        this.searchType = searchType;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
