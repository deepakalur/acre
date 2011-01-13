package org.acre.pdmengine;

import org.acre.pdmengine.model.PatternResult;

/**
 * User: rajmohan@sun.com
 * Date: Jan 11, 2005
 * Time: 3:00:56 PM
 */
public class DynamicRoleTestCase extends PatternEngineBaseTestCase {
    public void testDynamicRole() {
        PatternResult result = engine.execute("PackageView");

        System.out.println(result);
    }


}
