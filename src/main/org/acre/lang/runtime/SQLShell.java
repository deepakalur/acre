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
package org.acre.lang.runtime;

import org.acre.lang.analyser.AnalyserException;
import org.acre.lang.analyser.DMLStatement;
import org.acre.lang.analyser.NotYetImplementedException;
import org.acre.lang.analyser.ScriptModel;
import org.acre.lang.lexer.LexerException;
import org.acre.lang.parser.ParserException;
import org.acre.lang.pql.translator.Translate;

import java.io.*;
import java.sql.SQLException;

/**
 * Shell for interactive interpreter for PQL that goes to database
 *  
 * @author Syed Ali
 */                          
public class SQLShell {

    public static class Env {
        public InputStream in  = System.in;
        public PrintStream out = System.out;
        public PrintStream err = System.err;
    }
    private final static int SQL_MODE = 1;
    private final static int PQL_MODE = 2;
    private int mode = PQL_MODE;
    private int debug = 0;
    private Env env;
    private PQL pql;
    private ScriptModel scriptModel = new ScriptModel();

    public SQLShell(String dbDriver, String dbUrl, String dbUser, String dbPassword) {
        env = new Env();
        pql = PQL.createDatabasePQL(dbDriver, dbUrl, dbUser, dbPassword);
    }


    public void shellEvaluate() {
        String line;

        pql.getAdaptor().connect();
        InputStreamReader reader = new InputStreamReader(env.in);
        BufferedReader bufReader = new BufferedReader(reader);
        boolean done = false;
        while (!done) {
            env.out.print(getModePrompt() + "> ");
            try {
                line = bufReader.readLine();
                if (line == null || line.trim().length() == 0) {
                    continue;
                } else {
                    line = line.trim();
                    if (isKeyword(line.toLowerCase())) {
                        done = processKeyword(line.toLowerCase());
                        continue;
                    }
                    evaluate(line);
                }
            } catch (IOException e) {
                continue;
            }
        }
        env.out.println();
        freeEnv();
    }
    
    private void freeEnv() {
        //env.popScope();
    }
    private boolean processKeyword(String line) {
        if (line.equals("?") || line.equals("help")) {
            if (mode == SQL_MODE) {
                //help for pql
            } else {
                env.out.println("  Command       Meaning");
                env.out.println("  -----------   ----------------");
                env.out.println("  help or ?     prints this message");
                env.out.println("  sql            switch to SQL mode");
                env.out.println("  pql           switch to PQL mode");
                env.out.println("  load <path>   load database");
                env.out.println("  debugon       switch to debugging on");
                env.out.println("  debugon2      switch to level 2 debugging");
                env.out.println("  debugoff      switch to debugging off");
                env.out.println("  exit          exit interactive interpreter");
                env.out.println();
            }
        } else if (line.equals("preql") || line.equals("pql")) {
            mode = PQL_MODE;
        } else if (line.equals("sql")) {
            mode = SQL_MODE;
        } else if (line.equals("debugon")) {
            debug = 1;
//            ((DatabaseAdapter)pql.getAdaptor()).setDebug(debug);
            printDebugStaus();
        } else if (line.equals("debugon2")) {
            debug = 2;
            printDebugStaus();
        } else if (line.equals("debugoff")) {
            debug = 0;
            printDebugStaus();
        } else if (line.equals("exit") || line.equals("bye") || line.equals("quit")) {
            pql.getAdaptor().disconnect();
            return true;
        } else {
        }
        return false;
    }
    private boolean isKeyword(String line) {
        if (line.equals("?")
            || line.equals("help")
            || line.equals("print")
            || line.equals("pql")
            || line.equals("preql")
            || line.equals("sql")
            || line.startsWith("load")
            || line.equals("debugon")
            || line.equals("debugon2")
            || line.equals("debugoff")
            || line.equals("exit")
            || line.equals("bye")
            || line.equals("quit")) {
            return true;
        } else {
            return false;
        }
    }

    
    public void evaluate(String line) throws IOException {
        try {
            if (line != null && line.length() > 0) {
                if (mode == SQL_MODE) {
                    pql.executeNative(line);
                } else {
                    if (line.toUpperCase().startsWith("SQL:")) {
                        pql.executeNative(line.substring(5));
                    } else {
                        scriptModel.flushCommands();
                        DMLStatement[] commands = Translate.translateToSQL(scriptModel, line, null);
                        // TODO - FIXME

                        for (int i = 0; i < commands.length; i++) {
                          if (debug > 0) System.out.println(commands[i].getSql());
                          ((DatabaseAdapter) pql.getAdaptor()).sqlEvaluate(commands[i]);
                      }
                      //pql.executePQL(line);
                    }
                }
            }
        } catch (ParserException e) {
            env.out.println("Parser Error: " + e.getMessage());
            if (debug > 1) {
                e.printStackTrace();
            }
        } catch (LexerException e) {
            env.out.println("Lexer Error: " + e.getMessage());
            if (debug > 1) {
                e.printStackTrace();
            }
        } catch (AnalyserException e) {
            env.out.println("Analyser Error: " + e.getMessage());
            if (debug > 1) {
                e.printStackTrace();
            }
//        } catch (SQLException e) {
//            env.out.println("SQL Error: " + e.getMessage());
//            if (debug > 1) {
//                e.printStackTrace();
//            }
        } catch (NotYetImplementedException e) {
            env.out.println("Not yet implemented: " + e.getMessage());
            if (debug > 1) {
                e.printStackTrace();
            }
        } catch (IllegalArgumentException e) {
            env.out.println("Internal error: " + e.getMessage());
            if (debug > 1) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            env.out.println("Internal error: " + e.getMessage());
            if (debug > 1) {
                e.printStackTrace();
            }
        }
    }

    public void printDebugStaus() {
        env.out.println("Debug " + (debug > 0 ? "on: Level " + debug : "off"));
    }

    public String getModePrompt() {
        if (mode == PQL_MODE) {
            return "PQL";
        } else if (mode == SQL_MODE) {
            return "SQL";
        } else {
            return "";
        }
    }

 
    public int getDebug() {
        return debug;
    }

    public int getMode() {
        return mode;
    }

    public void setDebug(int b) {
        debug = b;
    }

    public void setMode(int i) {
        mode = i;
    }

}
