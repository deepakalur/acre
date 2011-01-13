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

import java.util.Collection;

public class PatternMetricsTreeNode {
    PatternMetrics pm;

    public PatternMetricsTreeNode(PatternMetrics pm) {
        this.pm=pm;
    }

    public PatternMetricsTreeNode(PatternMetrics pm, PatternMetricsTreeNode[] children) {
	    this.pm = pm;
    }


    /**
     * Returns the the string to be used to display this leaf in the JTree.
     */
    public String toString() {
    	return pm.getPatternName();
    }

    public PatternMetrics getPatternMetrics() {
	    return pm;
    }

    /**
     * Loads the children, caching the results in the children ivar.
     */
    protected Object[] getChildren() {

        PatternMetricsTreeNode [] children = new PatternMetricsTreeNode[0];

        if (pm != null) {
            if (pm.getRoles() != null) {
                Collection roles = pm.getRoles();
                Object [] roleArr = roles.toArray();
                children = new PatternMetricsTreeNode[roleArr.length];
                for (int i = 0; i < roleArr.length; i++) {
                    PatternMetrics pm = (PatternMetrics) roleArr[i];
                    PatternMetricsTreeNode n = new PatternMetricsTreeNode(pm, null);
                    children[i] = n;
                }
            }
        }

	    return children;
    }

    public boolean isLeaf() {

        if (pm == null)
            return true;

        if ((pm.getRoles() != null) && (pm.getRoles().size() > 0))
            return false;

        return true;
    }
}
