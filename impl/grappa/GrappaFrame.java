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
package org.acre.visualizer.grappa;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.net.URL;
import java.net.URLConnection;

public class GrappaFrame {
//        implements GrappaConstants {
    public GrappaUI frame = null;

    public final static String SCRIPT = "/devel/SalsaVizV1/formatDemo.sh";

    public static void main(String[] args) {
        InputStream input = System.in;
        if (args.length > 1) {
            System.err.println("USAGE: java GrappaFrame [input_graph_file]");
            System.exit(1);
        } else if (args.length == 1) {
            if (args[0].equals("-")) {
                input = System.in;
            } else {
                try {
                    input = new FileInputStream(args[0]);
                } catch (FileNotFoundException fnf) {
                    System.err.println(fnf.toString());
                    System.exit(1);
                }
            }
        }
        GrappaFrame demo = new GrappaFrame();
        demo.build(input);
    }

    public GrappaFrame() {
    }

    public JPanel getPanel() {
        if (frame != null) {
            return frame.getPanel();
        } else {
            return null;
        }
    }

    public void build(InputStream input) {
        build(input, true, true);
    }

    public void build(String input, boolean visible, boolean extraUI) {
        build(new StringBufferInputStream(input), visible, extraUI);
    }

    public void build(InputStream input, boolean visible, boolean extraUI) {
        frame = new GrappaUI(GrappaUtilities.buildGraph(input), extraUI);
        frame.setVisible(visible);
    }



    class GrappaUI extends JFrame implements ActionListener {
        GrappaPanel gp;
        Graph graph = null;

        JButton layout = null;
        JButton printer = null;
        JButton draw = null;
        JButton quit = null;
        JPanel panel = null;

        private final boolean extraUI;

        public GrappaUI(Graph graph) {
            this(graph, true);
        }


        public GrappaUI(Graph graph, boolean _extraUI) {
            super("GrappaUI");
            this.extraUI = _extraUI;

            this.graph = graph;

            setSize(1000, 1000);
            setLocation(50, 50);

            addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent wev) {
                    Window w = wev.getWindow();
                    w.setVisible(false);
                    w.dispose();
                    if (extraUI) {
                        System.exit(0);
                    }
                }
            });

            JScrollPane jsp = new JScrollPane();

            gp = GrappaUtilities.createGrappaPanel(graph);
            gp.addGrappaListener(new GrappaAdapter());

            GridBagLayout gbl = new GridBagLayout();
            GridBagConstraints gbc = new GridBagConstraints();

            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.NORTHWEST;

            panel = new JPanel();
            panel.setLayout(gbl);

            if (extraUI) {
                draw = new JButton("Redraw");
                gbl.setConstraints(draw, gbc);
                panel.add(draw);
                draw.addActionListener(this);

                layout = new JButton("Layout");
                gbl.setConstraints(layout, gbc);
                panel.add(layout);
                layout.addActionListener(this);

                printer = new JButton("Print");
                gbl.setConstraints(printer, gbc);
                panel.add(printer);
                printer.addActionListener(this);

                quit = new JButton("Close");
                gbl.setConstraints(quit, gbc);
                panel.add(quit);
                quit.addActionListener(this);
            }
            getContentPane().add("Center", jsp);

            if (extraUI) {
                getContentPane().add("West", panel);
            }

            setVisible(true);
            jsp.setViewportView(gp);
        }

        public JPanel getPanel() {
            return panel;
        }

        public void actionPerformed(ActionEvent evt) {
            if (evt.getSource() instanceof JButton) {
                JButton tgt = (JButton) evt.getSource();
                if (tgt == draw) {
                    graph.repaint();
                } else if (tgt == quit) {
                    System.exit(0);
                } else if (tgt == printer) {
                    graph.printGraph(System.out);
                    System.out.flush();
                } else if (tgt == layout) {
                    Object connector = null;
                    try {
                        //connector = Runtime.getRuntime().exec(GrappaFrame.SCRIPT);
                        //connector = null;
                        //connector = Runtime.getRuntime().exec("/Applications/GraphViz.app/Contents/MacOS/dot /devel/grappa/DEMO/crazy.dot");

                    } catch (Exception ex) {
                        System.err.println("Exception while setting up Process: " + ex.getMessage() + "\nTrying URLConnection...");
                        connector = null;
                    }
                    if (connector == null) {
                        try {
                            connector = (new URL("http://www.research.att.com/~john/cgi-bin/format-graph")).openConnection();
                            URLConnection urlConn = (URLConnection) connector;
                            urlConn.setDoInput(true);
                            urlConn.setDoOutput(true);
                            urlConn.setUseCaches(false);
                            urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        } catch (Exception ex) {
                            System.err.println("Exception while setting up URLConnection: " + ex.getMessage() + "\nLayout not performed.");
                            connector = null;
                        }
                    }
                    if (connector != null) {
                        if (!GrappaSupport.filterGraph(graph, connector)) {
                            System.err.println("ERROR: somewhere in filterGraph");
                        }
                        if (connector instanceof Process) {
                            try {
                                int code = ((Process) connector).waitFor();
                                if (code != 0) {
                                    System.err.println("WARNING: proc exit code is: " + code);
                                }
                            } catch (InterruptedException ex) {
                                System.err.println("Exception while closing down proc: " + ex.getMessage());
                                ex.printStackTrace(System.err);
                            }
                        }
                        connector = null;
                    }
                    graph.repaint();
                }
            }
        }
    }
}
