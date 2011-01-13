/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000 - 2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "Ant" and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */
package org.apache.tools.ant.gui;

import org.apache.tools.ant.gui.core.AppContext;
import org.apache.tools.ant.gui.util.WindowUtils;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;


/**
 * Dialog displaying information on the application.
 *
 * @version $Revision: 1.1.1.1 $
 * @author Simeon Fitch, Christoph Wilhelms
 */
public class About extends JDialog {
    
    /** The application context for this dialog. */
    private transient AppContext _context = null;
    
    /**
     * Standard ctor.
     *
     * @param context Application context.
     */
    public About(AppContext context) {
        super(context.getParentFrame(), true);
        _context = context;
        init();
    }

    /** 
     * Alternative ctor for use without AppContext.
     */
    public About(JFrame parent) {
        // TODO seems to be unused, should go?
        super(getFrame(parent));
        init();
    }
    
    private static Frame getFrame(JFrame frame) {
        // should be equivalent to:
        // return ((frame == null)?null:(frame instanceof Frame)?frame:JOptionPane.getFrameForComponent(frame));
        Frame pFrame = frame;
        if (frame != null && !(frame instanceof Frame)) {
            pFrame = JOptionPane.getFrameForComponent(frame);
        }
        return pFrame;
    }

    /**
     * Initialization (and display).
     */
    private void init() {
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        setModal(true);
        String title = getResource("title", "Characteristics Extractor Tool");
        setTitle(title);
        JPanel mainPanel = new JPanel(new BorderLayout());
        setContentPane(mainPanel);

        JTabbedPane mainTabbedPane = new JTabbedPane();

        mainTabbedPane.add(getResource("about", "About"), getImagePanel());
        mainTabbedPane.add(getResource("info", "Info"), getClientInfoPanel());

        mainPanel.add(mainTabbedPane, BorderLayout.CENTER);
        
        // Just go ahead and show it...
        pack();
        if (_context != null) {
            WindowUtils.centerWindow(_context.getParentFrame(), this);
        }
        setResizable(false);
        setVisible(true);
    }
    
    /**
     * Builds the nice About panel.
     *
     * @return the About panel
     */
    private JPanel getImagePanel() {
        JPanel imagePanel = new JPanel(new BorderLayout());
//        JLabel imageLabel = new JLabel(new ImageIcon(getClass().getResource("/org/apache/tools/ant/gui/resources/About.gif")));
        JLabel imageLabel = new JLabel(new ImageIcon(getClass().getResource("/org/apache/tools/ant/gui/resources/SalsaSplash.gif")));
        imagePanel.add(imageLabel, BorderLayout.CENTER);

        if (_context != null) {
            JLabel messageLabel = new JLabel(_context.getResources().getMessage(getClass(), "message", new Object[] {}));
            messageLabel.setBorder(BorderFactory.createEmptyBorder(0, 4, 3, 4));
            imagePanel.add(messageLabel, BorderLayout.SOUTH);
        }
        
        return imagePanel;
    }
    
    /**
     * Builds the (client)info panel.
     *
     * @return the clientinfo panel
     */
    private JPanel getClientInfoPanel() {
        JPanel infoPanel = new JPanel(new GridBagLayout());
        
        Properties props = new Properties();
        Properties antProps = new Properties();
        
        try {
            props.load(getClass().getResourceAsStream("version.txt"));
            antProps.load(getClass().getResourceAsStream("/org/apache/tools/ant/version.txt"));
        }
        catch(IOException ex) {
            // XXX log me.
            ex.printStackTrace();
        }
        
        JLabel iaLogoLabel = new JLabel();
        iaLogoLabel.setIcon(new ImageIcon(getClass().getResource("/org/apache/tools/ant/gui/resources/ant_small.gif")));
        infoPanel.add(iaLogoLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 6, 0, 0), 0, 0));
        
        JLabel titleLabel = new JLabel("Characteristics Extractor Tool");
        infoPanel.add(titleLabel, new GridBagConstraints(1, 0, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 6, 0, 0), 0, 0));
        
        JTable clientTable = new JTable();
        clientTable.setAutoCreateColumnsFromModel(false);
        clientTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        TableColumn col = new TableColumn(0);
        col.setHeaderValue(getResource("property", "Property"));
        col.setPreferredWidth(100);
        clientTable.getColumnModel().addColumn(col);
        
        col = new TableColumn(1);
        col.setHeaderValue(getResource("value", "Value"));
        col.setPreferredWidth(250);
        clientTable.getColumnModel().addColumn(col);
        
        
        String [] dummy = {"",""};
        String[][] data = new String[11][2];
        data[0][0] = getResource("version", "Version");
        data[0][1] = props.getProperty("VERSION", "??");
        data[1][0] = getResource("date", "Date");
        data[1][1] = props.getProperty("DATE", "??");
        data[2][0] = getResource("antVersion", "Ant Version");
        data[2][1] = antProps.getProperty("VERSION", "??");
        data[3][0] = getResource("antDate", "Ant Build Date");
        data[3][1] = antProps.getProperty("DATE", "??");
        data[4][0] = getResource("operatingSystem", "Operating System");
        data[4][1] = System.getProperty("os.name")+" version "+System.getProperty("os.version")+" running on "+System.getProperty("os.arch");
        data[5][0] = "Java";
        data[5][1] = System.getProperty("java.version");
        data[6][0] = "VM";
        data[6][1] = System.getProperty("java.vm.name")+" "+System.getProperty("java.vm.version");
        data[7][0] = "Java Home";
        data[7][1] = System.getProperty("java.home");
        data[8][0] = "System Locale";
        data[8][1] = System.getProperty("user.language")+(System.getProperty("user.region")==null?"":"_"+System.getProperty("user.region"));
        data[9][0] = getResource("freeMemory", "Free Memory");
        data[9][1] = (Runtime.getRuntime().freeMemory()/1024)+" kByte";
        data[10][0] = getResource("totalMemory", "Total Memory");
        data[10][1] = (Runtime.getRuntime().totalMemory()/1024)+" kByte";

        clientTable.setModel(new DefaultTableModel(data, dummy) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        });
        
        clientTable.setRowSelectionAllowed(false);
		clientTable.setTableHeader(null);
        
        JScrollPane tableScrollPane = new JScrollPane(clientTable);
        tableScrollPane.setPreferredSize(new Dimension(250, 100));
        infoPanel.add(tableScrollPane, new GridBagConstraints(0, 4, 3, 1, 0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(1, 6, 2, 6), 0, 0));
        
        JLabel classpathLabel = new JLabel("Classpath:");
        infoPanel.add(classpathLabel, new GridBagConstraints(0, 5, 3, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 6, 0, 0), 0, 0));

        String classFileString = System.getProperty("java.class.path");
        Vector pathElements = new Vector();
        for(StringTokenizer strTokenizer = new StringTokenizer(classFileString, System.getProperty("path.separator")); strTokenizer.hasMoreElements(); pathElements.add(strTokenizer.nextElement()));
        JList list = new JList(pathElements);
        JScrollPane listScrollPane = new JScrollPane(list);
        listScrollPane.setPreferredSize(new java.awt.Dimension(100, 80));

        infoPanel.add(listScrollPane, new GridBagConstraints(0, 6, 3, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(1, 6, 2, 6), 0, 0));
        
        infoPanel.setBorder(BorderFactory.createEmptyBorder(7,7,5,7));
        
        return infoPanel;
    }

    /**
     * Helper method for retrieving a resource string.
     *
     * @param key the key to the resource
     * @param defaultValue value returned if the AppContext is null
     * @return the resource associated to the key, or defaultValue if the
     *     AppContext is null.
     * @see AppContext
     */
    private String getResource(String key, String defaultValue) {
        String value = defaultValue;
        if (_context != null) {
            value = _context.getResources().getString(getClass(), key);
        }
        return value;
    }
}
