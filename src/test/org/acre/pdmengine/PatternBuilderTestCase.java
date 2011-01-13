package org.acre.pdmengine;

import org.acre.pdmengine.model.PatternResult;
import org.acre.pdmengine.model.RoleResult;
import org.acre.pdmengine.model.QueryResult;
import org.acre.pdmengine.model.Artifact;
import org.acre.pdmengine.core.PatternBuilderImpl;
import org.acre.pdm.RoleType;
import org.acre.pdm.PDMType;
import org.acre.analytics.AcreContainer;

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
public class PatternBuilderTestCase extends PatternEngineBaseTestCase {

    public void testSimplePatternComposition() {
        AcreContainer container = new AcreContainer();

        PatternEngine patternEngine = container.getPatternEngine();
        PatternResult result = patternEngine.execute("FrontController");
        RoleResult roleResult = (RoleResult)result.getRoles().iterator().next();
        QueryResult queryRS = (QueryResult)roleResult.getRoleResult();

        Artifact[] artifacts = queryRS.getArtifacts();

        PatternBuilder patternBuilder = container.getPatternBuilder();
        RoleType roleType = patternBuilder.createRole("ManagementControllers", artifacts);
        patternBuilder.createPattern("ManagementPDM", new RoleType[]{roleType});
        patternBuilder.savePattern();

    }

    public void testSimplePatternComposition2() {
        PatternResult result = engine.execute("FrontController");
        RoleResult roleResult = (RoleResult)result.getRoleReference("FrontController");

        AcreContainer container = new AcreContainer();

        PatternBuilder patternBuilder = container.getPatternBuilder();
        RoleType roleType = patternBuilder.createRole("ManagementControllers", roleResult);
        patternBuilder.createPattern("ManagementPDM", new RoleType[]{roleType});
        patternBuilder.savePattern();

    }

    public void testJ2EEArchStyle() {
        PatternResult result = engine.execute("PackageView");
        RoleResult roleResult1 = result.getRoleReference("com.sun.sjc.psa.core");
        RoleResult roleResult2 = result.getRoleReference("com.sun.sjc.psa.ejb");
        RoleResult roleResult3 = result.getRoleReference("com.sun.sjc.psa.util");

        AcreContainer container = new AcreContainer();
        PatternBuilder patternBuilder = container.getPatternBuilder();

        RoleType roleType1 = patternBuilder.createRole("core", roleResult1);
        RoleType roleType2 = patternBuilder.createRole("ejb", roleResult2);
        RoleType roleType3 = patternBuilder.createRole("util", roleResult3);


        PDMType pdmType = patternBuilder.createPattern("BizTier", new RoleType[]{roleType1, roleType2, roleType3});

        RoleType bizTier = patternBuilder.createRole("BizTierRef", pdmType);

        patternBuilder.createPattern("J2EEArchStyle", new RoleType[]{bizTier});

        patternBuilder.savePattern();

    }

    public void testJ2EEArch() {
        PatternResult result = engine.execute("J2EEArchStyle");
        System.out.println(result.toString());
    }

}
