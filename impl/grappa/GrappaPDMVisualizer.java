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
package org.acre.visualizer.grappa;

import javax.swing.JPanel;
import java.util.logging.Logger;

/**
 * Grappa GUI adapter
 *
 * @author Yury Kamen
 */
public class GrappaPDMVisualizer {
    Logger log = Logger.getLogger("GrappaPDMVisualizer");

    GrappaFrame grappaFrame;
    JPanel grappaPanel;


    static public void main(String[] args) throws Exception {
        String pdmName = "BD_Calls_SF";  // "ApplicationController"
        if (args.length == 0) {

        } else if (args.length == 1) {
            pdmName = args[0];
        } else {
             throw new IllegalArgumentException("args.length == " + args.length);
        }
        GrappaPDMVisualizer grappaPDMVisualizer = new GrappaPDMVisualizer();
        PDMExecutor pdmExecutor = new PDMExecutor();
        pdmExecutor.setUp();
        PatternResult patternResult = pdmExecutor.execute(pdmName);
        grappaPDMVisualizer.build2D(patternResult);
        pdmExecutor.tearDown();
    }

    public void build2D(PatternResult patternResult) throws PDMServiceException {
         build2D(patternResult, true, true);
    }
    public void build2D(PatternResult patternResult, boolean visible, boolean extraUI) throws PDMServiceException {
        grappaFrame = new GrappaFrame();
        String dotGrappaSource = GrappaUtilities.getPDMDotImage(patternResult);
        grappaFrame.build(dotGrappaSource, visible, extraUI);
        grappaPanel = grappaFrame.getPanel();
    }
}
