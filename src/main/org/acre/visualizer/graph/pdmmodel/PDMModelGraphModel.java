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

import org.acre.visualizer.graph.AcreGraphModel;
import org.acre.visualizer.graph.pdmmodel.edges.PDMToRoleEdge;
import org.acre.visualizer.graph.pdmmodel.edges.RoleToPDMEdge;
import org.acre.visualizer.graph.pdmmodel.edges.RoleToRoleEdge;
import org.acre.visualizer.graph.pdmmodel.vertex.PDMVertex;
import org.acre.visualizer.graph.pdmmodel.vertex.RoleVertex;

/**
 * @author Deepak.Alur@Sun.com
 * @version Dec 22, 2004 3:28:59 PM
 */
public interface PDMModelGraphModel extends AcreGraphModel {

    void insertRoleToPDMEdge(RoleToPDMEdge edge);
    void insertRoleToRoleEdge(RoleToRoleEdge e);
    void insertPDMRoleEdge(PDMToRoleEdge e);
    void insertRoleVertex(RoleVertex v);
    void insertPDMVertex(PDMVertex pdmVertex);

}
