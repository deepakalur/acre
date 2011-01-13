package com.sun.salsa.pdm.component;

import java.util.*;
import com.sun.salsa.lang.runtime.lib.PDMComponentBase;
import com.sun.salsa.pdm.component.*;

/**
 * PDM Component test class
 * @author Yury Kamen
 */
public class PDMEJBRemoteObjects extends PDMComponentBase {
    String name        = 'Find Remote EJB Object classes'      // + this.class.name
    String description = 'Find all EJB Remote EJB Object classes'  // + this.class.name
    String metadata    = null                    // + this.class.name
    ArrayList outputNames  = ["Remote EJBs"];

    Object execute(Map inputs) {
        return pql.executePQL("""
return SELECT c FROM classes c WHERE c.extendsClass.**.name = "javax.ejb.EJBObject"
""");
    }
}
