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
package org.acre.visualizer.ui.codehighlight;

/**
 *  Simple PQL scanner
 * @author Yury Kamen
 */
public class PQLScanner extends JavaScanner {
    /**
     *  Initializes list of PQL keywords
     */
    protected void initSymbolTable() {
        setCaseInsensitiveKeywords(true);
        lookup(KEYWORD, "abs");
        lookup(KEYWORD, "alias");
        lookup(KEYWORD, "all");
        lookup(KEYWORD, "andthen");
        lookup(KEYWORD, "and");
        lookup(KEYWORD, "any");
        lookup(KEYWORD, "array");
        lookup(KEYWORD, "as");
        lookup(KEYWORD, "asc");
        lookup(KEYWORD, "avg");
        lookup(KEYWORD, "bag");
        lookup(KEYWORD, "boolean");
        lookup(KEYWORD, "by");
        lookup(KEYWORD, "count");
        lookup(KEYWORD, "char");
        lookup(KEYWORD, "date");
        lookup(KEYWORD, "define");
        lookup(KEYWORD, "desc");
        lookup(KEYWORD, "dictionary");
        lookup(KEYWORD, "distinct");
        lookup(KEYWORD, "double");
        lookup(KEYWORD, "element");
        lookup(KEYWORD, "enum");
        lookup(KEYWORD, "except");
        lookup(KEYWORD, "exists");
        lookup(KEYWORD, "false");
        lookup(KEYWORD, "first");
        lookup(KEYWORD, "flatten");
        lookup(KEYWORD, "float");
        lookup(KEYWORD, "for");
        lookup(KEYWORD, "from");
        lookup(KEYWORD, "group");
        lookup(KEYWORD, "having");
        lookup(KEYWORD, "include");
        lookup(KEYWORD, "instanceof");
        lookup(KEYWORD, "intersect");
        lookup(KEYWORD, "interval");
        lookup(KEYWORD, "in");
        lookup(KEYWORD, "is_defined");
        lookup(KEYWORD, "is_undefined");
        lookup(KEYWORD, "last");
        lookup(KEYWORD, "like");
        lookup(KEYWORD, "rlike");
        lookup(KEYWORD, "listtoset");
        lookup(KEYWORD, "list");
        lookup(KEYWORD, "long");
        lookup(KEYWORD, "max");
        lookup(KEYWORD, "mod");
        lookup(KEYWORD, "min");
        lookup(KEYWORD, "nil");
        lookup(KEYWORD, "not");
        lookup(KEYWORD, "null");
        lookup(KEYWORD, "octet");
        lookup(KEYWORD, "order");
        lookup(KEYWORD, "orelse");
        lookup(KEYWORD, "or");
        lookup(KEYWORD, "return");
        lookup(KEYWORD, "tquery");
        lookup(KEYWORD, "select");
        lookup(KEYWORD, "set");
        lookup(KEYWORD, "some");
        lookup(KEYWORD, "short");
        lookup(KEYWORD, "string");
        lookup(KEYWORD, "struct");
        lookup(KEYWORD, "sum");
        lookup(KEYWORD, "timestamp");
        lookup(KEYWORD, "time");
        lookup(KEYWORD, "true");
        lookup(KEYWORD, "undefined");
        lookup(KEYWORD, "undefine");
        lookup(KEYWORD, "union");
        lookup(KEYWORD, "unique");
        lookup(KEYWORD, "unsigned");
        lookup(KEYWORD, "where");
    }

//    /**
//     * Provides case-insensitive keyword matching
//     * @param type
//     * @param name
//     * @return
//     */
//    protected Symbol lookup(int type, String name) {
//        return super.lookup(type, name.toLowerCase());
//    }
}
