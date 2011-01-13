/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999, 2000 The Apache Software Foundation.  All rights
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

import org.apache.tools.ant.gui.core.*;

import org.apache.tools.ant.gui.event.AppStartupEvent;
import org.apache.tools.ant.gui.event.AppShutdownEvent;
import org.apache.tools.ant.gui.event.BusMember;
import org.apache.tools.ant.gui.event.BusFilter;
import org.apache.tools.ant.gui.event.EventBus;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import java.util.EventObject;
import java.util.LinkedList;

/**
 * The root class for the Ant GUI. Assembles all the graphical components
 * based on the configuration files.
 * 
 * @version $Revision: 1.1.1.1 $ 
 * @author Simeon Fitch 
 */
public class Antidote extends JComponent {
    /** Source of application state data. */
    private AppContext _context = null;
    /** List of our modules */
    private LinkedList _modules = new LinkedList();
    /** The main menu bar */
    private JMenuBar _menu;

	/** 
	 * Default ctor.
	 * 
	 */
    public Antidote(AppContext context) {
        setLayout(new BorderLayout());

        _context = context;

        // Add the various modules to the editing area.
        JSplitPane splitter = new JSplitPane();
        splitter.add(JSplitPane.LEFT, populateModules("left"));
        splitter.add(JSplitPane.RIGHT, populateModules("right"));
        // This is necessary because, frankly, the JSplitPane widget
        // sucks, and doesn't provide enought (working) control over the
        // initial size of it's subcomponents. setDividerLocation(double)
        // doesn't seem to work until after the widget is visible.
        splitter.setPreferredSize(new Dimension(500, 300));

        // Top bottom splitter. 
        JSplitPane splitter2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitter2.setOneTouchExpandable(true);

        splitter2.add(JSplitPane.TOP, splitter);
        splitter2.add(JSplitPane.BOTTOM, populateModules("bottom"));

        add(BorderLayout.CENTER, splitter2);
        splitter2.resetToPreferredSizes();

        add(BorderLayout.NORTH, populateModules("top"));

        populateModules("hidden");
        
        setPreferredSize(new Dimension(640, 480));
        
        // Listen for startup and shutdown events
        _context.getEventBus().addMember(
            EventBus.MONITORING, new EventListener());
    }


	/** 
	 * Instantiate the configured modules.
	 * 
     * @return Component containing the modules(s).
	 */
    private JComponent populateModules(String prefix) {

        String[] classNames = _context.getResources().
            getStringArray(getClass(), prefix + ".modules");

        AntModule modules[] = new AntModule[classNames.length];

        for(int i = 0; i < classNames.length; i++) {
            try {
                Class type = Class.forName(classNames[i]);

                AntModule newModule = (AntModule) type.newInstance();
                newModule.contextualize(_context);
                modules[i] = newModule;
                _modules.add(newModule);
            }
            catch(Exception ex) {
                // XXX log me.
                ex.printStackTrace();
            }
        }

        if(modules.length == 1) {
            return modules[0];
        }
        else if(modules.length > 1) {
            JTabbedPane tabbed = new JTabbedPane(JTabbedPane.BOTTOM);

            for(int i = 0; i < modules.length; i++) {
                tabbed.addTab(modules[i].getName(), modules[i]);
            }
            return tabbed;
        }
        else {
            return new JLabel("I shouldn't be here...");
        }
    }

    /** Sets the main menu bar*/
    public void setJMenuBar(JMenuBar menu) {
        _menu = menu;
    }
    
    /** Listener for initalizing modules */
    private class EventListener implements BusMember {
        /** Event filter. */
        private final Filter _filter = new Filter();
        
        /** 
         * Get the filter to that is used to determine if an event should
         * to to the member.
         * 
         * @return Filter to use.
         */
        public BusFilter getBusFilter() {
            return _filter;
        }

        /** 
         * Called when an event is to be posed to the member.
         * 
         * @param event Event to post.
         * @return true if event should be propogated, false if
         * it should be cancelled.
         */
        public boolean eventPosted(EventObject event) {
            if (event instanceof AppStartupEvent) {
                for(int i = 0; i < _modules.size(); i++) {
                    AntModule module = (AntModule) _modules.get(i);
                    module.insertInto(_menu);
                }
            }
            if (event instanceof AppShutdownEvent) {
            }
            return true;
        }
    }

    /** Filter for startup and shutdown events. */
    private static class Filter implements BusFilter {
        public boolean accept(EventObject event) {
            return (event instanceof AppStartupEvent ||
                event instanceof AppShutdownEvent );
        }
    }
}
