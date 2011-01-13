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
import org.acre.visualizer.graph.graph.pdmmodel.edges.RoleToPDMEdge;
import org.acre.visualizer.graph.graph.pdmmodel.vertex.PDMVertex;
import org.acre.visualizer.graph.graph.pdmmodel.vertex.RoleVertex;


/**
 * @author Deepak.Alur@Sun.com
 * @version Dec 15, 2004 4:35:19 PM
 */
public class RoleToPDMEdgeImpl extends AcreEdgeImpl
        implements RoleToPDMEdge
 {
    public RoleToPDMEdgeImpl (PDMVertex parentPDMVertex, RoleVertex fromRole, PDMVertex toPDM) {
        super(null);
        AcreEdgeData data = new AcreEdgeData(
                AcreEdgeData.DEFAULT_MAPS_NAME,
                AcreEdgeData.DEFAULT_RELATIONSHIP_DATA,
                fromRole, toPDM);
        setParentVertex(parentPDMVertex);
        data.initDefaults();
        initEdgeAttributes(data);
        setSalsaData(data);
    }

    protected void initEdgeAttributes(AcreEdgeData data) {
        data.setLineEndArrowSimple();
        data.useDottedLine();
    }

}
