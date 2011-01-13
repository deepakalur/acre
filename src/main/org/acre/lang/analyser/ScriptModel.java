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

import org.acre.lang.node.*;
import org.acre.model.metamodel.FactMetaModel;
import org.acre.model.metamodel.MetaAttribute;
import org.acre.model.metamodel.MetaRelationship;
import org.acre.model.metamodel.MetaType;

import java.util.*;

/**
 * Metamodel for preQL scripts
 * 
 * @author Syed Ali
 */
public class ScriptModel {

    
    private static final String PQL_TEMP_PREFIX = "ttt_pql_temp_variable_";
    public static final String DEFAULT_RETURN_VARIABLE = "result";
    public static final String COOKIE_PREFIX = "session_cookie_000";
    public static final int COOKIE_PREFIX_LEN = COOKIE_PREFIX.length();
    public static final int SCOPE_SESSION = -1;
    private static Map envVarNames = null;
    static {
        initEnvironmentVariableNames();
    }

    private int ctr1;
    private String cookie;
    private int scope = 0;

    private Properties environmentVariables;
    private List variables = new ArrayList();
    private Map variableMap = new HashMap();
    private List commands = new ArrayList();
    private boolean targetSQL;
    private int includeLevel = 0;
    //following variables need not be used in included states
    private Set returnVariables = new HashSet();
    private Set savedVariables = new HashSet();
    

    public ScriptModel() {
    }
    
    public String getCookie() {
        return cookie;
    }
    public void setCookie(int value) {
      StringBuffer buffer = new StringBuffer(COOKIE_PREFIX + value);
      if (buffer.length() > COOKIE_PREFIX_LEN + 1) {
          buffer.delete(COOKIE_PREFIX_LEN - 3, buffer.length() - 4);
      }
      cookie = buffer.toString();
    }
    public int getScope() {
        return scope;
    }

    public void pushScope() {
//        return ++scope;
    }

    public void popScope() {
//        return --scope;
    }

    
    public List getCommands() {
        return commands;
    }
    public void addCommand(String command) {
        commands.add(command);
    }
    public void addCommand(Properties command) {
        commands.add(command);
    }
    public void addCommand(QueryModel command) {
        commands.add(command);
    }
    public Expression getVariable(String name) {
        return (Expression) variableMap.get(name.toLowerCase());
    }
    
    private void addVariable(String name, Expression variable) {
        Expression old;
        if ((old = getVariable(name)) != null) {
            variables.remove(old);
        }
        variableMap.put(name.toLowerCase(), variable);
        variables.add(variable);
    }
    
    public void addVariable(String name, List structExpressions) {
        if (FactMetaModel.getInstance().isMetaType(name)) {
            QueryAnalyser.err(Messages.getString("QueryWalker.Error.Message.PRE_DEFINE_VARIABLE_OVERRIDE") + " for variable :" + name);
        }
        Expression newVar = Expression.makeExpression(name, structExpressions);
        Expression existingVar = (Expression) getVariable(name);
        if (existingVar != null && existingVar.getScope() != SCOPE_SESSION) {
            if (!Expression.typeEquals(existingVar, newVar)) {
                QueryAnalyser.err(Messages.getString("QueryWalker.Error.Message.VARIABLE_TYPE_MISMATCH") + " for variable " + name + ": " + existingVar.toString("", true) + " != " + newVar.toString("", true));
            }
        } else {
            newVar.cookie = cookie;
            newVar.scope = scope;
            addVariable(newVar.name, newVar);
//            if (existingVar != null && existingVar.getScope() == SCOPE_SESSION) {
//                variables.remove(existingVar);
//            }
//            variables.add(newVar);
//            variableMap.put();
        }
    }
    public void saveVariable(String name) {
        if (includeLevel > 0) 
            return;
        if (getVariable(name) != null) {
            savedVariables.add(name.toLowerCase());
        }
    }
    /*
    public void importScript(ScriptModel model) {
        variableMap.putAll(model.variableMap);
        variables.addAll(model.variables);
        commands.addAll(model.commands);
    }
    */
    
	public void reset() {
        for (Iterator iter = returnVariables.iterator(); iter.hasNext();) {
            String variableName = (String) iter.next();
            Expression variable = (Expression) getVariable(variableName);
            if (variable != null) {
                variable.scope = SCOPE_SESSION;
                saveVariable(variableName);
            }
        }
		flushCommands();
        flushVariables();
        flushReturnVariables();
	}
	public void flushCommands() {
		commands.clear();
	}
    public void flushVariables() {
        for (int i = 0; i < variables.size();) {
            Expression variable = (Expression) variables.get(i);
            String variableName = variable.getName();
            if (variable.getScope() != SCOPE_SESSION && !savedVariables.contains(variableName.toLowerCase())) {
                variableMap.remove(variableName.toLowerCase());
                variables.remove(i);
            } else {
                i++;
            }
        }
        savedVariables.clear();
    }
    public void flushReturnVariables() {
        returnVariables.clear();
    }

//    private String getVariablePrefix() {
//        return PQL_TEMP_PREFIX + "_";
////          return PQL_TEMP_PREFIX + commands.size()+"_" + queryStack.size() + "_";
//    }
//  private String getMyNextVariable() {
//  return getVariablePrefix() + ++ctr2;
//}

    public String getNextVariable() {
        return PQL_TEMP_PREFIX + ++ctr1;
    }

    public Expression createVariable(String variableName, String value) {
        Expression expr = new Expression();
        expr.name = variableName;
        expr.alias = variableName;
        expr.type = Expression.UNKNOWN_TYPE;
        expr.exprType = Expression.EXPR_VARIABLE;
        expr.path = value;
        expr.parent = null;
        expr.line = -1;
        List expressions = new ArrayList();
        expressions.add(expr);
        return createVariable(variableName, expressions);
    }

    public Expression createVariable(String variableName, List expressions) {
        // this methods should be called only after the entire query has been parsed
        if (expressions.size() == 0) {
            throw new IllegalAccessError("Variable expression types not initialized");
        }
        addVariable(variableName, expressions);
        Expression v = getVariable(variableName);
        if (v == null) {
            throw new IllegalAccessError("Variable "+variableName+" not initialized");
        }
        return v;
    }

    /**
     * @return Returns the returnVariables.
     */
    public Set getReturnVariables() {
        return returnVariables;
    }
    /**
     * @param returnVariables The returnVariables to set.
     */
    public void addReturnVariable(String variableName) {
        if (returnVariables.contains(DEFAULT_RETURN_VARIABLE)) {
            //throw new IllegalArgumentException("Script cannot return variable "+variableName+", when script has a return statement with a query");
            throw new IllegalArgumentException("Script can contain only ONE RETURN statement for scripts returning expression or result of a query. Use DEFINE variables to return multiple variables.");
        }
        if (variableName == null || variableName == DEFAULT_RETURN_VARIABLE) {
            if (returnVariables.size() > 0) {
                //cannot have more than one return statements, when script has a return statement with a query
                throw new IllegalArgumentException("Script can contain only ONE RETURN statement for scripts returning expression or result of a query. Use DEFINE variables to return multiple variables.");
            }
            variableName = DEFAULT_RETURN_VARIABLE;
        } 
        if (includeLevel > 0) 
            return;
        if (getVariable(variableName) != null) {
            returnVariables.add(variableName);
        } else {
            throw new IllegalAccessError("Variable "+variableName+" not found");
        }
    }

    public boolean isTargetSQL() {
        return targetSQL;
    }
    
    public void setTargetSQL(boolean targetSQL) {
        this.targetSQL = targetSQL;
    }
    
    private Set attrTypes = new HashSet();
    
    public Expression parseExpression(PExpr expr, Expression parent) {
        String path = "";
        if (parent != null && parent.path != null && parent.path.trim().length() > 0) {
            path = path + ".";
        }
        Expression result = null;
        if (expr instanceof AIdentifierExpr) {
            AIdentifierExpr aIdentifierExpr = (AIdentifierExpr) expr;
            String identifier = aIdentifierExpr.getIdentifier().getText();
            try { 
                result = getExpression(parent, identifier, null);
            } catch(AnalyserException ae) {
                ae.setNode(expr);
                throw ae;
            }
            if (result == null) {
                QueryAnalyser.err(Messages.getString("QueryWalker.Error.Message.PATH_NOT_FOUND") + " \"" 
                + (parent != null ? parent.path + "." : "") 
                + identifier + "\"", expr);
            }
            return result;
        } else if (expr instanceof APathExpr) {
            APathExpr aPathExpr = (APathExpr) expr;
            Expression left = parseExpression(aPathExpr.getLeft(), parent);
            Expression right = parseExpression(aPathExpr.getRight(), left);
            if (aPathExpr.getDotarrow() instanceof ATransitivedotDotarrow) {
                left.exprType = Expression.EXPR_TRANSITIVE_RELATION;
                if (!Expression.isValidTransitivePath(left)) {
                    QueryAnalyser.err(Messages.getString("QueryWalker.Error.Message.INVALID_TRANSITIVE_PATH") + " \"" 
                            + left.path + ".**." + right.name + "\"", expr);
                }
            }
            return right;
        } else if (expr instanceof AAdditionExpr) {
            AAdditionExpr aExpr = (AAdditionExpr) expr;
            return parseExpression(aExpr.getLeft(), aExpr.getRight(), aExpr.getPlus().getText()); 
        } else if (expr instanceof ASubstractionExpr) {
            ASubstractionExpr aExpr = (ASubstractionExpr) expr;
            return parseExpression(aExpr.getLeft(), aExpr.getRight(), aExpr.getMinus().getText()); 
        } else if (expr instanceof AIntersectionExpr) {
            AIntersectionExpr aExpr = (AIntersectionExpr) expr;
            return parseExpression(aExpr.getLeft(), aExpr.getRight(), aExpr.getSetAnd().getText()); 
        } else if (expr instanceof ANestedExpr) {
            ANestedExpr aExpr = (ANestedExpr) expr;
            return parseExpression(((AQuery) aExpr.getQuery()).getExpr(), parent);
        } else {
            QueryAnalyser.notYetImplemented("parseExpression", expr);
        }
        return null;
    }
    public Expression parseExpression(PExpr left, PExpr right, String operator) {
        Expression lhs = parseExpression(left, null);
        Expression rhs = parseExpression(right, null);
        if (!Expression.typeEquals(lhs, rhs)) {
            QueryAnalyser.err(Messages.getString("QueryWalker.Error.Message.TYPES_NOT_MATCH") + " \"" + lhs.toString("", true) + " & " + rhs.toString("", true) + "\"", left);
        }
        if (isTargetSQL()) {
            QueryModel leftQry =  null;
            QueryModel rightQry =  null;
            if (false) {
            } else if ("^".equals(operator) || "-".equals(operator)) {
                leftQry = new QueryModel(null);
                if (lhs.isQuery()) {
                    lhs = getTemporaryVariable(lhs.getQuery());
                }
                leftQry.addFromExpressions(getRootExpression(lhs));
                leftQry.addSelectExpressions(lhs);

                rightQry = new QueryModel(null);
                if (rhs.isQuery()) {
                    rhs = getTemporaryVariable(rhs.getQuery());
                }
                rightQry.addFromExpressions(getRootExpression(rhs));
                rightQry.addSelectExpressions(rhs);
                
                Condition c = new Condition();
                c.setLeft(lhs);
                c.setRight(Expression.makeExpression(rightQry));
                if ("^".equals(operator)) {
                    c.setOperator("IN");
                } else {
                    c.setOperator("NOT IN");
                }
                leftQry.addWhereConditions(c);
                return Expression.makeExpression(getNextVariable(), leftQry);
            } else if ("+".equals(operator)) {
                if (lhs.isQuery()) {
                    leftQry = lhs.getQuery();
                } else {
                    leftQry = new QueryModel(null);
                    leftQry.addFromExpressions(getRootExpression(lhs));
                    leftQry.addSelectExpressions(lhs);
                }
                if (rhs.isQuery()) {
                    rightQry = rhs.getQuery();
                } else {
                    rightQry = new QueryModel(null);
                    rightQry.addFromExpressions(getRootExpression(rhs));
                    rightQry.addSelectExpressions(rhs);
                }
                leftQry.setUnion(rightQry);
            } else {
                QueryAnalyser.err(Messages.getString("QueryWalker.Error.Message.OPERATOR_NOT_APPLICABLE") + " \"" + left + " " + operator+ " " + right + "\"", left);
            }
            return Expression.makeExpression(leftQry);
        } else {
            return lhs;
        }
//        return null;
    }
    public Expression getTemporaryVariable(QueryModel query) {
        String variableName = getNextVariable();
        query.setAssignVariable(createVariable(variableName, query.getSelectExpressions()));
        Expression variable = getVariable(variableName);
        Expression result = (Expression) variable.clone();
        addCommand(query);
        return result;
    }
    public static Expression getRootExpression(Expression expr) {
        return expr.getParent() == null ? expr : getRootExpression(expr.getParent()); 
    }
    
    public Expression getExpression(Expression parent, String token, QueryModel queryModel) {
        Expression result;
        String path = "";
        if (parent != null && parent.path != null && parent.path.trim().length() > 0) {
            path = parent.path + ".";
        }
        String currentPath = path + token;
//        result = model.getFromClauseExpression(currentPath);
        if (parent == null || parent.isModel()) {
            //look up in meta model
            if ((result = FactMetaModel.getInstance().getMetaModelExpression(token)) != null) {
                return result;
            //look up in meta types
            } else if ((result = FactMetaModel.getInstance().getMetaTypeExpression(token)) != null) {
                return result;
            //look up in script variables
            } else if (queryModel != null && (result = queryModel.getFromClauseExpression(token)) != null) {
                return result;
            } else if ((result = (Expression) this.getVariable(token)) != null) {
                return result;
            } else if (queryModel != null) {
                //look up in script variables
                List variableExpressions = new ArrayList(queryModel.getFromExpressions().size());
                for (Iterator iter = queryModel.getFromExpressions().entrySet().iterator(); iter.hasNext();) {
                    Map.Entry element = (Map.Entry) iter.next();
                    Expression temp = getExpression((Expression) element.getValue(), token, queryModel);
                    if (temp != null) {
                        variableExpressions.add(temp);
                    }
                }
                if (variableExpressions.size() > 1) {
                    QueryAnalyser.err(Messages.getString("QueryWalker.Error.Message.AMBIGOUS_PATH") + " \"" + token + "\"");
                } else if (variableExpressions.size() == 1) {
                    return (Expression) variableExpressions.get(0);
                }
                //look up in script variables
                variableExpressions = new ArrayList(queryModel.getSelectExpressions().size());
                for (Iterator iter = queryModel.getSelectExpressions().iterator(); iter.hasNext();) {
                    Expression e = (Expression) iter.next();
                    if (token.equals(e.alias)) {
                        variableExpressions.add(e);
                    }
                }
                if (variableExpressions.size() > 1) {
                    QueryAnalyser.err(Messages.getString("QueryWalker.Error.Message.AMBIGOUS_PATH") + " \"" + token + "\"");
                } else if (variableExpressions.size() == 1) {
                    return (Expression) variableExpressions.get(0);
                }
            }
            return null;
        } else if (parent.isModel()) {
            //look up in script variables
            if ((result = FactMetaModel.getInstance().getMetaTypeExpression(parent.getPath() + "." + token)) != null) {
                return result;
            }
            return null;
        } else {
            if (!parent.isStruct() && parent.type == null) {
                throw new IllegalArgumentException("Parent type not initialize "+currentPath+".");
            }
            if (!parent.isStruct()) {
                MetaType parentType = FactMetaModel.getInstance().lookupMetaType(parent.type);
                if (parentType == null) {
                    if (!isAttributeType(parent.type)) {
                        throw new IllegalArgumentException("Parent type '" + parent.type + "' not found for " + parent.path);
                    } else {
                        QueryAnalyser.err(Messages.getString("QueryWalker.Error.Message.PATH_NOT_FOUND") + " \"" + parent.path + "." + token + "\"");
                    }
                }
                MetaRelationship relationship = FactMetaModel.lookupRelationship(parentType, token);
                if (relationship != null) {
                    result = new Expression();
                    result.name = token;
                    result.type = getType(relationship.getPointerMetaType());
                    result.exprType = Expression.EXPR_RELATION;
                    result.path = currentPath;
                    result.parent = parent;
                    result.line = -1;
                    return result;
                    //return
                }

                MetaAttribute attribute = FactMetaModel.lookupAttribute(parentType, token);
                if (attribute != null) {
                    attrTypes.add(attribute.getType());
                    result = new Expression();
                    result.name = token;
                    result.type = attribute.getType();
                    result.exprType = Expression.EXPR_ATTRIBUTE;
                    result.path = currentPath;
                    result.parent = parent;
                    result.line = -1;
                    return result;
                }
            } else {
                Expression v = this.getVariable(parent.getPath());
                if (v == null) {
                    throw new IllegalArgumentException("Invalid variable: " + parent.getPath());
                }
                for (int i = 0; i < v.structTypes.length; i++) {
                    if (token.equalsIgnoreCase(v.structTypes[i].name) || token.equalsIgnoreCase(v.structTypes[i].alias)) {
                        return v.structTypes[i];
                    }
                }
            }
        }
        if (FactMetaModel.getInstance().isImplicitAttribute(token)) {
            if (environmentVariables != null && environmentVariables.size() > 0) {
                QueryAnalyser.err(Messages.getString("QueryWalker.Error.Message.IMPLICIT_AND_ENVIRONMENT_VARIABLES") + " : " + token);
                
            }
            result = new Expression();
            result.name = token;
            result.type = "String";
            result.exprType = Expression.EXPR_IMPLICIT_ATTRIBUTE;
            result.path = currentPath;
            result.parent = parent;
            result.line = -1;
            return result;
        }
        return null;
    }
    private static String getType(MetaType type) {
        return type.getName();
    }

    private boolean isAttributeType(String name) {
        return attrTypes.contains(name);
    }

    public Set getVariableNames() {
        return variableMap.keySet();
    }
    
    protected void addEnvironmentVariable(String envVariableName, String value) {
        getEnvironmentVariables(envVariableName);
        if (environmentVariables.getProperty(envVariableName) != null) {
            QueryAnalyser.warn(Messages.getString("QueryWalker.ENVIRONMENT_VARIABLE_OVERWRITTEN")
                  + " :" + envVariableName + " - value " + environmentVariables.getProperty(envVariableName) + ") by " + value);
        }
        environmentVariables.put(envVariableName, value);
    }

    protected void removeEnvironmentVariable(String envVariableName) {
        getEnvironmentVariables(envVariableName);
        environmentVariables.remove(envVariableName);
    }
    
    protected Properties getEnvironmentVariables(String envVariableName) {
        Object command = getLastCommand();
        if (command instanceof Properties) {
            environmentVariables = (Properties) command;
        } else if (environmentVariables != null) {
            environmentVariables = (Properties) environmentVariables.clone();
            addCommand(environmentVariables);
        } else {
            environmentVariables = new Properties();
            addCommand(environmentVariables);
        }
        return environmentVariables;
    }
    
    protected Object getLastCommand() {
        int last = getCommands().size() - 1;
        if (last >= 0) {
            Object o = getCommands().get(last);
            return o;
        }
        return null;
    }
    
    protected void setDefaultEnvironmentVariableName() {
        addEnvironmentVariable("system", "GLOBAL");
        addEnvironmentVariable("version", "LATEST");
    }
    
    private static void initEnvironmentVariableNames() {
        if (envVarNames != null) return; 
        envVarNames = new HashMap();
        envVarNames.put("system", "system");
        envVarNames.put("project", "project");
        envVarNames.put("version", "version");
        envVarNames.put("timestamp", "extraction_time");
        envVarNames.put("systemid", "id");

    }
    
    protected static String getEnvironmentVariableColumnName(String varName) {
        return (String) envVarNames.get(varName.toLowerCase());
        
    }
    public int pushInclude() {
        return ++includeLevel;
    }
    public int popInclude() {
        return --includeLevel;
    }
}
