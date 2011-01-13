package org.acre.analytics;

import org.acre.pdmengine.model.*;
import org.acre.pdmengine.PatternEngineBaseTestCase;
import org.acre.pdmengine.PatternEngine;
import org.acre.analytics.*;
import org.acre.dao.PatternRepository;
import org.acre.dao.DAOFactory;

import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
 * Copyright (c) 2004
 * Sun Microsystems, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Sun
 * Microsystems, Inc. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Sun.
 *
 * SUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. SUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 */

/**
 * @author rajmohan@Sun.com
 */
public class PatternMetricsTestCase extends PatternEngineBaseTestCase {
    public void testSimpleMetrics() {

        PatternResult result = engine.execute("BD_Calls_SF");

        printPatternMetrics(result, 0);

        result = engine.execute("BD_Calls_Singletons");

        printPatternMetrics(result, 0);

        result = engine.execute("FC_Calls_SF");

        printPatternMetrics(result, 0);


        result = engine.execute("Command_Calls_SF");

        printPatternMetrics(result, 0);

        result = engine.execute("mA_FC_BD_SF_DAO");

        printPatternMetrics(result, 0);



    }


    public void testPatternCompliance() {
        PatternResult result = engine.execute("Command_Calls_SF");

        printPatternMetrics(result, 0);


        printPatternSequences(result);
/**
        RelationshipResult relationship1 = (RelationshipResult)result.getRelationships().iterator().next();

        Iterator roles = result.getRoles().iterator();
        roles.next();
        RoleResult roleResult = (RoleResult)roles.next();

        assertNotNull(roleResult);

        PatternResult bdCallsSF = (PatternResult)roleResult.getRoleResult();
        assertNotNull(bdCallsSF);
        RelationshipResult relationship2 = (RelationshipResult)bdCallsSF.getRelationships().iterator().next();
        int count = getCrossLinksCount(relationship1, relationship2);
*/
        int count = getPatternComplianceCount(result);
        System.out.println("Cross Links count = " + count);

    }


    public void testPatternCompliance2() {
        PatternResult result = engine.execute("mA_Command_BD_SF_Singletons");

        printPatternMetrics(result, 0);

        printPatternSequences(result);

        int count = getPatternComplianceCount(result);
        System.out.println("Cross Links count = " + count);

    }



    public void testPatternCompliance3() {
            PatternResult result = engine.execute("BD_Calls_SF");

            printPatternMetrics(result, 0);
            printPatternSequences(result);
            int count = getPatternComplianceCount(result);
            System.out.println("Cross Links count = " + count);

            result = engine.execute("BD_Calls_Singletons");

            printPatternMetrics(result, 0);
            printPatternSequences(result);
            count = getPatternComplianceCount(result);
            System.out.println("Cross Links count = " + count);

            result = engine.execute("FC_Calls_SF");

            printPatternMetrics(result, 0);
            printPatternSequences(result);
            count = getPatternComplianceCount(result);
            System.out.println("Cross Links count = " + count);


            result = engine.execute("Command_Calls_SF");

            printPatternMetrics(result, 0);
            printPatternSequences(result);
            count = getPatternComplianceCount(result);
            System.out.println("Cross Links count = " + count);

            result = engine.execute("mA_FC_BD_SF_DAO");

            printPatternMetrics(result, 0);
            printPatternSequences(result);
            count = getPatternComplianceCount(result);
            System.out.println("Cross Links count = " + count);
    }

    private void printPatternSequences(PatternResult result) {
        Iterator relationships = result.getRelationships().iterator();

        while ( relationships.hasNext()) {
            RelationshipResult relationship = (RelationshipResult)relationships.next();
            System.out.println(" " + relationship.getName());
            ArtifactLink [] links = relationship.getLinks();
            for ( int i=0; i < links.length; i++) {
                ArtifactLink link = links[i];
                System.out.println("     " + link.getSourceArtifact().getName());
                Artifact [] targets = link.getTargetArtifacts();
                for ( int j=0; j < targets.length; j++) {
                    System.out.println("          " + targets[j].getName());
                }
            }
        }
    }


    void printPatternMetrics(PatternResult patternResult, int indent)  {
        char [] p = new char[indent*4];
        Arrays.fill(p, ' ');
        String buf = new String(p);
        System.out.println(buf + patternResult.getName() + " " + patternResult.getHitCount());

        Iterator roles = patternResult.getRoles().iterator();
        while ( roles.hasNext() ) {
            RoleResult roleResult = (RoleResult)roles.next();
            if ( roleResult.getRoleResult() instanceof PatternResult)
                printPatternMetrics((PatternResult)roleResult.getRoleResult(), indent+1);
            else
                System.out.println("  " + buf + roleResult.getName() + " " + roleResult.getHitCount());
        }
    }


    /**
     * Check for pattern compliance by traversing the artifact-links in all relationships
     * @param patternResult
     * @return
     */
    int getPatternComplianceCount(PatternResult patternResult) {

        List relationships = getOrderedRelationships(patternResult);

        int crossLinks = 0;
        if ( relationships.size() == 0 )
            return 0;
        else if ( relationships.size() == 1 ) {

            RelationshipResult relation = (RelationshipResult)relationships.get(0);
            ArtifactLink links[] = relation.getLinks();

            for ( int i=0; i < links.length; i++) {
                ArtifactLink link = links[i];
                Artifact targetArtifacts[] = link.getTargetArtifacts();
                crossLinks +=  targetArtifacts.length;
            }
        }
        else {
            RelationshipResult startRelation = (RelationshipResult) relationships.get(0);

            List chainedRelations = relationships.subList(1, relationships.size());

            ArtifactLink [] links = startRelation.getLinks();

            for ( int l=0; l<links.length; l++) {
                Artifact sourceArtifact = links[l].getSourceArtifact();
                Artifact [] targetArtifacts = links[l].getTargetArtifacts();

                for ( int t=0; t < targetArtifacts.length; t++) {
                    crossLinks += getEndPoints(targetArtifacts[t], chainedRelations, 0);
                }
            }
        }
        return crossLinks;
    }

    private int getEndPoints(Artifact artifact, List relations, int relIdx) {
        RelationshipResult relationship = (RelationshipResult)relations.get(relIdx);
        System.out.println("getEndPoints : IN  " + relationship.getName() + " Looking for " + artifact.getName());
        int linkCount = 0;
        ArtifactLink chainLink = relationship.getLink(artifact);
        if ( chainLink != null ) {
            if ( relIdx < relations.size()-1 ) {
                relIdx++;
                Artifact [] targetArtifacts = chainLink.getTargetArtifacts();
                for ( int t=0; t < targetArtifacts.length; t++ ) {
                    linkCount += getEndPoints(targetArtifacts[t], relations, relIdx);
                }
            }
            else  // end of chain...get artifact count
                linkCount = chainLink.getTargetArtifacts().length;
        }
        System.out.println("     returning " + linkCount);
        return linkCount;

    }


    List getOrderedRelationships(PatternResult patternResult) {
            List allRelationships = new ArrayList();
            collectRelationships(patternResult, "Calls", allRelationships);
            return allRelationships;
    }



    public void collectRelationships(PatternResult patternResult, String type, List result) {

        Iterator relationships;
        relationships = patternResult.getRelationships().iterator();
        while ( relationships.hasNext()) {
            RelationshipResult relationship = (RelationshipResult)relationships.next();
            if ( relationship.getRelationship().getType().equalsIgnoreCase(type))
                result.add(relationship);
        }

        Iterator roles;
        roles = patternResult.getRoles().iterator();
        while ( roles.hasNext()) {
            RoleResult role = (RoleResult)roles.next();
            if ( role.getRoleResult() instanceof PatternResult) {
                collectRelationships((PatternResult)role.getRoleResult(), type, result);
            }
        }
    }



    /**
     * Get the # of links crossing / chained from Relationship1 to Relationship2
     * i.e.,
     * Relationship1 {(a1,b1), (a1,b2), (a2, b1)}
     * Relationship2 {(b1,c1), (b1, c2), (b2,c1)}
     *
     * result would be 6 {(a1,b1,c1) (a1,b1,c2) (a1, b2, c1) (a2, b1, c1) (a2,b1,c2)}
     */
    int getCrossLinksCount(RelationshipResult relation1, RelationshipResult relation2 ) {
        ArtifactLink links1[] = relation1.getLinks();
        ArtifactLink links2[] = relation2.getLinks();

        int linkCount=0;

        for ( int i=0; i < links1.length; i++) {

            ArtifactLink link1 = links1[i];

            Artifact sourceArtifact = link1.getSourceArtifact();
            Artifact targetArtifacts[] = link1.getTargetArtifacts();

            for ( int t=0; t < targetArtifacts.length; t++) {
                Artifact targetArtifact = targetArtifacts[t];
                linkCount += getArtifactChainCount(targetArtifact, relation2);
            }
        }

        return linkCount;
    }

    private int getArtifactChainCount(Artifact artifact, RelationshipResult relationship) {
            int linkCount = 0;
            ArtifactLink chainLink = relationship.getLink(artifact);
            if ( chainLink != null ) {
                linkCount = chainLink.getTargetArtifacts().length;
            }
            return linkCount;
    }



    public void testAnalysticsEngine() {
        AcreContainer acreContainer = AcreContainerFactory.getInstance().getAcreContainer();
        PatternAnalyticsEngineImpl  analyticsEngine = (PatternAnalyticsEngineImpl)acreContainer.getAnalyticsEngine();

        PatternMetrics metrics;

        metrics = analyticsEngine.getPatternMetrics("BD_Calls_SF");
        printPatternMetrics(metrics, 0);

        metrics = analyticsEngine.getPatternMetrics("BusinessDelegate");
        printPatternMetrics(metrics, 0);

        metrics = analyticsEngine.getPatternMetrics("SessionFacade");
        printPatternMetrics(metrics, 0);

        metrics = analyticsEngine.getPatternMetrics("SessionFacade");
        printPatternMetrics(metrics, 0);


        metrics = analyticsEngine.getPatternMetrics("BD_Calls_Singletons");
        printPatternMetrics(metrics, 0);

        metrics = analyticsEngine.getPatternMetrics("FC_Calls_SF");
        printPatternMetrics(metrics, 0);

        metrics = analyticsEngine.getPatternMetrics("Command_Calls_SF");
        printPatternMetrics(metrics, 0);

        metrics = analyticsEngine.getPatternMetrics("mA_FC_BD_SF_DAO");
        printPatternMetrics(metrics, 0);

    }

    public void testAnalyticsEngineDashboard() {
        AcreContainer acreContainer = AcreContainerFactory.getInstance().getAcreContainer();
        PatternAnalyticsEngine  analyticsEngine = acreContainer.getAnalyticsEngine();
        Snapshots dashboard;

        dashboard = analyticsEngine.getSnapshots(null, null);

        printSnapshots(dashboard);
    }

    private void printSnapshots(Snapshots snapshots) {

        Iterator ssIterator = snapshots.getAllSnapshots();

        while ( ssIterator.hasNext() ) {
            Snapshot ss = (Snapshot)ssIterator.next();

            System.out.print(" system = " + ss.getSystem());
            System.out.print(" version= " + ss.getVersion());
            System.out.print(" timestamp="+ss.getTimestamp());
            System.out.println("------------");
            Iterator pmetricsItr =  ss.getPatternMetrics().iterator();
            while ( pmetricsItr.hasNext() ) {
                PatternMetrics pm = (PatternMetrics)pmetricsItr.next();
                printPatternMetrics(pm, 0);
            }
        }
    }

    private void printPatternMetrics(PatternMetrics metrics, int indent) {
        char [] p = new char[indent*4];
        Arrays.fill(p, ' ');
        String buf = new String(p);
        System.out.println(buf + metrics.getPatternName() + " " + metrics.getHitCount());

        Iterator roles = metrics.getRoles().iterator();
        while ( roles.hasNext() ) {
            PatternMetrics role = (PatternMetrics)roles.next();
                printPatternMetrics(role, indent+1);
        }

    }


    public void testMicroArch() {
        AcreContainer acreContainer = AcreContainerFactory.getInstance().getAcreContainer();
        PatternAnalyticsEngineImpl  analyticsEngine = (PatternAnalyticsEngineImpl)acreContainer.getAnalyticsEngine();
        PatternMetrics metrics;

        metrics = analyticsEngine.getPatternMetrics("mA_FC_BD_SF_DAO");
        printPatternMetrics(metrics, 0);
    }

    public void testMicroArchWithTimestamp() throws ParseException {
        AcreContainer acreContainer = AcreContainerFactory.getInstance().getAcreContainer();
        PatternAnalyticsEngine  analyticsEngine = acreContainer.getAnalyticsEngine();
        PatternMetrics metrics[];

        Date dt1, dt2;

        dt1 = new SimpleDateFormat("dd-MM-yyyy").parse("01-01-2005");
        dt2 = new SimpleDateFormat("dd-MM-yyyy").parse("01-05-2005");
//        dt2 = Calendar.getInstance().getTime();


        String names[] = {
                          "BD_Calls_SF",
                   //       "BusinessDelegate",
                   //       "SessionFacade",
                    //      "ClassesWithAbsAndNonAbsMethods",
                   //       "EJBClasses",
                   //       "EJBHomes",
                   //       "EJBLocalHomes",
                   //       "EJBLocalObjects",
                  //        "EJBRemoteObjects",
                 //         "EntityBeans",
                 //         "Servlets",
                   //       "SessionBeans",
                     //     "SingletonGroovyPDM",
                      //    "IF_intercepts_FC",
                       //   "InterceptingFilter",
                       //   "FrontController",
                   //       "BD_Calls_Singletons",
                       //   "FC_Calls_BD",
                       //   "FC_Calls_SF",
                       //   "Command_Calls_SF",
                       //   "SF_Calls_DAO",
                    //    "PackageView"
        };

        for ( int k=0; k < names.length; k++ ) {
            try {
                System.out.println("Pattern Name : " + names[k]);
                /*
                PatternResult []result = engine.execute(names[k], null, new String[]{"PSA"}, dt1, dt2);
                for ( int j=0; j<result.length; j++)
                    System.out.println(result[j]);
                */
                SnapshotQuery query = new SnapshotQuery();
                query.addSystemFilter("PSA");
                query.setStartDate(dt1);
                query.setEndDate(dt2);
                query.addPatternFilter(names[k]);
                Snapshots snapshots = analyticsEngine.getSnapshots(query);
                printSnapshots(snapshots);
            }
            catch(Throwable t) {
                System.out.println(t.getMessage());
            }
        }
    }

    public void testDashboardMetrics2() throws ParseException {
        testDashboardMetrics();
        testDashboardMetrics();
    }

    public void testDashboardMetrics() throws ParseException {
        AcreContainer acreContainer = AcreContainerFactory.getInstance().getAcreContainer();
        PatternAnalyticsEngine  analyticsEngine = acreContainer.getAnalyticsEngine();

        Snapshots snapshots;

        Date dt1, dt2;

        long startTS = System.currentTimeMillis();

        dt1 = new SimpleDateFormat("dd-MM-yyyy").parse("01-01-2005");
        dt2 = Calendar.getInstance().getTime();

        snapshots = analyticsEngine.getSnapshots(dt1, dt2);

        printSnapshots(snapshots);

    }
/**
    private void printSnapshots(Snapshots snapshots) {
        Iterator ssItr = snapshots.getSortedByTime();

        System.out.print("Pattern/Timestamp    ");
        while ( ssItr.hasNext()) {
            Snapshot ss = (Snapshot)ssItr.next();
            System.out.print(ss.getTimestamp() +  "           ");
        }
        System.out.println("");

        Iterator pnamesItr = snapshots.getPatternNames();

        while ( pnamesItr.hasNext()) {
            String patternName = (String)pnamesItr.next();
            System.out.print(patternName+ "       ");

            ssItr = snapshots.getSortedByTime();
            while( ssItr.hasNext() ) {
                Snapshot ss = (Snapshot)ssItr.next();

                PatternMetrics pm = ss.getPatternMetrics(patternName);
                if ( pm != null )
                    printPatternMetrics(pm, 0);

            }
            System.out.println("");
        }
    }
**/

}
