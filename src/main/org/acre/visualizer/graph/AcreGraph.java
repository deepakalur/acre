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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;

/**
 * @author Deepak.Alur@Sun.com
 * @version Dec 20, 2004 4:11:06 PM
 */
public interface AcreGraph {
    int ANNEALING_LAYOUT = 0;
    int CIRCLE_GRAPH_LAYOUT = 1;
    int GEM_LAYOUT = 2;
    int MOEN_LAYOUT = 3;
    int RADIAL_TREE_LAYOUT = 4;
    int SPRING_EMBEDDED_LAYOUT = 5;
    int SUGIYAMA_LAYOUT = 6;
    int TREE_LAYOUT = 7;
    int DEFAULT_LAYOUT = SUGIYAMA_LAYOUT;
    double DEFAULTSCALE = 1.0;
    double SCALEFACTOR = 0.25;
    Dimension DEFAULT_GRAPH_SIZE = new Dimension(750, 750);

    void applyLayout(int layoutType);

    void applyLayout();

    void setLayoutType(int layoutType);

    void hideSelectedCells();

    void showHiddenCells();

    void zoomIn(Point point);

    void zoomOut(Point point);

    void zoomRevert();

    void zoomIn();

    void zoomOut();

    void groupSelected();

    void ungroupSelected();

// todo   void expand(Object [] parentVertices);
//
// todo   void collapse(Object [] parentVertices);

    // Determines if a Cell is a Group
    boolean isGroup(Object cell);

    // Brings the Specified Cells to Front
    void toFront(Object[] c);

    // Sends the Specified Cells to Back
    void toBack(Object[] c);

    void setAcreGraphModel(AcreGraphModel model);

    AcreGraphModel getAcreGraphModel();

    void setScale(double newScale);

    public double getScale();

    int getSelectionCount();

    Object[] getSelectionCells();

    Object getFirstCellForLocation(double x, double y);

    void removeAllCells();

    void setGridVisible(boolean b);

    Component getView();


    void selectAll();

    void addSelectionListener(AcreGraphSelectionListener l);

    void removeSelectionListeners() ;

    AcreGraphSelectionListener [] getSelectionListeners();

}
