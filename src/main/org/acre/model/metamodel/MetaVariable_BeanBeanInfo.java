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
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

public class MetaVariable_BeanBeanInfo extends SimpleBeanInfo {

    Class beanClass = MetaVariable_Bean.class;

    public MetaVariable_BeanBeanInfo() {
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
          // Following prevents compilation error in case 
          // there are no properties, in which case the catch
          // below would catch an exception that's not thrown
          if (false) {
            throw new IntrospectionException("Dryrot");
          }

          /** Bound property names of attributes. */
          PropertyDescriptor _name = new PropertyDescriptor("name", beanClass, "getName", "setName");

          /** Bound property names for relationships. */
          PropertyDescriptor _metaScope = new PropertyDescriptor("metaScope", beanClass, "getMetaScope", "setMetaScope");
          PropertyDescriptor _metaType = new PropertyDescriptor("metaType", beanClass, "getMetaType", "setMetaType");

          /** Bound property names for pseudo-attribute getter methods. */

          // Collect descriptors into an array
          PropertyDescriptor[] pds = new PropertyDescriptor[] {
                _name,
                _metaScope,
                _metaType,
          };
          return pds;
        } catch(IntrospectionException ex) {
            ex.printStackTrace();
            return null;
        }
    } // end getPropertyDescriptors

} // end class MetaVariable_BeanBeanInfo
