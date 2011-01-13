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

import java.util.Iterator;


/**
 * ResultSetMetaDataImpl for PQL queries
 *
 * @author Syed Ali
 */
public interface PQLResultSetMetaData {
    public String getAlias();
    public int getColumnCount();
    public Iterator getColumnNames();
    public String getColumnLabel(int index);
    public String getColumnName(int index);
    public String getColumnType(int index);
    public int getColumnIndex(String columnName);
    public PQLColumnHandle getColumnHandle(int index);
    public String getName();
    public int getRowCount();
    public String getType();
}