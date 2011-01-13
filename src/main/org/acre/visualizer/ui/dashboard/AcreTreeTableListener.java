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

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

/**
 * User: Deepak Alur
 * Date: May 3, 2005
 * Time: 10:51:35 AM
 */
public class AcreTreeTableListener implements TreeSelectionListener {
    AcreTreeTable treetable;

    public AcreTreeTableListener(AcreTreeTable treetable) {
        this.treetable = treetable;
    }

    public void valueChanged(TreeSelectionEvent e) {
        System.out.println("Tree event = " + e);
        TreePath [] paths = e.getPaths();
        for (int i=0; i < paths.length; i++) {
            System.out.println("Path - " + i + " = " + paths[i] + " class = " + paths[i].getClass().getName());
        }

        //DefaultMutableTreeNode node = (DefaultMutableTreeNode)

        Object node = null;            //tree.getLastSelectedPathComponent();

        if (node == null) return;

        if (node instanceof SnapshotsTreeNode) {
            SnapshotsTreeNode sssnode = (SnapshotsTreeNode) node;
            System.out.println("SnapshotsTreeNode = " + sssnode);
            if (sssnode.isLeaf()) {
                // leaf SnapshotsTreeNode
            } else {
                // non-leaf SnapshotsTreeNode
            }
        } else if (node instanceof PatternMetricsTreeNode) {
            // PatternMetrics selected
            PatternMetricsTreeNode pmnode = (PatternMetricsTreeNode) node;
            System.out.println("PM Node = " + pmnode);
            if (pmnode.isLeaf()) {
                // leaf PM Node (Role metrics
            } else {
                // non-leaf PM Node
            }
        } else if (node instanceof SnapshotTreeNode) {
            // Snapshot selected
            SnapshotTreeNode ssnode = (SnapshotTreeNode) node;
            System.out.println("SS Node = " + ssnode);
            if (ssnode.isLeaf()) {
                // leaf PM Node (Role metrics
            } else {
                // non-leaf PM Node
            }
        }
    }
}