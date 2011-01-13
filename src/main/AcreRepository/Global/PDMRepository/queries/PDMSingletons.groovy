package com.sun.salsa.pdm.component;

import java.util.*;
import com.sun.salsa.lang.runtime.lib.PDMComponentBase;
import com.sun.salsa.pdm.component.*;

/**
 * PDM Component test class
 * @author Yury Kamen
 * @date v
 */
public class PDMSingletons extends PDMComponentBase {
    String name        = 'Find Singleton classes'       // + this.class.name
    String description = 'Find all Singleton classes'   // + this.class.name
    String metadata    = null                           // + this.class.name
        ArrayList outputNames  = ["Singletons"];

    Object execute(Map inputs) {

    	String packageName = null;

	if ( inputs != null ) 
		packageName = inputs["package"];
	
	String query = """
return
SELECT m.parentClass FROM methods m
WHERE m.returnType = m.parentClass
and m.accessibility = "public"
and m.isStatic = true
and m.parentClass IN (select m.parentClass from methods m where m.accessibility = "private" AND m.isConstructor = true)
AND m.parentClass NOT IN (select m.parentClass from methods m where m.accessibility = "public" AND m.isConstructor = true) 
""";

	String packageScope = null;
	if ( inputs != null )
		packageScope = " and m.parentClass.parentPackage.name = \"" + packageName + "\";"
	else
	     packageScope = " ; "

	println query + packageScope;
        result = pql.executePQL(query + packageScope);
        return result
    }
}
