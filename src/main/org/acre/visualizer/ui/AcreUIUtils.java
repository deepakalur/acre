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

package org.acre.visualizer.ui;

import org.acre.dao.PatternQueryRepository;
import org.acre.dao.PatternRepository;
import org.acre.model.metamodel.PopulateMetaModel;
//import org.acre.visualizer.ui.pdmcomposer.PDMList;
import org.acre.visualizer.ui.pqleditor.QueriesList;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.Component;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: Jun 4, 2004
 * Time: 10:30:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class AcreUIUtils {

    public static JButton createImageButton(ActionListener listener,
                                            String imageName,
                                            String actionCommand,
                                            String toolTipText,
                                            String altText) {
        //Look for the image.
        String imgLocation = "icons/"
                + imageName
                + ".gif";
        URL imageURL = AcreUIUtils.class.getResource(imgLocation);

        //Create and initialize the button.
        JButton button = new JButton();
        button.setActionCommand(actionCommand);
        button.setToolTipText(toolTipText);
        if (listener != null) button.addActionListener(listener);
        //button.setSize(24, 24);

        if (imageURL != null) {
            //image found
            button.setIcon(new ImageIcon(imageURL, altText));
            button.setText("");
        } else {
            //no image found
            button.setText(actionCommand);
            System.err.println("Resource not found: " + imgLocation);
        }
        button.setBorderPainted(true);
        button.setMargin(new Insets(0,0,0,0));

        return button;
    }

    public static JToggleButton createImageToggleButton(ActionListener listener,
                                                        String imageName,
                                                        String actionCommand,
                                                        String toolTipText,
                                                        String altText) {

        //Look for the image.
        String imgLocation = "icons/"
                + imageName
                + ".gif";

        URL imageURL = AcreUIUtils.class.getResource(imgLocation);

        //Create and initialize the button.
        JToggleButton button = new JToggleButton();
        button.setActionCommand(actionCommand);
        button.setToolTipText(toolTipText);
        button.addActionListener(listener);

        if (imageURL != null) {
            //image found
            button.setIcon(new ImageIcon(imageURL, altText));
        } else {
            //no image found
            button.setText(actionCommand);
            System.err.println("Resource not found: " + imgLocation);
        }
//        button.setBorderPainted(false);

        return button;
    }

    public static Image createImage(String imageName) {
        String path = "icons/" + imageName;
        java.net.URL imgURL = AcreUIUtils.class.getResource(path);
        if (imgURL != null) {
            return Toolkit.getDefaultToolkit().getImage(imgURL);
        } else {
            System.err.println("AcreUIUtils.createImage() : Couldn't find file: " + path);
            return null;
        }
    }

    public static ImageIcon createImageIcon(String imageName) {

        String path = "icons/" + imageName;
        java.net.URL imgURL = AcreUIUtils.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("AcreUIUtils.createImageIcon(): Couldn't find file: " + path);
            return null;
        }
    }

    public static JMenu createMenu(String menu, String[] menuItemNames) {
        JMenu jmenu = new JMenu(menu);
        createMenuItems(jmenu, menuItemNames);
        jmenu.setMnemonic(menu.charAt(0));
        return jmenu;
    }

    public static void createMenuItems(JMenu menu, String[] menuItemNames) {
        ArrayList menuItems = new ArrayList();
        JMenuItem menuItem = null;
        for (int i = 0; i < menuItemNames.length; i++) {
            menuItem = new JMenuItem(menuItemNames[i]);
            menuItem.setMnemonic(menuItemNames[i].charAt(0));
            menu.add(menuItem);
        }
    }

    public static Icon createPatternIcon(String patternImageName) {
        return AcreUIUtils.createImageIcon("patterns/" + patternImageName);
    }

    public static Icon createPDMTabIcon() {
        return AcreUIUtils.createImageIcon("tabs/PDM.gif");
    }

    public static Icon createDiagramTabIcon() {
        return AcreUIUtils.createImageIcon("tabs/AcreDiagram.gif");
    }

    public static Icon createViewSourceTabIcon() {
        return AcreUIUtils.createImageIcon("tabs/ViewSource.gif");
    }

    public static Icon createPQLIcon() {
        return AcreUIUtils.createImageIcon("tabs/PQL.gif");
    }

    public static Icon createRoleIcon() {
        return AcreUIUtils.createImageIcon("tabs/Role.gif");
    }

    public static Icon createRelationshipIcon() {
        return AcreUIUtils.createImageIcon("tabs/Relationship.gif");
    }

    public static Icon getPQLTreeIcon(String nodeValue) {
        ImageIcon icon = null;

        if (nodeValue.equalsIgnoreCase(PopulateMetaModel.ANNOTATIONS)) {
            icon = AcreUIUtils.createImageIcon(AcreIconConstants.ANNOTATION_ICON);
        } else if (nodeValue.equalsIgnoreCase(PopulateMetaModel.ANNOTATION)) {
            icon = AcreUIUtils.createImageIcon(AcreIconConstants.ANNOTATION_ICON);
        } else if (nodeValue.equalsIgnoreCase(PopulateMetaModel.ANNOTATION_ARGUMENTS)) {
            icon = AcreUIUtils.createImageIcon(AcreIconConstants.ARGUMENT_ICON);
        } else if (nodeValue.equalsIgnoreCase(PopulateMetaModel.ARGUMENTS)) {
            icon = AcreUIUtils.createImageIcon(AcreIconConstants.ARGUMENT_ICON);
        } else if (nodeValue.equalsIgnoreCase(PopulateMetaModel.CALLS)) {
            icon = AcreUIUtils.createImageIcon(AcreIconConstants.CALLS_ICON);
        } else if (nodeValue.equalsIgnoreCase(PopulateMetaModel.CALLERS)) {
            icon = AcreUIUtils.createImageIcon(AcreIconConstants.CALLS_ICON);
        } else if (nodeValue.equalsIgnoreCase(PopulateMetaModel.CAUGHTBY)) {
            icon = AcreUIUtils.createImageIcon(AcreIconConstants.CATCHES_ICON);
        } else if (nodeValue.equalsIgnoreCase(PopulateMetaModel.CATCHESEXCEPTIONS)) {
            icon = AcreUIUtils.createImageIcon(AcreIconConstants.CATCHES_ICON);
        } else if (nodeValue.equalsIgnoreCase(PopulateMetaModel.CLASSES)) {
            icon = AcreUIUtils.createImageIcon(AcreIconConstants.CLASS_ICON);
        } else if (nodeValue.equalsIgnoreCase(PopulateMetaModel.CONTAINS)) {
            icon = AcreUIUtils.createImageIcon(AcreIconConstants.CONTAINS_ICON);
        } else if (nodeValue.equalsIgnoreCase(PopulateMetaModel.CONTAININGCLASS)) {
            icon = AcreUIUtils.createImageIcon(AcreIconConstants.CLASS_ICON);
        } else if (nodeValue.equalsIgnoreCase(PopulateMetaModel.EXTENDSCLASS)) {
            icon = AcreUIUtils.createImageIcon(AcreIconConstants.EXTENDS_ICON);
        } else if (nodeValue.equalsIgnoreCase(PopulateMetaModel.EXTENDINGCLASSES)) {
            icon = AcreUIUtils.createImageIcon(AcreIconConstants.CLASS_ICON);
        } else if (nodeValue.equalsIgnoreCase(PopulateMetaModel.FIELDS)) {
            icon = AcreUIUtils.createImageIcon(AcreIconConstants.FIELD_ICON);
        } else if (nodeValue.equalsIgnoreCase("implements")) { // hack
            icon = AcreUIUtils.createImageIcon(AcreIconConstants.IMPLEMENTS_ICON);
        } else if (nodeValue.equalsIgnoreCase(PopulateMetaModel.IMPLEMENTSINTERFACES)) {
            icon = AcreUIUtils.createImageIcon(AcreIconConstants.IMPLEMENTS_ICON);
        } else if (nodeValue.equalsIgnoreCase(PopulateMetaModel.IMPLEMENTINGCLASSES)) {
            icon = AcreUIUtils.createImageIcon(AcreIconConstants.IMPLEMENTS_ICON);
        } else if (nodeValue.equalsIgnoreCase("instanceof")) { // hack todo
            icon = AcreUIUtils.createImageIcon(AcreIconConstants.INSTANCEOF_ICON);
        } else if (nodeValue.equalsIgnoreCase("inv(instanceof)")) { // hack todo
            icon = AcreUIUtils.createImageIcon(AcreIconConstants.INSTANCEOF_ICON);
        } else if (nodeValue.equalsIgnoreCase(PopulateMetaModel.INSTANTIATES)) {
            icon = AcreUIUtils.createImageIcon(AcreIconConstants.CREATES_ICON);
        } else if (nodeValue.equalsIgnoreCase(PopulateMetaModel.INSTANTIATEDBY)) {
            icon = AcreUIUtils.createImageIcon(AcreIconConstants.CREATES_ICON);
        } else if (nodeValue.equalsIgnoreCase(PopulateMetaModel.METHOD)) {
            icon = AcreUIUtils.createImageIcon(AcreIconConstants.METHOD_ICON);
        } else if (nodeValue.equalsIgnoreCase(PopulateMetaModel.METHODS)) {
            icon = AcreUIUtils.createImageIcon(AcreIconConstants.METHOD_ICON);
        } else if (nodeValue.equalsIgnoreCase(PopulateMetaModel.PACKAGES)) {
            icon = AcreUIUtils.createImageIcon(AcreIconConstants.PACKAGE_ICON);
        } else if (nodeValue.equalsIgnoreCase(PopulateMetaModel.PARAMETERS)) {
            icon = AcreUIUtils.createImageIcon(AcreIconConstants.PARAMETER_ICON);
        } else if (nodeValue.equalsIgnoreCase(PopulateMetaModel.PARENTPACKAGE)) {
            icon = AcreUIUtils.createImageIcon(AcreIconConstants.PACKAGE_ICON);
        } else if (nodeValue.equalsIgnoreCase(PopulateMetaModel.PARENTCLASS)) {
            icon = AcreUIUtils.createImageIcon(AcreIconConstants.CLASS_ICON);
        } else if (nodeValue.equalsIgnoreCase(PopulateMetaModel.PARENTMETHOD)) {
            icon = AcreUIUtils.createImageIcon(AcreIconConstants.METHOD_ICON);
        } else if (nodeValue.equalsIgnoreCase(PopulateMetaModel.PARENTFIELD)) {
            icon = AcreUIUtils.createImageIcon(AcreIconConstants.FIELD_ICON);
        } else if (nodeValue.equalsIgnoreCase(PopulateMetaModel.PARENTPARAMETER)) {
            icon = AcreUIUtils.createImageIcon(AcreIconConstants.PARAMETER_ICON);
        } else if (nodeValue.equalsIgnoreCase(PopulateMetaModel.RETURNEDBY)) {
            icon = AcreUIUtils.createImageIcon(AcreIconConstants.THROWS_ICON);
        } else if (nodeValue.equalsIgnoreCase(PopulateMetaModel.RETURNTYPE)) {
            icon = AcreUIUtils.createImageIcon(AcreIconConstants.CLASS_ICON);
        } else if (nodeValue.equalsIgnoreCase(PopulateMetaModel.THROWNBY)) {
            icon = AcreUIUtils.createImageIcon(AcreIconConstants.CLASS_ICON);
        } else if (nodeValue.equalsIgnoreCase(PopulateMetaModel.TYPE)) {
            icon = AcreUIUtils.createImageIcon(AcreIconConstants.TYPE_ICON);
        } else if (nodeValue.equalsIgnoreCase(PopulateMetaModel.TYPEFIELDS)) {
            icon = AcreUIUtils.createImageIcon(AcreIconConstants.FIELD_ICON);
        } else if (nodeValue.equalsIgnoreCase(PopulateMetaModel.TYPEPARAMETERS)) {
            icon = AcreUIUtils.createImageIcon(AcreIconConstants.PARAMETER_ICON);
        } else if (nodeValue.equalsIgnoreCase(PopulateMetaModel.TYPEANNOTATIONS)) {
            icon = AcreUIUtils.createImageIcon(AcreIconConstants.ANNOTATION_ICON);
        } else if (nodeValue.equalsIgnoreCase(PopulateMetaModel.TYPEANNOTATIONARGUMENTS)) {
            icon = AcreUIUtils.createImageIcon(AcreIconConstants.ARGUMENT_ICON);
        } else if (nodeValue.equalsIgnoreCase(PopulateMetaModel.THROWSEXCEPTIONS)) {
            icon = AcreUIUtils.createImageIcon(AcreIconConstants.THROWS_ICON);
        } else if (nodeValue.equalsIgnoreCase(PopulateMetaModel.USEDFIELDS)) {
            icon = AcreUIUtils.createImageIcon(AcreIconConstants.USES_ICON);
        } else if (nodeValue.equalsIgnoreCase(PopulateMetaModel.USEDBYMETHODS)) {
            icon = AcreUIUtils.createImageIcon(AcreIconConstants.METHOD_ICON);
        } else {
            //System.out.println("Could not find icon for :" + nodeValue);
            icon = AcreUIUtils.createImageIcon(AcreIconConstants.DEFAULT_ICON);
        }

        return icon;
    }


    // todo move these methods to a separate util class
    public static QueriesList createGlobalQueriesList(PatternQueryRepository patternQueryRepository) {
        QueriesList panel = new QueriesList();
        List queries = patternQueryRepository.getGlobalQueryList();
        panel.setListData(new ArrayList(queries));
        return panel;
    }

    public static QueriesList createProjectQueriesList(PatternQueryRepository patternQueryRepository, String projectName) {
        QueriesList panel = new QueriesList();
        List projectQueries = patternQueryRepository.getProjectQueryList(projectName);
        panel.setListData(new ArrayList(projectQueries));
        return panel;
    }

//    public static PDMList createGlobalPDMList(PatternRepository patternRepository) {
//        PDMList panel = new PDMList();
//        List pdms = patternRepository.getGlobalPatternModels();
//        panel.setListData(new ArrayList(pdms));
//        return panel;
//    }

    public static QueriesList createProjectPDMList(PatternRepository patternRepository, String projectName) {
        QueriesList panel = new QueriesList();
        List projectPdms= patternRepository.getProjectPatternModels(projectName);
        panel.setListData(new ArrayList(projectPdms));
        return panel;
    }

    public static void showFeatureNotImplemented(Component component) {
        JOptionPane.showMessageDialog(component,
            "Feature Not Implemented", AcreUIConstants.APP_NAME,
            JOptionPane.INFORMATION_MESSAGE);

    }

    public static void initColumnSizes(JTable table) {
        TableModel model = (TableModel)table.getModel();
        Object[] longValues = new Object[model.getColumnCount()];

        for (int i =0 ;  i < model.getColumnCount(); i++) {
            longValues[i] = table.getModel().getColumnName(i);
        }

        TableColumn column = null;
        Component comp = null;
        int headerWidth = 0;
        int cellWidth = 0;

        TableCellRenderer headerRenderer =
            table.getTableHeader().getDefaultRenderer();

        for (int i = 0; i < 5; i++) {
            column = table.getColumnModel().getColumn(i);

            comp = headerRenderer.getTableCellRendererComponent(
                                 null, column.getHeaderValue(),
                                 false, false, 0, 0);
            headerWidth = comp.getPreferredSize().width;

            comp = table.getDefaultRenderer(model.getColumnClass(i)).
                             getTableCellRendererComponent(
                                 table, longValues[i],
                                 false, false, 0, i);
            cellWidth = comp.getPreferredSize().width;

            column.setPreferredWidth(Math.max(headerWidth, cellWidth));
        }
    }
}
