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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


/**
 *  The Java class MetaType_Bean implements the Bean representation for
 *  the following objects in the specification:
 *
 *     MetaType
 *         persistent String name
 *         persistent String mappedName
 *         MetaModel metaModel
 *         List<MetaAttribute> metaAttributes
 *         List<MetaRelationship> metaRelationships
 *
 *  When the setXxx mutator methods are called, property change events are generated.
 *  Methods are provided for registering listeners for property change events.
 *
 */


public class MetaType_Bean  implements MetaType {

  ///////////////////// Attribute Instance Variables /////////////////

  protected java.lang.String name;
  protected java.lang.String mappedName;

  //////////////////// Relationship Instance Variables ////////////////

  protected MetaModel metaModel;
  protected List metaAttributes;
  protected List metaRelationships;

  ////////////////////////// List of Listeners ////////////////////////

  protected EventListenerList listenerList = new EventListenerList();


  /** Defunct object state detection. */
  protected boolean _defunct; // defunct object state
  public boolean get_Defunct() { return _defunct; }
  protected void defunct_error()
  {
      throw new RuntimeException("Attempt to use defunct object: " + toString());
  }

  /**
   * Recursive Set method for _defunct status variable.
   */
  public void set_Defunct()
  {
    /* Set _defunct in all owned objects */
    _defunct = true; // must do this last!
  } // end set_Defunct()

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
   * Default Constructor for MetaType_Bean
   *
   */
  public MetaType_Bean() {
    this.name = "";
    this.mappedName = "";
    this.metaModel = null;
    this.metaAttributes = new ArrayList();
    this.metaRelationships = new ArrayList();
  }

  /**
   * Constructor(s) for the primary key property and the owner
   *
   */
  public MetaType_Bean(MetaModel metaModel,java.lang.String name) {
     this();
     this.metaModel = metaModel;
     //setName(name);
     this.name = name;
  }


  /**
   * Get method for name attribute.
   */
  public java.lang.String getName( )
  {
    if (_defunct)
      defunct_error();
     return this.name;
  }

  /**
   * Set method for name attribute.
   * It fires a property change event if somebody is listening.
   */
  public void setName( java.lang.String n ) {
    if (_defunct)
      defunct_error();
     java.lang.String oldName = this.name;
     this.name = n;
  }

  /**
   * Get method for mappedName attribute.
   */
  public java.lang.String getMappedName( )
  {
    if (_defunct)
      defunct_error();
     return this.mappedName;
  }

  /**
   * Set method for mappedName attribute.
   * It fires a property change event if somebody is listening.
   */
  public void setMappedName( java.lang.String n ) {
    if (_defunct)
      defunct_error();
     java.lang.String oldMappedName = this.mappedName;
     this.mappedName = n;

  }

  /**
   * Get method for metaModel relationship.
   */
  public MetaModel getMetaModel( ) {
    if (_defunct)
      defunct_error();
     return this.metaModel;
  }

  /**
   * Set method for metaModel relationship.
   * It fires a property change event if somebody is listening.
   */
  public void setMetaModel( MetaModel n ) {
    if (_defunct)
      defunct_error();
     MetaModel oldMetaModel = this.metaModel;
     this.metaModel = n;

  }

  /**
   * Get method for 1:n metaAttributes relationship.
   */
  public List getMetaAttributes( ) {
    if (_defunct)
      defunct_error();
     return this.metaAttributes;
  }

  /**
   * Indexed get of an element of metaAttributes
   * (Indexed Bean property)
   */
  public MetaAttribute getMetaAttributes( int where ) {
    if (_defunct)
      defunct_error();
     return (MetaAttribute) this.metaAttributes.get(where);
  }
  /**
   * lookup of an element of metaAttributes by primary key attributes
   */
  public MetaAttribute lookupInMetaAttributes( java.lang.String name ) {
    if (_defunct)
      defunct_error();
    Iterator i = getMetaAttributes().iterator();
    while (i.hasNext()) {
        MetaAttribute m = (MetaAttribute) i.next();
        if ( (m.getName().equalsIgnoreCase(name)))
              return m;
    }
    return null;
  }

  /**
   * Set method for 1:n metaAttributes relationship.
   */
  public void setMetaAttributes( List m ) {
    if (_defunct)
      defunct_error();
     List oldm = this.metaAttributes;
     this.metaAttributes = m;

  }

  /**
   * Insert of an element into metaAttributes
   */
  public boolean insertIntoMetaAttributes( MetaAttribute m )
  {
    if (_defunct)
      defunct_error();
     if (metaAttributes.contains(m))
         return false;
     if (metaAttributes.add(m)) {
       return true;
    }
    else
      return false;
  }

  public boolean insertIntoMetaAttributesAt( MetaAttribute m, int where )
  {
    if (_defunct)
      defunct_error();
     if (metaAttributes.contains(m))
         return false;
     metaAttributes.add(where,m);
      return true;
  }

  /**
   * Move of an element of metaAttributes
   */
  public void moveInMetaAttributes( int oldIndex, int newIndex, int numberOfElements ) {
    if (_defunct)
      defunct_error();
     moveObjects("metaAttributes",metaAttributes,oldIndex,newIndex,numberOfElements);
  }

  /**
   * Remove of an element from metaAttributes
   */
  public void removeFromMetaAttributes( MetaAttribute m ) {
    if (_defunct)
      defunct_error();
     int where = metaAttributes.indexOf(m);
     metaAttributes.remove(m);
  }

  /**
   * Remove a collection of elements from metaAttributes
   */
  public void removeFromMetaAttributes( Collection m ) {
    if (_defunct)
      defunct_error();
     int howMany = m.size();
    Collection myobjs = new ArrayList(m);
    for (Iterator iter = myobjs.iterator(); iter.hasNext();) {
      MetaAttribute obj = (MetaAttribute) iter.next();
      metaAttributes.remove(obj);
    }
  }

  public void removeFromMetaAttributesAt( int where ) {
    if (_defunct)
      defunct_error();
     MetaAttribute m = (MetaAttribute)this.metaAttributes.get(where);
     metaAttributes.remove(where);
  }

  /**
   * Remove all elements from metaAttributes
   */
  public synchronized void removeAllMetaAttributes( ) {
    if (_defunct)
      defunct_error();
     int howMany = this.metaAttributes.size();
     this.metaAttributes.clear();
  }

  /**
   * Replacement of an element of metaAttributes (Indexed Bean property)
   */
  public void setMetaAttributes( int where, MetaAttribute n ) {
    if (_defunct)
      defunct_error();
     MetaAttribute oldMetaAttributes = (MetaAttribute) this.metaAttributes.get(where);
     metaAttributes.set(where,n);
  }

  /**
   * Get method for 1:n metaRelationships relationship.
   */
  public List getMetaRelationships( ) {
    if (_defunct)
      defunct_error();
     return this.metaRelationships;
  }

  /**
   * Indexed get of an element of metaRelationships
   * (Indexed Bean property)
   */
  public MetaRelationship getMetaRelationships( int where ) {
    if (_defunct)
      defunct_error();
     return (MetaRelationship) this.metaRelationships.get(where);
  }
  /**
   * lookup of an element of metaRelationships by primary key attributes
   */
  public MetaRelationship lookupInMetaRelationships( java.lang.String name ) {
    if (_defunct)
      defunct_error();
    Iterator i = getMetaRelationships().iterator();
    while (i.hasNext()) {
        MetaRelationship m = (MetaRelationship) i.next();
        if ( (m.getName().equalsIgnoreCase(name)))
              return m;
    }
    return null;
  }

  /**
   * Set method for 1:n metaRelationships relationship.
   */
  public void setMetaRelationships( List m ) {
    if (_defunct)
      defunct_error();
     List oldm = this.metaRelationships;
     this.metaRelationships = m;
  }

  /**
   * Insert of an element into metaRelationships
   */
  public boolean insertIntoMetaRelationships( MetaRelationship m )
  {
    if (_defunct)
      defunct_error();
     if (metaRelationships.contains(m))
         return false;
     if (metaRelationships.add(m)) {
       return true;
    }
    else
      return false;
  }

  public boolean insertIntoMetaRelationshipsAt( MetaRelationship m, int where )
  {
    if (_defunct)
      defunct_error();
     if (metaRelationships.contains(m))
         return false;
     metaRelationships.add(where,m);
     return true;
  }

  /**
   * Move of an element of metaRelationships
   */
  public void moveInMetaRelationships( int oldIndex, int newIndex, int numberOfElements ) {
    if (_defunct)
      defunct_error();
     moveObjects("metaRelationships",metaRelationships,oldIndex,newIndex,numberOfElements);
  }

  /**
   * Remove of an element from metaRelationships
   */
  public void removeFromMetaRelationships( MetaRelationship m ) {
    if (_defunct)
      defunct_error();
     int where = metaRelationships.indexOf(m);
     metaRelationships.remove(m);
  }

  /**
   * Remove a collection of elements from metaRelationships
   */
  public void removeFromMetaRelationships( Collection m ) {
    if (_defunct)
      defunct_error();
     int howMany = m.size();
    Collection myobjs = new ArrayList(m);
    for (Iterator iter = myobjs.iterator(); iter.hasNext();) {
      MetaRelationship obj = (MetaRelationship) iter.next();
      metaRelationships.remove(obj);
    }
  }

  public void removeFromMetaRelationshipsAt( int where ) {
    if (_defunct)
      defunct_error();
     MetaRelationship m = (MetaRelationship)this.metaRelationships.get(where);
     metaRelationships.remove(where);
  }

  /**
   * Remove all elements from metaRelationships
   */
  public synchronized void removeAllMetaRelationships( ) {
    if (_defunct)
      defunct_error();
     int howMany = this.metaRelationships.size();
     this.metaRelationships.clear();
  }

  /**
   * Replacement of an element of metaRelationships (Indexed Bean property)
   */
  public void setMetaRelationships( int where, MetaRelationship n ) {
    if (_defunct)
      defunct_error();
     MetaRelationship oldMetaRelationships = (MetaRelationship) this.metaRelationships.get(where);
     metaRelationships.set(where,n);
  }

 /////////////// toString() //////////////////////

  public String toString() {
        StringBuffer result = new StringBuffer(256);
        result.append("Class MetaType\n");
        result.append("    PRIMARY KEY\n");
        result.append("        name = " + this.name + "\n");
        result.append("    mappedName = " + this.mappedName + "\n");
        return result.toString();
  }


}// End Of Class Definition
