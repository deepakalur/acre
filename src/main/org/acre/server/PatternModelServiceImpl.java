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

import org.acre.pdmqueries.QueryType;
import org.acre.dao.DAOFactory;
import org.acre.dao.PatternRepository;

import org.acre.pdm.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/**
 * @author John Crupi
 */
public class PatternModelServiceImpl extends UnicastRemoteObject implements PatternModelService {
    PatternRepository repo;

    {
        repo = DAOFactory.getPatternRepository();
    }

    public PatternModelServiceImpl() throws RemoteException {
    }

    public void refresh() {
        repo.refresh();
    }

    public void refresh(String projectName) {
        repo.refresh( projectName );
    }

    public void save(String projectName) {
        repo.save( projectName );
    }

    public void save() {
        repo.save();
    }

    public void saveAll() {
        repo.saveAll();
    }

    public List getProjects() {
        return repo.getProjects();
    }

    public List getProjectPatternModels(String projectName) {
	return repo.getProjectPatternModels( projectName );
    }

    public PDMType getProjectPatternModel(String projectName, String pdmName) {
	return repo.getProjectPatternModel( projectName, pdmName );
    }

    public List getProjectPatternModelRoles(String project, String pdmName) {
	return repo.getProjectPatternModelRoles( project, pdmName );
    }

    public List getProjectPatternModelRelationships(String project, String pdmName) {
	return repo.getProjectPatternModelRelationships( project, pdmName );
    }

    public List getGlobalPatternModels() {
	return repo.getGlobalPatternModels();
    }

    public PDMType getGlobalPatternModel(String pdmName) {
	return repo.getGlobalPatternModel( pdmName );
    }

    public List getGlobalPatternModelRoles(String pdmName) {
	return repo.getGlobalPatternModelRoles( pdmName );
    }

    public List getGlobalPatternModelRelationships(String pdmName) {
	return repo.getGlobalPatternModelRelationships( pdmName );
    }

    public void createRelationship(String name, String type, String fromRole, String toRole) {
	repo.createRelationship(name, type, fromRole,  toRole);
    }

    public void createRelationships() {
	repo.createRelationships();
    }

    public RolesType createRoles() {
	return repo.createRoles();
    }

    public RoleType createRole(
            String name,
            String type,
            String sequence,
            String typeReferenceName,
            String queryName,
            String returnVariableName,
            List arguments ) {

	return repo.createRole( name,
				type,
				sequence,
				typeReferenceName,
				queryName,
				returnVariableName,
				arguments );
    }

    public RoleType createAnnotatedRole(
            String name,
            String sequence,
            String repository,
            List arguments ) {
	return repo.createAnnotatedRole( name, sequence, repository, arguments );
    }

    public void insertGlobalPatternModel(
                String name,
                String description,
                String category,
                String tier,
                String type,
                String attributes,
                String scriptedPDMPath,
                String factModelType,
                List roles,
                List relationships) {
	repo.insertGlobalPatternModel( name, description, category, tier, type, attributes, scriptedPDMPath, factModelType, roles, relationships );
    }

    public void insertProjectPatternModel(String projectName,
					  String name,
					  String description,
					  String category,
					  String tier,
					  String type,
					  String attributes,
					  String scriptedPDMPath,
					  String factModelType,
					  List roles,
					  List relationships) {
        repo.insertProjectPatternModel( projectName, name, description, category, tier, type, attributes, scriptedPDMPath, factModelType, roles, relationships);
    }


    public void insertPatternModel(PDMList list,
				   String name,
				   String description,
				   String category,
				   String tier,
				   String type,
				   String attributes,
				   String scriptedPDMPath,
				   String factModelType,
				   List roles,
				   List relationships) {
	repo.insertPatternModel(list, name, description, category, tier, type, attributes, scriptedPDMPath, factModelType, roles, relationships );
    }

    public void refreshCache() {
	repo.refreshCache( ) ;
    }
    public void printPatternModels() {
	repo.printPatternModels();
    }

    public PDMType createNewPatternModel() {
	return repo.createNewPatternModel();
    }

    public boolean deleteGlobalPatternModel(PDMType pdmType) {
	return repo.deleteGlobalPatternModel( pdmType );
    }

    public RoleType createNewRole() {
	return repo.createNewRole();
    }

    public RelationshipType createNewRelationship() {
	return repo.createNewRelationship();
    }

    public void insertGlobalPatternModel(PDMType newPDM) {
	repo.insertGlobalPatternModel( newPDM );
    }

    public PDMType clonePatternModel(PDMType pdm) {
	return repo.clonePatternModel( pdm );
    }

    public RoleType cloneRole (RoleType role) {
	return repo.cloneRole( role );
    }
    public List cloneArgumentList(List argument) {
	return repo.cloneArgumentList( argument );
    }

    public ArgumentType cloneArgument(ArgumentType arg) {
	return repo.cloneArgument( arg );
    }

    public void updatePatternModel(PDMType originalPDM, PDMType currentPDM) {
	repo.updatePatternModel( originalPDM, currentPDM );
    }

    public List getGlobalPatternModelNames() {
	return repo.getGlobalPatternModelNames();
    }

    public List getGlobalPatternRoleNames(String pdmName) {
	return repo.getGlobalPatternRoleNames( pdmName );
    }

    public void createRoleArgument(
				   String sequence,
				   String argumentName,
				   String type,
				   String value) {
	repo.createRoleArgument( sequence, argumentName, type, value );
    }


    public void createPatternModelFromQueries(String pdmName, QueryType[] selectedQueries) {
	repo.createPatternModelFromQueries( pdmName, selectedQueries );
    }

    public void createPatternModelFromQuery(String pdmName, String queryName) {
	repo.createPatternModelFromQuery( pdmName, queryName );
    }
}
                    
