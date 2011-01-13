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

import javax.swing.event.EventListenerList;
import java.util.List;


/**
 *  The Java class MetaVariable_Bean implements the Bean representation for 
 *  the following objects in the specification:
 *
 *     MetaVariable
 *         persistent String name
 *         MetaScope metaScope
 *         MetaType metaType
 *
 *  When the setXxx mutator methods are called, property change events are generated. 
 *  Methods are provided for registering listeners for property change events.
 *
 */


public class MetaVariable_Bean  implements MetaVariable {

  ///////////////////// Attribute Instance Variables /////////////////

  protected java.lang.String name;

  //////////////////// Relationship Instance Variables ////////////////

  protected MetaScope metaScope;
  protected MetaType metaType;

  ////////////////////////// List of Listeners ////////////////////////

  protected EventListenerList listenerList = new EventListenerList();


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
   * Default Constructor for MetaVariable_Bean
   * 
   */
  public MetaVariable_Bean() {
    this.name = "";
    this.metaScope = null;
    this.metaType = null;
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
   * Get method for metaScope relationship.
   */
  public MetaScope getMetaScope( ) {
     return this.metaScope;
  }

  /**
   * Set method for metaScope relationship.
   */
  public void setMetaScope( MetaScope n ) {
     this.metaScope = n;
  }

  /**
   * Get method for metaType relationship.
   */
  public MetaType getMetaType( ) {
     return this.metaType;
  }

  /**
   * Set method for metaType relationship.
   */
  public void setMetaType( MetaType n ) {
     this.metaType = n;
  }

 /////////////// toString() //////////////////////

  public String toString() {
        StringBuffer result = new StringBuffer(256);
        result.append("Class MetaVariable\n");
        result.append("    name = " + this.name + "\n");
        return result.toString();
  }


}// End Of Class Definition
