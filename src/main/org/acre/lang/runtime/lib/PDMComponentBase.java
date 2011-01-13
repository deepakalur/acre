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
package org.acre.lang.runtime.lib;

import org.acre.lang.runtime.PQL;

import java.util.ArrayList;
import java.util.List;

/**
 * PDM component base class
 * @author Yury Kamen
 */
public abstract class PDMComponentBase implements PDMComponent {

    private String metaData;
    private String description;
    private String name;

    private PQL pql;

    public PDMComponentBase() {
    }

    public PDMComponentBase(PQL pql) {
        this.pql = pql;
    }

    public PQL getPql() {
        return pql;
    }

    public void setPql(PQL pql) {
        this.pql = pql;
    }

    public String getMetaData() {
        return metaData;
    }

    public void setMetaData(String metaData) {
        this.metaData = metaData;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getMetadata() {
        return metaData;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object execute() {
        return execute(null);
    }

    public List getOutputNames() {
        List list = new ArrayList(1);
        list.add("Results");
        return list;
    }

    public List getInputNames() {
        return new ArrayList(1);
    }
}
