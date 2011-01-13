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

import java.util.List;


/**
 *  The Java class MetaRelationship_Bean implements the Bean representation for 
 *  the following objects in the specification:
 *
 *     MetaRelationship
 *         persistent String name
 *         persistent String mappedName
 *         persistent String inverseName
 *         persistent Boolean collection
 *         persistent Boolean primarySide
 *         MetaType pointerMetaType
 *
 *  When the setXxx mutator methods are called, property change events are generated. 
 *  Methods are provided for registering listeners for property change events.
 *
 */


public class MetaRelationship_Bean  implements MetaRelationship {

  ///////////////////// Attribute Instance Variables /////////////////

  /** 
     *Example for class contains method & method isContained class
     forwardName = "contains"
     inverseName = "isContained"
     parentMetaType = "class"
     chilMetaType = "method"

     
     *Example for b = a.contains
     SELECT  c.fields FROM class c WHERE c.methods.accesibility = "public";
     
     class is a MetaType
     method is a MetaType
     field is a MetaType
     class has MetaRelationship with name "methods" to method
     method has MetaRelationship with name "class" to class
     class has MetaRelationship with name "fields" to field
     field has MetaRelationship with name "class" to class
     
     */
  protected java.lang.String name;
  protected java.lang.String mappedName;
  protected java.lang.String inverseName;
  protected boolean collection;
  protected boolean primarySide;

  //////////////////// Relationship Instance Variables ////////////////

  protected MetaType pointerMetaType;

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
   * Default Constructor for MetaRelationship_Bean
   * 
   */
  public MetaRelationship_Bean() {
    this.name = "";
    this.mappedName = "";
    this.inverseName = "";
    this.collection = false;
    this.primarySide = false;
    this.pointerMetaType = null;
  }

  /**
   * Constructor for the primary key property
   * 
   */
  public MetaRelationship_Bean(java.lang.String name) {
     this();
     //setName(name);
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
   */
  public void setName( java.lang.String n ) {
     java.lang.String oldName = this.name;
     this.name = n;
  }

  /**
   * Get method for mappedName attribute.
   */
  public java.lang.String getMappedName( )
  {
     return this.mappedName;
  }

  /**
   * Set method for mappedName attribute.
   */
  public void setMappedName( java.lang.String n ) {
     this.mappedName = n;
  }

  /**
   * Get method for inverseName attribute.
   */
  public java.lang.String getInverseName( )
  {
     return this.inverseName;
  }

  /**
   * Set method for inverseName attribute.
   * It fires a property change event if somebody is listening.
   */
  public void setInverseName( java.lang.String n ) {
     this.inverseName = n;
  }

  /**
   * Get method for collection attribute.
   */
  public boolean getCollection( )
  {
     return this.collection;
  }


  public boolean isCollection( )
  { return getCollection(); }

  /**
   * Set method for collection attribute.
   * It fires a property change event if somebody is listening.
   */
  public void setCollection( boolean n ) {
     this.collection = n;
  }

  /**
   * Get method for primarySide attribute.
   */
  public boolean getPrimarySide( )
  {
     return this.primarySide;
  }


  public boolean isPrimarySide( )
  { return getPrimarySide(); }

  /**
   * Set method for primarySide attribute.
   * It fires a property change event if somebody is listening.
   */
  public void setPrimarySide( boolean n ) {
     this.primarySide = n;
  }

  /**
   * Get method for pointerMetaType relationship.
   */
  public MetaType getPointerMetaType( ) {
     return this.pointerMetaType;
  }

  /**
   * Set method for pointerMetaType relationship.
   * It fires a property change event if somebody is listening.
   */
  public void setPointerMetaType( MetaType n ) {
     this.pointerMetaType = n;
  }

 /////////////// toString() //////////////////////

  public String toString() {
        StringBuffer result = new StringBuffer(256);
        result.append("Class MetaRelationship\n");
        result.append("    PRIMARY KEY\n");
        result.append("        name = " + this.name + "\n");
        result.append("    mappedName = " + this.mappedName + "\n");
        result.append("    inverseName = " + this.inverseName + "\n");
        result.append("    collection = " + this.collection + "\n");
        result.append("    primarySide = " + this.primarySide + "\n");
        return result.toString();
  }

}// End Of Class Definition
