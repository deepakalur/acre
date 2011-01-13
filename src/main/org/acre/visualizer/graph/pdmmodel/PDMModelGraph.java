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

import org.acre.visualizer.graph.AcreGraph;
import org.acre.visualizer.graph.pdmmodel.edges.RoleToRoleEdge;
import org.acre.visualizer.graph.pdmmodel.vertex.PDMVertex;
import org.acre.visualizer.graph.pdmmodel.vertex.RoleVertex;
import org.acre.visualizer.ui.components.Editor;

/**
 * @author Deepak.Alur@Sun.com
 * @version Jan 5, 2005 11:40:49 PM
 */
public interface PDMModelGraph extends AcreGraph {

    void setPDMEditor(Editor editor);
    Editor getPDMEditor();

    void setRoleEditor(Editor editor);
    Editor getRoleEditor();

    void setRelationshipEditor(Editor editor);
    Editor getRelationshipEditor();

    RoleVertex getSelectedRole();
    PDMVertex getSelectedPDM();
    RoleToRoleEdge getSelectedRoleToRoleRelationship();

    RoleVertex [] getSelectedRoles() ;
    PDMVertex [] getSelectedPDMs();
    RoleToRoleEdge[] getSelectedRoleToRoleRelationships();

}
