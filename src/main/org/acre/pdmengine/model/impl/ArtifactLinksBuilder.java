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

import org.acre.lang.pql.pdbc.PQLResultSet;
import org.acre.pdmengine.PatternEngineException;
import org.acre.pdmengine.model.Artifact;
import org.acre.pdmengine.pqe.ArtifactAdapterFactory;
import org.acre.pdmengine.util.BidiLinksMap;
import org.acre.pdmengine.util.LinkMapBuilder;

import java.util.Map;

/**
 * User: rajmohan@sun.com
 * Date: Nov 5, 2004
 * Time: 4:59:46 PM
 */
public class ArtifactLinksBuilder {

    public static BidiLinksMap buildLinksMap(Map roleResults) {
        if ( roleResults.size() != 1 ) {
            throw new PatternEngineException("Internal Error : Relationships have invalid # of roles" +
                    roleResults );
        }

        PQLResultSet rs = (PQLResultSet)roleResults.values().iterator().next();
        Artifact[][] artifacts =  ArtifactAdapterFactory.transform(rs);
        LinkMapBuilder linkMapBuilder = new LinkMapBuilder();
        return linkMapBuilder.buildLinkMap(artifacts);
    }



    /**
    private RoleResultImpl fromRole, toRole;

    public ArtifactLinksBuilder(Map roleResults) {
        this(roleResults, null, null);    
    }

    public ArtifactLinksBuilder(Map roleResults, RoleResultImpl fromRole, RoleResultImpl toRole) {
        if ( roleResults.size() != 1 ) {
            throw new PatternEngineException("Internal Error : Relationships have invalid # of roles" +
                    roleResults );
        }
        this.fromRole = fromRole;
        this.toRole = toRole;

        PQLResultSet rs = (PQLResultSet)roleResults.values().iterator().next();
        int nRows;
        nRows = rs.getMetaData().getRowCount();
        PQLArtifact [][]links = new PQLArtifact[nRows][2];

        for ( int iRow=0; iRow < nRows; iRow++ ) {
                PQLArtifact cols[];
                cols = rs.getRow(iRow);
                links[iRow] = cols;
        }

        extractLinksFromResultSet(links);

    }

    public ArtifactLinksBuilder(PQLArtifact [][]links) {
        extractLinksFromResultSet(links);
    }

    private void extractLinksFromResultSet(PQLArtifact links[][]) {
        src2TargetLinksMap = new HashMap(1009);
        target2srcLinksMap = new HashMap(1009);

        // Map PQLArtifacts to build Artifact links
        for ( int iRow=0; iRow < links.length; iRow++ ) {
                PQLArtifact cols[];
                cols = links[iRow];
                Artifact sourceArtifact;
                if ( fromRole != null )
                    sourceArtifact = fromRole.findArtifact(cols[0]);
                else
                    // orphan artifact, hence Null parent
                    sourceArtifact = new ArtifactImpl(cols[0], null);

                Artifact targetArtifact;
                if ( toRole != null )
                     targetArtifact = toRole.findArtifact(cols[1]);
                else
                     targetArtifact = new ArtifactImpl(cols[1], null);

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
    }

    private Map src2TargetLinksMap;
    private Map target2srcLinksMap;

    public Map getLinksMap () {
        return src2TargetLinksMap;
    }

    public Map getReverseLinksMap() {
        return target2srcLinksMap;
    }

    public String toString() {
        return "(LinksManager) {" + "\n" +
                "Forward Links : " + "\n" +
                src2TargetLinksMap + "\n" +
                "Reverse Links " + "\n" +
                target2srcLinksMap + "\n" +
                "}";

    }
 **/
}
