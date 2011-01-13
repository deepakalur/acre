package org.acre.analytics;

import junit.framework.TestCase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Arrays;

/**
 * Copyright (c) 2004
 * Sun Microsystems, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Sun
 * Microsystems, Inc. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Sun.
 *
 * SUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. SUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 */

/**
 * @author rajmohan@Sun.com
 */
public class SnapshotQueryTestCase extends TestCase {

    public void testDateAndSystemFilter() throws ParseException {
        AcreContainer acreContainer = AcreContainerFactory.getInstance().getAcreContainer();
        PatternAnalyticsEngine  analyticsEngine = acreContainer.getAnalyticsEngine();

        SnapshotQuery query = new SnapshotQuery();

        Date dt1, dt2;

        dt1 = new SimpleDateFormat("dd-MM-yyyy").parse("01-01-2005");
        dt2 = Calendar.getInstance().getTime();

        query.setStartDate(dt1);
        query.setEndDate(dt2);

        query.addSystemFilter("PSA");

        Snapshots snapshots = analyticsEngine.getSnapshots(query);
        printSnapshots(snapshots);
    }

    public void testDateSystemVersionPatternFilter() throws ParseException {
        AcreContainer acreContainer = AcreContainerFactory.getInstance().getAcreContainer();
        PatternAnalyticsEngine  analyticsEngine = acreContainer.getAnalyticsEngine();

        Date dt1, dt2;
        dt1 = new SimpleDateFormat("dd-MM-yyyy").parse("01-01-2005");
        dt2 = new SimpleDateFormat("dd-MM-yyyy").parse("01-05-2005");

            try {
                SnapshotQuery query = new SnapshotQuery();
                query.setStartDate(dt1);
                query.setEndDate(dt2);
                query.addSystemFilter("PSA");
                query.addVersionFilter("V3.0");
                query.addPatternFilter("BD_Calls_SF");
                Snapshots snapshots = analyticsEngine.getSnapshots(query);
                printSnapshots(snapshots);
            }
            catch(Throwable t) {
                System.out.println(t.getMessage());
            }
    }


    public void testDateSystemVersionPatternFilter2() throws ParseException {
        AcreContainer acreContainer = AcreContainerFactory.getInstance().getAcreContainer();
        PatternAnalyticsEngine  analyticsEngine = acreContainer.getAnalyticsEngine();

        Date dt1, dt2;
        dt1 = new SimpleDateFormat("dd-MM-yyyy").parse("01-01-2005");
        dt2 = new SimpleDateFormat("dd-MM-yyyy").parse("01-05-2005");

            try {
                SnapshotQuery query = new SnapshotQuery();
                query.setStartDate(dt1);
                query.setEndDate(dt2);
                query.addSystemFilter("PSA");
                query.addVersionFilter("V4.0");
                query.addPatternFilter("BD_Calls_SF");
                Snapshots snapshots = analyticsEngine.getSnapshots(query);
                printSnapshots(snapshots);
            }
            catch(Throwable t) {
                System.out.println(t.getMessage());
            }
    }


    public void testMultipleVersionsWithDate() throws ParseException {
        AcreContainer acreContainer = AcreContainerFactory.getInstance().getAcreContainer();
        PatternAnalyticsEngine  analyticsEngine = acreContainer.getAnalyticsEngine();

        Date dt1, dt2;
        dt1 = new SimpleDateFormat("dd-MM-yyyy").parse("01-01-2005");
        dt2 = new SimpleDateFormat("dd-MM-yyyy").parse("01-05-2005");

            try {
                SnapshotQuery query = new SnapshotQuery();
                query.setStartDate(dt1);
                query.setEndDate(dt2);
                query.addSystemFilter("PSA");
                query.addVersionFilter("V4.0");
                query.addVersionFilter("V3.0");
                query.addPatternFilter("BD_Calls_SF");
                Snapshots snapshots = analyticsEngine.getSnapshots(query);
                printSnapshots(snapshots);
            }
            catch(Throwable t) {
                System.out.println(t.getMessage());
            }
    }

    public void testMultipleVersionsWithoutDate() throws ParseException {
        AcreContainer acreContainer = AcreContainerFactory.getInstance().getAcreContainer();
        PatternAnalyticsEngine  analyticsEngine = acreContainer.getAnalyticsEngine();

            try {
                SnapshotQuery query = new SnapshotQuery();
                query.addSystemFilter("PSA");
                query.addVersionFilter("V4.0");
                query.addVersionFilter("V3.0");
                query.addPatternFilter("BD_Calls_SF");
                Snapshots snapshots = analyticsEngine.getSnapshots(query);
                printSnapshots(snapshots);
            }
            catch(Throwable t) {
                System.out.println(t.getMessage());
            }
    }


    public void testMultipleVersionsWithoutDateForAllPatterns() throws ParseException {
        AcreContainer acreContainer = AcreContainerFactory.getInstance().getAcreContainer();
        PatternAnalyticsEngine  analyticsEngine = acreContainer.getAnalyticsEngine();

            try {
                SnapshotQuery query = new SnapshotQuery();
                query.addSystemFilter("PSA");
                query.addVersionFilter("V4.0");
                query.addVersionFilter("V3.0");
                Snapshots snapshots = analyticsEngine.getSnapshots(query);
                printSnapshots(snapshots);
            }
            catch(Throwable t) {
                System.out.println(t.getMessage());
            }
    }

    public void testWithOnlyVersionFilter() throws ParseException {
        AcreContainer acreContainer = AcreContainerFactory.getInstance().getAcreContainer();
        PatternAnalyticsEngine  analyticsEngine = acreContainer.getAnalyticsEngine();

        SnapshotQuery query = new SnapshotQuery();


        query.addVersionFilter("V3.0");
        query.addVersionFilter("V4.0");
        query.addPatternFilter("BD_Calls_SF");
        Snapshots snapshots = analyticsEngine.getSnapshots(query);
        printSnapshots(snapshots);
    }

    public void testWithEmptyFilter() throws ParseException {
        AcreContainer acreContainer = AcreContainerFactory.getInstance().getAcreContainer();
        PatternAnalyticsEngine  analyticsEngine = acreContainer.getAnalyticsEngine();

        SnapshotQuery query = new SnapshotQuery();

        query.addPatternFilter("BD_Calls_SF");

        Snapshots snapshots = analyticsEngine.getSnapshots(query);
        printSnapshots(snapshots);
    }


    private void printSnapshots(Snapshots snapshots) {

        Iterator ssIterator = snapshots.getAllSnapshots();

        while ( ssIterator.hasNext() ) {
            Snapshot ss = (Snapshot)ssIterator.next();

            System.out.print(" system = " + ss.getSystem());
            System.out.print(" version= " + ss.getVersion());
            System.out.print(" timestamp="+ss.getTimestamp());
            System.out.println("------------");
            Iterator pmetricsItr =  ss.getPatternMetrics().iterator();
            while ( pmetricsItr.hasNext() ) {
                PatternMetrics pm = (PatternMetrics)pmetricsItr.next();
                printPatternMetrics(pm, 0);
            }
        }
    }

    private void printPatternMetrics(PatternMetrics metrics, int indent) {
        char [] p = new char[indent*4];
        Arrays.fill(p, ' ');
        String buf = new String(p);
        System.out.println(buf + metrics.getPatternName() + " " + metrics.getHitCount());

        Iterator roles = metrics.getRoles().iterator();
        while ( roles.hasNext() ) {
            PatternMetrics role = (PatternMetrics)roles.next();
                printPatternMetrics(role, indent+1);
        }

    }
}
