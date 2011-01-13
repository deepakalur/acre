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


import org.acre.visualizer.ui.GlobalSettings;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Deepak Alur
 * Date: Feb 23, 2005
 * Time: 11:03:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class PDMViewPanel extends JSplitPane {
    static final String OPEN_MENU = "Maximize View";

    JTabbedPane resultTab;
    String pdmName;
    JPopupMenu popupMenu;
    JMenuItem openMenuItem;
    AcrePDMVisualizer pdmVisualizer;
//    ModelBrowser view3d;
//    GrappaPanel view2d;
    JPanel pdmTreePanel;
    PDMViewer pdmViewer;
    static final String VIEW_3D_TAB_TITLE = "3D View";
    static final String VIEW_2D_TAB_TITLE = "2D View";


    private PDMViewPanel() {
        // do not use this constructor
        super();
    }

    PDMViewPanel(String pdmName, JPanel pdmTreePanel, PDMViewer pdmViewer) {
        super();
        this.pdmName = pdmName;
        this.pdmTreePanel = pdmTreePanel;
        this.pdmViewer = pdmViewer;

        setOrientation(JSplitPane.VERTICAL_SPLIT);
        setOneTouchExpandable(true);
        setDividerLocation(GlobalSettings.getHorizontalDividerLocation());
        setDividerLocation(400);
        //setDividerSize(GlobalSettings.getDividerSize());
        popupMenu = new JPopupMenu("Views");
        openMenuItem = new JMenuItem(OPEN_MENU);
        openMenuItem.addActionListener(new PDMViewPanelActionListener(this) );

        popupMenu.add(openMenuItem);
        popupMenu.setLightWeightPopupEnabled(false);

        resultTab = new JTabbedPane();
        resultTab.addMouseListener(new PDMViewTabPaneListener(popupMenu));

        this.setRightComponent(resultTab);
        this.setLeftComponent(null);
    }

    public void setPDMDiagram(AcrePDMVisualizer view) {
        pdmVisualizer = view;
        JScrollPane sp = new JScrollPane(view);
        this.setLeftComponent(sp);
        setDividerLocation(400);
        invalidate();
    }

//    public void setResult3DView(String tabName, Scene scene) {
//        if (true) return;
//
//        JScrollPane sp = new JScrollPane(scene.getModelBrowser());
//        view3d = scene.getModelBrowser();
//
//        int removeTabInd = getTabIndex(resultTab, VIEW_3D_TAB_TITLE);
//        if (removeTabInd != -1)
//            resultTab.removeTabAt(removeTabInd);
//
//        resultTab.addTab(VIEW_3D_TAB_TITLE, sp);
//        resultTab.setSelectedComponent(sp);
//        resultTab.setMnemonicAt(resultTab.getSelectedIndex(), '3');
//        pdmTreePanel.removeAll();
//        pdmTreePanel.add(scene.getModelBrowser().getLeftHandSplitPane(),
//                BorderLayout.CENTER);
//        pdmTreePanel.invalidate();
//        invalidate();
//    }
//
//    public void setResult2DView(String tabName, GrappaPanel view2d) {
//        this.view2d = view2d;
//        JScrollPane sp = new JScrollPane(view2d);
//
//        int removeTabInd = getTabIndex(resultTab, VIEW_2D_TAB_TITLE);
//        if (removeTabInd != -1)
//            resultTab.removeTabAt(removeTabInd);
//
//        resultTab.addTab(VIEW_2D_TAB_TITLE, sp);
//        resultTab.setSelectedComponent(sp);
//        resultTab.setMnemonicAt(resultTab.getSelectedIndex(), '2');
//
//        if (view3d == null) {
//                JScrollPane treeScroll = new JScrollPane();
//                treeScroll.getViewport().setView(pdmViewer.getTree());
//                JTabbedPane treeTabPane = new JTabbedPane();
//                treeTabPane.addTab("Pattern Hierarchy", treeScroll);
//                pdmTreePanel.setLayout(new BorderLayout());
//                pdmTreePanel.add(treeTabPane, BorderLayout.CENTER);
//                pdmTreePanel.invalidate();
//                invalidate();
//        }
//        invalidate();
//    }

    // todo - move this to utils
    private int getTabIndex(JTabbedPane resultTab, String title) {
        for (int i = 0; i < resultTab.getTabCount(); i++) {
            if (resultTab.getTitleAt(i).equalsIgnoreCase(title))
                return i;
        }
        return -1;
    }

    public String getPdmName() {
        return pdmName;
    }

    public void setPdmTreePanel(JPanel pdmTreePanel) {
        this.pdmTreePanel = pdmTreePanel;
    }

    class PDMViewTabPaneListener extends MouseAdapter {
        JPopupMenu popupMenu;

        private PDMViewTabPaneListener() {

        }

        PDMViewTabPaneListener(JPopupMenu popupMenu) {
            this.popupMenu = popupMenu;
        }

        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent event) {
            if( event.isPopupTrigger() )
            {
                popupMenu.show( event.getComponent(),
                                    event.getX(), event.getY() );
            }
        }
    }

}
