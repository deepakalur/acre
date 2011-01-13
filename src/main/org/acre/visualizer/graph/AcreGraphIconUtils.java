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

import org.acre.visualizer.ui.AcreUIUtils;

import javax.swing.Icon;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: Jun 8, 2004
 * Time: 1:37:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class AcreGraphIconUtils {
    private static String classIcon = "special/Class.gif";
    private static final String PACKAGE_ICON = "special/Package.gif";
    private static final String CLASS_ICON = "special/Class.gif";
    private static final String INTERFACE_ICON = "special/Interface.gif";
    private static final String METHOD_ICON = "special/Method.gif";
    private static final String PARAMETER_ICON = "special/Parameter.gif";
    private static final String ABSTRACT_CLASS_ICON = "special/AbstractClass.gif";
    private static final String ABSTRACT_METHOD_ICON = "special/AbstractMethod.gif";
    private static final String FIELD_ICON = "special/Field.gif";
    private static final String ANNOTATION_ICON = "special/Annotation.gif";
    private static final String DEFAULT_ICON = "special/Default.gif";


    private static final String CALLS_ICON = "special/Calls.gif";
    private static final String DEPENDS_ICON = "special/Depends.gif";
    private static final String CREATES_ICON = "special/Creates.gif";
    private static final String THROWS_ICON = "special/Throws.gif";
    private static final String CATCHES_ICON = "special/Catches.gif";
    private static final String IMPLEMENTS_ICON = "special/Implements.gif";
    private static final String EXTENDS_ICON = "special/Extends.gif";
    private static final String USES_ICON = "special/Uses.gif";
    private static final String CONTAINS_ICON = "special/Contains.gif";
    private static final String INSTANCEOF_ICON = "special/Instanceof.gif";
    private static final String PDM_ICON = "tabs/PDM.gif";
    private static final String PDMROLE_ICON = "tabs/Role.gif";
    private static final String PDMRELATIONSIHP_ICON = "tabs/Relationship.gif";
    private static final String ARGUMENT_ICON = "special/Argument.gif";
    private static final String TYPE_ICON = "special/Type.gif";


    public static Icon getCellIcon(int type) {

        Icon icon = null;
        switch (type) {
            case AcreGraphConstants.PACKAGE:
                icon = AcreUIUtils.createImageIcon(PACKAGE_ICON);
                break;
            case AcreGraphConstants.CLASS:
                icon = AcreUIUtils.createImageIcon(CLASS_ICON);
                break;
            case AcreGraphConstants.INTERFACE:
                icon = AcreUIUtils.createImageIcon(INTERFACE_ICON);
                break;
            case AcreGraphConstants.METHOD:
                icon = AcreUIUtils.createImageIcon(METHOD_ICON);
                break;
            case AcreGraphConstants.PARAMETER:
                icon = AcreUIUtils.createImageIcon(PARAMETER_ICON);
                break;
            case AcreGraphConstants.ABSTRACT_CLASS:
                icon = AcreUIUtils.createImageIcon(ABSTRACT_CLASS_ICON);
                break;
            case AcreGraphConstants.ABSTRACT_METHOD:
                icon = AcreUIUtils.createImageIcon(ABSTRACT_METHOD_ICON);
                break;
            case AcreGraphConstants.FIELD:
                icon = AcreUIUtils.createImageIcon(FIELD_ICON);
                break;
            case AcreGraphConstants.ANNOTATION:
                icon = AcreUIUtils.createImageIcon(ANNOTATION_ICON);
                break;
            case AcreGraphConstants.PDM:
                icon = AcreUIUtils.createImageIcon(PDM_ICON);
                break;
            case AcreGraphConstants.PDMROLE:
                icon = AcreUIUtils.createImageIcon(PDMROLE_ICON);
                break;
            case AcreGraphConstants.PDMRELATIONSHIP:
                icon = AcreUIUtils.createImageIcon(PDMRELATIONSIHP_ICON);
                break;
            default:
                icon = AcreUIUtils.createImageIcon(DEFAULT_ICON);
                break;
        }
        return icon;
    }

    public static Icon getDefaultCellIcon() {
        return AcreUIUtils.createImageIcon(DEFAULT_ICON);
    }


    public static Icon getEdgeIcon(int type) {


        Icon icon;
        switch (type) {
            case AcreGraphConstants.EDGE_CALLS:
                icon = AcreUIUtils.createImageIcon(CALLS_ICON);
                break;
            case AcreGraphConstants.EDGE_DEPENDS:
                icon = AcreUIUtils.createImageIcon(DEPENDS_ICON);
                break;
            case AcreGraphConstants.EDGE_CREATES:
                icon = AcreUIUtils.createImageIcon(CREATES_ICON);
                break;
            case AcreGraphConstants.EDGE_THROWS:
                icon = AcreUIUtils.createImageIcon(THROWS_ICON);
                break;
            case AcreGraphConstants.EDGE_CATCHES:
                icon = AcreUIUtils.createImageIcon(CATCHES_ICON);
                break;
            case AcreGraphConstants.EDGE_IMPLEMENTS:
                icon = AcreUIUtils.createImageIcon(IMPLEMENTS_ICON);
                break;
            case AcreGraphConstants.EDGE_EXTENDS:
                icon = AcreUIUtils.createImageIcon(EXTENDS_ICON);
                break;
            case AcreGraphConstants.EDGE_USES:
                icon = AcreUIUtils.createImageIcon(USES_ICON);
                break;
            case AcreGraphConstants.EDGE_CONTAINS:
                icon = AcreUIUtils.createImageIcon(CONTAINS_ICON);
                break;
            case AcreGraphConstants.EDGE_INSTANCEOF:
                icon = AcreUIUtils.createImageIcon(INSTANCEOF_ICON);
                break;
            default:
                icon = AcreUIUtils.createImageIcon(DEFAULT_ICON);
                break;
        }
        return icon;
    }

    public static Icon getDefaultEdgeIcon() {
        return AcreUIUtils.createImageIcon(DEFAULT_ICON);
    }

}