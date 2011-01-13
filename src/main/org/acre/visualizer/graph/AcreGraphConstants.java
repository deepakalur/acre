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

import org.acre.config.ConfigService;

import java.awt.Color;

/**
 * @author Deepak.Alur@Sun.com
 * @version Dec 14, 2004 12:01:47 PM
 */
public class AcreGraphConstants {
    public static final int PACKAGE=0;
    public static final int CLASS=1;
    public static final int INTERFACE=2;
    public static final int METHOD=3;
    public static final int PARAMETER=4;
    public static final int ABSTRACT_CLASS=5;
    public static final int ABSTRACT_METHOD=6;
    public static final int FIELD=7;
    public static final int ANNOTATION=8;
    public static final int PDM=9;
    public static final int PDMROLE=10;
    public static final int PDMRELATIONSHIP=11;
    public static final int ARTIFACT = 12;
    public static final int PDMRESULT = 13;
    public static final int ROLERESULT = 14;

    public static final String ORANGE_PORT_ICON="OrangePortIcon.gif";
    public static final String RED_PORT_ICON="RedBall.gif";

    public static Color BLUE =new Color(0, 0xCC, 0xFF); // blue
    public static Color LIGHT_ORANGE = new Color(0xFF, 0xCC, 0); // light orange
    public static Color  LIGHT_BLUE = new Color (0x99, 0xCC, 0xFF); //  - lightblue
    public static Color  LIGHT_YELLOW_ORANGE = new Color(0xFF, 0xCC, 0x66); // light yellow orange
    public static Color PURPLE_BLUE = new Color(0x63, 0x30, 0xff); // sun purple
    public static Color LIGHT_BRIGHT_YELLOW = new Color(0xFF, 0xFF, 0x66); // light brighter yellow
    public static Color LIGHTER_ORANGE = new Color (0xff, 0x99, 0x33); // lighter orange
    public static Color LIGHT_YELLOW = new Color(0xFF, 0xFF, 0x99); // light yellow
    public static Color LIGHT_GREEN = new Color(0x00, 0x66, 0x00); // light green

    Color CLASS_COLOR = BLUE;
    Color METHOD_COLOR = LIGHT_ORANGE;
    Color INTERFACE_COLOR = LIGHT_BLUE;
    Color PACKAGE_COLOR = LIGHT_YELLOW_ORANGE;
    static Color PDM_COLOR = LIGHT_YELLOW_ORANGE;
    static Color PDMROLE_COLOR = LIGHT_YELLOW;
    Color PDMRELATIONSHIP_COLOR = LIGHTER_ORANGE;
    Color DEFAULT_COLOR = LIGHT_YELLOW;
    static Color ARTIFACT_COLOR = LIGHT_GREEN;

    public static final int DEFAULT_VERTEX_WIDTH=125;
    public static final int DEFAULT_VERTEX_HEIGHT=50;
    public static final int DEFAULT_VERTEX_SPACE_X=50;
    public static final int DEFAULT_VERTEX_SPACE_Y=50;

    public static final int IMAGE_VERTEX_WIDTH=  100;
    public static final int IMAGE_VERTEX_HEIGHT = 100;

    public static final int DEFAULT_VERTEX_LOCATION_X = 50;
    public static final int DEFAULT_VERTEX_LOCATION_Y = 50;


    public static final int DEFAULT_ROW_HEIGHT = 12;
    public static final int DEFAULT_CHAR_WIDTH = 6;
    public static final int DEFAULT_BOLD_CHAR_WIDTH = 8;

    // used in JUNG
    public static int VERTEX_HEIGHT = 20;


    public static String VERTEX_NAME = "VERTEX_NAME";
    public static String ACRE_DATA = "ACRE_DATA";


    public static final String EDGE_NAME = "EDGE_NAME";
    public static final int EDGE_CALLS=1;
    public static final int EDGE_DEPENDS=2;
    public static final int EDGE_CREATES=3;
    public static final int EDGE_THROWS=4;
    public static final int EDGE_CATCHES=5;
    public static final int EDGE_IMPLEMENTS=6;
    public static final int EDGE_EXTENDS=7;
    public static final int EDGE_USES=8;
    public static final int EDGE_CONTAINS=9;
    public static final int EDGE_INSTANCEOF=10;
    public static final int EDGE_MAP = 11;
    public static final int EDGE_REFERENCES = 12;
    public static final String EDGE_CALLS_LABEL = "Calls";
    public static final String EDGE_DEPENDS_LABEL = "Depends";
    public static final String EDGE_CREATES_LABEL = "Creates";
    public static final String EDGE_THROWS_LABEL = "Throws";
    public static final String EDGE_CATCHES_LABEL = "Catches";
    public static final String EDGE_IMPLEMENTS_LABEL = "Implements";
    public static final String EDGE_EXTENDS_LABEL = "Extends";
    public static final String EDGE_USES_LABEL = "Uses";
    public static final String EDGE_CONTAINS_LABEL = "Contains";
    public static final String EDGE_INSTANCEOF_LABEL="Instanceof";
    public static final String EDGE_MAP_LABEL="Has";
    public static final String EDGE_REFERENCES_LABEL = "References";

    public static final int DEFAULT_ARTIFACT_VERTEX_WIDTH = DEFAULT_VERTEX_WIDTH;
    public static final int DEFAULT_ARTIFACT_VERTEX_HEIGHT = DEFAULT_ROW_HEIGHT + 6;
    public static final int DEFAULT_PDMRESULT_VERTEX_WIDTH = DEFAULT_VERTEX_WIDTH;
    public static final int DEFAULT_PDMRESULT_VERTEX_HEIGHT = DEFAULT_ROW_HEIGHT + 6;

    public static final double DEFAULT_CIRCLE_LAYOUT_RADIUS = 200;
    public static final int DEFAULT_REPULSION_RANGE = DEFAULT_VERTEX_SPACE_X ;
    public static final String EDGE_KEY = "AcreEdge";

    private AcreGraphConstants() {

    }

    public static Color getPDMColor() {
        Color c = ConfigService.getInstance().getConfigData().getPdmColor();
        if (c != null)
            return c;
        else
            return PDM_COLOR;
    }

    public static Color getRoleColor() {
        Color c = ConfigService.getInstance().getConfigData().getRoleColor();
        if (c != null)
            return c;
        else
            return PDMROLE_COLOR;
    }

    public static Color getArtifactColor() {
        Color c = ConfigService.getInstance().getConfigData().getArtifactColor();
        if (c != null)
            return c;
        else
            return ARTIFACT_COLOR;        
    }
}
