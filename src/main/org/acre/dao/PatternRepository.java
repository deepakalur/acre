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

import org.acre.pdm.*;
import org.acre.pdmqueries.QueryType;

import java.io.InputStream;
import java.util.List;

/**
 * @author rajmohan@Sun.com
 */
public interface PatternRepository {
    void load();

    void setGlobalPatternModel(InputStream is);

    void refresh();

    void refresh(String projectName);

    void save(String projectName);

    void save();

    void saveAll();

    List getProjects();

    List getProjectPatternModels(String projectName);

    PDMType getProjectPatternModel(String projectName, String pdmName);

    List getProjectPatternModelRoles(String project, String pdmName);

    List getProjectPatternModelRelationships(String project, String pdmName);

    List getGlobalPatternModels();

    PDMType getGlobalPatternModel(String pdmName);

    List getGlobalPatternModelRoles(String pdmName);

    List getGlobalPatternModelRelationships(String pdmName);

    RelationshipType createRelationship(String name, String type, String fromRole, String toRole);

    RelationshipsType createRelationships();

    RolesType createRoles();

    RoleType createRole(
            String name,
            String type,
            String sequence,
            String typeReferenceName,
            String queryName,
            String returnVariableName,
            List arguments
            );

    RoleType createAnnotatedRole(
            String name,
            String sequence,
            String repository,
            List arguments
            );

    void insertGlobalPatternModel(
                String name,
                String description,
                String category,
                String tier,
                String type,
                String attributes,
                String scriptedPDMPath,
                String factModelType,
                List roles,
                List relationships);

    void insertProjectPatternModel(String projectName,
                String name,
                String description,
                String category,
                String tier,
                String type,
                String attributes,
                String scriptedPDMPath,
                String factModelType,
                List roles,
                List relationships);

    void insertPatternModel(PDMList list,
                String name,
                String description,
                String category,
                String tier,
                String type,
                String attributes,
                String scriptedPDMPath,
                String factModelType,
                List roles,
                List relationships);

    void refreshCache();

    void printPatternModels();

    PDMType createNewPatternModel();

    boolean deleteGlobalPatternModel(PDMType pdmType);

    RoleType createNewRole();

    RelationshipType createNewRelationship();

    void insertGlobalPatternModel(PDMType newPDM);

    PDMType clonePatternModel(PDMType pdm);

    RoleType cloneRole (RoleType role);
    
    RelationshipType cloneRelationship(RelationshipType rel);

    List cloneArgumentList(List argument);

    ArgumentType cloneArgument(ArgumentType arg);

    void updatePatternModel(PDMType originalPDM, PDMType currentPDM);

    List getGlobalPatternModelNames();

    List getGlobalPatternRoleNames(String pdmName);

    ArgumentType createRoleArgument(
            String sequence,
            String argumentName,
            String type,
            String value);

    void createPatternModelFromQueries(String pdmName, QueryType[] selectedQueries);

    void createPatternModelFromQuery(String pdmName, String queryName);
}
                    