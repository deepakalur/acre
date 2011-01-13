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
package org.acre.visualizer.grappa;

import att.grappa.Graph;
import att.grappa.GrappaPanel;
import att.grappa.Parser;
import org.acre.common.AcreStringUtil;
import org.acre.config.ConfigService;
import org.acre.pdmengine.model.PatternResult;

import java.io.*;

/**
 * Common Utilities class for PDM Grappa Visualizer
 *
 * @author Yury Kamen
 */
public class GrappaUtilities {
    public static String getPDMDotImage(PatternResult patternResult) {
        if (null == patternResult) {
            throw new IllegalArgumentException("patternResult == null");
        }
        try {
            PDMDotWriter dotWriter = new PDMDotWriter();
            dotWriter.createDotPDM(patternResult);
            String s = dotWriter.getDotSource();
            //System.out.println("==================== No-coords DOT source file");
            //System.out.println(s);
            //System.out.println("==================== No-coords DOT source file");

            String dotexecutable = ConfigService.getInstance().getConfigData().getGrappaDOTExecutablePath();

            if ((dotexecutable==null)
                || (AcreStringUtil.isEmpty(dotexecutable))
                || (!new File(dotexecutable).exists())) {

                throw new IllegalArgumentException("DOT executable file is invalid or not found: dotexecutable=" + dotexecutable);
            }
            Process p = Runtime.getRuntime().exec(dotexecutable);
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            Pipe.between(new StringBufferInputStream(s), p.getOutputStream());
            Pipe.between(p.getInputStream(), bo).join();
            String res = bo.toString();
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static Graph buildGraph(String input) {
        return buildGraph(new StringBufferInputStream(input));
    }

    public static Graph buildGraph(InputStream input) {
        Parser program = new Parser(input, System.err);
        try {
            //program.debug_parse(4);
            program.parse();
        } catch (Exception ex) {
            System.err.println("Exception: " + ex.getMessage());
            ex.printStackTrace(System.err);
            System.exit(1);
        }
        Graph graph = null;

        graph = program.getGraph();

//        System.err.println("The graph contains " + graph.countOfElements(Grappa.NODE | Grappa.EDGE | Grappa.SUBGRAPH) + " elements.");

        graph.setEditable(true);
        graph.setMenuable(true);
        graph.setErrorWriter(new PrintWriter(System.err, true));
        //graph.printGraph(new PrintWriter(System.out));

        //System.err.println("bbox=" + graph.getBoundingBox().getBounds().toString());
        return graph;


    }

    public static GrappaPanel createGrappaPanel(Graph graph) {
        GrappaPanel gp = new GrappaPanel(graph);
        gp.setScaleToFit(true); // used to be false
        java.awt.Rectangle bbox = graph.getBoundingBox().getBounds();
        return gp;
    }

    public static GrappaPanel createGrappaPanel(String input) {
        return createGrappaPanel(buildGraph(input));
    }

    public static GrappaPanel createGrappaPanel(InputStream input) {
        return createGrappaPanel(buildGraph(input));
    }

    public static GrappaPanel createGrappaPanel(PatternResult patternResult) {
        return createGrappaPanel(buildGraph(getPDMDotImage(patternResult)));
    }

    public static String getSampleGraph() {
        return "digraph PDMResults {\n" +
                "\tgraph [orientation=portrait, nodesep=\".2\"];\n" +
                "\tnode [label=\"\\N\"];\n" +
                "\tgraph [bb=\"0,0,602,674\"];\n" +
                "\tBD_Calls_SFPDM [label=BD_Calls_SF, color=lightblue, fontsize=14, style=filled, pos=\"265,656\", width=\"1.44\", height=\"0.50\"];\n" +
                "\tBusinessDelegatePDM [label=BusinessDelegate, color=lightblue, fontsize=14, style=filled, pos=\"144,582\", width=\"1.72\", height=\"0.50\"];\n" +
                "\tBusinessDelegateROLE [label=BusinessDelegate, color=pink, fontsize=12, style=filled, shape=hexagon, pos=\"110,508\", width=\"1.78\", height=\"0.50\"];\n" +
                "\tBusinessDelegateArtifacts [label=\"BusinessDelegate ARTIFACTS\\n\\ncom.sun.sjc.psa.delegate.CommitmentDelegate\\ncom.sun.sjc.psa.delegate.CustomerDelegate\\ncom.sun.sj\\\n" +
                "c.psa.delegate.EmployeeDelegate\\ncom.sun.sjc.psa.delegate.ProjectDelegate\\ncom.sun.sjc.psa.delegate.ResourceDelegate\\ncom.sun.sj\\\n" +
                "c.psa.delegate.SearchDelegate\\ncom.sun.sjc.psa.delegate.SkillDelegate\\ncom.sun.sjc.psa.delegate.SystemDelegate\", color=burlywood, fontsize=10, style=filled, shape=box, pos=\"103,374\", width=\"2.86\", height=\"1.78\"];\n" +
                "\tSessionFacadePDM [label=SessionFacade, color=lightblue, fontsize=14, style=filled, pos=\"341,582\", width=\"1.50\", height=\"0.50\"];\n" +
                "\tSessionFacadeHomeROLE [label=SessionFacadeHome, color=pink, fontsize=12, style=filled, shape=hexagon, pos=\"260,508\", width=\"2.00\", height=\"0.50\"];\n" +
                "\tSessionFacadeInterfaceROLE [label=SessionFacadeInterface, color=pink, fontsize=12, style=filled, shape=hexagon, pos=\"300,374\", width=\"2.22\", height=\"0.50\"];\n" +
                "\tSessionFacadeInterfaceArtifacts [label=\"SessionFacadeInterface ARTIFACTS\\n\\ncom.sun.sjc.psa.ejb.CommitmentLocal\\ncom.sun.sjc.psa.ejb.CustomerLocal\\ncom.sun.sjc.psa.ejb.\\\n" +
                "EmployeeLocal\\ncom.sun.sjc.psa.ejb.ProjectLocal\\ncom.sun.sjc.psa.ejb.ResourceLocal\\ncom.sun.sjc.psa.ejb.SkillLocal\\ncom.sun.sjc.\\\n" +
                "psa.ejb.CommitmentFacade\\ncom.sun.sjc.psa.ejb.CustomerFacade\\ncom.sun.sjc.psa.ejb.EmployeeFacade\\ncom.sun.sjc.psa.ejb.ProjectFac\\\n" +
                "ade\\ncom.sun.sjc.psa.ejb.ResourceFacade\\ncom.sun.sjc.psa.ejb.SearchFacade\\ncom.sun.sjc.psa.ejb.SkillFacade\\ncom.sun.sjc.psa.ejb.\\\n" +
                "SystemFacade\\ncom.sun.sjc.psa.ejb.Commitment\\ncom.sun.sjc.psa.ejb.Customer\\ncom.sun.sjc.psa.ejb.Employee\\ncom.sun.sjc.psa.ejb.Pr\\\n" +
                "oject\\ncom.sun.sjc.psa.ejb.Resource\\ncom.sun.sjc.psa.ejb.Skill\", color=burlywood, fontsize=10, style=filled, shape=box, pos=\"300,136\", width=\"2.44\", height=\"3.78\"];\n" +
                "\tSessionFacadeBeanROLE [label=SessionFacadeBean, color=pink, fontsize=12, style=filled, shape=hexagon, pos=\"445,508\", width=\"1.92\", height=\"0.50\"];\n" +
                "\tSessionFacadeBeanArtifacts [label=\"SessionFacadeBean ARTIFACTS\\n\\ncom.sun.sjc.psa.ejb.CommitmentFacadeSession\\ncom.sun.sjc.psa.ejb.CustomerFacadeSession\\ncom.sun.s\\\n" +
                "jc.psa.ejb.EmployeeFacadeSession\\ncom.sun.sjc.psa.ejb.ProjectFacadeSession\\ncom.sun.sjc.psa.ejb.ResourceFacadeSession\\ncom.sun.s\\\n" +
                "jc.psa.ejb.SearchFacadeSession\\ncom.sun.sjc.psa.ejb.SkillFacadeSession\\ncom.sun.sjc.psa.ejb.SystemFacadeSession\", color=burlywood, fontsize=10, style=filled, shape=box, pos=\"498,374\", width=\"2.89\", height=\"1.78\"];\n" +
                "\tBD_Calls_SFPDM -> BusinessDelegatePDM [pos=\"e,170,598 239,640 222,630 198,615 179,603\"];\n" +
                "\tBusinessDelegatePDM -> BusinessDelegateROLE [pos=\"e,119,526 136,564 132,555 127,545 123,535\"];\n" +
                "\tBusinessDelegateROLE -> BusinessDelegateArtifacts [style=dotted, color=red, pos=\"e,106,438 109,490 108,479 107,464 107,448\"];\n" +
                "\tBD_Calls_SFPDM -> SessionFacadePDM [pos=\"e,323,599 283,639 293,629 305,617 316,606\"];\n" +
                "\tSessionFacadePDM -> SessionFacadeHomeROLE [pos=\"e,280,526 322,565 311,555 299,544 288,533\"];\n" +
                "\tSessionFacadePDM -> SessionFacadeInterfaceROLE [pos=\"e,308,392 342,564 344,545 344,515 339,490 334,459 322,425 312,401\"];\n" +
                "\tSessionFacadeInterfaceROLE -> SessionFacadeInterfaceArtifacts [style=dotted, color=red, pos=\"e,300,272 300,356 300,339 300,311 300,282\"];\n" +
                "\tSessionFacadePDM -> SessionFacadeBeanROLE [pos=\"e,420,526 364,566 378,556 396,543 412,532\"];\n" +
                "\tSessionFacadeBeanROLE -> SessionFacadeBeanArtifacts [style=dotted, color=red, pos=\"e,485,438 461,490 465,484 469,478 472,472 476,464 479,456 482,448\"];\n" +
                "\tSessionFacadeHomeROLE -> SessionFacadeInterfaceROLE [color=blue, labelfontcolor=blue, label=Creates, pos=\"e,294,392 265,490 272,467 284,429 291,402\", lp=\"297,464\"];\n" +
                "\tSessionFacadeBeanROLE -> SessionFacadeInterfaceROLE [color=blue, labelfontcolor=blue, label=Implements, pos=\"e,319,392 424,490 417,484 410,478 403,472 377,447 347,419 326,399\", lp=\"443,464\"];\n" +
                "\tBusinessDelegateROLE -> SessionFacadeInterfaceROLE [color=blue, labelfontcolor=blue, label=Calls, pos=\"e,276,392 137,490 158,476 188,456 213,438 232,425 252,410 268,398\", lp=\"202,464\"];\n" +
                "}" ;
    }
}
