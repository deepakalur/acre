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
package org.acre.pdmengine.pqe;

import java.util.Map;

/**
 * @author rajmohan@Sun.com
 * @version Jan 16, 2004
 */

public class PQLVariable {
    public static final String TYPE_CLASS = "JClass";
    public static final String TYPE_PACKAGE = "JPackage";

    // Implicit Variables available from PQLEngine
    public static PQLVariable allClasses = new PQLVariable("classes",
            TYPE_CLASS);
    public static PQLVariable allPackages = new PQLVariable("packages",
            TYPE_PACKAGE);

    public PQLVariable(String name) {
        this(name, "unknown", null);
    }

    public PQLVariable(String name, String type) {
        this(name, type, null);
    }

    public PQLVariable(String name, String type, Map pqlResultMap) {
        this.name = name;
        this.type = type;
        this.pqlResultMap = pqlResultMap;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map getPqlResultMap() {
        return pqlResultMap;
    }

    public void setPqlResultMap(Map pqlResultMap) {
        this.pqlResultMap = pqlResultMap;
    }

    private String name;
    private String type;
    private Map pqlResultMap;

}

class PQLClassVariable extends PQLVariable {
    public PQLClassVariable(String name) {
        super(name, TYPE_CLASS);
    }
}