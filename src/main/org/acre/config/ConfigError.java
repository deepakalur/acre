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
package org.acre.config;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;

/**
 * @author Deepak.Alur@Sun.com
 *         Date: Oct 24, 2004
 *         Time: 11:03:36 PM
 */
public class ConfigError extends Properties {
    public static final Object ACRE_REPOSITORY_EMPTY_ERROR = "config.repository.empty";
    public static final Object ACRE_REPOSITORY_PERMISSION_ERROR = "config.repositorydir.permission";
    public static final Object ACRE_REPOSITORY_NOTDIRECTORY_ERROR = "config.repositorydir.notdir";
    public static final Object ACRE_REPOSITORYDIR_EMPTY_ERROR = "config.repositorydir.empty";
    public static final Object ACRE_REPOSITORYDIR_NOTEXIST_ERROR = "config.repositorydir.notexist";

    public static final Object ACRE_DBTYPE_EMPTY_ERROR = "config.dbtype.empty";
    public static final Object ACRE_DBTYPE_INVALID_ERROR = "config.dbtype.invalid";

    public static final Object ACRE_TDBFILEPATH_EMPTY_ERROR = "config.tdbfilepath.empty";
    public static final Object ACRE_TDBFILEPATH_NOTEXIST_ERROR = "config.tdbfilepath.notexist";
    public static final Object ACRE_TDBFILEPATH_PERMISSION_ERROR = "config.tdbfile.permission";

    public static final Object ACRE_RDBMSURL_EMPTY_ERROR = "config.rdbmsurl.empty";
    public static final Object ACRE_RDBMSDBNAME_EMPTY_ERROR = "config.rdbmsdbname.empty";
    public static final Object ACRE_RDBMSUSERID_EMPTY_ERROR = "config.rdbmsuserid.empty";
    public static final Object ACRE_RDBMSUSERPASSWORD_EMPTY_ERROR = "config.rdbmsuserpassword.empty";

    public static final Object ACRE_DEBUGLEVEL_EMPTY_ERROR = "config.debuglevel.empty";
    public static final Object ACRE_DEBUGLEVEL_INVALID_ERROR = "config.debuglevel.invalid";
    public static final Object ACRE_DEBUGON_EMPTY_ERROR = "config.debugon.empty";

    public static final Object ACRE_DBTYPE_UNKNOWN_ERROR = "config.dbtype.unknown";
    public static final Object ACRE_HOMEDIR_EMPTY_ERROR = "config.acrehome.empty";
    public static final Object ACRE_HOMEDIR_NOTEXIST_ERROR = "config.acrehome.notexist";
    public static final Object ACRE_HOMEDIR_NOTDIRECTORY_ERROR = "config.acrehome.notdir";


    public void addError(Object key, Object value) {
            put(key, value);
        }

        public Object getError(Object key) {
            return get(key);
        }

        public Enumeration getErrorKeys() {
                return this.keys();
        }

        public Enumeration getErrorValues() {
                return this.elements();
        }


        public String toString() {
            Iterator errs = this.values().iterator();
            StringBuffer buf = new StringBuffer();

            while (errs.hasNext()) {
                buf.append(errs.next());
                buf.append("\n");
            }

            return buf.toString();
        }
}
