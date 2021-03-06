// This file is copyrighted and is part of nzdis-test package.
// See the file LICENSE for copyright information and the terms and conditions for copying, distributing and modifications of nzdis-test package.
// @copyright@

package org.acre.lang.test;

/**/
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;
import java.util.Vector;
import java.util.Enumeration;

import org.acre.lang.TreeBuilder;

/**
 * Test utility for ODMG OQL3 grammar.
 *
 * @author Mariusz Nowostawski
 * @version @version@ $Revision: 1.1.1.1 $
 */
public class TestGrammar extends TestCase {

  /**/
  public TestGrammar(String name) {
    super(name);
  }

  /** Setup. */
  protected void setUp(){
  }

  public void testParsingAllQueries(){
    final Vector v = new Vector();
    for(int i=0; i<testQueries.length; i++) {
      String query = testQueries[i];
      try{
        TreeBuilder.getNode(query, false);
      }catch(Exception ex){
        v.addElement(query);
      }
    }
    int i = 1;
    if(v.size()>0){
      System.out.println("\n\n *** Parser failed for following queries:");
      Enumeration enumerator = v.elements();
      while(enumerator.hasMoreElements())
        System.out.println(i++ + ". " + enumerator.nextElement().toString());
    }
    if(i>1)
      System.out.println("\n Parser: "+testQueries.length+" queries tested.");
    else
      System.out.println("\n Parser: "+testQueries.length+" queries tested - full success.  All OK.");
  }


  public void testSimplifyingAllQueries(){
    final Vector v = new Vector();
    for(int i=0; i<testQueries.length; i++) {
      String query = testQueries[i];
      System.out.println(query);
      try{
        TreeBuilder.getNode(query, true);
      }catch(Exception ex){
        ex.printStackTrace();
        v.addElement(query);
      }
    }
    if(v.size()>0){
      System.out.println("\n\n *** AST simplification failed for following queries:");
      int i = 1;
      Enumeration enumerator = v.elements();
      while(enumerator.hasMoreElements())
        System.out.println(i++ + ". " + enumerator.nextElement().toString());
    }
    if(v.size() == 0)
      System.out.println("\n Simplifier: "+testQueries.length+" queries tested - full success.  All OK. ");
    else
      System.out.println("\n Simplifier: "+testQueries.length+" queries tested.");
  }

  /**
   * Test suite for OQL grammar. */
  public static Test suite() {
    return new TestSuite(TestGrammar.class);
  }

  /*
    NOT CORRECT OQL QUERIES ARE:
       select distinct(smth)
        (Persons)
  */

  /**
   * Valid OQL queries for testing.
   */
  String[] testQueries = new String[] {

    "Persons",

    "method()",
    "method(sd,12)",

    "p.methodcall()",
    "p.methodcall(12,43)",
    "p.methodcall(12, b)",
    "p.methodcall(12, b.o)",

    "a.p.methodcall()",
    "a.p.methodcall(12,43)",
    "a.p.methodcall(12, b)",
    "a.p.methodcall(12, b.o)",

    "select p from Persons p",

    "Student except TA",

    "select ((Employee) s).salary from Student s",

    "select p from p in Persons",
    "select p from Persons p",
    "select p from Persons as p",

    "define jones as select distinct x from Students x where x.name = \"Jones\";"+
    "select distinct student_id from Students",
    "select p from distinct(select distinct p from Students) as p",
    "select p from Students d where p = distinct(d)",

    "define Does as select x from Student x where x.name = \"Doe\";" +
    "select distinct student_id from Does",

    "define Doe as element(select x from Student x where x.name = \"Doe\");"+
    "select distinct student_id from Doe",

    "Employee (name: Name, boss: Chairman)",
    "Employee (name: \"Peter\", boss: Chairman)",
    "BigNumber (real: 2.243, realreal: -423.123e-34)",
    "Vector(data: set(1,3,10))",

    "Person(name: \"Mariusz\")",
    "select * from Persons p where p = Person(name: \"Mariusz\", surname: \"Nowostawski\", age: 12)",

    "struct(name:\"Peter\", age:25)",
    "set(1,2,3)",
    "set(1,2,3)<set(3,4,2,1)",
    "list(1,2,3,4)",
    "list(a,b,c,d)[1]",
    "list(a,b,c,d)[1:3]",
    "list(1 .. 2)",

    "listtoset(list(1,2,3,2))",
    "flatten(list(1,2,3,2))",
    "distinct(select p from Person p)",
    "element(Persons)",

    "bag(1,1,2,3,3)",

    "array(3,4,2,1,1)",

    "10 < first(list(8,15,7,22))",

    "10 < some(list(8,15,7,22))",
    "10 < some list(8,15,7,22) ",
    "10 < any array(8,15,7,22) ",
    "100 > all(bag(8,15,7,22))",

    "flatten(list(set(1,2,3), set(3,4,5,6), set(7)))",

    "not true",

    "count(Students) - count(TA)",

    "\"a nice string\" like \"%nice%str_ng\"",

    "Doe = element(select s from Students s where s.name=\"Doe\")",

    "Doe.name ",
    "Doe.name.toLowerCase ",
    "Doe.name.toLowerCase() ",
    "Doe.name.substring(123) ",
    "Doe.name.substring[1,3] ",

    "((octet) o)",
    "(octet) o",
    "((Mama) m)",
    "(Employee) student",

    "Doe->spouse != nil and Doe ->spouse->name = \"Carol\"",

    "jones -> number_of_students",

    "Mariusz in Persons",

    "Doe->apply_course(\"Math\", Turing)->number",

    "for all x in Students: x.student_id > 0",

    "exists x in Doe.takes: x.taught_by.name = \"Turing\"",

    "max(select salary from professors)",

    "select couple(student: x.name, professor: z.name)"+
    " from Students as x,"+
    "   x.takes as y,"+
    "   y.taught_by as z"+
    " where z.rank = \"full professor\"",

    "select * from Students as x",

    "select p from Persons p order by p.age, p.name",

    "select couples(student: Students.name, professor: z.name)"+
    " from Students,"+
    "  Students.takes y,"+
    "  y.taught_by z"+
    " where z.rank = \"full professor\"",

    "select * from Employees e "+
    " group by low: salary < 1000,"+
    " medium: salary >= 1000 and salary < 10000,"+
    " high: salary >= 10000",

    "select department,"+
    "  avg_salary: avg(select e.salary from partition)"+
    " from Employees e"+
    "  group by department:e.deptno"+
    "  having avg(select e.salary from partition) > 30000",

    "select * from Persons order by age desc, name asc, department",

    "a+ b",
    "1+2",
    "(1+2)",
    "(a+b)",
    "a+b*c",
    "(a+b)*c",
    "c*(a+b)",
    "(c*(a+b)*d)+w",
    "((c*(a+b)*d)+w)",
    "((a+b)*c)",

    "(c and (a or b) and d) or w",
    "((c and (a or b) and d) or w)",

    "(c = (a+b) < d) > w",
    "(c > (a < b) >= d) = w",
    "w = (c > (a < b) >= d)",
    "((c > (a < b) >= d) = w)",

    "p = (c*(a+b)*d)+w",
    "(c*(a+b)*d)+w = p",
    "((c*(a+b)*d)+w) = p",
    "(((c*(a+b)*d)+w) = p)",

    "select * from Persons p where p = a + b",
    "select * from Persons p where p = 4 + 2",
    "select * from Persons p where p = (a + b)*c",
    "select * from Persons p where p = (4 + 2)*21",
    "select * from Persons p where p = (a or b) or c",
    "select * from Persons p where p = (a and b) or c",
    "select * from Persons p where p = (a or b) and c",
    "select * from Persons p where p = (4 and d) or 2",

    "element(select x"+
    "  from Courses x"+
    "  where x.name = \"Math\" and x.number = 101).requires[2]",

    "select s.year"+
    " from Student s"+
    " where s in (select sec.assistant from Sections sec)",

    "select ((Employee) s).salary"+
    " from Student s"+
    " where s in (select sec.assistant from Sections sec)",

    "select p from Persons p where p.address in"+
    " (select address from Addresses address where address.city in"+
    " (select city from Zones z, z.city city))",

    "select distinct p.name from (select d from House d where for all x in House: x.value > 1000) as p",

    "select scope1"+
    " from Persons,"+
    "     Cities c"+
    " where exists(select scope2 from children as child)"+
    "       or count(select scope3, (select scope4 from partition)"+
    "               from children p,"+
    "                     scope5 v"+
    "               group by age: scope6"+
    "              )"

  };


} // TestObservableStore
//////////////////// end of file ////////////////////
