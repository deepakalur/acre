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
package org.acre.pdmengine.analyzer;

import org.acre.pdmengine.model.ArtifactLink;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rajmohan@Sun.com
 */
public class ArtifactLinksTreeNode {
    private ArtifactLink link;

    public ArtifactLinksTreeNode(ArtifactLink link) {
        this.link = link;
    }

    public ArtifactLink getLink() { return link; }

    // List<ArtifactLinksTreeNode>
    public List getNextLinkNodes() {
            return chains; 
    }

    void addLinkNode(ArtifactLinksTreeNode chainedLink) {
        chains.add(chainedLink);
    }

    List chains = new ArrayList();
}
