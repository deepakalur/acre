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
package org.acre.lang;

import org.acre.lang.node.ANamedParamExpr;
import org.acre.lang.node.Token;

import java.util.Hashtable;
import java.util.Vector;

/**
 * Abstract strategy for OQL Pretty Printer.
 *
 *@author Mariusz Nowostawski
 *@version @version@ $Revision: 1.1.1.1 $
 */
abstract class OqlOutputStrategy {

  boolean needSpace = false;
  Vector indent = new Vector();
  int nlnum;

  public OqlOutputStrategy() {
    indent.addElement(new Integer(0));
    nlnum = 0;
  }
  
  public void pushIndent(int indentoffset) {
      if(indent.size() == 0) {
          indent.add(new Integer(0));
      }
    indent.insertElementAt(new Integer(((Integer)indent.elementAt(0)).intValue()+indentoffset),0);
  }

  public void popIndent() {
    indent.removeElementAt(0);
  }

  abstract public void doSubst(ANamedParamExpr node, Hashtable context);
  abstract public void withoutSpace(Token node);
  abstract public void withSpace(Token node);
  abstract public void withNewLine(Token node);

}
