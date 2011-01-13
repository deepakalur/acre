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
package org.acre.visualizer.graphimpl.jgraph;

import org.acre.visualizer.graphimpl.jgraph.pdmmodel.*;
import org.acre.visualizer.graphimpl.jgraph.pdmresult.*;
import org.acre.visualizer.graph.graph.GraphObjectFactory;
import org.acre.visualizer.graph.graph.AcreGraphException;
import org.acre.visualizer.graph.graph.edges.AcreEdge;
import org.acre.visualizer.graph.graph.edges.AcreEdgeData;
import org.acre.visualizer.graph.graph.pdmmodel.edges.PDMToRoleEdge;
import org.acre.visualizer.graph.graph.pdmmodel.edges.RoleToPDMEdge;
import org.acre.visualizer.graph.graph.pdmmodel.edges.RoleToRoleEdge;
import org.acre.visualizer.graph.graph.pdmmodel.vertex.PDMVertex;
import org.acre.visualizer.graph.graph.pdmmodel.vertex.RoleVertex;
import org.acre.visualizer.graph.graph.pdmresult.QueryResultVertex;
import org.acre.visualizer.graph.graph.pdmresult.edges.ArtifactEdge;
import org.acre.visualizer.graph.graph.pdmresult.edges.PDMResultToRoleResultEdge;
import org.acre.visualizer.graph.graph.pdmresult.edges.RoleResultToRoleResultEdge;
import org.acre.visualizer.graph.graph.pdmresult.vertex.ArtifactVertex;
import org.acre.visualizer.graph.graph.pdmresult.vertex.PDMResultVertex;
import org.acre.visualizer.graph.graph.pdmresult.vertex.RoleResultVertex;
import org.acre.visualizer.graph.graph.vertex.AcreVertex;
import org.acre.pdm.PDMType;
import org.acre.pdm.RelationshipType;
import org.acre.pdm.RoleType;
import org.acre.pdmengine.model.Artifact;
import org.acre.pdmengine.model.PatternResult;
import org.acre.pdmengine.model.QueryResult;
import org.acre.pdmengine.model.RoleResult;
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.DefaultPort;

/**
 * @author Deepak.Alur@Sun.com
 * @version Dec 20, 2004 3:23:52 PM
 */
public class JGraphObjectFactoryImpl extends GraphObjectFactory {

    public RoleResultToRoleResultEdge createRoleResultToRoleResultEdge(String name,
                                               RelationshipType relationship,
                                               AcreVertex parentVertex,
                                               AcreVertex fromVertex,
                                               AcreVertex toVertex) {

        RoleResultToRoleResultEdge edge = new RoleResultToRoleResultEdgeImpl(
                relationship.getName(),
                relationship,
                parentVertex,
                fromVertex,
                toVertex);

        connectEdgeVertices(edge, fromVertex, toVertex);

        return edge;
    }

    public ArtifactVertex createArtifactVertex(Artifact artifact) {
        ArtifactVertex v = new ArtifactVertexImpl(artifact);
        return v;
    }

    public ArtifactEdge createArtifactEdge(String edgeName,
                                           Object relationship,
                                           AcreVertex parentVertex,
                                           AcreVertex sourceVertex,
                                           ArtifactVertex artifactVertex) {
        ArtifactEdge edge;
        if ((sourceVertex!= null) && (artifactVertex != null)) {

            edge = new ArtifactEdgeImpl(edgeName, relationship, parentVertex, sourceVertex, artifactVertex);
            connectEdgeVertices(
                edge,
                sourceVertex,
                artifactVertex);

            return edge;
        }

        return null; // todo error - throw exception ?

    }

    public RoleResultVertex createRoleResultVertex(PatternResult pattern, RoleResult result) {
        RoleResultVertex v = new RoleResultVertexImpl(pattern, result);
        return v;
    }

    public PDMResultToRoleResultEdge createPDMResultToRolePDMResultRelationship(PDMResultVertex pdmResultVertex, PDMResultVertex rolePDMResultVertex) {
        PDMResultToRoleResultEdge edge = new PDMResultToRoleResultEdgeImpl(pdmResultVertex, rolePDMResultVertex);
        return edge;
    }

    public PDMResultVertex createPDMResultVertex(PatternResult pattern) {
        PDMResultVertex resultVertex = new PDMResultVertexImpl(pattern);

        return resultVertex;
    }

    // MODEL objects
    public PDMToRoleEdge createPDMToRoleEdge(PDMVertex pdmVertex, RoleVertex roleVertex) {
        PDMToRoleEdge edge = new PDMToRoleEdgeImpl(pdmVertex, roleVertex);
        return edge;
    }

    public RoleToRoleEdge createRoleToRoleEdge(String name, RelationshipType rel, PDMVertex parentPDMVertex,
                                               RoleVertex from, RoleVertex to) {
        RoleToRoleEdge edge = new RoleToRoleEdgeImpl(parentPDMVertex, name, rel, from, to);
        return edge;
    }

    public RoleToPDMEdge createRoleToPDMEdge(PDMVertex parentPDMVertex, RoleVertex v, PDMVertex rolePDMVertex) {
        RoleToPDMEdge edge = new RoleToPDMEdgeImpl(parentPDMVertex, v, rolePDMVertex);
        return edge;
    }

    public PDMVertex createPDMVertex(PDMType pdm) {
        PDMVertex v = new PDMVertexImpl(pdm);
        return v;
    }

    public RoleVertex createRoleVertex(PDMType pdm, RoleType role) {
        RoleVertex v = new RoleVertexImpl(pdm, role);
        return v;
    }

    public PDMResultToRoleResultEdge createPDMResultToRoleResultRelationship(PDMResultVertex pdmResultVertex,
                                                                             RoleResultVertex roleResultVertex) {
        PDMResultToRoleResultEdge edge = new PDMResultToRoleResultEdgeImpl(pdmResultVertex, roleResultVertex);
        return edge;
    }

    public QueryResultVertex createQueryResultVertex(PatternResult patternResult, QueryResult queryResult) {
        QueryResultVertex v = new QueryResultVertexImpl(patternResult, queryResult);
        return v;
    }

    public PDMResultToRoleResultEdge createPDMToQueryRelationship(PDMResultVertex pdmResultVertex,
                                                                  QueryResultVertex queryResultVertex) {
        PDMResultToRoleResultEdge edge = new PDMResultToRoleResultEdgeImpl(pdmResultVertex, queryResultVertex);
        return edge;
    }

    public void connectEdgeVertices(AcreEdge edge,
                             AcreVertex sourceVertex,
                             AcreVertex targetVertex) {

        if ((sourceVertex == null) || (!(sourceVertex instanceof AcreVertexImpl)))
            throw new AcreGraphException("connectEdge - 'sourceVertex' object is not a Cell : " + sourceVertex);

        if ((targetVertex == null) || (!(targetVertex instanceof AcreVertexImpl)))
                throw new AcreGraphException("connectEdge - 'targetVertex' object is not a Cell : " + targetVertex);

        if ((edge == null) || (!(edge instanceof AcreEdgeImpl)))
                throw new AcreGraphException("connectEdge - 'edge' object is not a Cell : " + edge);

        String connName = ((AcreEdgeData)((AcreEdgeImpl)edge).getSalsaData()).getEdgeName();
        DefaultPort source = new DefaultPort(connName + "StartPort");
        source.addEdge(edge);
        ((AcreVertexImpl)sourceVertex).add(source);

        DefaultPort target = new DefaultPort(connName + "EndPort");
        target.addEdge(edge);
        ((AcreVertexImpl)targetVertex).add(target);

        ConnectionSet cs = new ConnectionSet(edge, source, target);

        ((AcreEdgeImpl)edge).setConnectionSet(cs);

    }

}