package org.acre.pdmengine;

import org.acre.pdmengine.model.*;


import java.util.Iterator;

/**
 *
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
 * @version Dec 9, 2004 4:27:32 PM
 */
public class PatternEngineCompTestCase extends PatternEngineBaseTestCase {

/**
    public void testRelco() {
        PatternResult patternResult = engine.execute("BD_calls_SF");
        System.out.println(patternResult.toString());

        Iterator relationships = patternResult.getRelationships().iterator();

        while ( relationships.hasNext() ) {
            RelationshipResult relationship = (RelationshipResult)relationships.next();

            if ( "Calls".equalsIgnoreCase(relationship.getName()) ) {
                RelationshipValidator relationshipValidator = new RelationshipValidator(PSATestFacts.BDCallsSF);
                relationshipValidator.validateRelationship(relationship);
            }

            System.out.println("FROM ROLE ...................");
            System.out.println(relationship.getFromRole());

            System.out.println("TO ROLE......................");
            System.out.println(relationship.getToRole());

            ArtifactLink link[] = relationship.getLinks();
//            System.out.println("Links " + links);
            printLinks(link);

            ArtifactLink [] relcoLinks = relationship.findRelcos();
            printLinks(relcoLinks);


            RelationshipValidator relationshipValidator = new RelationshipValidator(PSATestFacts.RelcoOfBDCallsSF);
            relationshipValidator.validateLinks(relcoLinks);

            findRelcosUsingArtifacts(link, relationship);
        }
    }

    private void findRelcosUsingArtifacts(ArtifactLink[] links, RelationshipResult relationship) {
        for ( int iLink=0; iLink < links.length; iLink++ ) {
            Artifact targetArtifacts[];
            targetArtifacts = links[iLink].getTargetArtifacts();
            for ( int iTarget=0; iTarget < targetArtifacts.length; iTarget++ ) {
                Artifact relcos[] = relationship.findRelcos(targetArtifacts[iTarget]);
                System.out.println("<Relco for " + targetArtifacts[iTarget] + ">");
                for ( int j=0; j<relcos.length;j++) {
                    System.out.println("Relco -> " + relcos[j]);
                }
                System.out.println("<///Relco for " + targetArtifacts[iTarget] + ">");
            }
        }
    }

    private void findRelcosUsingArtifacts(Artifact[] artifacts, RelationshipResult relationship) {
        for ( int i=0; i < artifacts.length; i++ ) {
            Artifact targetArtifacts[];
            targetArtifacts = relationship.findRelcos((Artifact)artifacts[i]);
            System.out.println("<Relco for " + artifacts[i] + ">");
            for ( int j=0; j<targetArtifacts.length;j++) {
                System.out.println("Relco -> " + targetArtifacts[j]);
            }
            System.out.println("<///Relco for " + artifacts[i] + ">");
        }
    }

    private void findInvRelcosUsingArtifacts(Artifact[] artifacts, RelationshipResult relationship) {
        for ( int i=0; i < artifacts.length; i++ ) {
            Artifact targetArtifacts[];
            targetArtifacts = relationship.findInvRelcos((Artifact)artifacts[i]);
            System.out.println("<InvRelco for " + artifacts[i] + ">");
            for ( int j=0; j<targetArtifacts.length;j++) {
                System.out.println("InvRelco -> " + targetArtifacts[j]);
            }
            System.out.println("<///InvRelco for " + artifacts[i] + ">");
        }
    }

    public void testInvRelco() {
        PatternResult patternResult = engine.execute("BD_calls_SF");
        System.out.println(patternResult.toString());

        Iterator relationships = patternResult.getRelationships().iterator();

        while ( relationships.hasNext() ) {
            RelationshipResult relationship = (RelationshipResult)relationships.next();

            if ( "Calls".equalsIgnoreCase(relationship.getName()) ) {
                RelationshipValidator relationshipValidator = new RelationshipValidator(PSATestFacts.BDCallsSF);
                relationshipValidator.validateRelationship(relationship);
            }

            System.out.println("FROM ROLE ...................");
            System.out.println(relationship.getFromRole());

            System.out.println("TO ROLE......................");
            System.out.println(relationship.getToRole());

            ArtifactLink link[] = relationship.getLinks();
//            System.out.println("Links " + links);
            printLinks(link);

            ArtifactLink [] invRelcoLinks = relationship.findInvRelcos();
            printLinks(invRelcoLinks);

            RelationshipValidator relationshipValidator = new RelationshipValidator(PSATestFacts.InvRelcoOfBDCallsSF);
            relationshipValidator.validateLinks(invRelcoLinks);

        }
    }

    public void testRelcoUsingGroovyPDM() {
        PatternResult patternResult = engine.execute("BD_calls_Singletons");
        System.out.println(patternResult.toString());

        Iterator relationships = patternResult.getRelationships().iterator();

        while ( relationships.hasNext() ) {
            RelationshipResult relationship = (RelationshipResult)relationships.next();

            System.out.println("InvRelcos");
            ArtifactLink [] invRelcoLinks = relationship.findInvRelcos();
            printLinks(invRelcoLinks);

            System.out.println("Relcos");
            ArtifactLink [] relcoLinks = relationship.findRelcos();
            printLinks(relcoLinks);


            System.out.println("Links");
            ArtifactLink link[] = relationship.getLinks();
            printLinks(link);
            QueryResult queryResult = (QueryResult)relationship.getToRole().getRoleResult();
            Artifact artifacts[] = queryResult.getArtifacts();

            findRelcosUsingArtifacts(artifacts, relationship);

            queryResult = (QueryResult)relationship.getFromRole().getRoleResult();
            artifacts = queryResult.getArtifacts();
            findInvRelcosUsingArtifacts(artifacts, relationship);
        }
    }

    public void testMultipleCalls() {
        PatternResult patternResult = engine.execute("SF_calls_DAO");
        System.out.println(patternResult.toString());

        Iterator relationships = patternResult.getRelationships().iterator();

        while ( relationships.hasNext() ) {
            RelationshipResult relationship = (RelationshipResult)relationships.next();

            System.out.println("Relcos");
            ArtifactLink [] relcoLinks = relationship.findRelcos();
            printLinks(relcoLinks);

            System.out.println("Relcos");
            relcoLinks = relationship.findRelcos();
            printLinks(relcoLinks);

            System.out.println("InvRelcos");
            ArtifactLink [] invRelcoLinks = relationship.findInvRelcos();
            printLinks(invRelcoLinks);

            System.out.println("Links");
            ArtifactLink link[] = relationship.getLinks();
            printLinks(link);

            System.out.println("Relcos");
            relcoLinks = relationship.findRelcos();
            printLinks(relcoLinks);

            System.out.println("InvRelcos");
            invRelcoLinks = relationship.findInvRelcos();
            printLinks(invRelcoLinks);

            System.out.println("Links");
            link = relationship.getLinks();
            printLinks(link);

            System.out.println("Relcos");
            relcoLinks = relationship.findRelcos();
            printLinks(relcoLinks);

            System.out.println("InvRelcos");
            invRelcoLinks = relationship.findInvRelcos();
            printLinks(invRelcoLinks);

        }
    }


    public void testSFCallsDAO() {
        printRelcos(engine.execute("SF_calls_DAO"));
    }

    public void testFCCallsSF() {
        printRelcos(engine.execute("FC_Calls_SF"));
    }

    public void testCommandCallsSF() {
        printRelcos(engine.execute("Command_Calls_SF"));
    }

    public void testMA2() {
        printRelcos(engine.execute("mA2"));
    }

    private void printRelcos(PatternResult patternResult) {
//        System.out.println("<PatternResult.toString>");
//        System.out.println(patternResult.toString());
//        System.out.println("</PatternResult.toString>");

        Iterator roles = patternResult.getRoles().iterator();
        while ( roles.hasNext() ) {
            RoleResult roleResult = (RoleResult)roles.next();
            Result roleRoleResult = roleResult.getRoleResult();
            if ( roleRoleResult instanceof PatternResult )
                printRelcos((PatternResult)roleRoleResult);
        }

        Iterator relationships = patternResult.getRelationships().iterator();
        while ( relationships.hasNext() ) {
            RelationshipResult relationship = (RelationshipResult)relationships.next();

            System.out.println("<Relationship : " +
                        relationship.getRelationship().getName() + " - " +
                        relationship.getFromRole().getName() + " " +
                        relationship.getRelationship().getType() + " " +
                        relationship.getToRole().getName());
            System.out.println("<Links---------------------------->");
            ArtifactLink link[] = relationship.getLinks();
            printLinks(link);
            System.out.println("</Links---------------------------->");

            System.out.println("<Relcos--------------------------->");
            ArtifactLink [] relcoLinks = relationship.findRelcos();
            printLinks(relcoLinks);
            System.out.println("</Relcos--------------------------->");

            System.out.println("<InvRelcos--------------------------->");
            ArtifactLink [] invRelcoLinks = relationship.findInvRelcos();
            printLinks(invRelcoLinks);
            System.out.println("</InvRelcos--------------------------->");
            System.out.println("</Relationship : " + relationship.getRelationship().getName());

        }
    }

 **/
 
}
