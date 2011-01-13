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

import org.acre.pdmqueries.ArgumentType;
import org.acre.pdmqueries.QueryType;
import org.acre.pdmqueries.ReturnVariableType;
import org.acre.common.AcreErrors;
import org.acre.common.AcreRuntimeException;
import org.acre.config.ConfigService;
import org.acre.dao.DAOFactory;
import org.acre.dao.PDMQueryValidator;
import org.acre.dao.PDMXMLConstants;
import org.acre.dao.PatternQueryRepository;
import org.acre.visualizer.ui.Main;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author  Administrator
 */
public class PQLEditorForm extends javax.swing.JPanel {

    /** Creates new form PQLEditorForm */
    public PQLEditorForm() {
        patternQueryRepository = DAOFactory.getPatternQueryRepository();
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        fieldsPanel = new javax.swing.JPanel();
        nameLabel = new javax.swing.JLabel();
        nameTF = new javax.swing.JFormattedTextField();
        relativeFilePathLabel = new javax.swing.JLabel();
        relativeFilePathTF = new javax.swing.JTextField();
        languageLabel = new javax.swing.JLabel();
        typeLabel = new javax.swing.JLabel();
        languageCB = new javax.swing.JComboBox();
        typeCB = new javax.swing.JComboBox();
        descriptionLabel = new javax.swing.JLabel();
        descScroll = new javax.swing.JScrollPane();
        descriptionTF = new javax.swing.JTextArea();
        queryDetailLabel = new javax.swing.JLabel();
        queryScroll = new javax.swing.JScrollPane();
        queryScriptTF = new javax.swing.JTextPane();
        argPanel = new javax.swing.JPanel();
        argScroll = new javax.swing.JScrollPane();
        argTable = new javax.swing.JTable();
        argButtonPanel = new javax.swing.JPanel();
        addArgButton = new javax.swing.JButton();
        deleteArgButton = new javax.swing.JButton();
        retPanel = new javax.swing.JPanel();
        rvscrollPane = new javax.swing.JScrollPane();
        returnVariableTable = new javax.swing.JTable();
        varButtonPanel = new javax.swing.JPanel();
        addVarButton = new javax.swing.JButton();
        deleteVarButton = new javax.swing.JButton();
        buttonPanel = new javax.swing.JPanel();
        saveButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        setMinimumSize(new java.awt.Dimension(500, 600));
        setPreferredSize(new java.awt.Dimension(800, 600));
        fieldsPanel.setLayout(new java.awt.GridBagLayout());

        fieldsPanel.setMinimumSize(new java.awt.Dimension(800, 120));
        fieldsPanel.setPreferredSize(new java.awt.Dimension(800, 120));
        nameLabel.setText("Query Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 5);
        fieldsPanel.add(nameLabel, gridBagConstraints);

        initQueryNameTF();
        nameTF.setColumns(20);
        nameTF.setToolTipText("Unique Query Name for this query");
        nameTF.setNextFocusableComponent(typeCB);
        nameTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameTFActionPerformed(evt);
            }
        });
        nameTF.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                queryNameFocusLostEvent(evt);
            }
        });
        nameTF.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                queryNameKeyTypedEvent(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 2.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 0);
        fieldsPanel.add(nameTF, gridBagConstraints);

        relativeFilePathLabel.setText("Relative Query File Path");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 5);
        fieldsPanel.add(relativeFilePathLabel, gridBagConstraints);

        relativeFilePathTF.setColumns(25);
        relativeFilePathTF.setEditable(false);
        relativeFilePathTF.setToolTipText("(Automatic) File where the Script will be stored");
        relativeFilePathTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                relativeFilePathTFActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 2.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 0);
        fieldsPanel.add(relativeFilePathTF, gridBagConstraints);

        languageLabel.setText("Query Language");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 5);
        fieldsPanel.add(languageLabel, gridBagConstraints);

        typeLabel.setText("Query Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 5);
        fieldsPanel.add(typeLabel, gridBagConstraints);

        initQueryLanguageCB();
        languageCB.setToolTipText("Choose a Language");
        languageCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                languageCBActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 0);
        fieldsPanel.add(languageCB, gridBagConstraints);

        initQueryTypeCB();
        typeCB.setToolTipText("Choose a Query Type");
        typeCB.setNextFocusableComponent(languageCB);
        typeCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                typeCBActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 0);
        fieldsPanel.add(typeCB, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 2.0;
        add(fieldsPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 5);
        add(descriptionLabel, gridBagConstraints);

        descScroll.setBorder(new javax.swing.border.TitledBorder("Query Description"));
        descScroll.setMinimumSize(new java.awt.Dimension(800, 80));
        descScroll.setPreferredSize(new java.awt.Dimension(800, 80));
        descriptionTF.setColumns(25);
        descriptionTF.setRows(3);
        descScroll.setViewportView(descriptionTF);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 2.0;
        add(descScroll, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        add(queryDetailLabel, gridBagConstraints);

        queryScroll.setBorder(new javax.swing.border.TitledBorder("Query Script"));
        queryScroll.setMinimumSize(new java.awt.Dimension(800, 160));
        queryScroll.setPreferredSize(new java.awt.Dimension(800, 160));
        queryScriptTF.setToolTipText("Query Script with one or more statements and ending in a return statement.");
        queryScroll.setViewportView(queryScriptTF);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 2.0;
        add(queryScroll, gridBagConstraints);

        argPanel.setLayout(new java.awt.BorderLayout());

        argPanel.setBorder(new javax.swing.border.TitledBorder("Input Arguments (Dynamic)"));
        argPanel.setMinimumSize(new java.awt.Dimension(400, 150));
        argPanel.setPreferredSize(new java.awt.Dimension(400, 150));
        argScroll.setPreferredSize(new java.awt.Dimension(200, 150));
        argTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Name", "Type", "Related Query Name", "Return Query Variable", "Value", "Description"
            }
        ));
        argTable.setPreferredSize(new java.awt.Dimension(200, 150));
        argScroll.setViewportView(argTable);

        argPanel.add(argScroll, java.awt.BorderLayout.CENTER);

        argButtonPanel.setPreferredSize(new java.awt.Dimension(75, 35));
        addArgButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/acre/visualizer/ui/icons/buttons/NewButton.gif")));
        addArgButton.setBorderPainted(false);
        addArgButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        addArgButton.setRolloverEnabled(true);
        addArgButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addArgButtonActionPerformed(evt);
            }
        });

        argButtonPanel.add(addArgButton);

        deleteArgButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/acre/visualizer/ui/icons/buttons/DeleteButton.gif")));
        deleteArgButton.setBorderPainted(false);
        deleteArgButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        deleteArgButton.setRolloverEnabled(true);
        deleteArgButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteArgButtonActionPerformed(evt);
            }
        });

        argButtonPanel.add(deleteArgButton);

        argPanel.add(argButtonPanel, java.awt.BorderLayout.SOUTH);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(argPanel, gridBagConstraints);

        retPanel.setLayout(new java.awt.BorderLayout());

        retPanel.setBorder(new javax.swing.border.TitledBorder("Return Variables for Query"));
        retPanel.setMinimumSize(new java.awt.Dimension(400, 150));
        retPanel.setPreferredSize(new java.awt.Dimension(400, 150));
        rvscrollPane.setToolTipText("List of return variables must match the return statement in the Query Script.");
        rvscrollPane.setFocusable(false);
        rvscrollPane.setPreferredSize(new java.awt.Dimension(200, 150));
        initReturnVariableTable(null);
        returnVariableTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Type", "Description"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        returnVariableTable.setFocusable(false);
        returnVariableTable.setPreferredSize(new java.awt.Dimension(200, 150));
        rvscrollPane.setViewportView(returnVariableTable);

        retPanel.add(rvscrollPane, java.awt.BorderLayout.CENTER);

        varButtonPanel.setPreferredSize(new java.awt.Dimension(75, 35));
        addVarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/acre/visualizer/ui/icons/buttons/NewButton.gif")));
        addVarButton.setBorderPainted(false);
        addVarButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        addVarButton.setRolloverEnabled(true);
        addVarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addVarButtonActionPerformed(evt);
            }
        });

        varButtonPanel.add(addVarButton);

        deleteVarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/acre/visualizer/ui/icons/buttons/DeleteButton.gif")));
        deleteVarButton.setBorderPainted(false);
        deleteVarButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        deleteVarButton.setRolloverEnabled(true);
        deleteVarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteVarButtonActionPerformed(evt);
            }
        });

        varButtonPanel.add(deleteVarButton);

        retPanel.add(varButtonPanel, java.awt.BorderLayout.SOUTH);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        add(retPanel, gridBagConstraints);

        buttonPanel.setMinimumSize(new java.awt.Dimension(800, 35));
        buttonPanel.setPreferredSize(new java.awt.Dimension(800, 35));
        saveButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/acre/visualizer/ui/icons/buttons/SaveButton.gif")));
        saveButton.setMnemonic('S');
        saveButton.setToolTipText("Save Query Edits");
        saveButton.setBorderPainted(false);
        saveButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        saveButton.setNextFocusableComponent(cancelButton);
        saveButton.setRolloverEnabled(true);
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(saveButton);

        cancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/acre/visualizer/ui/icons/buttons/CancelButton.gif")));
        cancelButton.setMnemonic('C');
        cancelButton.setToolTipText("Cancel Query Edits");
        cancelButton.setBorderPainted(false);
        cancelButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        cancelButton.setNextFocusableComponent(nameTF);
        cancelButton.setRolloverEnabled(true);
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(cancelButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 2.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        add(buttonPanel, gridBagConstraints);

    }//GEN-END:initComponents

    private void deleteArgButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteArgButtonActionPerformed
        deleteQueryArgument();
    }//GEN-LAST:event_deleteArgButtonActionPerformed


    private void addArgButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addArgButtonActionPerformed
        addQueryArgument();
    }//GEN-LAST:event_addArgButtonActionPerformed

    private void deleteVarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteVarButtonActionPerformed
        deleteQueryReturnVariable();

    }//GEN-LAST:event_deleteVarButtonActionPerformed


    private void addVarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addVarButtonActionPerformed

        addQueryReturnVariable();

    }//GEN-LAST:event_addVarButtonActionPerformed

    private void queryNameFocusLostEvent(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_queryNameFocusLostEvent
        setRelativeFilePath();
    }//GEN-LAST:event_queryNameFocusLostEvent

    private void queryNameKeyTypedEvent(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_queryNameKeyTypedEvent
        char c = evt.getKeyChar();
//        setRelativeFilePath();
    }//GEN-LAST:event_queryNameKeyTypedEvent

    private void nameTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameTFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nameTFActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        cancel();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        save();
    }//GEN-LAST:event_saveButtonActionPerformed

    private void languageCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_languageCBActionPerformed
        setRelativeFilePath();
    }//GEN-LAST:event_languageCBActionPerformed

    private void typeCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_typeCBActionPerformed
        setRelativeFilePath();
    }//GEN-LAST:event_typeCBActionPerformed

    private void relativeFilePathTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_relativeFilePathTFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_relativeFilePathTFActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addArgButton;
    private javax.swing.JButton addVarButton;
    private javax.swing.JPanel argButtonPanel;
    private javax.swing.JPanel argPanel;
    private javax.swing.JScrollPane argScroll;
    private javax.swing.JTable argTable;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton deleteArgButton;
    private javax.swing.JButton deleteVarButton;
    private javax.swing.JScrollPane descScroll;
    private javax.swing.JLabel descriptionLabel;
    private javax.swing.JTextArea descriptionTF;
    private javax.swing.JPanel fieldsPanel;
    private javax.swing.JComboBox languageCB;
    private javax.swing.JLabel languageLabel;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JFormattedTextField nameTF;
    private javax.swing.JLabel queryDetailLabel;
    private javax.swing.JTextPane queryScriptTF;
    private javax.swing.JScrollPane queryScroll;
    private javax.swing.JLabel relativeFilePathLabel;
    private javax.swing.JTextField relativeFilePathTF;
    private javax.swing.JPanel retPanel;
    private javax.swing.JTable returnVariableTable;
    private javax.swing.JScrollPane rvscrollPane;
    private javax.swing.JButton saveButton;
    private javax.swing.JComboBox typeCB;
    private javax.swing.JLabel typeLabel;
    private javax.swing.JPanel varButtonPanel;
    // End of variables declaration//GEN-END:variables


    private QueryType currentQuery;
    private QueryType originalQuery;

    private PQLQueryVariableTableModel returnVariableTableModel;
    private PQLArgumentTableModel argTableModel;

    boolean updated = false;
    private PQLReturnVariableForm returnVariableForm=null;
    private PQLArgumentForm argumentForm = null;
    private JDialog argumentDialog;
    private JDialog returnVariableDialog;
    private JFrame parentFrame=null;
    private JDialog myDialog;
    private boolean isNew=true;
    private boolean operationCanceled = false;
    private PatternQueryRepository patternQueryRepository;

    public JFrame getParentFrame() {
        return parentFrame;
    }

    public void setParentFrame(JFrame parentFrame) {
        this.parentFrame = parentFrame;
    }

    public void setQuery(QueryType query, boolean newQuery) {
        this.isNew = newQuery;
        this.originalQuery = query;
        currentQuery = patternQueryRepository.cloneQuery(query);

        if (query == null) {
            throw new AcreRuntimeException("Cannot set null Query into PQL Editor");
        }

        this.currentQuery = query;
        this.nameTF.setText(query.getName());
        this.relativeFilePathTF.setText(query.getRelativeFilePath());
        languageCB.setSelectedItem(query.getLanguage());
        typeCB.setSelectedItem(query.getType());
        query.getRelativeFilePath();
        this.descriptionTF.setText(query.getDescription());

        //todo - use relativeFilePathTF or facade.getGlobalQueryFile ?
        if (!newQuery) {
            this.queryScriptTF.setText(
                patternQueryRepository.getGlobalQueryFile(query.getName()));
        } else {
            // this.queryScriptTF.setText("");
        }

        initReturnVariableTable(query);
        initQueryArgumentTable(query);
    }

    private void setLanguageCBSelected(String language) {
        languageCB.setSelectedItem(language);
    }

    private void initQueryTypeCB() {
        Object [] values = PDMQueryValidator.getInstance().getQueryTypes();
        javax.swing.DefaultComboBoxModel model = new javax.swing.DefaultComboBoxModel(values);
        typeCB.setModel(model);                
    }

    private void initQueryLanguageCB() {
        Object [] values = PDMQueryValidator.getInstance().getQueryLanguages();        
        javax.swing.DefaultComboBoxModel model = new javax.swing.DefaultComboBoxModel(values);
        languageCB.setModel(model);
    }
    
    private void initReturnVariableTable(QueryType query) {        
        if (query ==null) return;
        returnVariableTableModel = new PQLQueryVariableTableModel(query);
        returnVariableTable.setModel(returnVariableTableModel);
    }

    private void initQueryArgumentTable(QueryType query) {
        if (query ==null) return;
        argTableModel = new PQLArgumentTableModel(query);
        argTable.setModel(argTableModel);
    }

    private String getFileExtension() {
        String lang = null;
        if (this.languageCB.getSelectedItem() == null) {
            return "";
        }
        lang = languageCB.getSelectedItem().toString();

        if (PDMXMLConstants.QUERY_LANGUAGE_GROOVY.equalsIgnoreCase(lang))
            return PDMXMLConstants.GROOVY_FILE_EXTENSION;
        else if (PDMXMLConstants.QUERY_LANGUAGE_PQL.equalsIgnoreCase(lang))
            return PDMXMLConstants.PQL_FILE_EXTENSION;

        return "";
    }

    private void setRelativeFilePath() {
        if (nameTF.getText() != null) {
            String name = nameTF.getText() + getFileExtension();
            name = name.replaceAll("\\s", "" );
            this.relativeFilePathTF.setText(
//                ConfigService.getInstance().getGlobalPDMQueryDetailsDirPath() +
//                ConfigService.FILE_SEPARATOR +
                name
                    );
            this.relativeFilePathTF.setToolTipText(
                  "Absolute Path: " +
                  ConfigService.getInstance().getGlobalPDMQueryDetailsDirPath() +
                  ConfigService.FILE_SEPARATOR +
                  name
            );
            this.relativeFilePathTF.revalidate();
        } else {
            this.relativeFilePathTF.setText("Query Name Not set");
        }
    }

    private void initQueryNameTF() {
        nameTF.setText("");
    }


    private void save() {
        try {
            if (currentQuery != null) {
                currentQuery.setName(nameTF.getText());
                currentQuery.setDescription(descriptionTF.getText());
                currentQuery.setType(typeCB.getSelectedItem().toString());
                currentQuery.setLanguage(languageCB.getSelectedItem().toString());
                currentQuery.setRelativeFilePath(relativeFilePathTF.getText());
                String queryScript = this.queryScriptTF.getText();

                AcreErrors errors;
                errors = PDMQueryValidator.getInstance().validateQuery(currentQuery);
                if (!errors.isEmpty()) {
                    Main.showMainError("Query Validation Failed:\n" +
                            errors.toString(), "Save Query");
                    return;
                }

                if (isNew) {
                    patternQueryRepository.insertGlobalQuery(currentQuery);
                } else {
                    patternQueryRepository.updateGlobalQuery(originalQuery, currentQuery);
                }

                // save query script
                patternQueryRepository.saveGlobalScript(currentQuery, queryScript);

                // save query
                patternQueryRepository.save();

                isNew = false;
                hideMe();
                operationCanceled = false;
                setUpdated(false);
            }
        } catch (Throwable t) {
            JOptionPane.showMessageDialog(this,  t.getMessage(),
                    "Save Query Error",
                    JOptionPane.ERROR_MESSAGE);
            t.printStackTrace();

        }
    }
    
    public void setUpdated(boolean updated) {
        this.updated = updated;
    }
    
    public boolean isUpdated() {
        return updated;
    }

    public static void main(String args[]) {
        JFrame f = new JFrame();
        PQLEditorForm form = new PQLEditorForm();
        form.setParentFrame(f);

        form.setQuery((QueryType)
                DAOFactory.getPatternQueryRepository().getGlobalQueryList().get(0),
                false);
        f.getContentPane().add(form);
        f.pack();
        f.setVisible(true);
    }


    public JDialog getDialog() {
        return myDialog;
    }

    public void setDialog(JDialog dialog) {
        this.myDialog = dialog;
        myDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    }

    public void showMe() {
        if (myDialog != null) {
            myDialog.show();
        }
    }

    public void hideMe() {
        if (myDialog != null) {
            myDialog.hide();
        }
    }

    public void clear() {
        nameTF.setText("");
        relativeFilePathTF.setText("");
        descriptionTF.setText("");
        queryScriptTF.setText("");

        if (argumentForm != null)
            argumentForm.clear();
        if (returnVariableForm != null)
            returnVariableForm.clear();

        returnVariableTableModel = new PQLQueryVariableTableModel(null);
        argTableModel = new PQLArgumentTableModel(null);
        argTable.setModel(argTableModel);
        returnVariableTable.setModel(returnVariableTableModel);
        currentQuery = null;
        isNew = false;
    }

    private void deleteQueryArgument() {

        int selectedRow = argTable.getSelectedRow();
        if (selectedRow == -1) {
            Main.showMainError("Query Argument not selected to delete.\nPlease select and retry.",
                    "Delete Query Argument");
            return;
        }

        String argName = argTableModel.getSelectedArgumentName(selectedRow);

        PDMQueryValidator.deleteArgumentFromQuery(currentQuery, argName);

        argTableModel.removeRow(selectedRow);
        argTable.changeSelection(
            argTable.getRowCount()-1,
            argTable.getColumnCount(), false, false);
    }


    private void addQueryArgument() {

        initQueryArgumentForm();
        argumentForm.clear();
        argumentForm.setSequence(Integer.toString(
                argTable.getRowCount()));

        argumentForm.setQuery(currentQuery);

        argumentForm.showMe();


        if (argumentForm.operationCanceled())
            return;

        String name = argumentForm.getArgumentName();
        String desc = argumentForm.getDescription();
        String type = argumentForm.getType();
        String value = argumentForm.getValue();
        String seq = argumentForm.getSequence();
        String relatedQuery = argumentForm.getRelatedQueryName();
        String relatedVar = argumentForm.getRelatedVariableName();

        ArgumentType newArg = patternQueryRepository.createArgument(
                seq,
                name,
                type,
                value,
                desc,
                relatedQuery,
                relatedVar);

        if (PDMQueryValidator.argumentExists(currentQuery, newArg.getName())) {
            Main.showMainError("Argument already exists, cannot add duplicate argument",
                    "Add Query Argument");
            return;
        }

        currentQuery.getArgument().add(newArg);
        setUpdated(true);

        initQueryArgumentTable(currentQuery);
    }

    private void initQueryArgumentForm() {
        if (this.argumentForm == null) {
            argumentForm = new PQLArgumentForm();
        }

        if (this.argumentDialog == null) {
            if (getParentFrame() != null)
                argumentDialog = new JDialog(getParentFrame(), true);
            else
                argumentDialog = new JDialog();

            argumentDialog.setContentPane(argumentForm);
            argumentDialog.setTitle("Query Arguments");
            argumentDialog.setVisible(false);
            argumentDialog.pack();
            argumentForm.setDialog(argumentDialog);
            if (getParentFrame() != null)
                argumentDialog.setLocationRelativeTo(getParentFrame());

//            System.out.println("Created Arguments Dialog");
        }

    }

    private void deleteQueryReturnVariable() {
        int selectedRow = returnVariableTable.getSelectedRow();
        if (selectedRow == -1) {
            Main.showMainError("Query Return Variable not selected to delete.\nPlease select and retry.",
                    "Delete Query Return Variable");
            return;
        }

        String varName = returnVariableTableModel.getSelectedReturnVariableName(selectedRow);

        PDMQueryValidator.deleteReturnVariableFromQuery(currentQuery, varName);

        returnVariableTableModel.removeRow(selectedRow);
        returnVariableTable.changeSelection(
            returnVariableTable.getRowCount()-1,
            returnVariableTable.getColumnCount(), false, false);
    }

    private void addQueryReturnVariable() {
        initReturnVariableForm();

        returnVariableForm.clear();
        returnVariableForm.setQuery(currentQuery);

        returnVariableForm.showMe();

        if (returnVariableForm.operationCanceled())
            return;

        String name = returnVariableForm.getReturnVariableName();
        String desc = returnVariableForm.getDescription();
        String type = returnVariableForm.getType();

        ReturnVariableType newVar = patternQueryRepository.createReturnVariable(name, type, desc);

        if (PDMQueryValidator.returnVariableExists(currentQuery, newVar.getName())) {
            Main.showMainError("Return Variable already exists, cannot add duplicate.",
                    "Add Query Return Variable");
            return;
        }

        currentQuery.getReturnVariable().add(newVar);
        setUpdated(true);

        initReturnVariableTable(currentQuery);

    }

    private void initReturnVariableForm() {
        if (this.returnVariableForm == null) {
            returnVariableForm = new PQLReturnVariableForm();
        }

        if (this.returnVariableDialog == null) {
            returnVariableDialog = new JDialog(getParentFrame(), true);
            returnVariableDialog.setContentPane(returnVariableForm);
            returnVariableDialog.setTitle("Query Return Variables");
            returnVariableDialog.setVisible(false);
            returnVariableDialog.pack();
            returnVariableForm.setDialog(returnVariableDialog);
            if (getParentFrame() != null)
                returnVariableDialog.setLocationRelativeTo(getParentFrame());
//            System.out.println("Created Return Variable Dialog");
        }
    }

    public boolean isOperationCanceled() {
        return operationCanceled;
    }

    private void cancel() {
        operationCanceled = true;
        hideMe();
    }

    public void setQueryScript(String text) {
        this.queryScriptTF.setText(text);
    }
}