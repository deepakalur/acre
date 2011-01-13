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

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Deepak.Alur@Sun.com
 * @version Nov 8, 2004 11:32:34 PM
 */
public class AcreAbout extends JPanel implements ActionListener{
    JButton button;
    JDialog dialog;

    public AcreAbout() {
        super();
        initialize();
    }

    private void initialize() {
        button = AcreUIUtils.createImageButton(this, "SalsaAbout", "About", "About", "About");
        button.setBorderPainted(false);
        this.add(button);
    }

    public void actionPerformed(ActionEvent e) {
        if (dialog != null)
            dialog.hide();
    }

    public static void main(String args[]) {
        JFrame f = new JFrame();
        AcreAbout b = new AcreAbout();
        f.getContentPane().add(b);
        f.pack();
        f.setVisible(true);
    }

    public void setDialog(JDialog dialog) {
        this.dialog = dialog;
    }

    public JDialog getDialog() {
        return dialog;
    }
}
