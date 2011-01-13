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

import org.acre.pdmqueries.ArgumentType;
import org.acre.pdmqueries.QueryType;
import org.acre.dao.DAOFactory;
import org.acre.dao.PatternQueryRepository;
import org.acre.visualizer.ui.AcreUIUtils;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Deepak.Alur@Sun.com
 *         Date: Jun 22, 2004
 *         Time: 5:53:08 PM
 */
public class QueryTreePanel extends JTree {

    JPopupMenu basePopup;

    List dataList;
    boolean expandAllAlways = false;

    String PARAMETER_ICON = "special/Parameter.gif";
    String PROPERTY_ICON = "special/Properties.gif";
    String LIST_ICON = "special/List.gif";


    public boolean isExpandAllAlways() {
        return expandAllAlways;
    }

    public void setExpandAllAlways(boolean expandAllAlways) {
        this.expandAllAlways = expandAllAlways;
        if (expandAllAlways)
            expandAllRows();
    }

    public QueryTreePanel() {
        super();
        setBackground(Color.lightGray);
        initialize();
    }

    public QueryTreePanel(List dataList) {
        super();
        this.dataList = dataList;
        initialize();
    }

    public String convertValueToText(Object value, boolean selected,
                                     boolean expanded, boolean leaf, int row, boolean hasFocus) {
        if (false) {
            return "";
        } else if (value instanceof List) {
            return "";//node.get;
        } else if (value instanceof QueryType) {
            QueryType node = (QueryType) value;
            return node.getName();//node.get;
        } else if (value instanceof ArgumentType) {
            ArgumentType node = (ArgumentType) value;
            return node.getName(); // + " " + node.getType();
        }
        return super.convertValueToText(value, selected, expanded, leaf, row, hasFocus);
    }

    public List getDataList() {
        return dataList;
    }


    public void setDataList(List newList) {
        if (newList == null)
            return;

        this.dataList = newList;

        getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        setModel(new QueryTreeModel(dataList));
        if (expandAllAlways)
            expandAllRows();
    }

    private void expandAllRows() {
        for (int row = 0; row < getRowCount(); row++) {
            expandRow(row);
        }
    }

    private void collapseAllRows() {
        for (int row = 0; row < getRowCount(); row++) {
            collapseRow(row);
        }
    }

    private void initialize() {
        createPopupMenu(this);
        //setLayout(new java.awt.BorderLayout());
        setCellRenderer(new QueryTreeRenderer());
        ToolTipManager.sharedInstance().registerComponent(this);
        setModel(null);
        setDataList(new ArrayList());
    }

    public void createPopupMenu(JComponent parent) {
        JMenuItem menuItem;
        QueryActionListener actionListener = new QueryActionListener();

        //Create the popup menu here if necessary.

        //Add listener to the text area so the popup menu can come up.
        MouseListener popupListener = new QueryPopupListener();
        parent.addMouseListener(popupListener);
    }

    private class QueryPopupListener extends MouseAdapter {

        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
//                System.out.println(e);
//                System.out.println("TODO: Implement maybeShowPopup() for " + e.getSource());
//                System.out.println("maybeShowPopup() Copmonent = " + e.getComponent());
                if (basePopup != null) {
                    basePopup.show(e.getComponent(),
                        e.getX(), e.getY());
                }
            }
        }
    }

    private static class QueryActionListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
//            System.out.println("TODO: Implement actionPerformed() for " + ((JMenuItem) evt.getSource()).getText());

        }
    }

    private class QueryTreeRenderer extends DefaultTreeCellRenderer {

        public QueryTreeRenderer() {
            super();
            setBackground(Color.lightGray);
            setBackgroundNonSelectionColor(Color.lightGray);
            setBackgroundSelectionColor(Color.BLUE);
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

            String tooltip = setSalsaIcon(value);
            if (tooltip != null) {
                //System.out.println("Setting tooltip = " + tooltip + "for this=" + this);
                setToolTipText(tooltip);
            }

            if (row == 0) {
                //setIcon(AcreUIUtils.createPDMTabIcon());
                //setToolTipText(value.toString());
            } else {
                //setToolTipText(null); //no tool tip
            }

            return this;
        }

        private String setSalsaIcon(Object value) {

            Icon icon = null;
            String tooltip = "";

            //System.out.println("Setting Icon for : " + value);
            if (value == null)
                return null;

            String nodeValue = value.toString();

            if (nodeValue == null)
                return null;

            if (value instanceof List) {
                icon = AcreUIUtils.createImageIcon(LIST_ICON);
            } else if (value instanceof QueryType) {
                icon = AcreUIUtils.createPQLIcon();
                tooltip = ((QueryType) value).getDescription();

            } else if (value instanceof ArgumentType) {
                icon = AcreUIUtils.createImageIcon(PARAMETER_ICON);
                tooltip = "Type = " + ((ArgumentType) value).getType() +
                        "Value = " + ((ArgumentType) value).getType()
                        ;
            } else {
                icon = AcreUIUtils.createImageIcon(PROPERTY_ICON);
            }

            if (icon != null) {
                this.setIcon(icon);
                this.setToolTipText(tooltip);
            }

            return tooltip;
        }

    }


    public static void main(String[] args) {
        //PDMDAOFacade
                
        PatternQueryRepository patternQueryRepository = DAOFactory.getPatternQueryRepository();
        System.out.println(patternQueryRepository);
        List l = patternQueryRepository.getGlobalQueryList();
        QueryTreePanel t = new QueryTreePanel();
        t.setDataList(l);

        JFrame f = new JFrame();
        f.getContentPane().add(t, java.awt.BorderLayout.CENTER);

        f.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                System.exit(0);
            }
        });

        f.pack();
        f.setVisible(true);

    }

}
