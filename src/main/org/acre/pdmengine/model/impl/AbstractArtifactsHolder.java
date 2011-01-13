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

import org.acre.pdmengine.model.Artifact;
import org.acre.pdmengine.model.RoleResult;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author rajmohan@Sun.com
 */
public abstract class AbstractArtifactsHolder {
    protected RoleResult parent;

    public abstract String[] getVariableKeys();

    public abstract String [] getArtifactNames(Object key);

    public abstract Artifact[] getArtifacts(int columnNumber);

    // It is a linear search - base on profiling & usage, we may re-implement
    // using a indexed search
    public abstract Artifact findArtifact(int columnNumber, Object target, Comparator artifactComparator);

    public abstract List matchArtifacts(int columnNumber, Pattern pattern);

    public abstract String getType();

    protected abstract boolean isEmpty();

    public void setParent(RoleResult parent) {
        this.parent = parent;
    }
}
