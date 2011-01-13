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
package org.acre.pdmengine.model;

import org.acre.lang.pql.pdbc.PQLConnection;

/**
 * @author rajmohan@Sun.com
 * @version Dec 13, 2004 5:04:17 PM
 */
public interface Artifact {
    boolean equals(Object obj);

    int hashCode();

    String toString();

    boolean isPrimitive();

    String getType();

    Object getValue();

    String getName();

    String getAttribute(PQLConnection pqlConnection, String attrName);

    RoleResult getParent();
}
