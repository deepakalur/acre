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

import org.acre.analytics.Snapshot;
import org.acre.analytics.Snapshots;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * User: Deepak Alur
 * Date: May 2, 2005
 * Time: 9:02:19 PM
 */
public class SnapshotsTreeNode {
    Snapshots ss;

    public SnapshotsTreeNode(Snapshots ss) {
        this.ss = ss;
    }

    public String toString() {
        if (ss != null) {
            return ss.toString(); // todo
        } else {
            return null;
        }
    }

    public Object[] getChildren() {
        Iterator ssi = ss.getAllSnapshots();
        ArrayList c = new ArrayList();
        while (ssi.hasNext()) {
            Snapshot s = (Snapshot) ssi.next();
            SnapshotTreeNode snapshotTreeNode= new SnapshotTreeNode(s);
            c.add(snapshotTreeNode);
        }
        return c.toArray();
    }

    public Snapshots getSnapshots() {
        return ss;
    }

    public boolean isLeaf() {
        if (ss == null)
            return true;

        if (ss.getAllSnapshots() == null)
            return true;

        Iterator i = ss.getAllSnapshots();
        if (! i.hasNext())
            return true;

        return false;
    }
}
