package org.acre.pdmengine;

import org.acre.pdmengine.model.ArtifactLink;
import org.acre.pdmengine.model.Artifact;
import org.acre.pdmengine.model.PatternResult;
import org.acre.pdmengine.model.RoleResult;
import org.acre.config.ConfigService;

import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;


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
 * @version Dec 13, 2004 11:53:43 PM
 */
public class RelcoFacadeTestCase extends PatternEngineBaseTestCase {

    protected void setUp() throws Exception {
        super.setUp();

    }

    // TODO
    // Test for various Relationship types - Calls, Uses, Implements - in progress
    // Test PDMs with multiple Roles & multiple relationships - in progress
    // More Test conditions - TBD
    // long relationship name - done
    // Find specific artifact - done
    // Implement appropriate Caching - done
    // Check in relevant GlobalPDM.xml
    // Some more test cases - clear printouts for manual verification (automated testing later)
    // findPDMs(artifacts[]); - TBD


    public void testRelcoBDCallingSF() {
        ArtifactLink links[];
        links = engine.findRelcos("BD_Calls_SF", "SessionFacadeInterface", "Calls");

        printLinks("Relcos  for 'calls SFInterface'", links);

        System.out.println();

        links = engine.findRelcos("BD_Calls_SF", "SessionFacadeInterface", "Implements");
        printLinks("Relcos  for 'implements SFInterface'", links);

        String artifactName = "com.sun.sjc.psa.ejb.CommitmentLocal";
        Artifact artifacts[];
        artifacts = engine.findRelcos("BD_Calls_SF", "SessionFacadeInterface",
                "Calls", artifactName);

        System.out.println("Relcos for " + artifactName);
        for ( int i=0; (artifacts != null) && i < artifacts.length; i++ ) {
            System.out.println(artifacts[i]);
        }

    }

    public void testCoarseRelcoBDCallingSF() {

        PatternResult pResult = engine.execute("BD_Calls_SF", SearchContext.COARSE);

        ArtifactLink links[];
        RoleResult sfIRR = pResult.getRoleReference("SessionFacadeInterface");
        links = engine.findRelcos(sfIRR,  "Calls");

        printLinks("Relcos  for 'calls SFInterface'", links);

        System.out.println();
        links = engine.findRelcos(sfIRR,  "Implements");
        printLinks("Relcos  for 'implements SFInterface'", links);

        String artifactName = "com.sun.sjc.psa.ejb.CommitmentLocal";
        Artifact[] artifacts = engine.findRelcos("BD_Calls_SF",
                "SessionFacadeInterface", "Calls", artifactName);

        System.out.println("Relcos for " + artifactName);
        for ( int i=0; i < artifacts.length; i++ ) {
            System.out.println(artifacts[i]);
        }


        links = engine.findRelcos("BD_Calls_SF",
                "SessionFacadeInterface", "Calls");

        printLinks("Relcos  for 'calls SFInterface'", links);


    }


    public void testRelcosBDCallingSingletons() {
        String pdm = "BD_Calls_Singletons";
        String rel[] = new String[]{"Calls"};
        System.out.println(pdm);
        ArtifactLink[] links;
        links = engine.findRelcos(pdm, "singleton", "Calls");
        printLinks("Relcos for Singleton ", links);

        Artifact artifacts[] ;
        artifacts = engine.findRelcos(pdm, "singleton", "Calls",
                "com.sun.sjc.psa.util.PSAServiceLocator");

        System.out.println("Relcos for " + "PSAServiceLocator");
        for ( int i=0; i < artifacts.length; i++ ) {
            System.out.println(artifacts[i]);
        }
    }

    public void    testInvRelcosBDCallingSingletons() {

        String pdm = "BD_Calls_Singletons";
        String rel[] = new String[]{"Calls"};
        System.out.println(pdm);
        ArtifactLink[] links;
        links = engine.findInvRelcos(pdm, "BusinessDelegate", "Calls");
        printLinks("InvRelcos of 'Calls BusinessDelegate'", links);

   //     map = engine.findInvRelcos(pdm, "Singletons", rel);
   //     links = (ArtifactLink[])map.get("Calls");
   //     printLinks("InvRelcos of 'Calls Singleton'", links);


        Artifact artifacts[] = engine.findInvRelcos(pdm, "BusinessDelegate", "Calls",
                "com.sun.sjc.psa.delegate.ProjectDelegate");

        System.out.println("InvRelcos for " + "com.sun.sjc.psa.delegate.ProjectDelegate");
        for ( int i=0; i < artifacts.length; i++ ) {
            System.out.println(artifacts[i]);
        }

    }

    public void testRelcosSFCallingDAO() {
        String pdm = "SF_Calls_DAO";
        ArtifactLink[] links;

        links = engine.findRelcos(pdm, "DataAccessObject", "Calls");
        printLinks("Relcos of DataAccessObject", links);

        links = engine.findInvRelcos(pdm, "SessionFacadeBean", "Calls");
        printLinks("InvRelcos of SessionFacadeBean", links);
    }


    public void testRelcoFCCallsBD() {
        ArtifactLink[] links  = engine.findRelcos("FC_Calls_BD",
                "BusinessDelegate", "Calls");
        printLinks("Relcos of BusinessDelegate", links);
    }

    public void testInvRelcoFCCallsBD() {
        ArtifactLink[] links = engine.findInvRelcos("FC_Calls_BD", "FrontController", "Calls");
        printLinks("InvRelcos of FrontController", links);

        PatternResult result = engine.execute("FC_Calls_BD", PatternEngine.COARSE);

        RoleResult role = result.getRoleReference("FrontController");

        links = engine.findInvRelcos(role, "Calls");
        printLinks("InvRelcos of FrontController", links);


    }

    public void testFCCallsSF() {
        String pdm = "FC_Calls_SF";
        ArtifactLink[] links;

        links = engine.findRelcos(pdm, "FrontController", "Calls");
        printLinks("Relcos of FrontController", links);

        links = engine.findRelcos(pdm, "BusinessDelegate", "Calls");
        printLinks("Relcos of BusinessDelegate", links);

        links = engine.findRelcos(pdm, "SessionFacadeInterface", "Calls");
        printLinks("Relcos of SessionFacadeInterface", links);

        links = engine.findInvRelcos(pdm, "FrontController", "Calls");
        printLinks("InvRelcos of FrontController", links);

        links = engine.findInvRelcos(pdm, "BusinessDelegate", "Calls");
        printLinks("InvRelcos of BusinessDelegate", links);

        links = engine.findInvRelcos(pdm, "SessionFacadeBean", "Calls");
        printLinks("InvRelcos of SessionFacadeBean", links);

    }


    public void testCoarseFCCallsSF() {
        String pdm = "FC_Calls_SF";
        String rel[] = new String[]{"Calls"};
        ArtifactLink[] links;
        Map map;
        PatternResult pResult = engine.execute(pdm, PatternEngine.COARSE);

        RoleResult fc = pResult.getRoleReference("FrontController");
        links = engine.findRelcos(fc, "Calls");
        printLinks("Relcos of FrontController", links);

        RoleResult bd = pResult.getRoleReference("BusinessDelegate");
        links = engine.findRelcos(bd, "Calls");
        printLinks("Relcos of BusinessDelegate", links);

        RoleResult sfi = pResult.getRoleReference("SessionFacadeInterface");
        links = engine.findRelcos(sfi, "Calls");
        printLinks("Relcos of SessionFacadeInterface", links);


        RoleResult fcRole = pResult.getRoleReference("FrontController");
        links = engine.findInvRelcos(fcRole, "Calls");
        printLinks("InvRelcos of FrontController", links);

        RoleResult bdRole = pResult.getRoleReference("BusinessDelegate");
        links = engine.findInvRelcos(bdRole, "Calls");
        printLinks("InvRelcos of BusinessDelegate", links);

        RoleResult sfBeanRole = pResult.getRoleReference("SessionFacadeBean");
        links = engine.findInvRelcos(sfBeanRole, "Calls");
        printLinks("InvRelcos of SessionFacadeBean", links);

    }



    public void testMicroArchitecturePDM() {
        String pdm = "mA_FC_BD_SF_DAO";
        String rel[] = new String[]{"Calls"};
        ArtifactLink[] links;
        Map map;

        links = engine.findRelcos(pdm, "BusinessDelegate", "Calls");
        printLinks("Relcos of BusinessDelegate", links);

        links = engine.findRelcos(pdm, "SessionFacadeInterface", "Calls");
        printLinks("Relcos of SessionFacadeInterface", links);

        links = engine.findInvRelcos(pdm, "BusinessDelegate", "Calls");
        printLinks("InvRelcos of BusinessDelegate", links);

        links = engine.findInvRelcos(pdm, "SessionFacadeBean", "Calls");
        printLinks("InvRelcos of SessionFacadeBean", links);
    }

/**
 public void testCommandCallsSF() {
         printRelcos(engine.execute("Command_Calls_SF"));
 }
 public void testRelcos1() {
     String pdm = "mA4";
     String rel[] = new String[]{"Calls"};
     engine.findRelcos(pdm, "Singleton", rel, "com.sun.sjc.psa.delegate.SkillDelegate");
 }

    public void testRelcosErrorConditions() {
        ArtifactLink[] links;
        try {
            links = engine.findRelcos("unknown", "Calls");
            fail("Should raise PatternEngineException");
        }
        catch(PatternEngineException e) {
            System.out.println(e.getMessage());
        }

        try {
            links = engine.findRelcos("BD_calls_singletons", "unknown");
            fail("Should raise PatternEngineException");
        }
        catch(PatternEngineException e) {
            System.out.println(e.getMessage());
        }

        try {
            links = engine.findRelcos("BD_calls_singletons", "");
            fail("Should raise PatternEngineException");
        }
        catch(PatternEngineException e) {
            System.out.println(e.getMessage());
        }

        try {
            links = engine.findRelcos("", null);
            fail("Should raise PatternEngineException");
        }
        catch(PatternEngineException e) {
            System.out.println(e.getMessage());
        }

        try {
            links = engine.findRelcos(null, "unknown");
            fail("Should raise PatternEngineException");
        }
        catch(PatternEngineException e) {
            System.out.println(e.getMessage());
        }

        try {
            links = engine.findRelcos("BD_calls_singletons", null);
            fail("Should raise PatternEngineException");
        }
        catch(PatternEngineException e) {
            System.out.println(e.getMessage());
        }

        Artifact artifacts[] = engine.findInvRelcos("BD_calls_singletons", "Calls",
                "unknownArtifact");
        assertEquals(artifacts.length, 0);
        try {
            artifacts = engine.findInvRelcos("BD_calls_singletons", "Calls",
                    null);
            fail("Should raise PatternEngineException");
        }
        catch(PatternEngineException e) {
            System.out.println(e.getMessage());
        }

    }


    public void testInvRelcosUsingArtifacts() {
        Artifact[] artifacts = engine.findInvRelcos("BD_calls_singletons", "Calls",
                "com.sun.sjc.psa.delegate.SearchDelegate");
        System.out.println("InvRelco for com.sun.sjc.psa.delegate.SearchDelegate artifact " );
        for ( int i =0; i < artifacts.length; i++)
            System.out.println(artifacts[i]);

        artifacts = engine.findInvRelcos("BD_calls_singletons", "Calls", "com.sun.sjc.psa.delegate.SkillDelegate");
        System.out.println("InvRelco for com.sun.sjc.psa.delegate.SkillDelegate artifact " );
        for ( int i =0; i < artifacts.length; i++)
            System.out.println(artifacts[i]);

    }

    public void testRelcosUsingArtifacts() {
        Artifact[] artifacts = engine.findRelcos("BD_calls_singletons", "Calls",
                "com.sun.sjc.psa.core.CommitmentStatus");
        System.out.println("Relco for CommitmentStatus artifact " );
        for ( int i =0; i < artifacts.length; i++)
            System.out.println(artifacts[i]);

        artifacts = engine.findRelcos("BD_calls_singletons", "Calls",
                "com.sun.sjc.psa.util.PSAServiceLocator");
        System.out.println("Relco for PSAServiceLocator artifact " );
        for ( int i =0; i < artifacts.length; i++)
            System.out.println(artifacts[i]);

    }


    public void testInvRelcos() {

        String pdm = "BD_calls_singletons";
        String relationship = "Calls";
        ArtifactLink[] links = engine.findInvRelcos(pdm, relationship);
        printLinks(links);

        links = engine.findInvRelcos(pdm.toLowerCase(), relationship.toLowerCase());
        printLinks(links);

        links = engine.findInvRelcos(pdm.toUpperCase(), relationship.toUpperCase());
        printLinks(links);

    }

*/

    public void testRelcoUsingRoleResult() {
        PatternResult patternResult = engine.execute("BD_Calls_SF", PatternEngine.COARSE);

        RoleResult sfi = patternResult.getRoleReference("SessionFacadeInterface");
        ArtifactLink links[];

        links = engine.findRelcos(sfi, "Calls");

        printLinks("Relcos  for 'calls SFInterface'", links);
        System.out.println();

        RoleResult sfRoleResult = patternResult.getRoleReference("SessionFacade.SessionFacadeInterface");
        assertNotNull(sfRoleResult);
        links = engine.findRelcos(sfRoleResult, "Calls");

        printLinks("Relcos  for 'calls SFInterface'", links);
        System.out.println();
    }

    public void testRelcoOfBD() {
        String relationshipTypes[] = new String[]{"Calls", "Implements"};
        PatternResult patternResult = engine.execute("BD_Calls_SF", PatternEngine.COARSE);


        RoleResult bd = patternResult.getRoleReference("BusinessDelegate");
        ArtifactLink links[];

        links = engine.findRelcos(bd, "Calls");

        printLinks("Relcos  for 'calls BusinessDelegate'", links);
        System.out.println();
    }

    public void testInvRelcoOfCoarseSF() {
        String relationshipTypes[] = new String[]{"Calls"};
        PatternResult patternResult = engine.execute("BD_Calls_SF", PatternEngine.COARSE);

        RoleResult sfbean = patternResult.getRoleReference("SessionFacade.SessionFacadeBean");
        ArtifactLink[] links = engine.findInvRelcos(sfbean, "Calls");

        printLinks("InvRelcos  for 'calls SessionFacadeBean'", links);
        System.out.println();
    }

    public void testInvRelcoOfExactSF() {
        PatternResult patternResult = engine.execute("BD_Calls_SF", PatternEngine.EXACT);

        ArtifactLink links[]= engine.findInvRelcos(patternResult.getName(), "SessionFacadeBean", "Calls");

        printLinks("InvRelcos  for 'calls SessionFacadeBean'", links);
        System.out.println();
    }

    public void testInvRelcoUsingRoleResult() {
        PatternResult patternResult = engine.execute("BD_Calls_SF", PatternEngine.COARSE);

        RoleResult sfBeanRole = patternResult.getRoleReference("SessionFacade.SessionFacadeBean");
        assertNotNull(sfBeanRole);
        ArtifactLink links[] = engine.findInvRelcos(sfBeanRole, "Calls");

        printLinks("InvRelcos  for 'calls SessionFacadeBean'", links);
        System.out.println();
    }


}
