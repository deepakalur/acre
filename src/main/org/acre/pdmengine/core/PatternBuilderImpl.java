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
package org.acre.pdmengine.core;

import org.acre.pdm.PDMType;
import org.acre.pdm.RoleType;
import org.acre.pdm.RolesType;
import org.acre.config.ConfigService;
import org.acre.dao.DAOFactory;
import org.acre.dao.PDMXMLConstants;
import org.acre.dao.PatternRepository;
import org.acre.model.metamodel.PopulateMetaModel;
import org.acre.pdmengine.PatternBuilder;
import org.acre.pdmengine.model.*;
import org.acre.pdmengine.pqe.PQLEngineFacade;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * PDMBuilder is responsible for dynamically composing high-level Patterns
 * from previously discovered patterns & artifacts
 */
public class PatternBuilderImpl implements PatternBuilder {
    private PatternRepository patternRepository = DAOFactory.getPatternRepository();
    private Map transientRoles = new HashMap();
    private PDMType rootPDMType;
    private PQLEngineFacade pqlEngineFacade;
    public PatternBuilderImpl(PQLEngineFacade pqlEngineFacade) {
        this.pqlEngineFacade = pqlEngineFacade;
    }

    public RoleType createRole(String roleName, RoleResult roleResult) {
        Result  result = roleResult.getRoleResult();
        RoleType roleType = null;
        if (result instanceof QueryResult) {
            QueryResult queryRS = (QueryResult)result;
            roleType = createRole(roleName, queryRS.getArtifacts());
        }
        else if (result instanceof PatternResult) {
            PatternResult patternResult = (PatternResult)result;
            roleType = createRole(roleName, patternResult.getPdm());
        }
        return roleType;
    }
    public RoleType createRole(String roleName, Artifact artifacts[]) {
            transientRoles.put(roleName, artifacts);
            return patternRepository.createAnnotatedRole(roleName, "0", "@"+roleName, null);
    }

    public RoleType createRole(String roleName, PDMType referedPattern) {
            transientRoles.put(roleName, referedPattern);
            return patternRepository.createRole(roleName,
                    PDMXMLConstants.ROLE_TYPE_PDM,
                    "0",
                    referedPattern.getName(),
                    null, null, null);
    }
    public PDMType createPattern(String patternName, RoleType[] roles) {

        // create new root PDMType
        PDMType newPDMType;
        newPDMType = patternRepository.createNewPatternModel();
        newPDMType.setName(patternName);

        RolesType rolesType;
        rolesType = newPDMType.getRoles();

        // create dynamic PDM transientRoles
        for ( int idx=0; idx < roles.length; idx++) {
            RoleType roleType = roles[idx];

            if ( transientRoles.containsKey(roleType.getName()) == false) {
                throw new RuntimeException("Missing Role in PDMBuilder : " + roleType.getName());
            }

            newPDMType.getRoles().getRole().add(roleType);
        }

        rootPDMType = newPDMType;
        return newPDMType;
    }

    public void savePattern() {
        savePattern(rootPDMType);
    }

    public void savePattern(PDMType pdmType) {

        // TODO - Save annotated artifacts to repository
        for ( Iterator itr = transientRoles.keySet().iterator(); itr.hasNext(); ) {
            String roleName = (String)itr.next();
            Object o = transientRoles.get(roleName);
            System.out.println(roleName);
            if ( o instanceof Artifact[]) {
                String filePath = ConfigService.getInstance().
                        getPDMAnnotationsFilePath("@"+roleName);
                System.out.println(filePath);
                try {
                    OutputStream os = new FileOutputStream(filePath);
                    Properties properties = new Properties();
                    Artifact[] artifacts = (Artifact[]) o;
                    StringBuffer artifactIds = new StringBuffer(artifacts.length * 7);
                    for ( int i=0; i<artifacts.length; i++) {
                        Artifact artifact = artifacts[i];
                        String id = artifact.getAttribute(pqlEngineFacade.getPQLConnection(),
                                "id");
                        if ( i > 0)
                            artifactIds.append(",");
                        artifactIds.append(id);
                    }

                    String mappedType;
                    mappedType = PopulateMetaModel.getMappedName1(artifacts[0].getType());
                    properties.setProperty(mappedType, artifactIds.toString());
                    properties.store(os, null);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }


            }
            else if ( o instanceof PDMType ) {
                patternRepository.insertGlobalPatternModel((PDMType)o);
            }
        }
        patternRepository.insertGlobalPatternModel(pdmType);
        patternRepository.save();
    }
}
