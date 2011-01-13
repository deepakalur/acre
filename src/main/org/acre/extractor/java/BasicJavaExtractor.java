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
package org.acre.extractor.java;

import org.acre.extractor.tools.jackpot.PackageTree.PackageDef;
import org.acre.extractor.tools.jackpot.PackageTree;
import com.sun.tools.javac.code.*;
import static com.sun.tools.javac.code.Flags.*;
import static com.sun.tools.javac.code.Flags.SYNCHRONIZED;
import com.sun.tools.javac.code.Scope.Entry;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.code.Symbol.PackageSymbol;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.code.Type.*;
import com.sun.tools.javac.comp.Attr;
import com.sun.tools.javac.comp.Resolve;
import com.sun.tools.javac.tree.Tree;
import com.sun.tools.javac.tree.Tree.*;
import com.sun.tools.javac.tree.TreeInfo;
import com.sun.tools.javac.util.*;
import com.sun.tools.javac.util.List;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

/**
 * Dumps extracted facts for external static analysis.
 * Version 2.0 was the first Salsa (non-Jackpot) version of the extractor.
 *
 * @author Mike Godfrey, Yury Kamen
 * @version 2.1 June 17, 2004
 */

// To learn more about this class, have a look at these files:
//	$SALSA_EXTRACTOR/doc/usingSalsaFactExtractor.txt
//	$SALSA_EXTRACTOR/doc/notesOnTheExtractedFacts.txt

// TODO:
//     -- Investigate using this as an extractor for jar/class files
//        (i.e., no source provided).  See work started below on
//        walkSymbolTable().

public class BasicJavaExtractor {
    // The default behaviour is to dump line number but not column position
    // information about entities (the information comes directly from
    // javac).  By default, we don't dump the column position as it's often
    // wrong, it makes the line number information harder to deal with, and
    // it's not clear that the column position is useful information.  But
    // we will leave the possibility of extracting it just in case.
    protected static final boolean dumpColumnPosition = false;

    // Entities whose existence is inferred from jar files do not have
    // line/col positions in their symbols, so we invent one for them.
    protected static final String defaultPosition = dumpColumnPosition ? "1.1" : "1";

    // The scanner will do the actual tree walking; this is a member class
    // defined below.
    protected FactExtractorScanner scanner;

    protected boolean isRunning = false;
    private final String factTupleFileName = "factTuples.ta";
    private final String factAttributeFileName = "factAttributes.ta";
    private final String debugMesgFileName = "factDebugMesgs.txt";

    // Receives messages that appear in the lower left hand corner of the
    // Jackpot GUI.
    private Writer log;

    // Where the tuples, attributes, and debug messages get sent to.
    private FileWriter factTupleWriter;
    private FileWriter factAttributeWriter;
    private FileWriter debugMesgWriter;

    // Tom's adds:  These fields used to be inherited from the "env" field in Jackpot's Operator class.
    private Context context;
    private Name.Table nameTable;
    private Types types;
    private Resolve rs;
    private Attr attr;
    // migod add
    private Symtab syms;

    public BasicJavaExtractor(Context context) {
        this.context = context;
        nameTable = Name.Table.instance(context);
        types = Types.instance(context);
        rs = Resolve.instance(context);
        attr = Attr.instance(context);
        // migod add
        syms = Symtab.instance(context);
    }

    protected void writeFact(FileWriter writer, String s) {
        try {
            if (writer == null) {
                System.out.println("Sorry, this writer is null.");
            } else {
                writer.write(s + "\n");
                // flush while still developing; nuke when stable
                writer.flush();
            }
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }

    public void apply() {
        PackageDef root = PackageTree.instance(context).getRoot();
        FactExtractorScanner factExtractorScanner = getExtractor();
        root.accept(factExtractorScanner);
        factExtractorScanner.finishUp();
        finish();
        printSummary();
    }

    protected void finish() {

    }

    public void printSummary() {
        System.out.println("QL Fact extraction summary:");
        System.out.println("Success!\n\n"
                + "You can find the output in the files\n\""
                + factTupleFileName + "\" and \""
                + factAttributeFileName + "\".");
    }

    protected void initializeFactBases() {
        writeFact(factTupleWriter, "\nFACT TUPLE :\n");
        writeFact(factAttributeWriter, "\nFACT ATTRIBUTE :\n");
    }

    protected void initializeOutputWriters() throws IOException {
        factTupleWriter = new FileWriter(factTupleFileName);
        factAttributeWriter = new FileWriter(factAttributeFileName);
        debugMesgWriter = new FileWriter(debugMesgFileName);
    }

    protected FactExtractorScanner getExtractor() {
        if (!isRunning) {
            try {
                isRunning = true;
                initializeOutputWriters();
                initializeFactBases();
                scanner = new FactExtractorScanner();
            } catch (IOException e) {
                System.err.println(e.toString());
                System.err.println("Whoops, could NOT create the writers!");
                assert false;
            }
        }
        return scanner;
    }

    protected class FactExtractorScanner extends PackageTree.Scanner {
        protected static final int CLASS_ANNOTATION = 0;
        protected static final int FIELD_ANNOTATION = 1;
        protected static final int METHOD_ANNOTATION = 2;
        protected static final int PARAMETER_ANNOTATION = 3;

        // Constant objects that indicate the kind of the most recently
        // encountered (but not yet finished) interesting entity; these
        // (abstract) values are stored on the
        // pendingInterestingSymbolKindStack.  These ought to be enums,
        // but I didn't feel brave use these new language features.
        final Integer aClass = new Integer(0);
        final Integer aMethod = new Integer(1);
        final Integer aField = new Integer(2);

        final private boolean debugging = true;

        // Need to keep track of these to know who is calling whom, etc.
        // When you get to visitApply, etc, the information is gone.
        private MethodSymbol curTopLevelMethodSymbol;
        private Stack interestingClassStack;
        private Stack pendingInterestingSymbolKindStack;
        private boolean topLevelMethodContainsSynchBlock;
        private HashMap fudgedInitMethodMap;
        private HashSet nonClassTypes;

        // These sets (of Symbols) keep track of the entities that have
        // been "visited" (and thus had their information dumped into the
        // fact bases) versus merely "seen" (i.e., library objects).  At
        // the end of the visiting, we process the entities that have been
        // seen but not visited.
        private HashSet packagesVisited, classesVisited, methodsVisited,
        fieldsVisited;
        private HashSet packagesSeen, classesSeen, methodsSeen, fieldsSeen;

        // Set this to true during the final phase where the library
        // entities are at last processed.  It's no longer clear that this
        // is needed, except for debugging.
        private boolean processingLibraryEntities;


//-- 	// Decomment for "last resort" debugging.
//-- 	public void scan (Tree tree) {
//-- 	    super.scan(tree);
//-- 	    debug("\nKind: " + tree.getClass() + "\n" + tree);
//-- 	}
//-- 	public void scan (Tree tree, Tree nparent) {
//-- 	    super.scan(tree, nparent);
//-- 	    if (tree == null) {
//-- 		debug ("\nKind: " + "Null class\n" + tree);
//-- 	    } else {
//-- 		debug ("\nKind: " + tree.getClass() + "\n" + tree);
//-- 	    }
//-- 	}
        public FactExtractorScanner() {
            this.curTopLevelMethodSymbol = null;
            this.interestingClassStack = new Stack();
            this.pendingInterestingSymbolKindStack = new Stack();
            this.packagesVisited = new HashSet();
            this.classesVisited = new HashSet();
            this.methodsVisited = new HashSet();
            this.fieldsVisited = new HashSet();
            this.packagesSeen = new HashSet();
            this.classesSeen = new HashSet();
            this.methodsSeen = new HashSet();
            this.fieldsSeen = new HashSet();
            this.fudgedInitMethodMap = new HashMap();
            this.nonClassTypes = new HashSet();
            this.processingLibraryEntities = false;
            this.topLevelMethodContainsSynchBlock = false;
            this.initialize();
        }

        protected void initialize() {

        }

        // nonClassTypes stores the names of all basic types, array types
        // and type variables (for generics) as they are encountered in the
        // source to the factbase.  The names get dumped at the end of the
        // extraction.
        private void dumpNonClassTypeInstances() {
            Iterator i = nonClassTypes.iterator();
            while (i.hasNext()) {
                String typeName = (String) i.next();
                writeNonClassTypeFact(typeName);
            }
        }

        protected void writeNonClassTypeFact(String typeName) {
            writeFact(factTupleWriter, "$INSTANCE " + typeName
                    + " jNonClassType");
        }

        // If you find an interesting entity, you should also dump
        // information about *all* of its structural ancestors, i.e., trace
        // up the "owner" link of the symbol until you get to the root
        // package.
        private void visitOwners(Symbol sym) {
            sym = sym.owner;
            // Will iterate until sym==null or when you hit the innermost
            // containing package.
            while (sym instanceof ClassSymbol) {
                if (!classesVisited.contains((ClassSymbol) sym)) {
                    dumpClassInfo((ClassSymbol) sym, defaultPosition);
                    classesVisited.add((ClassSymbol) sym);
                }
                sym = sym.owner;
            }
            // Will iterate until you get to the root package (its owner is
            // null).
            while (sym instanceof PackageSymbol) {
                if (!packagesVisited.contains((PackageSymbol) sym)) {
                    dumpPackageInfo((PackageSymbol) sym);
                    packagesVisited.add((PackageSymbol) sym);
                }
                sym = sym.owner;
            }
            assert sym == null;
        }

        // At the end of the tree walk, we wil likely have a set of fields,
        // methods, classes, and packages that have been "seen" but not
        // visited (as they are defined in other source trees).  Lucky for
        // us, the symbols for these entities are created by the compiler
        // from jar files that were linked in (or else javac would have
        // complained).  We need to dump the facts about these entities to
        // have a semantically complete factbase.
        public void finishUp() {
            processingLibraryEntities = true;
            // Classes / packages might get added to classesVisited /
            // packagesVisited resp. while we are walking through them
            // here!  This means that we can't use a normal iterator for
            // them.  For uniformity, we'll use the same (slightly strange)
            // iteration idiom for all entities.
//-- 	    debug ("I saw, but did not visit, these fields: ");
            while (!fieldsSeen.isEmpty()) {
                VarSymbol v = (VarSymbol) fieldsSeen.iterator().next();
                if (!fieldsVisited.contains(v)) {
//-- 		    debug("  + field " + getVarName(v));
                    dumpFieldInfo(v, defaultPosition);
                    fieldsVisited.add(v);
                }
                fieldsSeen.remove(v);
                visitOwners(v);
            }
//-- 	    debug ("I saw, but did not visit, these methods: ");
            while (!methodsSeen.isEmpty()) {
                MethodSymbol m = (MethodSymbol) methodsSeen.iterator().next();
                if (!methodsVisited.contains(m)) {
//-- 		    debug("  + method " + getMethodName(m));

//                    System.out.println("tree.body.pos=" + tree.body.endpos+ " tree.body.endPos=" + tree.body.endpos);

                    updateClassCounters();
                    dumpMethodInfo(m, defaultPosition);
                    initMethodCounters();
                    methodsVisited.add(m);
                }
                methodsSeen.remove(m);
                visitOwners(m);
            }
//-- 	    debug ("I saw, but did not visit, these classes: ");
            while (!classesSeen.isEmpty()) {
                ClassSymbol c = (ClassSymbol) classesSeen.iterator().next();
                if (!classesVisited.contains(c)) {
//-- 		    debug("  + class " + getClassName(c));
                    dumpClassInfo(c, defaultPosition);
                    classesVisited.add(c);
                }
                classesSeen.remove(c);
                visitOwners(c);
            }
//-- 	    debug ("I saw, but did not visit, these pacakges: ");
            while (!packagesSeen.isEmpty()) {
                PackageSymbol p = (PackageSymbol)
                        packagesSeen.iterator().next();
                if (!packagesVisited.contains(p)) {
//-- 		    debug("  + package " + getPackageName(p));
                    dumpPackageInfo(p);
                    packagesVisited.add(p);
                }
                packagesSeen.remove(p);
                visitOwners(p);
            }
            dumpNonClassTypeInstances();
            // The next call is commented out ... just an experiment, but I
            // don't want to remove the code just yet.
//-- 	    walkSymbolTable();
        }

        // Some experimenting with walking the symbol table.  This works
        // fine, I just have to figure out what options the user might want
        // to use and implement those.  To get at MethodSymbols and
        // VarSymbols (for fields), you will have to walk through the
        // owning class.  This is NOT called is the current implementation
        private void walkSymbolTable() {
            debug("Dumping packages in the symbol table:");
            Map<Name, PackageSymbol> packages = syms.packages;
            for (Iterator i = packages.entrySet().iterator(); i.hasNext();) {
                Map.Entry e = (Map.Entry) i.next();
                PackageSymbol packageSymbol = (PackageSymbol) e.getValue();
                String packageName = getPackageName(packageSymbol);
                debug("    " + packageName);
            }
            // Need to watch out for the predef class, which has no name
            // per se.
            debug("\nDumping classes in the symbol table:");
            Map<Name, ClassSymbol> classes = syms.classes;
            for (Iterator i = classes.entrySet().iterator(); i.hasNext();) {
                Map.Entry e = (Map.Entry) i.next();
                ClassSymbol classSymbol = (ClassSymbol) e.getValue();
                String className = getClassName(classSymbol);
                debug("    " + className);
            }
            debug("\nDumping class that contains *predefined* symbols:");
            ClassSymbol predefClass = syms.predefClass;
            debug("    has name: \"" + getClassName(predefClass) + "\"");
        }

//-- 	final private boolean debugging = false;
        protected void debug(String msg) {
            writeFact(debugMesgWriter, msg);
        }

        // The shortName attribute is for convenience when writing queries.
        protected String getShortName(String s) {
            // Find first "[" if it exists, lop off everything after.
            String ans;
            final int firstBracketPos = s.indexOf('[');
            if (firstBracketPos >= 0) {
                ans = s.substring(0, firstBracketPos);
            } else {
                ans = s;
            }
            // Find last "." if it exists, lop off everything before.
            // Must do this after the "[" lopping, as the params may
            // contain "."s too.
            final int lastDotPos = ans.lastIndexOf('.');
            if (lastDotPos >= 0) {
                ans = ans.substring(lastDotPos + 1);
            }
            return ans;
        }

        // The signarure attribute is for convenience when writing queries.
        protected String getSignature(String s) {
            // Find first "[" if it exists, lop off everything after.
            String ans;
            final int firstBracketPos = s.indexOf('[');
            if (firstBracketPos >= 0) {
                ans = s.substring(0, firstBracketPos);
            } else {
                ans = s;
            }
            // Find last "." if it exists, lop off everything before.
            // Must do this after the "[" lopping, as the params may
            // contain "."s too.
            final int lastDotPos = ans.lastIndexOf('.');
            if (lastDotPos >= 0) {
                ans = s.substring(lastDotPos + 1);
            } else {
                ans = s;
            }

            // Replace [] with ()
            ans = ans.replace('[', '(');
            ans = ans.replace(']', ')');
            return ans;
        }

        protected String getParamSignature(String s) {
            // Find first "[" if it exists, lop off everything after.
            String ans;
            final int firstBracketPos = s.indexOf('[');
            if (firstBracketPos >= 0) {
                ans = s.substring(firstBracketPos);
            } else {
                ans = s;
            }
            // Replace [] with ()
            ans = ans.replace('[', '(');
            ans = ans.replace(']', ')');
            return ans;
        }

        // Make an entity into an acceptable TA identifier.  This means
        // -- changing parens to square brackets
        // -- changing blanks (which occur only in generic types) to "_"s
        private String mangleName(String s) {
            return deblankify(deparenify(s));
        }

        // change ( and ) into [ and ] resp. for TA compliance
        private String deparenify(String s) {
            return (s.replace('(', '[')).replace(')', ']');
        }

        // Some strings come with embedded blanks, which we convert to an
        // underscore (any better ideas?)
        private String deblankify(String s) {
            return s.replace(' ', '_');
        }

        // QL doesn't like backslashes inside strings, so convert all
        // file locations to Unix format with forward slashes instead.
        private String unixifyFileLocation(String fileLocation) {
            return fileLocation.replace('\\', '/');
        }

        // packageName is what you would expect; note that there are two
        // special cases:  the unnamed package and the root package.
        private String getPackageName(PackageSymbol packageSymbol) {
            String packageName = packageSymbol.flatName().toString();
            if (packageName.equals("unnamed package")) {
                packageName = "<unnamed_package>";
            } else if (packageName.equals("")) {
                packageName = "<root_package>";
            }
            return mangleName(packageName);
        }

        // className is the mangled normal full class name.
        private String getClassName(ClassSymbol classSymbol) {
            return classSymbol == null
                    ? "nullClass"  // only here because of a bug in javac
                    : mangleName(classSymbol.fullName().toString());
        }

        // methodName is fullNameOfOwningClass.methodName[paramType1,...]
        // We need to include the param types in the name to disambiguate
        // overloading.
        protected String getMethodName(MethodSymbol methodSymbol) {
            if (methodSymbol == null) {
                return "nullMethod"; // only here because of a bug in javac
            } else {
                final String ownerName =
                        methodSymbol.owner.fullName().toString();
                // Constructors are named "<init>" inside javac; we prefer
                // to use the name of the clas itself.  You can tell a
                // constructor from a method that happens to have the same
                // name as its class by looking at the isConstructor
                // attribute in the factbase.
                final String myShortName =
                        methodSymbol.name.toString().equals("<init>")
                        ? methodSymbol.owner.name.toString()
                        : methodSymbol.name.toString();
                MethodType methodType;
                // Want to get the return type of the method ... but the
                // "Type" might be a ForAll which models the type of
                // generic methods (those that introduce a new type
                // variable not found in the containing class).
                if (methodSymbol.type instanceof MethodType) {
                    methodType = (MethodType) methodSymbol.type;
                } else if (methodSymbol.type instanceof ForAll) {
                    methodType = (MethodType)
                            ((ForAll) methodSymbol.type).qtype;
                } else {
                    // Should never happen ...
                    debug("Error, method " + methodSymbol
                            + " has type that is NOT a MethodType or ForAll"
                            + "\nInstead, \"" + methodSymbol.type.toString()
                            + "\" is a " + methodSymbol.type.getClass());
                    methodType = null;
                    assert false;
                }
                String paramNames = "";
                for (List<Type> paramTypes = methodType.argtypes();
                     !paramTypes.isEmpty(); paramTypes = paramTypes.tail) {
                    final Type paramType = paramTypes.head;
                    final String paramTypeName = getTypeName(paramType);
                    paramNames += paramTypeName + ",";
                }
                // Lop off trailing comma on last param type
                if (paramNames.length() > 0) {
                    paramNames = paramNames.substring(0, paramNames.length() - 1);
                }
                final String methodName = ownerName + "." + myShortName
                        + "[" + paramNames + "]";
                return methodName;
            }
        }

        // fieldName is fullNameOfOwningClass.varName
        private String getVarName(VarSymbol varSymbol) {
            return varSymbol == null
                    ? "nullField" // only here because of a bug in javac
                    : mangleName(getSalsaOwner(varSymbol).fullName().toString()
                    + "." + varSymbol.toString());
        }

        private Symbol getSalsaOwner(VarSymbol varSymbol) {
            if (null == varSymbol) {
                return null;
            }
            ClassSymbol owner;
            if (isInterestingSalsa(varSymbol)
                    && (varSymbol.owner instanceof MethodSymbol)) {
                return ((MethodSymbol) varSymbol.owner).owner;
            } else {
                return varSymbol.owner;
            }
        }

        // See the internals of dumpMethodInfo for how paramNames are
        // generated.

        // Given a type, return its name as a string, using my naming
        // convention.  As a side effect, mark any classes / packages
        // encountered as having been "seen", and generate jNonClassType
        // $INSTANCE entries for array types, basic types, and (generic)
        // type variables.
        protected String getTypeName(Type type) {
            if (type instanceof ClassType) {
                // ClassType includes ErrorType
                final ClassSymbol classSymbol = (ClassSymbol) type.tsym;
                classesSeen.add(classSymbol);
                return getClassName(classSymbol);
            } else if (type instanceof ArrayType) {
                // Simple recursive call!
                final String arrayTypeName =
                        getTypeName(((ArrayType) type).elemtype) + "[]";
                nonClassTypes.add(arrayTypeName);
                return arrayTypeName;
            } else if (type instanceof MethodType) {
                // The "type" of a method is its return type
                return getTypeName(((MethodType) type).restype());
            } else if (type instanceof TypeVar) {
                // A type variable (used for generics)
                // This shouldn't occur any more, actually.
                final Symbol symbol = type.tsym;
                final String varTypeName =
                        mangleName(symbol.externalType(types).toString());
                nonClassTypes.add(varTypeName);
                return varTypeName;
            } else if (type instanceof PackageType) {
                final PackageSymbol packageSymbol = (PackageSymbol) type.tsym;
                packagesSeen.add(packageSymbol);
                return getPackageName(packageSymbol);
            } else if (type instanceof ForAll) {
                // A ForAll models the types of polymorphic methods (ie,
                // methods that introduce new type variables not already
                // introduced by the enclosing class).  We simply call
                // getTypeName on its restype.
                final String forAllName
                        = getTypeName(((ForAll) type).restype());
                return forAllName;
            } else {
                // The only "Type"s are basic types: void, int, boolean etc
                final String basicTypeName = mangleName(type.toString());
                nonClassTypes.add(basicTypeName);
                return basicTypeName;
            }
        }

        // These are the names of special objects that are represented as
        // varSymbols in the parse tree.  They are "boring".




        // "Interesting" relations (method calls, instantiations, etc) may
        // occur inside a field declaration and/or in a class's init clause
        // (static or not).  To model this, we consider that such a class
        // has a method called "<initializer>" or "<staticInitializer>"
        // (depending on if the block / variable declaration is static or
        // not) that is the method caller / instantiator etc.  The logic of
        // the fact extractor requries that we create a MethodSymbol for
        // this pseudo-method; the MethodSymbol does not live in the symbol
        // table, but rather in a (local-to-this-class) map called
        // fudgedInitMethodMap.
        private MethodSymbol getFudgedInitializerMethodSymbol
                (ClassSymbol owner, boolean isStatic) {
            // A class may have many initialization clauses, but we create
            // at most two initialization MethodSymbols per class (one for
            // static, one for non-static initialization).  The first time
            // such a clause is encountered, we create the symbol and put
            // it in the map fudgedInitMethodMap in case any more such
            // clauses are found.
            final String ownerName = getClassName(owner);
            final String fudgedMethodNameString =
                    (isStatic ? "<staticI" : "<i") + "nitializer>[]";
            final String fullPseudoMethodNameString = ownerName + "."
                    + fudgedMethodNameString;
            // Look up the fudgedMethodSymbol and see if we've already
            // created on (stored in fudgedInitMethodMap).
            MethodSymbol fudgedMethodSymbol = (MethodSymbol)
                    fudgedInitMethodMap.get(fullPseudoMethodNameString);
            // If we haven't created the symbol already, we create it now.
            if (fudgedMethodSymbol == null) {
                // Need to lop off the trailing [] of the name.
                // When the name is requested, the [] gets generated each
                // time from the (empty) param list in getMethodName.
                Name fudgedMethodName = Name.fromString(nameTable,
                        fudgedMethodNameString.substring(0,
                                fudgedMethodNameString.length() - 2));
                // Generate facts for this fake method!
                updateClassCounters();
                writeInitializerMethodFact(fullPseudoMethodNameString, ownerName, isStatic);
                initMethodCounters();
// MethodSymbol (long flags, Name name, Type type,
                //               Symbol owner)
                // WARNING: I'm not sure this is the correct thing to do.
                final Type voidType =
                        com.sun.tools.javac.code.Symtab.instance(context).voidType;
                // the last NULL is probably wrong
                final MethodType fudgedMethodType
                        = new MethodType(new List<Type>(), voidType,
                                new List<Type>(), null);
                fudgedMethodSymbol = new MethodSymbol(PRIVATE,
                        fudgedMethodName, fudgedMethodType, owner);
                fudgedInitMethodMap.put(fullPseudoMethodNameString,
                        fudgedMethodSymbol);
                methodsVisited.add(fudgedMethodSymbol);
            }
            return fudgedMethodSymbol;
        }

        protected void writeInitializerMethodFact(final String fullPseudoMethodNameString, final String ownerName, boolean isStatic) {
            writeFact(factTupleWriter, "$INSTANCE "
                    + fullPseudoMethodNameString + " jMethod");
            writeFact(factTupleWriter, "contain " + ownerName + " " +
                    fullPseudoMethodNameString);
            writeFact(factTupleWriter, "isOfType "
                    + fullPseudoMethodNameString + " void");
            writeFact(factAttributeWriter, fullPseudoMethodNameString
                    + " {" + " accessibility=private"
                    + " isStatic=" + (isStatic ? "true" : "false")
                    + " isFinal=false" + " isAbstract=false"
                    + " isConstructor=false" + " numParams=0"
                    + " lineNum=" + defaultPosition
                    + " }");
        }

        // Dump the following facts about package foo:
        //	$INSTANCE foo jPackage
        //	contain foosOwner foo*
        //	foo { shortName }
        // * if foo is not the special "root package".
        // Note that javac creates an unnamed root package that "contain"
        // other "root" packages; e.g., "sym.owner" for java, javax, etc
        // all point to this unnamed (but real) package.  The unnamed root
        // package's "owner" link is null.
        private void dumpPackageInfo(PackageSymbol packageSymbol) {
            packagesVisited.add(packageSymbol);
            final String packageName = getPackageName(packageSymbol);
            final String shortName = getShortName(packageName);
            final PackageSymbol owner = (PackageSymbol) packageSymbol.owner;
            String ownerName = null;

            if (null != owner) {
                ownerName = getPackageName(owner);
            }
            writePackageInfoFact(packageName, shortName, ownerName);
        }

        protected void writePackageInfoFact(String packageName, String shortName, String ownerName) {
            writeFact(factTupleWriter, "$INSTANCE " + packageName + " jPackage");
            writeFact(factAttributeWriter, packageName + " { shortName=" + shortName + " }");
            if (null != ownerName) {
                writeFact(factTupleWriter, "contain " + ownerName + " " + packageName);
            }
        }

        private String getPackageOwnerName(ClassSymbol classSymbol) {
            Symbol owner = classSymbol.owner;
            while (null != owner) {
                if (owner instanceof PackageSymbol) {
                    return getPackageName((PackageSymbol) owner);
                }
                owner = ((ClassSymbol) owner).owner;
            }
            return null;
        }

        // Any visited package is interesting, so just dump its facts.
        public void visitPackageDef(PackageDef tree) {
            final PackageSymbol packageSymbol = tree.sym;
            dumpPackageInfo(packageSymbol);
            super.visitPackageDef(tree);
        }

        // Dump the following facts about class/interface foo:
        //	$INSTANCE foo [jClass|jInterface]
        //	contain bar foo        // bar is containment parent cls/pkg
        //	extends foo bar
        //	implements foo bar     // foo a class, bar an intf
        //	foo { shortName accessibility isAbstract isFinal* isStatic
        //	      isFinal* lineNum }
        // *for classes but not interfaces
        private void dumpClassInfo(ClassSymbol classSymbol, String lineNum) {
            classesVisited.add(classSymbol);
            final String className = getClassName(classSymbol);
            final boolean isInterface =
                    INTERFACE == (classSymbol.flags() & INTERFACE);
            final String sClassKind = isInterface ? "jInterface" : "jClass";
            // Who contains me (structurally)?
            String ownerName = "";
            String classOwnerName = null;
            String packageOwnerName = getPackageOwnerName(classSymbol);

            if (classSymbol.owner instanceof PackageSymbol) {
                packagesSeen.add((PackageSymbol) classSymbol.owner);
                ownerName = getPackageName((PackageSymbol) classSymbol.owner);
            } else if (classSymbol.owner instanceof ClassSymbol) {
                classesSeen.add((ClassSymbol) classSymbol.owner);
                ownerName = getClassName((ClassSymbol) classSymbol.owner);
                classOwnerName = ownerName;
            } else {
                debug("*** visited a class owned by a non-class/package.");
                debug("    class: " + className);
                debug("    owned by : " + classSymbol.owner);
                assert false;
            }
            // Now write out the attributes.
            final String sAccessibility =
                    PUBLIC == (classSymbol.flags() & PUBLIC) ? "public"
                    : PROTECTED == (classSymbol.flags() & PROTECTED) ? "protected"
                    : PRIVATE == (classSymbol.flags() & PRIVATE) ? "private"
                    : "<package>";
            final String sIsStatic = STATIC == (classSymbol.flags() & STATIC)
                    ? "true" : "false";
            final String sIsFinal = FINAL == (classSymbol.flags() & FINAL)
                    ? "true" : "false";
            final String sIsAbstract = ABSTRACT == (classSymbol.flags()
                    & ABSTRACT
                    ) ? "true" : "false";
            final String sIsInner = classSymbol.owner instanceof ClassSymbol
                    ? "true" : "false";
            final String sourceFileName = classSymbol.sourcefile == null
                    ? "<no_source_file>"
                    : unixifyFileLocation(classSymbol.sourcefile.toString());
            final String shortName = getShortName(className);

            writeClassInfoFact(ownerName, classOwnerName, packageOwnerName, className, sClassKind,
                    shortName, sAccessibility, sIsStatic, sIsInner, isInterface, sIsAbstract, sIsFinal,
                    sourceFileName, lineNum);
// If I'm a class, whom do I extend?
            if (!isInterface) {
//-- 		final Type parentType = classSymbol.type.supertype();
                final Type parentType = types.supertype(classSymbol.type);
                if (parentType == null) {
                    // The compiler-only classes that represent basic types:
                    // int boolean char byte float double long short void
//-- 		    debug (className + " has null inheritance parent");
                } else {
                    final ClassSymbol inheritanceParent =
                            (ClassSymbol) parentType.tsym;
                    if (inheritanceParent == null) {
                        // A singularity:  the type of java.lang.Object's
                        // supertype exists, but its type symbol is null.
                        assert className.equals("java.lang.Object");
                    } else {
                        final String inheritanceParentName =
                                getClassName(inheritanceParent);
                        classesSeen.add(inheritanceParent);
                        writeClassExtendsFact(className, inheritanceParentName);
                    }
                }
            }
            // Which interfaces do I extend / implement?
            for (List<Type> interfaces =
                    types.interfaces(classSymbol.type);
                 !interfaces.isEmpty(); interfaces = interfaces.tail) {
                final ClassSymbol intrface = (ClassSymbol) interfaces.head.tsym;
                classesSeen.add(intrface);
                final String intrfaceName = getClassName(intrface);
                writeClassExtendsOrImplementsFact(isInterface, className, intrfaceName);
            }
        }

        protected void writeClassInfoFact(String ownerName,
                                          String classOwnerName, String packageOwnerName,
                                          String className,
                                          String sClassKind, String shortName,
                                          String sAccessibility, String sIsStatic,
                                          String sIsInner, boolean anInterface,
                                          String sIsAbstract, String sIsFinal,
                                          String sourceFileName, String lineNum) {
            writeFact(factTupleWriter, "contain " + ownerName + " " + className);
            writeFact(factTupleWriter, "$INSTANCE " + className + " "
                    + sClassKind);
            writeFact(factAttributeWriter, className + " {"
                    + " shortName=" + shortName
                    + " accessibility=" + sAccessibility
                    + " isStatic=" + sIsStatic
                    + " isInner=" + sIsInner
                    + ((anInterface) ? ""
                    : (" isAbstract=" + sIsAbstract + " isFinal=" + sIsFinal)
                    )
                    + " sourceFile=" + sourceFileName
                    + " lineNum=" + lineNum
                    + " }");
        }

        protected void writeClassExtendsOrImplementsFact(final boolean anInterface, final String className, final String intrfaceName) {
            writeFact(factTupleWriter,
                    (anInterface ? "extends " : "implements ")
                    + className + " " + intrfaceName);
        }

        protected void writeClassExtendsFact(final String className, final String inheritanceParentName) {
            writeFact(factTupleWriter, "extends "
                    + className + " " + inheritanceParentName);
        }

        // Only dump information about interesting (non-local/anonymous)
        // classes.
        public void visitClassDef(ClassDef tree) {
            final ClassSymbol classSymbol = tree.sym;
            final boolean imInteresting = isInteresting(classSymbol);

            if (imInteresting) {
                final int line = Position.line(tree.pos);
                final int column = Position.column(tree.pos);
                final String lineNum = line + (dumpColumnPosition ? "." + column : "");
// 		System.err.println("Entering interesting class: "+getClassName(classSymbol));
                pendingInterestingSymbolKindStack.push(aClass);
                interestingClassStack.push(classSymbol);

                // TODO: Collect class lines info here
//                dumpClassInfo(classSymbol, lineNum);  // Scary orig
                initClassCounters();
            } else {
// 		System.err.println ("Entering boring class: " + getClassName(classSymbol));
            }

            super.visitClassDef(tree);

            if (imInteresting) {      // Scary new
                dumpClassInfo(classSymbol, Position.line(tree.pos)
                        + (dumpColumnPosition ? "." + Position.column(tree.pos) : ""));
                initClassCounters();
            }

            // [[[[[[[[[[[[[[[[[[ Yury Kamen: add annotations
            if (imInteresting && null != tree.mods && null !=
                    tree.mods.annotations && tree.mods.annotations.length() > 0) {
                final String className = getClassName(classSymbol);

                int count = 1;
                for (List<Annotation> annotations = tree.mods.annotations; !annotations.isEmpty(); annotations =
                        annotations.tail) {
                    addAnnotation(annotations.head, className, count, CLASS_ANNOTATION);
                    count += 1;
                }
            }
            // ]]]]]]]]]]]]]]]]]]] Yury Kamen: add annotations
            if (imInteresting) {
                pendingInterestingSymbolKindStack.pop();
                interestingClassStack.pop();
//-- 		debug ("Exiting interesting class: " + classSymbol);
            } else {
//-- 		debug ("Exiting boring class: " + classSymbol);
            }
        }

        // This should be done by javac in Symbol, but it isn't in the
        // current 1.5 beta 2 due to a faulty guard ("params != null"
        // should be "params == null").  It's fixed in the source tree now,
        // but not in the released version.
        private List<VarSymbol> getParamList(MethodSymbol methodSymbol) {
            ListBuffer<VarSymbol> buf = new ListBuffer<VarSymbol>();
            int i = 0;
            for (Type t : getMethodTypeFromSymbol(methodSymbol).argtypes()) {
                buf.append(new VarSymbol(PARAMETER,
                        methodSymbol.name.table.fromString("arg" + i),
                        t, methodSymbol));
                // This increment is broken in the current version of javac.
                i++;
            }
            return buf.toList();
        }

        private MethodType getMethodTypeFromSymbol(MethodSymbol methodSymbol) {
            // Want to get the return type of the method ... but the "Type"
            // might be a ForAll which models the type of polymorphic
            // methods (those that introduce a new type variable not found
            // in the containing class).
            if (methodSymbol.type instanceof MethodType) {
                return (MethodType) methodSymbol.type;
            } else if (methodSymbol.type instanceof ForAll) {
                // FIXME: Do we want to set MethodType to this?
                //     ((ForAll) methodSymbol.type).restype()
                return (MethodType) ((ForAll) methodSymbol.type).asMethodType();
            } else {
                debug("Error, method " + methodSymbol
                        + " has type that is NOT a MethodType or ForAll"
                        + "\nInstead, it is a " +
                        methodSymbol.type.getClass());
                assert false;
                return null;
            }
        }

        // Dump the following facts about method foo:
        //	contain foosClass foo
        //	$INSTANCE foo jMethod
        //	foo { shortName accessibility isStatic isFinal isAbstract
        //	    numParams isConstructor isSynchronized
        //	    containsSynchBlock lineNum }
        //  For each param flurble of foo:
        //	hasParam foo flurble
        //	$INSTANCE flurble jParameter
        //	isOfType foo foosTypeName
        //	flurble { shortName index }
        private void dumpMethodInfo(MethodSymbol methodSymbol, String lineNum) {
            methodsVisited.add(methodSymbol);
            final ClassSymbol owner = (ClassSymbol) methodSymbol.owner;
            classesSeen.add(owner);
            final String className = getClassName((ClassSymbol) owner);
            final String methodName = getMethodName(methodSymbol);
            final boolean isConstructor = methodSymbol.isConstructor();
            final String returnTypeName = getTypeName(methodSymbol.type);
            // Now do method attributes ...
            final String sAccessibility =
                    PUBLIC == (methodSymbol.flags() & PUBLIC)
                    ? "public" :
                    PROTECTED == (methodSymbol.flags() & PROTECTED)
                    ? "protected" :
                    PRIVATE == (methodSymbol.flags() & PRIVATE)
                    ? "private" : "<package>";
            final String sIsStatic = STATIC == (methodSymbol.flags() & STATIC)
                    ? "true" : "false";
            final String sIsFinal = FINAL == (methodSymbol.flags() & FINAL)
                    ? "true" : "false";
            final String sIsAbstract =
                    ABSTRACT == (methodSymbol.flags() & ABSTRACT)
                    ? "true" : "false";
            final String sIsSynchronized =
                    SYNCHRONIZED == (methodSymbol.flags() & SYNCHRONIZED)
                    ? "true" : "false";
            final String sContainsSynchBlock =
                    topLevelMethodContainsSynchBlock ? "true" : "false";
            MethodType methodType = getMethodTypeFromSymbol(methodSymbol);
            final String sIsConstructor = isConstructor ? "true" : "false";
            final String shortName = getShortName(methodName);
            writeMethodInfoFact(methodName, className, returnTypeName, shortName, sAccessibility, sIsStatic, sIsFinal, sIsAbstract, sIsConstructor, sIsSynchronized, sContainsSynchBlock, methodType, lineNum);
// List my params.  The params need an indentifier of some kind
            // to be usable inside QL/grok.  We do not use the param names
            // that appear in the source code, as they do not appear in the
            // symbol table; instead, we use generated names "<param1>",
            // "<param2>", ...
            int paramIndex = 1;

            // Yury Kamen [[[[[[[[[[[[[[[
            List<VarSymbol> params = methodSymbol.params();
            // Fixes by migod
            // params == null only in the case that we are using a buggy
            // version of javac where the Symbol.params() method fails to
            // complete out the params list.  If there are no params, the
            // var params should be a List of no elements, not null.  This
            // is appears to be an issue only when processing library
            // symbols.  This solution should work once the bug is fixed
            // too; it probably should be left like this in case a user is
            // using one of the betas with the bug.
            if (params == null) {
                params = getParamList(methodSymbol);
            }
            assert (params.length() == methodType.argtypes().length());

            // Yury Kamen ]]]]]]]]]]]]]]]
//            for (List<Type> paramTypes = methodType.argtypes(); !paramTypes.isEmpty(); paramTypes = paramTypes.tail) {
            for (List<VarSymbol> parameters = params; !parameters.isEmpty(); parameters = parameters.tail) {
                final VarSymbol parameter = parameters.head;
//                final String shortParamName = "<param" + paramIndex + ">";
                final String shortParamName =  parameter.name.toString();
//                final String paramName = methodName + "." + "<param index=" + paramIndex + " name=" + shortParamName + ">";
                final String paramName = methodName + "." + paramIndex + "." + shortParamName;
                final String paramTypeName = getTypeName(parameter.type);

                writeParameterInfoFact(paramName, methodName, paramTypeName, shortParamName, paramIndex);
//-- 		if (!processingLibraryEntities) {
                // Yury Kamen [[[[[[[[[[[[[[[ add parameter attributes
                final VarSymbol paramSymbol = (VarSymbol) params.head;
                if (paramSymbol.attributes().length() > 0) {
                    for (List<Attribute.Compound> attributes =
                            paramSymbol.attributes(); !attributes.isEmpty();
                         attributes = attributes.tail) {
                        Attribute.Compound attribute = attributes.head;
                        addAttribute(attribute, paramName, paramIndex);
                    }
                }
                // Yury Kamen ]]]]]]]]]]]]]]] add parameter attributes
                params = params.tail; // Yury Kamen: move to the next param
//-- 		}
                paramIndex++;
            }
        }

        protected void writeParameterInfoFact(final String paramName, final String methodName, final String paramTypeName, final String shortParamName, int paramIndex) {
            writeFact(factTupleWriter, "$INSTANCE " + paramName + " jParameter");
            writeFact(factTupleWriter, "hasParam " + methodName + " " + paramName);
            writeFact(factTupleWriter, "isOfType " + paramName + " " + paramTypeName);
            writeFact(factAttributeWriter, paramName + " {"
                    + " shortName=" + shortParamName
                    + " index=" + paramIndex
                    + " }");
        }

        protected void writeMethodInfoFact(final String methodName, final String className,
                                           final String returnTypeName, final String shortName,
                                           final String sAccessibility, final String sIsStatic,
                                           final String sIsFinal, final String sIsAbstract,
                                           final String sIsConstructor, final String sIsSynchronized,
                                           final String sContainsSynchBlock, Type.MethodType methodType,
                                           String lineNum) {
            writeFact(factTupleWriter, "$INSTANCE " + methodName + " jMethod");
            writeFact(factTupleWriter, "contain " + className + " " + methodName);
            writeFact(factTupleWriter, "isOfType " + methodName + " " + returnTypeName);
            writeFact(factAttributeWriter, methodName + " {"
                    + " shortName=" + shortName
                    + " accessibility=" + sAccessibility
                    + " isStatic=" + sIsStatic
                    + " isFinal=" + sIsFinal
                    + " isAbstract=" + sIsAbstract
                    + " isConstructor=" + sIsConstructor
                    + " isSynchronized=" + sIsSynchronized
                    + " containsSynchBlock=" + sContainsSynchBlock
                    + " numParams=" + methodType.argtypes().length()
                    + " lineNum=" + lineNum
                    + " }");
        }

        public void scan(Tree tree) {
            super.scan(tree);    //To change body of overridden methods use File | Settings | File Templates.
        }

        // Note that all "interesting" relationships that occur within a
        // top-level method are "lifted" to make the method become the
        // entity of interest, and thus we don't care about attributes of
        // non-top-level methods ...
        public void visitMethodDef(MethodDef tree) {
            final MethodSymbol methodSymbol = tree.sym;
            final boolean imInteresting = isInteresting(methodSymbol);
            if (imInteresting) {
//-- 		debug ("Entering interesting method: "
//-- 			+ getMethodName(methodSymbol));
                pendingInterestingSymbolKindStack.push(aMethod);
                curTopLevelMethodSymbol = methodSymbol;
            } else {
                // Strange subcase: a boring method is declared as
                // synchronized ... we wish the current top level method to
                // register true for the attribute containsSynchBlock.
                topLevelMethodContainsSynchBlock =
                        SYNCHRONIZED == (methodSymbol.flags() & SYNCHRONIZED);
//-- 		debug ("Entering boring method: "+ getMethodName(methodSymbol));
            }
            // Need to process subparts first, then dump methodInfo after
            // visit is completed.
            super.visitMethodDef(tree);
            if (imInteresting) {
                final int line = Position.line(tree.pos);
                final int column = Position.column(tree.pos);
                final String lineNum = line + (dumpColumnPosition ? "." + column : "");


                if (null != tree.body && tree.body.endpos >= tree.body.pos) {
                    methodCounters.numLines += Position.line(tree.body.endpos) - Position.line(tree.body.pos);
//                    System.out.println(
//                             "Position.line(tree.pos)" + Position.line(tree.pos)
//                            + "Position.line(tree.body.pos)" + Position.line(tree.body.endpos)
//                            + " Position.line(tree.body.endPos)=" + Position.line(tree.body.endpos)
//                    );
                }

                updateClassCounters();
                dumpMethodInfo(methodSymbol, lineNum);
                initMethodCounters();

                // [[[[[[[[[[[[[[[[[[ Yury Kamen: add annotations
                if (null != tree.mods && null != tree.mods.annotations
                        && tree.mods.annotations.length() > 0) {
                    final String methodName = getMethodName(methodSymbol);
                    int count = 1;
                    for (List<Annotation> annotations = tree.mods.annotations;
                         !annotations.isEmpty();
                         annotations = annotations.tail) {
                        addAnnotation(annotations.head, methodName, count, METHOD_ANNOTATION);
                        count += 1;
                    }
                }
                // ]]]]]]]]]]]]]]]]]]] Yury Kamen: add annotations
                pendingInterestingSymbolKindStack.pop();
                //  Reset to null ONLY if it's a top level method.
                curTopLevelMethodSymbol = null;
                topLevelMethodContainsSynchBlock = false;
            } else {
//-- 		debug ("Leaving boring method: " + methodSymbol);
            }
        }

        // Dump the following facts about field foo:
        //	contain foosClass foo
        //	$INSTANCE foo jField
        //	foo { shortName accessibility isStatic isFinal lineNum }
        private void dumpFieldInfo(VarSymbol varSymbol, String lineNum) {
            fieldsVisited.add(varSymbol);
            final ClassSymbol owner = (ClassSymbol) getSalsaOwner(varSymbol);
            classesSeen.add(owner);
            final String className = getClassName((ClassSymbol) owner);
            final String varName = getVarName(varSymbol);
            final String typeName = getTypeName(varSymbol.type);
            final Type varType = varSymbol.type;
            final String sAccessibility =
                    PUBLIC == (varSymbol.flags() & PUBLIC) ? "public" :
                    PROTECTED == (varSymbol.flags() & PROTECTED) ? "protected" :
                    PRIVATE == (varSymbol.flags() & PRIVATE) ? "private" :
                    "<package>";
            final String sIsStatic = STATIC == (varSymbol.flags() & STATIC) ? "true" : "false";
            final String sIsFinal = FINAL == (varSymbol.flags() & FINAL) ? "true" : "false";
            final String shortName = getShortName(varName);

            writeFieldInfoFact(varName, className, typeName, shortName, sAccessibility, sIsStatic, sIsFinal, lineNum);
        }

        protected void writeFieldInfoFact(final String varName, final String className, final String typeName, final String shortName, final String sAccessibility, final String sIsStatic, final String sIsFinal, String lineNum) {
            writeFact(factTupleWriter, "$INSTANCE " + varName + " jField");
            writeFact(factTupleWriter, "contain " + className + " " + varName);
            writeFact(factTupleWriter, "isOfType " + varName + " " + typeName);
            writeFact(factAttributeWriter, varName + " {"
                    + " shortName=" + shortName
                    + " accessibility=" + sAccessibility
                    + " isStatic=" + sIsStatic
                    + " isFinal=" + sIsFinal
                    + " lineNum=" + lineNum
                    + " }");
        }

        // This method visits ALL varDefs: fields, params, locals, but we
        // are only intersted in fields of interesting classes (the call to
        // isInteresting checks if the owner is a class).
        public void visitVarDef(VarDef tree) {
            final VarSymbol varSymbol = tree.sym;
            final boolean imInteresting = isInteresting(varSymbol);
            boolean createdANewInitialzerMethodSymbol = false;
            if (imInteresting) {
//-- 		debug ("Entering interesting field: " +
//-- 			getVarName(varSymbol));
                pendingInterestingSymbolKindStack.push(aField);
                final int line = Position.line(tree.pos);
                final int column = Position.column(tree.pos);
                final String lineNum = line
                        + (dumpColumnPosition ? "." + column : "");
                dumpFieldInfo(varSymbol, lineNum);
                // If an interesting VarDef has an initialization clause,
                // we need to create a fake MethodSymbol to associate with
                // any embedded relations we find and make this the
                // curTopLevelMethodSymbol.
                // [If a non-interesting VarDef has an initialization
                // clause, then there is already a curTopLevelMethodSymbol
                // defined and we should use that for the embedded
                // relations.]
                final boolean hasInitClause = (tree.init != null);
                if (hasInitClause) {
                    final boolean isStatic
                            = STATIC == (varSymbol.flags() & STATIC);
                    curTopLevelMethodSymbol = getFudgedInitializerMethodSymbol
                            ((ClassSymbol) varSymbol.owner, isStatic);
                    methodsSeen.add(curTopLevelMethodSymbol);
//-- 		    debug ("visitVarDef: " + tree);
//-- 		    debug ("    adding initalizer method " +
//-- 			    getMethodName(curTopLevelMethodSymbol));
                    createdANewInitialzerMethodSymbol = true;
                }
            } else {
//-- 		debug ("Entering boring field: " + getVarName(varSymbol));
            }
            super.visitVarDef(tree);
            // [[[[[[[[[[[[[[[[[[ Yury Kamen: add annotations
            if (imInteresting && null != tree.mods
                    && null != tree.mods.annotations
                    && tree.mods.annotations.length() > 0) {
                final String varName = getVarName(varSymbol);
                int count = 1;
                for (List<Annotation> annotations = tree.mods.annotations;
                     !annotations.isEmpty();
                     annotations = annotations.tail) {
                    addAnnotation(annotations.head, varName, count, FIELD_ANNOTATION);
                    count += 1;
                }
            }
            // ]]]]]]]]]]]]]]]]]]] Yury Kamen: add annotations
            if (imInteresting) {
//-- 		debug ("Leaving interesting field: " + varSymbol);
                pendingInterestingSymbolKindStack.pop();
                if (createdANewInitialzerMethodSymbol) {
                    curTopLevelMethodSymbol = null;
                }
            } else {
//-- 		debug ("Leaving boring field: " + varSymbol);
            }
        }

        // Need to catch relations inside initialization blocks of
        // interesting classes.
        public void visitBlock(Block tree) {
            boolean createdANewInitialzerMethodSymbol = false;
            if (pendingInterestingSymbolKindStack.peek() == aClass) {
                // Create a fake method symbol to associate with any
                // calls/refs inside an initialization block ... but only
                // do this if there isn't already a curTopLevelMethodSymbol
                // defined (if there is, we are in a sub-block of an
                // initializer).
                if (curTopLevelMethodSymbol == null) {
                    final ClassSymbol curTopLevelClassSymbol =
                            (ClassSymbol) interestingClassStack.peek();
                    classesSeen.add(curTopLevelClassSymbol);
                    final String className = getClassName
                            (curTopLevelClassSymbol);
                    createdANewInitialzerMethodSymbol = true;
                    final boolean isStatic
                            = (TreeInfo.flags(tree) & STATIC) != 0;
                    curTopLevelMethodSymbol = getFudgedInitializerMethodSymbol
                            (curTopLevelClassSymbol, isStatic);
                    methodsSeen.add(curTopLevelMethodSymbol);
//-- 		    debug ("visitBlock: " + tree);
//-- 		    debug ("    adding initalizer method " +
//-- 			    getMethodName(curTopLevelMethodSymbol));
                }
            }
            super.visitBlock(tree);
            // reset curTopLevelMethodSymbol to null if we created a fake
            // method symbol.
            if (createdANewInitialzerMethodSymbol) {
                curTopLevelMethodSymbol = null;
//-- 		debug ("Leaving interesting initalizer: "
//-- 			+ curTopLevelMethodSymbol);
            }
        }

        // Dump the following facts about method foo calling method bar
        //	calls foo bar
        //	(calls foo bar) { lineNum }
        public void visitApply(Apply tree) {
            // If the source code is incomplete/inconsistent, a missing
            // method may be inferred to be a ClassSymbol.  (This happens
            // if you try to process src.zip from jdk 1.4.2_03!)
            // You can either ignore the problem (since the callee method
            // doesn't "exist", this isn't a bad option) or assert false
            // and die with dignity.
            if (TreeInfo.symbol(tree.meth) instanceof ClassSymbol) {
                debug("WHOOPS tree.meth should be a method, not a class: "
                        + TreeInfo.symbol(tree.meth));
                debug("    Here is the tree: " + tree);
                // Just skip it for now ...
//-- 		assert false;
            } else {
                final MethodSymbol calleeSym = (MethodSymbol) TreeInfo.symbol(tree.meth);
                if (isInteresting(calleeSym)) {
                    final String calleeName = getMethodName(calleeSym);
                    final String callerName = getMethodName(curTopLevelMethodSymbol);
                    final int line = Position.line(tree.pos);
                    final int column = Position.column(tree.pos);
//-- 		    debug ("visitApply: " + tree);
//-- 		    debug ("    adding callee method " + calleeName);

                    writeMethodCallsFact(callerName, calleeName, line, column);
                    writeMethodCallFact(curTopLevelMethodSymbol, calleeSym);
                    methodsSeen.add(calleeSym);
                }
            }
            super.visitApply(tree);
        }

        protected void writeMethodCallFact(MethodSymbol callerSym, MethodSymbol calleeSym) {
            //To change body of created methods use File | Settings | File Templates.
        }

        protected void writeMethodCallsFact(final String callerName, final String calleeName, final int line, final int column) {
            final String factTuple = "calls " + callerName + " " + calleeName;
            writeFact(factTupleWriter, factTuple);
            writeFact(factAttributeWriter, "(" + factTuple + ") {"
                    + " lineNum="
                    + line + (dumpColumnPosition ? "." + column : "")
                    + " }");
        }

        // Dump the following fact about method foo instantiating class bar:
        //	instantiates foo bar
        //	(instantiates foo bar) { lineNum }
        public void visitNewClass(NewClass tree) {
            final ClassSymbol instantiateeClassSymbol = (ClassSymbol) tree.constructor.owner;
            if (isInteresting(instantiateeClassSymbol)) {
                final String instantiatorMethodName = getMethodName(curTopLevelMethodSymbol);
                final String instantiateeClassName = getClassName(instantiateeClassSymbol);
//-- 		debug ("visitNewClass: " + tree);
//-- 		debug ("    adding instantiatee class "+instantiateeClassName);
                classesSeen.add(instantiateeClassSymbol);
                final int line = Position.line(tree.pos);
                final int column = Position.column(tree.pos);
                writeMethodInstantiatesClassInstanceFact(instantiatorMethodName, instantiateeClassName, line, column);
            }
            super.visitNewClass(tree);
        }

        protected void writeMethodInstantiatesClassInstanceFact(final String instantiatorMethodName, final String instantiateeClassName, final int line, final int column) {
            final String factTuple = "instantiates " + instantiatorMethodName + " " + instantiateeClassName;
            writeFact(factTupleWriter, factTuple);
            writeFact(factAttributeWriter, "(" + factTuple + ") { lineNum=" + line
                    + (dumpColumnPosition ? "." + column : "")
                    + " }");
        }

        // Dump the following fact about method foo using field bar
        //	uses foo bar
        //	(uses foo bar) { lineNum }
        public void visitSelect(Select tree) {
            visitIdentOrSelect(tree);
            super.visitSelect(tree);
        }

        // Dump the following fact about method foo using field bar
        //	uses foo bar
        //	(uses foo bar) { lineNum }
        public void visitIdent(Ident tree) {
            visitIdentOrSelect(tree);
            super.visitIdent(tree);
        }

        // Dump the following fact about method foo using field bar uses foo bar (uses foo bar) { lineNum }
        private void visitIdentOrSelect(Tree tree) {
            // We only care about Idents/Selects that are fields, everything
            // else is caught elsewhere or isn't worth worrying about.
            final Symbol sym = TreeInfo.symbol(tree);
            if (sym instanceof VarSymbol && sym.owner instanceof ClassSymbol) {
                final VarSymbol varSymbol = (VarSymbol) sym;
                if (isInteresting(varSymbol)) {
                    final String userName =
                            getMethodName(curTopLevelMethodSymbol);
                    final String useeOwnerName = getClassName
                            ((ClassSymbol) varSymbol.owner);
                    final String useeName = getVarName(varSymbol);
//-- 		    debug ("visitIdentOrSelect: " + tree);
//-- 		    debug ("    adding used field " + useeName);
                    fieldsSeen.add(varSymbol);
                    final int line = Position.line(tree.pos);
                    final int column = Position.column(tree.pos);
                    writeMethodUsesFieldFact(userName, useeName, line, column);
                }
            }
        }

        protected void writeMethodUsesFieldFact(final String userName, final String useeName, final int line, final int column) {
            final String factTuple = "uses " + userName + " " + useeName;
            writeFact(factTupleWriter, factTuple);
            final String lineNum = line + (dumpColumnPosition ? "." + column : "");
            writeFact(factAttributeWriter, "(" + factTuple + ") { lineNum=" + lineNum + " }");
        }

        // Dump the following fact about method foo catching (throwable) class bar:
        //	catches foo bar (catches foo bar) { lineNum }
        public void visitCatch(Catch tree) {
            // It seems that tree.param.vartype (statically a Tree) is an
            // Ident or a Select.
            final Tree exceptionTree = tree.param.vartype;
            final ClassSymbol exceptionSymbol =
                    (ClassSymbol) TreeInfo.symbol(exceptionTree);
            if (isInteresting(exceptionSymbol)) {
                final String exceptionName = getClassName(exceptionSymbol);
                final String catcherMethodName = getMethodName(curTopLevelMethodSymbol);
                final int line = Position.line(tree.pos);
                final int column = Position.column(tree.pos);
                classesSeen.add(exceptionSymbol);
                writeMethodCatchesExceptionFact(catcherMethodName, exceptionName, line, column);
            }
            super.visitCatch(tree);
        }

        protected void writeMethodCatchesExceptionFact(final String catcherMethodName, final String exceptionName, final int line, final int column) {
            final String factTuple = "catches " + catcherMethodName + " " + exceptionName;
            writeFact(factTupleWriter, factTuple);
            writeFact(factAttributeWriter, "(" + factTuple + ") {"
                    + " lineNum=" + line
                    + (dumpColumnPosition ? "." + column : "")
                    + " }");
        }

        // Dump the following fact about method foo throwing (throwable)
        // class bar: throws foo bar (throws foo bar) { lineNum }
        // Why not do this via the walking through the method's throws
        // specification in visitMethodDef?  Because we want to model the
        // throwing of both checked and unchecked exceptions; the throws
        // specification need list only checked exceptions that are thrown.
        public void visitThrow(Throw tree) {
            final ClassSymbol throweeSymbol = (ClassSymbol) tree.expr.type.tsym;
            if (isInteresting(throweeSymbol)) {
                final String throweeName = getClassName(throweeSymbol);
                final int line = Position.line(tree.pos);
                final int column = Position.column(tree.pos);
                final String throwerMethodName = getMethodName(curTopLevelMethodSymbol);
                classesSeen.add(throweeSymbol);
                writeClassThrowsExceptionFact(throwerMethodName, throweeName, line, column);
            }
            super.visitThrow(tree);
        }

        protected void writeClassThrowsExceptionFact(final String throwerMethodName, final String exceptionName, final int line, final int column) {
            final String factTuple = "throws " + throwerMethodName + " " + exceptionName;
            writeFact(factTupleWriter, factTuple);
            writeFact(factAttributeWriter, "(" + factTuple + ") {"
                    + " lineNum=" + line
                    + (dumpColumnPosition ? "." + column : "")
                    + " }");
        }

        public void visitSynchronized(Synchronized tree) {
//-- 	    final String owningMethodName = curTopLevelMethodSymbol == null
//-- 		? "(null curTopLevelMethodSymbol)" 
//-- 		: getMethodName (curTopLevelMethodSymbol);
//-- 	    debug ("\nvisitSynchronized: " + owningMethodName);
//-- 	    debug("       lock: " + tree.lock);
//-- 	    debug("    of type: " + tree.lock.getClass().toString());
//-- 	    debug("       body: " + tree.body);
//-- 	    debug("    of type: " + tree.body.getClass().toString());
            topLevelMethodContainsSynchBlock = true;
            super.visitSynchronized(tree);
        }

        /* Yury Kamen: processing Java annotations */
        private String quoteString(String s) {
            if (s.equals("\"\"")) {
                return s;
            }
            if (s.length() >= 3 && s.charAt(0) == '"'
                    && s.charAt(s.length() - 1) == '"') {
                s = s.substring(1, s.length() - 1);
            }
            return '"' + Utils.quoteJavaString(s) + '"';
        }

        private void addAnnotation(Tree.Annotation ann, final String ownerName, int count, int kind) {
            final String annTypeName = getTypeName(ann.annotationType.type);
            final String annName = ownerName + "$annotation$" + count;

            Set<Symbol> symbolSet = new HashSet<Symbol>();
            for (Entry elems = ann.type.tsym.members().elems;
                 elems != null; elems = elems.sibling) {
                symbolSet.add(elems.sym);
            }
            writeAnnotationInstanceFact(annName, ownerName, annTypeName, kind);
            int argcount = 1;
            String argName;
            for (List<Tree> args = ann.args; !args.isEmpty();
                 args = args.tail) {
                Tree arg = args.head;
                argName = annName + "$arg$" + argcount;
                String name = annTypeName;

                writeAnnotationArgumentInstanceFact(argName, annName, name);

                if (arg instanceof Tree.Assign) {
                    Tree.Assign assign = (Tree.Assign) arg;
                    name = "" + assign.lhs;
                    if ((assign.lhs instanceof Tree.Ident) && (((Tree.Ident) assign.lhs).sym instanceof MethodSymbol)) {
                        name = getMethodName((MethodSymbol) ((Tree.Ident) assign.lhs).sym);
                        symbolSet.remove(((Tree.Ident) assign.lhs).sym);
                    }
                    String shortName = getShortName(name);
                    String stringValue = "" + assign.rhs;
                    writeAnnotationArgumentShortNameAndStringValueFact(argName, shortName, stringValue);
                }
                argcount += 1;
            }

            // Process default arguments
            for (Iterator<Symbol> iterator = symbolSet.iterator();
                 iterator.hasNext();) {
                Symbol sym = iterator.next();
                if (sym instanceof MethodSymbol) {
                    MethodSymbol methodSym = (MethodSymbol) sym;
                    if (null != methodSym.defaultValue) {
                        argName = annName + "$arg$" + argcount;
                        String name = getMethodName(methodSym);

                        String shortName = getShortName(name);
                        String stringValue = "" + methodSym.defaultValue;

                        writeAnnotationArgumentInstanceFact(argName, annName, name);
                        writeAnnotationArgumentShortNameAndStringValueFact(argName, shortName, stringValue);
                        argcount += 1;
                    }
                }
            }
            String shortName = getShortName(annName);
            writeAnnotationShortNameAndNumArgumentsFact(annName, shortName, argcount);
        }

        private void addAttribute(Attribute.Compound attribute, final String ownerName, int count) {
            final String annTypeName = getTypeName(attribute.type);
            final String annName = ownerName + "$annotation$" + count;

            Set<Symbol> symbolSet = new HashSet<Symbol>();
            for (Entry elems = attribute.type.tsym.members().elems;
                 elems != null; elems = elems.sibling) {
                symbolSet.add(elems.sym);
            }

            writeAnnotationInstanceFact(annName, ownerName, annTypeName, PARAMETER_ANNOTATION);

            int argcount = 1;
            String argName;
            for (List<Pair<MethodSymbol, Attribute>> args = attribute.values;
                 !args.isEmpty(); args = args.tail) {
                Pair<MethodSymbol, Attribute> arg = args.head;
                String name = getMethodName(arg.fst);
                argName = annName + "$arg$" + argcount;
                writeAnnotationArgumentInstanceFact(argName, annName, name);
                writeAnnotationArgumentShortNameAndStringValueFact(argName, getShortName(name), "" + arg.snd);
                symbolSet.remove(arg.fst);
                argcount += 1;
            }
            // Add default annotation arguments
            for (Iterator<Symbol> iterator = symbolSet.iterator();
                 iterator.hasNext();) {
                Symbol sym = iterator.next();
                if (sym instanceof MethodSymbol) {
                    MethodSymbol methodSym = (MethodSymbol) sym;
                    if (null != methodSym.defaultValue) {
                        argName = annName + "$arg$" + argcount;
                        String name = getMethodName(methodSym);
                        writeAnnotationArgumentInstanceFact(argName, annName, name);
                        writeAnnotationArgumentShortNameAndStringValueFact(argName, getShortName(name), ""
                                + methodSym.defaultValue);
                        argcount += 1;
                    }
                }
            }
            writeAnnotationShortNameAndNumArgumentsFact(annName, getShortName(annName), argcount);
        }

        protected void writeAnnotationShortNameAndNumArgumentsFact(final String annName, String shortName, int argcount) {
            writeFact(factAttributeWriter, annName + " {"
                    + " shortName=" + shortName
                    + " numArgs=" + (argcount - 1)
                    + " }");
        }

        protected void writeAnnotationArgumentShortNameAndStringValueFact(String argName, String shortName, String stringValue) {
            writeFact(factAttributeWriter, argName + " {"
                    + " shortName=" + shortName
                    + " stringValue=" + quoteString(stringValue)
                    + " }");
        }

        protected void writeAnnotationArgumentInstanceFact(String argName, final String annName, String type) {
            writeFact(factTupleWriter, "$INSTANCE " + argName + " jAnnotationArgument");
            writeFact(factTupleWriter, "hasAnnotationArgument " + annName + " " + argName);
            writeFact(factTupleWriter, "isOfType " + argName + " " + type);
        }

        protected void writeAnnotationInstanceFact(final String annName, final String ownerName, final String annTypeName, int kind) {
            writeFact(factTupleWriter, "$INSTANCE " + annName + " jAnnotation");
            writeFact(factTupleWriter, "hasAnnotations " + ownerName + " " + annName);
            writeFact(factTupleWriter, "isOfType " + annName + " " + annTypeName);
        }
    }
}
