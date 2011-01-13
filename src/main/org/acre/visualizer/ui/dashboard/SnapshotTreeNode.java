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

import java.util.ArrayList;
import java.util.Iterator;

public class SnapshotTreeNode {
    Snapshot ss;

    public SnapshotTreeNode(Snapshot ss) {
        this.ss = ss;
    }

    public String toString() {
        if (ss != null) {
            return ss.getSystem() + " (" + ss.getVersion() +") @ " + ss.getTimestamp();
        } else {
            return null;
        }
    }

    public Object[] getChildren() {
        Iterator pmi = ss.getPatternMetrics().iterator();
        ArrayList c = new ArrayList();
        while (pmi.hasNext()) {
            PatternMetrics m = (PatternMetrics) pmi.next();
            PatternMetricsTreeNode patternMetricsTreeNode = new PatternMetricsTreeNode(m);
            c.add(patternMetricsTreeNode);
        }
        return c.toArray();
    }

    public Snapshot getSnapshot() {
        return ss;
    }

    public boolean isLeaf() {
        if (ss == null)
            return true;

        if ((ss.getPatternMetrics() != null) && (ss.getPatternMetrics().size() > 0))
            return false;
        else
            return true;
    }
}
