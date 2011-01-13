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

import org.acre.lang.analyser.Expression;
import org.acre.lang.analyser.ScriptModel;
import org.acre.lang.pql.pdbc.PQLResultSet;
import org.acre.lang.pql.pdbc.PQLValueHolder;


/**
 * Interface to connect to the backend
 * 
 * @author Syed Ali
 */
public interface RuntimeAdapter {


    public void connect();
    public void disconnect();
    
    public Object evaluateNative(String line);
    
    public Object evaluatePQL(ScriptModel model, String line);

    public void resetScope();
    public void pushScope();
    public void popScope();
    
    public Object getProperty(String name);

    /**
     * Sets PQL/QL variable to a given value
     * @param name   variable name
     * @param value  variable value
     */
    public void setProperty(String name, Object value);

    public PQLResultSet getVariable(Expression variable);

    public PQLValueHolder getPQLData(long nodeId, String type);

    public void complete(PQLValueHolder data);

}

