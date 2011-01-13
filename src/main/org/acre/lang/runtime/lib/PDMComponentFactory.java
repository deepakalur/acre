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
package org.acre.lang.runtime.lib;

import groovy.lang.GroovyClassLoader;
import org.codehaus.groovy.control.CompilationFailedException;

import java.io.File;
import java.io.IOException;

/**
 * @author Yury Kamen
 */
public class PDMComponentFactory {
    public static PDMComponent getPDMComponent(File file) throws IOException, CompilationFailedException, IllegalAccessException, InstantiationException {
        ClassLoader parent = PDMComponentFactory.class.getClassLoader();
        GroovyClassLoader gcl = new GroovyClassLoader(parent);
        Class clazz = gcl.parseClass(file);
        Object aScript = clazz.newInstance();
        PDMComponent component = (PDMComponent) aScript;
        return component;
    }

    public static PDMComponent getPDMComponent(String cmd) throws IOException, CompilationFailedException, IllegalAccessException, InstantiationException {
        ClassLoader parent = PDMComponentFactory.class.getClassLoader();
        GroovyClassLoader gcl = new GroovyClassLoader(parent);
        Class clazz = gcl.parseClass(cmd);
        Object aScript = clazz.newInstance();
        PDMComponent component = (PDMComponent) aScript;
        return component;
    }

}
