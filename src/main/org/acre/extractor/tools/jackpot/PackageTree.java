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
package org.acre.extractor.tools.jackpot;

import static com.sun.tools.javac.code.Flags.SYNTHETIC;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.PackageSymbol;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.Tree;
import com.sun.tools.javac.tree.Tree.TopLevel;
import com.sun.tools.javac.tree.TreeScanner;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;

/**
 * A class that provides a "single point of entry" for scanning a list of
 * Java source files, by creating a hierarchy of javac Trees which match
 * the package symbol hierarchy.
 *
 * @author Tom Ball, Yury Kamen
 */
public final class PackageTree {
    public final static int PACKAGEDEF_TAG = 0;

    private static final Context.Key<PackageTree> treeKey =
            new Context.Key<PackageTree>();

    private final Context context;
    private final PackageDef root;
    private final Symtab symtab;
    private final Name.Table names;

    private PackageTree(Context context) {
        this.context = context;
        context.put(treeKey, this);
        symtab = Symtab.instance(context);
        root = new PackageDef(symtab.rootPackage);
        names = Name.Table.instance(context);
    }

    public static PackageTree instance(Context context) {
        PackageTree instance = context.get(treeKey);
        if (instance == null) {
            instance = new PackageTree(context);
        }
        return instance;
    }

    public static PackageDef create(Context context, List<Tree> roots) {
        PackageTree pkgtree = instance(context);
        for (List<Tree> l = roots; l.nonEmpty(); l = l.tail) {
            assert l.head.tag == Tree.TOPLEVEL;
            pkgtree.addToPackage((TopLevel) l.head);
        }
        return pkgtree.root;
    }

    private void addToPackage(TopLevel tree) {
        PackageDef pkg = findOrAddPackage((PackageSymbol) tree.packge);
        pkg.addChild(tree);
    }

    private PackageDef findOrAddPackage(Symbol sym) {
        assert sym instanceof PackageSymbol;
        if (sym.owner == null) {
            assert root.sym == sym;
            return root;
        }
        PackageDef owner = findOrAddPackage(sym.owner);
        for (List<Tree> l = owner.defs; l.nonEmpty(); l = l.tail) {
            if (l.head instanceof PackageDef &&
                    ((PackageDef) l.head).sym == sym) {
                return (PackageDef) l.head;
            }
        }

        // not found: create new PackageDef and add it to its owner.
        PackageDef me = new PackageDef((PackageSymbol) sym);
        owner.addChild(me);
        return me;
    }

    public PackageDef getRoot() {
        return root;
    }

    public static class PackageDef extends Tree {
        public Name name;
        public List<Tree> defs;
        public PackageSymbol sym;

        public PackageDef(PackageSymbol sym) {
            super(PACKAGEDEF_TAG);
            this.sym = sym;
            this.name = sym.name;
            this.defs = new List<Tree>();
        }

        public void accept(Visitor v) {
            if (v instanceof Scanner) {
                ((Scanner) v).visitPackageDef(this);
            } else {
                for (List<Tree> l = defs; l.nonEmpty(); l = l.tail) {
                    l.head.accept(v);
                }
            }
        }

        void addChild(Tree t) {
            defs = defs.append(t);
        }
    }

    /**
     * Java Fact extractor scanner (collects usage counters)
     *
     * @author Yury Kamen
     */
    public static class MethodCounters {
        public int numStatements = 0;
        public int numLocalVariables = 0;
        public int numTryClauses = 0;
        public int numCatchClauses = 0;
        public int numCalls = 0;
        public int numInstantiations = 0;
        public int numAnonClassInstantiations = 0;
        public int numSynchronizedBlocks = 0;
        public int numReturnStatements = 0;
        public int numThrowStatements = 0;
        public int numAssertStatements = 0;
        public int numCasts = 0;
        public int numSkips = 0;
        public int numAssignStatements = 0;
        public int numInitializedLocalVariables = 0;
        public int numLoopStatements = 0;
        public int numIfStatements = 0;
        public int numSwitchStatements = 0;
        public int numCaseStatements = 0;
        public int numBranchStatements = 0;
        public int numLinearSegments = 1;
        public int numLocalAnnotations = 0;
        public int numLines = 0;
        public boolean hasSources = false;

        public void complete() {
            if (numStatements == 0) {
                numLinearSegments = 0;
            }
        }

        public void clear() {
            numStatements = 0;
            numLocalVariables = 0;
            numTryClauses = 0;
            numCatchClauses = 0;
            numCalls = 0;
            numInstantiations = 0;
            numAnonClassInstantiations = 0;
            numSynchronizedBlocks = 0;
            numReturnStatements = 0;
            numThrowStatements = 0;
            numAssertStatements = 0;
            numCasts = 0;
            numSkips = 0;
            numAssignStatements = 0;
            numInitializedLocalVariables = 0;
            numLoopStatements = 0;
            numIfStatements = 0;
            numSwitchStatements = 0;
            numCaseStatements = 0;
            numBranchStatements = 0;
            numLinearSegments = 1;
            numLocalAnnotations = 0;
            numLines = 0;
            hasSources = false;
        }
    }

    public static class ClassCounters extends MethodCounters {
        public int numFields = 0;
        public int numInitializedFields = 0;
        public int numMethods = 0;

        public void clear() {
            super.clear();
            numInitializedFields = 0;
            numFields = 0;
            numMethods = 0;
        }
    }

    public static class Scanner extends TreeScanner {
        protected ClassCounters classCounters = new ClassCounters();
        protected MethodCounters methodCounters = new MethodCounters();

        protected void initMethodCounters() {
            methodCounters.clear();
        }

        public void initClassCounters() {
            classCounters.clear();
            methodCounters.clear();
        }

        public void updateClassCounters() {
            methodCounters.complete();

            classCounters.numStatements += methodCounters.numStatements;
            classCounters.numLocalVariables += methodCounters.numLocalVariables;
            classCounters.numTryClauses += methodCounters.numTryClauses;
            classCounters.numCatchClauses += methodCounters.numCatchClauses;
            classCounters.numCalls += methodCounters.numCalls;
            classCounters.numInstantiations += methodCounters.numInstantiations;
            classCounters.numAnonClassInstantiations += methodCounters.numAnonClassInstantiations;
            classCounters.numSynchronizedBlocks += methodCounters.numSynchronizedBlocks;
            classCounters.numReturnStatements += methodCounters.numReturnStatements;
            classCounters.numThrowStatements += methodCounters.numThrowStatements;
            classCounters.numAssertStatements += methodCounters.numAssertStatements;
            classCounters.numCasts += methodCounters.numCasts;
            classCounters.numSkips += methodCounters.numSkips;
            classCounters.numAssignStatements += methodCounters.numAssignStatements;
            classCounters.numInitializedLocalVariables += methodCounters.numInitializedLocalVariables;
            classCounters.numLoopStatements += methodCounters.numLoopStatements;
            classCounters.numIfStatements += methodCounters.numIfStatements;
            classCounters.numSwitchStatements += methodCounters.numSwitchStatements;
            classCounters.numCaseStatements += methodCounters.numCaseStatements;
            classCounters.numBranchStatements += methodCounters.numBranchStatements;
            classCounters.numLinearSegments += methodCounters.numLinearSegments;
            classCounters.numLocalAnnotations += methodCounters.numLocalAnnotations;
            classCounters.numLines += methodCounters.numLines;

            classCounters.numMethods += 1;
        }

        protected boolean isSpecialName(String name) {
            return name.equals("null") || name.equals("true")
                    || name.equals("false") || name.equals("this")
                    || name.equals("super");
        }

        // A variable is interesting iff it is a field (owned by a class),
        // and its owning class is interesting.
        protected boolean isInteresting(Symbol.VarSymbol varSymbol) {
            return (!(isSpecialName(varSymbol.name.toString()))
                    && varSymbol.owner instanceof Symbol.ClassSymbol
                    && isInteresting((Symbol.ClassSymbol) varSymbol.owner)
                    )
                    || isInterestingSalsa(varSymbol);
        }

        // Yury Kamen - made acre-generated
        protected boolean isInterestingSalsa(Symbol.VarSymbol varSymbol) {
            return varSymbol.name.toString().startsWith("$acre$");
        }

        // A method is interesting iff its owning class is interesting.
        protected boolean isInteresting(Symbol.MethodSymbol methodSymbol) {
            return isInteresting((Symbol.ClassSymbol) methodSymbol.owner);
        }

        // Not really needed ... but kept for possible future debugging as
        // I've had "issues" with encountering synthetic entities where I
        // wasn't expecting them.
        protected boolean isSynthetic(Symbol s) {
            return (s.flags() & SYNTHETIC) != 0;
        }

        // A class is interesting iff it is owned by a package or an
        // interesting class.
        protected boolean isInteresting(Symbol.ClassSymbol classSymbol) {
            // This should not be necessary.
            if (isSynthetic(classSymbol)) {
                return false;
            } else if (classSymbol.owner instanceof PackageSymbol) {
                return true;
            } else if (classSymbol.owner instanceof Symbol.ClassSymbol) {
                return isInteresting((Symbol.ClassSymbol) classSymbol.owner);
            } else {
                return false;
            }
        }

        public void visitTopLevel(Tree.TopLevel topLevel) {
            super.visitTopLevel(topLevel);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void visitImport(Tree.Import anImport) {
            super.visitImport(anImport);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void visitClassDef(Tree.ClassDef classDef) {
            initClassCounters();
            classCounters.hasSources = true;
            super.visitClassDef(classDef);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void visitMethodDef(Tree.MethodDef methodDef) {
            initMethodCounters();
            methodCounters.hasSources = true;

            // Synchronized method is considered as one more synchronized block
            if((methodDef.mods.flags & Flags.SYNCHRONIZED) != 0) {
                methodCounters.numSynchronizedBlocks++;
            }

            super.visitMethodDef(methodDef);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void visitVarDef(Tree.VarDef varDef) {
            Symbol.VarSymbol varSymbol = varDef.sym;
            if (!(
                    isSpecialName(varSymbol.name.toString())
                    || isInterestingSalsa(varSymbol)
                    || isSynthetic(varSymbol)
                    )
            ) {
                if (varSymbol.owner instanceof Symbol.MethodSymbol) {
                    methodCounters.numLocalVariables++;
                    if (varDef.init != null) {
                        methodCounters.numStatements++;
                        methodCounters.numInitializedLocalVariables++;
                    }
                } else if (varSymbol.owner instanceof Symbol.ClassSymbol
                        && isInteresting((Symbol.ClassSymbol) varSymbol.owner)) {
                    classCounters.numFields++;
                    if (varDef.init != null) {
                        classCounters.numInitializedFields++;
                    }
                }
            }

            super.visitVarDef(varDef);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void visitSkip(Tree.Skip skip) {
            methodCounters.numStatements++;
            methodCounters.numSkips++;
            super.visitSkip(skip);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void visitBlock(Tree.Block block) {
            super.visitBlock(block);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void visitDoLoop(Tree.DoLoop doLoop) {
            methodCounters.numStatements++;
            methodCounters.numLoopStatements++;
            methodCounters.numLinearSegments++;
            super.visitDoLoop(doLoop);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void visitWhileLoop(Tree.WhileLoop whileLoop) {
            methodCounters.numStatements++;
            methodCounters.numLoopStatements++;
            methodCounters.numLinearSegments++;
            super.visitWhileLoop(whileLoop);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void visitForLoop(Tree.ForLoop forLoop) {
            methodCounters.numStatements++;
            methodCounters.numLoopStatements++;
            methodCounters.numLinearSegments++;
            super.visitForLoop(forLoop);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void visitForeachLoop(Tree.ForeachLoop foreachLoop) {
            methodCounters.numStatements++;
            methodCounters.numLoopStatements++;
            methodCounters.numLinearSegments++;
            super.visitForeachLoop(foreachLoop);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void visitLabelled(Tree.Labelled labelled) {
            super.visitLabelled(labelled);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void visitSwitch(Tree.Switch aSwitch) {
            methodCounters.numStatements++;
            methodCounters.numSwitchStatements++;
            super.visitSwitch(aSwitch);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void visitCase(Tree.Case aCase) {
            methodCounters.numCaseStatements++;
            super.visitCase(aCase);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void visitSynchronized(Tree.Synchronized aSynchronized) {
            methodCounters.numStatements++;
            methodCounters.numSynchronizedBlocks++;
            super.visitSynchronized(aSynchronized);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void visitTry(Tree.Try aTry) {
            methodCounters.numStatements++;
            methodCounters.numTryClauses++;
            super.visitTry(aTry);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void visitCatch(Tree.Catch aCatch) {
            methodCounters.numCatchClauses++;
            methodCounters.numLinearSegments++;
            super.visitCatch(aCatch);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void visitConditional(Tree.Conditional conditional) {
            super.visitConditional(conditional);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void visitIf(Tree.If anIf) {
            methodCounters.numStatements++;
            methodCounters.numIfStatements++;
            methodCounters.numLinearSegments++;
            super.visitIf(anIf);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void visitExec(Tree.Exec exec) {
            methodCounters.numStatements++;
            super.visitExec(exec);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void visitBreak(Tree.Break aBreak) {
            methodCounters.numStatements++;
            methodCounters.numBranchStatements++;
            super.visitBreak(aBreak);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void visitContinue(Tree.Continue aContinue) {
            methodCounters.numStatements++;
            methodCounters.numBranchStatements++;
            super.visitContinue(aContinue);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void visitReturn(Tree.Return aReturn) {
            methodCounters.numStatements++;
            methodCounters.numReturnStatements++;
            super.visitReturn(aReturn);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void visitThrow(Tree.Throw aThrow) {
            methodCounters.numStatements++;
            methodCounters.numThrowStatements++;
            super.visitThrow(aThrow);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void visitAssert(Tree.Assert assertTree) {
            methodCounters.numStatements++;
            methodCounters.numAssertStatements++;
            super.visitAssert(assertTree);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void visitApply(Tree.Apply apply) {
            methodCounters.numCalls++;
            super.visitApply(apply);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void visitNewClass(Tree.NewClass newClass) {
            methodCounters.numInstantiations++;
            if(newClass.def != null) {
                methodCounters.numAnonClassInstantiations++;
            }
            super.visitNewClass(newClass);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void visitNewArray(Tree.NewArray newArray) {
            methodCounters.numInstantiations++;
            super.visitNewArray(newArray);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void visitParens(Tree.Parens parens) {
            super.visitParens(parens);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void visitAssign(Tree.Assign assign) {
            methodCounters.numStatements++;
            methodCounters.numAssignStatements++;
            super.visitAssign(assign);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void visitAssignop(Tree.Assignop assignop) {
            methodCounters.numStatements++;
            methodCounters.numAssignStatements++;
            super.visitAssignop(assignop);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void visitUnary(Tree.Unary unary) {
            super.visitUnary(unary);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void visitBinary(Tree.Binary binary) {
            super.visitBinary(binary);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void visitTypeCast(Tree.TypeCast typeCast) {
            methodCounters.numCasts++;
            super.visitTypeCast(typeCast);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void visitTypeTest(Tree.TypeTest typeTest) {
            super.visitTypeTest(typeTest);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void visitIndexed(Tree.Indexed indexed) {
            super.visitIndexed(indexed);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void visitSelect(Tree.Select select) {
            super.visitSelect(select);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void visitIdent(Tree.Ident ident) {
            super.visitIdent(ident);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void visitLiteral(Tree.Literal literal) {
            super.visitLiteral(literal);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void visitTypeIdent(Tree.TypeIdent typeIdent) {
            super.visitTypeIdent(typeIdent);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void visitTypeArray(Tree.TypeArray typeArray) {
            super.visitTypeArray(typeArray);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void visitTypeApply(Tree.TypeApply typeApply) {
            super.visitTypeApply(typeApply);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void visitTypeParameter(Tree.TypeParameter typeParameter) {
            super.visitTypeParameter(typeParameter);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void visitTypeArgument(Tree.TypeArgument typeArgument) {
            super.visitTypeArgument(typeArgument);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void visitModifiers(Tree.Modifiers modifiers) {
            super.visitModifiers(modifiers);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void visitAnnotation(Tree.Annotation annotation) {
            methodCounters.numLocalAnnotations++;
            super.visitAnnotation(annotation);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void visitErroneous(Tree.Erroneous erroneous) {
            super.visitErroneous(erroneous);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void visitLetExpr(Tree.LetExpr letExpr) {
            super.visitLetExpr(letExpr);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void visitTree(Tree tree) {
            super.visitTree(tree);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void scan(List<? extends Tree> list) {
            super.scan(list);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void scan(Tree tree) {
            super.scan(tree);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void visitPackageDef(PackageDef tree) {
            scan(tree.defs);
        }
    }
}
