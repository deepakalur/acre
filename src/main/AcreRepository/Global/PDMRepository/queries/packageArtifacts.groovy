package com.sun.salsa.pdm.component;

import java.util.*;
import com.sun.salsa.lang.runtime.lib.PDMComponentBase;
import com.sun.salsa.pdm.component.*;

/**
 * PDM Component test class
 * @author Yury Kamen
 * @date v
 */
public class packageArtifacts extends PDMComponentBase {
    String name        = 'packageArtifacts'       // + this.class.name
    String description = 'Find all public classes'   // + this.class.name
    String metadata    = null                           // + this.class.name
        ArrayList outputNames  = ["packageArtifacts"];

    Object execute(Map inputs) {

    	String packageName = null;
	String scope = null
	String returnVar = "pa";
	
	if ( inputs != null ) {
		packageName = inputs["rolename"];
		scope = inputs["scope"]
		if ( scope != null ) {
			returnVar += "_" + scope
		}
	}

	System.out.println("_____________________________________________");
	println inputs
	println packageName
String query = "
define " +returnVar + " as
select c 
from classes c ";

if ( packageName != null )
	query += " where c.parentPackage.name = \"" + packageName + "\";";
else
	query += " ; "

query += " return " + returnVar + " ; "
	println(query);
	System.out.println(query);
	System.out.println("_____________________________________________");
        result = pql.executePQL(query);
        return result
    }
}
