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

import org.acre.pdm.RelationshipType;
import org.acre.dao.PDMXMLConstants;
import org.acre.visualizer.graph.AcreGraphConstants;

/**
 * @author Deepak.Alur@Sun.com
 * @version Dec 15, 2004 4:07:30 PM
 */
public class AcreEdgeUtils {

    public static void setEdgeAttributes(AcreEdgeData data) {
        // get relationship object
        Object relData = data.getRelationship();
        if (relData instanceof RelationshipType) {
            RelationshipType rel = (RelationshipType) relData;
            String relType = rel.getType();
            int connectionType = getConnectionType(relType);
            setEdgeAttributesForConnection(data, connectionType);
        } else if (relData instanceof String) {
            if (AcreEdgeData.DEFAULT_RELATIONSHIP_DATA.equals(relData)) {
                data.useDottedLine();
                data.setLineEndArrowNone();
                //data.useOrthogonalLineStyle();
            }
        }
    }

    private static void setEdgeAttributesForConnection(AcreEdgeData data, int connectionType) {

        //data.useOrthogonalLineStyle();

        switch (connectionType) {
                    case AcreGraphConstants.EDGE_CALLS:
                        data.setLineEndArrowClassic();
                        break;
                    case AcreGraphConstants.EDGE_CATCHES:
                        data.setLineEndArrowCircle();
                        break;
                    case AcreGraphConstants.EDGE_CONTAINS:
                        data.setLineEndArrowDiamond();
                        break;
                    case AcreGraphConstants.EDGE_CREATES:
                        data.setLineEndArrowClassic();
                        data.setEndFill(false);
                        data.useDottedLine();
                        break;
                    case AcreGraphConstants.EDGE_DEPENDS:
                        data.setEndFill(false);
                        data.useDottedLine();
                        break;
                    case AcreGraphConstants.EDGE_EXTENDS:
                        data.setLineEndArrowTechnical();
                        break;
                    case AcreGraphConstants.EDGE_IMPLEMENTS:
                        data.setLineEndArrowTechnical();
                        data.setEndFill(false);
                        break;
                    case AcreGraphConstants.EDGE_THROWS:
                        data.setLineEndArrowCircle();
                        data.setEndFill(false);
                        break;
                    case AcreGraphConstants.EDGE_USES:
                        data.setLineEndArrowNone();
                        data.useDottedLine();
                        break;
                    case AcreGraphConstants.EDGE_INSTANCEOF:
                        data.setLineEndArrowTechnical();
                        data.useDottedLine();
                        break;
                    case AcreGraphConstants.EDGE_MAP:
                        data.setLineEndArrowNone();
                        data.useDottedLine();
                        break;
                   case AcreGraphConstants.EDGE_REFERENCES:
                        data.setLineEndArrowSimple();
                        data.useDottedLine();
                        break;
                   default:
                        break;
                }
    }

    private static int getConnectionType (String relType) {

        int connectionType=AcreGraphConstants.EDGE_DEPENDS;

        if (relType.equals(PDMXMLConstants.RELATIONSHIP_TYPE_CALLS)) {
            connectionType=AcreGraphConstants.EDGE_CALLS;
            //cinfo.connectionTypeName=ConnectionTypeConstants.CALLS_LABEL;
        }
        else
        if (relType.equals(PDMXMLConstants.RELATIONSHIP_TYPE_CATCHES)) {
            connectionType=AcreGraphConstants.EDGE_CATCHES;
        }
        else
        if (relType.equals(PDMXMLConstants.RELATIONSHIP_TYPE_CONTAINS)) {
            connectionType=AcreGraphConstants.EDGE_CONTAINS;
        }
        else
        if (relType.equals(PDMXMLConstants.RELATIONSHIP_TYPE_CREATES)) {
            connectionType=AcreGraphConstants.EDGE_CREATES;
        }
        else
        if (relType.equals(PDMXMLConstants.RELATIONSHIP_TYPE_EXTENDS)) {
            connectionType=AcreGraphConstants.EDGE_EXTENDS;
        }
        else
        if (relType.equals(PDMXMLConstants.RELATIONSHIP_TYPE_IMPLEMENTS)) {
            connectionType=AcreGraphConstants.EDGE_IMPLEMENTS;
        }
        else
        if (relType.equals(PDMXMLConstants.RELATIONSHIP_TYPE_ISOFTYPE)) {
            connectionType=AcreGraphConstants.EDGE_INSTANCEOF;
            // connectionType=ConnectionTypeConstants.CREATES;
            // todo - what is the correct way to display IsOfType

        }
        else
        if (relType.equals(PDMXMLConstants.RELATIONSHIP_TYPE_THROWS)) {
            connectionType=AcreGraphConstants.EDGE_THROWS;
        }
        else
        if (relType.equals(PDMXMLConstants.RELATIONSHIP_TYPE_USES)) {
            connectionType=AcreGraphConstants.EDGE_USES;
        }
        else
        if (relType.equals(PDMXMLConstants.RELATIONSHIP_TYPE_THROWS)) {
            connectionType=AcreGraphConstants.EDGE_THROWS;
        }


        return connectionType;
    }

}
