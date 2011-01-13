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

import org.acre.visualizer.graph.graph.AcreGraphConstants;
import org.acre.visualizer.graph.graph.vertex.AcreVertexData;
import org.acre.visualizer.ui.AcreUIUtils;
import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.GraphConstants;

import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

/**
 * @author Deepak.Alur@Sun.com
 * @version Dec 14, 2004 3:39:26 PM
 */
public class SalsaVertexRenderer extends JPanel implements CellViewRenderer, Serializable {

    private String cellName;
    private String cellValue;
    private Icon cellIcon;
    private Border cellBorder;
    private Rectangle2D cellBounds;
    private Object[] columns;

    /**
     * The default foreground color = Color.black
     */
    public static final Color DEFAULT_FOREGROUND = Color.black;

    public static final Color DEFAULT_BACKGROUND = Color.yellow;

    /**
     * The filename for the key icon = "resources/key.gif"
     */
    public static final String KEY_ICON_FILENAME = "resources/key.gif";


    /**
     * Vertical padding around title
     */
    public static final int TITLE_PADDING = 3;

    /**
     * Padding around column names
     */
    public static final int COLUMN_PADDING = 1;

    /**
     * Left margin for column icon
     */
    public static final int COLUMN_ICON_LEFT = 2;

    /**
     * Left margin for column icon
     */
    public static final int COLUMN_ICON_RIGHT = 2;

    /**
     * Left margin for column name text (cell icon width + margin)
     */
    public static final int COLUMN_LEFT = 12 + 2 + COLUMN_ICON_LEFT;

    /**
     * The padding between text rows in pixels
     */
    public static final int ROW_SPACING = 3;

    /**
     * The padding around the more-content indicator arrow
     */
    public static final int MORE_ARROW_PADDING = 4;

    /**
     * The width of the more-content indicator arrow
     */
    public static final int MORE_ARROW_WIDTH = 8;

    /**
     * The height of the more-content indicator arrow
     */
    public static final int MORE_ARROW_HEIGHT = 8;

    /**
     * Cache the current graph for drawing.
     */
    transient protected JGraph graph;

    /**
     * Cached hasFocus and selected value.
     */
    transient protected boolean hasFocus, selected, preview, opaque;

    /**
     * Cached default foreground and default background.
     */
    transient protected Color defaultForeground, defaultBackground, bordercolor;

    /**
     * Cached borderwidth.
     */
    transient protected int borderWidth;
    private Color cellColor;
    private boolean cellAutoSize;
    private String cellImageName;

    private JList colList = new JList();
    private static final float ROW_TEXT_HEIGHT = 10.0f;

    public SalsaVertexRenderer() {
        super();
    }

//    public Dimension getPreferredSize() {
//
//    }

    public Component getRendererComponent(JGraph graph, CellView view, boolean sel, boolean focus, boolean preview) {
        this.graph = graph;

        if (view instanceof SalsaJGraphCellView) {
            AcreVertexImpl vertex = (AcreVertexImpl) view.getCell();
            Object userObject = vertex.getUserObject();

            if (userObject instanceof AcreVertexData) {
                AcreVertexData data = (AcreVertexData) userObject;
                cellName = data.getVertexName();

                if (data.getData() != null)
                    cellValue = data.getData().toString();
                else
                    cellValue = "";

                cellIcon = data.getCellIcon();
                //cellIcon = AcreGraphIconUtils.getCellIcon(data.getCellType());
                cellBorder = data.getBorder();
                cellBounds = data.getBounds();
                cellColor = data.getColor();
                cellAutoSize = data.isAutoSize();
                cellImageName = data.getCellImageName();
                columns = new Object[0];
                //columns[0] = data.getData();
                populateColumns(data);

                if (cellAutoSize) {
                    int h = calculateHeight();
                    int w = calculateWidth();
                    setPreferredSize(new Dimension(w, h));
                }
            }
        }

        setBorder(cellBorder);
        setForeground(cellColor);
        setBackground(cellColor);
        //setSize(100, 135);

        /*
        if (!cellAutoSize) {

            System.out.println("Setting Autosize: W = " + cellBounds.getBounds().width + " H = " + cellBounds.getBounds().height);
            setSize(cellBounds.getBounds().width, cellBounds.getBounds().height);
        }
        else {
            if (cellName != null)
                  setSize(cellName.length() * 20, CellTypeConstants.DEFAULT_CELL_HEIGHT);
            else
                  setSize(cellBounds.getBounds().width, cellBounds.getBounds().height);

            System.out.println("Set Size: " + getSize());
        }
        */

        return this;
    }

    private int calculateHeight() {
        if (cellImageName != null)
            return AcreGraphConstants.DEFAULT_VERTEX_HEIGHT;

//        if ((columns != null) & (columns.length > 4)) {
//            return columns.length * 10 + 40;
//        }

        if ((columns != null) && (columns.length > 0)) {
            return columns.length * AcreGraphConstants.DEFAULT_ROW_HEIGHT;
        }

        return AcreGraphConstants.DEFAULT_VERTEX_HEIGHT;
    }

    private int calculateWidth() {
        if (cellName == null)
            return AcreGraphConstants.DEFAULT_VERTEX_HEIGHT;

        int width = cellName.length() * AcreGraphConstants.DEFAULT_CHAR_WIDTH;

        if (width < AcreGraphConstants.DEFAULT_VERTEX_WIDTH)
            return AcreGraphConstants.DEFAULT_VERTEX_WIDTH;

        return width;
    }

    private void populateColumns(Object data) {

        if (data instanceof AcreVertexData) {
            AcreVertexData sData = (AcreVertexData) data;
            columns = sData.getRowLabels();
        }
    }

    public void paint(Graphics g) {

        if (cellImageName == null) super.paint(g);

        Graphics2D g2 = (Graphics2D) g;
        Dimension dim = getSize();
        int width = dim.width;
        int height = dim.height;
        boolean imageonly = false;

        FontMetrics fm = g.getFontMetrics();
        Font normalFont = g.getFont();
        Font smallFont = normalFont.deriveFont(ROW_TEXT_HEIGHT);

        Font titleFont = normalFont.deriveFont(Font.BOLD);
        int colTextHeight =
                fm.getMaxAscent() - fm.getMaxDescent() + ROW_SPACING;
        int yCursor = colTextHeight + TITLE_PADDING;
        int keyIconYOffset=12;
        int iconHeight = 12;
        if (cellIcon != null) {
            iconHeight = cellIcon.getIconHeight();
        }

        keyIconYOffset = -(iconHeight
                            + fm.getMaxAscent()
                            - fm.getMaxDescent()) / 2;

        String title = cellName;
        int xCursor = 0;
        int titleWidth = fm.stringWidth(cellName);

        // = (width - fm.stringWidth( cellName )) / 2;

        Stroke lineStroke = new BasicStroke((float) 1.0); //borderWidth );
        Stroke textStroke = new BasicStroke(1.0f);

        Dimension d = getSize();

        g2.setStroke(textStroke);
        g.setColor(Color.black);
        g.setFont(titleFont);

        Image img = null;

        if (cellImageName != null)
            img = AcreUIUtils.createImage(cellImageName);

        if ((cellImageName != null) && (img != null)) {

            setBorder(null);
            setBackground(Color.white);
            setSize((int) cellBounds.getWidth(), (int) cellBounds.getHeight() + 50);

            g.drawImage(img, 0, 0, (int) cellBounds.getWidth(),
                    (int) cellBounds.getHeight(), Color.white, null);

            yCursor += cellBounds.getHeight() + 2;
            g.setClip(0, 0, titleWidth,
                    (int) cellBounds.getHeight() + 30);

            imageonly = true;

        } else if (cellIcon != null) {
            cellIcon.paintIcon(this, g, COLUMN_ICON_LEFT,
                    yCursor + keyIconYOffset);
            xCursor += COLUMN_ICON_LEFT + cellIcon.getIconWidth() +
                    COLUMN_PADDING;
        }

        // draw the Cell name
        g.drawString(title, xCursor + 2, yCursor);


        if (!imageonly) {

            g2.setStroke(lineStroke);
            yCursor += TITLE_PADDING + 1; //borderWidth;
            g.drawLine(0, yCursor, width, yCursor);

            // Draw the columns
            //g.setColor( getForeground());
            g.setColor(DEFAULT_FOREGROUND);
            g2.setStroke(textStroke);
            g.setFont(smallFont);//normalFont );

            for (int i = 0; i < columns.length; i++) {

                StringBuffer colSB = new StringBuffer(40);
                colSB.append(columns[i]);

                /*if ((cellValue == null) || (cellValue.equals(AcreCellData.NODATA)))
                    continue;*/

                yCursor += ROW_SPACING + ROW_TEXT_HEIGHT;

                //g.drawString( cellValue/*colSB.toString()*/, COLUMN_LEFT, yCursor );
                // todo - deepak...no icon for rows now, so draw from as far left as possible
                // g.drawString( (String) columns[i], COLUMN_LEFT, yCursor );
                g.drawString((String) columns[i], COLUMN_LEFT, yCursor);

                // If we're below the the bottom of the box
                if (yCursor > height) {
                    // Draw the more-content arrow
                    int right = width - MORE_ARROW_PADDING;
                    int left = right - MORE_ARROW_WIDTH;
                    int center = right - MORE_ARROW_WIDTH / 2;
                    int bottom = height - MORE_ARROW_PADDING;
                    int top = bottom - MORE_ARROW_HEIGHT;
                    int[] xPts = {center, left, right};
                    int[] yPts = {bottom, top, top};

                    //g.setColor(cellColor);
                    g.fillPolygon(xPts, yPts, 3);
                    //g.setColor(DEFAULT_FOREGROUND);

                    break;
                }
            }
        }

        if (selected || hasFocus) {

            // Draw selection/highlight border
            ((Graphics2D) g).setStroke(GraphConstants.SELECTION_STROKE);
            if (hasFocus)
                g.setColor(graph.getGridColor());
            else if (selected)
                g.setColor(graph.getHighlightColor());

            d = getSize();

            g.setColor(cellColor);
            g.drawRect(0, 0, d.width - 1, d.height - 1);
            g.setColor(DEFAULT_FOREGROUND);
        }
    }
}