/**
 *   Copyright 2004-2005 Sun Microsystems, Inc.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.acre.extractor.wsdl.wsdlimport;

import org.w3c.dom.Element;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.JAXBException;

import org.acre.extractor.jaxb.xsd.*;

import java.util.List;

/**
 * User: Administrator
 */
public class XMLSchemaFactExtractor {

    public void extractFacts(Element elem) {
        JAXBContext jaxbContext = null;
        try {
            jaxbContext = JAXBContext.newInstance("org.acre.extractor.jaxb.xsd");
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            Object schemaElement = unmarshaller.unmarshal(elem);
            extractSchemaElementFacts((XsdElemSchema)schemaElement);
        } catch (JAXBException e) {
            e.printStackTrace();  // Handle Exception
        }
    }

    private void extractSchemaElementFacts(XsdElemSchema xsdElemSchema) {
        System.out.println("Schema: TargetNameSpace = " + xsdElemSchema.getTargetNamespace());
        List typesList = xsdElemSchema.getSimpleTypeOrComplexTypeOrGroup();

        // first pass : create symbol table entries for Global Simple & Complex Types
        for ( Object obj : typesList ) {
            if ( obj instanceof XsdElemSimpleType ) {
                XsdElemSimpleType simpleType = (XsdElemSimpleType)obj;
                XMLSchemaDAO.createSimpleElementType(simpleType.getName());
            }
            else if ( obj instanceof XsdElemComplexType) {
                XsdElemComplexType complexType = (XsdElemComplexType) obj;
                System.out.println("ComplexType");
                System.out.println(complexType.getName());
                System.out.println();
            }
        }

        // second pass : create symbol table entries for Global Elements, attributes,
        // Element groups & attribute groups
        for ( Object obj : typesList ) {
            if ( obj instanceof XsdElemAttributeGroup) {
            }
            else if ( obj instanceof XsdElemAttribute) {
            }
            else if ( obj instanceof XsdElemElement) {
            }
            else if ( obj instanceof XsdElemGroup) {

            }
        }

        // third pass : All symbol table entires have been created, now time to generate XMLSchema facts
        for ( Object obj : typesList ) {
            if ( obj instanceof XsdElemSimpleType ) {
            }
            else if ( obj instanceof XsdElemComplexType) {
            }
            else if ( obj instanceof XsdElemAttributeGroup) {
            }
            else if ( obj instanceof XsdElemAttribute) {
            }
            else if ( obj instanceof XsdElemAnnotation) {
            }
            else if ( obj instanceof XsdElemElement) {
            }
            else if ( obj instanceof XsdElemNotation) {
            }
            else if ( obj instanceof XsdElemGroup) {
            }
        }
    }
}
