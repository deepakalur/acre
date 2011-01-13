package com.sun.salsa.pdm.component;

import java.util.*;
import com.sun.salsa.lang.runtime.lib.PDMComponentBase;
import com.sun.salsa.pdm.component.*;

/**
 * PDM Component test class
 * @author Yury Kamen
 */
public class PDMMethodsUsingPublicFields extends PDMComponentBase {
    String name        = 'Find methods using public fields'      // + this.class.name
    String description = 'Find all methods using public fields'  // + this.class.name
    String metadata    = null                                    // + this.class.name
    ArrayList outputNames  = ["Methods"];

    Object execute(Map inputs) {
        return pql.executePQL("""

DEFINE methodsUsingPublicFields AS
    SELECT m as method, m.usedFields as field FROM methods m
    WHERE m.usedFields IN (SELECT f
      FROM fields f WHERE f.isFinal=false
      AND f.accessibility="public"
      AND f.parentClass.accessibility ="public"
    );

return
SELECT mupf FROM methodsUsingPublicFields mupf
WHERE mupf.method.parentClass.parentPackage != mupf.field.parentClass.parentPackage;

""");
    }
}
