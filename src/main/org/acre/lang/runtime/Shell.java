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

import ca.uwaterloo.cs.ql.interp.Env;
import org.acre.lang.analyser.AnalyserException;
import org.acre.lang.analyser.NotYetImplementedException;
import org.acre.lang.analyser.ScriptModel;
import org.acre.lang.lexer.LexerException;
import org.acre.lang.parser.ParserException;
import org.acre.lang.pql.translator.Translate;
import org.gnu.readline.Readline;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Proxy shell for interactive interpreter for PQL
 *  
 * @author Syed Ali
 */                          
public class Shell {
    private final static int REQL_MODE = 1;
    private final static int PREQL_MODE = 2;
    private int mode = PREQL_MODE;
    private int debug = 0;
    private Env env;
    private ca.uwaterloo.cs.ql.Shell reqlShell;
    private ScriptModel scriptModel = new ScriptModel();

    public Shell() {
        reqlShell = new ca.uwaterloo.cs.ql.Shell();
        reqlShell.initEnv();
        env = reqlShell.getEnv();
    }

    public void shellEvaluate() {
        String line;


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
        Readline.cleanup();
        freeEnv();
    }
    private void freeEnv() {
        env.popScope();
    }
    private boolean processKeyword(String line) {
        if (line.equals("?") || line.equals("help")) {
            if (mode == REQL_MODE) {
                //help for pql
            } else {
                env.out.println("  Command       Meaning");
                env.out.println("  -----------   ----------------");
                env.out.println("  help or ?     prints this message");
                env.out.println("  ql            switch to pql mode");
                env.out.println("  pql           switch to PQL mode");
                env.out.println("  load <path>   load database");
                env.out.println("  debugon       switch to debugging on");
                env.out.println("  debugon2      switch to level 2 debugging");
                env.out.println("  debugoff      switch to debugging off");
                env.out.println("  exit          exit interactive interpreter");
                env.out.println();
            }
        } else if (line.equals("preql") || line.equals("pql")) {
            mode = PREQL_MODE;
        } else if (line.equals("ql") || line.equals("pql")) {
            mode = REQL_MODE;
        } else if (line.startsWith("load")) {
            String database = line.substring(4);
            env.err.println("Loading '"+database+"' .....");
            if (database!= null && database.trim().length() > 0) {
                database = database.trim();
                File f = new File(database);
                if (f.exists()) {
                    reqlEvaluate(Translate.getImport(database.trim()));
                    String[] lines = Translate.PREQL_PROLOGUE;
                    for (int i = 0; i < lines.length; i++) {
                        reqlEvaluate(lines[i]);
                    }
                } else {
                    env.out.println("Load Error: database file does not exist");
                }
            } else {
                env.out.println("Load Error: database path not specified");
            }
        } else if (line.equals("debugon")) {
            debug = 1;
            printDebugStaus();
        } else if (line.equals("debugon")) {
            debug = 1;
            printDebugStaus();
        } else if (line.equals("debugon2")) {
            debug = 2;
            printDebugStaus();
        } else if (line.equals("debugoff")) {
            debug = 0;
            printDebugStaus();
        } else if (line.equals("exit") || line.equals("bye") || line.equals("quit")) {
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
            || line.equals("ql")
            || line.equals("pql")
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

    public void reqlEvaluate(String command) {
        if (debug > 0) {
            env.out.println("pql: " + command);
        }
        reqlShell.evaluate(command);
    }
    
    public void evaluate(String line) throws IOException {
        try {
            if (line != null && line.length() > 0) {
                String[] commands = {};
                if (mode == REQL_MODE) {
                    commands = new String[]{line};
                } else {
                    if (line.toUpperCase().startsWith("REQL:")) {
                        commands = new String[]{line.substring(5)};
                    } else {
                        scriptModel.flushCommands();
                        String commandsString = Translate.translate(scriptModel, line);
                        List commandsList = new ArrayList();
                        int pos;
                        while ((pos = commandsString.indexOf('\n')) > 0) {
                            commandsList.add(commandsString.substring(0, pos));
                            commandsString = commandsString.substring(pos + 1);
                        }
                        commandsList.add(commandsString);
                        commands = new String[commandsList.size()];
                        commandsList.toArray(commands);                    
//                        System.out.println("@@@@@@@@@@@@@@@");
//                        for (int i = 0; i < commands.length; i++) {
//                            System.out.println(i+ ": "+ commands[i]);
//                        }
//                        System.out.println("@@@@@@@@@@@@@@@");
                    }
                }
                for (int i = 0; i < commands.length; i++) {
                    reqlEvaluate(commands[i]);
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
        }
    }

    public void printDebugStaus() {
        env.out.println("Debug " + (debug > 0 ? "on: Level " + debug : "off"));
    }

    public String getModePrompt() {
        if (mode == PREQL_MODE) {
            return "PQL";
        } else if (mode == REQL_MODE) {
            return "QL";
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

    /**
     * @param string
     */
    public void loadDatabase(String database) throws IOException {
        if (database != null) 
            processKeyword("load "+database);
    }

}
