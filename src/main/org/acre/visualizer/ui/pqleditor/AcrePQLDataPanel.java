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

import org.acre.dao.PQLConnectionManager;
import org.acre.lang.pql.pdbc.*;

import javax.swing.*;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: Jun 8, 2004
 * Time: 1:39:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class AcrePQLDataPanel extends JSplitPane {

    // contains the PQLDataTreeView and the toString View
    private PQLDataDetailTreePanel treePanel;
    private JTextArea pqlDataTextArea;

    public AcrePQLDataPanel() {
        super();
        initialize();
    }

    public void setData(String text) {
        pqlDataTextArea.setText(text);
        treePanel.clear();
    }
    public void setData(PQLArtifact artifact) {
        if (artifact == null) {
            setData("Nil");
        } else if (artifact.isPrimitive()) {
            setData(artifact.toString());
        } else {
            PQLValueHolder data = (PQLValueHolder) artifact.getValue(); 
            if (!data.isComplete()) {
                try {
                    fetchComplete(data);
                } catch (PQLException e) {
                    JOptionPane.showMessageDialog(this,
                            "Failed to fetch data for : " + data,
                            "Fetch Complete Data Error", 
                            JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
            pqlDataTextArea.setText(data.toString(true, true));
            treePanel.setData(data);
        }
    }

    public static void fetchComplete(PQLValueHolder data)
            throws PQLException {
        // todo. replace PQL with PQLConnection
        try {
            PQLConnection conn = PQLConnectionManager.getInstance().getGlobalConnection();
            conn.fetchComplete(data);
        } catch (PQLException e) {
            throw e;
        } finally {
            PQLConnectionManager.getInstance().release();
        }

    }


    public void initialize() {
        treePanel= new PQLDataDetailTreePanel();
        pqlDataTextArea =  new JTextArea();
        pqlDataTextArea.setEditable(false);
        pqlDataTextArea.setRows(10);
        pqlDataTextArea.setColumns(40);
        this.setOneTouchExpandable(true);
        this.setDividerLocation(250);
//        this.setDividerSize(GlobalSettings.getDividerSize());
        this.setOrientation(JScrollBar.HORIZONTAL);
        this.setLeftComponent(new JScrollPane(treePanel));
        this.setRightComponent(new JScrollPane(pqlDataTextArea));
    }

    public static void main (String [] args) {

        PQLConnection pql = null;
        try {
            pql = PQLConnectionFactory.createPQLConnectionToTDB(
                                                "database/factdb/psa/psaFacts.ta");
            String q = "define result as select c from classes c where c.shortName = \"StaffResourceFB\";return result;";
            PQLStatement stmt = pql.createStatement();
            Map r = stmt.executeQuery(q);

            JFrame frame = new JFrame();
            frame.setVisible(true);
            AcrePQLDataPanel p = new AcrePQLDataPanel();

            if (r instanceof Map) {
               // not required for this query
            }
            else if (r instanceof PQLResultSet) {
                PQLResultSet rs = (PQLResultSet) r;
                PQLArtifact data = (PQLArtifact) rs.getValue(0, 0); // hack...may be PQLData or String
                p.setData(data);
            }

            frame.getContentPane().add(p);
            frame.pack();
        } catch (Throwable e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
}
