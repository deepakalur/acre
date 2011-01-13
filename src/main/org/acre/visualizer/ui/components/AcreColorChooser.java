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

import javax.swing.JColorChooser;
import javax.swing.JDialog;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Deepak.Alur@Sun.com
 * @version Jan 7, 2005 10:27:38 AM
 */
public class AcreColorChooser  extends JColorChooser implements ActionListener {
    JDialog myDialog;
    Component parent;
    String title;
    boolean colorChanged = false;

    public AcreColorChooser(Component parent, String title) {
        super();
        this.parent = parent;
        this.title = title;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("OK")) {
            colorChanged = true;
        } else {
            colorChanged = false;
        }
    }

    private JDialog getDialog() {
        if (myDialog == null) {
            myDialog = JColorChooser.createDialog(parent, title, true, this, this, this);
        }
        return myDialog;
    }

    public void setTitle(String title) {
        getDialog().setTitle(title);
    }

    public void showMe() {
        colorChanged = false;
        getDialog().setVisible(true);
    }

    public void hideMe() {
        getDialog().setVisible(false);
    }

    public boolean isColorChanged() {
        return colorChanged;
    }

}
