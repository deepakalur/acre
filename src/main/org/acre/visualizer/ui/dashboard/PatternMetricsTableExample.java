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

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;


public class PatternMetricsTableExample
{
    public static void main(String[] args) {
	new PatternMetricsTableExample();
    }

    public PatternMetricsTableExample() {
	    JFrame frame = new JFrame("PatternMetrics");

        PatternMetrics pm1 = createSample("One");
        PatternMetrics pm2 = createSample("Two");
        PatternMetrics pm3 = createSample("Three");
        ArrayList r = new ArrayList();
        r.add(pm3);
        r.add(pm1);
        pm2.setRoles(r);
        PatternMetrics [] pmarr = {pm1, pm2};
        DashboardMetricsTableModel model = new DashboardMetricsTableModel(pm2);

//        Snapshot ss = SnapshotsUtil.getLatestSnapshot();
//
//        DashboardMetricsTableModel model = new DashboardMetricsTableModel(ss);
        AcreTreeTable treeTable = new AcreTreeTable(model);
	    //JTreeTable treeTable = new JTreeTable(new PatternMetricsModel(dt1, dt2));

	frame.addWindowListener(new WindowAdapter() {
	    public void windowClosing(WindowEvent we) {
		System.exit(0);
	    }
	});

	frame.getContentPane().add(new JScrollPane(treeTable));
	frame.pack();
	frame.show();
    }

    private PatternMetrics createSample(String name) {
        PatternMetrics root = new PatternMetrics("Root-" + name);
        ArrayList c = new ArrayList();
        c.add(new PatternMetrics(name+"-C1"));
        c.add(new PatternMetrics(name+"-C2"));
        c.add(new PatternMetrics(name+"-C3"));

        PatternMetrics nested = new PatternMetrics("Nested");
        PatternMetrics n1 = new PatternMetrics("n1");
        ArrayList n1rs = new ArrayList();
        n1rs.add(new PatternMetrics("n1-r1"));
        n1rs.add(new PatternMetrics("n1-r2"));
        n1.setRoles(n1rs);

        c.add(n1);
        root.setRoles(c);
        return root;
    }

}

