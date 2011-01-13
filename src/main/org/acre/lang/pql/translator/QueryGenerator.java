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
package org.acre.lang.pql.translator;

import org.acre.lang.analyser.*;
import org.acre.model.metamodel.FactMetaModel;
import org.acre.model.metamodel.MetaType;
import org.acre.model.metamodel.RelationshipInfo;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Syed Ali
 *
 */

public class QueryGenerator {
    protected QueryModel query = null;
    protected ScriptModel script = null;

    public QueryGenerator(QueryModel query, ScriptModel script) {
        this.query = query;
        this.script = script;
    }

    public QueryGenerator(QueryModel query) {
        this(query, null);
    }

    public QueryModel getQuery() {
        return query;
    }

    protected void init() {
        if (query == null) {
            throw new IllegalArgumentException("query is null");
        }
    }

    public String generate() {
        Iterator iter;
        init();
        StringBuffer reql = new StringBuffer();
        List selectClause = query.getSelectExpressions();
        Map fromClause = query.getFromExpressions();
        Set whereClause = query.getWhereConditions();
        Expression fromVar = getRootFromExpression(fromClause);
        if (whereClause != null && whereClause.size() > 0) {
            for (iter = whereClause.iterator(); iter.hasNext();) {
                Condition element = (Condition) iter.next();
                translateWhereCondition(reql, element);
            }
            bracket(reql);
        } else {
            reql.append(getExtent(fromVar));
        }
        for (int i = 0; i < selectClause.size(); i++) {
            Expression element = (Expression) selectClause.get(i);
            if (selectClause.size() == 1) {
                translateSelectOneProjection(reql, fromVar.getName(), element);
            } else {
                translateSelectMultiProjection(reql, fromVar.getName(), element);
                form(reql, i, i == selectClause.size() - 1);
            }
            String function = element.getFunction();
            if (function != null) {
                if (false) {
                } else if ("COUNT".equals(function)) {
                    reql.insert(0, '#');
                } else {
                    notYetImplemented(" aggregation funcation " + function);
                }
            }
        }
        if (query.getAssignVariable() != null) {
            reql.insert(0, query.getAssignVariable().getName() + " = ");
//            if (addReturn) {
//                reql.append("\n return " + query.getAssignVariable().getName());
//            }        
//        } else if (addReturn) {
//            reql.insert(0, "return ");
        }
        return reql.toString();
    }

    protected void form(StringBuffer reql1, int index, boolean last) {
        if (last) {
            //last one
            //drop the right most column
            reql1.insert(0, "form(");
            for (int j = 0; j < index; j++) {
                reql1.append(", \"&" + j + "\"");
            }
            reql1.append(", \"&" + (index + 1) + "\"");
            reql1.append(")");
        } else {
            //swap the the right most column with the previous one
            reql1.insert(0, "form(");
            for (int j = 0; j < index; j++) {
                reql1.append(", \"&" + j + "\"");
            }
            reql1.append(", \"&" + (index + 1) + "\"");
            reql1.append(", \"&" + index + "\"");
            reql1.append(")");
        }
    }

    protected void translateSelectOneProjection(StringBuffer buffer, String rootName, Expression pathExpr) {
        if (pathExpr.isAttribute() && "name".equals(pathExpr.getName())) {
            if (pathExpr.getParent() == null) {
                throw new IllegalArgumentException("Cannot query attribute name with null parent");
            }
            pathExpr = pathExpr.getParent(); 
        }
        String relationJoin = getRelationshipJoin(rootName, pathExpr.getParent());
        if (relationJoin != null) {
            join(buffer, relationJoin.toString());
        }

        Expression parent = null;
        if (false) {
        } else if (pathExpr.isLiteral()) {
            notYetImplemented(" projection of literals " + pathExpr.getName());
            //product(buffer, pathExpr.getName());
        } else if (pathExpr.isAttribute() && !"name".equals(pathExpr.getName())) {
            if (pathExpr.getParent() == null) {
                throw new IllegalArgumentException("Cannot query attribute with null parent");
            }
            if (relationJoin != null) {
                join(buffer, "@" + pathExpr.getName());
                buffer.insert(0, "rng(").append(")");
            } else {
                project(buffer, "@" + pathExpr.getName());
            }
        } else {
            if (!rootName.equals(pathExpr.getPath())) {
                join(buffer, getRelation(pathExpr));
                join(buffer, getExtent(pathExpr));
//                join(buffer, getExtent(pathExpr.getType()));
                buffer.insert(0, "rng(").append(")");
            }
        }
    }
    protected void translateSelectMultiProjection(StringBuffer buffer, String rootName, Expression pathExpr) {
        if (pathExpr.isAttribute() && "name".equals(pathExpr.getName())) {
            if (pathExpr.getParent() == null) {
                throw new IllegalArgumentException("Cannot query attribute name with null parent");
            }
            pathExpr = pathExpr.getParent(); 
        }
        if ("lineNum".equals(pathExpr.getName()))
            //TODO: Implement projection of lineNum
            notYetImplemented("projection of lineNum attribute");
        /*
        boolean hasRelation = false;
        Expression tempExpr = pathExpr.getParent();
        while (tempExpr != null && tempExpr.getParent() != null) {
            if (rootName.equals(tempExpr.getPath())) {
                break;
            }
            if (!hasRelation) {
                compose(buffer, getRelation(tempExpr));
            } else {
                join(buffer, getRelation(tempExpr));
            }
            join(buffer, getExtent(tempExpr.getType()));
            hasRelation = true;
            tempExpr = tempExpr.getParent();
        }
        */
        String relationJoin = getRelationshipJoin(rootName, pathExpr.getParent());
        if (relationJoin != null) {
            compose(buffer, relationJoin.toString());
        }

        Expression parent = null;
        if (false) {
        } else if (pathExpr.isLiteral()) {
            notYetImplemented(" projection of literals " + pathExpr.getName());
            //product(buffer, pathExpr.getName());
        } else if (pathExpr.isAttribute() && !"name".equals(pathExpr.getName())) {
            if (pathExpr.getParent() == null) {
                throw new IllegalArgumentException("Cannot query attribute with null parent");
            }
            if (relationJoin == null) {
                compose(buffer, "@" + pathExpr.getName());
            } else {
                join(buffer, "@" + pathExpr.getName());
            }
        } else {
            //TODO: could use show function to display objects 
            if (!rootName.equals(pathExpr.getPath())) {
                if (relationJoin == null) {
                    compose(buffer, getRelation(pathExpr));
                } else {
                    join(buffer, getRelation(pathExpr));
                }
                join(buffer, getExtent(pathExpr.getType()));
            } else {
                compose(buffer, "id("+getExtent(pathExpr.getType())+")");
            }
        }

    }

    /**
     * @param rootName  name of the root from variable 
     * @param pathExpr expression that is to be traversed
     * @return joing string for ql relationships
     */
    private String getRelationshipJoin(String rootName, Expression pathExpr) {
        StringBuffer relationBuffer = null;
        while (pathExpr != null && pathExpr.getParent() != null) {
            if (rootName.equals(pathExpr.getPath())) {
                break;
            }
            if (relationBuffer == null) {
                relationBuffer = new StringBuffer(getRelation(pathExpr) + " o " + getExtent(pathExpr.getType()));
            } else {
                relationBuffer.insert(0, getRelation(pathExpr) + " o " + getExtent(pathExpr.getType()) + " o ");

            }
            pathExpr = pathExpr.getParent();
        }
        return relationBuffer == null ? null : relationBuffer.toString();
    }

    protected void translateWhereCondition(StringBuffer buffer, Condition c) {
        c.validate();
        if (c.isLeftExpression() && c.isRightExpression()) {
            Expression left = (Expression) c.getLeft(); 
            Expression right = (Expression) c.getRight(); 
            if (left.isLiteral() && !right.isLiteral()) {
                translateWhereConditionWithLiteral(buffer, right, left, c.getOperator());
            } else if (right.isLiteral() && !left.isLiteral()) {
                translateWhereConditionWithLiteral(buffer, left, right, c.getOperator());
            } else {
                translateWhereConditionWithJoin(buffer, c);
            }
        } else if (c.isLeftCondition()) {
            Condition left = (Condition) c.getLeft(); 
            Condition right = (Condition) c.getRight(); 
            StringBuffer leftBuffer = new StringBuffer();
            translateWhereCondition(leftBuffer, left);
            buffer.append(bracket(leftBuffer));
            if ("AND".equals(c.getOperator())) {
                buffer.append(" ^ ");
            } else if ("OR".equals(c.getOperator())) {
                buffer.append(" + ");
            } else {
                notYetImplemented(" conditional operator " + c.getOperator());
            }
            StringBuffer rightBuffer = new StringBuffer();
            translateWhereCondition(rightBuffer, right);
            buffer.append(bracket(rightBuffer));
        } else {
            notYetImplemented(" Condtion generator :\n" + c);
        }
    }
    protected void translateWhereConditionWithJoin(StringBuffer buffer, Condition c)
    {
        Expression rootExpr = null;
        Expression fromExpr = null;
        Expression joinExpr = null;
        String operator = c.getOperator();
        Expression left = (Expression) c.getLeft();
        Expression right = (Expression) c.getRight();
        if ((rootExpr = getFromExpression(query.getFromExpressions(), left)) != null) {
            fromExpr = left;
            joinExpr = right;
        } else if ((rootExpr = getFromExpression(query.getFromExpressions(), right)) != null) {
            fromExpr = right;
            joinExpr = left;
        } else {
            notYetImplemented(" conditions without from clause " + left.getPath() + " " + c.getOperator() + " " + right.getPath());
        }
        QLExpression lhsType = translateWhereConditionExpression(fromExpr);
        QLExpression rhsType = translateWhereConditionExpression(joinExpr);
        if (lhsType == null || rhsType == null || !lhsType.type.equals(rhsType.type)) {
            //SMA: For now avoid reporting error for UNKNOWNs
	        if (lhsType.type != Expression.UNKNOWN_TYPE && rhsType.type != Expression.UNKNOWN_TYPE) { 
	            notYetImplemented(" expression of different types " + lhsType.type + " " + operator + " " + rhsType.type);
	        }
        }

        if ("=".equals(operator) || "IN".equals(operator)) {
            if (!lhsType.isRelation && !rhsType.isRelation) {
                buffer.append(lhsType.ql + " ^ " + rhsType.ql);
            } else if (lhsType.isRelation && !rhsType.isRelation) {
                buffer.append("(" + lhsType.ql + ") . " + rhsType.ql+"");
                //buffer.append("rng((" + lhsType.ql + ") o " + rhsType.ql+")");
            } else if (!lhsType.isRelation && rhsType.isRelation) {
                buffer.append("" + rhsType.ql + " . (" + lhsType.ql +")");
                //buffer.append("dom("+lhsType.ql + " o (" + rhsType.ql + "))");
            } else if (lhsType.isRelation && rhsType.isRelation) {
                if (!lhsType.root.isVariable() && !rhsType.root.isVariable()) {  
                    buffer.append("dom( id(" + getExtent(lhsType.root) + ") ^ (");
                } else {
                    buffer.append("dom( (" + getExtent(lhsType.root) + " X " + getExtent(rhsType.root) + ") ^ (");
                } 
                buffer.append(lhsType.ql);
                buffer.append(" o ");
                buffer.append("inv(" + rhsType.ql + ")");
                buffer.append("))");
            }
        } else if ("!=".equals(operator) || "NOT IN".equals(operator)) {
            if (!lhsType.isRelation && !rhsType.isRelation) {
                buffer.append(lhsType.ql + " - " + rhsType.ql);
            } else if (lhsType.isRelation && !rhsType.isRelation) {
                buffer.append("(rng(" + lhsType.ql + ") - " + rhsType.ql+")");
            } else if (!lhsType.isRelation && rhsType.isRelation) {
                buffer.append("("+lhsType.ql + " - rng(" + rhsType.ql + "))");
            } else if (lhsType.isRelation && rhsType.isRelation) {
//                if (!lhsType.root.isVariable() && !rhsType.root.isVariable()) {  
//                    buffer.append("dom( id(" + getExtent(lhsType.root) + ") ^ (");
//                } else {
//                    buffer.append("dom( (" + getExtent(lhsType.root) + " X " + getExtent(rhsType.root) + ") ^ (");
//                } 
//                buffer.append(lhsType.ql).append(" - (");
//                buffer.append(lhsType.ql);
//                buffer.append(" o ");
//                buffer.append("inv(" + rhsType.ql + ")");
//                buffer.append(")))");
//                if (!lhsType.root.isVariable() && !rhsType.root.isVariable()) {  
//                    buffer.append("dom( id(" + getExtent(lhsType.root) + ") ^ (");
//                } else {
//                    buffer.append("dom( (" + getExtent(lhsType.root) + " X " + getExtent(rhsType.root) + ") ^ (");
//                } 
                buffer.append("(" + getExtent(lhsType.root) + " - dom(");
                buffer.append(lhsType.ql);
                buffer.append(" o ");
                buffer.append("inv(" + rhsType.ql + ")");
                buffer.append("))");
            }
        } else {
            notYetImplemented(" operator for expressions " + rootExpr.getPath() + " " + operator + " " + joinExpr.getPath());
        }
        //SMA: this might not be required as the relationship traversal has already been taken care off.
        //if (lhsType.isRelation ^ rhsType.isRelation && !fromExpr.getType().equals(rootExpr.getType())) {
        //    translateWhereExpression(buffer, fromExpr);
        //}
    }

    protected void translateWhereExpression(StringBuffer buffer, Expression expr) {
        if (expr.getParent() != null) {
            String invRelation = getRelation(expr, true);
            project(buffer, invRelation);
            translateWhereExpression(buffer, expr.getParent());
        } else {
            intersect(buffer, getExtent(expr));
//            intersect(buffer, getExtent(expr.getType()));
        }
    }

    protected QLExpression translateWhereConditionExpression(Expression expr) {
        QLExpression result;
        if (expr.getParent() != null) {
            result = translateWhereConditionExpression(expr.getParent());
        } else {
            result = new QLExpression();
            result.type = expr.getType();
            result.ql = getExtent(expr);
            result.root = expr;
//            result.rootType = result.type;
            return result;
        }
        if (expr.isRelation() || expr.isTransitiveRelation()) {
            result.ql += " o " + getRelation(expr);
            result.ql += " o " + getExtent(expr.getType());
            result.isRelation = true;
            result.type = expr.getType();
        }
        return result;
    }
    protected String translateWhereCondition(StringBuffer buffer, Expression expr) {
        String result;
        if (expr.getParent() != null) {
            result = translateWhereCondition(buffer, expr.getParent());
        } else {
            buffer.append(getExtent(expr.getType()));
            return expr.getType();
        }
        if (expr.isRelation() || expr.isTransitiveRelation()) {
            buffer.append(" o ").append(getRelation(expr));
            buffer.append(" o ").append(getExtent(expr.getType()));
        }
        return result;
    }
    protected void translateWhereConditionWithLiteral(
        StringBuffer buffer,
        Expression pathExpr,
        Expression literalExpr,
        String operator) {
        Expression parent = null;
        if (pathExpr.isAttribute() && !"name".equals(pathExpr.getName())) {
            if (pathExpr.getParent() == null) {
                throw new IllegalArgumentException("Cannot query attribute with null parent");
            }
            filter(buffer, getExtent(pathExpr.getParent().getType()), pathExpr.getName(), literalExpr.getName(), operator);
            pathExpr = pathExpr.getParent();
        } else {
            if ("name".equals(pathExpr.getName())) {
                if (pathExpr.getParent() == null) {
                    throw new IllegalArgumentException("Cannot query attribute name with null parent");
                }
                pathExpr = pathExpr.getParent();
            }
            filter(buffer, getExtent(pathExpr.getType()), null, literalExpr.getName(), operator);
        }
        translateWhereExpression(buffer, pathExpr);
    }

    protected void filter(StringBuffer buffer, String extent, String attribute, String literalExpr, String operator) {
        if (attribute == null) {
            if ("=".equals(operator) || "IN".equals(operator)) {
                buffer.append(extent + " ^ { " + literalExpr + " }");
            } else if ("NOT IN".equals(operator)) {
                buffer.append("  ("+extent+" - ("+extent + " ^ { " + literalExpr + " }))  ");
            } else if ("!=".equals(operator) || ">".equals(operator) || ">=".equals(operator) || "<".equals(operator) || "<=".equals(operator)) {
                buffer.append(extent + "[&0 " + operator + " " + literalExpr + "]");
            } else if ("LIKE".equals(operator)) {
                buffer.append(extent + "[&0 =~ " + literalExpr + "]");
            } else if ("INSTANCEOF".equals(operator)) {
                buffer.append("instanceof+ . { " + literalExpr + " }");
            } else {
                notYetImplemented(" operator " + operator);
            }
        } else {
            if ("=".equals(operator) || "IN".equals(operator)) {
                buffer.append("@" + attribute + " . { " + literalExpr + " }");
            } else if ("!=".equals(operator) || ">".equals(operator) || ">=".equals(operator) || "<".equals(operator) || "<=".equals(operator)) {
                buffer.append("dom(@" + attribute + "[&1 " + operator + " " + literalExpr + " ])");
            } else if ("LIKE".equals(operator)) {
                buffer.append("dom(@" + attribute + "[&1 =~ " + literalExpr + " ])");
            } else {
                notYetImplemented(" operator " + operator);
            }
            intersect(buffer, extent);
        }

    }

    protected static StringBuffer id(StringBuffer buffer) {
        buffer.insert(0, "id(").append(")");
        return buffer;
    }

    private void join(StringBuffer buffer, String set2) {
        buffer.append(" o ").append(set2);
    }

    private void project(StringBuffer buffer, String set) {
        bracket(buffer).append(" . ").append(set);
    }

    private void compose(StringBuffer buffer, String set) {
        buffer.append(" ** ").append(set);
    }

    private void product(StringBuffer buffer, String set) {
        buffer.append(" X ").append(set);
    }

    private void intersect(StringBuffer buffer, String set) {
        bracket(buffer).append(" ^ ").append(set);
    }

    protected String getRelation(Expression expr) {
        return getRelation(expr, false);
    }
    protected String getRelation(Expression expr, boolean inverse) {
        if (expr.isVariable()) {
            return expr.getName();
        }
        if (expr.isAttribute()) {
            notYetImplemented("relations for attributes: "+ expr.getPath() + " ("+expr.getType()+")");
        }
        if (expr.getParent() == null) {
            throw new IllegalArgumentException("Cannot determine relation name for null parent: " + expr.getName() +" "+ expr.getPath());
        }
        RelationshipInfo ri = new RelationshipInfo(expr.getParent().getType(), expr.getName());
        if (!inverse) {
            return ri.getForwardRel().getMappedName() + (expr.isTransitiveRelation() ? "+" : "");
        } else {
            return ri.getReverseRel().getMappedName() + (expr.isTransitiveRelation() ? "+" : "");
        }
    }

    protected String getExtent(String name) {
        MetaType type = FactMetaModel.getInstance().lookupMetaType(name);
        if (type == null) {
            throw new IllegalArgumentException("Cannot MetaType: " + name);
        }
        return type.getMappedName();
    }
    
    protected String getExtent(Expression expr) {
        if (expr.isVariable()) {
            return expr.getName();
        } else {
            MetaType type = FactMetaModel.getInstance().lookupMetaType(expr.getType());
            if (type == null) {
                throw new IllegalArgumentException("Cannot MetaType: " + expr.getType());
            }
            return type.getMappedName();
        }
    }

    protected static StringBuffer bracket(StringBuffer buffer) {
        buffer.insert(0, "(").append(")");
        return buffer;
    }


    protected Expression getRootFromExpression(Map fromClause) {
        Expression result = null;
        for (Iterator iter = fromClause.values().iterator(); iter.hasNext();) {
            Expression from = (Expression) iter.next();
            if (from.getParent() == null) {
                if (result == null) {
                    result = from;
                } else if (!result.getPath().equals(from.getPath())) {
                    notYetImplemented(" queries with multiple from clauses '" + result.getPath() + "' & '" + from.getPath() + "'");
                }
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("No root from clause found in query");
        }
        return result;
    }

    protected Expression getFromExpression(Map fromClause, Expression selectProjection) {
        Expression result = (Expression) fromClause.get(selectProjection.getPath());
        if (result != null) {
            return result;
        }
        if (selectProjection.getParent() == null) {
            throw new IllegalArgumentException("From clause could not be found");
        } else {
            return getFromExpression(fromClause, selectProjection.getParent());
        }
    }

//    public static void notYetImplemented(String message) {
//        throw new NotYetImplementedException("REQL translator cannot handle " + message);
//    }
    public void notYetImplemented(String message) {
        System.err.println(query);
        throw new NotYetImplementedException("REQL translator cannot handle " + message);
    }
    public void warn(String message) {
        System.err.println("Warning: REQL translator cannot handle " + message);
    }
    private class QLExpression {
        boolean isVariable;
        boolean isRelation;
        Expression root;
//        String rootType;
        String type;
        String ql;
    }
    
}
