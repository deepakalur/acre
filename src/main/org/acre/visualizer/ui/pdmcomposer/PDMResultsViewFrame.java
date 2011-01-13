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

import org.acre.visualizer.ui.AcreIconConstants;
import org.acre.visualizer.ui.AcreUIUtils;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Created by IntelliJ IDEA.
 * User: Deepak Alur
 * Date: Feb 24, 2005
 * Time: 12:07:52 PM
 * To change this template use File | Settings | File Templates.
 */
class PDMResultsViewFrame extends JFrame implements WindowListener {
    String pdmName;
    String title;
    Component view;
    JTabbedPane resultTab;
    private JPanel pdmTreePanel;
    char mnemonic;

    PDMResultsViewFrame (String pdmName, String title, Component v, JTabbedPane resultTab, JPanel pdmTreePanel, char mnemonic) {
        super(pdmName + " - " + title);
        this.setIconImage(AcreUIUtils.createImage(AcreIconConstants.MDI_BIG_ICON));
        this.pdmName = pdmName;
        this.title = title;
        this.view = v;
        this.resultTab = resultTab;
        this.pdmTreePanel = pdmTreePanel;
        this.mnemonic = mnemonic;

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addWindowListener(this);
        JScrollPane s=null;
//        if (v instanceof ModelBrowser) {
//            ModelBrowser mb = (ModelBrowser) v;
//            JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
//            split.setDividerLocation(200);
//            split.setLeftComponent(mb.getLeftHandSplitPane());
//            split.setRightComponent(mb);
//            s = new JScrollPane(split);
//        } else {
//            s = new JScrollPane(v);
//        }
        getContentPane().add(s);
        pack();
    }

    public void windowOpened(WindowEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void windowClosing(WindowEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void windowClosed(WindowEvent e) {
        JScrollPane s = new JScrollPane(view);
        resultTab.addTab(title, s);
        resultTab.setSelectedComponent(s);
        resultTab.setMnemonicAt(resultTab.getSelectedIndex(), mnemonic);
//        if (view instanceof ModelBrowser) {
//            pdmTreePanel.removeAll();
//            pdmTreePanel.add(((ModelBrowser) view).getLeftHandSplitPane(), BorderLayout.CENTER);
//            pdmTreePanel.invalidate();
//        }
    }

    public void windowIconified(WindowEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void windowDeiconified(WindowEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void windowActivated(WindowEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void windowDeactivated(WindowEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}
