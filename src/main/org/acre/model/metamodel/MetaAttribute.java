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
 *  The Java interface MetaAttribute corresponds to
 *  the following objects in the specification:
 *
 *     MetaAttribute
 *         persistent String name
 *         persistent String mappedName
 *         persistent String type
 *
 */


public interface MetaAttribute {


  /** Constant change event names. */
  public final static String DYNAMIC_INHERITANCE_MORPHED_PROPERTY = "Dynamic Inheritance Morphed";

  /** Bound property names of attributes. */
  public final static String NAME_PROPERTY = "org.acre.model.metamodel.MetaAttribute.name";
  public final static String MAPPEDNAME_PROPERTY = "org.acre.model.metamodel.MetaAttribute.mappedName";
  /**
     *Enum of Java EBTs
     */
  public final static String TYPE_PROPERTY = "org.acre.model.metamodel.MetaAttribute.type";

    /** Bound property names of relationships. */

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
   * Get method for type attribute.
   */
  public java.lang.String getType( );

  /**
   * Set method for type attribute.
   * It fires a property change event if somebody is listening.
   */
  public void setType( java.lang.String n );


}// End Of Interface Definition
