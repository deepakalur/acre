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

import org.acre.lang.pql.pdbc.PQLValueHolder;
import org.acre.pdmengine.model.Artifact;

import java.io.Serializable;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author rajmohan@Sun.com
 */
public class FilteredArtifactsHolder extends AbstractArtifactsHolder implements Serializable {
    private Set artifacts = new HashSet();
    private String type;
    private String variableName;

    public FilteredArtifactsHolder(String variableName, String type, Collection artifacts) {
        this.variableName = variableName;
        this.type = type;
        addArtifacts(artifacts);
    }

    public FilteredArtifactsHolder(String variableName, String type) {
        this(variableName, type, null);
    }

    // copy constructor
    public FilteredArtifactsHolder(FilteredArtifactsHolder f) {
        this(f.variableName, f.type, f.artifacts);
    }

    public void addArtifacts(Collection filteredArtifacts) {
        if ( filteredArtifacts != null ) {
            artifacts.clear();
            artifacts.addAll(filteredArtifacts);
        }
/**
        if ( artifactsColl != null ) {
            Collection scratch = new ArrayList(artifactsColl);
            scratch.removeAll(artifacts);
            artifacts.addAll(scratch);
        }
 */
    }

    public String[] getVariableKeys() {
        return new String[]{variableName};
    }

    public String[] getArtifactNames(Object key) {
        String [] result = null;
        if ( key instanceof String ) {
            if ( variableName.equalsIgnoreCase((String)key)) {
                result = new String[artifacts.size()];
                Iterator artifactsItr = artifacts.iterator();
                int i = 0;
                while ( artifactsItr.hasNext() ) {
                    Artifact artifact = (Artifact)artifactsItr.next();
                    Object value = artifact.getValue();
                    String name;
                    if ( value instanceof PQLValueHolder) {
                        PQLValueHolder pvh = (PQLValueHolder)value;
                        name = pvh.getName();
                    }
                    else
                        name = value.toString();
                    result[i++] = name;
                }


            }
        }
        return result;
    }

    public Artifact[] getArtifacts(int columnNumber) {
        if ( columnNumber > 1)
            return null;

        Artifact [] result = new Artifact[artifacts.size()];
        artifacts.toArray(result);

        return result;
    }

    // It is a linear search - base on profiling & usage, we may re-implement
    // using a indexed search
    public Artifact findArtifact(int columnNumber, Object target, Comparator artifactComparator) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List matchArtifacts(int columnNumber, Pattern pattern) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getType() {
        return type;
    }

    protected boolean isEmpty() {
        return artifacts.isEmpty();
    }

    public String toString() {
        return "FilteredArtifacts{" +
                "result=" + artifacts +
                "}";
    }

}
