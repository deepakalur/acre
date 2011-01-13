/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.analysis;

import org.acre.lang.node.*;

public interface Analysis extends Switch
{
    Object getIn(Node node);
    void setIn(Node node, Object in);
    Object getOut(Node node);
    void setOut(Node node, Object out);

    void caseStart(Start node);
    void caseAQueryProgram(AQueryProgram node);
    void caseAFullQueryProgram(AFullQueryProgram node);
    void caseAQueryQueryProgram(AQueryQueryProgram node);
    void caseADeclarationQueryProgram(ADeclarationQueryProgram node);
    void caseAStandaloneSemicolonQueryProgram(AStandaloneSemicolonQueryProgram node);
    void caseAIncludeDeclaration(AIncludeDeclaration node);
    void caseAAliasDeclaration(AAliasDeclaration node);
    void caseAReturnDeclaration(AReturnDeclaration node);
    void caseADefineDeclaration(ADefineDeclaration node);
    void caseAUndefDeclaration(AUndefDeclaration node);
    void caseASimpleIncludeClause(ASimpleIncludeClause node);
    void caseAAliasClause(AAliasClause node);
    void caseAAsIdentifier(AAsIdentifier node);
    void caseADefineQuery(ADefineQuery node);
    void caseADefineQueryX(ADefineQueryX node);
    void caseAReturnQuery(AReturnQuery node);
    void caseATupleListReturnQuery(ATupleListReturnQuery node);
    void caseABracketedTupleListReturnQuery(ABracketedTupleListReturnQuery node);
    void caseASingleBracketedTupleList(ASingleBracketedTupleList node);
    void caseAMultipleBracketedTupleList(AMultipleBracketedTupleList node);
    void caseASingleTupleList(ASingleTupleList node);
    void caseAMultipleTupleList(AMultipleTupleList node);
    void caseAIdentifierList(AIdentifierList node);
    void caseASingleIdentifierList(ASingleIdentifierList node);
    void caseAParameterList(AParameterList node);
    void caseASingleParameterList(ASingleParameterList node);
    void caseAUndefineQuery(AUndefineQuery node);
    void caseAQualifiedName(AQualifiedName node);
    void caseASingleQualifiedName(ASingleQualifiedName node);
    void caseAQuery(AQuery node);
    void caseATmpSelectQuery(ATmpSelectQuery node);
    void caseATmpExprQuery(ATmpExprQuery node);
    void caseAIdentifierQuery(AIdentifierQuery node);
    void caseADistinctQuery(ADistinctQuery node);
    void caseADistinctIdentifierQuery(ADistinctIdentifierQuery node);
    void caseASelectQueryRestricted(ASelectQueryRestricted node);
    void caseAExprQueryRestricted(AExprQueryRestricted node);
    void caseASelectX(ASelectX node);
    void caseAListProjectionAttributes(AListProjectionAttributes node);
    void caseAAnyProjectionAttributes(AAnyProjectionAttributes node);
    void caseAProjectionList(AProjectionList node);
    void caseASingleProjectionList(ASingleProjectionList node);
    void caseAFieldProjection(AFieldProjection node);
    void caseATmpExprProjection(ATmpExprProjection node);
    void caseATmpIdentifierProjection(ATmpIdentifierProjection node);
    void caseAExprProjection(AExprProjection node);
    void caseAFromClause(AFromClause node);
    void caseAFromClauseList(AFromClauseList node);
    void caseASingleFromClauseList(ASingleFromClauseList node);
    void caseAIteratorDef(AIteratorDef node);
    void caseADistinctIteratorDef(ADistinctIteratorDef node);
    void caseADistinctIdentifierIteratorDef(ADistinctIdentifierIteratorDef node);
    void caseAAsIdentifierOptAs(AAsIdentifierOptAs node);
    void caseAWhereClause(AWhereClause node);
    void caseATmpWhereClause(ATmpWhereClause node);
    void caseAGroupClause(AGroupClause node);
    void caseAHavingClause(AHavingClause node);
    void caseATmpHavingClause(ATmpHavingClause node);
    void caseAOrderClause(AOrderClause node);
    void caseASortCriteria(ASortCriteria node);
    void caseASortCriteriaT(ASortCriteriaT node);
    void caseASortCriterion(ASortCriterion node);
    void caseAAscSortCriterionT(AAscSortCriterionT node);
    void caseADescSortCriterionT(ADescSortCriterionT node);
    void caseAExpr(AExpr node);
    void caseAIdentifierExpr(AIdentifierExpr node);
    void caseALiteralExpr(ALiteralExpr node);
    void caseAStarExpr(AStarExpr node);
    void caseALongParamExpr(ALongParamExpr node);
    void caseANamedParamExpr(ANamedParamExpr node);
    void caseASelectExpr(ASelectExpr node);
    void caseANestedExpr(ANestedExpr node);
    void caseACastExpr(ACastExpr node);
    void caseACastPrimitiveExpr(ACastPrimitiveExpr node);
    void caseAOrExpr(AOrExpr node);
    void caseABinorExpr(ABinorExpr node);
    void caseAExceptExpr(AExceptExpr node);
    void caseAOrelseExpr(AOrelseExpr node);
    void caseAAndExpr(AAndExpr node);
    void caseAEqualityExpr(AEqualityExpr node);
    void caseAInstanceofExpr(AInstanceofExpr node);
    void caseALikeExpr(ALikeExpr node);
    void caseARlikeExpr(ARlikeExpr node);
    void caseARelationalExpr(ARelationalExpr node);
    void caseAAdditionExpr(AAdditionExpr node);
    void caseASubstractionExpr(ASubstractionExpr node);
    void caseAMultiplicationExpr(AMultiplicationExpr node);
    void caseAIntersectionExpr(AIntersectionExpr node);
    void caseADivisionExpr(ADivisionExpr node);
    void caseAModuloExpr(AModuloExpr node);
    void caseAIntersectExpr(AIntersectExpr node);
    void caseAInExpr(AInExpr node);
    void caseAForallExpr(AForallExpr node);
    void caseAExistsExpr(AExistsExpr node);
    void caseAUnaryPlusExpr(AUnaryPlusExpr node);
    void caseAUnaryMinusExpr(AUnaryMinusExpr node);
    void caseAUnaryAbsExpr(AUnaryAbsExpr node);
    void caseAUnaryNotExpr(AUnaryNotExpr node);
    void caseANewObjectExpr(ANewObjectExpr node);
    void caseANewArrayExpr(ANewArrayExpr node);
    void caseANewSetExpr(ANewSetExpr node);
    void caseANewBagExpr(ANewBagExpr node);
    void caseANewListExpr(ANewListExpr node);
    void caseANewStructExpr(ANewStructExpr node);
    void caseAPathExpr(APathExpr node);
    void caseAMethodExpr(AMethodExpr node);
    void caseAIndexExpr(AIndexExpr node);
    void caseAConversionListtosetExpr(AConversionListtosetExpr node);
    void caseAConversionElementExpr(AConversionElementExpr node);
    void caseAConversionDistinctExpr(AConversionDistinctExpr node);
    void caseAConversionFlattenExpr(AConversionFlattenExpr node);
    void caseACollectionFirstExpr(ACollectionFirstExpr node);
    void caseACollectionLastExpr(ACollectionLastExpr node);
    void caseACollectionUniqueExpr(ACollectionUniqueExpr node);
    void caseACollectionExistsExpr(ACollectionExistsExpr node);
    void caseAAggregateSumExpr(AAggregateSumExpr node);
    void caseAAggregateMinExpr(AAggregateMinExpr node);
    void caseAAggregateMaxExpr(AAggregateMaxExpr node);
    void caseAAggregateAvgExpr(AAggregateAvgExpr node);
    void caseAIsundefExpr(AIsundefExpr node);
    void caseAIsdefExpr(AIsdefExpr node);
    void caseAAggregateCountExpr(AAggregateCountExpr node);
    void caseAExprRestricted(AExprRestricted node);
    void caseATmpCastExpr(ATmpCastExpr node);
    void caseAIdentifierTmpCastExpr(AIdentifierTmpCastExpr node);
    void caseAPrimitiveTmpCastExpr(APrimitiveTmpCastExpr node);
    void caseAPrimitiveIdentifierTmpCastExpr(APrimitiveIdentifierTmpCastExpr node);
    void caseAOrTmpCastExpr(AOrTmpCastExpr node);
    void caseATmpOrExpr(ATmpOrExpr node);
    void caseALastTmpOrExpr(ALastTmpOrExpr node);
    void caseAFirstTmpOrExpr(AFirstTmpOrExpr node);
    void caseABothTmpOrExpr(ABothTmpOrExpr node);
    void caseAOrelseTmpOrExpr(AOrelseTmpOrExpr node);
    void caseATmpOrelseExpr(ATmpOrelseExpr node);
    void caseALastTmpOrelseExpr(ALastTmpOrelseExpr node);
    void caseAFirstTmpOrelseExpr(AFirstTmpOrelseExpr node);
    void caseABothTmpOrelseExpr(ABothTmpOrelseExpr node);
    void caseAAndExprTmpOrelseExpr(AAndExprTmpOrelseExpr node);
    void caseATmpAndExpr(ATmpAndExpr node);
    void caseALastTmpAndExpr(ALastTmpAndExpr node);
    void caseAFirstTmpAndExpr(AFirstTmpAndExpr node);
    void caseABothTmpAndExpr(ABothTmpAndExpr node);
    void caseAQuantifierExprTmpAndExpr(AQuantifierExprTmpAndExpr node);
    void caseAForallQuantifierExpr(AForallQuantifierExpr node);
    void caseAIdentifierForallQuantifierExpr(AIdentifierForallQuantifierExpr node);
    void caseAExistsQuantifierExpr(AExistsQuantifierExpr node);
    void caseAIdentifierExistsQuantifierExpr(AIdentifierExistsQuantifierExpr node);
    void caseAAndthenQuantifierExpr(AAndthenQuantifierExpr node);
    void caseAInClause(AInClause node);
    void caseAIdentifierInClause(AIdentifierInClause node);
    void caseADistinctInClause(ADistinctInClause node);
    void caseADistinctIdentifierInClause(ADistinctIdentifierInClause node);
    void caseAAndthenExpr(AAndthenExpr node);
    void caseALastAndthenExpr(ALastAndthenExpr node);
    void caseAFirstAndthenExpr(AFirstAndthenExpr node);
    void caseABothAndthenExpr(ABothAndthenExpr node);
    void caseAEqualityExprAndthenExpr(AEqualityExprAndthenExpr node);
    void caseAEqneTmpEqualityExpr(AEqneTmpEqualityExpr node);
    void caseALastEqneTmpEqualityExpr(ALastEqneTmpEqualityExpr node);
    void caseAFirstEqneTmpEqualityExpr(AFirstEqneTmpEqualityExpr node);
    void caseABothEqneTmpEqualityExpr(ABothEqneTmpEqualityExpr node);
    void caseADistinctTmpEqualityExpr(ADistinctTmpEqualityExpr node);
    void caseADistinctIdentifierTmpEqualityExpr(ADistinctIdentifierTmpEqualityExpr node);
    void caseAFirstDistinctTmpEqualityExpr(AFirstDistinctTmpEqualityExpr node);
    void caseAFirstDistinctIdentifierTmpEqualityExpr(AFirstDistinctIdentifierTmpEqualityExpr node);
    void caseARlikeTmpEqualityExpr(ARlikeTmpEqualityExpr node);
    void caseALastRlikeTmpEqualityExpr(ALastRlikeTmpEqualityExpr node);
    void caseAFirstRlikeTmpEqualityExpr(AFirstRlikeTmpEqualityExpr node);
    void caseABothRlikeTmpEqualityExpr(ABothRlikeTmpEqualityExpr node);
    void caseALikeTmpEqualityExpr(ALikeTmpEqualityExpr node);
    void caseALastLikeTmpEqualityExpr(ALastLikeTmpEqualityExpr node);
    void caseAFirstLikeTmpEqualityExpr(AFirstLikeTmpEqualityExpr node);
    void caseABothLikeTmpEqualityExpr(ABothLikeTmpEqualityExpr node);
    void caseAInstanceofTmpEqualityExpr(AInstanceofTmpEqualityExpr node);
    void caseALastInstanceofTmpEqualityExpr(ALastInstanceofTmpEqualityExpr node);
    void caseAFirstInstanceofTmpEqualityExpr(AFirstInstanceofTmpEqualityExpr node);
    void caseABothInstanceofTmpEqualityExpr(ABothInstanceofTmpEqualityExpr node);
    void caseARelationalExprTmpEqualityExpr(ARelationalExprTmpEqualityExpr node);
    void caseATmpRelationalExpr(ATmpRelationalExpr node);
    void caseALastTmpRelationalExpr(ALastTmpRelationalExpr node);
    void caseAFirstTmpRelationalExpr(AFirstTmpRelationalExpr node);
    void caseABothTmpRelationalExpr(ABothTmpRelationalExpr node);
    void caseAAdditiveExprTmpRelationalExpr(AAdditiveExprTmpRelationalExpr node);
    void caseAEqEqne(AEqEqne node);
    void caseANeEqne(ANeEqne node);
    void caseALtCompareToken(ALtCompareToken node);
    void caseALeCompareToken(ALeCompareToken node);
    void caseAGtCompareToken(AGtCompareToken node);
    void caseAGeCompareToken(AGeCompareToken node);
    void caseASomeCompositePredicate(ASomeCompositePredicate node);
    void caseAAnyCompositePredicate(AAnyCompositePredicate node);
    void caseAAllCompositePredicate(AAllCompositePredicate node);
    void caseAPlusAdditiveExpr(APlusAdditiveExpr node);
    void caseALastPlusAdditiveExpr(ALastPlusAdditiveExpr node);
    void caseAFirstPlusAdditiveExpr(AFirstPlusAdditiveExpr node);
    void caseABothPlusAdditiveExpr(ABothPlusAdditiveExpr node);
    void caseAMinusAdditiveExpr(AMinusAdditiveExpr node);
    void caseALastMinusAdditiveExpr(ALastMinusAdditiveExpr node);
    void caseAFirstMinusAdditiveExpr(AFirstMinusAdditiveExpr node);
    void caseABothMinusAdditiveExpr(ABothMinusAdditiveExpr node);
    void caseAUnionAdditiveExpr(AUnionAdditiveExpr node);
    void caseALastUnionAdditiveExpr(ALastUnionAdditiveExpr node);
    void caseAFirstUnionAdditiveExpr(AFirstUnionAdditiveExpr node);
    void caseABothUnionAdditiveExpr(ABothUnionAdditiveExpr node);
    void caseAExceptAdditiveExpr(AExceptAdditiveExpr node);
    void caseALastExceptAdditiveExpr(ALastExceptAdditiveExpr node);
    void caseAFirstExceptAdditiveExpr(AFirstExceptAdditiveExpr node);
    void caseABothExceptAdditiveExpr(ABothExceptAdditiveExpr node);
    void caseABinorAdditiveExpr(ABinorAdditiveExpr node);
    void caseALastBinorAdditiveExpr(ALastBinorAdditiveExpr node);
    void caseAFirstBinorAdditiveExpr(AFirstBinorAdditiveExpr node);
    void caseABothBinorAdditiveExpr(ABothBinorAdditiveExpr node);
    void caseAMultiplicativeExprAdditiveExpr(AMultiplicativeExprAdditiveExpr node);
    void caseATimesMultiplicativeExpr(ATimesMultiplicativeExpr node);
    void caseALastTimesMultiplicativeExpr(ALastTimesMultiplicativeExpr node);
    void caseAFirstTimesMultiplicativeExpr(AFirstTimesMultiplicativeExpr node);
    void caseABothTimesMultiplicativeExpr(ABothTimesMultiplicativeExpr node);
    void caseAIntersectionMultiplicativeExpr(AIntersectionMultiplicativeExpr node);
    void caseALastIntersectionMultiplicativeExpr(ALastIntersectionMultiplicativeExpr node);
    void caseAFirstIntersectionMultiplicativeExpr(AFirstIntersectionMultiplicativeExpr node);
    void caseABothIntersectionMultiplicativeExpr(ABothIntersectionMultiplicativeExpr node);
    void caseADivideMultiplicativeExpr(ADivideMultiplicativeExpr node);
    void caseALastDivideMultiplicativeExpr(ALastDivideMultiplicativeExpr node);
    void caseAFirstDivideMultiplicativeExpr(AFirstDivideMultiplicativeExpr node);
    void caseABothDivideMultiplicativeExpr(ABothDivideMultiplicativeExpr node);
    void caseAModMultiplicativeExpr(AModMultiplicativeExpr node);
    void caseALastModMultiplicativeExpr(ALastModMultiplicativeExpr node);
    void caseAFirstModMultiplicativeExpr(AFirstModMultiplicativeExpr node);
    void caseABothModMultiplicativeExpr(ABothModMultiplicativeExpr node);
    void caseAIntersectMultiplicativeExpr(AIntersectMultiplicativeExpr node);
    void caseALastIntersectMultiplicativeExpr(ALastIntersectMultiplicativeExpr node);
    void caseAFirstIntersectMultiplicativeExpr(AFirstIntersectMultiplicativeExpr node);
    void caseABothIntersectMultiplicativeExpr(ABothIntersectMultiplicativeExpr node);
    void caseAInExprMultiplicativeExpr(AInExprMultiplicativeExpr node);
    void caseATmpInExpr(ATmpInExpr node);
    void caseAListTmpInExpr(AListTmpInExpr node);
    void caseALastTmpInExpr(ALastTmpInExpr node);
    void caseAFirstTmpInExpr(AFirstTmpInExpr node);
    void caseABothTmpInExpr(ABothTmpInExpr node);
    void caseADistinctTmpInExpr(ADistinctTmpInExpr node);
    void caseADistinctIdentifierTmpInExpr(ADistinctIdentifierTmpInExpr node);
    void caseAUnaryExprTmpInExpr(AUnaryExprTmpInExpr node);
    void caseACommaExpr(ACommaExpr node);
    void caseACommaSeparatedExprCommaExpr(ACommaSeparatedExprCommaExpr node);
    void caseAInlist(AInlist node);
    void caseAPlusUnaryExpr(APlusUnaryExpr node);
    void caseAIdentifierPlusUnaryExpr(AIdentifierPlusUnaryExpr node);
    void caseAMinusUnaryExpr(AMinusUnaryExpr node);
    void caseAIdentifierMinusUnaryExpr(AIdentifierMinusUnaryExpr node);
    void caseAAbsUnaryExpr(AAbsUnaryExpr node);
    void caseAIdentifierAbsUnaryExpr(AIdentifierAbsUnaryExpr node);
    void caseANotUnaryExpr(ANotUnaryExpr node);
    void caseAIdentifierNotUnaryExpr(AIdentifierNotUnaryExpr node);
    void caseAPostfixUnaryExpr(APostfixUnaryExpr node);
    void caseAIndexPostfixExpr(AIndexPostfixExpr node);
    void caseAIdentifierIndexPostfixExpr(AIdentifierIndexPostfixExpr node);
    void caseAFieldPostfixExpr(AFieldPostfixExpr node);
    void caseAIdentifierFieldPostfixExpr(AIdentifierFieldPostfixExpr node);
    void caseAMethodPostfixExpr(AMethodPostfixExpr node);
    void caseAIdentifierMethodPostfixExpr(AIdentifierMethodPostfixExpr node);
    void caseAPrimaryExprPostfixExpr(APrimaryExprPostfixExpr node);
    void caseATransitivedotDotarrow(ATransitivedotDotarrow node);
    void caseADotDotarrow(ADotDotarrow node);
    void caseAArrowDotarrow(AArrowDotarrow node);
    void caseAIndex(AIndex node);
    void caseAIndexAll(AIndexAll node);
    void caseASimpleIndexAll(ASimpleIndexAll node);
    void caseAColonIndexAll(AColonIndexAll node);
    void caseALastIdentifierIndexAll(ALastIdentifierIndexAll node);
    void caseAIdentifierIndexAll(AIdentifierIndexAll node);
    void caseACommaIndexAll(ACommaIndexAll node);
    void caseAFirstCommaIndexAll(AFirstCommaIndexAll node);
    void caseALastCommaIndexAll(ALastCommaIndexAll node);
    void caseABothCommaIndexAll(ABothCommaIndexAll node);
    void caseAConversionPrimaryExpr(AConversionPrimaryExpr node);
    void caseACollectionPrimaryExpr(ACollectionPrimaryExpr node);
    void caseAAggregatePrimaryExpr(AAggregatePrimaryExpr node);
    void caseAUndefinedPrimaryExpr(AUndefinedPrimaryExpr node);
    void caseAObjconstrPrimaryExpr(AObjconstrPrimaryExpr node);
    void caseAStructconstrPrimaryExpr(AStructconstrPrimaryExpr node);
    void caseACollconstrPrimaryExpr(ACollconstrPrimaryExpr node);
    void caseAMethodPrimaryExpr(AMethodPrimaryExpr node);
    void caseAQueryparamPrimaryExpr(AQueryparamPrimaryExpr node);
    void caseALiteralPrimaryExpr(ALiteralPrimaryExpr node);
    void caseATmpQueryPrimaryExpr(ATmpQueryPrimaryExpr node);
    void caseAListtosetConversionExpr(AListtosetConversionExpr node);
    void caseAIdentifierListtosetConversionExpr(AIdentifierListtosetConversionExpr node);
    void caseAElementConversionExpr(AElementConversionExpr node);
    void caseAIdentifierElementConversionExpr(AIdentifierElementConversionExpr node);
    void caseAFlattenConversionExpr(AFlattenConversionExpr node);
    void caseAIdentifierFlattenConversionExpr(AIdentifierFlattenConversionExpr node);
    void caseAFirstCollectionExpr(AFirstCollectionExpr node);
    void caseAIdentifierFirstCollectionExpr(AIdentifierFirstCollectionExpr node);
    void caseALastCollectionExpr(ALastCollectionExpr node);
    void caseAIdentifierLastCollectionExpr(AIdentifierLastCollectionExpr node);
    void caseAUniqueCollectionExpr(AUniqueCollectionExpr node);
    void caseAIdentifierUniqueCollectionExpr(AIdentifierUniqueCollectionExpr node);
    void caseAExistsCollectionExpr(AExistsCollectionExpr node);
    void caseAIdentifierExistsCollectionExpr(AIdentifierExistsCollectionExpr node);
    void caseASumAggregateExpr(ASumAggregateExpr node);
    void caseAIdentifierSumAggregateExpr(AIdentifierSumAggregateExpr node);
    void caseAMinAggregateExpr(AMinAggregateExpr node);
    void caseAIdentifierMinAggregateExpr(AIdentifierMinAggregateExpr node);
    void caseAMaxAggregateExpr(AMaxAggregateExpr node);
    void caseAIdentifierMaxAggregateExpr(AIdentifierMaxAggregateExpr node);
    void caseAAvgAggregateExpr(AAvgAggregateExpr node);
    void caseAIdentifierAvgAggregateExpr(AIdentifierAvgAggregateExpr node);
    void caseACountAggregateExpr(ACountAggregateExpr node);
    void caseAQueryStar(AQueryStar node);
    void caseAIdentifierQueryStar(AIdentifierQueryStar node);
    void caseAStarQueryStar(AStarQueryStar node);
    void caseAUndefUndefinedExpr(AUndefUndefinedExpr node);
    void caseAIdentifierUndefUndefinedExpr(AIdentifierUndefUndefinedExpr node);
    void caseADefUndefinedExpr(ADefUndefinedExpr node);
    void caseAIdentifierDefUndefinedExpr(AIdentifierDefUndefinedExpr node);
    void caseAStructConstruction(AStructConstruction node);
    void caseAFieldList(AFieldList node);
    void caseASingleFieldList(ASingleFieldList node);
    void caseAField(AField node);
    void caseATmpField(ATmpField node);
    void caseATmpIdentifierField(ATmpIdentifierField node);
    void caseAArrayCollectionConstruction(AArrayCollectionConstruction node);
    void caseASetCollectionConstruction(ASetCollectionConstruction node);
    void caseABagCollectionConstruction(ABagCollectionConstruction node);
    void caseAListCollectionConstruction(AListCollectionConstruction node);
    void caseAValueValueOrRange(AValueValueOrRange node);
    void caseARangeValueOrRange(ARangeValueOrRange node);
    void caseAValueList(AValueList node);
    void caseATmpValueList(ATmpValueList node);
    void caseASingleValueList(ASingleValueList node);
    void caseALastValueList(ALastValueList node);
    void caseAIdentifierValueList(AIdentifierValueList node);
    void caseAListRange(AListRange node);
    void caseAQueryParam(AQueryParam node);
    void caseANamedQueryParam(ANamedQueryParam node);
    void caseAShortType(AShortType node);
    void caseALongType(ALongType node);
    void caseALonglongType(ALonglongType node);
    void caseAFloatType(AFloatType node);
    void caseADoubleType(ADoubleType node);
    void caseACharType(ACharType node);
    void caseAStringType(AStringType node);
    void caseABooleanType(ABooleanType node);
    void caseAOctetType(AOctetType node);
    void caseAEnumType(AEnumType node);
    void caseADateType(ADateType node);
    void caseATimeType(ATimeType node);
    void caseAIntervalType(AIntervalType node);
    void caseATimestampType(ATimestampType node);
    void caseASetType(ASetType node);
    void caseABagType(ABagType node);
    void caseAListType(AListType node);
    void caseAArrayType(AArrayType node);
    void caseADictionaryType(ADictionaryType node);
    void caseATypeX(ATypeX node);
    void caseABooleanLiteral(ABooleanLiteral node);
    void caseALongLiteral(ALongLiteral node);
    void caseADoubleLiteral(ADoubleLiteral node);
    void caseACharLiteral(ACharLiteral node);
    void caseAStringLiteral(AStringLiteral node);
    void caseADateLiteral(ADateLiteral node);
    void caseATimeLiteral(ATimeLiteral node);
    void caseATimestampLiteral(ATimestampLiteral node);
    void caseANilLiteral(ANilLiteral node);
    void caseAUndefinedLiteral(AUndefinedLiteral node);
    void caseATruePBoolean(ATruePBoolean node);
    void caseAFalsePBoolean(AFalsePBoolean node);
    void caseAPDate(APDate node);
    void caseAPTime(APTime node);
    void caseAPTimestamp(APTimestamp node);
    void caseADateContent(ADateContent node);
    void caseATimeContent(ATimeContent node);

    void caseTWhite(TWhite node);
    void caseTDotdotdot(TDotdotdot node);
    void caseTArrow(TArrow node);
    void caseTBinor(TBinor node);
    void caseTBracketL(TBracketL node);
    void caseTBracketR(TBracketR node);
    void caseTColon(TColon node);
    void caseTComma(TComma node);
    void caseTDivide(TDivide node);
    void caseTDollar(TDollar node);
    void caseTDotdot(TDotdot node);
    void caseTDot(TDot node);
    void caseTEq(TEq node);
    void caseTGe(TGe node);
    void caseTGt(TGt node);
    void caseTLe(TLe node);
    void caseTLt(TLt node);
    void caseTMinus(TMinus node);
    void caseTNe(TNe node);
    void caseTPlus(TPlus node);
    void caseTQuote(TQuote node);
    void caseTSemicolon(TSemicolon node);
    void caseTSqBracketL(TSqBracketL node);
    void caseTSqBracketR(TSqBracketR node);
    void caseTStar(TStar node);
    void caseTSetAnd(TSetAnd node);
    void caseTAbs(TAbs node);
    void caseTAlias(TAlias node);
    void caseTAll(TAll node);
    void caseTAndthen(TAndthen node);
    void caseTAnd(TAnd node);
    void caseTAny(TAny node);
    void caseTArray(TArray node);
    void caseTAs(TAs node);
    void caseTAsc(TAsc node);
    void caseTAvg(TAvg node);
    void caseTBag(TBag node);
    void caseTBoolean(TBoolean node);
    void caseTBy(TBy node);
    void caseTCount(TCount node);
    void caseTChar(TChar node);
    void caseTDate(TDate node);
    void caseTDefine(TDefine node);
    void caseTDesc(TDesc node);
    void caseTDictionary(TDictionary node);
    void caseTDistinct(TDistinct node);
    void caseTDouble(TDouble node);
    void caseTElement(TElement node);
    void caseTEnum(TEnum node);
    void caseTExcept(TExcept node);
    void caseTExists(TExists node);
    void caseTFalse(TFalse node);
    void caseTFirst(TFirst node);
    void caseTFlatten(TFlatten node);
    void caseTFloat(TFloat node);
    void caseTFor(TFor node);
    void caseTFrom(TFrom node);
    void caseTGroup(TGroup node);
    void caseTHaving(THaving node);
    void caseTInclude(TInclude node);
    void caseTInstanceof(TInstanceof node);
    void caseTIntersect(TIntersect node);
    void caseTInterval(TInterval node);
    void caseTIn(TIn node);
    void caseTIsDefined(TIsDefined node);
    void caseTIsUndefined(TIsUndefined node);
    void caseTLast(TLast node);
    void caseTLike(TLike node);
    void caseTRlike(TRlike node);
    void caseTListtoset(TListtoset node);
    void caseTList(TList node);
    void caseTLong(TLong node);
    void caseTMax(TMax node);
    void caseTMod(TMod node);
    void caseTMin(TMin node);
    void caseTNil(TNil node);
    void caseTNot(TNot node);
    void caseTOctet(TOctet node);
    void caseTOrder(TOrder node);
    void caseTOrelse(TOrelse node);
    void caseTOr(TOr node);
    void caseTReturn(TReturn node);
    void caseTTquery(TTquery node);
    void caseTSelect(TSelect node);
    void caseTSet(TSet node);
    void caseTSome(TSome node);
    void caseTShort(TShort node);
    void caseTString(TString node);
    void caseTStruct(TStruct node);
    void caseTSum(TSum node);
    void caseTTimestamp(TTimestamp node);
    void caseTTime(TTime node);
    void caseTTrue(TTrue node);
    void caseTUndefined(TUndefined node);
    void caseTUndefine(TUndefine node);
    void caseTUnion(TUnion node);
    void caseTUnique(TUnique node);
    void caseTUnsigned(TUnsigned node);
    void caseTWhere(TWhere node);
    void caseTIdentifier(TIdentifier node);
    void caseTDoubleLiteral(TDoubleLiteral node);
    void caseTLongLiteral(TLongLiteral node);
    void caseTCharLiteral(TCharLiteral node);
    void caseTStringLiteral(TStringLiteral node);
    void caseTLineComment(TLineComment node);
    void caseTMultilineComment(TMultilineComment node);
    void caseEOF(EOF node);
}