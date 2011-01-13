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

import javax.swing.Icon;
import javax.swing.border.Border;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.Hashtable;

/**
 * @author Deepak.Alur@Sun.com
 * @version Dec 23, 2004 3:44:57 PM
 */
public class AcreAttributes extends Hashtable {
    public static final String COLOR = "COLOR";
    public  static final String OPAQUE = "OPAQUE";
    public  static final String BORDER = "BORDER";
    public  static final String AUTOSIZE = "AUTOSIZE";
    public  static final String BOUNDS = "BOUNDS";
    public  static final String CELL_IMAGE_NAME = "CELL_IMAGE_NAME";
    public  static final String CELL_ICON = "CELL_ICON";
    public  static final String DOTTED_LINE_PATTERN = "DOTTED_LINE_PATTERN";
    public  static final String LINE_STYLE = "LINE_STYLE";

    public  static final String LINE_STYLE_SPLINE = "LINE_STYLE_SPLINE";
    public  static final String LINE_STYLE_BEZIER = "LINE_STYLE_BEZIER";
    public  static final String LINE_STYLE_ORTHOGONAL = "LINE_STYLE_ORTHOGONAL";

    public  static final String LINE_ROUTING = "LINE_ROUTING";
    public  static final String ROUTING_SIMPLE = "ROUTING_SIMPLE";
    public  static final String ROUTING_PARALLEL_EDGE = "ROUTING_PARALLEL_EDGE";
    public  static final String SHOW_EDGE_LABEL = "SHOW_EDGE_LABEL";
    public  static final String MOVABLE = "MOVABLE";
    public  static final String END_FILL = "END_FILL";
    private static final String BEGIN_FILL = "BEGIN_FILL";
    public  static final String BENDABLE = "BENDABLE";
    public  static final String DISCONNECTABLE = "DISCONNECTABLE";
    public  static final String CONNECTABLE = "CONNECTABLE";

    public  static final String LINE_BEGIN = "LINE_BEGIN";
    public  static final String LINE_END = "LINE_END";

    public  static final String ARROW_NONE = "ARROW_NONE";
    public  static final String ARROW_CLASSIC = "ARROW_CLASSIC";
    public  static final String ARROW_TECHNICAL = "ARROW_TECHNICAL";
    public  static final String ARROW_SIMPLE = "ARROW_SIMPLE";
    public  static final String ARROW_CIRCLE = "ARROW_CIRCLE";
    public  static final String ARROW_LINE = "ARROW_LINE";
    public  static final String ARROW_DOUBLE_LINE = "ARROW_DOUBLE_LINE";
    public  static final String ARROW_DIAMOND = "ARROW_DIAMOND";
    public  static final String LINE_COLOR = "LINE_COLOR";
    public  static final String LINE_WIDTH = "LINE_WIDTH";
    public  static final float DEFAULT_LINE_WIDTH = 1.0f;
    public  static final String BACKGROUND_COLOR = "BACKGROUND_COLOR";
    public  static final String EDITABLE = "EDITABLE";
    public  static final String FOREGROUND_COLOR = "FOREGROUND_COLOR";
    private static final boolean DEFAULT_CONNECTABLE = false;
    private static final boolean DEFAULT_DISCONNECTABLE = false;
    private static final boolean DEFAULT_EDITABLE = false;
    private static final boolean DEFAULT_MOVEABLE = true;
    private static final boolean DEFAULT_OPAQUE = false;
    private static final boolean DEFAULT_AUTOSIZE = false;
    private static final boolean DEFAULT_SHOW_EDGE_LABEL = false;
    private static final boolean DEFAULT_END_FILL = true;
    private static final boolean DEFAULT_BENDABLE = true;
    private static final boolean DEFAULT_BEGIN_FILL = true;


    public AcreAttributes() {
        super();
    }

    public void setColor(Color c) {
        setBackgroundColor(c);
    }

    public Color getColor() {
        return getBackgroundColor();
    }

    public void setOpaque(boolean flag) {
        put(OPAQUE, new Boolean(flag));
    }

    public boolean isOpaque() {
        Boolean b = (Boolean) get(OPAQUE);
        if (b != null)
            return b.booleanValue();

        return DEFAULT_OPAQUE;
    }

    public void setBorder(Border border) {
        put(BORDER, border);
    }

    public Border getBorder() {
        return (Border) get(BORDER);
    }

    public void setAutoSize(boolean flag) {
        put(AUTOSIZE, new Boolean(flag) );
    }

    public boolean isAutoSize() {
        Boolean b = (Boolean) get(AUTOSIZE);
        if (b != null)
            return b.booleanValue();

        return DEFAULT_AUTOSIZE;
    }

    public Rectangle2D getBounds() {
        return (Rectangle2D) get(BOUNDS);
    }

    public void setBounds(Rectangle2D bounds) {
        put(BOUNDS, bounds);
    }

    public void setBounds(int x, int y, int width, int height) {
        Rectangle r = new Rectangle(x, y, width, height);
        setBounds(r);
    }

    public String getCellImageName() {
        return (String) get(CELL_IMAGE_NAME);
    }

    public void setCellImageName(String cellImageName) {
        put(CELL_IMAGE_NAME, cellImageName);
    }

    public void setCellIcon(Icon icon) {
        put(CELL_ICON, icon);
    }

    public Icon getCellIcon() {
        return (Icon) get(CELL_ICON);
    }

    public void setDottedLinePattern(float[] dotPattern) {
        put (DOTTED_LINE_PATTERN, dotPattern);
    }

    public float[] getDottedLinePattern() {
        return (float[]) get(DOTTED_LINE_PATTERN);
    }

    public String getLineStyle() {
        return (String) get(LINE_STYLE);
    }

    public void setLineStyleSpline() {
        put (LINE_STYLE, LINE_STYLE_SPLINE);
    }

    public void setLineStyleBezier() {
        put (LINE_STYLE, LINE_STYLE_BEZIER);
    }

    public void setLineStyleOrthogonal() {
        put (LINE_STYLE, LINE_STYLE_ORTHOGONAL);
    }

    public void setRoutingSimple() {
        put (LINE_ROUTING, ROUTING_SIMPLE);
    }

    public void setRoutingParallelEdge() {
        put (LINE_ROUTING, ROUTING_PARALLEL_EDGE);
    }

    public void showEdgeLabel(boolean show) {
        put (SHOW_EDGE_LABEL, new Boolean(show));
    }

    public boolean isShowEdgeLabel() {
        Boolean b = (Boolean) get(SHOW_EDGE_LABEL);
        if (b != null)
            return b.booleanValue();

        return DEFAULT_SHOW_EDGE_LABEL;
    }

    public void setMovable(boolean movable) {
        put (MOVABLE, new Boolean(movable));
    }

    public boolean isMovable() {
        Boolean b = (Boolean) get(MOVABLE);
        if (b != null)
            return b.booleanValue();

        return DEFAULT_MOVEABLE;

    }

    public void setEndFill(boolean fill) {
        put (END_FILL, new Boolean(fill));
    }

    public boolean isEndFill() {
        Boolean b = (Boolean) get(END_FILL);
        if (b != null)
            return b.booleanValue();

        return DEFAULT_END_FILL;

    }

    public void setBendable(boolean bendable) {
        put (BENDABLE, new Boolean(bendable));
    }

    public boolean isBendable() {
        Boolean b = (Boolean) get(BENDABLE);
        if (b != null)
            return b.booleanValue();

        return DEFAULT_BENDABLE;
    }

    public void setDisconnectable(boolean flag) {
        put (DISCONNECTABLE, new Boolean(flag));
    }

    public boolean isDisconnectable() {
        Boolean b = (Boolean) get(DISCONNECTABLE);
        if (b != null)
            return b.booleanValue();

        return DEFAULT_DISCONNECTABLE;
    }

    public void setConnectable(boolean flag) {
        put (CONNECTABLE, new Boolean(flag));
    }

    public boolean isConnectable() {
        Boolean b = (Boolean) get(CONNECTABLE);
        if (b != null)
            return b.booleanValue();

        return DEFAULT_CONNECTABLE;
    }

    public void setLineBeginArrowNone() {
        put(LINE_BEGIN, ARROW_NONE);
    }

    public boolean isLineBeginArrowNone() {
        String begin = (String) get(LINE_BEGIN);
        if ((begin !=null) && (begin.equals(ARROW_NONE)))
            return true;

        return false;
    }

    public void setLineEndArrowNone() {
        put (LINE_END, ARROW_NONE);
    }

    public boolean isLineEndArrowNone() {
        String end = (String) get(LINE_END);
        if ((end !=null) && (end.equals(ARROW_NONE)))
            return true;

        return false;
    }

    public void setLineBeginArrowClassic() {
        put (LINE_BEGIN, ARROW_CLASSIC);
    }

    public boolean isLineBeginArrowClassic() {
        String end = (String) get(LINE_BEGIN);
        if ((end !=null) && (end.equals(ARROW_CLASSIC)))
            return true;

        return false;
    }

    public void setLineEndArrowClassic() {
        put (LINE_END, ARROW_CLASSIC);
    }

    public boolean isLineEndArrowClassic() {
        String end = (String) get(LINE_END);
        if ((end !=null) && (end.equals(ARROW_CLASSIC)))
            return true;

        return false;
    }


    public void setLineBeginArrowTechnical() {
        put (LINE_BEGIN, ARROW_TECHNICAL);
    }

    public boolean isLineBeginArrowTechnical() {
        String end = (String) get(LINE_BEGIN);
        if ((end !=null) && (end.equals(ARROW_TECHNICAL)))
            return true;

        return false;
    }


    public void setLineEndArrowTechnical() {
        put (LINE_END, ARROW_TECHNICAL);
    }

    public boolean isLineEndArrowTechnical() {
        String end = (String) get(LINE_END);
        if ((end !=null) && (end.equals(ARROW_TECHNICAL)))
            return true;

        return false;
    }


    public void setLineBeginArrowSimple() {
        put (LINE_BEGIN, ARROW_SIMPLE);
    }

    public boolean isLineBeginArrowSimple() {
        String end = (String) get(LINE_BEGIN);
        if ((end !=null) && (end.equals(ARROW_SIMPLE)))
            return true;

        return false;
    }

    public void setLineEndArrowSimple() {
        put (LINE_END, ARROW_SIMPLE);
    }

    public boolean isLineEndArrowSimple() {
        String end = (String) get(LINE_END);
        if ((end !=null) && (end.equals(ARROW_SIMPLE)))
            return true;

        return false;
    }


    public void setLineBeginArrowCircle() {
        put (LINE_BEGIN, ARROW_CIRCLE);
    }

    public boolean isLineBeginArrowCircle() {
        String end = (String) get(LINE_BEGIN);
        if ((end !=null) && (end.equals(ARROW_CIRCLE)))
            return true;

        return false;
    }


    public void setLineEndArrowCircle() {
        put (LINE_END, ARROW_CIRCLE);
    }

    public boolean isLineEndArrowCircle() {
        String end = (String) get(LINE_END);
        if ((end !=null) && (end.equals(ARROW_CIRCLE)))
            return true;

        return false;
    }

    public void setLineBeginArrowLine() {
        put (LINE_BEGIN, ARROW_LINE);
    }

    public boolean isLineBeginArrowLine() {
        String end = (String) get(LINE_BEGIN);
        if ((end !=null) && (end.equals(ARROW_LINE)))
            return true;

        return false;
    }

    public void setLineEndArrowLine() {
        put (LINE_END, ARROW_LINE);
    }

    public boolean isLineEndArrowLine() {
        String end = (String) get(LINE_END);
        if ((end !=null) && (end.equals(ARROW_LINE)))
            return true;

        return false;
    }

    public void setLineBeginArrowDoubleLine() {
        put (LINE_BEGIN, ARROW_DOUBLE_LINE);
    }

    public boolean isLineBeginArrowDoubleLine() {
        String end = (String) get(LINE_BEGIN);
        if ((end !=null) && (end.equals(ARROW_DOUBLE_LINE)))
            return true;

        return false;
    }

    public void setLineEndArrowDoubleLine() {
        put (LINE_END, ARROW_DOUBLE_LINE);
    }

    public boolean isLineEndArrowDoubleLine() {
        String end = (String) get(LINE_END);
        if ((end !=null) && (end.equals(ARROW_DOUBLE_LINE)))
            return true;

        return false;
    }

    public void setLineBeginArrowDiamond() {
        put (LINE_BEGIN, ARROW_DIAMOND);
    }

    public boolean isLineBeginArrowDiamond() {
        String end = (String) get(LINE_BEGIN);
        if ((end !=null) && (end.equals(ARROW_DIAMOND)))
            return true;

        return false;
    }


    public void setLineEndArrowDiamond() {
        put (LINE_END, ARROW_DIAMOND);
    }

    public boolean isLineEndArrowDiamond() {
        String end = (String) get(LINE_END);
        if ((end !=null) && (end.equals(ARROW_DIAMOND)))
            return true;

        return false;
    }


    public void setLineColor(Color c) {
        put (LINE_COLOR, c);
    }

    public Color getLineColor() {
        return (Color) get(LINE_COLOR);
    }

    public void setLineWidth(float pixels) {
        put (LINE_WIDTH, new Float(pixels));
    }

    public float getLineWidth() {
        Float w = (Float) get(LINE_WIDTH);
        if (w != null) {
            return w.floatValue();
        }

        return DEFAULT_LINE_WIDTH;
    }

    public void setBackgroundColor(Color c) {
        put (BACKGROUND_COLOR, c);
    }

    public Color getBackgroundColor() {
        return (Color) get(BACKGROUND_COLOR);
    }

    public void setBeginFill(boolean fill) {
        put (BEGIN_FILL, new Boolean(fill));
    }

    public boolean isBeginFill() {
        Boolean b = (Boolean) get(BEGIN_FILL);
        if (b != null)
            return b.booleanValue();

        return DEFAULT_BEGIN_FILL;
    }

    public boolean isEditable() {

        Boolean b = (Boolean) get(EDITABLE);
        if (b != null)
            return b.booleanValue();

        return DEFAULT_EDITABLE;
    }

    public void setEditable(boolean editable) {
        put (EDITABLE, new Boolean(editable));
    }

    public void setForegroundColor(Color c) {
        put (FOREGROUND_COLOR, c);
    }

    public Color getForegoundColor() {
        return (Color) get(FOREGROUND_COLOR);
    }

    public String getLineBegin() {
        return (String) get(LINE_BEGIN);
    }

    public String getLineEnd() {
        return (String) get(LINE_END);
    }

    public String getRouting() {
        return (String) get(LINE_ROUTING);
    }

    public Boolean getBeginFill() {
        return (Boolean) get(BEGIN_FILL);
    }

    public Boolean getEndFill() {
        return (Boolean) get(END_FILL);
    }

    public Boolean getShowEdgeLabel() {
        return (Boolean) get(SHOW_EDGE_LABEL);
    }
}
