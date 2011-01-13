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
package org.acre.visualizer.ui.components;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * @author Deepak.Alur@Sun.com
 *         Date: Jun 21, 2004
 *         Time: 11:05:06 PM
 */
public abstract class AbstractAcreList extends JList
        implements MouseListener {

    public static final String ADD = ADREActionButtonConstants.ADDACTION;
    public static final String EDIT = ADREActionButtonConstants.EDITACTION;
    public static final String EXECUTE = ADREActionButtonConstants.EXECUTEACTION;
    public static final String DELETE = ADREActionButtonConstants.DELETEACTION;
    public static final String REFRESH = ADREActionButtonConstants.REFRESHACTION;

    JPopupMenu popup;
    Object[] popupItems;

    public AbstractAcreList() {
        super();
    }

    /**
     * this must be a list of QueryType objects obtained from the
     * QUery Mappings file
     *
     * @param list
     */
    public void setListData(ArrayList list) {
        ArrayList newList = convertList(list);
        this.setModel(new SalsaListModel(newList));        
    }

    protected abstract ArrayList convertList(ArrayList list);

    protected abstract void initialize();

    public abstract void setSelectedValue(String itemName) ;

    protected void createPopupMenu() {
        popup = new JPopupMenu("SalsaList");
        ArrayList popups = new ArrayList();

        JMenuItem item = null;
        item = new JMenuItem(ADD);
        popup.add(item);
        popups.add(item);

        item = new JMenuItem(EDIT);
        popup.add(item);
        popups.add(item);

        item = new JMenuItem(EXECUTE);
        popup.add(item);
        popups.add(item);

        item = new JMenuItem(DELETE);
        popup.add(item);
        popups.add(item);

        item = new JMenuItem(REFRESH);
        popup.add(item);
        popups.add(item);

        popupItems = popups.toArray();

        popup.setVisible(false);
    }

    public void mouseClicked(MouseEvent e) {
//        System.out.println("Mouse clicked:" + e);
        if (getSelectedIndex() != -1) {
            if (SwingUtilities.isRightMouseButton(e))
                showPopup(e.getComponent(), e.getX(), e.getY());
        }
    }

    private void showPopup(Component onWhat, int x, int y) {
        popup.show(onWhat, x, y);
    }

    public void addPopupActionListener(ListTreeViewer l) {
        if (popupItems != null) {
            for (int item = 0; item < popupItems.length; item++) {
                JMenuItem menu = (JMenuItem) popupItems[item];
                menu.addActionListener(l);
            }
        }
    }
       
    public void mouseEntered(MouseEvent e) {
        //System.out.println("Mouse Entered:" + e);
    }

    public void mouseExited(MouseEvent e) {
        //System.out.println("Mouse Exited:" + e);
    }

    public void mousePressed(MouseEvent e) {
        //System.out.println("Mouse Pressed:" + e);
    }

    public void mouseReleased(MouseEvent e) {
        //System.out.println("Mouse Released:" + e);
    }

    public class SalsaListModel implements ListModel {

        private ArrayList list;

        public SalsaListModel() {
            list = new ArrayList();
        }

        public SalsaListModel(ArrayList newList) {
            list = newList;
        }

        public ArrayList getList() {
            return list;
        }

        public void setList(ArrayList list) {
            this.list = list;
        }

        public int getSize() {
            return list.size();
        }

        public Object getElementAt(int index) {
            return list.get(index);
        }

        public void addListDataListener(ListDataListener l) {
        }

        public void removeListDataListener(ListDataListener l) {
        }
    }


}
