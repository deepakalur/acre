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
package org.acre.visualizer.graphimpl;

import org.acre.dao.DAOFactory;
import org.acre.visualizer.graph.graph.*;
import org.acre.visualizer.graph.graph.pdmmodel.PDMModelGraphBuilder;
import org.acre.visualizer.graph.graph.pdmmodel.PDMModelGraphModel;
import org.acre.pdm.PDMType;

import javax.swing.JFrame;
import java.awt.Component;

/**
 * @author Deepak.Alur@Sun.com
 * @version Dec 22, 2004 3:53:53 PM
 */
public class PDMModelGraphTester {
    public static void main(String args[]) {

        int graphType;
        graphType = GraphFactoryConstants.JGRAPH;
        showGraph(graphType, AcreGraph.TREE_LAYOUT);
        graphType = GraphFactoryConstants.JUNG;
        showGraph(graphType, AcreGraph.TREE_LAYOUT);

    }

    private static void showGraph(int graphType, int layoutType) {
        PDMModelGraphBuilder builder;
        builder = new PDMModelGraphBuilder(graphType);
        builder.setFactory(GraphObjectFactory.getFactory(graphType));
        PDMModelGraphModel model = GraphModelFactory.createPDMModelGraph(graphType);
        builder.setModel(model);

        try {

            PDMType pdm = DAOFactory.getPatternRepository().getGlobalPatternModel("BD_Calls_SF");

            builder.addPDM(pdm, true);

            AcreGraph g = GraphFactory.createPDMModelGraph(graphType);

            //SalsaPDMResultJGraph g = new SalsaPDMResultJGraph();

            g.setSalsaGraphModel(model);

            g.setLayoutType(layoutType);
            g.applyLayout();

            JFrame f = new JFrame ("PDMResultVis");

            Component view = g.getView();

            f.getContentPane().add(view);
            f.pack();
            f.setVisible(true);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        } catch (Throwable e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
