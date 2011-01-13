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

import org.acre.model.metamodel.MetaModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Queries model
 * 
 * @author Syed Ali
 */
public class Expression {
    public final static int EXPR_LITERAL = 10;
    public final static int EXPR_ATTRIBUTE = 20;
    public final static int EXPR_IMPLICIT_ATTRIBUTE = 21;
    public final static int EXPR_RELATION = 30;
    public final static int EXPR_TRANSITIVE_RELATION = 40;
    public final static int EXPR_QUERY = 41;
    public final static int EXPR_MODEL = 49;
    public final static int EXPR_PRE_DEFINE = 50;
    public final static int EXPR_VARIABLE = 200;
    public final static int EXPR_STRUCT = 300;
    public static final String UNKNOWN_TYPE = "UNKNOWN";

    String name;
    String alias;
    String path;
    String type;
    String function;
    int exprType;
    Expression parent;
    //Populated on for Variable expressions. Populated by ScriptModel
    String cookie;
    //Populated on for Variable expressions. Populated by ScriptModel
    int scope;
    Expression[] structTypes;
    private QueryModel query;
    
    int line = -1;
    int column = -1;

    public String getName() {
        return name;
    }
    public String getAlias() {
        return alias;
    }
    public String getPath() {
        return path;
    }
    public String getType() {
        if (isStruct()) {
            StringBuffer type = new StringBuffer("Struct[");
            for (int i = 0; i < structTypes.length; i++) {
                type.append(structTypes[i].toString("", true) + ",");
            }
            type.append("]");
            return type.toString();
        } else {
            return type;
        }
    }
    public Expression[] getStructType() {
        if (isStruct()) {
            return structTypes;
        } else {
            return new Expression[]{this};
        }
    }
    public boolean isLiteral() {
        return exprType == EXPR_LITERAL;
    }
    public boolean isAttribute() {
        return exprType == EXPR_ATTRIBUTE;
    }
    public boolean isImplicitAttribute() {
        return exprType == EXPR_IMPLICIT_ATTRIBUTE;
    }
    public boolean isTransitiveRelation() {
        return exprType == EXPR_TRANSITIVE_RELATION;
    }
    public boolean isRelation() {
        return exprType == EXPR_RELATION;
    }
    public boolean isVariable() {
        return exprType >= EXPR_VARIABLE;
    }
        
    public boolean isSystemVariable() {
        return exprType >= EXPR_PRE_DEFINE;
    }
    
    public boolean isStruct() {
        return exprType == EXPR_STRUCT || (exprType == EXPR_VARIABLE && type == null);
    }
    
    public boolean isModel() {
        return exprType == EXPR_MODEL;
    }
    
    public boolean isQuery() {
        return exprType == EXPR_QUERY;
    }
    
    public boolean isPrimitive() {
        return exprType != EXPR_STRUCT && isPrimitiveType(type);
    }

    public boolean isObject() {
        return exprType != EXPR_STRUCT && !isPrimitiveType(type) && !isAttribute() && !isLiteral();
    }

    public Expression getParent() {
        return parent;
    }

    public String getFunction() {
        return function;
    }
    
    public QueryModel getQuery() {
        return query;
    }

    public String getCookie() {
        return cookie;
    }

    public int getScope() {
        return scope;
    }

    public boolean equals(Object anObject) {
        if (this == anObject) {
            return true;
        }
        Expression anotherExpression = (Expression) anObject;
        if (anotherExpression.name == null || name == null
            || anotherExpression.path == null || path == null
        ) 
        {
            return false;
        }
        if (
            anotherExpression.name.equals(name)
            && anotherExpression.path.equals(path)
            && ((alias == null && anotherExpression.alias == null) || anotherExpression.alias.equals(alias))
            && ((type == null && anotherExpression.type == null) || anotherExpression.type.equals(type))
            && ((function == null && anotherExpression.function == null) || anotherExpression.function.equals(function))
            && anotherExpression.exprType == exprType
        ) 
        {
            return true;
        }
        return false;
    }
    
    public String toString() {
        return toString("", false);
    }
    
    public String toString(String indent, boolean full) {
        String result;
        if (isQuery())
            result = "(\n" + query.toString(indent) + ")\n";
        else if (full) {  
            result = (alias != null ? alias + ":" : "")  
//            + name + (!name.equals(path) ? "-" + path : "") 
            + path + "(" + getType() + ")";
        } else { 
            result = path + " ";
        }
        if (function != null) {
            result = function + "(" + result + ")";
        }
        return result;
    }
    
    public Object clone() {
        Expression newExpression = new Expression();  
        newExpression.name = name;
        newExpression.alias = alias;
        newExpression.path = path;
        newExpression.type = type;
        newExpression.function = function;
        newExpression.exprType = exprType;
        newExpression.line = line;
        newExpression.column = column;
        return newExpression;
    }
    
    public static Expression makeExpression(MetaModel model) {
        Expression e = makeExpression(model.getName(), model.getName());
        e.exprType = Expression.EXPR_MODEL;
        return e;
    }
    public static Expression makeExpression(String typeName, String name) {
        if (typeName == null) {
            throw new IllegalArgumentException("MetaType cannot be NULL");
        }
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("Invalid token: " + name);
        }

        Expression result = new Expression();
        result.name = name;
        result.alias = name;
        result.type = typeName;
        result.exprType = Expression.EXPR_PRE_DEFINE;
        result.path = name;
        result.parent = null;
        result.line = -1;
        return result;
    }
    public static Expression makeExpression(QueryModel query) {
        return makeExpression(null, query);
    }
    public static Expression makeExpression(String name, QueryModel query) {
        Expression result = makeExpression(name, query.getSelectExpressions());
        result.query = query;
        result.exprType = EXPR_QUERY;
        return result;
    }
    
    public static Expression makeExpression(String name, List structExpressions) {
        if (structExpressions.size() == 1 && !((Expression) structExpressions.get(0)).isStruct()) {
            return makeExpression(name, (Expression) structExpressions.get(0));
        }
        Expression result =  new Expression();
        result.name = name;
        result.path = name;
        result.exprType = EXPR_STRUCT;
        List structTypes = new ArrayList(structExpressions.size());
        for (int i = 0; i < structExpressions.size(); i++) {
            Expression expr = (Expression) structExpressions.get(i);
            if (expr.isStruct()) {
                for (int j = 0; j < expr.structTypes.length; j++) {
                    Expression expr1 = (Expression) expr.structTypes[j].clone();
//                    if (!expr.isLiteral()) {
//                        expr1.path = result.path + "." + expr1.path.substring(expr.path.length()+1);
//                    }
                    expr1.parent = result;
                    structTypes.add(expr1);
                }
            } else {
                Expression expr1 = (Expression) expr.clone();
//                if (!expr.isLiteral()) {
//                    expr1.path = result.path + "." + expr1.path;
//                }
                expr1.parent = result;
                structTypes.add(expr1);
            }
        }
        result.structTypes = new Expression[structTypes.size()];
        structTypes.toArray(result.structTypes);
        return result;
    }
    public static Expression makeExpression(String name, Expression type) {
        Expression result = (Expression) type.clone();
        result.name = name;
        result.path = name;
        result.alias = null;
        result.exprType = EXPR_VARIABLE;
        return result;
    }

    public static boolean isPrimitiveType(String type) {
        return "boolean".equalsIgnoreCase(type)
        || "byte".equalsIgnoreCase(type)
        || "short".equalsIgnoreCase(type)
        || "int".equalsIgnoreCase(type)
        || "long".equalsIgnoreCase(type)
        || "float".equalsIgnoreCase(type)
        || "double".equalsIgnoreCase(type)
        || "byte".equalsIgnoreCase(type)
        || "Integer".equalsIgnoreCase(type)
        || "Decimal".equalsIgnoreCase(type)
        || "BigDecimal".equalsIgnoreCase(type)
        || "BigInteger".equalsIgnoreCase(type)
        || "String".equalsIgnoreCase(type)
        || "Date".equalsIgnoreCase(type)
        || "Time".equalsIgnoreCase(type)
        || "Timestamp".equalsIgnoreCase(type)
        ;
    }
    
    public boolean isNilLiteral() {
        return isLiteral() && "nil".equalsIgnoreCase(getType());
    }    

    public static boolean typeEquals(Expression expr1, Expression expr2) {
        if (!"nil".equalsIgnoreCase(expr1.getType()) && !"nil".equalsIgnoreCase(expr2.getType())) {
            if (expr1.getType() != null && expr2.getType() != null && !expr1.getType().equals(expr2.getType())) {
                return false;
            }
        }
        return true;
    }
    public static boolean isValidTransitivePath(Expression expr) {
        if (expr == null || expr.getParent() == null || !typeEquals(expr, expr.getParent())) {
            return false;
        }
        return true;
    }
}