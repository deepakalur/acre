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
 *  The Java interface MetaVariable corresponds to
 *  the following objects in the specification:
 *
 *     MetaVariable
 *         persistent String name
 *         MetaScope metaScope
 *         MetaType metaType
 *
 */


public interface MetaVariable {


  /** Constant change event names. */
  public final static String DYNAMIC_INHERITANCE_MORPHED_PROPERTY = "Dynamic Inheritance Morphed";

  /** Bound property names of attributes. */
  public final static String NAME_PROPERTY = "org.acre.model.metamodel.MetaVariable.name";

    /** Bound property names of relationships. */
  public final static String METASCOPE_PROPERTY = "org.acre.model.metamodel.MetaVariable.metaScope";
  public final static String METATYPE_PROPERTY = "org.acre.model.metamodel.MetaVariable.metaType";


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
   * Get method(s) for metaScope relationship.
   */
  public MetaScope getMetaScope( );

  /**
   * Set method for metaScope relationship.
   * It fires a property change event if somebody is listening.
   */
  public void setMetaScope( MetaScope n );

  /**
   * Get method(s) for metaType relationship.
   */
  public MetaType getMetaType( );

  /**
   * Set method for metaType relationship.
   * It fires a property change event if somebody is listening.
   */
  public void setMetaType( MetaType n );

  public static final String xmlEncodingVersion = "1"; // XML

}// End Of Interface Definition
