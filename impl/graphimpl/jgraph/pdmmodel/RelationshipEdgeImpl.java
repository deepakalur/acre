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
import org.acre.visualizer.graph.graph.edges.RelationshipEdge;
import org.acre.visualizer.graph.graph.edges.AcreEdgeData;
import org.acre.visualizer.graph.graph.edges.AcreEdgeUtils;
import org.acre.visualizer.graph.graph.vertex.AcreVertex;

/**
 * @author Deepak.Alur@Sun.com
 * @version Dec 14, 2004 2:25:43 PM
 */
public abstract class RelationshipEdgeImpl
        extends AcreEdgeImpl
        implements RelationshipEdge {

    public RelationshipEdgeImpl(
            String edgeName,
            Object relationship,
            AcreVertex parentVertex,
            AcreVertex from,
            AcreVertex to) {

        super(null);
        setParentVertex(parentVertex);
        AcreEdgeData data = new AcreEdgeData(edgeName, relationship, from, to);
        data.initDefaults();
        initEdgeAttributes(data);
        setSalsaData(data);
    }

    protected void initEdgeAttributes(AcreEdgeData data) {
        data.useDottedLine();
        AcreEdgeUtils.setEdgeAttributes(data);

        // override arrow
        data.setLineEndArrowSimple();
    }


}
