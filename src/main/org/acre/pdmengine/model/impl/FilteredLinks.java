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
import org.acre.pdmengine.model.Artifact;
import org.acre.pdmengine.pqe.ArtifactAdapterFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author rajmohan@Sun.com
 */
public class FilteredLinks implements Serializable {

    public FilteredLinks(PQLResultSet rs) {
        links = ArtifactAdapterFactory.transform(rs);
    }

    // copy constructor
    public FilteredLinks(org.acre.pdmengine.model.impl.FilteredLinks f) {
        this.links = (Artifact[][])f.links.clone();
/**
        PQLArtifact [][] srcLinks = srcObj.links;
        links = new PQLArtifact[srcLinks.length][2];
        for ( int iRow=0; iRow < srcLinks.length; iRow++ ) {
                links[iRow] = (PQLArtifact[])srcLinks[iRow].clone();
        }
 **/

    }

    public void setLinks(Artifact[][] links) {
        this.links = links;
    }

    public Artifact[][] getLinks() {
        return links;
    }

    public Artifact[] getSources() {
        Artifact[] artifacts = new Artifact[links.length];
        for ( int i=0; i < links.length; i++) {
            artifacts[i] = links[i][0];
        }
        return artifacts;
    }

    public Artifact[] getTargets() {
        Artifact[] artifacts = new Artifact[links.length];
        for ( int i=0; i < links.length; i++) {
            artifacts[i] = links[i][1];
        }
        return artifacts;
    }

    public void intersectSources(Artifact[] artifacts) {

        Artifact[][] filteredLinks = intersectArtifacts(artifacts, 0);

        setLinks(filteredLinks);
    }

    public void intersectTargets(Artifact[] artifacts) {

        Artifact[][] filteredLinks = intersectArtifacts(artifacts, 1);

        setLinks(filteredLinks);
    }

    private Artifact[][] intersectArtifacts(Artifact[] artifacts, int col) {
        List filteredLinksList = new ArrayList();

        for ( int i=0; i < links.length; i++) {
            Artifact[] link = links[i];

            Artifact source;

            source = link[col];
            for ( int j=0; j < artifacts.length; j++) {

                Artifact artifact;
                artifact = artifacts[j];

                if ( artifact.equals(source) ) {
                    filteredLinksList.add(link);
                    break;
                }
            }
        }

        Artifact[][] filteredLinks = new Artifact[filteredLinksList.size()][2];
        filteredLinksList.toArray(filteredLinks);
        return filteredLinks;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(200);
        sb.append("Filtered Links { \n");
        for ( int i=0; i < links.length; i++) {
            Artifact link[] = links[i];
            sb.append("( " + link[0]);
            sb.append(" , " + link[1]);
            sb.append(" ) \n");
        }
        sb.append("}\n");
        return sb.toString();
    }

    private Artifact [][] links;
}

