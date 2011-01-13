package org.acre.pdmengine;

import java.io.IOException;

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
public class CacheTestCase extends PatternEngineBaseTestCase {

    public void testImplicitLoading() {

        long l = System.currentTimeMillis();

        String [] orphanArtifacts = new String[] {"com.sun.sjc.psa.util.PSAServiceLocator"};
        String [] patterns = engine.findPatterns(new SearchContext("global", "latest"), orphanArtifacts);
        System.out.println("Orphan artifacts : " + orphanArtifacts);
        for ( int i=0; i < patterns.length; i++ ) {
                System.out.println("PDM Hit =" +  patterns[i]);
        }
        System.out.println("TimeStamp: Implict Loading : " + (System.currentTimeMillis() - l));
    }
    public void textExplicitLoading() {

        long l = System.currentTimeMillis();
        engine.loadAllPatterns(new SearchContext("PSA", "1.0"), false, null);
        System.out.println("TimeStamp: Explict Loading of PSA + 1.0 " + (System.currentTimeMillis() - l)/1000);

        l = System.currentTimeMillis();
        String [] orphanArtifacts = new String[] {"com.sun.sjc.psa.util.PSAServiceLocator"};
        String [] patterns = engine.findPatterns(new SearchContext("PSA", "1.0"), orphanArtifacts);
        System.out.println("Orphan artifacts : " + orphanArtifacts);
        for ( int i=0; i < patterns.length; i++ ) {
                System.out.println("PDM Hit =" +  patterns[i]);
        }
        System.out.println("TimeStamp: Finding After Explict Loading of PSA+1.0 " + (System.currentTimeMillis() - l)/1000);


        l = System.currentTimeMillis();
        engine.loadAllPatterns(SearchContext.GLOBAL_SEARCH, false, null);
        System.out.println("Explict Loading of global + latest " + (System.currentTimeMillis() - l)/1000);


        l = System.currentTimeMillis();
        patterns = engine.findPatterns(new SearchContext("global", "latest"), orphanArtifacts);
        System.out.println("Orphan artifacts : " + orphanArtifacts);
        for ( int i=0; i < patterns.length; i++ ) {
                System.out.println("PDM Hit =" +  patterns[i]);
        }
        System.out.println("TimeStamp: Finding after Explict Loading of global + latest " + (System.currentTimeMillis() - l)/1000);

    }


    public void testMultipleExplicitLoads() {
        long l = System.currentTimeMillis();
        engine.loadAllPatterns(new SearchContext("PSA", "1.0"), false, null);
        System.out.println("TimeStamp: Explict Loading of PSA + 1.0 " + (System.currentTimeMillis() - l)/1000);

        l = System.currentTimeMillis();
        engine.loadAllPatterns(SearchContext.GLOBAL_SEARCH, false, null);
        System.out.println("TimeStamp: Explict Loading of global + latest " + (System.currentTimeMillis() - l)/1000);

        l = System.currentTimeMillis();
        engine.loadAllPatterns(new SearchContext("PSA", "1.0"), false, null);
        System.out.println("TimeStamp: Explict Loading of PSA + 1.0 " + (System.currentTimeMillis() - l)/1000);

        l = System.currentTimeMillis();
        engine.loadAllPatterns(SearchContext.GLOBAL_SEARCH, false, null);
        System.out.println("TimeStamp: Explict Loading of global + latest " + (System.currentTimeMillis() - l)/1000);

    }


    public void testFindPatternPerformance() throws IOException {

        long l = System.currentTimeMillis();
        engine.loadAllPatterns(new SearchContext("PSA", "1.0"), false, null);
        System.out.println("TimeStamp: Explict Loading of PSA + 1.0 " + (System.currentTimeMillis() - l)/1000);


        l = System.currentTimeMillis();
        String [] orphanArtifacts = new String[] {"com.sun.sjc.psa.util.PSAServiceLocator"};
        String [] patterns = engine.findPatterns(new SearchContext("PSA", "1.0"), orphanArtifacts);
        System.out.println("Orphan artifacts : " + orphanArtifacts);
        for ( int i=0; i < patterns.length; i++ ) {
                System.out.println("PDM Hit =" +  patterns[i]);
        }
        System.out.println("TimeStamp: Finding orphan artifact " + (System.currentTimeMillis() - l)/1000);

        l = System.currentTimeMillis();
        patterns = engine.findPatterns(new SearchContext("PSA", "1.0"), orphanArtifacts);
        System.out.println("Orphan artifacts : " + orphanArtifacts);
        for ( int i=0; i < patterns.length; i++ ) {
                System.out.println("PDM Hit =" +  patterns[i]);
        }
        System.out.println("TimeStamp: Finding orphan artifact " + (System.currentTimeMillis() - l)/1000);

        l = System.currentTimeMillis();
        patterns = engine.findPatterns(new SearchContext("PSA", "1.0"), orphanArtifacts);
        System.out.println("Orphan artifacts : " + orphanArtifacts);
        for ( int i=0; i < patterns.length; i++ ) {
                System.out.println("PDM Hit =" +  patterns[i]);
        }
        System.out.println("TimeStamp: Finding orphan artifact " + (System.currentTimeMillis() - l)/1000);

    }

}
