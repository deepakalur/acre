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
package org.acre.visualizer.graphimpl.jgraph;

import org.acre.visualizer.graph.graph.AcreData;
import org.acre.visualizer.graph.graph.AcreGraphException;
import org.acre.visualizer.graph.graph.edges.AcreEdge;
import org.acre.visualizer.graph.graph.edges.AcreEdgeData;
import org.acre.visualizer.graph.graph.vertex.AcreVertex;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.DefaultEdge;

/**
 * @author Deepak.Alur@Sun.com
 * @version Dec 14, 2004 12:55:29 PM
 */
public abstract class AcreEdgeImpl
        extends DefaultEdge implements AcreEdge {

    ConnectionSet connectionSet=null;
    String edgeName = "Unknown";
    AcreEdgeData edgeData;
    private AcreVertex parentVertex;

    public AcreEdgeImpl(AcreEdgeData data) {
        super(data);

        setSalsaData(data);
        initialize();
    }

    // must be implemented by the subclasses
    // this method is invoked by setSalsaData() method
    protected abstract void initEdgeAttributes(AcreEdgeData data);

    private void initialize() {
        // create attributes map if not already created
        if (attributes == null)
            attributes = new AttributeMap();

        // if Salsa Data object was supplied,
        // then get attributes from it and add
        Object data = getSalsaData();
        if (data != null) {
            if (data instanceof AcreEdgeData) {
                AcreEdgeData acreEdgeData = (AcreEdgeData) data;
                edgeName = acreEdgeData .getEdgeName();
                AttributeMap edgeAttrMap;
                edgeAttrMap = SalsaJGraphUtils.convertSalsaAttributes(acreEdgeData);
                attributes.putAll(edgeAttrMap);
            }
        }
    }

    public void setSalsaData(AcreData data) {
        if (data == null)
            return;
        
        if (data instanceof AcreEdgeData) {
            edgeData = (AcreEdgeData) data;
            setUserObject(edgeData);
            edgeName = edgeData.getEdgeName();
            initEdgeAttributes((AcreEdgeData)data);
        } else {
            throw new AcreGraphException("AcreData must be AcreEdgeData type for AcreEdge");
        }
    }

    public AcreData getSalsaData() {
        return (AcreData) getUserObject();
    }

    public void setConnectionSet(ConnectionSet cset) {
        this.connectionSet = cset;
    }

    public ConnectionSet getConnectionSet() {
        return connectionSet;
    }

    public String toString() {
        return edgeName;
    }

    public String getEdgeName() {
        return edgeName;
    }

    public void setEdgeName(String edgeName) {
        this.edgeName = edgeName;
    }

    public String getToolTipText() {
        if (edgeData == null)
            return edgeName;

        return edgeName + " from " + edgeData.getFrom() + " to " + edgeData.getTo();
    }


    public AcreVertex getFromVertex() {
        if (getSalsaData() instanceof AcreEdgeData) {
            AcreEdgeData d = (AcreEdgeData) getSalsaData();
            if (d != null) {
                return (AcreVertex) d.getFrom();
            }
        }
        return null;
    }

    public  AcreVertex getToVertex() {
        if (getSalsaData() instanceof AcreEdgeData) {
            AcreEdgeData d = (AcreEdgeData) getSalsaData();
            if (d != null) {
                return (AcreVertex) d.getTo();
            }
        }
        return null;
    }

    public AcreVertex getParentVertex() {
        return parentVertex;
    }

    public void setParentVertex(AcreVertex v) {
        parentVertex = v;
    }


}
