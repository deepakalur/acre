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

import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphModel;
import org.jgraph.layout.JGraphLayoutAlgorithm;

import javax.swing.SwingConstants;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.*;

/**
 * TODO:COMMENT ME!
 *
 * @author krueger
 */
public class SalsaTreeLayoutAlgorithm extends JGraphLayoutAlgorithm {


    protected int alignment = SwingConstants.TOP;
    protected int orientation = SwingConstants.NORTH;
    protected int levelDistance = 30;
    protected int nodeDistance = 20;
    protected boolean centerRoot = true;
    protected boolean combineLevelNodes = true;

    protected JGraph graph;

    protected Map cell2node = new HashMap(); //CellView -> TreeNode

    /**
     * Returns the name of this algorithm in human
     * readable form.
     */
    public String toString() {
        return "Tree Layout";
    }

    /**
     * Get a human readable hint for using this layout.
     */
    public String getHint() {
        return "Select a root node";
    }
//
//    public JGraphLayoutSettings createSettings() {
//        return new TreeLayoutSettings(this);
//    }

    /**
     * SwingConstants.TOP
     * SwingConstants.CENTER
     * SwingConstants.BOTTOM
     * @param alignment
     */
    public void setAlignment(int alignment) {
        if (alignment != SwingConstants.TOP
            && alignment != SwingConstants.CENTER
            && alignment != SwingConstants.BOTTOM) {
            throw new IllegalArgumentException("Alignment must be one of TOP, CENTER or BOTTOM"); //$NON-NLS-1$
        }

        this.alignment = alignment;
    }

    /**
     * SwingConstants.NORTH
     * SwingConstants.EAST
     * SwingConstants.SOUTH
     * SwingConstants.WEST
     * @param orientation
     */
    public void setOrientation(int orientation) {
        if (orientation != SwingConstants.NORTH
            && orientation != SwingConstants.EAST
            && orientation != SwingConstants.SOUTH
            && orientation != SwingConstants.WEST) {
            throw new IllegalArgumentException("Orientation must be one of NORTH, EAST, SOUTH or WEST");
        }
        this.orientation = orientation;
    }

    public void setLevelDistance(int distance) {
        levelDistance = distance;
    }

    public void setNodeDistance(int distance) {
        nodeDistance = distance;
    }

    public void setCenterRoot(boolean b) {
        centerRoot = b;
    }

    /*
     * @param graph JGraph instance
     * @param dynamic_cells List of all nodes the layout should move
     * @param static_cells List of node the layout should not move but allow for
     */
    public void run(JGraph graph, Object[] dynamic_cells, Object[] static_cells) {
        this.graph = graph;
        CellView[] selectedCellViews =
            graph.getGraphLayoutCache().getMapping(new Object[]{dynamic_cells[0]});

        List roots = Arrays.asList(selectedCellViews);

        for (Iterator it = roots.iterator(); it.hasNext();) {
            if (!(it.next() instanceof CellView)) {
                it.remove();
            }
        }

        roots = buildTrees(roots);

        layoutTrees(roots);

        if (combineLevelNodes) {
            setLevelHeights(roots);
        }

        setPosition(roots);
    }

    /* Building Tree */

    protected List buildTrees(List roots) {
        List l = new ArrayList();
        for (Iterator it = roots.iterator(); it.hasNext();) {
            l.add(buildTree((CellView) it.next()));
        }
        return l;
    }

    protected TreeNode buildTree(CellView view) {
        List children = getChildren(view);
        TreeNode node = getTreeNode(view);

        for (Iterator it = children.iterator(); it.hasNext();) {
            CellView c = (CellView) it.next();
            TreeNode n = buildTree(c);
             node.children.add(n);
        }

        return node;
    }

    protected List getChildren(CellView view) {
        ArrayList children = new ArrayList();
        GraphModel model = graph.getModel();
        Object cell = view.getCell();

        for (int i = 0; i < model.getChildCount(cell); i++) {
            Object port = model.getChild(cell, i);

            for (Iterator edges = model.edges(port); edges.hasNext();) {
                Object edge = edges.next();

                if (port == model.getSource(edge)) {
                    Object targetPort = model.getTarget(edge);
                    Object targetVertex = model.getParent(targetPort);
                    children.add(graph.getGraphLayoutCache().getMapping(targetVertex, false));
                }
            }
        }

        return children;
    }

    protected TreeNode getTreeNode(CellView view) {
        Object o = cell2node.get(view);
        if (o != null) {
            return (TreeNode) o;
        }

        TreeNode node = new TreeNode(view);
        cell2node.put(view, node);
        return node;
    }

    /* Layout trees */

    protected void layoutTrees(List roots) {
        for (Iterator it = roots.iterator(); it.hasNext();) {
            layout((TreeNode) it.next());
        }
    }

    protected void layout(TreeNode node) {
        if (node.children.size() == 0) {
            //do nothing
        } else if (node.children.size() == 1) {
            TreeNode sub = (TreeNode) node.children.get(0);
            sub.depth = node.depth + 1;
            layout(sub);

            sub.leftContour.dx = (sub.width - node.width) / 2;
            sub.rightContour.dx = (sub.width - node.width) / 2;
            node.leftContour.next = sub.leftContour;
            node.rightContour.next = sub.rightContour;
        } else {
            for (Iterator it = node.children.iterator(); it.hasNext();) {
                TreeNode n = (TreeNode) it.next();
                n.depth = node.depth + 1;
                layout(n);
            }

            join(node);
        }
    }

    /* Joining trees */

    protected void join(TreeNode node) {
        int distance = 0;
        for (int i = 0; i < node.children.size(); i++) {
            TreeNode n1 = (TreeNode) node.children.get(i);

            for (int j = i + 1; j < node.children.size(); j++) {
                TreeNode n2 = (TreeNode) node.children.get(j);
                int dist = distance(n1.rightContour, n2.leftContour) / (j - i);
                distance = Math.max(distance, dist);
            }

        }

        distance += nodeDistance;

        //set relative position
        int left;
        if (node.children.size() % 2 == 0) {
            left = (node.children.size() / 2 - 1) * distance + distance / 2;
        } else {
            left = node.children.size() / 2 * distance;
        }

        Iterator it = node.children.iterator();
        for (int i = 0; it.hasNext(); i++) {
            ((TreeNode) it.next()).x = -left + i * distance;
        }

        //new contour
        TreeNode first = getLeftMostX(node);
        TreeNode last = getRightMostX(node);

        // deepak- i think if the node.right/left == first/last.left/right, it should be set to null
        node.leftContour.next = first.leftContour;
        node.rightContour.next = last.rightContour;

        for (int i = 1; i < node.children.size(); i++) {
            TreeNode n = (TreeNode) node.children.get(i);
            merge(node.leftContour.next, n.leftContour, i * distance + node.width);
        }

        for (int i = node.children.size() - 2; i >= 0; i--) {
            TreeNode n = (TreeNode) node.children.get(i);
            merge(node.rightContour.next, n.rightContour, i * distance + node.width);
        }

        distance = (node.children.size() - 1) * distance / 2;

        node.leftContour.next.dx += distance - node.width / 2;
        node.rightContour.next.dx += distance - node.width / 2;
    }

    protected TreeNode getLeftMostX(TreeNode node)
    {
          int tmp = Integer.MAX_VALUE;
          TreeNode mostLeft = null;
          Iterator iter = node.getChildren();
          while (iter.hasNext())
          {
             TreeNode child = (TreeNode) iter.next();

             int leftPos = child.x - child.getLeftWidth();
             if (leftPos < tmp)
             {
                mostLeft = child;
                tmp = leftPos;
             }
          }
          return (mostLeft != null) ? mostLeft : (TreeNode) node.children.get(0);
    }

    protected TreeNode getRightMostX(TreeNode node)
    {
          int tmp = Integer.MIN_VALUE;

          TreeNode mostRight = null;
          Iterator iter = node.getChildren();
          while (iter.hasNext())
          {
             TreeNode child = (TreeNode) iter.next();

             int rightPos = child.x + child.getRightWidth();
             if (rightPos > tmp)
             {
                mostRight = child;
                tmp = rightPos;
             }
          }
          return (mostRight != null) ? mostRight : (TreeNode) node.children.get(0);
     }

     protected void merge(PolyLine main, PolyLine left, int distance) {

        while (main != null) {

            if (main == left) { // deepak
                return; // deepak
            }

            if (main.next == main) { // deepak
                return; // deepak
            }

            if (left.next == null) {
                return;
            }

            if (main.next == null) {
                left = left.next;
                break;
            }

            distance += main.dx - left.dx;
            main = main.next;
            left = left.next;
        }

        left.dx += -distance;
        main.next = left;
    }

    protected int distance(PolyLine right, PolyLine left) {
        int distance = 0;

        for (int i = 0; right != null && left != null;) {
            i += right.dx + left.dx;
            if (i > 0) {
                distance += i;
                i = 0;
            }

            right = right.next;
            left = left.next;
        }

        return distance;
    }

    /* Positioning */

    protected void setPosition(List roots) {
        for (Iterator it = roots.iterator(); it.hasNext();) {
            ((TreeNode) it.next()).setPosition(null, 0);
        }
    }

    protected void setLevelHeights(List roots) {
        for (Iterator it = roots.iterator(); it.hasNext();) {
            List level = ((TreeNode) it.next()).getNodesByLevel();

            int max = 0;
            for (int i = 0; i < level.size(); i++) {
                List l = (List) level.get(i);

                for (int j = 0; j < l.size(); j++) {
                    max = Math.max(max, ((TreeNode) l.get(j)).height);
                }

                for (int j = 0; j < l.size(); j++) {
                    ((TreeNode) l.get(j)).levelheight = max;
                }

                max = 0;
            }
        }
    }

    protected class TreeNode {
        List children;
        int width;
        int height;
        int x;
        int y;
        int levelheight;
        PolyLine leftContour;
        PolyLine rightContour;
        int depth;

        CellView view;

        public TreeNode(CellView view) {
            this.view = view;
            if (orientation == SwingConstants.NORTH || orientation == SwingConstants.SOUTH) {
                width = (int) view.getBounds().getWidth();
                height = (int) view.getBounds().getHeight();
            } else {
                width = (int) view.getBounds().getHeight();
                height = (int) view.getBounds().getWidth();
            }
            this.children = new ArrayList();
            this.leftContour = new PolyLine(width / 2);
            this.rightContour = new PolyLine(width / 2);
            this.depth = 0;
        }

        public Iterator getChildren() {
            return children.iterator();
        }

        public int getLeftWidth() {
            int width = 0;

            PolyLine poly = leftContour;
            int tmp = 0;
            while(poly != null) {
                tmp += poly.dx;
                if(tmp > 0) {
                    width += tmp;
                    tmp = 0;
                }
                if (poly != poly.next) { // deepak
                    poly = poly.next;
                } else { // deepak
                    break; // deepak
                } // deepak
            }

            return width;
        }

        public int getRightWidth() {
            int width = 0;

            PolyLine poly = rightContour;
            int tmp = 0;
            while(poly != null) {
                tmp += poly.dx;
                if(tmp > 0) {
                    width += tmp;
                    tmp = 0;
                }
                if (poly != poly.next) {   // deepak
                    poly = poly.next;
                } else {     // deepak
                    break;   // deepak
                }          // deepak
            }

            return width;
        }

        public int getHeight() {
            if(children.isEmpty()) {
                return levelheight;
            }

            int height = 0;

            for(Iterator it = children.iterator(); it.hasNext(); ) {
                height = Math.max(height, ((TreeNode) it.next()).getHeight());
            }

            return height + levelDistance + levelheight;
        }

        public void setPosition(Point parent, int levelHeight) {
            int nextLevelHeight = 0;
            for (Iterator it = children.iterator(); it.hasNext();) {
                nextLevelHeight = Math.max(nextLevelHeight, ((TreeNode) it.next()).height);
            }

            if (parent == null) {
                Rectangle2D b = view.getBounds();
                Rectangle bounds = new Rectangle((int) b.getX(),(int) b.getY(), (int) b.getWidth(), (int) b.getHeight());
                Point p = bounds.getLocation();

                if(centerRoot) {
                    int lw = getLeftWidth();
                    int rw = getRightWidth();
                    int h = getHeight();


                    Insets i = graph.getInsets();

                    if(orientation == SwingConstants.NORTH) {
                        bounds.x = lw - width / 2;
                        bounds.y = i.top;
                    } else if (orientation == SwingConstants.EAST) {
                        bounds.x = i.left + h - width;
                        bounds.y = lw - height / 2;
                    } else if (orientation == SwingConstants.SOUTH) {
                        bounds.x = lw - width / 2;
                        bounds.y = i.top + h;
                    } else if (orientation == SwingConstants.WEST) {
                        bounds.x = i.right;
                        bounds.y = lw - width / 2;
                    }

                    Object cell = view.getCell();
                    Map attributes =
                        GraphConstants.createAttributes(cell, GraphConstants.BOUNDS, bounds);
                    graph.getGraphLayoutCache().edit(attributes, null, null, null);

                    if (orientation == SwingConstants.WEST || orientation == SwingConstants.EAST) {
                        graph.setPreferredSize(new Dimension(h + i.left + i.right, lw+rw+ i.top+ i.bottom));
                    } else {
                        graph.setPreferredSize(new Dimension(lw+rw + i.left + i.right, h + i.top + i.bottom));
                    }

                    p = bounds.getLocation();
                }

                if (orientation == SwingConstants.WEST || orientation == SwingConstants.EAST) {
                    int tmp = p.x;
                    p.x = p.y;
                    p.y = tmp;
                }

                if (orientation == SwingConstants.NORTH || orientation == SwingConstants.WEST) {
                    parent = new Point(p.x + width / 2, p.y + height);
                } else if (
                    orientation == SwingConstants.SOUTH || orientation == SwingConstants.EAST) {
                    parent = new Point(p.x + width / 2, p.y);
                }

                for (Iterator it = children.iterator(); it.hasNext();) {
                    ((TreeNode) it.next()).setPosition(parent, nextLevelHeight);
                }

                return;
            }

            if (combineLevelNodes) {
                levelHeight = this.levelheight;
            }

            Rectangle cellBounds = new Rectangle(width, height);


            if (orientation == SwingConstants.NORTH || orientation == SwingConstants.WEST) {
                cellBounds.x = x + parent.x - width / 2;
                cellBounds.y = parent.y + levelDistance;
            } else {
                cellBounds.x = x + parent.x - width / 2;
                cellBounds.y = parent.y - levelDistance - levelheight;
            }

            if (alignment == SwingConstants.CENTER) {
                cellBounds.y += (levelHeight - height) / 2;
            } else if (alignment == SwingConstants.BOTTOM) {
                cellBounds.y += levelHeight - height;
            }

            if (orientation == SwingConstants.WEST || orientation == SwingConstants.EAST) {
                int tmp = cellBounds.x;
                cellBounds.x = cellBounds.y;
                cellBounds.y = tmp;

                tmp = cellBounds.width;
                cellBounds.width = cellBounds.height;
                cellBounds.height = tmp;
            }

            Object cell = view.getCell();
            Map attributes =
                GraphConstants.createAttributes(cell, GraphConstants.BOUNDS, cellBounds);
            graph.getGraphLayoutCache().edit(attributes, null, null, null);

            if (orientation == SwingConstants.NORTH || orientation == SwingConstants.WEST) {
                y = parent.y + levelDistance + levelHeight;
            } else {
                y = parent.y - levelDistance - levelHeight;
            }

            for (Iterator it = children.iterator(); it.hasNext();) {
                ((TreeNode) it.next()).setPosition(new Point(x + parent.x, y), nextLevelHeight);
            }
        }

        public List getNodesByLevel() {
            List level = new ArrayList();
            for (Iterator it = children.iterator(); it.hasNext();) {
                List l2 = ((TreeNode) it.next()).getNodesByLevel();

                if (level.size() < l2.size()) {
                    List tmp = level;
                    level = l2;
                    l2 = tmp;
                }

                for (int i = 0; i < l2.size(); i++) {
                    ((List) level.get(i)).addAll((List) l2.get(i));
                }
            }

            ArrayList node = new ArrayList();
            node.add(this);
            level.add(0, node);

            return level;
        }

    }

    protected class PolyLine {
        int dx;
        PolyLine next;

        public PolyLine(int dx) {
            this.dx = dx;
        }
    }

    /**
     * @return Returns the combineLevelNodes.
     */
    public boolean isCombineLevelNodes() {
        return combineLevelNodes;
    }
    /**
     * @param combineLevelNodes The combineLevelNodes to set.
     */
    public void setCombineLevelNodes(boolean combineLevelNodes) {
        this.combineLevelNodes = combineLevelNodes;
    }
    /**
     * @return Returns the alignment.
     */
    public int getAlignment() {
        return alignment;
    }
    /**
     * @return Returns the centerRoot.
     */
    public boolean isCenterRoot() {
        return centerRoot;
    }
    /**
     * @return Returns the levelDistance.
     */
    public int getLevelDistance() {
        return levelDistance;
    }
    /**
     * @return Returns the nodeDistance.
     */
    public int getNodeDistance() {
        return nodeDistance;
    }
    /**
     * @return Returns the orientation.
     */
    public int getOrientation() {
        return orientation;
    }
}