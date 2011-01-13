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
package org.acre.server;

import org.acre.pdm.ArgumentType;
import org.acre.pdmqueries.QueryType;
import org.acre.pdmqueries.ReturnVariable;
import org.acre.pdmqueries.ReturnVariableType;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * @author John Crupi
 */
public interface PatternQueryModelService extends Remote {
    List getProjectQueryList(String project)throws RemoteException;

    public void load()throws RemoteException;

    public void saveAll()throws RemoteException;

    public void save(String projectName)throws RemoteException;

    public void save()throws RemoteException;

    public List getGlobalQueryList()throws RemoteException;

    public List getGlobalQueryNamesList()throws RemoteException;

    public QueryType getGlobalQuery(String queryName)throws RemoteException;

    public boolean globalQueryFileExists(String queryName)throws RemoteException;

    public boolean globalQueryFileExists(QueryType query)throws RemoteException;

    public String getGlobalQueryFileName(String queryName)throws RemoteException;

    public String getGlobalQueryFile(String queryName)throws RemoteException;

    public QueryType getProjectQuery(String projectName, String queryName)throws RemoteException;

    public QueryType createNewQuery()throws RemoteException;

    public QueryType createNewAndInsertGlobalQuery()throws RemoteException;

    public QueryType createQuery(
            String name,
            String type,
            String language,
            String description,
            String relativeFilePath,
            List arguments,
            List returnVariables
            )throws RemoteException;

    public org.acre.pdmqueries.ArgumentType createArgument(
            String seq, String name,
            String type, Serializable value, String description,
            String relatedQueryName,
            String relatedQueryVariableName
            )throws RemoteException;

    public void insertProjectQuery(
            String projectName,
            String name,
            String type,
            String language,
            String description,
            String relativeFilePath,
            List arguments,
            List returnVariables,
            String queryScript
            )throws RemoteException;

    public void insertGlobalQuery(
            String name,
            String type,
            String language,
            String description,
            String relativeFilePath,
            List arguments,
            List returnVariables
            )throws RemoteException;

    public void saveProjectScript(String projectName, QueryType query, String queryScript)throws RemoteException;

    public boolean deleteGlobalScript(QueryType query)throws RemoteException;

    public void saveGlobalScript(QueryType query, String queryScript)throws RemoteException;

    public boolean checkDuplicateGlobalQuery(String queryName)throws RemoteException;

    public boolean checkDuplicateProjectQuery(String projectName, String queryName)throws RemoteException;

    public void refreshCache()throws RemoteException;

    public List getProjects()throws RemoteException;

    public void printAllProjectQueryLists()throws RemoteException;

    public void printProjectQueryList(String projectName)throws RemoteException;

    public void printGlobalQueryList()throws RemoteException;

    public ReturnVariable createReturnVariable(String name, String type, String description)throws RemoteException;

    public boolean deleteGlobalQuery(QueryType queryType)throws RemoteException;

    public void insertGlobalQuery(QueryType query)throws RemoteException;

    public QueryType cloneQuery(QueryType query)throws RemoteException;

    public ReturnVariableType cloneReturnVariableType(ReturnVariableType ret)throws RemoteException;

    public ArgumentType cloneArgument(ArgumentType arg)throws RemoteException;

    public void updateGlobalQuery(QueryType originalQuery, QueryType currentQuery)throws RemoteException;

    public List getGlobalPatternQueryModels() throws RemoteException;
}
                    
