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

import org.acre.analytics.PatternMetrics;
import org.acre.analytics.Snapshot;
import org.acre.analytics.Snapshots;
import org.acre.config.ConfigService;
import org.acre.visualizer.ui.components.treetable.AbstractTreeTableModel;
import org.acre.visualizer.ui.components.treetable.TreeTableModel;

import java.util.logging.Logger;

/**
 * User: Deepak Alur
 * Date: Apr 29, 2005
 * Time: 9:42:49 AM
 */
public class DashboardMetricsTableModel extends AbstractTreeTableModel{
    // column mames
    static protected String[]  cNames = {"Pattern/Roles", "#Hits"};
    private Logger logger;

    // column types
    static protected Class[]  cTypes = {TreeTableModel.class, Integer.class};

    public DashboardMetricsTableModel(Snapshots ss) {
        super(new SnapshotsTreeNode(ss));
        logger = ConfigService.getInstance().getLogger(this);
    }

    public DashboardMetricsTableModel(Snapshot ss) {
        super (new SnapshotTreeNode(ss));
        logger=ConfigService.getInstance().getLogger(this);
    }

    public DashboardMetricsTableModel(PatternMetrics pm) {
        super (new PatternMetricsTreeNode(pm));
        logger=ConfigService.getInstance().getLogger(this);
    }

    public int getColumnCount() {
        return cNames.length;
    }

    public String getColumnName(int column) {
        return cNames[column];
    }

    public Object getValueAt(Object node, int column) {

        PatternMetricsTreeNode pmnode = null;
        PatternMetrics pm = null;
        SnapshotTreeNode ssnode = null;
        SnapshotsTreeNode sssnode = null;

        logger.info("getValueAt: col=" + column + " : object = " + node );

        if (node instanceof SnapshotsTreeNode) {
            sssnode = (SnapshotsTreeNode) node;
            return getSSSNodeValue(sssnode, column);
        } else if (node instanceof SnapshotTreeNode) {
            ssnode = (SnapshotTreeNode) node;
            return getSSNodeValue(ssnode, column);
        } else if (node instanceof PatternMetricsTreeNode) {
            pmnode = (PatternMetricsTreeNode) node;
            pm = pmnode.getPatternMetrics();
        } else if (node instanceof PatternMetrics) {
            pm = (PatternMetrics) node;
        }
        return getPMValue(pm, column);

    }

    private Object getSSSNodeValue(SnapshotsTreeNode sssnode, int column) {
        switch (column) {
            case 0:
                if (sssnode != null)
                    return sssnode.getSnapshots();
            default:
                return null;
        }
    }

    private Object getSSNodeValue(SnapshotTreeNode ssnode, int column) {
        switch (column) {
            case 0:
                if ((ssnode != null)
                    && (ssnode.getSnapshot() != null))
                    return ssnode.getSnapshot().getSystem();
                else
                    return "Unknown Snapshot";
            default:
                return null;
        }
    }

    private Object getPMValue(PatternMetrics pm, int column) {
        switch(column) {
            case 0:
                if (pm != null)
                    return pm.getPatternName();
                else return "Unknown Pattern";
            case 1:
                if (pm != null)
                    return new Integer(pm.getHitCount());
                else return null;
        }
        return null;
    }

    public Object getChild(Object parent, int index) {
        return getChildren(parent)[index];
    }

    public int getChildCount(Object node) {
	    Object[] children = getChildren(node);
	    return (children == null) ? 0 : children.length;
    }

    protected Object[] getChildren(Object node) {

        if (node instanceof SnapshotsTreeNode) {
            SnapshotsTreeNode sssnode = (SnapshotsTreeNode) node;
            return sssnode.getChildren();
        }
        if (node instanceof SnapshotTreeNode) {
            SnapshotTreeNode ssnode = ((SnapshotTreeNode) node);
            return ssnode.getChildren();
        }
        if (node instanceof PatternMetricsTreeNode) {
	        PatternMetricsTreeNode pmnode = ((PatternMetricsTreeNode)node);
	        return pmnode.getChildren();
        }
        return null;
    }

    public boolean isLeaf(Object node) {

        if (node instanceof SnapshotsTreeNode)
    	    return ((SnapshotsTreeNode)node).isLeaf();
        else if (node instanceof PatternMetricsTreeNode)
    	    return ((PatternMetricsTreeNode)node).isLeaf();
        else if (node instanceof SnapshotTreeNode)
            return ((SnapshotTreeNode)node).isLeaf();

        return true;
    }

    /**
     * Returns the class for the particular column.
     */
    public Class getColumnClass(int column) {
    	return cTypes[column];
    }
}


