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
package org.acre.visualizer.graph.popup;

import org.acre.visualizer.graph.AcreGraph;
import org.acre.visualizer.graph.pdmmodel.edges.PDMToRoleEdge;
import org.acre.visualizer.graph.pdmmodel.edges.RoleToPDMEdge;
import org.acre.visualizer.graph.pdmmodel.edges.RoleToRoleEdge;
import org.acre.visualizer.graph.pdmmodel.vertex.PDMVertex;
import org.acre.visualizer.graph.pdmmodel.vertex.RoleVertex;
import org.acre.visualizer.graph.pdmresult.edges.ArtifactEdge;
import org.acre.visualizer.graph.pdmresult.edges.PDMResultToRoleResultEdge;
import org.acre.visualizer.graph.pdmresult.edges.RoleResultToRoleResultEdge;
import org.acre.visualizer.graph.pdmresult.vertex.ArtifactVertex;
import org.acre.visualizer.graph.pdmresult.vertex.PDMResultVertex;
import org.acre.visualizer.graph.pdmresult.vertex.RoleResultVertex;

import javax.swing.JPopupMenu;

/**
 * @author Deepak.Alur@Sun.com
 * @version Dec 14, 2004 12:02:41 PM
 */
public class GraphPopupFactory {

    public static JPopupMenu createPopup(AcreGraph graph, Object clickedOnItem) {

        if (clickedOnItem instanceof RoleVertex) {
            return createRoleCellPopup(graph, (RoleVertex)clickedOnItem);
        } else if (clickedOnItem instanceof PDMVertex) {
            return createPDMCellPopup(graph, (PDMVertex)clickedOnItem);
        } else if (clickedOnItem instanceof RoleToPDMEdge) {
            return createRoleToPDMEdgePopup(graph, (RoleToPDMEdge)clickedOnItem);
        } else if (clickedOnItem instanceof RoleToRoleEdge) {
            return createRoleToRoleEdgePopup(graph, (RoleToRoleEdge)clickedOnItem);
        } else if (clickedOnItem instanceof PDMToRoleEdge) {
            return createPDMToRoleEdgePopup(graph, (PDMToRoleEdge)clickedOnItem);
        }  else if (clickedOnItem instanceof ArtifactEdge) {
            return createArtifactEdgePopup(graph, (ArtifactEdge) clickedOnItem);
        } else if (clickedOnItem instanceof PDMResultToRoleResultEdge) {
            return createPDMResultToRoleResultEdgePopup(graph, (PDMResultToRoleResultEdge)clickedOnItem);
        } else if (clickedOnItem instanceof RoleResultToRoleResultEdge) {
            return createRoleResultToRoleResultEdgePopup(graph, (RoleResultToRoleResultEdge)clickedOnItem);
        } else if (clickedOnItem instanceof ArtifactVertex) {
            return createArtifactVertexPopup(graph, (ArtifactVertex)clickedOnItem);
        } else if (clickedOnItem instanceof PDMResultVertex) {
            return createPDMResultVertexPopup(graph, (PDMResultVertex)clickedOnItem);
        } else if (clickedOnItem instanceof RoleResultVertex) {
            return createRoleResultVertexPopup(graph, (RoleResultVertex)clickedOnItem);
        }
        else {
            return createGraphPopup(graph);
        }
    }

    private static JPopupMenu createRoleResultVertexPopup(AcreGraph graph, RoleResultVertex roleResultVertex) {
        JPopupMenu menu = new RoleResultVertexPopup(graph, roleResultVertex);
        return menu;
    }

    private static JPopupMenu createPDMResultVertexPopup(AcreGraph graph, PDMResultVertex pdmResultVertex) {
        JPopupMenu menu = new PDMResultVertexPopup(graph, pdmResultVertex);
        return menu;
    }

    private static JPopupMenu createArtifactVertexPopup(AcreGraph graph, ArtifactVertex artifactVertex) {
        JPopupMenu menu = new ArtifactVertexPopup(graph, artifactVertex);
        return menu;

    }

    private static JPopupMenu createRoleResultToRoleResultEdgePopup(AcreGraph graph,
                                                                    RoleResultToRoleResultEdge roleResultToRoleResultEdge) {
        JPopupMenu menu = new RoleResultToRoleResultEdgePopup(graph, roleResultToRoleResultEdge);
        return menu;
    }

    private static JPopupMenu createPDMResultToRoleResultEdgePopup(AcreGraph graph,
                                                                   PDMResultToRoleResultEdge pdmResultToRoleResultEdge) {
        JPopupMenu menu = new PDMResultToRoleResultEdgePopup(graph, pdmResultToRoleResultEdge);
        return menu;
    }

    private static JPopupMenu createArtifactEdgePopup(AcreGraph graph, ArtifactEdge artifactEdge) {
        JPopupMenu menu = new ArtifactEdgePopup(graph, artifactEdge);
        return menu;
    }

    private static JPopupMenu createPDMToRoleEdgePopup(AcreGraph graph, PDMToRoleEdge pdmToRoleEdge) {
        JPopupMenu menu = new PDMToRoleEdgePopup(graph, pdmToRoleEdge);
        return menu;

    }

    private static JPopupMenu createRoleToRoleEdgePopup(AcreGraph graph, RoleToRoleEdge roleToRoleEdge) {
        JPopupMenu menu = new RoleToRoleEdgePopup(graph, roleToRoleEdge);
        return menu;
    }

    private static JPopupMenu createGraphPopup(AcreGraph graph) {
        JPopupMenu graphPopupMenu = new GraphPopupImpl(graph);
        return graphPopupMenu;
    }

    private static JPopupMenu createRoleToPDMEdgePopup(AcreGraph graph, RoleToPDMEdge salsaEdge) {
        RoleToPDMEdgePopup edgePopup = new RoleToPDMEdgePopup(graph, salsaEdge);
        return edgePopup;
    }

    private static JPopupMenu createPDMCellPopup(AcreGraph graph, PDMVertex pdmVertex) {
        PDMVertexPopup pdmVertexPopup = new PDMVertexPopup(graph, pdmVertex);
        return pdmVertexPopup;
    }

    private static JPopupMenu createRoleCellPopup(AcreGraph graph, RoleVertex cell) {
        RoleVertexPopup roleVertexPopup = new RoleVertexPopup(graph, cell);
        return roleVertexPopup;
    }
}