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

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.LayoutManager;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: Jun 8, 2004
 * Time: 12:37:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class AcreGridPanel extends JPanel {
    public AcreGridPanel() {
        super();
        initialize();
    }

    public AcreGridPanel(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
        initialize();
    }

    public AcreGridPanel(LayoutManager layout) {
        super(layout);
        initialize();
    }

    public AcreGridPanel(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
        initialize();
    }

    public void initialize() {
        this.setLayout(new GridLayout(0, 1));
    }

    public void addToPanel(JComponent component) {
        this.add(component);
    }

        public static void main (String [] args) {
        JFrame frame = new JFrame();
        frame.setVisible(true);
        AcreGridPanel p = new AcreGridPanel();


        AcreFileChooser sfc = new AcreFileChooser();
        sfc.setFilePathLabel("File:");
        p.add(sfc);
     
        sfc = new AcreFileChooser();
        sfc.setFilePathLabel("File:");
        p.add(sfc);

        frame.getContentPane().add(p);
        frame.pack();
    }
}
