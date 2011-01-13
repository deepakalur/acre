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

package org.acre.visualizer.ui.pqleditor;

import org.acre.visualizer.graph.AcreGraphConstants;
import org.acre.visualizer.graph.AcreGraphIconUtils;
import org.acre.lang.pql.pdbc.PQLValueHolder;
import org.acre.visualizer.ui.AcreUIUtils;
import org.acre.visualizer.ui.components.AcreTreePanel;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.awt.Component;
import java.util.*;

/**
 * @author Deepak Alur, Syed Ali
 *         Date: Jun 8, 2004
 */
public class PQLDataDetailTreePanel extends AcreTreePanel {

    public PQLDataDetailTreePanel() {
        super();
        initialize();
        clear();
    }

    public void setData(PQLValueHolder data) {
        setModel(new DataTreeModel(data));
    }

    public void clear() {
        setModel(null);
    }

    private void initialize() {
        setBorder(new javax.swing.border.EtchedBorder());
        setCellRenderer(new PQLDataDetailTreeRenderer());
    }

    private static class DataTreeModel implements TreeModel {

        PQLValueHolder data;
        List relList = new ArrayList();
        Map relsMap = new HashMap();

        public DataTreeModel(PQLValueHolder data) {
            this.data = data;
            for (Iterator iter = data.getRelationshipNames(); iter.hasNext();) {
                String element = (String) iter.next();
                relList.add(element);
                relsMap.put(element, data.getRelationship(element));
            }
        }

        public Object getRoot() {
            return data;
        }

        public int getChildCount(Object parent) {
            int count = 0;
            if (parent instanceof PQLValueHolder) {
                for (Iterator iter = ((PQLValueHolder) parent).getRelationshipNames(); iter.hasNext();) {
                    String element = (String) iter.next();
                    count++;
                }
            } else if (parent instanceof String && relsMap.get(parent) != null) {
                count = ((PQLValueHolder[]) relsMap.get(parent)).length;
            }
            return count;
        }

        public boolean isLeaf(Object node) {
            if (node instanceof PQLValueHolder) {
                return false;
            } else if (node instanceof String && relsMap.get(node) != null) {
                return false;
            }
            return true;
        }

        public void addTreeModelListener(TreeModelListener l) {
            // TODO Auto-generated method stub

        }

        public void removeTreeModelListener(TreeModelListener l) {
            // TODO Auto-generated method stub

        }

        public Object getChild(Object parent, int index) {
            if (parent == data) {
                return relList.get(index);
            } else if (parent instanceof String && relsMap.get(parent) != null) {
                return ((PQLValueHolder[]) relsMap.get(parent))[index].getName();
            }
            return null;
        }

        public int getIndexOfChild(Object parent, Object child) {
            return 0;
        }

        public void valueForPathChanged(TreePath path, Object newValue) {
            // TODO Auto-generated method stub
        }
    }

    private class PQLDataDetailTreeRenderer extends DefaultTreeCellRenderer {

        public PQLDataDetailTreeRenderer() {
            super();
        }

        public Component getTreeCellRendererComponent(JTree tree,
                                                      Object value,
                                                      boolean sel,
                                                      boolean expanded,
                                                      boolean leaf,
                                                      int row,
                                                      boolean hasFocus) {

            super.getTreeCellRendererComponent(tree, value, sel,
                    expanded, leaf, row,
                    hasFocus);
            //System.out.println("TreeCellRendered:" + value + "row = " + row);

            setSalsaIcon(value);

            if (leaf || row == 0) {
                setIcon(AcreGraphIconUtils.getCellIcon(AcreGraphConstants.CLASS));
                setToolTipText(value.toString());
            } else {
                setToolTipText(null); //no tool tip
            }

            return this;
        }

        
        private void setSalsaIcon(Object value) {

            Icon icon = null;
            //System.out.println("Setting Icon for : " + value);
            if (value == null)
                return;

            String nodeValue = value.toString();

            if (nodeValue == null)
                return;

            icon = AcreUIUtils.getPQLTreeIcon(nodeValue);

            if (icon != null)
                this.setIcon(icon);

        }
    }


}
