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

import org.acre.pdmengine.model.Artifact;
import org.acre.pdmengine.model.impl.ArtifactLinkImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * Class responsible for building LinkMap from arrays.
 */
public class LinkMapBuilder {

    public static BidiLinksMap buildLinkMap(Artifact links[][]) {
        Map src2TargetLinksMap = new HashMap(1009);
        Map target2srcLinksMap = new HashMap(1009);

        for ( int iRow=0; iRow < links.length; iRow++ ) {
                Artifact cols[];
                cols = links[iRow];
                Artifact sourceArtifact, targetArtifact;
                sourceArtifact = cols[0];
                targetArtifact = cols[1];

                // build link map (source->targets)
                ArtifactLinkImpl link = (ArtifactLinkImpl)src2TargetLinksMap.get(sourceArtifact.getName());
                if ( link == null ) {
                    link = new ArtifactLinkImpl();
                    link.setSourceArtifact(sourceArtifact);
                    src2TargetLinksMap.put(sourceArtifact.getName(), link);
                }
                link.addTargetArtifact(targetArtifact);

                // build reverse link map(target->sources) - faster performance with some memory trade-off
                ArtifactLinkImpl reverseLinks = (ArtifactLinkImpl)target2srcLinksMap.get(targetArtifact.getName());
                if ( reverseLinks == null ) {
                    reverseLinks = new ArtifactLinkImpl();
                    reverseLinks.setSourceArtifact(targetArtifact);
                    target2srcLinksMap.put(targetArtifact.getName(), reverseLinks);
                }
                reverseLinks.addTargetArtifact(sourceArtifact);
        }

        BidiLinksMap bidiMap = new BidiLinksMap(src2TargetLinksMap, target2srcLinksMap);
        return bidiMap;
    }

}
