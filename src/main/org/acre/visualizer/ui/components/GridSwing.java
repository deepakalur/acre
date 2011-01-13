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

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.Color;
import java.awt.Component;
import java.util.Vector;

/** Simple table model to represent a grid of tuples.
 *
 * @version 1.7.2
 */
public class GridSwing extends AbstractTableModel {

    JTable   jtable = null;
    Object[] headers;
    Vector   rows;

    /**
     * Default constructor.
     */
    public GridSwing() {

        super();

        headers = new Object[0];    // initially empty
        rows    = new Vector();     // initially empty
    }

    /**
     * Get the name for the specified column.
     */
    public String getColumnName(int i) {
        return headers[i].toString();
    }

    public Class getColumnClass(int i) {

        if (rows.size() > 0) {
            Object o = getValueAt(0, i);

            if (o != null) {
                return o.getClass();
            }
        }

        return super.getColumnClass(i);
    }

    /**
     * Get the number of columns.
     */
    public int getColumnCount() {
        return headers.length;
    }

    /**
     * Get the number of rows currently in the table.
     */
    public int getRowCount() {
        return rows.size();
    }

    /**
     * Get the current column headings.
     */
    public Object[] getHead() {
        return headers;
    }

    /**
     * Get the current table data.
     *  Each row is represented as a <code>String[]</code>
     *  with a single non-null value in the 0-relative
     *  column position.
     *  <p>The first row is at offset 0, the nth row at offset n etc.
     */
    public Vector getData() {
        return rows;
    }

    /**
     * Get the object at the specified cell location.
     */
    public Object getValueAt(int row, int col) {

        if (row >= rows.size()) {
            return null;
        }

        Object colArray[] = (Object[]) rows.elementAt(row);

        if (col >= colArray.length) {
            return null;
        }

        return colArray[col];
    }

    /**
     * Set the name of the column headings.
     */
    public void setHead(Object[] h) {
        headers = new Object[h.length];
        for (int i = 0; i < h.length; i++) {
            headers[i] = h[i];
        }
    }

    Color[] backgroundColors;
    public void setBackgroundColor(Color[] h) {
        backgroundColors = new Color[h.length];
        for (int i = 0; i < h.length; i++) {
            backgroundColors[i] = h[i];
        }
    }

    public Color[] getBackgroundColor() {
        return backgroundColors;
    }
    /**
     * Append a tuple to the end of the table.
     */
    public void addRow(Object[] r) {

        Object[] row = new Object[r.length];

        // System.arraycopy(r, 0, row, 0, r.length);
        for (int i = 0; i < r.length; i++) {
            row[i] = r[i];

            if (row[i] == null) {

//                row[i] = "(null)";
            }
        }

        rows.addElement(row);
    }

    /**
     * Remove data from all cells in the table (without
     *  affecting the current headings).
     */
    public void clear() {
        rows.removeAllElements();
    }

    public void setJTable(JTable table) {
        jtable = table;
    }

    public void fireTableChanged(TableModelEvent e) {
        super.fireTableChanged(e);
        autoSizeTableColumns(jtable);
    }

    public static void autoSizeTableColumns(JTable table) {

        TableModel  model        = table.getModel();
        TableColumn column       = null;
        Component   comp         = null;
        int         headerWidth  = 0;
        int         maxCellWidth = Integer.MIN_VALUE;
        int         cellWidth    = 0;
        TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();

        for (int i = 0; i < table.getColumnCount(); i++) {
            column = table.getColumnModel().getColumn(i);
            comp = headerRenderer.getTableCellRendererComponent(table, column.getHeaderValue(), false, false, 0, 0);
            headerWidth  = comp.getPreferredSize().width + 10;
            maxCellWidth = Integer.MIN_VALUE;

            for (int j = 0; j < Math.min(model.getRowCount(), 30); j++) {
                TableCellRenderer r = table.getCellRenderer(j, i);

                comp = r.getTableCellRendererComponent(table, model.getValueAt(j, i), false, false, j, i);
                cellWidth = comp.getPreferredSize().width;

                if (cellWidth >= maxCellWidth) {
                    maxCellWidth = cellWidth;
                }
            }

            column.setPreferredWidth(Math.max(headerWidth, maxCellWidth) + 10);
        }
    }
}
