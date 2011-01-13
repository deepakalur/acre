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
package org.acre.visualizer.ui.pdmcomposer;

import org.acre.pdm.PDMType;
import org.acre.pdm.RelationshipType;
import org.acre.pdm.RoleType;
import org.acre.dao.DAOFactory;
import org.acre.dao.PatternRepository;
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
 * @author Syed Ali
 */
public class PDMTreePanel extends JTree {

    JPopupMenu basePopup;
    JPopupMenu pdmPopup;
    List dataList;
    boolean expandAllAlways = false;

    String PROPERTY_ICON = "special/Properties.gif";
    String LIST_ICON = "special/List.gif";

    public PDMTreePanel() {
        super();
        initialize();
    }

    public PDMTreePanel(List pdmList) {
        super();
        this.dataList = pdmList;
        initialize();
    }

    public boolean isExpandAllAlways() {
        return expandAllAlways;
    }

    public void setExpandAllAlways(boolean expandAllAlways) {
        this.expandAllAlways = expandAllAlways;
        if (expandAllAlways)
            expandAllRows();
    }

    public String convertValueToText(Object value, boolean selected,
                         boolean expanded, boolean leaf, int row, boolean hasFocus) {
        if (false) {
            return "";
        } else if (value instanceof List) {
            return "";//node.get;
        } else if (value instanceof PDMType) {
            PDMType node = (PDMType) value;
            return node.getName();//node.get;
        } else if (value instanceof RoleType) {
            RoleType node = (RoleType) value;
            return node.getName(); // + " (" + node.getType() + ")";
        } else if (value instanceof RelationshipType) {
            RelationshipType node = (RelationshipType) value;
            return node.getName();//node.get;
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
        setModel(new PDMTreeModel(dataList));
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
        setLayout(new java.awt.BorderLayout());
        setCellRenderer(new PDMTreeRenderer());
        ToolTipManager.sharedInstance().registerComponent(this);
        setToolTipText("PDM Details");
        if (dataList != null) {
            getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
            setModel(new PDMTreeModel(dataList));
        }
        else {
            setModel(null);
            setDataList(new ArrayList());
        }
    }

    public void createPopupMenu(JComponent parent) {
        JMenuItem menuItem;
        PDMActionListener actionListener = new PDMActionListener();
        //Create the popup menu here if necessary
//        JPopupMenu popup = new JPopupMenu();
/*        basePopup = new JPopupMenu();
        menuItem = new JMenuItem("Visualize");
        menuItem.addActionListener(actionListener);
        basePopup.add(menuItem);
        menuItem = new JMenuItem("Edit");
        menuItem.addActionListener(actionListener);
        basePopup.add(menuItem);
        menuItem = new JMenuItem("Execute");
        menuItem.addActionListener(actionListener);
        basePopup.add(menuItem);

//        listpopup = new JPopupMenu();
        pdmPopup = basePopup;
        menuItem = new JMenuItem("Add Role");
        menuItem.addActionListener(actionListener);
        menuItem.setEnabled(false);
        pdmPopup.add(menuItem);
        menuItem = new JMenuItem("Add Relationship");
        menuItem.addActionListener(actionListener);
        menuItem.setEnabled(false);
        pdmPopup.add(menuItem);

//        queryPopup = new JPopupMenu();

//        relPopup = new JPopupMenu();
*/
        //Add listener to the text area so the popup menu can come up.
        MouseListener popupListener = new PDMPopupListener();
        parent.addMouseListener(popupListener);
    }

    private class PDMPopupListener extends MouseAdapter {

        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                basePopup.show(e.getComponent(),
                        e.getX(), e.getY());
            }
        }
    }

    private static class PDMActionListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
//            System.out.println("TODO: Implement actionPerformed() for " + evt);
                    //((JMenuItem) evt.getSource()).getText());
        }
    }

    private class PDMTreeRenderer extends DefaultTreeCellRenderer {

        public PDMTreeRenderer() {
            super();
            setBackground(Color.lightGray);
            setBackgroundNonSelectionColor(Color.lightGray);
            setBackgroundSelectionColor(Color.lightGray);
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

            String tooltip = setSalsaIcon(value);
            if (tooltip != null) {
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

            if (value == null)
                return null;

            String nodeValue = value.toString();

            if (nodeValue == null)
                return null;
            if (value instanceof List) {
                icon = AcreUIUtils.createImageIcon(LIST_ICON);
            } else if (value instanceof RoleType) {
                RoleType r = (RoleType) value;
                String roleType = r.getType();
                tooltip = "Role:" + r.getName() +
                        "Seq#:" + r.getSequence() +
                        "Type:" + r.getType() +
                        "QueryName:" + r.getQueryName() +
                        "Args:" + r.getArgument();
                icon = AcreUIUtils.createRoleIcon();
                /*if (roleType.equals(PDMXMLConstants.ROLE_TYPE_PDM)) {
                    icon = AcreUIUtils.createPDMTabIcon();
                } else if (roleType.equals(PDMXMLConstants.ROLE_TYPE_QUERY)) {
                    icon = AcreUIUtils.createPQLIcon();
                } else {
                    icon = AcreUIUtils.createRoleIcon();
                } */
            } else if (value instanceof PDMType) {
                PDMType pdm = (PDMType) value;
                tooltip = "PDM:" + pdm.getName() +
                        " Type:" + pdm.getType() +
                        " Tier:" + pdm.getTier() +
                        " Category:" + pdm.getCategory() +
                        " Desc:" + pdm.getDescription();
                icon = AcreUIUtils.createPDMTabIcon();
            } else if (value instanceof RelationshipType) {
                RelationshipType rel = (RelationshipType) value;
                tooltip = "Relationship:" + rel.getName() +
                        "Type:" + rel.getType() +
                        "From:" + rel.getFromRole() +
                        "To:" + rel.getToRole();
                icon = AcreUIUtils.createRelationshipIcon();
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
        PatternRepository facade = DAOFactory.getPatternRepository();
        System.out.println(facade);
        List l = facade.getGlobalPatternModels();
        PDMTreePanel t = new PDMTreePanel();
        t.setDataList(l);
        //t.setCellRenderer(new PQLDataDetailTreeRenderer());
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
