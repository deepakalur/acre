package com.sun.salsa.pdm.component;

import java.util.*;
import com.sun.salsa.lang.runtime.lib.PDMComponentBase;
import com.sun.salsa.pdm.component.*;

/**
 * PDM Component test class
 * @author Yury Kamen
 */
public class SimplePDM extends PDMComponentBase {
    String name        = 'This is my name:        '  + this.class.name
    String description = 'This is my description: '  + this.class.name
    String metadata    = 'This is my metadata:    '  + this.class.name

    SimplePDM(){} // Groovy bug - hidden by constructor below
    SimplePDM(pql) { super(pql) }

    runQuery() {
        query = 'SELECT c.parentPackage FROM classes c WHERE c.parentPackage.name LIKE "j.*"'
        println '    +++++ runQuery(): query = ' + query
        result = pql.executePQL(query)
        print   '    +++++ result = ' + result
        println '    +++++ Query result is a Groovy collection:  +++++'
        for (e in result) {
            println '========== query element ============ ' + e
            println '            e.names  = ' + e.names
            e.names.each() {  println ' +++++ name  = ' + it }
            println '            e.values = '
            e.values.each() {  println ' +++++ value = ' + it }
            println '            e[0]     = ' + e[0]
                    //      // TODO: this does not work yet
                    //        for (name in e.names) {
                    //            value = e[name]
                    //            println "     name=${name}  value=${e[name]}"
                    //        }
                    // println '            e.res  = ' + e.res // TODO: this does not work yet
                    // TODO: this does not work yet
                    //for (x in e) {
                    //    println '    query element = ' + x
                    //}
        }
        return result
    }

    Object execute(Map inputs) {
        println "  @@@@@ Start  executing: "  + this.class.name
        println "        inputs=${inputs}"
        res = [ 'result':  runQuery(), 'out1':1, 'out2':2, 'out3':3]
        println "  @@@@@ Finish executing: "  + this.class.name
        return res
    }
}
