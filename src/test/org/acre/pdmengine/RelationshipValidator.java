package org.acre.pdmengine;

import junit.framework.Assert;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

import org.acre.pdmengine.model.RelationshipResult;
import org.acre.pdmengine.model.Artifact;
import org.acre.pdmengine.model.ArtifactLink;

/**
 *
 * Copyright (c) 2004
 * Sun Microsystems, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Sun
 * Microsystems, Inc. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Sun.
 *
 * SUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. SUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 */

/**
 * @author rajmohan@Sun.com
 * @version Nov 29, 2004 10:17:15 AM
 */

public class RelationshipValidator extends Assert {
    Set linkRefSet;
    String [][] links;

    RelationshipValidator(String [][] links) {
        this.links = links;
        linkRefSet = new HashSet();
        for ( int row=0; row<links.length; row++) {
                 linkRefSet.add(new LinkRef(links[row][0], links[row][1]));
        }
    }

    Set getLinks() {
        return linkRefSet;
    }

    List getLinkedTargets(Artifact artifact) {
        String name = artifact.getName();
        return getLinkedTargets(name);
    }

    List getLinkedTargets(String name) {
        List linkedTargets = new ArrayList();

        for ( int row=0; row < links.length; row++ ) {
            if (name.equalsIgnoreCase(links[row][0])) {
                linkedTargets.add(links[row][1]);
            }
        }

        return linkedTargets;
    }

    List getLinkedSources(Artifact artifact) {
        String name = artifact.getName();
        return getLinkedSources(name);
    }

    List getLinkedSources(String name) {
        List linkedSources = new ArrayList();

        for ( int row=0; row < links.length; row++ ) {
            if (name.equalsIgnoreCase(links[row][1])) {
                linkedSources.add(links[row][0]);
            }
        }

        return linkedSources;
    }


    void validateRelationship(RelationshipResult relationship) {

        ArtifactLink link[] = relationship.getLinks();

        validateLinks(link);

        for ( int iLink=0; iLink < link.length; iLink++) {
            Artifact artifact = link[iLink].getSourceArtifact();
            Artifact [] flinks = relationship.getLink(artifact).getTargetArtifacts();
            validateForwardLinks(artifact, flinks);
        }
/**
        for ( int iLink=0; iLink < links.length; iLink++) {
            PQLArtifact artifact = links[iLink].getTargetArtifact();
            Object [] flinks = relationship.getLinkedSourceArtifacts(artifact);
            validateReverseLinks(artifact, flinks);
        }
*/
    }

    protected void validateLinks(ArtifactLink[] links) {
        Set referenceLinks = getLinks();

        int cLinks = 0;
        for ( int linkIdx = 0; linkIdx < links.length; linkIdx++ ) {
            ArtifactLink testLink = links[linkIdx];

            String sourceName;
            sourceName = testLink.getSourceArtifact().getName();
            Artifact[] targetArtifacts = testLink.getTargetArtifacts();

            for ( int iTarget=0; iTarget < targetArtifacts.length; iTarget++ ) {
                String targetName = targetArtifacts[iTarget].getName();

                LinkRef linkRef = new LinkRef(sourceName, targetName);

                assertTrue(linkRef + " not found in links test collection", referenceLinks.contains(linkRef));
                cLinks++;
            }
        }

        assertEquals(cLinks, referenceLinks.size());
    }

    public void validateForwardLinks(Artifact artifact, Artifact[] forwardLinks) {
        List fLinksList = getLinkedTargets(artifact);

        compareArtifactNames(fLinksList, forwardLinks);
    }

    private void compareArtifactNames(List fLinksList, Object[] artifactLinks) {
        assertEquals(fLinksList.size(), artifactLinks.length);
        for ( int iLink=0; iLink < artifactLinks.length; iLink++) {
            Artifact artifactLink = (Artifact)artifactLinks[iLink];
            String name = artifactLink.getName();
            assertTrue(fLinksList.contains(name));
        }
    }

    public void validateReverseLinks(Artifact artifact, Object[] reverseLinks) {
        List rLinksList = getLinkedSources(artifact);

        compareArtifactNames(rLinksList, reverseLinks);

    }

    class LinkRef {
        public LinkRef(String fromArtifact, String toArtfiact) {
            this.sourceArtifact = fromArtifact.toLowerCase().trim();
            this.targetArtifact = toArtfiact.toLowerCase().trim();
        }

        public boolean equals(Object obj) {
                if ( obj == this )
                    return true;

                if ( obj instanceof LinkRef) {
                    LinkRef that = (LinkRef)obj;
                    if ( sourceArtifact.equalsIgnoreCase(that.sourceArtifact) &&
                         targetArtifact.equalsIgnoreCase(that.targetArtifact))
                        return true;
                }
                return false;
        }

        public int hashCode() {
            int hashCode = sourceArtifact.hashCode() + targetArtifact.hashCode();
            return hashCode;
        }

        public String toString() {
            return "LinkRef : " + sourceArtifact + " : " + targetArtifact ;
        }

        String sourceArtifact;
        String targetArtifact;
    }
}



