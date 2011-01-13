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
package org.acre.visualizer.ui;

import org.acre.visualizer.ui.components.AcreSplasher;

import javax.swing.JWindow;
import java.awt.*;

/**
 * @author Deepak.Alur@Sun.com
 *         Date: Jun 17, 2004
 *         Time: 3:22:58 PM
 */
public class AcreSplash extends JWindow {

    private AcreSplasher splasher;
    private static final String SPLASHIMAGE = "AcreSplash.gif";

    public AcreSplash() {
        super();
        initialize();
    }

    public AcreSplash(Frame owner) {
        super(owner);
        initialize();
    }

    public AcreSplash(GraphicsConfiguration gc) {
        super(gc);
        initialize();
    }

    public AcreSplash(Window owner) {
        super(owner);
        initialize();
    }

    public AcreSplash(Window owner, GraphicsConfiguration gc) {
        super(owner, gc);
        initialize();
    }

    private void initialize() {
        splasher = new AcreSplasher();
        splasher.setStatus("");
        getContentPane().add(splasher, BorderLayout.CENTER);
        this.pack();

        int width = this.getSize().width; // 300;
        int height = this.getSize().height; //220;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - width) / 2;
        int y = (screen.height - height) / 2;
        setBounds(x, y, width, height);
        setVisible(false);
    }

    public void showMe() {
        setVisible(true);
    }

    public void hideMe() {
        setVisible(false);
    }

    public void show(int howManySeconds) {
        int showTime=5;

        // min 5 secs and max 30 secs showing
        if ((howManySeconds < 5) || (howManySeconds > 30))
            showTime=5;

        showMe();
        try {Thread.sleep(showTime *1000); } catch (Exception e) {
            e.printStackTrace();
        }
        hideMe();

    }

    public static void main(String args[]) {
        try {
            AcreSplash splash = new AcreSplash();
            splash.showMe();
            splash.setStatus("Hello...");
            splash.setProgress(35);
            Thread.sleep(1000);
            splash.setStatus("World...");
            splash.setProgress(100);
            Thread.sleep(3000);
            splash.hideMe();
            splash.setProgress(0);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void setStatus(String s) {
        splasher.setStatus(s);
        splasher.revalidate();
        updateMe();
    }

    public void setProgress(int percentage) {
        splasher.setProgressBar(percentage);
        splasher.revalidate();
        updateMe();
    }

    private void updateMe() {
        this.repaint();
        this.validate();
        this.validateTree();
        this.requestFocus();
    }
}
