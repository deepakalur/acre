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

import org.acre.visualizer.graphimpl.jgraph.AcreEdgeImpl;
import org.acre.visualizer.graph.graph.edges.AcreEdgeData;
import org.acre.visualizer.graph.graph.pdmmodel.edges.PDMToRoleEdge;
import org.acre.visualizer.graph.graph.pdmmodel.vertex.PDMVertex;
import org.acre.visualizer.graph.graph.pdmmodel.vertex.RoleVertex;


/**
 * @author Deepak.Alur@Sun.com
 * @version Dec 14, 2004 12:53:28 PM
 */
public class PDMToRoleEdgeImpl
        extends AcreEdgeImpl implements
        PDMToRoleEdge {
    private PDMVertex pdmVertex;
    private RoleVertex roleVertex;

    public PDMToRoleEdgeImpl(PDMVertex from, RoleVertex to) {
        super(null);
        AcreEdgeData data = new AcreEdgeData(
                AcreEdgeData.DEFAULT_HAS_NAME,
                AcreEdgeData.DEFAULT_RELATIONSHIP_DATA, from, to);
        data.initDefaults();
        initEdgeAttributes(data);
        setSalsaData(data);
        this.pdmVertex = from;
        this.roleVertex = to;
    }

    protected void initEdgeAttributes(AcreEdgeData data) {
        data.setLineEndArrowNone();
        data.useDottedLine();
    }

    public String getToolTipText() {
        StringBuffer buf = new StringBuffer();

        if (pdmVertex != null)
            buf.append(pdmVertex.getVertexName());
        else
            buf.append("PDM from is null");

        if (roleVertex != null) {
            buf.append(" to ");
            buf.append(roleVertex.getVertexName());
        } else {
            buf.append (" to Role is null");
        }
        return buf.toString();
    }
}
