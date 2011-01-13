SELECT c FROM classes c WHERE c.name = "java.lang.String" ;
SELECT c FROM classes c WHERE c.name LIKE "java.lang..*" ;
SELECT c FROM classes c WHERE c.shortName LIKE "St.*" ;
SELECT c FROM classes c WHERE c.parentPackage.name LIKE "j.*" ;
SELECT c FROM classes c WHERE c.parentPackage.shortName LIKE "j.*" ;
SELECT c FROM classes c WHERE c.parentPackage.classes.shortName LIKE "St.*" ;


