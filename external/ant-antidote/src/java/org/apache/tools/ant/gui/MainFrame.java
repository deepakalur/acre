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

import org.apache.tools.ant.gui.core.AppContext;
import org.apache.tools.ant.gui.core.PropertiesManager;

import org.apache.tools.ant.gui.event.ProjectClosedEvent;
import org.apache.tools.ant.gui.event.ProjectSelectedEvent;

import org.apache.tools.ant.gui.acs.ACSProjectElement;

import java.io.IOException;
import java.awt.Dimension;

/**
 * The main frame for the application
 * 
 * @version $Revision: 1.1.1.1 $ 
 * @author Nick Davis
 */
public class MainFrame extends javax.swing.JFrame {
    
    private AppContext _context;
    
    /**
     * Standard Constructor
     *
     * @param title the frame's title
     */
    public MainFrame(String title) {
        super(title);
        initComponents();
    }

    /**
     * 
     */
    private void initComponents() {
        enableEvents(java.awt.event.WindowEvent.WINDOW_CLOSING);
        pack();
    }

    /** 
     * Called before window is closed.  Will close each
     * open project.  If there is a problem closing a
     * project, the window will not close.
     */
    private boolean exitForm(java.awt.event.WindowEvent evt) {
        boolean okToClose = true;
        
        ACSProjectElement[] projects = _context.getProjectManager().getOpen();
        while(projects.length > 0) {
            try {
                if ( _context.getProjectManager().close(projects[0]) == false) {
                    okToClose = false;
                    break;
                }
                _context.getEventBus().postEvent(
                    new ProjectClosedEvent(_context));

                ACSProjectElement[] open = 
                    _context.getProjectManager().getOpen();
                if(open != null && open.length > 0) {
                    _context.getEventBus().postEvent(
                        new ProjectSelectedEvent(_context, open[0]));
                }
            } catch (IOException e) {
                e.printStackTrace();
                okToClose = false;
                break;
            }
            
            projects = _context.getProjectManager().getOpen();
        }

        return okToClose;
    }

    /**
     * Sets the application context
     */
    public void setContext(AppContext context) {
        _context = context;
    }

    /**
     * Save the window size for the next session
     */
    protected void persistSize () {
        Dimension d = getSize();
        PropertiesManager pm = _context.getPropertiesManager();
        pm.setProperty("MainFrame.height", new Integer(new Double(d.getHeight()).intValue()).toString());
        pm.setProperty("MainFrame.width", new Integer(new Double(d.getWidth()).intValue()).toString());
        try {
        _context.saveProperties();
        }
        catch (java.io.FileNotFoundException e) {
            // log it?
        }
        catch (java.io.IOException e) {
            // log it?
        }
    }
    
     /**
     * Restore the window size from the previous session
     */
    protected void restorePersistentSize () {
      PropertiesManager pm = _context.getPropertiesManager();
      try {
      setSize(pm.get_int_property("MainFrame.width"), pm.get_int_property("MainFrame.height"));
      }
      catch (PropertiesManager.NoSuchPropertyException e) {
       // Just means that it's never been saved to persistent properties yet.   
      }
      
      catch (PropertiesManager.InvalidIntPropertyException e) {
       // Should never occur unless user edited file and messed it up.
      }
    }
    
    /**
     * Intercept the window close event
     */
    protected void processWindowEvent(java.awt.event.WindowEvent windowEvent) {
        persistSize(); // Save window size for next session
        if (windowEvent.getID() == java.awt.event.WindowEvent.WINDOW_CLOSING) {
            if (exitForm(windowEvent)) {
                super.processWindowEvent(windowEvent);
            }
        }
    }
    
}
