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

import org.acre.config.ConfigService;
import org.acre.pdmengine.PatternEngineException;

import java.util.Map;
import java.util.logging.Logger;



/**
 * User: rajmohan@sun.com
 * Date: Nov 8, 2004
 * Time: 11:54:02 AM
 */
public abstract class PatternBaseCommand {

    Logger logger = ConfigService.getInstance().getLogger(PatternBaseCommand.class);

    // dynamic query construction to find results for the given relationship
    // involving 2 entities
    abstract String buildDynamicQuery(PQLVariable resultEntity,
                                      PQLVariable fromEntity,
                                      PQLVariable toEntity, boolean transitive);


    public PQLVariable execute(PQLEngineFacade pqlEngineFacade,
                                 String resultVariableName,
                                 PQLVariable fromEntity,
                                 PQLVariable toEntity,
                                 boolean transitive) {

            PQLVariable resultEntityVariable = new PQLVariable(resultVariableName);

            Map pqlResultsMap = null;


            String dynamicQuery = buildDynamicQuery(resultEntityVariable,
                                fromEntity,
                                toEntity,
                                transitive);

            long startTS = System.currentTimeMillis();
            logger.info("DYNAMIC QUERY : " + resultVariableName + ":" + fromEntity + ":" +
                    toEntity + ":" + "\n" + dynamicQuery);

            if ( dynamicQuery.length() == 0 ) {
                throw new PatternEngineException("Internal Error - Error in generating dynamic PQL query "
                        + resultVariableName + ":" + fromEntity + ":" + toEntity + ":" + getClass());
            }

            // execute base dynamic query
            pqlResultsMap = pqlEngineFacade.executeQuery(dynamicQuery);
            resultEntityVariable.setPqlResultMap(pqlResultsMap);

            logger.info("PQL Query Execution Time : " + (System.currentTimeMillis() - startTS)/1000 );

            return resultEntityVariable;
        }

        protected final static int QUERY_LENGTH = 300;



    static class CallsCommand extends PatternBaseCommand {
        // query to find "fromRole" resultset in relationship
        String buildDynamicQuery(PQLVariable resultEntity, PQLVariable fromEntity,
                                 PQLVariable toEntity, boolean transitive) {

            String fromType = fromEntity.getType();
            String toType = toEntity.getType();

            if ( PQLVariable.TYPE_CLASS.equalsIgnoreCase(fromType) &&
                    PQLVariable.TYPE_CLASS.equalsIgnoreCase(toType))  {

                return buildClassTypeCalls(resultEntity, fromEntity.getName(),
                            toEntity.getName(), transitive);
            }
            else if (PQLVariable.TYPE_PACKAGE.equalsIgnoreCase(fromType) &&
                    PQLVariable.TYPE_PACKAGE.equalsIgnoreCase(toType)) {

                return buildPackageTypeCalls(resultEntity, fromEntity.getName(),
                            toEntity.getName(), transitive);

            }
            return "";
        }

        private String buildClassTypeCalls(PQLVariable resultEntity, String fromEntity,
                                            String toEntity, boolean transitive) {
            StringBuffer query = new StringBuffer(QUERY_LENGTH);
            String callsOperator = (transitive ? ".**." : ".");
            resultEntity.setType(PQLVariable.TYPE_CLASS);
            query
                .append("define " + resultEntity.getName() + " as select c as c1, cc as c2 ")
                .append(" from " + fromEntity + " c, c.methods m, m.calls" +
                        callsOperator + "parentClass cc ") //cc-calling class
                .append(" where cc in " + toEntity +
                    " and  c != cc and c.isInner = false ;")  // reducing design 'noise'
                .append(" return " + resultEntity.getName() + " ;");

            return query.toString();
        }

        private String buildPackageTypeCalls(PQLVariable resultEntity, String fromEntity,
                                            String toEntity, boolean transitive) {

            resultEntity.setType(PQLVariable.TYPE_PACKAGE);
            StringBuffer query = new StringBuffer(QUERY_LENGTH);
            String callsOperator = (transitive ? ".**." : ".");

            query
                .append("define " + resultEntity.getName() + " as select p1 as c1, p2 as c2 ")
                .append(" from " + fromEntity + " p1, p1.classes.methods.calls.parentClass.parentPackage p2 " )
                .append(" where p1 != p2 order by p1.name ; ")
                .append(" return " + resultEntity.getName() + " ;");
            return query.toString();
        }

    }


    static class UsesCommand extends PatternBaseCommand {
        // query to find "fromRole" resultset in relationship
        String buildDynamicQuery(PQLVariable resultEntity, PQLVariable fromEntity,
                                 PQLVariable toEntity, boolean transitive) {
            StringBuffer query = new StringBuffer(QUERY_LENGTH);
            resultEntity.setType(PQLVariable.TYPE_CLASS);
            query
                .append("define " + resultEntity.getName() + " as select c as c1, f.type as c2 ")
                .append(" from " + fromEntity.getName() + " c, c.fields f ")
                .append(" where f.type in " + toEntity.getName() + "; ")
                .append(" return " + resultEntity.getName() + " ;");

            return query.toString();
        }

    }

    static class CreatesCommand extends PatternBaseCommand {
        // TODO - STUBBED VERSION - REPLACE WITH instantiates and instantiatedBy operators
        // query to find "fromRole" resultset in relationship
        String buildDynamicQuery(PQLVariable resultEntity, PQLVariable fromEntity,
                                 PQLVariable toEntity, boolean transitive) {
            StringBuffer query = new StringBuffer(QUERY_LENGTH);

            query
                .append("define " + resultEntity.getName() + " as select c as c1, m.instantiates as c2 ")
                .append(" from " + fromEntity.getName() + " c, c.methods m ")
                .append(" where m.instantiates in " + toEntity.getName() + "; ")
                .append(" return " + resultEntity.getName() + " ;");

            return query.toString();
        }

    }

    static class ContainsCommand extends PatternBaseCommand {
        // query to find "fromRole" resultset in relationship
        String buildDynamicQuery(PQLVariable resultEntity, PQLVariable fromEntity,
                                 PQLVariable toEntity, boolean transitive) {
            StringBuffer query = new StringBuffer(QUERY_LENGTH);

            String fromType = fromEntity.getType();
            String toType = toEntity.getType();

            if ( (PQLVariable.TYPE_PACKAGE.equalsIgnoreCase(fromType)) &&
                    PQLVariable.TYPE_PACKAGE.equalsIgnoreCase(toType) ) {
                query
                        .append("define " + resultEntity.getName() + " as select p as c1, p.classes as c2 ")
                        .append(" from " + fromEntity.getName() + " p ")
                        .append(" where p.classes in " + toEntity.getName() + "; ")
                        .append(" return " + resultEntity.getName() + " ;");
            }
            else {
                query
                    .append("define " + resultEntity.getName() + " as select c as c1, cc as c2 ")
                    .append(" from " + fromEntity.getName() + " c, c.contains cc ")
                    .append(" where cc in " + toEntity.getName() + "; ")
                    .append(" return " + resultEntity.getName() + " ;");
            }
            return query.toString();
        }

    }

    static class IsOfTypeCommand extends PatternBaseCommand {
        // query to find "fromRole" resultset in relationship
        String buildDynamicQuery(PQLVariable resultEntity, PQLVariable fromEntity,
                                 PQLVariable toEntity, boolean transitive) {
            StringBuffer query = new StringBuffer(QUERY_LENGTH);

            query
                .append("define " + resultEntity.getName() + " as select c as c1, m.returnType as c2 ")
                .append(" from " + fromEntity.getName() + " c, c.methods m ")
                .append(" where m.returnType in " + toEntity.getName() + "; ")
                .append(" return " + resultEntity.getName() + " ;");

            return query.toString();
        }

    }

    static class ThrowsCommand extends PatternBaseCommand {
        // query to find "fromRole" resultset in relationship
        String buildDynamicQuery(PQLVariable resultEntity, PQLVariable fromEntity,
                                 PQLVariable toEntity, boolean transitive) {
            StringBuffer query = new StringBuffer(QUERY_LENGTH);

            query
                .append("define " + resultEntity.getName() + " as select c as c1, m.throwsExceptions as c2 ")
                .append(" from " + fromEntity.getName() + " c, c.methods m ")
                .append(" where m.throwsExceptions in " + toEntity.getName() + "; ")
                .append(" return " + resultEntity.getName() + " ;");

            return query.toString();
        }

    }

    static class CatchesCommand extends PatternBaseCommand {
        // query to find "fromRole" resultset in relationship
        String buildDynamicQuery(PQLVariable resultEntity, PQLVariable fromEntity,
                                 PQLVariable toEntity, boolean transitive) {
            StringBuffer query = new StringBuffer(QUERY_LENGTH);

            query
                .append("define " + resultEntity.getName() + " as select c as c1, m.catchesExceptions as c2 ")
                .append(" from " + fromEntity.getName() + " c, c.methods m ")
                .append(" where m.catchesExceptions in " + toEntity.getName() + "; ")
                .append(" return " + resultEntity.getName() + " ;");

            return query.toString();
        }

    }

    static class ExtendsCommand extends PatternBaseCommand {
        //TODO - make this transitive ?
        String buildDynamicQuery(PQLVariable resultEntity, PQLVariable fromEntity,
                                 PQLVariable toEntity, boolean transitive) {
            StringBuffer query = new StringBuffer(QUERY_LENGTH);

            query
                .append("define " + resultEntity.getName() + " as select c as c1, e as c2")
                .append(" from " + fromEntity.getName() + " c, c.extendsClass e ")
                .append(" where e in " + toEntity.getName() + "; ")
                .append(" return " + resultEntity.getName() + " ;");

            return query.toString();
        }

    }

    static class ImplementsCommand extends PatternBaseCommand {
        // query to find "fromRole" resultset in relationship
        String buildDynamicQuery(PQLVariable resultEntity, PQLVariable fromEntity,
                                 PQLVariable toEntity, boolean transitive) {
            StringBuffer query = new StringBuffer(QUERY_LENGTH);

            //TODO - make this transitive ?
            query
                .append("define " + resultEntity.getName() + " as select c as c1, i as c2 ")
                .append(" from " + fromEntity.getName() + " c, c.implementsInterfaces i ")
                .append(" where i in " + toEntity.getName() + " ; ")
                .append(" return " + resultEntity.getName() + " ;");
            return query.toString();
        }

    }

}

class AbscoCommand extends PatternBaseCommand {
    String buildDynamicQuery(PQLVariable resultEntity, PQLVariable universalEntity,
                             PQLVariable targetEntity, boolean transitive) {

        resultEntity.setType(PQLVariable.TYPE_CLASS);
        StringBuffer query = new StringBuffer(QUERY_LENGTH);

        String universalSet = universalEntity.getName();

        String targetSet = targetEntity.getName();

        query
            .append("define ").append(resultEntity.getName() + " as " + universalSet + " - " + targetSet + " ; ")
            .append(" return " + resultEntity.getName() + " ;");

        return query.toString();
    }


}

