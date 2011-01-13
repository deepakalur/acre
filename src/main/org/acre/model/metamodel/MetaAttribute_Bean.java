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
 *  The Java class MetaAttribute_Bean implements the Bean representation for 
 *  the following objects in the specification:
 *
 *     MetaAttribute
 *         persistent String name
 *         persistent String mappedName
 *         persistent String type
 *
 */


public class MetaAttribute_Bean  implements MetaAttribute {

  ///////////////////// Attribute Instance Variables /////////////////

  protected java.lang.String name;
  protected java.lang.String mappedName;
  /** 
     *Enum of Java EBTs
     */
  protected java.lang.String type;

  //////////////////// Relationship Instance Variables ////////////////


  ////////////////////////// List of Listeners ////////////////////////


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
   * Default Constructor for MetaAttribute_Bean
   * 
   */
  public MetaAttribute_Bean() {
    this.name = "";
    this.mappedName = "";
    this.type = "";
  }

  /**
   * Constructor for the primary key property
   * 
   */
  public MetaAttribute_Bean(java.lang.String name) {
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
   * It fires a property change event if somebody is listening.
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
   * It fires a property change event if somebody is listening.
   */
  public void setMappedName( java.lang.String n ) {
     this.mappedName = n;
  }

  /**
   * Get method for type attribute.
   */
  public java.lang.String getType( )
  {
     return this.type;
  }

  /**
   * Set method for type attribute.
   */
  public void setType( java.lang.String n ) {
     this.type = n;

  }

 /////////////// toString() //////////////////////

  public String toString() {
        StringBuffer result = new StringBuffer(256);
        result.append("Class MetaAttribute\n");
        result.append("    PRIMARY KEY\n");
        result.append("        name = " + this.name + "\n");
        result.append("    mappedName = " + this.mappedName + "\n");
        result.append("    type = " + this.type + "\n");
        return result.toString();
  }

}// End Of Class Definition
