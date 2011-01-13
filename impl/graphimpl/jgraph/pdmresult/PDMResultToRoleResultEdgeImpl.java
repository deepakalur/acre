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
package org.acre.visualizer.graphimpl.jgraph.pdmresult;

import org.acre.visualizer.graphimpl.jgraph.AcreEdgeImpl;
import org.acre.visualizer.graph.graph.edges.AcreEdgeData;
import org.acre.visualizer.graph.graph.pdmresult.QueryResultVertex;
import org.acre.visualizer.graph.graph.pdmresult.edges.PDMResultToRoleResultEdge;
import org.acre.visualizer.graph.graph.pdmresult.vertex.PDMResultVertex;
import org.acre.visualizer.graph.graph.pdmresult.vertex.RoleResultVertex;

/**
 * @author Deepak.Alur@Sun.com
 * @version Dec 22, 2004 10:53:50 AM
 */
public class PDMResultToRoleResultEdgeImpl
        extends AcreEdgeImpl
        implements PDMResultToRoleResultEdge {
    public PDMResultToRoleResultEdgeImpl(
            PDMResultVertex pdmResultVertex,
            PDMResultVertex rolePDMResultVertex) {
        super(null);
        AcreEdgeData data = new AcreEdgeData("",
                null, // todo - change this to avoid null
                pdmResultVertex,
                rolePDMResultVertex);
        data.initDefaults();
        initEdgeAttributes(data);
        setSalsaData(data);
    }

    public PDMResultToRoleResultEdgeImpl(PDMResultVertex pdmResultVertex, RoleResultVertex roleResultVertex) {
        super(null);
        AcreEdgeData data = new AcreEdgeData("",
                null, // todo - change this to avoid null
                pdmResultVertex,
                roleResultVertex);
        data.initDefaults();
        initEdgeAttributes(data);
        setSalsaData(data);

    }

    public PDMResultToRoleResultEdgeImpl(PDMResultVertex pdmResultVertex, QueryResultVertex queryResultVertex) {
        super(null);
        AcreEdgeData data = new AcreEdgeData("",
                null, // todo - change this to avoid null
                pdmResultVertex,
                queryResultVertex);
        data.initDefaults();
        initEdgeAttributes(data);
        setSalsaData(data);
    }

    // must be implemented by the subclasses
    // this method is invoked by setSalsaData() method
    protected void initEdgeAttributes(AcreEdgeData data) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
