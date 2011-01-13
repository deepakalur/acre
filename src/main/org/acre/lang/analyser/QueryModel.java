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

import java.util.*;

/**
 * Queries meta model for both PQL as well as transformed SQL
 * 
 * @author Syed Ali
 */
public class QueryModel {
    
    protected QueryModel parent = null;
    protected QueryModel union = null;
    protected Expression assignVariable = null;
    protected Map fromVariables = new HashMap();
    protected Map fromExpressions = new HashMap();
    protected List selectExpressions = new ArrayList();
    protected List orderByExpressions = new ArrayList();
    protected Set whereConditions = new HashSet();

    public QueryModel(QueryModel parent) {
        this.parent = parent;
    }
    public List getSelectExpressions() {
        return selectExpressions;
    }
    
    public void addSelectExpressions(Expression e) {
        addSelectExpressions(e, true);
    }
    public void addSelectExpressions(Expression e, boolean insert) {
        if (insert && selectExpressions.size() > 0) {
            selectExpressions.add(0, e);
        } else {
            selectExpressions.add(e);
        }
    }

    public List getOrderByExpressions() {
        return orderByExpressions;
    }
    
    public void addOrderByExpressions(Expression e, boolean descending) {
        if (e.isAttribute() || e.isRelation()) {
            orderByExpressions.add(new OrderByStruct(e, descending));
        } else {
            throw new IllegalArgumentException("Cannot add expression '"+e.getPath()+"' of type " + e.getType());
        }
    }

    public Map getFromExpressions() {
        return fromExpressions;
    }

    public void addFromExpressions(Expression e) {
        if (e.alias != null) {
            fromVariables.put(e.alias, e);
        } else {
            fromVariables.put(e.path, e);
        }
        fromExpressions.put(e.path, e);
    }

    protected void addFromExpressions(String path, Expression e) {
        fromExpressions.put(path, e);
        if (e.alias != null) {
            fromVariables.put(e.alias, e);
        } else {
            fromVariables.put(e.path, e);
        }
    }

    public Set getWhereConditions() {
        return whereConditions;
    }
        
    public void addWhereConditions(Condition c) {
        whereConditions.add(c);
    }
        
    public String toString() {
        return toString("");
    }
    public String toString(String indent) {
        StringBuffer result = new StringBuffer();
        if (assignVariable != null) {
            result.append(indent).append("ASSIGN " + assignVariable.toString("", true) + " AS \n");
        }
        result.append(indent).append("SELECT ");
        for (Iterator iter = selectExpressions.iterator(); iter.hasNext();) {
            Expression expr = (Expression) iter.next();
            result.append(expr.toString("  ", true));
            result.append(", ");
        }
        result.append("FROM ");
        for (Iterator iter = fromExpressions.entrySet().iterator(); iter.hasNext();) {
            Map.Entry element = (Map.Entry) iter.next();
            Expression expr = ((Expression) (element.getValue()));
            result.append(expr.toString("  ", true));
            result.append(", ");
        }
        if (whereConditions.size() > 0) {
            result.append("WHERE \n");
            for (Iterator iter = whereConditions.iterator(); iter.hasNext();) {
                Condition element = (Condition) iter.next();
                result.append(indent).append(element);
                result.append('\n');
            }
        } else {
            result.append("\n");
        }
        if (orderByExpressions.size() > 0) {
            result.append("ORDER BY ");
            for (Iterator iter = orderByExpressions.iterator(); iter.hasNext();) {
                OrderByStruct element = (OrderByStruct) iter.next();
                result.append(indent).append(element.expression);
                result.append(element.isDescending() ? " DESC," : " ASC,");
            }
        } 
        if (union != null) {
            result.append("UNION\n");
            result.append(union.toString(indent)).append('\n');
        }
            
        if (indent == null || indent.length() == 0) {
            result.append(";\n");
        }
        return result.toString();
    }

    public void dump1() {
        if (assignVariable != null) {
            System.out.println("ASSIGN " + assignVariable.getName() + " (" +  assignVariable.getType()+ ") AS ");
        }
        System.out.println("SELECT ");
        for (Iterator iter = selectExpressions.iterator(); iter.hasNext();) {
            Expression expr = (Expression) iter.next();
            System.out.print(expr.getFunction() != null ? expr.getFunction() +"(" : "");
            System.out.print("  " + expr.getPath()
                    + (expr.getFunction() != null ? " )" : "")
                    + " (" + expr.getType() + ") ");
            System.out.println(expr.alias != null ? expr.alias : "");
        }
        System.out.println("FROM ");
        for (Iterator iter = fromExpressions.entrySet().iterator(); iter.hasNext();) {
            Map.Entry element = (Map.Entry) iter.next();
            Expression expr = ((Expression) (element.getValue()));
            System.out.print("  " + expr.getName() + "(" + expr.getType() + ") ");
            System.out.print(expr.alias != null ? expr.alias : "");
            System.out.println(" - "+ element.getKey());
            
        }
        System.out.println("WHERE ");
        for (Iterator iter = whereConditions.iterator(); iter.hasNext();) {
            Condition element = (Condition) iter.next();
            System.out.println(element);
        }
        System.out.println(";");
    }

    Expression getFromClauseExpression(String currentPath) {
        Expression result = (Expression) fromVariables.get(currentPath);
        if (result == null) {
            result = (Expression) fromExpressions.get(currentPath);
        }
        return result;
    }

    String getFromVariable(String path) {
        if (path != null) {
            for (Iterator iter = fromVariables.entrySet().iterator(); iter.hasNext();) {
                Map.Entry element = (Map.Entry) iter.next();
                if (path.equals(((Expression) (element.getValue())).path)) {
                    return (String) element.getKey();
                }
            }
        }
        return null;
    }

    public Expression getAssignVariable() {
        return assignVariable;
    }

    public void setAssignVariable(Expression value) {
        assignVariable = value;
    }
    public QueryModel getParent() {
        return parent;
    } 
    public void setParent(QueryModel value) {
        parent = value;
    } 
    
    public QueryModel getUnion() {
        return union;
    }

    public void setUnion(QueryModel value) {
        if (union != null) {
            union.setUnion(value);
        } else {
            union = value;
        }
    }
    public static class OrderByStruct {
        private Expression expression;
        private boolean descending;
        private OrderByStruct(Expression e, boolean ascending) {
            this.expression = e;
            this.descending = ascending;
        }
        public boolean isDescending() {
            return descending;
        }
        public Expression getExpression() {
            return expression;
        }
    }
}
