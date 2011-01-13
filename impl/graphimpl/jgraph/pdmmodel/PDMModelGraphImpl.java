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

import org.acre.visualizer.graphimpl.jgraph.AcreGraphImpl;
import org.acre.pdm.PDMType;
import org.acre.pdm.RelationshipType;
import org.acre.pdm.RoleType;
import org.acre.visualizer.graph.graph.pdmmodel.PDMModelGraph;
import org.acre.visualizer.graph.graph.pdmmodel.edges.RoleToRoleEdge;
import org.acre.visualizer.graph.graph.pdmmodel.vertex.PDMVertex;
import org.acre.visualizer.graph.graph.pdmmodel.vertex.RoleVertex;
import org.acre.visualizer.ui.components.Editor;
import org.jgraph.graph.BasicMarqueeHandler;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;

import java.util.ArrayList;

/**
 * @author Deepak.Alur@Sun.com
 * @version Dec 14, 2004 12:01:14 PM
 */
public class PDMModelGraphImpl extends AcreGraphImpl implements PDMModelGraph {
    private Editor pdmEditor;
    private Editor relationshipEditor;
    private Editor roleEditor;

    public PDMModelGraphImpl() {
        super();
        initialize();
    }

    public PDMModelGraphImpl(GraphModel model) {
        super(model);
        initialize();
    }

    public PDMModelGraphImpl(GraphModel model, GraphLayoutCache view) {
        super(model, view);
        initialize();
    }

    public PDMModelGraphImpl(GraphModel model, BasicMarqueeHandler mh) {
        super(model, mh);
        initialize();
    }

    public PDMModelGraphImpl(GraphModel model, GraphLayoutCache layoutCache, BasicMarqueeHandler mh) {
        super(model, layoutCache, mh);
        initialize();
    }

//
//    // Create a Group that Contains the Cells
//    public void deprecatedgroup(Object[] cells) {
//        // Order Cells by View Layering
////        vertex = this.getGraphLayoutCache().order(vertex);
//
//        // If Any Cells in View
//        if (cells != null && cells.length > 0) {
//            // Create Group Cell
//            int count = getCellCount();
//            DefaultGraphCell group =
//                    new DefaultGraphCell(new Integer(count - 1));
//            // Create Change Information
//            ParentMap map = new ParentMap();
//            // Insert Child Parent Entries
//            for (int i = 0; i < cells.length; i++)
//                map.addEntry(cells[i], group);
//            // Insert into model
//            getGraphLayoutCache().insert(new Object[]{group},
//                    null,
//                    null,
//                    map,
//                    null);
//        }
//    }



    public void setPDMEditor(Editor editor) {
        pdmEditor = editor;
    }

    public Editor getPDMEditor() {
        return pdmEditor;
    }

    public void setRoleEditor(Editor editor) {
        roleEditor = editor;
    }

    public Editor getRoleEditor() {
        return roleEditor;
    }

    public void setRelationshipEditor(Editor editor) {
        relationshipEditor = editor;
    }

    public Editor getRelationshipEditor() {
        return relationshipEditor;
    }

    public RoleVertex getSelectedRole() {
        RoleVertex [] selRoles = getSelectedRoles();
        if ((selRoles != null) && (selRoles.length != 0)) {
            return selRoles[0];
        }

        return null;
    }


    public PDMVertex getSelectedPDM() {
        PDMVertex [] selPDMs = getSelectedPDMs();
        if ((selPDMs != null) && (selPDMs.length != 0)) {
            return selPDMs[0];
        }

        return null;
    }

    public RoleToRoleEdge getSelectedRoleToRoleRelationship() {
        RoleToRoleEdge [] selRels = getSelectedRoleToRoleRelationships();
        if ((selRels != null) && (selRels.length != 0)) {
            return selRels[0];
        }

        return null;
    }

    public RoleVertex[] getSelectedRoles() {
        ArrayList roles = new ArrayList();
        Object [] cells = getSelectionCells();
        for (int i=0; i < cells.length; i++) {
            if (cells[i] instanceof RoleVertex) {
                RoleVertex roleV = (RoleVertex) cells[i];
                roles.add(roleV);
            }
        }
        RoleVertex [] ret = new RoleVertex[roles.size()];
        roles.toArray(ret);
        return (ret);
    }

    public PDMVertex[] getSelectedPDMs() {
        ArrayList sel = new ArrayList();
        Object [] cells = getSelectionCells();
        for (int i=0; i < cells.length; i++) {
            if (cells[i] instanceof PDMVertex) {
                PDMVertex v = (PDMVertex) cells[i];
                sel.add(v);
            }
        }
        PDMVertex [] vertices = new PDMVertex[sel.size()];
        sel.toArray(vertices);
        return vertices;
    }

    public RoleToRoleEdge[] getSelectedRoleToRoleRelationships() {
        ArrayList sel = new ArrayList();
        Object [] cells = getSelectionCells();
        for (int i=0; i < cells.length; i++) {
            if (cells[i] instanceof RoleToRoleEdge) {
                RoleToRoleEdge v = (RoleToRoleEdge) cells[i];
                sel.add(v);
            }
        }
        RoleToRoleEdge[] edges = new RoleToRoleEdge[sel.size()];
        sel.toArray(edges);
        return (edges);
    }
}
