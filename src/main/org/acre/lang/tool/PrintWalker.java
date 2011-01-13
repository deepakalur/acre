/**
 *   Copyright 2004-2005 Sun Microsystems, Inc.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.acre.lang.tool;

import org.acre.lang.analysis.DepthFirstAdapter;
import org.acre.lang.node.*;

/**
 * Simple AST walker. This is simple Abstract Syntax Tree walker
 * which will visit each of the nodes and print on standard output the
 * name of particular node visited. This class can be very usefull while
 * testing the tree structure for a given input. Check PrintTree class.
 *@author Mariusz Nowostawski
 */
public class PrintWalker extends DepthFirstAdapter {

  int indent = 0;

  void indent(){
    String s = new String ("");
    for(int i=0; i < indent; i++) s += " ";
    System.out.print(s);
  }

  public void defaultIn(Node node) {
    indent();
    System.out.println(node.getClass().getName().substring("org.acre.lang.tool".length()+1));
    indent++;
  }

  public void defaultOut(Node node) {
    indent--;
  }

  public void defaultCase(Node node) {
    indent();
    System.out.println(node.getClass().getName().substring("org.acre.lang.tool".length()+1)+": " + ((Token) node).getText());
  }

}

