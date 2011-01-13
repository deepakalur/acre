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


import org.acre.pdm.ArgumentType;
import org.acre.pdm.PDMType;
import org.acre.pdm.RelationshipType;
import org.acre.pdm.RoleType;
import org.acre.common.AcreErrors;
import org.acre.common.AcreStringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Deepak.Alur@Sun.com
 *         Date: Jun 14, 2004
 *         Time: 2:20:26 PM
 */
public class PDMValidator {

    private final ArrayList pdmCategories = new ArrayList();
    private final ArrayList pdmTypes = new ArrayList();
    private final ArrayList pdmRelationshipTypes = new ArrayList();
    private final ArrayList pdmTiers= new ArrayList();
    private final ArrayList pdmRoleTypes = new ArrayList();
    private final ArrayList pdmFactModelTypes = new ArrayList();
    private final ArrayList pdmRoleArgumentTypes = new ArrayList();

    private static PDMValidator ourInstance = new PDMValidator();

    private PDMValidator() {
        initValidValues();
    }

    public static PDMValidator getInstance() {
        return ourInstance;
    }

    public boolean isValidRoleType(String roleType) {
        if (roleType == null)
            return false;

        return pdmRoleTypes.contains(roleType);
    }

    public boolean isValidRelationshipType(String relType) {
        if (relType == null)
            return false;

        return pdmRelationshipTypes.contains(relType);
    }

    public boolean isValidPDMType(String pdmType) {
        if (pdmType == null)
            return false;
        return pdmTypes.contains(pdmType);
    }

    public boolean isValidPDMCategory(String category) {
        if (category == null)
            return false;
        return pdmCategories.contains(category);
    }

    public boolean isValidTier (String tier) {
        if (tier == null)
            return false;

        return pdmTiers.contains(tier);
    }

    private void initValidValues() {
        // PDM Category
        pdmCategories.add(PDMXMLConstants.PDM_CATEGORY_COREJ2EEPATTERNS);
        pdmCategories.add(PDMXMLConstants.PDM_CATEGORY_GOF);
        pdmCategories.add(PDMXMLConstants.PDM_CATEGORY_OTHER);

        // PDM Types
        pdmTypes.add(PDMXMLConstants.PDM_TYPE_MODEL_PDM);
        pdmTypes.add(PDMXMLConstants.PDM_TYPE_MODEL_GROOVY);
        pdmTypes.add(PDMXMLConstants.PDM_TYPE_MODEL_PQLPDM);
        pdmTypes.add(PDMXMLConstants.PDM_TYPE_MODEL_QLPDM);
        pdmTypes.add(PDMXMLConstants.PDM_TYPE_MODEL_JAVAPDM);

        // Relationship types
        pdmRelationshipTypes.add(PDMXMLConstants.RELATIONSHIP_TYPE_CREATES);
        pdmRelationshipTypes.add(PDMXMLConstants.RELATIONSHIP_TYPE_CALLS);
        pdmRelationshipTypes.add(PDMXMLConstants.RELATIONSHIP_TYPE_CONTAINS);
        pdmRelationshipTypes.add(PDMXMLConstants.RELATIONSHIP_TYPE_USES);
        pdmRelationshipTypes.add(PDMXMLConstants.RELATIONSHIP_TYPE_INSTANTIATES);
        pdmRelationshipTypes.add(PDMXMLConstants.RELATIONSHIP_TYPE_ISOFTYPE);
        pdmRelationshipTypes.add(PDMXMLConstants.RELATIONSHIP_TYPE_THROWS);
        pdmRelationshipTypes.add(PDMXMLConstants.RELATIONSHIP_TYPE_CATCHES);
        pdmRelationshipTypes.add(PDMXMLConstants.RELATIONSHIP_TYPE_EXTENDS);
        pdmRelationshipTypes.add(PDMXMLConstants.RELATIONSHIP_TYPE_IMPLEMENTS);

        // tier types
        pdmTiers.add(PDMXMLConstants.PDM_TIER_TYPE_ANY);
        pdmTiers.add(PDMXMLConstants.PDM_TIER_TYPE_BUSINESS);
        pdmTiers.add(PDMXMLConstants.PDM_TIER_TYPE_PRESENTATION);
        pdmTiers.add(PDMXMLConstants.PDM_TIER_TYPE_INTEGRATION);
        pdmTiers.add(PDMXMLConstants.PDM_TIER_TYPE_OTHER);
        pdmTiers.add(PDMXMLConstants.PDM_TIER_TYPE_SUBSYSTEM);


        // role types
        pdmRoleTypes.add(PDMXMLConstants.ROLE_TYPE_PDM);
        pdmRoleTypes.add(PDMXMLConstants.ROLE_TYPE_DYNAMIC_PDM);
        pdmRoleTypes.add(PDMXMLConstants.ROLE_TYPE_QUERY);
        pdmRoleTypes.add(PDMXMLConstants.ROLE_TYPE_ABSTRACT);
        pdmRoleTypes.add(PDMXMLConstants.ROLE_TYPE_ANNOTATED);

        // role arguments type
        pdmRoleArgumentTypes.add(PDMXMLConstants.PDM_ROLE_ARGUMENT_TYPE_STRING);
        pdmRoleArgumentTypes.add(PDMXMLConstants.PDM_ROLE_ARGUMENT_TYPE_INTEGER);
        pdmRoleArgumentTypes.add(PDMXMLConstants.PDM_ROLE_ARGUMENT_TYPE_DECIMAL);
        pdmRoleArgumentTypes.add(PDMXMLConstants.PDM_ROLE_ARGUMENT_TYPE_BOOLEAN);
        pdmRoleArgumentTypes.add(PDMXMLConstants.PDM_ROLE_ARGUMENT_TYPE_ROLEPATH);

        // Fact Model Types
        pdmFactModelTypes.add(PDMXMLConstants.PDM_FACT_MODEL_JAVA);
        pdmFactModelTypes.add(PDMXMLConstants.PDM_FACT_MODEL_WSDL);
        pdmFactModelTypes.add(PDMXMLConstants.PDM_FACT_MODEL_JAVAWSDL_HYBRID);

    }

    public ArrayList getPdmCategories() {
        return pdmCategories;
    }

    public ArrayList getPdmTypes() {
        return pdmTypes;
    }

    public ArrayList getPdmRelationshipTypes() {
        return pdmRelationshipTypes;
    }

    public ArrayList getPdmTiers() {
        return pdmTiers;
    }

    public ArrayList getPdmRoleTypes() {
        return pdmRoleTypes;
    }

    public ArrayList getPdmFactModelTypes() {
        return pdmFactModelTypes;
    }

    public AcreErrors validatePDM(PDMType pdm) {
        AcreErrors errors = new AcreErrors();

        if (! pdmTypes.contains(pdm.getType())) {
            errors.addError(PDMErrors.PDM_TYPE_INVALID, "Invalid PDM Type '"
                + pdm.getType() + "'"
            );
        }

        if (! pdmFactModelTypes.contains(pdm.getFactModelType())) {
            errors.addError(PDMErrors.PDM_FACTMODEL_TYPE_INVALID, "Invalid Fact Model '"
                + pdm.getFactModelType() + "'"
            );
        }

        if (! isValidTier(pdm.getTier())) {
            errors.addError(
                    PDMErrors.PDM_TIER_INVALID,
                    "Attempting to create PDM with Invalid Category: tier = " +
                    pdm.getTier());
        }
        if (! isValidPDMCategory(pdm.getCategory())) {
            errors.addError(
                    PDMErrors.PDM_CATEGORY_INVALID,
                    "Attempting to create PDM with Invalid Category: category = " +
                    pdm.getCategory());
        }

        errors.addAcreErrors(validateRoles(pdm));
        errors.addAcreErrors(validateRelationships(pdm));
        return errors;
    }

    public AcreErrors validateRoles(PDMType pdm) {
        AcreErrors errors = new AcreErrors();
        if (pdm != null) {
            if ((pdm.getRoles().getRole() != null) && (pdm.getRoles().getRole().size() !=0)) {
                for (int i = 0; i < pdm.getRoles().getRole().size(); i++) {
                    RoleType role = (RoleType) pdm.getRoles().getRole().get(i);
                    errors.addAcreErrors(validateRole(pdm, role, false));
                }
            }
        }
        return errors;
    }

    public AcreErrors validateRelationships(PDMType pdm) {
        AcreErrors errors = new AcreErrors();
        if (pdm != null) {
            if ((pdm.getRelationships().getRelationship() != null) &&
                    (pdm.getRelationships().getRelationship().size() !=0)) {
                for (int i = 0; i < pdm.getRelationships().getRelationship().size(); i++) {
                    RelationshipType rel = (RelationshipType) pdm.getRelationships().getRelationship().get(i);
                    errors.addAcreErrors(validateRelationship(pdm, rel, false));
                }
            }
        }
        return errors;
    }


    public AcreErrors validateRole(PDMType pdm, RoleType role, boolean isNewRole) {
        AcreErrors errors = new AcreErrors();


        if (role == null) {
            errors.addError(PDMErrors.PDM_ROLE_EMPTY, "Cannot validate empty role");
            return errors;
        }

        if (role.getSequence() == null) {
            errors.addError(PDMErrors.ROLE_SEQUENCE_INVALID, "Role Sequence # cannot be null or empty");
        }

        if ( AcreStringUtil.isEmpty(role.getName()) )
            errors.addError(PDMErrors.ROLE_NAME_INVALID, "Role Name cannot be null or empty");

        if (( AcreStringUtil.isEmpty(role.getType())) ||
            (! pdmRoleTypes.contains(role.getType()))) {
            errors.addError(PDMErrors.ROLE_TYPE_INVALID, "Invalid Role Type '"
                + role.getType() + "'"
            );
        } else {
            // todo - if type == query, then queryName must be present
            if (role.getType().equalsIgnoreCase(PDMXMLConstants.ROLE_TYPE_QUERY)) {
                if (AcreStringUtil.isEmpty(role.getQueryName()))
                    errors.addError(PDMErrors.ROLE_QUERY_NAME_INVALID,
                            "Role type Query requires a valid Query Name");
                if (AcreStringUtil.isEmpty(role.getReturnVariableName()))
                    errors.addError(PDMErrors.ROLE_QUERY_RETURNVARIABLENAME_INVALID,
                            "Role type Query requires a valid return variable name");
            }
            else if (role.getType().equalsIgnoreCase(PDMXMLConstants.ROLE_TYPE_PDM)) {
                // todo - if type == PDM, then type refernce name must be present
                if (AcreStringUtil.isEmpty(role.getTypeReferenceName()))
                    errors.addError(PDMErrors.ROLE_TYPEREFERENCE_NAME_INVALID,
                            "Role type is not query, Type Reference Name is invalid");
            }
        }

        if (pdm != null) {
            // if new role, check if duplicate exists
            if (isNewRole) {
                if (isDuplicateRole(pdm, role) ) {
                    errors.addError(PDMErrors.ROLE_DUPLICATE_EXISTS,
                        "Role '" + role.getName() + "' already exists in PDM '" + pdm.getName() +"'");
                }
            }
        }

        return errors;
    }

    public boolean isDuplicateRelationship(PDMType pdm, RelationshipType rel) {
        if (pdm.getRelationships() == null)
            return false;

        if ((pdm.getRelationships().getRelationship() == null) ||
            (pdm.getRelationships().getRelationship().size() == 0))
            return false;

        for (int i=0; i < pdm.getRelationships().getRelationship().size(); i++) {
            RelationshipType r = (RelationshipType) pdm.getRelationships().getRelationship().get(i);
            if (r.getName().equals(rel.getName()))
                return true;
        }

        return false;
    }

    public boolean isDuplicateRole(PDMType pdm, RoleType role) {
        if (pdm.getRoles() == null)
            return false;

        if ((pdm.getRoles().getRole() == null) ||
            (pdm.getRoles().getRole().size() == 0))
            return false;

        for (int i=0; i < pdm.getRoles().getRole().size(); i++) {
            RoleType r = (RoleType) pdm.getRoles().getRole().get(i);
            if (r.getName().equals(role.getName()))
                return true;
        }

        return false;
    }

    public AcreErrors validateRelationship(PDMType pdm, RelationshipType rel, boolean isNewRel) {
        AcreErrors errors = new AcreErrors();

        errors.addAcreErrors(validateRelationship(rel));

        if (pdm != null ) {
            if (isNewRel) {
                if (isDuplicateRelationship(pdm, rel)) {
                    errors.addError(PDMErrors.RELATIONSHIP_DUPLICATE_EXISTS,
                                "Relationship '" + rel.getName() + "' already exists in PDM '" + pdm.getName() +"'");
                }
            }
        }

        return errors;
    }

    public AcreErrors validateRelationship(RelationshipType rel) {
        AcreErrors errors = new AcreErrors();

        if ( AcreStringUtil.isEmpty(rel.getName()) )
            errors.addError(PDMErrors.RELATIONSHIP_NAME_INVALID, "Relationship Name cannot be null or empty");

        if ( AcreStringUtil.isEmpty(rel.getType()) )
            errors.addError(PDMErrors.RELATIONSHIP_TYPE_INVALID, "Relationship Type cannot be null or empty");

        if ( AcreStringUtil.isEmpty(rel.getFromRole()) )
            errors.addError(PDMErrors.RELATIONSHIP_FROM_ROLE_INVALID, "Relationship From Role cannot be null or empty");

        if ( AcreStringUtil.isEmpty(rel.getToRole()) )
            errors.addError(PDMErrors.RELATIONSHIP_TO_ROLE_INVALID, "Relationship To Role cannot be null or empty");

        if ( ! pdmRelationshipTypes.contains(rel.getType())) {
            errors.addError(PDMErrors.RELATIONSHIP_TYPE_INVALID, "Invalid Relationship Type '"
                + rel.getType() + "'"
            );
        }

        return errors;
    }

    public AcreErrors validateRoleArgument(ArgumentType arg) {
        AcreErrors errors = new AcreErrors();

        if (AcreStringUtil.isEmpty(arg.getName())) {
            errors.addError(PDMErrors.ROLE_ARGUMENT_NAME_INVALID,
                    "Role Argument Name cannot be null or empty");
        }
        if (AcreStringUtil.isEmpty(arg.getType())) {
            errors.addError(PDMErrors.ROLE_ARGUMENT_TYPE_INVALID,
                    "Role Argument Type cannot be null or empty");
        }

        if (AcreStringUtil.isEmpty(arg.getValue())) {
            errors.addError(PDMErrors.ROLE_ARGUMENT_VALUE_INVALID,
                    "Role Argument Value cannot be null or empty");
        }

        if (arg.getSequence() == null) {
            errors.addError(PDMErrors.ROLE_ARGUMENT_SEQUENCE_INVALID,
                    "Role Argument Sequence # cannot be null or empty");
        }

        return errors;
    }

    public boolean isScriptedPDM(PDMType pdm) {
        boolean scriptedPDMType =
                PDMXMLConstants.PDM_TYPE_MODEL_GROOVY.equalsIgnoreCase(pdm.getType()) ||
                PDMXMLConstants.PDM_TYPE_MODEL_PQLPDM.equalsIgnoreCase(pdm.getType()) ;

        return ((scriptedPDMType) && !AcreStringUtil.isEmpty(pdm.getScriptedPDMPath()));
    }

    public ArrayList getRoleArgumentTypes() {
        return pdmRoleArgumentTypes;
    }

    public static void deleteArgumentFromRole(RoleType role, String argName) {
        if (argName == null)
            return;
        if ((role.getArgument() != null) && (role.getArgument().size() != 0)) {
            for (int i = 0; i < role.getArgument().size(); i++) {
                ArgumentType arg = (ArgumentType) role.getArgument().get(i);
                if (arg.getName().equalsIgnoreCase(argName)) {
                    role.getArgument().remove(i);
                    break;
                }
            }
        }
    }

    public static void deleteRelationshipFromPDM(PDMType currentPDM, String relationshipName) {
        if (relationshipName == null)
            return;

        if (currentPDM.getRelationships() == null)
            return;

        List rels = currentPDM.getRelationships().getRelationship();
        for (int i = 0; i < rels.size(); i++) {
            RelationshipType r = (RelationshipType) rels.get(i);
            if (r.getName().equalsIgnoreCase(relationshipName)) {
                currentPDM.getRelationships().getRelationship().remove(i);
                break;
            }
        }
    }

    public static void deleteRoleFromPDM(PDMType currentPDM, String roleName) {
            if (roleName == null)
                return;

            if (currentPDM.getRoles() == null)
                return;

            List roles = currentPDM.getRoles().getRole();
            for (int i = 0; i < roles.size(); i++) {
                RoleType r = (RoleType) roles.get(i);
                if (r.getName().equalsIgnoreCase(roleName)) {
                    currentPDM.getRoles().getRole().remove(i);
                    break;
                }
            }
        }
}
