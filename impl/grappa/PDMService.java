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
import org.acre.pdmengine.model.Result;

/**
 * Created by IntelliJ IDEA.
 * User: johncrupi
 * Date: Oct 10, 2004
 * Time: 10:36:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class PDMService {

    public PatternResult getPDMResults(String pdmName) throws PDMServiceException {
        PDMAppService as = new PDMAppService();
        return as.getFullPDMResults(pdmName);
    }

    public String getPDMResultsDotSource(String pdmName) throws PDMServiceException {
        PDMAppService as = new PDMAppService();
        PatternResult result = as.getFullPDMResults(pdmName);
        return as.getPDMDotSource(result);
    }
     public byte[] getPDMResultsDotImage(String pdmName) throws PDMServiceException {
        PDMAppService as = new PDMAppService();
        PatternResult result = as.getFullPDMResults(pdmName);
        return as.getPDMDotImage(result);
    }
    public Result getArtifactRelationships(String pdmName, String roleName, String artifactName)
            throws PDMServiceException {
        return null;
    }

    public String getArtifactRelationshipsInDOT(String pdmName, String roleName, String artifactName)
            throws PDMServiceException {
        return null;
    }

    public PatternResult getPDMRelcoResults(String pdmName, String[] relcoRoleNames)
            throws PDMServiceException {
        return null;
    }

    public String getPDMRelcoResultsInDOT(String pdmName, String[] relcoRoleNames)
            throws PDMServiceException {
        return null;
    }
    /*
    public void build3DViewer(String pdmName) {
        PDMAppService as = new PDMAppService();
        as.create3DViewer(pdmName);
    }
    */
}
