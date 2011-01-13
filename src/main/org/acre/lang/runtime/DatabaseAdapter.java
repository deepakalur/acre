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

import org.acre.lang.analyser.DMLStatement;
import org.acre.lang.analyser.Expression;
import org.acre.lang.analyser.ScriptModel;
import org.acre.lang.lexer.LexerException;
import org.acre.lang.parser.ParserException;
import org.acre.lang.pql.pdbc.PQLArtifact;
import org.acre.lang.pql.pdbc.PQLResultSet;
import org.acre.lang.pql.pdbc.PQLResultSetImpl;
import org.acre.lang.pql.pdbc.PQLValueHolder;
import org.acre.lang.pql.translator.Translate;
import org.acre.lang.runtime.SQLShell.Env;
import org.acre.lang.runtime.lib.PQLRuntimeException;
import org.acre.model.metamodel.*;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Backend connector implementation for RDBMS Implemenation for Fact Datastore
 * 
 * @author Syed Ali
 */
public class DatabaseAdapter implements RuntimeAdapter {

    private int debug = 0;
    
    private String dbDriver = "org.gjt.mm.mysql.Driver";
    private String dbUrl = "jdbc:mysql://localhost:3306/salsa";
    private String dbUser = "salsa";
    private String dbPassword = "salsa";
    
//  private String dbDriver = "com.pointbase.jdbc.jdbcUniversalDriver";
//  private String dbUrl = "jdbc:pointbase://localhost/ace";
//  private String dbUser = "jmodel";
//  private String dbPassword = "jmodel";
        
    public static final int SYSTEM_NOT_INITIALIZED = -2;
    public static final int SYSTEM_UNDEFINED = -1;
    //private long systemId = SYSTEM_NOT_INITIALIZED;
    
    private StringBuffer cookies = new StringBuffer();
    private boolean connected = false;
    private Connection dbConnection;
    private Env env = new Env();
    private ScriptModel scriptModel;

    public final static boolean USE_TEMP_TABLES = true;

    public DatabaseAdapter(ScriptModel scriptModel) {
        this.scriptModel = scriptModel;
    }
    
    public String getDbDriver() {
        return dbDriver;
    }
    public void setDbDriver(String dbDriver) {
        this.dbDriver = dbDriver;
    }
    public String getDbPassword() {
        return dbPassword;
    }
    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }
    public String getDbUrl() {
        return dbUrl;
    }
    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }
    public String getDbUser() {
        return dbUser;
    }
    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public void connect() {
        if (connected) {
            return;
        }
        if (cookies.length() > 0) {
            cookies.delete(0, cookies.length() -1);
        }
        try {
            env.out.println("Connecting to Database " + dbUser+"@"+dbUrl + " using " + dbDriver + "...");
            Class.forName(dbDriver);
            dbConnection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            connected = true;
        } catch (Exception e) {
            throw new RuntimeException("Couldn't connect to database", e);
        }
    }
    
    public void disconnect() {
        if (connected) {
            //clean all the variables
            if (!USE_TEMP_TABLES) {
                if (cookies.length() > 0) {
                    cookies.deleteCharAt(cookies.length() -1);
                    String writeVarQry = "DELETE FROM WriteVariable WHERE cookie IN (?) AND scope >= ?";
                    String readVarQry = "DELETE FROM ReadVariable WHERE cookie IN (?) AND scope >= ?";
                    executeScopeQueries(writeVarQry, readVarQry, cookies.toString(), ScriptModel.SCOPE_SESSION);
                }
            } else {
                //do nothing for temporary tables as they will disapear automatically.
            }
            
            
            try {
                dbConnection.close();
                connected = false;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    
    public void pushScope() {
        String currentCookie;
        if ((currentCookie = scriptModel.getCookie()) != null) {
            cookies.append('\'').append(currentCookie).append("',");
        }
        scriptModel.setCookie(getLastCookieId());
        //scriptModel.pushScope();
    }

    /**
     * This method is not called by the PQLSupport any more
     * */
    public void resetScope() {
        if (!USE_TEMP_TABLES) {
            String writeVarQry = "DELETE FROM WriteVariable WHERE cookie = ? AND scope > ?";
            String readVarQry = "DELETE FROM ReadVariable WHERE cookie = ? AND scope > ?";
            executeScopeQueries(writeVarQry, readVarQry);
        } else {
            //TO DO: Remove temporary variables with scope > ?
            // For now don't do anything as this method is not 
            // called by the PQLSupport any more
//            for (Iterator iter = scriptModel.getVariableNames().iterator(); iter.hasNext();) {
//                String variable = (String) iter.next();
//            }
        }
    }

    public void popScope() {
        if (!USE_TEMP_TABLES) {
            String readVarQry, writeVarQry;
            // Move the return variable to session scope
            StringBuffer variableList = new StringBuffer();
            for (Iterator iter = scriptModel.getReturnVariables().iterator(); iter.hasNext();) {
                String variable = (String) iter.next();
                variableList.append('\'').append(variable).append("\',");
            }
            if (variableList.length() > 0) {
                variableList.deleteCharAt(variableList.length() - 1);
                writeVarQry = "UPDATE WriteVariable SET scope = "
                    +ScriptModel.SCOPE_SESSION 
                    +" WHERE cookie = ? AND scope = ? AND name IN ("
                    +variableList+")";
                readVarQry = "UPDATE ReadVariable SET scope = "
                    +ScriptModel.SCOPE_SESSION 
                    +" WHERE cookie = ? AND scope = ? AND name IN ("
                    +variableList+")";
                executeScopeQueries(writeVarQry, readVarQry);
            }
            
            // Remove all the remaining temp variables
            writeVarQry = "DELETE FROM WriteVariable WHERE cookie = ? AND scope = ?";
            readVarQry = "DELETE FROM ReadVariable WHERE cookie = ? AND scope = ?";
            executeScopeQueries(writeVarQry, readVarQry);
        } else {
            Statement stmt;
            String sqlString;
            //Save return variables
            for (Iterator iter = scriptModel.getReturnVariables().iterator(); iter.hasNext();) {
                String variableName = (String) iter.next();
                Expression variable = scriptModel.getVariable(variableName);
                sqlString = "CREATE TEMPORARY TABLE " +getTemporaryTableName(variable, true, true) 
                    + " AS SELECT * FROM "+ getReadTemporaryTableName(variable);
                try {
                    if (debug > 1) System.out.println(sqlString);
                    stmt = dbConnection.createStatement();
                    stmt.executeUpdate(sqlString);
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            //drop all temporary variables
            for (Iterator iter = scriptModel.getVariableNames().iterator(); iter.hasNext();) {
                String variableName = (String) iter.next();
                Expression variable = scriptModel.getVariable(variableName);
                if (variable.getScope() == ScriptModel.SCOPE_SESSION) 
                    continue;
                try {
                    sqlString = "DROP TEMPORARY TABLE IF EXISTS " +getTemporaryTableName(variable); 
                    if (debug > 1) System.out.println(sqlString);
                    stmt = dbConnection.createStatement();
                    stmt.executeUpdate(sqlString);
                    stmt.close();
                    
                    sqlString = "DROP TEMPORARY TABLE IF EXISTS " +getReadTemporaryTableName(variable); 
                    if (debug > 1) System.out.println(sqlString);
                    stmt = dbConnection.createStatement();
                    stmt.executeUpdate(sqlString);
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void executeScopeQueries(String writeVarQry, String readVarQry) {
        executeScopeQueries(writeVarQry, readVarQry, scriptModel.getCookie(), scriptModel.getScope());
    }
    private void executeScopeQueries(String writeVarQry, String readVarQry, String cookie, int scope) {
        PreparedStatement stmt = null;
        int result;
        try {
            if (debug > 0) System.out.println(writeVarQry);
            stmt = dbConnection.prepareStatement(writeVarQry);
            stmt.setString(1, cookie);
            stmt.setInt(2, scope);
            result = stmt.executeUpdate();
            if (debug > 0) System.out.println(result + " row(s) affected");
            stmt.close();
            if (debug > 0) System.out.println(readVarQry);
            stmt = dbConnection.prepareStatement(readVarQry);
            stmt.setString(1, cookie);
            stmt.setInt(2, scope);
            result = stmt.executeUpdate();
            if (debug > 0) System.out.println(result + " row(s) affected");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }
    private int getLastCookieId() {
        Statement stmt = null;
        ResultSet rs = null;
        String sql;
        try {
            sql = "INSERT INTO SalsaCookie(id) VALUES (NULL)";
            if (debug > 1) System.out.println(sql);
            stmt = dbConnection.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();

            sql = "SELECT LAST_INSERT_ID()";
            if (debug > 1) System.out.println(sql);
            stmt = dbConnection.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        return 1;
    }

    public Object getProperty(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    public void setProperty(String name, Object value) {
        // TODO Auto-generated method stub

    }

    protected void sqlEvaluate(DMLStatement command) throws SQLException {
        if (command.isExecutable()) {
            sqlExecute(command.getSql());
        } else {
            sqlEvaluate(command.getSql());
        }
    }

    protected void sqlEvaluate(String command) throws SQLException {
        if (connected) {
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {
                if (debug > 0) System.out.println(command);
                stmt = dbConnection.prepareStatement(command);
                rs = stmt.executeQuery();
                if (rs != null) {
                    ResultSetMetaData rsmd = rs.getMetaData();
                    for (int ctr = 0; ctr < rsmd.getColumnCount(); ctr++) {
                        env.out.print(rsmd.getColumnName(ctr + 1) + " | ");
                    }
                    env.out.println();
                    env.out.println("-----------------------------------------");
                    for (; rs.next(); ) {
                        for (int ctr = 0; ctr < rsmd.getColumnCount(); ctr++) {
                            env.out.print(rs.getString(ctr + 1) + " | ");
                        }
                        env.out.println();
                    }
                    env.out.println("-----------------------------------------");
                }
            } finally {
                try {
                    if (rs != null) rs.close();
                    if (stmt != null) stmt.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
    
    protected void sqlExecute(String command) throws SQLException {
        if (connected) {
            PreparedStatement stmt = null;
            try {
                if (debug > 0) System.out.println(command);
                stmt = dbConnection.prepareStatement(command);
                stmt.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Error in command: " + command);
                throw e;
            } finally {
                try {
                    if (stmt != null) stmt.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
    
    public Object evaluatePQL(ScriptModel model, String line) {
        DMLStatement[] commands;
        try {
            commands = Translate.translateToSQL(model, line, dbConnection);
            for (int i = 0; i < commands.length; i++) {
                sqlEvaluate(commands[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new PQLRuntimeException("IO error occurred during execute PQL\n" + line, e);
        } catch (LexerException e) {
            e.printStackTrace();
            throw new PQLRuntimeException("Lexical error occurred during execute PQL\n" + line, e);
        } catch (ParserException e) {
            e.printStackTrace();
            throw new PQLRuntimeException("Parsing error occurred during execute PQL\n" + line, e);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new PQLRuntimeException("SQL Exception occurred during execute PQL\n" + line, e);
        }
        return null;
    }

    public Object evaluateNative(String line) {
        try {
            sqlEvaluate(line);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public PQLValueHolder getPQLData(long nodeId, String type) {
        PQLValueHolder result =  new PQLValueHolder(nodeId);
        result.setType(type);
        complete(result);
        return result;
    }

    public void complete(PQLValueHolder data) {
        if (data.isComplete())
            return;
        fetchAttributes(data);
        fetchRelationships(data);
    }
    protected void fetchAttributes(PQLValueHolder data) {
        String baseQuery = "SELECT * FROM "+data.getType()+" WHERE id = ?";
        //String baseQuery = "SELECT * FROM "+data.getType()+" WHERE id = ? AND name = ?";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            if (debug > 0) System.out.println(baseQuery);
            stmt = dbConnection.prepareStatement(baseQuery);
            stmt.setLong(1, data.getNodeId());
            //stmt.setString(2, data.getName());
            rs = stmt.executeQuery();
            MetaType type = FactMetaModel.getInstance().lookupMetaType(data.getType());
            if (rs != null) {
                for (; rs.next(); ) {
                    if (data.isComplete()) {
                        throw new IllegalAccessError("More than one row retreived for "+ data);
                    }
                    for (int i = 0; i < type.getMetaAttributes().size(); i++) {
                        MetaAttribute attr = type.getMetaAttributes(i);
                        String attrName = attr.getName();
                        data.addAttribute(attr.getName(), rs.getString(attrName));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }
    protected void fetchRelationships(PQLValueHolder data) {
        MetaType type = FactMetaModel.getInstance().lookupMetaType(data.getType());
        for (int i = 0; i < type.getMetaRelationships().size(); i++) {
            MetaRelationship rel = type.getMetaRelationships(i);
            String relName = rel.getName();
            fetchRelationship(data, relName);
        }
    }
    protected void fetchRelationship(PQLValueHolder data, String relName) {
        RelationshipInfo r = new RelationshipInfo(data.getType(), relName);
        if (r.isOneToOne()) {
            //1:1
            throw new IllegalArgumentException("Cannot map 1:1 relationships " + data.getType() + " - " + relName);
        } else if (r.isOneToMany()) {
            //1:n
            //if (debug > 0) System.out.println("1:n");
            List rels = fetchRelPQLData(r.getReverseType().getName(), 
                    r.getForwardType().getName(), 
                    relName, null, data.getNodeId());
            data.addRelationship(relName, rels);
        } else if (r.isManyToOne()) {
            //n:1
            //if (debug > 0) System.out.println("n:1");
            List rels = fetchRelPQLData(r.getReverseType().getName(), 
                    null, r.getReverseName(), null, data.getNodeId());
            data.addRelationship(relName, rels);
        } else if (r.isManyToMany()) {
            //m:n
            //if (debug > 0) System.out.println("m:n");
            List rels = fetchRelPQLData(r.getReverseType().getName(), 
                    r.getJoinTableName(),
                    relName, r.getReverseName(), data.getNodeId());
            data.addRelationship(relName, rels);
        } else {
            throw new IllegalArgumentException("Cardinality cannot be decifered for relationship " + data.getType() + " - " + relName);
        }
    }
    
    protected List fetchRelPQLData(String extent1, String extent2, String foreignKey1, String foreignKey2, long nodeId) {
        String relQry = "SELECT r.id, r.name FROM "+extent1+" r ";
        if (extent2 != null) {
            if (foreignKey1 == null)
                throw new IllegalArgumentException("foreignKey1 cannot be null when extent2 is not null:" + extent2);
                
            relQry += " , "+extent2+" o WHERE r.id = o."+foreignKey1+" ";
            if (foreignKey2 != null) {
                relQry += " AND o."+foreignKey2+" = ?";
            } else {
                relQry += " AND o.id = ?";
            }
        } else {
            relQry += " WHERE r."+foreignKey1+" = ?";
            
        }
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            if (debug > 0) System.out.println(relQry);
            stmt = dbConnection.prepareStatement(relQry);
            stmt.setLong(1, nodeId);
            rs = stmt.executeQuery();
            if (rs != null) {
                List result = new ArrayList();
                for (; rs.next(); ) {
                    long id = rs.getLong(1);
                    PQLValueHolder col = new PQLValueHolder(id); 
                    col.setName(rs.getString(2));
                    col.setType(extent1);
                    result.add(col);
                }
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

    protected int getSystemId(String name, String version, String timestamp) {
        int result = SYSTEM_UNDEFINED;
        if (name == null && version == null && timestamp == null) {
            return result; 
        } 
        String baseQuery = "SELECT id FROM SalsaProject WHERE ";
        boolean putAnd = false;
        if (name != null) {
            baseQuery += " project = ? ";
            putAnd = true;
        } 
        if (version != null) {
            baseQuery += (putAnd ? " AND " : "") + " version = ? ";
            putAnd = true;
        } 
        if (timestamp != null) {
            baseQuery += (putAnd ? " AND " : "") + " extraction_time = ? ";
        } 
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            if (debug > 0) System.out.println(baseQuery);
            stmt = dbConnection.prepareStatement(baseQuery);
            int ctr = 0;
            if (name != null) {
                stmt.setString(++ctr, name);
            } 
            if (version != null) {
                stmt.setString(++ctr, version);
            } 
            if (timestamp != null) {
                stmt.setString(++ctr, timestamp);
            } 
            rs = stmt.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        return result;
    }
    
    protected String getSystemIds(String name, String version, String timestamp) {
        if (name == null && version == null && timestamp == null) {
            return null; 
        } 
        String baseQuery = "SELECT id FROM SalsaProject WHERE ";
        boolean putAnd = false;
        if (name != null) {
            baseQuery += " project = ? ";
            putAnd = true;
        } 
        if (version != null) {
            baseQuery += (putAnd ? " AND " : "") + " version = ? ";
            putAnd = true;
        } 
        if (timestamp != null) {
            baseQuery += (putAnd ? " AND " : "") + " extraction_time = ? ";
        } 
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            if (debug > 0) System.out.println(baseQuery);
            stmt = dbConnection.prepareStatement(baseQuery);
            int ctr = 0;
            if (name != null) {
                stmt.setString(++ctr, name);
            } 
            if (version != null) {
                stmt.setString(++ctr, version);
            } 
            if (timestamp != null) {
                stmt.setString(++ctr, timestamp);
            } 
            rs = stmt.executeQuery();
            StringBuffer result = new StringBuffer();
            for (; rs.next(); ) {
                int id = rs.getInt(1);
                if (debug > 0) System.out.println("id: "+id);
                result.append(id).append(',');
                if (debug > 0) System.out.println("result: "+result);
            }
            if (result.length() > 0) {
                result.deleteCharAt(result.length() - 1);
                return result.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }
    
    protected int getLatestSystemId() {
        int result = SYSTEM_UNDEFINED;
        String baseQuery = "SELECT MAX(id) FROM SalsaProject";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            if (debug > 0) System.out.println(baseQuery);
            stmt = dbConnection.prepareStatement(baseQuery);
            rs = stmt.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        return result;
    }
    
    public PQLResultSet getVariable(Expression variable) {
        String name = variable.getName();
        
        String variableQuery;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            if (!USE_TEMP_TABLES) {
                variableQuery = "SELECT * FROM ReadVariable v WHERE v.cookie = ? AND v.scope = ? AND v.name = ?";
                if (debug > 0) System.out.println(variableQuery);
                stmt = dbConnection.prepareStatement(variableQuery);
                stmt.setString(1, variable.getCookie());
                stmt.setInt(2, variable.getScope());
                stmt.setString(3, name);
            } else {
                variableQuery = "SELECT * FROM " + getReadTemporaryTableName(variable);
                if (debug > 0) System.out.println(variableQuery);
                stmt = dbConnection.prepareStatement(variableQuery);
            }

            rs = stmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            
            List rows = new ArrayList();
            if (rs != null) {
                for (; rs.next(); ) {
                    PQLArtifact col; 
                    List cols = new ArrayList();
                    if (false) {
                    } else if (variable.isStruct()) {
                        Expression[] exprs = variable.getStructType();
                        int index = 0;
                        for (int i = 0; i < exprs.length; i++) {
                            Expression e = exprs[i];
                            cols.add(getArtifact(rs, index, e));
                            index++;
                            if (!e.isPrimitive() && USE_TEMP_TABLES) {
                                index++;
                            }
                        }
                    } else {
                        cols.add(getArtifact(rs, 0, variable));
                    }
                    rows.add(cols);
                }
            } 
            PQLResultSetImpl result = PQLResultSetImpl.create(variable, null);
            result.populate(rows);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }
    
    private static PQLArtifact getArtifact(ResultSet rs, int index, Expression e) throws SQLException {
        if (e.isStruct()) {
            throw new IllegalArgumentException("Nested structs not supported: " + e);
        } if (e.isPrimitive() || e.getFunction() != null) {
            if (!USE_TEMP_TABLES) {
                return new PQLArtifact(e.getType(), rs.getObject("nodeName"+index));
            } else {
                return new PQLArtifact(e.getType(), rs.getObject(index+1));
            }
        } else {
            return getArtifact(rs, index, e.getType());
        }
    }
    private static PQLArtifact getArtifact(ResultSet rs, int index, String type) throws SQLException {
        if (!USE_TEMP_TABLES) {
            return getArtifact(rs.getLong("nodeId"+index), rs.getString("nodeName"+index), type);
        } else {
            return getArtifact(rs.getLong(index+1), rs.getString(index+2), type);
        }
    }
    private static PQLArtifact getArtifact(long id, String name, String type) {
        PQLValueHolder value = new PQLValueHolder(id); 
        value.setName(name);
        value.setType(type);
        return new PQLArtifact(value); 
    }
    public int getDebug() {
        return debug;
    }

    public void setDebug(int debug) {
        this.debug = debug;
    }

    public ScriptModel getScriptModel() {
        return scriptModel;
    }
    private static void execPQL(PQL pql, String s) {
        Map r;
        r = pql.executePQL(s);
        for (Iterator iter = r.values().iterator(); iter.hasNext();) {
            PQLResultSet element = (PQLResultSet) iter.next();
            System.out.println("[[[");
            System.out.println(element);
            System.out.println("]]]");
            
        }
    }

    public static String getTemporaryTableName(Expression assignTo) {
        return getTemporaryTableName(assignTo, false, false);
    }

    public static String getReadTemporaryTableName(Expression assignTo) {
        return getTemporaryTableName(assignTo, true, false);
    }

    private static String getTemporaryTableName(Expression assignTo, boolean read, boolean session) {
        return (read ? "READ_" : "" )+ assignTo.getCookie() 
            +"_"+(session || assignTo.getScope() < 0 ? "SESSION" :  assignTo.getScope()+"")
            +"_"+assignTo.getName();
    }
}
