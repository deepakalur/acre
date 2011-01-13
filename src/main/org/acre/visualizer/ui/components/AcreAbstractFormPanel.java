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

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: Jun 7, 2004
 * Time: 11:54:10 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AcreAbstractFormPanel extends JPanel {

    protected String [] labelNames;

    protected JLabel [] jlabels;
    protected JComponent [] widgets;


    public void setTextValue(String label, String value)  {
        setTextValue(getComponent(label), value);
    }


    public String getTextValue(String label) {
        return getTextValue(getComponent(label));
    }

    public abstract String getTextValue(JComponent component);

    public abstract void setTextValue(JComponent component, String value);

    public abstract JComponent createWidget(String value);


    public JComponent getComponent(String label) {
        for (int i=0; i < labelNames.length; i++) {
            if (labelNames[i].equals(label)) {
                return widgets[i];
            }
        }
        return null;
    }

    public AcreAbstractFormPanel (String [] labels) {
        super();
        labelNames = labels;
    }

    public void initComponents() {
        if (labelNames.length == 0)
            return;

        jlabels = new JLabel[labelNames.length];
        widgets = new JTextField[labelNames.length];

        for (int i=0; i < labelNames.length; i++) {
            widgets[i]=createWidget(labelNames[i]);
            jlabels[i]=new JLabel(labelNames[i], JLabel.TRAILING);
            jlabels[i].setLabelFor(widgets[i]);
            add(jlabels[i]);
            add(widgets[i]);
        }

        this.setLayout(new SpringLayout());

        SpringUtilities.makeCompactGrid(this,
                labelNames.length, 2, //rows, cols
                5, 5,        //initX, initY
                5, 5);       //xPad, yPad

    }

}
