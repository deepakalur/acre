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
import org.acre.visualizer.graph.AcreGraph;
import org.acre.visualizer.graph.edges.AcreEdge;
import org.acre.visualizer.graph.pdmmodel.PDMModelGraph;
import org.acre.visualizer.graph.pdmmodel.vertex.PDMVertex;
import org.acre.visualizer.graph.vertex.AcreVertex;

import javax.swing.JMenuItem;
import java.awt.event.ActionEvent;

/**
 * @author Deepak.Alur@Sun.com
 * @version Dec 15, 2004 10:00:26 AM
 */
public class PDMVertexPopup
        extends GraphPopup {

    public PDMVertexPopup(AcreGraph graph, PDMVertex cell) {
        super(graph, cell, "PDM");
        initializeMenus();
    }

    // subclasses to override this method if needed
    protected void handleEdgeAction(ActionEvent e, GraphPopup popup, AcreGraph graph, AcreEdge edge) {
        // do nothing. this is not a vertex
    }

    // subclasses to override this method if needed
    protected void handleVertexAction(ActionEvent e, GraphPopup popup, AcreGraph graph, AcreVertex vertex) {

        PDMType pdm = (PDMType) vertex.getAcreData().getData();
        if (e.getActionCommand().equals(POPUP_EXECUTE_PDM)) {
            if (graph instanceof PDMModelGraph) {
                PDMModelGraph pdmModelGraph = (PDMModelGraph) graph;
                if (pdmModelGraph.getPDMEditor() != null)  {
                   pdmModelGraph.getPDMEditor().executeObject(pdm);
                }
            }
        } else if (e.getActionCommand().equals(POPUP_ADD_ROLE)) {
            if (graph instanceof PDMModelGraph) {
                PDMModelGraph pdmModelGraph = (PDMModelGraph) graph;
                if (pdmModelGraph.getPDMEditor() != null) {
//                    if (pdmModelGraph.getPDMEditor() instanceof AcrePDMExplorerPanel) {
//                        AcrePDMExplorerPanel pdmEditor = (AcrePDMExplorerPanel) pdmModelGraph.getPDMEditor();
//                        pdmEditor.addPDMNewRole(pdm);
//                    }
                }
            }
        } else if (e.getActionCommand().equals(POPUP_ADD_PDM)) {
            if (graph instanceof PDMModelGraph) {
                PDMModelGraph pdmModelGraph = (PDMModelGraph) graph;
                if (pdmModelGraph.getPDMEditor() != null)  {
                   pdmModelGraph.getPDMEditor().addObject(null);
                }
            }
        } else if (e.getActionCommand().equals(POPUP_DELETE_PDM)) {
            if (graph instanceof PDMModelGraph) {
                PDMModelGraph pdmModelGraph = (PDMModelGraph) graph;
                if (pdmModelGraph.getPDMEditor() != null)  {
                   pdmModelGraph.getPDMEditor().deleteObject(pdm);
                }
            }
        } else if (e.getActionCommand().equals(POPUP_EDIT_PDM)) {
            if (graph instanceof PDMModelGraph) {
                PDMModelGraph pdmModelGraph = (PDMModelGraph) graph;
                if (pdmModelGraph.getPDMEditor() != null)  {
                   pdmModelGraph.getPDMEditor().editObject(pdm);
                }
            }
        }  else if (e.getActionCommand().equals(POPUP_ADD_REL)) {
            if (graph instanceof PDMModelGraph) {
                PDMModelGraph pdmModelGraph = (PDMModelGraph) graph;
                if (pdmModelGraph.getPDMEditor() != null) {
//                    if (pdmModelGraph.getPDMEditor() instanceof AcrePDMExplorerPanel) {
//                        AcrePDMExplorerPanel pdmEditor = (AcrePDMExplorerPanel) pdmModelGraph.getPDMEditor();
//                        pdmEditor.addPDMNewRelationship(pdm);
//                    }
                }
            }
        }
    }

    protected void initializeMenus() {

        initializeDefaultMenus();
        addSeparator();
        JMenuItem editPDMMenu = addMenu(POPUP_EDIT_PDM, null, this);
        JMenuItem execPDMMenu = addMenu(POPUP_EXECUTE_PDM, null, this);
        JMenuItem addPDMMenu = addMenu(POPUP_ADD_PDM, null, this);
        JMenuItem delPDMMenu = addMenu(POPUP_DELETE_PDM, null, this);
        addSeparator();

        JMenuItem addRoleMenu = addMenu(POPUP_ADD_ROLE, null, this);
        JMenuItem addRelMenu = addMenu(POPUP_ADD_REL, null, this);

        addRoleMenu.setEnabled(false);
        delPDMMenu.setEnabled(false);
        addPDMMenu.setEnabled(false);
        editPDMMenu.setEnabled(false);
        execPDMMenu.setEnabled(false);
        addRelMenu.setEnabled(false);


        // if multiple vertex are selected
        Object [] cells = getGraph().getSelectionCells();
        if ((cells == null) || (cells.length == 0)) {
            addPDMMenu.setEnabled(true);
            return;
        }

        // 1 cell selected
        if (cells.length == 1) {
            if (cells[0] instanceof PDMVertex) {
                // enable delete role
                addRoleMenu.setEnabled(true);
                delPDMMenu.setEnabled(true);
                editPDMMenu.setEnabled(true);
                execPDMMenu.setEnabled(true);
                addRelMenu.setEnabled(true);
            }
        } else {
            // many vertex selected, enable delete only if all vertex are PDMs
            boolean allPDM = true;
            for (int i=0; i < cells.length; i++) {
                Object cell = cells[i];
                if (! (cell instanceof PDMVertex)) {
                    allPDM = false;
                    break;
                }
            }
            if (allPDM) {
               delPDMMenu.setEnabled(true);
               editPDMMenu.setEnabled(false);
               execPDMMenu.setEnabled(false);
            }
        }
    }
}
