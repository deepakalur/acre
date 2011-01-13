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
package org.acre.extractor.java;

import com.sun.tools.javac.util.Convert;

/**
 * Contains Miscellaneous utility classes
 */
public class Utils {

    /**
     * Converts input String into equivalent MySQL string
     *
     * @param s input string
     * @return converted MySQL string
     */
    public static String quoteSQLString(String s) {
        if (null == s) {
            return "NULL";
        } else {
            return "'" + s.replace("'", "\\'") + "' ";
        }
    }

    /**
     * Converts input String into equivalent "Java source" string
     * @param s input string
     * @return converted "Java source" string
     */
    public static String quoteJavaString(String s) {
        return Convert.escapeUnicode(Convert.quote(s));
    }
}
