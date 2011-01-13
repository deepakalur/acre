package org.acre.dao;

import junit.framework.TestCase;

import java.util.Calendar;
import java.util.Date;

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
public class SalsaDbDAOTestCase extends TestCase {

    AcreDbDAO dao;
    protected void setUp() throws Exception {
        dao = new AcreDbDAO();
    }

    public void testExtractTime() {
        Calendar cal = Calendar.getInstance();
        Date dt1, dt2;

        cal.set(2005, 2, 20);
        dt1 = cal.getTime();
        cal.set(2005, 2, 22);
        dt2 = cal.getTime();

        Date dts[] = dao.getCharExtractTimeStamps(dt1, dt2);

        System.out.println("Extracted Times...");
        for ( int i =0; i < dts.length; i++)
            System.out.println(dts[i]);
    }


    public void testExtractVersion() {
        String version;

        version = dao.findVersion("PSA", null);

        System.out.println("Latest Version " + version);


        Calendar cal = Calendar.getInstance();
        Date dt;

        cal.set(2005, 3, 22, 9, 53, 39);
        dt = cal.getTime();

        version = dao.findVersion("PSA", dt);

        System.out.println(dt + "= " + version);


        version = dao.findVersion("global", null);

        System.out.println(version);
    }


    public void testFindExtractTime() {
        System.out.println(dao.findExtractionTime("PSA", "V3.0"));
        System.out.println(dao.findExtractionTime("PSA", "V4.0"));
        System.out.println(dao.findExtractionTime("PSA", "V2.0"));
        System.out.println(dao.findExtractionTime("UNKNOWN", "V2.0"));
    }



    public void testLatestVersions() {
        String[] systems = dao.findAllSystems();

        for ( int i=0; i < systems.length; i++) {
            String version = dao.findVersion(systems[i], null);
            System.out.println(systems[i] + ":" + version);
        }
    }
}
