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
package org.acre.analytics;

import org.acre.common.AcreException;
import org.acre.server.AcreDelegate;
import org.acre.server.UserContextObject;

//import org.jfree.chart.ChartPanel;
//import org.jfree.chart.JFreeChart;
//import org.jfree.data.category.CategoryDataset;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: Deepak Alur
 * Date: May 2, 2005
 * Time: 2:38:36 PM
 */
public class SnapshotsUtil {

    public static final SnapshotQuery LATEST_SNAPSHOT_QUERY = new SnapshotQuery("Latest", null, null);

    public static List getPatternHitCounts(String patternName, SnapshotQuery query) {
        ArrayList result = new ArrayList();
        Snapshots snapshots;
        Snapshots [] allSnapshots;

        if (query != null) {
            snapshots = SnapshotsHolder.getInstance().getSnapshots(query);
            allSnapshots = new Snapshots[1];
            allSnapshots[0] = snapshots;
        }
        else {
            allSnapshots = SnapshotsHolder.getInstance().getAllSnapshots();
        }

        if ((allSnapshots == null) || (allSnapshots.length == 0))
            return result;

        for (int ssn=0; ssn < allSnapshots.length; ssn++) {
            Snapshots ss = allSnapshots[ssn];
            Iterator itr = ss.getSortedByTime();

            int versionNum = 0;
            while ( itr.hasNext() ) {
                Snapshot s  = (Snapshot)itr.next();

                PatternMetrics pm = s.getPatternMetrics(patternName);

                HitCountData hcd = null;
                String version = s.getVersion();
                if ((version == null) || (version.trim().length() ==0)) {
                    version = "T" + versionNum;
                    versionNum++;
                }

                if (pm != null) {
                    hcd = new HitCountData(s.getSystem(), version, s.getTimestamp(), new Integer(pm.getHitCount()));
                }
                else {
                    hcd = new HitCountData(s.getSystem(), version, s.getTimestamp(), new Integer(0));
                    // no hits for this pattern in this snapshot
                }
                result.add(hcd);
            }
        }
        return result;
    }

    public static Snapshots getSnapshots(UserContextObject user, SnapshotQuery query) throws AcreException {
        Snapshots snapshots = null;
        snapshots = SnapshotsHolder.getInstance().getSnapshots(query);

        if (snapshots == null) {
            snapshots = createSnapshots(user, query);
            SnapshotsHolder.getInstance().addSnapshots(query, snapshots);
        }

        return snapshots;
    }

    private static Snapshots createSnapshots(UserContextObject user, SnapshotQuery query) throws AcreException {
        Snapshots snapshots = null;

        AcreDelegate delegate = new AcreDelegate( user );
        return delegate.getQuerySnapshots(query);
    }

    public static Snapshot getLatestSnapshot(UserContextObject user, SnapshotQuery query)
            throws AcreException {
        Snapshots snapshots = null;
        Snapshot latestSS = null;

        snapshots = getSnapshots(user, query);

        if (snapshots == null)
            return null;

        Iterator ssItr = snapshots.getSortedByTime();
        while ( ssItr.hasNext()) {
            latestSS = (Snapshot)ssItr.next();
        }
        return latestSS;
    }

    public static Snapshot getLatestSnapshot(UserContextObject user) throws AcreException {
        return getLatestSnapshot(user, LATEST_SNAPSHOT_QUERY);
    }

    public static void main(String args[])  {

        UserContextObject user = new UserContextObject( "user", "pass");
        Snapshots ss = null;
        try {
            ss = getSnapshots(user, LATEST_SNAPSHOT_QUERY);
        } catch (AcreException e) {
            e.printStackTrace();
        }
        Iterator pn = ss.getPatternNames();
        ArrayList pNames = new ArrayList();
        while (pn.hasNext()) {
            pNames.add((String) pn.next());
        }
        String [] pNamesArr = new String[pNames.size()];
        pNames.toArray(pNamesArr);

//        CategoryDataset ds = AcreChartUtil.createDataset(pNamesArr, LATEST_SNAPSHOT_QUERY);
//        JFreeChart chart = AcreChartUtil.createChart("ACRE", "Patterns", "#Hits", ds);

//        ChartPanel cp = new ChartPanel(chart);
        JFrame f = new JFrame("Chart");
        f.getContentPane().add(new JLabel("Not Implemented"));//cp);
        f.pack();
        f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        f.setVisible(true);
    }

}

