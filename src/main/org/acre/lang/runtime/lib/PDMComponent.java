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
package org.acre.lang.runtime.lib;

import org.acre.lang.runtime.PQL;

import java.util.List;
import java.util.Map;

/**
 * PDM component interface
 * @author Yury Kamen
 */
public interface PDMComponent {

    /**
     * Returns PDM description
     * @return PDM description
     */
    String getDescription();

    /**
     * Returns PDM name
     * @return PDM name
     */
    String getName();

    /**
     * Returns PDM metadata
     * @return PDM metadata
     */
    String getMetadata();

    /**
     * Executes PDM
     * @return  Execution result
     */
    Object execute();
    /**
     * Executes PDM
     * @param inputParameters input parameters map
     * @return  Execution result
     */
    Object execute(Map inputParameters);


    List getOutputNames();

    List getInputNames();
    /**
     * Sets PQL instance
     * @param pql QL instance
     */
    void setPql(PQL pql);

    /**
     * Returns PQL instance
     * @return  PQL instance
     */
    PQL getPql();
}
