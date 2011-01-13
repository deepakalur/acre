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

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * <code>DOMNodeManager</code> is used to create and find the abstraction
 * objects.
 *
 * @version $Revision: 1.1.1.1 $
 * @author Nick Davis<a href="mailto:nick_home_account@yahoo.com">nick_home_account@yahoo.com</a>
 */
public class DOMNodeManager implements DOMNodeContainer, DOMNodeFactory {

    private DOMNodeFinder _nodeFinder = new DOMNodeFinder();
    private Map _nodeToClass;

    /**
     * Creates a <code>DOMNodeManager</code> object
     *
     * @param nodeToClass maps the node name to a class name which
     * represents the node.
     */
    public DOMNodeManager(Map nodeToClass) {
        if (nodeToClass == null) {
            throw new IllegalArgumentException(
                "Map can not be null");
        }
        _nodeToClass = nodeToClass;
    }

    /**
     * Creates a <code>DOMNodeManager</code> object
     *
     * @param nodeToClass maps the node name to a class name which
     * represents the node.
     */
    public DOMDocument parse(InputStream stream) 
            throws SAXException, ParserConfigurationException, IOException {
                
        DocumentBuilder builder;
        DocumentBuilderFactory factory =
            DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        
        builder = factory.newDocumentBuilder();
        Document doc = builder.parse(stream);

        DOMDocument domDoc = createDOMDocument(doc);
        Node node = doc.getDocumentElement();
        DOMNode domNode = createDOMNode(node);
        createAllChildren(domNode);
        return domDoc;
    }

    DOMNodeFinder getNodeFinder() {
        return _nodeFinder;
    }
    
    public void createAllChildren(DOMNode node) {
        if (node == null) {
            throw new IllegalArgumentException("node can not be a null");
        }
        TreeWalker i = new TreeWalker(node.getImpl());
        Node real = i.getCurrent();
        while(real != null) {
            createDOMNode(real);
            real = i.getNext();
        }
    }
    
    public DOMDocument createDOMDocument(Document impl) {
        if (impl == null) {
            return null;
        }
        
        DOMDocument doc = find(impl);
        if (doc != null) {
            return doc;
        }

        doc = new DOMDocument();
        doc.setImpl(impl);
        _nodeFinder.assign(impl, doc);
        doc.setFactory(this);
        doc.setContainer(this);
        return doc;
    }
    
    public DOMNode createDOMNode(Node impl) {
        if (impl == null) {
            return null;
        }
        
        DOMNode node = find(impl);
        if (node != null) {
            return node;
        }
        
        String name = impl.getNodeName();
        String className = (String) _nodeToClass.get(name);
        if (className == null && impl instanceof Element) {
            className = (String) _nodeToClass.get("*Element");
        }
        if (className == null) {
            className = (String) _nodeToClass.get("*Node");
            if (className == null) {
                return null;
            }
        }
        try {
            Class theClass = Class.forName (className);
            if (theClass == null) {
                return null;
            }
            node = (DOMNode) theClass.newInstance();
            node.setImpl(impl);
            _nodeFinder.assign(impl, node);
            node.setFactory(this);
        } catch (Exception e) {
            e.printStackTrace();
            node = null;
        }
        return node;
    }
    
    public void assign(Node real, DOMNode proxy) {
        _nodeFinder.assign(real, proxy);
    }
    
    public DOMNode find(Node impl) {
        return _nodeFinder.find(impl);
    }
    
    public DOMDocument find(Document impl) {
        return _nodeFinder.find(impl);
    }
    
    public List find(List list) {
        return _nodeFinder.find(list);
    }
}
