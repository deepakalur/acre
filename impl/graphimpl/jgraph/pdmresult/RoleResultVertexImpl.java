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
package org.acre.visualizer.graphimpl.jgraph.pdmresult;

import org.acre.visualizer.graphimpl.jgraph.AcreVertexImpl;
import org.acre.visualizer.graph.graph.pdmresult.vertex.RoleResultVertex;
import org.acre.visualizer.graph.graph.vertex.AcreVertexData;
import org.acre.pdmengine.model.PatternResult;
import org.acre.pdmengine.model.RoleResult;

/**
 * @author Deepak.Alur@Sun.com
 * @version Dec 20, 2004 10:45:03 PM
 */
public class RoleResultVertexImpl
        extends AcreVertexImpl
        implements RoleResultVertex {

    private PatternResult pattern;
    private RoleResult result;

    public RoleResultVertexImpl(PatternResult pattern, RoleResult result) {
        super(null);
        this.result = result;
        this.pattern = pattern;
        AcreVertexData data = new AcreVertexData(result.getName(), result);
        assignAttributes(data);
        setSalsaData(data);

    }

    // this method to be implemented by all inheriters
    // this method is invoked by setSalsaData
    protected void assignAttributes(AcreVertexData data) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getVertexName() {
        return result.getName();
    }

    public String getToolTipText() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append(result.toString());

        sbuf.append(result.getRoleResult().toString());

        String str = sbuf.toString();
        
        if (str.length() > 120) {
            return str.substring(0, 120) + "..." ;
        } else return str;
    }


    public PatternResult getPDMResult() {
        return pattern;
    }

    public RoleResult getRoleResult() {
        return result;
    }
}
