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

import org.acre.analytics.AcreContainer;
import org.acre.analytics.AcreContainerFactory;
import org.acre.dao.PQLConnectionManager;
import org.acre.visualizer.graph.graph.GraphFactory;
import org.acre.visualizer.graph.graph.GraphFactoryConstants;
import org.acre.visualizer.graph.graph.GraphModelFactory;
import org.acre.visualizer.graph.graph.AcreGraph;
import org.acre.visualizer.graph.graph.pdmresult.PDMResultGraphBuilder;
import org.acre.visualizer.graph.graph.pdmresult.PDMResultGraphModel;
import org.acre.lang.pql.pdbc.PQLConnection;
import org.acre.pdmengine.PatternEngine;
import org.acre.pdmengine.model.PatternResult;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import java.awt.Component;

/**
 * @author Deepak.Alur@Sun.com
 * @version Dec 20, 2004 11:09:16 PM
 */
public class PDMResultGraphTester {
    public static void main(String args[]) {

        int graphType;
        graphType = GraphFactoryConstants.JGRAPH;
        showGraph(graphType, AcreGraph.SUGIYAMA_LAYOUT);
        graphType = GraphFactoryConstants.JUNG;
        showGraph(graphType, AcreGraph.SPRING_EMBEDDED_LAYOUT);

    }

    private static void showGraph(int graphType, int layoutType) {

        PDMResultGraphBuilder builder;
        builder = new PDMResultGraphBuilder(graphType);
        PDMResultGraphModel model = GraphModelFactory.createPDMResultGraphModel(graphType);
        builder.setModel(model);

        PQLConnection conn = null;
        try {
            conn = PQLConnectionManager.getInstance().getGlobalConnection();

            PatternEngine engine;
            AcreContainer container = AcreContainerFactory.getInstance().getSalsaContainer();
            engine = container.getPatternEngine();

            PatternResult patternResult = engine.execute("SessionFacade");

            builder.addPDMResult(patternResult);


            AcreGraph g = GraphFactory.createPDMResultGraph(graphType);
            //SalsaPDMResultJGraph g = new SalsaPDMResultJGraph();
            g.setSalsaGraphModel(model);
            g.setLayoutType(layoutType);
            g.applyLayout();

            JFrame f = new JFrame ("PDMResultVis");

            Component view = g.getView();


            JScrollPane scroller = new JScrollPane();
            scroller.getViewport().setView(view);
            f.getContentPane().add(scroller);
            f.pack();
            f.setVisible(true);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        } catch (Throwable e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            PQLConnectionManager.getInstance().release();
        }

    }
}
