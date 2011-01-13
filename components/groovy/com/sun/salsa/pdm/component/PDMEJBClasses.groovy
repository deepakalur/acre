package com.sun.salsa.pdm.component;

import java.util.*;
import com.sun.salsa.lang.runtime.lib.PDMComponentBase;
import com.sun.salsa.pdm.component.*;

/**
 * PDM Component test class
 * @author Yury Kamen
 */
public class PDMEJBClasses extends PDMComponentBase {
    String name        = 'Find EJB classes'      // + this.class.name
    String description = 'Find all EJB classes'  // + this.class.name
    String metadata    = null                    // + this.class.name
    ArrayList outputNames  = ["Beans", "Homes", "Interfaces"];

    Object execute(Map inputs) {
        return pql.executePQL("""
return
SELECT c FROM classes c
WHERE c.extendsClass.**.name IN
    (   "javax.ejb.EntityBean",
        "javax.ejb.SessionBean",
        "javax.ejb.EJBHome",
        "javax.ejb.EJBLocalHome",
        "javax.ejb.EJBLocalObject",
        "javax.ejb.EJBObject"
    )
""");
    }
}
