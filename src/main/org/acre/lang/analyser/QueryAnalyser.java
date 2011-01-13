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

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Semantic analyser for Queries
 * 
 * @author Syed Ali
 */
public class QueryAnalyser {

    private QueryModel model;
    private ScriptModel scriptModel;

    private AListProjectionAttributes selectClause;

    public QueryAnalyser(ScriptModel scriptModel) {
        this.scriptModel = scriptModel;
        this.model = new QueryModel(null);
    }

    private static boolean isLogicalOperator(String operator) {
        //System.out.print("isLogicalOperator : '" + operator + "'");
        if ("AND".equals(operator) || "OR".equals(operator)) {
            return true;
        }
        return false;
    }
    
    public void processWhereClause(AWhereClause node) {
        Condition c = parseWhereClauseExpression(node.getExpr());
//        if (node.getAll() != null) {
//            c.collCondition = Condition.COLL_ALL;
//        }
        if (c != null) {
            model.addWhereConditions(c);
        }
    }

    public void processSelectClause() {
        processProjectionList(selectClause.getProjectionList());
//        complete = true;
    }
    
    public void processProjectionList(PProjectionList node) {
        if (node instanceof ASingleProjectionList) {
            ASingleProjectionList aSingleProjectionList = (ASingleProjectionList) node;
            processProjection(aSingleProjectionList.getProjection());
        } else if (node instanceof AProjectionList) {
            AProjectionList aProjectionList = (AProjectionList) node;
            processProjection(aProjectionList.getProjection());
            processProjectionList(aProjectionList.getProjectionList());
        } else {
            notYetImplemented("processProjectionList: ", node);
        }
    }

    public void processProjection(PProjection node) {
        if (node instanceof AExprProjection) {
            AExprProjection aExprProjection = (AExprProjection) node;
            Expression e = parseExpression(aExprProjection.getExpr(), null);
            AAsIdentifier aAsIdentifier = (AAsIdentifier) aExprProjection.getAsIdentifier();
            if (aAsIdentifier != null) {
                // add e to variables
                String alias = aAsIdentifier.getIdentifier().getText();
                if (alias != null && FactMetaModel.getInstance().isMetaType(alias)) {
                    QueryAnalyser.err(Messages.getString("QueryWalker.Error.Message.PRE_DEFINE_VARIABLE_OVERRIDE") + " for variable :" + alias, aAsIdentifier.getIdentifier());
                }
                e.alias = alias;
            }
            model.addSelectExpressions(e);
            aExprProjection.getAsIdentifier();
        } else {
            notYetImplemented("processProjection: ", node);
        }
    }

    public void processFromClauseList(PFromClauseList fromClauseList) {
        if (fromClauseList instanceof ASingleFromClauseList) {
            ASingleFromClauseList aSingleFromClauseList = (ASingleFromClauseList) fromClauseList;
            processIterator(aSingleFromClauseList.getIteratorDef());
        } else if (fromClauseList instanceof AFromClauseList) {
            AFromClauseList aFromClauseList = (AFromClauseList) fromClauseList;
            processFromClauseList(aFromClauseList.getFromClauseList());
            processIterator(aFromClauseList.getIteratorDef());
        } else {
            notYetImplemented("processFromClauseList: ", fromClauseList);
        }
    }

    public void processIterator(PIteratorDef iteratorDef) {
        if (iteratorDef instanceof AIteratorDef) {
            AIteratorDef aIteratorDef = (AIteratorDef) iteratorDef;
            if (scriptModel.isTargetSQL() && aIteratorDef.getExpr() instanceof ANestedExpr) 
                notYetImplemented("processIterator: ", iteratorDef);
            Expression e = parseExpression(aIteratorDef.getExpr(), null);
            AAsIdentifierOptAs aAsIdentifierOptAs = (AAsIdentifierOptAs) aIteratorDef.getAsIdentifierOptAs();
            if (aAsIdentifierOptAs != null) {
                // add e to variables
                String alias = aAsIdentifierOptAs.getIdentifier().getText();
                if (alias != null && FactMetaModel.getInstance().isMetaType(alias)) {
                    QueryAnalyser.err(Messages.getString("QueryWalker.Error.Message.PRE_DEFINE_VARIABLE_OVERRIDE") + " for variable :" + alias, aAsIdentifierOptAs.getIdentifier());
                }
                if (model.getFromClauseExpression(alias) != null) {
                    QueryAnalyser.err(Messages.getString("QueryWalker.Error.Message.ALIAS_ALREADY_EXISTS") + " for variable "+e.path+" :" + alias, aAsIdentifierOptAs.getIdentifier());
                }
                
                e.alias = alias;
            } else {
            }
            if (e.isModel()) {
                QueryAnalyser.err(Messages.getString("QueryWalker.Error.Message.MODEL_QUERYING") + " :" + e.path, aAsIdentifierOptAs);
            }
            model.addFromExpressions(e);
        } else {
            notYetImplemented("processIterator: ", iteratorDef);
        }
    }
    
    public Expression processNestedQuery(ASelectExpr node) {
        // Log in not right currently
        QueryAnalyser nestedQuery = new QueryAnalyser(scriptModel);
        nestedQuery.getQuery().setParent(model);
        if (node.getSelectX() instanceof ASelectX) {
            ASelectX aSelectX = (ASelectX) node.getSelectX();
            if (aSelectX.getProjectionAttributes() instanceof AListProjectionAttributes) {
                nestedQuery.setSelectClause((AListProjectionAttributes) aSelectX.getProjectionAttributes());
            } else {
                notYetImplemented("processNestedQuery", aSelectX.getProjectionAttributes());
            }
            if (aSelectX.getFromClause() instanceof AFromClause) {
                AFromClause aFromClause = (AFromClause) aSelectX.getFromClause();
                nestedQuery.processFromClauseList(aFromClause.getFromClauseList());
            } else {
                notYetImplemented("processNestedQuery", aSelectX.getFromClause());
            }
            if (aSelectX.getWhereClause() != null) {
                if (aSelectX.getWhereClause() instanceof AWhereClause) {
                    //AWhereClause aWhereClause = (AWhereClause) aSelectX.getWhereClause();
                    nestedQuery.processWhereClause(
                        (AWhereClause) aSelectX.getWhereClause());
                } else {
                    notYetImplemented("processNestedQuery", aSelectX.getWhereClause());
                }
            }
            nestedQuery.processSelectClause();
        } else {
            notYetImplemented("processNestedQuery", node.getSelectX());
        }
        
        
        if (scriptModel.isTargetSQL()) {
            return Expression.makeExpression(nestedQuery.getQuery());
        } else {
            return scriptModel.getTemporaryVariable(nestedQuery.getQuery());
        }
    }

    public Condition parseWhereClauseCondition(PQuery node) {
        Condition result = null;
        if (false) {
        } else if (node instanceof AQuery) {
            AQuery aQuery = (AQuery) node;
            return parseWhereClauseExpression(aQuery.getExpr());
        } else {
            notYetImplemented("parseWhereClauseCondition", node);
        }
        return result;
    }
    
    public Condition parseWhereClauseExpression(PExpr expr) {
        Condition result = null;
        try {
            if (false) {
            } else if (expr instanceof AEqualityExpr) {
                AEqualityExpr aEqualityExpr = (AEqualityExpr) expr;
                result = new Condition();
                result.setLeft(parseExpression(aEqualityExpr.getLeft(), null));
                result.setRight(parseExpression(aEqualityExpr.getRight(), null));
                result.setOperator(aEqualityExpr.getEqne().toString().trim());
                result.validate();
                return result;
            } else if (expr instanceof ALikeExpr) {
                ALikeExpr aLikeExpr = (ALikeExpr) expr;
                result = new Condition();
                result.setLeft(parseExpression(aLikeExpr.getLeft(), null));
                result.setRight(parseExpression(aLikeExpr.getRight(), null));
                result.setOperator(aLikeExpr.getLike().getText().toUpperCase());
                result.validate();
                return result;
            } else if (expr instanceof ARlikeExpr) {
                ARlikeExpr aLikeExpr = (ARlikeExpr) expr;
                result = new Condition();
                result.setLeft(parseExpression(aLikeExpr.getLeft(), null));
                result.setRight(parseExpression(aLikeExpr.getRight(), null));
                result.setOperator(aLikeExpr.getRlike().getText().toUpperCase());
                result.validate();
                return result;
            } else if (expr instanceof ARelationalExpr) {
                ARelationalExpr aRelationalExpr = (ARelationalExpr) expr;
                result = new Condition();
                result.setLeft(parseExpression(aRelationalExpr.getLeft(), null));
                result.setRight(parseExpression(aRelationalExpr.getRight(), null));
                result.setOperator(aRelationalExpr.getCompareToken().toString().trim().toUpperCase());
                result.validate();
                return result;
            } else if (expr instanceof AInExpr) {
                AInExpr aInExpr = (AInExpr) expr;
                result = new Condition();
                result.setLeft(parseExpression(aInExpr.getLeft(), null));
                result.setRight(parseInList(aInExpr.getRight()));
                result.setOperator(
                    (aInExpr.getNot() != null ? aInExpr.getNot().getText().toUpperCase() +" " : "") 
                    + aInExpr.getIn().getText().toUpperCase());
                result.validate();
                return result;
            } else if (expr instanceof AInstanceofExpr) {
                AInstanceofExpr aInstanceofExpr = (AInstanceofExpr) expr;
                result = new Condition();
                result.setLeft(parseExpression(aInstanceofExpr.getLeft(), null));
                result.setRight(parseExpression(aInstanceofExpr.getRight(), null));
                result.setOperator(aInstanceofExpr.getInstanceof().getText().toUpperCase());
                result.validate();
                return result;
            } else if (expr instanceof AAndExpr) {
                AAndExpr aAndExpr = (AAndExpr) expr;
                result = new Condition();
                result.setLeft(parseWhereClauseExpression(aAndExpr.getLeft()));
                result.setRight(parseWhereClauseExpression(aAndExpr.getRight()));
                result.setOperator(aAndExpr.getAnd().getText().toUpperCase());
                result.validate();
                return result;
            } else if (expr instanceof AOrExpr) {
                AOrExpr aOrExpr = (AOrExpr) expr;
                result = new Condition();
                result.setLeft(parseWhereClauseExpression(aOrExpr.getLeft()));
                result.setRight(parseWhereClauseExpression(aOrExpr.getRight()));
                result.setOperator(aOrExpr.getOr().getText().toUpperCase());
                result.validate();
                return result;
            } else if (expr instanceof ANestedExpr) {
                ANestedExpr aNestedExpr = (ANestedExpr) expr;
                result = parseWhereClauseCondition(aNestedExpr.getQuery());
                return result;
            } else {
                notYetImplemented("parseWhereClauseExpression", expr);
            }
        } catch (AnalyserException ae) {
            ae.setNode(expr);
            throw ae;
        }
        return result;
    }

    public Expression parseExpression(PQuery node, Expression parent) {
        Expression result = null;
        if (false) {
        } else if (node instanceof AQuery) {
            AQuery aQuery = (AQuery) node;
            return parseExpression(aQuery.getExpr(), parent);
        } else {
            notYetImplemented("parseExpression", node);
        }
        return result;
    }
    
    public Expression parseExpression(PExpr expr, Expression parent) {
        String path = "";
        if (parent != null && parent.path != null && parent.path.trim().length() > 0) {
            path = path + ".";
        }
        Expression result;
        if (expr instanceof AIdentifierExpr) {
            AIdentifierExpr aIdentifierExpr = (AIdentifierExpr) expr;
            String identifier = aIdentifierExpr.getIdentifier().getText();
            result = getExpression(parent, identifier);
            if (result == null) {
                err(Messages.getString("QueryWalker.Error.Message.PATH_NOT_FOUND") + " \"" 
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
        } else if (expr instanceof ALiteralExpr) {
            ALiteralExpr aLiteralExpr = (ALiteralExpr) expr;
            return parseLiteral(aLiteralExpr.getLiteral());
        } else if (expr instanceof ANestedExpr) {
            ANestedExpr aNestedExpr = (ANestedExpr) expr;
            return parseExpression(aNestedExpr.getQuery(), parent);
        } else if (expr instanceof AAggregateCountExpr) {
            AAggregateCountExpr aAggregateCountExpr = (AAggregateCountExpr) expr;
            Expression expression = parseExpression(aAggregateCountExpr.getQuery(), parent);
            expression.function = aAggregateCountExpr.getCount().getText().toUpperCase();
            return expression;
        } else if (expr instanceof AAggregateAvgExpr) {
            AAggregateAvgExpr AAggregateAvgExpr = (AAggregateAvgExpr) expr;
            Expression expression = parseExpression(AAggregateAvgExpr.getQuery(), parent);
            expression.function = AAggregateAvgExpr.getAvg().getText().toUpperCase();
            if (!isAggregate(expression.getType()))
                err("Cannot aggregate " + expression.path + " with function " + expression.function, expr);
            return expression;
        } else if (expr instanceof AAggregateMaxExpr) {
            AAggregateMaxExpr AAggregateMaxExpr = (AAggregateMaxExpr) expr;
            Expression expression = parseExpression(AAggregateMaxExpr.getQuery(), parent);
            expression.function = AAggregateMaxExpr.getMax().getText().toUpperCase();
            if (!isAggregate(expression.getType()))
                err("Cannot aggregate " + expression.path + " with function " + expression.function, expr);
            return expression;
        } else if (expr instanceof AAggregateMinExpr) {
            AAggregateMinExpr AAggregateMinExpr = (AAggregateMinExpr) expr;
            Expression expression = parseExpression(AAggregateMinExpr.getQuery(), parent);
            expression.function = AAggregateMinExpr.getMin().getText().toUpperCase();
            if (!isAggregate(expression.getType()))
                err("Cannot aggregate " + expression.path + " with function " + expression.function, expr);
            return expression;
        } else if (expr instanceof AAggregateSumExpr) {
            AAggregateSumExpr AAggregateSumExpr = (AAggregateSumExpr) expr;
            Expression expression = parseExpression(AAggregateSumExpr.getQuery(), parent);
            expression.function = AAggregateSumExpr.getSum().getText().toUpperCase();
            if (!isAggregate(expression.getType()))
                err("Cannot aggregate " + expression.path + " with function " + expression.function, expr);
            return expression;
//        } else if (expr instanceof AMethodExpr) {
//            AMethodExpr AMethodExpr = (AMethodExpr) expr;
//            Expression expression = ???;
//            expression.function = AMethodExpr.getIdentifier().getText().toUpperCase();
//            return expression;
        } else if (expr instanceof ASelectExpr) {
            return processNestedQuery((ASelectExpr) expr);
        } else {
            notYetImplemented("parseExpression", expr);
        }
        return null;
    }
    
    public Expression parseInList(LinkedList list) {
        Expression result = null;
        for (Iterator iter = list.iterator(); iter.hasNext();) {
            Node expr = (Node) iter.next();
            Expression elementExpr = null;
            if (expr instanceof PExpr) {
                elementExpr = parseExpression((PExpr) expr, null);
            } else if (expr instanceof ANestedExpr) {
                ANestedExpr aNestedExpr = (ANestedExpr) expr;
                elementExpr = parseExpression(aNestedExpr.getQuery(), null);
            } else {
              notYetImplemented("parseInList", expr);
            }
            if (result == null) {
                result = elementExpr;
            } else if (result.isLiteral()) {
                if (result.type.equals(elementExpr.type)) {
                    result.name +=  "," + elementExpr.name;
                    result.path = result.name;
                } else {
                    err(Messages.getString("QueryWalker.Error.Message.IN_CLAUSE_DIFFERENT_LITERALS") + ": " + result.path + "("+result.alias+") & " + elementExpr.path + "("+elementExpr.type +")", expr);
                }
            } else {
                err(Messages.getString("QueryWalker.Error.Message.IN_CLAUSE_MORE_THAN_ONE") + ": " + result.path + " & " + elementExpr.path, expr);
            }
        }
        return result;
    }
    
    public Expression parseLiteral(PLiteral node) {
        Expression result = new Expression();
        if (false) {
        } else if (node instanceof ABooleanLiteral) {
            ABooleanLiteral aBooleanLiteral = (ABooleanLiteral) node;
            result.name = "\"" + aBooleanLiteral.toString().trim().toLowerCase() + "\"";
            result.type = "Boolean";
        } else if (node instanceof ACharLiteral) {
            ACharLiteral aCharLiteral = (ACharLiteral) node;
            result.name = aCharLiteral.getCharLiteral().getText();
            result.type = "Character";
            //        } else if (node instanceof ADateLiteral) {
            //            ADateLiteral aDateLiteral = (ADateLiteral) node;
            //            result.name =  ((APDate) aDateLiteral.getPDate()).getText();
            //            result.type = "Date";
        } else if (node instanceof ADoubleLiteral) {
            ADoubleLiteral aDoubleLiteral = (ADoubleLiteral) node;
            result.name = "\"" + aDoubleLiteral.getDoubleLiteral().getText() + "\"";
            result.type = "Double";
        } else if (node instanceof ALongLiteral) {
            ALongLiteral aLongLiteral = (ALongLiteral) node;
            result.name = aLongLiteral.getLongLiteral().getText();
            result.type = "Long";
        } else if (node instanceof ANilLiteral) {
            ANilLiteral aNilLiteral = (ANilLiteral) node;
            result.name = aNilLiteral.getNil().getText();
            result.type = "Nil";
        } else if (node instanceof AStringLiteral) {
            AStringLiteral aStringLiteral = (AStringLiteral) node;
            result.name = aStringLiteral.getStringLiteral().getText();
            result.type = "String";
            //        } else if (node instanceof ATimeLiteral) {
            //            ATimeLiteral aTimeLiteral = (ATimeLiteral) node;
            //            result.name =  aTimeLiteral.getCharLiteral().getText();
            //            result.type = "Time";
            //        } else if (node instanceof ATimestampLiteral) {
            //            ATimestampLiteral aTimestampLiteral = (ATimestampLiteral) node;
            //            result.name =  aCharLiteral.getCharLiteral().getText();
            //            result.type = "Timestamp";
            //            aTimestampLiteral
            //        } else if (node instanceof AUndefinedLiteral) {
            //            AUndefinedLiteral aUndefinedLiteral = (AUndefinedLiteral) node;
            //            result.name =  aCharLiteral.getCharLiteral().getText();
            //            result.type = "Undefined";
            //            aUndefinedLiteral
        } else {
            notYetImplemented("parseLiteral", node);
        }
        result.exprType = Expression.EXPR_LITERAL;
        result.path = result.name;
        return result;
    }


    public Expression getExpression(Expression parent, String token) {
        return scriptModel.getExpression(parent, token, model);
    }
    /*
    private Expression determineExpresionParent(String token) {
        Expression result = null;
        List variableExpressions = new ArrayList(model.getFromExpressions().size());
        for (Iterator iter = model.getFromExpressions().values().iterator(); iter.hasNext();) {
            Expression element = (Expression) iter.next();
            String pt = determineExpresionType(element, token, false);
            if (pt != null) {
                variableExpressions.add(element);
            }
        }
        if (variableExpressions.size() > 1) {
            err(Messages.getString("QueryWalker.Error.Message.AMBIGOUS_PATH") + " \"" + token + "\"");
        } else if (variableExpressions.size() == 1) {
            return (Expression) variableExpressions.get(0);
        }
        return result;
    }
    
    private String determineExpresionType(Expression parent, String token, boolean reportError) {
        MetaType parentType = null;
        if (token == null || token.length() == 0) {
            throw new IllegalArgumentException("Invalid token: " + token);
        }
        if (parent == null) {
            for (int i = 0; i < metaModel.getMetaTypes().size(); i++) {
                MetaType type = metaModel.getMetaTypes(i);
                if (token.equals(type.getName()) || token.equals(type.getMappedName())) {
                    return getType(type);
                }
            }
            if (reportError) {
                List variableExpressions = new ArrayList(model.getFromExpressions().size());
                for (Iterator iter = model.getFromExpressions().entrySet().iterator(); iter.hasNext();) {
                    Map.Entry element = (Map.Entry) iter.next();
                    String pt = determineExpresionType((Expression) element.getValue(), token, false);
                    if (pt != null) {
                        variableExpressions.add(pt);
                    }
                }
                if (variableExpressions.size() > 1) {
                    if (!reportError)
                        return null;
                    else
                        err(Messages.getString("QueryWalker.Error.Message.AMBIGOUS_PATH") + " \"" + token + "\"");
                } else if (variableExpressions.size() == 1) {
                    return (String) variableExpressions.get(0);
                }
                Expression scriptVariable = scriptModel.getVariable(token);
                if (scriptVariable != null) {
                    return scriptVariable.getType();
                }
            }
            if (!reportError)
                return null;
            else
                err(Messages.getString("QueryWalker.Error.Message.PATH_NOT_FOUND") + " \"" + token + "\"");
        } else {
            if (parent.type == null) {
                throw new IllegalArgumentException("Parent type not initialize.");
            }
            if (!parent.isStruct()) {
                parentType = metaModel.lookupInMetaTypes(parent.type);
                if (parentType == null) {
                    if (!isAttributeType(parent.type)) {
                        throw new IllegalArgumentException("Parent type '" + parent.type + "' not found for " + parent.path);
                    } else {
                        if (!reportError)
                            return null;
                        else
                            err(Messages.getString("QueryWalker.Error.Message.PATH_NOT_FOUND") + " \"" + parent.path + "." + token + "\"");
                    }
                }
                for (int i = 0; i < parentType.getMetaRelationships().size(); i++) {
                    MetaRelationship relationship = parentType.getMetaRelationships(i);
                    if (token.equals(relationship.getName()) /*|| token.equals(relationship.getMappedName())* /
                        ) {
                        return getType(relationship.getPointerMetaType());
                    }
                }

                for (int i = 0; i < parentType.getMetaAttributes().size(); i++) {
                    MetaAttribute attribute = parentType.getMetaAttributes(i);
                    if (token.equals(attribute.getName()) /*|| token.equals(attribute.getMappedName())* /
                        ) {
                        attrTypes.add(attribute.getType());
                        return attribute.getType();
                    }
                }
            } else {
                Expression v = scriptModel.getVariable(parent.getPath());
                if (v == null) {
                    throw new IllegalArgumentException("Invalid variable: " + parent.getPath());
                }
                for (int i = 0; i < v.structTypes.length; i++) {
                    if (v.structTypes[i].name.equals(token) || v.structTypes[i].alias.equals(token)) {
                        return v.structTypes[i].getType();
                    }
                }
//                if (token.equals(parent.getName())) {
//                    return null;
//                }
//                throw new RuntimeException("Cannot support struct types yet");
//                return parent.getType();
            }
            if (!reportError)
                return null;
            else
                err(Messages.getString("QueryWalker.Error.Message.PATH_NOT_FOUND") + " \"" + parentType.getMappedName() + "." + token + "\"");
        }
        return null;
    }
*/
    public static boolean isAggregate(String type) {
        return true;
//        return "long".equals(type) || "Integer".equals(type) || "Short".equals(type);
    }

    public static void warn(String message) {
        System.err.println(message);
    }

    public static void err(String message) {
        throw new AnalyserException(message);
    }

    public static void err(String message, Node node) {
        throw new AnalyserException(message, node);
    }

    public static void err(String message, String position) {
        throw new AnalyserException(message + " " + position);
    }

    public static void notYetImplemented(String method, Node node) {
        throw new NotYetImplementedException("Semantic Analyser(" + method + ") cannot handle " + node.getClass() + ": " + node);
    }

    public AListProjectionAttributes getSelectClause() {
        return selectClause;
    }

    public void setSelectClause(AListProjectionAttributes attributes) {
        selectClause = attributes;
    }

    public QueryModel getQuery() {
        return model;
    }

    public void processOrderByExpression(PExpr expr, boolean descending) {
        Expression e = parseExpression(expr, null);
        model.addOrderByExpressions(e, descending);
    }

}
