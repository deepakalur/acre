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

package org.acre.visualizer.ui.pqleditor;

import org.acre.pdmqueries.QueryType;
import org.acre.common.AcreStringUtil;
import org.acre.dao.PDMQueryValidator;
import org.acre.visualizer.ui.Main;

import javax.swing.JDialog;


/**
 *
 * @author  Administrator
 */
public class PQLReturnVariableForm extends javax.swing.JPanel {

    /** Creates new form PQLReturnVariableForm */
    public PQLReturnVariableForm() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel1 = new javax.swing.JLabel();
        nameTF = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        typeCB = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        descriptionTF = new javax.swing.JTextField();
        buttonPanel = new javax.swing.JPanel();
        saveButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Variable Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 5);
        add(jLabel1, gridBagConstraints);

        nameTF.setColumns(15);
        nameTF.setToolTipText("Unique return variable for query");
        nameTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameTFActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(nameTF, gridBagConstraints);

        jLabel2.setText("Variable Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 5);
        add(jLabel2, gridBagConstraints);

        initTypeCB();
        typeCB.setToolTipText("Return variable type");
        typeCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                typeCBActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(typeCB, gridBagConstraints);

        jLabel3.setText("Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 5);
        add(jLabel3, gridBagConstraints);

        descriptionTF.setColumns(30);
        descriptionTF.setToolTipText("Description for the return variable");
        descriptionTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                descriptionTFActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(descriptionTF, gridBagConstraints);

        saveButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/acre/visualizer/ui/icons/buttons/SaveButton.gif")));
        saveButton.setMnemonic('S');
        saveButton.setToolTipText("Save Return Variable");
        saveButton.setBorderPainted(false);
        saveButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(saveButton);

        cancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/acre/visualizer/ui/icons/buttons/CancelButton.gif")));
        cancelButton.setMnemonic('C');
        cancelButton.setToolTipText("Cancel Return Variable Edits");
        cancelButton.setBorderPainted(false);
        cancelButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(cancelButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        add(buttonPanel, gridBagConstraints);

    }//GEN-END:initComponents

    private void descriptionTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_descriptionTFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_descriptionTFActionPerformed

    private void typeCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_typeCBActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_typeCBActionPerformed

    private void nameTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameTFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nameTFActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        save();
    }//GEN-LAST:event_saveButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        cancel();
    }//GEN-LAST:event_cancelButtonActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField descriptionTF;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField nameTF;
    private javax.swing.JButton saveButton;
    private javax.swing.JComboBox typeCB;
    // End of variables declaration//GEN-END:variables


    private JDialog myDialog;
    private boolean operationCanceled = false;
    private QueryType query;

    private void initTypeCB() {
        Object [] values = PDMQueryValidator.getInstance().getQueryReturnVariableTypes();
        javax.swing.DefaultComboBoxModel model = new javax.swing.DefaultComboBoxModel(values);
        typeCB.setModel(model);
    }

    public String getDescription() {
        return descriptionTF.getText();
    }

    public String getReturnVariableName() {
        return nameTF.getText();
    }

    public String getType() {
        return typeCB.getSelectedItem().toString();
    }


    public JDialog getDialog() {
        return myDialog;
    }

    public void setDialog(JDialog myDialog) {
        this.myDialog = myDialog;
        myDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    }

    public void clear() {
        this.descriptionTF.setText("");
        this.nameTF.setText("");
        this.typeCB.setSelectedIndex(0);
    }

    public void showMe() {
        if (myDialog != null)
            myDialog.setVisible(true);
    }

    public void hideMe() {
        if (myDialog != null)
            myDialog.setVisible(false);
    }

    public boolean operationCanceled() {
        return operationCanceled;
    }

    public void setQuery(QueryType query) {
        this.query = query;
    }

    public QueryType getQuery() {
        return query;
    }

    private void save() {
        if (AcreStringUtil.isEmpty(getReturnVariableName())) {
            Main.showMainError("Return Variable Name cannot be empty", "Save Query Return Variable");
            return;
        }
        if (AcreStringUtil.isEmpty(getType())) {
            Main.showMainError("Return Variable Type must be selected", "Save Query Return Variable");
            return;
        }
        if (PDMQueryValidator.returnVariableExists(query, getReturnVariableName())) {
            Main.showMainError("Return Variable already exists for same name.\n" +
                    " Cannot create duplicate Return Variable.", "Save Query Return Variable");
            return;
        }

        operationCanceled = false;
        hideMe();
    }

    private void cancel() {
        operationCanceled = true;
        hideMe();
    }
}
