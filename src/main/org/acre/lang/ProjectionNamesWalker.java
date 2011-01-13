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
import java.util.Vector;

/**
 * Projection names extractor. Traverses the AST and collects all the
 * references to names declared in projection nodes. 
 * works fine with identifiers and structs. Need to be extended for
 * different projections. Do not cope yet with star (*) projections.
 *
 *@author Mariusz Nowostawski
 *@version @version@ $Revision: 1.1.1.1 $
 */
class ProjectionNamesWalker extends DepthFirstAdapter {

  /** 
   * Projection names index by appropriate nodes. */
  private Hashtable list = new Hashtable();
  private Vector names = new Vector();

  private Node root = null;

  /** Returns the variable hash. */
  public Hashtable getList(){
    return this.list;
  }

  /** Returns variable names only. */
  public Vector getNames(){
    return this.names;
  }

  public void inAIdentifierExpr(AIdentifierExpr node){
    if(this.root == null)
      this.root = node;
  }
  
  public void outAIdentifierExpr(AIdentifierExpr node){
    if(root == node) {
      String name = node.getIdentifier().getText().trim();
      this.list.put(node, name);
      this.names.addElement(name);
    }
  }

  public void inANewStructExpr(ANewStructExpr node){
    if(this.root == null)
      this.root = node;
  }

  public void outANewStructExpr(ANewStructExpr node){
    if(root == node)
      node.apply(new FieldWalker());
  }
  


  /** Field walker for projections. */
  class FieldWalker extends DepthFirstAdapter {

    public void outAField(AField node){
      String name = node.getIdentifier().getText().trim();
      list.put(node.getExpr(), name);
      names.addElement(name);
    }

  }// FieldWalker


}// ProjectionNamesWalker
