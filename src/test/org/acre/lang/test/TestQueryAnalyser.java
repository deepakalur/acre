// This file is copyrighted and is part of nzdis-test package.
// See the file LICENSE for copyright information and the terms and conditions for copying, distributing and modifications of nzdis-test package.
// @copyright@

package org.acre.lang.test;

/**/
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;

import org.acre.lang.node.Node;
import org.acre.lang.QueryAnalyser;
import org.acre.lang.TreeBuilder;


/**
 * Test utility for Query Analyser.
 * 
 * @author Mariusz Nowostawski
 * @version @version@ $Revision: 1.1.1.1 $
 */
public class TestQueryAnalyser extends TestCase {
    
  /**/
  public TestQueryAnalyser(String name) {
    super(name);
  }
    
  /** Setup. */
  protected void setUp(){
  }

  static void assertBoolean(boolean val) {
      if(!val){
          throw new AssertionError("Assertion Failed");
      }
  }
  public void testSimple(){
    Node node = null;
    try{
      node = TreeBuilder.getNode("select p from Mama where p.name = $name", true);
    }catch(Exception e){ fail(e.getMessage()); }
    QueryAnalyser a = new QueryAnalyser();
    a.analyse(node);
    String[] list = a.getVariableList();
    assertBoolean(list.length == 1);
    assertEquals(list[0], "name");
  }
  
  public void testSimpleWithRepetition(){
    Node node = null;
    try{
      node = TreeBuilder.getNode("select p from Mama where p.name = $name and p.surname != $name", true);
    }catch(Exception e){ fail(e.getMessage()); }
    QueryAnalyser a = new QueryAnalyser();
    a.analyse(node);
    String[] list = a.getVariableList();
    assertBoolean(list.length == 1);
    assertEquals(list[0], "name");
  }

  public void testComplex(){
    Node node = null;
    try{
      node = TreeBuilder.getNode("select p from Mama where p.name = $name and p.surname = $surname", true);
    }catch(Exception e){ fail(e.getMessage()); }
    QueryAnalyser a = new QueryAnalyser();
    a.analyse(node);
    String[] list = a.getVariableList();
    assertBoolean(list.length == 2);
    assertBoolean(
           (list[0].equals("name") && list[1].equals("surname")) || 
           (list[1].equals("name") && list[0].equals("surname")));
  }
  
  public void testComplexWithRepetition(){
    Node node = null;
    try{
      node = TreeBuilder.getNode(
                                 "select p from Mama where p.name = $name and p.surname = $surname and $name < $surname",
                                 true);
    }catch(Exception e){ fail(e.getMessage()); }
    QueryAnalyser a = new QueryAnalyser();
    a.analyse(node);
    String[] list = a.getVariableList();
    assertBoolean(list.length == 2);
    assertBoolean(
           (list[0].equals("name") && list[1].equals("surname")) || 
           (list[1].equals("name") && list[0].equals("surname")));
  }

  public void testStructProjection(){
    Node node = null;
    try{
      node = TreeBuilder.getNode(
                                 "select struct(name: p.name, surname: p.surname) from Mama p where p.age < 20 ",
                                 true);
    }catch(Exception e){ fail(e.getMessage()); }
    QueryAnalyser a = new QueryAnalyser();
    a.analyse(node);
    String[] list = a.getProjectionNames();
    assertBoolean(list.length == 2);
    assertBoolean(
           (list[0].equals("name") && list[1].equals("surname")) || 
           (list[1].equals("name") && list[0].equals("surname")));
  }

  /**
   * Test suite for OQL grammar. */ 
  public static Test suite() { 
    return new TestSuite(TestQueryAnalyser.class); 
  }

} // TestQueryAnalyser
//////////////////// end of file ////////////////////
