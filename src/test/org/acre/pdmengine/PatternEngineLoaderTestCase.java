package org.acre.pdmengine;

import org.acre.pdm.PDM;
import org.acre.pdmengine.model.PatternResult;

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
 * @version Dec 10, 2004 9:14:47 PM
 */
public class PatternEngineLoaderTestCase extends PatternEngineBaseTestCase {

    public void testPDM() {
        PatternResult result = engine.execute("FC_calls_SF");
        System.out.println(result);
    }

    public void atestLoadingAllPQLPDMs() throws InterruptedException {

        Thread executor = new Thread(
                new Runnable() {
                    public void run() {
                        Iterator pdms = facade.getGlobalPatternModels().iterator();
                            while (pdms.hasNext()) {
                                PDM pdm = (PDM)pdms.next();
                                if ( !"GroovyPDM".equalsIgnoreCase(pdm.getType()) ) {
                                    final String pdmName = pdm.getName();
                                    System.out.println("Executing... " + pdmName);
                                    engine.execute(pdm);
                                    System.out.println("Done.\n\n\n");
                                }
                            }
                    }
                }
        );
        executor.start();

        executor.join();
    }
}
