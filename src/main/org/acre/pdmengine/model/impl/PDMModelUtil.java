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
import org.acre.pdm.RelationshipType;
import org.acre.pdm.RoleType;
import org.acre.pdmqueries.QueryType;
import org.acre.lang.pql.pdbc.PQLValueHolder;
import org.acre.pdmengine.model.PatternResult;
import org.acre.pdmengine.model.QueryResult;
import org.acre.pdmengine.model.Result;

/**
 * @author Deepak.Alur@Sun.com
 *         Date: Sep 14, 2004
 *         Time: 10:23:32 PM
 */
public class PDMModelUtil {
    // todo move to util
    public static String relationshipTypeToString(RelationshipType relationship) {
        return
            new StringBuffer()
                .append("RelationshipType {")
                .append("name = '").append(relationship.getName()).append("'")
                .append(", type = '").append(relationship.getType()).append("'")
                .append(", fromRole = '").append(relationship.getFromRole()).append("'")
                .append(", toRole = '").append(relationship.getToRole()).append("'")
                .append("}")
            .toString();
    }

    public static String roleTypeToString(RoleType role) {
        return
            new StringBuffer()
                .append("RoleType {")
                .append("name = '").append(role.getName()).append("'")
                .append(", type = '").append(role.getType()).append("'")
                .append(", sequence = '").append(role.getSequence()).append("'")
                .append(", queryName = '").append(role.getQueryName()).append("'")
                .append(", typeReferenceName = '").append(role.getTypeReferenceName()).append("'")
                .append("}")
            .toString();
    }

    public static String QueryToString(QueryType query) {
        return
            new StringBuffer()
                .append("QueryType {")
                .append("name = '").append(query.getName()).append("'")
                .append(", type = '").append(query.getType()).append("'")
                .append(", language = '").append(query.getLanguage()).append("'")
                .append(", description = '").append(query.getDescription()).append("'")
                .append(", relativeFilePath = '").append(query.getRelativeFilePath()).append("'")
                .append(", argument = '").append(query.getArgument()).append("'")
                .append("}")
                .toString();
    }

    public static String pdmToString(PDMType pdm) {
        return
            new StringBuffer()
                .append("PDMType {")
                .append("name = '").append(pdm.getName()).append("'")
                .append(", description = '").append(pdm.getDescription()).append("'")
                .append(", tier = '").append(pdm.getTier()).append("'")
                .append(", type = '").append(pdm.getType()).append("'")
                .append(", category = '").append(pdm.getCategory()).append("'")
                .append(", attributes = '").append(pdm.getAttributes()).append("'")
                .append(", scriptedPDMPath = '").append(pdm.getScriptedPDMPath()).append("'")
                .append(", relationships ='").append(pdm.getRelationships()).append("'")
                .append(", roles = '").append(pdm.getRoles()).append("'")
                .append("}")
            .toString();
    }

    public static String generateDOTModel(PatternResult model) {
        StringBuffer buf = new StringBuffer();//model.getPdm().getName() + " -> \n");

        // print model

        // get roles for the model
        RoleResultImpl[] roles=null;
        if (model.getRoles() != null) {
            roles = new RoleResultImpl[model.getRoles().size()];
            model.getRoles().toArray(roles);
            for (int roleNum=0; roleNum < roles.length; roleNum++) {
                Result roleResult = roles[roleNum].getRoleResult();
                if (roleResult instanceof PatternResult) {
                    buf.append(model.getPdm().getName()).append("->")
                            .append(roles[roleNum].getRole().getName())
                            .append(";\n");
                    buf.append(roles[roleNum].getRole().getName()).append("->")
                            .append(generateDOTModel((PatternResult)roleResult));
                            //.append(";\n");
                } else if (roleResult instanceof QueryResultImpl) {
                    //todo how to print resultset

                    buf.append(model.getPdm().getName()).append("->")
                            .append(roles[roleNum].getRole().getName())
                            .append(";\n");
                    QueryResult result = (QueryResult) roleResult;
                    String [] keys = result.getVariableKeys();
                    String roleName = roles[roleNum].getRole().getName();
                    for (int keynum=0; keynum < keys.length; keynum++) {
                        String keyName = keys[keynum].toString();
                        // get the artifact bucket and print the role -> artifact
                        buf.append(roleName).append("->")
                                .append(roleName)
                                .append(keyName).append(";\n");

                        // get and print results
                        //String [] values = artifacts.getArtifactNames(keys[keynum]);
                        Object [] values = result.getArtifacts();
                        for (int valNum=0; valNum < values.length; valNum++) {
                            Object val = values[valNum];
                            if (val instanceof PQLValueHolder) {
                                PQLValueHolder data = (PQLValueHolder) val;
                                buf.append(roleName).append(keyName).append("->").append(data.getName());
                            } else {
                                buf.append(roleName).append(keyName).append("->").append(values[valNum]);
                            }
                        }
                    }

                    /*
                            buf.append(roles[roleNum].getRole().getName()).append("->")
                            .append(result.getArtifactsHolder())
                            .append(";\n");
                            */
                }
            }
        }

        // print
        return buf.toString();

    }
}
