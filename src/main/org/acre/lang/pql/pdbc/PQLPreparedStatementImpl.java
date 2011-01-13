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


import org.acre.lang.TreeBuilder;
import org.acre.lang.analyser.ScriptModel;
import org.acre.lang.analyser.ScriptWalker;
import org.acre.lang.lexer.LexerException;
import org.acre.lang.node.Node;
import org.acre.lang.parser.ParserException;

import java.util.*;

/**
 * @author Deepak.Alur@Sun.com
 *         Date: Sep 27, 2004
 *         Time: 2:35:33 PM
 */
public class PQLPreparedStatementImpl extends PQLStatementImpl
        implements PQLPreparedStatement {

    HashMap parameters;
    String query;
    ScriptModel scriptModel;

    public PQLPreparedStatementImpl(PQLConnection connection, String queryStmt)
            throws PQLException {
        super(connection);
        this.query = queryStmt;
        parameters = PDBCUtil.extractParameters(query);
    }

    public void setParameter(String name, Object parameter) {
        parameters.put(PDBCUtil.PARAMETER_PREFIX + name, parameter);
    }

    public void setParameter(String name, int value) throws PQLParameterException {
        setParameter(name, new Integer(value));
    }

    public void setParameter(String name, float value) throws PQLParameterException {
        setParameter(name, new Float(value));
    }

    public void setParameter(String name, boolean value) throws PQLParameterException {
        setParameter(name, Boolean.valueOf(value));
    }

    public void setParameter(String name, Collection values) throws PQLParameterException {
        parameters.put(PDBCUtil.PARAMETER_PREFIX + name, values);
    }

    public void setParameter(String name, PQLResultSet result, String columnName) throws PQLParameterException {
        int columnIndex = result.getMetaData().getColumnIndex(columnName);
        if (columnIndex == -1) {
            throw new PQLParameterException("No such column '" + columnName + "'in PQLResultSet '" + result +"'");
        }
        setParameter(name, result, columnIndex);
    }

    public void setParameter(String name, PQLResultSet result, int columnId) throws PQLParameterException {
        PQLColumnHandle handle = result.getMetaData().getColumnHandle(columnId);
        parameters.put(PDBCUtil.PARAMETER_PREFIX + name, handle);
    }

    public Object getParameter(String name) {
        return parameters.get(PDBCUtil.PARAMETER_PREFIX + name);
    }

    public void clearParameters() {
        parameters.clear();
    }

    public String[] getReturnVariableNames() throws PQLException {
        if ( scriptModel == null )
            scriptModel = parseQuery(query);

        Set returnVariables = scriptModel.getReturnVariables();
        String [] names = new String[returnVariables.size()];
        returnVariables.toArray(names);
        return names;
    }

    private ScriptModel parseQuery(String query) throws PQLException {
        Node ast = null;
        try {
            ast = TreeBuilder.getNode(query, true);
        } catch (Exception e) {
            throw new PQLException("Error parsing query : " +
                    e.getMessage(), e);
        }
        ScriptModel script = new ScriptModel();
        ScriptWalker qw = new ScriptWalker(script, true);
        ast.apply(qw);
        return script;
    }

    public String getProcessedQuery() throws PQLParameterException {
        String processedQuery = PDBCUtil.substituteParameters(query, parameters);
        return processedQuery;
    }

    public Map executeQuery() throws PQLException{
        validateParameters();

        return connection.executePQL(getProcessedQuery());
    }

    private void validateParameters() throws PQLParameterException {

        if (PDBCUtil.isEmpty(query)) {
            throw new PQLParameterException("Query Statement cannot be null or empty");
        }

        if ((parameters == null) || (parameters.isEmpty())) {
            // todo - if no parameteres in this query, is this a static query. VALID
        }

        PQLErrors errors = new PQLErrors();
        Iterator iter = parameters.keySet().iterator();
        while (iter.hasNext()) {
            Object key = iter.next();
            Object value = parameters.get(key);
            if (value == null) {
                errors.addError(PQLErrors.PQL_PDBC_NOPARAMETERVALUE, "Parameter is null for '"
                        + key
                        + "'");
            }
        }

        if (errors.isEmpty())
            return;
        else
            throw new PQLParameterException(errors, "Some or all Parameter Values not supplied for prepared statement");
    }

    public HashMap getParameters() {
        return parameters;
    }

    public String toString() {
        StringBuffer ret = new StringBuffer();
        ret.append("PQLPreparedStatementImpl{")
                .append("parameters= {").append(parameters).append("}")
                .append(", query='").append(query).append("'") ;
        try {
             ret.append(", processedQuery='").append(getProcessedQuery()).append("'") ;

        } catch (PQLParameterException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        ret.append("}");
        return ret.toString();
    }

    public static void main(String args[]) {
        try {
            PQLPreparedStatementImpl impl = new PQLPreparedStatementImpl(null,
                    "select c from classes c " +
                    "where c.methods.name in (:salsaclasses ) and " +
                    "c.parentPackage.isPublic = :salsapack " +
                    "and c.methods in :cam " +
                    "and c in :cla " +
                    "and c in :blah"
                    );
            ArrayList classes = new ArrayList();
            classes.add("com.sun.salsa.ClassA");
            classes.add("com.sun.salsa.classB");
            impl.setParameter("salsaclasses", classes);
            impl.setParameter("cam", 8);
            impl.setParameter("cla", 5.4f);
            ArrayList valueSet = new ArrayList();
            valueSet.add(new Integer(10));
            valueSet.add(new Float(44.5f));
            valueSet.add(Boolean.valueOf(true));
            valueSet.add("Hello 'World' \"How are you\"");
            impl.setParameter("salsapack", true);
            impl.setParameter("blah", valueSet);

            System.out.println(impl);

            impl.validateParameters();
        } catch (PQLException e) {
            System.out.println("Errors:" + e.getPQLErrorMessages());
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
