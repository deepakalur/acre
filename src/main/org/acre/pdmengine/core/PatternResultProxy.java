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
package org.acre.pdmengine.core;

import org.acre.pdmengine.model.PatternResult;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author rajmohan@Sun.com
 */
public class PatternResultProxy implements InvocationHandler {
    private PatternResult coarsePattern, filteredPattern, delegate;

    public PatternResultProxy(PatternResult coarsePattern, PatternResult filteredPattern) {
        this.coarsePattern = coarsePattern;
        this.filteredPattern = filteredPattern;
        this.delegate = filteredPattern;
    }
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if ("viewCoarseResults".equalsIgnoreCase(method.getName())) {
                delegate = coarsePattern;
            }
            else if ( "viewFilteredResults".equalsIgnoreCase(method.getName())) {
                delegate = filteredPattern;
            }
            return method.invoke(delegate, args);
    }
}
