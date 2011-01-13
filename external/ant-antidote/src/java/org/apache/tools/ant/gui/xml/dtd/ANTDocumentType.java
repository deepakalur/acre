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
package org.apache.tools.ant.gui.xml.dtd;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * Reads the ANT DTD and provides information about it.
 *
 * @version $Revision: 1.1.1.1 $
 * @author Nick Davis<a href="mailto:nick_home_account@yahoo.com">mailto</a>
 */
public class ANTDocumentType {
    /** ID for core elements */
    public final static int CORE_ELEMENT = 0;
    /** ID for optional elements */
    public final static int OPTIONAL_ELEMENT = 1;
    
    /** True if the DTD has been loaded */
    private boolean _isInit = false;
    /** Hold the core DTD elements */
    private HashMap _coreElementMap = new HashMap();
    /** Hold the optional DTD elements */
    private HashMap _optionalElementMap = new HashMap();
    /** Temp storage for DTD elements */
    private HashMap _elementMap;
    /** singleton */
    private static ANTDocumentType _antDocumentType;
    /** First part of the XML document used to load the DTD */
    private final static String XMLDOC_1 = 
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
        //"<!DOCTYPE project SYSTEM \"file:/";
        "<!DOCTYPE project SYSTEM \"";
    /** Second part of the XML document used to load the DTD */
    private final static String XMLDOC_2 = 
        "\"><project name=\"sample-project\">" + 
        "</project>";
    /** DTD which holds the core tasks */
    private final static String DTD_1 = "project.dtd";
    /** DTD which holds the optional tasks */
    private final static String DTD_2 = "project-ext.dtd";
    /** DTD which holds the shared elements */
    private final static String DTD_SHARE = "share.dtd";

    /**
     * Standard ctor.
     */
    protected ANTDocumentType() {
    }
    
    static public ANTDocumentType getANTDocumentType() {
        if (_antDocumentType == null) {
            _antDocumentType = new ANTDocumentType();
        }
        return _antDocumentType;
    }
    
    public Map getCoreElementMap() {
        init();
        return _coreElementMap;
    }
    
    public Map getOptionalElementMap() {
        init();
        return _optionalElementMap;
    }
    
    /**
     * Loads the DTD if not already loaded.
     */
    public void init() {
        // Return if already inited. 
        if (_isInit) {
            return;
        }
        
        try {
            setupDefaultParser();
            
            XMLReader reader = XMLReaderFactory.createXMLReader();
            
            DtdHandler handler = new DtdHandler();
            reader.setProperty(
                "http://xml.org/sax/properties/lexical-handler",
                handler);
            reader.setProperty(
                "http://xml.org/sax/properties/declaration-handler",
                handler);
            
            reader.setDTDHandler(handler);
            reader.setContentHandler(handler);
            reader.setEntityResolver(new ACSResolver());
            
            String coreDoc = XMLDOC_1 + DTD_1 + XMLDOC_2;
            String optionalDoc = XMLDOC_1 + DTD_2 + XMLDOC_2;
            
            java.net.URL urlClass =  
                getClass().getResource("ANTDocumentType.class");
            
            // Parse the core task DTD
            _elementMap = _coreElementMap;
            InputStream xmldocCore = 
                new ByteArrayInputStream(coreDoc.getBytes());
            InputSource is = new InputSource(xmldocCore);
            is.setSystemId(urlClass.toExternalForm());
            reader.parse(is);
            
            // Parse the optional task DTD
            _elementMap = _optionalElementMap;
            InputStream xmldocOptional = 
                new ByteArrayInputStream(optionalDoc.getBytes());
            is = new InputSource(xmldocOptional);
            is.setSystemId(urlClass.toExternalForm());
            reader.parse(is);
            
            _isInit = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * If a parser has not been configured, setup a default.
     */
    protected void setupDefaultParser() {
        
        String prop = "org.xml.sax.driver";
        String className = System.getProperty(prop);
        
        // If the default parser is set, do nothing.
        if (className != null) {
            return;
        }
        
        // Look for the crimson parser
        try {
            className = "org.apache.xerces.parsers.SAXParser";
            Class.forName(className);
            System.setProperty(prop, className);
            return;
        } catch (Exception e) {}
        
        // Look for the xerces parser
        try {
            className = "org.apache.crimson.parser.XMLReaderImpl";
            Class.forName(className);
            System.setProperty(prop, className);
            return;
        } catch (Exception e) {}
    }
    
    /**
     * Returns the dtd element.
     *
     * @param elementType CORE_ELEMENT or OPTIONAL_ELEMENT
     * @param name the element name
     */
    public DtdElement findElement(int elementType, String name) {
        init();
        if (elementType == OPTIONAL_ELEMENT) {
            return (DtdElement) _optionalElementMap.get(name);
        }
        return (DtdElement) _coreElementMap.get(name);
    }
    
    /**
     * Class which represents a DTD element.
     */
    public static class DtdElement {
        private String _name;
        private String[] _contentModel;
        private DtdAttributes _map = new DtdAttributes();
        
        public String getName() {
            return _name;
        }
        public void setName(String name) {
            _name = name;
        }
        public String[] getContentModel() {
            return _contentModel;
        }
        public void setContentModel(String[] model) {
            _contentModel = model;
        }
        public DtdAttributes getAttributes() {
            return _map;
        }
    }
    
    /**
     * Class which represents a DTD attribute.
     */
    public static class DtdAttribute {
        private String _name;
        private String _type;
        private String _defaultValue;
        private boolean _isFixed;
        private boolean _isRequired;
        
        public String getName() {
            return _name;
        }
        public void setName(String name) {
            _name = name;
        }
        public String getType() {
            return _type;
        }
        public void setType(String type) {
            _type = type;
        }
        public String getDefaultValue() {
            return _defaultValue;
        }
        public void setDefaultValue(String value) {
            _defaultValue = value;
        }
        public boolean isFixed() {
            return _isFixed;
        }
        public void setFixed(boolean value) {
            _isFixed = value;
        }
        public boolean isRequired() {
            return _isRequired;
        }
        public void setRequired(boolean value) {
            _isRequired = value;
        }
    }

    /**
     * Class which represents a collection of DTD attributes.
     */
    public static class DtdAttributes extends HashMap {
        /**
         * Default constructor
         */
        public DtdAttributes() {
        }

        /**
         * Adds the Attribute
         *
         * @param attribute new attribute
         */
        public void addAttribute(DtdAttribute attribute) {
            put(attribute.getName(), attribute);
        }

        /**
         * Return the requested attribute
         *
         * @param name attribute name
         * @return the requested attribute
         */
        public DtdAttribute getAttribute(String name) {
            return (DtdAttribute) get(name);
        }

        /**
         * @return an array of the optional attribute names
         */
        public String[] getOptionalAttributes() {
            ArrayList list = new ArrayList();
            Iterator i = values().iterator();
            while(i.hasNext()) {
                DtdAttribute a = (DtdAttribute)i.next();
                if (!a.isRequired()) {
                    list.add(a.getName());
                }
            }
            String[] result = new String[list.size()];
            list.toArray(result);
            return result;
        }

        /**
         * @return an array of the required attribute names
         */
        public String[] getRequiredAttributes() {
            ArrayList list = new ArrayList();
            Iterator i = values().iterator();
            while(i.hasNext()) {
                DtdAttribute a = (DtdAttribute)i.next();
                if (a.isRequired()) {
                    list.add(a.getName());
                }
            }
            String[] result = new String[list.size()];
            list.toArray(result);
            return result;
        }
        /**
         * @return an array of the all attribute names
         */
        public String[] getAttributes() {
            ArrayList list = new ArrayList();
            Iterator i = values().iterator();
            while(i.hasNext()) {
                DtdAttribute a = (DtdAttribute)i.next();
                list.add(a.getName());
            }
            String[] result = new String[list.size()];
            list.toArray(result);
            return result;
        }
    }
    
    /**
     * When parsing XML documents, DTD related events are signaled through
     * this interface. 
     */
    class DtdHandler implements DTDHandler, ContentHandler, DeclHandler,
            LexicalHandler {
        
        /**
         * Reports an attribute declaration found within the DTD.
         *
         * @param elementName The name of the element to which the attribute
         *	applies; this includes a namespace prefix if one was used within
         *	the DTD.
         * @param attributeName The name of the attribute being declared; this
         *	includes a namespace prefix if one was used within the DTD.
         * @param attributeType The type of the attribute, either CDATA, NMTOKEN,
         *	NMTOKENS, ENTITY, ENTITIES, NOTATION, ID, IDREF, or IDREFS as
         *	defined in the XML specification; or null for enumerations.
         * @param valueDefault A string representing the attribute default
         *      ("#IMPLIED", "#REQUIRED", or "#FIXED") or null if
         *      none of these applies.
         * @param value A string representing the attribute's default value,
         *      or null if there is none.
         */
        public void attributeDecl (
            String		elementName,
            String		attributeName,
            String		attributeType,
            String		valueDefault,
            String              value
        ) throws SAXException
        {
            // Try to find the element.
            DtdElement e = (DtdElement) _elementMap.get(elementName);
            if (e == null) {
                throw new SAXException("element " + elementName +
                " not declared before attributes");
            }
            
            // Update the element's attribute.
            DtdAttribute attrib = new DtdAttribute();
            attrib.setName(attributeName);
            attrib.setType(attributeType);
            boolean isRequired = ("#REQUIRED".equals(valueDefault));
            boolean isFixed = ("#FIXED".equals(valueDefault));
            attrib.setFixed(isFixed);
            attrib.setRequired(isRequired);
            attrib.setDefaultValue(value);
            e.getAttributes().addAttribute(attrib);
        }
        
        /**
         * Reports an element declaration found within the DTD.  The content
         * model will be a string such as "ANY", "EMPTY", "(#PCDATA|foo)*",
         * or "(caption?,tr*)".
         *
         * @param elementName The name of the element; this includes a namespace
         *	prefix if one was used within the DTD.
         * @param contentModel The content model as defined in the DTD, with
         *	any whitespace removed.
         */
        public void elementDecl (
            String elementName,
            String contentModel
        ) throws SAXException
        {
            DtdElement e = new DtdElement();
            e.setName(elementName);

            // Break the contentModel string into pieces.
            ArrayList list = new ArrayList();
            StringTokenizer st = new StringTokenizer(contentModel, "|()*");
            while (st.hasMoreTokens()) {
                String s = st.nextToken();
                if ( s.length() > 0 && !"EMPTY".equals(s) ) {
                    list.add(s);
                }
            }            
            String[] array = new String[list.size()];
            list.toArray(array);
            e.setContentModel(array);
            
            // Update the map
            _elementMap.put(e.getName(), e);
        }
        
        public void unparsedEntityDecl(String p1, String p2, String p3,
                String p4) throws SAXException {}

        public void notationDecl(String p1, String p2, String p3)
                throws SAXException {}
   
        public void endElement(String p1,String p2,String p3)
                throws SAXException {}
        
        public void setDocumentLocator(Locator p1) {}
        
        public void startPrefixMapping(String p1,String p2)
                throws SAXException {}
        
        public void endDocument() throws SAXException {}
        
        public void startDocument() throws SAXException {}
        
        public void endPrefixMapping(String p1) throws SAXException {}
        
        public void startElement(String p1,String p2,String p3,
                Attributes p4) throws SAXException {}
        
        public void ignorableWhitespace(char[] p1,int p2,int p3)
                throws SAXException {}
        
        public void skippedEntity(String p1) throws SAXException {}
        
        public void processingInstruction(String p1,String p2)
                throws SAXException {}
        
        public void characters(char[] p1,int p2,int p3)
                throws SAXException {}
        
        public void externalEntityDecl(String p1,String p2,String p3)
                throws SAXException {}
        
        public void internalEntityDecl(String p1,String p2)
                throws SAXException {}
        
        public void endDTD()
                throws SAXException {}
        
        public void startCDATA()
                throws SAXException {}
        
        public void startEntity(String p1)
                throws SAXException {}
        
        public void endCDATA()
                throws SAXException {}
        
        public void endEntity(String p1)
                throws SAXException {}
        
        public void startDTD(String p1,String p2,String p3)
                throws SAXException {}
        
        public void comment(char[] p1,int p2,int p3)
                throws SAXException {}
    }
    
    /**
     * We provide the location for the ant dtds.
     */
    class ACSResolver implements org.xml.sax.EntityResolver {
        
        /**
         * We process the project.dtd and project-ext.dtd.
         *
         * @param name Used to find alternate copies of the entity, when
         *	this value is non-null; this is the XML "public ID".
         * @param uri Used when no alternate copy of the entity is found;
         *	this is the XML "system ID", normally a URI.
         */
        public InputSource resolveEntity (
            String publicId,
            String systemId)
            throws SAXException, IOException {
                
            InputSource is = null;
            
            try {
                URL url = null;

                // Is it the project.dtd?
                if (systemId.indexOf(DTD_1) != -1) {
                    url = this.getClass().getResource(DTD_1);
                }
                // Is it the project-ext.dtd?
                if (systemId.indexOf(DTD_2) != -1) {
                    url = this.getClass().getResource(DTD_2);
                }
                // Is it the share.dtd?
                if (systemId.indexOf(DTD_SHARE) != -1) {
                    url = this.getClass().getResource(DTD_SHARE);
                }

                if (url != null) {
                    is = new InputSource(url.openStream());
                    is.setSystemId(url.toExternalForm());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            return is;
        }
    }
}
