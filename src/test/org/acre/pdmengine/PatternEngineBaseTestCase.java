package org.acre.pdmengine;

import org.acre.dao.*;
import org.acre.lang.pql.pdbc.PQLConnection;
import org.acre.pdmengine.model.*;
import org.acre.pdmengine.pqe.PQLEngineFacade;
import org.acre.pdmengine.pqe.PQLEngineFacadeFactory;
import org.acre.pdmengine.core.PatternSearchEngine;
import junit.framework.TestCase;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Arrays;

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
 * @version Nov 10, 2004 11:43:40 AM
 */
public abstract class PatternEngineBaseTestCase extends TestCase {
    public final static String MODULE_NAME = "PDMUnitTest";
    Logger log = Logger.getLogger(MODULE_NAME);

    protected PatternEngine engine;
    protected PatternSearchEngine searchEngine;
    protected PatternRepository facade;
    protected PatternQueryRepository patternQueryRepository;
    protected PQLConnection pqlConnection;
    protected PQLEngineFacade pqlEngineFacade;

    public PatternEngineBaseTestCase() {
        super();
    }

    public PatternEngineBaseTestCase(String s) {
        super(s);
    }

    protected void setUp() throws Exception {
        super.setUp();

        facade = DAOFactory.getPatternRepository();
        facade.refresh();

        patternQueryRepository = DAOFactory.getPatternQueryRepository();
        patternQueryRepository.refreshCache();


        engine = (PatternEngineImpl)PatternEngineFactory.getInstance().createEngine();
        engine.setPatternRepository(facade);
        engine.setPatternQueryRepository(patternQueryRepository);
        pqlEngineFacade = ((PatternEngineImpl)engine).getPqlEngineFacade();
        pqlConnection = pqlEngineFacade.getPQLConnection();

        PQLEngineFacade pqlFacade = PQLEngineFacadeFactory.getInstance().createPQLEngineFacade();
        searchEngine = new PatternSearchEngine(pqlFacade, facade, patternQueryRepository);

        Logger logger = Logger.getLogger("org.acre.pdmengine");
        logger.setLevel(Level.OFF);

    }

    protected void tearDown() throws Exception {
        super.tearDown();

        engine.shutdown();

        engine.refreshAll();
    }

    protected void printLinks(String header, ArtifactLink[] artifactLinks) {
        System.out.println(header);

        char line[] = new char[header.length()];
        Arrays.fill(line, '-');
        System.out.println(line);
        printLinks(artifactLinks);
    }
    protected void printLinks(ArtifactLink[] artifactLinks) {
        for ( int iLink=0; iLink < artifactLinks.length; iLink++ ) {
            ArtifactLink link = artifactLinks[iLink];
            System.out.println(link);
        }
/**
        if ( artifactLinks.length > 0 )
        {
            Artifact sourceArtifact = artifactLinks[0].getSourceArtifact();
            Artifact targetArtifact = artifactLinks[0].getTargetArtifacts()[0];

            try {
                PQLValueHolder sourceVH = (PQLValueHolder)sourceArtifact.getValue();
                pqlConnection.fetchComplete(sourceVH);
                PQLValueHolder methods[] = sourceVH.getRelationship("methods");
                for ( int i=0; i < methods.length; i++) {
                    PQLValueHolder method = methods[i];
                    System.out.println("Method : " + method.getName());
                    pqlConnection.fetchComplete(method);
                    PQLValueHolder callsMethods[] = method.getRelationship("calls");
                    for ( int c=0; c < callsMethods.length; c++ ) {
                        PQLValueHolder calledMethod = callsMethods[c];
                        System.out.println("Called Method : " + calledMethod.getName());
                    }

                }

            } catch (PQLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
**/
    }

    protected void printForwardLinks(RelationshipResult result) {
        log.info("Forward Links>>>>>>>>>>>>>>>");

        QueryResult fromRoleQR = (QueryResult)result.getFromRole().getRoleResult();
        Artifact artifacts[] = fromRoleQR.getArtifacts();

        for ( int i=0; i < artifacts.length; i++ ) {
            assertTrue(artifacts[i] instanceof Artifact);
            Artifact artifact = (Artifact)artifacts[i];
            Artifact targets[] = result.getLink(artifact).getTargetArtifacts();
            assertNotNull(targets);

            log.info("Link targets for " + artifact);
            for ( int j=0; j < targets.length; j++) {
                System.out.println("Links....               " + targets[j]);
            }
        }
    }

/*
    protected void printReverseLinks(RelationshipResult result) {
        log.info("Reverse Links<<<<<<<<<<<<<<<");

        QueryResult toRoleQR = (QueryResult)result.getToRole().getRoleReference();
        Object artifacts[]= toRoleQR.getArtifactsHolder().getArtifactsHolder(0);
        for ( int i=0; i < artifacts.length; i++ ) {
            PQLArtifact artifact = (PQLArtifact)artifacts[i];
            Object sources[] = result.getLinkedSourceArtifacts(artifact);
            assertNotNull(sources);

            System.out.println("Linked targets for " + artifact);
            for ( int j=0; j < sources.length; j++) {
                System.out.println("Links....               " + sources[j]);
            }

        }
    }
*/
}


