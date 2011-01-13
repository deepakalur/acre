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

import org.acre.config.ConfigService;
import org.acre.visualizer.graph.graph.AcreGraphModel;
import org.acre.visualizer.graph.graph.edges.AcreEdge;
import org.acre.visualizer.graph.graph.edges.AcreEdgeData;
import org.acre.visualizer.graph.graph.vertex.AcreVertex;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.DefaultPort;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author Deepak.Alur@Sun.com
 * @version Dec 18, 2004 5:32:55 PM
 */
public abstract class AcreGraphModelImpl
        extends DefaultGraphModel
        implements AcreGraphModel {
    protected Logger logger;

    HashMap edges = new HashMap();
    HashMap vertices = new HashMap();

    public AcreGraphModelImpl() {
        super();
        initialize();
    }

    public AcreGraphModelImpl(List roots, AttributeMap attributes) {
        super(roots, attributes);
        initialize();
    }

    protected void initialize() {
        logger = ConfigService.getInstance().getLogger(this);
    }

    public void createConnection(AcreEdge edge, AcreVertex start, AcreVertex end) {
        String connName = ((AcreEdgeData)edge.getSalsaData()).getEdgeName();

        AcreEdgeImpl jEdge = (AcreEdgeImpl) edge;
        AcreVertexImpl startVertex = (AcreVertexImpl) start;
        AcreVertexImpl endVertex = (AcreVertexImpl) end;

        DefaultPort source = new DefaultPort(connName + "StartPort");
        source.addEdge(edge);
        startVertex.add(source);

        DefaultPort target = new DefaultPort(connName + "EndPort");
        target.addEdge(edge);
        endVertex.add(target);

        ConnectionSet cs = new ConnectionSet(edge, source, target);

        jEdge.setConnectionSet(cs);
    }

    public void insertSalsaVertex(AcreVertex v) {
        if (checkExists(v)) return;

        Map graphAttributes;
        Object roleCells []  = {v};
        graphAttributes = new Hashtable();
        AttributeMap vAttrMap = SalsaJGraphUtils.convertSalsaAttributes(v.getSalsaData());
        graphAttributes.put(v, vAttrMap);

        logger.info("Inserting Vertex " + v);

        vertices.put(v, v);
        this.insert(roleCells, graphAttributes, null, null, null);
    }

    private boolean checkExists(AcreVertex v) {
        if (vertices.get(v) != null)
            return true;
        else
            return false;
    }

    private boolean checkExists(AcreEdge e) {
        if (true) return false; // hack
        
        if (edges.get(e) != null)
            return true;
        else
            return false;
    }

    public void insertSalsaEdge(AcreEdge edge) {
        if (checkExists(edge)) return;

        AcreEdgeImpl jEdge = (AcreEdgeImpl) edge;
        Map graphAtributes = new Hashtable();

        AttributeMap eAttrMap = SalsaJGraphUtils.convertSalsaAttributes(edge.getSalsaData());
        graphAtributes.put(edge, eAttrMap);
        Object[] rpCells = {edge};
        edges.put(edge, edge);
        insert(rpCells, graphAtributes, jEdge.getConnectionSet(), null, null);
    }

    public Object[] getAllCells() {
        return getDescendants(this, getRoots(this)).toArray();
    }

    public void remove(Object[] cells) {
        super.remove(cells);
    }

    public void clearAll() {
        edges.clear();
        vertices.clear();
        remove(getAllCells());
    }

}
