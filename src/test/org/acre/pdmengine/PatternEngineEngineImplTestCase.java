/*
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
package org.acre.pdmengine;

import org.acre.config.ConfigService;
import org.acre.lang.pql.pdbc.PQLException;
import org.acre.pdmengine.model.PatternResult;
import org.acre.pdmengine.model.RelationshipResult;
import org.acre.pdmengine.model.Result;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.util.Iterator;

/**
 * User: rajmohan@sun.com
 * Date: Nov 3, 2004
 * Time: 8:51:31 PM
 */
public class PatternEngineEngineImplTestCase extends PatternEngineBaseTestCase {
    public static Test suite() {
      return new TestSuite(PatternEngineEngineImplTestCase.class);
    }

    public PatternEngineEngineImplTestCase() {
        super();
    }

    public PatternEngineEngineImplTestCase(String s) {
        super(s);
    }

    protected void setUp() throws Exception {
        ConfigService configService = ConfigService.getInstance();
        configService.setTemporaryAcreRepositoryDirectory("c:/salsa/salsa/src/test/TestRepository");
//        configService.setTemporaryAcreRepositoryDirectory("c:/ace/SALSA/workspace/salsa/src/main/TestRepository");
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testSimplePDM() throws PQLException {
            PatternResult patternResult = engine.execute("IF_intercepts_FC");

            Iterator relationships = patternResult.getRelationships().iterator();
            while ( relationships.hasNext() ) {
                RelationshipResult relationship = (RelationshipResult)relationships.next();

                RelationshipValidator relationshipValidator = new RelationshipValidator(PSATestFacts.IFCallsFC);
                relationshipValidator.validateRelationship(relationship);
            }
            log.info(patternResult.toString());
    }


    public void testCallsTypePDM() throws PQLException {
            PatternResult patternResult = engine.execute("IF_intercepts_FC_2");

            Iterator relationships = patternResult.getRelationships().iterator();
            while ( relationships.hasNext() ) {
                RelationshipResult relationship = (RelationshipResult)relationships.next();

                RelationshipValidator relationshipValidator = new RelationshipValidator(PSATestFacts.IFCallsFC);
                relationshipValidator.validateRelationship(relationship);
            }
            log.info(patternResult.toString());
    }


    public void testApplicationControllerPDM() throws PQLException {
            PatternResult patternResult = engine.execute("ApplicationController");

            Iterator relationships = patternResult.getRelationships().iterator();

            while ( relationships.hasNext() ) {
                RelationshipResult relationship = (RelationshipResult)relationships.next();

                if ( "resolves".equalsIgnoreCase(relationship.getName()) ) {
                    RelationshipValidator relationshipValidator = new RelationshipValidator(PSATestFacts.FCCallsMapper);
                    relationshipValidator.validateRelationship(relationship);
                }
                else if ( "uses".equalsIgnoreCase(relationship.getName()) ) {
                    RelationshipValidator relationshipValidator = new RelationshipValidator(PSATestFacts.MapperUsesMap);
                    relationshipValidator.validateRelationship(relationship);
                }
            }

            log.info(patternResult.toString());
    }


    public void testSessionFacadePDM() throws PQLException {
            PatternResult patternResult = engine.execute("SessionFacade");
            log.info(patternResult.toString());

            Iterator relationships = patternResult.getRelationships().iterator();

            relationships = patternResult.getRelationships().iterator();

            while ( relationships.hasNext() ) {
                RelationshipResult relationship = (RelationshipResult)relationships.next();
                if ( "Creates".equalsIgnoreCase(relationship.getName())) {
                    printLinks(relationship.getLinks());

                    RelationshipValidator validator = new RelationshipValidator(PSATestFacts.SFHomeCreatesSFInterface);
                    validator.validateRelationship(relationship);
                }
                else if ( "Implements".equalsIgnoreCase(relationship.getName())) {
                    printLinks(relationship.getLinks());
                    // TODO : J2EE fact extractor to generate synthetic facts
                }
            }
    }


    public void testCommandFrameworkPDM() {
        Result pdmResult = engine.execute("CommandFramework");
        log.info(pdmResult.toString());

    }

    public void testRecursionPDM() {
        PatternResult patternResult = engine.execute("FC_Calls_FC");
        log.info(patternResult.toString());

        Iterator relationships = patternResult.getRelationships().iterator();
        while (relationships.hasNext()) {
            RelationshipResult relationshipResult = (RelationshipResult)relationships.next();
            printLinks(relationshipResult.getLinks());

            RelationshipValidator relationshipValidator = new RelationshipValidator(PSATestFacts.FCCallsFC);
            relationshipValidator.validateRelationship(relationshipResult);
        }

    }

    public void testBDCallsSFPDM() {
        PatternResult patternResult = engine.execute("BD_calls_SF");
        System.out.println(patternResult.toString());

        Iterator relationships = patternResult.getRelationships().iterator();

        while ( relationships.hasNext() ) {
            RelationshipResult relationship = (RelationshipResult)relationships.next();

            if ( "Calls".equalsIgnoreCase(relationship.getName()) ) {
                RelationshipValidator relationshipValidator = new RelationshipValidator(PSATestFacts.BDCallsSF);
                relationshipValidator.validateRelationship(relationship);
            }

        }
    }

}
