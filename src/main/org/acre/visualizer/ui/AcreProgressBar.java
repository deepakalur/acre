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
package org.acre.visualizer.ui;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;

/**
 * @author Deepak.Alur@Sun.com
 * @version Nov 14, 2004 11:06:15 AM
 */
public class AcreProgressBar extends JPanel {
    JProgressBar bar;
    JLabel message;
    JFrame parentFrame;
    private JDialog dialog;
    private JButton cancelButton;

    public AcreProgressBar(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        initComponents();
    }

    public JFrame getParentFrame() {
        return parentFrame;
    }

    public void setParentFrame(JFrame parentFrame) {
        this.parentFrame = parentFrame;
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        bar = new JProgressBar();
        bar.setIndeterminate(true);
        cancelButton = new JButton("Cancel");
        message = new JLabel("Working...");
        message.setHorizontalAlignment(JLabel.CENTER);
        add(bar, BorderLayout.CENTER);
        add(message, BorderLayout.NORTH);
        JPanel cPanel=new JPanel();
        cPanel.add(cancelButton);
        add(cPanel, BorderLayout.SOUTH);
        dialog = new JDialog(getParentFrame(), AcreUIConstants.APP_NAME, false);
        dialog.getContentPane().add(this);
        dialog.pack();
        dialog.setLocationRelativeTo(getParentFrame());
        dialog.setVisible(false);
        dialog.setResizable(true);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    }

    public void showProgress(String message) {
        if (message != null) {
            this.message.setText(message);
            dialog.pack();
        }
//        dialog.repaint();
//        dialog.requestFocus();
        dialog.setVisible(true);
    }

    public void hideProgress() {
        dialog.setVisible(false);
    }

    public void addActionListener(ActionListener l) {
//        only one listener allowed ?
//        ActionListener [] list = cancelButton.getActionListeners();
//        for (int i =0; i < list.length; i++) {
//            cancelButton.removeActionListener(list[i]);
//        }
        cancelButton.addActionListener(l);
    }

    public static void main(String arg[]) {
        JFrame f = new JFrame();
        f.setLocation(500,500);
        AcreProgressBar b = new AcreProgressBar(f);
        b.showProgress("Hello");
//        f.getContentPane().add(b);
//        f.pack();
//        f.setVisible(true);
    }
}
