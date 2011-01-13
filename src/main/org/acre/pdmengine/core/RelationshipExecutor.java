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

import org.acre.common.AcreStringUtil;
import org.acre.dao.DAOFactory;
import org.acre.dao.PDMXMLConstants;
import org.acre.dao.PatternRepository;
import org.acre.pdmengine.PatternEngineException;
import org.acre.pdmengine.model.PatternResult;
import org.acre.pdmengine.model.QueryResult;
import org.acre.pdmengine.model.RelationshipResult;
import org.acre.pdmengine.model.RoleResult;
import org.acre.pdmengine.model.impl.RelationshipResultImpl;
import org.acre.pdmengine.pqe.PQLEngineFacade;
import org.acre.pdmengine.pqe.PQLVariable;
import org.acre.pdmengine.pqe.PatternBaseCommand;
import org.acre.pdmengine.pqe.PatternCommandFactory;
import org.acre.pdmengine.util.PMTypeUtil;
import org.acre.pdmengine.util.PatternEngineUtil;
import org.acre.pdm.*;

import javax.xml.bind.JAXBException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * User: rajmohan@sun.com
 * Date: Nov 7, 2004
 * Time: 3:17:14 PM
 */
public class RelationshipExecutor {

    private PQLEngineFacade pqlEngineFacade;

    public RelationshipExecutor(PQLEngineFacade pqlEngineFacade) {
        this.pqlEngineFacade = pqlEngineFacade;
    }

    /**
     * Preprocess & Execute all relationships specified in PDM,
     * and accumulate the results in PatternResult
     * @param pdm
     * @param patternResult
     */
    public void executeRelationships(PDMType pdm, PatternResult patternResult) {
        // evaluate relationships
        List relationshipResults = new ArrayList();
        RelationshipsType relationships = pdm.getRelationships();

        if ( relationships == null )
            return;

        String patternName = pdm.getName();

        boolean anyJoinType = false;
        // Pass1 : PreProcess all relationships

        ListIterator iter = relationships.getRelationship().listIterator();
        while ( iter.hasNext() ) {
            RelationshipType relationship = (RelationshipType) iter.next();

            simplifyRoleReferences(patternName, relationship);

            // resolve any reference to role="PatternName"
            List newRelationships = preProcessRelationship(pdm, relationship);

            if ( newRelationships.size() > 0) {
                // Replace current relationship, with newly minted relationships
                iter.remove();
                Iterator newRelItr = newRelationships.iterator();
                while ( newRelItr.hasNext() ) {
                    iter.add(newRelItr.next());
                }
                if ( newRelationships.size() > 1)
                    anyJoinType = true;
            }
        }

        // Pass2: Execute all relationships
        iter = relationships.getRelationship().listIterator();
        while ( iter.hasNext() ) {
            RelationshipType relationship = (RelationshipType) iter.next();

            RelationshipResultImpl relationshipResult;

            // get FromRole relationshipResult set
            RoleResult fromRoleResult = patternResult.getRoleReference(relationship.getFromRole());

            // get ToRole relationshipResult set
            RoleResult toRoleResult = patternResult.getRoleReference(relationship.getToRole());

            relationshipResult = (RelationshipResultImpl)executeRelationship(patternResult,
                                        relationship,
                                        fromRoleResult,
                                        toRoleResult);

            // temporary hack - convert all expanded relationships to ANY Join Type
            // ultimately, this requires a change in the Pattern Schema to support
            // ALL or ANY Relationship Types
            if ( anyJoinType )
                relationshipResult.setJoinType(RelationshipResultImpl.JOIN_TYPE_ANY);
            relationshipResults.add(relationshipResult);
        }
        // add relationship models to the patternResult
        patternResult.setRelationships(relationshipResults);
    }

    // rule1: Translate RoleReference = "thisPattern.role" to "role"
    private void simplifyRoleReferences(String patternName, RelationshipType relationship) {
        PatternEngineUtil.RoleReference rolereference;

        rolereference = PatternEngineUtil.getRoleReference(relationship.getFromRole());
        if ( patternName.equalsIgnoreCase(rolereference.pattern))
            relationship.setFromRole(rolereference.role);

        rolereference = PatternEngineUtil.getRoleReference(relationship.getToRole());
        if ( patternName.equalsIgnoreCase(rolereference.pattern))
            relationship.setToRole(rolereference.role);

    }

    private List preProcessRelationship(PDMType pdm, RelationshipType relationship) {
        List newRelationships = new ArrayList();
        PatternRepository patternRepository = DAOFactory.getPatternRepository();


        String [] fromRoles = null, toRoles = null;

        RoleType fromRoleType = PMTypeUtil.getRoleType(pdm, relationship.getFromRole());
        if ( (fromRoleType != null ) &&
                PDMXMLConstants.ROLE_TYPE_PDM.equalsIgnoreCase(fromRoleType.getType()) ) {
                String referedPattern;
                referedPattern = PMTypeUtil.getReferedPatternName(fromRoleType);

                fromRoles = PMTypeUtil.getPatternRoleNames(referedPattern);
                // expand role to pattern.role
                for ( int i=0; i < fromRoles.length; i++) {
                    fromRoles[i] = relationship.getFromRole() +"."+fromRoles[i];
                }

        }

        RoleType toRoleType = PMTypeUtil.getRoleType(pdm, relationship.getToRole());
        if ( (toRoleType != null ) &&
                PDMXMLConstants.ROLE_TYPE_PDM.equalsIgnoreCase(toRoleType.getType()) ) {
                String referedPattern;
                referedPattern = PMTypeUtil.getReferedPatternName(toRoleType);

                toRoles = PMTypeUtil.getPatternRoleNames(referedPattern);
                for ( int i=0; i < toRoles.length; i++) {
                    toRoles[i] = relationship.getToRole() +"." + toRoles[i];
                }
        }

        if ( AcreStringUtil.isEmpty(fromRoles) && AcreStringUtil.isEmpty(toRoles))
            return newRelationships;  // no pattern references, return

        if ( AcreStringUtil.isEmpty(fromRoles) )
            fromRoles = new String[]{relationship.getFromRole()};

        if (AcreStringUtil.isEmpty(toRoles))
            toRoles = new String[]{relationship.getToRole()};

        for ( int i=0; i < fromRoles.length; i++ ) {
            for ( int j=0; j < toRoles.length; j++) {
                RelationshipType newRelationship;
                newRelationship = patternRepository.cloneRelationship(relationship);
                newRelationship.setFromRole(fromRoles[i]);
                newRelationship.setToRole(toRoles[j]);
                newRelationships.add(newRelationship);
            }
        }
        return newRelationships;
    }


    /**
     * Evaluate a relationship type - calls, creates, uses, etc.
     * @param parentPatternResult - relationship's parent PatternResult
     * @param relationship - relationship to be evaluated
     * @param fromRole - relationhip's left role operand
     * @param toRole - relationship's right role operand
     * @return RelationshipResult - result of evaluation
     */
    public RelationshipResult executeRelationship(PatternResult parentPatternResult, RelationshipType relationship,
                                 RoleResult fromRole, RoleResult toRole) {


        RelationshipResultImpl relationshipResult = new RelationshipResultImpl(relationship, parentPatternResult);
        relationshipResult.setFromRole(fromRole);
        relationshipResult.setToRole(toRole);

        PatternCommandFactory factory =
                    PatternCommandFactory.getInstance();

        PatternBaseCommand relationalOperator =
                factory.getPDMRelationalOperator(relationship.getType());

        String pdmName = "transient";
        if ( parentPatternResult != null )
            pdmName = parentPatternResult.getName();

        String resultVariable = pdmName + "_" + relationship.getName();

        String operandFromVariable = fromRole.getVariableName();

        QueryResult fromQR = (QueryResult)fromRole.getRoleResult();
        fromQR.getArtifactType();
        PQLVariable fromEntity = new PQLVariable(operandFromVariable, fromQR.getArtifactType());

        String operandToVariable = toRole.getVariableName();

        QueryResult toQR = (QueryResult)fromRole.getRoleResult();
        PQLVariable toEntity = new PQLVariable(operandToVariable, toQR.getArtifactType());

        PQLVariable resultEntity;

        resultEntity = relationalOperator.execute(pqlEngineFacade,
                                            resultVariable,
                                            fromEntity,
                                            toEntity,
                                            true);

        relationshipResult.setResult(resultEntity.getPqlResultMap());

        return relationshipResult;
    }

    // List<RelationshipResult>
    public List discoverRelationships(PatternResult sourcePattern, PatternResult targetPattern,
                                                      String relationshipType[]) {
        if ( sourcePattern.getRoles().isEmpty() || targetPattern.getRoles().isEmpty() )
                return null;

        Iterator sourcePDMRoles = sourcePattern.getRoles().iterator();
        List relationshipResultList = new ArrayList();

        while ( sourcePDMRoles.hasNext()) {
            RoleResult sourceRole;
            sourceRole = (RoleResult)sourcePDMRoles.next();

            Iterator targetPDMRoles = targetPattern.getRoles().iterator();
            while ( targetPDMRoles.hasNext()) {
                RoleResult targetRole;
                targetRole = (RoleResult)targetPDMRoles.next();

                if( sourceRole == targetRole)
                    continue;
                
                List result;
                result = discoverRelationship(sourcePattern.getName(), sourceRole,
                        targetPattern.getName(), targetRole, relationshipType);
                relationshipResultList.addAll(result);
            }
        }

        return relationshipResultList;
    }

    // List<RelationshipResult>
    private List discoverRelationship( String sourcePDMName, RoleResult sourceRole,
                                        String targetPDMName, RoleResult targetRole,
                                        String relOpTypes[]) {
        RelationshipType relationshipType;

        List resultList = new ArrayList();

        for ( int idx=0; idx < relOpTypes.length; idx++ ) {
            ObjectFactory objectFactory = new ObjectFactory();
            try {
                relationshipType = objectFactory.createRelationshipType();
                relationshipType.setName(relOpTypes[idx]);
                relationshipType.setType(relOpTypes[idx]);
                relationshipType.setFromRole(sourcePDMName + "." + sourceRole.getName());
                relationshipType.setToRole(targetPDMName + "." + targetRole.getName());
            } catch (JAXBException e) {
                throw new PatternEngineException("Error creating RelationshipType : " +
                        e.getMessage(), e);
            }
            RelationshipResultImpl result;
            result = (RelationshipResultImpl)executeRelationship(null, relationshipType, sourceRole, targetRole);
            if ( result != null ) {
                if ( !result.isEmpty())
                    resultList.add(result);
            }
        }
        return resultList;
    }


    /**
     * Discover relationships among Roles in the given PDM
     * @param pattern
     * @param relationshipType
     * @return
     */
    // List<RelationshipResult>
    public List discoverRelationships(PatternResult pattern, String relationshipType[]) {
        if ( pattern.getRoles().isEmpty() )
                return null;

        Iterator pdmRoles = pattern.getRoles().iterator();
        List relationshipResultList = new ArrayList();

        while ( pdmRoles.hasNext()) {
            RoleResult sourceRole;
            sourceRole = (RoleResult)pdmRoles.next();

            Iterator targetPDMRoles = pattern.getRoles().iterator();
            while ( targetPDMRoles.hasNext()) {
                RoleResult targetRole;
                targetRole = (RoleResult)targetPDMRoles.next();

                if ( sourceRole != targetRole ) {
                    List result;
                    result = discoverRelationship(pattern.getName(), sourceRole,
                            pattern.getName(), targetRole, relationshipType);
                    relationshipResultList.addAll(result);
                }
            }
        }

        return relationshipResultList;
    }


}

