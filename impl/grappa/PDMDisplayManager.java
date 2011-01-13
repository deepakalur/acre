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

import org.acre.pdmengine.model.PatternResult;

import javax.swing.JPanel;
import java.io.ByteArrayInputStream;


/**
 * Created by IntelliJ IDEA.
 * User: johncrupi
 * Date: Aug 3, 2004
 * Time: 5:51:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class PDMDisplayManager {
    GrappaFrame grappaFrame;
    JPanel grappaPanel;

    static public void main(String[] args) {
        System.out.println("Starting...");
        new PDMDisplayManager();
    }

    public PDMDisplayManager() {
        // createModels();
        //writePDMDot(BdSlSf);
        //writeArtifactsDot(artifactPDM, artifactRole, artifact);
        PDMDotWriter dotWriter = null;
        PatternResult patternResult = null;
        try {
            PDMService pdmService = new PDMService();
            build2D( "BD_Calls_SF");
    //        build3D( "BD_Calls_SF" );

        } catch (PDMServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private void build2D(String pdmName) throws PDMServiceException {
        PDMService pdmService = new PDMService();

        //String pdmResults = pdmService.getPDMResultsDotSource(pdmName);
        byte[] image = pdmService.getPDMResultsDotImage( pdmName  );

        grappaFrame = new GrappaFrame();
        grappaFrame.build( new ByteArrayInputStream( image ) );
        grappaPanel = grappaFrame.getPanel();
    }
    /*
    private void build3D(String pdmName) throws PDMServiceException {
        PDMService pdmService = new PDMService();
        pdmService.build3DViewer(pdmName);
    }
    */
}
