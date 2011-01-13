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
package org.acre.dao;

import org.acre.pdmqueries.ArgumentType;
import org.acre.pdmqueries.QueryType;
import org.acre.pdmqueries.ReturnVariable;
import org.acre.pdmqueries.ReturnVariableType;

import java.io.Serializable;
import java.util.List;

/**
 * @author deepak.alur@Sun.com
 */
public interface PatternQueryRepository {
    List getProjectQueryList(String project);

    void load();

    void saveAll();

    void save(String projectName);

    void save();

    List getGlobalQueryList();

    List getGlobalQueryNamesList();

    QueryType getGlobalQuery(String queryName);

    boolean globalQueryFileExists(String queryName);

    boolean globalQueryFileExists(QueryType query);

    String getGlobalQueryFileName(String queryName);

    String getGlobalQueryFile(String queryName);

    QueryType getProjectQuery(String projectName, String queryName);

    QueryType createNewQuery();

    QueryType createNewAndInsertGlobalQuery();

    QueryType createQuery(
            String name,
            String type,
            String language,
            String description,
            String relativeFilePath,
            List arguments,
            List returnVariables
            );

    ArgumentType createArgument(
            String seq, String name,
            String type, Serializable value, String description,
            String relatedQueryName,
            String relatedQueryVariableName
            );

    void insertProjectQuery(
            String projectName,
            String name,
            String type,
            String language,
            String description,
            String relativeFilePath,
            List arguments,
            List returnVariables,
            String queryScript
            );

    void insertGlobalQuery(
            String name,
            String type,
            String language,
            String description,
            String relativeFilePath,
            List arguments,
            List returnVariables
            );

    void saveProjectScript(String projectName, QueryType query, String queryScript);

    boolean deleteGlobalScript(QueryType query);

    void saveGlobalScript(QueryType query, String queryScript);

    boolean checkDuplicateGlobalQuery(String queryName);

    boolean checkDuplicateProjectQuery(String projectName, String queryName);

    void refreshCache();

    List getProjects();

    void printAllProjectQueryLists();

    void printProjectQueryList(String projectName);

    void printGlobalQueryList();

    ReturnVariable createReturnVariable(String name, String type, String description);

    boolean deleteGlobalQuery(QueryType queryType);

    void insertGlobalQuery(QueryType query);

    QueryType cloneQuery(QueryType query);

    ReturnVariableType cloneReturnVariableType(ReturnVariableType ret);

    ArgumentType cloneArgument(ArgumentType arg);

    void updateGlobalQuery(QueryType originalQuery, QueryType currentQuery);
}
