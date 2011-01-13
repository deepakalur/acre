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

import java.util.Map;

/**
 * Bi-directional Links Map
 */
public class BidiLinksMap {
    private Map src2TargetLinksMap;
    private Map target2srcLinksMap;

    public BidiLinksMap(Map src2TargetLinksMap, Map target2srcLinksMap) {
        this.src2TargetLinksMap = src2TargetLinksMap;
        this.target2srcLinksMap = target2srcLinksMap;
    }

    public Map getLinksMap () {
        return src2TargetLinksMap;
    }

    public Map getReverseLinksMap() {
        return target2srcLinksMap;
    }

    public String toString() {
        return "(Bi-directional Links Map) {" + "\n" +
                "Forward Links : " + "\n" +
                src2TargetLinksMap + "\n" +
                "Reverse Links " + "\n" +
                target2srcLinksMap + "\n" +
                "}";

    }

}
