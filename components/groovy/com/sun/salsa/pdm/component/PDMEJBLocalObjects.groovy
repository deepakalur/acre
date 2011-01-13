package com.sun.salsa.pdm.component;

import java.util.*;
import com.sun.salsa.lang.runtime.lib.PDMComponentBase;
import com.sun.salsa.pdm.component.*;

/**
 * PDM Component test class
 * @author Yury Kamen
 */
public class PDMEJBLocalObjects extends PDMComponentBase {
    String name        = 'Find Local EJB Objects'     // + this.class.name
    String description = 'Find all Local EJB Objects' // + this.class.name
    String metadata    = null                         // + this.class.name
    ArrayList outputNames  = ["Local EJBs"];
    Object execute(Map inputs) {
        return pql.executePQL("""
return SELECT c FROM classes c WHERE c.extendsClass.**.name = "javax.ejb.EJBLocalObject"
""");
    }
}
