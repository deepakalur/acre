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

import org.acre.pdm.ArgumentType;
import org.acre.pdm.RoleType;
import org.acre.dao.DAOFactory;
import org.acre.dao.PDMXMLConstants;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Deepak.Alur@Sun.com
 * @version Nov 12, 2004 12:26:18 PM
 */
public class PDMRoleArgumentTableModel extends DefaultTableModel {

    private static String DEFAULT_TYPE = PDMXMLConstants.DEFAULT_PDM_ROLE_ARGUMENT_TYPE;

    private static Object[] columnNames =
        {"#",
         "Name",
         "Type",
         "Value"};

    private Class[] types = new Class [] {
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class
            };

    private static final int SEQ_POS = 0;
    private static final int NAME_POS = 1;
    private static final int TYPE_POS = 2;
    private static final int VALUE_POS = 3;

    RoleType currentRole;

    public PDMRoleArgumentTableModel(RoleType role) {
        super();
        this.currentRole = role;
        this.setColumnIdentifiers(columnNames);
        this.setColumnCount(columnNames.length);
        initModel();
    }

    private void initModel() {
        if ((currentRole ==null) ||
            (currentRole.getArgument() == null) ||
            (currentRole.getArgument().size()  == 0)) {

            return;
        }

        List args = currentRole.getArgument();

        Object[][] data;

        data = new Object[args.size()][columnNames.length];

        for (int i=0; i < args.size(); i++) {
            ArgumentType arg = (ArgumentType) args.get(i);

            data[i][SEQ_POS] = (arg.getSequence()==null?Integer.toString(0):arg.getSequence().toString());
            data[i][NAME_POS] = (arg.getName()==null?"":arg.getName());
            data[i][TYPE_POS] = (arg.getType()==null?"":arg.getType());
            data[i][VALUE_POS] = (arg.getValue()==null?"":arg.getValue());
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
        PDMRoleArgumentTableModel model = new PDMRoleArgumentTableModel(null);
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
    public List getArguments() {
        List argsInTable = new ArrayList();
        for (int rowNum = 0; rowNum < getRowCount(); rowNum ++) {
            String sequence = getValueAt(rowNum, SEQ_POS).toString();
            String name = getValueAt(rowNum, NAME_POS).toString();
            String type = getValueAt(rowNum, TYPE_POS).toString();
            String value = getValueAt(rowNum, VALUE_POS).toString();

            ArgumentType arg =
                    DAOFactory.getPatternRepository().createRoleArgument(sequence, name, type, value);
            argsInTable.add(arg);
        }

        return argsInTable;
    }

    public String getSelectedArgumentName(int selectedRow) {
        if (getValueAt(selectedRow, NAME_POS) != null)
            return getValueAt(selectedRow, NAME_POS).toString();
        else
            return null;
    }

    public int getRowNumberFor(ArgumentType arg) {
        for (int i=0; i < getRowCount(); i++) {
            String argName = getValueAt(i, NAME_POS).toString();
            if (argName.equalsIgnoreCase(arg.getName()))
                return i;
        }

        return -1;
    }
}
