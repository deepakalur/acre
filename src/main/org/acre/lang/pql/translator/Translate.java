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

import org.acre.lang.TreeBuilder;
import org.acre.lang.analyser.*;
import org.acre.lang.lexer.LexerException;
import org.acre.lang.node.*;
import org.acre.lang.parser.ParserException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Interface class for translator
 * 
 * @author Syed Ali
 */

public class Translate {

    public static String getImport(String filePath) {
        return "getta(\"" + filePath + "\")\n";
    }

    public final static String[] PREQL_PROLOGUE = {
        "packages = $INSTANCE . {\"jPackage\"}",
        "jclasses = $INSTANCE . {\"jClass\"}",
        "interfaces = $INSTANCE . {\"jInterface\"}",
        "methods = $INSTANCE . {\"jMethod\"}",
        "fields = $INSTANCE . {\"jField\"}",
        "parameters = $INSTANCE . {\"jParameter\"}",
        "annotations = $INSTANCE . {\"jAnnotation\"}",
        "annotation_arguments = $INSTANCE . {\"jAnnotationArgument\"}",
        "classes = jclasses + interfaces",
        "instanceof = extends + implements",
    };

    public static String translate(ScriptModel script, String preql) throws IOException, LexerException, ParserException {
        List commands = parsePQL(script, preql);
        StringBuffer ql = new StringBuffer();
        for (int i = 0; i < commands.size(); i++) {
            if (commands.get(i) instanceof QueryModel) {
                QueryModel element = (QueryModel) commands.get(i);
                QueryGenerator gq = new QueryGenerator(element);
                ql.append(gq.generate()).append('\n');
            } else if (commands.get(i) instanceof Properties) {
                throw new IllegalArgumentException("Unexpected command: " + commands.get(i));
            } else {
                ql
                  //SMA: Don't know who added this line. It is obviously wrong,
                  //as QL doesn't support variable names with $ in it.
                  //.append(addDefine && i+1 == commands.size() ? "DEFINE qlReturnResult AS " : "")
                 .append(commands.get(i).toString()).append('\n');
            }
        }
        return ql.toString();
    }

    public static DMLStatement[] translateToSQL(ScriptModel script, String pql, Connection dbConnection)
            throws IOException, LexerException, ParserException, SQLException {
        List commands = parsePQL(script, pql);
        List resultList = new ArrayList();
        Properties lastEnvVars = null;
        int currentSalsaProjectId = -1;
        for (int i = 0; i < commands.size(); i++) {
            if (commands.get(i) instanceof QueryModel) {
                QueryModel element = (QueryModel) commands.get(i);
                SQLAnalyser gq = new SQLAnalyser(element);
                if (lastEnvVars != null && lastEnvVars.size() > 0) {
                    gq.setProjectIdFilter(currentSalsaProjectId);
                }
                resultList.addAll(gq.getSQLStatements());
                //ql.append(gq.generate()).append('\n');
            } else if (commands.get(i) instanceof Properties) {
                lastEnvVars = (Properties) commands.get(i);
                currentSalsaProjectId = getProjectIdFromEnvironmentVars(lastEnvVars,
                        dbConnection);
            } else {
                throw new IllegalArgumentException("Unexpected command: " + commands.get(i));
//                ql
//                  //SMA: Don't know who added this line. It is obviously wrong,
//                  //as QL doesn't support variable names with $ in it.
//                  //.append(addDefine && i+1 == commands.size() ? "DEFINE qlReturnResult AS " : "")
//                 .append(commands.get(i).toString()).append('\n');
            }
        }
        //return ql.toString();
        DMLStatement[] result = new DMLStatement[resultList.size()];
        resultList.toArray(result);
        return result;
    }

    /**
     * @param script
     * @param pql
     * @return
     * @throws IOException
     * @throws LexerException
     * @throws ParserException
     */
    public static List parsePQL(String pql) throws IOException, LexerException, ParserException {
        return parsePQL(null, pql);
    }
    
    public static List parsePQL(ScriptModel script, String pql) throws IOException, LexerException, ParserException {
        Node ast = TreeBuilder.getNode(pql, true);
        ScriptWalker qw = new ScriptWalker(script, true);
        ast.apply(qw);
        return qw.getModel().getCommands();
    }

    public static void main(String[] args) {
        String line;
        try {
            BufferedReader br;
            boolean generate = false;
            if (args.length > 0) {
                System.out.println("Reading file " + args[0] + " ...");
                br = new BufferedReader(new InputStreamReader(new FileInputStream(args[0])));
            } else {
                System.out.println("Reading resource Test.ql...");
                br = new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream("Test.ql")));
            }
            if (args.length > 1) {
                generate = "generate".equals(args[1].trim());
            }
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.length() > 0 && !line.startsWith("#")) { //$NON-NLS-1$
                    System.out.println("________________________"); //$NON-NLS-1$
                    System.out.println(line);
                    System.out.println("------------------------"); //$NON-NLS-1$
                    System.out.println(line);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    private static int getProjectIdFromEnvironmentVars(Properties envVars, Connection dbConnection) throws SQLException {
        String query = getEnvVarQuery(envVars);
        System.out.println("FilterQuery: " + query);
        return getProjectId(query, dbConnection);
    }

    private static String getEnvVarQuery(Properties envVars) {
        if (envVars == null || envVars.size() < 1) {
            throw new IllegalArgumentException("No environment variables found: " + envVars);
        }
        StringBuffer result = new StringBuffer("SELECT id FROM SalsaProject ");
        boolean andWhere = false;
        for (Iterator iter = envVars.entrySet().iterator(); iter.hasNext();) {
            Map.Entry element = (Map.Entry) iter.next();
            String key = (String) element.getKey();
            String value = (String) element.getValue();
            if ("system".equalsIgnoreCase(key)) {
                if (!"global".equalsIgnoreCase(value)) {
                    andWhere = addWhereClause(result, andWhere, " system = '" + value +"'");
                }
            } else if ("version".equalsIgnoreCase(key)) {
                if ("latest".equalsIgnoreCase(value)) {
                    andWhere = addWhereClause(result, andWhere,
                            " extraction_time IN (SELECT MAX(extraction_time) FROM SalsaProject GROUP BY system)");
                } else {
                    andWhere = addWhereClause(result, andWhere, " version = '" + value + "'");
                }
            } else {
                andWhere = addWhereClause(result, andWhere, " "+key+" = '" + value +"'");
            }
        }
        return result.toString();
    }

    private static boolean addWhereClause(StringBuffer query, boolean andWhere, String where) {
        if (andWhere) {
            query.append(" AND ");
        } else {
            query.append(" WHERE ");
            andWhere = true;
        }
        query.append(where);
        return andWhere;
    }

    private static int getProjectId(String envFilterSql, Connection dbConnection) throws SQLException {
            PreparedStatement stmt = null;
            try {
                stmt = dbConnection.prepareStatement(envFilterSql);
                ResultSet rs = stmt.executeQuery();
                int projectId = -1;
                if ( rs.next() ) {
                    projectId = rs.getInt(1);
                }
                return projectId;

            } finally {
                try {
                    if (stmt != null) stmt.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
    }


}
