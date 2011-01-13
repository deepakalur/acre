package org.acre.pdmengine;

import junit.framework.TestSuite;
import junit.framework.Test;

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
 * @version Nov 10, 2004 12:03:43 PM
 */
public class PDMTestSuite extends TestSuite {

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(SamplePatternEngineTestCase.class);
//        suite.addTestSuite(PatternEngineEngineImplTestCase.class);
        suite.addTestSuite(PatternEngineResultCacheTestCase.class);
        suite.addTestSuite(RelcoCommandTestCase.class);
        suite.addTestSuite(RelcoFacadeTestCase.class);
        suite.addTestSuite(PatternEngineAggregatorTestCase.class);
        suite.addTestSuite(RelationshipDiscoveryTestCase.class);
        suite.addTestSuite(DynamicRoleTestCase.class);
        suite.addTestSuite(TransitiveLinksTestCase.class);
        // suite.addTestSuite(PatternBuilderTestCase.class);
        suite.addTestSuite(SystemVersionTimePatternTestCase.class);
        suite.addTestSuite(SearchContextTestCase.class);
        suite.addTestSuite(ExactPatternsTestCase.class);
        return suite;
    }

    public static void main(String args[]) {
        junit.textui.TestRunner.run(suite());
    }


}
