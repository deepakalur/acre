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
package org.acre.visualizer.ui.dashboard;

import org.acre.visualizer.ui.components.treetable.JTreeTable;
import org.acre.visualizer.ui.components.treetable.TreeTableModel;

import javax.swing.tree.TreeSelectionModel;
import java.awt.Dimension;

/**
 * User: Deepak Alur
 * Date: May 2, 2005
 * Time: 12:24:35 PM
 */
public class AcreTreeTable extends JTreeTable {
    public AcreTreeTable(TreeTableModel treeTableModel) {
        super(treeTableModel);
        initialize();
    }

    private void initialize() {
        PMTreeCellRenderer pmtcr = new PMTreeCellRenderer();
        tree.setCellRenderer(pmtcr);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);        

        // Make the tree and table row heights the same.
	    tree.setRowHeight(getRowHeight());

	    // Install the tree editor renderer and editor.
	    setDefaultRenderer(TreeTableModel.class, tree);
	    setDefaultEditor(TreeTableModel.class, new TreeTableCellEditor());

	    setShowGrid(false);
	    setIntercellSpacing(new Dimension(0, 0));
    }


}

