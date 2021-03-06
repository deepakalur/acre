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
package org.acre.visualizer.graph.pdmmodel.edges;

import org.acre.pdm.PDMType;
import org.acre.pdm.RelationshipType;
import org.acre.visualizer.graph.edges.AcreEdge;
import org.acre.visualizer.graph.pdmmodel.vertex.RoleVertex;

/**
 * @author Deepak.Alur@Sun.com
 * @version Dec 22, 2004 3:07:25 PM
 */
public interface RoleToRoleEdge extends AcreEdge {
    PDMType getPDM();
    RelationshipType getRelationship();
    RoleVertex getFromRole();
    RoleVertex getToRole();

}
