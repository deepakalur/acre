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
package org.acre.pdmengine.model.impl;

import org.acre.pdm.PDMType;
import org.acre.pdm.RoleType;
import org.acre.common.AcreStringUtil;
import org.acre.config.ConfigService;
import org.acre.dao.PDMXMLConstants;
import org.acre.dao.AcreDbDAO;
import org.acre.pdmengine.PatternEngineException;
import org.acre.pdmengine.SearchContext;
import org.acre.pdmengine.model.*;
import org.acre.pdmengine.model.visitor.ResultVisitor;
import org.acre.pdmengine.util.InputSpec;
import org.acre.pdmengine.util.PMTypeUtil;

import java.io.Serializable;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class PatternResultImpl extends ResultImpl implements PatternResult, Serializable {
    PDMType pdm;
//    Collection<Result> roles;
//    Collection<RelationshipResult> relationships;
    Collection roles = new ArrayList();
    Collection relationships = new ArrayList();
    private RoleResult parentRole;
    private String system;
    private String version;
    private Date timestamp;

    private int patternConformance;

    static final long serialVersionUID = -8503138857672616548L;

    private static Logger logger = ConfigService.getInstance().getLogger(PatternResultImpl.class);


    public PatternResultImpl(PDMType pdm, InputSpec inputSpec) {
        this.pdm = pdm;

        setResultDimensions(inputSpec);
    }

    public PatternResultImpl(PDMType pdm) {
        this(pdm, null);
    }

    // Copy Constructor
    public PatternResultImpl(PatternResult o, RoleResult parent) {

        this.pdm = o.getPdm();
        this.system = o.getSystem();
        this.version = o.getVersion();
        this.timestamp = o.getTimestamp();

        setParent(parent);

        // clone roles
        Iterator oRolesItr = o.getRoles().iterator();
        while ( oRolesItr.hasNext() ) {
            RoleResultImpl oRole = (RoleResultImpl)oRolesItr.next();
            RoleResult role = new RoleResultImpl(oRole, this);
            roles.add(role);
        }

        // clone relationships
        Iterator oRelationshipsItr = o.getRelationships().iterator();
        while ( oRelationshipsItr.hasNext() ) {
            RelationshipResultImpl oRelationship =
                    (RelationshipResultImpl)oRelationshipsItr.next();

            RelationshipResult relationship = new RelationshipResultImpl(oRelationship, this);
            relationships.add( relationship );
        }

    }


    public PDMType getPdm() {
        return pdm;
    }

    public void setPdm(PDMType pdm) {
        this.pdm = pdm;
    }

//    public Collection<Model> getRoles() {
    public Collection getRoles() {
        return roles;
    }

    /**
     * RoleReference could be specified as follows :
     *      <PDMName>.<Rolename>
     *      <Rolename> - rolename may be PDM or Query type
      * @param roleReference
     * @return
     */
        public RoleResult getRoleReference(String roleReference) {
            RoleResult roleResult = null;

            // check if valid RoleName
            RoleType roleType = getRoleType(roleReference);
            if ( roleType == null ) {
                // Not a role name, is it role reference ?
                int sepIdx = roleReference.indexOf('.');
                if ( sepIdx >= 0 ) {
                    String pdmName, roleName;
                    pdmName = roleReference.substring(0, sepIdx);
                    roleName = roleReference.substring(sepIdx+1);
                    RoleResult pdmRoleResult = (RoleResult)findRole(pdmName);
                    if ( pdmRoleResult != null ) {
                        PatternResult patternResult = (PatternResult)pdmRoleResult.getRoleResult();
                        roleResult = patternResult.getRoleReference(roleName);
                    }
                    else {
                            // check if self.rolename format
                            if ( getName().equalsIgnoreCase(pdmName) ) {
                                roleResult = getRoleReference(roleName);
                            }
                            else {
                                throw new PatternEngineException("Role Reference not found : " +  roleReference);
                            }

                    }


                }
                else {

                    // search nested patterns
                    Iterator roles = getRoles().iterator();
                    while ( roles.hasNext()) {
                        RoleResult rr = (RoleResult)roles.next();
                        if ( rr.getRoleResult() instanceof PatternResult) {
                            PatternResult pr = (PatternResult)rr.getRoleResult();
                            roleResult = pr.getRoleReference(roleReference);
                            if ( roleResult != null)
                                break;
                        }

                    }

                    //if  top of recursion and roleResult is still not found, then notfound error
                    if ( (roleResult==null) && (getParent() == null))
                        throw new PatternEngineException("Role Reference not found : " + roleReference);
                }
            }
            else {
                if ( PDMXMLConstants.ROLE_TYPE_QUERY.equalsIgnoreCase(roleType.getType()) ) {
                    roleResult = (RoleResult)findRole(roleReference);
                }
                else if ( PDMXMLConstants.ROLE_TYPE_PDM.equalsIgnoreCase(roleType.getType())) {
                    RoleResult pdmRoleResult = (RoleResult)findRole(roleReference);
                    PatternResult patternResult = (PatternResult)pdmRoleResult.getRoleResult();
                    if ( patternResult.getRoles().size() > 1 ) {
                        throw new PatternEngineException("Ambigous role reference :" + roleReference);
                    }
                    roleResult = (RoleResult)patternResult.getRoles().iterator().next();
                }
            }
            return roleResult;
        }


    private Result findRole(String name ) {
            if ( getRoles() == null )
                return null;

            Iterator resultItr = getRoles().iterator();
            while ( resultItr.hasNext() ) {
                Result result = (Result) resultItr.next();
                if ( result.getName().equalsIgnoreCase(name))
                    return result;
            }
            return null;
    }

//    public void setRoles(Collection<Model> roles) {
      public void setRoles(Collection roles) {
        this.roles = roles;
    }

//    public Collection<RelationshipModel> getRelationships() {
      public Collection getRelationships() {
        return relationships;
    }

//    public void setRelationships(Collection<RelationshipModel> relationships) {
      public void setRelationships(Collection relationships) {
        this.relationships = relationships;
    }

    public Artifact findArtifact(String artifactName) {
        Artifact artifact = null;
        Iterator roles = getRoles().iterator();

        while ( roles.hasNext() ) {
            RoleResult roleResult = (RoleResult)roles.next();
            artifact = roleResult.findArtifact(artifactName);
            if ( artifact != null )
                break;
        }
        return artifact;
    }

    public List matchArtifacts(String regexp) {
        Pattern pattern = Pattern.compile(regexp);
        return matchArtifacts(pattern);
    }

    public List matchArtifacts(Pattern pattern) {
        List artifacts = new ArrayList();

        Iterator roles = getRoles().iterator();

        while ( roles.hasNext() ) {
            List roleArtifacts;
            RoleResult roleResult = (RoleResult)roles.next();
            roleArtifacts = roleResult.matchArtifacts(pattern);
            if ( roleArtifacts.size() > 0 )
                artifacts.addAll(roleArtifacts);
        }
        return artifacts;

    }

    public void accept(ResultVisitor visitor) {
        try {
            visitor.visitPatternResult(this);

            if ( visitor.isVisitCompleted() ) {
                return;
            }

            Iterator roles;
            roles = getRoles().iterator();
            while ( roles.hasNext()) {
                RoleResult role = (RoleResult)roles.next();
                role.accept(visitor);

                if ( visitor.isVisitCompleted())
                    return;
            }

            Iterator relationships;
            relationships = getRelationships().iterator();
            while ( relationships.hasNext()) {
                RelationshipResult relationship = (RelationshipResult)relationships.next();
                relationship.accept(visitor);

                if ( visitor.isVisitCompleted())
                    return;

            }
        }
        finally {
            visitor.visitPatternComplete(this);
        }
    }


    public String toString() {
        return "PDMModel{" + "\n" +
                "pdm=" + PDMModelUtil.pdmToString(pdm) + "\n" +
                "system = " + system + "\n" +
                "version = " + version + "\n" +
                "timestamp = " + timestamp + "\n" +
                "roles=" + toStringRoles(roles) + "\n" +
                "relationships=" + toStringRelationships(relationships) + "\n" +
                "}\n";
    }

//    private String toStringRelationships(Collection<RelationshipModel> relationships) {
    private String toStringRelationships(Collection relationships) {
        if ((relationships==null) || (relationships.size() == 0))
            return "Relationships{}";

        StringBuffer buf = new StringBuffer("Relationships{\n");

//        for (RelationshipModel model: relationships) {
        for (Iterator iter = relationships.iterator(); iter.hasNext();) {
            RelationshipResult model = (RelationshipResult) iter.next();
                buf.append(model);
        }
        buf.append("}\n");

        return buf.toString();
    }

//    private String toStringRoles(Collection<Model> roles) {
      private String toStringRoles(Collection roles) {

        if ((roles==null) || (roles.size() == 0))
            return "Roles{}";

        StringBuffer buf = new StringBuffer("Roles{\n");

//        for (Model model: roles) {
        for (Iterator iter = roles.iterator(); iter.hasNext();) {
            Result result = (Result) iter.next();
                buf.append(result);
                buf.append("\n");
        }

        buf.append("}\n");
        return buf.toString();
    }

    public String getName() {
        return getPdm().getName();
    }


    public List findAssociatedRoles(String roleName, String relationshipType) {
        List associatedRoles = new ArrayList();
        findAssociatedRoleResults(roleName, relationshipType, associatedRoles, false);
        return associatedRoles;
    }

/**
    // find associated FromRole(s) with the given ToRole (i.e., reverse associations)
    public List findAssociatedFromRoles(String roleName, String relationshipType) {
        List associatedRoles = new ArrayList();
        findAssociatedRoleResults(roleName, relationshipType, associatedRoles, false);
        return associatedRoles;
    }

    // find associated ToRole(s) with the given FromRole (i.e., forward associations)
    public List findAssociatedToRoles(String roleName, String relationshipType) {
        List associatedRoles = new ArrayList();
        findAssociatedRoleResults(roleName, relationshipType, associatedRoles, true);
        return associatedRoles;
    }
**/
    // Find all roles in relationship with the given roleName (including in nested PDMs)
    private void findAssociatedRoleResults(String roleName, String relationshipType,
                                           List accumulateRoles, boolean forwardAssoc) {

        // Note: using Roles from Result tree for traversing i.e.,
        // not using RoleType source tree for traversing because they are not resolved into a linked Tree structure
        if ( !getRoles().isEmpty() ) {
            Iterator roles = getRoles().iterator();
            while ( roles.hasNext() ) {
                RoleResult roleResult = (RoleResult)roles.next();
                Result result = roleResult.getRoleResult();
                if (result instanceof PatternResult) {
                    PatternResultImpl pdmResult = (PatternResultImpl)result;
                    pdmResult.findAssociatedRoleResults(roleName, relationshipType, accumulateRoles,
                            forwardAssoc);
                }
            }
        }
        findAssociatedRolesInRelationship(roleName, relationshipType, accumulateRoles, forwardAssoc);
    }


    private void findAssociatedRolesInRelationship(String matchRoleName, String relationshipType,
                                                   List accumulateRoles, boolean fromRole) {
        if ( AcreStringUtil.isEmpty(matchRoleName))
              return;

        if ( AcreStringUtil.isEmpty(relationshipType))
              return;

        if ( getRelationships().isEmpty() )
               return;

        Iterator relationships = getRelationships().iterator();

        while ( relationships.hasNext()) {
            RelationshipResult relationship = (RelationshipResult)relationships.next();

            // interested only in similar relationship Types
            if ( !relationshipType.equalsIgnoreCase(relationship.getRelationship().getType()) )
                continue;

            String associatedRoleName, roleName;
            RoleResult assocRoleResult;


            if ( matchRoleName.equalsIgnoreCase(relationship.getFromRole().getName()) ) {
                logger.info("Associated Role : " + relationship.getToRole().getName());
                accumulateRoles.add(relationship.getToRole());
            }
            else if (matchRoleName.equalsIgnoreCase(relationship.getToRole().getName()) ) {
                logger.info("Associated Role : " + relationship.getFromRole().getName());
                accumulateRoles.add(relationship.getFromRole());
            }

/**
            if ( fromRole ) {
                roleName = relationship.getFromRole().getName();
                associatedRoleName = relationship.getToRole().getName();
                assocRoleResult = relationship.getToRole();
            }
            else {
                roleName = relationship.getToRole().getName();
                associatedRoleName = relationship.getFromRole().getName();
                assocRoleResult = relationship.getFromRole();
            }

            if ( roleName.equalsIgnoreCase(matchRoleName) ) {
                logger.info("Associated Role : " + associatedRoleName);
                accumulateRoles.add(assocRoleResult);
            }
 **/
        }
        return ;
    }

    // travese the depth of the PatternResult tree to find the given role name
    public RoleResult searchRole(String roleName) {
        if ( getRoles().isEmpty() )
            return null;
        Iterator roles = getRoles().iterator();
        while ( roles.hasNext() ) {
            RoleResult roleResult = (RoleResult)roles.next();
            Result result = roleResult.getRoleResult();
            if ( result instanceof QueryResult ) {
                if ( roleResult.getName().equalsIgnoreCase(roleName) )
                    return roleResult;
            }
            else if ( result instanceof PatternResult) {
                PatternResultImpl pdmResult = (PatternResultImpl)result ;
                RoleResult res = pdmResult.searchRole(roleName);
                if ( res != null )
                    return res;
            }
        }
        return null;
    }

    /**
     * class RoleReference {
     * RoleReference(String roleReference){
     * int sepIdx = roleReference.indexOf('.');
     * if ( sepIdx >= 0 ) {
     * pdmName = roleReference.substring(0, sepIdx);
     * roleName = roleReference.substring(sepIdx+1);
     * }
     * else
     * roleName = roleReference;
     * }
     * <p/>
     * public String getPdmName() {
     * return pdmName;
     * }
     * <p/>
     * public String getRoleName() {
     * return roleName;
     * }
     * <p/>
     * private String pdmName;
     * private String roleName;
     * }
     */
    public void setParent(RoleResult roleResult) {
        parentRole = roleResult;
    }

    public RoleResult getParent() {
        return parentRole;
    }

    RoleType getRoleType(String roleName) {
        return PMTypeUtil.getRoleType(getPdm(), roleName);
    }

    public PatternResult getRoot() {
        PatternResultImpl root = this;
        while ( root.getParent() != null ) {
            root = (PatternResultImpl)getParent().getParent();
        }
        return root;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }


    public String getSystem() {
        return system;
    }

    public String getVersion() {
        return version;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    // experimental API
    public void viewCoarseResults() {
        // stub
    }

    public void viewFilteredResults() {
        // stub
    }


    public int getHitCount() {
        int hits = 0;
        if ( getRelationships().size() == 0 ) {
            Iterator roles = getRoles().iterator();
            while ( roles.hasNext() ) {
                RoleResult roleResult = (RoleResult)roles.next();
                hits = Math.max(roleResult.getHitCount(), hits);
            }
        } else {
            Iterator relationships = getRelationships().iterator();
            while ( relationships.hasNext() ) {
                RelationshipResult relationship = (RelationshipResult)relationships.next();
                hits = Math.max(relationship.getHitCount(), hits);
            }
        }

        return hits;
    }

    public PatternResult getPattern() {
        return this;
    }


    public void filterAndJoinLinks() {

        List relations = new ArrayList();

        collectRelationships(this, relations);

        Iterator relItr1 = relations.iterator();

        JoinProcessor joinProcessor = new JoinProcessor();
        while (relItr1.hasNext() ) {
            RelationshipResultImpl relResult1 = (RelationshipResultImpl)relItr1.next();
            joinProcessor.filterRelationshipRoles(relResult1);

            Iterator relItr2 = relations.iterator();
            while ( relItr2.hasNext() ) {
                RelationshipResultImpl relResult2 = (RelationshipResultImpl)relItr2.next();
                joinProcessor.filterRelationshipRoles(relResult2);

                joinProcessor.joinRelationships(relResult1, relResult2);
            }
        }

        // signal process complete to all relationships
        Iterator relItr = relations.iterator();
        while ( relItr.hasNext() ) {
            RelationshipResultImpl rel = (RelationshipResultImpl)relItr.next();
            rel.joinComplete();
        }
    }


    // walk the entire PatternResult tree and collect all relationships
    private void collectRelationships(PatternResult patternResult, List result) {

        Iterator relationships;
        relationships = patternResult.getRelationships().iterator();
        while ( relationships.hasNext()) {
            RelationshipResult relationship = (RelationshipResult)relationships.next();
                result.add(relationship);
        }

        Iterator roles;
        roles = patternResult.getRoles().iterator();
        while ( roles.hasNext()) {
            RoleResult role = (RoleResult)roles.next();
            if ( role.getRoleResult() instanceof PatternResult) {
                collectRelationships((PatternResult)role.getRoleResult(), result);
            }
        }

    }



    // TODO
    void ForEachRole(RoleClosure action)
    {
        Iterator rolesItr = roles.iterator();
         while ( rolesItr.hasNext() ) {
             RoleResultImpl role = (RoleResultImpl)rolesItr.next();
             Result roleResult = role.getRoleResult();
             action.execute(role);
         }

    }

    private void setResultDimensions(InputSpec inputSpec) {
        if ( inputSpec == null )
            return;

        String system = inputSpec.getSystem();
        String version = inputSpec.getVersion();
        Date time = inputSpec.getTimestamp();

        if ( system != null ) {
            AcreDbDAO dao = new AcreDbDAO();
            if ( AcreStringUtil.isEmpty(version) )
                version = dao.findVersion(system, time);
            else if ( time == null )
                time = dao.findExtractionTime(system, version);
        }

        setSystem(system);
        setVersion(version);
        setTimestamp(time);
        setPatternConformance(inputSpec.getSearchType());
    }

    public void setPatternConformance(int patternConformance) {
        this.patternConformance = patternConformance;
    }

    public boolean isRefined() {
        return patternConformance == SearchContext.EXACT;
    }

    public boolean isCoarse() {
        return patternConformance == SearchContext.COARSE;
    }
}

interface RoleClosure {
    void execute(RoleResultImpl role);
}