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
import org.acre.visualizer.graph.graph.vertex.AcreVertex;
import org.acre.visualizer.graph.graph.vertex.AcreVertexData;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultGraphCell;

/**
 * @author Deepak.Alur@Sun.com
 * @version Dec 14, 2004 11:59:27 AM
 */
public abstract class AcreVertexImpl
        extends DefaultGraphCell
        implements AcreVertex {

    String vertexName = "Unknown";
    AcreVertexData vertexData;

    public AcreVertexImpl(AcreVertexData data) {
        super(data);
        vertexData = data;
        setSalsaData(data);
        initialize();
    }

    private void initialize() {
        if (getSalsaData() != null)
            vertexName = ((AcreVertexData) getSalsaData()).getName();

        // create attributes if not already created
        if (attributes == null) {
            attributes = new AttributeMap();
        }

        // if user object was supplied, get attribtues and add
        Object udata = getUserObject();
        if (udata != null) {
            if (udata instanceof AcreVertexData) {
                AcreVertexData data = (AcreVertexData) udata;

                AttributeMap vAttrMap = SalsaJGraphUtils.convertSalsaAttributes(data);
                attributes.putAll(vAttrMap);
            } else {
                throw new AcreGraphException("Uknown Cell Data '"
                        + udata
                        + "' for Cell '" + this + "'");
            }
        }
    }

    public final void setSalsaData(AcreData acreData) {
        if (acreData == null)
            return;

        if (acreData instanceof AcreVertexData) {
            setUserObject(acreData);
            initialize();
            assignAttributes((AcreVertexData)acreData);
        } else {
            throw new AcreGraphException("AcreData must be AcreVertexData type for AcreVertex");
        }
    }

    public final AcreData getSalsaData() {
        return (AcreData) getUserObject();
    }

    // this method to be implemented by all inheriters
    // this method is invoked by setSalsaData
    protected abstract void assignAttributes(AcreVertexData data);

    public String toString() {
        return vertexName;
    }

    public String getVertexName() {
        return vertexName;
    }

    public void setVertexName(String vertexName) {
        this.vertexName = vertexName;
    }

    // override this method to return more meaningful tooltip
    public String getToolTipText() {        
        return vertexName;
    }

}
