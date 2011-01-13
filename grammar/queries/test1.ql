
//Deepak's precanned queries;
//...................... classes with abstract and non-abstract methods;
SELECT c FROM classes c, c.methods m WHERE m.isAbstract = true AND m.isAbstract = false;
// for RDB 
SELECT c FROM classes c where c IN (select m.parentClass from methods m where m.isAbstract = true) AND c IN (select m.parentClass from methods m where m.isAbstract = false);

// ..................... Find all servlets;
SELECT c FROM classes c WHERE c.extendsClass.**.name IN ("javax.servlet.http.HttpServlet", "javax.servlet.http.GenericServlet");

//...................... Find all EJB classes;
SELECT c FROM classes c WHERE c.extendsClass.**.name IN ("javax.ejb.EntityBean","javax.ejb.SessionBean","javax.ejb.EJBHome","javax.ejb.EJBLocalHome","javax.ejb.EJBLocalObject","javax.ejb.EJBObject");

// ...................... Find all Singleton. Implemented with variables and IN
define publicConstructorClasses AS select m.parentClass from methods m where m.accessibility = "public" AND m.isConstructor = true;
define privateConstructorClasses AS select m.parentClass from methods m where m.accessibility = "private" AND m.isConstructor = true;
SELECT m.parentClass FROM methods m
WHERE m.returnType = m.parentClass
and m.accessibility = "public"
and m.isStatic = true
and m.parentClass IN privateConstructorClasses
AND m.parentClass NOT IN publicConstructorClasses;

// ...................... Find all Singleton. Implemented with nested queries
SELECT m.parentClass FROM methods m
WHERE m.returnType = m.parentClass
and m.accessibility = "public"
and m.isStatic = true
and m.parentClass IN (select m.parentClass from methods m where m.accessibility = "private" AND m.isConstructor = true)
AND m.parentClass NOT IN (select m.parentClass from methods m where m.accessibility = "public" AND m.isConstructor = true);


// ########################################### Tests ###################################################
// Parser & generator both work;
SELECT c FROM classes c WHERE c.parentPackage.name LIKE "j.*";
SELECT c FROM classes c WHERE c.parentPackage.shortName LIKE "j.*";
SELECT c FROM classes c WHERE c.methods.shortName LIKE "get.*";
SELECT c FROM classes c WHERE c.fields.shortName LIKE "is.*";
SELECT c FROM classes c WHERE c.parentPackage.classes.shortName LIKE "St.*";
SELECT c FROM classes c WHERE c.parentPackage.classes.fields.shortName LIKE "is.*";
SELECT c FROM classes c WHERE c.name = "java.lang.String";
SELECT classes.fields FROM classes WHERE classes.accessibility = "private";
SELECT classes.fields FROM classes c WHERE classes.parentPackage.shortName = "lang";
SELECT c.name FROM classes c WHERE c.parentPackage.shortName LIKE "t.*";
SELECT c.name FROM classes c WHERE c.parentPackage.shortName LIKE "uti.*" OR c.shortName LIKE "A.*";
SELECT c.name FROM classes c WHERE c.parentPackage.shortName LIKE "uti.*" AND c.shortName LIKE "A.*";
SELECT c.name FROM classes c WHERE c.accessibility = "public" AND c.parentPackage.shortName LIKE "lang.*";
SELECT c.name FROM classes c WHERE (c.accessibility = "public" AND c.parentPackage.shortName LIKE "uti.*") OR (c.shortName LIKE "A.*") OR (c.shortName LIKE "C.*" AND c.shortName LIKE ".*n" );
SELECT c.parentPackage.shortName FROM classes c WHERE c.parentPackage.name LIKE "j.*";

//  WITH expressions on RHS;
SELECT m FROM methods m WHERE m.returnType = m.parentClass AND m.parentClass.parentPackage.shortName = "lang";
SELECT m FROM methods m WHERE m.returnType.parentPackage = m.parentClass.parentPackage;
SELECT f FROM fields f WHERE f.type = f.parentClass;
SELECT m FROM fields m WHERE m.type.parentPackage = m.parentClass.parentPackage;
SELECT m.shortName FROM methods m WHERE m.parameters.type = m.parentClass;
SELECT shortName FROM methods m WHERE parentClass.accessibility = "private" ;

SELECT c FROM classes c WHERE c.name != "java.lang.Exception" and c.parentPackage.name = "java.lang";
SELECT c FROM classes c WHERE c.name > "java.lang.S" and c.parentPackage.name = "java.lang";
SELECT c FROM classes c WHERE c.name >= "java.lang.S" and c.parentPackage.name = "java.lang";
SELECT c FROM classes c WHERE c.name < "java.lang.D" and c.parentPackage.name = "java.lang";
SELECT c FROM classes c WHERE c.name <= "java.lang.D" and c.parentPackage.name = "java.lang";
SELECT c FROM classes c WHERE c.shortName != "Exception" and c.parentPackage.name = "java.lang";
SELECT c FROM classes c WHERE c.shortName > "S" and c.parentPackage.name = "java.lang";
SELECT c FROM classes c WHERE c.shortName >= "S" and c.parentPackage.name = "java.lang";
SELECT c FROM classes c WHERE c.shortName < "D" and c.parentPackage.name = "java.lang";
SELECT c FROM classes c WHERE c.shortName <= "D" and c.parentPackage.name = "java.lang";


SELECT c FROM classes c WHERE c.parentPackage.shortName = "java";
SELECT c FROM classes c, c.parentPackage p WHERE c.parentPackage.shortName = "java" OR p.parentPackage.**.shortName = "java" ;
SELECT c.extendsClass.**.name FROM classes c WHERE c.shortName = "Stack";



SELECT c.name FROM classes c WHERE c.name IN "java.util.Collection"  ;
SELECT c.name FROM classes c WHERE c.name IN ("java.util.Collection")  ;
SELECT c.name FROM classes c WHERE c.name IN ("java.util.Collection", "java.util.List");
SELECT c FROM classes c WHERE c.name NOT IN "java.lang.Exception" and c.parentPackage.name = "java.lang";
SELECT c FROM classes c WHERE c.name NOT IN ("java.lang.Exception") and c.parentPackage.name = "java.lang";
SELECT c FROM classes c WHERE c.name NOT IN ("java.lang.Exception","java.lang.Object") and c.parentPackage.name = "java.lang";

//SELECT count(c.name) FROM classes c WHERE c instanceof "java.util.Collection"  ;

// ##########################################################################;
// Following queries need work and testing. Possible after we have ;
//  joins for variables working;
// ##########################################################################;
SELECT c.name FROM classes c WHERE c.methods = c.methods.calls;
SELECT c.name FROM classes c WHERE c.methods IN c.methods.calls;
//SELECT c.name FROM classes c WHERE c.parentPackage.name IN c.methods.shortName;
//SELECT c.name FROM classes c WHERE c.parentPackage.name IN (c.methods.shortName);

SELECT c.name FROM classes c WHERE c.methods != c.methods.calls;
SELECT c.name FROM classes c WHERE c.methods NOT IN c.methods.calls;
//SELECT c.name FROM classes c WHERE c.parentPackage.name NOT IN c.methods.shortName;
//SELECT c.name FROM classes c WHERE c.parentPackage.name NOT IN (c.methods.shortName);

SELECT c
FROM classes c
WHERE c.parentPackage.shortName = "util";

SELECT c.name
FROM classes c
WHERE c.parentPackage.shortName = "util";

SELECT c, c.name
FROM classes c
WHERE c.parentPackage.shortName = "util";

SELECT c, c.name, c.parentPackage
FROM classes c
WHERE c.parentPackage.shortName = "util";

SELECT c.shortName, c.parentPackage, c.accessibility, c, c.methods.shortName
FROM classes c
WHERE c.name = "com.sun.sjc.psa.delegate.SearchDelegate";

SELECT c.parentPackage, c.accessibility, c.shortName, c, c.methods, c.methods.shortName FROM classes c
WHERE c.parentPackage.name LIKE "j.*";
SELECT c.parentPackage, c.accessibility, c.shortName, c, c.methods, c.methods.shortName FROM classes c
WHERE c.parentPackage.shortName LIKE "j.*";
SELECT c.parentPackage, c.accessibility, c.shortName, c, c.methods, c.methods.shortName FROM classes c
WHERE c.accessibility = "public" AND c.parentPackage.shortName LIKE "lang.*";
SELECT c.parentPackage, c.accessibility, c.shortName, c, c.methods, c.methods.shortName FROM classes c
WHERE (c.accessibility = "public" AND c.parentPackage.shortName LIKE "uti.*") OR (c.shortName LIKE "A.*") OR (c.shortName LIKE "C.*" AND c.shortName LIKE ".*n" );
SELECT c.parentPackage, c.accessibility, c.shortName, c, c.methods, c.methods.shortName FROM classes c
WHERE c.parentPackage.name LIKE "j.*";
SELECT c.parentPackage, c.accessibility, c.shortName, c, c.methods, c.methods.shortName FROM classes c
WHERE c.name IN ("java.util.Collection", "java.util.List");
SELECT c.parentPackage, c.accessibility, c.shortName, c, c.methods, c.methods.shortName FROM classes c
WHERE c.name NOT IN ("java.lang.Exception","java.lang.Object") and c.parentPackage.name = "java.lang";

select c from classes c where c.accessibility = "protected";
select c.shortName, c.shortName from classes c where c.accessibility = "protected";
select c.containingClass.shortName from classes c where c.accessibility = "protected";
select c.containingClass from classes c where c.accessibility = "protected";
select c.containingClass.parentPackage from classes c where c.accessibility = "protected";
select c, c.containingClass, c.containingClass.parentPackage from classes c where c.accessibility = "protected";
select c.shortName, c.containingClass.shortName, c.containingClass.parentPackage from classes c where c.accessibility = "protected";
select c.containingClass.parentPackage, c.containingClass, c from classes c where c.accessibility = "protected";

define x as SELECT c FROM classes c WHERE c.parentPackage.name LIKE "j.*";
define ooo as SELECT c FROM classes c WHERE c.parentPackage.name LIKE "o.*";
define ccc as SELECT c FROM classes c WHERE c.parentPackage.name LIKE "c.*";

define ocococ as ooo + ccc;

select c from classes c
WHERE c IN (select c from classes c where c.shortName = "Object")
   OR c IN (select c from classes c
            where c IN (
                select c from classes c
                where c.shortName IN ("String","StringBuffer")
            )
   );

define utils as select c from classes c where c.parentPackage.shortName LIKE ".*util.*";
select c.parentPackage.shortName from classes c where c.methods.calls IN utils.methods AND c NOT IN utils;


//select c.methods.shortName, c.methods.isConstructor  from classes c where c.name = "java.lang.String";
//select c.accessibility, "class",c.name from (select x from classes x where x.parentPackage.shortName = "util") c where c.shortName LIKE "D.*"


define ClassWithGetMethods as select c, m from  classes c, c.methods m where c.shortName LIKE "A%" and m.shortName LIKE "get%";
define AgainClassWithGetMethods as select a from ClassWithGetMethods a;


/* define FilteredClassWithGetMethods as select a from a WHERE a.1 = ??? ...;
*/

// Nil tests
SELECT c FROM classes c WHERE c.name = nil;
SELECT c FROM classes c WHERE c.name != nil;
SELECT c FROM classes c WHERE c.parentPackage != nil;
SELECT c FROM classes c WHERE c.parentPackage = nil;

//order by tests
SELECT c FROM classes c WHERE c.shortName LIKE "A%" ORDER BY c.shortName;
SELECT c FROM classes c WHERE c.shortName LIKE "A%" ORDER BY c.shortName ASC;
SELECT c FROM classes c WHERE c.shortName LIKE "A%" ORDER BY c.shortName DESC;
SELECT c FROM classes c WHERE c.shortName LIKE "A%" ORDER BY shortName;
SELECT c FROM classes c WHERE c.shortName LIKE "A%" ORDER BY shortName ASC;
SELECT c FROM classes c WHERE c.shortName LIKE "A%" ORDER BY shortName DESC;
SELECT c FROM classes c WHERE c.shortName LIKE "A%" ORDER BY c.parentPackage.shortName;
SELECT c FROM classes c WHERE c.shortName LIKE "A%" ORDER BY c.parentPackage.shortName ASC;
SELECT c FROM classes c WHERE c.shortName LIKE "A%" ORDER BY c.parentPackage.shortName DESC;

//select alias test
SELECT c, c.shortName AS sn FROM classes c WHERE sn LIKE "A%";
SELECT c, c.shortName AS sn FROM classes c ORDER BY sn DESC;
SELECT c, c.shortName AS n FROM classes c WHERE n LIKE "A%" ORDER BY n ASC;
SELECT c, c.shortName AS n FROM classes c WHERE n LIKE "A%" ORDER BY n DESC;

//aggregate functions
SELECT COUNT(m.numLinearSegments) FROM JMethod m;
SELECT MAX(m.numLinearSegments) FROM JMethod m;
SELECT MIN(m.numLinearSegments) FROM JMethod m;
SELECT AVG(m.numLinearSegments) FROM JMethod m;
SELECT SUM(m.numLinearSegments) FROM JMethod m;

SELECT COUNT(m.shortName) FROM JMethod m;
SELECT MAX(m.shortName) FROM JMethod m;
SELECT MIN(m.shortName) FROM JMethod m;
SELECT AVG(m.shortName) FROM JMethod m;
SELECT SUM(m.shortName) FROM JMethod m;

