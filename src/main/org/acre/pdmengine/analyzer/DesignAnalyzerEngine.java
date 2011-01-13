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

import org.acre.pdmengine.model.*;
import org.acre.pdmengine.model.visitor.ResultVisitor;

/**
 * @author rajmohan@Sun.com
 */
public class DesignAnalyzerEngine {

    public ArtifactLinksTreeNode findTransitiveLinks(PatternResult pattern, String relationshipType, Artifact artifact) {

        ArtifactLink startLink;

        FindArtifactVisitor visitor = new FindArtifactVisitor(relationshipType, artifact);
        pattern.accept(visitor);
        startLink = visitor.getArtifactLink();

        if ( startLink == null )
            return null;

        ArtifactLinksTreeNode startNode = new ArtifactLinksTreeNode(startLink);

        Artifact[] relatedArtifacts = startLink.getTargetArtifacts();
        for ( int i=0;   i < relatedArtifacts.length; i++ ) {
            ArtifactLinksTreeNode connectedTreeNode =
                    findTransitiveLinks(pattern, relationshipType, relatedArtifacts[i]);
            if ( connectedTreeNode != null ) {
                startNode.addLinkNode(connectedTreeNode);
            }
        }
        return startNode;
    }

}

class FindArtifactVisitor extends ResultVisitor {

    private String relationshipType;
    private Artifact artifact;

    public ArtifactLink getArtifactLink() {
        return artifactLink;
    }

    private ArtifactLink artifactLink;
    public FindArtifactVisitor(String relationshipType, Artifact artifact) {
        this.relationshipType = relationshipType;
        this.artifact = artifact;
    }

    public void visitPatternResult(PatternResult patternResult) {
    }

    public void visitPatternRoleResult(RoleResult roleResult) {
    }

    public void visitRelationshipResult(RelationshipResult relationshipResult) {
        if ( relationshipResult.getRelationship().getType().
                equalsIgnoreCase(relationshipType) ) {
            artifactLink = relationshipResult.getLink(artifact);
            if ( artifactLink != null )
                setVisitCompleted(true);
        }
    }

    public void visitQueryResult(QueryResult queryResult) {
    }

    public void visitPatternComplete(PatternResult patternResult) {
    }
}

