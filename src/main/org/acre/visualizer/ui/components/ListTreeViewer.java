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
package org.acre.visualizer.ui.components;

import org.acre.visualizer.ui.GlobalSettings;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.ListSelectionListener;
import java.awt.BorderLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Deepak.Alur@Sun.com
 *         Date: Jun 22, 2004
 *         Time: 10:31:41 PM
 */
public abstract class ListTreeViewer extends JPanel
        implements ListSelectionListener, ActionListener {

    private AbstractAcreList list;
    private JTree tree;
    private AddDeleteExecuteRefreshButtonPanel buttons;
    private int dividerSize=GlobalSettings.getDividerSize();
    private int dividerLocation=GlobalSettings.getVerticalDividerLocation();

    public AddDeleteExecuteRefreshButtonPanel getButtons() {
        return buttons;
    }

    public void setButtons(AddDeleteExecuteRefreshButtonPanel buttons) {
        this.buttons = buttons;
    }

    private JScrollPane listScroller;
    private JScrollPane treeScroller;
    private JSplitPane splitter;
    protected Editor editor;

    public ListTreeViewer(LayoutManager layout) {
        super(layout);
    }

    public ListTreeViewer(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
    }

    public ListTreeViewer(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
    }

    public ListTreeViewer() {
        super();
    }

    public AbstractAcreList getList() {
        return list;
    }

    public void setList(AbstractAcreList list) {
        this.list = list;
        //this.list.addListSelectionListener(this);
        listScroller.getViewport().setView(this.list);
    }

    public JTree getTree() {
        return tree;
    }

    public void setTree(JTree tree) {
        this.tree = tree;
        treeScroller.getViewport().setView(this.tree);
    }

    public Editor getEditor() {
        return editor;
    }

    public void setEditor(Editor editor) {
        this.editor = editor;
    }

    protected void initialize() {

        setLayout(new BorderLayout());
        splitter = new JSplitPane();
        listScroller = new JScrollPane();
        treeScroller = new JScrollPane();
        buttons = new AddDeleteExecuteRefreshButtonPanel();
        buttons.setActionListener(this);
        buttons.setVisible(false); // hack - hide this because now we have toolbars
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.add(listScroller, BorderLayout.CENTER);
        leftPanel.add(buttons, BorderLayout.SOUTH);
        splitter.setLeftComponent(leftPanel);

        splitter.setRightComponent(treeScroller);

        splitter.setOneTouchExpandable(false);
        //splitter.setDividerSize(dividerSize);
        splitter.setDividerLocation(dividerLocation);
        splitter.setOrientation(JSplitPane.VERTICAL_SPLIT);

        add(splitter);
    }

    public void actionPerformed(ActionEvent e) {
        if (ADREActionButtonConstants.ADDACTION.equals(e.getActionCommand())) {
            addActionPerformed();
        } else if (ADREActionButtonConstants.DELETEACTION.equals(e.getActionCommand())) {
            deleteActionPerformed();
        } else if (ADREActionButtonConstants.REFRESHACTION.equals(e.getActionCommand())) {
            refreshActionPerformed();
        } else if (ADREActionButtonConstants.EXECUTEACTION.equals(e.getActionCommand())) {
            executeActionPerformed();
        } else if (ADREActionButtonConstants.EDITACTION.equals(e.getActionCommand())) {
            editActionPerformed();
        }
    }

    public boolean checkSelected(String operationTitle) {
        Object data = null;

        if (getList() != null)
            data = getList().getSelectedValue();

        if (data == null)
            return false;
//        if (data == null) {
//            JOptionPane.showMessageDialog(
//                    this,
//                    "Select an item from the list and retry...",
//                    operationTitle + " - No item selected",
//                    JOptionPane.ERROR_MESSAGE);
//            return false;
//        }
        return true;
    }

    public abstract void addActionPerformed() ;

    public abstract void deleteActionPerformed();

    public abstract void executeActionPerformed();

    public abstract void refreshActionPerformed();

    public abstract void editActionPerformed();


}
