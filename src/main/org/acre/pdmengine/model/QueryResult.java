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
package org.acre.pdmengine.model;

import org.acre.pdmqueries.QueryType;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author rajmohan@Sun.com
 * @version Nov 22, 2004 10:23:12 PM
 */
public interface QueryResult extends Result {

    QueryType getQuery();

    void setQuery(QueryType query);

    String getQueryStatement();

    void setQueryStatement(String queryStatement);

    String[] getVariableKeys();

    String [] getArtifactNames(Object key);

    Artifact[] getArtifacts();
    
    Artifact[] getArtifacts(int columnNumber);

    String getArtifactType();

    Artifact findArtifact(String artifactName);

    List matchArtifacts(Pattern pattern);

    RoleResult getParent();
}
