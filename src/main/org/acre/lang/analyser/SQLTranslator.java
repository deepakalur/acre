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
package org.acre.lang.analyser;

import org.acre.lang.runtime.DatabaseAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Translate Query Model to SQL String
 * 
 * @author Syed Ali
 */
public class SQLTranslator {

    private QueryModel sqlQuery;
    private int style;
    private final static int STYLE_NONE     = 0;
    private final static int STYLE_NESTED   = 2;
    private final static int STYLE_UNION    = 3;
    private boolean unioun;
    private String envVarFilter = null;

    public SQLTranslator(QueryModel sqlQuery) {
      this.sqlQuery = sqlQuery;
    }

    public List getSQLStatements() {
        List result = new ArrayList();
        DMLStatement statement;
        if (style == STYLE_NONE) {
            if (sqlQuery.assignVariable != null) {
                if (!DatabaseAdapter.USE_TEMP_TABLES) {
                    statement = new DMLStatement();
                    statement.sql = "DELETE FROM WriteVariable WHERE cookie = '"
                        +sqlQuery.assignVariable.getCookie()+"' AND scope = "+sqlQuery.assignVariable.getScope()
                        +" AND name = '"+sqlQuery.assignVariable.getName()+"'";
                    statement.executable = true;
                    result.add(statement);
                } else {
                    statement = new DMLStatement();
                    statement.sql = "DROP TEMPORARY TABLE IF EXISTS "+DatabaseAdapter.getTemporaryTableName(sqlQuery.assignVariable);
                    statement.executable = true;
                    result.add(statement);
                }
            }
            statement = new DMLStatement();
            statement.sql = getSQLString();
            statement.executable = sqlQuery.assignVariable != null;
            result.add(statement);
        }
        if (sqlQuery.assignVariable != null) {
            if (!DatabaseAdapter.USE_TEMP_TABLES) {
                statement = new DMLStatement();
                statement.sql = "DELETE FROM ReadVariable WHERE cookie = '"
                    +sqlQuery.assignVariable.getCookie()+"' AND scope = "+sqlQuery.assignVariable.getScope()
                    +" AND name = '"+sqlQuery.assignVariable.getName()+"'";
                statement.executable = true;
                result.add(statement);
                statement = new DMLStatement();
                statement.sql = "INSERT INTO ReadVariable SELECT * FROM WriteVariable  WHERE cookie = '"+sqlQuery.assignVariable.getCookie()+"' AND scope = "+sqlQuery.assignVariable.getScope()+" AND name = '"+sqlQuery.assignVariable.getName()+"'";
                statement.executable = true;
                result.add(statement);
            } else {
                statement = new DMLStatement();
                statement.sql = "DROP TEMPORARY TABLE IF EXISTS "+DatabaseAdapter.getReadTemporaryTableName(sqlQuery.assignVariable);
                statement.executable = true;
                result.add(statement);
                statement = new DMLStatement();
                statement.sql = "CREATE TEMPORARY TABLE "+DatabaseAdapter.getReadTemporaryTableName(sqlQuery.assignVariable)
                    +" AS SELECT * FROM "+ DatabaseAdapter.getTemporaryTableName(sqlQuery.assignVariable);
                statement.executable = true;
                result.add(statement);
            }
        } 
        return result;
    }
    
    public String getSQLString() {
        if (sqlQuery.getSelectExpressions().size() > 10) {
            if (sqlQuery.assignVariable != null) {
                throw new IllegalArgumentException("Cannot handle projection of more 10 values for variable " + sqlQuery.assignVariable.getName());
            }
        }
        StringBuffer buffer = new StringBuffer();
        if (sqlQuery.assignVariable != null) {
            if (style == STYLE_NONE) {
                if (!DatabaseAdapter.USE_TEMP_TABLES) {
                    //buffer.append("INSERT INTO WriteVariable(cookie, scope, name, varIndex, nodeId, nodeName) ");
                    buffer.append("INSERT INTO WriteVariable(cookie, scope, name, varIndex,");
                    for (int i = 0; i < sqlQuery.getSelectExpressions().size(); i++) {
                        Expression e = ((Expression) sqlQuery.getSelectExpressions().get(i));
                        if (e.getFunction() == null && !e.isLiteral() && !e.isAttribute()) {
                            buffer.append(" nodeId"+i+",");
                        }
                        buffer.append(" nodeName"+i+",");
                    }
                    buffer.deleteCharAt(buffer.length() - 1).append(") ");
                } else {
                    buffer.append("CREATE TEMPORARY TABLE ").append(DatabaseAdapter.getTemporaryTableName(sqlQuery.assignVariable) + " AS ");
                }
            }
            if (!DatabaseAdapter.USE_TEMP_TABLES) {
                buffer.append("SELECT DISTINCT '"+sqlQuery.assignVariable.getCookie()+"', "+sqlQuery.assignVariable.getScope()+", '"+sqlQuery.assignVariable.getName()+"', 0, ");
            } else {
                buffer.append("SELECT DISTINCT ");
            }
        } else {
            buffer.append("SELECT DISTINCT ");
        }
        int index = 1;
        for (Iterator iterator = sqlQuery.getSelectExpressions().iterator(); iterator.hasNext();) {
            Expression e = (Expression) iterator.next();
            if (false) {
            } else if (e.isAttribute()) {
                buffer.append(getSQLString(e, index));
            } else if (e.isLiteral()) {
                buffer.append(getSQLString(e, index));
            } else if (e.isObject()) {
                buffer.append(getSQLString(e, index));
            }
            if (iterator.hasNext())
                buffer.append(", ");
            index++;
        }
        buffer.append(" FROM ");
        for (Iterator iterator = sqlQuery.getFromExpressions().values().iterator(); iterator.hasNext();) {
            Expression e = (Expression) iterator.next();
            buffer.append(e.getType()).append(" ").append(e.getAlias());
            if (iterator.hasNext())
                buffer.append(", ");
        }
        if (sqlQuery.getWhereConditions().size() > 0) {
            buffer.append(" WHERE ( ");
            for (Iterator iterator = sqlQuery.getWhereConditions().iterator(); iterator.hasNext();) {
                Condition c = (Condition) iterator.next();
                buffer.append(getSQLString(c));
            }
            buffer.append(" ) ");
        } 
        appendEnvironmentVariableFilter(buffer, sqlQuery.getWhereConditions().size() > 0);
        if (sqlQuery.getOrderByExpressions().size() > 0) {
            buffer.append(" ORDER BY ");
            for (Iterator iterator = sqlQuery.getOrderByExpressions().iterator(); iterator.hasNext();) {
                QueryModel.OrderByStruct e = (QueryModel.OrderByStruct) iterator.next();
                buffer.append(getSQLString(e.getExpression()));
                if (e.isDescending()) {
                    buffer.append(" DESC ");
                } else {    
                    buffer.append(" ASC ");
                }
                if (iterator.hasNext())
                    buffer.append(", ");
            }
        } 
        if (sqlQuery.getUnion() != null) {
            SQLTranslator uniounst = new SQLTranslator(sqlQuery.getUnion());
            uniounst.setUniounStyle();
            buffer.append(" UNION ").append(uniounst.getSQLString());
        }
        return buffer.toString();
    }

    private void appendEnvironmentVariableFilter(StringBuffer buffer, boolean andFilter) {
        if (envVarFilter != null) {
            String filter = getEnvVarWhereClause();
            if (filter != null && filter.length() > 0) {
                if (andFilter) {
                    buffer.append(" AND (");
                } else{
                    buffer.append(" WHERE (");
                }
                buffer.append(filter);
                buffer.append(" )");
            }
        }
    }

    private String getEnvVarWhereClause() {
        StringBuffer buffer = new StringBuffer();
        for (Iterator iterator = sqlQuery.getFromExpressions().values().iterator(); iterator.hasNext();) {
            Expression e = (Expression) iterator.next();
            //Omit variable temp tables and relationship traversals
            if (e.isVariable() || e.getPath().indexOf('.') > -1) {
                continue;
            }
            if (buffer.length() > 0) {
                buffer.append(" AND ");
            }

            buffer.append(e.getAlias()).append(".salsaProjectId = ")
                .append(envVarFilter)
            ;
        }
        return buffer.toString();
    }
    private String getSQLString(Condition c) {
        String operator = c.getOperator();
        if ("INSTANCEOF".equalsIgnoreCase(operator)) {
            notYetImplemented("instanceof operator yet");
        }
        if (c.isLeftCondition()) {
            return "(" + getSQLString((Condition) c.getLeft()) + " " + operator+ " " + getSQLString((Condition) c.getRight()) + ")";
        } 
        boolean bracket = "IN".equals(operator) || "NOT IN".equals(operator);
        StringBuffer result = new StringBuffer("(");
        if (c.isLeftExpression()) {
            result.append(getSQLString((Expression) c.getLeft()));
        }
        result.append(' ').append(operator).append(' ');
        if (c.isRightExpression()) {
            if (bracket)
                result.append('(');
            result.append(getSQLString((Expression) c.getRight()));
            if (bracket)
                result.append(')');
        }
        result.append(")");
        return result.toString();
    }
    
    private String getSQLString(Expression e) {
        return getSQLString(e, -1);
    }
    
    private String getSQLString(Expression e, int project) {
        if (e.isQuery()) {
            SQLTranslator st = new SQLTranslator(e.getQuery());
            st.setNestedStyle();
            return st.getSQLString();
        }
        StringBuffer result = new StringBuffer();
        if (project > 0 && e.getFunction() != null) {
            result.append(e.getFunction()+"(");
        }
        int lastCharPOS = e.getPath().length() - 1;
        if (false) {
        } else if (e.isNilLiteral()) {
            result.append("NULL");
        } else if (e.isLiteral()  && "String".equals(e.getType()) && e.getPath().charAt(0) == '"' && e.getPath().charAt(lastCharPOS) == '"') {
            result.append(e.getPath().replace('"', '\''));
        } else if (e.isLiteral() && "Boolean".equals(e.getType()) && e.getPath().charAt(0) == '"' && e.getPath().charAt(lastCharPOS) == '"') {
            result.append(e.getPath());
            result.deleteCharAt(0);
            result.deleteCharAt(lastCharPOS - 1);
        } else if (e.isObject()) {
            if (project > 0 && style != STYLE_NESTED && e.getFunction() == null) {
                result.append(e.getPath())
                    .append(".id" + " __id" +project).append(", ")
                    .append(e.getPath()).append(".name" + " __name" +project);
            } else {
                result.append(e.getPath()).append(".id");
            }
        } else {
            result.append(e.getPath());
        }
        if (project > 0 && e.getFunction() != null) {
            result.append(")");
        }
        // provide alias only for non objects
        if (project > 0 && e.getAlias() != null && !e.isObject()) {
            result.append(" " + e.getAlias());
        }
        return result.toString();
    }

    private String getColumnAlias(String path) {
        return path.replace('.', '_');
    }

    public static void notYetImplemented(String message) {
        throw new NotYetImplementedException("SQL Translator cannot handle : " + message);
    }

    public void unsetStyle() {
        this.style = STYLE_NONE;
    }
    public void setNestedStyle() {
        this.style = STYLE_NESTED;
    }
    public void setUniounStyle() {
        this.style = STYLE_UNION;
    }

    public void setEnvironmentVariableFilter(String value) {
        envVarFilter = value;
    }
}
