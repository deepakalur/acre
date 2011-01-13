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

import org.acre.lang.node.Node;
import org.acre.lang.node.PExpr;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Vector;


/**
 * Represents the logical structure of the query. This is to help
 * developers to analyse AST without digging in the AST too much.
 *
 *<br><br>
 * QueryAnalyser.java<br>
 * Created: Fri Feb 25 12:30:12 2000<br>
 *
 * @author Mariusz Nowostawski
 * @version @version@ $Revision: 1.1.1.1 $
 */
public class QueryAnalyser {

  /** Set of all free variables names. */
  private HashSet freeVariables;
  private Hashtable projections;
  private Vector projNames;

  public QueryAnalyser() {
    this.projections = new Hashtable();
    this.freeVariables = new HashSet();
  }

  public void analyse(Node node){
    projectionAnalysis(node);
    variableAnalysis(node);
  }

  private void projectionAnalysis(Node node) {
    ProjectionWalker out = new ProjectionWalker();
    ProjectionNamesWalker ins = new ProjectionNamesWalker();
    node.apply(out);
    final Hashtable proj = out.getAllProjections();
    Enumeration enumerator = proj.elements();
    while(enumerator.hasMoreElements()){
      ((PExpr) enumerator.nextElement()).apply(ins);
    }
    this.projections = ins.getList();
    this.projNames = ins.getNames();
  }

  private void variableAnalysis(Node node) {
    final FreeVariableWalker w = new FreeVariableWalker();
    node.apply(w);
    final Hashtable res = w.getVariables();
    final HashSet set = new HashSet();
    final Enumeration enumerator = res.elements();
    while(enumerator.hasMoreElements()){
      set.add((String) enumerator.nextElement());
    }
    this.freeVariables = set;
  }

  /** Returns Projection nodes with names as elemnts. */
  public Hashtable getProjections(){
    return this.projections;
  }

  /** Returns projection variable names. */
  public String[] getProjectionNames(){
    final String[] result = new String[this.projNames.size()];
    for(int i=0; i < projNames.size(); i++){
      result[i] = (String)projNames.elementAt(i);
    }
    return result;
  }

  public String[] getVariableList(){
    final Object[] ar = freeVariables.toArray();
    final String[] result = new String[ar.length];
    for(int i=0; i<ar.length; i++) {
      result[i] = (String) ar[i];
    }
    return result;
  }

} // QueryAnalyser
//////////////////// end of file ////////////////////
