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
 * This represents a model that is used to create a graph of objects in Salsa
 * Visualization. Graph model implementations have to implement this interface.
 *
 */
public interface AcreGraphModel {

    public void insertAcreEdge(AcreEdge edge);
    public void insertAcreVertex(AcreVertex vertex);
    public void createConnection(AcreEdge edge, AcreVertex start, AcreVertex end);
    public Object[] getAllCells();

    void remove(Object[] cells);
    void clearAll();
}
