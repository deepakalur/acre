package com.sun.salsa.pdm.component;

import java.util.*;
import com.sun.salsa.lang.runtime.lib.PDMComponentBase;
import com.sun.salsa.pdm.component.*;

/**
 * PDM Component test class
 * @author Yury Kamen
 */
public class PDMClassesWithAbsAndNonAbsMethods extends PDMComponentBase {
    String name        = 'Find classes with abstract and non-abstract methods'      // + this.class.name
    String description = 'Find all classes with abstract and non-abstract methods'  // + this.class.name
    String metadata    = null                                                       // + this.class.name

    Object execute(Map inputs) {
        return pql.executePQL("""
return SELECT c FROM classes c, c.methods m WHERE m.isAbstract = true AND m.isAbstract = false;
""");
    }
}
