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

import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.VertexView;

/**
 * @author Deepak.Alur@Sun.com
 * @version Dec 16, 2004 4:52:46 PM
 */
public class SalsaCellViewFactory extends DefaultCellViewFactory{
    protected VertexView createVertexView(Object cell) {
        if (cell instanceof AcreVertexImpl) {
            return new SalsaJGraphCellView(cell);
        } else {
            return super.createVertexView(cell);
        }
    }

//            protected EdgeView createEdgeView(Object edge) {
//                if (edge instanceof AcreEdge) {
//                    return new SalsaEdgeView(edge);
//                } else {
//                    return super.createEdgeView(edge);
//                }
//            }

}
