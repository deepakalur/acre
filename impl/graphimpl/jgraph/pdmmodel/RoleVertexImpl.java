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
import org.acre.pdm.RoleType;
import org.acre.visualizer.graph.graph.AcreGraphConstants;
import org.acre.visualizer.graph.graph.AcreGraphIconUtils;
import org.acre.visualizer.graph.graph.pdmmodel.vertex.RoleVertex;
import org.acre.visualizer.graph.graph.vertex.AcreVertexData;

import javax.swing.BorderFactory;
import java.awt.Rectangle;

/**
 * @author Deepak.Alur@Sun.com
 * @version Dec 14, 2004 12:39:22 PM
 */
public class RoleVertexImpl
        extends AcreVertexImpl
        implements RoleVertex {
    public static final int DEFAULT_ROLE_CELL_WIDTH = AcreGraphConstants.DEFAULT_VERTEX_WIDTH;
    public static final int DEFAULT_ROLE_CELL_HEIGHT = AcreGraphConstants.DEFAULT_VERTEX_HEIGHT;

    RoleType role;
    PDMType pdm;

    public RoleVertexImpl(PDMType pdm, RoleType role) {
        super(null);
        AcreVertexData data = new AcreVertexData(role.getName(), role);
        setSalsaData(data);
        this.role = role;
        this.pdm = pdm;
    }

    // this method to be implemented by all inheriters
    protected void assignAttributes(AcreVertexData d) {
        d.setCellType(AcreGraphConstants.PDMROLE);
        d.setColor(AcreGraphConstants.getRoleColor());
        d.setOpaque(true);
        d.setBounds(new Rectangle(DEFAULT_ROLE_CELL_WIDTH, DEFAULT_ROLE_CELL_HEIGHT));
        d.setBorder(BorderFactory.createRaisedBevelBorder());
        d.setAutoSize(false);
        d.setCellIcon(AcreGraphIconUtils.getCellIcon(d.getCellType()));
    }

    public String getVertexName() {
        return role.getName();
    }

    public String getToolTipText() {
        StringBuffer tooltip = new StringBuffer();
        tooltip.append("<html>").
                append(role.getName()).
                append("<br>This role is a '").
                append(role.getType()).
                    append("'</html>");

            return tooltip.toString();
    }

    public PDMType getPDM() {
        return pdm;
    }

    public RoleType getRole() {
        return role;
    }
}
