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
package org.acre.visualizer.ui.pqleditor;

import org.acre.pdmqueries.QueryType;
import org.acre.common.AcreException;
import org.acre.common.AcreRuntimeException;
import org.acre.config.ConfigData;
import org.acre.config.ConfigService;
import org.acre.dao.PDMXMLConstants;
import org.acre.dao.PQLConnectionManager;
import org.acre.lang.runtime.PQL;
import org.acre.pdmengine.ScriptEngineFacade;
import org.acre.server.AcreDelegate;
import org.acre.visualizer.ui.GlobalSettings;
import org.acre.visualizer.ui.Main;
import org.acre.visualizer.ui.codehighlight.GroovyScanner;
import org.acre.visualizer.ui.codehighlight.PQLScanner;
import org.acre.visualizer.ui.codehighlight.SyntaxHighlighter;
import org.acre.visualizer.ui.components.AbstractAcreList;
import org.acre.visualizer.ui.components.Editor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

/**
 * @author DeepakAlur@Sun.com
 * @author Yury Kamen
 *
 */
public class AcreQueryPanel extends javax.swing.JPanel implements ActionListener, Editor {
    private static final String VALIDATE = "Validate Query";
    private static final String EXECUTE = "Execute Query";
    private static final String CLEAR = "Clear All";
    private static final String SAVE = "Save Changes";
    private static final String SAVE_NEW = "Save New";
    private static final String SHOW_TABLE = "Show Table";
    private static final String SHOW_TEXT = "Show Text";
    private static final String CANCEL = "Cancel";
    private int dividerSize = GlobalSettings.getDividerSize();
    private int dividerLocation = GlobalSettings.getVerticalDividerLocation();
    private Hashtable pqlResultMap = new Hashtable();

    //JComboBox database;
    JTextField queryName;
    SyntaxHighlighter queryText;
    PQLComboPane queryResultsTab;
    JButton validateButton;
    JButton executeButton;
    JButton clearButton;
    JButton saveButton;
    JToggleButton showHideResultsButton;

    JTabbedPane queryListTab;
    QueryViewer globalQueriesList;
    QueryViewer projectQueriesList;
    PQLEditorForm pqlEditorForm;
    JDialog pqlEditorDialog;

    AcreDelegate delegate;
    public void addNewQuery() {
        addObject(null);
    }

    public void editSelectedQuery() {
        editObject(this.globalQueriesList.getSelectedQuery());

    }

    public void deleteSelectedQuery() {
        deleteObject(this.globalQueriesList.getSelectedQuery());
    }

    public void checkRefreshSelectedQuery() {
        refreshList();
    }

    public void validateSelectedQuery() {
        validateObject(this.globalQueriesList.getSelectedQuery());
    }

    public void executeSelectedQuery() {
        executeObject(this.globalQueriesList.getSelectedQuery());
    }

    public void makePDM() {
        QueryType [] selectedQueries = this.globalQueriesList.getSelectedQueries();
        if ((selectedQueries == null) || (selectedQueries.length == 0)) {
            Main.showMainError(
                    "Select one or more query from the list\n" +
                    "to make a new PDM",
                    "Make PDM Error");
        }

        initMakePDMForm();

        if (selectedQueries.length == 1) {
            // only one query selected

            QueryType query = selectedQueries[0];
            String queryName = query.getName();

            makePDMForm.setPdmName(queryName);
            makePDMForm.showMe();

            if (makePDMForm.isCanceled()) {
                return;
            }

            String pdmName = makePDMForm.getPdmName();
            try {
                delegate.createPatternModelFromQuery(pdmName, queryName);
            } catch (AcreException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            Main.showMainInfo("Created Pattern Model '"
                    + pdmName
                    + "'",
                    "Make Pattern Model");
        } else {

            Date date = new Date(System.currentTimeMillis());
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            String d =
                     "" +
                    c.get(Calendar.DAY_OF_MONTH) + "-" +
                    (c.get(Calendar.MONTH) + 1) + "-" +
                    c.get(Calendar.HOUR_OF_DAY) + ":" +
                    c.get(Calendar.MINUTE) +  ":" +
                    c.get(Calendar.SECOND);

            String pdmName = "AutoPDM-" + d;

            makePDMForm.setPdmName("");
            makePDMForm.showMe();

            if (makePDMForm.isCanceled()) {
                return;
            }

            pdmName = makePDMForm.getPdmName();

            try {
                delegate.createPatternModelFromQueries(pdmName, selectedQueries);
            } catch (AcreException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            Main.showMainInfo("Created Pattern Model '"
                    + pdmName
                    + "'",
                    "Make Pattern Model");
        }
    }

    private static class ResultReference {
        Object referent;

        public ResultReference(Object referent) {
            this.referent = referent;
        }

        public Object get() {
            return referent;
        }
    }

    private static final Cursor waitCursor = new Cursor(Cursor.WAIT_CURSOR);
    private java.lang.Thread runningThread = null;
    private Cursor fMainCursor;
    private Cursor queryTextCursor;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private QueryType currentEditQuery = null;
    private JFrame parentFrame;
    private JOptionPane confirmYesNoOptionPane;
    private JDialog confirmYesNoDialog;
    private JDialog makePDMFormDialog;
    private MakePDMForm makePDMForm;

    public AcreQueryPanel(AcreDelegate delegate) {
        super();
        this.delegate = delegate;
        initialize();
    }

    public AcreQueryPanel(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
        initialize();
    }

    public AcreQueryPanel(LayoutManager layout) {
        super(layout);
        initialize();
    }

    public AcreQueryPanel(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
        initialize();
    }

    public void databaseLoaded() {
        if (true) {
            return; // hack
        }

        queryText.setEditable(true);
        queryText.setBackground(Color.white);
        executeButton.setEnabled(true);
        clearButton.setEnabled(true);
        saveButton.setEnabled(true);
    }


    public void initialize() {

        queryListTab = new JTabbedPane();

        globalQueriesList = new QueryViewer();
        globalQueriesList.setEditor(this);
        globalQueriesList.setDelegate( delegate );
        projectQueriesList = new QueryViewer();
        projectQueriesList.setEditor(this);
        projectQueriesList.setDelegate( delegate );
        
        queryListTab.addTab("Global Queries", globalQueriesList);
        //queryListTab.addTab("Project Queries", projectQueriesList);

        leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.add(queryListTab);

        queryName = new JTextField();
        //queryName.setEditable(false);
        queryName.setColumns(30);

        //        queryText = new JTextPane();
        //        CodeDocument doc = new CodeDocument();
        //        queryText.setDocument(doc);
        queryText = new SyntaxHighlighter(24, 80, new PQLScanner());
        JScrollPane queryScroll = new JScrollPane(queryText);
        queryScroll.setBorder(BorderFactory.createTitledBorder("Query Script"));

        validateButton = new JButton();
        validateButton.setActionCommand(VALIDATE);
        validateButton.setMnemonic('V');
        validateButton.setIcon(new javax.swing.ImageIcon(getClass()
                                .getResource("/org/acre/visualizer/ui/icons/buttons/ValidateButton.gif")));
        validateButton.setToolTipText("Validate Selected Query");
        validateButton.setBorderPainted(false);
        validateButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        validateButton.setRolloverEnabled(true);
        validateButton.addActionListener(this);

        executeButton = new JButton();
        executeButton.setActionCommand(EXECUTE);
        executeButton.setMnemonic('X');
        executeButton.setIcon(new javax.swing.ImageIcon(getClass()
                                .getResource("/org/acre/visualizer/ui/icons/buttons/ExecuteButton.gif")));
        executeButton.setToolTipText("Execute Selected Query");
        executeButton.setBorderPainted(false);
        executeButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        executeButton.setRolloverEnabled(true);
        executeButton.addActionListener(this);


        clearButton = new JButton();
        clearButton.setActionCommand(CLEAR);
        clearButton.setMnemonic('C');
        clearButton.setIcon(new javax.swing.ImageIcon(getClass()
                                .getResource("/org/acre/visualizer/ui/icons/buttons/ClearAllButton.gif")));
        clearButton.setToolTipText("Clear Query");
        clearButton.setBorderPainted(false);
        clearButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        clearButton.setRolloverEnabled(true);
        clearButton.addActionListener(this);

        saveButton = new JButton();
        saveButton.setActionCommand(SAVE_NEW);
        saveButton.setMnemonic('S');
        saveButton.setIcon(new javax.swing.ImageIcon(getClass()
                                .getResource("/org/acre/visualizer/ui/icons/buttons/SaveNewButton.gif")));
        saveButton.setToolTipText("Save as New Query");
        saveButton.setBorderPainted(false);
        saveButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        saveButton.setRolloverEnabled(true);
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

        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        //buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(clearButton);
        buttonPanel.add(validateButton);
        buttonPanel.add(executeButton);
        buttonPanel.add(saveButton);

        rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());

        rightPanel.add(queryScroll, BorderLayout.CENTER);
        rightPanel.add(buttonPanel, BorderLayout.SOUTH);

        JSplitPane nsSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, textPanel, rightPanel);
//        nsSplitPane.setDividerSize(dividerSize);
        nsSplitPane.setDividerLocation(dividerLocation);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        JSplitPane ewSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, nsSplitPane);
//        ewSplitPane.setDividerSize(dividerSize);
        ewSplitPane.setDividerLocation(GlobalSettings.getHorizontalDividerLocation());

        // disable until PQL is available
        //queryText.setEditable(false);
        //queryText.setBackground(Color.lightGray);
        //executeButton.setEnabled(false);
        //clearButton.setEnabled(false);
        //saveButton.setEnabled(false);

        PQLExplorerToolBar toolbar = new PQLExplorerToolBar();
        toolbar.setQueryPanel(this);

        this.setLayout(new BorderLayout());
        //        SalsaQueryActionPanel actionPanel = new SalsaQueryActionPanel();
        //        this.add(actionPanel, BorderLayout.NORTH);
        JPanel toolBarPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        toolBarPanel.add(toolbar);
        this.add(toolBarPanel, BorderLayout.NORTH);
        this.add(ewSplitPane, BorderLayout.CENTER);

    }

    private void initMakePDMForm() {
        if (makePDMFormDialog == null) {
            if (getParentFrame() != null) {
                makePDMFormDialog = new JDialog(getParentFrame(), "Make PDM From Queries", true);
                makePDMFormDialog.setLocationRelativeTo(getParentFrame());
            } else {
                makePDMFormDialog = new JDialog();
                makePDMFormDialog.setModal(true);
                makePDMFormDialog.setTitle("Make PDM From Queries");
                makePDMFormDialog.setLocationRelativeTo(this);
            }
            makePDMForm = new MakePDMForm();
            makePDMFormDialog.getContentPane().add(makePDMForm);
            makePDMFormDialog.pack();
            makePDMForm.setDialog(makePDMFormDialog);
        }
    }

    public void setGlobalQueriesList(List queries) {
        AbstractAcreList l = this.globalQueriesList.getList();
        if( l != null ){
            l.setListData(new ArrayList(queries));
        }
    }

    public static void main(String args[]) {
        JFrame f = new JFrame();
        AcreDelegate delegate = new AcreDelegate( "user", "pass");
        //PatternQueryRepository patternQueryRepository = DAOFactory.getPatternQueryRepository();   // Yury Kamen: hack
        List queries = null;                             // Yury Kamen: hack
        try {
            queries = delegate.getGlobalQueryList();
        } catch (AcreException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        //        PatternQueryRepository patternQueryRepository = PatternQueryRepository.getInstance();//"src/main");   // Yury Kamen: hack
        //        List queries = patternQueryRepository.getGlobalQueryList();                                  // Yury Kamen: hack

        final AcreQueryPanel panel = new AcreQueryPanel(delegate);

        panel.setGlobalQueriesList(queries);                                    // Yury Kamen: hack
        f.getContentPane().add(panel);
        new Thread(new Runnable() {
            public void run() {
                //                panel.setPql(new PQL("PSA/factDatabase.ta"));
            }
        }).start();

        f.pack();
        f.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        //        System.out.println("AcreQueryPanel : Action = " + e);

        if (e.getSource() instanceof JButton) {
            if (e.getActionCommand().equals(VALIDATE)) {
                validatePQL();
            } else if (e.getActionCommand().equals(EXECUTE)) {
                //                System.out.println("Execute query:" + queryText.getText());
                execute();
            } else if (e.getActionCommand().equals(SAVE)) {
                //                System.out.println("Save query:" + queryText.getText());
                saveQuery();
            } else if (e.getActionCommand().equals(SAVE_NEW)) {
                //                System.out.println("Save query:" + queryText.getText());
                saveQuery();
            } else if (e.getActionCommand().equals(CLEAR)) {
                //                System.out.println("Clear all");
                clear();
            }
            if (e.getActionCommand().equals(CANCEL)) {
                cancelExecute();
            }
        }
        if (e.getSource() instanceof JComboBox) {
            // todo : select database here...
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

    private void saveQuery() {
        //        if (AcreStringUtil.isEmpty(queryText.getText())) {
        //            MainMDIFrame.showMainError("No query entered, cannot save script.",
        //                    "Save Script Error");
        //            return;
        //        }
        try {
            if (globalQueriesList.getSelectedQuery() == null) {
                // new query
                initPQLEditorForm();
                QueryType newQuery = delegate.createNewQuery();
                String queryTextStr = queryText.getText();
                pqlEditorForm.setQueryScript(queryText.getText());
                showQueryEditor(newQuery, true);
            } else {
                delegate.saveGlobalScript(globalQueriesList.getSelectedQuery(),queryText.getText());
            }
        } catch (Throwable t) {
            Main.showMainError("Failed to save script for Query '"
                            + globalQueriesList.getSelectedQuery().getName()
                            + "'", "Save Script Error");
        }
    }

    //////////////////////////// Yury Kamen: executing queries //////////////////////////
    private void execute() {
        if (runningThread != null && runningThread.isAlive()) {
            Toolkit.getDefaultToolkit().beep();
            return;
        }
        runningThread = new ExecuteThread();
        runningThread.start();
    }

    private void cancelExecute() {
        if ((runningThread != null) && (runningThread.isAlive())) {
            //runningThread.interrupt();
            // todo - cancel the execution and return
        }
    }

    //////////////////////////// Ali: validating queries //////////////////////////
    private void validatePQL() {
        if (runningThread != null && runningThread.isAlive()) {
            Toolkit.getDefaultToolkit().beep();
            return;
        }
        runningThread = new ExecuteThread(true);
        runningThread.start();
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

    /**
     * Shows recent command history
     *
     * @param sCmd
     */
    private void addToRecent(String sCmd) {
        // TODO: not implemented yet
    }

    private String prependSystemVersion(String sCmd) {
        ConfigData configData = ConfigService.getInstance().getConfigData();
        if ((configData.getAcreDefaultSystem() != null) &&
            (configData.getAcreDefaultVersion() != null)) {
            StringBuffer sBuf = new StringBuffer();
            sBuf.append("define @system as ");
            sBuf.append("\"");
            sBuf.append(configData.getAcreDefaultSystem());
            sBuf.append("\";\n");
            sBuf.append("define @version as ");
            sBuf.append("\"");
            sBuf.append(configData.getAcreDefaultVersion());
            sBuf.append("\";\n");
            sBuf.append(sCmd);
            return sBuf.toString();
        } else {
            return sCmd;
        }
    }

    /**
     * Hack to prepend return to simple queries
     *
     * @param sCmd
     * @return
     */
    private String fakeReturnStatement(String sCmd) {
        String s = sCmd.toLowerCase().trim();
        if (s.indexOf("return") < 0 && s.startsWith("select") && s.indexOf(';') == s.lastIndexOf(';')) {
            return "RETURN " + sCmd;
        }
        return sCmd;
    }

    public void setParentFrame(JFrame parentFrame) {
        this.parentFrame = parentFrame;
    }

    public JFrame getParentFrame() {
        return parentFrame;
    }

    private class ExecuteThread extends Thread {
        private boolean validating;

        public ExecuteThread() {
            this(false);
        }

        public ExecuteThread(boolean validating) {
            this.validating = validating;
        }

        public void run() {

            setWaiting(true);
            queryResultsTab.clear();
            if (validating) {
                validateButton.setEnabled(false);
            } else {
                executeButton.setEnabled(false);
            }

            showMainStatus("Started PQL query, please wait ...", true);
            Main.showBusyProgress(validating ? "Validating" : "Executing" + " PQL Query...");

            String sCmd = queryText.getText();

            ConfigData configData = ConfigService.getInstance().getConfigData();
            configData.getAcreDefaultSystem();
            configData.getAcreDefaultVersion();

            Map res = null;
            QueryType query = globalQueriesList.getSelectedQuery();
            try {
                if ((sCmd != null) && (sCmd.trim().length() != 0)) {
                    if (validating) {
                        if (PDMXMLConstants.QUERY_LANGUAGE_GROOVY.equalsIgnoreCase(query.getLanguage())) {
                            // TODO: Not implemented yet
                            Main.showMainInfo("Groovy Query Validation is not implemented yet!", "Validate Groovy");
                        } else {
                            PQL.validatePQL(sCmd);
                            Main.showMainInfo("Completed PQL Validation.\nNo errors were found.", "Validate PQL");
                        }
                    } else {
                        ResultReference ref = (ResultReference) pqlResultMap.get(sCmd);
                        if (null != ref) {
                            res = (Map) ref.get();
                        }
                        if (null == res) {
                            if (query != null && PDMXMLConstants.QUERY_LANGUAGE_GROOVY.equalsIgnoreCase(query.getLanguage())) {
                                res = ScriptEngineFacade.executeGroovyString(
                                        sCmd, new HashMap(), PQLConnectionManager.getInstance().getGlobalConnection()
                                );
                            } else {
                                String cmdWithSystemVersion = prependSystemVersion(sCmd);
                                res = PQLConnectionManager.execute(fakeReturnStatement(cmdWithSystemVersion));
                                
                            }

                            if (null != res) {
                                pqlResultMap.put(sCmd, new ResultReference(res));
                            }
                        }
                    }
                }
            } catch (Throwable e) {
                if (e instanceof InterruptedException) {
                    // cancel PQL Execution
                    PQLConnectionManager.cancelExecute();
                    return;
                }
                Main.showMainError("Error Message:" +
                                e.getMessage(), "Error " + (validating ? "Validating" : "Executing") + " PQL");
                e.printStackTrace();
            } finally {
                setWaiting(false);
                if (validating) {
                    validateButton.setEnabled(true);
                } else {
                    executeButton.setEnabled(true);
                }
            }

            Main.hideBusyProgress();

            if (!validating) {
                queryResultsTab.setData(res);
                showMainStatus("Finished PQL query", true);
                addToRecent(sCmd);
            }

            // Call with invokeLater because these commands change the gui.
            // Do not want to be updating the gui outside of the AWT event
            // thread.
            SwingUtilities.invokeLater(new Thread() {
                public void run() {
                    updateResult();
                    //System.gc();
                    setWaiting(false);
                }
            });
        }

    }


    private void showMainStatus(String statusMessage, boolean waitFlag) {
        if (Main.getMDIFrame() != null) {
            Main.getMDIFrame().showStatus(statusMessage, waitFlag);
        }
    }

    private void updateResult() {
        queryResultsTab.updateResult();
        //        queryText.selectAll();
        queryText.requestFocus();
    }

    // Make the Query Panel implement this interface and plug it into the QueryViewer
    public void viewObject(Object queryType) {

        if (queryType == null) {
            Main.showMainError("Attempt to view 'null' query", "View Query");
            return;
        }

        if (queryResultsTab != null) {
            queryResultsTab.setData(null);
            updateResult();
        }
        currentEditQuery = (QueryType) queryType;
        String queryStatement = null;
        try {
            queryStatement = delegate.getGlobalQueryFile(currentEditQuery.getName()).trim();
        } catch (AcreException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        this.globalQueriesList.getList().setSelectedValue(currentEditQuery.getName());
        if (PDMXMLConstants.QUERY_LANGUAGE_GROOVY.equalsIgnoreCase(currentEditQuery.getLanguage())) {
            queryText.getScanner().setSymbolTable(new GroovyScanner().getSymbolTable());
            queryText.getScanner().setCaseInsensitiveKeywords(false);
        } else {
            queryText.getScanner().setSymbolTable(new PQLScanner().getSymbolTable());
            queryText.getScanner().setCaseInsensitiveKeywords(true);
        }

        this.queryText.setText(queryStatement);
        saveButton.setActionCommand(SAVE);
        saveButton.setIcon(new javax.swing.ImageIcon(getClass()
                                .getResource("/org/acre/visualizer/ui/icons/buttons/SaveButton.gif")));
        //saveButton.setText(SAVE);
        ResultReference ref = (ResultReference) pqlResultMap.get(queryStatement);
        if (null != ref) {
            Map res = (Map) ref.get();
            if (null != res) {
                execute();
                //                queryResultsTab.setData(res);
                //                queryResultsTab.updateResult();
            }
        }
    }

    public void deleteObject(Object key) {
        if (key == null) {
            Main.showMainError("Cannot delete 'null' query", "Delete Query");
            return;
        }

        if (key instanceof QueryType) {
            // todo delete query
            boolean confirm = Main.showMainConfirm(this,
                    "Do you really want to delete query '" + ((QueryType) key).getName()
                            + "' ?\n", "Delete Query");

            if (confirm) {
                try {
                    if (delegate.deleteGlobalQuery((QueryType) key)) {
                        Main.showMainInfo("Deleted query '" + ((QueryType) key).getName()
                                        + "'", "Delete Query");
                    }
                } catch (Throwable t) {
                    Main.showMainError(t.getMessage(), "Delete Query Failed");
                }
            }

        } else {
            Main.showMainError("Attempt to delete unknown object type" +
                            key, "Delete Query");
        }
        //clear editor
        clear();

        // refresh list
        refreshGlobalQueriesList();
    }

    public void addObject(Object info) {
        // todo - info is not used for now...
        //QueryType newQuery = PatternQueryRepository.getInstance().createNewAndInsertGlobalQuery();
        try {
            QueryType newQuery = null;
            try {
                newQuery = delegate.createNewQuery();
            } catch (AcreException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            showQueryEditor(newQuery, true);
        } catch (AcreRuntimeException t) {
            Main.showMainError(t.getErrorMessages(), "Error - Add New Query");
        }
    }

    private void showQueryEditor(QueryType query, boolean isNewQuery) {
        initPQLEditorForm();
        if (isNewQuery) {
            pqlEditorDialog.setTitle("PQL Editor - Add New Query");
        } else {
            pqlEditorDialog.setTitle("PQL Editor - Edit Query");
        }

        this.pqlEditorForm.setQuery(query, isNewQuery);
        pqlEditorForm.showMe();

        // refresh the list of queries
        refreshGlobalQueriesList();

        // re-select the edited query in the list
        this.globalQueriesList.getList().setSelectedValue(query.getName());
    }

    private void initPQLEditorForm() {
        if (pqlEditorForm == null) {
            pqlEditorForm = new PQLEditorForm();
            pqlEditorForm.setParentFrame(getParentFrame());
            pqlEditorDialog = new JDialog(getParentFrame(), true);
            pqlEditorDialog.setContentPane(pqlEditorForm);
            pqlEditorDialog.pack();
            if (getParentFrame() != null) {
                pqlEditorDialog.setLocationRelativeTo(getParentFrame());
            }
            pqlEditorForm.setDialog(pqlEditorDialog);
        }
    }


    private void refreshGlobalQueriesList() {

        QueryType q = globalQueriesList.getSelectedQuery();

        // force reload the list of queries from the repository
        // this will pick up any new files created/deposited by the user
        // manually outside of SALSA UI
        try {
            delegate.loadGlobalQueries();
        } catch (AcreException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        List refreshQueries = null;
        try {
            refreshQueries = delegate.getGlobalQueryList();
        } catch (AcreException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        setGlobalQueriesList(refreshQueries);
        if (q != null) {
            globalQueriesList.getList().setSelectedValue(q.getName());
        } else {
            globalQueriesList.getList().setSelectedIndex(-1);
        }
    }

    public void validateObject(Object key) {
        if (key == null) {
            Main.showMainError("Cannot validate 'null' query", "Validate Query");
            return;
        }

        if (key instanceof QueryType) {
            QueryType valueQuery = (QueryType) key;
            QueryType execQuery = null;
            try {
                execQuery = delegate.getGlobalQuery(valueQuery.getName());
            } catch (AcreException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            viewObject(execQuery);
            validatePQL();
        } else {
            Main.showMainError("Attempt to Validate unknown object type" +
                            key, "Validate Query");
        }
    }

    public void executeObject(Object key) {
        if (key == null) {
            Main.showMainError("Cannot execute 'null' query", "Execute Query");
            return;
        }

        if (key instanceof QueryType) {
            QueryType valueQuery = (QueryType) key;
            QueryType execQuery = null;
            try {
                execQuery = delegate.getGlobalQuery(valueQuery.getName());
            } catch (AcreException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            viewObject(execQuery);
            execute();
        } else {
            Main.showMainError("Attempt to Execute unknown object type" +
                            key, "Execute Query");
        }
    }

    public void editObject(Object value) {
        if (value == null) {
            Main.showMainError("Query not selected from the list.\nPlease selecte a query and retry.", "Edit Query");
            return;
        }
        if (value instanceof QueryType) {
            QueryType valueQuery = (QueryType) value;
            QueryType editQuery = null;
            try {
                editQuery = delegate.getGlobalQuery(valueQuery.getName());
            } catch (AcreException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            saveButton.setActionCommand(SAVE);
            saveButton.setIcon(new javax.swing.ImageIcon(getClass()
                                    .getResource("/org/acre/visualizer/ui/icons/buttons/SaveButton.gif")));

            showQueryEditor(editQuery, false);
        } else {
            Main.showMainError("Attempt to edit unknown object type" +
                            value, "Edit Query");
        }


    }

    public void clear() {
        if (queryResultsTab != null) {
            queryResultsTab.setData(null);
        }
        queryText.setText("");
        if (globalQueriesList != null) {
            globalQueriesList.getList().clearSelection();
            //globalQueriesList.getList().setSelectedIndex(-1);
        }
        if (pqlEditorForm != null) {
            pqlEditorForm.clear();
        }

        saveButton.setActionCommand(SAVE_NEW);
        saveButton.setIcon(new javax.swing.ImageIcon(getClass()
                                .getResource("/org/acre/visualizer/ui/icons/buttons/SaveNewButton.gif")));
    }

    public void refreshList() {
        refreshGlobalQueriesList();
    }

}
