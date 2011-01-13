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
package org.acre.pdmengine.model.impl;

import org.acre.lang.pql.pdbc.PQLArtifact;
import org.acre.lang.pql.pdbc.PQLConnection;
import org.acre.lang.pql.pdbc.PQLException;
import org.acre.lang.pql.pdbc.PQLValueHolder;
import org.acre.pdmengine.PatternEngineException;
import org.acre.pdmengine.model.Artifact;
import org.acre.pdmengine.model.RoleResult;

/**
 * @author rajmohan@Sun.com
 * @version Dec 13, 2004 2:47:22 PM
 */
public class ArtifactImpl implements Artifact, java.io.Serializable {

    static final long serialVersionUID = 1191875360667897701L;
    
    private PQLArtifact pqlArtifact;

    private RoleResult parent;

    public ArtifactImpl(PQLArtifact pqlArtifact, RoleResult parent) {
        this.pqlArtifact = pqlArtifact;
        this.parent = parent;
    }

    public boolean equals(Object obj) {
        if ( obj == null )
            return false;

        if ( obj == this)
            return true;

        if ( obj instanceof Artifact) {
            ArtifactImpl that = (ArtifactImpl)obj;
            return pqlArtifact.equals(that.pqlArtifact);
        }
        return false;
    }

    public int hashCode() {
        return pqlArtifact.hashCode();
    }

    public String toString() {
        return pqlArtifact.toString();
    }

    public boolean isPrimitive() {
        return pqlArtifact.isPrimitive();
    }

    public String getType() {
        return pqlArtifact.getType();
    }

    public Object getValue() {
        return pqlArtifact.getValue();
    }

    public String getName() {
        Object valueObj = getValue();
        if ( valueObj instanceof PQLValueHolder) {
            PQLValueHolder value = (PQLValueHolder) valueObj;
            return value.getName();
        }
        return "";
    }

    public String getAttribute(PQLConnection pqlConnection, String attrName) {
        Object value = getValue();
        if ( value instanceof PQLValueHolder ) {
            PQLValueHolder valueHolder = (PQLValueHolder)value;
            // first try if the value already exists
            String attrValue = null;
            try {
                attrValue = valueHolder.getAttribute(attrName);
            } catch (IllegalArgumentException e) {
                // value does not exist
            }

            if (attrValue != null)
                return attrValue;

            // value not found, fetch from PQL Connection
            try {
                pqlConnection.fetchComplete(valueHolder);
            } catch (PQLException e) {
                throw new PatternEngineException("Internal Error - Fetching artifact attribute : "
                    + e.getMessage(), e);
            }
            return valueHolder.getAttribute(attrName);
        }
        return null;
    }

    public RoleResult getParent() {
        return parent;
    }
}
