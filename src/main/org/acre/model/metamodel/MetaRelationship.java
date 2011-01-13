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

/**
 *  The Java interface MetaRelationship corresponds to
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
 */


public interface MetaRelationship {


  /** Constant change event names. */
  public final static String DYNAMIC_INHERITANCE_MORPHED_PROPERTY = "Dynamic Inheritance Morphed";

  /** Bound property names of attributes. */
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
  public final static String NAME_PROPERTY = "org.acre.model.metamodel.MetaRelationship.name";
  public final static String MAPPEDNAME_PROPERTY = "org.acre.model.metamodel.MetaRelationship.mappedName";
  public final static String INVERSENAME_PROPERTY = "org.acre.model.metamodel.MetaRelationship.inverseName";
  public final static String COLLECTION_PROPERTY = "org.acre.model.metamodel.MetaRelationship.collection";
  public final static String PRIMARYSIDE_PROPERTY = "org.acre.model.metamodel.MetaRelationship.primarySide";

    /** Bound property names of relationships. */
  public final static String POINTERMETATYPE_PROPERTY = "org.acre.model.metamodel.MetaRelationship.pointerMetaType";

  /**
   * Get method for name attribute.
   */
  public java.lang.String getName( );

  /**
   * Set method for name attribute.
   * It fires a property change event if somebody is listening.
   */
  public void setName( java.lang.String n );

  /**
   * Get method for mappedName attribute.
   */
  public java.lang.String getMappedName( );

  /**
   * Set method for mappedName attribute.
   * It fires a property change event if somebody is listening.
   */
  public void setMappedName( java.lang.String n );

  /**
   * Get method for inverseName attribute.
   */
  public java.lang.String getInverseName( );

  /**
   * Set method for inverseName attribute.
   * It fires a property change event if somebody is listening.
   */
  public void setInverseName( java.lang.String n );

  /**
   * Get method for collection attribute.
   */
  public boolean getCollection( );

  /**
   * Get method for collection attribute.
   */
  public boolean isCollection( );

  /**
   * Set method for collection attribute.
   * It fires a property change event if somebody is listening.
   */
  public void setCollection( boolean n );

  /**
   * Get method for primarySide attribute.
   */
  public boolean getPrimarySide( );

  /**
   * Get method for primarySide attribute.
   */
  public boolean isPrimarySide( );

  /**
   * Set method for primarySide attribute.
   * It fires a property change event if somebody is listening.
   */
  public void setPrimarySide( boolean n );

  /**
   * Get method(s) for pointerMetaType relationship.
   */
  public MetaType getPointerMetaType( );

  /**
   * Set method for pointerMetaType relationship.
   * It fires a property change event if somebody is listening.
   */
  public void setPointerMetaType( MetaType n );

  public static final String xmlEncodingVersion = "1"; // XML

}// End Of Interface Definition
