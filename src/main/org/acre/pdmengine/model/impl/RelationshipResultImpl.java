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

import org.acre.pdm.RelationshipType;
import org.acre.lang.pql.pdbc.PQLResultSet;
import org.acre.pdmengine.model.*;
import org.acre.pdmengine.model.visitor.ResultVisitor;
import org.acre.pdmengine.util.BidiLinksMap;
import org.acre.pdmengine.util.LinkMapBuilder;

import java.util.Map;

public class RelationshipResultImpl extends ResultImpl implements RelationshipResult, java.io.Serializable {

    private PatternResult parent;
    private RelationshipType relationship;

    private RoleResultImpl fromRole; // points to the role from which this relationship originates
    private RoleResultImpl toRole; // points to the role to which this relationship terminates

    // private Map<String artifactName, ArtifactLink>
    private Map linksMap;
    private Map reverseLinksMap;


    // filtered links
    private FilteredLinks filteredLinks;

    private PQLResultSet rs;

    static final long serialVersionUID = -7965575669337511463L;

    public RelationshipResultImpl(RelationshipResultImpl clone, PatternResult parent) {
        this(clone.getRelationship(), parent);

        if ( clone.rs != null )
            filteredLinks = new FilteredLinks(clone.rs);
        else
            filteredLinks = new FilteredLinks(clone.filteredLinks);

        RoleResult fromRole, toRole;

        fromRole = parent.getRoleReference(clone.getRelationship().getFromRole());
        setFromRole(fromRole);

        toRole = parent.getRoleReference(clone.getRelationship().getToRole());
        setToRole(toRole);

        linksMap = clone.linksMap;
        reverseLinksMap = clone.reverseLinksMap;
    }

    public void setResult(Map result) {
        BidiLinksMap bidiMap = ArtifactLinksBuilder.buildLinksMap(result);
        linksMap = bidiMap.getLinksMap();
        reverseLinksMap = bidiMap.getReverseLinksMap();

        rs = (PQLResultSet)result.values().iterator().next();
        filteredLinks = new FilteredLinks(rs);
    }


    public RelationshipResultImpl(RelationshipType rel, PatternResult parent) {
        super();
        relationship = rel;
        this.parent = parent;
    }

    public RelationshipType getRelationship() {
        return relationship;
    }

    public String getName() {
        return getRelationship().getName();
    }

    public void setRelationship(RelationshipType relationship) {
        this.relationship = relationship;
    }

    public RoleResult getFromRole() {
        return fromRole;
    }

    public void setFromRole(RoleResult fromRole) {
        this.fromRole = (RoleResultImpl)fromRole;
    }

    public RoleResult getToRole() {
        return toRole;
    }

    public void setToRole(RoleResult toRole) {
        this.toRole = (RoleResultImpl)toRole;
    }

    public ArtifactLink [] getLinks() {
        ArtifactLink[] links = new ArtifactLink[linksMap.size()];
        linksMap.values().toArray(links);
        return links;
    }

    public ArtifactLink getLink(Artifact artifact) {
        return (ArtifactLink)linksMap.get(artifact.getName());
    }

    public boolean isEmpty() {
        return (getLinks().length == 0);
    }

    public Artifact[] getLinkedArtifacts(String artifactName) {
        ArtifactLink artifactLink =  (ArtifactLink)linksMap.get(artifactName);
        if ( artifactLink != null )
            return artifactLink.getTargetArtifacts();
        return new Artifact[0];
    }

    public Artifact[] getLinkedArtifacts(Artifact artifact) {
        return getLinkedArtifacts(artifact.getName());
    }

    public PatternResult getParent() {
        return parent;
    }

    public void accept(ResultVisitor visitor) {
        visitor.visitRelationshipResult(this);
    }

    public int getHitCount() {
        int linksCount=0;
        ArtifactLink links [] = getLinks();
        for ( int idx=0; idx < links.length; idx++ ) {
            linksCount += links[idx].getTargetArtifacts().length;
        }
        return linksCount;
    }

    public PatternResult getPattern() {
        return getParent();
    }

    FilteredLinks getFilteredLinks() {
        return filteredLinks;
    }

    public String toString() {
        return "(RelationshipModel) {" +
                "relationship=" +
                PDMModelUtil.relationshipTypeToString(relationship) +  "\n" +
//                " linkArtifacts = " + linksMap +
                " filteredLinks = " + filteredLinks +
                "}\n";
    }

    private int joinType = JOIN_TYPE_ALL;
    public static final int JOIN_TYPE_ANY = 1;
    public static final int JOIN_TYPE_ALL = 2;

    public void setJoinType(int joinType) {
        this.joinType = joinType;
    }

    public int getJoinType() {
        return joinType;
    }

    public void joinComplete() {
        BidiLinksMap bidiMap = LinkMapBuilder.buildLinkMap(filteredLinks.getLinks());
        linksMap = bidiMap.getLinksMap();
        reverseLinksMap = bidiMap.getReverseLinksMap();
    }

/** TODO - To be Deleted after July 1st (buffer zone) - Raj
    public ArtifactLink [] findRelcos() {
        computeRelco();
        ArtifactLink result[] = new ArtifactLink[relcoLinksMap.size()];
        relcoLinksMap.values().toArray(result);
        return result;
    }

    public Artifact[] findRelcos(String artifactName) {
        computeRelco();
        ArtifactLink artifactLink =  (ArtifactLink)reverseRelcoLinksMap.get(artifactName);
        if ( artifactLink != null )
            return artifactLink.getTargetArtifacts();
        return new Artifact[0];
    }

    public Artifact[] findRelcos(Artifact artifact) {
        return findRelcos(artifact.getName());
    }

    public ArtifactLink[] findInvRelcos() {
        computeInvRelco();
        ArtifactLink result[] = new ArtifactLink[invRelcoLinksMap.size()];
        invRelcoLinksMap.values().toArray(result);
        return result;
    }

    public Artifact[] findInvRelcos(String artifactName) {
        computeInvRelco();
        ArtifactLink link = (ArtifactLink)invRelcoLinksMap.get(artifactName);
        if ( link != null )
            return link.getTargetArtifacts();
        return new Artifact[0];
    }

    public Artifact[] findInvRelcos(Artifact artifact) {
        return findInvRelcos(artifact.getName());
    }

    private void computeInvRelco() {
        if ( invRelcoLinksMap == null ) {
            InvRelcoCommand invRelcoOp = new InvRelcoCommand();

            String sourceEntity = getFromRole().getVariableName();
            String targetEntity = getToRole().getVariableName();
            String resultEntity = "invrelco_" + getName();
            Map invRelco = invRelcoOp.execute(pqlConnection, resultEntity, sourceEntity, targetEntity,
                        getRelationship().getType());

            ArtifactLinksBuilder artifactLinksBuilder = new ArtifactLinksBuilder(invRelco);
            invRelcoLinksMap = artifactLinksBuilder.getLinksMap();
        }
    }


    private void computeRelco() {
        if ( relcoLinksMap == null ) {
            RelcoCommand relcoOp = new RelcoCommand();

            String sourceEntity = getFromRole().getVariableName();
            String targetEntity = getToRole().getVariableName();
            String resultEntity = "relco_" + getName();
            Map relcos = relcoOp.execute(pqlConnection, resultEntity , sourceEntity, targetEntity,
                        getRelationship().getType());

            ArtifactLinksBuilder artifactLinksBuilder = new ArtifactLinksBuilder(relcos);
            relcoLinksMap = artifactLinksBuilder.getLinksMap();
            reverseRelcoLinksMap = artifactLinksBuilder.getReverseLinksMap();
        }
    }
 **/

}
