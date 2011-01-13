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
import org.acre.pdm.RoleType;
import org.acre.dao.DAOFactory;
import org.acre.dao.PatternRepository;
import org.acre.visualizer.graph.*;
import org.acre.visualizer.graph.pdmmodel.PDMModelGraph;
import org.acre.visualizer.graph.pdmmodel.PDMModelGraphBuilder;
import org.acre.visualizer.graph.pdmmodel.PDMModelGraphModel;
import org.acre.visualizer.graph.pdmmodel.edges.RoleToRoleEdge;
import org.acre.visualizer.graph.pdmmodel.vertex.RoleVertex;
import org.acre.visualizer.ui.components.Editor;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.Component;
import java.awt.LayoutManager;

/**
 * @author Deepak.Alur@Sun.com
 *         Date: Jun 18, 2004
 *         Time: 1:10:11 AM
 */
public class AcrePDMVisualizer extends JPanel implements Editor {
    AcreGraphViewToolBar toolbar;
    JScrollPane diagramPane;
    PDMModelGraph diagram;
    PDMModelGraphModel model;
    int graphType;
    PDMModelGraphBuilder builder;
    Editor uiEditor;

    public void setUIEditor(Editor editor) {
        this.uiEditor = editor;
        diagram.setPDMEditor(uiEditor);
    }

    public Editor getUIEditor() {
        return uiEditor;
    }

    //private SalsaPDMGraphModel model;

    public AcreGraph getDiagram() {
        return diagram;
    }

    public void setDiagram(PDMModelGraph diagram) {
        this.diagram = diagram;
        diagramPane.getViewport().add((Component) diagram);
        toolbar.setGraph(diagram);
        ((Component)diagram).repaint();
    }

    public AcrePDMVisualizer() {
        super();
        initialize();
    }

    public AcrePDMVisualizer(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
        initialize();
    }

    public AcrePDMVisualizer(LayoutManager layout) {
        super(layout);
        initialize();
    }

    public AcrePDMVisualizer(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
        initialize();
    }

    public void initialize() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        graphType = GraphFactoryConstants.JGRAPH;
        model = GraphModelFactory.createPDMModelGraph(graphType);
        builder = new PDMModelGraphBuilder(graphType);
        builder.setFactory(GraphObjectFactory.getFactory(graphType));
        builder.setModel(model);
        diagram = (PDMModelGraph) GraphFactory.createPDMModelGraph(graphType);
        diagram.setLayoutType(AcreGraph.TREE_LAYOUT);
        diagram.setAcreGraphModel(model);
        toolbar = new AcreGraphViewToolBar();
        toolbar.setGraph(diagram);
        add(toolbar);
        diagramPane = new JScrollPane();
        diagramPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        diagramPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        diagramPane.getViewport().add((Component) diagram);
        add(diagramPane);
        ((Component)diagram).repaint();
    }

    public static void main(String args[]) {
        JFrame frame = new JFrame("SalsaPDMVisualzer");

        PatternRepository facade = DAOFactory.getPatternRepository();

        AcrePDMVisualizer pdmViz = new AcrePDMVisualizer();
        //AcreDiagram pdmGraph = pdmViz.getDiagram(); //(pdmGraph);
        //PDMModelGraphModel model = (PDMModelGraphModel) pdmViz.getDiagram().getAcreGraphModel(); //new SalsaPDMGraphModel();

        pdmViz.insertPDM((PDMType) facade.getGlobalPatternModels().get(0));
//        pdmViz.insertPatternModel((PDMType) facade.getGlobalPatternModels().get(1));
//        pdmViz.insertPatternModel((PDMType) facade.getGlobalPatternModels().get(2));

        //AcreDiagram pdmGraph = new AcreDiagram(model);


        try {
            pdmViz.getDiagram().applyLayout();
        } catch (Throwable t) {
            t.printStackTrace();
        }

        frame.getContentPane().add(pdmViz);

        frame.pack();

        frame.setVisible(true);
    }

    public PDMModelGraphModel getModel() {
        return (PDMModelGraphModel) diagram.getAcreGraphModel();
    }

    public void setModel(PDMModelGraphModel newModel) {
        //model = newModel;
        diagram.setAcreGraphModel(newModel);
        //diagram.applyLayout();
    }

    // Make the Query Panel implement this interface and plug it into the QueryViewer
    public void viewObject(Object editObject) {
//        System.out.println("AcrePDMVisualizer: setEditObject" + editObject);

    }

    public void deleteObject(Object key) {
        //todo - delete PDM
    }

    public void addObject(Object info) {
        // todo - add PDM
    }

    public void executeObject(Object key) {
        // todo - execute PDM
    }

    public void editObject(Object value) {
        // todo - edit PDM
    }

    public void clear() {
        // todo - clear UI
    }

    public void refreshList() {
        // todo - refresh PDM list
    }

    public Component getView() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void removeAllPDMs() {
        Object [] cells = ((PDMModelGraphModel) getDiagram().getAcreGraphModel()).getAllCells();

        if ((cells == null) || (cells.length == 0))
            return;

        getDiagram().getAcreGraphModel().remove(cells);

    }

    public void insertPDM(PDMType pdm) {
        builder.setModel(getModel());
        builder.addPDM(pdm, false);
        redoLayout();
    }

    private void redoLayout() {
        try {
            //this.diagram.setAcreGraphModel(model);
            this.diagram.applyLayout();
            ((Component)diagram).repaint();
            
        } catch (AcreGraphException e) {
            e.printStackTrace(); // todo
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void showSinglePDM(PDMType pdm) {
        model.clearAll();
        insertPDM(pdm);
    }

    public RoleVertex getSelectedRole() {
        return diagram.getSelectedRole();
    }

    public RoleToRoleEdge getSelectedRelationship() {
        return diagram.getSelectedRoleToRoleRelationship();
    }
}
