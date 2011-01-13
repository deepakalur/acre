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
package org.acre.lang.pql.pdbc;

import java.util.Properties;

/**
 * @author Deepak.Alur@Sun.com
 *         Date: Sep 27, 2004
 *         Time: 2:46:52 PM
 */
public class PQLConnectionProperties extends Properties{
    public static final String FACTDB_DRIVER = "FACTDB_DRIVER";
    public static final String FACTDB_USERID = "FACTDB_USERID";
    public static final String FACTDB_PASSWORD = "FACTDB_PASSWORD";
    public static final String FACTDB_URL = "FACTDB_URL";
    private static final String FACTDB_TYPE = "FACTDB_TYPE";
    private static final String FACTDB_TYPE_MYSQL = "MYSQL";
    private static final String FACTDB_TYPE_QL = "QL";

    public String getFactDBUrl() {
        return (String) get(FACTDB_URL);
    }

    public void setFactDBUrl(String factDBurl) {
        put(FACTDB_URL, factDBurl);
    }

    public String getFactDBUserId() {
        return (String) get(FACTDB_USERID);
    }

    public void setFactDBUserId(String userid) {
        put (FACTDB_USERID, userid);
    }

    public String getFactDBPassword() {
        return (String) get(FACTDB_PASSWORD);
    }

    public void setFactDBPassword(String password) {
        put (FACTDB_PASSWORD, password);
    }

    public String getFactDBDriver() {
        return (String) get(FACTDB_DRIVER);
    }

    public void setFactDBDriver(String driver) {
        put (FACTDB_DRIVER, driver);
    }

    public String getFactDBType() {
        return (String) get(FACTDB_TYPE);
    }

    public boolean isDBTypeSQL() {
        if (getFactDBType().equals(FACTDB_TYPE_MYSQL))
            return true;
        else
            return false;
    }

    public boolean isDBTypeQL() {
        if (getFactDBType().equals(FACTDB_TYPE_QL))
            return true;
        else
            return false;
    }

    public void setFactDBTypeToSQL() {
        setFactDBType(FACTDB_TYPE_MYSQL);
    }

    public void setFactDBTypeToQL() {
        setFactDBType(FACTDB_TYPE_QL);
    }

    private void setFactDBType(String type) {
        if (type != null) {
            if (type.equals(FACTDB_TYPE_MYSQL)) {
                put (FACTDB_TYPE, FACTDB_TYPE_MYSQL);
            } else if (type.equals(FACTDB_TYPE_QL)) {
                put (FACTDB_TYPE, FACTDB_TYPE_QL);
            }
        } else {
            put (FACTDB_TYPE, FACTDB_TYPE_MYSQL);
        }
    }
}
