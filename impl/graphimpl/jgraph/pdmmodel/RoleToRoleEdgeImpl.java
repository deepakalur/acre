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
package org.acre.visualizer.graphimpl.jgraph.pdmmodel;

import org.acre.pdm.PDMType;
import org.acre.pdm.RelationshipType;
import org.acre.visualizer.graph.graph.pdmmodel.edges.RoleToRoleEdge;
import org.acre.visualizer.graph.graph.pdmmodel.vertex.PDMVertex;
import org.acre.visualizer.graph.graph.pdmmodel.vertex.RoleVertex;
import org.acre.visualizer.graph.graph.vertex.AcreVertex;

/**
 * @author Deepak.Alur@Sun.com
 * @version Dec 22, 2004 3:07:56 PM
 */
public class RoleToRoleEdgeImpl
        extends RelationshipEdgeImpl
        implements RoleToRoleEdge {

    PDMType pdm;
    RelationshipType relationship;


    public RoleToRoleEdgeImpl(PDMVertex parentVertex, String edgeName, RelationshipType relationship, AcreVertex from, AcreVertex to) {
        super(edgeName, relationship, parentVertex, from, to);
        if (parentVertex != null) {
           this.pdm = parentVertex.getPDM();
        }
        if (relationship != null) {
            this.relationship = (RelationshipType) relationship;
        }
    }

    public RoleVertex getFromRole() {
        return (RoleVertex) getFromVertex();
    }

    public RoleVertex getToRole() {
        return (RoleVertex) getToVertex();
    }

    public PDMType getPDM() {
        return pdm;
    }

    public RelationshipType getRelationship() {
        return relationship;
    }

}
