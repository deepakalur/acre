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
import org.acre.visualizer.graph.graph.AcreGraphConstants;
import org.acre.visualizer.graph.graph.AcreGraphIconUtils;
import org.acre.visualizer.graph.graph.pdmresult.vertex.PDMResultVertex;
import org.acre.visualizer.graph.graph.vertex.AcreVertexData;
import org.acre.pdmengine.model.PatternResult;

import javax.swing.BorderFactory;
import java.awt.Rectangle;

/**
 * @author Deepak.Alur@Sun.com
 * @version Dec 18, 2004 10:53:11 AM
 */
public class PDMResultVertexImpl
        extends AcreVertexImpl
        implements PDMResultVertex {

    PatternResult result;

    public PDMResultVertexImpl(PatternResult result) {
        super(null);
        this.result = result;
        AcreVertexData data = new AcreVertexData(result.getName(), result);
        //assignAttributes(data);
        setSalsaData(data);
    }

    // this method to be implemented by all inheriters
    // this method is invoked by setSalsaData
    protected void assignAttributes(AcreVertexData data) {
        data.setCellType(AcreGraphConstants.PDMRESULT);
        data.setColor(AcreGraphConstants.getPDMColor());
        data.setOpaque(true);

        data.setBounds(new Rectangle(AcreGraphConstants.DEFAULT_PDMRESULT_VERTEX_WIDTH,
                AcreGraphConstants.DEFAULT_PDMRESULT_VERTEX_WIDTH));
        data.setBorder(BorderFactory.createRaisedBevelBorder());
        data.setAutoSize(false);
        data.setCellIcon(AcreGraphIconUtils.getCellIcon(AcreGraphConstants.PDM));
    }

    public String getVertexName() {
        return result.getName();
    }

    public String getToolTipText() {
        String str = result.toString();

        if (str.length() > 120) {
            return str.substring(0, 120) + "..." ;
        } else return str;
    }
}
