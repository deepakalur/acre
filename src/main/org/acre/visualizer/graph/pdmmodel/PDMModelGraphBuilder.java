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
package org.acre.visualizer.graph.pdmmodel;

import org.acre.pdm.PDMType;
import org.acre.pdm.RelationshipType;
import org.acre.pdm.RoleType;
import org.acre.common.AcreStringUtil;
import org.acre.config.ConfigService;
import org.acre.dao.DAOFactory;
import org.acre.dao.PDMXMLConstants;
import org.acre.visualizer.graph.GraphObjectFactory;
import org.acre.visualizer.graph.AcreGraphException;
import org.acre.visualizer.graph.edges.AcreEdgeData;
import org.acre.visualizer.graph.pdmmodel.edges.PDMToRoleEdge;
import org.acre.visualizer.graph.pdmmodel.edges.RoleToPDMEdge;
import org.acre.visualizer.graph.pdmmodel.edges.RoleToRoleEdge;
import org.acre.visualizer.graph.pdmmodel.vertex.PDMVertex;
import org.acre.visualizer.graph.pdmmodel.vertex.RoleVertex;
import org.acre.visualizer.graph.vertex.AcreVertexData;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Deepak.Alur@Sun.com
 * @version Dec 22, 2004 3:34:52 PM
 */

public class PDMModelGraphBuilder {
    List pdmList = new ArrayList();
    List pdmVertices = new ArrayList();
    List roleVertices = new ArrayList();
    List pdmToRoleEdges = new ArrayList();
    List relationshipEdges = new ArrayList();
    List roleToPDMEdges = new ArrayList();
    GraphObjectFactory factory;
    private PDMModelGraphModel model;
    private Logger logger;
    int graphType;

    public PDMModelGraphBuilder(int graphType) {
        super();
        this.graphType = graphType;
        logger = ConfigService.getInstance().getLogger(this);
        factory = GraphObjectFactory.getFactory(graphType);
    }

    public List getPdmList() {
        return pdmList;
    }

    public void setPdmList(List pdmList) {
        this.pdmList = pdmList;
    }

    public List getPdmVertices() {
        return pdmVertices;
    }

    public void setPdmVertices(List pdmVertices) {
        this.pdmVertices = pdmVertices;
    }

    public List getRoleVertices() {
        return roleVertices;
    }

    public void setRoleVertices(List roleVertices) {
        this.roleVertices = roleVertices;
    }

    public List getPdmToRoleEdges() {
        return pdmToRoleEdges;
    }

    public void setPdmToRoleEdges(List pdmToRoleEdges) {
        this.pdmToRoleEdges = pdmToRoleEdges;
    }

    public List getRelationshipEdges() {
        return relationshipEdges;
    }

    public void setRelationshipEdges(List relationshipEdges) {
        this.relationshipEdges = relationshipEdges;
    }

    public List getRoleToPDMEdges() {
        return roleToPDMEdges;
    }

    public void setRoleToPDMEdges(List roleToPDMEdges) {
        this.roleToPDMEdges = roleToPDMEdges;
    }

    public GraphObjectFactory getFactory() {
        return factory;
    }

    public void setFactory(GraphObjectFactory factory) {
        this.factory = factory;
    }

    public PDMModelGraphModel getModel() {
        return model;
    }

    public void setModel(PDMModelGraphModel model) {
        this.model = model;
    }

    public PDMVertex addPDM(PDMType pdm, boolean recursive) {

        if (factory == null) {
            throw new AcreGraphException("GraphObjectFactory cannot be null");
        }

        if (model == null) {
            throw new AcreGraphException("GraphModel cannot be null");
        }

        PDMVertex pdmVertex;

        if (pdm == null)
            throw new AcreGraphException("PDM Cannot be NULL to add");

        if (getPDMVertexFromCache(pdm) != null) {
            // already added to the graph, do not add again
            return getPDMVertexFromCache(pdm);
        }

        logger.info("Adding PDM: " + pdm.getName());
        this.pdmList.add(pdm);

        // create a vertex for the PDM
        pdmVertex = createPDMVertex(pdm);

        // add the PDM vertex to the model
        model.insertPDMVertex(pdmVertex);

        this.pdmVertices.add(pdmVertex);

        // create vertices for each role in the PDM
        List roleVertices = createRoleVertices(pdmVertex, recursive);

        // add role vertices to the list
        for (int i=0; i < roleVertices.size(); i++) {
            RoleVertex v =(RoleVertex ) roleVertices.get(i);
            AcreVertexData vertexData = (AcreVertexData) v.getAcreData();
            Object data = vertexData.getData();

            logger.info("Got Role Vertex " + ((RoleType) data).getName());
            this.roleVertices.add(v);

            //model.insertRoleVertex(v);

        }

        // get edges from PDM vertex to the role vertices
        List pdmToRoleEdges = createPDMtoRoleEdges(
                pdmVertex,
                roleVertices);

        // add these edges to the graph
        for (int i=0; i < pdmToRoleEdges.size(); i++) {
            PDMToRoleEdge e = (PDMToRoleEdge) pdmToRoleEdges.get(i);
            AcreEdgeData data = (AcreEdgeData) e.getAcreData();
            logger.info("Got PDM To Role Edge " +
                    data.getName() + " from = " + data.getFrom() + "to = " + data.getTo());

            this.pdmToRoleEdges.add(e);

            // insert edge
            model.insertPDMRoleEdge(e);

        }

        List roleToRoleEdges = createRoleToRoleEdges(pdmVertex);

        // add edges
        for (int i=0; i < roleToRoleEdges.size(); i++) {
            RoleToRoleEdge e = (RoleToRoleEdge) roleToRoleEdges.get(i);
            AcreEdgeData data = (AcreEdgeData) e.getAcreData();

            logger.info("Got Relationship Edge " +
                data.getName() + " from = " + data.getFrom()
                    + " to = " + data.getTo());

            this.relationshipEdges.add(e);

            model.insertRoleToRoleEdge(e);

        }

        // finally add role to PDM Edges
        for (int i = 0 ; i < roleToPDMEdges.size(); i++) {
            RoleToPDMEdge edge = (RoleToPDMEdge) roleToPDMEdges.get(i);
            AcreEdgeData  edgeData = null;
            edgeData = (AcreEdgeData) edge.getAcreData();

            logger.info("Connecting Role "+ edgeData.getFrom()
                        + " to PDM " + edgeData.getTo());
            model.insertRoleToPDMEdge(edge);
        }

        return pdmVertex;
    }

    private List createRoleToRoleEdges(PDMVertex pdmVertex) {
        List edges = new ArrayList();
        PDMType pdm = pdmVertex.getPDM();

        List rels = pdm.getRelationships().getRelationship();

        for (int i=0; i < rels.size(); i++) {
            RelationshipType rel = (RelationshipType) rels.get(i);

            String fromName = rel.getFromRole();
            fromName = AcreStringUtil.getRoleNameEnding(rel.getFromRole());
            RoleVertex from = getRoleVertex(pdm.getName(), fromName);
            if (from == null) {
                fromName = AcreStringUtil.getRoleNameBeginning(rel.getFromRole());
                from = getRoleVertex(pdm.getName(), fromName);
            }

            String toName = rel.getToRole();
            toName = AcreStringUtil.getRoleNameEnding(rel.getToRole());
            RoleVertex to = getRoleVertex(pdm.getName(), toName);
            if (to== null) {
                toName = AcreStringUtil.getRoleNameBeginning(rel.getToRole());
                to = getRoleVertex(pdm.getName(), toName);
            }

            logger.info("Finding Edge : " + fromName + " TO " + toName);

            if ((from != null) && (to != null)) {
                RoleToRoleEdge edge= factory.createRoleToRoleEdge(
                        rel.getName(), rel, pdmVertex, from, to
                );

                model.createConnection(edge, from, to);
                edges.add(edge);
            } else {
                logger.warning("Did not add Edge for : from = " + from + " to = " + to);
            }
        }
        return edges;
    }

    private RoleVertex getRoleVertex(String pdmName, String roleName) {

        logger.info("Searching for Vertex = " + roleName);

        // search vertices for Role name matching roleName
        List vertices = roleVertices;
        for (int i=0; i < vertices.size(); i++) {
            RoleVertex v = (RoleVertex ) vertices.get(i);
            AcreVertexData data = (AcreVertexData) v.getAcreData();
            Object value = data.getData();

            if (value != null) {
                if (value instanceof RoleType) {
                    RoleType role = (RoleType) value;
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

    private List createPDMtoRoleEdges(PDMVertex pdmVertex, List roleVertices) {
        List edges = new ArrayList();

        for (int i=0; i < roleVertices.size(); i++) {
            RoleVertex v = (RoleVertex) roleVertices.get(i);
            PDMToRoleEdge edge = connectPDMToRole(pdmVertex, v);
            edges.add(edge);
        }
        return edges;
    }

    private PDMToRoleEdge connectPDMToRole(PDMVertex pdmVertex, RoleVertex v) {
        PDMToRoleEdge edge=null;
        edge = factory.createPDMToRoleEdge(pdmVertex, v);
//        edge = new PDMToRoleJGraphEdge(pdmVertex, v);

        model.createConnection(edge, pdmVertex, v);

        return edge;
    }

    private List createRoleVertices(PDMVertex parentPDMVertex, boolean recursive) {
        List vertices = new ArrayList();

        PDMType pdm = parentPDMVertex.getPDM();
        List roles = pdm.getRoles().getRole();
        for (int i=0; i < roles.size(); i++) {
            RoleType role = (RoleType) roles.get(i);

            RoleVertex v = createRoleVertex(pdm, role);
            model.insertRoleVertex(v);

            vertices.add(v);

            if (recursive) {
                // recursive requested, drill down to roles that are PDMs and add
                if (role.getType().equals(PDMXMLConstants.ROLE_TYPE_PDM)) {
                    String typeRef = role.getTypeReferenceName();
                    if (typeRef == null)
                        typeRef = role.getName();

                    PDMType rolePDM = DAOFactory.getPatternRepository().getGlobalPatternModel(typeRef);
                    PDMVertex rolePDMVertex = addPDM(rolePDM, recursive);

                    RoleToPDMEdge roleToPDMEdge = connectRoleToPDM(parentPDMVertex, v, rolePDMVertex);
                    roleToPDMEdges.add(roleToPDMEdge);
                }
            }
        }
        return vertices;
    }

    private RoleToPDMEdge connectRoleToPDM(PDMVertex parentPDMVertex, RoleVertex v, PDMVertex rolePDMVertex) {
        RoleToPDMEdge edge=null;
        edge = factory.createRoleToPDMEdge(parentPDMVertex, v, rolePDMVertex);
//        edge = new SalsaRoleToPDMJGraphEdge(v, rolePDMVertex);

        model.createConnection(edge, v, rolePDMVertex);

        return edge;

    }

    private PDMVertex createPDMVertex(PDMType pdm) {
//        PDMVertex pdmCell = new SalsaPDMJGraphVertex(pdm);
        PDMVertex pdmCell = factory.createPDMVertex(pdm);
        return pdmCell;
    }

    private RoleVertex createRoleVertex(PDMType pdm, RoleType role) {
        RoleVertex roleCell;
        roleCell = factory.createRoleVertex(pdm, role);
        return roleCell;
    }

    private PDMVertex getPDMVertexFromCache(PDMType pdm) {
        if (pdm == null)
            throw new AcreGraphException("Cannot check exists for NULL PDM");

        for (int i=0; i < pdmList.size(); i++) {
            PDMType gotPDM = (PDMType) pdmList.get(i);
            if (gotPDM.getName().equalsIgnoreCase(pdm.getName()))
                return (PDMVertex) pdmList.get(i);
        }

        return null;
    }

    public void addPDM(PDMType pdm) {
        addPDM(pdm, false);
    }
}
/*
class ModelGraphBuilder {
    List pdmList = new ArrayList();
    List pdmVertices = new ArrayList();
    List roleVertices = new ArrayList();
    List pdmToRoleEdges = new ArrayList();
    List roleToRoleEdges = new ArrayList();
    List roleToPDMEdges = new ArrayList();
    GraphObjectFactory factory;
    private PDMModelGraphModel model;
    private Logger logger;
    int graphType = -1;

    public ModelGraphBuilder(int graphType) {
        super();
        logger = ConfigService.getInstance().getLogger(this);
        this.graphType = graphType;
        factory = GraphObjectFactory.getFactory(graphType);
    }

    public List getPdmList() {
        return pdmList;
    }

    public void setPdmList(List pdmList) {
        this.pdmList = pdmList;
    }

    public List getPdmVertices() {
        return pdmVertices;
    }

    public void setPdmVertices(List pdmVertices) {
        this.pdmVertices = pdmVertices;
    }

    public List getRoleVertices() {
        return roleVertices;
    }

    public void setRoleVertices(List roleVertices) {
        this.roleVertices = roleVertices;
    }

    public List getPdmToRoleEdges() {
        return pdmToRoleEdges;
    }

    public void setPdmToRoleEdges(List pdmToRoleEdges) {
        this.pdmToRoleEdges = pdmToRoleEdges;
    }

    public List getRoleToRoleEdges() {
        return roleToRoleEdges;
    }

    public void setRoleToRoleEdges(List roleToRoleEdges) {
        this.roleToRoleEdges = roleToRoleEdges;
    }

    public List getRoleToPDMEdges() {
        return roleToPDMEdges;
    }

    public void setRoleToPDMEdges(List roleToPDMEdges) {
        this.roleToPDMEdges = roleToPDMEdges;
    }

    public GraphObjectFactory getFactory() {
        return factory;
    }

    public void setFactory(GraphObjectFactory factory) {
        this.factory = factory;
    }

    public PDMModelGraphModel getModel() {
        return model;
    }

    public void setModel(PDMModelGraphModel model) {
        this.model = model;
    }

    public PDMVertex addPDM(PDMType pdm, boolean recursive) {

        if (factory == null) {
            throw new AcreGraphException("GraphObjectFactory cannot be null");
        }

        if (model == null) {
            throw new AcreGraphException("GraphModel cannot be null");
        }

        PDMVertex pdmVertex;

        if (pdm == null)
            throw new AcreGraphException("PDM Cannot be NULL to add");

        if (getPDMVertexFromCache(pdm) != null) {
            // already added to the graph, do not add again
            return getPDMVertexFromCache(pdm);
        }

        logger.info("Adding PDM: " + pdm.getName());
        this.pdmList.add(pdm);

        // create a vertex for the PDM
        pdmVertex = createPDMVertex(pdm);

        // add the PDM vertex to the model
        model.insertPDMVertex(pdmVertex);

        this.pdmVertices.add(pdmVertex);

        // create vertices for each role in the PDM
        List pdmRoleVertices = createRoleVertices(pdm, recursive);

        // add role vertices to the list
        for (int i=0; i < pdmRoleVertices.size(); i++) {
            RoleVertex v =(RoleVertex ) pdmRoleVertices .get(i);
            AcreVertexData vertexData = (AcreVertexData) v.getAcreData();
            Object data = vertexData.getData();

            logger.info("Got Role Vertex " + ((RoleType) data).getName());
            this.roleVertices.add(v);

            model.insertRoleVertex(v);
        }

        // get edges from PDM vertex to the role vertices
        List pdmRoleEdges = createPDMtoRoleEdges(
                pdmVertex,
                pdmRoleVertices);

        // add these edges to the graph
        for (int i=0; i < pdmRoleEdges.size(); i++) {
            PDMToRoleEdge e = (PDMToRoleEdge) pdmRoleEdges.get(i);
            AcreEdgeData data = (AcreEdgeData) e.getAcreData();
            logger.info("Got PDM To Role Edge " +
                    data.getName() + " from = " + data.getFrom() + "to = " + data.getTo());

            // add to the list
            this.pdmToRoleEdges.add(e);

            // insert edge
            model.insertPDMRoleEdge(e);

        }

        List pdmroleToRoleEdges = createRoleToRoleEdges(pdm);

        // add edges
        for (int i=0; i < pdmroleToRoleEdges.size(); i++) {
            RoleToRoleEdge e = (RoleToRoleEdge) pdmroleToRoleEdges.get(i);
            AcreEdgeData data = (AcreEdgeData) e.getAcreData();

            logger.info("Got Relationship Edge " +
                data.getName() + " from = " + data.getFrom()
                    + " to = " + data.getTo());

            this.roleToRoleEdges.add(e);

            model.insertRoleToRoleEdge(e);

        }

        // todo test code starts
        // if recursive, add role pdms
        if (recursive) {
            List roles = pdm.getRoles().getRole();
            for (int i=0; i < roles.size(); i++) {
                RoleType role = (RoleType) roles.get(i);
                // recursive requested, drill down to roles that are PDMs and add
                if (role.getType().equals(PDMXMLConstants.ROLE_TYPE_PDM)) {
                    RoleVertex roleVertex = getRoleVertex(pdm.getName(), role.getName());
                    if (roleVertex == null) {
                        continue;
                    }
                    String typeRef = role.getTypeReferenceName();
                    if (typeRef == null) {
                        typeRef = role.getName();
                    }
                    PDMType rolePDM = PatternRepository.getInstance().getGlobalPatternModel(typeRef);
                    PDMVertex rolePDMVertex = addPDM(rolePDM, recursive);
                    RoleToPDMEdge roleToPDMEdge = connectRoleToPDM(roleVertex, rolePDMVertex);
                    roleToPDMEdges.add(roleToPDMEdge);
                }
            }
        }

        // todo test code ends

        // finally add role to PDM Edges
        for (int i = 0 ; i < roleToPDMEdges.size(); i++) {
            RoleToPDMEdge edge = (RoleToPDMEdge) roleToPDMEdges.get(i);
            AcreEdgeData  edgeData = null;
            edgeData = (AcreEdgeData) edge.getAcreData();

            logger.info("Connecting Role "+ edgeData.getFrom()
                        + " to PDM " + edgeData.getTo());
            model.insertRoleToPDMEdge(edge);
        }

        return pdmVertex;
    }

    private List createRoleToRoleEdges(PDMType pdm) {
        List edges = new ArrayList();
        List rels = pdm.getRelationships().getRelationship();

        for (int i=0; i < rels.size(); i++) {
            RelationshipType rel = (RelationshipType) rels.get(i);

            String fromName = rel.getFromRole();
            fromName = getRoleNameEnding(rel.getFromRole());
            RoleVertex from = getRoleVertex(pdm.getName(), fromName);
            if (from == null) {
                fromName = getRoleNameBeginning(rel.getFromRole());
                from = getRoleVertex(pdm.getName(), fromName);
            }

            String toName = rel.getToRole();
            toName = getRoleNameEnding(rel.getToRole());
            RoleVertex to = getRoleVertex(pdm.getName(), toName);
            if (to== null) {
                toName = getRoleNameBeginning(rel.getToRole());
                to = getRoleVertex(pdm.getName(), toName);
            }

            logger.info("Finding Edge : " + fromName + " TO " + toName);

            if ((from != null) && (to != null)) {
                RoleToRoleEdge edge= factory.createRoleToRoleEdge(
                        rel.getName(), rel, from, to
                );

                model.createConnection(edge, from, to);
                edges.add(edge);
            } else {
                logger.warning("Did not add Edge for : from = " + from + " to = " + to);
            }
        }
        return edges;
    }

    private RoleVertex getRoleVertex(String pdmName, String roleName) {

        logger.info("Searching for Vertex = " + roleName);

        // search vertices for Role name matching roleName
        List vertices = roleVertices;
        for (int i=0; i < vertices.size(); i++) {
            RoleVertex v = (RoleVertex ) vertices.get(i);
            AcreVertexData data = (AcreVertexData) v.getAcreData();
            Object value = data.getData();

            if (value != null) {
                if (value instanceof RoleType) {
                    RoleType role = (RoleType) value;
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


        logger.severe("Could not find role vertex for pdm = " + pdmName + " role = " + roleName);

        return null;
    }

    private String getRoleNameBeginning(String fullyQualifiedRoleName) {
        StringTokenizer tokenizer = new StringTokenizer(
                fullyQualifiedRoleName,
                GraphFactoryConstants.SEPARATOR);
        String roleName = tokenizer.nextToken();
        return roleName;
    }

    private String getRoleNameEnding(String fullyQualifiedRoleName) {
        StringTokenizer tokenizer = new StringTokenizer(fullyQualifiedRoleName, GraphFactoryConstants.SEPARATOR);
        String roleName = null;
        while(tokenizer.hasMoreTokens()) {
            roleName = tokenizer.nextToken();
        }
        return roleName;
    }

    private List createPDMtoRoleEdges(PDMVertex pdmVertex, List roleVertices) {
        List edges = new ArrayList();

        for (int i=0; i < roleVertices.size(); i++) {
            RoleVertex v = (RoleVertex) roleVertices.get(i);
            PDMToRoleEdge edge = connectPDMToRole(pdmVertex, v);
            edges.add(edge);
        }
        return edges;
    }

    private PDMToRoleEdge connectPDMToRole(PDMVertex pdmVertex, RoleVertex v) {
        PDMToRoleEdge edge=null;
        edge = factory.createPDMToRoleEdge(pdmVertex, v);
//        edge = new PDMToRoleJGraphEdge(pdmVertex, v);

        model.createConnection(edge, pdmVertex, v);

        return edge;
    }

    private List createRoleVertices(PDMType pdm, boolean recursive) {
        List vertices = new ArrayList();

        List roles = pdm.getRoles().getRole();
        for (int i=0; i < roles.size(); i++) {
            RoleType role = (RoleType) roles.get(i);

            RoleVertex v = createRoleVertex(role);

            vertices.add(v);

//            if (recursive) {
//                // recursive requested, drill down to roles that are PDMs and add
//                if (role.getType().equals(PDMXMLConstants.ROLE_TYPE_PDM)) {
//                    String typeRef = role.getTypeReferenceName();
//                    if (typeRef == null) {
//                        typeRef = role.getName();
//                    }
//                    PDMType rolePDM = PatternRepository.getInstance().getGlobalPatternModel(typeRef);
//                    PDMVertex rolePDMVertex = addPDM(rolePDM, recursive);
//                    RoleToPDMEdge roleToPDMEdge = connectRoleToPDM(v, rolePDMVertex);
//                    roleToPDMEdges.add(roleToPDMEdge);
//                }
//            }
        }
        return vertices;
    }

    private RoleToPDMEdge connectRoleToPDM(RoleVertex v, PDMVertex rolePDMVertex) {
        RoleToPDMEdge edge=null;
        edge = factory.createRoleToPDMEdge(v, rolePDMVertex);
        model.createConnection(edge, v, rolePDMVertex);

        return edge;
    }

    private PDMVertex createPDMVertex(PDMType pdm) {
        PDMVertex pdmCell = factory.createPDMVertex(pdm);
        return pdmCell;
    }

    private RoleVertex createRoleVertex(RoleType role) {
        RoleVertex roleCell;
        roleCell = factory.createRoleVertex(role);
        return roleCell;
    }

    private PDMVertex getPDMVertexFromCache(PDMType pdm) {
        if (pdm == null)
            throw new AcreGraphException("Cannot check exists for NULL PDM");

        for (int i=0; i < pdmVertices.size(); i++) {
            PDMVertex vertex = (PDMVertex) pdmVertices.get(i);
            PDMType gotPDM = vertex.getPDM();
            if (gotPDM.getName().equalsIgnoreCase(pdm.getName()))
                return vertex;
        }

        return null;
    }

    public PDMVertex addPDM(PDMType pdm) {
        return addPDM(pdm, false);
    }
}
*/