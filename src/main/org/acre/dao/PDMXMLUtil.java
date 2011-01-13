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
package org.acre.dao;

import org.acre.pdm.PDMType;

/**
 * @author Deepak.Alur@Sun.com
 *         Date: Oct 21, 2004
 *         Time: 1:03:23 PM
 */
public class PDMXMLUtil {

    public static String toString (PDMType pdm) {
        if (pdm==null)
            return "null";

        StringBuffer buf = new StringBuffer();
        buf.append("PDM = { name=");
        buf.append(pdm.getName());
        buf.append(", type=");
        buf.append(pdm.getType());
        buf.append(", desc=");
        buf.append(pdm.getDescription());
        buf.append(", scriptedPDM=");
        buf.append(pdm.getScriptedPDMPath());
        buf.append(", tier=");
        buf.append(pdm.getTier());
        buf.append(",");
        buf.append(pdm.getCategory());
        buf.append(", roles=(");

        if (pdm.getRoles() != null)
            buf.append(pdm.getRoles().getRole());

        buf.append("), relationships=(");

        if (pdm.getRelationships() != null)
            buf.append(pdm.getRelationships().getRelationship());

        buf.append(")}");

        return buf.toString();
    }
}
