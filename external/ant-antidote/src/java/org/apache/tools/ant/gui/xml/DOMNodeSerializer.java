/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999-2003 The Apache Software Foundation.  All rights
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

import java.io.Writer;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * <code>DOMNodeSerializer</code> converts a node to xml
 *
 * @version $Revision: 1.1.1.1 $
 * @author Nick Davis<a href="mailto:nick_home_account@yahoo.com">nick_home_account@yahoo.com</a>
 */
class DOMNodeSerializer {

    private DOMNodeFactory _factory;

    /**
     * Constructor
     *
     * @param factory node factory
     */
    public DOMNodeSerializer(DOMNodeFactory factory) {
        _factory = factory;
    }

    /**
     * Converts the node the xml
     *
     * @param domNode the node to convert
     * @param writer the Writer to use
     */
    public void write(DOMNode domNode, Writer writer) throws SAXException {
        DataWriter w = new DataWriter(writer);
        w.setIndentStep(4);
        writeNode(w, domNode.getImpl());
    }

    /**
     * Iterates through all the child nodes and calls writeNode
     */
    protected void internalWrite(DataWriter w, Node n) throws SAXException {
        Node node = n;
        while(node != null) {

            // Skip attributes, they will be taken care of
            // by writeNode
            if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
                continue;
            }

            writeNode(w, node);

            node = node.getNextSibling();
            if (node == null) {
                Node parent = n.getParentNode();
                if (parent != null) {
                    node = parent.getFirstChild();
                }
            }

            if (node == n) {
                break;
            }
        }
    }
    
    /**
     * 
     */
    protected void writeNode(DataWriter w, Node node)
            throws SAXException {
        
        Attributes attrs = new NamedDOMNodeMap(_factory,
            node.getAttributes() ).convertToAttributes();

        String uri = (node.getNamespaceURI() == null) ? "" :
            node.getNamespaceURI();

        String localname = (node.getLocalName() == null) ? "" :
            node.getLocalName();
        if (localname.equals("")) {
            localname = (node.getNodeName() == null ) ? "" : node.getNodeName();
        }

        String prefix = (node.getPrefix() == null) ? "" :
            node.getPrefix();

        String value = (node.getNodeValue() == null) ? "" :
            node.getNodeValue();

        if (node.hasChildNodes()) {
            writeNodeStart (w, node, node.getNodeType(), uri,
                localname, prefix, attrs, value);
            internalWrite(w, node.getFirstChild());
            writeNodeStop (w, node, node.getNodeType(), uri,
                localname, prefix);
        } else {
            writeNode (w, node, node.getNodeType(), uri,
                localname, prefix, attrs, value);
        }
    }
    
    protected void writeNodeStart(DataWriter w, Node node, int type,
            String uri, String localname, String prefix, Attributes attrs,
            String content) throws SAXException {

        switch (type) {
            case Node.ELEMENT_NODE:
                w.startElement (uri, localname, prefix, attrs);
                break;
                
            case Node.DOCUMENT_NODE:
                w.startDocument();
                
            case Node.TEXT_NODE:
            case Node.CDATA_SECTION_NODE:
            case Node.PROCESSING_INSTRUCTION_NODE:
            case Node.COMMENT_NODE:
                writeNode(w, node, type, uri, localname, prefix, attrs,
                    content);
                break;
        }
    }
    
    protected void writeNodeStop(DataWriter w, Node node, int type, String uri,
            String localname, String prefix) throws SAXException {

        switch (type) {
            case Node.ELEMENT_NODE:
                w.endElement (uri, localname, prefix);
                break;
                
            case Node.DOCUMENT_NODE:
                w.endDocument();
                break;
        }
    }
    
    protected void writeNode(DataWriter w, Node node, int type, String uri,
            String localname, String prefix, Attributes attrs,
            String content) throws SAXException {

        switch (type) {
            case Node.ELEMENT_NODE:
                if (content.equals("")) {
                    w.emptyElement (uri, localname, prefix, attrs);
                } else {
                    w.dataElement (uri, localname, prefix, attrs, content);
                }
                break;
                
            case Node.CDATA_SECTION_NODE:
                CharacterData cd = (CharacterData) node;
                w.characters(cd.getData());
                break;
                
            case Node.COMMENT_NODE:
                CharacterData comment = (CharacterData) node;
                w.comment(comment.getData());
                break;
                
            case Node.PROCESSING_INSTRUCTION_NODE:
                ProcessingInstruction pi = (ProcessingInstruction) node;
                w.processingInstruction(pi.getTarget(), pi.getData());
                break;
        }
    }
}
