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

import org.acre.visualizer.graphimpl.jgraph.pdmmodel.PDMModelGraphImpl;
import org.acre.visualizer.graph.graph.*;
import org.acre.visualizer.graph.graph.edges.AcreEdge;
import org.acre.visualizer.graph.graph.vertex.AcreVertex;
import org.acre.visualizer.ui.components.Editor;
import org.jgraph.JGraph;
import org.jgraph.event.GraphModelEvent;
import org.jgraph.event.GraphModelListener;
import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.event.GraphSelectionListener;
import org.jgraph.graph.*;
import org.jgraph.layout.*;
import org.jgraph.util.JGraphUtilities;

import javax.swing.ToolTipManager;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * @author Deepak.Alur@Sun.com
 * @version Dec 20, 2004 4:15:20 PM
 */
public abstract class AcreGraphImpl extends
        JGraph implements AcreGraph {

    private int layoutType = DEFAULT_LAYOUT;
    private ArrayList hiddenCells = new ArrayList();
    Editor editor;
    ArrayList selectionListeners = new ArrayList();

    public AcreGraphImpl(GraphModel model) {
        super(model);
    }

    public AcreGraphImpl(GraphModel model, GraphLayoutCache view) {
        super(model, view);
    }

    public AcreGraphImpl(GraphModel model, GraphLayoutCache layoutCache, BasicMarqueeHandler mh) {
        super(model, layoutCache, mh);
    }

    public AcreGraphImpl() {
        super();
    }

    public AcreGraphImpl(GraphModel model, BasicMarqueeHandler mh) {
        super(model, mh);
    }

    protected void initialize() {

        SalsaMarqueeHandler marquee = new SalsaMarqueeHandler(this);

        setMarqueeHandler(marquee);
        setSalsaCellViewFactory();

        setLayoutType(DEFAULT_LAYOUT);
        setSizeable(true);
        setEditable(false);
        setPortsVisible(false);
        // JGraph 5.2 setSelectClonedCells(true);
        setSelectionEnabled(true); // JGraph 5.3
        getGraphLayoutCache().setSelectsAllInsertedCells(false);
        setMoveable(true);
        setGridSize(10.0d);
        setGridVisible(true);
        setEditClickCount(2);
        setMinimumSize(DEFAULT_GRAPH_SIZE);

        //MouseListener that Prints the Cell on Doubleclick
        // removed mouse listener & selection listner
        // because they interfere with selection behavior=
        //addMouseListener(marquee);
        addGraphSelectionListener(new SalsaSelectionListener());

        getModel().addGraphModelListener(new ModelListener());

        // Add an Instance to the View
        getGraphLayoutCache().addObserver(new SalsaViewObserver());
        ToolTipManager.sharedInstance().registerComponent(this);


    }

    private void setSalsaCellViewFactory() {

        getGraphLayoutCache().setFactory(new SalsaCellViewFactory());

        //getGraphLayoutCache().setRememberCellViews(true);
    }

    public void applyLayout(int layoutType) {

        JGraphLayoutAlgorithm algorithm = null;

        Object[] cells = getDescendants(getRoots());

        if ((cells == null) || (cells.length == 0))
            return;

        switch (layoutType) {
            case ANNEALING_LAYOUT:
                AnnealingLayoutAlgorithm al = new AnnealingLayoutAlgorithm();

                algorithm = al;
                break;
            case CIRCLE_GRAPH_LAYOUT:
                CircleGraphLayout cl = new CircleGraphLayout();
                algorithm = cl;
                break;
            case GEM_LAYOUT:
                AnnealingLayoutAlgorithm optimizer = new AnnealingLayoutAlgorithm();
                GEMLayoutAlgorithm gem = new GEMLayoutAlgorithm(optimizer);

                algorithm = gem;
                break;
            case MOEN_LAYOUT:
                MoenLayoutAlgorithm moen= new MoenLayoutAlgorithm();
                moen.setLayoutOrientation(1);
                algorithm = moen;
                break;
            case RADIAL_TREE_LAYOUT:
                RadialTreeLayoutAlgorithm radial = new RadialTreeLayoutAlgorithm();
                algorithm = radial;
                break;
            case SPRING_EMBEDDED_LAYOUT:
                SpringEmbeddedLayoutAlgorithm spring = new SpringEmbeddedLayoutAlgorithm();
                spring.setFrame(new Rectangle(800,600));
//                spring.setMaxIterations(25);
//                spring.setFrame(new Rectangle(50, 50));
                algorithm = spring;
                break;
            case SUGIYAMA_LAYOUT:
                SugiyamaLayoutAlgorithm sugi = new SugiyamaLayoutAlgorithm();
                sugi.setVertical(false);
                sugi.setSpacing(new Point(
                        AcreGraphConstants.DEFAULT_VERTEX_WIDTH
                        +
                        AcreGraphConstants.DEFAULT_VERTEX_SPACE_X
                        ,
                        AcreGraphConstants.DEFAULT_VERTEX_HEIGHT
                        +
                        AcreGraphConstants.DEFAULT_VERTEX_SPACE_Y
                        ));
                algorithm = sugi;

                break;
            case TREE_LAYOUT:
                SalsaTreeLayoutAlgorithm treeAlg = new SalsaTreeLayoutAlgorithm();
                treeAlg.setCenterRoot(true);
                treeAlg.setCombineLevelNodes(false);

                treeAlg.setNodeDistance(
                        AcreGraphConstants.DEFAULT_VERTEX_SPACE_X * 2
//                        +
//                        AcreGraphConstants.DEFAULT_VERTEX_WIDTH
                   );
                treeAlg.setLevelDistance(
                        AcreGraphConstants.DEFAULT_VERTEX_SPACE_Y
                        //AcreGraphConstants.DEFAULT_VERTEX_HEIGHT
                    );
                algorithm = treeAlg;
                break;
            default:
                break;
        }

        if (algorithm != null) {
            try {
                algorithm.run(this, cells, null);
            } catch (Throwable e) {
//                System.out.println("Exception occurred in applying layout");
                throw new AcreGraphException("Exception occurred in applying layout", e);
            }
        }

    }

    public void applyLayout() {
        applyLayout(getLayoutType());
    }

    private int getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
    }

    public void hideSelectedCells() {
        Object[] cells = this.getSelectionCells();
        if ((cells == null) || (cells.length == 0)) {
            return;
        }

        hiddenCells = new ArrayList();
        for (int i=0; i < cells.length; i++) {
            hiddenCells.add(cells[i]);
        }

//        if (! getGraphLayoutCache().isPartial()) {
//            GraphLayoutCache cache = new GraphLayoutCache(
//                    getModel(),
//                    new SalsaCellViewFactory(),
//                    true);
//            setGraphLayoutCache(cache);
//        }
        GraphLayoutCache cache = new GraphLayoutCache(
                    getModel(),
                    new SalsaCellViewFactory(),
                    true);
        cache.setVisible(cells, false);
    }

    public void showHiddenCells() {
        if ((hiddenCells == null) || (hiddenCells.size() == 0)) {
                return;
        }

        Object [] cells = hiddenCells.toArray();
        GraphLayoutCache cache = new GraphLayoutCache(getModel(), getGraphLayoutCache().getFactory(), true);
        cache.setVisible(cells, true);

        //getGraphLayoutCache().setVisible(vertex, true);
    }

    public void zoomIn(Point point) {
        scrollPointToVisible(point);
        //toScreen(point);
        zoomIn();
    }

    public void zoomOut(Point point) {
        scrollPointToVisible(point);
        //toScreen(point);
        zoomOut();
    }

    public void zoomRevert() {
        setScale(DEFAULTSCALE);
    }

    public void zoomIn() {
        double newScale = getScale() * (1.0 + SCALEFACTOR);
        setScale(newScale);
    }

    public void zoomOut() {
        double newScale = getScale() / (1.0 + SCALEFACTOR);
        setScale(newScale);
    }

    public String getToolTipText(MouseEvent event) {
        if (event != null) {
            Object cell = getFirstCellForLocation(event.getX(), event.getY());
            String toolTip = null;

            if (cell instanceof AcreGraphObject) {
                AcreGraphObject go = (AcreGraphObject) cell;
                toolTip = go.getToolTipText();
            }

            if (toolTip == null)
                toolTip = convertValueToString(cell);
            return (toolTip != null && toolTip.length() > 0) ? toolTip : "";
        }
        return "";
    }

    public void groupSelected() {
        Object[] cells = this.getSelectionCells();
        if ((cells ==null) || (cells.length==0))
            return;
        group(cells);
    }

    public void ungroupSelected() {
        Object[] cells = this.getSelectionCells();
        if ((cells ==null) || (cells.length==0))
            return;
        ungroup(cells);
    }

    public void group(Object [] cells) {
        DefaultGraphCell groupCell = new DefaultGraphCell();
        ArrayList selectCells = new ArrayList();
        for (int i=0; i < cells.length; i++) {
            Object cell = cells[i];
            if (cell instanceof AcreVertex) {
                selectCells.add(cell);
            }
        }
        Object [] sCells = selectCells.toArray();

        graphLayoutCache.insertGroup(groupCell, sCells); // JGraph 5.3
        // JGraph 5.2 JGraphUtilities.group(this, groupCell, cells, true);
    }

    // Returns the total number of vertex in a graph
    protected int getCellCount() {
        Object[] cells = getDescendants(getRoots());
        return cells.length;
    }

    // Ungroup the Groups in Cells and Select the Children
    public void ungroup(Object[] cells) {
        // If any Cells
        if (cells != null && cells.length > 0) {
            // List that Holds the Groups
            ArrayList groups = new ArrayList();
            // List that Holds the Children
            ArrayList children = new ArrayList();
            // Loop Cells
            for (int i = 0; i < cells.length; i++) {
                // If Cell is a Group
                if (isGroup(cells[i])) {
                    // Add to List of Groups
                    groups.add(cells[i]);
                    // Loop Children of Cell
                    for (int j = 0;
                         j < getModel().getChildCount(cells[i]);
                         j++) {
                        // Get Child from Model
                        Object child = getModel().getChild(cells[i], j);
                        // If Not Port
                        if (!(child instanceof Port))
                        // Add to Children List
                            children.add(child);
                    }
                }
            }
            // Remove Groups from Model (Without Children)
            getGraphLayoutCache().remove(groups.toArray());
            // Select Children
            setSelectionCells(children.toArray());
        }
    }

    // Determines if a Cell is a Group
    public boolean isGroup(Object cell) {
        // Map the Cell to its View
        CellView view = getGraphLayoutCache().getMapping(cell, false);
        if (view != null)
            return !view.isLeaf();
        return false;
    }

    // Brings the Specified Cells to Front
    public void toFront(Object[] c) {
        getGraphLayoutCache().toFront(c);
    }

    // Sends the Specified Cells to Back
    public void toBack(Object[] c) {
        getGraphLayoutCache().toBack(c);
    }

    public void setSalsaGraphModel(AcreGraphModel model) {
        setModel((GraphModel)model);
    }

    public AcreGraphModel getSalsaGraphModel() {
        return (AcreGraphModel) getModel();
    }

    public class ModelListener implements GraphModelListener {
        public void graphCellsChanged(GraphModelEvent e) {
//            System.out.println("Model Listener Cell Change: " + e.getChange());
        }

        public void graphChanged(GraphModelEvent event) {
//            System.out.println("Model Listener Graph Change:" + event.getChange());
        }
    }

    // Define a Selection Listener
    public class SalsaSelectionListener implements GraphSelectionListener {
        public void valueChanged(GraphSelectionEvent e) {
            // todo - what?
            Object [] cells = e.getCells();
//            System.out.println("Selection changed: " + e);
            if (e.getSource() instanceof PDMModelGraphImpl) {
//                System.out.println("Selected Cell in Event = : " + e.getCell());
                notifyListeners(e);
                PDMModelGraphImpl d = (PDMModelGraphImpl) e.getSource();
//                System.out.println("Selected Cell on Diagram = " + d.getSelectionCell());
            }
        }

        private void notifyListeners(GraphSelectionEvent e) {
            Object[] cells = e.getCells();
            Object[] gcells = getSelectionCells();
            cells = getNewSelectionOnly(e);
            if (gcells.length == 0) {
                notifyNoSelection();
            } else
            if ((gcells.length == 1) && (cells.length == 1)) {
                Object cell = cells[0];

                if (cell instanceof AcreEdge) {
                    notifyEdgeSelection((AcreEdge) cell);
                } else if (cell instanceof AcreVertex) {
                    notifyVertexSelection((AcreVertex)cell);
                }
            }
            else {
                notifyMultipleSelection(e.getCells());
            }
        }

        private void notifyNoSelection() {
            for (int i=0; i < selectionListeners.size(); i++) {
                AcreGraphSelectionListener l = (AcreGraphSelectionListener) selectionListeners.get(i);
                l.noneSelected();
            }
        }

        private Object[] getNewSelectionOnly(GraphSelectionEvent e) {
            Object [] cells = e.getCells();
            ArrayList sel = new ArrayList();
            for (int i=0; i < cells.length; i++) {
                if (e.isAddedCell(cells[i])) {
                    sel.add(cells[i]) ;
                }
            }
            return sel.toArray();
        }

        private void notifyMultipleSelection(Object[] cells) {
            for (int i=0; i < selectionListeners.size(); i++) {
                AcreGraphSelectionListener l = (AcreGraphSelectionListener) selectionListeners.get(i);
                l.multipleSelected(cells);
            }
        }

        private void notifyVertexSelection(AcreVertex acreVertex) {
            for (int i=0; i < selectionListeners.size(); i++) {
                AcreGraphSelectionListener l = (AcreGraphSelectionListener) selectionListeners.get(i);
                l.vertexSelected(acreVertex);
            }
        }

        private void notifyEdgeSelection(AcreEdge acreEdge) {
            for (int i=0; i < selectionListeners.size(); i++) {
                AcreGraphSelectionListener l = (AcreGraphSelectionListener) selectionListeners.get(i);
                l.edgeSelected(acreEdge);
            }
        }
    }

    // Define an Observer
    public class SalsaViewObserver implements Observer {
        public void update(Observable o, Object arg) {
            //System.out.println("View changed: " + o);
        }
    }


    public void removeAllCells() {
        Object [] cells = getDescendants(getRoots());

        if ((cells == null) || (cells.length == 0))
            return;

        getModel().remove(cells);
    }

    public Component getView() {
        return this;
    }

    public Editor getEditor() {
        return editor;
    }

    public void setEditor(Editor editor) {
        this.editor = editor;
    }

    public void selectAll() {
        Object [] cells = getDescendants(getRoots());
        setSelectionCells(cells);
    }

    public void addSelectionListener(AcreGraphSelectionListener l) {
        selectionListeners.add(l);
    }

    public void removeSelectionListeners() {
        selectionListeners.clear();
    }

    public AcreGraphSelectionListener [] getSelectionListeners() {
        AcreGraphSelectionListener [] ls = new AcreGraphSelectionListener[selectionListeners.size()];
        selectionListeners.toArray(ls);
        return ls;
    }
}
