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
package org.acre.visualizer.graph;

import org.acre.visualizer.graph.edges.AcreEdge;
import org.acre.visualizer.graph.vertex.AcreVertex;

/**
 * Created by IntelliJ IDEA.
 * User: deepakalur
 * Date: Jan 20, 2005
 * Time: 4:05:58 PM
 * To change this template use File | Settings | File Templates.
 */
public interface AcreGraphSelectionListener {
    // invoked if a single vertex is selected on the graph
    void vertexSelected(AcreVertex v);

    // invoked if a single edge is selected on the graph
    void edgeSelected(AcreEdge e);

    // invoked if several items (edges and/or vertices) are selected on the graph
    // graphObjects contains an array of selected edges/vertices
    void multipleSelected(Object[] graphObjects);

    // invoked if all items are cleard on the graph, i.e. nothing is selected
    void noneSelected();
}
