package org.acre.pdmengine;

import org.acre.pdmengine.model.PatternResult;

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
public class ExactPatternsTestCase extends PatternEngineBaseTestCase {

    public void testSequentialExecution() {
        System.out.println(engine.execute("BD_Calls_SF"));
        System.out.println(engine.execute("BusinessDelegate"));
    }

    public void testBDcallsSF() {
        System.out.println(engine.execute("BD_Calls_SF"));
    }

    public void testIFinterceptsFC() {
        System.out.println(engine.execute("IF_intercepts_FC"));
    }

    public void testFCcallsSF() {
        System.out.println(engine.execute("FC_calls_SF"));
    }

    public void testCommandcallsSF() {
        System.out.println(engine.execute("Command_Calls_SF"));
    }

    public void testmA_FC_BD_SF_DAO() {
        System.out.println(engine.execute("mA_FC_BD_SF_DAO"));
    }

    public void testBDcallsSingletons() {
        System.out.println(engine.execute("BD_Calls_Singletons"));
    }

    public void testSFcallsDAO() {
        System.out.println(engine.execute("SF_calls_DAO"));
    }

}
