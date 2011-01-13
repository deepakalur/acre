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
import org.acre.dao.DAOFactory;
import org.acre.dao.PatternRepository;
import org.acre.visualizer.ui.components.ADREActionButtonConstants;
import org.acre.visualizer.ui.components.ListTreeViewer;

import javax.swing.JFrame;
import javax.swing.event.ListSelectionEvent;
import java.awt.Color;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Deepak.Alur@Sun.com
 *         Date: Jun 23, 2004
 *         Time: 4:01:13 PM
 */

public class PDMViewer extends ListTreeViewer {

    public PDMViewer() {
        super();
        initialize();
    }

    public PDMViewer(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
        initialize();
    }

    public PDMViewer(LayoutManager layout) {
        super(layout);
        initialize();
    }

    public PDMViewer(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
        initialize();
    }

    public void initialize() {
        super.initialize();
        PDMList ql = new PDMList(this);
        ql.addListSelectionListener(this);
        ql.addPopupActionListener(this);        
        PDMTreePanel qt = new PDMTreePanel();
        qt.setBackground(Color.lightGray);

        ql.setListData(new ArrayList());
        setList(ql);
        setTree(qt);
    }

    public static void main(String[] args) {
        //PDMDAOFacade

        PatternRepository facade = DAOFactory.getPatternRepository();

        System.out.println(facade);

        List l = facade.getGlobalPatternModels();
        PDMViewer v = new PDMViewer();

        v.getList().setListData(new ArrayList(l));

        JFrame f = new JFrame();
        f.getContentPane().add(v, java.awt.BorderLayout.CENTER);

        f.pack();
        f.setVisible(true);

    }

    public void valueChanged(ListSelectionEvent e) {

        if (e.getSource() == getList()) {
            if (!e.getValueIsAdjusting()) {
//                System.out.println("PDMViewer: List Value Changed" + e);
                PDMList.PDMListItemData data = (PDMList.PDMListItemData) getList().getSelectedValue();

                if (data == null)
                    return;

                PDMType type = data.getDetails();

                ArrayList l = new ArrayList();
                l.add(type);
                PDMTreePanel tree = (PDMTreePanel) this.getTree();
                tree.setDataList(l);
                tree.setExpandAllAlways(true);
                if (editor != null) {
//                    System.out.println("PDMViewer: Setting Edit Object");

                    editor.viewObject(DAOFactory.getPatternRepository()
                            .getGlobalPatternModel(type.getName()));
                }
            }
        }
    }


    public void actionPerformed(ActionEvent e) {
        if (ADREActionButtonConstants.EXECUTEACTION.equals(e.getActionCommand())) {
            PDMList.PDMListItemData data = (PDMList.PDMListItemData) getList().getSelectedValue();
            executePDMListItemData(data);
        } else {
            super.actionPerformed(e);
            // todo delete action here
        }
    }

    public void addActionPerformed() {
        if (editor != null) {
            editor.addObject(null); // create a new query
        }
    }

    public void editActionPerformed() {
        if (editor != null) {
            // get item selected and proceed
            PDMType editPDM = getSelectedPDM();

            editor.editObject(editPDM);
        }
    }

    public void deleteActionPerformed() {

        if (editor != null) {
            // get item selected and proceed
            PDMType delPDM = getSelectedPDM();

            editor.deleteObject(delPDM);
        }
    }

    public void executeActionPerformed() {
        // get item selected and proceed
        PDMType execPDM = getSelectedPDM();
        editor.executeObject(execPDM);

    }

    public void refreshActionPerformed() {
        // todo - refresh PDM
    }

    public void executePDMListItemData(PDMList.PDMListItemData data) {
        PDMType type = data.getDetails();
//        executePDM(type);
        editor.executeObject(type);
    }


    public PDMType getSelectedPDM() {
        PDMList.PDMListItemData data =
                (PDMList.PDMListItemData) getList().getSelectedValue();

        if (data == null)
            return null;

        PDMType type = data.getDetails();
        PDMType selQuery = DAOFactory.getPatternRepository().getGlobalPatternModel(type.getName());
        return selQuery;

    }

    public void setSelectedPDM(String pdmName) {
        this.getList().setSelectedValue(pdmName);        
    }
}
