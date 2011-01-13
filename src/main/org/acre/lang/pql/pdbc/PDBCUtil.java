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

import java.util.*;

/**
 * @author Deepak.Alur@Sun.com
 *         Date: Oct 14, 2004
 *         Time: 4:26:28 PM
 */
public class PDBCUtil {
    public static final String PARAMETER_PREFIX = ":";
    public static final String ROUND_BRACKET_START = "(";
    public static final String ROUND_BRACKET_END = ")";
    private static final String COMMA = ", ";


    public static boolean isEmpty(String str) {
        if (str == null) return true;
        if (str.trim().length() == 0) return true;

        return false;
    }

    public static HashMap extractParameters(String query) throws PQLException {
        HashMap params = new HashMap();

        if (isEmpty(query)) {
            throw new PQLException ("Prepared Statement query cannot be null or empty");
        }

        StringTokenizer tokenizer;
        tokenizer = new StringTokenizer(query);
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (token.startsWith(PARAMETER_PREFIX)) {
                System.out.println("Got Parameter '" + token +"'");
                params.put(token, null);
            } else if (token.startsWith(ROUND_BRACKET_START)) {
                StringTokenizer inTok = new StringTokenizer(token, ROUND_BRACKET_START);
                while (inTok.hasMoreTokens()) {
                    String insideToken = inTok.nextToken();
                    System.out.println("Got Inside Parameter '" + insideToken +"'");
                    params.put(insideToken, null);
                }
            }
        }

        return params;
    }

    public static String substituteParameters(String query, HashMap parameters) throws PQLParameterException {
       for (Iterator iter = parameters.entrySet().iterator(); iter.hasNext();) {
           Map.Entry element = (Map.Entry) iter.next();
           String parameterName = (String) element.getKey();
           Object parameterValue = element.getValue();
           System.out.println("Replacing  '" + parameterName + "' with '" + parameterValue + "'");

           //todo - escape param values
           String paramValueString;
           if (parameterValue == null) {
                paramValueString = getNullValue(parameterValue);
           } else {
               // todo - look at the value type and figure how to construct the values string
                paramValueString = getParameterString(parameterValue);
                query = query.replaceAll(parameterName, paramValueString);
           }
       }
       return query;
    }

    private static String getParameterString(Object parameterValue)
            throws PQLParameterException {

        if (parameterValue instanceof Collection) {
            Collection values = (Collection) parameterValue;
            StringBuffer parStr = new StringBuffer();
            if (values.isEmpty())
                return "";

            Iterator valueIter = values.iterator();
            while (valueIter.hasNext()) {
                parStr.append(getStringValue(valueIter.next()));
                if (valueIter.hasNext()) parStr.append(COMMA);
            }
            return parStr.toString();
        } else {
            return getStringValue(parameterValue);
        }
    }

    private static String getNullValue(Object value) {
        return "null";
    }

    private static String getStringValue(Object value) throws PQLParameterException {
        if (value instanceof String) {
            return (String) escapeString((String) "\"" + value + "\"");
        }
        else if (value instanceof Integer) {
            return ((Integer) value).toString();
        }
        else if (value instanceof Boolean) {
            return ((Boolean) value).toString();
        }
        else if (value instanceof Float) {
            return ((Float)value).toString();
        } else if (value instanceof PQLColumnHandle) {
            return ((PQLColumnHandle) value).getHandleId();
        }
        else {
            throw new PQLParameterException("Unknown Value = '"
                + value
                + "' Type = '"
                + value.getClass().getName()
                + "'");
        }
    }

    private static String escapeString(String value) {
        // todo escape value
        return value;
    }

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
            return "'" + s.replaceAll("'", "\\'") + "' ";
        }
    }

}
