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

import org.acre.lang.pql.translator.QueryGenerator;
import org.acre.lang.runtime.DatabaseAdapter;
import org.acre.model.metamodel.FactMetaModel;
import org.acre.model.metamodel.MetaRelationship;
import org.acre.model.metamodel.MetaType;
import org.acre.model.metamodel.RelationshipInfo;

import java.util.Iterator;
import java.util.List;


/**
 * Transforms PQL Query Model to SQL Query Model
 * 
 * @author Syed Ali
 */
public class SQLAnalyser extends QueryGenerator {

    private QueryModel sqlQuery = null;
    private final static boolean debug = false;
    private Condition rootWhereCondition = null;
    private int projectIdFilter = -1;

    public SQLAnalyser(QueryModel query, ScriptModel script) {
        super(query, script);
    }

    public SQLAnalyser(QueryModel query) {
        this(query, null);
    }
    
    public QueryModel constructSQLQuery(QueryModel parent) {
        sqlQuery = new QueryModel(parent);
        //Begin first pass for creating SQL FROM clause
        for (Iterator iterator = query.getFromExpressions().values().iterator(); iterator.hasNext();) {
            Expression pqlFrom = (Expression) iterator.next();
            buildSQLFromClause(pqlFrom);
        }
        for (Iterator iterator = query.getWhereConditions().iterator(); iterator.hasNext();) {
            Condition pqlWhere = (Condition) iterator.next();
            buildSQLFromClause(pqlWhere);
        }
        for (Iterator iterator = query.getSelectExpressions().iterator(); iterator.hasNext();) {
            Expression pqlSelect = (Expression) iterator.next();
            buildSQLFromClause(pqlSelect);
        }
        for (Iterator iterator = query.getOrderByExpressions().iterator(); iterator.hasNext();) {
            QueryModel.OrderByStruct pqlOrderBy = (QueryModel.OrderByStruct) iterator.next();
            buildSQLFromClause(pqlOrderBy.getExpression());
        }
        //End first pass for creating SQL FROM clause
        
        for (Iterator iterator = query.getWhereConditions().iterator(); iterator.hasNext();) {
            Condition pqlWhere = (Condition) iterator.next();
            
            addWhereCondition(buildSQLWhereClause(pqlWhere), "AND");
        }
        if (rootWhereCondition != null) {
            sqlQuery.addWhereConditions(rootWhereCondition);
        }
        
        for (Iterator iterator = query.getSelectExpressions().iterator(); iterator.hasNext();) {
            Expression pqlSelect = (Expression) iterator.next();
            if (pqlSelect.isStruct()) {
                Expression[] exprs = pqlSelect.getStructType();
                for (int i = 0; i < exprs.length; i++) {
                    Expression e = exprs[i];
                    sqlQuery.addSelectExpressions(getSQLExpression(e), false) ;
                }
            } else {
                sqlQuery.addSelectExpressions(getSQLExpression(pqlSelect), false) ;
            }
        }
        for (Iterator iterator = query.getOrderByExpressions().iterator(); iterator.hasNext();) {
            QueryModel.OrderByStruct pqlOrderBy = (QueryModel.OrderByStruct) iterator.next();
            sqlQuery.addOrderByExpressions(getSQLExpression(pqlOrderBy.getExpression()), pqlOrderBy.isDescending()) ;
        }
        if (query.getUnion() != null) {
            SQLAnalyser uniounst = new SQLAnalyser(query.getUnion(), script);
            QueryModel sqlUnionQuery = uniounst.constructSQLQuery(query);
            //sqlUnionQuery.setAssignVariable(query.getAssignVariable());
            sqlQuery.setUnion(sqlUnionQuery);
        }
        if (query.assignVariable != null) {
            sqlQuery.assignVariable = query.assignVariable;
        }
        if (debug) {
            System.out.println("--- query ---");
            System.out.println(query);
            System.out.println("--- sql ---");
            System.out.println(sqlQuery);
        }
        return sqlQuery;
    }
    
    public String generate() {
        StringBuffer result = new StringBuffer();
        for (Iterator iter = getSQLStatements().iterator(); iter.hasNext();) {
            DMLStatement element = (DMLStatement) iter.next();
            result.append(element.sql).append('\n');
        }
        return result.toString();
    }
    public List getSQLStatements() {
        SQLTranslator st = new SQLTranslator(constructSQLQuery(null));
        if (projectIdFilter >= 0) {
            st.setEnvironmentVariableFilter(Integer.toString(projectIdFilter));
            if (debug) {
                System.err.println("ProjectID Filter = " + projectIdFilter);
            }
        }
        return st.getSQLStatements();
    }
    
    public void buildSQLFromClause(Condition c) {
        Expression e;
        Expression other;
        c.validate();
        if (c.isLeftCondition()) {
            buildSQLFromClause((Condition) c.getLeft());
            buildSQLFromClause((Condition) c.getRight());
        } else {
            if (c.isLeftExpression()) {
                e = (Expression) c.getLeft();
                other = (Expression) c.getRight();
                if (!e.isQuery() && !other.isNilLiteral()) 
                    buildSQLFromClause(e);
            }
            if (c.isRightExpression()) {
                e = (Expression) c.getRight();
                other = (Expression) c.getLeft();
                if (!e.isQuery() && !other.isNilLiteral()) 
                    buildSQLFromClause(e);
            }
        } 
    }
    public Condition buildSQLWhereClause(Condition c) {
        if (c.isLeftCondition()) {
            Condition result = new Condition();
            result.setLeft(buildSQLWhereClause((Condition) c.getLeft()));
            result.setRight(buildSQLWhereClause((Condition) c.getRight()));
            result.setOperator(c.getOperator());
            result.validate();
            return result;
        } else if (c.isRightExpression()) {
            Condition result = new Condition();
            Expression left = (Expression) c.getLeft();
            Expression right = (Expression) c.getRight();
            if (left.isNilLiteral() || right.isNilLiteral()) {
                result.setLeft(getSQLExpression(left, false));
                result.setRight(getSQLExpression(right, false));
                result.setOperator(c.getSQLOperatorForNull());
                result.validate();
                return result;
            } else {
                result.setLeft(getSQLExpression(left));
                result.setRight(getSQLExpression(right));
                result.setOperator(c.getOperator());
                result.validate();
                return result;
            }
        }
        throw new IllegalArgumentException("Cannot process condition: " + c);
    }
    
    public void buildSQLFromClause(Expression e) {
        if (e.isLiteral()) {
            return;
        }
        if (e.isAttribute()) {
            e = e.getParent();
            if (e == null)
                return;
        }
            
            
        Expression sqlFrom = sqlQuery.getFromClauseExpression(e.getPath());
        if (sqlFrom == null) {
            sqlFrom = new Expression();
            if (e.getParent() == null) {
                if (e.isStruct()) {
                    buildSQLFromClauseForStructVariable(e);
                } else if (e.isVariable() && !e.isPrimitive()) {
                    buildSQLFromClauseForVariable(e);
                } else {
                    sqlFrom.name = e.getName();
                    sqlFrom.alias = getUniqueFromClauseAlias(sqlFrom.name);
                    sqlFrom.path = e.getPath();
                    sqlFrom.type = e.getType();
                    sqlFrom.exprType = e.exprType;
                    sqlFrom.parent = null;
                    sqlFrom.line = -1;
                    sqlQuery.addFromExpressions(sqlFrom);
                } 
            } else if (e.isImplicitAttribute()) {
//                if (true)
//                    throw new RuntimeException(e.toString());
//                //join with related entity
                sqlFrom.name = "SalsaProject";//getExtent("Salsa");
                sqlFrom.alias = getUniqueFromClauseAlias(sqlFrom.name);
                sqlFrom.path = sqlFrom.name + "." + e.getPath();
                sqlFrom.type = sqlFrom.name;// e.getType();
                sqlFrom.exprType = Expression.EXPR_IMPLICIT_ATTRIBUTE;
                sqlFrom.parent = null;
                sqlFrom.line = -1;
                sqlQuery.addFromExpressions(sqlFrom);
                //process parent
//                buildSQLFromClause(e.getParent());
                //add join condition to where clause
                andWhereCondition(getJoinCondition(e.getParent(), sqlFrom, "salsaProjectId", "id"));
            } else if (e.isRelation() || e.isTransitiveRelation()) {
                //join with related entity
                sqlFrom.name = getExtent(e.getType());
                sqlFrom.alias = getUniqueFromClauseAlias(sqlFrom.name);
                sqlFrom.path = e.getPath();
                sqlFrom.type = e.getType();
                sqlFrom.exprType = Expression.EXPR_RELATION;
                sqlFrom.parent = null;
                sqlFrom.line = -1;
                sqlQuery.addFromExpressions(sqlFrom);
                //process parent
                buildSQLFromClause(e.getParent());
                //add join condition to where clause
                addJoinCondition(e);
            }
        }
    }

    private Expression getFromClauseForVariable(Expression variable) {
        Expression fromVariable; 
        if ((fromVariable = sqlQuery.getFromClauseExpression(variable.getName()+".var")) != null) {
            return fromVariable;
        }
        if (!DatabaseAdapter.USE_TEMP_TABLES) {
            fromVariable = new Expression();
            fromVariable.name = variable.getName();
            fromVariable.alias = getUniqueFromClauseAlias(fromVariable.name);
            fromVariable.path = fromVariable.name + ".var";
            fromVariable.type = "ReadVariable";
            fromVariable.exprType = Expression.EXPR_VARIABLE;
            fromVariable.parent = null;
            fromVariable.line = -1;
            fromVariable.cookie = variable.getCookie();
            fromVariable.scope = variable.getScope();
            sqlQuery.addFromExpressions(fromVariable);

            andWhereCondition(createWhereConditionForLiteral(fromVariable, "cookie", "String", "'"+fromVariable.cookie+"'"));
            andWhereCondition(createWhereConditionForLiteral(fromVariable, "scope", "Integer", ""+fromVariable.scope));
            andWhereCondition(createWhereConditionForLiteral(fromVariable, "name", "String", "\""+fromVariable.getName()+"\""));
        } else {
            fromVariable = new Expression();
            fromVariable.name = variable.getName();
            fromVariable.alias = getUniqueFromClauseAlias(fromVariable.name);
            fromVariable.path = fromVariable.name + ".var";
            fromVariable.type = DatabaseAdapter.getReadTemporaryTableName(variable);
            fromVariable.exprType = Expression.EXPR_VARIABLE;
            fromVariable.parent = null;
            fromVariable.line = -1;
            fromVariable.cookie = variable.getCookie();
            fromVariable.scope = variable.getScope();
            sqlQuery.addFromExpressions(fromVariable);        
        }
        
        return fromVariable;
    }
    private void buildSQLFromClauseForStructVariable(Expression variable) {
        Expression fromVariable = getFromClauseForVariable(variable);
        Expression[] exprs = variable.getStructType();
        for (int i = 0; i < exprs.length; i++) {
            Expression e = exprs[i];
            if (!e.isPrimitive()) {
                buildSQLFromClauseForStructVariable(fromVariable, e, i);
            }
        }
    }
    private void buildSQLFromClauseForStructVariable(Expression fromVariable, Expression e, int index) {
        Expression fromVarJoin; 
        if (sqlQuery.getFromClauseExpression(e.getPath()) != null) {
            return;
        }
        if (!DatabaseAdapter.USE_TEMP_TABLES) {
            MetaType type = FactMetaModel.getInstance().lookupMetaType(e.getType());
            fromVarJoin = new Expression();
            fromVarJoin.name = type.getMappedName();
            fromVarJoin.alias = getUniqueFromClauseAlias(fromVarJoin.name);
            fromVarJoin.path = e.getPath();
            fromVarJoin.type = e.getType();
            fromVarJoin.exprType = Expression.EXPR_VARIABLE;
            fromVarJoin.parent = null;
            fromVarJoin.line = -1;
            sqlQuery.addFromExpressions(fromVarJoin);
            
            andWhereCondition(getJoinCondition(fromVariable, fromVarJoin, "nodeId"+index, "id"));
        } else {
            MetaType type = FactMetaModel.getInstance().lookupMetaType(e.getType());
            fromVarJoin = new Expression();
            fromVarJoin.name = type.getMappedName();
            fromVarJoin.alias = getUniqueFromClauseAlias(fromVarJoin.name);
            fromVarJoin.path = e.getPath();
            fromVarJoin.type = e.getType();
            fromVarJoin.exprType = Expression.EXPR_VARIABLE;
            fromVarJoin.parent = null;
            fromVarJoin.line = -1;
            sqlQuery.addFromExpressions(fromVarJoin);
            andWhereCondition(getJoinCondition(fromVariable, fromVarJoin, "__id"+(index+1), "id"));
            
        }
    }
    private void buildSQLFromClauseForVariable(Expression e) {
        if (!DatabaseAdapter.USE_TEMP_TABLES) {
            Expression fromVariable = new Expression();
            fromVariable.name = e.getName();
            fromVariable.alias = getUniqueFromClauseAlias(fromVariable.name);
            fromVariable.path = fromVariable.name + ".var";
            fromVariable.type = "ReadVariable";
            fromVariable.exprType = Expression.EXPR_VARIABLE;
            fromVariable.parent = null;
            fromVariable.line = -1;
            fromVariable.cookie = e.getCookie();
            fromVariable.scope = e.getScope();
            sqlQuery.addFromExpressions(fromVariable);
            
            Expression fromVarJoin = new Expression();
            MetaType type = FactMetaModel.getInstance().lookupMetaType(e.getType());
            fromVarJoin.name = type.getMappedName();
            fromVarJoin.alias = getUniqueFromClauseAlias(fromVarJoin.name);
            fromVarJoin.path = fromVariable.name;
            fromVarJoin.type = e.getType();
            fromVarJoin.exprType = Expression.EXPR_VARIABLE;
            fromVarJoin.parent = null;
            fromVarJoin.line = -1;
            sqlQuery.addFromExpressions(fromVarJoin);
            
            andWhereCondition(createWhereConditionForLiteral(fromVariable, "cookie", "String", "'"+fromVariable.cookie+"'"));
            andWhereCondition(createWhereConditionForLiteral(fromVariable, "scope", "Integer", ""+fromVariable.scope));
            andWhereCondition(createWhereConditionForLiteral(fromVariable, "name", "String", "\""+fromVariable.getName()+"\""));
            andWhereCondition(getJoinCondition(fromVariable, fromVarJoin, "nodeId0", "id"));
        } else {
            Expression fromVariable = new Expression();
            fromVariable.name = e.getName();
            fromVariable.alias = getUniqueFromClauseAlias(fromVariable.name);
            fromVariable.path = fromVariable.name + ".var";
            fromVariable.type = DatabaseAdapter.getReadTemporaryTableName(e);
            fromVariable.exprType = Expression.EXPR_VARIABLE;
            fromVariable.parent = null;
            fromVariable.line = -1;
            fromVariable.cookie = e.getCookie();
            fromVariable.scope = e.getScope();
            sqlQuery.addFromExpressions(fromVariable);
            
            Expression fromVarJoin = new Expression();
            MetaType type = FactMetaModel.getInstance().lookupMetaType(e.getType());
            fromVarJoin.name = type.getMappedName();
            fromVarJoin.alias = getUniqueFromClauseAlias(fromVarJoin.name);
            fromVarJoin.path = fromVariable.name;
            fromVarJoin.type = e.getType();
            fromVarJoin.exprType = Expression.EXPR_VARIABLE;
            fromVarJoin.parent = null;
            fromVarJoin.line = -1;
            sqlQuery.addFromExpressions(fromVarJoin);

            andWhereCondition(getJoinCondition(fromVariable, fromVarJoin, "__id1", "id"));
        }
    }

    public Expression getSQLExpression(Expression e) {
        return getSQLExpression(e, true);
    }
    public Expression getSQLExpression(Expression e, boolean join) {
        Expression result = null;
        if (false) {
        } else if (e.isLiteral()) {
            return (Expression) e.clone();
        } else if (e.isQuery()) {
            SQLAnalyser nestedAnalyser = new SQLAnalyser(e.getQuery(), script);
            return Expression.makeExpression(nestedAnalyser.constructSQLQuery(query));
        } else if (e.isAttribute() && e.getParent() != null) {
            Expression lhs = new Expression();
            lhs.name = getFromClauseAlias(e.getParent().getPath());
            lhs.path = lhs.name;
            lhs.type = e.getParent().getType();
            lhs.exprType = e.getParent().exprType;
            lhs.line = -1;
            
            result = new Expression();
            result.name = e.getName();
            result.path = lhs.path + "." + e.getName();
            result.type = e.getType();
            result.exprType = Expression.EXPR_ATTRIBUTE;
            result.parent = lhs;
            result.line = -1;
        } else if (e.isImplicitAttribute()) {
            Expression lhs = new Expression();
            lhs.name = getFromClauseAlias("SalsaProject."+e.getPath());
            lhs.path = lhs.name;
            lhs.type = e.getParent().getType();
            lhs.exprType = e.exprType;
            lhs.line = -1;

            result = new Expression();
            result.name = e.getName();
            result.path = lhs.name + "." + e.getName();
            result.type = e.getType();
            result.exprType = e.exprType;
            result.line = -1;
        } else if (e.isRelation()) {
            if (e.getParent() == null) {
                throw new IllegalArgumentException("Parent cannot be null for relationship expression: " + e.getPath());
            }
            result = new Expression();
            if (join) {
                result.name = getFromClauseAlias(e.getPath());
                result.path = result.name;
                result.exprType = Expression.EXPR_RELATION;
            } else {
                Expression lhs = new Expression();
                lhs.name = getFromClauseAlias(e.getParent().getPath());
                lhs.path = lhs.name;
                lhs.type = e.getParent().getType();
                lhs.exprType = e.getParent().exprType;
                lhs.line = -1;
                
                result.name = e.getName();
                result.path = lhs.path + "." + e.getName();
                result.exprType = Expression.EXPR_ATTRIBUTE;
                result.parent = lhs;
            }
            result.type = e.getType();
            result.line = -1;
//        } else if (e.isStruct()) {
        } else if (e.isVariable() || e.isSystemVariable()) {
            result = new Expression();
            result.name = getFromClauseAlias(e.getPath());
            result.path = result.name;
            result.type = e.getType();
            result.exprType = e.exprType;
            result.line = -1;
        } else {
            notYetImplemented(" queries with expressions of type: " + e.getPath() + "("+e.exprType+")");
        }
        if (e.alias == null) {
            String alias = getUniqueSelectClauseAlias(e.name);
            if (!alias.equals(e.name)) {
                result.alias = alias;
            }
        } else {
            result.alias = e.alias;
        }
        if (e.function != null) {
            result.function = e.function;
        }
        return result;
    }
    public String getUniqueSelectClauseAlias(String name) {
        int count = 1;
        String result = name;
        for (int i = 0; i < sqlQuery.getSelectExpressions().size(); i++) {
            Expression e = (Expression) sqlQuery.getSelectExpressions().get(i);
            if (result.equals(e.getName())) {
                result = name + count++;
                i = 0;
            }
        }
        return result;
    }
    public String getUniqueFromClauseAlias(String name) {
        if (name == null)
            return null;
//        return name.charAt(0) + ""+(
        return name + ""+(
                (sqlQuery.getParent() != null ? sqlQuery.getParent().getFromExpressions().size() : 0) +
                sqlQuery.getFromExpressions().size() + 1
                );
    }
    
    public String getRelationPath(String path) {
        if (checkFromClauseAlias(path+".join") != null) {
            return path + ".join";
        }
        return path;
    }
    public String checkFromClauseAlias(String path) {
        Expression e = sqlQuery.getFromClauseExpression(path);
        if (e != null)
            return e.getAlias();
        return null;
    }
    
    public String getFromClauseAlias(String path) {
        String result = checkFromClauseAlias(path);
        if (result == null)
            result = checkFromClauseAlias(path+".var");
        if (result == null)
            throw new IllegalArgumentException("Cannot find From clause for path " + path);
        return result;
    }

    private void andWhereCondition(Condition condition) {
        addWhereCondition(condition, "AND");
    }
    private void orWhereCondition(Condition condition) {
        addWhereCondition(condition, "OR");
    }
    private void addWhereCondition(Condition condition, String operator) {
        if (rootWhereCondition == null)
            rootWhereCondition = condition;
        else {
            Condition root = new Condition();
            root.setLeft(rootWhereCondition);
            root.setOperator(operator);
            root.setRight(condition);
            rootWhereCondition = root;
        }
    }
    public void addJoinCondition(Expression e) {
        if (e.getParent() == null)
            return;
        RelationshipInfo ri = new RelationshipInfo(e.getParent().getType(), e.getName());
        Condition result;
        if (e.isRelation()) {
            if (ri.isOneToOne()) {
                throw new IllegalArgumentException("Cannot map 1:1 relationships " + e.getParent().getType() + " - " + e.getName()+ " - "+e.getType());
                // process 1 side
            } else if (ri.isManyToOne()) {
                result = getJoinCondition(e.getParent(), e, "id", ri.getReverseName());
                andWhereCondition(result);
            } else if (ri.isOneToMany()) {
                result = getJoinCondition(e, e.getParent(), "id", ri.getForwardName());
                andWhereCondition(result);
            } else {
                addJoinConditionWithJoinTable(e, ri.getJoinTableName(), ri.getForwardRel(), ri.getReverseRel());
            }
        } else if (e.isTransitiveRelation()) {
            addJoinConditionWithJoinTable(e, ri.getTransitiveJoinTableName(), ri.getForwardRel(), ri.getReverseRel());
        } else {
            throw new IllegalArgumentException("Cannot map expression " + e.toString("", true));
        }
    }

    protected void addJoinConditionWithJoinTable(Expression e, String jointTableName, MetaRelationship parentRel, MetaRelationship childRel) {
        Condition result;
        Expression joinTable = new Expression();
        joinTable.name = jointTableName;
        joinTable.alias = getUniqueFromClauseAlias(joinTable.name);
        joinTable.path = e.getPath()+".join";
        joinTable.type = joinTable.name;
        joinTable.exprType = Expression.EXPR_VARIABLE;
        joinTable.parent = null;
        joinTable.line = -1;
        sqlQuery.addFromExpressions(joinTable);
        
        
        result = getJoinCondition(e, joinTable, "id", parentRel.getName());
        andWhereCondition(result);
        result = getJoinCondition(e.getParent(), joinTable, "id", childRel.getName());
        andWhereCondition(result);
    }

    private Condition createWhereConditionForLiteral(Expression lhsParent, String lhsColumnName, String lhsColumnType, String rhsValue) {
        Expression lhs = new Expression();
        lhs.name = lhsColumnName;
        lhs.path = getFromClauseAlias(lhsParent.path) + "." + lhsColumnName;
        lhs.type = lhsColumnType;
        lhs.exprType = Expression.EXPR_ATTRIBUTE;
        lhs.parent = lhsParent;
        lhs.line = -1;

        Expression rhs = new Expression();
        rhs.name = rhsValue;
        rhs.path = rhsValue;
        rhs.type = lhsColumnType;
        rhs.exprType = Expression.EXPR_LITERAL;
        rhs.parent = null;
        rhs.line = -1;
        return createWhereCondition(lhs, rhs);
    }

    
    private Condition createWhereCondition(Expression lhs, Expression rhs) {
        return createWhereCondition(lhs, rhs, " = ");
    }

    private Condition createWhereCondition(Expression lhsExpr, Expression rhsExpr, String operator) {
        Condition result = new Condition();
        result.setLeft(lhsExpr);
        result.setRight(rhsExpr);
        result.setOperator(operator);
        result.validate();
        return result;
    }
    private Condition getJoinCondition(Expression lhsExpr, Expression rhsExpr, String lhsColumn, String rhsColumn) {
        Expression lhsParent = new Expression();
        lhsParent.name = getFromClauseAlias(lhsExpr.getPath());
        lhsParent.path = lhsParent.name;
        lhsParent.type = lhsExpr.getType();
        lhsParent.exprType = lhsExpr.exprType;
        lhsParent.line = -1;
        Expression lhs = new Expression();
        lhs.name = lhsColumn;
        lhs.path = lhsParent.path + "." + lhsColumn;
        lhs.type = "long";
        lhs.exprType = Expression.EXPR_ATTRIBUTE;
        lhs.parent = lhsParent;
        lhs.line = -1;

        Expression rhsParent = new Expression();
        rhsParent.name = getFromClauseAlias(rhsExpr.getPath());
        rhsParent.path = rhsParent.name;
        rhsParent.type = rhsExpr.getType();
        rhsParent.exprType = rhsExpr.exprType;
        rhsParent.line = -1;
        Expression rhs = new Expression();
        rhs.name = rhsColumn;
        rhs.path = rhsParent.path + "." + rhsColumn;
        rhs.type = "long";
        rhs.exprType = Expression.EXPR_ATTRIBUTE;
        rhs.parent = rhsParent;
        rhs.line = -1;

        return createWhereCondition(lhs, rhs, " = ");
    }

    public void setProjectIdFilter(int projectId) {
        projectIdFilter = projectId;
    }

    /*
    // hack for getting join table name
    public static String getTransitiveJoinTableName(String typeName, String relName) {
        return getJoinTableName(typeName, relName) + "Transitive";
    }
//    public static String getJoinTableName(String typeName, String relName) {
//    }
    public static String getJoinTableName(String typeName, String relName) {
        if (false) {
        } else if ("JClass".equals(typeName)) {
            if (false) {
            } else if ("implementsInterfaces".equals(relName) || "implementingClasses".equals(relName)) {
                return "JClass_ImplementsInterfaces";
//            } else if ("extendsClasses".equals(relName) || "extendingClasses".equals(relName)) {
//                //Modified the relationship to match Yury's schema. Am not sure why he did this????
//                return "JClass_ExtendsClasses";
            } else if ("thrownBy".equals(relName)) {
                return "JMethod_ThrowsExceptions";
            } else if ("caughtBy".equals(relName)) {
                return "JMethod_CatchesExceptions";
            } else if ("instantiatedBy".equals(relName)) {
                return "JMethod_Instantiates";
            } 
        } else if ("JMethod".equals(typeName)) {
            if (false) {
            } else if ("calls".equals(relName) || "callers".equals(relName)) {
                return "JMethod_Calls";
            } else if ("throwsExceptions".equals(relName)) {
                return "JMethod_ThrowsExceptions";
            } else if ("catchesExceptions".equals(relName)) {
                return "JMethod_CatchesExceptions";
            } else if ("usedFields".equals(relName)) {
                return "JMethod_UsedFields";
            } else if ("instantiates".equals(relName)) {
                return "JMethod_Instantiates";
            }
        } else if ("JField".equals(typeName)) {
            if (false) {
            } else if ("usedByMethods".equals(relName)) {
                return "JMethod_UsedFields";
            }
        } 
        throw new IllegalArgumentException("Cannot find Join table for " + typeName + "'s relationship " + relName);
    }
    public static String translate(ScriptModel script, String preql) throws IOException, LexerException, ParserException {
        Node ast = TreeBuilder.getNode(preql, true);
        ScriptWalker qw;
        if (script == null)
            qw = new ScriptWalker();
        else 
            qw = new ScriptWalker(script, true);
        ast.apply(qw);
        List commands = qw.getModel().getCommands();
        StringBuffer ql = new StringBuffer();
        for (int i = 0; i < commands.size(); i++) {
            if (commands.get(i) instanceof QueryModel) {
                QueryModel element = (QueryModel) commands.get(i);
                SQLAnalyser gq = new SQLAnalyser(element);
                ql.append(gq.generate()).append('\n');
            } else {
                ql
                  //SMA: Don't know who added this line. It is obviously wrong,
                  //as QL doesn't support variable names with $ in it.
                  //.append(addDefine && i+1 == commands.size() ? "DEFINE qlReturnResult AS " : "")
                 .append(commands.get(i).toString()).append('\n');
            }
        }
        return ql.toString();
    }
    public static void main(String[] args) throws IOException, LexerException, ParserException {
        String s = "define aaa as select count(m) " +
                "from classes c, c.parentPackage, c.methods m " +
                "where c.shortName =\"foo\" AND m.usedFields.name = \"bar\"; return aaa";
        System.out.println(translate(null, s));
    }
    */

}
