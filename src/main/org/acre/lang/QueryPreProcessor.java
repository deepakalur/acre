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
package org.acre.lang;

import org.acre.common.AcreStringUtil;
import org.acre.config.ConfigService;
import org.acre.dao.DAOFactory;
import org.acre.lang.pql.pdbc.PQLException;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author rajmohan@Sun.com
 */
public class QueryPreProcessor {
    private static final String SYNTAX_ERROR = "Syntax Error in Define Statement : ";

    // global scope
    private static Map<String, String> macrosMap = new HashMap<String, String>();

    private static Logger logger = ConfigService.getInstance().getLogger(QueryPreProcessor.class);

    // PQL syntax constants
    private static final String AS = "as";
    private static final String STMT_DELIM = ";";
    private static final String HASH = "#";
    private static final String DEFINE = "define";
    private static final String DEFINE_KEYWORD = HASH + DEFINE;
    private static final String DOLLAR = "$";
    private static final String INCLUDE = "include";

    public String compile(String pql) throws PQLException {

        StringBuffer sb = new StringBuffer(pql.length() + 1024);

        pql = stripComments(pql);

        // split buffer into PQL statements delimited by ';'
        Scanner scanner = new Scanner(pql);

        scanner.useDelimiter(STMT_DELIM);
        while ( scanner.hasNext()) {
            String statement = scanner.next();
            if ( statement != null ) {
                if (isDefineStatement(statement) ) {
                    Macro macro = processDefineStatement(statement);
                    macrosMap.put(macro.name.toLowerCase(), macro.value);
                }
                else if ( isIncludeStatement(statement) ) {
                    sb.append(statement + ";");
                    preProcessInclude(statement);
                }
                else if ( !AcreStringUtil.isEmpty(statement)) {
                    sb.append(statement + ";");
                }
            }
        }

        String processedQuery = expandMacros(sb.toString(), macrosMap);
        return processedQuery;
    }

    // #define
    private boolean isDefineStatement(String statement) {
        return statement.trim().startsWith(HASH);
    }

    // include "query"
    private void preProcessInclude(String statement) throws PQLException {
        String tokens[] = statement.split("\\s+");

        if ( tokens.length < 2)
            return; //no-op

        String scriptName = stripQuote(tokens[1]);
        System.out.println("script name : " + scriptName);
        String iScript = DAOFactory.getPatternQueryRepository().getGlobalQueryFile(scriptName);

        compile(iScript);
    }

    private boolean isIncludeStatement(String statement) {
        return (statement.toLowerCase().startsWith(INCLUDE));
    }

    // syntax : #define type "valueText"
    private Macro processDefineStatement(String statement) throws PQLException {

        Scanner scanner = new Scanner(statement);
        scanner.useDelimiter("\\s+");

        if ( !isDefineKeyword(scanner) )
            throw new PQLException(SYNTAX_ERROR + statement);

        if ( !hasToken(scanner)) {
            throw new PQLException(SYNTAX_ERROR + statement);
        }

        String macroName = scanner.next();
        if ( macroName.startsWith(DOLLAR) )
            macroName = macroName.substring(1);

        if ( !hasToken(scanner)) {
            throw new PQLException(SYNTAX_ERROR + statement);
        }

        String macroValue;
        String token = scanner.next();
        if ( AS.equalsIgnoreCase(token))
            macroValue = scanner.next();
        else
            macroValue = token;

        macroValue = stripQuote(macroValue);

        return new Macro(macroName, macroValue);
    }

    private String stripQuote(String str) {
        str = str.trim();

        int start = 0, end = str.length();
        if ( str.startsWith("\"") || str.startsWith("'") )
            start++;

        if ( str.endsWith("\"") || str.endsWith("'") )
            end--;


        return str.substring(start, end);
    }

    private boolean isDefineKeyword(Scanner scanner) {
        if ( !hasToken(scanner)) {
            return false;
        }

        String token1 = scanner.next().toLowerCase();

        if ( DEFINE_KEYWORD.equalsIgnoreCase(token1) )
            return true;

        String token2 = scanner.next();

        if ( HASH.equalsIgnoreCase(token1) && DEFINE.equalsIgnoreCase(token2))
            return true;

        return false;
    }

    String expandMacros(String str, Map<String, String> macros) throws PQLException {

        if ( AcreStringUtil.isEmpty(str))
            return str;

        StringBuffer result = new StringBuffer(str.length() * 2);
        Pattern pattern = Pattern.compile("\\$\\w+\\b");
        Matcher matcher = pattern.matcher(str);

        while ( matcher.find()) {
            String macro = matcher.group().substring(1); // ignoring the leading $

            String value = macros.get(macro.toLowerCase());

            if ( value != null )
                matcher.appendReplacement(result, value);

//                  TODO - unknown-macro error handling
//                  i.e., select...from....where like "$..." is valid non-macro usage
//                throw new PQLException("Undefined Macro : " + macro +
//                        " must be defined using #define " + macro + " as .... syntax");

        }
        matcher.appendTail(result);


        return result.toString();
    }

    private boolean hasToken(Scanner scanner) {
        return scanner.hasNext();
    }


    String stripComments(String query) {
        // replace single & multi-line comments
        return query.replaceAll("//.*", "").replaceAll("/\\*.*\\*/", "");
    }

    private class Macro {
        public Macro(String name, String value) {
            this.name = name;
            this.value = value;
        }

        String name;
        String value;
    }

    static void clearMarcos() {
        macrosMap.clear();
    }
}
