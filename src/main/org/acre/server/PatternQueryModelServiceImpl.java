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
import org.acre.dao.DAOFactory;
import org.acre.dao.PatternQueryRepository;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/**
 * @author John Crupi
 */
public class PatternQueryModelServiceImpl extends UnicastRemoteObject implements PatternQueryModelService {
    PatternQueryRepository repo;

    public PatternQueryModelServiceImpl() throws RemoteException {
        super();
        repo = DAOFactory.getPatternQueryRepository();
    }
    public List getProjectQueryList(String project) {
	return repo.getProjectQueryList(project);
    }
    public void load() {
	repo.load();
    }
    public void saveAll() {
	repo.saveAll();
    }
    public void save(String projectName) {
	repo.save(projectName);
    }
    public void save() {
	repo.save();
    }
    public List getGlobalQueryList() {
	return repo.getGlobalQueryList();
    }
    public List getGlobalQueryNamesList() {
	return repo.getGlobalQueryNamesList();
    }
    public QueryType getGlobalQuery(String queryName) {
	return repo.getGlobalQuery(queryName);
    }
    public boolean globalQueryFileExists(String queryName) {
	return repo.globalQueryFileExists(queryName);
    }
    public boolean globalQueryFileExists(QueryType query) {
	return repo.globalQueryFileExists( query);
    }
    public String getGlobalQueryFileName(String queryName) {
	return repo.getGlobalQueryFileName( queryName);
    }
    public String getGlobalQueryFile(String queryName) {
	return repo.getGlobalQueryFile( queryName);
    }
    public QueryType getProjectQuery(String projectName, String queryName) {
	return repo.getProjectQuery( projectName, queryName);
    }
    public QueryType createNewQuery() {
	return repo.createNewQuery();
    }
    public QueryType createNewAndInsertGlobalQuery() {
	return repo.createNewAndInsertGlobalQuery();
    }

    public QueryType createQuery(String name,
                                 String type,
                                 String language,
                                 String description,
                                 String relativeFilePath,
                                 List arguments,
                                 List returnVariables) throws RemoteException {
        return repo.createQuery( name,
				 type,
				 language,
				 description,
				 relativeFilePath,
				 arguments,
				 returnVariables );
    }

    public org.acre.pdmqueries.ArgumentType createArgument(String seq, String name,
                                       String type, Serializable value, String description,
                                       String relatedQueryName,
                                       String relatedQueryVariableName) throws RemoteException {
        return repo.createArgument(seq,
                name,
                type,
                value,
                description,
                relatedQueryName,
                relatedQueryVariableName);
    }


    public void insertProjectQuery(String projectName,
                                   String name,
                                   String type,
                                   String language,
                                   String description,
                                   String relativeFilePath,
                                   List arguments,
                                   List returnVariables,
                                   String queryScript) throws RemoteException {
	 repo.insertProjectQuery( projectName,
					name,
					type,
					language,
					description,
					relativeFilePath,
					arguments,
					returnVariables,
					queryScript );

    }

    public void insertGlobalQuery(String name,
                                  String type,
                                  String language,
                                  String description,
                                  String relativeFilePath,
                                  List arguments,
                                  List returnVariables) throws RemoteException {
	 repo.insertGlobalQuery( name,
				       type,
				       language,
				       description,
				       relativeFilePath,
				       arguments,
				       returnVariables);

    }

    public void saveProjectScript(String projectName, QueryType query, String queryScript) throws RemoteException {
        repo.saveProjectScript(projectName, query, queryScript);
    }

    public boolean deleteGlobalScript(QueryType query) throws RemoteException {
        return repo.deleteGlobalScript(query);
    }

    public void saveGlobalScript(QueryType query, String queryScript) throws RemoteException {
        repo.saveGlobalScript( query, queryScript);
    }

    public boolean checkDuplicateGlobalQuery(String queryName) throws RemoteException {
        return repo.checkDuplicateGlobalQuery(queryName);
    }
    public boolean checkDuplicateProjectQuery(String projectName, String queryName) throws RemoteException {
	return repo.checkDuplicateProjectQuery( projectName, queryName );
    }
    public void refreshCache() throws RemoteException {
        repo.refreshCache();
    }

    public List getProjects() throws RemoteException {
        return repo.getProjects();
    }

    public void printAllProjectQueryLists() throws RemoteException {
	repo.printAllProjectQueryLists();
    }

    public void printProjectQueryList(String projectName) throws RemoteException {
	repo.printProjectQueryList(projectName);
    }
    public void printGlobalQueryList() throws RemoteException {
	repo.printGlobalQueryList();
    }

    public ReturnVariable createReturnVariable(String name, String type, String description) throws RemoteException {
	return repo.createReturnVariable( name,  type, description);
    }

    public boolean deleteGlobalQuery(QueryType queryType) throws RemoteException {
        return repo.deleteGlobalQuery( queryType );
    }

    public void insertGlobalQuery(QueryType query) throws RemoteException {
         repo.insertGlobalQuery(query);
    }

    public QueryType cloneQuery(QueryType query) throws RemoteException {
	return repo.cloneQuery(query);
    }

    public ReturnVariableType cloneReturnVariableType(ReturnVariableType ret) throws RemoteException {
	return repo.cloneReturnVariableType(ret);
    }

    public ArgumentType cloneArgument(org.acre.pdm.ArgumentType arg) throws RemoteException {
        return null;
    }

    public void updateGlobalQuery(QueryType originalQuery, QueryType currentQuery) throws RemoteException {
	repo.updateGlobalQuery(originalQuery, currentQuery);
    }

    public List getGlobalPatternQueryModels() throws RemoteException {
        return repo.getGlobalQueryList();
    }

}
