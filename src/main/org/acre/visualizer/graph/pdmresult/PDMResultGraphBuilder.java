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

import org.acre.pdm.PDMType;
import org.acre.pdm.Relationship;
import org.acre.pdm.RelationshipType;
import org.acre.pdm.RoleType;
import org.acre.config.ConfigService;
import org.acre.visualizer.graph.GraphObjectFactory;
import org.acre.visualizer.graph.AcreGraphException;
import org.acre.visualizer.graph.pdmresult.edges.ArtifactEdge;
import org.acre.visualizer.graph.pdmresult.edges.PDMResultToRoleResultEdge;
import org.acre.visualizer.graph.pdmresult.edges.RoleResultToRoleResultEdge;
import org.acre.visualizer.graph.pdmresult.vertex.ArtifactVertex;
import org.acre.visualizer.graph.pdmresult.vertex.PDMResultVertex;
import org.acre.visualizer.graph.pdmresult.vertex.RoleResultVertex;
import org.acre.visualizer.graph.vertex.AcreVertex;
import org.acre.visualizer.graph.vertex.AcreVertexData;
import org.acre.pdmengine.model.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Deepak.Alur@Sun.com
 * @version Dec 22, 2004 12:08:15 AM
 */
public class PDMResultGraphBuilder {
    private GraphObjectFactory factory = null;
    private ArrayList artifactVertices = new ArrayList();
    private ArrayList artifactEdges = new ArrayList();
    private ArrayList roleVertices = new ArrayList();
    private ArrayList roleToRoleEdges = new ArrayList();
    private ArrayList pdmRoleEdges = new ArrayList();
    private ArrayList pdmVertices = new ArrayList();
    private PDMResultGraphModel model;
    int graphType;
    private Logger logger;
    private ArrayList queryResultVertices = new ArrayList();

    public PDMResultGraphBuilder(int graphType) {
        super();
        logger = ConfigService.getInstance().getLogger(this);
        this.graphType = graphType;
        factory = GraphObjectFactory.getFactory(graphType);
    }

    public PDMResultGraphModel getModel() {
        return model;
    }

    public void setModel(PDMResultGraphModel model) {
        this.model = model;
    }

    public GraphObjectFactory getFactory() {
        return factory;
    }

    public void setFactory(GraphObjectFactory factory) {
        this.factory = factory;
    }

    public ArtifactVertex [] getArtifactVertices() {
        ArtifactVertex [] v = new ArtifactVertex [artifactVertices.size()];
        artifactVertices.toArray(v);
        return v;
    }

    public ArtifactEdge [] getArtifactEdges() {
        ArtifactEdge [] e = new ArtifactEdge[artifactEdges.size()];
        artifactEdges.toArray(e);
        return e;
    }

    public RoleResultVertex [] getRoleVertices() {
        RoleResultVertex [] r = new RoleResultVertex [roleVertices.size()];
        roleVertices.toArray(r);
        return r;
    }

    public RoleResultToRoleResultEdge [] getRoleToRoleEdges() {
        RoleResultToRoleResultEdge [] r = new RoleResultToRoleResultEdge [roleToRoleEdges.size()];
        roleToRoleEdges.toArray(r);
        return r;
    }

    public PDMResultToRoleResultEdge [] getPdmRoleEdges() {
        PDMResultToRoleResultEdge [] e = new PDMResultToRoleResultEdge [pdmRoleEdges.size()];
        pdmRoleEdges.toArray(e);
        return e;
    }

    public PDMResultVertex [] getPdmVertices() {
        PDMResultVertex v[] = new PDMResultVertex [pdmVertices.size()];
        pdmVertices.toArray(v);
        return v;
    }

    public PDMResultVertex addPDMResult(PatternResult patternResult) {
        if (model == null)
            throw new AcreGraphException("GraphModelBuilder cannot be null");

        if (factory == null)
            throw new AcreGraphException("GraphObjectFactory cannot be null");

        RoleResult[] roles = null;

        // first add the PDM vertex
        PDMResultVertex pdmResultVertex = createPDMVertex(patternResult);
        logger.info("Created PDM Result Vertex = " + pdmResultVertex);

        // insert PDM into the graph model
        model.insertPDMVertex(pdmResultVertex);
        pdmVertices.add(pdmResultVertex);

        // get the roles from the result
        if (patternResult.getRoles() != null) {
            roles = new RoleResult[patternResult.getRoles().size()];
            patternResult.getRoles().toArray(roles);

            for (int roleNum =0; roleNum < roles.length; roleNum++) {
                RoleResult result = (RoleResult) roles[roleNum];
                // create Role Result Vertex
                RoleResultVertex roleResultVertex = createRoleVertex(
                        patternResult,
                        result);

                model.insertRoleVertex(roleResultVertex);
                roleVertices.add(roleResultVertex);

                logger.info("Created Role Result Vertex = " + roleResultVertex);

                // connect PDM to Role Result vertices
                PDMResultToRoleResultEdge edge =
                        createPDMToRoleRelationship(pdmResultVertex,
                                roleResultVertex);

                model.insertPDMRoleEdge(edge);
                pdmRoleEdges.add(edge);

                logger.info("Connected PDM Result Vertex "
                        + pdmResultVertex
                        + " to Role Result vertex "
                        + roleResultVertex
                        );
            }

            //for each role, drill down
            for (int roleNum = 0; roleNum < roles.length; roleNum++) {
                Result roleResult = roles[roleNum].getRoleResult();

                if (roleResult instanceof PatternResult) {
                    // the role result contains another PDM result
                    PatternResult rolePatternResult = (PatternResult) roleResult;

                    // create / insert PDM Result Vertex
                    PDMResultVertex rolePDMResultVertex =
                            addPDMResult(rolePatternResult);

                    // create PDM to Role PDM connection
                    PDMResultToRoleResultEdge edge =
                            createPDMToRolePDMRelationship(
                            pdmResultVertex,
                            rolePDMResultVertex);

                    model.insertPDMRoleEdge(edge);
                    pdmRoleEdges.add(edge);

                    logger.info("Connected PDM Result "
                            + pdmResultVertex
                            + "to Role PDM Result "
                            + rolePDMResultVertex
                            );

//                } else if (roleResult instanceof RoleResult) {
//                    RoleResult result = (RoleResult) roleResult;
//                    // create Role Result Vertex
//                    RoleResultVertex roleResultVertex = createRoleVertex(
//                            patternResult,
//                            result);
//
//                    model.insertRoleVertex(roleResultVertex);
//                    roleVertices.add(roleResultVertex);
//
//                    // connect PDM to Role Result vertices
//                    PDMResultToRoleResultEdge edge =
//                            createPDMToRoleRelationship(pdmResultVertex,
//                                    roleResultVertex);
//
//                    model.insertPDMRoleEdge(edge);
//                    pdmRoleEdges.add(edge);

                } else if (roleResult instanceof QueryResult) {
                    // role result contains a query result
                    QueryResult result = (QueryResult) roleResult;

                    QueryResultVertex queryResultVertex = createQueryResultVertex(
                            patternResult,
                            result);

                    model.insertQueryResultVertex(queryResultVertex);
                    queryResultVertices.add(queryResultVertex);

                    logger.info ("Created Query Result Vertex = " + queryResultVertex);

                    // create pdm to query result role edge
                    // connect PDM to Role Result
                    PDMResultToRoleResultEdge edge =
                            createPDMToQueryRelationship(pdmResultVertex,
                                    queryResultVertex);

                    this.pdmRoleEdges.add(edge);
                    model.insertPDMRoleEdge(edge);

                    logger.info("Connected PDMResultVertex "
                        + pdmResultVertex
                        + " to QueryResult Vertex "
                        + queryResultVertex
                    );

                    // create the artifacts
                    //todo - for now only show the first column artifacts
                    ArtifactVertex [] vertices =
                            createArtifactVertices(result.getArtifacts());

                    // insert Artifacts
                    model.insertArtifactVertices(vertices);
                    for (int i=0; i < vertices.length; i++) {
                        artifactVertices.add(vertices[i]);
                    }

                    logger.info("Created Artifact Vertices for "+ vertices.length +" artifacts");

                    ArtifactEdge [] edges =
                            createArtifactEdges("", null,
                                    queryResultVertex, vertices);

                    model.insertArtifactEdges(edges);

                    for (int i=0; i < edges.length; i++) {
                        artifactEdges.add(edges[i]);
                    }
                    logger.info("Created Artifact Edges for "+ edges.length +" artifacts edges");
                }
            }
        }

        if (patternResult.getRelationships() != null) {
            for (Iterator iterator = patternResult.getRelationships().iterator(); iterator.hasNext();) {
                Object o = iterator.next();

                if(o instanceof Relationship) {
                    Relationship relationship = (Relationship)o ;
                    RoleResultToRoleResultEdge edge = createRoleToRoleRelationship(
                            pdmResultVertex,
                            patternResult.getPdm(),
                            relationship);
                    if (edge != null) {
                        // insert role to role edge
                        model.insertRoleToRoleEdge(edge);

                        roleToRoleEdges.add(edge);
                    }
                } else {
                    RelationshipResult relres = (RelationshipResult)o ;

                    RoleResultToRoleResultEdge r2rEdge = createRoleToRoleRelationship(
                            pdmResultVertex,
                            patternResult.getPdm(),
                            relres.getRelationship());

                    if (r2rEdge != null) {
                    // insert role to role edge
                        model.insertRoleToRoleEdge(r2rEdge);
                        roleToRoleEdges.add(r2rEdge);
                    }

                    ArtifactEdge[] edges =
                            createArtifactToArtifactRelationship(
                                    pdmResultVertex,
                                    relres);

                    // insert artifact edges
                    model.insertArtifactEdges(edges);
                    for (int i=0; i < edges.length; i++)
                        artifactEdges.add(edges[i]);

                }
            }
        }

        return pdmResultVertex;
    }

    private PDMResultToRoleResultEdge createPDMToQueryRelationship(PDMResultVertex pdmResultVertex,
                                                                   QueryResultVertex queryResultVertex) {
        PDMResultToRoleResultEdge edge = factory.createPDMToQueryRelationship(pdmResultVertex, queryResultVertex);
        return edge;
    }

    private QueryResultVertex createQueryResultVertex(PatternResult patternResult,
                                                      QueryResult queryResult) {
        QueryResultVertex v = factory.createQueryResultVertex(patternResult, queryResult);
        return v;
    }

    private PDMResultToRoleResultEdge createPDMToRoleRelationship(PDMResultVertex pdmResultVertex,
                                                                  RoleResultVertex roleResultVertex) {
        PDMResultToRoleResultEdge edge = factory.createPDMResultToRoleResultRelationship(pdmResultVertex, roleResultVertex);

        return edge;
    }


    private PDMResultVertex createPDMVertex(PatternResult pattern) {
        PDMResultVertex v = factory.createPDMResultVertex(pattern);
        return v;
    }

    private PDMResultToRoleResultEdge createPDMToRolePDMRelationship(PDMResultVertex pdmResultVertex, PDMResultVertex rolePDMResultVertex) {
        PDMResultToRoleResultEdge edge = factory.createPDMResultToRolePDMResultRelationship(pdmResultVertex, rolePDMResultVertex);

        return edge;
    }

    private RoleResultVertex createRoleVertex(PatternResult pattern, RoleResult result) {
        RoleResultVertex resultVertex = factory.createRoleResultVertex(pattern, result);
        return resultVertex;
    }

    private ArtifactVertex[] createArtifactVertices(Artifact[] artifacts) {
        ArrayList vertices = new ArrayList();
        for (int i = 0; i < artifacts.length; i++) {
            ArtifactVertex v = createArtifactVertex(artifacts[i]);
            vertices.add(v);
        }

        ArtifactVertex vs[] = new ArtifactVertex[vertices.size()];
        vertices.toArray(vs);

        return vs;
    }

    private ArtifactVertex createArtifactVertex(Artifact artifact) {
        ArtifactVertex vertex = factory.createArtifactVertex(artifact);
        return vertex;
    }

    private ArtifactEdge[] createArtifactEdges(
            String edgeName,
            Object relationship,
            AcreVertex sourceVertex,
            ArtifactVertex[] artifactVertices) {
        ArrayList edges = new ArrayList();

        for (int i =0; i < artifactVertices.length; i++) {
            ArtifactEdge edge = createArtifactEdge(
                    edgeName, relationship,
                    sourceVertex, artifactVertices[i]);
            edges.add(edge);
        }

        ArtifactEdge [] es = new ArtifactEdge[edges.size()];
        edges.toArray(es);
        return (es);
    }

    private ArtifactEdge createArtifactEdge(
            String edgeName,
            Object relationship,
            AcreVertex pdmVertex,
            ArtifactVertex artifactVertex) {

        ArtifactEdge edge = factory.createArtifactEdge(
                edgeName, relationship, pdmVertex, pdmVertex, artifactVertex);

        return edge;
    }

    private RoleResultToRoleResultEdge createRoleToRoleRelationship(
            PDMResultVertex parentPDMVertex,
            PDMType pdm,
            RelationshipType relationship) {


        AcreVertex fromVertex = getRoleVertex(pdm, relationship.getFromRole());
        AcreVertex toVertex = getRoleVertex(pdm, relationship.getToRole());

        RoleResultToRoleResultEdge r2rEdge = null;
        if ((fromVertex != null) && (toVertex != null)) {

            r2rEdge = factory.createRoleResultToRoleResultEdge(
                            relationship.getName(),
                            relationship,
                            parentPDMVertex,
                            fromVertex,
                            toVertex);
        } else {
            logger.warning("Failed to create Edge for from = " + relationship.getFromRole()
            + " and to = " + relationship.getToRole() +
                    " fromVertex = " + fromVertex + " toVertex = " + toVertex);
        }

        return r2rEdge;
    }

    private AcreVertex getRoleVertex(PDMType pdm, String roleName) {
        logger.info("Searching for Vertex = " + roleName);

        // search vertices for Role name matching roleName
        List vertices = roleVertices;
        for (int i=0; i < vertices.size(); i++) {
            RoleResultVertex v = (RoleResultVertex) vertices.get(i);
            AcreVertexData data = (AcreVertexData) v.getAcreData();
            Object value = data.getData();

            if (value != null) {
                if (value instanceof RoleResult) {
                    RoleResult roleRes = (RoleResult) value;
                    RoleType role = roleRes.getRole();
                    if ((role.getTypeReferenceName()!=null) &&
                            (role.getTypeReferenceName().equalsIgnoreCase(roleName))) {
                        return v;
                    }
                    else if (role.getName().equals(roleName)) {
                        return v;
                    }
                }
            }
        }

        return null;

    }


    private ArtifactEdge[] createArtifactToArtifactRelationship(
            PDMResultVertex parentPDMVertex,
            RelationshipResult relres) {

        ArrayList artToArtEdges = new ArrayList();
        ArtifactLink [] links = relres.getLinks();
        for (int i =0; i < links.length; i++) {
            ArtifactLink link = links[i];
            Artifact source = link.getSourceArtifact();
            ArtifactVertex sourceArtifact = getArtifactVertex(source);

            Artifact targets [] = link.getTargetArtifacts();
            for (int targArt=0; targArt < targets.length; targArt++) {
                Artifact target = targets[targArt];
                ArtifactVertex targetArtifact = getArtifactVertex(target);

                if ((targetArtifact != null) && (sourceArtifact != null)) {
                    ArtifactEdge edge =
                            factory.createArtifactEdge(relres.getName(), relres, parentPDMVertex,
                                    sourceArtifact, targetArtifact);
                    artToArtEdges.add(edge);
                }
            }
        }

        ArtifactEdge [] e = new ArtifactEdge[artToArtEdges.size()];
        artToArtEdges.toArray(e);
        return e;
    }


    private ArtifactVertex getArtifactVertex(Artifact parent) {
        for (int i=0; i < artifactVertices.size(); i++) {
            ArtifactVertex vertex = (ArtifactVertex) artifactVertices.get(i);

            if (vertex.getAcreData().getName().equals(parent.getName())) {
                return vertex;
            }

        }
        return null;
    }

//    protected abstract void insertPDMVertex(PDMVertex pdmVertex);
//    protected abstract void insertPDMRoleEdge(PDMRoleEdge edge);
//    protected abstract void insertRoleVertex(RoleVertex vertex);
//    protected abstract void insertArtifactEdges(ArtifactEdge[] artifactEdges);
//    protected abstract void insertArtifactEdge(ArtifactEdge artifactEdge);
//    protected abstract void insertRoleToRoleEdge(RoleToRoleEdge edge);

}
