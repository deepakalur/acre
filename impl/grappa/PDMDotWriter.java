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

import org.acre.pdm.PDMType;
import org.acre.pdm.Relationship;
import org.acre.config.ConfigService;
import org.acre.pdmengine.model.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Logger;


/**
 * Created by IntelliJ IDEA.
 * User: johncrupi
 * Date: Aug 18, 2004
 * Time: 7:40:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class PDMDotWriter {
    final boolean RANK_LR = false;

    //DotWriterAdapter dotPDMWriter;
    GraphViz dotPDMWriter;
    FileWriter dotArtifactsWriter;
    private Logger logger;

    public PDMDotWriter() throws IOException {
        dotPDMWriter = new GraphViz();
        if (ConfigService.getInstance() !=null) {
            logger = ConfigService.getInstance().getLogger(this);
        } else {
            logger = Logger.getLogger(this.getClass().getPackage().getName());
        }
    }

    public void createDotPDM(PatternResult patternResult) throws IOException {
        try {
            logger.info("*** Writing : " + dotPDMWriter);
            writePDMHeader();
            writePDM(patternResult);
            writePDMFooter();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public int hashCode() {
        return super.hashCode();    //To change body of overridden methods use File | Settings | File Templates.
    }


    void writePDMBlock(String pdmName, String label) {
        dotPDMWriter.add("\n\t\"" + pdmName + "\" [label=\"" + label + "\" color=darkgoldenrod1, fontsize=14, style=filled]; /*PDMBlock*/");
    }

    void writePDM(PatternResult patternResult) {

        RoleResult[] roles = null;
        if (patternResult.getRoles() != null) {
            roles = new RoleResult[patternResult.getRoles().size()];
            patternResult.getRoles().toArray(roles);
            String pdmName = patternResult.getPdm().getName();
            writePDMBlock(getPDMName(pdmName), pdmName);
            
            for (int roleNum = 0; roleNum < roles.length; roleNum++) {
                Result roleResult = roles[roleNum].getRoleResult();
                if (roleResult instanceof PatternResult) {
                    PatternResult rolePatternResult = (PatternResult) roleResult;
                    String rolePDMName = rolePatternResult.getPdm().getName();
                    writePDMBlock(getPDMName(rolePDMName), rolePDMName);
                    writePDMToRoleRelationship(getPDMName(pdmName), getPDMName(rolePDMName));
                    PDMType rolePDM = ((PatternResult) rolePatternResult).getPdm();
                    writePDM((PatternResult) rolePatternResult);
                } else if (roleResult instanceof QueryResult) {
                    QueryResult queryResult = (QueryResult) roleResult;

                    String[] keys = queryResult.getVariableKeys();
                    String roleName = roles[roleNum].getRole().getName();

                    writeRoleBlock(getRoleName(roleName), roleName);
                    writePDMToRoleRelationship(getPDMName(patternResult.getPdm().getName()), getRoleName(roleName));

                    for (int keynum = 0; keynum < keys.length; keynum++) {
                        String keyName = keys[keynum].toString();
                        String[] values = queryResult.getArtifactNames(keys[keynum]);
                        try {
                            writeArtifacts(roleName, values);
                        } catch (IOException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }

                        for (int valNum = 0; valNum < values.length; valNum++) {
                            //logger.info( "**** " + roleName + " " + keyName + "  " +  values[valNum].getClass().getName() );
                            logger.info("Artifact->>>  " + (String) values[valNum]);
                        }
                    }
                }
            }
        }

        if (patternResult.getRelationships() != null) {
            for (Iterator iterator = patternResult.getRelationships().iterator(); iterator.hasNext();) {
                Object o = iterator.next();

                if(o instanceof Relationship) {
                Relationship relationship = (Relationship)o ;
                writeRoleToRoleRelationship(relationship.getName(), getRoleName(relationship.getFromRole()),
                        getRoleName(relationship.getToRole()));
                } else {
                RelationshipResult relres = (RelationshipResult)o ;
                writeRoleToRoleRelationship(relres.getName(), getRoleName(relres.getFromRole().getRole().getName()),
                        getRoleName(relres.getToRole().getRole().getName()));
                }
            }
        }
    }

    private void writeRoleBlock(String roleName, String label) {
        dotPDMWriter.add("\n\t\"" + roleName + "\" [shape=hexagon, fontsize=12, color=lightgoldenrod1, style=filled, label=" +
                         "\"" + label + "\"");
        dotPDMWriter.add("]; /* RoleBlock */");
        //dotPDMWriter.add(role.getName() + "(" + role.getNumArtifacts() + ") " );
    }

    /*
    private void writeArtifactsRoleRelationship(Role fromRole, Relationship relationship) {
        String artifactsBoxName = getArtifactsName(fromRole);
        String toArtifactsBoxName = getArtifactsName(relationship.getToRole());
        try {
            dotPDMWriter.add("\n\t\"" + artifactsBoxName + "\" -> \"" + toArtifactsBoxName + "\" [label=" +
                    relationship.getRelationshipType() + " color=black]; ");
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
    */

    private String getRoleName(String role) {
        return role + " (R)";
    }

    private String getPDMName(String pdm) {
        return pdm + " (P)";
    }

    private void writePDMToRoleRelationship(String pdmName, String roleName) {
        logger.info("Writing PDMToRole Relationship...");
        dotPDMWriter.add("\n\t\"" + pdmName + "\" -> \"" + roleName + "\"");
    }

    private void writeRoleToRoleRelationship(String relationshipName, String sourceRoleName, String targetRoleName) {
        logger.info("Writing RoleToRole Relationship...");
        dotPDMWriter.add("\n\t\"" + sourceRoleName + "\" -> \"" + targetRoleName + "\""
           + " [color=blue, labelfontcolor=blue, label=\"" + relationshipName + "\"];"
        );
    }

    private void writeArtifacts(String roleName, String[] artifactNames) throws IOException {
        String artifactsBoxName = getArtifactsName(roleName);
        String roleBoxName = getRoleName(roleName);
        for (int i = 0; i < artifactNames.length; i++) {
            String relationship = roleBoxName + "\" -> \"" + artifactsBoxName + "\" [style=dotted, color=red];";

            if (i == 0) {
                dotPDMWriter.add("\n\t\"" + relationship);
                dotPDMWriter.add("\n\t\"" + artifactsBoxName +
                                 "\" [shape=box, style=filled, fontsize=10, color=darkolivegreen1, label=\"" + roleName + " ARTIFACTS\\n\\n");
            }
            logger.info("ArtifactName : " + artifactNames[i]);
            dotPDMWriter.add((String) artifactNames[i]);

            if (i == artifactNames.length - 1) {
                dotPDMWriter.add("\"];");
            } else {
                dotPDMWriter.add("\\n");
            }
        }
    }

    public String getArtifactsName(String roleName) {
        String name = roleName + " (A)";
        return name;
    }


    private void writePDMHeader() throws IOException {
        dotPDMWriter.add("digraph PDMResults { \n" +
                         "\torientation=portrait;\n" +
                         "\tnodesep=\".2\";");
    }

    private void writeArtifactsHeader() throws IOException {
        dotArtifactsWriter.write("digraph PDMResults { \n" +
                                 "\trankdir=LR;\n" +
                                 "\torientation=portrait;\n" +
                                 "\tratio=compress;\n" +
                                 "\tsize=\"16,10\";");
    }

    void writePDMFooter() throws IOException {
        dotPDMWriter.add("\n} \n");
    }

    void writeArtifactsFooter() throws IOException {
        dotArtifactsWriter.write("\n} \n");
    }

    public String getDotSource() {
        return dotPDMWriter.getDotSource();
    }

    public byte[] getDotImage() {
        return dotPDMWriter.getGraph(dotPDMWriter.getDotSource());
    }

    public void writeDotFile(byte[] dotImage, String fileName) {
        dotPDMWriter.writeGraphToFile(dotImage, fileName);
    }
}


