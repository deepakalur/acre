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

import org.acre.pdm.PDMType;
import org.acre.pdm.RelationshipType;
import org.acre.dao.PDMXMLConstants;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 * @author Deepak.Alur@Sun.com
 *         Date: Oct 21, 2004
 *         Time: 12:18:59 PM
 */
public class PDMRelationshipTableModel extends DefaultTableModel {

    private static Object[] columnNames =
        {
            "Name",
            "Type",
            "From Role",
            "To Role"
        };

    private static int NAME_POS =0;
    private static int TYPE_POS=1;
    private static int FROMROLE_POS=2;
    private static int TOROLE_POS=3;

    private Class[] types = new Class [] {
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class
            };


    private PDMType currentPDM;
    private static final String DEFAULT_TYPE = PDMXMLConstants.RELATIONSHIP_TYPE_INSTANTIATES;

    /** Creates a new instance of PDMRoleTableModel */
    public PDMRelationshipTableModel(PDMType pdm) {
        super();
        currentPDM = pdm;
        this.setColumnIdentifiers(columnNames);
        this.setColumnCount(columnNames.length);
        initModel();
    }

    private void initModel() {
        if ((currentPDM ==null) ||
            (currentPDM.getRelationships() == null) ||
            (currentPDM.getRelationships().getRelationship() == null)) {

            return;
        }

        List relationships = currentPDM.getRelationships().getRelationship();

        Object[][] data;

        data = new Object[relationships.size()][columnNames.length];

        for (int i=0; i < relationships.size(); i++) {
            RelationshipType rel = (RelationshipType) relationships.get(i);

            data[i][NAME_POS] = rel.getName();
            data[i][TYPE_POS] = rel.getType();
            data[i][FROMROLE_POS] = rel.getFromRole();
            data[i][TOROLE_POS] = rel.getToRole();
        }

        this.setDataVector(data, columnNames);
    }

     public Class getColumnClass(int columnIndex) {
        if (columnIndex > getColumnCount()) {
            throw new RuntimeException("Attempting to get Column " + columnIndex + " out of " + getColumnCount() + "columns.");
        }

        return super.getColumnClass(columnIndex);

     }

    public String getColumnName(int col) {
        if (col > getColumnCount()) {
            throw new RuntimeException("Attempting to get Column " + col + " out of " + getColumnCount() + "columns.");
        }
        return super.getColumnName(col);
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public boolean isCellEditable(int row, int col) {
        return false;
    }

    public static  void main (String args[]) {
        PDMRelationshipTableModel model = new PDMRelationshipTableModel (null);
        JTable table = new JTable();
        table.setModel(model);
        JScrollPane scroll = new JScrollPane();
        scroll.setViewportView(table);
        JFrame frame = new JFrame();
        frame.getContentPane().add(scroll);
        frame.pack();
        frame.setVisible(true);
    }

//    public List getRelationships() {
//        List relsInTable = new ArrayList();
//        for (int rowNum = 0; rowNum < getRowCount(); rowNum ++) {
//            String name = getValueAt(rowNum, NAME_POS).toString();
//            String type = getValueAt(rowNum, TYPE_POS).toString();
//            String fromRole = getValueAt(rowNum, FROMROLE_POS).toString();
//            String toRole = getValueAt(rowNum, TOROLE_POS).toString();
//
//            RelationshipType rel =
//                    PatternRepository.getInstance()
//                    .createRelationship(
//                            name,
//                            type,
//                            fromRole,
//                            toRole
//                    );
//            relsInTable .add(rel);
//        }
//
//        return relsInTable ;
//    }

//    public void addRow(String name, String type, String fromRole, String toRole) {
//        addRow(new Object[] {
//            name,
//            type,
//            fromRole,
//            toRole
//        });
//    }
//
//    public void addNewRow() {
//        addRow("NewRelation", DEFAULT_TYPE, "", "");
//    }

//    public void removeRow(int rowNum) {
//        String name = getValueAt(rowNum, NAME_POS).toString();
//        String type = getValueAt(rowNum, TYPE_POS).toString();
//        String fromRole = getValueAt(rowNum, FROMROLE_POS).toString();
//        String toRole = getValueAt(rowNum, TOROLE_POS).toString();
//        super.removeRow(rowNum);
//    }


//    public PDMType getCurrentPDM() {
//        return currentPDM;
//    }
//
//    public void setCurrentPDM(PDMType currentPDM) {
//        this.currentPDM = currentPDM;
//    }

    public String getSelectedRelationshipName(int rowNum) {
        if (getValueAt(rowNum, NAME_POS) != null)
            return getValueAt(rowNum, NAME_POS).toString();
        else return null;
    }

    public int getRowNumberFor(RelationshipType newRel) {
        for (int i=0; i < getRowCount(); i++) {
            String relName = getValueAt(i, NAME_POS).toString();
            if (relName.equalsIgnoreCase(newRel.getName()))
                return i;
        }

        return -1;
    }
}
