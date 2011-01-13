package com.sun.salsa.pdm.component;

import java.util.*;
import com.sun.salsa.lang.runtime.PQL;
import com.sun.salsa.lang.runtime.lib.PDMComponentBase;
import com.sun.salsa.pdm.component.*;

/**
 * PDM Component test class
 * @author Yury Kamen
 */
public class TestEmbedded extends PDMComponentBase {
    String name        = 'This is my name:        '  + this.class.name
    String description = 'This is my description: '  + this.class.name
    String metadata    = 'This is my metadata:    '  + this.class.name

    public TestEmdedded() {}
    // TestEmbedded(pql) { super(pql) } // Groovy bug - if defined, hides default constructor

    readDatabase() {
        database = "database/factdb/psa/psaFacts.ta"
        println '    Create PQL instance, and load facts database: ' + database
        this.pql = new com.sun.salsa.lang.runtime.PQL(database)
    }

    Object execute(Map inputs) {
        println "##### Start  executing : "  + this.class.name
        println "      inputs=${inputs}"

        readDatabase()

        output = new SimplePDM(pql).execute(['in1':'aaa', 'in2':'bbb', 'in3': 'ccc'])
        println '    Output of SimplePDM x : ' + output

        println "##### Finish executing : "  + this.class.name

        return output['result']
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
