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

import org.acre.analytics.AcreContainer;
import org.acre.analytics.AcreContainerFactory;
import org.acre.pdmengine.PatternEngine;
import org.acre.pdmengine.model.PatternResult;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: johncrupi
 * Date: Oct 11, 2004
 * Time: 12:14:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class PDMAppService {
    public PatternResult getFullPDMResults(String pdmName) {
        return getResults(pdmName);
    }

    private PatternResult getResults( String pdmName) {


       // facade.printPatternModels();
       // patternQueryRepositoryyRepositoryyRepository.printGlobalQueryList();

        PatternEngine engine;
        AcreContainer acreContainer = AcreContainerFactory.getInstance().getSalsaContainer();
        engine = acreContainer.getPatternEngine();

        PatternResult result = null;
        result = engine.execute(pdmName);
        return result;
    }

    public byte[] getPDMDotImage(PatternResult patternResult) {
        PDMDotWriter dotWriter = null;
        if (patternResult != null) {
            try {
                dotWriter = new PDMDotWriter( );
                dotWriter.createDotPDM(patternResult);
                dotWriter.writeDotFile( dotWriter.getDotImage(), "graphs/BD_Calls_SF.dot");
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return dotWriter.getDotImage();
    }
    /*
     protected void buildScene(Model model){
        // now we can build the scene...
        Scene scene = new Scene(model.getName());
        SceneBuilder sceneBuilder = new SceneBuilder(model, scene);

        sceneBuilder.buildScene();
    }
    */
    public String getPDMDotSource(PatternResult patternResult) {
        PDMDotWriter dotWriter = null;
        if (patternResult != null) {
            try {
                dotWriter = new PDMDotWriter( );
                dotWriter.createDotPDM(patternResult);
                dotWriter.writeDotFile( dotWriter.getDotImage(), "BD_Calls_SF.dot");
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return dotWriter.getDotSource();
    }

    /*
    public void create3DViewer(String pdmName) {
        PatternResult results = getResults( pdmName );
        W3DWriter w3dwriter = new W3DWriter();
        w3dwriter.writePDM( results );
    }
    */

}
