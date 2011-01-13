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
package org.acre.model.metamodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


/**
 *  The Java class MetaScope_Bean implements the Bean representation for 
 *  the following objects in the specification:
 *
 *     MetaScope
 *         persistent String name
 *         persistent String sourceFile
 *         persistent Integer filePosition
 *         List<MetaVariable> metaVariables
 *         List<MetaScope> containedMetaScopes
 *         MetaScope containingMetaScope *
 */


public class MetaScope_Bean  implements MetaScope {

  ///////////////////// Attribute Instance Variables /////////////////

  protected java.lang.String name;
  protected java.lang.String sourceFile;
  protected int filePosition;

  //////////////////// Relationship Instance Variables ////////////////

  protected List metaVariables;
  protected List containedMetaScopes;
  protected MetaScope containingMetaScope;


  protected void moveObjects( String cName, List list,
                              int oldIndex, int newIndex,
                              int numberOfElements )
  {
     if (oldIndex < 0 || newIndex < 0 ||
         oldIndex + numberOfElements > list.size() ||
         newIndex < 0 || newIndex > list.size() || 
         ! (newIndex < oldIndex || newIndex >= oldIndex + numberOfElements - 1)
     )
         throw new java.lang.IndexOutOfBoundsException("Move(" + oldIndex + "," + newIndex + "," + numberOfElements + ") on " + cName + "[" + list.size() + "]");
     Object x;
     for (int i = 0; i < numberOfElements; i++) {
         x = list.get(oldIndex);
         if (newIndex >= oldIndex + numberOfElements - 1) {
             list.remove(oldIndex);
             list.add(newIndex - 1,x);
         }
         else { // newIndex < oldIndex
             list.remove(oldIndex + i);
             list.add(newIndex + i,x);
         }
     }
  }



  /**
   * Default Constructor for MetaScope_Bean
   * 
   */
  public MetaScope_Bean() {
    this.name = "";
    this.sourceFile = "";
    this.filePosition = (int) 0;
    this.metaVariables = new ArrayList();
    this.containedMetaScopes = new ArrayList();
    this.containingMetaScope = null;
  }

  /**
   * Get method for name attribute.
   */
  public java.lang.String getName( )
  {
     return this.name;
  }

  /**
   * Set method for name attribute.
   * It fires a property change event if somebody is listening.
   */
  public void setName( java.lang.String n ) {
     this.name = n;
  }

  /**
   * Get method for sourceFile attribute.
   */
  public java.lang.String getSourceFile( )
  {
     return this.sourceFile;
  }

  /**
   * Set method for sourceFile attribute.
   * It fires a property change event if somebody is listening.
   */
  public void setSourceFile( java.lang.String n ) {
     java.lang.String oldSourceFile = this.sourceFile;
  }

  /**
   * Get method for filePosition attribute.
   */
  public int getFilePosition( )
  {
     return this.filePosition;
  }

  /**
   * Set method for filePosition attribute.
   * It fires a property change event if somebody is listening.
   */
  public void setFilePosition( int n ) {
     this.filePosition = n;
  }

  /**
   * Get method for containingMetaScope relationship.
   */
  public MetaScope getContainingMetaScope( ) {
     return this.containingMetaScope;
  }

  /**
   * Set method for containingMetaScope relationship.
   * It fires a property change event if somebody is listening.
   */
  public void setContainingMetaScope( MetaScope n ) {
     this.containingMetaScope = n;
  }

  /**
   * Get method for 1:n metaVariables relationship.
   */
  public List getMetaVariables( ) {
     return this.metaVariables;
  }

  /**
   * Indexed get of an element of metaVariables
   * (Indexed Bean property)
   */
  public MetaVariable getMetaVariables( int where ) {
     return (MetaVariable) this.metaVariables.get(where);
  }
  /**
   * Set method for 1:n metaVariables relationship.
   */
  public void setMetaVariables( List m ) {
     this.metaVariables = m;
  }

  /**
   * Insert of an element into metaVariables
   */
  public boolean insertIntoMetaVariables( MetaVariable m )
  {
     if (metaVariables.contains(m))
        return false;
     if (metaVariables.add(m)) {
        return true;
        }
     else
        return false;
  }

  public boolean insertIntoMetaVariablesAt( MetaVariable m, int where )
  {
     if (metaVariables.contains(m))
         return false;
     metaVariables.add(where,m);
      return true;
  }

  /**
   * Move of an element of metaVariables
   */
  public void moveInMetaVariables( int oldIndex, int newIndex, int numberOfElements ) {
     moveObjects("metaVariables",metaVariables,oldIndex,newIndex,numberOfElements);
  }

  /**
   * Remove of an element from metaVariables
   */
  public void removeFromMetaVariables( MetaVariable m ) { 
     int where = metaVariables.indexOf(m);
     metaVariables.remove(m);
  }

  /**
   * Remove a collection of elements from metaVariables
   */
  public void removeFromMetaVariables( Collection m ) { 
     int howMany = m.size();
    Collection myobjs = new ArrayList(m);
    for (Iterator iter = myobjs.iterator(); iter.hasNext();) {
      MetaVariable obj = (MetaVariable) iter.next();
      metaVariables.remove(obj);
    }
  }

  public void removeFromMetaVariablesAt( int where ) { 
     MetaVariable m = (MetaVariable)this.metaVariables.get(where);
     metaVariables.remove(where);
  }

  /**
   * Remove all elements from metaVariables
   */
  public synchronized void removeAllMetaVariables( ) {
     int howMany = this.metaVariables.size();
     this.metaVariables.clear();
  }

  /**
   * Replacement of an element of metaVariables (Indexed Bean property)
   */
  public void setMetaVariables( int where, MetaVariable n ) { 
     metaVariables.set(where,n);
  }

  /**
   * Get method for 1:n containedMetaScopes relationship.
   */
  public List getContainedMetaScopes( ) {
     return this.containedMetaScopes;
  }

  /**
   * Indexed get of an element of containedMetaScopes
   * (Indexed Bean property)
   */
  public MetaScope getContainedMetaScopes( int where ) {
     return (MetaScope) this.containedMetaScopes.get(where);
  }
  /**
   * Set method for 1:n containedMetaScopes relationship.
   */
  public void setContainedMetaScopes( List m ) {
     this.containedMetaScopes = m;
  }

  /**
   * Insert of an element into containedMetaScopes
   */
  public boolean insertIntoContainedMetaScopes( MetaScope m )
  {
     if (containedMetaScopes.contains(m))
         return false;
     if (containedMetaScopes.add(m)) {
       return true;
    }
    else
      return false;
  }

  public boolean insertIntoContainedMetaScopesAt( MetaScope m, int where )
  {
     if (containedMetaScopes.contains(m))
         return false;
     containedMetaScopes.add(where,m);
      return true;
  }

  /**
   * Move of an element of containedMetaScopes
   */
  public void moveInContainedMetaScopes( int oldIndex, int newIndex, int numberOfElements ) {
     moveObjects("containedMetaScopes",containedMetaScopes,oldIndex,newIndex,numberOfElements);
  }

  /**
   * Remove of an element from containedMetaScopes
   */
  public void removeFromContainedMetaScopes( MetaScope m ) { 
     int where = containedMetaScopes.indexOf(m);
     containedMetaScopes.remove(m);
  }

  /**
   * Remove a collection of elements from containedMetaScopes
   */
  public void removeFromContainedMetaScopes( Collection m ) { 
     int howMany = m.size();
    Collection myobjs = new ArrayList(m);
    for (Iterator iter = myobjs.iterator(); iter.hasNext();) {
      MetaScope obj = (MetaScope) iter.next();
      containedMetaScopes.remove(obj);
    }
  }

  public void removeFromContainedMetaScopesAt( int where ) { 
     MetaScope m = (MetaScope)this.containedMetaScopes.get(where);
     containedMetaScopes.remove(where);
  }

  /**
   * Remove all elements from containedMetaScopes
   */
  public synchronized void removeAllContainedMetaScopes( ) {
     int howMany = this.containedMetaScopes.size();
     this.containedMetaScopes.clear();
  }

  /**
   * Replacement of an element of containedMetaScopes (Indexed Bean property)
   */
  public void setContainedMetaScopes( int where, MetaScope n ) { 
     containedMetaScopes.set(where,n);
  }

 /////////////// toString() //////////////////////

  public String toString() {
        StringBuffer result = new StringBuffer(256);
        result.append("Class MetaScope\n");
        result.append("    name = " + this.name + "\n");
        result.append("    sourceFile = " + this.sourceFile + "\n");
        result.append("    filePosition = " + this.filePosition + "\n");
        return result.toString();
  }
}// End Of Class Definition
