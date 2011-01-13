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
 *  The Java interface MetaModel corresponds to
 *  the following objects in the specification:
 *
 *     MetaModel
 *         persistent String name
 *         List<MetaType> metaTypes
 *
 */


public interface MetaModel {


  /** Constant change event names. */
  public final static String DYNAMIC_INHERITANCE_MORPHED_PROPERTY = "Dynamic Inheritance Morphed";

  /** Bound property names of attributes. */
  public final static String NAME_PROPERTY = "org.acre.model.metamodel.MetaModel.name";

    /** Bound property names of relationships. */
  public final static String METATYPES_PROPERTY = "org.acre.model.metamodel.MetaModel.metaTypes";

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
   * Get method for 1:n metaTypes relationship.
   */
  public List getMetaTypes( );

  /**
   * Indexed get of an element of metaTypes
   * (Indexed Bean property)
   */
  public MetaType getMetaTypes( int where );

  /**
   * lookup of an element of metaTypes by primary key attributes
   */
  public MetaType lookupInMetaTypes( java.lang.String name );

  /**
   * Set method for 1:n metaTypes relationship.
   */
  public void setMetaTypes( List m );

  /**
   * Insert of an element into metaTypes
   */
  public boolean insertIntoMetaTypes( MetaType m );

  public boolean insertIntoMetaTypesAt( MetaType m, int where );

  /**
   * Move of an element of metaTypes
   */
  public void moveInMetaTypes( int oldIndex, int newIndex, int numberOfElements );

  /**
   * Remove of an element from metaTypes
   */
  public void removeFromMetaTypes( MetaType m );

  /**
   * Remove a collection of elements from metaTypes
   */
  public void removeFromMetaTypes( Collection m );

  public void removeFromMetaTypesAt( int where );

  /**
   * Remove all elements from metaTypes
   */
  public void removeAllMetaTypes( );

  /**
   * Replacement of an element of metaTypes (Indexed Bean property)
   */
  public void setMetaTypes( int where, MetaType n );

  public static final String xmlEncodingVersion = "1"; // XML

}// End Of Interface Definition
