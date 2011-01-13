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
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Options;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.*;

/**
 * Performs Java Characteristics extraction.
 *
 * @author Yury Kamen
 */
public class SQLBasicJavaExtractor extends BasicJavaExtractor implements SQLTableConstants {
    // Maximum number of iterations to build transitive relationships
    private static final int NUMBER_OF_TRANSITIVE_ITERATIONS = 5000;

    // Database-located global unique id counter increment
    private static final int GLOBAL_ID_COUNTER_INCREMENT = 100;

    // Large prime number for initial HashMap sizes
    private static final int INITIAL_HASHMAP_CAPACITY = 21111;

    private static final String COMMA = ", ";

    //  SQL keywords
    private static final String UPDATE = "UPDATE ";
    private static final String REPLACE = "REPLACE INTO ";
    private static final String INSERT = "INSERT INTO ";

    // Salsa-specific SQL table names
    private static final String JClass_ContainsClasses = "JClass_ContainsClasses";
    private static final String SalsaProject = "SalsaProject";
    private static final String JClass_ExtendsClass = "JClass_ExtendsClass";
    private final static String Transitive = "Transitive";      // Transitive relationsip table name suffix

    private String sqlFileName = "facts.sql";
    private java.io.PrintWriter out; // = new PrintWriter(System.out, true);
    private boolean createTransitiveClosure = true;
    private boolean useMD5PrimaryKeys = false;

    private HashMap fileNameTranslationMap = new HashMap();     // Speeds up Java file name translation

    // Maps primary key to unique name (fully qualified class name etc)
    private HashMap<String, Long> md5Map = new HashMap<String, Long>(INITIAL_HASHMAP_CAPACITY);
    private long md5Count = 0;
    private long globalIdCount = -1;

    private Options options;     // Javac compiler options

    private String projectName = "global";    // Default project name
    private String projectVersion = "latest"; // Default project version
    private String projectSource = null; // Default project source  name

    private String prefix = null;  // Common key prefix for the primary key ---> unique name mapping

    // RDBMS access properties
    private String databaseDriver = "org.gjt.mm.mysql.Driver";
    private String databaseURL = "jdbc:mysql://localhost:3306/salsa";
    private String databaseUser = "salsa";
    private String databasePassword = "salsa";
    private Connection databaseConnection = null;
    private Statement sqlStatement = null;
    private boolean initialized = false;
    private int applicationId;
    private boolean truncate = false;
    private boolean debug = false;
    private int salsaProjectId = -1;

    // Prepared statements
    private PreparedStatement statement_JAnnotationArgument;



    // SQL database access methods [[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[
    private void checkState() {
        if (null == databaseDriver) {
            throw new IllegalStateException("null == databaseDriver");
        }
        if (null == databaseURL) {
            throw new IllegalStateException("null == databaseURL");
        }
        if (null == databaseUser) {
            throw new IllegalStateException("null == databaseUser");
        }
        if (null == databasePassword) {
            throw new IllegalStateException("null == databasePassword");
        }
    }

    // Database access methods
    public Connection connect() {
        checkState();
        disconnect();

        try {
            Class.forName(databaseDriver);
            databaseConnection = DriverManager.getConnection(databaseURL, databaseUser, databasePassword);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return databaseConnection;
    }

    public void disconnect() {
        if (null != databaseConnection) {
            try {
                sqlStatement.close();
                sqlStatement = null;

                databaseConnection.close();
                databaseConnection = null;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean isConnected() {
        return null != databaseConnection;
    }

    protected void executeSQL(String sqlString) {
        executeSQL(sqlString, false);
    }

    protected void executeSQL(String sqlString, boolean printOnly) {
        if (out != null) {
            out.println(sqlString);
        }
        if (debug) {
            System.out.println("SQL <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< ");
            System.out.println(sqlString);
            System.out.println("SQL >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ");
        }

        if (!printOnly) {
            try {
                if (null == sqlStatement) {
                    sqlStatement = databaseConnection.createStatement();
                }
                if (sqlString.indexOf("REPLACE") >= 0 && sqlString.indexOf("@project_id") < 0) {
                    int foo = 1;
                }
                sqlStatement.execute(sqlString);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    protected int getLastInsertId() {
        try {
            ResultSet rs = sqlStatement.executeQuery("SELECT last_insert_id()");
            rs.next();
            int res = rs.getInt(1);
            rs.close();
            return res;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected void init() throws IOException {
        //        super.init();
        initializeOutputWriters();
        initializeFactBases();
        checkState();
        if (!initialized) {
            initialized = true;
        }
    }

    protected void findSalsaProjectId() {
        try {
            ResultSet rs = sqlStatement.executeQuery(new StringBuffer()
                            .append("SELECT @project_id := id FROM ")
                                    .append(SalsaProject).append(" WHERE project = ")
                                    .append(quoted(projectName))
                                    .append(" AND version = ")
                                    .append(quoted(projectVersion))
                                    .append(";")
                                    .toString());
            rs.next();
            salsaProjectId = rs.getInt(1);
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void truncateAllSQLTables() {
        for (int i = 0; i < SQL_TABLES.length; i++) {
            executeSQL("DELETE FROM " + SQL_TABLES[i] + ";");
        }
        executeSQL("DELETE FROM salsaProject;");
        executeSQL("DELETE FROM salsaCookie;");
        executeSQL("DELETE FROM readVariable;");
        executeSQL("DELETE FROM writeVariable;");
    }

    public void truncateProject() {
        for (int i = 0; i < SQL_TABLES.length; i++) {
            executeSQL("DELETE FROM " + SQL_TABLES[i] +
                    "\nUSING " + SQL_TABLES[i] + ", salsaProject" +
                    "\nWHERE salsaProject.project = '" + projectName + "'" +
                    "\n  AND salsaProject.version = '" + projectVersion + "'" +
                    "\n  AND salsaProject.id = " + SQL_TABLES[i] + ".salsaProjectId" +
                    ";");
        }
        executeSQL("DELETE FROM salsaProject" +
                "\nWHERE salsaProject.project = '" + projectName + "'" +
                "\n  AND salsaProject.version = '" + projectVersion + "'" +
                ";");
    }

    protected void writeSalsaProjectInfo() {
        executeSQL(new StringBuffer()
                .append(INSERT)
                        .append(SalsaProject)
                        .append(" (system, project, version, source) values(")
                        .append(quoted(projectName))
                        .append(COMMA)
                        .append(quoted(projectName))
                        .append(COMMA)
                        .append(quoted(projectVersion))
                        .append(COMMA)
                        .append(quoted(projectSource))
                        .append(");")
                        .toString());
    }

    protected void writeSalsaProject() {
        writeSalsaProjectInfo();
        findSalsaProjectId();
    }
    // SQL database access methods ]]]]]]]]]]]]]]]]]]]]]]]]]]]]]

    /**
     * SQL Fact extractor constructor
     *
     * @param context Compilation context
     */
    public SQLBasicJavaExtractor(Context context) {
        super(context);
        processCommandLineOptions(context);
        checkState();
    }


    protected void initializeFactBases() {
    }

    protected void writeFact(FileWriter writer, String s) {
    }

    /**
     * Initializes output stream
     *
     * @throws IOException
     */
    protected void initializeOutputWriters() throws IOException {
        out = new PrintWriter(new FileWriter(sqlFileName));
    }

    protected FactExtractorScanner getExtractor() {
        if (!isRunning) {
            try {
                isRunning = true;
                init();
                connect();
                if (truncate) {
                    truncateAllSQLTables();
                } else {
                    truncateProject();
                }
                //                writeSalsaProject();

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
            projectName = "global";
        }

        // Project version
        projectVersion = options.get("projectversion");
        if (null == projectVersion) {
            projectVersion = "latest";
        }

        // Output SQL file name
        sqlFileName = options.get("outputfile");
        if (null == sqlFileName) {
            sqlFileName = "facts.sql";
        }

        // Process database-specific command line options
        if (null != options.get("dbdriver")) {
            databaseDriver = options.get("dbdriver");
        }

        if (null != options.get("dburl")) {
            databaseURL = options.get("dburl");
        }

        if (null != options.get("dbuser")) {
            databaseUser = options.get("dbuser");
        }

        if (null != options.get("dbpasswd")) {
            databasePassword = options.get("dbpasswd");
        }

        if (null != options.get("truncate")) {
            if ("true".equalsIgnoreCase(options.get("truncate"))) {
                truncate = true;
            } else if ("false".equalsIgnoreCase(options.get("truncate"))) {
                truncate = false;
            } else {
                throw new IllegalArgumentException("Wrong 'truncat' option: " + options.get("truncate"));
            }
        }
    }

    protected int getLastGlobalCounter() {
        try {
            ResultSet rs = sqlStatement.executeQuery("SELECT counter FROM SalsaIdCounter");
            rs.next();
            int res = rs.getInt(1);
            rs.close();
            sqlStatement.execute("UPDATE SalsaIdCounter SET counter = counter + " + GLOBAL_ID_COUNTER_INCREMENT);
            return res;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String getMD5(String name) {
        if (null == name) {
            return "NULL";
        }

        if (null == prefix) {
            prefix = projectName + ":" + projectSource + ":";
        }

        if (!useMD5PrimaryKeys) {
            Long l = md5Map.get(prefix + name);
            if (null == l) {
                if (globalIdCount == -1 || md5Count >= globalIdCount + GLOBAL_ID_COUNTER_INCREMENT) {
                    globalIdCount = md5Count = getLastGlobalCounter();
                }
                l = new Long(md5Count++);
                md5Map.put(prefix + name, l);
            }
            return l.toString();
        } else {
            return "setMD5('" + name + "') ";
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
        disconnect();
    }

    public void printSummary() {
        //        System.out.println("-- SQL Fact extraction summary:");
        //        System.out.println("-- Success!\n\nYou can find the output in the file \"" + sqlFileName + "\".");
        out.close();
    }

    void populateTransitiveTables() {
        if (true || debug) {
            System.out.println("Started Creating Transitive Relationships");
        }
        populateTransitiveTable(JClass_ContainsClasses, "containsClasses", "containingClass", NUMBER_OF_TRANSITIVE_ITERATIONS, ", a.salsaProjectId");
        populateTransitiveTable(JClass_ImplementsInterfaces, "implementsInterfaces", "implementingClasses", NUMBER_OF_TRANSITIVE_ITERATIONS, ", a.salsaProjectId");
        populateTransitiveTable(JClass_ExtendsClass, "extendsClass", "extendingClasses", NUMBER_OF_TRANSITIVE_ITERATIONS, ", a.salsaProjectId");
        populateTransitiveTable("JPackage_Packages", "packages", "parentPackage", NUMBER_OF_TRANSITIVE_ITERATIONS, ", a.salsaProjectId");
        populateTransitiveTable(JMethod_Calls, "calls", "callers", NUMBER_OF_TRANSITIVE_ITERATIONS, ", a.filePosition, a.salsaProjectId");
        if (true || debug) {
            System.out.println("Finished Creating Transitive Relationships");
        }

    }

    void populateTransitiveTable(String table, String child, String parent) {
        populateTransitiveTable(table, child, parent, NUMBER_OF_TRANSITIVE_ITERATIONS, "");
    }

    protected int findCntOld() {
        int res = -1;
        try {
            ResultSet rs = sqlStatement.executeQuery("SELECT @cntold;");
            rs.next();
            res = rs.getInt(1);
            rs.close();
            return res;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected int findCntNew() {
        int res = -1;
        try {
            ResultSet rs = sqlStatement.executeQuery("SELECT @cntnew;");
            rs.next();
            res = rs.getInt(1);
            rs.close();
            return res;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    void populateTransitiveTable(String table, String child, String parent, int iterations, String extra) {
        String transitiveTable = table + Transitive;
        executeSQL("--  ============== Populate " + transitiveTable + ": Start");
        executeSQL("SELECT @cntold := 0;");
        executeSQL("SELECT @cntnew := COUNT(*) FROM " + transitiveTable + ";");
        for (int i = 0; i < iterations; i++) {
            executeSQL("--  ============== Populate " + transitiveTable + ": iteration " + (i + 1));
            String s = new StringBuffer().append("REPLACE into ")
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
                            .append(" AND a.salsaProjectId = @project_id AND b.salsaProjectId = @project_id;")
                            .toString();
            executeSQL(s);

            executeSQL("SELECT @cntold := @cntnew;");
            executeSQL("SELECT @cntold := @cntnew;");
            executeSQL("SELECT @cntnew := COUNT(*) FROM " + transitiveTable + ";");
            if (findCntNew() == findCntOld()) {
                break;
            }
        }
        executeSQL("--  ============== Populate " + transitiveTable + ": End");
    }

    protected class SQLFactExtractorScanner extends BasicJavaExtractor.FactExtractorScanner {
        protected void initialize() {
            writeSalsaProject();
        }

        // At the end of the tree walk, we wil likely have a set of fields,
        // methods, classes, and packages that have been "seen" but not
        // visited (as they are defined in other source trees).  Lucky for
        // us, the symbols for these entities are created by the compiler
        // from jar files that were linked in (or else javac would have
        // complained).  We need to dump the facts about these entities to
        // have a semantically complete factbase.
        public void finishUp() {
            super.finishUp();    //To change body of overridden methods use File | Settings | File Templates.

            writeCallTree();
        }

        private void writeCallTree() {
            Iterator<CallTree> stacks = calls.values().iterator();
            while ( stacks.hasNext()) {
                CallTree callTree = stacks.next();
                writeCallTreeFacts(callTree, new ArrayList<Symbol.MethodSymbol>());
            }
        }

        void writeCallTreeFacts(CallTree callTree, List<Symbol.MethodSymbol> callPath) {
            if ( callTree.callees.isEmpty() || callPath.contains(callTree.caller)) {
                callPath.add(callTree.caller);
                Symbol.MethodSymbol firstMethod = callPath.get(0);

                String parentMethod = getMD5(getMethodName(firstMethod));

                Iterator<Symbol.MethodSymbol> callPathItr = callPath.iterator();

                Set<Symbol> classSymbols = new HashSet<Symbol>();

                StringBuffer callPathStr = new StringBuffer(5*1024);
                for ( int i = 0; i < callPath.size()-1; i++ ) {
                    Symbol.MethodSymbol callerSym, calleeSym;
                    callerSym = callPath.get(i);
                    calleeSym = callPath.get(i+1);

                    String callerName = callerSym.owner.name.toString() + "." + callerSym.name;
                    String calleeName = calleeSym.owner.name.toString() + "." + calleeSym.name;
                    if ( i > 0 )
                        callPathStr.append(":").append(calleeName);
                    else
                        callPathStr.append(callerName).append(":").append(calleeName);

                    if ( !classSymbols.contains(calleeSym.owner))
                        classSymbols.add(calleeSym.owner);

                    if ( !classSymbols.contains(callerSym.owner))
                        classSymbols.add(callerSym.owner);
                }
                String table = "JCallpath";
                String path = callPathStr.toString();
                executeSQL(REPLACE + table + "(id, name, parentMethod, salsaProjectId) VALUES (" +
                        getMD5(path) + COMMA + quoted(path) + COMMA + parentMethod + COMMA + " @project_id);");

                Iterator<Symbol> classSymbolsItr = classSymbols.iterator();
                while ( classSymbolsItr.hasNext()) {
                    Symbol sym = classSymbolsItr.next();
                    writeCallPathClasses(path, sym.fullName().toString());
                }

                callPath.clear();
            }
            else {
                callPath.add(callTree.caller);
                Iterator <CallTree> callsItr = callTree.callees.values().iterator();
                while ( callsItr.hasNext()) {
                    CallTree subCallTree = callsItr.next();
                    List<Symbol.MethodSymbol> subCallPath =
                                new ArrayList<Symbol.MethodSymbol>(callPath);
                    writeCallTreeFacts(subCallTree, subCallPath);
                }
            }
        }

        private void writeCallPathClasses(String callpath, String className) {
            String table = "JCallpath_classes";
            executeSQL(REPLACE + table + "(callpaths, classes, salsaProjectId) VALUES (" +
                    getMD5(callpath) + COMMA + getMD5(className) + " , @project_id) ;");

        }

        protected void writeSalsaProject() {
            executeSQL(new StringBuffer().append(INSERT)
                            .append(SalsaProject)
                            .append(" (system, project, version, source) values(")
                            .append(quoted(projectName))
                            .append(COMMA).append(quoted(projectName))
                            .append(COMMA)
                            .append(quoted(projectVersion))
                            .append(COMMA)
                            .append(quoted(projectSource))
                            .append(");")
                            .toString());
            executeSQL(new StringBuffer().append("SELECT @project_id := id FROM ")
                            .append(SalsaProject)
                            .append(" WHERE project = ")
                            .append(quoted(projectName))
                            .append(" AND version = ")
                            .append(quoted(projectVersion))
                            .append(";")
                            .toString());
        }

        protected void writeNonClassTypeFact(String typeName) {
            executeSQL(REPLACE + JNonClassType
                    + " (id, name, shortName, salsaProjectId) values("
                    + getMD5(typeName) + COMMA + quoted(typeName) + COMMA + quoted(sname(typeName))
                    + ", @project_id"
                    + ");");
        }

        protected void writeInitializerMethodFact(final String fullPseudoMethodNameString,
                                                  final String ownerName, boolean isStatic) {
            String signature = "void " + getSignature(fullPseudoMethodNameString);
            String paramSignature = getParamSignature(fullPseudoMethodNameString);
            String typeSignature = "void " + paramSignature;

            StringBuffer buf = new StringBuffer();
            buf.append(REPLACE)
                    .append(JMethod)
                    .append("(id, name, shortName, isAbstract, isConstructor, isStatic, isFinal, numParams, accessibility, ")
                    .append("filePosition, parentClass,returnType, signature, typeSignature, paramSignature, ");

            if (methodCounters.hasSources) {
                buf.append("numStatements, ")
                        .append("numLocalVariables, ")
                        .append("numTryClauses, ")
                        .append("numCatchClauses, ")
                        .append("numCalls, ")
                        .append("numInstantiations, ")
                        .append("numAnonClassInstantiations, ")
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
                    .append(getMD5(fullPseudoMethodNameString))
                    .append(COMMA).append(quoted(fullPseudoMethodNameString))
                    .append(COMMA).append(quoted(sname(fullPseudoMethodNameString)))
                    .append(COMMA).append("false")
                    .append(COMMA).append("false")
                    .append(COMMA).append(isStatic ? "true" : "false")
                    .append(COMMA).append("false")
                    .append(COMMA).append("0")
                    .append(COMMA).append("'private'")
                    .append(COMMA).append(defaultPosition)
                    .append(COMMA).append(getMD5(ownerName))
                    .append(COMMA).append(getMD5("void"))
                    .append(COMMA).append(quoted(signature))
                    .append(COMMA).append(quoted(typeSignature))
                    .append(COMMA).append(quoted(paramSignature));
            if (methodCounters.hasSources) {
                buf.append(COMMA + methodCounters.numStatements)
                        .append(COMMA + methodCounters.numLocalVariables)
                        .append(COMMA + methodCounters.numTryClauses)
                        .append(COMMA + methodCounters.numCatchClauses)
                        .append(COMMA + methodCounters.numCalls)
                        .append(COMMA + methodCounters.numInstantiations)
                        .append(COMMA + methodCounters.numAnonClassInstantiations)
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

            executeSQL(buf.toString());
        }

        protected void writeMethodInfoFact(final String methodName, final String className,
                                           final String returnTypeName, final String shortName,
                                           final String sAccessibility, final String sIsStatic,
                                           final String sIsFinal, final String sIsAbstract, final String sIsConstructor,
                                           final String sIsSynchronized, final String sContainsSynchBlock,
                                           Type.MethodType methodType,
                                           String lineNum) {
            String signature = returnTypeName + " " + getSignature(methodName);
            String paramSignature = getParamSignature(methodName);
            String typeSignature = returnTypeName + " " + paramSignature;
            StringBuffer buf = new StringBuffer();
            buf.append(REPLACE)
                    .append(JMethod)
                    .append("(id, name, shortName,isAbstract, isConstructor, isStatic, isFinal, numParams, accessibility,")
                    .append("filePosition, parentClass, returnType, signature, typeSignature, paramSignature, ");

            if (methodCounters.hasSources) {
                buf.append("numStatements, ")
                        .append("numLocalVariables, ")
                        .append("numTryClauses, ")
                        .append("numCatchClauses, ")
                        .append("numCalls, ")
                        .append("numInstantiations, ")
                        .append("numAnonClassInstantiations, ")
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
                    .append(getMD5(methodName))
                    .append(COMMA).append(quoted(methodName))
                    .append(COMMA).append(quoted(shortName))
                    .append(COMMA).append(sIsAbstract)
                    .append(COMMA).append(sIsConstructor)
                    .append(COMMA).append(sIsStatic)
                    .append(COMMA).append(sIsFinal)
                    .append(COMMA).append(methodType.argtypes().length())
                    .append(COMMA).append(quoted(sAccessibility))
                    .append(COMMA).append(lineNum)
                    .append(COMMA).append(getMD5(className))
                    .append(COMMA).append(getMD5(returnTypeName))
                    .append(COMMA).append(quoted(signature))
                    .append(COMMA).append(quoted(typeSignature))
                    .append(COMMA).append(quoted(paramSignature));

            if (methodCounters.hasSources) {
                buf.append(COMMA + methodCounters.numStatements)
                        .append(COMMA).append(methodCounters.numLocalVariables)
                        .append(COMMA).append(methodCounters.numTryClauses)
                        .append(COMMA).append(methodCounters.numCatchClauses)
                        .append(COMMA).append(methodCounters.numCalls)
                        .append(COMMA).append(methodCounters.numInstantiations)
                        .append(COMMA).append(methodCounters.numAnonClassInstantiations)
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
            executeSQL(buf.toString());
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
                executeSQL(new StringBuffer().append(REPLACE)
                                .append(JPackage)
                                .append("(id, name, shortName, parentPackage, salsaProjectId) VALUES(")
                                .append(getMD5(packageName))
                                .append(COMMA)
                                .append(quoted(packageName))
                                .append(COMMA)
                                .append(quoted(shortName))
                                .append(COMMA)
                                .append(getMD5(ownerName))
                                .append(", @project_id")
                                .append(");")
                                .toString());
                if (null != ownerName) {
                    executeSQL(new StringBuffer().append(REPLACE)
                                    .append(JPackage_PackagesTransitive)
                                    .append("(packages, parentPackage, salsaProjectId) VALUES(")
                                    .append(getMD5(packageName))
                                    .append(COMMA)
                                    .append(getMD5(ownerName))
                                    .append(", @project_id")
                                    .append(");")
                                    .toString());
                    //                    makeTransitiveClosure(setMD5(ownerName), setMD5(packageName), "JPackage_Packages", "parentPackage", "packages");
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
                        .append("numAnonClassInstantiations, ")
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
                    .append(getMD5(className))
                    .append(COMMA).append(quoted(className))
                    .append(COMMA).append(quoted(shortName))
                    .append(COMMA).append(sIsStatic)
                    .append(COMMA).append(sIsFinal)
                    .append(COMMA).append(sIsInner)
                    .append(COMMA).append(anInterface ? "true" : "false")
                    .append(COMMA).append(quoted(sAccessibility))
                    .append(COMMA).append(quoted(getAbsoluteFileName(sourceFileName)))
                    .append(COMMA).append(lineNum)
                    .append(COMMA).append(getMD5(classOwnerName))
                    .append(COMMA).append(getMD5(packageOwnerName));

            if (classCounters.hasSources) {
                buf.append(COMMA).append(classCounters.numStatements)
                        .append(COMMA).append(classCounters.numLocalVariables)
                        .append(COMMA).append(classCounters.numTryClauses)
                        .append(COMMA).append(classCounters.numCatchClauses)
                        .append(COMMA).append(classCounters.numCalls)
                        .append(COMMA).append(classCounters.numInstantiations)
                        .append(COMMA).append(classCounters.numAnonClassInstantiations)
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

            executeSQL(buf.toString());
            // Process transitive closure of class ----(contains)----> inner_class
            if (null != classOwnerName) {
                String table = JClass_ContainsClasses;
                String b = getMD5(classOwnerName);
                String a = getMD5(className);
                executeSQL(REPLACE + table + Transitive + "(containsClasses, containingClass) VALUES(" + a + COMMA
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
            String b = getMD5(className);
            String a = getMD5(intrfaceName);

            if (anInterface) {
                writeClassExtendsFact(className, intrfaceName);
            } else {
                executeSQL(REPLACE + table + "(implementsInterfaces, implementingClasses, salsaProjectId) VALUES(" + a
                        + COMMA
                        + b
                        + ", @project_id);");
                executeSQL(REPLACE + table + Transitive
                        + "(implementsInterfaces, implementingClasses, salsaProjectId) VALUES("
                        + a
                        + COMMA
                        + b
                        + ", @project_id);");
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
            String b = getMD5(className);
            String a = getMD5(inheritanceParentName);
            executeSQL(UPDATE + JClass + " SET extendsClass = " + a + " WHERE id = " + b + ";");
            executeSQL(REPLACE + JClass_ExtendsClass + Transitive
                    + "(extendsClass, extendingClasses, salsaProjectId) VALUES ("
                    + a
                    + COMMA
                    + b
                    + ", @project_id);");
            //makeTransitiveClosure(a, b, JClass_ExtendsClass, "extendsClass", "extendingClasses");
        }

        protected void writeParameterInfoFact(final String paramName, final String methodName,
                                              final String paramTypeName,
                                              final String shortParamName, int paramIndex) {
            executeSQL(new StringBuffer().append(REPLACE)
                            .append(JParameter)
                            .append("(id, name, shortName, parameterIndex, method, type, salsaProjectId")
                            .append(") VALUES (")
                            .append(getMD5(paramName))
                            .append(COMMA)
                            .append(quoted(paramName))
                            .append(COMMA)
                            .append(quoted(shortParamName))
                            .append(COMMA)
                            .append(paramIndex)
                            .append(COMMA)
                            .append(getMD5(methodName))
                            .append(COMMA)
                            .append(getMD5(paramTypeName))
                            .append(", @project_id")
                            .append(");")
                            .toString());
        }

        protected void writeFieldInfoFact(final String varName, final String className, final String typeName,
                                          final String shortName, final String sAccessibility, final String sIsStatic,
                                          final String sIsFinal, String lineNum) {
            executeSQL(new StringBuffer().append(REPLACE)
                            .append(JField)
                            .append("(id, name, shortName, isStatic, isFinal, accessibility,")
                            .append("filePosition, parentClass, type, salsaProjectId")
                            .append(") VALUES (")
                            .append(getMD5(varName))
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
                            .append(getMD5(className))
                            .append(COMMA)
                            .append(getMD5(typeName))
                            .append(", @project_id")
                            .append(");")
                            .toString());
        }

        protected void writeMethodCallFact(Symbol.MethodSymbol callerSym, Symbol.MethodSymbol calleeSym) {

            CallTree caller = calls.get(callerSym);
            if ( caller == null ) {
                caller = new CallTree(callerSym);
                calls.put(callerSym, caller);
            }

            CallTree callee = calls.get(calleeSym);
            if ( callee == null ) {
                callee = new CallTree(calleeSym);
                calls.put(calleeSym, callee);
            }

            caller.addMethodCall(callee);
        }

        protected void writeMethodCallsFact(final String callerName, final String calleeName, final int line,
                                            final int column) {
            // TODO: Column number is ignored for now add it to the database schema


            String b = getMD5(callerName);
            String a = getMD5(calleeName);
            String table = JMethod_Calls;
            executeSQL(REPLACE + table + "(calls, callers, filePosition, salsaProjectId) VALUES (" + a + COMMA + b
                    + COMMA
                    + line
                    + ", @project_id);");
            executeSQL(REPLACE + table + Transitive + "(calls, callers, filePosition, salsaProjectId) VALUES (" + a
                    + COMMA
                    + b
                    + COMMA
                    + line
                    + ", @project_id);");
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
            String a = getMD5(instantiatorMethodName);
            String b = getMD5(instantiateeClassName);
            String table = JMethod_Instantiates;
            executeSQL(new StringBuffer().append(REPLACE)
                            .append(table)
                            .append("(instantiatedBy, instantiates, filePosition, salsaProjectId) VALUES (")
                            .append(a)
                            .append(COMMA)
                            .append(b)
                            .append(COMMA)
                            .append(line)
                            .append(", @project_id);")
                            .toString());
        }

        protected void writeMethodCatchesExceptionFact(final String catcherMethodName, final String exceptionName,
                                                       final int line, final int column) {
            // TODO: Column number is ignored for now; add it to the database schema later
            executeSQL(new StringBuffer().append(REPLACE)
                            .append(JMethod_CatchesExceptions)
                            .append("(catchesExceptions, caughtBy, filePosition, salsaProjectId) VALUES (")
                            .append(getMD5(exceptionName))
                            .append(COMMA)
                            .append(getMD5(catcherMethodName))
                            .append(COMMA)
                            .append(line)
                            .append(", @project_id);")
                            .toString());
        }

        protected void writeClassThrowsExceptionFact(final String throwerMethodName, final String exceptionName,
                                                     final int line, final int column) {
            // TODO: Column number is ignored for now; add it to the database schema later
            executeSQL(new StringBuffer().append(REPLACE)
                            .append(JMethod_ThrowsExceptions)
                            .append("(throwsExceptions, thrownBy, filePosition, salsaProjectId) VALUES (")
                            .append(getMD5(exceptionName))
                            .append(COMMA)
                            .append(getMD5(throwerMethodName))
                            .append(COMMA)
                            .append(line)
                            .append(", @project_id);")
                            .toString());
        }

        protected void writeMethodUsesFieldFact(final String userName, final String useeName,
                                                final int line, final int column) {
            // TODO: Column number is ignored for now; add it to the database schema later
            executeSQL(new StringBuffer().append(REPLACE)
                            .append(JMethod_UsedFields)
                            .append("(usedFields, usedByMethods, filePosition, salsaProjectId) VALUES (")
                            .append(getMD5(useeName))
                            .append(COMMA)
                            .append(getMD5(userName))
                            .append(COMMA)
                            .append(line)
                            .append(", @project_id);")
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
                    parentMethod = getMD5(ownerName);
                    break;
                case FIELD_ANNOTATION:
                    parentField = getMD5(ownerName);
                    break;
                case CLASS_ANNOTATION:
                    parentClass = getMD5(ownerName);
                    break;
                case PARAMETER_ANNOTATION:
                    parentParameter = getMD5(ownerName);
                    break;
                default:
                    throw new IllegalArgumentException("Illegal argument 'kind' = " + kind);
            }
            // TODO: Column number is ignored for now add it to the database schema
            executeSQL(new StringBuffer().
                    append(REPLACE).
                            append(JAnnotation).
                            append("(id, name, parentClass, parentMethod, parentField, parentParameter, type, shortName, salsaProjectId")
                            .append(") VALUES (")
                            .append(getMD5(annName))
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
                            .append(getMD5(annTypeName))
                            .append(COMMA)
                            .append(quoted(getShortName(annName)))
                            .append(", @project_id")
                            .append(");")
                            .toString());
        }

        protected void writeAnnotationShortNameAndNumArgumentsFact(final String annName,
                                                                   String shortName, int argcount) {
            executeSQL(new StringBuffer().append(UPDATE)
                            .append(JAnnotation)
                            .append(" SET ")
                            .append("shortName = ")
                            .append(quoted(shortName))
                            .append(COMMA)
                            .append("numArgs = ")
                            .append(argcount)
                            .append(" WHERE id = ")
                            .append(getMD5(annName))
                            .append(";")
                            .toString());
        }

        protected void writeAnnotationArgumentInstanceFact(String argName, final String annName, String type) {
            executeSQL(new StringBuffer().append(REPLACE)
                            .append(JAnnotationArgument)
                            .append("(id, name, annotation, type, salsaProjectId")
                            .append(") VALUES (")
                            .append(getMD5(argName))
                            .append(COMMA)
                            .append(quoted(argName))
                            .append(COMMA)
                            .append(getMD5(annName))
                            .append(COMMA)
                            .append(getMD5(type))
                            .append(", @project_id")
                            .append(");")
                            .toString());
        }

        protected void writeAnnotationArgumentShortNameAndStringValueFact(String argName,
                                                                          String shortName, String stringValue) {
            stringValue = stringValue.replace("\\r", "\r");
            stringValue = stringValue.replace("\\n", "\n");

            try {
                if (null == statement_JAnnotationArgument) {
                    String sql = new StringBuffer().append(UPDATE).append(JAnnotationArgument).
                                    append(" SET shortName = ?, stringValue = ? WHERE id = ?").toString();
                    statement_JAnnotationArgument = databaseConnection.prepareStatement(sql);
                }
                int i = 1;
                PreparedStatement statement = statement_JAnnotationArgument;
                statement.setString(i++, shortName);
                statement.setString(i++, stringValue);
                statement.setInt(i++, Integer.parseInt(getMD5(argName)));
                statement.execute();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            executeSQL(new StringBuffer().append(UPDATE)
                            .append(JAnnotationArgument)
                            .append(" SET ")
                            .append("shortName = ")
                            .append(quoted(shortName))
                            .append(COMMA)
                            .append("stringValue = ")
                            .append(quoted(stringValue))
                            .append(" WHERE id = ")
                            .append(getMD5(argName))
                            .append(";")
                            .toString()
                , true);
        }

    }

    static class CallTree {
        public CallTree(Symbol.MethodSymbol caller) {
            this.caller = caller;
        }
        private Symbol.MethodSymbol caller;

        void addMethodCall(CallTree tree) {
            callees.put(tree.caller, tree);
        }

        Map<Symbol.MethodSymbol, CallTree> callees = new HashMap<Symbol.MethodSymbol, CallTree>();
    }

    Map<Symbol.MethodSymbol, CallTree> calls = new HashMap<Symbol.MethodSymbol, CallTree>();

    private void printTree(Map<String, CallTree>tree) {
        Iterator<String> names = tree.keySet().iterator();
        while ( names.hasNext() ) {
            String name = names.next();
            CallTree calls = tree.get(name);
            print(calls, null);
        }
    }

    static void printRoot(CallTree calls) {
        System.out.print(":"+calls.caller);

    }

    static void print(CallTree callTree, List<Symbol.MethodSymbol> callPath) {
        if ( callTree.callees.isEmpty() || callPath.contains(callTree.caller)) {
            System.out.print(":"+callTree.caller);
            callPath.add(callTree.caller);
            System.out.println("Call Path" + callPath);
            callPath.clear();
        }
        else {
            Iterator <CallTree> callsItr = callTree.callees.values().iterator();
            while ( callsItr.hasNext()) {
                System.out.print(":"+callTree.caller);
                callPath.add(callTree.caller);
                CallTree subTree = callsItr.next();
                print(subTree, callPath);
                System.out.println();

            }
        }
    }


}
