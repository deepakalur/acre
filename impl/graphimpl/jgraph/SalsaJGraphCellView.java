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

import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.VertexView;

/**
 * @author Deepak.Alur@Sun.com
 * @version Dec 14, 2004 3:38:55 PM
 */
public class SalsaJGraphCellView extends VertexView {
   static SalsaVertexRenderer renderer = new SalsaVertexRenderer();
   // Constructor for Superclass
   public SalsaJGraphCellView(Object cell) {
        super(cell);
   }

   // Returns Perimeter Point for Ellipses
   //public Point getPerimeterPoint(Point source, Point p) {
   //}

    // Returns the Renderer for this View
   public CellViewRenderer getRenderer() {
     return renderer;
   }

 }
