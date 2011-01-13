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
 * Free variables extractor. Traverses the AST and collects all the
 * references to free variables. 
 *
 *@author Mariusz Nowostawski
 *@version @version@ $Revision: 1.1.1.1 $
 */
class FreeVariableWalker extends DepthFirstAdapter {

  /** 
   * String variable names without leading $ indexed by appropriate
   * AST nodes.*/
  private Hashtable variables = new Hashtable();

  /** Returns the variable hash. */
  public Hashtable getVariables(){
    return this.variables;
  }

  

  public void outALongParamExpr(ALongParamExpr node){
    this.variables.put(node, node.getLongLiteral().getText().trim());
  }
  
  public void outANamedParamExpr(ANamedParamExpr node){
    this.variables.put(node, node.getIdentifier().getText().trim());
  }
  
}
