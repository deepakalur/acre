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
package org.acre.dao;

import org.acre.common.AcreErrors;

/**
 * @author Deepak.Alur@Sun.com
 * @version Nov 6, 2004 5:37:23 PM
 */
public class PQLXMLErrors extends AcreErrors {
    public static final String QUERY_RETURN_VARIABLE_INVALID="pdmquery.returnvariable.invalid";
    public static final String QUERY_EXISTS = "pdmquery.query.exists";
    public static final String QUERY_NAME_INVALID = "pdmquery.query.name.invalid";
    public static final String QUERY_TYPE_INVALID = "pdmquery.query.type.invalid";
    public static final String QUERY_RELATIVEFILEPATH_INVALID = "pdmquery.query.relativefilepath.invalid";
    public static final String QUERY_LANGUAGE_INVALID = "pdmquery.query.language.invalid";
}
