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

import org.acre.extractor.jaxb.wsdl.AnyTopLevelOptionalElementPortType;
import org.acre.extractor.jaxb.wsdl.AnyTopLevelOptionalElementBinding;
import org.acre.extractor.jaxb.wsdl.WsdlTypeTOperation;

import java.util.*;

/**
 * User: rajmohan@sun.com
 * Date: Oct 15, 2004
 * Time: 3:49:57 PM
 */
class WSDLSymbols {

    /** flat symbol-table with type-prefixed keys to support WSDL-define name scopes as
     * defined in WSDL 1.1 Section 2
     * Symbol entry format is as follows:
     *  <namespace>&&TYPE&&<typeName>&&<subTypeNames...>
     *  TYPE could be PORTTYPE, MESSAGE, SERVICE, BINDING, PORT
     *  This allows for having same name across different definition types.
    */

    public Object lookupElement(String scope, String type, String name) {
        String key = scope + type + name;
        return elements.get(key);
    }

    public AnyTopLevelOptionalElementPortType lookupPorttypeElement(String namespace, String name) {
        StringBuffer key = new StringBuffer();
        key.append(namespace).append(TYPE_PORTTYPE).append(name);
        return (AnyTopLevelOptionalElementPortType) elements.get(key.toString());
    }

    public WsdlTypeTOperation lookupOperationElement(String namespace, String porttype, String operation) {
        StringBuffer key = new StringBuffer();
        key.append(namespace).append(TYPE_PORTTYPE).append(porttype).append(SUBTYPE).append(operation);
        return (WsdlTypeTOperation) elements.get(key.toString());
    }

    public AnyTopLevelOptionalElementBinding lookupBindingElement(String namespace, String name) {
        StringBuffer key = new StringBuffer();
        key.append(namespace).append(TYPE_BINDING).append(name);
        return (AnyTopLevelOptionalElementBinding) elements.get(key.toString());
    }

    public  String lookup(String scope, String type, String name) {
        String key = scope + type + name;
        return lookup(key);
    }

    public  String addSymbol(String scope, String type, String name, Object element) {
        return addSymbol(scope + type + name, element);
    }


    public  String addSymbol(String fullKey, Object element) {
        elements.put(fullKey, element);
        return addSymbol(fullKey);
    }

    public  String addSymbol(String key) {
        Long value;

        value = (Long) keys.get(key);
        if ( value == null ) {
            keys.put(key, value = uniqueKey++);
        }
        return value.toString();
    }

    public  String lookup(String key) {
        Long value;
        value = (Long) keys.get(key);
        if ( value != null )
            return value.toString();
        return null;
    }

    void printSymbolTable() {
        for ( String k : keys.keySet() )
            System.out.println(k);
    }

    private Map<String, Long> keys = new HashMap<String, Long>(24593);
    private Map<String, Object> elements = new HashMap<String, Object>(24593);

    static long uniqueKey = 1001; // TODO: Make this persistent
    static final String TYPE_BINDING = "&&BINDING&&";
    static final String TYPE_PORTTYPE ="&&PORTTYPE&&";
    static final String TYPE_MESSAGE = "&&MESSAGE&&";
    static final String TYPE_SERVICE = "&&SERVICE&&";
    static final String TYPE_IMPORT = "&&IMPORT&&";
    static final String SUBTYPE = "&&";

    // TODO: Map<String, WsdlSymbol> symbols = new HashMap<String, WsdlSymbol>(24593);


}
