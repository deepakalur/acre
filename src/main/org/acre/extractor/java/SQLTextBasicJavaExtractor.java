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

import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Options;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * This class implements fact extraction into a list of SQL statements
 * according to JModel database schema
 *
 * @author Yury Kamen
 */
public class SQLTextBasicJavaExtractor extends BasicJavaExtractor {
    // Number of iterations to build transitive relationships
    public int numberOfIterations = 50;
    private static final String COMMA = ", ";

    //  SQL keywords
    private static final String UPDATE = "UPDATE ";
    private static final String REPLACE = "REPLACE INTO ";


    // Table names for Java SQL tables
    private static final String JPackage = "JPackage";
    private static final String JField = "JField";
    private static final String JFactDatabase = "JFactDatabase";
    private static final String JMethod = "JMethod";
    private static final String JNonClassType = "JNonClassType";
    private static final String JClass = "JClass";
    private static final String JClass_ImplementsInterfaces = "JClass_ImplementsInterfaces";
    private static final String JClass_ExtendsClass = "JClass_ExtendsClass";
    private static final String JParameter = "JParameter";
    private static final String JMethod_Calls = "JMethod_Calls";
    private static final String JMethod_Instantiates = "JMethod_Instantiates";
    private static final String JMethod_UsedFields = "JMethod_UsedFields";
    private static final String JMethod_CatchesExceptions = "JMethod_CatchesExceptions";
    private static final String JMethod_ThrowsExceptions = "JMethod_ThrowsExceptions";
    private static final String JAnnotation = "JAnnotation";
    private static final String JAnnotationArgument = "JAnnotationArgument";
    private static final String JClass_ContainsClasses = "JClass_ContainsClasses";
    private static final String SalsaProject = "SalsaProject";
    private static final String JPackage_PackagesTransitive = "JPackage_PackagesTransitive";

    private static final int INITIAL_CAPACITY = 21111; // Large prime number


    private String sqlFileName = "facts.sql";
    private PrintWriter out; // = new PrintWriter(System.out, true);
    private boolean createTransitiveClosure = true;
    private boolean useMD5PrimaryKeys = false;

    // Speeds up Java file name translattion
    private HashMap fileNameTranslationMap = new HashMap();

    // Maps primary key to unique name (fully qualified class name etc)
    private HashMap<String, Long> md5Map = new HashMap<String, Long>(INITIAL_CAPACITY); // Large prime number
    private long md5Count = 1;
    private Options options;

    // Transitive relationsip table name suffix
    private String Transitive = "Transitive";

    // Default project names
    private String projectName = "Unknown";
    private String projectVersion = "Unknown";

    private String prefix = null;  // common name prefix in the primary key ---> unique name map

    // Default project source  name
    private String projectSource = null;

    /**
     * SQL Fact extractor constructor
     *
     * @param context Compilation context
     */
    public SQLTextBasicJavaExtractor(Context context) {
        super(context);
        processCommandLineOptions(context);
    }

    protected void initializeFactBases() {
    }

    protected void writeFact(FileWriter writer, String s) {
    }

    /**
     * Initializes output stream
     *
     * @throws java.io.IOException
     */
    protected void initializeOutputWriters() throws IOException {
        out = new PrintWriter(new FileWriter(sqlFileName));
    }

    protected FactExtractorScanner getExtractor() {
        if (!isRunning) {
            try {
                isRunning = true;
                initializeOutputWriters();
                initializeFactBases();
                scanner = new SQLFactExtractorScanner();
            } catch (IOException e) {
                System.err.println(e.toString());
                System.err.println("Whoops, could NOT create the writers!");
                assert false;
            }
        }
        return scanner;
    }

    /**
     * Translates relative Java file name to absolute name
     * using --sourcepath (or default convention)
     *
     * @param name Relative Java file name
     * @return
     */
    private String getAbsoluteFileName(String name) {
        String res = (String) fileNameTranslationMap.get(name);
        if (null != res) {
            return res;
        }
        String path = options.get("-sourcepath");
        if (null == path) {
            File f = new File(name);
            if (f.exists()) {
                try {
                    res = f.getCanonicalPath();
                    res = res.replace('\\', '/');
                    fileNameTranslationMap.put(name, res);
                    return res;
                } catch (IOException e) {
                    throw new IllegalArgumentException(e);
                }
            } else {
                fileNameTranslationMap.put(name, name);
                return name;
            }
        }
        int i = 0;
        int plen = path.length();
        while (i < plen) {
            int end = path.indexOf(File.pathSeparator, i);
            if (end < 0) {
                end = plen;
            }
            String pathname = path.substring(i, end);
            File f = new File(pathname + File.separator + name);
            if (f.exists()) {
                try {
                    res = f.getCanonicalPath();
                    res = res.replace('\\', '/');
                    fileNameTranslationMap.put(name, res);
                    return res;
                } catch (IOException e) {
                    throw new IllegalArgumentException(e);
                }
            }
            i = end + 1;
        }
        fileNameTranslationMap.put(name, name);
        return name;
    }

    /**
     * Processes relevant command line options
     *
     * @param context
     */
    private void processCommandLineOptions(Context context) {
        options = Options.instance(context); // creates a new one

        String transitive;

        // -XDcreatetransitivefacts or -XDcreatetransitivefacts={true,false}
        transitive = options.get("createtransitivefacts");
        if ("true".equalsIgnoreCase(transitive) || "createtransitivefacts".equalsIgnoreCase(transitive)) {
            createTransitiveClosure = true;
        } else if ("false".equalsIgnoreCase(transitive)) {
            createTransitiveClosure = false;
        }

        //   "-createtransitivefacts:{true,false}
        transitive = options.get("-createtransitivefacts:");
        if ("true".equalsIgnoreCase(transitive)) {
            createTransitiveClosure = true;
        } else if ("false".equalsIgnoreCase(transitive)) {
            createTransitiveClosure = false;
        }

        // Project path
        projectName = options.get("projectname");
        if (null == projectName) {
            projectName = "Unknown";
        }

        // Project version
        projectVersion = options.get("projectversion");
        if (null == projectVersion) {
            projectVersion = "Unknown";
        }

        // Output SQL file name
        sqlFileName = options.get("outputfile");
        if (null == sqlFileName) {
            sqlFileName = "facts.sql";
        }
    }

    private String md5(String name) {
        if (null == name) {
            return "NULL";
        }

        if (null == prefix) {
            prefix = projectName + ":" + projectSource + ":";
        }

        if (!useMD5PrimaryKeys) {
            Long l = md5Map.get(prefix + name);
            if (null == l) {
                l = new Long(md5Count);
                md5Map.put(prefix + name, l);
                md5Count += 1;
            }
            return l.toString();
        } else {
            return "md5('" + name + "') ";
        }
    }

    /**
     * Printe message to stderr
     *
     * @param msg
     */
    protected void debug(String msg) {
        System.err.println(msg);
    }

    /**
     * Wraps string in SQL quotes
     *
     * @param name
     * @return
     */
    private String quoted(String name) {
        return Utils.quoteSQLString(name);
    }

    private String sname(String name) {
        int i = name.lastIndexOf('.');
        if (i < 0) {
            return name;
        } else {
            return name.substring(i + 1);
        }
    }

    protected void finish() {
        populateTransitiveTables();
    }

    public void printSummary() {
        System.out.println("SQL Fact extraction summary:");
        System.out.println("Success!\n\nYou can find the output in the file \"" + sqlFileName + "\".");
        out.close();
    }

    void populateTransitiveTables() {
        populateTransitiveTable(JClass_ContainsClasses, "containsClasses", "containingClass");
        populateTransitiveTable(JClass_ImplementsInterfaces, "implementsInterfaces", "implementingClasses");
        populateTransitiveTable(JClass_ExtendsClass, "extendsClass", "extendingClasses");
        populateTransitiveTable("JPackage_Packages", "packages", "parentPackage", numberOfIterations, ", a.salsaProjectId");
        populateTransitiveTable(JMethod_Calls, "calls", "callers", numberOfIterations, ", a.filePosition");
    }

    void populateTransitiveTable(String table, String child, String parent) {
        populateTransitiveTable(table, child, parent, numberOfIterations, "");
    }

    void populateTransitiveTable(String table, String child, String parent, int iterations, String extra) {
        String transitiveTable = table + Transitive;
        out.println("--  ============== Populate " + transitiveTable + ": Start");
        out.println("SELECT @cntold := 0;\n" +
                "SELECT @cntnew := COUNT(*) FROM " + transitiveTable + ";");
        for (int i = 0; i < iterations; i++) {
            out.println("--  ============== Populate " + transitiveTable + ": iteration " + (i + 1));
            out.println(new StringBuffer().append("REPLACE into ")
                    .append(transitiveTable)
                    .append(" SELECT a.")
                    .append(child)
                    .append(", b.")
                    .append(parent)
                    .append(extra)
                    .append(" ")
                    .append("FROM ")
                    .append(transitiveTable)
                    .append(" AS a, ")
                    .append(transitiveTable)
                    .append(" AS b\n")
                    .append("WHERE @cntnew != @cntold AND a.")
                    .append(parent)
                    .append(" = b.")
                    .append(child)
                    .append(";\n")
                    .append("SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM ")
                    .append(transitiveTable)
                    .append(";")
                    .toString());
        }
        out.println("--  ============== Populate " + transitiveTable + ": End");
    }

    protected class SQLFactExtractorScanner extends FactExtractorScanner {
        protected void initialize() {
            writeSalsaProject();
        }

        protected void writeSalsaProject() {
            out.println(new StringBuffer().append(REPLACE)
                    .append(SalsaProject)
                    .append(" (id, system, project, version, source) values(")
                    .append(md5(projectName))
                    .append(COMMA).append(quoted(projectName))
                    .append(COMMA).append(quoted(projectName))
                    .append(COMMA).append(quoted(projectVersion))
                    .append(COMMA).append(quoted(projectSource))
                    .append(");")
                    .toString());
            out.println(new StringBuffer().append("SELECT @project_id := id FROM ")
                    .append(SalsaProject)
                    .append(" WHERE project = ")
                    .append(quoted(projectName))
                    .append(" AND version = ")
                    .append(quoted(projectVersion))
                    .append(";")
                    .toString());
        }

        protected void writeNonClassTypeFact(String typeName) {
            out.println(REPLACE + JNonClassType
                    + " (id, name, shortName, salsaProjectId) values("
                    + md5(typeName) + COMMA + quoted(typeName) + COMMA + quoted(sname(typeName))
                    + ", @project_id"
                    + ");");
        }

        protected void writeInitializerMethodFact(final String fullPseudoMethodNameString,
                                                  final String ownerName, boolean isStatic) {
            String signature = "void " + getSignature(fullPseudoMethodNameString);
            StringBuffer buf = new StringBuffer();
            buf.append(REPLACE)
                    .append(JMethod)
                    .append("(id, name, shortName, isAbstract, isConstructor, isStatic, isFinal, numParams, accessibility, ")
                    .append("filePosition, parentClass,returnType, signature, ");

            if (methodCounters.hasSources) {
                buf.append("numStatements, ")
                        .append("numLocalVariables, ")
                        .append("numTryClauses, ")
                        .append("numCatchClauses, ")
                        .append("numCalls, ")
                        .append("numInstantiations, ")
                        .append("numLines, ")
                        .append("numSynchronizedBlocks, ")
                        .append("numReturnStatements, ")
                        .append("numThrowStatements, ")
                        .append("numAssertStatements, ")
                        .append("numCasts, ")
                        .append("numSkips, ")
                        .append("numAssignStatements, ")
                        .append("numInitializedLocalVariables, ")
                        .append("numLoopStatements, ")
                        .append("numIfStatements, ")
                        .append("numSwitchStatements, ")
                        .append("numCaseStatements, ")
                        .append("numBranchStatements, ")
                        .append("numLinearSegments, ")
                        .append("numLocalAnnotations, ");
            }
            buf.append("salsaProjectId) ")
                    .append(" VALUES(")
                    .append(md5(fullPseudoMethodNameString))
                    .append(COMMA)
                    .append(quoted(fullPseudoMethodNameString))
                    .append(COMMA)
                    .append(quoted(sname(fullPseudoMethodNameString)))
                    .append(COMMA)
                    .append("false")
                    .append(COMMA)
                    .append("false")
                    .append(COMMA)
                    .append(isStatic ? "true" : "false")
                    .append(COMMA)
                    .append("false")
                    .append(COMMA)
                    .append("0")
                    .append(COMMA)
                    .append("'private'")
                    .append(COMMA)
                    .append(defaultPosition)
                    .append(COMMA)
                    .append(md5(ownerName))
                    .append(COMMA)
                    .append(md5("void"))
                    .append(COMMA)
                    .append(quoted(signature));
            if (methodCounters.hasSources) {
                buf.append(COMMA + methodCounters.numStatements)
                        .append(COMMA + methodCounters.numLocalVariables)
                        .append(COMMA + methodCounters.numTryClauses)
                        .append(COMMA + methodCounters.numCatchClauses)
                        .append(COMMA + methodCounters.numCalls)
                        .append(COMMA + methodCounters.numInstantiations)
                        .append(COMMA + methodCounters.numLines)
                        .append(COMMA + methodCounters.numSynchronizedBlocks)
                        .append(COMMA + methodCounters.numReturnStatements)
                        .append(COMMA + methodCounters.numThrowStatements)
                        .append(COMMA + methodCounters.numAssertStatements)
                        .append(COMMA + methodCounters.numCasts)
                        .append(COMMA + methodCounters.numSkips)
                        .append(COMMA + methodCounters.numAssignStatements)
                        .append(COMMA + methodCounters.numInitializedLocalVariables)
                        .append(COMMA + methodCounters.numLoopStatements)
                        .append(COMMA + methodCounters.numIfStatements)
                        .append(COMMA + methodCounters.numSwitchStatements)
                        .append(COMMA + methodCounters.numCaseStatements)
                        .append(COMMA + methodCounters.numBranchStatements)
                        .append(COMMA + methodCounters.numLinearSegments)
                        .append(COMMA + methodCounters.numLocalAnnotations);
            }

            buf.append(", @project_id")
                    .append(");");

            out.println(buf.toString());
        }

        protected void writeMethodInfoFact(final String methodName, final String className,
                                           final String returnTypeName, final String shortName,
                                           final String sAccessibility, final String sIsStatic,
                                           final String sIsFinal, final String sIsAbstract, final String sIsConstructor,
                                           final String sIsSynchronized, final String sContainsSynchBlock,
                                           Type.MethodType methodType,
                                           String lineNum) {
            String signature = returnTypeName + " " + getSignature(methodName);
            StringBuffer buf = new StringBuffer();
            buf.append(REPLACE)
                    .append(JMethod)
                    .append("(id, name, shortName,isAbstract, isConstructor, isStatic, isFinal, numParams, accessibility,")
                    .append("filePosition, parentClass, returnType, signature, ");

            if (methodCounters.hasSources) {
                buf.append("numStatements, ")
                        .append("numLocalVariables, ")
                        .append("numTryClauses, ")
                        .append("numCatchClauses, ")
                        .append("numCalls, ")
                        .append("numInstantiations, ")
                        .append("numLines, ")
                        .append("numSynchronizedBlocks, ")
                        .append("numReturnStatements, ")
                        .append("numThrowStatements, ")
                        .append("numAssertStatements, ")
                        .append("numCasts, ")
                        .append("numSkips, ")
                        .append("numAssignStatements, ")
                        .append("numInitializedLocalVariables, ")
                        .append("numLoopStatements, ")
                        .append("numIfStatements, ")
                        .append("numSwitchStatements, ")
                        .append("numCaseStatements, ")
                        .append("numBranchStatements, ")
                        .append("numLinearSegments, ")
                        .append("numLocalAnnotations, ");
            }
            buf.append("salsaProjectId")
                    .append(") VALUES (")
                    .append(md5(methodName))
                    .append(COMMA).append(quoted(methodName))
                    .append(COMMA).append(quoted(shortName))
                    .append(COMMA).append(sIsAbstract)
                    .append(COMMA).append(sIsConstructor)
                    .append(COMMA).append(sIsStatic)
                    .append(COMMA).append(sIsFinal)
                    .append(COMMA).append(methodType.argtypes().length())
                    .append(COMMA).append(quoted(sAccessibility))
                    .append(COMMA).append(lineNum)
                    .append(COMMA).append(md5(className))
                    .append(COMMA).append(md5(returnTypeName))
                    .append(COMMA).append(quoted(signature));

            if (methodCounters.hasSources) {
                buf.append(COMMA + methodCounters.numStatements)
                        .append(COMMA).append(methodCounters.numLocalVariables)
                        .append(COMMA).append(methodCounters.numTryClauses)
                        .append(COMMA).append(methodCounters.numCatchClauses)
                        .append(COMMA).append(methodCounters.numCalls)
                        .append(COMMA).append(methodCounters.numInstantiations)
                        .append(COMMA).append(methodCounters.numLines)
                        .append(COMMA).append(methodCounters.numSynchronizedBlocks)
                        .append(COMMA).append(methodCounters.numReturnStatements)
                        .append(COMMA).append(methodCounters.numThrowStatements)
                        .append(COMMA).append(methodCounters.numAssertStatements)
                        .append(COMMA).append(methodCounters.numCasts)
                        .append(COMMA).append(methodCounters.numSkips)
                        .append(COMMA).append(methodCounters.numAssignStatements)
                        .append(COMMA).append(methodCounters.numInitializedLocalVariables)
                        .append(COMMA).append(methodCounters.numLoopStatements)
                        .append(COMMA).append(methodCounters.numIfStatements)
                        .append(COMMA).append(methodCounters.numSwitchStatements)
                        .append(COMMA).append(methodCounters.numCaseStatements)
                        .append(COMMA).append(methodCounters.numBranchStatements)
                        .append(COMMA).append(methodCounters.numLinearSegments)
                        .append(COMMA).append(methodCounters.numLocalAnnotations);
            }
            buf.append(", @project_id")
                    .append(");");
            out.println(buf.toString());
        }

        /**
         * For two packages: com.sun and com.sun.salsa
         * In JPackage_PackagesTransitive:
         * --------------------
         * |packages            |parentPackage
         * |com.sun.salsa       |com.sun
         */
        protected void writePackageInfoFact(String packageName, String shortName, String ownerName) {
            if ("<root_package>".equals(packageName) || "<unnamed_package>".equals(packageName)) {
                packageName = null;
            }
            if ("<root_package>".equals(ownerName) || "<unnamed_package>".equals(ownerName)) {
                ownerName = null;
            }
            if (null != packageName) {
                out.println(new StringBuffer().append(REPLACE)
                        .append(JPackage)
                        .append("(id, name, shortName, parentPackage, salsaProjectId) VALUES(")
                        .append(md5(packageName))
                        .append(COMMA)
                        .append(quoted(packageName))
                        .append(COMMA)
                        .append(quoted(shortName))
                        .append(COMMA)
                        .append(md5(ownerName))
                        .append(", @project_id")
                        .append(");")
                        .toString());
                if (null != ownerName) {
                    out.println(new StringBuffer().append(REPLACE)
                            .append(JPackage_PackagesTransitive)
                            .append("(packages, parentPackage, salsaProjectId) VALUES(")
                            .append(md5(packageName))
                            .append(COMMA)
                            .append(md5(ownerName))
                            .append(", @project_id")
                            .append(");")
                            .toString());
//                    makeTransitiveClosure(md5(ownerName), md5(packageName), "JPackage_Packages", "parentPackage", "packages");
                }
            }
        }

        /**
         * A is inner class of B
         * Then in JClass_ContainsClasses:
         * --------------------
         * |containsClass      |containingClass
         * |A                  |B
         */
        protected void writeClassInfoFact(String ownerName,
                                          String classOwnerName, String packageOwnerName,
                                          String className,
                                          String sClassKind, String shortName,
                                          String sAccessibility, String sIsStatic,
                                          String sIsInner, boolean anInterface,
                                          String sIsAbstract, String sIsFinal,
                                          String sourceFileName, String lineNum) {
            if ("<unnamed_package>".equals(packageOwnerName)) {
                packageOwnerName = null;
            }
            StringBuffer buf = new StringBuffer();
            buf.append(REPLACE)
                    .append(JClass)
                    .append("(id, name, shortName, isStatic, isFinal, isInner, isInterface, accessibility, ")
                    .append("sourceFile, filePosition, containingClass, parentPackage, ");

            if (classCounters.hasSources) {
                buf.append("numStatements, ")
                        .append("numLocalVariables, ")
                        .append("numTryClauses, ")
                        .append("numCatchClauses, ")
                        .append("numCalls, ")
                        .append("numInstantiations, ")
                        .append("numLines, ")
                        .append("numSynchronizedBlocks, ")
                        .append("numReturnStatements, ")
                        .append("numThrowStatements, ")
                        .append("numAssertStatements, ")
                        .append("numCasts, ")
                        .append("numSkips, ")
                        .append("numAssignStatements, ")
                        .append("numInitializedLocalVariables, ")
                        .append("numLoopStatements, ")
                        .append("numIfStatements, ")
                        .append("numSwitchStatements, ")
                        .append("numCaseStatements, ")
                        .append("numBranchStatements, ")
                        .append("numLinearSegments, ")
                        .append("numLocalAnnotations, ")
                        .append("numFields, ")
                        .append("numInitializedFields, ")
                        .append("numMethods, ");
            }
            buf.append("salsaProjectId")
                    .append("\n    ) VALUES(")
                    .append(md5(className))
                    .append(COMMA).append(quoted(className))
                    .append(COMMA).append(quoted(shortName))
                    .append(COMMA).append(sIsStatic)
                    .append(COMMA).append(sIsFinal)
                    .append(COMMA).append(sIsInner)
                    .append(COMMA).append(anInterface ? "true" : "false")
                    .append(COMMA).append(quoted(sAccessibility))
                    .append(COMMA).append(quoted(getAbsoluteFileName(sourceFileName)))
                    .append(COMMA).append(lineNum)
                    .append(COMMA).append(md5(classOwnerName))
                    .append(COMMA).append(md5(packageOwnerName));

            if (classCounters.hasSources) {
                buf.append(COMMA).append(classCounters.numStatements)
                        .append(COMMA).append(classCounters.numLocalVariables)
                        .append(COMMA).append(classCounters.numTryClauses)
                        .append(COMMA).append(classCounters.numCatchClauses)
                        .append(COMMA).append(classCounters.numCalls)
                        .append(COMMA).append(classCounters.numInstantiations)
                        .append(COMMA).append(classCounters.numLines)
                        .append(COMMA).append(classCounters.numSynchronizedBlocks)
                        .append(COMMA).append(classCounters.numReturnStatements)
                        .append(COMMA).append(classCounters.numThrowStatements)
                        .append(COMMA).append(classCounters.numAssertStatements)
                        .append(COMMA).append(classCounters.numCasts)
                        .append(COMMA).append(classCounters.numSkips)
                        .append(COMMA).append(classCounters.numAssignStatements)
                        .append(COMMA).append(classCounters.numInitializedLocalVariables)
                        .append(COMMA).append(classCounters.numLoopStatements)
                        .append(COMMA).append(classCounters.numIfStatements)
                        .append(COMMA).append(classCounters.numSwitchStatements)
                        .append(COMMA).append(classCounters.numCaseStatements)
                        .append(COMMA).append(classCounters.numBranchStatements)
                        .append(COMMA).append(classCounters.numLinearSegments)
                        .append(COMMA).append(classCounters.numLocalAnnotations)

                        .append(COMMA).append(classCounters.numFields)
                        .append(COMMA).append(classCounters.numInitializedFields)
                        .append(COMMA).append(classCounters.numMethods);
            }

            buf.append(", @project_id").append(");");

            out.println(buf.toString());
            // Process transitive closure of class ----(contains)----> inner_class
            if (null != classOwnerName) {
                String table = JClass_ContainsClasses;
                String b = md5(classOwnerName);
                String a = md5(className);
                out.println(REPLACE + table + Transitive + "(containsClasses, containingClass) VALUES(" + a + COMMA
                        + b
                        + ");");
//                makeTransitiveClosure(a, b, table, "containsClasses", "containingClass");
            }
        }

        /**
         * B implements A
         * Then in JClass_ImplementsInterfaces:
         * --------------------
         * |implementsInterfaces   |implementingClasses
         * |A                      |B
         * <p/>
         * Inheritance example:
         * interface IY {}
         * interface IX extends IY{}
         * <p/>
         * class A implements IX{}
         * class B extends A {}
         * <p/>
         * extendsClass
         * B   A
         * IX  IY
         * extendsClass.**.
         * B   A
         * IX  IY
         * implementsInterfaces
         * A   IX
         * <p/>
         * implementsInterfaces.**.
         * A   IX
         * A   IY
         * B   IX
         * B   IY
         */
        protected void writeClassExtendsOrImplementsFact(final boolean anInterface, final String className,
                                                         final String intrfaceName) {
            // anInterface means whether className is an interface itself
            String table = JClass_ImplementsInterfaces;
            String b = md5(className);
            String a = md5(intrfaceName);

            if (anInterface) {
                writeClassExtendsFact(className, intrfaceName);
            } else {
                out.println(REPLACE + table + "(implementsInterfaces, implementingClasses) VALUES(" + a + COMMA + b
                        + ");");
                out.println(REPLACE + table + Transitive + "(implementsInterfaces, implementingClasses) VALUES(" + a
                        + COMMA
                        + b
                        + ");");
                //            makeTransitiveClosure(a, b, table, "implementsInterfaces", "implementingClasses");
            }
        }

        /**
         * B extends A.
         * Then in JClass:
         * --------------------
         * |name    |extendsClass
         * |B       |A
         * <p/>
         * Then in JClass_ExtendsClass:
         * --------------------
         * |extendsClass   |extendingClasses
         * |A              |B
         */
        protected void writeClassExtendsFact(final String className, final String inheritanceParentName) {
            String b = md5(className);
            String a = md5(inheritanceParentName);
            out.println(UPDATE + JClass + " SET extendsClass = " + a + " WHERE id = " + b + ";");
            out.println(REPLACE + JClass_ExtendsClass + Transitive + "(extendsClass, extendingClasses) VALUES (" + a
                    + COMMA
                    + b
                    + ");");
            //makeTransitiveClosure(a, b, JClass_ExtendsClass, "extendsClass", "extendingClasses");
        }

        protected void writeParameterInfoFact(final String paramName, final String methodName,
                                              final String paramTypeName,
                                              final String shortParamName, int paramIndex) {
            out.println(new StringBuffer().append(REPLACE)
                    .append(JParameter)
                    .append("(id, name, shortName, parameterIndex, method, type, salsaProjectId")
                    .append(") VALUES (")
                    .append(md5(paramName))
                    .append(COMMA)
                    .append(quoted(paramName))
                    .append(COMMA)
                    .append(quoted(shortParamName))
                    .append(COMMA)
                    .append(paramIndex)
                    .append(COMMA)
                    .append(md5(methodName))
                    .append(COMMA)
                    .append(md5(paramTypeName))
                    .append(", @project_id")
                    .append(");")
                    .toString());
        }

        protected void writeFieldInfoFact(final String varName, final String className, final String typeName,
                                          final String shortName, final String sAccessibility, final String sIsStatic,
                                          final String sIsFinal, String lineNum) {
            out.println(new StringBuffer().append(REPLACE)
                    .append(JField)
                    .append("(id, name, shortName, isStatic, isFinal, accessibility,")
                    .append("filePosition, parentClass, type, salsaProjectId")
                    .append(") VALUES (")
                    .append(md5(varName))
                    .append(COMMA)
                    .append(quoted(varName))
                    .append(COMMA)
                    .append(quoted(shortName))
                    .append(COMMA)
                    .append(sIsStatic)
                    .append(COMMA)
                    .append(sIsFinal)
                    .append(COMMA)
                    .append(quoted(sAccessibility))
                    .append(COMMA)
                    .append(lineNum)
                    .append(COMMA)
                    .append(md5(className))
                    .append(COMMA)
                    .append(md5(typeName))
                    .append(", @project_id")
                    .append(");")
                    .toString());
        }

        protected void writeMethodCallsFact(final String callerName, final String calleeName, final int line,
                                            final int column) {
            // TODO: Column number is ignored for now add it to the database schema
            String b = md5(callerName);
            String a = md5(calleeName);
            String table = JMethod_Calls;
            out.println(REPLACE + table + "(calls, callers, filePosition) VALUES (" + a + COMMA + b + COMMA + line
                    + ");");
            out.println(REPLACE + table + Transitive + "(calls, callers, filePosition) VALUES (" + a + COMMA + b
                    + COMMA
                    + line
                    + ");");
//            makeTransitiveClosure(a, b, table, "calls", "callers");
        }

        /**
         * Method a calls method b
         * Then in JMethod_Calls:
         * --------------------
         * |calls     |callers
         * |b         |a
         */
        protected void writeMethodInstantiatesClassInstanceFact(final String instantiatorMethodName,
                                                                final String instantiateeClassName,
                                                                final int line, final int column) {
//          // TODO: Column number is ignored for now; add it to the database schema later
            String a = md5(instantiatorMethodName);
            String b = md5(instantiateeClassName);
            String table = JMethod_Instantiates;
            out.println(new StringBuffer().append(REPLACE)
                    .append(table)
                    .append("(instantiatedBy, instantiates, filePosition) VALUES (")
                    .append(a)
                    .append(COMMA)
                    .append(b)
                    .append(COMMA)
                    .append(line)
                    .append(");")
                    .toString());
        }

        protected void writeMethodCatchesExceptionFact(final String catcherMethodName, final String exceptionName,
                                                       final int line, final int column) {
            // TODO: Column number is ignored for now; add it to the database schema later
            out.println(new StringBuffer().append(REPLACE)
                    .append(JMethod_CatchesExceptions)
                    .append("(catchesExceptions, caughtBy, filePosition) VALUES (")
                    .append(md5(exceptionName))
                    .append(COMMA)
                    .append(md5(catcherMethodName))
                    .append(COMMA)
                    .append(line)
                    .append(");")
                    .toString());
        }

        protected void writeClassThrowsExceptionFact(final String throwerMethodName, final String exceptionName,
                                                     final int line, final int column) {
            // TODO: Column number is ignored for now; add it to the database schema later
            out.println(new StringBuffer().append(REPLACE)
                    .append(JMethod_ThrowsExceptions)
                    .append("(throwsExceptions, thrownBy, filePosition) VALUES (")
                    .append(md5(exceptionName))
                    .append(COMMA)
                    .append(md5(throwerMethodName))
                    .append(COMMA)
                    .append(line)
                    .append(");")
                    .toString());
        }

        protected void writeMethodUsesFieldFact(final String userName, final String useeName,
                                                final int line, final int column) {
            // TODO: Column number is ignored for now; add it to the database schema later
            out.println(new StringBuffer().append(REPLACE)
                    .append(JMethod_UsedFields)
                    .append("(usedFields, usedByMethods, filePosition) VALUES (")
                    .append(md5(useeName))
                    .append(COMMA)
                    .append(md5(userName))
                    .append(COMMA)
                    .append(line)
                    .append(");")
                    .toString());
        }

        protected void writeAnnotationInstanceFact(final String annName, final String ownerName,
                                                   final String annTypeName, int kind) {
            String parentMethod = "NULL";
            String parentClass = "NULL";
            String parentField = "NULL";
            String parentParameter = "NULL";
            switch (kind) {
                case METHOD_ANNOTATION:
                    parentMethod = md5(ownerName);
                    break;
                case FIELD_ANNOTATION:
                    parentField = md5(ownerName);
                    break;
                case CLASS_ANNOTATION:
                    parentClass = md5(ownerName);
                    break;
                case PARAMETER_ANNOTATION:
                    parentParameter = md5(ownerName);
                    break;
                default:
                    throw new IllegalArgumentException("Illegal argument 'kind' = " + kind);
            }
            // TODO: Column number is ignored for now add it to the database schema
            out.println(new StringBuffer().
                    append(REPLACE).
                    append(JAnnotation).
                    append("(id, name, parentClass, parentMethod, parentField, parentParameter, type, shortName, salsaProjectId")
                    .append(") VALUES (")
                    .append(md5(annName))
                    .append(COMMA)
                    .append(quoted(annName))
                    .append(COMMA)
                    .append(parentClass)
                    .append(COMMA)
                    .append(parentMethod)
                    .append(COMMA)
                    .append(parentField)
                    .append(COMMA)
                    .append(parentParameter)
                    .append(COMMA)
                    .append(md5(annTypeName))
                    .append(COMMA)
                    .append(quoted(getShortName(annName)))
                    .append(", @project_id")
                    .append(");")
                    .toString());
        }

        protected void writeAnnotationShortNameAndNumArgumentsFact(final String annName,
                                                                   String shortName, int argcount) {
            out.println(new StringBuffer().append(UPDATE)
                    .append(JAnnotation)
                    .append(" SET ")
                    .append("shortName = ")
                    .append(quoted(shortName))
                    .append(COMMA)
                    .append("numArgs = ")
                    .append(argcount)
                    .append(" WHERE id = ")
                    .append(md5(annName))
                    .append(";")
                    .toString());
        }

        protected void writeAnnotationArgumentInstanceFact(String argName, final String annName, String type) {
            out.println(new StringBuffer().append(REPLACE)
                    .append(JAnnotationArgument)
                    .append("(id, name, annotation, type, salsaProjectId")
                    .append(") VALUES (")
                    .append(md5(argName))
                    .append(COMMA).append(quoted(argName))
                    .append(COMMA).append(md5(annName))
                    .append(COMMA).append(md5(type))
                    .append(", @project_id").append(");")
                    .toString());
        }

        protected void writeAnnotationArgumentShortNameAndStringValueFact(String argName,
                                                                          String shortName, String stringValue) {
            stringValue = stringValue.replace("\\r", "\r");
            stringValue = stringValue.replace("\\n", "\n");
            out.println(new StringBuffer().append(UPDATE)
                    .append(JAnnotationArgument)
                    .append(" SET ")
                    .append("shortName = ")
                    .append(quoted(shortName))
                    .append(COMMA)
                    .append("stringValue = ")
                    .append(stringValue)
                    .append(" WHERE id = ")
                    .append(md5(argName))
                    .append(";")
                    .toString());
        }
    }
}

////////////////////// Obsolete code TODO: Remove me later //////////////////////////////////
//    protected void makeTransitiveClosure(String a, String b, String table, String fieldA, String fieldB) {
//        if (createTransitiveClosure) {
//            table = table + "Transitive"; // Compose transitive closure table name
//            out.println("-- [[[[[[[[[[[[[ Calculate Transitive closure [[[[[[[[[[[[[[[[[[[[[[[[[");
//            out.println("TRUNCATE table stage1;");
//            out.println("TRUNCATE table stage2;");
//            out.println("TRUNCATE table stage3;");
//            out.println("REPLACE INTO stage1 (a,b) VALUES(" + a + ", " + b + ");");
//            out.println("REPLACE INTO stage2 SELECT stage1.a, " + table + "." + fieldB + " FROM " + table + ", stage1 WHERE " + table + "." + fieldA + " = stage1.b;");
//            out.println("REPLACE INTO stage3 SELECT " + table + "." + fieldA + ", stage2.b FROM " + table + ", stage2 WHERE " + table + "." + fieldB + " = stage2.a;");
//            out.println("REPLACE INTO " + table + "(" + fieldA + ", " + fieldB + ") SELECT * FROM stage1;");
//            out.println("REPLACE INTO " + table + "(" + fieldA + ", " + fieldB + ") SELECT * FROM stage2;");
//            out.println("REPLACE INTO " + table + "(" + fieldA + ", " + fieldB + ") SELECT * FROM stage3;");
//            out.println("-- ]]]]]]]]]]]]] Calculate Transitive closure ]]]]]]]]]]]]]]]]]]]]]]]]]");
//        }
//    }

//    void populateJPackage_PackagesTransitive() {
//        out.println("--  ============== Populate JPackage_PackagesTransitive: Start");
//        out.println("SELECT @cntold := 0;\n" +
//                    "SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;");
//        for (int i = 0; i < 100; i++) {
//            out.println("--  ============== Populate JPackage_PackagesTransitive: iteration " + (i + 1));
//            out.println("REPLACE into JPackage_PackagesTransitive SELECT a.packages, b.parentPackage, a.salsaProjectId\n" +
//                        "FROM JPackage_PackagesTransitive AS a, JPackage_PackagesTransitive AS b\n" +
//                        "WHERE @cntnew != @cntold\n" +
//                        "  AND a.parentPackage = b.packages;\n" +
//                        "SELECT @cntold := @cntnew;\n" +
//                        "SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;");
//        }
//        out.println("--  ============== Populate JPackage_PackagesTransitive: End");
//    }

//    void populateJMethod_CallsTransitive() {
//        out.println("--  ============== Populate JMethod_CallsTransitive: Start");
//        out.println("SELECT @cntold := 0;\n" +
//                    "SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;");
//        for (int i = 0; i < 100; i++) {
//            out.println("--  ============== Populate JMethod_CallsTransitive: iteration " + (i + 1));
//            out.println("REPLACE into JMethod_CallsTransitive SELECT a.calls, b.callers, a.filePosition\n" +
//                        "FROM JMethod_CallsTransitive AS a, JMethod_CallsTransitive AS b\n" +
//                        "WHERE @cntnew != @cntold\n" +
//                        "  AND a.callers = b.calls;\n" +
//                        "SELECT @cntold := @cntnew;\n" +
//                        "SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;");
//        }
//        out.println("--  ============== Populate JMethod_CallsTransitive: End");
//    }
