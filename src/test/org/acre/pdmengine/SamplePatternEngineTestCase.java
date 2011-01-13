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
package org.acre.pdmengine;


import org.acre.pdmengine.model.*;
import org.acre.pdmengine.model.impl.PatternResultImpl;
import org.acre.pdm.RoleType;
import org.acre.dao.PatternRepository;
import org.acre.lang.pql.pdbc.PQLException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.util.*;
import java.io.*;

/**
 * @author rajmohan@Sun.com
 * @version Nov 10, 2004 11:40:15 AM
 */
public class SamplePatternEngineTestCase extends PatternEngineBaseTestCase {

    public SamplePatternEngineTestCase() {
        super();
    }

    public SamplePatternEngineTestCase(String s) {
        super(s);
    }


    public void testBDCallsSFPDM() {
        SearchContext ctx = new SearchContext();
        ctx.setSearchType(SearchContext.COARSE);
        PatternResult patternResult = engine.execute("BD_Calls_SF", null, ctx)[0];
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

            System.out.println("From Role's Parent");
            System.out.println(relationship.getFromRole().getParent().getName());
            assertEquals("BusinessDelegate", relationship.getFromRole().getParent().getName());

            System.out.println("To Role's Parent");
            System.out.println(relationship.getToRole().getParent().getName());
            assertEquals("SessionFacade", relationship.getToRole().getParent().getName());

            System.out.println("Relationship's Parent");
            System.out.println(relationship.getParent().getName());
            assertEquals("BD_Calls_SF", relationship.getParent().getName());

        }

        RoleResult bizDelegate = patternResult.getRoleReference("BusinessDelegate");
        System.out.println("BusinessDelegate role found");

        Artifact artifact =
                bizDelegate.findArtifact("com.sun.sjc.psa.delegate.CommitmentDelegate");

        System.out.println("artifact = "+ artifact);

    }


    public void testIFinterceptsFC() {

        PatternResult patternResult = engine.execute("IF_intercepts_FC");
        log.info(patternResult.toString());

        Iterator relationshipsItr = patternResult.getRelationships().iterator();
        while ( relationshipsItr.hasNext() ) {
            RelationshipResult relationship = (RelationshipResult) relationshipsItr.next();

            RelationshipValidator relationshipValidator = new RelationshipValidator(PSATestFacts.IFCallsFC);
            relationshipValidator.validateRelationship(relationship);

        }
    }

    public void testFCCallsSF() {
        PatternResult patternResult = engine.execute("FC_calls_SF");
        log.info(patternResult.toString());
    }

    public void testCommandCallsSF() {
        SearchContext ctx = new SearchContext();
        ctx.setSearchType(SearchContext.COARSE);
        PatternResult patternResult = engine.execute("Command_Calls_SF", null, ctx)[0];
//        log.info(patternResult.toString());
        printPDMRoles(patternResult);
        Iterator relationships = patternResult.getRelationships().iterator();

        while ( relationships.hasNext() ) {

            RelationshipResult relationship;
            relationship = (RelationshipResult)relationships.next();

            if ( "Calls".equalsIgnoreCase(relationship.getName()) ) {

                ArtifactLink link[]  = relationship.getLinks();
                printLinks(link);

                RelationshipValidator relationshipValidator = new RelationshipValidator(PSATestFacts.CommandCallsBD);
                relationshipValidator.validateRelationship(relationship);

                System.out.println("FROM ROLE ...................");
//                System.out.println(relationship.getFromRole());

                System.out.println("TO ROLE......................");
//                System.out.println(relationship.getToRole());
            }

        }


    }

    void printPDMRoles(PatternResult patternResult) {
        // traverse roles
        System.out.println("PDM : " + patternResult.getName());

        Iterator roles = patternResult.getRoles().iterator();
        while ( roles.hasNext() ) {
            RoleResult roleResult = (RoleResult)roles.next();
            Result result = roleResult.getRoleResult();

            if ( result instanceof PatternResult ) {
                  printPDMRoles((PatternResult)result);
            }
        }
    }

    public void testmA2() {
        PatternResult patternResult = engine.execute("mA_FC_BD_SF_DAO");
        log.info(patternResult.toString());

    }

    public void testGroovyPDM0() {

        SearchContext ctx = new SearchContext();
        ctx.setSearchType(SearchContext.COARSE);

        PatternResult patternResult = engine.execute("SingletonGroovyPDM");
        System.out.println(patternResult.toString());
    }
    public void testGroovyPDM() {
        Map params;

        params = new HashMap();
        params.put("package", "com.sun.sjc.psa.util");

        SearchContext ctx = new SearchContext();
        ctx.setSearchType(SearchContext.COARSE);

        PatternResult patternResult = engine.execute("SingletonGroovyPDM", params, ctx)[0];
        log.info(patternResult.toString());

        params = new HashMap();
        params.put("package", "com.sun.sjc.psa.core");
        patternResult = engine.execute("SingletonGroovyPDM", params);
        log.info(patternResult.toString());

    }

    public void testGroovyPDM2() {
        PatternResult patternResult = engine.execute("BD_Calls_Singletons");
        log.info(patternResult.toString());
        RelationshipResult relationship = (RelationshipResult)patternResult.getRelationships().iterator().next();
        printLinks(relationship.getLinks());
    }

    public void testRecursivePDM() {
        PatternResult patternResult = engine.execute("RecursivePDM");
        log.info(patternResult.toString());
        RelationshipResult relationship = (RelationshipResult)patternResult.getRelationships().iterator().next();
        printLinks(relationship.getLinks());
    }

    public void testMultiplePDMs() {
        testBDCallsSFPDM();
        testBDCallsSFPDM();
    }

    public void testMultiplePDMs2() {
        testPDM("BD_calls_SF");
        testPDM("SF_calls_DAO");

    }

    private void testPDM(String pdmName) {
        PatternResult result = engine.execute(pdmName);
        log.info(result.toString());
    }

/**
    public void testPackageArtifactsGroovyPDM() {
        Map params;

        params = new HashMap();
        params.put("param1", "com.sun.sjc.psa.util");
        PatternResult pdmResult = engine.execute("PackageArtifacts", params);
        log.info(pdmResult.toString());

        params = new HashMap();
        params.put("param1", "com.sun.sjc.psa.core");
        pdmResult = engine.execute("PackageArtifacts", params);
        log.info(pdmResult.toString());

    }
*/
    public void atestPackageDependencies() {
        PatternResult result = engine.execute("PackageView");
        log.info(result.toString());

        List relationships = engine.discoverRoleRelationships("PackageView", new String[]{"Calls"});

        for (int i = 0; i < relationships.size(); i++) {
            Object o = relationships.get(i);
            System.out.println("o = " + (RelationshipResult)o);
        }

    }
/**
    public void testPackageViewPDM() {
        PatternResult result = engine.execute("PackageViewPDM");
        log.info(result.toString());

//        List relationships = engine.discoverRoleRelationships("PackageViewPDM", new String[]{"Calls"});

        Iterator itr = result.getRoles().iterator();

        List pdms = new ArrayList();

        while ( itr.hasNext()) {
            RoleResult rolRst = (RoleResult)itr.next();
            pdms.add(((PatternResult)rolRst.getRoleResult()));
        }

        System.out.println("packages = " + pdms);

        PatternResult pdmResults[] = new PatternResult[pdms.size()];
        pdms.toArray(pdmResults);

        List relRst = engine.discoverRelationships(pdmResults, new String[]{"Calls"});

        System.out.println("Dynamically discovered relationships");
        System.out.println("-----------------------------------");

        for (int i = 0; i < relRst.size(); i++) {
            Object o = relRst.get(i);
            System.out.println("o = " + o);
        }


    }
    public void testPackageViewPDM2() {
        PatternResult result = engine.execute("PackageViewPDM2");
        log.info(result.toString());
    }
 */


    public void testPDMSerialization() {
        PatternResult result = engine.execute("BD_Calls_SF");

        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("PDM.ser"));

            os.writeObject(result);
            os.close();

            PatternResult serResult;

            ObjectInputStream is = new ObjectInputStream(new FileInputStream("PDM.ser"));
            serResult = (PatternResult)is.readObject();

            System.out.println("Serialized PatternResult-> " + serResult);
            is.close();

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public void testPatternRepositorySerialization() {
        PatternRepository result = engine.getPatternRepository();

        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("PatRep.ser"));

            os.writeObject(result);
            os.close();

            PatternResult serResult;

            ObjectInputStream is = new ObjectInputStream(new FileInputStream("PDM.ser"));
            serResult = (PatternResult)is.readObject();

            System.out.println("Serialized PatternRepository-> " + serResult);
            is.close();

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }



/*
    public void testDummy() {
        PatternResult pdmResult = engine.execute("FC_calls_SF");
        log.info(pdmResult.toString());

        RoleResult roleResult = (RoleResult)pdmResult.getRoles().iterator().next();

        RoleType roleType = roleResult.getRole();

        JAXBContext ctx;
        try {

            ctx = JAXBContext.newInstance("com.sun.salsa.pdm");
            Marshaller m = ctx.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.marshal(pdmResult.getPdm().getRoles(), System.out);
        } catch (JAXBException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }
*/

    public void testIntralPDMRelationshipDiscovery() {
//        PatternResult result = engine.execute("test3");
//        log.info(result.toString());

        List relationships = engine.discoverRoleRelationships("test3", new String[]{"Calls"});
        for (int i = 0; i < relationships.size(); i++) {
            Object o = relationships.get(i);
            System.out.println("o = " + (RelationshipResult)o);
        }

        relationships = engine.discoverRoleRelationships("test2", new String[]{"Calls"});
        for (int i = 0; i < relationships.size(); i++) {
            Object o = relationships.get(i);
            System.out.println("o = " + (RelationshipResult)o);
        }


    }


    public void testZTest1() {
        PatternResult result = engine.execute("ZTest");
        System.out.println(result);

        List relationships = engine.discoverRoleRelationships("ZTest", new String[]{"Calls"});
        for (int i = 0; i < relationships.size(); i++) {
            Object o = relationships.get(i);
            System.out.println("o = " + (RelationshipResult)o);
        }

    }

    public void testZTest2() {
        PatternResult result = engine.execute("BD_Calls_SF");
        System.out.println(result);

        result = engine.execute("BD_Calls_SF");
        System.out.println(result);

        result = engine.execute("BD_Calls_SF");
        System.out.println(result);

    }

    public void testBug() {
        System.out.println(engine.execute("SessionFacade"));
        System.out.println(engine.execute("BusinessDelegate"));
        System.out.println(engine.execute("SF_Calls_DAO"));

    }

    public void testCacheForProxy() {
        long l = System.currentTimeMillis();
        PatternResult pr = engine.execute("SessionFacade");
        System.out.println(pr);

        System.out.println("1st execution " + (System.currentTimeMillis() - l));
        l = System.currentTimeMillis();

        pr = engine.execute("SessionFacade");
        System.out.println(pr);

        System.out.println("2nd execution " + (System.currentTimeMillis() - l));
        l = System.currentTimeMillis();

        pr = engine.execute("SessionFacade");

        System.out.println(pr);

        System.out.println("3rd execution " + (System.currentTimeMillis() - l));
    }


    public void testSingularPatternCompliance() {
        PatternResult pr = engine.execute("SessionFacade");

        System.out.println(pr);

        SearchContext ctx = new SearchContext();
        ctx.setSearchType(SearchContext.COARSE);
        pr = engine.execute("SessionFacade", null, ctx)[0];

        System.out.println(pr);

    }

    public void testCompositePatternCompliance() {
        PatternResult pr  = engine.execute("BD_Calls_SF");

        System.out.println(pr);

        pr  = engine.execute("BD_Calls_SF");

        System.out.println(pr);

        pr  = engine.execute("BD_Calls_SF");

        System.out.println(pr);

    }


    public void testCompositePatternCompliance2() {
        System.out.println(engine.execute("SF_Calls_DAO"));
    }

    public void testSessionFacade() {
        System.out.println(engine.execute("SessionFacade"));
        System.out.println(engine.execute("SessionFacade"));

    }


    public void testRoleReference() {
        PatternResult result = engine.execute("FC_Calls_SF");

        RoleResult fc = result.getRoleReference("FrontController");
        System.out.println(fc);

        RoleResult bd = result.getRoleReference("BusinessDelegate");
        System.out.println(bd);

        RoleResult sf = result.getRoleReference("SessionFacadeBean");
        System.out.println(sf);

    }


    public void testPatternReferenceInRelationship() {

        PatternResult result = engine.execute("ServiceLocatorDependency");

        System.out.println(result);
    }



    public void testMacroSupport() throws PQLException {
        String query =  "include allMacros; \n " +
                        "return select c.$calledClasses.name from classes c;" ;

        Map result = pqlConnection.executePQL(query);

        System.out.println(result);

    }

}
