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

import java.util.Hashtable;


/**
 * Utility class for OQL Abstract Syntax Tree back to well-formed
 * ASCII representation. 
 *
 *@author Mariusz Nowostawski
 *@version @version@ $Revision: 1.1.1.1 $
 */
public class ToAscii {

  public static String toString(final String oql) {
    try{
      Node ast = TreeBuilder.getNode(oql, true);
      return toString(ast);
    }catch(Exception ex){
      ex.printStackTrace();
      return null;
    }
  }
  
  public static String toString(final Node ast){
    final Oql2AsciiWalker walker = new Oql2AsciiWalker();
    final OqlOutputStrategyString s = new OqlOutputStrategyString();
    walker.setStategy(s);
    ast.apply(walker);
    StringBuffer buf = s.toAscii();
    return buf.toString();
  }

  public static String toString(final Node ast, final Hashtable context){
    final Oql2AsciiWalker walker = new Oql2AsciiWalker();
    final OqlOutputStrategyString s = new OqlOutputStrategyString();
    walker.setStategy(s);
    walker.setContext(context);
    ast.apply(walker);
    StringBuffer buf = s.toAscii();
    return buf.toString();
  }

}

