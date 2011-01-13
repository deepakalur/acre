package com.sun.salsa.pdm.component;

import java.util.*;
import com.sun.salsa.lang.runtime.PQL;
import com.sun.salsa.lang.runtime.lib.PDMComponentBase;
import com.sun.salsa.pdm.component.*;

/**
 * PDM Component test class
 * @author Yury Kamen
 */
public class TestJsp extends PDMComponentBase {
    String name        = 'This is my name:        '  + this.class.name
    String description = 'This is my description: '  + this.class.name
    String metadata    = 'This is my metadata:    '  + this.class.name

    public TestJsp() {}

    readDatabase() {
        database = "factextractor/test.ta"
        //database = "database/factdb/psa/psaFacts.ta"
        println '    Create PQL instance, and load facts database: ' + database
        this.pql = new com.sun.salsa.lang.runtime.PQL(database)
    }

    printQuery(query) {
        println '    +++++ Executing query: ' + query

        result = pql.executePQL(query)

        println '    ===================== Query result: ===================================='

        println result
        if(false) {
            for (e in result) {
                println '========== query element ============ ' + e

                        //      // TODO: this does not work yet
                        //        for (name in e.names) {
                        //            value = e[name]
                        //            println "     name=${name}  value=${e[name]}"
                        //        }

                println '            e.names  = ' + e.names
                e.names.each() {  println ' +++++ name  = ' + it }
                println '            e.values = '
                e.values.each() {  println ' +++++ value = ' + it }
                println '            e[0]     = ' + e[0]

                        // println '            e.res  = ' + e.res // TODO: this does not work yet

                        // // TODO: this does not work yet
                        //for (x in e) {
                        //    println '    query element = ' + x
                        //}
            }
        }
        println '    ========================================================================'
    }

    Object execute(Map inputs) {
        println "##### Start  executing : "  + this.class.name
        println "      inputs=${inputs}"

        readDatabase()
        //printQuery('SELECT c.parentPackage FROM classes c WHERE c.parentPackage.name LIKE "j.*"')
        //printQuery('SELECT a FROM annotations a')

        //printQuery('RETURN SELECT a.arguments FROM annotations a where a.arguments.name LIKE "%"  ')
        //printQuery('RETURN SELECT a.arguments.shortName FROM annotations a where a.arguments.shortName = "JSP"')
        //printQuery('RETURN SELECT a.arguments.stringValue FROM annotations a where a.arguments.stringValue = "scriptletCount"')
        //printQuery('RETURN SELECT x.annotation.parentField from annotation_arguments x')
        //printQuery('RETURN SELECT a.parentField FROM annotations a')
        //printQuery('RETURN SELECT f.shortName FROM classes AS c, c.fields AS f WHERE f.shortName = "$salsa$scriptletCount" ')
        //printQuery('RETURN SELECT c.fields.shortName FROM classes c WHERE c.fields.shortName = "$salsa$scriptletCount" ')
        //printQuery('RETURN SELECT c.fields.shortName FROM classes c WHERE c.fields.shortName  LIKE "%scriptletCount" ')
        //printQuery('RETURN SELECT a.annotation FROM annotation_arguments a WHERE a.name = "my.salsa.test.TestJsp.$salsa$scriptletTotalLines$annotation$1$arg$2"')
        //printQuery('RETURN SELECT a.annotation.parentClass from annotation_arguments a WHERE a.name LIKE ".*scriptletTotalLines.*2"')
        //printQuery('RETURN SELECT a.annotation.parent FROM annotation_arguments a WHERE a.name LIKE ".*"')


        // Get All JSP pages that have scriptlets  // a.arguments.shortName, a.arguments.stringValue

/*
        printQuery("""
// Get All JSP pages that have scriptlets
RETURN
SELECT f.parentClass
FROM fields f
WHERE f.shortName = "$salsa$scriptletCount"
//  AND f.annotations.arguments.shortName = "type"
//  AND f.annotations.arguments.stringValue = "scriptletCount"
  AND f.annotations.arguments.shortName = "value"
  AND f.annotations.arguments.stringValue > "10000"
""")
*/
/*
    printQuery("""
// Get All JSP pages that have scriptlets
RETURN
SELECT a.parentField.parentClass
FROM annotations a
WHERE a.arguments.shortName = "type"
  AND a.arguments.stringValue = "scriptletCount"
  AND a.arguments.shortName = "value"
  AND a.arguments.stringValue > "10000"
""")
*/
/*
        printQuery("""
// Get All JSP pages
RETURN
SELECT a.parentClass
FROM annotations a
WHERE a.arguments.shortName = "type"
  AND a.arguments.stringValue = "JSP"
  AND a.arguments.shortName = "value"
  AND a.arguments.stringValue = "JSP"
""")
*/
/*
        printQuery("""
// Get All JSP pages that have many scriptlet lines
RETURN
SELECT a.parentField.parentClass
FROM annotations a
WHERE a.arguments.shortName = "type"
  AND a.arguments.stringValue = "scriptletTotalLines"
  AND a.arguments.shortName = "value"
  AND a.arguments.stringValue > "5"
""")
*/

    printQuery("""

DEFINE classesWithTotalLinesAnnotations AS
 SELECT a.annotation.parentField.parentClass
 FROM annotation_arguments a
 WHERE a.shortName = "type"
   AND a.stringValue = "scriptletTotalLines";


DEFINE classesWithLargeAnnotationValues AS
 SELECT a.annotation.parentField.parentClass
 FROM annotation_arguments a
 WHERE a.shortName = "value"
   AND a.stringValue > "5";

return classesWithTotalLinesAnnotations AND classesWithLargeAnnotationValues;
    """)


    }
}



/*   --------------------------------- Obsolete - remove me later -------------------
    URL url = Thread.currentThread().getContextClassLoader().getResource("hoge.groovy");
    ClassLoader parent = getClass().getClassLoader();
    GroovyClassLoader loader = new GroovyClassLoader(parent);
    Class groovyClass = loader.parseClass(url.openStream());
    containerFactory = (GroovyObject) groovyClass.newInstance();
    S2Container container = container.getContainer();
*/
