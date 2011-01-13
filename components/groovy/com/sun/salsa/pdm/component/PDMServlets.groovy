package com.sun.salsa.pdm.component;

import java.util.*;
import com.sun.salsa.lang.runtime.lib.PDMComponentBase;
import com.sun.salsa.pdm.component.*;

/**
 * PDM Component test class
 * @author Yury Kamen
 */
public class PDMServlets extends PDMComponentBase {
    String name        = 'Find Java Servlets'      // + this.class.name
    String description = 'Find all Java Servlets'  // + this.class.name
    String metadata    = null                      // + this.class.name
    ArrayList outputNames  = ["Servlets"];

    Object execute(Map inputs) {
        return pql.executePQL("""

return
SELECT c FROM classes c
WHERE c.extendsClass.**.name IN
      ("javax.servlet.http.HttpServlet", "javax.servlet.http.GenericServlet")

""");
    }
}
