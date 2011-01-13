/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999, 2003 The Apache Software Foundation.  All rights
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

import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import org.xml.sax.Attributes;

import org.xml.sax.helpers.AttributesImpl;

/**
 * <code>NamedDOMNodeMap</code> is an abstraction of the
 * <code>NamedNodeMap</code> object.
 *
 * @version $Revision: 1.1.1.1 $
 * @author Nick Davis<a href="mailto:nick_home_account@yahoo.com">nick_home_account@yahoo.com</a>
 */
public class NamedDOMNodeMap  {

    private NamedNodeMap _impl;
    private DOMNodeFactory _factory;

    /**
     * Creates an <code>NamedDOMNodeMap</code> from the input NamedNodeMap.
     *
     * @param impl the <code>NamedNodeMap</code>
     * @param factory the factory to use to create the DOMNodes.
     */
    public NamedDOMNodeMap(DOMNodeFactory factory, NamedNodeMap impl) {
        _impl = impl;
        _factory = factory;
    }

    /**
     * Creates a <code>org.xml.sax.Attributes</code> for the contained
     * nodes.
     *
     * @return a <code>org.xml.sax.Attributes</code>
     */
    public Attributes convertToAttributes() {
        AttributesImpl attrs = new AttributesImpl();
        if (_impl == null) {
            return attrs;
        }

        for (int i = 0; i < getLength(); i++) {
            Node n = _impl.item(i);

            String uri = (n.getNamespaceURI() == null) ? "" :
                n.getNamespaceURI();

            String localname = (n.getLocalName() == null) ? n.getNodeName() :
                n.getLocalName();

            String prefix = (n.getPrefix() == null) ? "" :
                n.getPrefix();

            String value = (n.getNodeValue() == null) ? "" :
                n.getNodeValue();

            attrs.addAttribute (uri, localname, prefix, "CDATA", value);
        }
        return attrs;
    }

    public DOMNode setNamedItem(DOMNode node) throws DOMException {
        Node temp = _impl.setNamedItem(node.getImpl());
        return _factory.createDOMNode(temp);
    }

    public DOMNode getNamedItem(String name) {
        Node node = _impl.getNamedItem(name);
        return _factory.createDOMNode(node);
    }

    public int getLength() {
        return _impl.getLength();
    }

    public DOMNode item(int index) {
        Node node = _impl.item(index);
        return _factory.createDOMNode(node);
    }

    public DOMNode removeNamedItem(String name) throws DOMException {
        Node node = _impl.removeNamedItem(name);
        return _factory.createDOMNode(node);
    }
}
