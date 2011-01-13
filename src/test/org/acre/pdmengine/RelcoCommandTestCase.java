package org.acre.pdmengine;

import org.acre.dao.PDMXMLConstants;
import org.acre.lang.pql.pdbc.PQLException;
import org.acre.pdmengine.model.ArtifactLink;
import org.acre.pdmengine.model.Artifact;
import org.acre.pdmengine.model.impl.ArtifactLinksBuilder;
import org.acre.pdmengine.model.impl.ArtifactsHolder;
import org.acre.pdmengine.pqe.PQLVariable;
import org.acre.pdmengine.pqe.PatternBaseCommand;
import org.acre.pdmengine.pqe.PatternCommandFactory;
import org.acre.pdmengine.pqe.RelcoCommand;
import org.acre.pdmengine.util.BidiLinksMap;

import java.util.Map;

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
 * @version Dec 8, 2004 2:54:59 PM
 */
public class RelcoCommandTestCase extends PatternEngineBaseTestCase {
    public void testCreatesCommand() {
        PatternBaseCommand createsCommand = PatternCommandFactory.getInstance().
                getPDMRelationalOperator(PDMXMLConstants.RELATIONSHIP_TYPE_CREATES);

        try {
            pqlConnection.executePQL("define servlets as select c from classes c where c.shortName=\"PSAServlet\"; return servlets; ");

            PQLVariable var1 = new PQLVariable("servlets", PQLVariable.TYPE_CLASS);
            PQLVariable var2 = new PQLVariable("classes", PQLVariable.TYPE_CLASS);

            PQLVariable result = createsCommand.execute(pqlEngineFacade, "result", var1, var2, false);
            System.out.println(result.getPqlResultMap());

            ArtifactsHolder artifactsHolder = new ArtifactsHolder(result.getPqlResultMap(), null);
            Artifact artifacts[] = artifactsHolder.getArtifacts(1);
            assertEquals(artifacts[0].getName(), "com.sun.sjc.psa.core.PSAException");
            assertEquals(artifacts[1].getName(), "com.sun.sjc.psa.request.PsaServletRequestWrapper");
            assertEquals(artifacts[2].getName(), "com.sun.sjc.psa.request.SJCFilterManager");
            assertEquals(artifacts[3].getName(), "com.sun.sjc.psa.request.ValidationSet");
            assertEquals(artifacts[4].getName(), "com.sun.sjc.psa.request.ViewMapManager");
            assertEquals(artifacts[5].getName(), "javax.servlet.ServletException");

        } catch (PQLException e) {
            e.printStackTrace();  // Handle Exception
        }

    }

    public void testAbscoOperator() {
        PatternBaseCommand abscoOp = PatternCommandFactory.getInstance().
                getPDMRelationalOperator(PDMXMLConstants.RELATIONSHIP_TYPE_ABSCO);

        try {
            pqlConnection.executePQL("define nonStaticClasses as select c from classes c where c.isStatic = false; return nonStaticClasses; ");

            PQLVariable op1 = new PQLVariable("classes", PQLVariable.TYPE_CLASS);
            PQLVariable op2 = new PQLVariable("nonStaticClasses", PQLVariable.TYPE_CLASS);

            PQLVariable result = abscoOp.execute(pqlEngineFacade, "result", op1, op2, false);
            System.out.println(result.getPqlResultMap());
            ArtifactsHolder artifactsHolder = new ArtifactsHolder(result.getPqlResultMap(), null);
            String staticClasses[] = artifactsHolder.getArtifactNames("result");
            assertEquals(staticClasses[0], "com.sun.salsa.jsp.annotation.Dannotation");
            assertEquals(staticClasses[1], "com.sun.salsa.jsp.annotation.JSPAnnotation");
            assertEquals(staticClasses[2], "com.sun.salsa.jsp.annotation.ScriptletAnnotation");
            assertEquals(staticClasses[3], "com.sun.sjc.psa.core.TypeCodes.NoSuchTypeException");
            assertEquals(staticClasses[4], "javax.mail.Message.RecipientType");


        } catch (PQLException e) {
            e.printStackTrace();  // Handle Exception
        }

    }


    public void testRelcoOperator() throws PQLException {
        engine.execute("BD_Calls_SF");


        RelcoCommand relcoOp = new RelcoCommand();

        Map relcos = relcoOp.execute(pqlEngineFacade, "result", new String[]{"BusinessDelegates"},
                "SFInterfaces", "Calls");

        System.out.println(relcos);

        BidiLinksMap bidiLinksMap = ArtifactLinksBuilder.buildLinksMap(relcos);
        Map linksMap = bidiLinksMap.getLinksMap();
        ArtifactLink link[] = new ArtifactLink[linksMap.size()];
        linksMap.values().toArray(link);

        RelationshipValidator relationshipValidator = new RelationshipValidator(PSATestFacts.RelcoOfBDCallsSF);
        relationshipValidator.validateLinks(link);
    }


     public void testCreatesRelco() throws PQLException {
        engine.execute("FrontController");

        pqlConnection.executePQL("define psaException as " +
                "select c from classes c " +
                "where c.shortName in (\"PSAException\");" +
                " return psaException");


        RelcoCommand relcoOp = new RelcoCommand();

        Map relcos = relcoOp.execute(pqlEngineFacade, "result",
                    new String[]{"frontControllers"},
                    "psaException",
                    "Creates");

        System.out.println(relcos);
        BidiLinksMap bidiLinksMap = ArtifactLinksBuilder.buildLinksMap(relcos);
        Map linksMap = bidiLinksMap.getLinksMap();
        ArtifactLink link[] = new ArtifactLink[linksMap.size()];
        linksMap.values().toArray(link);

        RelationshipValidator relationshipValidator = new RelationshipValidator(PSATestFacts.RelcoOfFCCreatesPSAException);
        relationshipValidator.validateLinks(link);


    }

    public void testImplementsRelco() throws PQLException {
       engine.execute("SessionFacade");

       pqlConnection.executePQL("define nullClasses as " +
               "select c from classes c " +
               "where c.shortName in (\"Unknown\");" +
               " return nullClasses");


       RelcoCommand relcoOp = new RelcoCommand();

       Map relcos = relcoOp.execute(pqlEngineFacade, "result",
                    new String[]{"nullClasses"},
                    "SFInterfaces",
                    "Implements");

       System.out.println(relcos);

        BidiLinksMap bidiLinksMap = ArtifactLinksBuilder.buildLinksMap(relcos);
        Map linksMap = bidiLinksMap.getLinksMap();
        ArtifactLink link[] = new ArtifactLink[linksMap.size()];
        linksMap.values().toArray(link);

        RelationshipValidator relationshipValidator = new RelationshipValidator(PSATestFacts.RelcoOfNullImplementsSFInterfaces);
        relationshipValidator.validateLinks(link);
   }



    public void testIsOfTypeRelco() throws PQLException {

       pqlConnection.executePQL("define nullClasses as " +
               "select c from classes c " +
               "where c.shortName in (\"Unknown\");" +
               " return nullClasses");

       pqlConnection.executePQL("define empLocal as select c from classes c " +
               "where c.shortName=\"EmployeeLocal\";" +
               "return empLocal");
       RelcoCommand relcoOp = new RelcoCommand();

       Map relcos = relcoOp.execute(pqlEngineFacade, "result",
                    new String[]{"nullClasses"},
                    "empLocal",
                    "IsOfType");

       System.out.println(relcos);

        BidiLinksMap bidiLinksMap = ArtifactLinksBuilder.buildLinksMap(relcos);
        Map linksMap = bidiLinksMap.getLinksMap();
        ArtifactLink link[] = new ArtifactLink[linksMap.size()];
        linksMap.values().toArray(link);
        RelationshipValidator relationshipValidator = new RelationshipValidator(PSATestFacts.RelcoIsOfType);
        relationshipValidator.validateLinks(link);
   }

    public void testUsesRelco() throws PQLException {

       pqlConnection.executePQL("define nullClasses as " +
               "select c from classes c " +
               "where c.shortName in (\"Unknown\");" +
               " return nullClasses");

       pqlConnection.executePQL("define objectPool as select c from classes c " +
               "where c.shortName=\"ObjectPool\";" +
               "return objectPool");
       RelcoCommand relcoOp = new RelcoCommand();

       Map relcos = relcoOp.execute(pqlEngineFacade, "result",
                   new String[]{"nullClasses"},
                    "objectPool",
                   "Uses");

       System.out.println(relcos);

        BidiLinksMap bidiLinksMap = ArtifactLinksBuilder.buildLinksMap(relcos);
        Map linksMap = bidiLinksMap.getLinksMap();
        ArtifactLink link[] = new ArtifactLink[linksMap.size()];
        linksMap.values().toArray(link);
        RelationshipValidator relationshipValidator = new RelationshipValidator(PSATestFacts.RelcoUses);
        relationshipValidator.validateLinks(link);
   }


    public void testExtendsRelco() throws PQLException {

       pqlConnection.executePQL("define nullClasses as " +
               "select c from classes c " +
               "where c.shortName in (\"Unknown\");" +
               " return nullClasses");

       pqlConnection.executePQL("define typecodes as select c from classes c " +
               "where c.shortName=\"TypeCodes\";" +
               "return typecodes");
       RelcoCommand relcoOp = new RelcoCommand();

       Map relcos = relcoOp.execute(pqlEngineFacade, "result",
                   new String[]{"nullClasses"},
                    "typecodes",
                   "Extends");

       System.out.println(relcos);

        BidiLinksMap bidiLinksMap = ArtifactLinksBuilder.buildLinksMap(relcos);
        Map linksMap = bidiLinksMap.getLinksMap();
        ArtifactLink link[] = new ArtifactLink[linksMap.size()];
        linksMap.values().toArray(link);
        RelationshipValidator relationshipValidator = new RelationshipValidator(PSATestFacts.RelcoExtends);
        relationshipValidator.validateLinks(link);
   }

    public void testContainsRelco() throws PQLException {

       pqlConnection.executePQL("define nullClasses as " +
               "select c from classes c " +
               "where c.shortName in (\"Unknown\");" +
               " return nullClasses");

       pqlConnection.executePQL("define skillAR as select c from classes c " +
               "where c.shortName=\"skillAR\";" +
               "return skillAR");
       RelcoCommand relcoOp = new RelcoCommand();

       Map relcos = relcoOp.execute(pqlEngineFacade, "result",
                    new String[]{"nullClasses"},
                    "skillAR",
                    "Contains");

       System.out.println(relcos);

        BidiLinksMap bidiLinksMap = ArtifactLinksBuilder.buildLinksMap(relcos);
        Map linksMap = bidiLinksMap.getLinksMap();
        ArtifactLink link[] = new ArtifactLink[linksMap.size()];
        linksMap.values().toArray(link);
        RelationshipValidator relationshipValidator = new RelationshipValidator(PSATestFacts.RelcoContains);
        relationshipValidator.validateLinks(link);
   }


    public void testThrowsRelco() throws PQLException {

       pqlConnection.executePQL("define nullClasses as " +
               "select c from classes c " +
               "where c.shortName in (\"Unknown\");" +
               " return nullClasses");

       pqlConnection.executePQL("define AuthorizationException as select c from classes c " +
               "where c.shortName=\"AuthorizationException\";" +
               "return AuthorizationException");
       RelcoCommand relcoOp = new RelcoCommand();

       Map relcos = relcoOp.execute(pqlEngineFacade, "result",
                    new String[]{"nullClasses"},
                    "AuthorizationException",
                    "Throws");

       System.out.println(relcos);

        BidiLinksMap bidiLinksMap = ArtifactLinksBuilder.buildLinksMap(relcos);
        Map linksMap = bidiLinksMap.getLinksMap();
        ArtifactLink link[] = new ArtifactLink[linksMap.size()];
        linksMap.values().toArray(link);
        RelationshipValidator relationshipValidator = new RelationshipValidator(PSATestFacts.RelcoThrows);
        relationshipValidator.validateLinks(link);
   }



    public void testCatchesRelco() throws PQLException {

       pqlConnection.executePQL("define nullClasses as " +
               "select c from classes c " +
               "where c.shortName in (\"Unknown\");" +
               " return nullClasses");

       pqlConnection.executePQL("define AuthorizationException as select c from classes c " +
               "where c.shortName=\"AuthorizationException\";" +
               "return AuthorizationException");
       RelcoCommand relcoOp = new RelcoCommand();

       Map relcos = relcoOp.execute(pqlEngineFacade, "result",
                    new String[]{"nullClasses"},
                    "AuthorizationException",
                    "Catches");

       System.out.println(relcos);

        BidiLinksMap bidiLinksMap = ArtifactLinksBuilder.buildLinksMap(relcos);
        Map linksMap = bidiLinksMap.getLinksMap();
        ArtifactLink link[] = new ArtifactLink[linksMap.size()];
        linksMap.values().toArray(link);
        RelationshipValidator relationshipValidator = new RelationshipValidator(PSATestFacts.RelcoCatches);
        relationshipValidator.validateLinks(link);
   }


}
