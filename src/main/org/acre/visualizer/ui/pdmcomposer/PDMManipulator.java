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

package org.acre.visualizer.ui.pdmcomposer;

import org.acre.pdm.PDMType;

/**
 *
 * @author  Administrator
 */
public class PDMManipulator {
    
    PDMType currentPDM;
    PDMEditorForm mainForm;
    PDMRelationshipForm relationshipForm;
    PDMRoleForm roleForm;
    
    /** Creates a new instance of PDMManipulator */
    public PDMManipulator(PDMType pdm, PDMEditorForm mainForm,
        PDMRelationshipForm relationshipForm, PDMRoleForm roleForm) {        
            this.currentPDM = pdm;
            this.mainForm = mainForm;
            this.relationshipForm = relationshipForm;
            this.roleForm = roleForm;
            
            
    }
    
    
    
}
