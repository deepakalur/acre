package org.acre.pdmengine;

import junit.framework.TestCase;
import org.acre.pdmengine.model.PatternResult;
import org.acre.pdmengine.model.impl.PatternResultImpl;
import org.acre.pdmengine.util.InputSpec;
import org.acre.pdmengine.core.PatternResultCache;
import sun.misc.SoftCache;


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
 * @version Dec 6, 2004 6:42:38 PM
 */
public class PatternEngineResultCacheTestCase extends PatternEngineBaseTestCase {
    public void testResultCace() {

        PatternResultCache resultCache = new PatternResultCache();
        PatternResult result, result2;

        InputSpec is= new InputSpec("TestPDM");
        result = (PatternResult)resultCache.get(is);
        assertNull(result);

        result = new PatternResultImpl(null);

        resultCache.put(is, result);
        result2 = (PatternResult)resultCache.get(is);
        assertEquals(result, result2);

    }

    public void testResultCace2() {

        PatternResultCache resultCache = new PatternResultCache();
        PatternResult result, result2;

        InputSpec is= new InputSpec("TestPDM", "psa", "V2.0", null);
        result = (PatternResult)resultCache.get(is);
        assertNull(result);

        result = new PatternResultImpl(null);

        resultCache.put(is, result);
        result2 = (PatternResult)resultCache.get(is);
        assertEquals(result, result2);

    }

    public void testBDCallsSF() {
        long l = System.currentTimeMillis();
        PatternResult patternResult = engine.execute("BD_calls_SF");
        System.out.println(System.currentTimeMillis() - l);

        for ( int i = 0; i < 4; i++ ) {
            l = System.currentTimeMillis();
            patternResult = engine.execute("BD_calls_SF");
            System.out.println(System.currentTimeMillis() - l);
        }

        engine.refreshAll();

        l = System.currentTimeMillis();
        patternResult = engine.execute("BD_calls_SF");
        System.out.println(System.currentTimeMillis() - l);

        for ( int i = 0; i < 4; i++ ) {
            l = System.currentTimeMillis();
            patternResult = engine.execute("BD_calls_SF");
            System.out.println(System.currentTimeMillis() - l);
        }

    }

    public void testFrontController() {

        for ( int i = 0; i < 10; i ++) {
            System.out.println(engine.execute("FrontController"));
        }
    }


}
