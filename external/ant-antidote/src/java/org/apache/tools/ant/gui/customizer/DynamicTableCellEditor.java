/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2003 The Apache Software Foundation.  All rights
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
package org.apache.tools.ant.gui.customizer;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.io.File;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;


/**
 * Widget for dynamically constructing a cell editor based on the
 * a type that gets registered with the editor.
 * 
 * @version $Revision: 1.1.1.1 $
 * @author Craig Campbell, Christoph Wilhelms
 */
public class DynamicTableCellEditor extends DefaultCellEditor {
	static {
		PropertyEditorManager.registerEditor(
			String.class, LinePropertyEditor.class);
		PropertyEditorManager.registerEditor(
			String[].class, StringArrayPropertyEditor.class);
		PropertyEditorManager.registerEditor(
			int.class, IntegerPropertyEditor.class);
		PropertyEditorManager.registerEditor(
			Integer.class, IntegerPropertyEditor.class);
		PropertyEditorManager.registerEditor(
			double.class, DoublePropertyEditor.class);
		PropertyEditorManager.registerEditor(
			Double.class, DoublePropertyEditor.class);
		PropertyEditorManager.registerEditor(
			Properties.class, PropertiesPropertyEditor.class);
		PropertyEditorManager.registerEditor(
			File.class, FilePropertyEditor.class);
		PropertyEditorManager.registerEditor(
			Object.class, ObjectPropertyEditor.class);
        PropertyEditorManager.registerEditor(
                boolean.class, BooleanPropertyEditor.class);
        PropertyEditorManager.registerEditor(
                Boolean.class, BooleanPropertyEditor.class);
	}

    /** Property name that PropertyDescriptors can save in their property
     *  dictionaries for for specifiying a display sorting order. The value
     *  sould be of type Integer. */
    public static final String SORT_ORDER = "sortOrder";
    /** keep the map from editor to key through all instances of object */
    private static HashMap _editor2Key = new HashMap();
    /** keep the map from key to editor through all instances of object */
    private static HashMap _key2Editor = new HashMap();
    /** keep a list of editor listeners */
    private EditorChangeListener _eListener = new EditorChangeListener();
    /** the value to return for the given editor */
    private String _value = null;
    /** Current editor */
    private PropertyEditor _editor = null;
    /** Read-only flag. */
    private boolean _readOnly = false;
    /** List of property change listeners interested when the bean
     *  being edited has been changed. */
    private java.util.List _changeListeners = new LinkedList();

    /**
     *Default constructor.
     *
     */
    public DynamicTableCellEditor()  {
        super(new JTextField());
    }

    /**
     * Register a key with a specified type.  The type is
     * checked against registered editors and if found the
     * key then points to the editor for that type.
     *
     * @param key unique identifier
     * @param type the class to find the editor
     */
    public void register(String key, Class type)    {
        if(_key2Editor.containsKey(key))
            return;

        // Find the editor.  If none exists revert to a string editor
        PropertyEditor editor = PropertyEditorManager.findEditor(type);

        if(editor == null)
            editor = PropertyEditorManager.findEditor(java.lang.String.class);

        // Add a listener to the editor so we know when to update
        // the bean's fields.
        editor.addPropertyChangeListener(_eListener);

//        System.out.println("registered key:" + key + " to editor:"  + editor); // dbg purposes only

        // Map the key to the editor and the editor to the key for quick lookup.
        _key2Editor.put(key, editor);
        _editor2Key.put(editor, key);
    }

    /**
	 * Add the given listener. Will receive a change event for
     * changes to the bean being edited.
	 *
	 * @param l Listner to add.
	 */
    public void addPropertyChangeListener(PropertyChangeListener l) {
        _changeListeners.add(l);
    }


	/**
	 * Remove the given property change listener.
	 *
	 * @param l Listener to remove.
	 */
    public void removePropertyChangeListener(PropertyChangeListener l) {
        _changeListeners.remove(l);
    }

    /**
	 * Fire a property change event to each listener.
	 *
	 * @param bean Bean being edited.
	 * @param propName Name of the property.
	 * @param oldValue Old value.
	 * @param newValue New value.
	 */
    protected void firePropertyChange(Object bean, String propName,
                                      Object oldValue, Object newValue) {

        PropertyChangeEvent e = new PropertyChangeEvent(
            bean, propName, oldValue, newValue);

        Iterator it = _changeListeners.iterator();
        while(it.hasNext()) {
            PropertyChangeListener l = (PropertyChangeListener) it.next();
            l.propertyChange(e);
        }
    }

    /************************************************************************
     *          TableCellEditor/CellEditor implementation methods
     ************************************************************************/

    /**
     * Get the <code>value</code> that the editor contains.
     *
     * @return  value found in the editor
     */
    public Object getCellEditorValue()  {
        return (_editor!=null?_editor.getValue():"");
    }

    /**  Sets an initial <code>value</code> for the editor.  This will cause
     *  the editor to <code>stopEditing</code> and lose any partially
     *  edited value if the editor is editing when this method is called. <p>
     *
     *  Returns the component that should be added to the client's
     *  <code>Component</code> hierarchy.  Once installed in the client's
     *  hierarchy this component will then be able to draw and receive
     *  user input.
     *
     * @param	table		the <code>JTable</code> that is asking the
     * 				editor to edit; can be <code>null</code>
     * @param	value		the value of the cell to be edited; it is
     * 				up to the specific editor to interpret
     * 				and draw the value.  For example, if value is
     * 				the string "true", it could be rendered as a
     * 				string or it could be rendered as a check
     * 				box that is checked.  <code>null</code>
     * 				is a valid value
     * @param	isSelected	true if the cell is to be rendered with
     * 				highlighting
     * @param	row     	the row of the cell being edited
     * @param	column  	the column of the cell being edited
     * @return	the component for editing
     */
    public Component getTableCellEditorComponent(JTable table, 
                                                Object value, 
                                                boolean isSelected, 
                                                int row, 
                                                int column) {

        // We need to get the key from the first column in the table
        // which will always be found at column 0.
        Object key = table.getModel().getValueAt(row, 0);

        // Lookup the editor.
        _editor = (PropertyEditor)_key2Editor.get(key);

        // initialize the return value to be a JTextField if the
        // editor does not contain a custom editor
        Component retVal = new JTextField();

        if(_editor != null)  {
            // Set the editor value from the parameter
            _editor.setAsText((String)value);

            // XXX What we need to do right here is provide a component
            // that makes use of the "paintable" capability of the editor.
            Component custom = _editor.getCustomEditor();
            if(custom != null) {
                retVal = custom;
            }
        }
        
        return retVal;    
    }
    
    /** Asks the editor if it can start editing using <code>anEvent</code>.
     * <code>anEvent</code> is in the invoking component coordinate system.
     * The editor can not assume the Component returned by
     * <code>getCellEditorComponent</code> is installed.  This method
     * is intended for the use of client to avoid the cost of setting up
     * and installing the editor component if editing is not possible.
     * If editing can be started this method returns true.
     *
     * @param	anEvent		the event the editor should use to consider
     * 				whether to begin editing or not
     * @return	true if editing can be started
     * @see #shouldSelectCell
     */
    public boolean isCellEditable(EventObject anEvent) {
        return !_readOnly;
    }

    /** Class for receiving change events from the PropertyEditor objects. */
    private class EditorChangeListener implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent e) {

            PropertyEditor editor = (PropertyEditor) e.getSource();

            Object oldValue = _value;
            _value = editor.getAsText();
            firePropertyChange(
                editor, (String)_editor2Key.get(editor), oldValue, _value);

        }
    }
}
