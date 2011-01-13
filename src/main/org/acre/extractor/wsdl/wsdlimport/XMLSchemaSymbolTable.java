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

import java.util.Map;
import java.util.HashMap;

/**
 * User: rajmohan@sun.com
 */
class XMLSchemaSymbolTable {
    public XMLSchemaSymbolTable(String namespace) {
        this.namespace = namespace;
    }

    public TypeDefinitionSymbol lookupType(String name) {
        return globalTypeDefinitions.get(name);
    }
    public void addTypeDefinition(String name, Object value) {
        TypeDefinitionSymbol symbol = new TypeDefinitionSymbol(name, "UNIQUE ID", value);
        globalTypeDefinitions.put(name, symbol);
    }


    public ElementDeclarationSymbol lookupElement(String name) {
        return globalElemDeclarations.get(name);
    }
    public void addElementDeclaration(String name, Object value) {
        ElementDeclarationSymbol symbol = new ElementDeclarationSymbol(name, "UNIQUE_ID", value);
        globalElemDeclarations.put(name, symbol);
    }


    public AttributeDeclarationSymbol lookupAttribute(String name) {
        return globalAttributeDeclarations.get(name);
    }
    public void addAttributeDeclaration(String name, Object value) {
        AttributeDeclarationSymbol symbol = new AttributeDeclarationSymbol(name, "UNIQUE_ID", value);
        globalAttributeDeclarations.put(name, symbol);
    }

    public String getNamespace() {
        return namespace;
    }

    private String id;
    private String namespace;

    private Map<String, XMLSchemaSymbolTable> imports = new HashMap<String, XMLSchemaSymbolTable>();

    private Map<String, TypeDefinitionSymbol> globalTypeDefinitions =
                                          new HashMap<String, TypeDefinitionSymbol>();

    private Map<String, ElementDeclarationSymbol> globalElemDeclarations =
                                          new HashMap<String, ElementDeclarationSymbol>();


    private Map<String, AttributeDeclarationSymbol> globalAttributeDeclarations =
                                          new HashMap<String, AttributeDeclarationSymbol>();

    abstract class Symbol {
        public Symbol(String id, String name, Object value) {
            this.id = id;
            this.name = name;
            this.value = value;
        }
        String id;
        String name;
        Object value;
    }

    class TypeDefinitionSymbol extends Symbol {
        public TypeDefinitionSymbol(String id, String name, Object value) {
            super(id, name, value);
        }
    }

    class ElementDeclarationSymbol extends Symbol {
        public ElementDeclarationSymbol(String id, String name, Object value) {
            super(id, name, value);
        }
    }

    class AttributeDeclarationSymbol extends Symbol {
        public AttributeDeclarationSymbol(String id, String name, Object value) {
            super(id, name, value);
        }
    }
}
