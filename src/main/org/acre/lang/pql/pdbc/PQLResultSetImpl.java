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
import ca.uwaterloo.cs.ql.fb.Node;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * ResultSet for PQL queries
 *
 * @author Syed Ali, Yury Kamen
 */
public class PQLResultSetImpl implements PQLResultSet, java.io.Serializable {
    private PQLArtifact[][] values;
    private PQLResultSetMetaDataImpl metadata;
    private Object intervalValue;

    static final long serialVersionUID = 7950455423257636162L;
    
    public static PQLResultSetImpl create(Expression e, Object qlReference) {
        PQLResultSetImpl result = new PQLResultSetImpl();
        result.metadata = PQLResultSetMetaDataImpl.create(e, qlReference);
        return result;
    }

    public Iterator iterator() {
        return Arrays.asList(getValues()).iterator();
    }
    
    public void populate(Node[] rows) {
        metadata.rowCount = rows.length;
        values = new PQLArtifact[metadata.rowCount][metadata.getColumnCount()];
        for (int i = 0; i < rows.length; i++) {
            values[i][0] = new PQLArtifact("String", rows[i].get());
        }
    }

    public void populate(List rows) {
        metadata.rowCount = rows.size();
        values = new PQLArtifact[metadata.rowCount][metadata.getColumnCount()];
        for (int i = 0; i < rows.size(); i++) {
            Object value = rows.get(i);
            if (value instanceof List) {
                List row = (List) value;
                if (row.size() != metadata.getColumnCount()) {
                    throw new IllegalArgumentException("Variable and ResultSet column could not match: Variable(" + metadata.getColumnCount() + ") != ResultSet(" + row.size() + ")");
                }
                for (int j = 0; j < row.size(); j++) {
                    values[i][j] = (PQLArtifact) row.get(j);
                }
            } else {
                values[i][0] = (PQLArtifact) value;
            }
        }
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer(metadata.toString());
        buffer.append("-------------------------------------------------------------------\n");
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[i].length; j++) {
                buffer.append(values[i][j]).append("  |  ");
            }
            buffer.append('\n');
        }
        buffer.append("-------------------------------------------------------------------\n");
        return buffer.toString();
    }

    public PQLArtifact[] getRow(int index) {
        return values[index];
    }

    public PQLArtifact getValue(int row, int col) {
        return values[row][col];
    }

    public PQLArtifact[][] getValues() {
        return values;
    }

    public Object getIntervalValue() {
        return intervalValue;
    }

    public void setIntervalValue(Object intervalValue) {
        this.intervalValue = intervalValue;
    }

    public PQLResultSetMetaData getMetaData() {
        return metadata;
    }
}