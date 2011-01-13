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

import org.acre.lang.ql.lib.object;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ca.uwaterloo.cs.ql.lib.FunctionLib;
import ca.uwaterloo.cs.ql.interp.Env;

/**
 * Main class for running the interactive interpreter for preql
 * 
 * @author Syed Ali
 */
public class Main {

    public Main() {
        env = new Env();
        object.register(FunctionLib.instance());
    }

    public void qlshell(boolean debug, String database) throws IOException {
        env.out.println("ql version 1.0");
        Shell shell1 = new Shell();
        shell1.setDebug(debug ? 1 : 0);
        shell1.loadDatabase(database);
        shell1.shellEvaluate();
    }

    public void sqlshell(boolean debug, String dbDriver, String dbUrl, String dbUser, String dbPassword) {
        env.out.println("sql version 1.0");
        SQLShell shell1 = new SQLShell(dbDriver, dbUrl, dbUser, dbPassword);
        shell1.setDebug(debug ? 1 : 0);
        shell1.shellEvaluate();
    }
/*
    public void exeFile(String as[]) {
        Timing timing = new Timing();
        timing.start();
        File file = new File(as[0]);
        try {
            Interp interp = new Interp(file);
            interp.fileEvaluate(env, as);
        } catch (FileNotFoundException filenotfoundexception) {
            System.err.println("No such a file " + file + " exists");
        }
        timing.stop();
        System.exit(1);
    }
*/
    public static void help() {
        System.out.println("Usage: java org.acre.lang.runtime.Main [-options]");
        System.out.println();
        System.out.println("where options include:");
        System.out.println("    -targetSQL    Connect to an RDBMS instead of QL engine");
        System.out.println("    -database <path to database file or database URL>");
        System.out.println("                  Loads the database files. Only one -database");
        System.out.println("                  argument can be specified");
        System.out.println("    -driver <Driver class name>");
        System.out.println("                  Driver for database. Only used with -targetSQL flag");
        System.out.println("    -user <User name>");
        System.out.println("                  User name for database. Only used with -targetSQL flag");
        System.out.println("    -password <user password>");
        System.out.println("                  User password for database. Only used with -targetSQL flag");
        System.out.println("    -script <path to PQL script file>");
        System.out.println("                  Loads the database files. Multiple -script");
        System.out.println("                   arguments can be specified");
        System.out.println("    -debug        runs in debug mode");
        System.out.println("    -? -help    print this help message");
        System.exit(1);
    }

    public static void main(String args[]) throws IOException {
        Main main1 = new Main();
        boolean debug = false;
        boolean targetSQL = false;
        List scripts = new ArrayList();
        String database = null;
        String driver = null;
        String user = null;
        String password = null;
        for (int i = 0; i < args.length; i++) {
            if (false) {
            } else if (args[i].equals("-?") || args[i].equals("-help")) {
                help();
            } else if (args[i].equals("-targetSQL")) {
                targetSQL = true;
            } else if (args[i].equals("-debug")) {
                debug = true;
            } else if (args[i].equals("-driver")) {
                if (driver != null) {
                    throw new IllegalArgumentException("Cannot load multiple drivers: " + driver +" & " + nextArg(args[i], i, args));
                } else {
                    driver = nextArg(args[i], i, args);
                    i++;
                }
            } else if (args[i].equals("-database")) {
                if (database != null) {
                    throw new IllegalArgumentException("Cannot load multiple databases: " + database +" & " + nextArg(args[i], i, args));
                } else {
                    database = nextArg(args[i], i, args);
                    i++;
                }
            } else if (args[i].equals("-user")) {
                if (user != null) {
                    throw new IllegalArgumentException("Cannot load multiple users: " + user +" & " + nextArg(args[i], i, args));
                } else {
                    user = nextArg(args[i], i, args);
                    i++;
                }
            } else if (args[i].equals("-password")) {
                if (password != null) {
                    throw new IllegalArgumentException("Cannot load multiple passwords: " + password +" & " + nextArg(args[i], i, args));
                } else {
                    password = nextArg(args[i], i, args);
                    i++;
                }
            } else if (args[i].equals("-script")) {
                scripts.add(nextArg(args[i], i, args));
                i++;
            } else {
                throw new IllegalArgumentException("Cannot understand argument " + args[i]);
            }
            
        }
        if (scripts.size() > 0) {
            //run scripts
        } else if (targetSQL) {
            main1.sqlshell(debug, driver, database, user, password);
        } else {
            main1.qlshell(debug, database);
        }
    }
    public static String nextArg(String switchArg, int index, String args[]) {
        if (args.length > index + 1) {
            return args[index + 1];
        } else {
            throw new IllegalArgumentException("Expecting argument after argument "+switchArg);
        }
    }

    private Env env;

}
