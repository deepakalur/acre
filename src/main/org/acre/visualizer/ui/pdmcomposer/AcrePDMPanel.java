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
import org.acre.pdm.RelationshipType;
import org.acre.pdm.RoleType;
import org.acre.common.AcreRuntimeException;
import org.acre.config.ConfigService;
import org.acre.dao.DAOFactory;
import org.acre.dao.PatternRepository;
import org.acre.visualizer.graph.AcreGraphSelectionListener;
import org.acre.visualizer.graph.edges.AcreEdge;
import org.acre.visualizer.graph.pdmmodel.edges.RoleToRoleEdge;
import org.acre.visualizer.graph.pdmmodel.vertex.PDMVertex;
import org.acre.visualizer.graph.pdmmodel.vertex.RoleVertex;
import org.acre.visualizer.graph.vertex.AcreVertex;
import org.acre.pdmengine.model.PatternResult;
import org.acre.visualizer.ui.GlobalSettings;
import org.acre.visualizer.ui.Main;
import org.acre.visualizer.ui.components.Editor;
import org.acre.visualizer.ui.pqleditor.PQLComboPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Logger;


/**
 * @author DeepakAlur@Sun.com
 */
public class AcrePDMPanel
        extends javax.swing.JPanel
        implements ActionListener, Editor, AcreGraphSelectionListener {

    public static final int DEFAULT_GRAPH_VIEW_WIDTH = 1000;
    public static final int DEFAULT_GRAPH_VIEW_HEIGHT = 1000;
    public static int SHOW_MODE_APPEND = 1;
    public static int SHOW_MODE_SINGLE = 2;

    private static final String EXECUTE = "Execute";
    private static final String CLEAR = "Clear";
    private static final String SAVE = "Save";
    private static final String CANCEL = "Cancel";
    private static final String SHOW_TABLE = "Show Table";
    private static final String SHOW_TEXT = "Show Text";
    private int dividerSize = GlobalSettings.getDividerSize();
    private int dividerLocation = GlobalSettings.getVerticalDividerLocation();
    private Hashtable grappaCache = new Hashtable();
    private Hashtable pdmVisualizerCache = new Hashtable();

    HashMap availableModels = new HashMap();

    JTextArea queryText;
    PQLComboPane queryResultsTab;
    JButton executeButton;
    JButton clearButton;
    JButton saveButton;
    JToggleButton showHideResultsButton;
    //AcrePDMVisualizer visualizer;

    JTabbedPane listTab;
    PDMViewer globalPDMList;
    PDMViewer projectPDMList;

    private static final Cursor waitCursor = new Cursor(Cursor.WAIT_CURSOR);
    private java.lang.Thread runningThread = null;
    private Cursor fMainCursor;
    private Cursor queryTextCursor;
    //private PQL pql;
    private JPanel leftPanel;

    private int showMode = SHOW_MODE_SINGLE;
    private static final String GLOBAL_PDM_LIST_NAME = "Global PDMs";
    private static final String PROJECT_PDM_LIST_NAME = "Project PDMs";
    private PDMEditorForm pdmEditorForm;
    private JDialog pdmEditorDialog;
    private JFrame parentFrame;
    private Logger logger;
    Component lastAdded=null;
    private JSplitPane nsSplitPane;
    private PDMEditorToolBar editorToolBar;

    public AcrePDMPanel() {
        super();
        initialize();
    }

    public AcrePDMPanel(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
        initialize();
    }

    public AcrePDMPanel(LayoutManager layout) {
        super(layout);
        initialize();
    }

    public AcrePDMPanel(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
        initialize();
    }

    public void initialize() {
        ToolTipManager.sharedInstance().registerComponent(this);
        setToolTipText("PDM Explorer");

        logger = ConfigService.getInstance().getLogger(this);

        AcrePDMVisualizer visualizer = new AcrePDMVisualizer();

        listTab = new JTabbedPane();

        globalPDMList = new PDMViewer();
        globalPDMList.setEditor(this);

        projectPDMList = new PDMViewer();
        projectPDMList.setEditor(this);

        listTab.addTab(GLOBAL_PDM_LIST_NAME, globalPDMList);
        //listTab.addTab(PROJECT_PDM_LIST_NAME, projectPDMList);

        leftPanel = new JPanel();
        //leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setLayout(new BorderLayout());

        queryText = new JTextArea();
        queryText.setColumns(65);
        queryText.setLineWrap(true);
        queryText.setRows(5);
        queryText.setEditable(false);
        queryText.setBackground(Color.lightGray);
        queryText.setToolTipText("PQL Queries generated for Translatable PDMs");
        ToolTipManager.sharedInstance().registerComponent(queryText);

        JScrollPane queryScroll = new JScrollPane(queryText);
        queryScroll.setBorder(BorderFactory.createTitledBorder("PDM->PQL"));

        executeButton = new JButton(EXECUTE);
        executeButton.addActionListener(this);
        clearButton = new JButton(CLEAR);
        clearButton.addActionListener(this);
        saveButton = new JButton(SAVE);
        saveButton.addActionListener(this);
        showHideResultsButton = new JToggleButton(SHOW_TEXT);
        JToggleButton.ToggleButtonModel model = new JToggleButton.ToggleButtonModel();
        model.setPressed(false);
        showHideResultsButton.setModel(model);
        showHideResultsButton.addActionListener(this);

        queryResultsTab = new PQLComboPane();

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.add(queryResultsTab);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(clearButton);
        buttonPanel.add(executeButton);
        buttonPanel.add(saveButton);

//        rightPanel = new JPanel();
//        rightPanel.setLayout(new BorderLayout());
//        rightPanel.add(visualizer, BorderLayout.CENTER);
//        lastAdded = visualizer;

        //rightPanel.add(queryScroll, BorderLayout.SOUTH);
        //rightPanel.add(buttonPanel, BorderLayout.SOUTH);

        leftPanel.add(listTab, BorderLayout.CENTER);
        //leftPanel.add(queryScroll, BorderLayout.NORTH);

        nsSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        nsSplitPane.setTopComponent(visualizer);
        nsSplitPane.setBottomComponent(textPanel);
        nsSplitPane.setOneTouchExpandable(true);
//        nsSplitPane.setDividerSize(dividerSize);
        nsSplitPane.setDividerLocation(dividerLocation);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        JSplitPane ewSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, nsSplitPane);
//        ewSplitPane.setDividerSize(dividerSize);
        ewSplitPane.setDividerLocation(GlobalSettings.getHorizontalDividerLocation());

        editorToolBar = new PDMEditorToolBar();
        // todo editorToolBar.setPDMPanel(this);
        editorToolBar.reset();

        this.setLayout(new BorderLayout());
        this.add(editorToolBar, BorderLayout.NORTH);
        this.add(ewSplitPane, BorderLayout.CENTER);
    }

    public void setGlobalPDMList(List queries) {
        this.globalPDMList.getList().setListData(new ArrayList(queries));
    }

    public static void main(String args[]) {
        JFrame f = new JFrame();

        PatternRepository facade = DAOFactory.getPatternRepository();
        List pdms = facade.getGlobalPatternModels();                             // Yury Kamen: hack

        AcrePDMPanel panel = new AcrePDMPanel();
        panel.setGlobalPDMList(pdms);
        f.getContentPane().add(panel);
        //panel.setPql(new PQL("factextractor/test.ta"));
        f.pack();
        f.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
//        System.out.println("AcreQueryPanel : Action = " + e);

        if (e.getSource() instanceof JButton) {
            if (e.getActionCommand().equals(EXECUTE)) {
//                System.out.println("Execute query:" + queryText.getText());
//                execute();

            } else if (e.getActionCommand().equals(SAVE)) {
//                System.out.println("Save query:" + queryText.getText());
            } else if (e.getActionCommand().equals(CLEAR)) {
//                System.out.println("Clear Query");
                queryText.setText(null);
            } else if (e.getActionCommand().equals(CANCEL)) {
                cancelExecute();
            }
        }
        if (e.getSource() instanceof JComboBox) {
//            // todo : select database here...
//            System.out.println("Combo Box Event:" + e);
        }
        if (e.getSource() instanceof JToggleButton) {
//            System.out.println("Toggle Box Event:" + e);
            showHideResultsButton.getModel().setPressed(!showHideResultsButton.getModel().isPressed());
            if (e.getActionCommand().equals(SHOW_TEXT)) {
                showHideResultsButton.setText(SHOW_TABLE);
                queryResultsTab.showResultsTable(false);
                queryResultsTab.showResultsText(true);
            } else {
                showHideResultsButton.setText(SHOW_TEXT);
                queryResultsTab.showResultsTable(true);
                queryResultsTab.showResultsText(false);
            }
        }
    }

    private void cancelExecute() {
        if ((runningThread != null) && (runningThread.isAlive())) {
            //runningThread.interrupt();
            // todo - cancel the execution and return
        }
    }

    public void setWaiting(boolean waiting) {
        queryResultsTab.setWaiting(waiting);

        if (waiting) {
            if (fMainCursor == null) {
                fMainCursor = this.getCursor();
                queryTextCursor = queryText.getCursor();
            }
            this.setCursor(waitCursor);
            queryText.setCursor(waitCursor);
        } else {
            this.setCursor(fMainCursor);
            queryText.setCursor(queryTextCursor);
        }

    }

//    private GrappaPanel getGrappaPanelForView(String pdmName) throws Exception {
//        GrappaPanel grappaPanel = (GrappaPanel) grappaCache.get(pdmName);
//        return grappaPanel;
//    }
//
//    public GrappaPanel getGrappaPanel(final String pdmName) throws Exception {
//        GrappaPanel grappaPanel = (GrappaPanel) grappaCache.get(pdmName);
//        if(null != grappaPanel) {
//            return grappaPanel;
//        }
//        return null;
//    }
//
//    public void removeGrappaPanel(final String pdmName) throws Exception {
//        GrappaPanel grappaPanel = (GrappaPanel) grappaCache.get(pdmName);
//        if (grappaPanel != null)
//            grappaCache.remove(pdmName);
//    }


    private void executePDM(String execPDMName) {
        ExecutePDMThread thread = new ExecutePDMThread(execPDMName);
        thread.start();
    }

    public void viewObject(Object editObject) {
        try {

            if (editObject == null) {
                return;
            }

            if (editObject instanceof PDMType) {
                PDMType pdm = (PDMType) editObject;
                editorToolBar.selectPDM();
                visualizePDM(pdm);
                queryResultsTab.showResultsGraphOnly(true);
//                GrappaPanel grappaPanel = getGrappaPanelForView(pdm.getName());
//                if (grappaPanel != null) {
//                    queryResultsTab.setResultsGraphViewPort(grappaPanel);
//                } else {
//                    queryResultsTab.removeResultsGraphViewPort();
//                }
            }
        } catch (Exception e) {
            logger.throwing("AcrePDMPanel", "viewObject", e);
        }
    }

    private void visualizePDM(PDMType pdm) {

        final PDMType visualPDM = pdm;
        final AcrePDMPanel uiEditor = this;

            SwingUtilities.invokeLater(new Thread() {
                public void run() {
//                    setWaiting(true);
                    AcrePDMVisualizer visualizer =
                            (AcrePDMVisualizer) pdmVisualizerCache.get(visualPDM.getName());

                    if (visualizer == null) {
                        System.out.println("Creating new PDM Visualizer for " + visualPDM.getName());
                        visualizer = new AcrePDMVisualizer();
                        visualizer.setUIEditor(uiEditor);
                        visualizer.getDiagram().addSelectionListener(uiEditor);
                        if (showMode == SHOW_MODE_SINGLE) {
                           visualizer.showSinglePDM(visualPDM);
                        } else if (showMode == SHOW_MODE_APPEND) {
                            visualizer.insertPDM(visualPDM);
                        }
                    } else {
                        System.out.println("Got Visualizer for " + visualPDM.getName());
                    }

                    int prevLoc = nsSplitPane.getDividerLocation();
                    nsSplitPane.setTopComponent(visualizer);
                    nsSplitPane.setDividerLocation(prevLoc);

                    if (pdmVisualizerCache.get(visualPDM.getName()) == null) {
                        System.out.println("Adding PDM Visualizer to cache " + visualPDM.getName());
                        pdmVisualizerCache.put(visualPDM.getName(), visualizer);
                    }
//                    setWaiting(false);
                }
            });
    }


    public void deleteObject(Object key) {
        if (key == null) {
            Main.showMainError("PDM not selected from the list.\nPlease selecte a PDM and retry.", "Delete PDM");

            return;
        }

        if (key instanceof PDMType) {

            boolean confirm = Main.showMainConfirm(this,
                    "Do you really want to delete PDM '" + ((PDMType) key).getName()
                    + "' ?\n", "Delete PDM");

            if (confirm) {
                try {
                    if (DAOFactory.getPatternRepository().deleteGlobalPatternModel((PDMType) key)) {
                        Main.showMainInfo("Deleted PDM '" + ((PDMType) key).getName()
                                + "'", "Delete PDM");
                        //clear editor
                        clear();

                        // refresh list
                        refreshGlobalPDMList();
                    }
                } catch (Throwable t) {
                    Main.showMainError(t.getMessage(), "Delete PDM Failed");
                }
            }

        } else {
            Main.showMainError("Attempt to delete unknown object type" +
                    key, "Delete PDM");
        }
    }

    public void addObject(Object info) {
        // info is not used for now...
        try {
            PDMType newPdm = DAOFactory.getPatternRepository().createNewPatternModel();
            showPDMEditor(newPdm, true);
        } catch (AcreRuntimeException t) {
            Main.showMainError(t.getErrorMessages(), "Error - Add New Query");
        }

    }

    private void initPdmEditorDialog() {
        if (pdmEditorForm == null) {
            pdmEditorForm = new PDMEditorForm();
            pdmEditorForm.setParentFrame(getParentFrame());
            pdmEditorDialog = new JDialog(getParentFrame(), true);
            pdmEditorDialog.setContentPane(pdmEditorForm);
            pdmEditorDialog.pack();
            pdmEditorDialog.setLocationRelativeTo(getParentFrame());
            pdmEditorForm.setDialog(pdmEditorDialog);
        }
    }

    private void showPDMEditor(PDMType newPDM, boolean isNewPDM) {

        initPdmEditorDialog();

        if (isNewPDM)
            pdmEditorDialog.setTitle("PDM Editor - Add New PDM");
        else
            pdmEditorDialog.setTitle("PDM Editor - Edit PDM");

        this.pdmEditorForm.setPDM(newPDM, isNewPDM);

        pdmEditorForm.showMe();

        checkRefresh(newPDM);


    }

    private void checkRefresh(PDMType newPDM) {
        if (newPDM == null)
            return;

        if ((pdmEditorForm != null) &&
            (! pdmEditorForm.isOperationCanceled())) {
            refreshPDMView(newPDM);
        }

        // refresh the list of PDMs
        refreshGlobalPDMList();

        // re-select the edited query in the list
        this.globalPDMList.getList().setSelectedValue(newPDM.getName());
    }

    private void refreshPDMView(PDMType pdm) {
        // recreate the visualization for the selecetd pdm

        pdmVisualizerCache.remove(pdm.getName());
        viewObject(pdm);

    }

    private AcrePDMVisualizer getPDMViewFromCache(PDMType pdm) {
        return (AcrePDMVisualizer) pdmVisualizerCache.get(pdm.getName());
    }

    public JFrame getParentFrame() {
        return parentFrame;
    }

    public void setParentFrame(JFrame parentFrame) {
        this.parentFrame = parentFrame;
    }

    public void executeObject(Object key) {
        if (key == null) {
            Main.showMainError("PDM not selected from the list.\nPlease selecte a PDM and retry.", "Execute PDM");
            return;
        }
        if (key instanceof PDMType) {
            PDMType pdm = (PDMType) key;
            executePDM(pdm.getName());
        }
    }

    public void editObject(Object value) {
        if (value == null) {
            Main.showMainError("PDM not selected from the list.\nPlease selecte a PDM and retry.", "Edit PDM");
            return;
        }
        if (value instanceof PDMType) {
            PDMType valuePDM = (PDMType) value;
            PDMType editPDM = DAOFactory.getPatternRepository().getGlobalPatternModel(valuePDM.getName());
            showPDMEditor(editPDM, false);
        } else {
            Main.showMainError("Attempt to edit unknown object type" +
                    value, "Edit PDM");
        }

    }

    public void clear() {
        if (queryResultsTab != null) {
            queryResultsTab.setData(null);
        }
        queryText.setText("");
        if (globalPDMList != null) {
            globalPDMList.getList().clearSelection();
            //globalQueriesList.getList().setSelectedIndex(-1);
        }
        if (pdmEditorForm != null) {
            pdmEditorForm.clear();
        }

    }

    public void refreshList() {
        refreshGlobalPDMList();
    }

    public Component getView() {
        return null;
    }


    private void refreshGlobalPDMList() {
        PDMType p = globalPDMList.getSelectedPDM();

        // force reload of all PDMs from the repository
        // this will pick up any new PDMs created/deposited by the user
        // manually outside of SALSA UI
        DAOFactory.getPatternRepository().load();

        List refreshQueries = DAOFactory.getPatternRepository().getGlobalPatternModels();
        setGlobalPDMList(refreshQueries);
        if (p != null) {
            globalPDMList.getList().setSelectedValue(p.getName());
        } else {
            globalPDMList.getList().setSelectedIndex(-1);
        }
    }

    // this is called from the toolbar
    public void addPDMNewRole() {
        PDMType selectedPDM = globalPDMList.getSelectedPDM();
        if (selectedPDM != null)
            addPDMNewRole(selectedPDM);
    }

    public void editSelectedRole() {
        PDMType selectedPDM = globalPDMList.getSelectedPDM();
        if (selectedPDM == null)
            return;
        AcrePDMVisualizer selectedVis = getPDMViewFromCache(selectedPDM);
        RoleVertex role = selectedVis.getSelectedRole();
        if (role != null) {
            editRole(role.getPDM(), role.getRole());
        }
    }

    public void deleteSelectedRole() {
        PDMType selectedPDM = globalPDMList.getSelectedPDM();
        if (selectedPDM == null)
            return;
        AcrePDMVisualizer selectedVis = getPDMViewFromCache(selectedPDM);
        RoleVertex role = selectedVis.getSelectedRole();
        if (role != null) {
            deleteRole(role.getPDM(), role.getRole());
        }
    }

    public void addPDMNewRelationship() {
        PDMType selectedPDM = globalPDMList.getSelectedPDM();
        if (selectedPDM != null)
            addPDMNewRelationship(selectedPDM);
    }

    public void editSelectedRelationship() {
        PDMType selectedPDM = globalPDMList.getSelectedPDM();
        if (selectedPDM == null)
            return;
        AcrePDMVisualizer selectedVis = getPDMViewFromCache(selectedPDM);
        RoleToRoleEdge e = selectedVis.getSelectedRelationship();
        if (e != null) {
            editRelationship(e.getPDM(), e.getRelationship());
        }
    }

    public void deleteSelectedRelationship() {
        PDMType selectedPDM = globalPDMList.getSelectedPDM();
        if (selectedPDM == null)
            return;
        AcrePDMVisualizer selectedVis = getPDMViewFromCache(selectedPDM);
        RoleToRoleEdge e = selectedVis.getSelectedRelationship();
        if (e != null) {
            deleteRelationship(e.getPDM(), e.getRelationship());
        }
    }

    // this is called from graph popup
    public void addPDMNewRole(PDMType pdm) {
        initPdmEditorDialog();

        pdmEditorDialog.setTitle("PDM Editor - Add Role");

        this.pdmEditorForm.setPDM(pdm, false);
        this.pdmEditorForm.addRole();
        if (!pdmEditorForm.getRoleForm().isOperationCanceled()) {
            this.pdmEditorForm.save();
            checkRefresh(pdm);
        }

    }

    // this is called from the graph popup
    public void editRole(PDMType pdm, RoleType role) {
        initPdmEditorDialog();

        pdmEditorDialog.setTitle("PDM Editor - Edit Role");

        this.pdmEditorForm.setPDM(pdm, false);
        this.pdmEditorForm.selectInRoleTable(role);
        this.pdmEditorForm.showEditRole();
//        this.pdmEditorForm.getRoleForm().save();
        if (! pdmEditorForm.getRoleForm().isOperationCanceled()) {
            this.pdmEditorForm.save();
            checkRefresh(pdm);
        }
    }

    public void editRelationship(PDMType pdm, RelationshipType rel) {
            initPdmEditorDialog();

            pdmEditorDialog.setTitle("PDM Editor - Edit Relationship");

            this.pdmEditorForm.setPDM(pdm, false);
            this.pdmEditorForm.selectInRelationshipTable(rel);
            this.pdmEditorForm.showEditRelationship();
//            this.pdmEditorForm.getRelForm().save();
            if (! pdmEditorForm.getRelForm().isOperationCanceled()) {
                this.pdmEditorForm.save();
                checkRefresh(pdm);
            }
        }


    // this is called from the popup
    public void addPDMNewRelationship(PDMType pdm) {
        initPdmEditorDialog();

        pdmEditorDialog.setTitle("PDM Editor - Add Relationship");

        this.pdmEditorForm.setPDM(pdm, false);
        this.pdmEditorForm.addRelationship();
        if (! pdmEditorForm.getRelForm().isOperationCanceled()) {
            this.pdmEditorForm.save();
            checkRefresh(pdm);
        }

    }

    public void deleteRelationship(PDMType pdm, RelationshipType rel) {
        initPdmEditorDialog();

        pdmEditorDialog.setTitle("PDM Editor - Delete Relationship");

        this.pdmEditorForm.setPDM(pdm, false);
        this.pdmEditorForm.selectInRelationshipTable(rel);
        this.pdmEditorForm.deleteRelationship();
        if (! pdmEditorForm.getRelForm().isOperationCanceled()) {
            this.pdmEditorForm.save();
            checkRefresh(pdm);
        }
    }

    public void deleteRole(PDMType pdm, RoleType role) {
        initPdmEditorDialog();

        pdmEditorDialog.setTitle("PDM Editor - Delete RolSalsae");

        this.pdmEditorForm.setPDM(pdm, false);
        this.pdmEditorForm.selectInRoleTable(role);
        this.pdmEditorForm.deleteRole();

        // todo - if a role is deleted, check and see if there are relationships that use this role
        // todo - impact of role deletion
        if (! pdmEditorForm.getRelForm().isOperationCanceled()) {
            this.pdmEditorForm.save();
            checkRefresh(pdm);
        }
    }

    public void executeSelectedPDM() {
        executeObject(this.globalPDMList.getSelectedPDM());
    }

    public void deleteSelectedPDM() {
        deleteObject(this.globalPDMList.getSelectedPDM());
    }

    public void editSelectedPDM() {
        editObject(this.globalPDMList.getSelectedPDM());
    }

    public void checkRefreshSelectedPDM() {
        checkRefresh(this.globalPDMList.getSelectedPDM());
    }

    public void addNewPDM() {
        addObject(null);
    }

    public void vertexSelected(AcreVertex v) {
        if (v instanceof PDMVertex) {
            editorToolBar.selectPDM();
        } else if (v instanceof RoleVertex) {
            editorToolBar.selectRole();
        }
    }

    public void edgeSelected(AcreEdge e) {
        if (e instanceof RoleToRoleEdge) {
            editorToolBar.selectRoleToRoleEdge();
        }
    }

    public void multipleSelected(Object[] graphObjects) {
        editorToolBar.setButtonsEnabled(false);
    }

    public void noneSelected() {
        editorToolBar.selectPDM();
    }


    private class ExecutePDMThread extends Thread {
        String pdmName;

        public ExecutePDMThread(String pdmName) {
            super();
            this.pdmName = pdmName;
        }

        public void run() {
            if (true) return;

//            setWaiting(true);
//            MainMDIFrame.showBusyProgress("Executing '" + pdmName + "'");
//            PDMExecutor pdmExecutor = null;
//            PatternResult patternResult=null;
//            GrappaPanel grappaPanel = null;
//            try {
//                pdmExecutor = new PDMExecutor();
//                pdmExecutor.setUp();
//                patternResult = pdmExecutor.execute(pdmName);
//
//                // remove Grappa Panel
//                removeGrappaPanel(pdmName);
//
//                // create a new Grappa Panel and replace it in the cache
//                grappaPanel = GrappaUtilities.createGrappaPanel(patternResult);
//                grappaPanel.setScaleToFit(true);
//                grappaPanel.setScaleToSize(new Dimension(DEFAULT_GRAPH_VIEW_WIDTH, DEFAULT_GRAPH_VIEW_HEIGHT));
//                grappaPanel.addGrappaListener(new GrappaAdapter());   // Required to enable tooltips etc.
//
//                if (grappaPanel != null) {
//                    // add new grappa panel to cache
//                    grappaCache.put(pdmName, grappaPanel);
//                }
//
//                queryResultsTab.setResultsGraphViewPort(grappaPanel);
//
//            } catch (Throwable t) {
//                logger.throwing(this.getClass().getName(), "executePDM", t);
//                MainMDIFrame.showMainError(t.getMessage(), "Execute PDM Error");
//            } finally {
//                MainMDIFrame.hideBusyProgress();
//                setWaiting(false);
//                if (pdmExecutor != null) {
//                    try {
//                        pdmExecutor.tearDown();
//                    } catch (Exception e) {
//                        logger.throwing(this.getClass().getName(), "executePDM", e);
//                    }
//                }
//            }
        }
    }

    public PDMEditorForm getPdmEditorForm() {
        return pdmEditorForm;
    }

    public void setPdmEditorForm(PDMEditorForm pdmEditorForm) {
        this.pdmEditorForm = pdmEditorForm;
    }

}
