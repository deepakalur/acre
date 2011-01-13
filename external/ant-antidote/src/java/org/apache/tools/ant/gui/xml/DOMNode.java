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

import java.io.CharArrayWriter;
import java.io.Writer;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * <code>DOMNode</code> represents an abstraction of
 * <code>org.w3c.dom.Node</code>. 
 *
 * @version $Revision: 1.1.1.1 $
 * @author Nick Davis<a href="mailto:nick_home_account@yahoo.com">nick_home_account@yahoo.com</a>
 */
public class DOMNode
{
    private Node _impl;
    private DOMNodeFactory _factory;
    
    /** true is this node has been modified */
    private boolean _modified = false;

   
    /**
     * Default constructor
     */
    public DOMNode() {
    }
    
    /**
     * @return the node factory
     */    
    public DOMNodeFactory getFactory() {
        return _factory;
    }
    
    /**
     * Sets the node container
     *
     * @param factory the factory used to create child nodes.
     */    
    void setFactory(DOMNodeFactory factory) {
        _factory = factory;
    }
    
    /** 
     * Sets the node implementation.
     *
     * @param impl the <code>org.w3c.dom.Node</code> object
     */    
    public void setImpl(Node impl) {
        _impl = impl;
    }
    
    /**
     * @return the <code>org.w3c.dom.Node</code> object.
     */
    public Node getImpl() {
        return _impl;
    }
    
    /**
     * Writes the XML node and its children to the writer.
     */
    public void write(Writer writer) throws SAXException {
        DOMNodeSerializer serializer = new DOMNodeSerializer(_factory);
        serializer.write(this, writer);
    }
    
    /**
     * @return xml for this node and its children.
     */
    public String toString() {
        DOMNodeSerializer serializer = new DOMNodeSerializer(_factory);
        CharArrayWriter w = new CharArrayWriter();
        try {
            serializer.write(this, w);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return w.toString();
    }
    
    /**
     * Passes the call to the implementation.
     */    
    public String getNodeName() {
        return _impl.getNodeName();
    }
    
    /**
     * Passes the call to the implementation.
     */    
    public void setNodeValue (String value) throws DOMException {
        _impl.setNodeValue (value);
        _modified = true;
    }
    
    /**
     * Passes the call to the implementation.
     */    
    public String getNodeValue () throws DOMException {
        return _impl.getNodeValue();
    }
    
    /**
     * Passes the call to the implementation.
     */    
    public short getNodeType () {
        return _impl.getNodeType ();
    }
    
    /**
     * Passes the call to the implementation.
     */    
    public DOMNode getParentNode () {
        return _factory.createDOMNode( _impl.getParentNode () );
    }
    
    /**
     * Passes the call to the implementation.
     */    
    public DOMNodeList getChildNodes () {
        return new DOMNodeList( _factory, _impl.getChildNodes() );
    }
    
    /**
     * Passes the call to the implementation.
     */    
    public DOMNode getFirstChild () {
        return _factory.createDOMNode( _impl.getFirstChild () );
    }
    
    /**
     * Passes the call to the implementation.
     */    
    public DOMNode getLastChild () {
        return _factory.createDOMNode( _impl.getLastChild () );
    }
    
    /**
     * Passes the call to the implementation and returns an abstraction
     * of the returned <code>Node</code>.
     * @return a <code>DOMNode</code> object
     */    
    public DOMNode replaceChild (DOMNode newChild, DOMNode oldChild)
            throws DOMException {
        setModified(true);
        return _factory.createDOMNode(
            _impl.replaceChild (newChild.getImpl(),
            oldChild.getImpl()) );
    }
    
    /**
     * Passes the call to the implementation and returns an abstraction
     * of the returned <code>Node</code>.
     * @return a <code>DOMNode</code> object
     */    
    public DOMNode removeChild (DOMNode oldChild) throws DOMException {
        setModified(true);
        return _factory.createDOMNode( _impl.removeChild (
            oldChild.getImpl()) );
    }
    
    /**
     * Passes the call to the implementation and returns an abstraction
     * of the returned <code>Node</code>.
     * @return a <code>DOMNode</code> object
     */    
    public DOMNode insertBefore (DOMNode newChild, DOMNode refChild)
            throws DOMException {
        setModified(true);
        return _factory.createDOMNode(
            _impl.insertBefore (newChild.getImpl(),
            refChild.getImpl()) );
    }
    
    /**
     * Passes the call to the implementation and returns an abstraction
     * of the returned <code>Node</code>.
     * @return a <code>DOMNode</code> object
     */    
    public DOMNode appendChild (DOMNode newChild) throws DOMException {
        setModified(true);
        return _factory.createDOMNode(
            _impl.appendChild (newChild.getImpl()) ); 
    }
    
    /**
     * Passes the call to the implementation.
     */    
    public boolean hasChildNodes () {
        return _impl.hasChildNodes ();
    }
    
    /**
     * Passes the call to the implementation and returns an abstraction
     * of the returned <code>Node</code>.
     * @return a <code>DOMNode</code> object
     */    
    public DOMNode getPreviousSibling () {
        return _factory.createDOMNode( _impl.getPreviousSibling() );
    }
    
    /**
     * Passes the call to the implementation and returns an abstraction
     * of the returned <code>Node</code>.
     * @return a <code>DOMNode</code> object
     */    
    public DOMNode getNextSibling () {
        return _factory.createDOMNode( _impl.getNextSibling() );
    }
    
    /**
     * Passes the call to the implementation and returns an abstraction
     * of the returned <code>NamedNodeMap</code>.
     * @return a <code>NamedDOMNodeMap</code> object
     */    
    public NamedDOMNodeMap getAttributes () {
        return new NamedDOMNodeMap( _factory, _impl.getAttributes() );
    }
    
    /**
     * Passes the call to the implementation.
     */    
    public boolean hasAttributes() {
        return (_impl.getAttributes().getLength() > 0);
    }
    
    /**
     * Passes the call to the implementation.
     */    
    public String getNamespaceURI() {
        return _impl.getNamespaceURI();
    }
    
    /**
     * Passes the call to the implementation.
     */    
    public String getPrefix() {
        return _impl.getPrefix();
    }
    
    /**
     * Passes the call to the implementation.
     */    
    public void setPrefix(String prefix) throws DOMException {
        setModified(true);
        _impl.setPrefix(prefix);
    }
    
    /**
     * Passes the call to the implementation.
     */    
    public String getLocalName() {
        return _impl.getLocalName();
    }
    
    /**
     * Passes the call to the implementation and returns an abstraction
     * of the returned <code>Node</code>.
     * @return a <code>DOMNode</code> object
     */    
    public DOMNode cloneNode (boolean deep) {
        return _factory.createDOMNode( _impl.cloneNode (deep) );
    }
    
    /**
     * Passes the call to the implementation.
     */    
    public void normalize() {
        _impl.normalize();
    }
    
    /**
     * Passes the call to the implementation and returns an abstraction
     * of the returned <code>Document</code>.
     * @return a <code>DOMDocument</code> object
     */    
    public DOMDocument getOwnerDocument() {
        return _factory.createDOMDocument(_impl.getOwnerDocument());
    }
    
    /**
     * @return true is node has been modified
     */
    public boolean isModified() {
        return _modified;
    }
    
    /**
     * Sets the modfided state of node.
     *
     * @param modified the new value
     */
    public void setModified(boolean modified) {
        _modified = modified;
        if (modified == true) {
            getOwnerDocument().setModified(true);
        }
    }
}
