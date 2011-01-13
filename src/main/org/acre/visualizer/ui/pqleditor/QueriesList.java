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
import org.acre.common.AcreStringUtil;
import org.acre.dao.DAOFactory;
import org.acre.dao.PatternQueryRepository;
import org.acre.visualizer.ui.AcreUIConstants;
import org.acre.visualizer.ui.components.AbstractAcreList;

import javax.swing.JFrame;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.ToolTipManager;
import javax.swing.event.ListSelectionEvent;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Deepak.Alur@Sun.com
 *         Date: Jun 18, 2004
 *         Time: 5:16:37 PM
 */

public class QueriesList
        extends AbstractAcreList { //implements ActionListener {

    public QueriesList() {
        super();
        initialize();
    }

    protected ArrayList convertList(ArrayList list) {
        ArrayList l = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            Object data = list.get(i);
            if (data instanceof QueryType) {
                l.add(new QueryListItemData((QueryType) data));
            } else {
                l.add(data);
            }
        }
        return l;
    }

    protected void initialize() {

        setToolTipText("Queries List");
        ToolTipManager.sharedInstance().registerComponent(this);
        setSelectionBackground(AcreUIConstants.SELECTED_ROW_BG_COLOR);
        setVisibleRowCount(AcreUIConstants.QUERYLIST_VISIBLE_ROWS);
        //addListSelectionListener(this);
        setModel(new SalsaListModel());
        setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        addMouseListener(this);

        createPopupMenu();
        //addPopupActionListener(this);
    }

    //todo - make this an abstract method in AbstractAcreList
    public void setSelectedValue(String queryName) {
        if (AcreStringUtil.isEmpty(queryName))
            return;

        ListModel queryListModel = getModel();
        for (int i=0; i < queryListModel.getSize(); i++) {
            QueryListItemData data = (QueryListItemData) queryListModel.getElementAt(i);
            if (queryName.equals(data.getQueryDetails().getName())) {
                setSelectedValue(data, true);
                return;
            }
        }
    }

    public class QueryListItemData {
        public QueryType getQueryDetails() {
            return queryDetails;
        }

        public void setQueryDetails(QueryType queryDetails) {
            this.queryDetails = queryDetails;
        }

        QueryType queryDetails;

        public QueryListItemData(QueryType queryDetails) {
            this.queryDetails = queryDetails;
        }

        public String toString() {
            return queryDetails.getName();
        }
    }

    public static void main(String args[]) {
        JFrame frame = new JFrame("Queries List");
        
        PatternQueryRepository patternQueryRepository = DAOFactory.getPatternQueryRepository();
        List queries = patternQueryRepository.getGlobalQueryList();

        QueriesList panel = new QueriesList();
        panel.setListData(new ArrayList(queries));
        frame.getContentPane().add(panel);
        frame.setVisible(true);
        frame.pack();

    }

//    public void actionPerformed(ActionEvent e) {
//        System.out.println("QueriesList - Action Performed: " + e);
//    }

    // todo - to use this, add ListSelectionListener to the implements 
    public void valueChanged(ListSelectionEvent e) {
//        System.out.println("QueryListPanel: Value Changed Event:" + e);
    }

}
