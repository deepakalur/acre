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

import org.acre.extractor.tools.jackpot.*;
import org.acre.extractor.tools.jackpot.utils.*;

import com.sun.tools.javac.comp.*;
import com.sun.tools.javac.main.*;
import com.sun.tools.javac.tree.*;
import com.sun.tools.javac.util.*;

/**
 * Runs Mike Godfrey's fact extractor as a command-line tool.
 *
 * @author Tom Ball
 */
public class Main {
    Attr attr;
    Enter enter;
    Log log;
    Options options;
    Todo todo;

    private static void usage() {
        System.err.println("java org.acre.extractor.java.Main <sources>");
        System.exit(1);
    }

    public static void main(String[] args) {
        new Main().compile(args, new Context());
    }

    public void compile(String[] sources, Context context) {
        options = Options.instance(context); // creates a new one
        options.put("-attrparseonly", "-attrparseonly"); // just build trees
        options.put("-source", "1.5");

        attr = Attr.instance(context);
        enter = Enter.instance(context);
        log = Log.instance(context);
        todo = Todo.instance(context);

        try {
            if (sources.length == 0)
                usage();

            List<String> filenames =
                    List.make(SourceFileList.findFiles(sources, sourceFilter));
            if (filenames.length() == 0) {
                System.err.println("Error: no files found");
                usage();
            }
            //parse all files
            JavaCompiler comp = JavaCompiler.instance(context);
            ListBuffer<Tree> trees = new ListBuffer<Tree>();
            for (List<String> l = filenames; l.nonEmpty(); l = l.tail)
                trees.append(comp.parse(l.head));

//enter symbols for all files
            List<Tree> roots = trees.toList();
            if (comp.errorCount() == 0)
                enter.main(roots);

            //attribution phase
            while (todo.nonEmpty()) {
                Env<AttrContext> env = todo.next();
                log.useSource(env.enclClass.sym.sourcefile);
                attr.attribClass(env.tree.pos, env.enclClass.sym);
            }

            if (comp.errorCount() != 0)
                System.exit(1);

            // Create package tree hierarchy
            PackageTree.PackageDef root = PackageTree.create(context, roots);

            // Execute task
            new BasicJavaExtractor(context).apply();

        } catch (Throwable ex) {
            System.err.println("fatal error: " + ex);
            ex.printStackTrace();
        }
    }

    public static final SourceFileFilter sourceFilter =
            new org.acre.extractor.tools.jackpot.utils.DefaultSourceFileFilter() {
                public boolean acceptFile(String name) {
                    return super.acceptFile(name) && (name.endsWith(".java"));
                }
            };


    //////////////////// Javac options ////////////////////////////////////////////

}
