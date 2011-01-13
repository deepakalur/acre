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
package org.acre.visualizer.graph.edges;

import org.acre.visualizer.graph.AcreAttributes;
import org.acre.visualizer.graph.AcreData;

import java.awt.Color;

/**
 * @author Deepak.Alur@Sun.com
 * @version Dec 14, 2004 12:56:35 PM
 */
public class AcreEdgeData implements AcreData {
    String edgeName;
    Object relationship;
    Object from;
    Object to;
    int edgeType;
    public static final String DEFAULT_HAS_NAME = "has";
    public static final String DEFAULT_MAPS_NAME = "maps";
    public static final String DEFAULT_RELATIONSHIP_DATA = "DEFAULT_RELATIONSHIP";
    private boolean dirty=false;
    AcreAttributes acreAttributes = new AcreAttributes();

    public AcreEdgeData(String edgeName, Object relationship,
                         Object from, Object to) {
        this.edgeName = edgeName;
        this.from = from;
        this.relationship = relationship;
        this.to = to;
        acreAttributes = new AcreAttributes();
    }

    // todo  move initDefaults to constructor of AcreEdgeData
    public void initDefaults() {
        // if you set the line style, the you must also set routing
        //useOrthogonalLineStyle();
        //useSimpleRouting();

        setLineEndArrowNone();
        setEndFill(true);
        setBendable(true);
        setDisconnectable(false);
        setConnectable(false);
        showLabel(true);
        // setMovable(false);
        setDirty(false);
    }

    public String getEdgeName() {
        if (edgeName != null)
            return edgeName;
        else return "";
    }

    public void setEdgeName(String edgeName) {
        this.edgeName = edgeName;
        setDirty(true);
    }

    public Object getRelationship() {
        return relationship;
    }

    public void setRelationship(Object relationship) {
        this.relationship = relationship;
        setDirty(true);
    }

    public Object getFrom() {
        return from;
    }

    public void setFrom(Object from) {
        this.from = from;
        setDirty(true);
    }

    public Object getTo() {
        return to;
    }

    public void setTo(Object to) {
        this.to = to;
        setDirty(true);
    }

    public AcreAttributes getAcreAttributes() {
        return acreAttributes;
    }

    public void setAcreAttributes(AcreAttributes attributes) {
        // todo - implemented method body
    }

    public void setSalsaAttribtes(AcreAttributes attributes) {
        this.acreAttributes = attributes;
        setDirty(true);
    }

    public String getName() {
        return edgeName;
    }

    public void setName(String name) {
        setEdgeName(name);
    }

    public int getEdgeType() {
        return edgeType;
    }

    public void setEdgeType(int edgeType) {
        this.edgeType = edgeType;
        setDirty(true);
    }

    public Object getData() {
        return relationship;
    }

    public void setData(Object data) {
        setRelationship(data);
        setDirty(true);
    }

    public void setDirty(boolean flag) {
        dirty = flag;
    }

    public boolean isDirty() {
        return dirty;
    }

    public String toString() {
        return edgeName;
    }

    public void useDottedLine() {
        acreAttributes.setDottedLinePattern(new float[] {4.0f, 2.0f});
        //GraphConstants.setDashPattern(edgeAttributes, new float[] {4.0f, 2.0f});
        setDirty(true);
    }

    public void useSolidLine() {
        acreAttributes.remove(AcreAttributes.DOTTED_LINE_PATTERN);
        setDirty(true);
    }

    public void useSplineLineStyle() {
        acreAttributes.setLineStyleSpline();
        //GraphConstants.setLineStyle(edgeAttributes, GraphConstants.STYLE_SPLINE);
        setDirty(true);
    }

    public void useOrthogonalLineStyle() {
        acreAttributes.setLineStyleOrthogonal();
        setDirty(true);
    }

    public void useBezierLineStyle() {
        acreAttributes.setLineStyleBezier();
        setDirty(true);
    }

    public void useSimpleRouting() {
        acreAttributes.setRoutingSimple();
        //GraphConstants.setRouting(edgeAttributes, GraphConstants.ROUTING_SIMPLE);
        setDirty(true);
    }

    public void useParallelEdgeRouting() {
        acreAttributes.setRoutingParallelEdge();
//        JGraphParallelEdgeRouter router = new JGraphParallelEdgeRouter();
//        router.setEdgeSeparation(20.0d);
//        GraphConstants.setRouting(edgeAttributes, router);
        setDirty(true);
    }

    public void showLabel(boolean show) {
        acreAttributes.showEdgeLabel(show);
        //GraphConstants.setLabelAlongEdge(edgeAttributes, show);
        setDirty(true);
    }

    public void setMovable(boolean movable) {
        acreAttributes.setMovable(movable);
        //GraphConstants.setMoveable(edgeAttributes, movable);
        setDirty(true);
    }

    public void setEndFill(boolean fill) {
        acreAttributes.setEndFill(fill);
        //GraphConstants.setEndFill(edgeAttributes, fill);
        setDirty(true);
    }

    public void setBendable(boolean bendable) {
        acreAttributes.setBendable(bendable);
        //GraphConstants.setBendable(edgeAttributes, bendable);
        setDirty(true);
    }

    private void setDisconnectable(boolean flag) {
        acreAttributes.setDisconnectable(flag);
        //GraphConstants.setDisconnectable(edgeAttributes, flag);
        setDirty(true);
    }
    private void setConnectable(boolean flag) {
        acreAttributes.setConnectable(flag);
        //GraphConstants.setConnectable(edgeAttributes, flag);
        setDirty(true);
    }

    public void setLineBeginArrowNone() {
        acreAttributes.setLineBeginArrowNone();
//        GraphConstants.setLineBegin(edgeAttributes, GraphConstants.ARROW_NONE);
        setDirty(true);
    }
    public void setLineEndArrowNone() {
        acreAttributes.setLineEndArrowNone();
        //GraphConstants.setLineEnd(edgeAttributes, GraphConstants.ARROW_NONE);
        setDirty(true);
    }

    public void setLineBeginArrowClassic() {
        acreAttributes.setLineBeginArrowClassic();
        //GraphConstants.setLineBegin(edgeAttributes, GraphConstants.ARROW_CLASSIC);
        setDirty(true);
    }
    public void setLineEndArrowClassic() {
        acreAttributes.setLineEndArrowClassic();
        //GraphConstants.setLineEnd(edgeAttributes, GraphConstants.ARROW_CLASSIC);
        setDirty(true);
    }

    public void setLineBeginArrowTechnical() {
        acreAttributes.setLineBeginArrowTechnical();
        //GraphConstants.setLineBegin(edgeAttributes, GraphConstants.ARROW_TECHNICAL);
        setDirty(true);
    }
    public void setLineEndArrowTechnical() {
        acreAttributes.setLineEndArrowTechnical();
        //GraphConstants.setLineEnd(edgeAttributes, GraphConstants.ARROW_TECHNICAL);
        setDirty(true);
    }

    public void setLineBeginArrowSimple() {
        acreAttributes.setLineBeginArrowSimple();
        //GraphConstants.setLineBegin(edgeAttributes, GraphConstants.ARROW_SIMPLE);
        setDirty(true);
    }
    public void setLineEndArrowSimple() {
        acreAttributes.setLineEndArrowSimple();
        //GraphConstants.setLineEnd(edgeAttributes, GraphConstants.ARROW_SIMPLE);
        setDirty(true);
    }

    public void setLineBeginArrowCircle() {
        acreAttributes.setLineBeginArrowCircle();
        //GraphConstants.setLineBegin(edgeAttributes, GraphConstants.ARROW_CIRCLE);
        setDirty(true);
    }
    public void setLineEndArrowCircle() {
        acreAttributes.setLineEndArrowCircle();
        //GraphConstants.setLineEnd(edgeAttributes, GraphConstants.ARROW_CIRCLE);
        setDirty(true);
    }

    public void setLineBeginArrowLine() {
        acreAttributes.setLineBeginArrowLine();
        //GraphConstants.setLineBegin(edgeAttributes, GraphConstants.ARROW_LINE);
        setDirty(true);
    }
    public void setLineEndArrowLine() {
        acreAttributes.setLineEndArrowLine();
        //GraphConstants.setLineEnd(edgeAttributes, GraphConstants.ARROW_LINE);
        setDirty(true);
    }

    public void setLineBeginArrowDoubleLine() {
        acreAttributes.setLineBeginArrowDoubleLine();
        //GraphConstants.setLineBegin(edgeAttributes, GraphConstants.ARROW_DOUBLELINE);
        setDirty(true);
    }
    public void setLineEndArrowDoubleLine() {
        acreAttributes.setLineEndArrowDoubleLine();
        //GraphConstants.setLineEnd(edgeAttributes, GraphConstants.ARROW_DOUBLELINE);
        setDirty(true);
    }

    public void setLineBeginArrowDiamond() {
        acreAttributes.setLineBeginArrowDiamond();
        //GraphConstants.setLineBegin(edgeAttributes, GraphConstants.ARROW_DIAMOND);
        setDirty(true);
    }
    public void setLineEndArrowDiamond() {
        acreAttributes.setLineEndArrowDiamond();
        //GraphConstants.setLineEnd(edgeAttributes, GraphConstants.ARROW_DIAMOND);
        setDirty(true);
    }

    public void setLineColor(Color c) {
        acreAttributes.setLineColor(c);
        //GraphConstants.setLineColor(edgeAttributes, c);
        setDirty(true);
    }

    public void setLineWidth(float pixels) {
        acreAttributes.setLineWidth(pixels);
        //GraphConstants.setLineWidth(edgeAttributes, (float) (pixels*1.0f));
        setDirty(true);
    }

}
