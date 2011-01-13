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
import org.apache.tools.ant.gui.command.LoadFileCmd;
import org.apache.tools.ant.gui.core.AppContext;
import org.apache.tools.ant.gui.core.EventDebugMonitor;
import org.apache.tools.ant.gui.core.EventResponder;
import org.apache.tools.ant.gui.core.ProjectSelectionMenu;
import org.apache.tools.ant.gui.event.AppShutdownEvent;
import org.apache.tools.ant.gui.event.AppStartupEvent;
import org.apache.tools.ant.gui.event.EventBus;
import org.apache.tools.ant.gui.util.XMLHelper;
import org.apache.tools.ant.gui.wizard.Wizard;
import org.apache.tools.ant.gui.wizard.WizardListener;
import org.apache.tools.ant.gui.wizard.build.BuildData;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import java.awt.BorderLayout;
import java.io.File;

/**
 * Launch point for the Antidote GUI. Configurs it as an application.
 *
 * @version $Revision: 1.1.1.1 $
 * @author Simeon Fitch
 */
public class Main {
    /**
     * Application start.
     *
     * @param args TBD
     */
    public static void main(String[] args) {
        XMLHelper.init();
        
        Args settings = new Args(args);
        
        try {
            javax.swing.UIManager.setLookAndFeel("net.sourceforge.mlf.metouia.MetouiaLookAndFeel");
        }
        catch (Exception e) {
            System.err.println("Could not set net.sourceforge.mlf.metouia.MetouiaLookAndFeel");
        }
        
        try {
            
            MainFrame f = new MainFrame("ACRE Characteristics Extractor Tool");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            AppContext context = new AppContext(f);
            f.setContext(context);

            /* Doing this in a block to limit scope of temps */
            {
                String propertiesFileName = settings.getPropertiesFile();
                if (propertiesFileName != null) {
                    context.setPropertiesFileName(propertiesFileName);   
                    }
                /* If the props file name wasn't passed on the command line
                 * and instanced in AppContext above, this will attempt to load
                 * the default filename initialized in the AppContext
                 */
                if (!context.loadProperties()) {
                    // System.err.println("Warning: Could not load properties file " + context.getPropertiesFileName());   
                    /* Not necessarily an error, e.g., first time Antidote has run */
                    /* Anyway, this is fairly well handled in AppContext already */
                    }
            }
            
            if(!settings.isWizardMode()) {
                EventResponder resp = new EventResponder(context);
                Antidote gui = new Antidote(context);
                
                JMenuBar menu = context.getActions().createMenuBar();
                f.setJMenuBar(menu);
                gui.setJMenuBar(menu);
                f.getContentPane().add(BorderLayout.CENTER, gui);
                f.getContentPane().add(BorderLayout.NORTH,
                context.getActions().createToolBar());
                
                // Add the project selection menu.
                ProjectSelectionMenu ps = new ProjectSelectionMenu(context);
                ps.insertInto(menu);
                
                // Add debugging items.
                if(settings.isDebugMode()) {
                    context.getEventBus().addMember(
                    EventBus.VETOING, new EventDebugMonitor());
                }
                
                // Load a build file if one is provided.
                if(settings.getBuildFile() != null) {
                    LoadFileCmd load = new LoadFileCmd(context);
                    load.setFile(new File(settings.getBuildFile()));
                    load.run();
                }
            }
            else {
                // We are in wizard mode. Create it.
                Wizard wiz = new Wizard(new BuildData());
                // XXX this is temporary for testing. Eventually
                // it will launch the regular antidote screen with the
                // results of the wizard.
                wiz.addWizardListener(new WizardListener() {
                    public void finished(Object model) {
                        BuildData data = (BuildData) model;
                        System.out.println(data.createProject());
                        System.exit(0);
                    }
                    public void canceled() {
                        System.exit(0);
                    }
                    
                });
                
                f.getContentPane().add(BorderLayout.CENTER, wiz);
            }
            
            ImageIcon icon =
            context.getResources().loadImageIcon("icon-small.gif");
            if(icon != null) {
                f.setIconImage(icon.getImage());
            }
            else {
                System.out.println("Application icon not found.");
            }
            
            // Send notice we are starting up
            context.getEventBus().postEvent(
            new AppStartupEvent(context));
            
            f.pack();
            f.restorePersistentSize();
            f.setVisible(true);
            
            // Send notice we are shutting down
            context.getEventBus().postEvent(
            new AppShutdownEvent(context));
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
