/*
 * PQL <----> Groovy runtime test
 */

    println '=================== START Groovy-PQL test ==========================================='

    database = "database/factdb/psa/psaFacts.ta"

    println '    Create PQL instance, and load facts database: ' + database

    pql = new com.sun.salsa.lang.runtime.PQL(database)


/*
    println '\n================== Test 1.1: Java syntax: print all packages in the database ========'

    for (e in pql.packages) {
        println('    package name = ' + e);
    }

    println '\n================== Test 1.2: Groovy  syntax: print all packages in the database ====='

    pql.packages.each() { println '    package name = ' + it }
*/
    println '\n================= Test 2: Execute PQL query: ======================================'

    query = 'SELECT c.parentPackage FROM classes c WHERE c.parentPackage.name LIKE "j.*"'

    println '     ' + query

    result = pql.executePQL(query)

    println '    +++++ Query result is a Groovy collection:  +++++'

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

/*
    println '\n================= Test 3: Expert usage: Evaulate QL expression ======================'
    println '       +++++ This test shows data passing between Groovy and PQL +++++'

    pql.x = 100; pql.y = 200;
    result = pql.executeQL("return x + y ")

    println '     +++++ PQL evaluation result: ' + result
    println '     ++++++ variable "result" can be directly used in Groovy +++++'
    println '               For example, result + 20 = ' + (result + 100)

    println '\n====================================== Finish ======================================='
*/
