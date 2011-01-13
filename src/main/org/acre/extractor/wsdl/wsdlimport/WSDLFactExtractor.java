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

import org.acre.extractor.jaxb.wsdl.*;
import org.w3c.dom.Element;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import java.io.*;
import java.util.List;
import java.util.Stack;

/**
 * WSDLFactExtractor is responsible for gathering/determining 'design-facts' from WSDL
 * file and delegating the facts to a DAO to be written into a repository.
 *
 * User: rajmohan@sun.com
 * Date: Sep 16, 2004
 * Time: 5:08:32 PM
 */

public class WSDLFactExtractor {
    private static final String SOAP_BINDING_STYLE = "style";
    private static final String SOAP_BINDING_TRANSPORT = "transport";
    private static final String SOAP_ACTION = "soapAction";


    public static void main(String[] args) {
        try {
            if (args.length > 0) {
                String fileName = args[0];
                WSDLFactDAO factWriter = new WSDLFactDAO("wsdlFacts.sql");

                WSDLFactExtractor extractor = new WSDLFactExtractor(fileName, factWriter);
                extractor.extractFacts();

            } else {
                System.out.println("ERROR: Must provide WSDL file name");
            }
        } catch (Throwable t) {
            System.err.println ("Unable to extract facts :  " + t.getMessage());
        }
    }

    private File inputFile;

    public WSDLFactExtractor(String fileName, WSDLFactDAO factWriter) {
        inputFile = new File(fileName);
        this.factWriter = factWriter;
    }

    public void extractFacts() {
        extractFacts(null);
        factWriter.finish();

        // debug
        //wsdlSymbols.printSymbolTable();
    }

    public void extractFacts(String namespace) {
        Object o = null;
        try {
            o = unmarshal(new FileInputStream(inputFile), "org.acre.extractor.jaxb.wsdl");
        } catch (JAXBException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        visitWSDLDocument ((WsdlElemDefinitions) o, namespace);
    }


    private Object unmarshal(InputStream is, String packageName)
	    throws JAXBException {
            // Unmarshaller to unmarshal xml to java object content tree
            JAXBContext jaxbContext = JAXBContext.newInstance(packageName);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return unmarshaller.unmarshal(is);
    }

    public void visitWSDLDocument (WsdlElemDefinitions definitions, String namespace) {

        List anyTopLevelOptionalElements = definitions.getAnyTopLevelOptionalElement();

        initializeContextValues();

        // Extract starting from the WSDL root-element i.e., <definitions.... >
        visitDefinitionsElement(definitions, namespace);

        try {
            scope.push(definitions.getTargetNamespace());
            for ( Object topLevelElement : anyTopLevelOptionalElements) {
                if ((topLevelElement instanceof AnyTopLevelOptionalElementImport)) {
                    visitImportElement((AnyTopLevelOptionalElementImport)topLevelElement);

                } else if (topLevelElement instanceof AnyTopLevelOptionalElementTypes) {
                    visitTypesElement((AnyTopLevelOptionalElementTypes)topLevelElement);

                } else if (topLevelElement instanceof AnyTopLevelOptionalElementMessage) {
                    visitMessageElement((AnyTopLevelOptionalElementMessage)topLevelElement);

                } else if (topLevelElement instanceof AnyTopLevelOptionalElementPortType) {
                    visitPortTypeElement((AnyTopLevelOptionalElementPortType)topLevelElement);

                } else if (topLevelElement instanceof AnyTopLevelOptionalElementBinding) {
                    visitBindingElement((AnyTopLevelOptionalElementBinding)topLevelElement);

                } else if (topLevelElement instanceof AnyTopLevelOptionalElementService) {
                    visitServiceElement((AnyTopLevelOptionalElementService)topLevelElement);
                }
            }
        } finally {
            scope.pop();
        }
    }

    private void visitDefinitionsElement(WsdlElemDefinitions definitions, String targetNameSpace) {
        String namespace  =  targetNameSpace == null ? definitions.getTargetNamespace() : targetNameSpace;
        if ( (namespace == null ) || (namespace.length() == 0))
            namespace = definitions.getName();
        String definitionsId = wsdlSymbols.addSymbol(namespace);

        factWriter.createDefinition(definitionsId, definitions.getName(),
                definitions.getTargetNamespace());

        definitions.setTargetNamespace(namespace);
        setDefinitionElement(definitions);
    }

    private void initializeContextValues() {
    }

    private void visitBindingElement(AnyTopLevelOptionalElementBinding bindingElement) {

        String bindingId = wsdlSymbols.addSymbol(getScope(), WSDLSymbols.TYPE_BINDING,
                bindingElement.getName(), bindingElement);

        List anyElements = bindingElement.getAny();

        Element firstElement = (Element) anyElements.get(0);
        String style = firstElement.getAttribute(SOAP_BINDING_STYLE);
        String transport = firstElement.getAttribute(SOAP_BINDING_TRANSPORT);

        String refPortType = bindingElement.getType().getLocalPart();
        String portTypeId = wsdlSymbols.lookup(
                            getDefinitionNS(),
                WSDLSymbols.TYPE_PORTTYPE,
                            refPortType);


        factWriter.createBinding(bindingId,
                bindingElement.getName(),
                portTypeId,
                SOAP_PROTOCOL,
                transport,
                style,
                getDefinitionId());

        try {
            scope.push(WSDLSymbols.TYPE_BINDING + bindingElement.getName());
            List operations = bindingElement.getOperation();
            for ( Object operation : operations)
                visitSOAPOperation(style, transport, (WsdlTypeTBindingOperation)operation,
                            refPortType, portTypeId, bindingId);
        }
        finally {
            scope.pop();
        }
    }

    private void visitSOAPOperation(String style, String transport,
                         WsdlTypeTBindingOperation operation,
                         String portTypeName, String portTypeId, String bindingId) {

        String protocolName = "SOAP";
        String operationName = operation.getName();
        String operationStyle;
        WsdlTypeTOperation abstractOperation = wsdlSymbols.lookupOperationElement(
                               getDefinitionNS(), portTypeName, operationName);

        // determine operation type - Notification, Request-Response, etc.
        String operationType = identifyOperationType(abstractOperation);


        // determine Operation binding style - document or RPC
        List soapExtensibilityElements = operation.getAny();
        Element soapOperationElement = (Element) soapExtensibilityElements.get(0);

        operationStyle = soapOperationElement.getAttribute(SOAP_BINDING_STYLE);

        if ( isEmptyString(operationStyle) ) {
            operationStyle = ( isEmptyString(style) ? "document" : style );
        }

        // Handle protocol-specific extensibility element(s)
        List elements = operation.getAny();
        Element SOAPOperationElement = (Element)elements.get(0);
        String soapAction = SOAPOperationElement.getAttribute(SOAP_ACTION);

        List parameters = abstractOperation.getParameterOrder();
        StringBuffer parameterOrder = new StringBuffer();
        for ( Object parameter : parameters )
            parameterOrder.append((String)parameter + " ");

        String operationId = wsdlSymbols.addSymbol(getScope(), WSDLSymbols.SUBTYPE, operationName,
                        operation);


        factWriter.createSOAPOperation(operationId,
                                        operationName,
                                        parameterOrder.toString(),
                                        operationType,
                                        protocolName,   // soap, mime
                                        soapAction,
                                        operationStyle,
                                        portTypeId,
                                        bindingId);
        try {
            scope.push(WSDLSymbols.SUBTYPE + operationName);

            visitOperationInputElement(operationId, abstractOperation, operation.getInput());

            visitOperationOutputElement(operationId, abstractOperation, operation.getOutput());

            visitOperationFaultElements(operationId, abstractOperation, operation.getFault());
        }
        finally {
            scope.pop();
        }
    }

    private boolean isEmptyString(String operationStyle) {
        if ( operationStyle == null )
            return true;
        operationStyle.trim();
        if (operationStyle.length() == 0)
            return true;
        return false;
    }

    private String identifyOperationType(WsdlTypeTOperation abstractOperation) {
        String operationType ;

        // Currently we identify only One-way; Request-Response; Notification
        // TODO - Solicit-Response type is NOT supported;
        if ( abstractOperation.getInput() == null )
            operationType = "Notification";
        else if (abstractOperation.getOutput() == null )
            operationType = "One-Way";
        else
            operationType="Request-Response";

        return operationType;
    }

    private void visitOperationFaultElements(String operationId, WsdlTypeTOperation abstractOperation, List faults) {
        for ( Object object : faults) {
            WsdlTypeTFault fault = (WsdlTypeTFault)object;

            String id = wsdlSymbols.addSymbol(getScope(), WSDLSymbols.SUBTYPE,  fault.getName(), fault);

            factWriter.createFault(id,
                    fault.getName(),
                    fault.getMessage().toString(),
                    operationId
                    );
        }
    }

    private void visitOperationOutputElement(String operationId, WsdlTypeTOperation abstractOperation,
                                             WsdlTypeTBindingOperationMessage output) {

        WsdlTypeTParam outputParam = abstractOperation.getOutput();

        String id = wsdlSymbols.addSymbol(getScope(), WSDLSymbols.SUBTYPE,  outputParam.getName(), outputParam);

        factWriter.createParameter(id,
                    outputParam.getName(),
                    outputParam.getMessage().toString(),
                    null,
                    operationId);

    }

    private void visitOperationInputElement(String operationId,
                                            WsdlTypeTOperation abstractOperation,
                                            WsdlTypeTBindingOperationMessage input) {

        WsdlTypeTParam inputParam = abstractOperation.getInput();

        String paramId = wsdlSymbols.addSymbol(getScope(), WSDLSymbols.SUBTYPE, inputParam.getName(), inputParam);

        factWriter.createParameter(paramId,
                        inputParam.getName(),
                        inputParam.getMessage().toString(),
                        operationId,
                        null);

        List<Element> inputBodyElements = input.getAny();
        for ( Element anyElement : inputBodyElements ) {
            if ( SOAPBody.isSOAPBodyElement(anyElement) ) {
                SOAPBody soapBody = new SOAPBody(anyElement);

                String soapBodyId = wsdlSymbols.addSymbol(getScope(), "BODY",
                                            inputParam.getName(), anyElement);


                factWriter.createSOAPBody(soapBodyId,
                        paramId,
                        soapBody.getNamespace(),
                        soapBody.getUse());
            }
        }

    }

    private void visitServiceElement(AnyTopLevelOptionalElementService serviceElement) {

        String serviceId = wsdlSymbols.addSymbol(getScope(), WSDLSymbols.TYPE_SERVICE,
                    serviceElement.getName(), serviceElement);

        factWriter.createService(serviceId, serviceElement.getName(), getDefinitionId());

        List<WsdlTypeTPort> ports = serviceElement.getPort();
        for ( WsdlTypeTPort port : ports ) {
            QName bindingRef = port.getBinding();

            AnyTopLevelOptionalElementBinding bindingElement =
                    wsdlSymbols.lookupBindingElement(bindingRef.getNamespaceURI(), bindingRef.getLocalPart());

            QName portTypeRef = bindingElement.getType();

            AnyTopLevelOptionalElementPortType portTypeElement =
                    wsdlSymbols.lookupPorttypeElement(portTypeRef.getNamespaceURI(), portTypeRef.getLocalPart());

            String portTypeId = wsdlSymbols.lookup(portTypeRef.getNamespaceURI(),
                        WSDLSymbols.TYPE_PORTTYPE, portTypeRef.getLocalPart());

            factWriter.createPortType(portTypeId,
                    portTypeElement.getName(),
                    "location?", // TODO - location attribute ???
                    getDefinitionId(),
                    serviceId);
        }



    }

    private void visitPortTypeElement(AnyTopLevelOptionalElementPortType portTypeElement) {

        String id = wsdlSymbols.addSymbol(getScope(), WSDLSymbols.TYPE_PORTTYPE, portTypeElement.getName(),
                    portTypeElement);
        // Currently, we do not write any facts for abstract PortTypes.

        try {
            scope.push(WSDLSymbols.TYPE_PORTTYPE + portTypeElement.getName());

            // handle abstract operation definitions
            List<WsdlTypeTOperation> operations = portTypeElement.getOperation();

            for ( WsdlTypeTOperation operation : operations ) {
                visitOperationElement(operation);
            }
        }
        finally {
         scope.pop();
        }
    }

    private void visitOperationElement(WsdlTypeTOperation operation) {
        // store abstract operation info for later concrete operation to use
        wsdlSymbols.addSymbol(getScope(), WSDLSymbols.SUBTYPE, operation.getName(), operation);
    }


    private void visitMessageElement(AnyTopLevelOptionalElementMessage messageElement) {

            String messageName;
            messageName = messageElement.getName();

            String messageId = wsdlSymbols.addSymbol(getScope(), WSDLSymbols.TYPE_MESSAGE, messageName,
                        messageElement);

            factWriter.createMessage(messageId, messageName, getDefinitionId());

            try {
                scope.push(WSDLSymbols.TYPE_MESSAGE + messageName);

                List<WsdlTypeTPart> list = messageElement.getPart();

                for ( WsdlTypeTPart part : list ) {

                    String partId = wsdlSymbols.addSymbol( getScope(), WSDLSymbols.SUBTYPE, part.getName(), part);

                    factWriter.createMessagePart(partId,
                                            part.getName(),
                                            part.getElement(),
                                            part.getType(),
                                            messageId);
                }
            }
            finally {
                scope.pop();
            }
    }

    private void visitTypesElement(AnyTopLevelOptionalElementTypes typeElement) {
        List<Element> list = typeElement.getAny();
        for ( Element element : list ) {
            XMLSchemaFactExtractor xmlSchemaExtractor = new XMLSchemaFactExtractor();
            xmlSchemaExtractor.extractFacts(element);
        }
    }

    private void visitImportElement(AnyTopLevelOptionalElementImport importElement) {
        String importId = wsdlSymbols.addSymbol(getScope(), WSDLSymbols.TYPE_IMPORT,
                                          importElement.getNamespace(), importElement);

        String definition = wsdlSymbols.lookup(getDefinitionNS());

        factWriter.createExternalImport(importId,
                importElement.getNamespace(),
                importElement.getLocation(),
                definition);

        String fileName = null;
        fileName = inputFile.getParentFile().getAbsolutePath() +
                                            File.separator +
                                            importElement.getLocation();
        System.out.println("fileName = " + fileName);

        WSDLFactExtractor factExtractor = new WSDLFactExtractor(fileName, factWriter);
        factExtractor.extractFacts(importElement.getNamespace());

    }

    private String getDefinitionId() {
        return wsdlSymbols.lookup(getDefinitionNS());
    }

    private String getDefinitionNS() {
        return getDefinitionElement().getTargetNamespace();
    }

    private WsdlElemDefinitions getDefinitionElement() {
        return elemDefinitions.peek();
    }

    private void setDefinitionElement(WsdlElemDefinitions currentDefinitionElement) {
        elemDefinitions.push(currentDefinitionElement);
    }

    static WSDLSymbols getWSDLSymbols() {
        return wsdlSymbols;
    }

    private static WSDLSymbols wsdlSymbols = new WSDLSymbols();

    private WSDLFactDAO factWriter;

    private Stack<WsdlElemDefinitions> elemDefinitions = new Stack<WsdlElemDefinitions>();

    private String getScope() {
        StringBuffer scopePath = new StringBuffer(2096);
        for (String s : scope )
                 scopePath.append(s);
        return scopePath.toString();
    }

    private Stack<String> scope = new Stack<String>();

    static final String SOAP_PROTOCOL = "SOAP";


}

class SOAPBody {

    public static boolean isSOAPBodyElement(Element element) {
        return element.getLocalName().equalsIgnoreCase("body");
    }

    public SOAPBody(Element soapElement) {
        parts = soapElement.getAttribute("parts");
        use = soapElement.getAttribute("use");
        encodingStyle = soapElement.getAttribute("encodingStyle");
        namespace = soapElement.getAttribute("namespace");
    }


    public String getParts() {
        return parts;
    }

    public String getUse() {
        return use;
    }

    public String getEncodingStyle() {
        return encodingStyle;
    }

    public String getNamespace() {
        return namespace;
    }

    private String parts;
    private String use;
    private String encodingStyle;
    private String namespace;
}

/**
 * TODO
*  - XSD type extractor
 * - no location attribute for PortType
 * - operation name vs operation signature (overloading)
 * - command line arguments : emit to output file
 * - automated testing with various wsdl documents*
 */
