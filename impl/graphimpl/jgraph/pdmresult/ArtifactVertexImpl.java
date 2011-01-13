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
import org.acre.visualizer.graph.graph.pdmresult.vertex.ArtifactVertex;
import org.acre.visualizer.graph.graph.vertex.AcreVertexData;
import org.acre.pdmengine.model.Artifact;

import javax.swing.BorderFactory;
import java.awt.Rectangle;

/**
 * @author Deepak.Alur@Sun.com
 * @version Dec 16, 2004 7:57:34 PM
 */
public class ArtifactVertexImpl
        extends AcreVertexImpl
        implements ArtifactVertex {

    Artifact artifact;

    // todo - change signature later
    public ArtifactVertexImpl(Artifact artifact) {
        super(null);
        this.artifact = artifact;

        AcreVertexData data = new AcreVertexData(artifact.getName(), artifact);
//        //assignAttributes(data);
//        data.setAutoSize(false);
//        data.setColor(AcreGraphConstants.ARTIFACT_COLOR);
        setSalsaData(data);
    }

    // this method to be implemented by all inheriters
    // this method is invoked by setSalsaData
    protected void assignAttributes(AcreVertexData d) {
        d.setCellType(AcreGraphConstants.ARTIFACT);
        d.setColor(AcreGraphConstants.getArtifactColor());
        d.setOpaque(true);

        d.setBounds(new Rectangle(
                AcreGraphConstants.DEFAULT_ARTIFACT_VERTEX_WIDTH,
                AcreGraphConstants.DEFAULT_ARTIFACT_VERTEX_HEIGHT));
        d.setBorder(BorderFactory.createRaisedBevelBorder());
        d.setAutoSize(false);
        d.setCellIcon(AcreGraphIconUtils.getCellIcon(d.getCellType()));

//        d.setCellType(AcreGraphConstants.ARTIFACT);
//        d.setColor(AcreGraphConstants.ARTIFACT_COLOR);
//        d.setOpaque(true);
//        //d.setBounds(new Rectangle(DEFAULT_PDM_CELL_WIDTH, DEFAULT_PDM_CELL_HEIGHT));
//        d.setBorder(BorderFactory.createRaisedBevelBorder());
//        d.setAutoSize(true);
//        d.setCellIcon(AcreGraphIconUtils.getCellIcon(d.getCellType()));
    }

    public Artifact getArtifact() {
        return artifact;
    }

    public void setArtifact(Artifact artifact) {
        this.artifact = artifact;
    }

    public String getVertexName() {
        return artifact.getName();
    }

    public String getToolTipText() {
        return
                new StringBuffer().append("<HTML>")
                .append(artifact.getName())
                .append(" is of type '")
                .append(artifact.getType())
                .append("'")
                .append("<BR>")
                .append("and its value is '")
                .append(artifact.getValue())
                .append("'</HTML>")
                .toString()
        ;
    }

}
