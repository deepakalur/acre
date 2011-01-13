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
package org.acre.visualizer.graphimpl.jgraph.pdmmodel;

import org.acre.visualizer.graphimpl.jgraph.AcreVertexImpl;
import org.acre.pdm.PDMType;
import org.acre.visualizer.graph.graph.AcreGraphConstants;
import org.acre.visualizer.graph.graph.AcreGraphIconUtils;
import org.acre.visualizer.graph.graph.pdmmodel.vertex.PDMVertex;
import org.acre.visualizer.graph.graph.vertex.AcreVertexData;

import javax.swing.BorderFactory;
import java.awt.Rectangle;

/**
 * @author Deepak.Alur@Sun.com
 * @version Dec 14, 2004 12:12:03 PM
 */
public class PDMVertexImpl
        extends AcreVertexImpl
        implements PDMVertex {
    public static final int DEFAULT_PDM_CELL_WIDTH = AcreGraphConstants.DEFAULT_VERTEX_WIDTH;
    public static final int DEFAULT_PDM_CELL_HEIGHT = 2 * AcreGraphConstants.DEFAULT_VERTEX_HEIGHT;

    PDMType pdm;

    public PDMVertexImpl(PDMType pdm) {
        super(null);
        AcreVertexData data = new AcreVertexData(pdm.getName(), pdm);
        setSalsaData(data);
        this.pdm = pdm;
    }

    public void assignAttributes(AcreVertexData d) {
        d.setCellType(AcreGraphConstants.PDM);
        d.setColor(AcreGraphConstants.getPDMColor());
        d.setOpaque(true);

        d.setBounds(new Rectangle(DEFAULT_PDM_CELL_WIDTH, DEFAULT_PDM_CELL_HEIGHT));
        d.setBorder(BorderFactory.createRaisedBevelBorder());
        d.setAutoSize(false);
        d.setCellIcon(AcreGraphIconUtils.getCellIcon(d.getCellType()));
    }

    public String getToolTipText() {
        Object data = getSalsaData().getData();
        if (data instanceof PDMType) {
            PDMType pdm = (PDMType) data;
            StringBuffer tooltip = new StringBuffer();
            tooltip.append("<html>").
                    append(pdm.getName()).
                    append("<br>").
                    append(pdm.getDescription()).
                    append("</html>");
            
            return tooltip.toString();
        } else {
            return super.getToolTipText();
        }
    }

    public String getVertexName() {
        return pdm.getName();
    }


    public PDMType getPDM() {
        return pdm;
    }
}
