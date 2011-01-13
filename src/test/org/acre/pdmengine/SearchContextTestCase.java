package org.acre.pdmengine;

import org.acre.pdmengine.model.PatternResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

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
public class SearchContextTestCase extends PatternEngineBaseTestCase {

    public void testDefaultSearchContext() {
        System.out.println(engine.execute("SessionFacade", null, null)[0]);

        SearchContext ctx = new SearchContext();
        System.out.println(engine.execute("SessionFacade", null, ctx)[0]);
    }

    public void testSystem() {

        SearchContext ctx = new SearchContext();
        ctx.addSystem("Psa");
        System.out.println(engine.execute("SessionFacade", null, ctx)[0]);
    }

    public void testMultiSystems() {

        SearchContext ctx = new SearchContext();
        ctx.addSystem("Psa");
        ctx.addSystem("Petstore");
        print(engine.execute("SessionFacade", null, ctx));
    }

    public void testSystemAndVersion() {

        SearchContext ctx = new SearchContext();
        ctx.addSystem("Psa");
        ctx.addVersion("V4.0");
        print(engine.execute("SessionFacade", null, ctx));
    }


    public void testSystemAndMultiVersion() {

        SearchContext ctx = new SearchContext();
        ctx.addSystem("Psa");
        ctx.addVersion("V4.0");
        ctx.addVersion("V5.0");
        print(engine.execute("SessionFacade", null, ctx));
    }

    public void testMultiSystemAndMultiVersion() {

        SearchContext ctx = new SearchContext();
        ctx.addSystem("Psa");
        ctx.addSystem("Petstore");
        ctx.addVersion("V4.0");
        ctx.addVersion("V5.0");
        print(engine.execute("SessionFacade", null, ctx));
    }

    public void testMultiSystemAndVersion() {

        SearchContext ctx = new SearchContext();
        ctx.addSystem("Psa");
        ctx.addSystem("Petstore");
        ctx.addVersion("V4.0");
        print(engine.execute("SessionFacade", null, ctx));
    }

    public void testFrontControllerWithTime() throws ParseException {
            Date timestamp = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2005-05-09 10:08:34");
            SearchContext ctx = new SearchContext();
            ctx.addSystem("psa");
            ctx.addTimestamp(timestamp);

            PatternResult patternResults[] = engine.execute("FrontController", null, ctx);
            print(patternResults);
    }

    public void testStartAndEndDate() throws ParseException {

        Date dt1 = new SimpleDateFormat("dd-MM-yyyy").parse("01-01-2005");
        Date dt2 = Calendar.getInstance().getTime();

        SearchContext ctx = new SearchContext();
        ctx.setStartDate(dt1);
        ctx.setEndDate(dt2);
        print(engine.execute("SessionFacade", null, ctx));

    }

    public void testStartAndEndDateWithSystem() throws ParseException {

        Date dt1 = new SimpleDateFormat("dd-MM-yyyy").parse("01-01-2005");
        Date dt2 = Calendar.getInstance().getTime();

        SearchContext ctx = new SearchContext();
        ctx.addSystem("PSA");
        ctx.setStartDate(dt1);
        ctx.setEndDate(dt2);
        print(engine.execute("SessionFacade", null, ctx));

    }

    public void testSearchType() {

        SearchContext ctx = new SearchContext();
        ctx.addSystem("Psa");
        ctx.setSearchType(SearchContext.COARSE);
        print(engine.execute("SessionFacade", null, ctx));

        ctx.setSearchType(SearchContext.EXACT);
        print(engine.execute("SessionFacade", null, ctx));

    }

    void print(PatternResult results[]) {
        System.out.println("# of results " + results.length);
        for ( int i=0; i < results.length; i++ ) {
            System.out.println(results[i]);
        }
    }

}
