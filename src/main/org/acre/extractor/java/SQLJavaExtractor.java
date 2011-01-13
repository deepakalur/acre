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

import com.sun.tools.javac.util.Context;

import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * Implements main method for SQL fact extractor
 *
 * @author Yury Kamen
 */
public class SQLJavaExtractor extends MainExtractor {
    public SQLJavaExtractor(String name) {
        super(name);
    }

    public SQLJavaExtractor(String name, PrintWriter out) {
        super(name, out);
    }

    public SQLJavaExtractor() {

    }

    protected void runFactExtractor(Context context) {
        new SQLBasicJavaExtractor(context).apply();
    }

    /**
     * Command line interface.
     *
     * @param args The command line parameters.
     */
    public static void main(String[] args) throws Exception {
        if (args.length > 0 && args[0].equals("-Xjdb")) {
            String[] newargs = new String[args.length + 2];
            Object rcvr = null;
            Class c = Class.forName("com.sun.tools.example.debug.tty.TTY");
            Method method = c.getDeclaredMethod("main", new Class[]{args.getClass()});
            method.setAccessible(true);
            System.arraycopy(args, 1, newargs, 3, args.length - 1);
            newargs[0] = "-connect";
            newargs[1] = "com.sun.jdi.CommandLineLaunch:options=-esa -ea:com.sun.tools...";
            newargs[2] = "com.sun.tools.javac.Main";
            method.invoke(rcvr, new Object[]{newargs});
        } else {
            System.exit(compileWrapper(args));
        }
    }

    /**
     * Programmatic interface.
     *
     * @param args The command line parameters.
     */
    public static int compileWrapper(String[] args) {
        SQLJavaExtractor compiler = new SQLJavaExtractor("javac");
        return compiler.compile(args);
    }

    /**
     * Programmatic interface.
     *
     * @param args The command line parameters.
     * @param out  Where the compiler's output is directed.
     */
    public static int compileWrapper(String[] args, PrintWriter out) {
        SQLJavaExtractor compiler = new SQLJavaExtractor("javac", out);
        return compiler.compile(args);
    }
}
