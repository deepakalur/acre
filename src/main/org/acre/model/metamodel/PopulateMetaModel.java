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

/**
 *  Populates in memory meta model for PQL parser.
 *
 *@author    Syed Ali
 */
public class PopulateMetaModel {

//    BOModel jmodel;
//    MetaModel model;

    public static final String ANNOTATIONS = "annotations";
    public static final String ANNOTATION = "annotation";
    public static final String ANNOTATION_ARGUMENTS = "annotation_arguments";
    
    public static final String ARGUMENTS = "arguments";
    public static final String CALLS = "calls";
    public static final String CALLERS = "callers";
    public static final String CAUGHTBY = "caughtBy";
    public static final String CATCHESEXCEPTIONS = "catchesExceptions";
    public static final String CLASSES = "classes";
    public static final String INTERFACES = "interfaces";
    public static final String CONTAINS = "contains";
    public static final String CONTAININGCLASS = "containingClass";
    public static final String EXTENDSCLASS = "extendsClass";
    public static final String EXTENDINGCLASSES = "extendingClasses";
    public static final String FIELDS = "fields";
    public static final String IMPLEMENTSINTERFACES = "implementsInterfaces";
    public static final String IMPLEMENTINGCLASSES = "implementingClasses";
    public static final String INSTANTIATES = "instantiates";
    public static final String INSTANTIATEDBY = "instantiatedBy";
    public static final String METHOD = "method";
    public static final String METHODS = "methods";
    public static final String PACKAGES = "packages";
    public static final String PARAMETERS = "parameters";
    public static final String PARENTPACKAGE = "parentPackage";
    public static final String PARENTCLASS = "parentClass";
    public static final String PARENTMETHOD = "parentMethod";
    public static final String PARENTFIELD = "parentField";
    public static final String PARENTPARAMETER = "parentParameter";
    public static final String RETURNEDBY = "returnedBy";
    public static final String RETURNTYPE = "returnType";
    public static final String THROWNBY = "thrownBy";
    public static final String TYPE = "type";
    public static final String TYPEFIELDS = "typeFields";
    public static final String TYPEPARAMETERS = "typeParameters";
    public static final String TYPEANNOTATIONS = "typeAnnotations";
    public static final String TYPEANNOTATIONARGUMENTS = "typeAnnotationArguments";
    public static final String THROWSEXCEPTIONS = "throwsExceptions";
    public static final String USEDFIELDS = "usedFields";
    public static final String USEDBYMETHODS = "usedByMethods";

    private static final boolean VERBOSE = true;
    private static String[][] mapping = new String[][] {
        //for extents
        {"JPackage",            PACKAGES},
        {"JClass",              CLASSES},
        {"JInterface",          INTERFACES},
        {"JMethod",             METHODS},
        {"JField",              FIELDS},
        {"JParameter",          PARAMETERS},
        {"JAnnotation",         ANNOTATIONS},
        {"JAnnotationArgument", ANNOTATION_ARGUMENTS}
    };


    private static String[][] relationNameMapping = new String[][] {
        //for JPackage relationships
        {"JPackage",    "JClass",       CLASSES,                 "contain"},
        {"JPackage",    "JPackage",     PACKAGES,                 "contain"},
        {"JPackage",    "JPackage",     PARENTPACKAGE,             "inv(contain)"},        

        //for JClass relationships
        {"JClass",      "JPackage",     PARENTPACKAGE,           "inv(contain)"},
        {"JClass",      "JClass",       IMPLEMENTSINTERFACES,    "implements"},
        {"JClass",      "JClass",       IMPLEMENTINGCLASSES,     "inv(implements)"},
        {"JClass",      "JClass",       EXTENDSCLASS,            "extends"},
        {"JClass",      "JClass",       EXTENDINGCLASSES,        "inv(extends)"},
        {"JClass",      "JClass",       CONTAINS,                "contain"}, //for inner classes
        {"JClass",      "JClass",       CONTAININGCLASS,         "inv(contain)"},
        {"JClass",      "JMethod",      METHODS,                 "contain"},
        {"JClass",      "JField",       FIELDS,                  "contain"},
        {"JClass",      "JAnnotation",  ANNOTATIONS,             "hasAnnotations"},
        {"JClass",      "JMethod",      RETURNEDBY,              "inv(isOfType)"},
        {"JClass",      "JMethod",      INSTANTIATEDBY,          "inv(instantiates)"},
        {"JClass",      "JMethod",      THROWNBY,                "inv(throws)"},
        {"JClass",      "JMethod",      CAUGHTBY,                "inv(catches)"},
        {"JClass",      "JField",       TYPEFIELDS,              "inv(isOfType)"},
        {"JClass",      "JParameter",   TYPEPARAMETERS,          "inv(isOfType)"},
        {"JClass",      "JAnnotation",  TYPEANNOTATIONS,         "inv(isOfType)"},
        {"JClass","JAnnotationArgument",TYPEANNOTATIONARGUMENTS, "inv(isOfType)"},

        //for JMethod relationships
        {"JMethod",     "JClass",       RETURNTYPE,              "isOfType"},
        {"JMethod",     "JClass",       INSTANTIATES,            "instantiates"},
        {"JMethod",     "JClass",       THROWSEXCEPTIONS,        "throws"},
        {"JMethod",     "JClass",       CATCHESEXCEPTIONS,       "catches"},
        {"JMethod",     "JMethod",      CALLS,                   "calls"}, //for called methods
        {"JMethod",     "JMethod",      CALLERS,                 "inv(calls)"},
        {"JMethod",     "JField",       USEDFIELDS,              "uses"},
        {"JMethod",     "JParameter",   PARAMETERS,              "hasParam"},
        {"JMethod",     "JAnnotation",  ANNOTATIONS,             "hasAnnotations"},
        //{"JMethod","JAnnotationArgument","annotationArgument", "inv(isOfType)"},
        //for JField relationships
        {"JField",      "JClass",       TYPE,                    "isOfType"},
        {"JField",      "JMethod",      USEDBYMETHODS,           "inv(uses)"},
        {"JField",      "JAnnotation",  ANNOTATIONS,             "hasAnnotations"},
        //for JField & JMethod relationships
        {"JMethod",     "JClass",       PARENTCLASS,             "inv(contain)"},
        {"JField",      "JClass",       PARENTCLASS,             "inv(contain)"},
        //for JParameter relationships
        {"JParameter",  "JClass",       TYPE,                  "isOfType"},
        {"JParameter",  "JMethod",      METHOD,                "inv(hasParam)"},
        {"JParameter",  "JAnnotation",  ANNOTATIONS,           "hasAnnotations"},
        //for JAnnotation relationships
        {"JAnnotation",  "JClass",      PARENTCLASS,           "inv(hasAnnotations)"},
        {"JAnnotation",  "JMethod",     PARENTMETHOD,          "inv(hasAnnotations)"},
        {"JAnnotation",  "JField",      PARENTFIELD,           "inv(hasAnnotations)"},
        {"JAnnotation",  "JParameter",  PARENTPARAMETER,       "inv(hasAnnotations)"},
        {"JAnnotation",  "JClass",      TYPE,                  "isOfType"},
        {"JAnnotation",  "JAnnotationArgument",ARGUMENTS,      "hasAnnotationArgument"},
        //for JAnnotationArgument relationships
        {"JAnnotationArgument","JClass",TYPE,                  "isOfType"},
        {"JAnnotationArgument","JAnnotation", ANNOTATION,      "inv(hasAnnotationArgument)"},
    };

    private static String[][] operatorOperands = new String[][] {
        {"instanceOf", "JClass", "String"},
    };

    /**
     * Returns PQL entity name for ql name
     * @param    ql name
     * @return    pql name
     */
    public static String getMappedName(String name) {
        if (name != null) {
            for (int i = 0; i < mapping.length; i++) {
                if (name.equals(mapping[i][0])) {
                    return mapping[i][1];
                }
            }
        }
        return name;
    }

    /**
     * Returns PQL entity name for caseless ql name
     * @param    caseless ql name
     * @return    pql name
     */
    public static String getMappedName1(String name) {
        if (name != null) {
            for (int i = 0; i < mapping.length; i++) {
                if (name.equalsIgnoreCase(mapping[i][0])) {
                    return mapping[i][1];
                }
            }
        }
        return name;
    }

    /**
     * Returns QL entity name for PQL name
     * @param    pql name
     * @return    ql name
     */
    private static String getReverseName(String name) {
        if (name != null) {
            for (int i = 0; i < mapping.length; i++) {
                if (name.equals(mapping[i][1])) {
                    return mapping[i][0];
                }
            }
        }
        return name;
    }

    /**
     * Returns PQL entity relationship name for ql relationship name
     * @param relName TODO
     * @param    ql relationship name
     * @return    pql relationship name
     */
    public static String getMappedRelationName(String typeName, String relName) {
        //SMA: Hack ast the INTERFACES doesn't have any relationship mapping
        if (INTERFACES.equals(typeName)) {
            typeName = CLASSES;
        }
        if (typeName != null) {
            for (int i = 0; i < relationNameMapping.length; i++) {
                if (typeName.equals(relationNameMapping[i][0]) && relName.equals(relationNameMapping[i][2])) {
                    return relationNameMapping[i][3];
                }
            }
        }
        return typeName;
    }

    /**
     * Returns QL entity relationship name for PQL relationship name
     * @param    pql relationship name
     * @return    ql relationship name
     */
    public static String getReverseRelationName(String from, String to, String name) {
        //SMA: Hack ast the INTERFACES doesn't have any relationship mapping
        if (INTERFACES.equals(from)) {
            from = CLASSES;
        }
        if (INTERFACES.equals(to)) {
            to = CLASSES;
        }
        if (name != null) {
            for (int i = 0; i < relationNameMapping.length; i++) {
                if (
                //from.equalsIgnoreCase(relationNameMapping[i][0])
                to.equalsIgnoreCase(relationNameMapping[i][1])
                && name.equals(relationNameMapping[i][3])) {
                    return relationNameMapping[i][2];
                }
            }
        }
        return name;
    }

    public static boolean isOperatorValid(String operator, String operand1, String operand2) {
        boolean result = true;
        if (operator != null && (operand1 != null && operand2 != null)) {
            for (int i = 0; i < operatorOperands.length; i++) {
                if (operator.equals(operatorOperands[i][0])) {
                    result = false;
                    if ((operand1.equals(operatorOperands[i][1]) && operand2.equals(operatorOperands[i][2]))
                        || (operand2.equals(operatorOperands[i][1]) && operand1.equals(operatorOperands[i][2]))
                    ) {
                        return true;
                    }
                }
            }
        }
        return result;
    }

//    public PopulateMetaModel(BOModel model) {
//        jmodel = model;
//    }
/*
    public void run() {
        if (jmodel != null) {
            model = new MetaModel_Bean();
            model.setName(jmodel.getName());
            for (int i = 0; i < jmodel.getBos().size(); i++) {
                MetaType type = makeMetaType(jmodel.getBos(i));
                type.setMetaModel(model);
                model.insertIntoMetaTypes(type);
            }
            makeMetaRelationShips();

        }
    }

    public static MetaType makeMetaType(BO bo) {
        MetaType type = null;
        if (bo != null) {
            type = new MetaType_Bean();
            type.setName(bo.getName());
            type.setMappedName(getMappedName(bo.getName()));

            for (int i = 0; i < bo.getAttributes().size(); i++) {
                MetaAttribute attribute = makeMetaAttribute(bo.getAttributes(i));
                type.insertIntoMetaAttributes(attribute);
            }
        }
        return type;
    }

    public static MetaAttribute makeMetaAttribute(Attribute attr) {
        MetaAttribute attribute = null;
        if (attr != null) {
            attribute = new MetaAttribute_Bean();
            attribute.setName(attr.getName());
            attribute.setMappedName(getMappedName(attr.getName()));
            attribute.setType(attr.getDomain().getName());
        }
        return attribute;
    }

    public void makeMetaRelationShips() {
        MetaRelationship relationship;
        for (int i = 0; i < jmodel.getRels().size(); i++) {
            Relationship rel = jmodel.getRels(i);
            BO parentBO = rel.getParentBO();
            BO childBO = rel.getChildBO();
            MetaType parentType = model.lookupInMetaTypes(parentBO.getName());
            MetaType childType = model.lookupInMetaTypes(childBO.getName());
            if (parentType == null) {
                System.err.println("Could not find MetaType '"+parentBO.getName()+"' for relationship " + rel.getForwardName());
            }
            if (childType == null) {
                System.err.println("Could not find MetaType '"+childBO.getName()+"' for relationship " + rel.getForwardName());
            }
            //for forward side
            if (rel.getForwardName() != null && rel.getForwardName().trim().length() > 0) {
                relationship = new MetaRelationship_Bean();
                relationship.setName(rel.getForwardName());
                relationship.setMappedName(getMappedRelationName(parentType.getName(), rel.getForwardName()));
                relationship.setInverseName(rel.getInverseName());
                relationship.setPointerMetaType(childType);
                relationship.setCollection(!rel.getForwardCardinalityMax().equals("1"));
                relationship.setPrimarySide(true);
                parentType.insertIntoMetaRelationships(relationship);
            }
            //for reverse side
            if (rel.getInverseName() != null && rel.getInverseName().trim().length() > 0) {
                relationship = new MetaRelationship_Bean();
                relationship.setName(rel.getInverseName());
                relationship.setMappedName(getMappedRelationName(childType.getName(), rel.getInverseName()));
                relationship.setInverseName(rel.getForwardName());
                relationship.setPointerMetaType(parentType);
                relationship.setCollection(!rel.getInverseCardinalityMax().equals("1"));
                childType.insertIntoMetaRelationships(relationship);
            }
        }
    }
*/

}



//INVERSE
////for JPackage relationships
//{"classes",                   "inv(contain)"},
////for JClass relationships
//{"parentPackage",           "contain"},
//{"implementsInterfaces",    "inv(implements)"},
//{"implementingClasses",     "implements"},
//{"extendsClass",              "inv(extends)"},
//{"extendingClasses",           "extends"},
//{"contains",                   "inv(contain)"}, //for inner classes
//{"containingClass",           "inv(contain)"},
//{"methods",                   "inv(contain)"},
//{"fields",                   "inv(contain)"},
////for JMethod relationships
//{"returnType",               "???"},//NO_INV_REL currently
//{"throwsExceptions",           "???"},//NO_INV_REL currently
//{"catchesExceptions",       "???"},//NO_INV_REL currently
//{"calls",                   "inv(calls)"}, //for called methods
//{"callers",                   "calls"},
//{"parameters",               "???"},//NO_INV_REL currently
////for JField & JMethod relationships
//{"parentClass",               "parameters"},
////for JParameter relationships
//{"type",                       "???"},//NO_INV_REL currently