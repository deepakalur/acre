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
package org.acre.model.metamodel;

import org.acre.common.AcreStringUtil;
import org.acre.config.ConfigService;
import org.acre.lang.analyser.Expression;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author rajmohan@Sun.com
 */
public class XMLFactMetaModel {
    private static XMLFactMetaModel instance;

    private MetaModel[] models;
    private Map cache;
    public Map metaModelExpressions;
    public Map metaTypeExpressions;
    public Set implicitAttributes;
    public Set keywords = new HashSet();

    private List<RelationshipDefn> relationships = new ArrayList<RelationshipDefn>();

    public static XMLFactMetaModel getInstance() {
        if (instance == null) {
            if (ConfigService.getInstance().getAcreRepositoryDirectory().trim().length() == 0)
                ConfigService.getInstance().setAcreRepositoryDirectory("./src/main/");
            instance = new XMLFactMetaModel();
        }
        return instance;
    }


    private XMLFactMetaModel() {
        String salsaRootPrefix = ConfigService.getInstance().getAcreRepositoryDirectory();
        if (salsaRootPrefix == null || salsaRootPrefix.trim().length() == 0) {
            throw new IllegalAccessError("Root directory not initialized: " + salsaRootPrefix);
        }

        String factModelRep = ConfigService.getInstance().getAcreRoot() + "/Global/FactModelRepository/";
        cache = new HashMap();
        try {
            Properties p = new Properties();
            p.load(new BufferedInputStream(new FileInputStream(
                    factModelRep+
                    "Models.properties")));
            models = new MetaModel[p.size()];
            int i = 0;
            for (Iterator iter = p.entrySet().iterator(); iter.hasNext();) {
                Map.Entry element = (Map.Entry) iter.next();
                String bosFileName = (String) element.getKey();
                String bosDesc = (String) element.getValue();
                models[i] = loadModel(factModelRep+bosFileName);
                if (models[i].getName() == null || models[i].getName().trim().length() == 0) {
                    models[i].setName(bosFileName.substring(0, bosFileName.indexOf('.')));
                }
                //models[i].setComments(bosDesc);
                i++;
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading Fact Models", e);
        }
        implicitAttributes = new HashSet();
        implicitAttributes.add("system");
        implicitAttributes.add("project");
        implicitAttributes.add("version");
        implicitAttributes.add("source");

        initKeywords();
        loadMetaModels();
    }

    private void initKeywords() {
        //TODO: get this list from grammar file: $SALSA/grammar/PQL.grammar
        keywords.add("abs");
        keywords.add("alias");
        keywords.add("all");
        keywords.add("andthen");
        keywords.add("and");
        keywords.add("any");
        keywords.add("array");
        keywords.add("as");
        keywords.add("asc");
        keywords.add("avg");
        keywords.add("bag");
        keywords.add("boolean");
        keywords.add("by");
        keywords.add("count");
        keywords.add("char");
        keywords.add("date");
        keywords.add("define");
        keywords.add("desc");
        keywords.add("dictionary");
        keywords.add("distinct");
        keywords.add("double");
        keywords.add("element");
        keywords.add("enum");
        keywords.add("except");
        keywords.add("exists");
        keywords.add("false");
        keywords.add("first");
        keywords.add("flatten");
        keywords.add("float");
        keywords.add("for");
        keywords.add("from");
        keywords.add("group");
        keywords.add("having");
        keywords.add("include");
        keywords.add("instanceof");
        keywords.add("intersect");
        keywords.add("interval");
        keywords.add("in");
        keywords.add("is_defined");
        keywords.add("is_undefined");
        keywords.add("last");
        keywords.add("like");
        keywords.add("rlike");
        keywords.add("listtoset");
        keywords.add("list");
        keywords.add("long");
        keywords.add("max");
        keywords.add("mod");
        keywords.add("min");
        keywords.add("nil");
        keywords.add("not");
        keywords.add("octet");
        keywords.add("order");
        keywords.add("orelse");
        keywords.add("or");
        keywords.add("return");
        keywords.add("tquery");
        keywords.add("select");
        keywords.add("set");
        keywords.add("some");
        keywords.add("short");
        keywords.add("string");
        keywords.add("struct");
        keywords.add("sum");
        keywords.add("timestamp");
        keywords.add("time");
        keywords.add("true");
        keywords.add("undefined");
        keywords.add("undefine");
        keywords.add("union");
        keywords.add("unique");
        keywords.add("unsigned");
        keywords.add("where");
    }

    public boolean isPQLKeyword(String name) {
        return keywords.contains(name.toLowerCase());
    }

    public boolean isMetaType(String name) {
        if (name == null)
            return false;
        return metaTypeExpressions.get(name.toLowerCase()) != null;
    }

    public boolean isImplicitAttribute(String name) {
        return implicitAttributes.contains(name);
    }

    public Expression getMetaModelExpression(String name) {
        if (name == null)
            return null;
        Expression result = (Expression) metaModelExpressions.get(name.toLowerCase());
        if (result != null)
            result = (Expression) result.clone();
        return result;
    }

    public Expression getMetaTypeExpression(String name) {
        if (name == null)
            return null;
        Expression result = (Expression) metaTypeExpressions.get(name.toLowerCase());
        if (result != null)
            result = (Expression) result.clone();
        return result;
    }

    private void loadMetaModels() {
        metaModelExpressions = new HashMap();
        metaTypeExpressions = new HashMap();
        for (int i = 0; i < models.length; i++) {
            loadMetaModelExpression(models[i]);
            for (int j = 0; j < models[i].getMetaTypes().size(); j++) {
                loadMetaTypeExpression(models[i].getMetaTypes(j));
            }
        }
//        for (int j = 0; j < models.length; j++) {
//            for (int i = 0; i < Translate.PREQL_PROLOGUE.length; i++) {
//                String varName = Translate.PREQL_PROLOGUE[i];
//                int pos = varName.indexOf('=');
//                if (pos > -1) {
//                    varName = varName.substring(0, pos).trim();
//                    loadPredefineMetaVariable(models[j], varName);
//                }
//            }
//        }
    }

    private void loadMetaModelExpression(MetaModel model) {
        Expression e = Expression.makeExpression(model);
        String name = model.getName().toLowerCase();
        if (metaModelExpressions.get(name) == null) {
            metaModelExpressions.put(name, e);
        } else {
            throw new RuntimeException("Error loading Fact Models: "+ model.getName() + " already loaded");
        }
    }
    private void loadMetaTypeExpression(MetaType type) {
        Expression e = Expression.makeExpression(type.getName(), type.getName());
        String name = type.getName().toLowerCase();
        String modelName = type.getMetaModel().getName().toLowerCase();
        String mappedName = type.getMappedName().toLowerCase();
        if (e != null) {
            if (metaTypeExpressions.get(name) == null) {
                metaTypeExpressions.put(name, e);
            } else {
                metaTypeExpressions.remove(name);
            }
            if (!name.equals(mappedName)) {
            if (metaTypeExpressions.get(mappedName) == null) {
                metaTypeExpressions.put(mappedName, e);
            } else {
                metaTypeExpressions.remove(mappedName);
            }
            }
            metaTypeExpressions.put(modelName + "." + name, e);
            metaTypeExpressions.put(modelName + "." + mappedName, e);
        }
    }
//    public Expression loadPredefineMetaVariable(MetaModel metaModel, String name) {
//        Expression e = Expression.makeExpression(name, name);
//            //ScriptModel.makeMetaTypeExpression(metaModel, name);
//        if (e != null) {
//            metaTypeExpressions.put(e.getName(), e);
//        }
//        return e;
//    }



    public MetaModel loadModel(String fileName) throws IOException {

        try {
            XMLInputFactory parserFactory = null ;
            parserFactory = XMLInputFactory.newInstance();

            InputStream is = new BufferedInputStream(new FileInputStream(fileName));
            XMLStreamReader parser = parserFactory.createXMLStreamReader(is);
            int eventType;

            eventType = parser.nextTag();
            if ( !"Model".equalsIgnoreCase(parser.getName().toString()) ) {
                throw new RuntimeException("Syntax Error in XML MetaModel - missing Model element");
            }

            MetaModel metaModel = new MetaModel_Bean();
            String metaModelName = parser.getAttributeValue(null, "type");
            metaModel.setName(metaModelName);
            while(parser.hasNext()){
                eventType = parser.next();

                if ( parser.isStartElement() ) {
                    MetaType type = makeMetaType(parser);
                    type.setMetaModel(metaModel);
                    metaModel.insertIntoMetaTypes(type);
                }
                else if ( parser.isEndElement() ) {
                    System.out.println("EndElement: " + parser.getName());
                }
            }
            resolveMetaRelationships(metaModel);
            return metaModel;
        } catch (XMLStreamException e) {
            throw new RuntimeException("Error parsing MetaModel information : " + e.getMessage(), e);
        }
    }

    private MetaType makeMetaType(XMLStreamReader parser) throws XMLStreamException {

        if ( !"Type".equalsIgnoreCase(parser.getName().toString()) ) {
              throw new RuntimeException("Syntax Error in XML MetaModel - missing Type element");
        }

        MetaType type = null;
        type = new MetaType_Bean();
        String typeName = parser.getAttributeValue(null, "name");
        type.setName(typeName);
        type.setMappedName(PopulateMetaModel.getMappedName(typeName));

        while ( parser.hasNext() ) {
            parser.nextTag();

            if ( parser.isEndElement()) {
                if ( "Type".equalsIgnoreCase(parser.getName().toString()))
                    break;
            }
            else if ( parser.isStartElement() ) {
                if ( "Attribute".equalsIgnoreCase(parser.getName().toString()) ) {
                    MetaAttribute attribute = makeMetaAttribute(parser);
                    type.insertIntoMetaAttributes(attribute);
                }
                else  if ( "Relationship".equalsIgnoreCase(parser.getName().toString()))
                    processMetaRelationship(parser, type.getName());
            }
        }

        return type;
    }

    private MetaAttribute makeMetaAttribute(XMLStreamReader parser) {
            MetaAttribute attribute = null;
            attribute = new MetaAttribute_Bean();

            String attrName = parser.getAttributeValue(null, "name");
            String attrType = parser.getAttributeValue(null, "type");

            attribute.setName(attrName);
            attribute.setMappedName(PopulateMetaModel.getMappedName(attrName));
            attribute.setType(attrType);
            return attribute;
    }

    // resolve forward & inverse relationship references to MetaTypes
    private void resolveMetaRelationships(MetaModel metaModel) {
        for ( RelationshipDefn r : relationships ) {

            MetaType parentType = metaModel.lookupInMetaTypes(r.parentType);
            if (parentType == null) {
                System.err.println("Could not find MetaType '" + r.parentType +"' for relationship " +
                        r.forwardRelationship.getName());
            }

            MetaType childType = metaModel.lookupInMetaTypes(r.childType);
            if (childType == null) {
                System.err.println("Could not find MetaType '" + r.childType + "' for relationship "
                        + r.forwardRelationship.getName());
            }


            r.forwardRelationship.setPointerMetaType(childType);
            parentType.insertIntoMetaRelationships(r.forwardRelationship);


            if ( r.inverseRelationship != null ) {
                r.inverseRelationship.setPointerMetaType(parentType);
                childType.insertIntoMetaRelationships(r.inverseRelationship);
            }
        }
    }

    private void processMetaRelationship(XMLStreamReader parser, String type)
                throws XMLStreamException {

            String assocType = parser.getAttributeValue(null, "assocType");

            parser.nextTag();

            String fwdName = null, invName = null;
            boolean fwdCardinality = false, invCardinality=false;

            String relationshipType = parser.getName().toString();
            if ( "Forward".equalsIgnoreCase(relationshipType) )  {
                fwdName = parser.getAttributeValue(null, "name");
                fwdCardinality = parser.getAttributeValue(null, "cardinality").equalsIgnoreCase("1") ? false : true;

                parser.nextTag();  // forward end tag
                parser.nextTag();  // inverse tag
                relationshipType = parser.getName().toString();
            }

            if ( "Inverse".equalsIgnoreCase(relationshipType) ) {
                invName = parser.getAttributeValue(null, "name");
                invCardinality = parser.getAttributeValue(null, "cardinality").equalsIgnoreCase("1") ? false : true;
            }


            if ( AcreStringUtil.isEmpty(fwdName) && AcreStringUtil.isEmpty(invName))
                return;

/**
            MetaType parentType = factModel.lookupInMetaTypes(type);
            MetaType childType = factModel.lookupInMetaTypes(assocType);
            if (parentType == null) {
                System.err.println("Could not find MetaType '" + type +"' for relationship " + fwdName);
            }
            if (childType == null) {
                System.err.println("Could not find MetaType '" + assocType + "' for relationship " + fwdName);
            }
*/
            MetaRelationship fwdRelationship, invRelationship;

            RelationshipDefn relationship = new RelationshipDefn();
            relationship.parentType = type;
            relationship.childType = assocType;

            //for forward side
            if (!AcreStringUtil.isEmpty(fwdName)) {
                fwdRelationship = new MetaRelationship_Bean();
                fwdRelationship.setName(fwdName);
                fwdRelationship.setMappedName(PopulateMetaModel.getMappedRelationName(type, fwdName));
                fwdRelationship.setInverseName(invName);
//                fwdRelationship.setPointerMetaType(childType);
                fwdRelationship.setCollection(fwdCardinality);
                fwdRelationship.setPrimarySide(true);
//                parentType.insertIntoMetaRelationships(fwdRelationship);
                relationship.forwardRelationship = fwdRelationship;
            }
            //for reverse side
            if (!AcreStringUtil.isEmpty(invName) ) {
                invRelationship = new MetaRelationship_Bean();
                invRelationship.setName(invName);
                invRelationship.setMappedName(PopulateMetaModel.getMappedRelationName(assocType, invName));
                invRelationship.setInverseName(fwdName);
//                invRelationship.setPointerMetaType(parentType);
                invRelationship.setCollection(invCardinality);
//                childType.insertIntoMetaRelationships(invRelationship);
                relationship.inverseRelationship = invRelationship;
            }

            relationships.add(relationship);
    }

    public static void printXML(MetaModel model) {
        System.out.println("MODEL: "+model.getName());
        for (int i = 0; i < model.getMetaTypes().size(); i++) {
            MetaType type = model.getMetaTypes(i);
            //System.out.println(type);
            System.out.println("<Type name=\"" + type.getName() + "\">");

            for (int j = 0; j < type.getMetaAttributes().size(); j++) {
                MetaAttribute attribute = type.getMetaAttributes(j);

                System.out.println("     <Attribute name=\""+attribute.getName() + "\" type=\"" + attribute.getType()+"\"/>");
            }
            for (int j = 0; j < type.getMetaRelationships().size(); j++) {
                MetaRelationship relationship = type.getMetaRelationships(j);
                if ( relationship.isPrimarySide() ) {
                    System.out.println("     <Relationship assocType=" + quote(relationship.getPointerMetaType().getName())+ ">");
                    System.out.println("          <Forward name=" + quote(relationship.getName()) +
                                                    " cardinality=" + quote(relationship.isCollection() ? "n" : "1" ) + "/>");

                    model.lookupInMetaTypes(relationship.getInverseName());
                    System.out.println("          <Inverse name=" + quote(relationship.getInverseName()) +
                                                    " cardinality=" + quote("1") + "/>");

                    System.out.println("     </Relationship>");
                }
            }
            System.out.println("</Type>");
        }
    }

    private static String quote(String str) {
        return "\""+str+"\"";
    }

    public void  clearCache() {
        cache.clear();
    }

    public MetaType lookupMetaType(String name) {
        MetaType type = (MetaType) cache.get(name);
        if (type != null) {
            return type;
        }
        List result = new ArrayList();
        for (int i = 0; i < models.length; i++) {
            type = models[i].lookupInMetaTypes(name);
            if (type != null) {
                result.add(type);
            }
        }

        if (result.size() == 0) {
            throw new IllegalArgumentException("Cannot find MetaType: " + name);
        } else if (result.size() > 1) {
            String error = "Multiple MetaTypes found with " + name + ": \n";
            for (Iterator iter = result.iterator(); iter.hasNext();) {
                type = (MetaType) iter.next();
                error += "  " + type.getMetaModel().getName() + "." + type.getName() + "\n";
            }
            throw new IllegalArgumentException(error);
        } else {
            type = (MetaType) result.get(0);
            cache.put(name, type);
            return type;
        }
    }

    public MetaModel[] getModels() {
        return models;
    }

    public static MetaAttribute lookupAttribute(MetaType parentType, String name) {
        /*
        for (int i = 0; i < parentType.getMetaAttributes().size(); i++) {
            MetaAttribute attribute = parentType.getMetaAttributes(i);
            if (name.equalsIgnoreCase(attribute.getName())
            //||name.equals(attribute.getMappedName())
                ) {
                return attribute;
            }
        }
        return null;
        */
        return parentType.lookupInMetaAttributes(name);
    }
    public static MetaRelationship lookupRelationship(MetaType parentType, String name) {
        /*
        for (int i = 0; i < parentType.getMetaRelationships().size(); i++) {
            MetaRelationship relationship = parentType.getMetaRelationships(i);
            if (name.equalsIgnoreCase(relationship.getName())
            //||name.equals(relationship.getMappedName())
                ) {
                return relationship;
            }
        }
        return null;
        */
        return parentType.lookupInMetaRelationships(name);
    }


    public static void main(String[] args) {
        MetaModel[] mms = XMLFactMetaModel.getInstance().getModels();
        for (int i = 0; i < mms.length; i++) {
            printXML(mms[i]);
        }
    }

    class RelationshipDefn {
        String parentType;
        String childType;
        MetaRelationship forwardRelationship;
        MetaRelationship inverseRelationship;
    }



}
