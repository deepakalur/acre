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
import org.acre.dao.DAOFactory;
import org.acre.server.AcreDelegate;
import org.acre.server.UserContextObject;
import org.acre.visualizer.ui.components.Editor;
import org.acre.visualizer.ui.components.ListTreeViewer;

import javax.swing.JFrame;
import javax.swing.event.ListSelectionEvent;
import java.awt.Color;
import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Deepak.Alur@Sun.com
 *         Date: Jun 22, 2004
 *         Time: 7:57:35 PM
 */
public class QueryViewer extends ListTreeViewer {

    AcreDelegate delegate;

    public QueryViewer(){
        super();
        initialize();
    }

    public QueryViewer(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
        initialize();
    }

    public QueryViewer(LayoutManager layout) {
        super(layout);
        initialize();
    }

    public QueryViewer(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
        initialize();
    }

    public void setDelegate( AcreDelegate delegate ) {
        this.delegate = delegate;
    }
    public void initialize(){
        super.initialize();

        QueriesList ql = new QueriesList();
        ql.addListSelectionListener(this);
        ql.addPopupActionListener(this);
        QueryTreePanel qt = new QueryTreePanel();
        qt.setBackground(Color.lightGray);

        ql.setListData(new ArrayList());
        setList(ql);
        setTree(qt);

        //patternQueryRepository = DAOFactory.getPatternQueryRepository();
    }

    //private PatternQueryRepository patternQueryRepository;

    public void addActionPerformed() {
        if (editor != null) {
           editor.addObject(null); // create a new query
        }
    }

    public void editActionPerformed() {
        if (editor != null) {
            QueryType editQuery = getSelectedQuery();

            editor.editObject(editQuery);
        }
    }

    public void deleteActionPerformed() {

        if (editor != null) {
            QueryType delQuery = getSelectedQuery();

            editor.deleteObject(delQuery);
        }
    }

    public void executeActionPerformed() {
        QueryType execQuery = getSelectedQuery();
        editor.executeObject(execQuery);

    }

    public void refreshActionPerformed() {
        editor.refreshList();
    }

    public static void main(String[] args) {
        //PDMDAOFacade
        AcreDelegate delegate = new AcreDelegate( new UserContextObject( "user", "pass"));
        //PatternQueryRepository patternQueryRepository = DAOFactory.getPatternQueryRepository();
        //System.out.println(patternQueryRepository);
        //List l = patternQueryRepository.getGlobalQueryList();
        List l = null;
        try {
            l = delegate.getGlobalPatternQueryModels();
        } catch (AcreException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        QueryViewer v = new QueryViewer();
        v.setDelegate( delegate );
        MyEditor editor = new MyEditor();
        editor.setViewer(v);
        v.setEditor(editor);


        v.getList().setListData(new ArrayList(l));

        //v.setTree(qt);

        JFrame f = new JFrame();
        f.getContentPane().add(v, java.awt.BorderLayout.CENTER);

        f.pack();
        f.setVisible(true);

    }

    public void valueChanged(ListSelectionEvent e) {

        if (e.getSource() == getList()) {

            if (! e.getValueIsAdjusting()) {
                QueriesList.QueryListItemData data = (QueriesList.QueryListItemData) getList().getSelectedValue();
                if (data == null)
                    return;

                QueryType type = data.getQueryDetails();
                ArrayList l = new ArrayList();
                l.add(type);
                QueryTreePanel tree = (QueryTreePanel) this.getTree();
                tree.setDataList(l);
                tree.setExpandAllAlways(true);
                if (editor!=null) {
                    //QueryType editQuery = patternQueryRepository.getGlobalQuery(type.getName());
                    QueryType editQuery = null;
                    try {
                        editQuery = delegate.getGlobalQuery(type.getName());
                        editor.viewObject(editQuery);
                    } catch (AcreException e1) {
                        e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
            }
        }
    }

    public QueryType getSelectedQuery() {
        QueriesList.QueryListItemData data =
                (QueriesList.QueryListItemData) getList().getSelectedValue();

        if (data == null)
            return null;

        QueryType type = data.getQueryDetails();
        //QueryType selQuery = patternQueryRepository.getGlobalQuery(type.getName());
        QueryType selQuery = null;
        try {
            selQuery = delegate.getGlobalQuery(type.getName());
        } catch (AcreException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return selQuery;
    }

    public QueryType [] getSelectedQueries() {
        Object[] queries = getList().getSelectedValues();

        QueryType [] queriesArray;
        if (queries == null)
            return new QueryType[0];

        queriesArray = new QueryType[queries.length];

        for (int i=0; i < queries.length; i++) {
            QueriesList.QueryListItemData data =
                    (QueriesList.QueryListItemData) queries[i];
            queriesArray[i] = data.getQueryDetails();
        }
        return queriesArray;
    }

    private static class MyEditor implements Editor {

        QueryViewer v;

        public void setViewer(QueryViewer v) {
            this.v = v;
        }

        public QueryViewer getViewer() {
            return v;
        }

        // Make the Query Panel implement this interface and plug it into the QueryViewer
        public void viewObject(Object value) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void deleteObject(Object key) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void addObject(Object info) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void executeObject(Object key) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void editObject(Object value) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void clear() {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void refreshList() {
            List l = DAOFactory.getPatternQueryRepository().getGlobalQueryList();
            getViewer().getList().setListData(
                new ArrayList(l));
            QueriesList ql = (QueriesList) getViewer().getList();
            ql.setSelectedValue("Singleton");
        }

    }
}
