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

import org.apache.tools.ant.taskdefs.compilers.*;

import java.lang.reflect.Method;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Commandline;

public class AntCompilerAdapter extends DefaultCompilerAdapter {
    /**
     * Integer returned by the "Modern" jdk1.3 compiler to indicate success.
     */
    private static final int MODERN_COMPILER_SUCCESS = 0;

    protected String getFactExtractorClassName() {
        return "org.acre.extractor.java.MainExtractor";
    }
    /**
     * Run the compilation.
     *
     * @throws BuildException if the compilation has problems.
     */
    public boolean execute() throws BuildException {
        attributes.log("Using BasicJavaExtractor compiler", Project.MSG_VERBOSE);
        Commandline cmd = setupModernJavacCommand();
        // Use reflection to be able to build on all JDKs >= 1.1:
        try {
            Class c = Class.forName(getFactExtractorClassName());
            Object compiler = c.newInstance();
            Method compile = c.getMethod("compile", new Class[]{(new String[]{}).getClass()});
            int result = ((Integer) compile.invoke(compiler, new Object[]{cmd.getArguments()})).intValue();
            return (result == MODERN_COMPILER_SUCCESS);
        } catch (Exception ex) {
            ex.printStackTrace(); // Yury Kamen: additional debugging
            if (ex instanceof BuildException) {
                throw (BuildException) ex;
            } else {
                throw new BuildException("Error starting BasicJavaExtractor compiler",
                        ex, location);
            }
        }
    }
}

