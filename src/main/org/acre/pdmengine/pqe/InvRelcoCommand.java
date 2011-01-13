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
package org.acre.pdmengine.pqe;

import org.acre.common.AcreStringUtil;
import org.acre.config.ConfigService;
import org.acre.dao.PDMXMLConstants;
import org.acre.pdmengine.PatternEngineException;
import org.acre.pdmengine.util.PatternEngineUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author rajmohan@Sun.com
 * @version Dec 9, 2004 5:42:42 PM
 */
public class InvRelcoCommand {

    private static final String PREFIX = "v_invrel_";
    private static Logger logger = ConfigService.getInstance().getLogger(InvRelcoCommand.class);


    public Map execute(PQLEngineFacade pqlFacade,
                                String resultName,
                                String srcRole,
                                String[] targetRoles,
                                String relationshipType) {

        Map pqlResultsMap = new HashMap();
        
        if ( srcRole == null )
            return pqlResultsMap;

        String invrelco;
        invrelco = PREFIX + PatternEngineUtil.generateTempName();

        String invrelcoQuery;

        invrelcoQuery = createInvRelcoQuery(srcRole, targetRoles, invrelco, relationshipType);
        logger.info("InvRelco Query : " + invrelcoQuery);



        // execute base dynamic query
        pqlResultsMap = pqlFacade.executeQuery(invrelcoQuery);

        return pqlResultsMap;

    }

    private String createInvRelcoQuery(String srcRole, String[] targetRoles, String invrelcoVariable,
                                       String relationship) {
        String invRelcoQuery;        
        if ( PDMXMLConstants.RELATIONSHIP_TYPE_CALLS.equalsIgnoreCase(relationship) ) {
            invRelcoQuery =  createCallsInvRelcoQuery(srcRole, targetRoles, invrelcoVariable);
        }
        else if (PDMXMLConstants.RELATIONSHIP_TYPE_IMPLEMENTS.equalsIgnoreCase(relationship)) {
            invRelcoQuery =  createImplementsInvRelcoQuery(srcRole, targetRoles, invrelcoVariable);
        }
        else if (PDMXMLConstants.RELATIONSHIP_TYPE_CREATES.equalsIgnoreCase(relationship) ||
                PDMXMLConstants.RELATIONSHIP_TYPE_INSTANTIATES.equalsIgnoreCase(relationship)) {
            invRelcoQuery =  createCreatesInvRelcoQuery(srcRole, targetRoles, invrelcoVariable);
        }
        else if (PDMXMLConstants.RELATIONSHIP_TYPE_ISOFTYPE.equalsIgnoreCase(relationship) ) {
            invRelcoQuery =  createIsOfTypeInvRelcoQuery(srcRole, targetRoles, invrelcoVariable);
        }
        else if ( PDMXMLConstants.RELATIONSHIP_TYPE_USES.equalsIgnoreCase(relationship)) {
            invRelcoQuery =  createUsesInvRelcoQuery(srcRole, targetRoles, invrelcoVariable);
        }
        else if ( PDMXMLConstants.RELATIONSHIP_TYPE_EXTENDS.equalsIgnoreCase(relationship)) {
            invRelcoQuery =  createExtendsInvRelcoQuery(srcRole, targetRoles, invrelcoVariable);
        }
        else if ( PDMXMLConstants.RELATIONSHIP_TYPE_CONTAINS.equalsIgnoreCase(relationship)) {
            invRelcoQuery =  createContainsInvRelcoQuery(srcRole, targetRoles, invrelcoVariable);
        }
        else if ( PDMXMLConstants.RELATIONSHIP_TYPE_THROWS.equalsIgnoreCase(relationship)) {
            invRelcoQuery =  createThrowsInvRelcoQuery(srcRole, targetRoles, invrelcoVariable);
        }
        else if ( PDMXMLConstants.RELATIONSHIP_TYPE_CATCHES.equalsIgnoreCase(relationship)) {
            invRelcoQuery =  createCatchesInvRelcoQuery(srcRole, targetRoles, invrelcoVariable);
        }
        else
            throw new PatternEngineException("InvRelco using Unknown Relationship Type : " + relationship);
        
        return invRelcoQuery;
    }

    private String createCallsInvRelcoQuery(String srcRole, String[] targetRoles, String invrelcoVariable) {
        String invrelcoQuery;
        if ( AcreStringUtil.isEmpty(targetRoles)) {
            invrelcoQuery =
                    " define " + invrelcoVariable + " as " +
                    " select c as c1, cc as c2  " +
                    " from classes c, c.methods m, m.calls.parentClass cc  " +
                    " where c in " + srcRole + " ; " +
                    " return " + invrelcoVariable + " ;";
        }
        else {
            String targetRolesStr = AcreStringUtil.concat(targetRoles, ',');

            invrelcoQuery =
                    " define " + invrelcoVariable + " as " +
                    " select c as c1, cc as c2  " +
                    " from classes c, c.methods m, m.calls.parentClass cc  " +
                    " where c in " + srcRole +
                    " and cc not in  (select x from " + targetRolesStr + " x )   ;  " +
                    " return " + invrelcoVariable + " ;";
        }
        return invrelcoQuery;
    }


    private String createImplementsInvRelcoQuery(String srcRole, String[] targetRoles, String invRelcoVariable ) {
        String invRelcoQuery;
        if ( AcreStringUtil.isEmpty(targetRoles)) {
            invRelcoQuery=
                    " define " + invRelcoVariable + " as " +
                    " select c as c1, i as c2  " +
                    " from classes i, i.implementingClasses c  " +
                    " where c in " + srcRole + " ; " +
                    " return " + invRelcoVariable + " ;";
        }
        else {
            String targetRolesStr = AcreStringUtil.concat(targetRoles, ',');

            invRelcoQuery =
                    " define " + invRelcoVariable + " as " +
                    " select c as c1, i as c2  " +
                    " from classes i, i.implementingClasses c  " +
                    " where c in " + srcRole + "  " +
                    " and i not in  (select x from " + targetRolesStr + " x )   ;  " +
                    " return " + invRelcoVariable + " ;";
        }
        return invRelcoQuery;
    }

    private String createCreatesInvRelcoQuery(String srcRole, String[] targetRoles, String invrelcoVariable ) {
        String invrelcoQuery;
        if ( AcreStringUtil.isEmpty(targetRoles)) {
                invrelcoQuery =
                        " define " + invrelcoVariable + " as " +
                        " select c as c1, cc as c2  " +
                        " from classes c, c.methods m, m.instantiates cc  " +
                        " where c in " + srcRole + " ; " +
                        " return " + invrelcoVariable + " ;";
        }
        else {
            String targetRolesStr = AcreStringUtil.concat(targetRoles, ',');

            invrelcoQuery =
                    " define " + invrelcoVariable + " as " +
                    " select c as c1, cc as c2  " +
                    " from classes c, c.methods m, m.instantiates cc  " +
                    " where c in " + srcRole +
                    " and cc not in  (select x from " + targetRolesStr + " x )   ;  " +
                    " return " + invrelcoVariable + " ;";
        }
        return invrelcoQuery;
    }

    private String createIsOfTypeInvRelcoQuery(String srcRole, String[] targetRoles, String invrelcoVariable ) {
        String invrelcoQuery;
        if ( AcreStringUtil.isEmpty(targetRoles)) {
                invrelcoQuery =
                        " define " + invrelcoVariable + " as " +
                        " select c as c1, cc as c2  " +
                        " from classes c, c.methods m, m.returnType cc  " +
                        " where c in " + srcRole + " ; " +
                        " return " + invrelcoVariable + " ;";
        }
        else {
            String targetRolesStr = AcreStringUtil.concat(targetRoles, ',');

            invrelcoQuery =
                    " define " + invrelcoVariable + " as " +
                    " select c as c1, cc as c2  " +
                    " from classes c, c.methods m, m.returnType cc  " +
                    " where c in " + srcRole +
                    " and cc not in  (select x from " + targetRolesStr + " x )   ;  " +
                    " return " + invrelcoVariable + " ;";
        }
        return invrelcoQuery;
    }

    private String createUsesInvRelcoQuery(String srcRole, String[] targetRoles, String invrelcoVariable ) {
        String invrelcoQuery;
        if ( AcreStringUtil.isEmpty(targetRoles)) {
                invrelcoQuery =
                        " define " + invrelcoVariable + " as " +
                        " select c as c1, cc as c2  " +
                        " from classes c, c.fields f, f.type cc  " +
                        " where c in " + srcRole + " ; " +
                        " return " + invrelcoVariable + " ;";
        }
        else {
            String targetRolesStr = AcreStringUtil.concat(targetRoles, ',');

            invrelcoQuery =
                    " define " + invrelcoVariable + " as " +
                    " select c as c1, cc as c2  " +
                    " from classes c, c.fields f, f.type cc  " +
                    " where c in " + srcRole +
                    " and cc not in  (select x from " + targetRolesStr + " x )   ;  " +
                    " return " + invrelcoVariable + " ;";
        }
        return invrelcoQuery;
    }



    private String createExtendsInvRelcoQuery(String srcRole, String[] targetRoles, String invrelcoVariable ) {
        String relcoQuery;
        if ( AcreStringUtil.isEmpty(targetRoles)) {
                relcoQuery =
                        " define " + invrelcoVariable + " as " +
                        " select c as c1, cc as c2  " +
                        " from classes c, c.extendsClass cc  " +
                        " where c in " + srcRole + " ; " +
                        " return " + invrelcoVariable + " ;";
        }
        else {
            String targetRolesStr = AcreStringUtil.concat(targetRoles, ',');

            relcoQuery =
                    " define " + invrelcoVariable + " as " +
                    " select c as c1, cc as c2  " +
                    " from classes c, c.extendsClass cc  " +
                    " where c in " + srcRole +
                    " and cc not in  (select x from " + targetRolesStr + " x )   ;  " +
                    " return " + invrelcoVariable + " ;";
        }
        return relcoQuery;
    }

    private String createContainsInvRelcoQuery(String srcRole, String[] targetRoles, String invrelcoVariable ) {
        String invrelcoQuery;
        if ( AcreStringUtil.isEmpty(targetRoles)) {
                invrelcoQuery =
                        " define " + invrelcoVariable + " as " +
                        " select c as c1, cc as c2  " +
                        " from classes c, c.contains cc  " +
                        " where c in " + srcRole + " ; " +
                        " return " + invrelcoVariable + " ;";
        }
        else {
            String targetRolesStr = AcreStringUtil.concat(targetRoles, ',');

            invrelcoQuery =
                    " define " + invrelcoVariable + " as " +
                    " select c as c1, cc as c2  " +
                    " from classes c, c.contains cc  " +
                    " where c in " + srcRole +
                    " and cc not in  (select x from " + targetRolesStr + " x )   ;  " +
                    " return " + invrelcoVariable + " ;";
        }
        return invrelcoQuery;
    }


    private String createThrowsInvRelcoQuery(String srcRole, String[] targetRoles, String invrelcoVariable ) {
        String invrelcoQuery;
        if ( AcreStringUtil.isEmpty(targetRoles)) {
                invrelcoQuery =
                        " define " + invrelcoVariable + " as " +
                        " select c as c1, cc as c2  " +
                        " from classes c, c.methods m, m.throwsExceptions cc  " +
                        " where c in " + srcRole + " ; " +
                        " return " + invrelcoVariable + " ;";
        }
        else {
            String targetRolesStr = AcreStringUtil.concat(targetRoles, ',');

            invrelcoQuery =
                    " define " + invrelcoVariable + " as " +
                    " select c as c1, cc as c2  " +
                    " from classes c, c.methods m, m.throwsExceptions cc  " +
                    " where c in " + srcRole +
                    " and cc not in  (select x from " + targetRolesStr + " x )   ;  " +
                    " return " + invrelcoVariable + " ;";
        }
        return invrelcoQuery;
    }

    private String createCatchesInvRelcoQuery(String srcRole, String[] targetRoles, String invrelcoVariable ) {
        String invrelcoQuery;
        if ( AcreStringUtil.isEmpty(targetRoles)) {
                invrelcoQuery =
                        " define " + invrelcoVariable + " as " +
                        " select c as c1, cc as c2  " +
                        " from classes c, c.methods m, m.catchesExceptions cc  " +
                        " where cc in " + srcRole + " ; " +
                        " return " + invrelcoVariable + " ;";
        }
        else {
            String targetRolesStr = AcreStringUtil.concat(targetRoles, ',');

            invrelcoQuery =
                    " define " + invrelcoVariable + " as " +
                    " select c as c1, cc as c2  " +
                    " from classes c, c.methods m, m.catchesExceptions cc  " +
                    " where c in " + srcRole +
                    " and cc not in  (select x from " + targetRolesStr + " x )   ;  " +
                    " return " + invrelcoVariable + " ;";
        }
        return invrelcoQuery;
    }



}
