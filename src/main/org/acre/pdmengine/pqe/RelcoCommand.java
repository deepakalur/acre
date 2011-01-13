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
 * @version Dec 9, 2004 5:41:26 PM
 */
public class RelcoCommand {
        private static final String VAR_PREFIX = "v_relco_";

        private static Logger logger = ConfigService.getInstance().getLogger(RelcoCommand.class);

        public Map execute(PQLEngineFacade pqlFacade,
                                    String resultName,
                                    String[] srcRoles,
                                    String targetRole,
                                    String relationship) {
            Map pqlResultsMap = new HashMap();

            if ( targetRole==null )
                return pqlResultsMap;

            String relco;
            relco = VAR_PREFIX  + PatternEngineUtil.generateTempName();
            String relcoQuery ;
            relcoQuery = createRelcoQuery(relationship, relco, srcRoles, targetRole);
            
            logger.info("Relco Query : " + relcoQuery);

            // execute base dynamic query
            pqlResultsMap = pqlFacade.executeQuery(relcoQuery);

            return pqlResultsMap;
        }


        String createRelcoQuery(String relationship, String relcoVariable, String[] srcRoles,
                                String targetRole ) {
            String relcoQuery = null;
            if ( PDMXMLConstants.RELATIONSHIP_TYPE_CALLS.equalsIgnoreCase(relationship) ) {
                relcoQuery = createCallsRelcoQuery(srcRoles, targetRole, relcoVariable);
            }
            else if (PDMXMLConstants.RELATIONSHIP_TYPE_IMPLEMENTS.equalsIgnoreCase(relationship)) {
                relcoQuery = createImplementsRelcoQuery( srcRoles, targetRole, relcoVariable);
            }
            else if (PDMXMLConstants.RELATIONSHIP_TYPE_CREATES.equalsIgnoreCase(relationship) ||
                    PDMXMLConstants.RELATIONSHIP_TYPE_INSTANTIATES.equalsIgnoreCase(relationship)) {
                relcoQuery = createCreatesRelcoQuery( srcRoles, targetRole, relcoVariable);
            }
            else if (PDMXMLConstants.RELATIONSHIP_TYPE_ISOFTYPE.equalsIgnoreCase(relationship) ) {
                relcoQuery = createIsOfTypeRelcoQuery( srcRoles, targetRole, relcoVariable);
            }
            else if ( PDMXMLConstants.RELATIONSHIP_TYPE_USES.equalsIgnoreCase(relationship)) {
                relcoQuery = createUsesRelcoQuery( srcRoles, targetRole, relcoVariable);
            }
            else if ( PDMXMLConstants.RELATIONSHIP_TYPE_EXTENDS.equalsIgnoreCase(relationship)) {
                relcoQuery = createExtendsRelcoQuery( srcRoles, targetRole, relcoVariable);
            }
            else if ( PDMXMLConstants.RELATIONSHIP_TYPE_CONTAINS.equalsIgnoreCase(relationship)) {
                relcoQuery = createContainsRelcoQuery( srcRoles, targetRole, relcoVariable);

            }
            else if ( PDMXMLConstants.RELATIONSHIP_TYPE_THROWS.equalsIgnoreCase(relationship)) {
                relcoQuery = createThrowsRelcoQuery( srcRoles, targetRole, relcoVariable);

            }
            else if ( PDMXMLConstants.RELATIONSHIP_TYPE_CATCHES.equalsIgnoreCase(relationship)) {
                relcoQuery = createCatchesRelcoQuery( srcRoles, targetRole, relcoVariable);

            }
            else
                throw new PatternEngineException("Relco using Unknown Relationship Type : " + relationship);
            return relcoQuery;
        }

    private String createImplementsRelcoQuery(String[] srcRoles, String targetRole, String relcoVariable ) {
        String relcoQuery;
        if ( AcreStringUtil.isEmpty(srcRoles)) {
            relcoQuery =
                    " define " + relcoVariable + " as " +
                    " select c as c1, i as c2  " +
                    " from classes i, i.implementingClasses c  " +
                    " where i in " + targetRole + " ; " +
                    " return " + relcoVariable + " ;";
        }
        else {
            String srcRolesStr = AcreStringUtil.concat(srcRoles, ',');

            relcoQuery =
                    " define " + relcoVariable + " as " +
                    " select c as c1, i as c2  " +
                    " from classes i, i.implementingClasses c  " +
                    " where i in " + targetRole + "  " +
                    " and c not in  (select x from " + srcRolesStr + " x )   ;  " +
                    " return " + relcoVariable + " ;";
        }
        return relcoQuery;
    }

    private String createCallsRelcoQuery(String[] srcRoles, String targetRole, String relcoVariable ) {
        String relcoQuery;
        if ( AcreStringUtil.isEmpty(srcRoles)) {
                relcoQuery =
                        " define " + relcoVariable + " as " +
                        " select c as c1, cc as c2  " +
                        " from classes c, c.methods m, m.calls.parentClass cc  " +
                        " where cc in " + targetRole + " and  c != cc and c.isInner = false ; " +
                        " return " + relcoVariable + " ;";
        }
        else {
            String srcRolesStr = AcreStringUtil.concat(srcRoles, ',');

            relcoQuery =
                    " define " + relcoVariable + " as " +
                    " select c as c1, cc as c2  " +
                    " from classes c, c.methods m, m.calls.parentClass cc  " +
                    " where cc in " + targetRole + " and  c != cc and c.isInner = false " +
                    " and c not in  (select x from " + srcRolesStr + " x )   ;  " +
                    " return " + relcoVariable + " ;";
        }
        return relcoQuery;
    }


    private String createCreatesRelcoQuery(String[] srcRoles, String targetRole, String relcoVariable ) {
        String relcoQuery;
        if ( AcreStringUtil.isEmpty(srcRoles)) {
                relcoQuery =
                        " define " + relcoVariable + " as " +
                        " select c as c1, cc as c2  " +
                        " from classes c, c.methods m, m.instantiates cc  " +
                        " where cc in " + targetRole + " ; " +
                        " return " + relcoVariable + " ;";
        }
        else {
            String srcRolesStr = AcreStringUtil.concat(srcRoles, ',');

            relcoQuery =
                    " define " + relcoVariable + " as " +
                    " select c as c1, cc as c2  " +
                    " from classes c, c.methods m, m.instantiates cc  " +
                    " where cc in " + targetRole +
                    " and c not in  (select x from " + srcRolesStr + " x )   ;  " +
                    " return " + relcoVariable + " ;";
        }
        return relcoQuery;
    }


    private String createIsOfTypeRelcoQuery(String[] srcRoles, String targetRole, String relcoVariable ) {
        String relcoQuery;
        if ( AcreStringUtil.isEmpty(srcRoles)) {
                relcoQuery =
                        " define " + relcoVariable + " as " +
                        " select c as c1, cc as c2  " +
                        " from classes c, c.methods m, m.returnType cc  " +
                        " where cc in " + targetRole + " ; " +
                        " return " + relcoVariable + " ;";
        }
        else {
            String srcRolesStr = AcreStringUtil.concat(srcRoles, ',');

            relcoQuery =
                    " define " + relcoVariable + " as " +
                    " select c as c1, cc as c2  " +
                    " from classes c, c.methods m, m.returnType cc  " +
                    " where cc in " + targetRole +
                    " and c not in  (select x from " + srcRolesStr + " x )   ;  " +
                    " return " + relcoVariable + " ;";
        }
        return relcoQuery;
    }



    private String createUsesRelcoQuery(String[] srcRoles, String targetRole, String relcoVariable ) {
        String relcoQuery;
        if ( AcreStringUtil.isEmpty(srcRoles)) {
                relcoQuery =
                        " define " + relcoVariable + " as " +
                        " select c as c1, cc as c2  " +
                        " from classes c, c.fields f, f.type cc  " +
                        " where cc in " + targetRole + " ; " +
                        " return " + relcoVariable + " ;";
        }
        else {
            String srcRolesStr = AcreStringUtil.concat(srcRoles, ',');

            relcoQuery =
                    " define " + relcoVariable + " as " +
                    " select c as c1, cc as c2  " +
                    " from classes c, c.fields f, f.type cc  " +
                    " where cc in " + targetRole +
                    " and c not in  (select x from " + srcRolesStr + " x )   ;  " +
                    " return " + relcoVariable + " ;";
        }
        return relcoQuery;
    }



    private String createExtendsRelcoQuery(String[] srcRoles, String targetRole, String relcoVariable ) {
        String relcoQuery;
        if ( AcreStringUtil.isEmpty(srcRoles)) {
                relcoQuery =
                        " define " + relcoVariable + " as " +
                        " select c as c1, cc as c2  " +
                        " from classes c, c.extendsClass cc  " +
                        " where cc in " + targetRole + " ; " +
                        " return " + relcoVariable + " ;";
        }
        else {
            String srcRolesStr = AcreStringUtil.concat(srcRoles, ',');

            relcoQuery =
                    " define " + relcoVariable + " as " +
                    " select c as c1, cc as c2  " +
                    " from classes c, c.extendsClass cc  " +
                    " where cc in " + targetRole +
                    " and c not in  (select x from " + srcRolesStr + " x )   ;  " +
                    " return " + relcoVariable + " ;";
        }
        return relcoQuery;
    }

    private String createContainsRelcoQuery(String[] srcRoles, String targetRole, String relcoVariable ) {
        String relcoQuery;
        if ( AcreStringUtil.isEmpty(srcRoles)) {
                relcoQuery =
                        " define " + relcoVariable + " as " +
                        " select c as c1, cc as c2  " +
                        " from classes c, c.contains cc  " +
                        " where cc in " + targetRole + " ; " +
                        " return " + relcoVariable + " ;";
        }
        else {
            String srcRolesStr = AcreStringUtil.concat(srcRoles, ',');

            relcoQuery =
                    " define " + relcoVariable + " as " +
                    " select c as c1, cc as c2  " +
                    " from classes c, c.contains cc  " +
                    " where cc in " + targetRole +
                    " and c not in  (select x from " + srcRolesStr + " x )   ;  " +
                    " return " + relcoVariable + " ;";
        }
        return relcoQuery;
    }


    private String createThrowsRelcoQuery(String[] srcRoles, String targetRole, String relcoVariable ) {
        String relcoQuery;
        if ( AcreStringUtil.isEmpty(srcRoles)) {
                relcoQuery =
                        " define " + relcoVariable + " as " +
                        " select c as c1, cc as c2  " +
                        " from classes c, c.methods m, m.throwsExceptions cc  " +
                        " where cc in " + targetRole + " ; " +
                        " return " + relcoVariable + " ;";
        }
        else {
            String srcRolesStr = AcreStringUtil.concat(srcRoles, ',');

            relcoQuery =
                    " define " + relcoVariable + " as " +
                    " select c as c1, cc as c2  " +
                    " from classes c, c.methods m, m.throwsExceptions cc  " +
                    " where cc in " + targetRole +
                    " and c not in  (select x from " + srcRolesStr + " x )   ;  " +
                    " return " + relcoVariable + " ;";
        }
        return relcoQuery;
    }

    private String createCatchesRelcoQuery(String[] srcRoles, String targetRole, String relcoVariable ) {
        String relcoQuery;
        if ( AcreStringUtil.isEmpty(srcRoles)) {
                relcoQuery =
                        " define " + relcoVariable + " as " +
                        " select c as c1, cc as c2  " +
                        " from classes c, c.methods m, m.catchesExceptions cc  " +
                        " where cc in " + targetRole + " ; " +
                        " return " + relcoVariable + " ;";
        }
        else {
            String srcRolesStr = AcreStringUtil.concat(srcRoles, ',');

            relcoQuery =
                    " define " + relcoVariable + " as " +
                    " select c as c1, cc as c2  " +
                    " from classes c, c.methods m, m.catchesExceptions cc  " +
                    " where cc in " + targetRole +
                    " and c not in  (select x from " + srcRolesStr + " x )   ;  " +
                    " return " + relcoVariable + " ;";
        }
        return relcoQuery;
    }

}
