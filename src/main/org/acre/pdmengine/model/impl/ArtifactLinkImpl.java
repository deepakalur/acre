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
import org.acre.pdmengine.model.ArtifactLink;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rajmohan@Sun.com
 * @version Dec 13, 2004 4:41:42 PM
 */
public class ArtifactLinkImpl implements ArtifactLink, java.io.Serializable {

    static final long serialVersionUID = -6855834836443394851L;
    
    public Artifact getSourceArtifact() {
        return source;
    }

    public void setSourceArtifact(Artifact source) {
        this.source = source;
    }

    public Artifact[] getTargetArtifacts() {
        Artifact[] result = new Artifact[targets.size()];
        targets.toArray(result);
        return result;
    }

    public void addTargetArtifact(Artifact target) {
        targets.add(target);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(100);
        sb.append("ArtifactLinks : sourceArtifact = ").append(source).
            append(" \b TargetArtifacts = ").append(targets).append("\n");
        return sb.toString();
    }
    private Artifact source;
    private List targets = new ArrayList();
}
