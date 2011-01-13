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

import org.acre.visualizer.graph.graph.AcreAttributes;
import org.acre.visualizer.graph.graph.AcreData;
import org.acre.visualizer.graph.graph.edges.AcreEdgeData;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.Edge;
import org.jgraph.graph.GraphConstants;
import org.jgraph.util.JGraphParallelEdgeRouter;

/**
 * @author Deepak.Alur@Sun.com
 * @version Dec 23, 2004 4:00:12 PM
 */
public class SalsaJGraphUtils {
    public static AttributeMap convertSalsaAttributes(AcreData data) {
        AttributeMap map = new AttributeMap();

        AcreAttributes acreAttributes = data.getSalsaAttributes();

        GraphConstants.setAutoSize(map, acreAttributes.isAutoSize());
        
        if (acreAttributes.getBackgroundColor() != null)
            GraphConstants.setBackground(map, acreAttributes.getBackgroundColor());
        
        if (acreAttributes.getForegoundColor() != null)
            GraphConstants.setForeground(map, acreAttributes.getForegoundColor());
        
        if (acreAttributes.getBorder() != null)
            GraphConstants.setBorder(map, acreAttributes.getBorder());
        
        if (acreAttributes.getBounds() != null)
            GraphConstants.setBounds(map, acreAttributes.getBounds());
                        
        GraphConstants.setConnectable(map, acreAttributes.isConnectable());
        GraphConstants.setDisconnectable(map, acreAttributes.isDisconnectable());
        GraphConstants.setEditable(map, acreAttributes.isEditable());
        GraphConstants.setMoveable(map, acreAttributes.isMovable());
        GraphConstants.setOpaque(map, acreAttributes.isOpaque());

        if (data instanceof AcreEdgeData) {
            // these apply for edges only
            if (acreAttributes.getBeginFill() != null)
                GraphConstants.setBeginFill(map, acreAttributes.isBeginFill());

            if (acreAttributes.getEndFill() != null)
                GraphConstants.setEndFill(map, acreAttributes.isEndFill());

            if ((acreAttributes.getDottedLinePattern() != null) &&
                (acreAttributes.getDottedLinePattern().length != 0))
                GraphConstants.setDashPattern(map, acreAttributes.getDottedLinePattern());

            if (acreAttributes.getShowEdgeLabel() != null)
                GraphConstants.setLabelAlongEdge(map, acreAttributes.isShowEdgeLabel());

            GraphConstants.setLineBegin(map, getLineBeginEndStyle(true, acreAttributes));

            if (acreAttributes.getLineColor() != null)
                GraphConstants.setLineColor(map, acreAttributes.getLineColor());

            GraphConstants.setLineEnd(map, getLineBeginEndStyle(false, acreAttributes));

            GraphConstants.setLineStyle(map, getLineStyle(acreAttributes.getLineStyle()));

            GraphConstants.setLineWidth(map, acreAttributes.getLineWidth());

            if (acreAttributes.getRouting() != null)
                GraphConstants.setRouting(map, getRouting(acreAttributes.getRouting()));
        }
        
        return map;        
    }

    private static Edge.Routing getRouting(String routing) {
        if (routing == null)
            return null;
        
        if (routing.equals(AcreAttributes.ROUTING_PARALLEL_EDGE))
            // return new JGraphParallelEdgeRouter(); JGraph 5.2
            return JGraphParallelEdgeRouter.getSharedInstance();
        if (routing.equals(AcreAttributes.ROUTING_SIMPLE))
            return GraphConstants.ROUTING_SIMPLE;
        
        return null;
    }


    private static int getLineStyle(String lineStyle) {
        if (lineStyle == null)
            return GraphConstants.STYLE_ORTHOGONAL;
        
        if (lineStyle.equals(AcreAttributes.LINE_STYLE_BEZIER))
            return GraphConstants.STYLE_BEZIER;
        
        if (lineStyle.equals(AcreAttributes.LINE_STYLE_BEZIER))
            return GraphConstants.STYLE_BEZIER;

        if (lineStyle.equals(AcreAttributes.LINE_STYLE_BEZIER))
            return GraphConstants.STYLE_BEZIER;
        
        return GraphConstants.STYLE_ORTHOGONAL;
        
    }

    private static int getLineBeginEndStyle(boolean begin, AcreAttributes acreAttributes) {
        String style = null;
        if (begin)
            style = acreAttributes.getLineBegin();
        else
            style = acreAttributes.getLineEnd();

        if (style == null)
            return GraphConstants.ARROW_NONE;
        
        if (style.equalsIgnoreCase(AcreAttributes.ARROW_CIRCLE))
            return GraphConstants.ARROW_CIRCLE;
        if (style.equalsIgnoreCase(AcreAttributes.ARROW_CLASSIC))
            return GraphConstants.ARROW_CLASSIC;
        if (style.equalsIgnoreCase(AcreAttributes.ARROW_DIAMOND))
                    return GraphConstants.ARROW_DIAMOND;
        if (style.equalsIgnoreCase(AcreAttributes.ARROW_DOUBLE_LINE))
                    return GraphConstants.ARROW_DOUBLELINE;
        if (style.equalsIgnoreCase(AcreAttributes.ARROW_LINE))
                    return GraphConstants.ARROW_LINE;
        if (style.equalsIgnoreCase(AcreAttributes.ARROW_NONE))
                    return GraphConstants.ARROW_NONE;
        if (style.equalsIgnoreCase(AcreAttributes.ARROW_SIMPLE))
                    return GraphConstants.ARROW_SIMPLE;
        if (style.equalsIgnoreCase(AcreAttributes.ARROW_TECHNICAL))
                    return GraphConstants.ARROW_TECHNICAL;
        
        return GraphConstants.ARROW_NONE;
    }
}
