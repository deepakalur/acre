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

import org.acre.pdmqueries.ArgumentType;
import org.acre.pdmqueries.QueryType;
import org.acre.pdmqueries.ReturnVariableType;
import org.acre.common.AcreErrors;
import org.acre.common.AcreRuntimeException;
import org.acre.common.AcreStringUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Deepak.Alur@Sun.com
 * @version Nov 4, 2004 2:46:45 PM
 */
public class PDMQueryValidator {

    private static PDMQueryValidator ourInstance = new PDMQueryValidator();
    private final ArrayList queryTypes = new ArrayList();
    private final ArrayList queryLanguages = new ArrayList();
    private final ArrayList queryReturnVariableTypes = new ArrayList();
    private final ArrayList queryArgumentTypes = new ArrayList();

    private PDMQueryValidator() {
        initValidValues();
    }

    public static PDMQueryValidator getInstance() {
            return ourInstance;
    }

    private void initValidValues() {
        queryTypes.add(PDMXMLConstants.QUERY_TYPE_STATIC);
        queryTypes.add(PDMXMLConstants.QUERY_TYPE_DYNAMIC);
        
        queryLanguages.add(PDMXMLConstants.QUERY_LANGUAGE_PQL);
        queryLanguages.add(PDMXMLConstants.QUERY_LANGUAGE_GROOVY);

        queryReturnVariableTypes.add(PDMXMLConstants.QUERY_RETURN_VARIABLE_TYPE_ANY);

        queryArgumentTypes.add(PDMXMLConstants.QUERY_ARGUMENT_TYPE_STATIC);
        queryArgumentTypes.add(PDMXMLConstants.QUERY_ARGUMENT_TYPE_RETURN);
        queryArgumentTypes.add(PDMXMLConstants.QUERY_ARGUMENT_TYPE_DYNAMIC);
    }

    public AcreErrors validateQuery(QueryType query) {
        return validateQuery(
                query.getName(),
                query.getType(),
                query.getLanguage(),
                query.getDescription(),
                query.getRelativeFilePath(),
                query.getArgument(),
                query.getReturnVariable());
    }

    public AcreErrors validateQuery(String name, String type,
                                String language, String description,
                                String relativeFilePath,
                                List arguments,
                                List returnVariables) {

        AcreErrors errors = new AcreErrors();

        if (AcreStringUtil.isEmpty(name))
            errors.addError(PQLXMLErrors.QUERY_NAME_INVALID, "Query Name cannot be empty or null");

        if (AcreStringUtil.isEmpty(type))
            errors.addError(PQLXMLErrors.QUERY_TYPE_INVALID, "Query Type cannot be empty or null");

        if (AcreStringUtil.isEmpty(relativeFilePath))
            errors.addError(PQLXMLErrors.QUERY_RELATIVEFILEPATH_INVALID, "Query Relative File Path cannot be empty or null");

        if (AcreStringUtil.isEmpty(language))
            errors.addError(PQLXMLErrors.QUERY_LANGUAGE_INVALID, "Query Language cannot be empty or null");

        // validate arguments
        errors.addAcreErrors(validateArguments(arguments));

        // validate return variables
        errors.addAcreErrors(validateReturnVariables(returnVariables));

        return errors;
    }

    public AcreErrors validateArguments(List args) {
        AcreErrors errors = new AcreErrors();
        if ((args == null) || (args.size() == 0))
            return errors;

        for (int i=0; i < args.size(); i++ ) {
            ArgumentType arg = (ArgumentType) args.get(i);
            errors.addAcreErrors(validateQueryArgument(arg));
        }

        return errors;
    }

    public AcreErrors validateReturnVariables(List vars) {
        AcreErrors errors = new AcreErrors();
        if ((vars == null) || (vars.size() == 0))
            return errors;

        for (int i=0; i < vars.size(); i++ ) {
            ReturnVariableType var = (ReturnVariableType) vars.get(i);
            errors.addAcreErrors(validateReturnVariable(var));
        }

        return errors;
    }


    public AcreErrors validateQueryArgument(String seq,
                                    String name,
                                    String type, Serializable value,
                                    String description, String relatedQueryName,
                                    String relatedQueryVariableName) {
        // todo valiation
        AcreErrors errors = new AcreErrors();

        return errors;
        // if invalid, throw PDMException
    }

    public AcreErrors validateQueryArgument(ArgumentType argumentType) {

        AcreErrors errors = new AcreErrors();

        // todo validation
//        if (!(argumentType instanceof org.acre.pdmqueries.ArgumentType))
//            throw new PDMException("Invalid argument type:" + argumentType);

        org.acre.pdmqueries.ArgumentType arg = argumentType;
//                (org.acre.pdmqueries.ArgumentType) argumentType;

        errors.addAcreErrors(
                validateQueryArgument(arg.getSequence().toString(),
                arg.getName(), arg.getType(), arg.getValue(),
                arg.getDescription(), arg.getRelatedQueryName(),
                arg.getRelatedQueryReturnVariableName())
        );

        return errors;
    }


    public AcreErrors validateReturnVariable(String name, String type, String description) {

        AcreErrors errors = new AcreErrors();

        if (! queryReturnVariableTypes.contains(type)) {
            errors.addError(PQLXMLErrors.QUERY_RETURN_VARIABLE_INVALID,
                    "Return Variable Type is invalid");
        }

        return errors;
    }

    public AcreErrors validateReturnVariable(ReturnVariableType var) {
         return validateReturnVariable(var.getName(), var.getType(),
                var.getDescription());
    }

    public Object [] getQueryTypes() {
        return queryTypes.toArray();
    }
    
    public Object[] getQueryLanguages() {
        return queryLanguages.toArray();
    }

    public Object[] getQueryReturnVariableTypes() {
        return queryReturnVariableTypes.toArray();
    }

    public static void main( String args[]) {
        PDMQueryValidator v = PDMQueryValidator.getInstance();
        Object [] t = v.getQueryTypes();
        Object [] r = v.getQueryReturnVariableTypes();
        Object [] l = v.getQueryLanguages();

    }

    public Object[] getQueryArgumentTypes() {
        return queryArgumentTypes.toArray();
    }

    public static boolean argumentExists(QueryType query, String argName) {

        if (argName == null)
            throw new AcreRuntimeException("No argument supplied to check duplicate.");

        if ((query.getArgument() == null) || (query.getArgument().size() == 0)) {
            return false;
        }

        for (int i = 0 ; i < query.getArgument().size(); i++) {
            ArgumentType arg = (ArgumentType) query.getArgument().get(i);
            if (arg.getName().equalsIgnoreCase(argName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean returnVariableExists(QueryType query, String returnVariableName) {
        if (returnVariableName == null)
            throw new AcreRuntimeException("No Return Variable supplied to check duplicate.");

        if ((query.getReturnVariable() == null) || (query.getReturnVariable().size() == 0)) {
            return false;
        }

        for (int i = 0 ; i < query.getReturnVariable().size(); i++) {
            ReturnVariableType var = (ReturnVariableType) query.getReturnVariable().get(i);
            if (var.getName().equalsIgnoreCase(returnVariableName)) {
                return true;
            }
        }
        return false;

    }

    public static void deleteArgumentFromQuery(QueryType query, String argName) {
        if (AcreStringUtil.isEmpty(argName))
            return;
        if ((query.getArgument() == null) || (query.getArgument().size() == 0))
            return;

        for (int i=0; i < query.getArgument().size(); i++) {
            ArgumentType arg = (ArgumentType) query.getArgument().get(i);
            if (arg.getName().equalsIgnoreCase(argName)) {
                query.getArgument().remove(i);
                break;
            }
        }
    }

    public static void deleteReturnVariableFromQuery(QueryType query, String varName) {
        if (AcreStringUtil.isEmpty(varName))
            return;

        if ((query.getReturnVariable() == null) || (query.getReturnVariable().size() == 0))
            return;

        for (int i=0; i < query.getReturnVariable().size(); i++) {
            ReturnVariableType var = (ReturnVariableType) query.getReturnVariable().get(i);
            if (var.getName().equalsIgnoreCase(varName)) {
                query.getReturnVariable().remove(i);
                break;
            }
        }
    }
}
