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

/**
 * Abstract Syntax Tree to ASCII converter. This class is
 * responsible for conversion the tree structure into ascii
 * well-formed OQL format.
 *
 *@author Mariusz Nowostawski
 *@version @version@ $Revision: 1.1.1.1 $
 */
class Oql2AsciiWalker extends DepthFirstAdapter {

  OqlOutputStrategy strategy = null;
  Hashtable context = new Hashtable();

  public void setStategy(OqlOutputStrategy strategy) {
    this.strategy = strategy;
  }

  public void setContext(Hashtable context) {
    this.context = context;
  }

//    public void defaultOut(Node node) {
////        super.defaultOut(node);    //To change body of overridden methods use File | Settings | File Templates.
//        System.out.println(node);    // TODO: implement better walker
//    }

  ///// Productions

  public void caseANamedParamExpr(ANamedParamExpr node) {    
    strategy.doSubst(node, context);
  }

  /////  TOKENS

  public void caseTArrow(TArrow node){
    strategy.needSpace = false;
    strategy.withSpace(node);
  }

  public void caseTBinor(TBinor node){
    strategy.withSpace(node);
  }  

  public void caseTBracketL(TBracketL node){
    strategy.needSpace = false;
    strategy.withNewLine(node);
    strategy.needSpace = false;
    strategy.pushIndent(2);
  }

  public void caseTBracketR(TBracketR node){
    strategy.needSpace = false;
    strategy.withoutSpace(node);
    strategy.popIndent();
    strategy.popIndent();
  }

  public void caseTColon(TColon node){
    strategy.needSpace = false;
    strategy.withoutSpace(node);
  }
  
  public void caseTComma(TComma node){
    strategy.needSpace = false;
    strategy.withoutSpace(node);
  }
  public void caseTDivide(TDivide node){
    strategy.withSpace(node);
  }
  public void caseTDollar(TDollar node){
    strategy.withoutSpace(node);
    strategy.needSpace = false;
  }
  public void caseTDotdot(TDotdot node){
    strategy.withSpace(node);
  }
  
  public void caseTDot(TDot node){
    strategy.needSpace = false;
    strategy.withoutSpace(node);
    strategy.needSpace = false;
  }

  public void caseTEq(TEq node){
    strategy.withSpace(node);
  }
  public void caseTGe(TGe node){
    strategy.withSpace(node);
  }
  public void caseTGt(TGt node){
    strategy.withSpace(node);
  }  
  public void caseTLe(TLe node){
    strategy.withSpace(node);
  }  
  public void caseTLt(TLt node){
    strategy.withSpace(node);
  }
  public void caseTNe(TNe node){
    strategy.withSpace(node);
  }
  public void caseTMinus(TMinus node){
    strategy.withSpace(node);
  }
  public void caseTPlus(TPlus node){
    strategy.withSpace(node);
  }
  public void caseTQoute(TQuote node){
    strategy.withoutSpace(node);
    strategy.needSpace = false;
  }
  public void caseTTquery(TTquery node){
    strategy.withSpace(node);
  }

  public void caseTSemicolon(TSemicolon node){
    strategy.needSpace = false;
    strategy.withSpace(node);
  }
  public void caseTSqBracketL(TSqBracketL node){
    strategy.needSpace = false;
    strategy.withoutSpace(node);
    strategy.needSpace = false;
  }
  public void caseTSqBracketR(TSqBracketR node){
    strategy.needSpace = false;
    strategy.withoutSpace(node);
  }
  public void caseTStar(TStar node){
    strategy.withSpace(node);
  }

  public void caseTAbs(TAbs node){
    strategy.withSpace(node);
  }
  public void caseTAll(TAll node){
    strategy.withSpace(node);
  }
  public void caseTAndthen(TAndthen node){
    strategy.withSpace(node);
  }
  public void caseTAnd(TAnd node){
    strategy.withNewLine(node);
  }
  public void caseTAny(TAny node){
    strategy.withSpace(node);
  }
  public void caseTArray(TArray node){
    strategy.withoutSpace(node);
  }
  public void caseTAs(TAs node){
    strategy.withSpace(node);
  }


  public void caseTDistinct(TDistinct node){
    strategy.withSpace(node);
  }
  public void caseTDouble(TDouble node){
    strategy.withoutSpace(node);
  }
  public void caseTElement(TElement node){
    strategy.withSpace(node);
  }
  public void caseTExcept(TExcept node){
    strategy.withSpace(node);
  }
  public void caseTExists(TExists node){
    strategy.withSpace(node);
  }
  public void caseTFalse(TFalse node){
    strategy.withSpace(node);
  }

  public void caseTStruct(TStruct node){
    strategy.withoutSpace(node);
    strategy.pushIndent(0);
    strategy.nlnum--; // stop the next '(' from newlining
  }  
  public void caseTSum(TSum node){
    strategy.withSpace(node);
  }  


  public void caseTTrue(TTrue node){
    strategy.withSpace(node);
  }  


  public void caseTIn(TIn node){
    strategy.withSpace(node);
  }

  public void caseTOr(TOr node){
    strategy.withNewLine(node);
  }

  public void caseTFrom(TFrom node){
    strategy.needSpace = false;
    strategy.withNewLine(node);
    strategy.pushIndent(5);
  }

  public void caseTWhere(TWhere node){
    strategy.needSpace = false;
    strategy.popIndent();
    strategy.withNewLine(node);
    strategy.pushIndent(6);
  }

  public void caseTSelect(TSelect node){
    strategy.needSpace = false;
    strategy.withSpace(node);
  }




  ///// Literals
  
  public void caseTDoubleLiteral(TDoubleLiteral node){
    strategy.withSpace(node);
  }
  public void caseTLongLiteral(TLongLiteral node){
    strategy.withSpace(node);
  }
  public void caseTCharLiteral(TCharLiteral node){
    strategy.withSpace(node);
  } 
  public void caseTStringLiteral(TStringLiteral node){
    strategy.withSpace(node);
  }
  
  public void caseTIdentifier(TIdentifier node){
    strategy.withoutSpace(node);
  }

}
