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
package org.acre.visualizer.graph.popup;

import org.acre.visualizer.graph.AcreGraph;
import org.acre.visualizer.graph.edges.AcreEdge;
import org.acre.visualizer.graph.pdmresult.vertex.ArtifactVertex;
import org.acre.visualizer.graph.vertex.AcreVertex;

import java.awt.event.ActionEvent;

/**
 * @author Deepak.Alur@Sun.com
 * @version Dec 22, 2004 10:53:58 PM
 */
public class ArtifactVertexPopup extends GraphPopup {

    public ArtifactVertexPopup(AcreGraph graph, ArtifactVertex artifactVertex) {
        super(graph, artifactVertex, "Artifact");
    }

    // subclasses to override this method if needed
    protected void handleEdgeAction(ActionEvent e, GraphPopup popup, AcreGraph graph, AcreEdge edge) {
        // do nothing
    }

    // subclasses to override this method if needed
    protected void handleVertexAction(ActionEvent e, GraphPopup popup, AcreGraph graph, AcreVertex vertex) {
        showFeatureNotImplemented();
    }

    protected void initializeMenus() {
        // todo - implemented method body
    }
}
