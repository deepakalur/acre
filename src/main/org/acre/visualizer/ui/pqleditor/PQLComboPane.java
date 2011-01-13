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

import org.acre.visualizer.graph.AcreGraphConstants;
import org.acre.visualizer.graph.AcreGraphIconUtils;
import org.acre.lang.analyser.ScriptModel;
import org.acre.lang.pql.pdbc.PQLArtifact;
import org.acre.lang.pql.pdbc.PQLResultSet;
import org.acre.lang.pql.pdbc.PQLResultSetMetaData;
import org.acre.lang.pql.pdbc.PQLValueHolder;
import org.acre.visualizer.ui.Main;
import org.acre.visualizer.ui.AcreUIConstants;
import org.acre.visualizer.ui.components.GridSwing;
import org.acre.visualizer.ui.components.TableSorter;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.Map;

/**
 * Combo Panel (Table and Tree View of the PQLResutlSet)
 *
 * @author Yury Kamen
 */
public class PQLComboPane extends JTabbedPane {
    private static final String RESULTS_TABLE_TAB = "Query Results";
    private static final String RESULTS_TEXT_TAB = "Debug Info";
    private static final String RESULTS_GRAPH_TAB = "Visualized Results";
    private static final String SHOW_TABLE = "Show Table";
    private static final String SHOW_TEXT = "Show Text";
    private static final String SHOW_GRAPH = "Show Graph";
    public static Color FLOP_COLOR = new Color(240, 240, 240);
    public static Color FLIP_COLOR = Color.WHITE;
    GridSwing gResult;
    JTable gResultTable;
    TableModel tableModel;
    JSplitPane pqlSplitPanel;
    JTable queryResultsTable;
    JScrollPane queryResultsGraph;
    JTextArea queryResultsTextArea;
    JTabbedPane queryResultsTab;
    private AcrePQLDataPanel pqlDataPanel;
    private int pqlDataPanelLastDividerLocation = -1;
    private int dividerSize = 8;
    private static final Cursor waitCursor = new Cursor(Cursor.WAIT_CURSOR);
    private Cursor fMainCursor;
    private Cursor queryResultsTextAreaCursor;

    private TableCellRenderer iconHeaderRenderer = new DefaultTableCellRenderer() {
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected,
                                                       boolean hasFocus,
                                                       int row, int column) {
            Color bgColor = Color.WHITE;
            TableModel model = table.getModel();
            if (model instanceof TableSorter) {
                model = ((TableSorter) model).getTableModel();
            }

            if (model instanceof GridSwing) {
                GridSwing g = (GridSwing) model;
                Color[] bg = g.getBackgroundColor();
                if (null != bg && null != bg[column]) {
                    bgColor = bg[column];
                }
            }

            if (value instanceof PQLArtifact) {
                PQLArtifact artifact = (PQLArtifact) value;
                if (!artifact.isPrimitive()) {
                    PQLValueHolder p = (PQLValueHolder) artifact.getValue();
                    String type = p.getType();
                    Icon icon = null;
                    if (type.equalsIgnoreCase("JClass") || type.equalsIgnoreCase("classes")) {
                        icon = AcreGraphIconUtils.getCellIcon(AcreGraphConstants.CLASS);
                    } else if (type.equalsIgnoreCase("jInterface")) {
                        icon = AcreGraphIconUtils.getCellIcon(AcreGraphConstants.INTERFACE);
                    } else if (type.equalsIgnoreCase("JMethod") || type.equalsIgnoreCase("methods")) {
                        icon = AcreGraphIconUtils.getCellIcon(AcreGraphConstants.METHOD);
                    } else if (type.equalsIgnoreCase("JField") || type.equalsIgnoreCase("fields")) {
                        icon = AcreGraphIconUtils.getCellIcon(AcreGraphConstants.FIELD);
                    } else if (type.equalsIgnoreCase("JParameter") || type.equalsIgnoreCase("typeParameters")) {
                        icon = AcreGraphIconUtils.getCellIcon(AcreGraphConstants.PARAMETER);
                    } else if (type.equalsIgnoreCase("JPackage") || type.equalsIgnoreCase("parentPackage")) {
                        icon = AcreGraphIconUtils.getCellIcon(AcreGraphConstants.PACKAGE);
                    } else {
                        icon = null;
                    }
                    setIcon(icon);
                }

                Object v = artifact.getValue();
                if (v instanceof PQLValueHolder) {
                    PQLValueHolder holder = (PQLValueHolder) v;
                    setText(holder.getName());
                } else {
                    setText(v.toString());
                }
            } else {
                setText(value.toString());
            }

            if (isSelected) {
                setBackground(AcreUIConstants.SELECTED_ROW_BG_COLOR);
            } else {
                setBackground(bgColor);
            }
            return this;
        }
    };

    public PQLComboPane() {
        queryResultsGraph = new JScrollPane();
        queryResultsGraph.setBackground(Color.white);
        queryResultsTextArea = new JTextArea();
        gResult = new GridSwing();
        TableSorter sorter = new TableSorter(gResult);
        tableModel = sorter;
        queryResultsTable = gResultTable = new JTable(sorter);
        sorter.setTableHeader(gResultTable.getTableHeader());

        gResultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        gResultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        gResultTable.setCellSelectionEnabled(true);   // deprecated
        gResultTable.setSelectionBackground(AcreUIConstants.SELECTED_ROW_BG_COLOR);

        gResult.setJTable(gResultTable);

        gResultTable.setDefaultRenderer(PQLArtifact.class, iconHeaderRenderer);

        gResultTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
//                if (e.getClickCount() == 2) {
                processTableDoubleClick(e);
//                }
            }
        });

        queryResultsTab = this;
        pqlDataPanel = new AcrePQLDataPanel();
        pqlSplitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, new JScrollPane(queryResultsTable), pqlDataPanel);
        pqlSplitPanel.setDividerLocation(1.0);
//        pqlSplitPanel.setDividerSize(0);
        pqlSplitPanel.setOneTouchExpandable(true);
        pqlDataPanel.setVisible(false);

//        queryResultsTab.addTab(RESULTS_GRAPH_TAB, queryResultsGraph);
        queryResultsTab.addTab(RESULTS_TABLE_TAB, pqlSplitPanel);
        queryResultsTab.addTab(RESULTS_TEXT_TAB, new JScrollPane(queryResultsTextArea));
    }

    public void updateResult() {
        gResult.fireTableChanged(null);

        queryResultsTable.doLayout();
        queryResultsTable.repaint();

        queryResultsGraph.doLayout();
        queryResultsGraph.repaint();
    }

    public void setResultsGraphViewPort(Component c) {
        removeResultsGraphViewPort();
        queryResultsGraph.setViewportView(c);
    }

    public void removeResultsGraphViewPort() {
        Component component = queryResultsGraph.getViewport().getView();
        if (null != component) {
            queryResultsGraph.getViewport().remove(component);
        }
    }

    public void showResultsAll(boolean flag) {
        showResultsGraph(flag);
        showResultsTable(flag);
        showResultsText(flag);
    }


    public void showResultsGraphOnly(boolean flag) {
        queryResultsTab.removeAll();
        if (flag) {
            queryResultsTab.addTab(RESULTS_GRAPH_TAB, queryResultsGraph);
        } else {
            queryResultsTab.addTab(RESULTS_TABLE_TAB, pqlSplitPanel);
            //queryResultsTab.addTab(RESULTS_TEXT_TAB, new JScrollPane(queryResultsTextArea));
        }
//        queryResultsTab.repaint();
    }

    public void showResultsGraph(boolean flag) {
        int index = queryResultsTab.indexOfTab(RESULTS_GRAPH_TAB);
        if ((index == -1) && (flag)) { // tab does not exist, but to show, add the tab
            queryResultsTab.addTab(RESULTS_GRAPH_TAB, queryResultsGraph);
        } else if (!flag) {            // tab exists, remove
            if (index >= 0) queryResultsTab.remove(index);
        } else {                       // tab exists, enable it
            if (index >= 0) queryResultsTab.setEnabledAt(index, flag);
        }
    }

    public void showResultsTable(boolean flag) {
        int index = queryResultsTab.indexOfTab(RESULTS_TABLE_TAB);

        if ((index == -1) && (flag)) { // tab does not exist, but to show, add the tab
            queryResultsTab.addTab(RESULTS_TABLE_TAB, queryResultsTable);
        } else if (!flag) {            // tab exists, remove
            if (index >= 0) queryResultsTab.remove(index);
        } else {                       // tab exists, enable it
            if (index >= 0) queryResultsTab.setEnabledAt(index, flag);
        }
    }

    public void clear() {
        gResult.clear();
        hidePQLData();
    }

    public void showResultsText(boolean flag) {
        int index = queryResultsTab.indexOfTab(RESULTS_TEXT_TAB);

        if ((index == -1) && (flag)) {         // tab does not exist, but to show, add the tab
            queryResultsTab.addTab(RESULTS_TEXT_TAB, queryResultsTextArea);
        } else if (!flag) {                   // tab exists, remove
            if (index >= 0) queryResultsTab.remove(index);
        } else {                              // tab exists, enable it
            if (index >= 0) queryResultsTab.setEnabledAt(index, flag);
        }
    }

    private void processTableDoubleClick(MouseEvent e) {
        JTable table = (JTable) e.getSource();
        Object o = table.getModel().getValueAt(table.getSelectedRow(), table.getSelectedColumn());
        if (o instanceof PQLArtifact) {
            viewPQLData((PQLArtifact) o);
        } else {
            hidePQLData();
        }
    }

    private void hidePQLData() {
        boolean wasVisible = pqlDataPanel.isVisible();
        pqlDataPanel.setVisible(false);
        if (wasVisible) {
            pqlDataPanelLastDividerLocation = pqlSplitPanel.getDividerLocation();
        }
        pqlSplitPanel.setDividerLocation(1.0);
//        pqlSplitPanel.setDividerSize(0);
    }

    private void viewPQLData(PQLArtifact pqlData) {
        pqlDataPanel.setData(pqlData);
        boolean wasVisible = pqlDataPanel.isVisible();
        pqlDataPanel.setVisible(true);
        if (pqlDataPanelLastDividerLocation < 0) {
            pqlSplitPanel.setDividerLocation(0.6);
            pqlDataPanelLastDividerLocation = pqlSplitPanel.getDividerLocation();
        } else {
            if (!wasVisible) {
                pqlSplitPanel.setDividerLocation(pqlDataPanelLastDividerLocation);
            }
        }
//        pqlSplitPanel.setDividerSize(dividerSize);
        pqlDataPanel.getParent().doLayout();
        Main.getMDIFrame().openArtifact(pqlData);
    }

    public void setData(Object res) {
        if (res == null) {
            queryResultsTextArea.setText("");
            String g[] = new String[1];
            g[0] = "Result";
            gResult.clear();
            gResult.setHead(g);
            g[0] = "(Results not available - Execute Query and retry)";
            gResult.addRow(g);
            gResultTable.repaint();
            return;
        }

        Map resultMap = null;
        PQLResultSet resultSet = null;
        StringBuffer sbuf = new StringBuffer();

        // hack - deepak
        if (res instanceof Map) {
            resultMap = (Map) res;
            Iterator iter = resultMap.keySet().iterator();
            while (iter.hasNext()) {
                Object key = iter.next();
                resultSet = (PQLResultSet) resultMap.get(key);
                sbuf.append("===============  Results Paramater ( " + key + " ) ===============\n");
                sbuf.append(resultSet.toString() + "\n");
            }
        }
        queryResultsTextArea.setText(sbuf.toString());

        if (res instanceof Map) {
            Map map = (Map) res;
            int col = map.size();
            if (col == 0) {
                queryResultsTextArea.setText("");
                String g[] = new String[1];
                g[0] = "Result";
                gResult.setHead(g);
                g[0] = "(Results not available - Execute Query and retry)";
                gResult.addRow(g);
                gResultTable.repaint();
                return;
            }
            int totalColumns = 0;
            int i = 0;
            PQLResultSet[] v = new PQLResultSet[col];
            for (Iterator iterator = map.keySet().iterator(); iterator.hasNext();) {
                String key = (String) iterator.next();
                PQLResultSet rset = (PQLResultSet) (map.get(key));
                v[i++] = rset;
                totalColumns += rset.getMetaData().getColumnCount();
            }

            Object h[] = new Object[totalColumns];
            Color c[] = new Color[totalColumns];
            i = 0;
            int maxRowCount = 0;
            boolean flip = false;
            for (Iterator iterator = map.keySet().iterator(); iterator.hasNext();) {
                flip = !flip;
                String key = (String) iterator.next();
                PQLResultSet rset = (PQLResultSet) (map.get(key));
                if (rset.getMetaData().getRowCount() > maxRowCount) {
                    maxRowCount = rset.getMetaData().getRowCount();
                }
                String prefix = key;
                PQLResultSetMetaData m = rset.getMetaData();
                for (int k = 0; k < m.getColumnCount(); k++) {
                    if (ScriptModel.DEFAULT_RETURN_VARIABLE.equals(key)) {
                        prefix = "";
                    } else {
                        prefix = key + ".";
                    }

                    String name = m.getColumnLabel(k);
                    if (null == name) {
                        name = m.getColumnName(k);
                    }
                    h[i] = prefix + name + "  :" + m.getColumnType(k);
                    if (flip) {
                        c[i] = FLIP_COLOR;
                    } else {
                        c[i] = FLOP_COLOR;
                    }
                    i++;
                }
            }
            gResult.setHead(h);
            gResult.setBackgroundColor(c);

            for (int rowindex = 0; rowindex < maxRowCount; rowindex++) {
                i = 0;
                for (int j = 0; j < col; j++) {
                    PQLResultSet rset = v[j];
                    PQLResultSetMetaData m = rset.getMetaData();
                    for (int k = 0; k < m.getColumnCount(); k++) {
                        if (m.getRowCount() <= rowindex) {
                            h[i++] = "";
                        } else {
                            h[i++] = rset.getRow(rowindex)[k];
                        }
                    }
                }
                gResult.addRow(h);
            }
            gResultTable.repaint();
            return;
        }

        queryResultsTextArea.setText("");
        String g[] = new String[1];
        g[0] = "Result";
        gResult.setHead(g);
        g[0] = "Not implemented yet for " + res.getClass().getName();
        gResult.addRow(g);
        return;
    }

    public void setWaiting(boolean waiting) {
        if (waiting) {
            if (fMainCursor == null) {
                fMainCursor = this.getCursor();
                queryResultsTextAreaCursor = queryResultsTextArea.getCursor();
            }
            this.setCursor(waitCursor);
            queryResultsTextArea.setCursor(waitCursor);
        } else {
            this.setCursor(fMainCursor);
            queryResultsTextArea.setCursor(queryResultsTextAreaCursor);
        }
    }
}
