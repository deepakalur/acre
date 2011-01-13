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

import org.acre.pdm.RoleType;
import org.acre.lang.pql.pdbc.PQLArtifact;
import org.acre.pdmengine.model.*;
import org.acre.pdmengine.model.visitor.ResultVisitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

public class RoleResultImpl extends ResultImpl implements RoleResult, java.io.Serializable {
    private RoleType role;
    /** roleResult can be PDMModel if the role is PDM or PQLResultModel
     * if the role is a Query
     */
    private Result roleResult;
    private PatternResult parent;

    private String scope;
    private String variableName;

    static final long serialVersionUID = 3631775255484624522L;

    public RoleResultImpl(PatternResult parent) {
        this.parent = parent;
    }

    // copy constructor
    public RoleResultImpl(RoleResultImpl o, PatternResult parent) {
        this(parent);

        setRole(o.getRole());
        setVariableName(o.getVariableName());
        setScope(o.getScope());

        Result oResult = o.getRoleResult();
        if ( oResult instanceof QueryResult ) {
            roleResult = new QueryResultImpl((QueryResultImpl)oResult, this);
        }
        else if ( oResult instanceof PatternResult) {
            PatternResult oPatternResult = (PatternResult)oResult;
            roleResult = new PatternResultImpl(oPatternResult, this);
        }
    }

    public Result getRoleResult() {
        return roleResult;
    }

    public void setRoleResult(Result roleResult) {
        this.roleResult = roleResult;
        if ( roleResult instanceof QueryResult ) {
            ((QueryResultImpl)roleResult).setParent(this);
        } else if ( roleResult instanceof PatternResult) {
            ((PatternResultImpl)roleResult).setParent(this);
        }
    }

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;

        String id = Integer.toString(Math.abs(role.getName().replace('.', '_').hashCode()));
        setScope(id);

        String name = role.getReturnVariableName();
        if ( name == null )
            name = role.getName();
        setVariableName(name);
    }

    public String getName() {
        return getRole().getName();
    }

    public String getVariableName() {
        return variableName;
    }

    public PatternResult getParent() {
        return parent;
    }

    public PatternResult getPattern() {
        return getParent();
    }
    
    public Artifact findArtifact(String artifactName) {
        Artifact artifact = null;
        if ( roleResult instanceof QueryResult) {
            QueryResult queryResult = (QueryResult)roleResult;
            artifact = queryResult.findArtifact(artifactName);
        }
        else if ( roleResult instanceof PatternResult ) {
            PatternResult patternResult = (PatternResult)roleResult;
            artifact = patternResult.findArtifact(artifactName);
        }
        return artifact;
    }

    public List matchArtifacts(Pattern pattern) {
        List artifacts = new ArrayList();
        if ( roleResult instanceof QueryResult) {
            QueryResult queryResult = (QueryResult)roleResult;
            artifacts = queryResult.matchArtifacts(pattern);
        }
        else if ( roleResult instanceof PatternResult ) {
            PatternResult patternResult = (PatternResult)roleResult;
            artifacts = patternResult.matchArtifacts(pattern);
        }
        return artifacts;
    }

    public void accept(ResultVisitor visitor) {
        visitor.visitPatternRoleResult(this);

        Result roleResult = getRoleResult();
        roleResult.accept(visitor);
    }

    public Artifact findArtifact(PQLArtifact pqlArtifact) {
        Artifact artifact = null;
        if ( roleResult instanceof QueryResultImpl) {
            QueryResultImpl queryResult = (QueryResultImpl)roleResult;
            artifact = queryResult.findArtifact(pqlArtifact);
        }
        // TODO - need to enable recursive search in nested PDMs
        // return roleResult.findArtifact(pqlArtifact)
        return artifact;
    }

    public void setVariableName(String name) {
        variableName = name;
    }

    public String toString() {
        return "(RoleModel) {" +
                "role=" + PDMModelUtil.roleTypeToString(role) +
                ", roleResult=" + roleResult +
                "}";
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public int getHitCount() {
        return roleResult.getHitCount(); 
    }


    public void filterArtifacts(Collection fromRoleArtifacts) {
        if ( roleResult instanceof QueryResultImpl) {
            QueryResultImpl queryResult = (QueryResultImpl)roleResult;

            queryResult.setFixedArtifacts(fromRoleArtifacts);
        }
    }

}


