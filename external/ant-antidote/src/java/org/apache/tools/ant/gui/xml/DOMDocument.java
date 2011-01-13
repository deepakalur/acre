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
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.EntityReference;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

/**
 * <code>DOMDocument</code> represents an abstraction of
 * <code>org.w3c.dom.Document</code>. 
 *
 * @version $Revision: 1.1.1.1 $
 * @author Nick Davis<a href="mailto:nick_home_account@yahoo.com">nick_home_account@yahoo.com</a>
 */
public class DOMDocument {

    private Document _impl;
    private DOMNodeFactory _factory;
    private DOMNodeContainer _container;
    
    /** true if any node in the document has been modfied */
    private boolean _modified = false;

    /**
     * Creates new DOMDocument
     */
    public DOMDocument() {
    }
    
    /**
     * @return the node factory
     */    
    public DOMNodeFactory getFactory() {
        return _factory;
    }
    
    /**
     * @param factory the node factory
     */    
    void setFactory(DOMNodeFactory factory) {
        _factory = factory;
    }
    
    /**
     * @return the node container
     */    
    public DOMNodeContainer getContainer() {
        return _container;
    }
    
    /** 
     * @param container the node container
     */    
    void setContainer(DOMNodeContainer container) {
        _container = container;
    }
    
    /** 
     * Sets the node implementation.
     *
     * @param impl the <code>org.w3c.dom.Document</code> object
     */    
    public void setImpl(Document impl) {
        _impl = impl;
    }

    /**
     * Pass call to the implementaion
     * @return the document type
     */
    public DocumentType getDoctype() {
        return _impl.getDoctype();
    }
    
    /**
     * Passes the call to the implementation
     */    
    public ProcessingInstruction createProcessingInstruction(String p1,String p2)
            throws DOMException {
        return _impl.createProcessingInstruction(p1, p2);
    }
    
    /**
     * Passes the call to the implementation
     */    
    public EntityReference createEntityReference(String p1)
            throws DOMException {
        return _impl.createEntityReference(p1);
    }
    
    /**
     * Passes the call to the implementation
     */    
    public Text createTextNode(String p1) {
        return _impl.createTextNode(p1);
    }
    
    /**
     * Passes the call to the implementation
     */    
    public CDATASection createCDATASection(String p1) throws DOMException {
        return _impl.createCDATASection(p1);
    }
    
    /**
     * Passes the call to the implementation and returns an abstraction
     * of the returned element.
     * @return a <code>DOMElement</code> object
     */    
    public DOMElement getDocumentElement() {
        return (DOMElement) _factory.createDOMNode(_impl.getDocumentElement());
    }
    
    /**
     * Passes the call to the implementation and returns an abstraction
     * of the returned <code>Implementation</code>.
     * @return a <code>DOMImplementation</code> object
     */    
    public DOMImplementation getImplementation() {
        return _impl.getImplementation();
    }
    
    /**
     * Passes the call to the implementation
     */    
    public Attr createAttribute(String p1) throws DOMException {
        return _impl.createAttribute(p1);
    }
    
    /**
     * Passes the call to the implementation
     */    
    public Comment createComment(String p1) {
        return _impl.createComment(p1);
    }
    
    /**
     * Passes the call to the implementation
     */    
    public DocumentFragment createDocumentFragment() {
        return _impl.createDocumentFragment();
    }
    
    /**
     * Passes the call to the implementation and returns an abstraction
     * of the returned <code>NodeList</code>.
     * @return a <code>DOMNodeList</code> object
     */    
    public DOMNodeList getElementsByTagName(String p1) {
        return new DOMNodeList(_factory, _impl.getElementsByTagName(p1));
    }
    
    /**
     * Passes the call to the implementation and returns an abstraction
     * of the returned <code>Element</code>.
     * @return a <code>DOMElement</code> object
     */    
    public DOMElement createElement(String p1) throws DOMException {
        return (DOMElement) _factory.createDOMNode(_impl.createElement(p1));
    }
    
    /**
     * Passes the call to the implementation and returns an abstraction
     * of the returned <code>Node</code>.
     * @return a <code>DOMNode</code> object
     */    
    public DOMNode getPreviousSibling() {
        return _factory.createDOMNode(_impl.getPreviousSibling());
    }
    
    /**
     * Passes the call to the implementation.
     */    
    public void setNodeValue(String p1) throws DOMException {
        _impl.setNodeValue(p1);
    }
    
    /**
     * Passes the call to the implementation.
     */    
    public String getNodeValue() throws DOMException {
        return _impl.getNodeValue();
    }
    
    /**
     * Passes the call to the implementation and returns an abstraction
     * of the returned <code>Node</code>.
     * @return a <code>DOMNode</code> object
     */    
    public DOMNode insertBefore(DOMNode p1, DOMNode p2) throws DOMException {
        return _factory.createDOMNode(
            _impl.insertBefore(p1.getImpl(), p2.getImpl()));
    }
    
    /**
     * Passes the call to the implementation and returns an abstraction
     * of the returned <code>Node</code>.
     * @return a <code>DOMNode</code> object
     */    
    public DOMNode getParentNode() {
        return _factory.createDOMNode(_impl.getParentNode());
    }
    
    /**
     * Passes the call to the implementation.
     */    
    public boolean hasChildNodes() {
        return _impl.hasChildNodes();
    }
    
    /**
     * Passes the call to the implementation.
     */    
    public String getNodeName() {
        return _impl.getNodeName();
    }
    
    /**
     * Passes the call to the implementation and returns an abstraction
     * of the returned <code>NamedNodeMap</code>.
     * @return a <code>NamedDOMNodeMap</code> object
     */    
    public NamedDOMNodeMap getAttributes() {
        return new NamedDOMNodeMap (_factory, _impl.getAttributes());
    }
    
    /**
     * Passes the call to the implementation.
     */    
    public short getNodeType() {
        return _impl.getNodeType();
    }
    
    /**
     * Passes the call to the implementation and returns an abstraction
     * of the returned <code>Node</code>.
     * @return a <code>DOMNode</code> object
     */    
    public DOMNode removeChild(DOMNode p1) throws DOMException {
        return _factory.createDOMNode(
            _impl.removeChild(p1.getImpl()));
    }
    
    /**
     * Passes the call to the implementation and returns an abstraction
     * of the returned <code>Node</code>.
     * @return a <code>DOMNode</code> object
     */    
    public DOMNode appendChild(DOMNode p1) throws DOMException {
        return _factory.createDOMNode(
            _impl.appendChild(p1.getImpl()));
    }
    
    /**
     * Passes the call to the implementation and returns an abstraction
     * of the returned <code>Node</code>.
     * @return a <code>DOMNode</code> object
     */    
    public DOMNode getNextSibling() {
        return _factory.createDOMNode(_impl.getNextSibling());
    }
    
    /**
     * Passes the call to the implementation and returns an abstraction
     * of the returned <code>Node</code>.
     * @return a <code>DOMNode</code> object
     */    
    public DOMNode getLastChild() {
        return _factory.createDOMNode(_impl.getLastChild());
    }
    
    /**
     * Passes the call to the implementation and returns an abstraction
     * of the returned <code>NodeList</code>.
     * @return a <code>DOMNodeList</code> object
     */    
    public DOMNodeList getChildNodes() {
        return new DOMNodeList (_factory, _impl.getChildNodes());
    }
    
    /**
     * Passes the call to the implementation and returns an abstraction
     * of the returned <code>Node</code>.
     * @return a <code>DOMNode</code> object
     */    
    public DOMNode getFirstChild() {
        return _factory.createDOMNode(_impl.getFirstChild());
    }
    
    /**
     * Passes the call to the implementation and returns an abstraction
     * of the returned <code>Node</code>.
     * @return a <code>DOMNode</code> object
     */    
    public DOMNode cloneNode(boolean p1) {
        return _factory.createDOMNode(_impl.cloneNode(p1));
    }
    
    /**
     * Passes the call to the implementation.
     */    
    public Document getOwnerDocument() {
        return _impl.getOwnerDocument();
    }
    
    /**
     * Passes the call to the implementation and returns an abstraction
     * of the returned <code>Node</code>.
     * @return a <code>DOMNode</code> object
     */    
    public DOMNode replaceChild(DOMNode p1, DOMNode p2) throws DOMException {
        return _factory.createDOMNode(
            _impl.replaceChild(p1.getImpl(), p2.getImpl()));
    }
    
    /**
     * @return true if any node in the document has been modified
     */
    public boolean isModified() {
        return _modified;
    }
    
    /**
     * Set the modified flag
     *
     * @param modified the new value
     */
    public void setModified(boolean modified) {
        _modified = modified;
    }
}
