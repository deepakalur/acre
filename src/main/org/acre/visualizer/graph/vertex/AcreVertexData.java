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
package org.acre.visualizer.graph.vertex;

import org.acre.pdm.PDMType;
import org.acre.pdm.RoleType;
import org.acre.visualizer.graph.AcreAttributes;
import org.acre.visualizer.graph.AcreData;
import org.acre.visualizer.graph.AcreGraphConstants;

import javax.swing.Icon;
import javax.swing.border.Border;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * @author Deepak.Alur@Sun.com
 * @version Dec 14, 2004 12:14:49 PM
 */
public class AcreVertexData implements AcreData {

    public final static Rectangle defaultBounds = new Rectangle(
            AcreGraphConstants.DEFAULT_VERTEX_LOCATION_X,
            AcreGraphConstants.DEFAULT_VERTEX_LOCATION_Y,
            AcreGraphConstants.DEFAULT_VERTEX_WIDTH,
            AcreGraphConstants.DEFAULT_VERTEX_HEIGHT);

    String vertexName;
    Object data;
    int cellType;
    private String cellImageName;
    private Icon cellIcon;
    private boolean dirty=false;
    AcreAttributes acreAttributes=new AcreAttributes();


    public AcreVertexData(String cellName, Object data) {
        this.vertexName = cellName;
        this.data = data;
        this.acreAttributes = new AcreAttributes();
    }

    public String getVertexName() {
        return vertexName;
    }

    public void setVertexName(String vertexName) {
        this.vertexName = vertexName;
        setDirty(true);
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
        setDirty(true);
    }

    public void setDirty(boolean flag) {
        dirty = flag;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setCellType(int type) {
        cellType = type;
        setDirty(true);
    }

    public int getCellType() {
        return cellType;
    }

    public String getName() {
        return getVertexName();
    }

    public void setName(String name) {
        setVertexName(name);
    }

    public AcreAttributes getAcreAttributes() {
        return acreAttributes;
    }

    public void setAcreAttributes(AcreAttributes attributes) {
        acreAttributes = attributes;
    }

    public void setColor(Color c) {
        acreAttributes.setColor(c);
        setDirty(true);
    }

    public Color getColor() {
        return acreAttributes.getColor();
    }

    public void setOpaque(boolean flag) {
        acreAttributes.setOpaque(flag);
        setDirty(true);
    }

    public boolean isOpaque() {
        return acreAttributes.isOpaque();
    }

    public void setBorder(Border border) {
        acreAttributes.setBorder(border);
        setDirty(true);
    }

    public Border getBorder() {
        return acreAttributes.getBorder();
    }

    public void setAutoSize(boolean flag) {
        acreAttributes.setAutoSize(flag);
        setDirty(true);
    }

    public boolean isAutoSize() {
        return acreAttributes.isAutoSize();
    }

    public Rectangle2D getBounds() {
        return acreAttributes.getBounds();
    }

    public void setBounds(Rectangle2D bounds) {
        int adjustedHeight = getCellHeight();
        int adjustedWidth = getCellWidth();

        if (adjustedHeight < bounds.getBounds().height)
            adjustedHeight = bounds.getBounds().height;

        if (adjustedWidth  < bounds.getBounds().width)
            adjustedWidth  = bounds.getBounds().width;

        Rectangle b = new Rectangle(adjustedWidth, adjustedHeight);

        acreAttributes.setBounds(b);
        setDirty(true);
    }

    public void setBounds(int x, int y, int width, int height) {
        acreAttributes.setBounds(x, y, width, height);
        setDirty(true);
    }

    public String toString() {
        return vertexName;
//        return "AcreCellData{" +
//                "cellName='" + cellName + "'" +
//                ", data=" + data +
//                ", cellAttributes=" + cellAttributes +
//                ", cellType=" + cellType +
//                "}";
    }

    public String getCellImageName() {
        return cellImageName;
    }

    public void setCellImageName(String cellImageName) {
        this.cellImageName = cellImageName;
        setDirty(true);
    }

    public void setCellIcon(Icon icon) {
        this.cellIcon = icon;
        setDirty(true);
    }

    public Icon getCellIcon() {
        return cellIcon;
    }

    public Object[] getRowLabels() {

        Object data = getData();

        ArrayList cols = new ArrayList();

        if (data instanceof PDMType) {
            PDMType pdm = (PDMType) data;
            cols.add("Type:" + pdm.getType());
            cols.add("Category:" + pdm.getCategory());
            cols.add("Model:" + pdm.getFactModelType());
            if (pdm.getTier() != null)
                cols.add("Tier:" + pdm.getTier());
            if (pdm.getScriptedPDMPath() != null)
                cols.add("ScriptPDM:"  + pdm.getScriptedPDMPath());
            if (pdm.getAttributes() != null)
                cols.add("Attribs:" + pdm.getAttributes());

//            cols.add("Description:" + pdm.getDescription());
//            cols.add("# Roles:" + pdm.getRoles().getRole().size());
//            cols.add("# Relationships:" + pdm.getRelationships().getRelationship().size());
        } else if (data instanceof RoleType) {
            RoleType role = (RoleType) data;
            cols.add("Type:" + role.getType());
            cols.add("Seq#:" + role.getSequence());
            if (role.getQueryName() != null)
                cols.add("Query:" + role.getQueryName());
            if (role.getTypeReferenceName() != null)
                cols.add("Refer:" + role.getTypeReferenceName());
            if ((role.getArgument() != null) && (role.getArgument().size() != 0))
                cols.add("NumArgs:" + role.getArgument().size());
            if (role.getRepository() != null)
                cols.add("Annote:" + role.getRepository());
        }

        return cols.toArray();

    }

    public int getCellHeight() {
        // 3 = header rows
        return (3 + getRowLabels().length) * AcreGraphConstants.DEFAULT_ROW_HEIGHT;
    }

    public int getCellWidth() {
        int width = 0;

        Object [] rowLabels = getRowLabels();

        int maxChars = 0;

        for (int i=0; i < rowLabels.length; i++) {
            String row = (String) rowLabels[i];
            if (maxChars < row.length())
                maxChars = row.length();
        }

        int titleWidth = vertexName.length() * AcreGraphConstants.DEFAULT_BOLD_CHAR_WIDTH;
        width = maxChars * AcreGraphConstants.DEFAULT_CHAR_WIDTH;

        width = (width < titleWidth)? titleWidth:width;
        width = (width < AcreGraphConstants.DEFAULT_VERTEX_WIDTH)?AcreGraphConstants.DEFAULT_VERTEX_WIDTH:width;

        return width;
    }
}
