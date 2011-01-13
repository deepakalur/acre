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

package org.apache.tools.ant.gui.xml;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * <code>DOMElement</code> represents an abstraction of
 * <code>org.w3c.dom.Element</code>. 
 *
 * @version $Revision: 1.1.1.1 $
 * @author Nick Davis<a href="mailto:nick_home_account@yahoo.com">nick_home_account@yahoo.com</a>
 */
public class DOMElement extends DOMNode {

    /**
     * Default constructor
     */
    public DOMElement() {
    }
    
    /**
     * @return the <code>org.w3c.dom.Element</code> object.
     */
    protected Element getElementImpl() {
        return (Element) getImpl();
    }
    
    /**
     * Passes the call to the implementation
     */    
    public String getAttribute(String str) {
        return getElementImpl().getAttribute(str);
    }
    
    /**
     * Passes the call to the implementation
     */    
    public Attr getAttributeNode(String str) {
        return getElementImpl().getAttributeNode(str);
    }
    
    /**
     * Passes the call to the implementation
     */    
    public String getTagName() {
        return getElementImpl().getTagName();
    }
    
    /**
     * Passes the call to the implementation
     */    
    public void normalize() {
        getElementImpl().normalize();
    }
    
    /**
     * Passes the call to the implementation
     */    
    public void removeAttribute(String str) throws DOMException {
        setModified(true);
        getElementImpl().removeAttribute(str);
    }
    
    /**
     * Passes the call to the implementation
     */    
    public Attr removeAttributeNode(Attr attr) throws DOMException {
        setModified(true);
        return getElementImpl().removeAttributeNode(attr);
    }
    
    /**
     * Passes the call to the implementation
     */    
    public void setAttribute(String str, String str1) throws DOMException {
        setModified(true);
        getElementImpl().setAttribute(str, str1);
    }
    
    /**
     * Passes the call to the implementation
     */    
    public Attr setAttributeNode(Attr attr) throws DOMException {
        setModified(true);
        return getElementImpl().setAttributeNode(attr);
    }
    
    /**
     * Passes the call to the implementation and returns an abstraction
     * of the returned NodeList.
     * @return a <code>DOMNodeList</code> object
     */    
    public DOMNodeList getElementsByTagName(String str) {
        NodeList list = getElementImpl().getElementsByTagName(str);
        return new DOMNodeList( getFactory(), list );
    }
    
    /**
     * Passes the call to the implementation
     */    
    public String getAttributeNS(String namespaceURI, String localName) {
        return getElementImpl().getAttributeNS(namespaceURI, localName);
    }
    
    /**
     * Passes the call to the implementation
     */    
    public void setAttributeNS(String namespaceURI, String qualifiedName,
            String value) throws DOMException {
        setModified(true);
        getElementImpl().setAttributeNS(namespaceURI, qualifiedName, value);
    }
    
    /**
     * Passes the call to the implementation
     */    
    public void removeAttributeNS(String namespaceURI, String localName)
            throws DOMException {
        setModified(true);
        getElementImpl().removeAttributeNS(namespaceURI, localName);
    }
    
    /**
     * Passes the call to the implementation
     */    
    public Attr getAttributeNodeNS(String namespaceURI, String localName) {
        return getElementImpl().getAttributeNodeNS(namespaceURI, localName);
    }
    
    /**
     * Passes the call to the implementation
     */    
    public Attr setAttributeNodeNS(Attr newAttr) throws DOMException {
        return getElementImpl().setAttributeNodeNS(newAttr);
    }
    
    /**
     * Passes the call to the implementation and returns an abstraction
     * of the returned NodeList.
     * @return a <code>DOMNodeList</code> object
     */    
    public DOMNodeList getElementsByTagNameNS(String namespaceURI, String localName) {
        NodeList list = getElementImpl().getElementsByTagNameNS(
            namespaceURI, localName);
        return new DOMNodeList( getFactory(), list );
    }
    
    /**
     * Passes the call to the implementation
     */    
    public boolean hasAttribute(String name) {
        return getElementImpl().hasAttribute(name);
    }
    
    /**
     * Passes the call to the implementation
     */    
    public boolean hasAttributeNS(String namespaceURI, String localName) {
        return getElementImpl().hasAttributeNS(namespaceURI, localName);
    }
}
