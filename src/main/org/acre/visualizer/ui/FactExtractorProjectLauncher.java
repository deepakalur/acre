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

import org.acre.config.ConfigService;
import org.apache.tools.ant.*;

import javax.swing.JOptionPane;
import java.awt.Component;
import java.io.File;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

/**
 * Launches Fact Extractor Project Builder/Executor using Ant
 * Code based on Anidote Ant GUI
 *
 * @author Yury Kamen
 */
public class FactExtractorProjectLauncher {
    private List buildListeners = new LinkedList();
    private Project antProject;

    /**
     * Run the build.
     */
    public synchronized boolean run() {
        String startFileName = getAntidoteStartFile();
        String javaHome = ConfigService.getInstance().getConfigData().getTigerHomePath();
        String s = ConfigService.getInstance().getConfigData().getAcreRepositoryDirectory();

        if (null == javaHome) {
            String title = Main.getMDIFrame().getSettingsTitle();

            Main.getMDIFrame().setSettingsTitle("Please set Characteristics Extractor Java VM");
            Component c = Main.getMDIFrame().getConfigPanel().selectExtractorPrefsPanel();
            Main.getMDIFrame().showSettings();
            Main.getMDIFrame().setSettingsTitle(title);
            Main.getMDIFrame().getConfigPanel().setSelectedComponent(c);

            javaHome = ConfigService.getInstance().getConfigData().getTigerHomePath();
            if(null == javaHome) {
                return false;
            }
        }

        File f = new File(startFileName);
        if(!f.exists()) {
            JOptionPane.showMessageDialog(
                    null,
                    "Home directory is not set properly!"
                , "Configuration Error", JOptionPane.ERROR_MESSAGE
            );
        }

        antProject = new Project();
        antProject.init();
        antProject.setUserProperty("ant.file", f.getAbsolutePath());

        antProject.setUserProperty("java.home.tiger", javaHome);
        antProject.setUserProperty("salsa.java.home.tiger", javaHome);

        antProject.setUserProperty("salsa.dbuser", ConfigService.getInstance().getConfigData().getRdbmsUserId());
        antProject.setUserProperty("salsa.dbpasswd", ConfigService.getInstance().getConfigData().getRdbmsUserPassword());
        antProject.setUserProperty("salsa.dbdriver", ConfigService.getInstance().getConfigData().getRdbmsJDBCDriver());
        antProject.setUserProperty("salsa.dburl", ConfigService.getInstance().getConfigData().getRdbmsURL());

        ProjectHelper helper = ProjectHelper.getProjectHelper();
        antProject.addReference("ant.projectHelper", helper);
        helper.parse(antProject, f);

        BuildListener[] listeners = getBuildListeners();
        for (int i = 0; i < listeners.length; i++) {
            antProject.addBuildListener(listeners[i]);
        }

        try {
            fireBuildEvent(new BuildEvent(antProject));
            Vector targetNames = new Vector();
            targetNames.add(antProject.getDefaultTarget());
            // Execute build on selected targets. XXX It would be
            // nice if the Project API supported passing in target
            // objects rather than String names.
            antProject.executeTargets(targetNames);
        } catch (BuildException ex) {
            BuildEvent errorEvent = new BuildEvent(antProject);
            errorEvent.setException(ex);
            errorEvent.setMessage(ex.getMessage(), Project.MSG_ERR);
            fireBuildEvent(errorEvent);
            return false;
        } finally {
            fireBuildEvent(new BuildEvent(antProject));

            // Remove the build listeners.
            for (int i = 0; i < listeners.length; i++) {
                antProject.removeBuildListener(listeners[i]);
            }
        }
        return true;
    }

    public static void main(String[] args) {
        FactExtractorProjectLauncher launcher = new FactExtractorProjectLauncher();
        launcher.run();
    }

    /**
     * Add a build listener.
     *
     * @param l Listener to add.
     */
    public void addBuildListener(BuildListener l) {
        synchronized (buildListeners) {
            buildListeners.add(l);
        }
    }

    /**
     * Remove a build listener.
     *
     * @param l Listener to remove.
     */
    public void removeBuildListener(BuildListener l) {
        synchronized (buildListeners) {
            buildListeners.remove(l);
        }
    }

    /**
     * Determine if the given BuildListener is registered.
     *
     * @param l Listener to test for.
     * @return True if listener has been added, false if unknown.
     */
    public boolean isRegisteredBuildListener(BuildListener l) {
        synchronized (buildListeners) {
            return buildListeners.contains(l);
        }
    }

    /**
     * Get the set of current build listeners.
     *
     * @return Set of current build listeners.
     */
    public BuildListener[] getBuildListeners() {
        synchronized (buildListeners) {
            BuildListener[] retval = new BuildListener[buildListeners.size()];
            buildListeners.toArray(retval);
            return retval;
        }
    }

    /**
     * Convenience method for causeing the project to fire a build event.
     * Implemented because the corresponding method in the Project class
     * is not publically accessible.
     *
     * @param event Event to fire.
     */
    private void fireBuildEvent(BuildEvent event) {
        Enumeration listeners = antProject.getBuildListeners().elements();
        while (listeners.hasMoreElements()) {
            BuildListener l = (BuildListener) listeners.nextElement();
            // TODO fire build event
        }
    }

    private String getAntidoteStartFile() {
        String salsahome = System.getProperty("salsa.home");
        String filename = "./ant-antidote/antidote.xml"; // Defelopment hack
        if(null != salsahome) {
            filename = salsahome + File.separator + "antidote" + File.separator + "antidote.xml";
        }
        return filename;
    }
}
