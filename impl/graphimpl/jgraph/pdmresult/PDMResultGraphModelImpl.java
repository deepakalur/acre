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
package org.acre.visualizer.graphimpl.jgraph.pdmresult;

import org.acre.visualizer.graphimpl.jgraph.AcreGraphModelImpl;
import org.acre.visualizer.graph.graph.pdmresult.PDMResultGraphModel;
import org.acre.visualizer.graph.graph.pdmresult.QueryResultVertex;
import org.acre.visualizer.graph.graph.pdmresult.edges.ArtifactEdge;
import org.acre.visualizer.graph.graph.pdmresult.edges.PDMResultToRoleResultEdge;
import org.acre.visualizer.graph.graph.pdmresult.edges.RoleResultToRoleResultEdge;
import org.acre.visualizer.graph.graph.pdmresult.vertex.ArtifactVertex;
import org.acre.visualizer.graph.graph.pdmresult.vertex.PDMResultVertex;
import org.acre.visualizer.graph.graph.pdmresult.vertex.RoleResultVertex;

/**
 * @author Deepak.Alur@Sun.com
 * @version Dec 16, 2004 8:05:33 PM
 */
public class PDMResultGraphModelImpl extends AcreGraphModelImpl
        implements PDMResultGraphModel
 {

    public PDMResultGraphModelImpl() {
        super();
    }

    public void insertPDMVertex(PDMResultVertex pdmResultVertex) {
        insertSalsaVertex(pdmResultVertex);
    }

    public void insertPDMRoleEdge(PDMResultToRoleResultEdge edge) {
        insertSalsaEdge(edge);
    }

    public void insertRoleVertex(RoleResultVertex resultVertex) {
        insertSalsaVertex(resultVertex);
    }

    public void insertArtifactVertices(ArtifactVertex[] artifactVertices) {
        for (int i=0; i < artifactVertices.length; i++) {
            insertArtifactVertex(artifactVertices[i]);
        }
    }

    public void insertArtifactVertex(ArtifactVertex artifactVertex) {
        insertSalsaVertex(artifactVertex);
    }

    public void insertQueryResultVertex(QueryResultVertex queryResultVertex) {
        insertSalsaVertex(queryResultVertex);
    }


    public void insertRoleToRoleEdge(RoleResultToRoleResultEdge edge) {
        insertSalsaEdge(edge);
    }

    public void insertArtifactEdges(ArtifactEdge[] artifactEdges) {
        for (int i = 0; i < artifactEdges.length; i++) {
            insertArtifactEdge(artifactEdges[i]);
        }
    }

    public void insertArtifactEdge(ArtifactEdge artifactEdge) {
        insertSalsaEdge(artifactEdge);
    }

}