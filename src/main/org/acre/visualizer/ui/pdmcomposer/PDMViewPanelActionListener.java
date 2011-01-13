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

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by IntelliJ IDEA.
 * User: Deepak Alur
 * Date: Feb 24, 2005
 * Time: 12:05:48 PM
 * To change this template use File | Settings | File Templates.
 */
class PDMViewPanelActionListener implements ActionListener {
    private Component currentView;
    private String currentTitle;
    private JScrollPane selectedViewScroll;
    private Component selectedView;
    private PDMViewPanel pdmViewPanel;
    private char mnemonic;

    public PDMViewPanelActionListener(PDMViewPanel pdmViewPanel) {
        this.pdmViewPanel = pdmViewPanel;
    }

    public void actionPerformed(ActionEvent e) {
        System.out.println(e);
        if (e.getActionCommand().equals(PDMViewPanel.OPEN_MENU)) {
            selectedView = pdmViewPanel.resultTab.getSelectedComponent();

            if (selectedView instanceof JScrollPane) {
                selectedViewScroll = (JScrollPane) selectedView;
                currentView = selectedViewScroll.getViewport().getView();
//                if (currentView instanceof GrappaPanel) {
//                    currentTitle = PDMViewPanel.VIEW_2D_TAB_TITLE;
//                    mnemonic = '2';
//                } else if (currentView instanceof ModelBrowser) {
//                    currentTitle = PDMViewPanel.VIEW_3D_TAB_TITLE;
//                    mnemonic = '3';
//                }
            }

            int currentTabIndex = pdmViewPanel.resultTab.getSelectedIndex();
            pdmViewPanel.resultTab.removeTabAt(currentTabIndex);
            pdmViewPanel.pdmTreePanel.removeAll();

            JScrollPane treeScroll = new JScrollPane();
            treeScroll.getViewport().setView(pdmViewPanel.pdmViewer.getTree());
            JTabbedPane treeTabPane = new JTabbedPane();
            treeTabPane.addTab("Pattern Hierarchy", treeScroll);
            pdmViewPanel.pdmTreePanel.setLayout(new BorderLayout());
            pdmViewPanel.pdmTreePanel.add(treeTabPane, BorderLayout.CENTER);
            pdmViewPanel.pdmTreePanel.invalidate();
            pdmViewPanel.invalidate();

            showViewInWindow(currentView);
        }
    }

    private void showViewInWindow(Component v) {
        PDMResultsViewFrame f = new PDMResultsViewFrame(pdmViewPanel.pdmName, currentTitle, v, pdmViewPanel.resultTab, pdmViewPanel.pdmTreePanel, mnemonic);
        f.setVisible(true);
    }
}
