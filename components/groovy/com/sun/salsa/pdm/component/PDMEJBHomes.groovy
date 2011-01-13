package com.sun.salsa.pdm.component;

import java.util.*;
import com.sun.salsa.lang.runtime.lib.PDMComponentBase;
import com.sun.salsa.pdm.component.*;

/**
 * PDM Component test class
 * @author Yury Kamen
 */
public class PDMEJBHomes extends PDMComponentBase {
    String name        = 'Find EJB Homes'      // + this.class.name
    String description = 'Find all Homes'      // + this.class.name
    String metadata    = null                  // + this.class.name
    ArrayList outputNames  = ["Homes"];
    Object execute(Map inputs) {
        return pql.executePQL("""
return SELECT c FROM classes c WHERE c.extendsClass.**.name = "javax.ejb.EJBHome"
""");
    }
}
