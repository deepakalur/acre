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
import org.acre.dao.PDMXMLConstants;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 * @author Deepak.Alur@Sun.com
 * @version Nov 5, 2004 12:25:28 PM
 */

public class PQLArgumentTableModel extends DefaultTableModel {

    private static String DEFAULT_TYPE = PDMXMLConstants.ROLE_TYPE_QUERY;

    private static Object[] columnNames = {
         "#",
         "Name",
         "Type",
         "Related Query Name",
         "Related Query Variable",
         "Value",
         "Description"
    };

    private Class[] types = new Class [] {
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class
            };


    private static final int SEQ_POS = 0;
    private static final int NAME_POS = 1;
    private static final int TYPE_POS = 2;
    private static final int RELATED_QUERY_POS =3;
    private static final int RELATED_QUERY_VAR_POS = 4;
    private static final int VALUE_POS =5;
    private static final int DESC_POS = 6;

    private QueryType currentQuery =null;

    /** Creates a new instance of PQLArgumentTableModel */
    public PQLArgumentTableModel(QueryType query) {
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
             (currentQuery.getArgument() == null)) {
             return;
         }


         List args = currentQuery.getArgument();

         Object[][] data = new Object[args.size()][columnNames.length];


         for (int i=0; i < args.size(); i++) {
             ArgumentType arg = (ArgumentType) args.get(i);

             data[i][SEQ_POS]  = (arg.getSequence()==null
                     ?Integer.toString(i)
                     :arg.getSequence().toString());
             data[i][NAME_POS] = (arg.getName()==null?"":arg.getName());
             data[i][TYPE_POS] = (arg.getType()==null?"":arg.getType());
             data[i][RELATED_QUERY_POS] = (arg.getRelatedQueryName()==null?"":arg.getRelatedQueryName());
             data[i][RELATED_QUERY_VAR_POS] = (arg.getRelatedQueryReturnVariableName()==null?"":arg.getRelatedQueryReturnVariableName());
             data[i][VALUE_POS] = (arg.getValue()==null?"":arg.getValue());
             data[i][DESC_POS] = (arg.getDescription()==null?"":arg.getDescription());
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
        PQLArgumentTableModel model = new PQLArgumentTableModel (null);
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
//     * @return
//     */
//
//    // todo - refactor - move to base class
//    public List getArguments() {
//        List rowsInTable = new ArrayList();
//        for (int rowNum = 0; rowNum < getRowCount(); rowNum ++) {
//            String seq = getValueAt(rowNum, SEQ_POS).toString();
//            String name = getValueAt(rowNum, NAME_POS).toString();
//            String type = getValueAt(rowNum, TYPE_POS).toString();
//            String description = getValueAt(rowNum, DESC_POS).toString();
//            String value = getValueAt(rowNum, VALUE_POS).toString();
//            String relatedQueryName = getValueAt(rowNum, RELATED_QUERY_POS).toString();
//            String relatedQueryVariableName = getValueAt(rowNum, RELATED_QUERY_VAR_POS).toString();
//
//            ArgumentType arg = DAOFactory.getPatternQueryRepository().createArgument(
//                    seq,
//                    name,
//                    type,
//                    value,
//                    description,
//                    relatedQueryName,
//                    relatedQueryVariableName);
//
//            rowsInTable.add(arg);
//        }
//
//        return rowsInTable;
//    }

//     todo - refactor - move to base class
//    public void addNewRow() {
//        addRow(
//                Integer.toString(getRowCount()),
//                "Name",
//                DEFAULT_TYPE,
//                "Related Query",
//                "Related Variable",
//                "",
//                "Description");
//    }

//    // todo - refactor - move to base class
//    public void addRow(
//              String seq,
//              String name,
//              String type,
//              String relatedQueryName,
//              String relatedQueryVariableName,
//              String value,
//              String description) {
//
//        checkDuplicate(
//                seq,
//                name,
//                type,
//                relatedQueryName,
//                relatedQueryVariableName,
//                value,
//                description);
//
//        ArgumentType newRow = null;
//        newRow = DAOFactory.getPatternQueryRepository().createArgument(
//                    seq,
//                    name,
//                    type,
//                    value,
//                    description,
//                    relatedQueryName,
//                    relatedQueryVariableName);
//
//        currentQuery.getArgument().add(newRow);
//        addRow(new Object[] {
//            seq,
//            name,
//            type,
//            relatedQueryName,
//            relatedQueryVariableName,
//            value,
//            description
//        });
//    }
//
//    private void checkDuplicate(
//            String seq,
//            String name,
//            String type,
//            String relatedQueryName,
//            String relatedQueryType,
//            String value,
//            String description) {
//
//        for (int rowNum = 0; rowNum < getRowCount(); rowNum ++) {
//            String rowname = getValueAt(rowNum, NAME_POS).toString();
//            // String rowtype = getValueAt(rowNum, TYPE_POS).toString();
//            // String rowdescription = getValueAt(rowNum, DESC_POS).toString();
//
//            if (rowname.equalsIgnoreCase(name)) {
//                throw new RuntimeException("Attempting to insert duplicate argument.");
//            }
//        }
//    }
//
//    public void removeRow(int rowNum) {
//        String name = getValueAt(rowNum, NAME_POS).toString();
//        String type = getValueAt(rowNum, TYPE_POS).toString();
//        String description = getValueAt(rowNum, DESC_POS).toString();
//
//        List list = currentQuery.getReturnVariable();
//        int foundRow = -1;
//        for (int i=0; i < list.size(); i++) {
//            ArgumentType var = (ArgumentType) list.get(i);
//            if (var.getName().equalsIgnoreCase(name)) {
//                foundRow = i;
//                break;
//            }
//        }
//
//        if (foundRow != -1) {
//            currentQuery.getArgument().remove(foundRow);
//            super.removeRow(rowNum);
//        }
//    }
//    public void setCurrentQuery(QueryType query) {
//        currentQuery = query;
//    }
//
//    public QueryType getCurrentQuery() {
//        return currentQuery;
//    }

    public String getSelectedArgumentName(int selectedRow) {
        return getValueAt(selectedRow, NAME_POS).toString();
    }
}
