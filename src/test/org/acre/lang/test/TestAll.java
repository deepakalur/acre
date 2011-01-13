// This file is copyrighted and is part of nzdis-test package.
// See the file LICENSE for copyright information and the terms and conditions for copying, distributing and modifications of nzdis-test package.
// @copyright@

package org.acre.lang.test;

/**/
import junit.framework.TestSuite;
import junit.framework.Test;
import junit.textui.TestRunner;

/**
 * Test utility for the whole OQL3 package. Run this test for
 * integrated test suite for all classes from the 
 * org.acre.lang.test package.
 * 
 *<br><br>
 * TestAll.java<br>
 * Created: Fri Oct 29 17:42:14 1999<br>
 *
 * @author Mariusz Nowostawski
 * @version @version@ $Revision: 1.1.1.1 $
 */
public class TestAll {
    
  /**
   * Test suite for otago.agent classes. */ 
  public static Test suite() { 
    TestSuite suite = new TestSuite(); 
    suite.addTest(TestGrammar.suite());
    suite.addTest(TestQueryAnalyser.suite());
    return suite;
  }

  /**
   * Execute all tests. */
  public static void main(String[] args){
    TestRunner.run(TestAll.suite());
  }

} // TestAll
//////////////////// end of file ////////////////////
