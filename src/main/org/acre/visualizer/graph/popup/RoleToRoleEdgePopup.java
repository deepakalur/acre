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
package org.acre.visualizer.graph.popup;

import org.acre.pdm.PDMType;
import org.acre.pdm.RelationshipType;
import org.acre.visualizer.graph.AcreGraph;
import org.acre.visualizer.graph.edges.AcreEdge;
import org.acre.visualizer.graph.edges.AcreEdgeData;
import org.acre.visualizer.graph.pdmmodel.PDMModelGraph;
import org.acre.visualizer.graph.pdmmodel.edges.RoleToRoleEdge;
import org.acre.visualizer.graph.pdmmodel.vertex.PDMVertex;
import org.acre.visualizer.graph.vertex.AcreVertex;

import javax.swing.JMenuItem;
import java.awt.event.ActionEvent;

/**
 * @author Deepak.Alur@Sun.com
 * @version Dec 22, 2004 10:39:01 PM
 */
public class RoleToRoleEdgePopup
        extends GraphPopup {

    RoleToRoleEdge r2redge;

    public RoleToRoleEdgePopup(AcreGraph graph, RoleToRoleEdge roleToRoleEdge) {
        super(graph, roleToRoleEdge, "Role Relationship");
        r2redge = roleToRoleEdge;
        initializeMenus();
    }

    // subclasses to override this method if needed
    protected void handleEdgeAction(ActionEvent e, GraphPopup popup, AcreGraph graph, AcreEdge edge) {

        if (r2redge.getParentVertex() == null)
            return;

        if (r2redge.getParentVertex() instanceof PDMVertex) {
            PDMVertex pdmVertex = (PDMVertex) r2redge.getParentVertex();
            PDMType pdm = pdmVertex.getPDM();

            if (e.getActionCommand().equals(POPUP_DELETE_REL)) {
                PDMModelGraph pdmModelGraph = (PDMModelGraph) graph;
                if (pdmModelGraph.getPDMEditor() != null) {
//                    if (pdmModelGraph.getPDMEditor() instanceof AcrePDMExplorerPanel) {
//                        AcrePDMExplorerPanel pdmEditor = (AcrePDMExplorerPanel) pdmModelGraph.getPDMEditor();
//                        if (r2redge.getAcreData() instanceof AcreEdgeData) {
//                            AcreEdgeData edgeData = (AcreEdgeData) r2redge.getAcreData();
//                            RelationshipType rel = (RelationshipType) edgeData.getRelationship();
//                            pdmEditor.deleteRelationship(pdm, rel);
//                        }
//                    }
                }
            } else if (e.getActionCommand().equals(POPUP_EDIT_PDM_REL)) {
                PDMModelGraph pdmModelGraph = (PDMModelGraph) graph;
                if (pdmModelGraph.getPDMEditor() != null) {
//                    if (pdmModelGraph.getPDMEditor() instanceof AcrePDMExplorerPanel) {
//                        AcrePDMExplorerPanel pdmEditor = (AcrePDMExplorerPanel) pdmModelGraph.getPDMEditor();
//                        if (r2redge.getAcreData() instanceof AcreEdgeData) {
//                            AcreEdgeData edgeData = (AcreEdgeData) r2redge.getAcreData();
//                            RelationshipType rel = (RelationshipType) edgeData.getRelationship();
//                            pdmEditor.editRelationship(pdm, rel);
//                        }
//                    }
                }
            }
        }
    }

    // subclasses to override this method if needed
    protected void handleVertexAction(ActionEvent e, GraphPopup popup, AcreGraph graph, AcreVertex vertex) {
        // do nothing
    }

    protected void initializeMenus() {
        AcreEdge edge = getEdge();
        if (edge != null) {
            if (edge instanceof RoleToRoleEdge) {
                initializeDefaultMenus();
                addSeparator();
                JMenuItem delRelMenu = addMenu(POPUP_DELETE_REL, null, this);
                JMenuItem editRelMenu = addMenu(POPUP_EDIT_PDM_REL, null, this);
                delRelMenu.setEnabled(false);
                editRelMenu.setEnabled(false);

                if (getGraph().getSelectionCount() == 1) {
                    delRelMenu.setEnabled(true);
                    editRelMenu.setEnabled(true);
                }
            }
        }
    }


}
