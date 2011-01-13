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
package org.acre.lang.pql.pdbc;


/**
 * Holder Object that will represent ResultSet Row Object for holdingPQL queries
 *
 * @author Syed Ali
 */
public class PQLArtifact implements java.io.Serializable {
	
    private boolean primitive;
    private String type;
    private Object value;

    static final long serialVersionUID = 174799518001725893L;
    
    public PQLArtifact(String type, Object value) {
        if (type == null || value == null) {
           throw new IllegalArgumentException("Type or value cannot be null");
        }
        this.type = type;
        this.value = value;
        primitive = true;
    }

    public PQLArtifact(PQLValueHolder data) {
        if (data == null || data.getType() == null) {
            throw new IllegalArgumentException("Type or value cannot be null");
         }
        value = data;
        type = data.getType();
        primitive = false;
    }

    public boolean equals(Object obj) {
        if ( obj == null )
            return false;

        if ( obj == this)
            return true;

        if ( obj instanceof PQLArtifact) {
            PQLArtifact that = (PQLArtifact)obj;
            return  type.equalsIgnoreCase(that.type) &&
                    value.equals(that.getValue());
        }
        return false;
    }
    
    public int hashCode() {
        return value.hashCode();
    }

    public String toString() {
        return value.toString();
    }

    public boolean isPrimitive() {
        return primitive;
    }

    public String getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }
}