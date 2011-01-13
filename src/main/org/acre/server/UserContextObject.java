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
package org.acre.server;

import java.util.Properties;

/**
 * User: johncrupi
 * Date: Apr 15, 2005
 * Time: 1:42:14 PM
 */
public class UserContextObject implements java.io.Serializable {
    String name;
    String password;
    Properties properties = new Properties();
    public UserContextObject( String name, String password ) {
        this.name = name;
        this.password = password;
    }
    String getName() {
        return name;
    }
    public String getPassword() {
        return password;
    }
    public Properties getProperties() {
        return properties;
    }
    public String getProperty(String propertyName) {
        return properties.getProperty(propertyName);
    }
    public void setProperty( String propertyName, String property ) {
        properties.setProperty( propertyName, property);
    }

    public int hashCode() {
        return name.hashCode() + password.hashCode();
    }

    public boolean equals(Object obj) {
        if ( obj == null )
            return false;

        if ( obj == this)
            return true;

        if ( obj instanceof UserContextObject) {
            UserContextObject that = (UserContextObject)obj;
            if ( that.getName().equalsIgnoreCase(getName()) &&
                 that.getPassword().equalsIgnoreCase(getPassword()) )
                return true;
        }
        return false;
    }

    public String toString() {
        return "UserContextObject: Name = " + name;
    }
}
