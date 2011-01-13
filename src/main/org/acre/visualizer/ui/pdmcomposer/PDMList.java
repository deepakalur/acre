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
import org.acre.common.AcreStringUtil;
import org.acre.dao.DAOFactory;
import org.acre.dao.PatternRepository;
import org.acre.visualizer.ui.AcreUIConstants;
import org.acre.visualizer.ui.AcreUIUtils;
import org.acre.visualizer.ui.components.AbstractAcreList;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


/**
 * @author Deepak.Alur@Sun.com
 *         Date: Jun 18, 2004
 *         Time: 5:16:37 PM
 */

public class PDMList
        extends AbstractAcreList implements ActionListener {

    public PDMList() {
        super();
        initialize();
    }
    private PDMViewer parentPDMViewer = null;
    public PDMList(PDMViewer parentPDMViewer) {
        super();
        initialize();
        this.parentPDMViewer = parentPDMViewer;
    }

    protected ArrayList convertList(ArrayList list) {
        ArrayList l = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            Object data = list.get(i);
            if (data instanceof PDMType) {
                l.add(new PDMListItemData((PDMType) data));
            } else {
                l.add(data);
            }
        }
        return l;
    }

    protected void initialize() {
        setToolTipText("PDM List");
        ToolTipManager.sharedInstance().registerComponent(this);
        setSelectionForeground(AcreUIConstants.SELECTED_ROW_FG_COLOR);
        setSelectionBackground(AcreUIConstants.SELECTED_ROW_BG_COLOR);
        setVisibleRowCount(AcreUIConstants.QUERYLIST_VISIBLE_ROWS);
        //addListSelectionListener(this);
        setModel(new SalsaListModel());
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        addMouseListener(this);

        createPopupMenu();
        //addPopupActionListener(this);
    }

    public void setSelectedValue(String pdmName) {
        if (AcreStringUtil.isEmpty(pdmName))
            return;

        ListModel pdmListModel = getModel();
        for (int i=0; i < pdmListModel.getSize(); i++) {
            PDMListItemData data = (PDMListItemData) pdmListModel.getElementAt(i);
            if (pdmName.equals(data.getDetails().getName())) {
                setSelectedValue(data, true);
                return;
            }
        }
    }

    public class PDMListItemData {
        public PDMType getDetails() {
            return details;
        }

        public void setDetails(PDMType details) {
            this.details = details;
        }

        PDMType details;

        public PDMListItemData(PDMType details) {
            this.details= details;
        }

        public String toString() {
            return details.getName();
        }
    }

    public static void main(String args[]) {
        JFrame frame = new JFrame("PDM List");
        //PatternQueryRepository facade = PatternQueryRepository.getInstance("src/main");
        //List queries = facade.();

        PatternRepository facade = DAOFactory.getPatternRepository();

        PDMList panel=new PDMList();
//        panel = AcreUIUtils.createGlobalPDMList(facade);

        frame.getContentPane().add(panel);
        frame.setVisible(true);
        frame.pack();

    }

    public void actionPerformed(ActionEvent e) {
//        System.out.println("PDMList- Action Performed: " + e);
        if(e.getActionCommand().equals(AbstractAcreList.EXECUTE)) {
            PDMListItemData data = (PDMList.PDMListItemData ) getSelectedValue();
            parentPDMViewer.executePDMListItemData(data);
        }
    }

    // todo - used for debugging
    public void valueChanged(ListSelectionEvent e) {
        //System.out.println("PDMList: Value Changed Event:" + e);
        if (e.getSource() == this) {
            if (! e.getValueIsAdjusting()) {
//                System.out.println("Value Done Adjusting:" + e);
//                System.out.println("Selected Value :" + this.getSelectedValue());
            }
        }
    }

}
