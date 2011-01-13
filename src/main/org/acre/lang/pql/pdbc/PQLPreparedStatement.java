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


import java.util.Collection;
import java.util.Map;

/**
 * @author Deepak.Alur@Sun.com
 *         Date: Sep 27, 2004
 *         Time: 2:33:52 PM
 */
public interface PQLPreparedStatement extends PQLStatement {
   public void setParameter(String name, Object value) throws PQLParameterException;
   public void setParameter(String name, int value) throws PQLParameterException;
   public void setParameter(String name, float value) throws PQLParameterException;
   public void setParameter(String name, boolean value) throws PQLParameterException;
   public void setParameter(String name, Collection values) throws PQLParameterException;
   public void setParameter(String name, PQLResultSet result, String columnName) throws PQLParameterException;
   public void setParameter(String name, PQLResultSet result, int columnId) throws PQLParameterException;
   public Object getParameter(String name) throws PQLParameterException;
   public void clearParameters();
   public String[] getReturnVariableNames() throws PQLException;
   public Map executeQuery() throws PQLException, PQLParameterException;
}
