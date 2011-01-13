package org.acre.pdmengine;

import org.acre.pdmengine.model.PatternResult;
import org.acre.pdmengine.model.ArtifactLink;
import org.acre.pdmengine.model.Artifact;
import org.acre.pdmengine.analyzer.DesignAnalyzerEngine;
import org.acre.pdmengine.analyzer.ArtifactLinksTreeNode;

import java.util.List;
import java.util.Iterator;

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
public class TransitiveLinksTestCase extends PatternEngineBaseTestCase {

    public void testArtifactFinders() {
        PatternResult result;
        result = engine.execute("BD_Calls_SF");

        Artifact artifact;
        String name = "com.sun.sjc.psa.delegate.ProjectDelegate";
        artifact = result.findArtifact(name);

        System.out.println(artifact.getName());
        assertEquals(name, artifact.getName());

        List artifacts = result.matchArtifacts(".*delegate.*");
        assertEquals(8, artifacts.size());
        System.out.println(artifacts);

        artifacts = result.matchArtifacts(".*CustomerDelegate");

        System.out.println(artifacts);
        assertEquals("com.sun.sjc.psa.delegate.CustomerDelegate", ((Artifact)artifacts.get(0)).getName());


    }


    public void testTransitiveLinks() {
        PatternResult result;

        SearchContext search = new SearchContext();
        search.setSearchType(SearchContext.COARSE);
        result = engine.execute("BD_Calls_SF", null, search)[0];

        Artifact artifact;

        artifact = result.findArtifact("com.sun.sjc.psa.delegate.ProjectDelegate");

        System.out.println(artifact.getName());

        DesignAnalyzerEngine analyzerEngine = new DesignAnalyzerEngine();

        ArtifactLinksTreeNode root = analyzerEngine.findTransitiveLinks(result, "calls", artifact);
        printTransitiveLinks(root);

        assertEquals(root.getLink().getSourceArtifact().getName(), "com.sun.sjc.psa.delegate.ProjectDelegate");
        assertEquals(root.getLink().getTargetArtifacts()[0].getName(), "com.sun.sjc.psa.ejb.ProjectFacade");

        assertEquals(0, root.getNextLinkNodes().size());

    }


    public void testTransitiveLinks2() {
        PatternResult result;

        SearchContext search = new SearchContext();
        search.setSearchType(SearchContext.COARSE);
        result = engine.execute("mA_FC_BD_SF_DAO", null, search)[0];

        Artifact artifact;

//        artifact = result.findArtifact("com.sun.sjc.psa.request.PSAServlet");
        artifact = result.findArtifact("org.apache.jsp.WEB_002dINF.FindResource_jsp");

        System.out.println(artifact.getName());

        DesignAnalyzerEngine analyzerEngine = new DesignAnalyzerEngine();

        ArtifactLinksTreeNode root = analyzerEngine.findTransitiveLinks(result, "calls", artifact);

        printTransitiveLinks(root);

        ArtifactLink link;
        link = root.getLink();
        assertEquals("org.apache.jsp.WEB_002dINF.FindResource_jsp", link.getSourceArtifact().getName());
        assertEquals("com.sun.sjc.psa.delegate.SearchDelegate", link.getTargetArtifacts()[0].getName());

        assertEquals(1, root.getNextLinkNodes().size());
        ArtifactLinksTreeNode node = (ArtifactLinksTreeNode)root.getNextLinkNodes().get(0);
        link = node.getLink();

        assertEquals("com.sun.sjc.psa.delegate.SearchDelegate", link.getSourceArtifact().getName());
        assertEquals("com.sun.sjc.psa.ejb.SearchFacade", link.getTargetArtifacts()[0].getName());

    }


    void printTransitiveLinks(ArtifactLinksTreeNode node) {
        if ( node == null )
            return;

        ArtifactLink link = node.getLink();

        // print links
        printLinks(new ArtifactLink[]{link});

        // print chained links
        Iterator nextLinks = node.getNextLinkNodes().iterator();

        while ( nextLinks.hasNext()) {
            printTransitiveLinks((ArtifactLinksTreeNode)nextLinks.next());
        }

    }

}
