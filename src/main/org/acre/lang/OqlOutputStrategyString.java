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

import org.acre.lang.analysis.DepthFirstAdapter;
import org.acre.lang.node.*;

import java.util.Hashtable;

/**
 * String based Pretty Printing strategy.
 *
 *@author Mariusz Nowostawski
 *@version @version@ $Revision: 1.1.1.1 $
 */
class OqlOutputStrategyString extends OqlOutputStrategy {

  //int indent = 0;
  StringBuffer result = new StringBuffer();
  boolean addNewLine = false;

  public void setPrefixNewLine(boolean newLine){
    this.addNewLine = newLine;
  }

  /**
   * Converts given Node and its tree structure into ascii. 
   *@param Node to be converted into ascii
   *@return ascii representation of the node and all its children
   */
  public StringBuffer toAscii(){
    return result;
  }

  /**
   * Produce empty string which length is len. */
  String indent(int len){
    String s = new String ("");
    for(int i=0; i < len; i++) s += " "; 
    return s;
  }

  public void doSubst(ANamedParamExpr node, Hashtable context) {
    final String key = node.getIdentifier().getText().trim();
    final String value = (String)context.get(key);
     if(value != null) {
      this.result.append(" ").append(value.toString()).append(" ");
    }
    else
      this.result.append("$").append(node.getIdentifier().getText()).append(" ");
  }


  ///// Utilities

  public void withoutSpace(Token node){
    if(needSpace)
      this.result.append(" ").append(node.getText().trim());
    else
      this.result.append(node.getText());
    needSpace = true;
  }

  public void withSpace(Token node){
    if(needSpace)
      this.result.append(" ").append(node.getText().trim()+" ");
    else
      this.result.append(node.getText()+" ");
    needSpace = false;
  }

  public void withNewLine(Token node){
    nlnum++;
    if(nlnum>0) {
      this.result.append("\n");
      for(int i=0; indent.size() > 0 &&  i<((Integer)indent.elementAt(0)).intValue(); i++)
        this.result.append(" ");
      nlnum--;
    }
    if(null != node)
      this.result.append(node.getText().trim()+" ");
    needSpace = false;
  }

}


  
  
