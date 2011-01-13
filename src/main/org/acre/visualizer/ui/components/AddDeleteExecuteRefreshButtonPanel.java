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

import org.acre.visualizer.ui.AcreUIUtils;

import javax.swing.*;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;

/**
 * @author Deepak.Alur@Sun.com
 *         Date: Jun 23, 2004
 *         Time: 12:37:38 PM
 */
public class AddDeleteExecuteRefreshButtonPanel extends JPanel {
    private static final String ADDBUTTON = "buttons/Add";
    private static final String EDITBUTTON = "buttons/Edit";
    private static final String DELETEBUTTON = "buttons/Delete";
    private static final String REFRESHBUTTON = "buttons/Refresh";
    private static final String EXECUTEBUTTON = "buttons/Execute";

    JButton addButton;
    JButton editButton;
    JButton removeButton;
    JButton refreshButton;
    JButton executeButton;

    public AddDeleteExecuteRefreshButtonPanel() {
        super();
        initialize();
    }

    public AddDeleteExecuteRefreshButtonPanel(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
        initialize();
    }

    public AddDeleteExecuteRefreshButtonPanel(LayoutManager layout) {
        super(layout);
        initialize();
    }

    public AddDeleteExecuteRefreshButtonPanel(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
        initialize();
    }

    public void initialize() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        addButton =
                AcreUIUtils.createImageButton(null, ADDBUTTON,
                        ADREActionButtonConstants.ADDACTION,
                        ADREActionButtonConstants.ADDACTION,
                        ADREActionButtonConstants.ADDACTION);
        editButton =
                AcreUIUtils.createImageButton(null, EDITBUTTON,
                        ADREActionButtonConstants.EDITACTION,
                        ADREActionButtonConstants.EDITACTION,
                        ADREActionButtonConstants.EDITACTION);
        removeButton =
                AcreUIUtils.createImageButton(null, DELETEBUTTON,
                        ADREActionButtonConstants.DELETEACTION,
                        ADREActionButtonConstants.DELETEACTION,
                        ADREActionButtonConstants.DELETEACTION);
        refreshButton =
                AcreUIUtils.createImageButton(null, REFRESHBUTTON,
                        ADREActionButtonConstants.REFRESHACTION,
                        ADREActionButtonConstants.REFRESHACTION,
                        ADREActionButtonConstants.REFRESHACTION);
        executeButton =
                AcreUIUtils.createImageButton(null, EXECUTEBUTTON,
                        ADREActionButtonConstants.EXECUTEACTION,
                        ADREActionButtonConstants.EXECUTEACTION,
                        ADREActionButtonConstants.EXECUTEACTION);

        addButton.setBorderPainted(false);
        editButton.setBorderPainted(false);
        removeButton.setBorderPainted(false);
        refreshButton.setBorderPainted(false);
        executeButton.setBorderPainted(false);

        add(Box.createVerticalGlue());
        add(Box.createHorizontalGlue());
        JPanel p = new JPanel();
        p.add(Box.createHorizontalStrut(1));
        p.add(addButton);
        p.add(Box.createHorizontalStrut(1));
        p.add(removeButton);
        p.add(Box.createHorizontalStrut(1));
        p.add(editButton);
        p.add(Box.createHorizontalStrut(1));        
        p.add(refreshButton);
        p.add(Box.createHorizontalStrut(1));
        p.add(executeButton);
        p.add(Box.createHorizontalStrut(10));

        add(p);
        add(Box.createVerticalGlue());
        add(Box.createHorizontalGlue());
    }

    public void setActionListener(ActionListener listener) {
        if (addButton != null)
            addButton.addActionListener(listener);
        if (editButton != null)
            editButton.addActionListener(listener);
        if (removeButton != null)
            removeButton.addActionListener(listener);
        if (executeButton != null)
            executeButton.addActionListener(listener);
        if (refreshButton != null)
            refreshButton.addActionListener(listener);
    }

    public static void main(String args []) {
        JFrame frame = new JFrame("Buttons");
        AddDeleteExecuteRefreshButtonPanel panel = new AddDeleteExecuteRefreshButtonPanel();

        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }
}
