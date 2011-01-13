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
package org.acre.dao;

/**
 * @author Deepak.Alur@Sun.com
 *         Date: Jun 11, 2004
 *         Time: 9:36:54 AM
 */
public interface PDMXMLConstants {

    // PDM Category
    public static final String PDM_CATEGORY_COREJ2EEPATTERNS = "CoreJ2EEPatterns";
    public static final String PDM_CATEGORY_GOF = "GoF";
    public static final String PDM_CATEGORY_OTHER = "Other";

    // PDM Types
    public static final String PDM_TYPE_MODEL_PDM = "ModelPDM";
    public static final String PDM_TYPE_MODEL_GROOVY = "GroovyPDM";
    public static final String PDM_TYPE_MODEL_PQLPDM = "PQLPDM";
    public static final String PDM_TYPE_MODEL_QLPDM = "QLPDM";
    public static final String PDM_TYPE_MODEL_JAVAPDM = "JavaPDM";

    // Relationship types
    public static final String RELATIONSHIP_TYPE_CREATES = "Creates";
    public static final String RELATIONSHIP_TYPE_CALLS = "Calls";
    public static final String RELATIONSHIP_TYPE_INSTANTIATES = "Instantiates";
    public static final String RELATIONSHIP_TYPE_CONTAINS = "Contains";
    public static final String RELATIONSHIP_TYPE_USES = "Uses";
    public static final String RELATIONSHIP_TYPE_ISOFTYPE = "IsOfType";
    public static final String RELATIONSHIP_TYPE_THROWS = "Throws";
    public static final String RELATIONSHIP_TYPE_CATCHES = "Catches";
    public static final String RELATIONSHIP_TYPE_EXTENDS = "Extends";
    public static final String RELATIONSHIP_TYPE_IMPLEMENTS = "Implements";
    public static final String RELATIONSHIP_TYPE_ABSCO = "Absco";
    public static final String RELATIONSHIP_TYPE_RELCO = "Relco";
    public static final String RELATIONSHIP_TYPE_INVRELCO = "InvRelco";

    // tier types
    public static final String PDM_TIER_TYPE_BUSINESS = "BusinessTier";
    public static final String PDM_TIER_TYPE_PRESENTATION = "PresentationTier";
    public static final String PDM_TIER_TYPE_INTEGRATION = "IntegrationTier";
    public static final String PDM_TIER_TYPE_OTHER = "Other";
    public static final String PDM_TIER_TYPE_SUBSYSTEM = "SubSystem";
    public static final String PDM_TIER_TYPE_ANY = "Any";

    //roles types
    public static final String ROLES_TYPE_DYNAMIC = "Dynamic";
    
    // role types
    public static final String ROLE_TYPE_PDM ="PDM";
    public static final String ROLE_TYPE_DYNAMIC_PDM ="DynamicPDM";
    public static final String ROLE_TYPE_QUERY ="Query";
    public static final String ROLE_TYPE_ABSTRACT ="Abstract";
    public static final String ROLE_TYPE_ANNOTATED = "Annotated";


    // role arguments type
    public static final String ROLE_TYPE_ARGUMENT_ROLEPATH = "RolePath";

    // Query Types
    public static final String QUERY_TYPE_STATIC = "Static";
    public static final String QUERY_TYPE_DYNAMIC = "Dynamic";

    // Query Language
    public static final String QUERY_LANGUAGE_PQL = "PQL";
    public static final String QUERY_LANGUAGE_GROOVY = "Groovy";

    // query Return Variable Types
    public static final String QUERY_RETURN_VARIABLE_TYPE_ANY = "Any";
    public static final String QUERY_RETURN_VARIABLE_TYPE_STATIC = "Static";
    public static final String QUERY_RETURN_VARIABLE_TYPE_DYNAMIC = "Dynamic";

    public static final String QUERY_ARGUMENT_TYPE_STATIC = "Static";
    public static final String QUERY_ARGUMENT_TYPE_DYNAMIC = "Dynamic";
    public static final String QUERY_ARGUMENT_TYPE_RETURN = "Query Return Variable";

    public static final String PQL_FILE_EXTENSION = ".pql";
    public static final String GROOVY_FILE_EXTENSION = ".groovy";
    public static final String PQL_TEMP_FILE_PREFIX = "PQL";
    public static final String NEW_QUERY_NAME = "";

    public static final String PDM_FACT_MODEL_JAVA = "Java";
    public static final String PDM_FACT_MODEL_WSDL = "WSDL";
    public static final String PDM_FACT_MODEL_JAVAWSDL_HYBRID = "Java+WSDL";

    public static final String NEW_PDM_NAME = "";
    public static final String DEFAULT_PDM_TYPE = PDM_TYPE_MODEL_PQLPDM;

    public static final String DEFAULT_PDM_CATEGORY_TYPE = PDM_CATEGORY_OTHER;
    public static final String DEFAULT_PDM_TIER_TYPE = PDM_TIER_TYPE_ANY;
    public static final String DEFAULT_DESCRIPTION = "Modify values before saving";
    public static final String DEFAULT_PDM_ATTRIBUTES ="";
    public static final String DEFAULT_PDM_SCRIPTED_PDM_PATH = "";
    public static final String DEFAULT_PDM_FACT_MODEL_TYPE = PDM_FACT_MODEL_JAVA;

    public static final String PDM_ROLE_ARGUMENT_TYPE_STRING = "String";
    public static final String PDM_ROLE_ARGUMENT_TYPE_INTEGER = "Integer";
    public static final String PDM_ROLE_ARGUMENT_TYPE_DECIMAL = "Decimal";
    public static final String PDM_ROLE_ARGUMENT_TYPE_BOOLEAN = "Boolean";
    public static final String PDM_ROLE_ARGUMENT_TYPE_ROLEPATH = "RolePath";

    public static final String DEFAULT_PDM_ROLE_ARGUMENT_TYPE = PDM_ROLE_ARGUMENT_TYPE_STRING;
    public static final String DEFAULT_PDM_ROLE_TYPE = ROLE_TYPE_QUERY;
    public static final String DEFAULT_PDM_RELATIONSHIP_TYPE = "";

    public static final String NAME_SEPARATOR = ".";
}
