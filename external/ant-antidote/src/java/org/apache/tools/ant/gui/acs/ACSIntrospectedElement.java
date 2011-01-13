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
package org.apache.tools.ant.gui.acs;

import java.util.Enumeration;
import java.util.Map;

import org.apache.tools.ant.gui.xml.DOMNode;
import org.apache.tools.ant.gui.xml.DOMAttributes;
import org.apache.tools.ant.gui.xml.NamedDOMNodeMap;

/**
 * An Introspected element.
 *
 * @version $Revision: 1.1.1.1 $
 * @author Christoph Wilhelms<a href="mailto:christoph.wilhelms@t-online.de">christoph.wilhelms@t-online.de</a>
 */
public class ACSIntrospectedElement extends ACSTreeNodeElement implements ACSInfoProvider {
    /** Property name for the task type. */
    public static final String TASK_TYPE = "taskType";
    /** Property name for attributes. It's called "namedValues" so
     *  it doesn't collide with the Node.getAttributes() method. */
    public static final String NAMED_VALUES = "namedValues";
    
    /** Our menu string */
    private String[] _menuString = null;
    
    /**
     * Default ctor.
     *
     */
    public ACSIntrospectedElement() {
    }
    
    /**
     * Get the task type.
     *
     * @return Task type.
     */
    public String getTaskType() {
        return getTagName();
    }
    
    /**
     * Get the display name of this.
     *
     * @return Display name.
     */
    public String getDisplayName() {
        String name = getTagName();
        
        // Is there only one attribute?
        if (getAttributes().getLength() == 1) {
            DOMNode onlyNode = getAttributes().item(0);
            
            // Display the only attribute
            name += ": " + onlyNode.getNodeValue();
        } else {
            
            // Display one of these attributes
            // if they are present.
            final String[] DISPLAY_ATTRIBUTES = {
                "name",
                "id",
                "property"
            };
            
            for(int i = 0; i < DISPLAY_ATTRIBUTES.length; i++) {
                DOMNode testNode =
                getAttributes().getNamedItem(DISPLAY_ATTRIBUTES[i]);
                if (testNode != null) {
                    name += ": " + testNode.getNodeValue();
                    break;
                }
            }
        }
        
        return name;
    }
    
    /**
     * Get the attributes (named value mappings). This method is not named
     * getAttributes() because there is already a method of that name in
     * the Node interface.
     *
     * @return Name-value mappings.
     */
    public DOMAttributes getNamedValues() {
        Map m = ACSFactory.getInstance().getClassInfo(getTagName(), (this instanceof ACSTargetElement))._attributes;
        DOMAttributes d = new DOMAttributes(m);

        NamedDOMNodeMap attribs = getAttributes();
        for(int i = 0, len = attribs.getLength(); i < len; i++) {
            DOMNode n = attribs.item(i);
            d.setProperty(n.getNodeName(), n.getNodeValue());
        }
        return d;
    }
    
    
    /**
     * Set the attributes. This method sets the Node attirbutes using
     * the given Map containing name-value pairs.
     *
     * @param attributes New attribute set.
     */
    public void setNamedValues(DOMAttributes attributes) {
        // XXX this code really sucks. It is really annoying that the
        // DOM interfaces don't have a general "setAttributes()" or
        // "removeAllAttributes()" method, but instead make you
        // remove each attribute individually, or require you to figure
        // out what the differences are between the two.
        
        // Although this is very inefficient, I'm taking the conceptually
        // simplistic approach to this and brute force removing the existing
        // set and replacing it with a brand new set. If this becomes a
        // performance concern (which I doubt it will) it can be optimized
        // later.
        
        DOMAttributes old = (DOMAttributes) getNamedValues();
        
        Enumeration enum = old.propertyNames();
        while(enum.hasMoreElements()) {
            String name = (String) enum.nextElement();
            removeAttribute(name);
        }
        
        enum = attributes.propertyNames();
        while(enum.hasMoreElements()) {
            String key = (String) enum.nextElement();
            setAttribute(key, attributes.getProperty(key));
        }
        
        firePropertyChange(NAMED_VALUES, old, attributes);
    }
    
    /**
     * Returns the menu items which may be used for this element.
     */
    public String[] getMenuString() {
        
        // If it already exists, use it.
        if (_menuString != null) {
            return _menuString;
        }
        
        // Find the DtdElement
        String name = getTagName();
        
        // Are we the project element?
        boolean isProject = false;
        if (name.equals("project")) {
            isProject = true;
        }
        
        if (isProject) {
            _menuString = new String[1];
            _menuString[_menuString.length-1] = "newElement";
        } else {
            // Add the delete and generic create commands
            _menuString = new String[(2)];
            _menuString[_menuString.length-1] = "deleteElement";
            _menuString[_menuString.length-2] = "newElement";
        }
        
        String[] possibleChildren = getPossibleChildren(TYPE_ELEMENTS);
        int dim = possibleChildren.length;
        if (dim > 0) dim++;
        if (!isProject) dim++;
        
        int j = 0;
        _menuString = new String[dim];
        if (possibleChildren.length > 0) {
            _menuString[j] = "newElement";
            j++;
        }
        if (!isProject) {
            _menuString[j] = "deleteElement";
            j++;
        }
        for (int i=0; i< possibleChildren.length;i++) {
            _menuString[j] = possibleChildren[i];
            j++;
        }
        
        return _menuString;
    }
    
    /**
     * Returns the string to use if an action ID is not found.
     * In our case, the newElement command is used.
     */
    public String getDefaultActionID() {
        return "newElement";
    }
    
    public final static int ALL_ELEMENTS = 3;
    public final static int TASK_ELEMENTS = 1;
    public final static int TYPE_ELEMENTS = 2;
    
    /**
     * Returns a string array which contains this elements
     * possible children.
     *
     * @param childType ACSIntrospectedElement.ALL_ELEMENTS or
     * ACSIntrospectedElement.TASK_ELEMENTS
     * ACSIntrospectedElement.TYPE_ELEMENTS
     */
    public String[] getPossibleChildren(int type) {
        Map m = new java.util.HashMap();
        switch (type) {
            case ALL_ELEMENTS:  m = ACSFactory.getInstance().getClassInfo(getTagName(), (this instanceof ACSTargetElement))._subelements;
                                if (this instanceof ACSTargetElement) m.putAll(ACSFactory.getInstance().getTasks());
                                break;
            case TASK_ELEMENTS: if (this instanceof ACSTargetElement) m = ACSFactory.getInstance().getTasks();
                                else m = new java.util.HashMap();
                                break;
            case TYPE_ELEMENTS: m = ACSFactory.getInstance().getClassInfo(getTagName(), (this instanceof ACSTargetElement))._subelements;
                                break;
        }

        String a[] = new String[m.keySet().size()];
        int i = 0;
        java.util.Iterator it = m.keySet().iterator();
        while (it.hasNext()) {
            a[i] = (String)it.next();
            i++;
        }
        java.util.Arrays.sort(a);
        return a;
    }
}

