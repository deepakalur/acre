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

import java.util.Collection;
import java.util.List;


/**
 *  The Java interface MetaType corresponds to
 *  the following objects in the specification:
 *
 *     MetaType
 *         persistent String name
 *         persistent String mappedName
 *         MetaModel metaModel
 *         List<MetaAttribute> metaAttributes
 *         List<MetaRelationship> metaRelationships
 *
 */


public interface MetaType {


  /** Constant change event names. */
  public final static String DYNAMIC_INHERITANCE_MORPHED_PROPERTY = "Dynamic Inheritance Morphed";

  /** Bound property names of attributes. */
  public final static String NAME_PROPERTY = "org.acre.model.metamodel.MetaType.name";
  public final static String MAPPEDNAME_PROPERTY = "org.acre.model.metamodel.MetaType.mappedName";

    /** Bound property names of relationships. */
  public final static String METAMODEL_PROPERTY = "org.acre.model.metamodel.MetaType.metaModel";
  public final static String METAATTRIBUTES_PROPERTY = "org.acre.model.metamodel.MetaType.metaAttributes";
  public final static String METARELATIONSHIPS_PROPERTY = "org.acre.model.metamodel.MetaType.metaRelationships";

  /** Class Constants. */

  /** Get/Set methods for _defunct status variable. */
  public boolean get_Defunct( );
  public void set_Defunct( );

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
   * Get method(s) for metaModel relationship.
   */
  public MetaModel getMetaModel( );

  /**
   * Set method for metaModel relationship.
   * It fires a property change event if somebody is listening.
   */
  public void setMetaModel( MetaModel n );

  /**
   * Get method for 1:n metaAttributes relationship.
   */
  public List getMetaAttributes( );

  /**
   * Indexed get of an element of metaAttributes
   * (Indexed Bean property)
   */
  public MetaAttribute getMetaAttributes( int where );

  /**
   * lookup of an element of metaAttributes by primary key attributes
   */
  public MetaAttribute lookupInMetaAttributes( java.lang.String name );

  /**
   * Set method for 1:n metaAttributes relationship.
   */
  public void setMetaAttributes( List m );

  /**
   * Insert of an element into metaAttributes
   */
  public boolean insertIntoMetaAttributes( MetaAttribute m );

  public boolean insertIntoMetaAttributesAt( MetaAttribute m, int where );

  /**
   * Move of an element of metaAttributes
   */
  public void moveInMetaAttributes( int oldIndex, int newIndex, int numberOfElements );

  /**
   * Remove of an element from metaAttributes
   */
  public void removeFromMetaAttributes( MetaAttribute m );

  /**
   * Remove a collection of elements from metaAttributes
   */
  public void removeFromMetaAttributes( Collection m );

  public void removeFromMetaAttributesAt( int where );

  /**
   * Remove all elements from metaAttributes
   */
  public void removeAllMetaAttributes( );

  /**
   * Replacement of an element of metaAttributes (Indexed Bean property)
   */
  public void setMetaAttributes( int where, MetaAttribute n );

  /**
   * Get method for 1:n metaRelationships relationship.
   */
  public List getMetaRelationships( );

  /**
   * Indexed get of an element of metaRelationships
   * (Indexed Bean property)
   */
  public MetaRelationship getMetaRelationships( int where );

  /**
   * lookup of an element of metaRelationships by primary key attributes
   */
  public MetaRelationship lookupInMetaRelationships( java.lang.String name );

  /**
   * Set method for 1:n metaRelationships relationship.
   */
  public void setMetaRelationships( List m );

  /**
   * Insert of an element into metaRelationships
   */
  public boolean insertIntoMetaRelationships( MetaRelationship m );

  public boolean insertIntoMetaRelationshipsAt( MetaRelationship m, int where );

  /**
   * Move of an element of metaRelationships
   */
  public void moveInMetaRelationships( int oldIndex, int newIndex, int numberOfElements );

  /**
   * Remove of an element from metaRelationships
   */
  public void removeFromMetaRelationships( MetaRelationship m );

  /**
   * Remove a collection of elements from metaRelationships
   */
  public void removeFromMetaRelationships( Collection m );

  public void removeFromMetaRelationshipsAt( int where );

  /**
   * Remove all elements from metaRelationships
   */
  public void removeAllMetaRelationships( );

  /**
   * Replacement of an element of metaRelationships (Indexed Bean property)
   */
  public void setMetaRelationships( int where, MetaRelationship n );

}// End Of Interface Definition
