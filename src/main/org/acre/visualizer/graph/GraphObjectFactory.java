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
package org.acre.visualizer.graph;


import org.acre.visualizer.graph.pdmmodel.edges.PDMToRoleEdge;
import org.acre.visualizer.graph.pdmmodel.edges.RoleToPDMEdge;
import org.acre.visualizer.graph.pdmmodel.edges.RoleToRoleEdge;
import org.acre.visualizer.graph.pdmmodel.vertex.PDMVertex;
import org.acre.visualizer.graph.pdmmodel.vertex.RoleVertex;
import org.acre.visualizer.graph.pdmresult.QueryResultVertex;
import org.acre.visualizer.graph.pdmresult.edges.ArtifactEdge;
import org.acre.visualizer.graph.pdmresult.edges.PDMResultToRoleResultEdge;
import org.acre.visualizer.graph.pdmresult.edges.RoleResultToRoleResultEdge;
import org.acre.visualizer.graph.pdmresult.vertex.ArtifactVertex;
import org.acre.visualizer.graph.pdmresult.vertex.PDMResultVertex;
import org.acre.visualizer.graph.pdmresult.vertex.RoleResultVertex;
import org.acre.visualizer.graph.vertex.AcreVertex;
import org.acre.pdmengine.model.Artifact;
import org.acre.pdmengine.model.PatternResult;
import org.acre.pdmengine.model.QueryResult;
import org.acre.pdmengine.model.RoleResult;
import org.acre.pdm.RelationshipType;
import org.acre.pdm.PDMType;
import org.acre.pdm.RoleType;

/**
 * @author Deepak.Alur@Sun.com
 * @version Dec 20, 2004 3:22:26 PM
 */
public abstract class GraphObjectFactory implements GraphFactoryConstants {


    public static GraphObjectFactory getFactory(int type) {
        switch (type) {
            case JGRAPH:
                try {
                    Class c = Class.forName("org.acre.visualizer.graphimpl.jgraph.JGraphObjectFactoryImpl");
                    return (GraphObjectFactory) c.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (IllegalAccessException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

            case JUNG:

                try {
                    Class c = Class.forName("org.acre.visualizer.graphimpl.jung.JungGraphObjectFactoryImpl");
                    return (GraphObjectFactory) c.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (IllegalAccessException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

            case PREFUSE:
                throw new AcreGraphException("PREFUSE Graph not implemented");

            default:
                throw new AcreGraphException("Uknown Graph Type supplied to GraphObjectFactory.getFactory()");

        }
    }


    public abstract RoleResultToRoleResultEdge createRoleResultToRoleResultEdge(String name,
                                               RelationshipType relationship,
                                               AcreVertex parentVertex,
                                               AcreVertex sourceVertex,
                                               AcreVertex targetVertex);

    public abstract ArtifactVertex createArtifactVertex(Artifact artifact);

    public abstract ArtifactEdge createArtifactEdge(String edgeName,
                                           Object relationship,
                                           AcreVertex parentVertex,
                                           AcreVertex sourceVertex,
                                           ArtifactVertex artifactVertex);

    public abstract RoleResultVertex createRoleResultVertex(PatternResult pattern, RoleResult result) ;


    public abstract PDMResultToRoleResultEdge createPDMResultToRolePDMResultRelationship(
            PDMResultVertex pdmResultVertex,
            PDMResultVertex rolePDMResultVertex);


    public abstract PDMResultVertex createPDMResultVertex(PatternResult pattern);

    // MODEL objects
    public abstract PDMToRoleEdge createPDMToRoleEdge(PDMVertex pdmVertex, RoleVertex roleVertex);

    public abstract RoleToRoleEdge createRoleToRoleEdge(String name, RelationshipType rel, PDMVertex parentPDMVertex, RoleVertex from, RoleVertex to);

    public abstract RoleToPDMEdge createRoleToPDMEdge(PDMVertex parentPDMVertex, RoleVertex v, PDMVertex rolePDMVertex);


    public abstract PDMVertex createPDMVertex(PDMType pdm);

    public abstract RoleVertex createRoleVertex(PDMType pdm, RoleType role);


    public abstract PDMResultToRoleResultEdge createPDMResultToRoleResultRelationship(PDMResultVertex pdmResultVertex,
                                                                             RoleResultVertex roleResultVertex) ;

    public abstract QueryResultVertex createQueryResultVertex(PatternResult patternResult, QueryResult queryResult);

    public abstract PDMResultToRoleResultEdge createPDMToQueryRelationship(PDMResultVertex pdmResultVertex,
                                                                  QueryResultVertex queryResultVertex) ;

}