package org.acre.lang;

import junit.framework.TestCase;
import org.acre.lang.pql.pdbc.PQLException;

import java.util.Map;
import java.util.HashMap;

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
public class QueryPreProcessorTestCase extends TestCase {

    private QueryPreProcessor preProcessor;

    protected void setUp() throws Exception {
        QueryPreProcessor.clearMarcos();
        preProcessor = new QueryPreProcessor();
    }

    public void testDefineMacros() throws PQLException {
        String pql =
                "#define calledClasses as \"methods.calls.parentClass\";" +
                "return select c from classes c where c.$calledClasses.name = \"abc\"; ";

        String processedQuery = preProcessor.compile(pql);

        assertEquals("return select c from classes c where c.methods.calls.parentClass.name = \"abc\";",
                processedQuery);

    }

    public void testDefineSyntax() {
        String pql =
                "#definexyz calledClassesas\"methods.calls.parentClass\"; " +
                "return select c from classes c where c.$calledClasses.name = \"abc\"; ";

        try {
            preProcessor.compile  (pql);
            fail("Must throw an pre-processor sytax exception ");
        } catch (PQLException e) {
        }

    }


    public void testMacroExpansion() throws PQLException {

        Map<String, String> m = new HashMap<String, String>();
        m.put("calledclasses", "methods.calls.parentClass");

        String query = "select c.$calledClasses.name from classes c";

        String processedQuery = preProcessor.expandMacros(query, m);

        assertEquals("select c.methods.calls.parentClass.name from classes c", processedQuery);
    }


    public void testQueryWithSingleSpace() throws PQLException {
        String query = "#define calledClasses as \"methods.calls.**.parentClass\";" +
                "define classesCallingServiceLocators as " +
                "select c " +
                "from classes c " +
                "where c.$calledClasses in EJBServiceLocators;";

        String processedQuery = preProcessor.compile(query);

        String expectedResult =
                "define classesCallingServiceLocators as " +
                "select c " +
                "from classes c " +
                "where c.methods.calls.**.parentClass in EJBServiceLocators;";

        assertEquals(processedQuery, expectedResult);

    }

    public void testQueryWithMultipleSpaces() throws PQLException {
        String query = "#define    calledClasses    as        \"methods.calls.**.parentClass\"    ;" +
                "define classesCallingServiceLocators as " +
                "select c " +
                "from classes c " +
                "where c.$calledClasses in EJBServiceLocators;";

        String processedQuery = preProcessor.compile(query);

        String reference =
                "define classesCallingServiceLocators as " +
                "select c " +
                "from classes c " +
                "where c.methods.calls.**.parentClass in EJBServiceLocators;";
        assertEquals(reference, processedQuery);
    }

    public void testQueryWithoutHashPrefix() {
        String query = "define    allCalledClasses    as        \"methods.calls.**.parentClass\"    ;" +
                "define classesCallingServiceLocators as " +
                "select c " +
                "from classes c " +
                "where c.$allCalledClasses in EJBServiceLocators; " +
                "define BusinessDelegates as " +
                "select c from classesCallingServiceLocators c " +
                "where  c.fields.type in SFInterfaces;";

        try {
            String processedQuery = preProcessor.compile(query);
            fail("Should have raised an unknown macro exception");
        } catch (PQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void testQueryWith$inDefinition() throws PQLException {
        String query = "#define    $allCalledClasses    as        \"methods.calls.**.parentClass\"    ;" +
                "define classesCallingServiceLocators as " +
                "select c " +
                "from classes c " +
                "where c.$allCalledClasses in EJBServiceLocators;";

        String processedQuery = preProcessor.compile(query);

        String expectedResult =
                "define classesCallingServiceLocators as " +
                "select c " +
                "from classes c " +
                "where c.methods.calls.**.parentClass in EJBServiceLocators;";

        assertEquals(expectedResult, processedQuery);
    }

    public void testMacrosCaseSensitivity() throws PQLException {
        String query = "#DEFinE    CALLEDClasses    as        \"methods.calls.**.parentClass\"    ;" +
                "define classesCallingServiceLocators as " +
                "select c " +
                "from classes c " +
                "where c.$calledClasses in EJBServiceLocators;";

        String processedQuery = preProcessor.compile(query);

        String expectedResult =
                "define classesCallingServiceLocators as " +
                "select c " +
                "from classes c " +
                "where c.methods.calls.**.parentClass in EJBServiceLocators;";

        assertEquals(expectedResult, processedQuery);

    }

    public void testUndefinedMacroReference() {
        String query =
                "define classesCallingServiceLocators as " +
                "select c " +
                "from classes c " +
                "where c.$allCalledClasses in EJBServiceLocators; " +
                "define BusinessDelegates as " +
                "select c from classesCallingServiceLocators c " +
                "where  c.fields.type in SFInterfaces;";

        try {
            String processedQuery = preProcessor.compile(query);
            fail("Should have raised an unknown macro exception");
        } catch (PQLException e) {
            System.out.println(e.getMessage());
        }


    }

    public void testQueryWithNoMacros() throws PQLException {
        String query =
                "define classesCallingServiceLocators as " +
                "select c " +
                "from classes c " +
                "where c.methods.calls.parentClass in EJBServiceLocators;\n" +
                "return select m from methods m;";


        String processedQuery = preProcessor.compile(query);

        assertEquals(query, processedQuery);


    }

    public void testServiceLocatorQueryWithComments() throws PQLException {
        String serviceLocatorQuery =
        "// service locator \n" +
        "#define calledclasses as \"methods.calls.parentClass\"; \n" +
        "#define returnTypes as \"methods.returnType.name\"; \n" +
        "define EJBServiceLocators as \n" +
        "select c from classes c \n" +
        "where c.$calledclasses.name=\"javax.naming.InitialContext\" \n"+
        "and c.$returnTypes in (\"javax.ejb.EJBObject\", \"javax.ejb.EJBHome\", \"javax.ejb.EJBLocalHome\");\n" +
        " return EJBServiceLocators; " ;

        String processedQuery = preProcessor.compile(serviceLocatorQuery);


        String reference =
        " \n" +
        "define EJBServiceLocators as \n" +
        "select c from classes c \n" +
        "where c.methods.calls.parentClass.name=\"javax.naming.InitialContext\" \n"+
        "and c.methods.returnType.name in (\"javax.ejb.EJBObject\", \"javax.ejb.EJBHome\", \"javax.ejb.EJBLocalHome\");\n" +
        " return EJBServiceLocators;" ;

        assertEquals(reference, processedQuery);

    }

    public void testServiceLocatorQueryWithComments2() throws PQLException {
        String serviceLocatorQuery =
        "// service locator \n" +
        "#define calledclasses as \"methods.calls.parentClass\"; " +
        "// defining return types macro \n" +
        "#define returnTypes as \"methods.returnType.name\"; " +
        " /* servicelocator pattern query " +
        "*/ define EJBServiceLocators as " +
        "select c from classes c " +
        "where c.$calledclasses.name=\"javax.naming.InitialContext\" "+
        "and c.$returnTypes in (\"javax.ejb.EJBObject\", \"javax.ejb.EJBHome\", \"javax.ejb.EJBLocalHome\"); " +
        " return EJBServiceLocators; " ;

        String processedQuery = preProcessor.compile(serviceLocatorQuery);

        System.out.println(processedQuery);

    }

    public void testMacroAcrossQueries() throws PQLException {
        String query1 =  "#define calledClasses as \"methods.calls.parentClass\"; \n";
        preProcessor.compile(query1);

        String query2 =  " return select c.$calledClasses.name from classes c;" ;

        String processedQuery = preProcessor.compile(query2);

        System.out.println(processedQuery);

    }


    public void testMacrosWithIncludes() throws PQLException {
        String query =  "include allMacros; \n " +
                        " return select c.$calledClasses.name from classes c;" ;

        String processedQuery = preProcessor.compile(query);

        String reference =  "include allMacros; \n " +
                        " return select c.methods.calls.parentClass.name from classes c;" ;
        assertEquals(reference, processedQuery);

    }

    public void testNonMacro$References() throws PQLException {
        String query =  "return select c from classes c where c.name like \"$test$\";" ;

        String processedQuery = preProcessor.compile(query);

        System.out.println(processedQuery);

        assertEquals(query, processedQuery);
    }

    public void testComments() {

        String s =  " // comment \n" +
                    " select c from classes c ; \n" +
                    " /* comment2 */ \n"+
                    " /* multi-line " +
                    "    comment */  \n" +
                    " /* commentX */ select d from classes d";

        String t = preProcessor.stripComments(s);

        System.out.println(t);

//        Scanner scanner = new Scanner(s);

//        scanner.useDelimiter("//.*");
//        System.out.println(scanner.findInLine("//.*"));

//        System.out.println(scanner.findInLine("\\*"));

/**
        Pattern p = Pattern.compile("/\\*.*$", Pattern.MULTILINE);
        Matcher m = p.matcher(s);
        while ( m.find())
            System.out.println(m.group());

        System.out.println("-------------");
        System.out.println(s);
        System.out.println("-------------");

        String d = s.replaceAll("//.*", "");
**/
//        String e = d.replaceAll("/\\*.*\\*/", "");
//        System.out.println(e);
//        System.out.println("-------------");

    }
}
