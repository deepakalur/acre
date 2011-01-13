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
package org.acre.lang;

import org.acre.lang.analysis.DepthFirstAdapter;
import org.acre.lang.node.*;

import java.util.Hashtable;
import java.util.Iterator;

/**
 * Represents a walker which is desinged to simplify the AST to the
 * form similar of ODMG OQL3 spec. It has some basic reductions
 * incorporated, further simplification can be added when needed.
 * Important: consult any changes to this file with NZDIS Team.
 * <p/>
 * <br><br>
 * SimpleAstWalker.java<br>
 *
 * @author Mariusz Nowostawski
 * @author Yury Kamen
 */
class SimpleAstWalker extends DepthFirstAdapter {

    Hashtable list = new Hashtable();

//    /** Yury Kamen: This method converts all identifiers into lower case
//     */
//    public void caseTIdentifier(TIdentifier node)
//    {
//        node.setText(node.getText().toLowerCase());
//        super.caseTIdentifier(node);
//        //System.out.println("============== Yury Kamen: Identifier: " + node.getText());
//    }


    public static void simplify(Node ast) {
        ast.apply(new SimpleAstWalker());
    }


    public void outAStandaloneSemicolonQueryProgram(AStandaloneSemicolonQueryProgram node) {
        node.replaceBy(node.getQueryProgram());
    }

    public void outAIndexAll(AIndexAll node) {
        super.outAIndexAll(node);    //To change body of overridden methods use File | Settings | File Templates.
//        node.replaceBy(new AIndexExpr());
//        node.replaceBy(new AIndexExpr((PExpr) this.list.remove(node.getExprRestricted()),
//                new TSqBracketL(), new PIndex(), new TSqBracketR()
//                ));
//         new AIndexAll()
        node.replaceBy(new ASimpleIndexAll((PExpr) this.list.remove(node.getExprRestricted())));
    }

    /**
     * ** Query ****
     */

    public void outATmpExprQuery(ATmpExprQuery node) {
        node.replaceBy(new AQuery((PExpr) this.list.remove(node.getExprRestricted())));
    }

    public void outATmpSelectQuery(ATmpSelectQuery node) {
        node.replaceBy(new AQuery(new ASelectExpr(node.getSelectX())));
    }


    public void outASelectQueryRestricted(ASelectQueryRestricted node) {
        this.list.put(node, new AQuery(new ASelectExpr(node.getSelectX())));
    }

    public void outAExprQueryRestricted(AExprQueryRestricted node) {
        this.list.put(node, new AQuery((PExpr) this.list.remove(node.getExprRestricted())));
    }


    /**
     * ** Projection ****
     */

    public void outATmpExprProjection(ATmpExprProjection node) {
        node.replaceBy(new AExprProjection((PExpr) this.list.remove(node.getExprRestricted()),
                node.getAsIdentifier()));
    }

    public void outATmpIdentifierProjection(ATmpIdentifierProjection node) {
        node.replaceBy(new AExprProjection(new AIdentifierExpr(node.getIdentifier()),
                node.getAsIdentifier()));
    }


    /**
     * ** Field ****
     */

    public void outATmpField(ATmpField node) {
        node.replaceBy(new AField(node.getIdentifier(),
                node.getColon(),
                (PExpr) this.list.remove(node.getExprRestricted())));
    }

    public void outATmpIdentifierField(ATmpIdentifierField node) {
        node.replaceBy(new AField(node.getLeft(),
                node.getColon(),
                new AIdentifierExpr(node.getRight())));
    }


    /**
     * ** Where ****
     */

    public void outATmpWhereClause(ATmpWhereClause node) {
        node.replaceBy(new AWhereClause(node.getWhere(),
                node.getAll(),
                (PExpr) this.list.remove(node.getExprRestricted())));
    }


    /**
     * ** Expr Restricted ***
     */

    public void outAExprRestricted(AExprRestricted node) {
        this.list.put(node, this.list.remove(node.getTmpCastExpr()));
    }


    /**
     * ** Cast Expr ****
     */

    public void outAOrTmpCastExpr(AOrTmpCastExpr node) {
        this.list.put(node, this.list.remove(node.getTmpOrExpr()));
    }

    public void outATmpCastExpr(ATmpCastExpr node) {
        this.list.put(node, new ACastExpr(node.getBracketL(),
                node.getIdentifier(),
                node.getBracketR(),
                (PExpr) this.list.remove(node.getTmpOrExpr())));
    }

    public void outAIdentifierTmpCastExpr(AIdentifierTmpCastExpr node) {
        this.list.put(node, new ACastExpr(node.getBracketL(),
                node.getType(),
                node.getBracketR(),
                new AIdentifierExpr(node.getExpr())));
    }

    public void outAPrimitiveIdentifierTmpCastExpr(APrimitiveIdentifierTmpCastExpr node) {
        this.list.put(node, new ACastPrimitiveExpr(node.getBracketL(),
                node.getType(),
                node.getBracketR(),
                new AIdentifierExpr(node.getIdentifier())));
    }

    public void outAPrimitiveTmpCastExpr(APrimitiveTmpCastExpr node) {
        this.list.put(node, new ACastPrimitiveExpr(node.getBracketL(),
                node.getType(),
                node.getBracketR(),
                (PExpr) this.list.remove(node.getTmpOrExpr())));
    }


    /**
     * ** Or Expr ****
     */

    public void outAOrelseTmpOrExpr(AOrelseTmpOrExpr node) {
        this.list.put(node, this.list.remove(node.getTmpOrelseExpr()));
    }

    /**
     * OR **
     */
    public void outATmpOrExpr(ATmpOrExpr node) {
        this.list.put(node, new AOrExpr((PExpr) this.list.remove(node.getTmpOrExpr()),
                node.getOr(),
                (PExpr) this.list.remove(node.getTmpOrelseExpr())));
    }

    public void outAFirstTmpOrExpr(AFirstTmpOrExpr node) {
        this.list.put(node, new AOrExpr(new AIdentifierExpr(node.getIdentifier()),
                node.getOr(),
                (PExpr) this.list.remove(node.getTmpOrelseExpr())));
    }

    public void outALastTmpOrExpr(ALastTmpOrExpr node) {
        this.list.put(node, new AOrExpr((PExpr) this.list.remove(node.getTmpOrExpr()),
                node.getOr(),
                new AIdentifierExpr(node.getIdentifier())));
    }

    public void outABothTmpOrExpr(ABothTmpOrExpr node) {
        this.list.put(node, new AOrExpr(new AIdentifierExpr(node.getLeft()),
                node.getOr(),
                new AIdentifierExpr(node.getRight())));
    }


    /**
     * ** Orelse Expr ****
     */

    public void outAAndExprTmpOrelseExpr(AAndExprTmpOrelseExpr node) {
        this.list.put(node, this.list.remove(node.getTmpAndExpr()));
    }


    /**
     * ** And Expr ****
     */

    public void outAQuantifierExprTmpAndExpr(AQuantifierExprTmpAndExpr node) {
        this.list.put(node, this.list.remove(node.getQuantifierExpr()));
    }

    /**
     * AND **
     */
    public void outATmpAndExpr(ATmpAndExpr node) {
        this.list.put(node, new AAndExpr((PExpr) this.list.remove(node.getTmpAndExpr()),
                node.getAnd(),
                (PExpr) this.list.remove(node.getQuantifierExpr())));
    }

    public void outALastTmpAndExpr(ALastTmpAndExpr node) {
        this.list.put(node, new AAndExpr((PExpr) this.list.remove(node.getTmpAndExpr()),
                node.getAnd(),
                new AIdentifierExpr(node.getIdentifier())));
    }

    public void outAFirstTmpAndExpr(AFirstTmpAndExpr node) {
        this.list.put(node, new AAndExpr(new AIdentifierExpr(node.getIdentifier()),
                node.getAnd(),
                (PExpr) this.list.remove(node.getQuantifierExpr())));
    }

    public void outABothTmpAndExpr(ABothTmpAndExpr node) {
        this.list.put(node, new AAndExpr(new AIdentifierExpr(node.getLeft()),
                node.getAnd(),
                new AIdentifierExpr(node.getRight())));
    }


    /**
     * **  Quantifier Expr ****
     */


    public void outAForallQuantifierExpr(AForallQuantifierExpr node) {
        this.list.put(node, new AForallExpr(node.getFor(),
                node.getAll(),
                node.getInClause(),
                node.getColon(),
                (PExpr) this.list.remove(node.getAndthenExpr())));
    }

    public void outAIdentifierForallQuantifierExpr(AIdentifierForallQuantifierExpr node) {
        this.list.put(node, new AForallExpr(node.getFor(),
                node.getAll(),
                node.getInClause(),
                node.getColon(),
                new AIdentifierExpr(node.getIdentifier())));
    }

    public void outAExistsQuantifierExpr(AExistsQuantifierExpr node) {
        this.list.put(node, new AExistsExpr(node.getExists(),
                node.getInClause(),
                node.getColon(),
                (PExpr) this.list.remove(node.getAndthenExpr())));
    }

    public void outAIdentifierExistsQuantifierExpr(AIdentifierExistsQuantifierExpr node) {
        this.list.put(node, new AExistsExpr(node.getExists(),
                node.getInClause(),
                node.getColon(),
                new AIdentifierExpr(node.getIdentifier())));
    }

    public void outAAndthenQuantifierExpr(AAndthenQuantifierExpr node) {
        this.list.put(node, this.list.remove(node.getAndthenExpr()));
    }


    /**
     * ** Andthen expr ****
     */

    public void outAEqualityExprAndthenExpr(AEqualityExprAndthenExpr node) {
        this.list.put(node, this.list.remove(node.getTmpEqualityExpr()));
    }


    /**
     * ** Equality Expr ****
     */

    public void outARelationalExprTmpEqualityExpr(ARelationalExprTmpEqualityExpr node) {
        this.list.put(node, this.list.remove(node.getTmpRelationalExpr()));
    }

    /**
     * = != **
     */
    public void outAEqneTmpEqualityExpr(AEqneTmpEqualityExpr node) {
        this.list.put(node, new AEqualityExpr((PExpr) this.list.remove(node.getTmpEqualityExpr()),
                node.getEqne(),
                (PExpr) this.list.remove(node.getTmpRelationalExpr())));
    }

    public void outAFirstEqneTmpEqualityExpr(AFirstEqneTmpEqualityExpr node) {
        this.list.put(node, new AEqualityExpr(new AIdentifierExpr(node.getIdentifier()),
                node.getEqne(),
                (PExpr) this.list.remove(node.getTmpRelationalExpr())));
    }

    public void outALastEqneTmpEqualityExpr(ALastEqneTmpEqualityExpr node) {
        this.list.put(node, new AEqualityExpr((PExpr) this.list.remove(node.getTmpEqualityExpr()),
                node.getEqne(),
                new AIdentifierExpr(node.getIdentifier())));
    }

    public void outABothEqneTmpEqualityExpr(ABothEqneTmpEqualityExpr node) {
        this.list.put(node, new AEqualityExpr(new AIdentifierExpr(node.getLeft()),
                node.getEqne(),
                new AIdentifierExpr(node.getRight())));
    }

    /**
     * LIKE **
     */
    public void outALikeTmpEqualityExpr(ALikeTmpEqualityExpr node) {
        this.list.put(node, new ALikeExpr((PExpr) this.list.remove(node.getTmpEqualityExpr()),
                node.getLike(),
                (PExpr) this.list.remove(node.getTmpRelationalExpr())));
    }

    public void outAFirstLikeTmpEqualityExpr(AFirstLikeTmpEqualityExpr node) {
        this.list.put(node, new ALikeExpr(new AIdentifierExpr(node.getIdentifier()),
                node.getLike(),
                (PExpr) this.list.remove(node.getTmpRelationalExpr())));
    }

    public void outALastLikeTmpEqualityExpr(ALastLikeTmpEqualityExpr node) {
        this.list.put(node, new ALikeExpr((PExpr) this.list.remove(node.getTmpEqualityExpr()),
                node.getLike(),
                new AIdentifierExpr(node.getIdentifier())));
    }

    public void outABothLikeTmpEqualityExpr(ABothLikeTmpEqualityExpr node) {
        this.list.put(node, new ALikeExpr(new AIdentifierExpr(node.getLeft()),
                node.getLike(),
                new AIdentifierExpr(node.getRight())));
    }

        /**
     * RLIKE **
     */
    public void outARlikeTmpEqualityExpr(ARlikeTmpEqualityExpr node) {
        this.list.put(node, new ARlikeExpr((PExpr) this.list.remove(node.getTmpEqualityExpr()),
                node.getRlike(),
                (PExpr) this.list.remove(node.getTmpRelationalExpr())));
    }

    public void outAFirstRlikeTmpEqualityExpr(AFirstRlikeTmpEqualityExpr node) {
        this.list.put(node, new ARlikeExpr(new AIdentifierExpr(node.getIdentifier()),
                node.getRlike(),
                (PExpr) this.list.remove(node.getTmpRelationalExpr())));
    }

    public void outALastRlikeTmpEqualityExpr(ALastRlikeTmpEqualityExpr node) {
        this.list.put(node, new ARlikeExpr((PExpr) this.list.remove(node.getTmpEqualityExpr()),
                node.getRlike(),
                new AIdentifierExpr(node.getIdentifier())));
    }

    public void outABothRlikeTmpEqualityExpr(ABothRlikeTmpEqualityExpr node) {
        this.list.put(node, new ARlikeExpr(new AIdentifierExpr(node.getLeft()),
                node.getRlike(),
                new AIdentifierExpr(node.getRight())));
    }
    /**
     * instanceof **
     */
    public void outAInstanceofTmpEqualityExpr(AInstanceofTmpEqualityExpr node) {
        this.list.put(node, new AInstanceofExpr((PExpr) this.list.remove(node.getTmpEqualityExpr()),
                node.getInstanceof(),
                (PExpr) this.list.remove(node.getTmpRelationalExpr())));
    }

    public void outAFirstInstanceofTmpEqualityExpr(AFirstInstanceofTmpEqualityExpr node) {
        this.list.put(node, new AInstanceofExpr(new AIdentifierExpr(node.getIdentifier()),
                node.getInstanceof(),
                (PExpr) this.list.remove(node.getTmpRelationalExpr())));
    }

    public void outALastInstanceofTmpEqualityExpr(ALastInstanceofTmpEqualityExpr node) {
        this.list.put(node, new AInstanceofExpr((PExpr) this.list.remove(node.getTmpEqualityExpr()),
                node.getInstanceof(),
                new AIdentifierExpr(node.getIdentifier())));
    }

    public void outABothInstanceofTmpEqualityExpr(ABothInstanceofTmpEqualityExpr node) {
        this.list.put(node, new AInstanceofExpr(new AIdentifierExpr(node.getLeft()),
                node.getInstanceof(),
                new AIdentifierExpr(node.getRight())));
    }


    /**
     * = != distinct **
     */
    public void outADistinctTmpEqualityExpr(ADistinctTmpEqualityExpr node) {
        this.list.put(node, new AEqualityExpr((PExpr) this.list.remove(node.getTmpEqualityExpr()),
                node.getEqne(),
                new AConversionDistinctExpr(node.getDistinct(),
                        node.getBracketL(),
                        (PQuery) this.list.remove(node.getQueryRestricted()),
                        node.getBracketR())));
    }

    public void outADistinctIdentifierTmpEqualityExpr(ADistinctIdentifierTmpEqualityExpr node) {
        this.list.put(node, new AEqualityExpr((PExpr) this.list.remove(node.getTmpEqualityExpr()),
                node.getEqne(),
                new AConversionDistinctExpr(node.getDistinct(),
                        node.getBracketL(),
                        new AQuery(new AIdentifierExpr(node.getIdentifier())),
                        node.getBracketR())));
    }

    public void outAFirstDistinctTmpEqualityExpr(AFirstDistinctTmpEqualityExpr node) {
        this.list.put(node, new AEqualityExpr(new AIdentifierExpr(node.getIdentifier()),
                node.getEqne(),
                new AConversionDistinctExpr(node.getDistinct(),
                        node.getBracketL(),
                        (PQuery) this.list.remove(node.getQueryRestricted()),
                        node.getBracketR())));
    }

    public void outAFirstDistinctIdentifierTmpEqualityExpr(AFirstDistinctIdentifierTmpEqualityExpr node) {
        this.list.put(node, new AEqualityExpr(new AIdentifierExpr(node.getLeft()),
                node.getEqne(),
                new AConversionDistinctExpr(node.getDistinct(),
                        node.getBracketL(),
                        new AQuery(new AIdentifierExpr(node.getRight())),
                        node.getBracketR())));
    }


    /**
     * ** Relational Expr ****
     */

    public void outAAdditiveExprTmpRelationalExpr(AAdditiveExprTmpRelationalExpr node) {
        this.list.put(node, this.list.remove(node.getAdditiveExpr()));
    }

    /**
     * Relational *
     */
    public void outATmpRelationalExpr(ATmpRelationalExpr node) {
        this.list.put(node, new ARelationalExpr((PExpr) this.list.remove(node.getTmpRelationalExpr()),
                node.getCompareToken(),
                node.getCompositePredicate(),
                (PExpr) this.list.remove(node.getAdditiveExpr())));
    }

    public void outAFirstTmpRelationalExpr(AFirstTmpRelationalExpr node) {
        this.list.put(node, new ARelationalExpr(new AIdentifierExpr(node.getIdentifier()),
                node.getCompareToken(),
                node.getCompositePredicate(),
                (PExpr) this.list.remove(node.getAdditiveExpr())));
    }

    public void outALastTmpRelationalExpr(ALastTmpRelationalExpr node) {
        this.list.put(node, new ARelationalExpr((PExpr) this.list.remove(node.getTmpRelationalExpr()),
                node.getCompareToken(),
                node.getCompositePredicate(),
                new AIdentifierExpr(node.getIdentifier())));
    }

    public void outABothTmpRelationalExpr(ABothTmpRelationalExpr node) {
        this.list.put(node, new ARelationalExpr(new AIdentifierExpr(node.getLeft()),
                node.getCompareToken(),
                node.getCompositePredicate(),
                new AIdentifierExpr(node.getRight())));
    }


    /**
     * ** Postfix Expr ****
     */

    public void outAPrimaryExprPostfixExpr(APrimaryExprPostfixExpr node) {
        this.list.put(node, this.list.remove(node.getPrimaryExpr()));
    }

    public void outAIndexPostfixExpr(AIndexPostfixExpr node) {
        this.list.put(node, new AIndexExpr((PExpr) this.list.remove(node.getPostfixExpr()),
                node.getSqBracketL(),
                node.getIndex(),
                node.getSqBracketR()));

    }

    public void outAIdentifierIndexPostfixExpr(AIdentifierIndexPostfixExpr node) {
        this.list.put(node, new AIndexExpr(new AIdentifierExpr(node.getIdentifier()),
                node.getSqBracketL(),
                node.getIndex(),
                node.getSqBracketR()));
    }

    public void outAFieldPostfixExpr(AFieldPostfixExpr node) {
        this.list.put(node, new APathExpr((PExpr) this.list.remove(node.getPostfixExpr()),
                node.getDotarrow(),
                new AIdentifierExpr(node.getIdentifier())));
    }

    public void outAIdentifierFieldPostfixExpr(AIdentifierFieldPostfixExpr node) {
        this.list.put(node, new APathExpr(new AIdentifierExpr(node.getLeft()),
                node.getDotarrow(),
                new AIdentifierExpr(node.getRight())));
    }

    public void outAMethodPostfixExpr(AMethodPostfixExpr node) {
        this.list.put(node, new APathExpr((PExpr) this.list.remove(node.getPostfixExpr()),
                node.getDotarrow(),
                new AMethodExpr(node.getIdentifier(),
                        node.getBracketL(),
                        node.getValueList(),
                        node.getBracketR())));
    }

    public void outAIdentifierMethodPostfixExpr(AIdentifierMethodPostfixExpr node) {
        this.list.put(node, new APathExpr(new AIdentifierExpr(node.getLeft()),
                node.getDotarrow(),
                new AMethodExpr(node.getRight(),
                        node.getBracketL(),
                        node.getValueList(),
                        node.getBracketR())));
    }


    /**
     * ** Unary Expr ****
     */

    public void outAPostfixUnaryExpr(APostfixUnaryExpr node) {
        this.list.put(node, this.list.remove(node.getPostfixExpr()));
    }

    public void outAMinusUnaryExpr(AMinusUnaryExpr node) {
        this.list.put(node, new AUnaryMinusExpr(node.getMinus(),
                (PExpr) this.list.remove(node.getUnaryExpr())));
    }

    public void outAIdentifierMinusUnaryExpr(AIdentifierMinusUnaryExpr node) {
        this.list.put(node, new AUnaryMinusExpr(node.getMinus(),
                new AIdentifierExpr(node.getIdentifier())));
    }

    public void outAPlusUnaryExpr(APlusUnaryExpr node) {
        this.list.put(node, new AUnaryPlusExpr(node.getPlus(),
                (PExpr) this.list.remove(node.getUnaryExpr())));
    }

    public void outAIdentifierPlusUnaryExpr(AIdentifierPlusUnaryExpr node) {
        this.list.put(node, new AUnaryPlusExpr(node.getPlus(),
                new AIdentifierExpr(node.getIdentifier())));
    }

    public void outANotUnaryExpr(ANotUnaryExpr node) {
        this.list.put(node, new AUnaryNotExpr(node.getNot(),
                (PExpr) this.list.remove(node.getUnaryExpr())));
    }

    public void outAIdentifierNotUnaryExpr(AIdentifierNotUnaryExpr node) {
        this.list.put(node, new AUnaryNotExpr(node.getNot(),
                new AIdentifierExpr(node.getIdentifier())));
    }

    public void outAAbsUnaryExpr(AAbsUnaryExpr node) {
        this.list.put(node, new AUnaryAbsExpr(node.getAbs(),
                (PExpr) this.list.remove(node.getUnaryExpr())));
    }

    public void outAIdentifierAbsUnaryExpr(AIdentifierAbsUnaryExpr node) {
        this.list.put(node, new AUnaryAbsExpr(node.getAbs(),
                new AIdentifierExpr(node.getIdentifier())));
    }

    /**
     * ** In Expr ****
     */

    public void outAUnaryExprTmpInExpr(AUnaryExprTmpInExpr node) {
        this.list.put(node, this.list.remove(node.getUnaryExpr()));
    }

    public void outAFirstTmpInExpr(AFirstTmpInExpr node) {
//        this.list.put(node, new AInExpr(new AIdentifierExpr(node.getIdentifier()),
//                node.getIn(),
//                (PExpr) this.list.remove(node.getUnaryExpr())));

        this.list.put(node, new AInExpr(new AIdentifierExpr(node.getIdentifier()),
                node.getNot(),
                node.getIn(),
                oneElementList((PExpr) this.list.remove(node.getUnaryExpr()))));
    }

    public void outALastTmpInExpr(ALastTmpInExpr node) {
//        this.list.put(node, new AInExpr((PExpr) this.list.remove(node.getTmpInExpr()),
//                node.getIn(),
//                new AIdentifierExpr(node.getIdentifier())));

        this.list.put(node, new AInExpr((PExpr) this.list.remove(node.getTmpInExpr()),
                node.getNot(),
                node.getIn(),
                oneElementList(new AIdentifierExpr(node.getIdentifier()))));
    }

    java.util.List oneElementList(Object o) {
        java.util.List list = new java.util.ArrayList(1);
        list.add(o);
        return list;
    }

    public void outABothTmpInExpr(ABothTmpInExpr node) {
//        this.list.put(node, new AInExpr(new AIdentifierExpr(node.getLeft()),
//                node.getIn(),
//                new AIdentifierExpr(node.getRight())));
        this.list.put(node, new AInExpr(new AIdentifierExpr(node.getLeft()),
                node.getNot(),
                node.getIn(),
                oneElementList(new AIdentifierExpr(node.getRight()))));
    }

    public void outAListTmpInExpr(AListTmpInExpr node) {        // xxxxxxxxxxxxxxxxxxxxxx
        java.util.List l = oneElementList((PExpr) this.list.remove(node.getUnaryExpr()));

        for (Iterator iterator = node.getInlist().listIterator(); iterator.hasNext();) {
            AInlist inlist = (AInlist) iterator.next();
            l.add((PExpr) this.list.remove(inlist.getUnaryExpr()));
        }

        this.list.put(node, new AInExpr(//(PExpr) this.list.remove(node.getTmpInExpr()),
                new AIdentifierExpr(node.getIdentifier()),
                node.getNot(),
                node.getIn(),
                l));
    }

    public void outATmpInExpr(ATmpInExpr node) {
//        this.list.put(node, new AInExpr((PExpr) this.list.remove(node.getTmpInExpr()),
//                node.getIn(),
//                (PExpr) this.list.remove(node.getUnaryExpr())));

        java.util.List l = new java.util.ArrayList();
        if (node.getCommaExpr() instanceof ACommaExpr) {
            ACommaExpr commaExpr = (ACommaExpr) (node.getCommaExpr());
            l.add((PExpr) this.list.remove(commaExpr.getUnaryExpr()));
        } else if (node.getCommaExpr() instanceof ACommaSeparatedExprCommaExpr) {
            ACommaSeparatedExprCommaExpr ccc = (ACommaSeparatedExprCommaExpr) (node.getCommaExpr());
            l.add((PExpr) this.list.remove(ccc.getUnaryExpr()));
            for (Iterator iterator = ccc.getInlist().listIterator(); iterator.hasNext();) {
                AInlist inlist = (AInlist) iterator.next();
                l.add((PExpr) this.list.remove(inlist.getUnaryExpr()));
            }
        }

        this.list.put(node, new AInExpr((PExpr) this.list.remove(node.getTmpInExpr()),
                node.getNot(),
                node.getIn(),
                l));

    }

    /***** Additive expression *****/

    /**
     * PLUS *
     */

    public void outAPlusAdditiveExpr(APlusAdditiveExpr node) {
        this.list.put(node, new AAdditionExpr((PExpr) this.list.remove(node.getAdditiveExpr()),
                node.getPlus(),
                (PExpr) this.list.remove(node.getMultiplicativeExpr())));
    }

    public void outAFirstPlusAdditiveExpr(AFirstPlusAdditiveExpr node) {
        this.list.put(node, new AAdditionExpr(new AIdentifierExpr(node.getIdentifier()),
                node.getPlus(),
                (PExpr) this.list.remove(node.getMultiplicativeExpr())));
    }

    public void outALastPlusAdditiveExpr(ALastPlusAdditiveExpr node) {
        this.list.put(node, new AAdditionExpr((PExpr) this.list.remove(node.getAdditiveExpr()),
                node.getPlus(),
                new AIdentifierExpr(node.getIdentifier())));
    }

    public void outABothPlusAdditiveExpr(ABothPlusAdditiveExpr node) {
        this.list.put(node, new AAdditionExpr(new AIdentifierExpr(node.getLeft()),
                node.getPlus(),
                new AIdentifierExpr(node.getRight())));
    }

    /**
     * MINUS *
     */

    public void outAMinusAdditiveExpr(AMinusAdditiveExpr node) {
        this.list.put(node, new ASubstractionExpr((PExpr) this.list.remove(node.getAdditiveExpr()),
                node.getMinus(),
                (PExpr) this.list.remove(node.getMultiplicativeExpr())));
    }

    public void outAFirstMinusAdditiveExpr(AFirstMinusAdditiveExpr node) {
        this.list.put(node, new ASubstractionExpr(new AIdentifierExpr(node.getIdentifier()),
                node.getMinus(),
                (PExpr) this.list.remove(node.getMultiplicativeExpr())));
    }

    public void outALastMinusAdditiveExpr(ALastMinusAdditiveExpr node) {
        this.list.put(node, new ASubstractionExpr((PExpr) this.list.remove(node.getAdditiveExpr()),
                node.getMinus(),
                new AIdentifierExpr(node.getIdentifier())));
    }

    public void outABothMinusAdditiveExpr(ABothMinusAdditiveExpr node) {
        this.list.put(node, new ASubstractionExpr(new AIdentifierExpr(node.getLeft()),
                node.getMinus(),
                new AIdentifierExpr(node.getRight())));
    }

    /**
     * EXCEPT *
     */

    public void outAExceptAdditiveExpr(AExceptAdditiveExpr node) {
        this.list.put(node, new AExceptExpr((PExpr) this.list.remove(node.getAdditiveExpr()),
                node.getExcept(),
                (PExpr) this.list.remove(node.getMultiplicativeExpr())));
    }

    public void outAFirstExceptAdditiveExpr(AFirstExceptAdditiveExpr node) {
        this.list.put(node, new AExceptExpr(new AIdentifierExpr(node.getIdentifier()),
                node.getExcept(),
                (PExpr) this.list.remove(node.getMultiplicativeExpr())));
    }

    public void outALastExceptAdditiveExpr(ALastExceptAdditiveExpr node) {
        this.list.put(node, new AExceptExpr((PExpr) this.list.remove(node.getAdditiveExpr()),
                node.getExcept(),
                new AIdentifierExpr(node.getIdentifier())));
    }

    public void outABothExceptAdditiveExpr(ABothExceptAdditiveExpr node) {
        this.list.put(node, new AExceptExpr(new AIdentifierExpr(node.getLeft()),
                node.getExcept(),
                new AIdentifierExpr(node.getRight())));
    }

    /**
     * Binor *
     */

    public void outABinorAdditiveExpr(ABinorAdditiveExpr node) {
        this.list.put(node, new ABinorExpr((PExpr) this.list.remove(node.getAdditiveExpr()),
                node.getBinor(),
                (PExpr) this.list.remove(node.getMultiplicativeExpr())));
    }

    public void outAFirstBinorAdditiveExpr(AFirstBinorAdditiveExpr node) {
        this.list.put(node, new ABinorExpr(new AIdentifierExpr(node.getIdentifier()),
                node.getBinor(),
                (PExpr) this.list.remove(node.getMultiplicativeExpr())));
    }

    public void outALastBinorAdditiveExpr(ALastBinorAdditiveExpr node) {
        this.list.put(node, new ABinorExpr((PExpr) this.list.remove(node.getAdditiveExpr()),
                node.getBinor(),
                new AIdentifierExpr(node.getIdentifier())));
    }

    public void outABothBinorAdditiveExpr(ABothBinorAdditiveExpr node) {
        this.list.put(node, new ABinorExpr(new AIdentifierExpr(node.getLeft()),
                node.getBinor(),
                new AIdentifierExpr(node.getRight())));
    }


    /**
     * ** Multiplicative Expr ****
     */

    public void outAInExprMultiplicativeExpr(AInExprMultiplicativeExpr node) {
        this.list.put(node, this.list.remove(node.getTmpInExpr()));
    }

    public void outAMultiplicativeExprAdditiveExpr(AMultiplicativeExprAdditiveExpr node) {
        this.list.put(node, this.list.remove(node.getMultiplicativeExpr()));
    }

//    public void inAIntersectionMultiplicativeExpr(AIntersectionMultiplicativeExpr node)
//    {
//        this.list.put(node, this.list.remove(node.getMultiplicativeExpr()));
//    }



    /**
     * MULTIPLICATION *
     */
    public void outATimesMultiplicativeExpr(ATimesMultiplicativeExpr node) {
        this.list.put(node, new AMultiplicationExpr((PExpr) this.list.remove(node.getMultiplicativeExpr()),
                node.getStar(),
                (PExpr) this.list.remove(node.getTmpInExpr())));
    }

    public void outALastTimesMultiplicativeExpr(ALastTimesMultiplicativeExpr node) {
        this.list.put(node, new AMultiplicationExpr((PExpr) this.list.remove(node.getMultiplicativeExpr()),
                node.getStar(),
                new AIdentifierExpr(node.getIdentifier())));
    }

    public void outAFirstTimesMultiplicativeExpr(AFirstTimesMultiplicativeExpr node) {
        this.list.put(node, new AMultiplicationExpr(new AIdentifierExpr(node.getIdentifier()),
                node.getStar(),
                (PExpr) this.list.remove(node.getTmpInExpr())));
    }

    public void outABothTimesMultiplicativeExpr(ABothTimesMultiplicativeExpr node) {
        this.list.put(node, new AMultiplicationExpr(new AIdentifierExpr(node.getLeft()),
                node.getStar(),
                new AIdentifierExpr(node.getRight())));
    }

    public void outAIntersectionMultiplicativeExpr(AIntersectionMultiplicativeExpr node) {
        this.list.put(node, new AIntersectionExpr((PExpr) this.list.remove(node.getMultiplicativeExpr()),
                node.getSetAnd(),
                (PExpr) this.list.remove(node.getTmpInExpr())));
    }
     public void outALastIntersectionMultiplicativeExprr(ALastIntersectionMultiplicativeExpr node) {
        this.list.put(node, new  AIntersectionExpr((PExpr) this.list.remove(node.getMultiplicativeExpr()),
                node.getSetAnd(),
                new AIdentifierExpr(node.getIdentifier())));
    }

    public void outAFirstIntersectionMultiplicativeExpr(AFirstIntersectionMultiplicativeExpr node) {
        this.list.put(node, new AIntersectionExpr(new AIdentifierExpr(node.getIdentifier()),
                node.getSetAnd(),
                (PExpr) this.list.remove(node.getTmpInExpr())));
    }

    public void outABothIntersectionMultiplicativeExpr(ABothIntersectionMultiplicativeExpr node) {
        this.list.put(node, new AIntersectionExpr(new AIdentifierExpr(node.getLeft()),
                node.getSetAnd(),
                new AIdentifierExpr(node.getRight())));
    }

    /**
     * DIVISION *
     */
    public void outADivideMultiplicativeExpr(ADivideMultiplicativeExpr node) {
        this.list.put(node, new ADivisionExpr((PExpr) this.list.remove(node.getMultiplicativeExpr()),
                node.getDivide(),
                (PExpr) this.list.remove(node.getTmpInExpr())));
    }

    public void outALastDivideMultiplicativeExpr(ALastDivideMultiplicativeExpr node) {
        this.list.put(node, new ADivisionExpr((PExpr) this.list.remove(node.getMultiplicativeExpr()),
                node.getDivide(),
                new AIdentifierExpr(node.getIdentifier())));
    }

    public void outAFirstDivideMultiplicativeExpr(AFirstDivideMultiplicativeExpr node) {
        this.list.put(node, new ADivisionExpr(new AIdentifierExpr(node.getIdentifier()),
                node.getDivide(),
                (PExpr) this.list.remove(node.getTmpInExpr())));
    }

    public void outABothDivideMultiplicativeExpr(ABothDivideMultiplicativeExpr node) {
        this.list.put(node, new ADivisionExpr(new AIdentifierExpr(node.getLeft()),
                node.getDivide(),
                new AIdentifierExpr(node.getRight())));
    }


    /**
     * ** Primary Expression ****
     */


    public void outAConversionPrimaryExpr(AConversionPrimaryExpr node) {
        this.list.put(node, this.list.remove(node.getConversionExpr()));
    }

    public void outACollectionPrimaryExpr(ACollectionPrimaryExpr node) {
        this.list.put(node, this.list.remove(node.getCollectionExpr()));
    }

    public void outAAggregatePrimaryExpr(AAggregatePrimaryExpr node) {
        this.list.put(node, this.list.remove(node.getAggregateExpr()));
    }

    public void outAUndefinedPrimaryExpr(AUndefinedPrimaryExpr node) {
        this.list.put(node, this.list.remove(node.getUndefinedExpr()));
    }

    public void outAStructconstrPrimaryExpr(AStructconstrPrimaryExpr node) {
        this.list.put(node, this.list.remove(node.getStructConstruction()));
    }

    public void outACollconstrPrimaryExpr(ACollconstrPrimaryExpr node) {
        this.list.put(node, this.list.remove(node.getCollectionConstruction()));
    }

    public void outAQueryparamPrimaryExpr(AQueryparamPrimaryExpr node) {
        this.list.put(node, this.list.remove(node.getQueryParam()));
    }


    public void outALiteralPrimaryExpr(ALiteralPrimaryExpr node) {
        this.list.put(node, new ALiteralExpr(node.getLiteral()));
    }

    public void outATmpQueryPrimaryExpr(ATmpQueryPrimaryExpr node) {
        this.list.put(node, new ANestedExpr(node.getBracketL(),
                (PQuery) this.list.remove(node.getQueryRestricted()),
                node.getBracketR()));
    }

    public void outAMethodPrimaryExpr(AMethodPrimaryExpr node) {
        this.list.put(node, new AMethodExpr(node.getIdentifier(),
                node.getBracketL(),
                node.getValueList(),
                node.getBracketR()));
    }

    public void outAObjconstrPrimaryExpr(AObjconstrPrimaryExpr node) {
        this.list.put(node, new ANewObjectExpr(new AIdentifierExpr(node.getIdentifier()),
                node.getBracketL(),
                node.getFieldList(),
                node.getBracketR()));
    }


    /**
     * ** Conversion expr ****
     */

    public void outAListtosetConversionExpr(AListtosetConversionExpr node) {
        this.list.put(node, new AConversionListtosetExpr(node.getListtoset(),
                node.getBracketL(),
                (PQuery) this.list.remove(node.getQueryRestricted()),
                node.getBracketR()));
    }

    public void outAIdentifierListtosetConversionExpr(AIdentifierListtosetConversionExpr node) {
        this.list.put(node, new AConversionListtosetExpr(node.getListtoset(),
                node.getBracketL(),
                new AQuery(new AIdentifierExpr(node.getIdentifier())),
                node.getBracketR()));
    }

    public void outAElementConversionExpr(AElementConversionExpr node) {
        this.list.put(node, new AConversionElementExpr(node.getElement(),
                node.getBracketL(),
                (PQuery) this.list.remove(node.getQueryRestricted()),
                node.getBracketR()));
    }

    public void outAIdentifierElementConversionExpr(AIdentifierElementConversionExpr node) {
        this.list.put(node, new AConversionElementExpr(node.getElement(),
                node.getBracketL(),
                new AQuery(new AIdentifierExpr(node.getIdentifier())),
                node.getBracketR()));
    }

    public void outAFlattenConversionExpr(AFlattenConversionExpr node) {
        this.list.put(node, new AConversionFlattenExpr(node.getFlatten(),
                node.getBracketL(),
                (PQuery) this.list.remove(node.getQueryRestricted()),
                node.getBracketR()));
    }

    public void outAIdentifierFlattenConversionExpr(AIdentifierFlattenConversionExpr node) {
        this.list.put(node, new AConversionFlattenExpr(node.getFlatten(),
                node.getBracketL(),
                new AQuery(new AIdentifierExpr(node.getIdentifier())),
                node.getBracketR()));
    }


    /**
     * ** Collection expr ****
     */

    public void outAFirstCollectionExpr(AFirstCollectionExpr node) {
        this.list.put(node, new ACollectionFirstExpr(node.getFirst(),
                node.getBracketL(),
                (PQuery) this.list.remove(node.getQueryRestricted()),
                node.getBracketR()));
    }

    public void outAIdentifierFirstCollectionExpr(AIdentifierFirstCollectionExpr node) {
        this.list.put(node, new ACollectionFirstExpr(node.getFirst(),
                node.getBracketL(),
                new AQuery(new AIdentifierExpr(node.getIdentifier())),
                node.getBracketR()));
    }

    public void outALastCollectionExpr(ALastCollectionExpr node) {
        this.list.put(node, new ACollectionLastExpr(node.getLast(),
                node.getBracketL(),
                (PQuery) this.list.remove(node.getQueryRestricted()),
                node.getBracketR()));
    }

    public void outAIdentifierLastCollectionExpr(AIdentifierLastCollectionExpr node) {
        this.list.put(node, new ACollectionLastExpr(node.getLast(),
                node.getBracketL(),
                new AQuery(new AIdentifierExpr(node.getIdentifier())),
                node.getBracketR()));
    }

    public void outAUniqueCollectionExpr(AUniqueCollectionExpr node) {
        this.list.put(node, new ACollectionUniqueExpr(node.getUnique(),
                node.getBracketL(),
                (PQuery) this.list.remove(node.getQueryRestricted()),
                node.getBracketR()));
    }

    public void outAIdentifierUniqueCollectionExpr(AIdentifierUniqueCollectionExpr node) {
        this.list.put(node, new ACollectionUniqueExpr(node.getUnique(),
                node.getBracketL(),
                new AQuery(new AIdentifierExpr(node.getIdentifier())),
                node.getBracketR()));
    }

    public void outAExistsCollectionExpr(AExistsCollectionExpr node) {
        this.list.put(node, new ACollectionExistsExpr(node.getExists(),
                node.getBracketL(),
                (PQuery) this.list.remove(node.getQueryRestricted()),
                node.getBracketR()));
    }

    public void outAIdentifierExistsCollectionExpr(AIdentifierExistsCollectionExpr node) {
        this.list.put(node, new ACollectionExistsExpr(node.getExists(),
                node.getBracketL(),
                new AQuery(new AIdentifierExpr(node.getIdentifier())),
                node.getBracketR()));
    }


    /**
     * *** Aggregate Expr ****
     */
    /* <<<<<<<<<<<<<<<<<< Not implemented in Salsa yet */
    public void outASumAggregateExpr(ASumAggregateExpr node) {
        this.list.put(node, new AAggregateSumExpr(node.getSum(),
                node.getBracketL(),
                (PQuery) this.list.remove(node.getQueryRestricted()),
                node.getBracketR()));
    }

    public void outAIdentifierSumAggregateExpr(AIdentifierSumAggregateExpr node) {
        this.list.put(node, new AAggregateSumExpr(node.getSum(),
                node.getBracketL(),
                new AQuery(new AIdentifierExpr(node.getIdentifier())),
                node.getBracketR()));
    }

    public void outAMinAggregateExpr(AMinAggregateExpr node) {
        this.list.put(node, new AAggregateMinExpr(node.getMin(),
                node.getBracketL(),
                (PQuery) this.list.remove(node.getQueryRestricted()),
                node.getBracketR()));
    }

    public void outAIdentifierMinAggregateExpr(AIdentifierMinAggregateExpr node) {
        this.list.put(node, new AAggregateMinExpr(node.getMin(),
                node.getBracketL(),
                new AQuery(new AIdentifierExpr(node.getIdentifier())),
                node.getBracketR()));
    }

    public void outAMaxAggregateExpr(AMaxAggregateExpr node) {
        this.list.put(node, new AAggregateMaxExpr(node.getMax(),
                node.getBracketL(),
                (PQuery) this.list.remove(node.getQueryRestricted()),
                node.getBracketR()));
    }

    public void outAIdentifierMaxAggregateExpr(AIdentifierMaxAggregateExpr node) {
        this.list.put(node, new AAggregateMaxExpr(node.getMax(),
                node.getBracketL(),
                new AQuery(new AIdentifierExpr(node.getIdentifier())),
                node.getBracketR()));
    }

    public void outAAvgAggregateExpr(AAvgAggregateExpr node) {
        this.list.put(node, new AAggregateAvgExpr(node.getAvg(),
                node.getBracketL(),
                (PQuery) this.list.remove(node.getQueryRestricted()),
                node.getBracketR()));
    }

    public void outAIdentifierAvgAggregateExpr(AIdentifierAvgAggregateExpr node) {
        this.list.put(node, new AAggregateAvgExpr(node.getAvg(),
                node.getBracketL(),
                new AQuery(new AIdentifierExpr(node.getIdentifier())),
                node.getBracketR()));
    }
/* >>>>>>>>>>>>>>>>>>>>>>> Not Implemented in Salsa yet */
    public void outACountAggregateExpr(ACountAggregateExpr node) {
        this.list.put(node, new AAggregateCountExpr(node.getCount(),
                node.getBracketL(),
                (PQuery) this.list.remove(node.getQueryStar()),
                node.getBracketR()));
    }


    /**
     * ** Query Star ****
     */

    public void outAQueryStar(AQueryStar node) {
        this.list.put(node, this.list.remove(node.getQueryRestricted()));
    }

    public void outAIdentifierQueryStar(AIdentifierQueryStar node) {
        this.list.put(node, new AQuery(new AIdentifierExpr(node.getIdentifier())));
    }

    public void outAStarQueryStar(AStarQueryStar node) {
        this.list.put(node, new AQuery(new AStarExpr(node.getStar())));
    }


    /**
     * *** Iterator ****
     */

    public void outAIteratorDef(AIteratorDef node) {
        Node n = node.getExpr();
        if (n instanceof AInExpr) {
            AInExpr nc = (AInExpr) n;

            nc.replaceBy((PExpr) nc.getRight().getFirst());
            node.setAsIdentifierOptAs(new AAsIdentifierOptAs(new TAs("as"),
                    ((AIdentifierExpr) nc.getLeft()).getIdentifier()));
        }
    }

    public void outAExpr(AExpr node) {
        node.replaceBy((PExpr) this.list.remove(node.getExprRestricted()));
    }

    /**
     * ** Query Param ****
     */

    public void outAQueryParam(AQueryParam node) {
        this.list.put(node, new ALongParamExpr(node.getDollar(),
                node.getLongLiteral()));
    }

    public void outANamedQueryParam(ANamedQueryParam node) {
        this.list.put(node, new ANamedParamExpr(node.getDollar(),
                node.getIdentifier()));
    }


    /**
     * ** Collection Construction ****
     */

    public void outAArrayCollectionConstruction(AArrayCollectionConstruction node) {
        this.list.put(node, new ANewArrayExpr(node.getArray(),
                node.getBracketL(),
                node.getValueList(),
                node.getBracketR()));
    }

    public void outASetCollectionConstruction(ASetCollectionConstruction node) {
        this.list.put(node, new ANewSetExpr(node.getSet(),
                node.getBracketL(),
                node.getValueList(),
                node.getBracketR()));
    }

    public void outABagCollectionConstruction(ABagCollectionConstruction node) {
        this.list.put(node, new ANewBagExpr(node.getBag(),
                node.getBracketL(),
                node.getValueList(),
                node.getBracketR()));
    }

    public void outAListCollectionConstruction(AListCollectionConstruction node) {
        this.list.put(node, new ANewListExpr(node.getList(),
                node.getBracketL(),
                node.getValueOrRange(),
                node.getBracketR()));
    }

    public void outAStructConstruction(AStructConstruction node) {
        this.list.put(node, new ANewStructExpr(node.getStruct(),
                node.getBracketL(),
                node.getFieldList(),
                node.getBracketR()));
    }


} // SimpleAstWalker

//////////////////// end of file ////////////////////
