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
package org.acre.lang.runtime.groovy;

import groovy.lang.GString;
import groovy.lang.GroovyObjectSupport;
import groovy.lang.MetaClassRegistry;
import org.acre.lang.analyser.ScriptModel;
import org.acre.lang.pql.pdbc.PQLArtifact;
import org.acre.lang.pql.pdbc.PQLResultSetImpl;
import org.acre.lang.pql.pdbc.PQLResultSetMetaDataImpl;
import org.acre.lang.pql.pdbc.PQLValueHolder;
import org.acre.lang.runtime.PQL;
import org.acre.lang.runtime.RuntimeAdapter;
import org.codehaus.groovy.runtime.InvokerHelper;

import java.beans.IntrospectionException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Groovy-PQL class adapter
 *
 * @author Yury Kamen
 */
public class GroovyPQL {
    PQL pql;
    boolean connected = false;

    public static void main(String[] args) throws Exception {
        GroovyPQL pql = new GroovyPQL("org.gjt.mm.mysql.Driver",
                "jdbc:mysql://localhost/salsa?autoReconnect=true",
                "root",
                "root");

        Map result;
//        result = pql.execute("define a as SELECT p.name, p.name FROM packages p;"
//                                 + "define b as SELECT p.name FROM packages p;"
//                                 + "return a,b;");
        result = pql.execute("return SELECT c.name,  c.shortName " +
                             "FROM classes c " +
                             "WHERE c.extendsClass.name = \"javax.servlet.http.HttpServlet\";");
        System.out.println("================ result=" + result);
        return;
    }

    static {
        initialize();
    }

    private static PQLMetaClass createMetaClass(MetaClassRegistry mr, Class c) throws IntrospectionException {
        if (c.equals(PQLArtifact.class)) {
            return new PQLArtifactMetaClass(mr, c);
        }
        if (c.equals(PQLResultSetImpl.class)) {
            return new PQLResultSetImplMetaClass(mr, c);
        }
        if (c.equals(PQLResultSetMetaDataImpl.class)) {
            return new PQLResultSetMetaDataImplMetaClass(mr, c);
        }
        return new PQLMetaClass(mr, c);
    }

    private static PQLMetaClass registerMetaClass(Class c, List list) throws IntrospectionException {
        MetaClassRegistry mr = InvokerHelper.getInstance().getMetaRegistry();
        PQLMetaClass m = createMetaClass(mr, c);
        mr.setMetaClass(c, m);
        list.add(m);
        return m;
    }

    private static void initializeMetaClasses(List list) {
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            PQLMetaClass pqlMetaClass = (PQLMetaClass) iterator.next();
            pqlMetaClass.checkInitialised();
        }
    }

    private static void initialize() {
        PQLMetaClass m;
        ArrayList list = new ArrayList();
        try {
            // Initialize individual metaclasses
            m = registerMetaClass(PQLResultSetImpl.class, list);
            m = registerMetaClass(PQLArtifact.class, list);
            m = registerMetaClass(PQLResultSetMetaDataImpl.class, list);

            // Add instance and static Groovy methods
            m = registerMetaClass(org.acre.lang.runtime.groovy.DefaultPQLGroovyMethods.class, list);
            m.registerInstanceMethods();
            m.checkInitialised();
            m = registerMetaClass(DefaultPQLGroovyStaticMethods.class, list);
            m.registerStaticMethods();
            m.checkInitialised();

            // Initalize metaclasses
            initializeMetaClasses(list);
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
    }

    public PQL getPql() {
        return pql;
    }
    
    public PQL createQLPQL(String database) {
        return PQL.createQLPQL(database);
    }

    public GroovyPQL() {
        pql = PQL.createDatabasePQL();
        connect();
    }

    public GroovyPQL(String name) {
        pql = PQL.createDatabasePQL(name);
        connect();
    }

    public GroovyPQL(String dbDriver, String dbUrl, String dbUser, String dbPassword) {
        pql = PQL.createDatabasePQL(dbDriver, dbUrl, dbUser, dbPassword);
        connect();
    }

    public GroovyPQL(String name, String dbDriver, String dbUrl, String dbUser, String dbPassword) {
        pql = PQL.createDatabasePQL(name, dbDriver, dbUrl, dbUser, dbPassword);
        connect();
    }

    public void connect() {
        if (!connected) {
            getAdaptor().connect();
            connected = true;
        }
    }

    public void disconnect() {
        if (connected) {
            getAdaptor().disconnect();
            connected = false;
        }
    }

    public Map executePQL(String query) {
        return new MapDelegate(pql.executePQL(query));
    }

    public Map execute(String query) {
        return executePQL(query);
    }

    public PQLValueHolder fetchPQLData(int nodeId, String type) {
        return pql.fetchPQLData(nodeId, type);
    }

    public void fetch(PQLValueHolder data) {
        pql.fetch(data);
    }

    public RuntimeAdapter getAdaptor() {
        return pql.getAdaptor();
    }

    public boolean getDebug() {
        return pql.getDebug();
    }

    public void setDebug(boolean debug) {
        pql.setDebug(debug);
    }

    /**
     * Executes the given SQL with embedded expressions inside
     */
    public Map execute(GString gstring) {
        return execute(gstring.toString());
//         List params = getParameters(gstring);
//         String sql = asSql(gstring, params);
//         return execute(sql, params);
    }

    /**
     * @return extracts the parameters from the expression as a List
     */
    protected List getParameters(GString gstring) {
        Object[] values = gstring.getValues();
        List answer = new ArrayList(values.length);
        for (int i = 0; i < values.length; i++) {
            if (values[i] != null) {
                answer.add(values[i]);
            }
        }
        return answer;
    }

    /**
     * @return the SQL version of the given query using ? instead of any
     *         parameter
     */
    protected String asSql(GString gstring, List values) {
        boolean nulls = false;
        String[] strings = gstring.getStrings();
        if (strings.length <= 0) {
            throw new IllegalArgumentException("No SQL specified in GString: " + gstring);
        }
        StringBuffer buffer = new StringBuffer();
        boolean warned = false;
        Iterator iter = values.iterator();
        for (int i = 0; i < strings.length; i++) {
            String text = strings[i];
            if (text != null) {
                buffer.append(text);
            }
            if (iter.hasNext()) {
                Object value = iter.next();
                if (value != null) {
                    boolean validBinding = true;
                    if (i < strings.length - 1) {
                        String nextText = strings[i + 1];
                        if ((text.endsWith("\"") || text.endsWith("'")) && (nextText.startsWith("'") || nextText.startsWith("\""))) {
                            if (!warned) {
                                System.err.println("In Groovy SQL please do not use quotes around dynamic expressions "
                                                   + "(which start with $) as this means we cannot use a JDBC PreparedStatement "
                                                   + "and so is a security hole. Groovy has worked around your mistake but the security hole is still there. The expression so far is: " + buffer.toString() + "?" + nextText);
                                warned = true;
                            }
                            buffer.append(value);
                            iter.remove();
                            validBinding = false;
                        }
                    }
                    if (validBinding) {
                        buffer.append("?");
                    }
                } else {
                    nulls = true;
                    buffer.append("?'\"?"); // will replace these with nullish
                    // values
                }
            }
        }
        String sql = buffer.toString();
        if (nulls) {
            sql = nullify(sql);
        }
        return sql;
    }

    /**
     * replace ?'"? references with NULLish
     *
     * @param sql
     * @return
     */
    protected String nullify(String sql) {
        /*
         * Some drivers (Oracle classes12.zip) have difficulty resolving data
         * type if setObject(null). We will modify the query to pass 'null', 'is
         * null', and 'is not null'
         */
        //could be more efficient by compiling expressions in advance.
        int firstWhere = findWhereKeyword(sql);
        if (firstWhere >= 0) {
            Pattern[] patterns = {Pattern.compile("(?is)^(.{" + firstWhere + "}.*?)!=\\s{0,1}(\\s*)\\?'\"\\?(.*)"),
                                  Pattern.compile("(?is)^(.{" + firstWhere + "}.*?)<>\\s{0,1}(\\s*)\\?'\"\\?(.*)"),
                                  Pattern.compile("(?is)^(.{" + firstWhere + "}.*?[^<>])=\\s{0,1}(\\s*)\\?'\"\\?(.*)"), };
            String[] replacements = {"$1 is not $2null$3", "$1 is not $2null$3", "$1 is $2null$3", };
            for (int i = 0; i < patterns.length; i++) {
                Matcher matcher = patterns[i].matcher(sql);
                while (matcher.matches()) {
                    sql = matcher.replaceAll(replacements[i]);
                    matcher = patterns[i].matcher(sql);
                }
            }
        }
        return sql.replaceAll("\\?'\"\\?", "null");
    }

    /**
     * Find the first 'where' keyword in the sql.
     *
     * @param sql
     * @return
     */
    protected int findWhereKeyword(String sql) {
        char[] chars = sql.toLowerCase().toCharArray();
        char[] whereChars = "where".toCharArray();
        int i = 0;
        boolean inString = false; //TODO: Cater for comments?
        boolean noWhere = true;
        int inWhere = 0;
        while (i < chars.length && noWhere) {
            switch (chars[i]) {
                case '\'':
                    if (inString) {
                        inString = false;
                    } else {
                        inString = true;
                    }
                    break;
                default:
                    if (!inString && chars[i] == whereChars[inWhere]) {
                        inWhere++;
                        if (inWhere == whereChars.length) {
                            return i;
                        }
                    }
            }
            i++;
        }
        return -1;
    }

    static public class MapDelegate extends GroovyObjectSupport implements Map {
        private Map map;

        public MapDelegate(Map map) {
            this.map = map;
        }

        /**
         * @return the given property
         */
        public Object getProperty(String property) {
            Object res = map.get(property);
            if (null == res && "result".equals(property)) {
                res = map.get(ScriptModel.DEFAULT_RETURN_VARIABLE);
            }
            if (null == res) {
                res = super.getProperty(property);
            }
            return res;
        }

        public int size() {
            return map.size();
        }

        public boolean isEmpty() {
            return map.isEmpty();
        }

        public boolean containsKey(Object key) {
            return map.containsKey(key);
        }

        public boolean containsValue(Object value) {
            return map.containsValue(value);
        }

        public Object get(Object key) {
            return map.get(key);
        }

        public Object put(Object key, Object value) {
            return map.put(key, value);
        }

        public Object remove(Object key) {
            return map.remove(key);
        }

        public void putAll(Map t) {
            map.putAll(t);
        }

        public void clear() {
            map.clear();
        }

        public Set keySet() {
            return map.keySet();
        }

        public Collection values() {
            return map.values();
        }

        public Set entrySet() {
            return map.entrySet();
        }

        public boolean equals(Object o) {
            return map.equals(o);
        }

        public int hashCode() {
            return map.hashCode();
        }
    }
}
