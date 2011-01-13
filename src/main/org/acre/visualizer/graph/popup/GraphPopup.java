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
import org.acre.visualizer.graph.vertex.AcreVertex;
import org.acre.visualizer.ui.AcreUIUtils;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * @author Deepak.Alur@Sun.com
 * @version Dec 14, 2004 12:00:29 PM
 */
public abstract class GraphPopup
        extends JPopupMenu
        implements ActionListener, GraphPopupMenuConstants {

    String menuTitle;
    AcreVertex vertex;
    AcreEdge edge;
    AcreGraph graph;
    ArrayList menuItems = new ArrayList();

    private static final String MENU_TITLE = "Graph";


    public AcreGraph getGraph() {
        return graph;
    }

    public void setGraph(AcreGraph graph) {
        this.graph = graph;
    }

    public AcreVertex getVertex() {
        return vertex;
    }

    public void setCell(AcreVertex vertex) {
        this.vertex = vertex;
    }

    public AcreEdge getEdge() {
        return edge;
    }

    public void setEdge(AcreEdge edge) {
        this.edge = edge;
    }

    public GraphPopup() {
        super();
        initialize();
    }

    public GraphPopup(AcreGraph graph, String menuTitle) {
        super();
        this.graph = graph;
        this.menuTitle = menuTitle;
        initialize();
        initializeDefaultMenus();
        addSeparator();
        JMenuItem addPDMMenu = addMenu(POPUP_ADD_PDM, null, this);
    }


    protected void setMenuTitle(String menuTitle) {
        this.menuTitle = menuTitle;
    }

    protected String getMenuTitle() {
        if (menuTitle == null)
            return MENU_TITLE;
        else
            return menuTitle;
    }

    public GraphPopup(AcreGraph graph, AcreVertex vertex, String menuTitle) {
        super(vertex.getVertexName());
        this.vertex = vertex;
        this.graph = graph;
        this.menuTitle = menuTitle;
        initialize();
    }

    public GraphPopup(AcreGraph graph, AcreEdge edge, String menuTitle) {
            super(edge.getEdgeName());
            this.edge = edge;
            this.graph = graph;
        this.menuTitle = menuTitle;
            initialize();
    }

   public JMenuItem addMenu(String menuItem, String iconName, ActionListener listener) {
       ImageIcon icon = null;
       JMenuItem item = null;

       if (iconName != null) {
           icon = AcreUIUtils.createImageIcon("popups/" + iconName);
       }

       if (icon != null) {
           item = new JMenuItem(menuItem);
       } else {
           item = new JMenuItem(menuItem, icon);
       }

       item.addActionListener(listener);
       this.add(item);
       menuItems.add(item);
       return item;
   }

    public void addMenus(String[] menuItems, String[] iconNames,
                         ActionListener listener) {

        if ((menuItems == null) || (menuItems.length == 0))
            return;

        for (int i = 0; i < menuItems.length; i++) {
            if ((iconNames != null) &&
                    (iconNames.length > 0) &&
                    (iconNames.length <= menuItems.length)) {
                addMenu(menuItems[i], iconNames[i], listener);
            } else {
                addMenu(menuItems[i], null, listener);
            }
        }
    }

    private void initialize() {
        setBorder(BorderFactory.createRaisedBevelBorder());
    }

    protected void addTitle(String s) {
        setLabel(s);
        JMenuItem title = new JMenuItem(getLabel());
        title.setEnabled(false);
        add(title);
        addSeparator();
    }

    protected void initializeDefaultMenus() {

       String[] menuItems = {
            POPUP_ZOOM_IN,
            POPUP_ZOOM_OUT,
            POPUP_ZOOM_REVERT,
            POPUP_GROUP_SEL,
            POPUP_UNGROUP_SEL,
            POPUP_SELECT_ALL
//            POPUP_HIDE_SEL,
//            POPUP_UNHIDE,
        };

        String[] iconNames = {
            // todo
        };

        addTitle(getMenuTitle());
        addSeparator();
        addMenus(menuItems, iconNames, this);
    }

    final public void actionPerformed(ActionEvent e) {

        if (graph == null) {
            return;
        } else {

            boolean handled = handleGraphAction(e, this, graph);

            if (handled)
                return;

            if ((edge == null) && (vertex == null)) {
                // if this is a graph popup menu, then edge/vertex will be null
                handleGraphAction(e, this, graph);
            } else if ((edge == null) && (vertex != null)) {
                // this is a vertex popup menu
                handleVertexAction(e, this, graph, vertex);
            } else if ((edge != null) && (vertex == null)) {
                // this is an edge popup menu
                handleEdgeAction(e, this, graph, edge);
            } else {
                handleGraphAction(e, this, graph);
            }
        }
    }

    // subclasses to override this method if needed
    protected abstract void handleEdgeAction(ActionEvent e, GraphPopup popup, AcreGraph graph, AcreEdge edge);

    // subclasses to override this method if needed
    protected abstract void handleVertexAction(ActionEvent e, GraphPopup popup, AcreGraph graph, AcreVertex vertex);

    protected boolean handleGraphAction(ActionEvent e, GraphPopup popup, AcreGraph graph) {

        if (e.getActionCommand().equals(POPUP_SELECT_ALL)) {
                    graph.selectAll();
        } else if (e.getActionCommand().equals(POPUP_GROUP_SEL)) {
            graph.groupSelected();
        } else if (e.getActionCommand().equals(POPUP_UNGROUP_SEL)) {
            graph.ungroupSelected();
        } else if (e.getActionCommand().equals(POPUP_ZOOM_IN)) {
            graph.zoomIn();
        } else if (e.getActionCommand().equals(POPUP_ZOOM_OUT)) {
            graph.zoomOut();;
        } else if (e.getActionCommand().equals(POPUP_ZOOM_REVERT)) {
            graph.zoomRevert();
        } else if (e.getActionCommand().equals(POPUP_HIDE_SEL)) {
            graph.hideSelectedCells();
        } else if (e.getActionCommand().equals(POPUP_UNHIDE)) {
            graph.showHiddenCells();
        } else if (e.getActionCommand().equals(POPUP_ADD_PDM)) {
            if (graph instanceof PDMModelGraph) {
                PDMModelGraph pdmModelGraph = (PDMModelGraph) graph;
                if (pdmModelGraph.getPDMEditor() != null)  {
                   pdmModelGraph.getPDMEditor().addObject(null);
                }
            }
        } else {
            return false;
        }

        return true;
    }

    public void addActionListener(ActionListener l) {
        this.addActionListener(l);
        for (int i=0; i < menuItems.size(); i++) {
            JMenuItem item = (JMenuItem) menuItems.get(i);
            item.addActionListener(l);
        }
    }

    protected void showFeatureNotImplemented() {
        AcreUIUtils.showFeatureNotImplemented(this);
    }

    protected abstract void initializeMenus();

}
