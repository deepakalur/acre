package com.sun.salsa.pdm.test;

import java.util.*;
import com.sun.salsa.lang.runtime.PQL;
import com.sun.salsa.lang.runtime.lib.PDMComponentBase;
import com.sun.salsa.pdm.component.*;

/**
 * PDM Component test class
 * @author Yury Kamen
 */
public class TestPDMLibrary extends PDMComponentBase {
    String name        = 'PDM component library test'                      //  + this.class.name
    String description = 'This module tests other PDM library components'  //  + this.class.name
//    String metadata    = 'This is my metadata:    '  + this.class.name

    public TestPDMLibrary() {}
    // TestPDMLibrary(pql) { super(pql) } // Groovy bug - if defined, hides default constructor

    readDatabase() {
        database = "database/factdb/psa/psaFacts.ta"
        println '    Create PQL instance, and load facts database: ' + database
        this.pql = new com.sun.salsa.lang.runtime.PQL(database)
    }

    getComponentList() {
        return [    null
                    , ['component': new PDMEJBClasses(),                     'inputs': null ]
                    , ['component': new PDMEJBLocalHomes(), 'inputs': null ]
                    , ['component': new PDMEJBHomes(), 'inputs': null ]
                    , ['component': new PDMEJBLocalObjects(), 'inputs': null ]
                    , ['component': new PDMEJBRemoteObjects(), 'inputs': null ]
                   , ['component': new PDMSessionBeans(), 'inputs': null ]
                    , ['component': new PDMEntityBeans(), 'inputs': null ]
                    , ['component': new PDMServlets(),                       'inputs': ['in1':'aaa', 'in2':'bbb', 'in3': 'ccc'] ]
                    , ['component': new PDMSingletons(),                     'inputs': null ]
                    , ['component': new PDMClassesWithAbsAndNonAbsMethods(), 'inputs': null ]
/*
                    , ['component': new PDMClassesWithAbsAndNonAbsMethods(), 'inputs': null ]
                    , ['component': new PDMClassesWithAbsAndNonAbsMethods(), 'inputs': null ]
                    , ['component': new PDMClassesWithAbsAndNonAbsMethods(), 'inputs': null ]
                    , ['component': new PDMClassesWithAbsAndNonAbsMethods(), 'inputs': null ]
                    , ['component': new PDMClassesWithAbsAndNonAbsMethods(), 'inputs': null ]
                    , ['component': new PDMClassesWithAbsAndNonAbsMethods(), 'inputs': null ]
                    , ['component': new PDMClassesWithAbsAndNonAbsMethods(), 'inputs': null ]
                    , ['component': new PDMClassesWithAbsAndNonAbsMethods(), 'inputs': null ]
                    , ['component': new PDMClassesWithAbsAndNonAbsMethods(), 'inputs': null ]
                    , ['component': new PDMClassesWithAbsAndNonAbsMethods(), 'inputs': null ]
*/


////////////////// Does not work yet: ////////////////////////////////////////////////////////////////
                    // , ['component': new PDMMethodsUsingPublicFields(), 'inputs': null ]
               ]
    }

    Object execute(Map inputmap) {
        println """
###############################################################################
                   Start ${name}
                   Inputs=${inputmap}
###############################################################################
"""
        componentList = getComponentList()
        readDatabase()

        for (elt in componentList) {
            if (null == elt) {
                continue;
            }
            component = elt['component']
            component.pql = this.pql
            inputs    = elt['inputs']
            if(null == inputs) {
                inputs = inputmap
            }
            println """
==============================================================================
                   Started testing PDM component: ${component.name}
                   Description: ${component.description}
                   Metadata:    ${component.metadata}
                   Inputs:      ${inputs}
                   Input names: ${component.inputNames   }
                   Output names: ${component.outputNames }
==============================================================================
"""

            outputs = component.execute(inputs)

            println """
.............................. Outputs .......................................
${outputs}
==============================================================================
                   Finished testing PDM component: ${component.name}
==============================================================================
"""

            //break;
        }

        println """
###############################################################################
                   Finish ${name}
###############################################################################
"""
        return null
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
