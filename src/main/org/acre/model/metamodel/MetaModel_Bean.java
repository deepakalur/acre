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
 *  The Java class MetaModel_Bean implements the Bean representation for
 *  the following objects in the specification:
 *
 *     MetaModel
 *         persistent String name
 *         List<MetaType> metaTypes
 *
 *  When the setXxx mutator methods are called, property change events are generated.
 *  Methods are provided for registering listeners for property change events.
 *
 */


public class MetaModel_Bean  implements MetaModel {

  ///////////////////// Attribute Instance Variables /////////////////

  protected java.lang.String name;

  //////////////////// Relationship Instance Variables ////////////////

  protected List metaTypes;


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
   * Default Constructor for MetaModel_Bean
   *
   */
  public MetaModel_Bean() {
    this.name = "";
    this.metaTypes = new ArrayList();
  }

  /**
   * Constructor for the primary key property
   *
   */
  public MetaModel_Bean(java.lang.String name) {
     this();
     this.name = name;
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
     java.lang.String oldName = this.name;
     this.name = n;
  }

  /**
   * Get method for 1:n metaTypes relationship.
   */
  public List getMetaTypes( ) {
     return this.metaTypes;
  }

  /**
   * Indexed get of an element of metaTypes
   * (Indexed Bean property)
   */
  public MetaType getMetaTypes( int where ) {
     return (MetaType) this.metaTypes.get(where);
  }
  /**
   * lookup of an element of metaTypes by primary key attributes
   */
  public MetaType lookupInMetaTypes( java.lang.String name ) {
    Iterator i = getMetaTypes().iterator();
    while (i.hasNext()) {
        MetaType m = (MetaType) i.next();
        if ( (m.getName().equalsIgnoreCase(name)))
              return m;
    }
    return null;
  }

  /**
   * Set method for 1:n metaTypes relationship.
   */
  public void setMetaTypes( List m ) {
     this.metaTypes = m;
  }

  /**
   * Insert of an element into metaTypes
   */
  public boolean insertIntoMetaTypes( MetaType m )
  {
     if (metaTypes.contains(m))
         return false;
     if (metaTypes.add(m)) {
       return true;
    }
    else
      return false;
  }

  public boolean insertIntoMetaTypesAt( MetaType m, int where )
  {
     if (metaTypes.contains(m))
         return false;
     metaTypes.add(where,m);
      return true;
  }

  /**
   * Move of an element of metaTypes
   */
  public void moveInMetaTypes( int oldIndex, int newIndex, int numberOfElements ) {
     moveObjects("metaTypes",metaTypes,oldIndex,newIndex,numberOfElements);
  }

  /**
   * Remove of an element from metaTypes
   */
  public void removeFromMetaTypes( MetaType m ) {
     metaTypes.remove(m);
  }

  /**
   * Remove a collection of elements from metaTypes
   */
  public void removeFromMetaTypes( Collection m ) {
    Collection myobjs = new ArrayList(m);
    for (Iterator iter = myobjs.iterator(); iter.hasNext();) {
      MetaType obj = (MetaType) iter.next();
      metaTypes.remove(obj);
    }
  }

  public void removeFromMetaTypesAt( int where ) {
     MetaType m = (MetaType)this.metaTypes.get(where);
     metaTypes.remove(where);
  }

  /**
   * Remove all elements from metaTypes
   */
  public synchronized void removeAllMetaTypes( ) {
     this.metaTypes.clear();
  }

  /**
   * Replacement of an element of metaTypes (Indexed Bean property)
   */
  public void setMetaTypes( int where, MetaType n ) {
     metaTypes.set(where,n);
  }

 /////////////// toString() //////////////////////

  public String toString() {
        StringBuffer result = new StringBuffer(256);
        result.append("Class MetaModel\n");
        result.append("    PRIMARY KEY\n");
        result.append("        name = " + this.name + "\n");
        return result.toString();
  }
}// End Of Class Definition
