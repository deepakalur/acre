package org.acre.pdmengine;

import java.util.List;

/**
 *
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
 * @version Dec 14, 2004 3:25:13 PM
 */
public class PatternEngineAggregatorTestCase extends PatternEngineBaseTestCase {
    public void testMultipleOrphans() {
        String orphanArtifacts[] = new String[] {"com.sun.sjc.psa.ejb.ResourceHome",
                    "com.sun.sjc.psa.ejb.ProjectHome",
                    "com.sun.sjc.psa.ejb.CustomerHome",
                    "com.sun.sjc.psa.ejb.EmployeeHome"};
        String patterns[] = engine.findPatterns(new SearchContext("PSA", "1.0"),
                orphanArtifacts);

        System.out.println("Orphan artifacts : " + orphanArtifacts);
        for ( int i=0; i < patterns.length; i++ ) {
                System.out.println("PDM Hit =" +  patterns[i]);
        }

        patterns = engine.findPatterns(new SearchContext("PSA", "1.0"),
                (String[])null);
        System.out.println("Orphan artifacts : null ");
        for ( int i=0; i < patterns.length; i++ ) {
                System.out.println("PDM Hit =" +  patterns[i]);
        }

        orphanArtifacts = new String[] {"com.sun.sjc.psa.util.PSAServiceLocator"};
        patterns = engine.findPatterns(new SearchContext("PSA", "1.0"), orphanArtifacts);
        System.out.println("Orphan artifacts : " + orphanArtifacts);
        for ( int i=0; i < patterns.length; i++ ) {
                System.out.println("PDM Hit =" +  patterns[i]);
        }

    }

    public void testFindArtifacts() {

        String orphanArtifact = "com.sun.sjc.psa.dao.CommitmentsDAO";
        String patterns[] = engine.findPatterns(SearchContext.GLOBAL_SEARCH, orphanArtifact);
        System.out.println("Orphan artifact : " + orphanArtifact);
        for ( int i=0; i < patterns.length; i++ ) {
                System.out.println("PDM Hit =" +  patterns[i]);
        }

        assertEquals(patterns[0], "SF_Calls_DAO");
        assertEquals(patterns[1], "Command");

        orphanArtifact = "com.sun.sjc.psa.ejb.ResourceHome";
        System.out.println("Orphan artifact : " + orphanArtifact);
        patterns = engine.findPatterns(SearchContext.GLOBAL_SEARCH, orphanArtifact);
        for ( int i=0; i < patterns.length; i++ ) {
                System.out.println("PDM Hit =" +  patterns[i]);
        }


        assertEquals(patterns[0], "SessionFacade");

        System.out.println("Orphan artifact : " + "EmployeeDelegate");
        patterns  = engine.findPatterns(SearchContext.GLOBAL_SEARCH, "com.sun.sjc.psa.delegate.EmployeeDelegate");
        for ( int i=0; i < patterns.length; i++ ) {
                System.out.println("PDM Hit =" +  patterns[i]);
        }

        System.out.println("Orphan artifact : " + "PSAFilter");
        patterns  = engine.findPatterns(SearchContext.GLOBAL_SEARCH, "com.sun.sjc.psa.request.PSAFilter");
        for ( int i=0; i < patterns.length; i++ ) {
                System.out.println("PDM Hit =" +  patterns[i]);
        }




        System.out.println("Orphan artifact : " + "ProjectHome");
        patterns  = engine.findPatterns(SearchContext.GLOBAL_SEARCH, "com.sun.sjc.psa.ejb.ProjectHome");
        for ( int i=0; i < patterns.length; i++ ) {
                System.out.println("PDM Hit =" +  patterns[i]);
        }

        System.out.println("Orphan artifact : " + "ProjectFacade");
        patterns = engine.findPatterns(SearchContext.GLOBAL_SEARCH, "com.sun.sjc.psa.ejb.ProjectFacade");
        for ( int i=0; i < patterns.length; i++ ) {
                System.out.println("PDM Hit =" +  patterns[i]);
        }

        System.out.println("Orphan artifact : " + "PSAServiceLocator");
        patterns = engine.findPatterns(SearchContext.GLOBAL_SEARCH, "com.sun.sjc.psa.util.PSAServiceLocator");
        for ( int i=0; i < patterns.length; i++ ) {
                System.out.println("PDM Hit =" +  patterns[i]);
        }

        System.out.println("Orphan artifact : " + "PSASERVICELOCATOR");
        patterns = engine.findPatterns(SearchContext.GLOBAL_SEARCH, "COM.SUN.SJC.PSA.UTIL.PSASERVICELOCATOR");
        for ( int i=0; i < patterns.length; i++ ) {
                System.out.println("PDM Hit =" +  patterns[i]);
        }

        System.out.println("Orphan artifact : " + "psaservicelocator");
        patterns  = engine.findPatterns(SearchContext.GLOBAL_SEARCH, "com.sun.sjc.psa.util.psaservicelocator");
        for ( int i=0; i < patterns.length; i++ ) {
                System.out.println("PDM Hit =" +  patterns[i]);
        }

    }

    public void testForUnknownArtifacts() {
        String patterns[] = engine.findPatterns(SearchContext.GLOBAL_SEARCH, "UnavailableArtifact1");

        assertEquals(patterns.length, 0);

        patterns = engine.findPatterns(SearchContext.GLOBAL_SEARCH, "com.sun.sjc.psa.util.PSAServiceLocator1");

        assertEquals(patterns.length, 0);

        patterns = engine.findPatterns(SearchContext.GLOBAL_SEARCH, "1com.sun.sjc.psa.ejb.ProjectFacade");

        assertEquals(patterns.length, 0);

    }

    public void testForNullArtifacts() {
        String patterns[] = engine.findPatterns(SearchContext.GLOBAL_SEARCH, "");
        assertEquals(patterns.length, 0);

        patterns = engine.findPatterns(SearchContext.GLOBAL_SEARCH, (String)null);
        assertEquals(patterns.length, 0);

    }
}
