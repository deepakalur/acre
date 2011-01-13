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
package org.apache.tools.ant.gui.modules;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EventObject;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.apache.tools.ant.gui.acs.ACSProjectElement;
import org.apache.tools.ant.gui.core.AntMenu;
import org.apache.tools.ant.gui.core.AntModule;
import org.apache.tools.ant.gui.core.AppContext;
import org.apache.tools.ant.gui.core.PropertiesManager;
import org.apache.tools.ant.gui.event.BusFilter;
import org.apache.tools.ant.gui.event.BusMember;
import org.apache.tools.ant.gui.event.EventBus;
import org.apache.tools.ant.gui.event.OpenRequestEvent;
import org.apache.tools.ant.gui.event.ProjectClosedEvent;

/**
 * Adds a "Reopen" menu which contains a list of files which
 * have been opened and closed.
 *
 * @version $Revision: 1.1.1.1 $
 * @author Nick Davis
 */
public class Reopener extends AntModule {
    
    /** Holds the frames original title */
    private Properties _fileList = new Properties();
    /** The parent menu */
    private JMenu _menu = null;
    /** Used to format the time the file was closed */
    private SimpleDateFormat _formatter
    = new SimpleDateFormat("yyyy.MM.dd G 'at' hh:mm:ss a zzz");
    /** The title of our menu */
    private String _menuName = null;
	/** The icon of our menu */
	private ImageIcon _icon = null;
    /** The menu we put our menu after */
    private String _insertAfterMenuName = null;
    /** The number of files to display */
    private static final int NUMBER_MENU_ITEMS = 10;
    
    /** The string prefixed to every reopener filelist entry in our Properties */
    public static final String REOPENER_FILELIST_PROPERTY_PREFIX="Reopener.filelist.";
    
    /** Test if a key (i.e., a Properties file key in our persistent properties)
     * is a key written/read by the Reopener as a filelist member.
     * @param key A string used as a properties file key.
     * @return true if indeed the string represents a Reopener filelist key.
     */
    public static boolean isReopenerFilelistKey(String key) {
        boolean result = false;
        if (null != key) {
            result = key.startsWith(REOPENER_FILELIST_PROPERTY_PREFIX);
        }
        return result;
    }
    
    /** Make a file key intended to be stored in our persistent properties
     * into a key written/read by the Reopener as a filelist member by prefixing
     * it correctly.
     * @param key A string used by the Reopener intended to be used as a properties file key.
     * @return The same string prefixed with the Reopener filelist key.
     */
    public static String makeReopenerFilelistKey(String protoKey) {
        String result= REOPENER_FILELIST_PROPERTY_PREFIX + protoKey;
        return result;
    }
    
    /** Make a file key intended to be stored in our persistent properties
     * into a the kind of key understood by the Reopener as a filelist member by
     * stripping the prefix that identifies it as a Reopener key in the properties file.
     * @param key A string used as a properties file key.
     * @return The same string with the prefix for a Reopener filelist key stripped from it.
     */
    public static String stripReopenerFilelistKey(String protoKey) {
        String result = protoKey;
        if (isReopenerFilelistKey(protoKey)) {
            result = protoKey.substring(REOPENER_FILELIST_PROPERTY_PREFIX.length());
        }
        return result;
    }
    
    /**
     * Register our event listener
     *
     * @param context Application context.
     */
    public void contextualize(AppContext context) {
        setContext(context);
        
        _menuName =
        context.getResources().getString(getClass(), "menuName");
        _insertAfterMenuName =
        context.getResources().getString(getClass(), "insertAfterMenuName");
		_icon =
		context.getResources().getImageIcon(getClass(), "icon");
        
        context.getEventBus().addMember(EventBus.RESPONDING, new Handler());
    }
    
    /**
     *  Insert our menu into the main menu bar
     *
     * @param menuBar the main menu bar
     */
    public void insertInto(JMenuBar menuBar) {
        final String findName = _insertAfterMenuName;
        _menu = new JMenu();
        _menu.setText(_menuName);
		_menu.setIcon(_icon);
        
        // Load the list of files
        loadList();
        
        // Loop throught the menus and look for the menu
        // we put our menu after.
        boolean breakOut = false;
        int count = menuBar.getComponentCount();
        for(int i = 0; i < count; i++) {
            JMenu menu = (JMenu) menuBar.getComponent(i);
            for(int x = 0; x < menu.getMenuComponentCount(); x++) {
                if (!(menu.getMenuComponent(x) instanceof JMenuItem)) {
                    continue;
                }
                JMenuItem subMenu = (JMenuItem) menu.getMenuComponent(x);
                
                // Is this the menu we are looking for?
                if(subMenu.getText().indexOf(findName) == 0) {
                    menu.add(_menu, x+1);
                    breakOut = true;
                    break;
                }
            }
            if (breakOut) {
                break;
            }
        }
        updateMenu();
    }
    
    /**
     *
     */
    protected void updateMenu() {
        
        // If the list is too large, remove the oldest entry.
        trimList();
        
        // Transfer the file map into a new map,
        // so it will be sorted by date.
        TreeMap map = new TreeMap();
        Iterator z = _fileList.keySet().iterator();
        while(z.hasNext()) {
            String key = (String) z.next();
            String value = (String) _fileList.get(key);
            try {
                Date date = _formatter.parse(value);
                if (date == null) {
                    continue;
                }
                map.put(new Long(date.getTime() * -1), key);
            } catch (Exception e) {}
        }
        
        // Remove all of our menu items.
        _menu.removeAll();
        
        // Loop through the map and add the updated
        // menu items.
        Set set = map.keySet();
        Iterator i = set.iterator();
        int count = 0;
        while(i.hasNext()) {
            String name = (String) map.get(i.next());
            
            // Setup an event which will be sent when
            // the menu item is pressed.
            OpenRequestEvent event = new OpenRequestEvent(
            getContext(), new File(name));
            AntMenu subMenu = new AntMenu(getContext(),
            event, name);
            
            JMenuItem item = _menu.add(subMenu);
            item.setAccelerator(
            KeyStroke.getKeyStroke("control " + count));
            count++;
        }
    }
    
    /**
     * Remove the oldest entries from the list,
     * if the list is too large.
     */
    protected void trimList() {
        
        // Transfer the file list in to
        // a map which is sorted by date.
        TreeMap map = new TreeMap();
        Iterator i = _fileList.keySet().iterator();
        while(i.hasNext()) {
            String key = (String) i.next();
            String value = (String) _fileList.get(key);
            map.put(value, key);
        }
        
        // Remove any extra entries
        while (map.size() > NUMBER_MENU_ITEMS) {
            Object key = map.firstKey();
            map.remove(key); // Remove it from our temp list used to reinstance _fileList
            // .AND. remove it from our global properties (first converting key into form
            // our persistent properties knows about!).
            getContext().getPropertiesManager().remove(makeReopenerFilelistKey(key.toString()));
        }
        
        // Transfer the temp map to the real map.
        _fileList.clear();
        Iterator z = map.keySet().iterator();
        while(z.hasNext()) {
            String key = (String) z.next();
            String value = (String) map.get(key);
            _fileList.put(value, key);
        }
    }
    
    /**
     * Saves the list to the global PropertiesManager and causes a props file save.
     */
    protected void saveList() {
        Iterator i = _fileList.keySet().iterator();
        PropertiesManager pm = getContext().getPropertiesManager();
        while(i.hasNext()) {
            String key = (String) i.next();
            String value = (String) _fileList.get(key);
            pm.setProperty(makeReopenerFilelistKey(key), value);
        }
        try {
            getContext().saveProperties();
        }
        catch (java.io.FileNotFoundException e) {
            // Log error? Show dialog? Couldn't save.
        }
        catch (java.io.IOException e) {
            // Log error? Show dialog? Couldn't save.
        }
    }
    
    /**
     * Refreshes the Properties used by the Reopened from the global PropertiesManager.
     */
    protected void loadList() {
        _fileList.clear();
        PropertiesManager pm = getContext().getPropertiesManager();
        Iterator i = pm.keySet().iterator();
        String key = null;
        String value = null;
        while(i.hasNext()) {
            key = (String) i.next();
            if (isReopenerFilelistKey(key)) {
                value = (String) pm.getProperty(key);
                _fileList.setProperty(stripReopenerFilelistKey(key), value);
            }
        }
    }
    
    /** Class for handling project events. */
    private class Handler implements BusMember {
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
            
            // We should only get project closed events
            
            // Find the name of the file which was just closed.
            ACSProjectElement project =
            getContext().getSelectionManager().getSelectedProject();
            URL url = project.getLocation();
            if (url == null) {
                return true;
            }
            String file = url.getFile();
            
            // Get the current time
            Date currentTime_1 = new Date();
            String dateString = _formatter.format(currentTime_1);
            
            // Add the file to the list
            _fileList.put(file, dateString);
            
            // Update the menu and save.
            updateMenu();
            saveList();
            return true;
        }
    }
    
    /** Class providing filtering for project events. */
    private static class Filter implements BusFilter {
        
        /**
         * Determines if the given event should be accepted.
         *
         * @param event Event to test.
         * @return True if event should be given to BusMember, false otherwise.
         */
        public boolean accept(EventObject event) {
            return (event instanceof ProjectClosedEvent);
        }
    }
}
