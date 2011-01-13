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
import org.acre.visualizer.graph.edges.RelationshipEdge;
import org.acre.visualizer.graph.edges.AcreEdge;
import org.acre.visualizer.graph.pdmmodel.edges.RoleToPDMEdge;
import org.acre.visualizer.graph.vertex.AcreVertex;

import javax.swing.JMenuItem;
import java.awt.event.ActionEvent;

/**
 * @author Deepak.Alur@Sun.com
 * @version Dec 16, 2004 11:49:49 AM
 */
public class RoleToPDMEdgePopup extends GraphPopup {

    public RoleToPDMEdgePopup(AcreGraph  graph, RoleToPDMEdge edge) {
        super(graph, edge, "Role-PDM Relationship");
        initializeMenus();
    }

    protected void initializeMenus() {
        AcreEdge edge = getEdge();
        if (edge != null) {
            if (edge instanceof RelationshipEdge) {
                initializeDefaultMenus();
                addSeparator();
                JMenuItem delRelMenu = addMenu(POPUP_DELETE_REL, null, this);
                delRelMenu.setEnabled(false);

                if (getGraph().getSelectionCount() == 1) {
                    delRelMenu.setEnabled(true);
                }
            }
        }

    }

    // subclasses to override this method if needed
    protected void handleEdgeAction(ActionEvent e, GraphPopup popup, AcreGraph graph, AcreEdge edge) {
        // do nothing. this is not a vertex
    }

    // subclasses to override this method if needed
    protected void handleVertexAction(ActionEvent e, GraphPopup popup, AcreGraph graph, AcreVertex vertex) {
        if (e.getActionCommand().equals(POPUP_DELETE_REL)) {
            // todo
            showFeatureNotImplemented();
        }
    }
}
