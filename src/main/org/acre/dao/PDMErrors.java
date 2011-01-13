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

import org.acre.common.AcreErrors;

/**
 * @author Deepak.Alur@Sun.com
 * @version Nov 6, 2004 4:28:52 PM
 */
public class PDMErrors extends AcreErrors {

    public static final String PDM_TYPE_INVALID = "pdm.type.invalid";
    public static final String PDM_CATEGORY_INVALID = "pdm.category.invalid";
    public static final String PDM_FACTMODEL_TYPE_INVALID = "pdm.factmodel.invalid";
    public static final String ROLE_TYPE_INVALID = "pdm.role.type.invalid";
    public static final String ROLE_NAME_INVALID = "pdm.role.name.invalid";
    public static final String ROLE_SEQUENCE_INVALID = "role.sequence.invalid";

    public static final String RELATIONSHIP_NAME_INVALID = "pdm.relationship.name.invalid";
    public static final String RELATIONSHIP_TYPE_INVALID = "pdm.relationship.type.invalid";
    public static final String RELATIONSHIP_FROM_ROLE_INVALID = "pdm.relationship.fromrole.invalid";
    public static final String RELATIONSHIP_TO_ROLE_INVALID = "pdm.relationship.torole.invalid";

    public static final String ROLE_ARGUMENT_NAME_INVALID = "pdm.role.argument.name.invalid";
    public static final String ROLE_ARGUMENT_TYPE_INVALID =  "pdm.role.argument.type.invalid";
    public static final String ROLE_ARGUMENT_VALUE_INVALID =  "pdm.role.argument.value.invalid";
    public static final String ROLE_ARGUMENT_SEQUENCE_INVALID =  "pdm.role.argument.sequence.invalid";
    public static final String ROLE_QUERY_NAME_INVALID = "pdm.role.query.name.invalid";
    public static final String ROLE_TYPEREFERENCE_NAME_INVALID = "pdm.role.typereference.name.invalid";
    public static final String ROLE_QUERY_RETURNVARIABLENAME_INVALID = "pdm.role.query.returnvariablename.invalid";
    public static final String ROLE_DUPLICATE_EXISTS = "pdm.role.duplicate.exists";
    public static final String PDM_ROLE_EMPTY = "pdm.role.empty";
    public static final String RELATIONSHIP_DUPLICATE_EXISTS = "pdm.relationship.duplicate.exists";
    public static final String PDM_TIER_INVALID = "pdm.tier.invalid";

}
