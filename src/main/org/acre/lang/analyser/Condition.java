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


import org.acre.model.metamodel.PopulateMetaModel;

/**
 * Queries model
 * 
 * @author Syed Ali
 */

public class Condition {

    public final static int COLL_NA = 0;

    public final static int COLL_ALL = 1;

    public final static int COLL_ANY = 2;

    private final static int EXPRESSION_TYPE = 1;

    private final static int CONDITION_TYPE = 2;

//    private final static int QUERY_TYPE = 3;

    //int collCondition;
    private int leftType;

    private int rightType;

    private Object left;

    private Object right;

    private String operator;

    //public int getCollCondition() {
    //    return collCondition;
    //}
    public Object getLeft() {
        return left;
    }

    public boolean isLeftExpression() {
        return leftType == EXPRESSION_TYPE;
    }

    public boolean isLeftCondition() {
        return leftType == CONDITION_TYPE;
    }

//    public boolean isLeftQuery() {
//        return leftType == QUERY_TYPE;
//    }
//
    public Object getRight() {
        return right;
    }

    public boolean isRightExpression() {
        return rightType == EXPRESSION_TYPE;
    }

    public boolean isRightCondition() {
        return rightType == CONDITION_TYPE;
    }

//    public boolean isRightQuery() {
//        return rightType == QUERY_TYPE;
//    }
//
    public void setOperator(String operator) {
        this.operator = operator;
    }

    void setLeft(Expression value) {
        leftType = EXPRESSION_TYPE;
        left = value;
    }

    void setLeft(Condition value) {
        leftType = CONDITION_TYPE;
        left = value;
    }

//    void setLeft(QueryModel value) {
//        leftType = QUERY_TYPE;
//        left = value;
//    }

    void setRight(Expression value) {
        rightType = EXPRESSION_TYPE;
        right = value;
    }

    void setRight(Condition value) {
        rightType = CONDITION_TYPE;
        right = value;
    }

//    void setRight(QueryModel value) {
//        rightType = QUERY_TYPE;
//        right = value;
//    }

    public String getOperator() {
        return operator;
    }

    public String getSQLOperatorForNull() {
        if ("=".equals(operator)) {
            return "IS";
        } else if ("!=".equals(operator)) {
            return "IS NOT";
        }
        return operator;
    }

    public void validate() {
        if (operator == null) 
            QueryAnalyser.err(Messages.getString("QueryWalker.Error.Message.OPERATOR_CANNOT_BE_NULL"));
        if (isLeftExpression() && isRightExpression()) {
            Expression left = (Expression) this.left;
            Expression right = (Expression) this.right;
            if (false) {
            } else if ("INSTANCEOF".equals(operator)) {
                
                if (!PopulateMetaModel.isOperatorValid(operator, left.getType(), right.getType())) {
                    QueryAnalyser.err(
                        Messages.getString("QueryWalker.Error.Message.OPERATOR_NOT_APPLICABLE")
                            + " \""
                            + left.getType()
                            + " "
                            + operator
                            + " "
                            + right.getType()
                            + "\"");
                }
//            } else if (!Expression.typeEquals(left, right)) {
//                //SMA: For now avoid reporting error for UNKNOWNs
//                if (left.getType() != Expression.UNKNOWN_TYPE && right.getType() != Expression.UNKNOWN_TYPE) { 
//                    QueryAnalyser.err(Messages.getString("QueryWalker.Error.Message.TYPES_NOT_MATCH") + " \"" + left.toString("", true) + " & " + right.toString("", true) + "\"");
//                }
            }
        } else if (isLeftCondition() && isRightCondition()) {
        } else {
            QueryAnalyser.err(Messages.getString("QueryWalker.Error.Message.UNBALANCED_CONDITION") + "\n" + this);
        }
    }
    
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        dump(buffer, this, "", 0);
        return buffer.toString();
    }

    public static void dump(Condition element, String indent) {
        StringBuffer buffer = new StringBuffer();
        dump(buffer, element, "", 0);
        System.out.println(buffer);
    }
    
    public static void dump(StringBuffer buffer, Condition element, String indent, long ctr) {
        //System.out.println(indent + "C" + ctr + "(");
        if (element.getLeft() != null) {
            if (element.isLeftExpression()) {
                //System.out.print(indent + element.getLeft().getPath() + "("+element.getLeft().getType()+") ");
                buffer.append(indent);
                buffer.append(((Expression) element.getLeft()).toString(indent + "  ", false));
            } else if (element.isLeftCondition()) {
                dump(buffer, (Condition) element.getLeft(), indent + "  ", ctr + 1);
            }
        }
        buffer.append(element.operator + " ");
        if (element.getRight() != null) {
            if (element.isRightExpression()) {
                buffer.append(((Expression) element.getRight()).toString(indent + "  ", false));
                //buffer.append(indent + ((Expression) element.getRight()).getPath() + " ");
                //System.out.print(indent + element.getRight().getPath() + "("+element.getRight().getType()+") ");
            } else if (element.isRightCondition()) {
                dump(buffer, (Condition) element.getRight(), indent + "  ", ctr + 1);
            }
        }
    }
}
