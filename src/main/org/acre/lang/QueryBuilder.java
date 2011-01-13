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
 * Represents an utility class via which the node substitution can be
 * easily made.
 * 
 *<br><br>
 * QueryBuilder.java<br>
 * Created: Fri Feb 25 12:30:12 2000<br>
 *
 * @author Mariusz Nowostawski
 * @version @version@ $Revision: 1.1.1.1 $
 */
public class QueryBuilder extends DepthFirstAdapter {

  Node ast;
  Hashtable context;

  public QueryBuilder() {
    this.context = new Hashtable();
  }

  public void setContext(Hashtable context){
    this.context = context;
  }

  public void setNode(Node ast){
    this.ast = ast;
  }
  
  /**
   * Returns the string representation of the query. */
  public String getQueryString(){
    final Oql2AsciiWalker w = new Oql2AsciiWalker();
    final OqlOutputStrategyString s = new OqlOutputStrategyString();
    w.setStategy(s);
    w.setContext(this.context);
    ast.apply(w);
    return s.toAscii().toString();
  }
  
} // QueryAnalyser
//////////////////// end of file ////////////////////
