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
package org.acre.visualizer.graphimpl.jgraph.pdmmodel;

import org.acre.visualizer.graphimpl.jgraph.AcreGraphModelImpl;
import org.acre.visualizer.graph.graph.pdmmodel.PDMModelGraphModel;
import org.acre.visualizer.graph.graph.pdmmodel.edges.PDMToRoleEdge;
import org.acre.visualizer.graph.graph.pdmmodel.edges.RoleToPDMEdge;
import org.acre.visualizer.graph.graph.pdmmodel.edges.RoleToRoleEdge;
import org.acre.visualizer.graph.graph.pdmmodel.vertex.PDMVertex;
import org.acre.visualizer.graph.graph.pdmmodel.vertex.RoleVertex;

/**
 * @author Deepak.Alur@Sun.com
 * @version Dec 14, 2004 11:54:27 AM
 */
public class PDMModelGraphModelImpl
        extends AcreGraphModelImpl
        implements PDMModelGraphModel {

    public PDMModelGraphModelImpl() {
        super();
        initialize();
    }


    public void insertRoleToPDMEdge(RoleToPDMEdge edge) {
        insertSalsaEdge(edge);
    }

    public void insertRoleToRoleEdge(RoleToRoleEdge e) {
        logger.info("Inserting Relationship Edge " + e);
        insertSalsaEdge(e);
    }

    public void insertPDMRoleEdge(PDMToRoleEdge e) {
        logger.info("Inserting PDM To Role edge" + e);
        insertSalsaEdge(e);
    }

    public void insertRoleVertex(RoleVertex v) {
        logger.info("Inserting Role Vertex : " + v);
        insertSalsaVertex(v);

    }

    public void insertPDMVertex(PDMVertex pdmVertex) {
        logger.info("Inserting PDM vertex : " + pdmVertex);
        insertSalsaVertex(pdmVertex);
    }


}
