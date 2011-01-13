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
import org.acre.pdmqueries.ReturnVariableType;
import org.acre.dao.PDMXMLConstants;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 *
 * @author  Deepak.Alur@sun.com
 */
public class PQLQueryVariableTableModel extends DefaultTableModel {

    private static String DEFAULT_TYPE = PDMXMLConstants.QUERY_RETURN_VARIABLE_TYPE_ANY;

    private static Object[] columnNames =
        {"Name", "Type", "Description"};    

    private Class[] types = new Class [] {
                java.lang.String.class, 
                java.lang.String.class, 
                java.lang.String.class, 
            };

    private static final int NAME_POS = 0;
    private static final int TYPE_POS = 1;
    private static final int DESC_POS = 2;

    private QueryType currentQuery =null;
    
    /** Creates a new instance of PQLQueryVariableTableModel */
    public PQLQueryVariableTableModel(QueryType query) {
        super();
        this.currentQuery = query;
        this.setColumnIdentifiers(columnNames);
        this.setColumnCount(columnNames.length);
        initModel();
    }

    /**
     * initializes the model and loads the return variables if any
     * into the table
     */
    private void initModel() {
         if ((currentQuery ==null) ||
             (currentQuery.getReturnVariable() == null)) {
             return;
         }


         List retVars = currentQuery.getReturnVariable();

         Object[][] data = new Object[retVars.size()][columnNames.length];

        
         for (int i=0; i < retVars.size(); i++) {
             ReturnVariableType var = (ReturnVariableType) retVars.get(i);
                            
             data[i][NAME_POS] = (var.getName()==null?"":var.getName());
             data[i][TYPE_POS] = (var.getType()==null?"":var.getType());
             data[i][DESC_POS] = (var.getDescription()==null?"":var.getDescription());
         }

         this.setDataVector(data, columnNames);
     }

    /**
     * returns the correct class for the given column
     * @param columnIndex
     * @return
     */
     public Class getColumnClass(int columnIndex) {
        if (columnIndex > getColumnCount()) {
            throw new RuntimeException("Attempting to get Column " + columnIndex + " out of " + getColumnCount() + "columns.");
        }

        return super.getColumnClass(columnIndex);
     }

    /**
     * returns the column name used in header display for the table
     * @param col
     * @return
     */
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
        PQLQueryVariableTableModel model = new PQLQueryVariableTableModel (null);
        JTable table = new JTable();
        table.setModel(model);
        JScrollPane scroll = new JScrollPane();
        scroll.setViewportView(table);
        JFrame frame = new JFrame();
        frame.getContentPane().add(scroll);
        frame.pack();
        frame.setVisible(true);        
    }


//    /**
//     * This method creates a list of ReturnVariable instances based ont
//     * the current values in the table. This List is then set into the
//     * QueryType instance
//     * @param facade
//     * @return
//     */
//    public List getReturnVariables(PatternQueryRepository facade) {
//        List rowsInTable = new ArrayList();
//        for (int rowNum = 0; rowNum < getRowCount(); rowNum ++) {
//            String name = getValueAt(rowNum, NAME_POS).toString();
//            String type = getValueAt(rowNum, TYPE_POS).toString();
//            String description = getValueAt(rowNum, DESC_POS).toString();
//
//            ReturnVariableType var= facade.createReturnVariable(name, type, description);
//            rowsInTable.add(var);
//        }
//
//        return rowsInTable;
//    }

//    public void addNewReturnVariable(PatternQueryRepository facade ) {
//        addReturnVariable(facade, "ReturnVariableName", DEFAULT_TYPE, "Description" );
//    }
//
//    public void addReturnVariable(PatternQueryRepository facade, String name, String type, String description) {
//
//        checkDuplicate(name, type, description);
//
//        ReturnVariableType newRow = null;
//        newRow = facade.createReturnVariable(name, type, description);
//
//        currentQuery.getReturnVariable().add(newRow);
//        addRow(new Object[] {name, type, description});
//    }
//
//    private void checkDuplicate(String name, String type, String description) {
//        for (int rowNum = 0; rowNum < getRowCount(); rowNum ++) {
//            String rowname = getValueAt(rowNum, NAME_POS).toString();
//            // String rowtype = getValueAt(rowNum, TYPE_POS).toString();
//            // String rowdescription = getValueAt(rowNum, DESC_POS).toString();
//
//            if (rowname.equalsIgnoreCase(name)) {
//                throw new RuntimeException("Attempting to insert duplicate return variable.");
//            }
//        }
//    }

//    public void removeRow(int rowNum) {
//        String name = getValueAt(rowNum, NAME_POS).toString();
//        String type = getValueAt(rowNum, TYPE_POS).toString();
//        String description = getValueAt(rowNum, DESC_POS).toString();
//
//        List list = currentQuery.getReturnVariable();
//        int foundRow = -1;
//        for (int i=0; i < list.size(); i++) {
//            ReturnVariableType var = (ReturnVariableType) list.get(i);
//            if (var.getName().equalsIgnoreCase(name)) {
//                foundRow = i;
//                break;
//            }
//        }
//
//        if (foundRow != -1) {
//            currentQuery.getReturnVariable().remove(foundRow);
//            super.removeRow(rowNum);
//        }
//
//    }
//    public void setCurrentQuery(QueryType query) {
//        currentQuery = query;
//    }
//
//    public QueryType getCurrentQuery() {
//        return currentQuery;
//    }

    public String getSelectedReturnVariableName(int selectedRow) {
        return getValueAt(selectedRow, NAME_POS).toString();
    }
}
