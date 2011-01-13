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
 * Projection extractor. Traverses the AST and collects all the
 * references to projection nodes. Should be fixed not to collect
 * inner queries projections.
 *
 *@author Mariusz Nowostawski
 *@version @version@ $Revision: 1.1.1.1 $
 */
class ProjectionWalker extends DepthFirstAdapter {

  /** 
   * String variable names without leading $ indexed by appropriate
   * AST nodes.*/
  private Hashtable list = new Hashtable();

  /** Returns the variable hash. */
  public Hashtable getAllProjections(){
    return this.list;
  }

  
  public void outAExprProjection(AExprProjection node){
    this.list.put(node, node.getExpr());
  }
  
}
