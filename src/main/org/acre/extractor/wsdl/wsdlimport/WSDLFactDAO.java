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

import javax.xml.namespace.QName;
import java.io.*;

/**
 * User: rajmohan@sun.com
 * Date: Sep 20, 2004
 * Time: 6:15:13 PM
 */
public class WSDLFactDAO {

    public WSDLFactDAO(String fileName) {
        try {
            out = new PrintWriter(writer = new FileWriter("wsdlFacts.sql"));
        } catch (IOException e) {
            throw new RuntimeException ("WSDLFactDAO : Unable to create file "
                    + fileName + e);
        }
    }

    public void createDefinition(String id, String name, String targetNamespace) {
        String [] columns = new String[]{"id", "name", "targetNamespace"};
        String [] values = new String[] {id, quoted(name), quoted(targetNamespace)};

        createInsertStatement(TABLE_DEFINITION, columns, values);
    }

    public void createExternalImport(String id, String namespace, String location, String definition) {

        String [] columns = new String[] {"id, namespace, location, definition"};
        String [] values = new String[] {id,
                                         quoted(namespace),
                                         quoted(location),
                                         definition};

        createInsertStatement(TABLE_EXTERNALIMPORT, columns, values);
    }

    public void createMessage(String id, String messageName, String definition) {

        String [] columns = new String[] {"id", "name, definition"};
        String [] values = new String[] {id, quoted(messageName), definition};

        createInsertStatement(TABLE_MESSAGE, columns, values);
    }

    public void createInsertStatement(String tableName, String names[], String values[]) {
        StringBuffer insertString = new StringBuffer(JREPLACE);
        insertString.append(" " + tableName );
        insertString.append ( " ( " );
        insertString.append(names[0]);
        for ( int columnIdx=1; columnIdx < names.length; columnIdx++) {
            insertString.append(", " + names[columnIdx]);
        }
        insertString.append ( " ) " );
        insertString.append ( " VALUES ( " );
        insertString.append(values[0]);
        for ( int valueIdx = 1; valueIdx < values.length; valueIdx++) {
            insertString.append(", " + values[valueIdx]);
        }
        insertString.append ( " ); " );

        out.println(insertString.toString());
        out.flush();
    }

    public void createMessagePart(String id, String partName, QName partElement, QName partType, String messageId) {
        String [] columns = new String[] {"id, name, element, type, message"};
        String [] values = new String[] {id,
                                         quoted(partName),
                                         (partElement!=null ? quoted(partElement.toString()) : null),
                                         (partType != null ? quoted(partType.toString()) : null ),
                                         messageId };

        createInsertStatement(TABLE_PART, columns, values);
    }

    public void createPortType(String id, String portName, String location, String definitionId, String serviceId) {
        String [] columns = new String[] {"id, name, location, definition, service"};
        String [] values = new String[] {id,
                                         quoted(portName),
                                         quoted(location),
                                         definitionId,
                                         serviceId};

        createInsertStatement(TABLE_PORTTYPE, columns, values);
    }

    public void createSOAPOperation(String id,
                                           String operationName,
                                           String parameterOrder,
                                           String type,
                                           String protocol,
                                           String soapAction,
                                           String style,
                                           String portTypeId,
                                           String bindingId) {


        String [] columns = new String[] {"id, name, parameterOrder, type, protocol," +
                "soapAction, style, portType, binding"};
        String [] values = new String[] {id,
                                         quoted(operationName),
                                         quoted(parameterOrder),
                                         quoted(type),
                                         quoted(protocol),
                                         quoted(soapAction),
                                         quoted(style),
                                         portTypeId,
                                         bindingId};

        createInsertStatement(TABLE_OPERATION, columns, values);
    }

    public void createBinding(String id, String bindingName, String portType, String protocol,
                        String transport, String style, String definitionId) {

        String [] columns = new String[] {"id, name, type, protocol, transport, style, definition"};
        String [] values = new String[] {
                                         id,
                                         quoted(bindingName),
                                         portType,
                                         quoted(protocol),
                                         quoted(transport),
                                         quoted(style),
                                         definitionId};

        createInsertStatement(TABLE_BINDING, columns, values);
    }

    public void createParameter(String id, String paramName, String messageName, String inOperationId, String outOperationId) {

        String [] columns = new String[] {"id, name, message, inOperation, outOperation"};
        String [] values = new String[] {id,
                                         quoted(paramName),
                                         quoted(messageName),
                                         inOperationId,
                                         outOperationId};

        createInsertStatement(TABLE_PARAMETER, columns, values);
    }

    public void createFault(String id, String name, String message, String operationId) {

        String [] columns = new String[] {"id, name, message, operation"};
        String [] values = new String[] {id, quoted(name), quoted(message), operationId};

        createInsertStatement(TABLE_FAULT, columns, values);

    }

    public void createSOAPBody(String id, String parameterId, String namespace, String use) {
        String [] columns = new String[] {"id, namespace, use1, parameter"};
        String [] values = new String[] {id,
                                         quoted(namespace),
                                         quoted(use),
                                         parameterId};

        createInsertStatement(TABLE_BODY, columns, values);
    }


    public void createService(String id, String serviceName, String definition) {
        String [] columns = new String[] {"id, name, definition"};
        String [] values = new String[] {id,
                                         quoted(serviceName),
                                         definition
                                            };

        createInsertStatement(TABLE_SERVICE, columns, values);

    }

    private static String quoted(String name) {
        if (null == name) {
            return "NULL";
        } else {
            return "'" + name.replace("'", "\\'") + "' ";
        }
    }


    public void finish() {
        try {
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException("Unable to close file :" + e.getMessage());
        }
    }

    private static final String TABLE_SERVICE = "Service";
    private static final String JREPLACE = "REPLACE INTO ";
    private static final String TABLE_DEFINITION = "Definition";
    private static final String TABLE_EXTERNALIMPORT = "ExternalImport";
    private static final String TABLE_MESSAGE = "Message";
    private static final String TABLE_OPERATION = "Operation";
    private static final String TABLE_PORTTYPE = "PortType";
    private static final String TABLE_BINDING = "Binding";
    private static final String TABLE_PART = "Part";
    private static final String TABLE_PARAMETER = "Parameter";
    private static final String TABLE_FAULT = "Fault";
    private static final String TABLE_BODY = "Body";

    private PrintWriter out;
    private Writer writer;


}
