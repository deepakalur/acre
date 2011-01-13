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

import org.acre.visualizer.graph.AcreGraph;
import org.acre.visualizer.ui.AcreUIUtils;

import javax.swing.*;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: Jun 5, 2004
 * Time: 2:29:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class AcreGraphViewToolBar extends JToolBar implements ActionListener {

    private static final String ZOOMIN = "ZoomIn";
    private static final String ZOOMOUT = "ZoomOut";
    private static final String ZOOMREVERT = "ZoomRevert";
    private static final String CLEARGRAPH = "ClearGraph";
    private static final String LAYOUT = "Layout";
    private static final String SALSA_ERROR_ICON = "SalsaError.gif";

    private static final String GRIDON = "GridOn";
    private static final String GRIDOFF = "GridOff";
    private static final String GROUP = "Group";
    private static final String UNGROUP = "Ungroup";

    private AcreGraph graph;

    private double scale = AcreGraph.DEFAULTSCALE;

    public static String[][] graphViewToolBarValues = {
        {"ClearGraph", "Select All"},
        {"ZoomIn", "Zoom In"},
        {"ZoomOut", "Zoom Out"},
        {"ZoomRevert", "Revert"},
        {"Layout", "Apply Layout"},
        //{"ClearGraph", "Clear Graph"},
        {"Group", "Group Selected items"},
        {"Ungroup", "Ungroup Selected group"}
    };

    public static String[][] toggleButtons = {
        {"Show/Hide Grid"},
    };

    private JToggleButton gridButton;

    public AcreGraphViewToolBar() {
        super();
        initialize();
    }

    public AcreGraphViewToolBar(int orientation) {
        super(orientation);
        initialize();
    }

    public AcreGraphViewToolBar(String name) {
        super(name);
        initialize();
    }

    public AcreGraphViewToolBar(String name, int orientation) {
        super(name, orientation);
        initialize();
    }

    public void actionPerformed(ActionEvent e) {

//        System.out.println("AcreGraphViewToolBar:" + e);
        boolean rescale = false;
        if (e.getActionCommand().equalsIgnoreCase(ZOOMIN)) {
            scale = scale * (1 + AcreGraph.SCALEFACTOR);
            rescale = true;
        } else if (e.getActionCommand().equalsIgnoreCase(ZOOMOUT)) {
            scale = scale / (1.0 + AcreGraph.SCALEFACTOR);
            rescale = true;
        } else if (e.getActionCommand().equalsIgnoreCase(CLEARGRAPH)) {
            graph.selectAll();
//            scale = 1.0;
//            rescale = false;
//            if (graph != null) {
//                graph.removeAllCells();
//            }
        } else if (e.getActionCommand().equalsIgnoreCase(ZOOMREVERT)) {
            scale = 1.0;
            rescale = true;
        } else if (e.getActionCommand().equalsIgnoreCase(LAYOUT)) {
            scale = 1.0;
            rescale = false;
            graph.applyLayout();
        } else if (e.getActionCommand().equalsIgnoreCase(GRIDON)) {
//            System.out.println("Setting Grid On");
            gridButton.setActionCommand(GRIDOFF);
            gridButton.setToolTipText("Show Grid");
            graph.setGridVisible(false);
        } else if (e.getActionCommand().equalsIgnoreCase(GRIDOFF)) {
//            System.out.println("Setting Grid Off");
            gridButton.setActionCommand(GRIDON);
            gridButton.setToolTipText("Hide Grid");
            graph.setGridVisible(true);
        } else if (e.getActionCommand().equalsIgnoreCase(GROUP)) {
//            System.out.println("Grouping selected cells");
            graph.groupSelected();
        } else if (e.getActionCommand().equalsIgnoreCase(UNGROUP)) {
//            System.out.println("Ungrouping selected cells");
            graph.ungroupSelected();
        } else {
            // not implemented
            JComponent optionParent = this;
            if (graph != null) {
                optionParent = (JComponent) graph;
            }
            JOptionPane.showMessageDialog(optionParent, "Sorry! Feature not yet implemented.",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE, AcreUIUtils.createImageIcon(SALSA_ERROR_ICON));
        }
        if (rescale) {
            if (graph != null) {
                graph.setScale(scale);
            }
//            System.out.println("Rescaling: " + scale);
        }
    }

    private void initialize() {

        setOrientation(JToolBar.HORIZONTAL);

        this.setFloatable(false);
        this.setToolTipText("Diagram Tools");
        //add(Box.createVerticalGlue());
        //add(Box.createHorizontalGlue());
        ArrayList buttons = createGraphViewToolBarButtons(this);
        for (int i = 0; i < buttons.size(); i++) {
            add((Component) buttons.get(i));
        }

        this.setRollover(true);


        gridButton = AcreUIUtils.createImageToggleButton(this, "Grid", GRIDON, "Hide Grid", "Show/Hide Grid");

        add(gridButton);


        //add(Box.createVerticalGlue());
        //add(Box.createHorizontalGlue());
    }

    public static void main(String args[]) {
        JFrame frame = new JFrame("MAIN VIEW TOOL BAR");
        AcreGraphViewToolBar bar = new AcreGraphViewToolBar();
        frame.getContentPane().add(bar);
        frame.setVisible(true);
        frame.pack();
    }

    public AcreGraph getGraph() {
        return graph;
    }

    public void setGraph(AcreGraph graph) {
        this.graph = graph;
    }

    public ArrayList createGraphViewToolBarButtons(ActionListener listener) {
        ArrayList buttons = new ArrayList();
        String buttonName;
        String buttonTip;

        for (int i = 0; i < graphViewToolBarValues.length; i++) {
            buttonName = graphViewToolBarValues[i][0];
            buttonTip = graphViewToolBarValues[i][1];
            JButton button = AcreUIUtils.createImageButton(listener, buttonName, buttonName, buttonTip, buttonTip);
            button.setBorderPainted(false);
            button.setMargin(new Insets(2, 2, 2, 2));
            
            buttons.add(button);
        }

        return buttons;
    }


}
