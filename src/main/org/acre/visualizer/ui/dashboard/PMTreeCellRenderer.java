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

import org.acre.visualizer.graph.AcreGraphConstants;
import org.acre.visualizer.graph.AcreGraphIconUtils;
import org.acre.visualizer.ui.AcreIconConstants;
import org.acre.visualizer.ui.AcreUIUtils;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.Component;

/**
 * User: Deepak Alur
 * Date: May 1, 2005
 * Time: 9:34:42 PM
 */
public class PMTreeCellRenderer extends DefaultTreeCellRenderer {

    public PMTreeCellRenderer() {
        super();
    }

    public Component getTreeCellRendererComponent(JTree tree,
                                                  Object value,
                                                  boolean sel,
                                                  boolean expanded,
                                                  boolean leaf,
                                                  int row,
                                                  boolean hasFocus) {

        super.getTreeCellRendererComponent(tree, value, sel,
                expanded, leaf, row,
                hasFocus);

        setIcon(value);

        if (leaf) {
            setIcon(AcreGraphIconUtils.getCellIcon(AcreGraphConstants.PDMROLE));
            setToolTipText(value.toString());
        } else {
            setToolTipText(null); //no tool tip
        }

        return this;
    }


    private void setIcon(Object value) {

        Icon icon = null;

        if (value == null)
            return;
        if (value instanceof SnapshotsTreeNode) {
            icon = new ImageIcon(AcreUIUtils.createImage(AcreIconConstants.DASHBOARD_FRAME_ICON));
        } else if (value instanceof SnapshotTreeNode) {
            icon = new ImageIcon(AcreUIUtils.createImage(AcreIconConstants.DASHBOARD_FRAME_ICON));
        } else if (value instanceof PatternMetricsTreeNode) {
            icon = new ImageIcon(AcreUIUtils.createImage(AcreIconConstants.PDM_FRAME_ICON));
        }

        if (icon != null)
            this.setIcon(icon);
    }
}
