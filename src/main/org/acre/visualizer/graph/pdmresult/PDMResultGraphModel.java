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
package org.acre.visualizer.graph.pdmresult;

import org.acre.visualizer.graph.AcreGraphModel;
import org.acre.visualizer.graph.pdmresult.edges.ArtifactEdge;
import org.acre.visualizer.graph.pdmresult.edges.PDMResultToRoleResultEdge;
import org.acre.visualizer.graph.pdmresult.edges.RoleResultToRoleResultEdge;
import org.acre.visualizer.graph.pdmresult.vertex.ArtifactVertex;
import org.acre.visualizer.graph.pdmresult.vertex.PDMResultVertex;
import org.acre.visualizer.graph.pdmresult.vertex.RoleResultVertex;

/**
 * @author Deepak.Alur@Sun.com
 * @version Dec 22, 2004 12:18:22 AM
 */
public interface PDMResultGraphModel extends AcreGraphModel {
    void insertPDMVertex(PDMResultVertex pdmResultVertex);
    void insertPDMRoleEdge(PDMResultToRoleResultEdge edge);
    void insertRoleVertex(RoleResultVertex resultVertex);
    void insertArtifactEdges(ArtifactEdge[] artifactEdges);
    void insertArtifactEdge(ArtifactEdge artifactEdge);
    void insertRoleToRoleEdge(RoleResultToRoleResultEdge edge);
    void insertArtifactVertices(ArtifactVertex[] artifactVertices);
    void insertArtifactVertex(ArtifactVertex artifactVertex);
    void insertQueryResultVertex(QueryResultVertex queryResultVertex);
}
