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
 *  The Java interface MetaScope corresponds to
 *  the following objects in the specification:
 *
 *     MetaScope
 *         persistent String name
 *         persistent String sourceFile
 *         persistent Integer filePosition
 *         List<MetaVariable> metaVariables
 *         List<MetaScope> containedMetaScopes
 *         MetaScope containingMetaScope
 *
 */


public interface MetaScope {


  /** Constant change event names. */
  public final static String DYNAMIC_INHERITANCE_MORPHED_PROPERTY = "Dynamic Inheritance Morphed";

  /** Bound property names of attributes. */
  public final static String NAME_PROPERTY = "org.acre.model.metamodel.MetaScope.name";
  public final static String SOURCEFILE_PROPERTY = "org.acre.model.metamodel.MetaScope.sourceFile";
  public final static String FILEPOSITION_PROPERTY = "org.acre.model.metamodel.MetaScope.filePosition";

    /** Bound property names of relationships. */
  public final static String METAVARIABLES_PROPERTY = "org.acre.model.metamodel.MetaScope.metaVariables";
  public final static String CONTAINEDMETASCOPES_PROPERTY = "org.acre.model.metamodel.MetaScope.containedMetaScopes";
  public final static String CONTAININGMETASCOPE_PROPERTY = "org.acre.model.metamodel.MetaScope.containingMetaScope";


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
   * Get method for sourceFile attribute.
   */
  public java.lang.String getSourceFile( );

  /**
   * Set method for sourceFile attribute.
   * It fires a property change event if somebody is listening.
   */
  public void setSourceFile( java.lang.String n );

  /**
   * Get method for filePosition attribute.
   */
  public int getFilePosition( );

  /**
   * Set method for filePosition attribute.
   * It fires a property change event if somebody is listening.
   */
  public void setFilePosition( int n );

  /**
   * Get method(s) for containingMetaScope relationship.
   */
  public MetaScope getContainingMetaScope( );

  /**
   * Set method for containingMetaScope relationship.
   * It fires a property change event if somebody is listening.
   */
  public void setContainingMetaScope( MetaScope n );

  /**
   * Get method for 1:n metaVariables relationship.
   */
  public List getMetaVariables( );

  /**
   * Indexed get of an element of metaVariables
   * (Indexed Bean property)
   */
  public MetaVariable getMetaVariables( int where );

  /**
   * Set method for 1:n metaVariables relationship.
   */
  public void setMetaVariables( List m );

  /**
   * Insert of an element into metaVariables
   */
  public boolean insertIntoMetaVariables( MetaVariable m );

  public boolean insertIntoMetaVariablesAt( MetaVariable m, int where );

  /**
   * Move of an element of metaVariables
   */
  public void moveInMetaVariables( int oldIndex, int newIndex, int numberOfElements );

  /**
   * Remove of an element from metaVariables
   */
  public void removeFromMetaVariables( MetaVariable m );

  /**
   * Remove a collection of elements from metaVariables
   */
  public void removeFromMetaVariables( Collection m );

  public void removeFromMetaVariablesAt( int where );

  /**
   * Remove all elements from metaVariables
   */
  public void removeAllMetaVariables( );

  /**
   * Replacement of an element of metaVariables (Indexed Bean property)
   */
  public void setMetaVariables( int where, MetaVariable n );

  /**
   * Get method for 1:n containedMetaScopes relationship.
   */
  public List getContainedMetaScopes( );

  /**
   * Indexed get of an element of containedMetaScopes
   * (Indexed Bean property)
   */
  public MetaScope getContainedMetaScopes( int where );

  /**
   * Set method for 1:n containedMetaScopes relationship.
   */
  public void setContainedMetaScopes( List m );

  /**
   * Insert of an element into containedMetaScopes
   */
  public boolean insertIntoContainedMetaScopes( MetaScope m );

  public boolean insertIntoContainedMetaScopesAt( MetaScope m, int where );

  /**
   * Move of an element of containedMetaScopes
   */
  public void moveInContainedMetaScopes( int oldIndex, int newIndex, int numberOfElements );

  /**
   * Remove of an element from containedMetaScopes
   */
  public void removeFromContainedMetaScopes( MetaScope m );

  /**
   * Remove a collection of elements from containedMetaScopes
   */
  public void removeFromContainedMetaScopes( Collection m );

  public void removeFromContainedMetaScopesAt( int where );

  /**
   * Remove all elements from containedMetaScopes
   */
  public void removeAllContainedMetaScopes( );

  /**
   * Replacement of an element of containedMetaScopes (Indexed Bean property)
   */
  public void setContainedMetaScopes( int where, MetaScope n );

  public static final String xmlEncodingVersion = "1"; // XML

}// End Of Interface Definition
