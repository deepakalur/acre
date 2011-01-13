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
package org.acre.lang.pql.pdbc;

import org.acre.lang.analyser.Expression;

import java.util.Arrays;
import java.util.Iterator;


/**
 * ResultSetMetaDataImpl for PQL queries
 *
 * @author Syed Ali
 */
public class PQLResultSetMetaDataImpl implements PQLResultSetMetaData, java.io.Serializable {
    //meta data        
    protected String name;
    protected String alias;
    protected String type;

    static final long serialVersionUID = 2298047483094680217L;

    //value for ql node
    protected Object value;
    protected int columnCount;
    protected String[] columnLabels;
    protected String[] columnNames;
    protected String[] columnTypes;
    protected PQLColumnHandle[] columnHandles; // todo Ali to populate this

    //data
    protected int rowCount;

    public String getAlias() {
        return alias;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public Iterator getColumnNames() {
        return Arrays.asList(columnNames).iterator();
    }

    public String getColumnLabel(int index) {
        return columnLabels[index];
    }

    public String getColumnName(int index) {
        return columnNames[index];
    }

    public String getColumnType(int index) {
        return columnTypes[index];
    }

    public int getColumnIndex(String columnName) {
        for (int i = 0; i < columnNames.length; i++) {
            if ((null != columnNames[i]) && (columnNames[i].equals(columnName)))
                return i;
        }
        return -1;
    }

    public PQLColumnHandle getColumnHandle(int index) {
        return columnHandles[index];
    }

    public String getName() {
        return name;
    }

    public int getRowCount() {
        return rowCount;
    }

    public String getType() {
        return type;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer(type + " " + name + "("+alias+") " + rowCount + " X " + columnCount + "\n");
        for (int i = 0; i < columnNames.length; i++) {
            buffer.append(columnNames[i]).append("  |  ");
        }
        buffer.append('\n');
        for (int i = 0; i < columnNames.length; i++) {
            buffer.append(columnTypes[i]).append("  |  ");
        }
        buffer.append('\n');
        return buffer.toString();
    }

    protected static PQLResultSetMetaDataImpl create(Expression e, Object qlReference) {
        PQLResultSetMetaDataImpl result = new PQLResultSetMetaDataImpl();
        result.name = e.getName();
        result.alias = e.getAlias();
        result.type = e.getType();
        result.value = qlReference;
        Expression[] columTypes = e.getStructType();
        result.columnCount = columTypes.length;
        result.columnLabels = new String[result.columnCount];
        result.columnNames = new String[result.columnCount];
        result.columnTypes = new String[result.columnCount];
        for (int i = 0; i < result.columnCount; i++) {
            result.columnLabels[i] = columTypes[i].getAlias();
            result.columnNames[i] = columTypes[i].getName();
            result.columnTypes[i] = columTypes[i].getType();
        }
        return result;
    }
    
}