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
package org.acre.visualizer.graphimpl.jgraph;

import org.acre.pdm.PDMType;
import org.acre.pdmqueries.QueryType;
import org.acre.common.AcreStringUtil;
import org.acre.dao.DAOFactory;
import org.acre.dao.PDMXMLConstants;
import org.acre.visualizer.graph.graph.AcreGraph;
import org.acre.visualizer.graph.graph.pdmmodel.PDMModelGraph;
import org.acre.visualizer.graph.graph.pdmmodel.vertex.PDMVertex;
import org.acre.visualizer.graph.graph.pdmmodel.vertex.RoleVertex;
import org.acre.visualizer.graph.graph.popup.GraphPopupFactory;
import org.acre.visualizer.ui.pdmcomposer.AcrePDMExplorerPanel;
import org.jgraph.graph.BasicMarqueeHandler;
import org.jgraph.graph.PortView;

import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author Deepak.Alur@Sun.com
 * @version Dec 14, 2004 4:53:57 PM
 */
public class SalsaMarqueeHandler extends BasicMarqueeHandler
 implements MouseListener {

    private AcreGraph graph;
    protected Point start, current;
    protected PortView port, firstPort, lastPort;
    protected transient JToggleButton buttonSelect = new JToggleButton();
    protected transient JToggleButton buttonEdge = new JToggleButton();


    public SalsaMarqueeHandler(AcreGraph g) {
        super();
        this.graph = g;
    }


    public AcreGraph getGraph() {
        return graph;
    }

    public void setGraph(AcreGraph graph) {
        this.graph = graph;
    }

    protected boolean isPopupTrigger(MouseEvent e) {
        if (e == null) return false;
        //return SwingUtilities.isRightMouseButton(e) && e.isControlDown();
        return SwingUtilities.isRightMouseButton(e);
    }

    public void mouseClicked(MouseEvent e) {
        //do nothing
        Object cell = graph.getFirstCellForLocation(e.getX(), e.getY());
        System.out.println("clicked on cell: " + cell);
        if (e.getClickCount() == 2) {
            System.out.println("double clicked on cell: " + cell);
            handleDoubleClick(e);
        }
    }

    public void mouseEntered(MouseEvent e) {
        //do nothing
    }

    public void mouseExited(MouseEvent e) {
        //do nothing
    }

    public void mousePressed(MouseEvent event) {
        if (event.getClickCount() == 2) {
            handleDoubleClick(event);
        }
        super.mousePressed(event);
    }

    private void handleDoubleClick(MouseEvent e) {
        Object cell = graph.getFirstCellForLocation(e.getX(), e.getY());
        if (cell instanceof PDMVertex) {
            handlePDMVertexDoubleClick((PDMVertex)cell);
        }
        else if (cell instanceof RoleVertex) {
            handlePDMRoleVertexDoubleClick((RoleVertex) cell);
        }
    }

    private void handlePDMRoleVertexDoubleClick(RoleVertex roleVertex) {
        if (roleVertex.getRole().getType().equals(PDMXMLConstants.ROLE_TYPE_PDM)) {
            // role is a PDM, edit PDM or view PDM
            if (graph instanceof PDMModelGraph) {
                PDMModelGraph pdmGraph = (PDMModelGraph) graph;
                String pdmName = roleVertex.getRole().getTypeReferenceName();
                if (pdmName == null)
                    pdmName = roleVertex.getRole().getName();

                pdmName = AcreStringUtil.getRoleNameBeginning(pdmName);
                PDMType pdm = DAOFactory.getPatternRepository().getGlobalPatternModel(pdmName);
                if (pdm != null)
                    pdmGraph.getPDMEditor().viewObject(pdm);
            }
        }
        else if (roleVertex.getRole().getType().equals(PDMXMLConstants.ROLE_TYPE_QUERY)) {
            // role is a Query, edit query or view Query
            if (graph instanceof PDMModelGraph) {
                PDMModelGraph pdmGraph = (PDMModelGraph) graph;
                String queryName = roleVertex.getRole().getQueryName();
                if (queryName == null)
                    queryName = roleVertex.getRole().getName();
                QueryType query;
                query = DAOFactory.getPatternQueryRepository().getGlobalQuery(queryName);

                if (query == null) {
                    // this is a scripted pdm
                    queryName = roleVertex.getPDM().getScriptedPDMPath();
                    query = DAOFactory.getPatternQueryRepository().getGlobalQuery(queryName);
                }
                if (query != null) {
                    if (pdmGraph.getPDMEditor() instanceof AcrePDMExplorerPanel) {
                        AcrePDMExplorerPanel p = (AcrePDMExplorerPanel) pdmGraph.getPDMEditor();
                        if (p.getQueryEditor() != null)
                            p.getQueryEditor().viewObject(query);
                    }
                }
            }
        }
    }

    private void handlePDMVertexDoubleClick(PDMVertex pdmVertex) {
        String pdmName = pdmVertex.getPDM().getName();
        if (graph instanceof PDMModelGraph) {
            PDMModelGraph pdmGraph = (PDMModelGraph) graph;
            PDMType pdm = DAOFactory.getPatternRepository().getGlobalPatternModel(pdmName);
            if (pdm != null)
                pdmGraph.getPDMEditor().editObject(pdm);
        }
    }

    public void mouseReleased(MouseEvent event) {
        //super.mouseReleased(event);
        if (isPopupTrigger(event)) {
            showPopup(event);
        } else {
            super.mouseReleased(event);
        }
    }

    private void showPopup(MouseEvent event) {
        Object cell = graph.getFirstCellForLocation(event.getX(), event.getY());

        JPopupMenu p = null;

        if (isPopupTrigger(event)) {
            p = GraphPopupFactory.createPopup(graph, cell);
            if (p != null)
                p.show((Component) graph, event.getX(), event.getY());
        }
        event.consume();
    }

    public boolean isForceMarqueeEvent(MouseEvent event) {
        if (isPopupTrigger(event))
            return true;

        if (event.getClickCount() == 2)
            return true;

        return super.isForceMarqueeEvent(event);
    }

}

