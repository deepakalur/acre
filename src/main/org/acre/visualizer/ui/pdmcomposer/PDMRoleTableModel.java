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
import org.acre.pdm.RoleType;
import org.acre.dao.PDMXMLConstants;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author  Administrator
 */
public class PDMRoleTableModel extends DefaultTableModel {

    private static String DEFAULT_TYPE = PDMXMLConstants.ROLE_TYPE_QUERY;

    private static Object[] columnNames =
        {"#",
         "Name",
         "Type",
         "Type Reference Name",
         "Query Name",
         "Return Variable Name"};

    private Class[] types = new Class [] {
                java.math.BigInteger.class, 
                java.lang.String.class, 
                java.lang.String.class, 
                java.lang.String.class, 
                java.lang.String.class,
                java.lang.String.class
            };

    private static final int SEQ_POS = 0;
    private static final int NAME_POS = 1;
    private static final int TYPE_POS = 2;
    private static final int TYPEREF_POS = 3;
    private static final int QUERYNAME_POS = 4;
    private static final int RETVAR_POS = 5;

    private PDMType currentPDM=null;

    /** Creates a new instance of PDMRoleTableModel */
    public PDMRoleTableModel(PDMType pdm) {
        super();
        this.currentPDM = pdm;
        this.setColumnIdentifiers(columnNames);
        this.setColumnCount(columnNames.length);
        initModel();
    }
    
    private void initModel() {
        if ((currentPDM ==null) ||
            (currentPDM.getRoles() == null) ||
            (currentPDM.getRoles().getRole() == null)) {

            return;
        }

        List roles = currentPDM.getRoles().getRole();

        Object[][] data;

        data = new Object[roles.size()][columnNames.length];
        
        for (int i=0; i < roles.size(); i++) {
            RoleType role = (RoleType) roles.get(i);
            //{"Sequence", "Name", "Type", "Type Reference Name", "Query Name"};    
            data[i][SEQ_POS] = (role.getSequence()==null?new BigInteger(Integer.toString(0)):role.getSequence());
            data[i][NAME_POS] = (role.getName()==null?"":role.getName());
            data[i][TYPE_POS] = (role.getType()==null?"":role.getType());
            data[i][TYPEREF_POS] = (role.getTypeReferenceName()==null?"":role.getTypeReferenceName());
            data[i][QUERYNAME_POS] = (role.getQueryName()==null?"":role.getQueryName());
            data[i][RETVAR_POS] = (role.getReturnVariableName()==null?"":role.getReturnVariableName()); 
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
        PDMRoleTableModel model = new PDMRoleTableModel(null);
        JTable table = new JTable();
        table.setModel(model);
        JScrollPane scroll = new JScrollPane();
        scroll.setViewportView(table);
        JFrame frame = new JFrame();
        frame.getContentPane().add(scroll);
        frame.pack();
        frame.setVisible(true);        
    }

    // todo - refactor - move to base class
//    public List getRoles() {
//        List rolesInTable = new ArrayList();
//        for (int rowNum = 0; rowNum < getRowCount(); rowNum ++) {
//            String sequence = getValueAt(rowNum, SEQ_POS).toString();
//            String name = getValueAt(rowNum, NAME_POS).toString();
//            String type = getValueAt(rowNum, TYPE_POS).toString();
//            String typeReferenceName = getValueAt(rowNum, TYPEREF_POS).toString();
//            String queryName = getValueAt(rowNum, QUERYNAME_POS).toString();
//            String returnVariableName = getValueAt(rowNum, RETVAR_POS).toString();
//
//            RoleType role =
//                    PatternRepository.getInstance().createRole(
//                            name,
//                            type,
//                            sequence,
//                            typeReferenceName,
//                            queryName,
//                            returnVariableName,
//                            null // arguments are not here...
//                            );
//            rolesInTable.add(role);
//        }
//
//        return rolesInTable;
//    }

//    public void addNewRow() {
//        addRow(
//                Integer.toString(getRowCount()),
//                "New Role",
//                DEFAULT_TYPE,
//                "",
//                "",
//                "");
//    }
//
//    public void addRow(
//            String seq,
//            String name,
//            String type,
//            String typeReferenceName,
//            String queryName,
//            String returnVariableName
//            ) {
//
//        RoleType newRole = null;
//        newRole = PatternRepository.getInstance()
//                    .createRole(
//                            name,
//                            type,
//                            seq,
//                            typeReferenceName,
//                            queryName,
//                            returnVariableName,
//                            null
//                            );
//
//        currentPDM.getRoles().getRole().add(newRole);
//        addRow(new Object[] {seq, name, type, typeReferenceName, queryName, returnVariableName});
//    }

//    public void removeRow(int rowNum) {
//        String sequence;
//        String name;
//        String type;
//        String typeReferenceName;
//        String queryName;
//        String returnVariableName;
//
//        sequence = getValueAt(rowNum, SEQ_POS).toString();
//        name = getValueAt(rowNum, NAME_POS).toString();
//        type = getValueAt(rowNum, TYPE_POS).toString();
//        typeReferenceName = getValueAt(rowNum, TYPEREF_POS).toString();
//        queryName = getValueAt(rowNum, QUERYNAME_POS).toString();
//        returnVariableName = getValueAt(rowNum, RETVAR_POS).toString();
//
//        List list = currentPDM.getRoles().getRole();
//        int foundRow = -1;
//        for (int i=0; i < list.size(); i++) {
//            RoleType role = (RoleType) list.get(i);
//            if ((name != null) && (name.equalsIgnoreCase(role.getName()))) {
//                foundRow = i;
//                break;
//            }
//            if ((sequence != null) && (sequence.equals(role.getSequence().toString()))) {
//                foundRow = i;
//                break;
//            }
//        }
//
//        if (foundRow != -1) {
//            currentPDM.getRoles().getRole().remove(foundRow);
//            super.removeRow(rowNum);
//        }
//
//    }



//    public PDMType getCurrentPDM() {
//        return currentPDM;
//    }

    public String getSelectedRoleName(int selectedRow) {
        if (getValueAt(selectedRow, NAME_POS) != null)
            return getValueAt(selectedRow, NAME_POS).toString();
        else
            return null;
    }

    public int getRowNumberFor(RoleType newRole) {
        for (int i=0; i < getRowCount(); i++) {
            String roleName = getValueAt(i, NAME_POS).toString();
            if (roleName.equalsIgnoreCase(newRole.getName()))
                return i;
        }

        return -1;
    }
}
