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

import groovy.lang.GroovyObjectSupport;
import groovy.lang.MetaClass;
import groovy.lang.MissingMethodException;
import org.acre.lang.analyser.Expression;
import org.acre.lang.analyser.ScriptModel;
import org.acre.lang.lexer.LexerException;
import org.acre.lang.parser.ParserException;
import org.acre.lang.pql.pdbc.PQLResultSet;
import org.acre.lang.pql.pdbc.PQLResultSetImpl;
import org.acre.lang.pql.pdbc.PQLValueHolder;
import org.acre.lang.pql.translator.Translate;
import org.codehaus.groovy.runtime.InvokerHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Base class for PQL Groovy/Java Proxy 
 * It also contains Java APIs to PQL
 *
 * @author Syed Ali
 */
public class PQLSupport extends GroovyObjectSupport {
    private boolean tryRealFirst;
    private MetaClass realMeta, first, second;
    protected ScriptModel scriptModel;
    private boolean debug = false;
    protected RuntimeAdapter adapter;

    protected PQLSupport(RuntimeAdapter real, boolean tryRealFirst) {
        this.tryRealFirst = tryRealFirst;
        this.adapter = real;
        setMetaClass(InvokerHelper.getMetaClass(real));
    }

    /**
     * Executes QL query, and return query result set back
     *
     * @param query QL query
     * @return query result set
     */
    public synchronized Object executeNative(String query) {
        return adapter.evaluateNative(query);
    }

    /**
     * Executes PQL query, and return query result set back
     *
     * @param query PQL query
     * @return query result set
     */
    public synchronized Map executePQL(String query) {
        String name;
        scriptModel.reset();
        adapter.pushScope();
        Map results = executePQLInScope(query);
        adapter.popScope();
        return results;
    }

    /**
     * Executes PQL query, and return query result set back
     *
     * @param query PQL query
     * @return query result set
     * @throws ParserException
     * @throws LexerException
     * @throws IOException
     */
    public static void validatePQL(String query) throws IOException, LexerException, ParserException {
        Translate.parsePQL(query);
    }

    private synchronized Map executePQLInScope(String query) {
        String name;
        query = query.trim();

        adapter.evaluatePQL(scriptModel, query);
        int returnVarCount = scriptModel.getReturnVariables().size();
        Map results = new HashMap(returnVarCount);
        for (Iterator iter = scriptModel.getReturnVariables().iterator(); iter.hasNext();) {
            name = (String) iter.next();
            PQLResultSet result = adapter.getVariable(scriptModel.getVariable(name));
            results.put(name, result);
        }
        return results;
    }

    public PQLValueHolder fetchPQLData(int nodeId, String type) {
        return adapter.getPQLData(nodeId, type);
    }
    
    public void fetch(PQLValueHolder data) {
        adapter.complete(data);
        
    }
    
//    /**
//     * Cleans up ql scope & pql script model.
//     * NOTE: the database has to reloaded after this.
//     *
//     */
//    public void clean() {
//        adaptor.resetScope();
//        scriptModel.reset();
//    }
//
    /**
     * Returns PQL variable value
     * @param name    PQL variable name
     * @return        PQL variable value
     */
    public Object getProperty(String name) {
        return adapter.getProperty(name);
    }

    /**
     * Sets PQL variable to a given value
     *
     * @param name  PQL variable name
     * @param value PQL variable value
     */
    public void setProperty(String name, Object value) {
        if (value instanceof PQLResultSet) {
            value = ((PQLResultSetImpl) value).getIntervalValue();
            scriptModel.saveVariable(name);
        }
        adapter.setProperty(name, value);
    }

    /**
     * Returns the MetaClass for the <b>real</b> object.
     *
     * @return MetaClass of real object
     */
    public MetaClass getMetaClass() {
        return realMeta;
    }

    /**
     * Returns the MetaClass for the <b>proxy </b> object.
     *
     * @return MetaClass of proxy object
     */
    public MetaClass getProxyMetaClass() {
        return super.getMetaClass();
    }

    /**
     * Returns the encapsulated object.
     *
     * @return the real object
     */
    public RuntimeAdapter getAdaptor() {
        return adapter;
    }

    /**
     * Call a method of this proxy, or the real object if method doesn't exist.
     *
     * @param name method to invoke
     * @param args arguments to pass
     * @return
     */
    public Object invokeMethod(String name, Object args) {
        try {
            return first.invokeMethod(first == realMeta ? (Object)adapter : this, name, args);
        } catch (MissingMethodException e) {
            return second.invokeMethod(first == realMeta ? this : (Object)adapter, name, args);
        }
    }

    /**
     * Dynamically change the meta class to use for the <b>real</b> object.
     *
     * @param metaClass substitute real meta class
     */
    public void setMetaClass(MetaClass metaClass) {
        realMeta = metaClass;
        if (tryRealFirst) {
            first = realMeta;
            second = getProxyMetaClass();
        } else {
            first = getProxyMetaClass();
            second = realMeta;
        }
    }

    /**
     * Dynamically change the meta class to use for the <b>proxy</b> object.
     *
     * @param metaClass substitute meta class for the proxy object
     */
    public void setProxyMetaClass(MetaClass metaClass) {
        super.setMetaClass(metaClass);
        if (tryRealFirst) {
            first = realMeta;
            second = getMetaClass();
        } else {
            first = getMetaClass();
            second = realMeta;
        }
    }
    
    private String getObjectFunction(Expression variable) {
        String columns = "";
        if (variable.isStruct()) {
            boolean required = false;
            Expression[] columnTypes = variable.getStructType();
            for (int i = 0; i < columnTypes.length; i++) {
                if (!columnTypes[i].isPrimitive()) {
                    if (columns == "")
                        columns += i;
                    else 
                        columns += ","+i;
                } else {
                    required = true;
                }
            }
            if (required) {
                columns = ", {"+columns+"}";
            } else {
                columns = "";
            }
        }
        return "return object("+variable.getName()+columns+")";
    }

    public boolean getDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}

