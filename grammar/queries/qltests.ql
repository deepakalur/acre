SELECT c.name FROM classes c WHERE c instanceof "java.util.Collection";
select c.accessibility, c.name from (select x from classes x where x.parentPackage.shortName = "util") c where c.shortName LIKE "D.*";

