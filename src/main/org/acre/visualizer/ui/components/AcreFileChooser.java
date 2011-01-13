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
import javax.swing.border.Border;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class AcreFileChooser extends JPanel
                             implements ActionListener {

    JTextField filePath;
    JButton chooseButton;
    JFileChooser fc;
    JLabel filePathLabel;

    public String getFilePath() {
        return filePath.getText();
    }

    public void setFilePath(String path) {
        filePath.setText(path);
    }

    public void setFilePathColumns(int col) {
        filePath.setColumns(col);
    }

    public void setFilePathEditable(boolean flag) {
        filePath.setEditable(flag);
    }

    public void setFilePathLabel(String label) {
        filePathLabel.setText(label);
    }

    public void setFulePathBorder(Border border) {
        filePath.setBorder(border);
    }

    public AcreFileChooser() {
        //super(new BorderLayout());

        filePath = new JTextField();
        filePath.setEditable(false);
        filePath.setVisible(true);
        filePath.setColumns(30);
        filePath.setBorder(BorderFactory.createEtchedBorder());


        //Create a file chooser
        fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        //Create the open button.  We use the image from the JLF
        //Graphics Repository (but we extracted it from the jar).
        chooseButton = new JButton("...",
                                 AcreUIUtils.createImageIcon("FileFolder.gif"));
        chooseButton.addActionListener(this);

        JPanel choosePanel = new JPanel(); //use FlowLayout
        //choosePanel.setLayout();//new SpringLayout());
        //SpringUtilities.makeCompactGrid(choosePanel, 1,
//                                        choosePanel.getComponentCount(),
//                                        6, 6, 6, 6);
        //choosePanel.setLayout(new GridLayout(0, 3));
        filePathLabel = new JLabel("Choose Facts Directory:");
        choosePanel.add(filePathLabel);
        choosePanel.add(filePath);
        choosePanel.add(chooseButton);

        //Add the buttons and the log to this panel.
        add(choosePanel);

    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == chooseButton) {
            int returnVal = fc.showOpenDialog(AcreFileChooser.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                //This is where a real application would open the file.
//                System.out.println("Chose: " + file.getAbsolutePath());
                this.filePath.setText(file.getAbsolutePath());
//            } else {
//                System.out.println("Open command cancelled by user.");
            }
        }
    }


    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);

        //Create and set up the window.
        JFrame frame = new JFrame("FileChooserDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        AcreFileChooser newContentPane = new AcreFileChooser();
        newContentPane.setFilePathColumns(20);
        newContentPane.setFilePathLabel(null);
        newContentPane.setFilePathEditable(true);

        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
