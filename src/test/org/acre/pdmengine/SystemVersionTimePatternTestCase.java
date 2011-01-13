package org.acre.pdmengine;

import org.acre.pdmengine.model.PatternResult;
import org.acre.lang.pql.pdbc.PQLStatement;
import org.acre.lang.pql.pdbc.PQLException;

import java.util.logging.Level;
import java.util.Map;
import java.util.Properties;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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
public class SystemVersionTimePatternTestCase extends PatternEngineBaseTestCase {
    public void testSimpleTime() {
            PatternResult patternResults[] =
                    searchEngine.execute("FrontController", null,
                            new String[]{"psa"}, new String[]{"v4.0", "v2.0"},
                            SearchContext.EXACT);

            printPatternResults(patternResults);

    }

    private void printPatternResults(PatternResult patternResults[]) {
        for ( int i=0; i < patternResults.length; i++ ) {
            log.info("<Pattern Result>");
            log.info("------------");            
            log.info(patternResults[i].toString());
            log.info("</Pattern Result>");
        }
    }

    public void testScopingRules() {
        try {
            PQLStatement statement = pqlConnection.createStatement();


            Properties p = new Properties();
            p.put("system", "\"psa\"");
            p.put("version" , "\"v4.0\"");
            pqlConnection.setEnvironment(p);
            Map result = statement.executeQuery("select c from classes c where c.shortName=\"PSAServlet\";");

            System.out.println(result);

            p.put("system", "\"psa\"");
            p.put("version" , "null");

            result = statement.executeQuery("select c from classes c where c.shortName=\"PSAServlet\";");

            System.out.println(result);


            p.put("system", "\"psa\"");
            p.put("version" , "\"v4.0\"");
            pqlConnection.setEnvironment(p);
            result = statement.executeQuery("select c from classes c where c.shortName=\"PSAServlet\";");

            System.out.println(result);


        } catch (PQLException e) {
            e.printStackTrace();
        }

    }

    public void testFrontControllerWithNullArguments() {
            PatternResult patternResults[] =
                    (searchEngine).execute("FrontController", null, null, (String[]) null,SearchContext.EXACT);

        printPatternResults(patternResults);
    }


    public void testFrontControllerWithMultiVersions() {
            PatternResult patternResults[] =
                    (searchEngine).execute("FrontController", null,
                            new String[]{"psa"}, new String[]{"v4.0", "v2.0"},
                            SearchContext.EXACT);
        printPatternResults(patternResults);
    }

    public void testFrontControllerWithSingleVersions() {
            PatternResult patternResults[] =
                    searchEngine.execute("FrontController", null, new String[]{"psa"},
                            new String[]{"v4.0"}, SearchContext.EXACT);
        printPatternResults(patternResults);
    }

    public void testFrontControllerWithMultipleSystems() {
            PatternResult patternResults[] =
                    searchEngine.execute("FrontController", null,
                            new String[]{"psa", "psa"}, new String[]{"v4.0", "v2.0"},
                            SearchContext.EXACT);
        printPatternResults(patternResults);
    }


    public void testFrontControllerWithTime() throws ParseException {
            Date timestamp = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2005-05-09 10:08:34");

            System.out.println(timestamp);
            System.out.println(timestamp.toString());
            System.out.println(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(timestamp));
            PatternResult patternResults[] =
                    searchEngine.execute("FrontController", null,
                            new String[]{"psa"}, new Date[]{timestamp},
                            SearchContext.EXACT);
            printPatternResults(patternResults);
    }

    public void testFrontControllerWithMultipleTime() throws ParseException {
            Date timestamp1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2005-03-16 11:18:32");
            Date timestamp2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2005-05-26 11:24:35");

            PatternResult patternResults[] =
                    searchEngine.execute("FrontController", null, new String[]{"psa"},
                            new Date[]{timestamp1, timestamp2},
                            SearchContext.EXACT);

            printPatternResults(patternResults);
    }

}
