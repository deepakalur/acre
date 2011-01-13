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

import org.acre.visualizer.graph.AcreGraph;
import org.acre.visualizer.graph.edges.AcreEdge;
import org.acre.visualizer.graph.pdmmodel.PDMModelGraph;
import org.acre.visualizer.graph.pdmmodel.vertex.RoleVertex;
import org.acre.visualizer.graph.vertex.AcreVertex;

import javax.swing.JMenuItem;
import java.awt.event.ActionEvent;

/**
 * @author Deepak.Alur@Sun.com
 * @version Dec 15, 2004 9:45:30 AM
 */
public class RoleVertexPopup
        extends GraphPopup {
    RoleVertex roleVertex;

    public RoleVertexPopup(AcreGraph graph, RoleVertex cell) {
        super(graph, cell, "Role");
        roleVertex = cell;
        initializeMenus();
    }

    protected void initializeMenus() {
        initializeDefaultMenus();
        addSeparator();
        JMenuItem editRoleMenu = addMenu(POPUP_EDIT_ROLE, null, this);
        JMenuItem delRoleMenu = addMenu(POPUP_DELETE_ROLE, null, this);
        addSeparator();
        JMenuItem addRelMenu = addMenu(POPUP_ADD_REL, null, this);
        JMenuItem delRelMenu = addMenu(POPUP_DELETE_REL, null, this);

        editRoleMenu.setEnabled(false);
        delRoleMenu.setEnabled(false);
        addRelMenu.setEnabled(false);
        delRelMenu.setEnabled(false);

        // if multiple roles are selected, disable the following operations
        Object [] cells = getGraph().getSelectionCells();
        if ((cells == null) || (cells.length == 0)) {
            // no vertex selected, return
            return;
        }

        // 1 cell selected
        if (cells.length == 1) {
            if (cells[0] instanceof RoleVertex) {
                // enable delete role
                delRoleMenu.setEnabled(true);
                editRoleMenu.setEnabled(true);
            }
        }
        // 2 vertex selected
        else if (cells.length == 2) {
             if ((cells[0] instanceof RoleVertex) &&
                (cells[1] instanceof RoleVertex)) {
                // enable all
                delRoleMenu.setEnabled(true);
                addRelMenu.setEnabled(true);
                delRelMenu.setEnabled(true);
            }
        }
    }

    // subclasses to override this method if needed
    protected void handleEdgeAction(ActionEvent e, GraphPopup popup, AcreGraph graph, AcreEdge edge) {
        // do nothing. this is not a vertex
    }

    // subclasses to override this method if needed
    protected void handleVertexAction(ActionEvent e, GraphPopup popup, AcreGraph graph, AcreVertex vertex) {
        if (e.getActionCommand().equals(POPUP_EDIT_ROLE)) {
           if (graph instanceof PDMModelGraph) {
                PDMModelGraph pdmModelGraph = (PDMModelGraph) graph;
                if (pdmModelGraph.getPDMEditor() != null) {
//                    if (pdmModelGraph.getPDMEditor() instanceof AcrePDMExplorerPanel) {
//                        AcrePDMExplorerPanel pdmEditor = (AcrePDMExplorerPanel) pdmModelGraph.getPDMEditor();
//                        pdmEditor.editRole(roleVertex.getPDM(), roleVertex.getRole());
//
//                    }
                }
            }
        }
        else if (e.getActionCommand().equals(POPUP_DELETE_ROLE)) {
            if (graph instanceof PDMModelGraph) {
                PDMModelGraph pdmModelGraph = (PDMModelGraph) graph;
//                if (pdmModelGraph.getPDMEditor() != null) {
//                    if (pdmModelGraph.getPDMEditor() instanceof AcrePDMExplorerPanel) {
//                        AcrePDMExplorerPanel pdmEditor = (AcrePDMExplorerPanel) pdmModelGraph.getPDMEditor();
//                        pdmEditor.deleteRole(roleVertex.getPDM(), roleVertex.getRole());
//                    }
//                }
            }
        } else if (e.getActionCommand().equals(POPUP_ADD_REL)) {
            // todo
            showFeatureNotImplemented();
        } else if (e.getActionCommand().equals(POPUP_DELETE_REL)) {
            // todo
            showFeatureNotImplemented();
        }
    }
}
