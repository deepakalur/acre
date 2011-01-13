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
package org.acre.lang.pql.translator;

import ca.uwaterloo.cs.ql.interp.*;
import org.acre.lang.QueryPreProcessor;
import org.acre.lang.TreeBuilder;
import org.acre.lang.analyser.DMLStatement;
import org.acre.lang.analyser.QueryModel;
import org.acre.lang.analyser.SQLAnalyser;
import org.acre.lang.analyser.ScriptWalker;
import org.acre.lang.application.ASTPrinter;
import org.acre.lang.lexer.LexerException;
import org.acre.lang.node.*;
import org.acre.lang.parser.ParserException;
import org.acre.lang.runtime.PQL;
import org.acre.lang.runtime.QLAdaptor;
import org.acre.lang.tool.PrintWalker;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

/**
 * Test Main class for code generator
 * 
 * @author Syed Ali
 */

public class Main {

    public static void help() {
        System.out.println("Usage: java org.acre.lang.pql.translate.Main [options] <action> <pql files>");
        System.out.println();
        System.out.println("where options include:");
        System.out.println("    -debug      runs in debug mode");
        System.out.println("    -? -help    print this help message");
        System.out.println("    -database   <path to database file>");
        System.out.println("where action can be one of the following:");
        System.out.println("    -targetQL   output QL. Default is targetQL when ommitted");
        System.out.println("    -targetSQL  output SQL");
        System.out.println("    -printtree  parsers and dumps AST tree for all the queries in pql files");
        System.out.println("    -printtree  parsers and dumps AST tree for all the queries in pql files");
        System.out.println("    -showtree   parsers and dumps different AST tree for all the queries in pql files");
        System.out.println("    -parse      parses the pql queries in pql files");
        System.out.println("    -analyse    analyses and dumps output for all the queries in pql files");
        System.out.println("    -sqlanalyse analyses & tranforms all the queries in pql files and dumps the SQL datastructure");
        System.out.println("    -translate  dumps the translated ql for all the queries in pql files");
        System.out.println("    -run        runs pql files and returns results. Requires -database options");
        System.out.println("    -run1       runs queries in pql files one by one. Requires -database options");
        System.exit(1);
    }

    public static boolean parseFile(BufferedReader preql, String database, String action, boolean debug, boolean generateSQL) throws IOException, LexerException, ParserException {
        if (false) {
        } else if ("showtree".equalsIgnoreCase(action)) {
            Node ast = TreeBuilder.getNode(preql, true); //simple tree
            ast.apply(new ASTPrinter());
        } else if ("printtree".equalsIgnoreCase(action)) {
            Node ast = TreeBuilder.getNode(preql, true); //simple tree
            ast.apply(new PrintWalker());
        } else if ("parse".equals(action)) {
            // already done by TreeBuilder.getNode(...)
            Node ast = TreeBuilder.getNode(preql, true); //simple tree
        } else if ("run1".equalsIgnoreCase(action)) {
            Node ast = TreeBuilder.getNode(preql, true); //simple tree
            QLAdaptor reqlShell = new QLAdaptor();
            ScriptWalker qw = new ScriptWalker();
            ast.apply(qw);
            List commands = qw.getModel().getCommands();
            StringBuffer ql = new StringBuffer();
            for (int i = 0; i < commands.size(); i++) {
                String query;
                if (commands.get(i) instanceof QueryModel) {
                    QueryModel element = (QueryModel) commands.get(i);
                    QueryGenerator gq = new QueryGenerator(element);
                    query = gq.generate();
                } else {
                    query = commands.get(i).toString();
                }
                System.out.println(query);
                reqlShell.evaluateNative(query);
            }
            System.out.println("@@@@@@@@@@@@@@@@@@@@");
            System.out.println(ql);
            System.out.println("@@@@@@@@@@@@@@@@@@@@");
            // run ql string

        } else if ("run".equalsIgnoreCase(action)) {
            PQL pql;
            if (generateSQL) {
                pql = PQL.createDatabasePQL("default");
                pql.getAdaptor().connect();
            } else {
                if (database == null) {
                    throw new IllegalArgumentException("-run requires database to be specified. For more information tyr -help");
                }
                pql = PQL.createQLPQL(database);
            }
            try {
                String line;
                StringBuffer buffer = new StringBuffer();
                while ((line = preql.readLine()) != null) {
                    buffer.append(line).append('\n');
                }
                Map pqlResult = pql.executePQL(buffer.toString());
//                if (pqlResult instanceof PQLResultSet) {
//                    System.out.println(pqlResult);
//                } else if (pqlResult instanceof Map) {
//                    Map m = (Map) pqlResult;
                    for (Iterator iter = pqlResult.entrySet().iterator(); iter.hasNext();) {
                        Map.Entry element = (Map.Entry) iter.next();
                        System.out.println("[[[ Start " + element.getKey() + "]]]");
                        System.out.println(element.getValue());
                        System.out.println("[[[ End " + element.getKey() + "]]]");
                    }
//                } else {
//                    System.out.println(pqlResult);
//                }
            } finally {
                pql.getAdaptor().disconnect();
            }
        } else if ("analyse".equals(action) || "sqlanalyse".equals(action) || "translate".equals(action)) {
            PQL.createDatabasePQL("default");
            Node ast = TreeBuilder.getNode(preql, true); //simple tree
            ScriptWalker qw = new ScriptWalker(generateSQL);
            ast.apply(qw);
            List commands = qw.getModel().getCommands();
            Properties p = null;
            for (int i = 0; i < commands.size(); i++) {
                if (commands.get(i) instanceof QueryModel) {
                    QueryModel element = (QueryModel) commands.get(i);
                    if ("translate".equals(action)) {
                        if (generateSQL) {
                            SQLAnalyser gq = new SQLAnalyser(element, qw.getModel());
                            if (p != null) {
                                // gq.setEnvironmentVariables(p);
                                // TODO - FIXME
                            }
                            List s = gq.getSQLStatements();
                            if (debug) 
                                for (Iterator iter = s.iterator(); iter.hasNext();) {
                                    DMLStatement st = (DMLStatement) iter.next();
                                    System.out.println(st.getSql());
                                }
                            //if (debug) System.out.println(gq.generate());
                        } else {
                            QueryGenerator gq = new QueryGenerator(element, qw.getModel());
                            if (debug) System.out.println(gq.generate());
                        }
                    } else if ("sqlanalyse".equals(action)) {
                        SQLAnalyser gq = new SQLAnalyser(element, qw.getModel());
                        if (debug) System.out.println("--- PQL ---");
                        if (debug) System.out.println(element);
                        if (debug) System.out.println("--- SQL ---");
                        if (debug) System.out.println(gq.constructSQLQuery(null));
                    } else {
                        if (debug) System.out.println(element);
                    }
                } else if (commands.get(i) instanceof Properties) {
                    p = (Properties) commands.get(i);
                } else {
                    if (debug) System.out.println(commands.get(i).toString());
                }
                if (debug) System.out.println();
            }
            if (debug) {
                String retStr = null;
                for (Iterator iter = qw.getModel().getReturnVariables().iterator(); iter.hasNext();) {
                    String element = (String) iter.next();
                    retStr = (retStr == null) ? "return " + element : retStr + ", " + element;
                }
                if (retStr != null) {
                    retStr += ";";
                    System.out.println(retStr);
                }
            }
        } else {
            err("Unknown option: -" + action);
        }

        return true;
    }

    public static void main(String[] args) {
        fileBasedMain(args);
//        lineBasedMain(args);
    }
    public static void fileBasedMain(String[] args) {
        boolean runResult = true;
        String line;
        String database = null;
        boolean generateSQL = false;
        try {
            BufferedReader br;
            String action = null;
            boolean debug = true;
            List options = new ArrayList();
            List files = new ArrayList();
            for (int i = 0; i < args.length; i++) {
                if (args[i].startsWith("-")) {
                    if ("-help".equalsIgnoreCase(args[i])) {
                        help();
                    } else if ("-nodebug".equalsIgnoreCase(args[i])) {
                        debug = false;
                    } else if ("-database".equalsIgnoreCase(args[i])) {
                        database = args[++i];
                    } else if ("-targetSQL".equalsIgnoreCase(args[i])) {
                        generateSQL = true;
                    } else {
                        options.add(args[i].substring(1));
                    }
                } else {
                    files.add(args[i]);
                }
            }
            if (options.size() == 0) {
                err("No option specified");
            }
            if (files.size() == 0) {
                err("No file specified");
            }
            for (int i = 0; i < options.size(); i++) {
                String option = (String) options.get(i);
                for (int j = 0; j < files.size(); j++) {
                    String file = (String) files.get(j);
                    System.out.println("Processing ("+option+") file " + file + " ...");

                    Scanner scanner = new Scanner(new FileInputStream(file));
                    scanner.useDelimiter("\\z");
                    StringBuffer sb = new StringBuffer(4096);
                    while ( scanner.hasNext() )
                        sb.append(scanner.next());

                    String query = sb.toString();
                    QueryPreProcessor preProcessor = new QueryPreProcessor();
                    query = preProcessor.compile(query);

                    br = new BufferedReader(new StringReader(query));
                    parseFile(br, database, option, debug, generateSQL);
                }
            }
//            if (args.length > 1) {
//                System.out.println("Reading file " + args[1] + " ...");
//                br = new BufferedReader(new InputStreamReader(new FileInputStream(args[1])));
//            } else {
//                System.out.println("Reading resource Test.ql...");
//                br = new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream("Test.ql")));
//            }
//            while ((line = br.readLine()) != null) {
//                line = line.trim();
//                if (line.length() > 0 && !line.startsWith("#")) { //$NON-NLS-1$
//                    System.out.println("________________________"); //$NON-NLS-1$
//                    System.out.println(line);
//                    System.out.println("------------------------"); //$NON-NLS-1$
//                    runResult = runResult && parse(line, action);
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
            runResult = false;
        }
        if (runResult) {
            System.out.println(
                "+++++++++++++++++++ Test Successful: parsed and semantically analysed the queries in src/main/org/acre/lang/analyser/Test.ql file");
        } else {
            System.out.println(
                "+++++++++++++++++++ Test Failed: One or more tests failed while parsing and semantically analysing the queries in src/main/org/acre/lang/analyser/Test.ql file");
        }
    }

    public static void err(String message) {
        throw new RuntimeException(message);
    }
    
    /*
    public static void lineBasedMain(String[] args) {
        System.out.println("args: " + args.length);
        boolean runResult = true;
        String line;
        try {
            BufferedReader br;
            String action = null;
            if (args.length > 0) {
                action = args[0]; 
            }
            if (args.length > 1) {
                System.out.println("Reading file " + args[1] + " ...");
                br = new BufferedReader(new InputStreamReader(new FileInputStream(args[1])));
            } else {
                System.out.println("Reading resource Test.ql...");
                br = new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream("Test.ql")));
            }
            getMetaModel();
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.length() > 0 && !line.startsWith("#")) { //$NON-NLS-1$
                    System.out.println("________________________"); //$NON-NLS-1$
                    System.out.println(line);
                    System.out.println("------------------------"); //$NON-NLS-1$
                    runResult = runResult && parseLine(line, action);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            runResult = false;
        }
        if (runResult) {
            System.out.println(
                "+++++++++++++++++++ Test Successful: parsed and semantically analysed the queries in src/main/com/sun/salsa/lang/analyser/Test.ql file");
        } else {
            System.out.println(
                "+++++++++++++++++++ Test Failed: One or more tests failed while parsing and semantically analysing the queries in src/main/com/sun/salsa/lang/analyser/Test.ql file");
        }

    }
    */
}


/*
QLAdaptor reqlShell = new QLAdaptor();
ScriptWalker qw = new ScriptWalker(getMetaModel());
ast.apply(qw);
List commands = qw.getModel().getCommands();
StringBuffer ql = new StringBuffer();
for (int i = 0; i < commands.size(); i++) {
    if (commands.get(i) instanceof QueryAnalyser) {
        QueryAnalyser element = (QueryAnalyser) commands.get(i);
        QueryGenerator gq = new QueryGenerator(element.getQuery(), getMetaModel());
        ql.append(gq.generate(i+1 == commands.size())).append(";\n");
    } else {
        ql.append(i+1 == commands.size() ? "return " : "")
            .append(commands.get(i).toString()).append(";\n");
    }
}
*/
///////TODO
