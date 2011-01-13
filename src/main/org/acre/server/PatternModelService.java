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

import org.acre.pdm.PDMType;
import org.acre.pdm.RolesType;
import org.acre.pdm.RoleType;
import org.acre.pdm.ArgumentType;
import org.acre.pdm.PDMList;
import org.acre.pdm.RelationshipType;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * @author John Crupi
 */
public interface PatternModelService extends Remote {
    public void refresh() throws RemoteException;

    public void refresh(String projectName) throws RemoteException;

    public void save(String projectName) throws RemoteException;

    public void save() throws RemoteException;

    public void saveAll() throws RemoteException;

    public List getProjects() throws RemoteException;

    public List getProjectPatternModels(String projectName) throws RemoteException;

    public PDMType getProjectPatternModel(String projectName, String pdmName) throws RemoteException;

    public List getProjectPatternModelRoles(String project, String pdmName) throws RemoteException;

    public List getProjectPatternModelRelationships(String project, String pdmName) throws RemoteException;

    public List getGlobalPatternModels() throws RemoteException;

    public PDMType getGlobalPatternModel(String pdmName) throws RemoteException;

    public List getGlobalPatternModelRoles(String pdmName) throws RemoteException;

    public List getGlobalPatternModelRelationships(String pdmName) throws RemoteException;

    public void createRelationship(String name, String type, String fromRole, String toRole) throws RemoteException;

    public void createRelationships() throws RemoteException;

    public RolesType createRoles() throws RemoteException;

    public RoleType createRole(
            String name,
            String type,
            String sequence,
            String typeReferenceName,
            String queryName,
            String returnVariableName,
            List arguments ) throws RemoteException;

    public RoleType createAnnotatedRole(
            String name,
            String sequence,
            String repository,
            List arguments ) throws RemoteException;


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
                List relationships) throws RemoteException;

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
				   List relationships) throws RemoteException;

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
			    List relationships) throws RemoteException;

    public void refreshCache() throws RemoteException;

    public void printPatternModels() throws RemoteException;

    public PDMType createNewPatternModel() throws RemoteException;

    public boolean deleteGlobalPatternModel(PDMType pdmType) throws RemoteException;

    public RoleType createNewRole() throws RemoteException;

    public RelationshipType createNewRelationship() throws RemoteException;
    public void insertGlobalPatternModel(PDMType newPDM) throws RemoteException;

    public PDMType clonePatternModel(PDMType pdm) throws RemoteException;

    public RoleType cloneRole (RoleType role) throws RemoteException;

    public List cloneArgumentList(List argument) throws RemoteException;

    public ArgumentType cloneArgument(ArgumentType arg) throws RemoteException;

    public void updatePatternModel(PDMType originalPDM, PDMType currentPDM) throws RemoteException;

    public List getGlobalPatternModelNames() throws RemoteException;

    public List getGlobalPatternRoleNames(String pdmName) throws RemoteException;

    public void createRoleArgument(String sequence,
				   String argumentName,
				   String type,
				   String value) throws RemoteException;

    public void createPatternModelFromQueries(String pdmName, QueryType[] selectedQueries) throws RemoteException;

    public void createPatternModelFromQuery(String pdmName, String queryName) throws RemoteException;
}
                    
