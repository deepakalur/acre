package com.sun.salsa.pdm.component;

import java.util.*;
import com.sun.salsa.lang.runtime.lib.PDMComponentBase;
import com.sun.salsa.pdm.component.*;

/**
 * PDM Component test class
 * @author Yury Kamen
 */
public class PDMEntityBeans extends PDMComponentBase {
    String name        = 'Find Entity Beans'      // + this.class.name
    String description = 'Find all Entity Beans'  // + this.class.name
    String metadata    = null                     // + this.class.name
    ArrayList outputNames  = ["Entity Beans"];

    Object execute(Map inputs) {
        return pql.executePQL("""
RETURN SELECT c FROM classes c WHERE c.extendsClass.**.name = "javax.ejb.EntityBean";
""");
    }
}
