package org.acre.pdmengine;

import org.acre.pdmengine.model.RelationshipResult;


import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.List;
import java.util.Iterator;

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
 * @version Dec 23, 2004 5:18:11 PM
 */
public class RelationshipDiscoveryTestCase extends PatternEngineBaseTestCase {

    protected void setUp() throws Exception {
        super.setUp();
//        Logger.getLogger("com.sun.salsa").setLevel(Level.OFF);
    }

    public void testBDCallingSFRelationship() {
        System.out.println("Discover calls relationship between BusinessDelegate and SessionFacade");
        System.out.println("-------------------------------------------------------");

        String [] pdms = new String[] {"BusinessDelegate", "SessionFacade"};
        List results = engine.discoverRelationships(pdms, new String[]{"Calls"});

        Iterator resultsItr = results.iterator();
        while ( resultsItr.hasNext()) {
            System.out.println(resultsItr.next());
        }
        System.out.println("-------------------------------------------------------");
    }

    public void testSFCallingSingletons() {
        System.out.println("Discover calls relationship between SessionFacade and SingletonGroovyPDM");
        System.out.println("-------------------------------------------------------");
        String [] pdms = new String[] {"SessionFacade", "SingletonGroovyPDM" };
        List results = engine.discoverRelationships(pdms, new String[]{"Calls"});

        Iterator resultsItr = results.iterator();
        while ( resultsItr.hasNext()) {
            System.out.println(resultsItr.next());
        }
        System.out.println("-------------------------------------------------------");
    }

    public void testSFCallingSingletons2() {
        System.out.println("Discover calls, Implements relationship among Command, FC, BD, SF, Singleton");
        System.out.println("-------------------------------------------------------");
        String pdms[] = new String[] {"Command", "FrontController", "BusinessDelegate",
                                      "SessionFacade", "SingletonGroovyPDM" /*, "DataAccessObject"*/};
        String types[] = new String[]{"Calls", "Implements"};
        List results = engine.discoverRelationships(pdms, types);
        Iterator resultsItr = results.iterator();
        while ( resultsItr.hasNext()) {
            System.out.println(resultsItr.next());
        }

        System.out.println("-------------------------------------------------------");
    }

    public void testPublicClassesRelationships() {
        System.out.println("Discover calls among public classes");
        System.out.println("-------------------------------------------------------");
        String pdms[] = new String[] {"authPCPDM", "ejbPCPDM", "corePCPDM",
                                      "delegatePCPDM", "daoPCPDM", "handlersPCPDM", "requestPCPDM", "handlerPCPDM",
                                      "helperPCPDM", "tagPCPDM", "utilPCPDM"};
        String types[] = new String[]{"Calls"};
        List results = engine.discoverRelationships(pdms, types);
        Iterator resultsItr = results.iterator();
        while ( resultsItr.hasNext()) {
            System.out.println(resultsItr.next());
        }

        System.out.println("-------------------------------------------------------");
    }
}
