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
package org.acre.pdmengine.model.impl;

import org.acre.lang.pql.pdbc.PQLArtifact;
import org.acre.lang.pql.pdbc.PQLResultSet;
import org.acre.lang.pql.pdbc.PQLValueHolder;
import org.acre.pdmengine.model.Artifact;
import org.acre.pdmengine.model.RoleResult;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Deepak.Alur@Sun.com
 *         Date: Oct 7, 2004
 *         Time: 1:10:11 PM
 *
 * This is a wrapper class that holds the collection of resulting artifacts obtained
 * from the query execution
 */

public class ArtifactsHolder extends AbstractArtifactsHolder implements java.io.Serializable {
    static final long serialVersionUID = -2381504720456276284L;

    private Map result; // this is the result obtained from PQL Execution. Map contains 1 or more PQLResultSet instances

    PQLArtifact [][] artifacts;

    public ArtifactsHolder(Map result) {
        this(result, null);
    }

    public ArtifactsHolder(Map result, RoleResult parent) {
        this.result = result;
        this.parent = parent;
    }


    private Map getResult() {
        return result;
    }


    public String[] getVariableKeys() {
        String [] variableKeys = new String[getResult().size()];
        getResult().keySet().toArray(variableKeys);
        return variableKeys;
    }

    public void setResult(Map result) {
        this.result = result;
    }


    public String [] getArtifactNames(Object key) {
        String [] n = null;

        Object r = result.get(key);
        if (r instanceof PQLResultSet) {
            PQLResultSet rpql = (PQLResultSet) r;
            n = new String[rpql.getMetaData().getRowCount()];
            for (int row=0; row < rpql.getMetaData().getRowCount(); row++) {
                PQLArtifact[] rowData = rpql.getRow(row);
                String s = "";
                for(int i = 0; i < rowData.length; i++) {
                    if(s.length() != 0) s += ", ";
                    if(rowData[i].getValue() instanceof PQLValueHolder) {
                        PQLValueHolder holder = ((PQLValueHolder)rowData[i].getValue());
                        s += holder.getName();
                    }  else {
                    s += rowData[i].getValue();
                    }
                }
                n[row]=s;
            }
        } else {
            n = new String[1];
            n[0]=r.toString();
        }

        return n;
    }

    public Artifact[] getArtifacts(int columnNumber) {
        Artifact [] colValues = null;

        for (Iterator iter = result.entrySet().iterator(); iter.hasNext();) {
            Map.Entry element = (Map.Entry) iter.next();
            String rsName = (String) element.getKey();
            PQLResultSet prs = (PQLResultSet) element.getValue();
            colValues = new Artifact[prs.getMetaData().getRowCount()];

            for (int i = 0; i < prs.getMetaData().getRowCount(); i++) {
                //for (int j = 0; j < prs.getColumnCount(); j++) {
                //Notice a cast is required
                // todo --- but will this cast work always, thought this was not always PQLData
                Object data = prs.getValue(i, columnNumber);
                if (data instanceof PQLValueHolder) {
                    PQLValueHolder pqldata = (PQLValueHolder) prs.getValue(i, columnNumber).getValue();
                    // colValues[i]= new ArtifactImpl(pqldata); TODO - FIXME

                    //fetch all the attributes and relationship for the PQLData
                    //pql.fetch(data);
                    //Print PQLData fetch with all the attributes and relationship
                    //System.out.println(data.toString(true, true));
                //}
                } else {
                    colValues[i]=new ArtifactImpl((PQLArtifact)data, parent);
                }
            }
        }

        return colValues;
    }

    // It is a linear search - base on profiling & usage, we may re-implement
    // using a indexed search
    public Artifact findArtifact(int columnNumber, Object target, Comparator artifactComparator) {
        Artifact artifact = null;

        for (Iterator iter = result.entrySet().iterator(); iter.hasNext();) {
            Map.Entry element = (Map.Entry) iter.next();
            String rsName = (String) element.getKey();
            PQLResultSet prs = (PQLResultSet) element.getValue();

            for (int i = 0; i < prs.getMetaData().getRowCount(); i++) {

                Object data = prs.getValue(i, columnNumber);
                if (data instanceof PQLArtifact) {
                    if ( artifactComparator.compare(target, data) == 0 ) {
                        artifact = new ArtifactImpl((PQLArtifact)data, parent);
                        break;
                    }
                }
            }
        }

        return artifact;
    }



    public List matchArtifacts(int columnNumber, Pattern pattern) {
        List artifacts = new ArrayList();

        for (Iterator iter = result.entrySet().iterator(); iter.hasNext();) {
            Map.Entry element = (Map.Entry) iter.next();
            String rsName = (String) element.getKey();
            PQLResultSet prs = (PQLResultSet) element.getValue();

            for (int i = 0; i < prs.getMetaData().getRowCount(); i++) {

                Object data = prs.getValue(i, columnNumber);
                if (data instanceof PQLArtifact) {
                    PQLArtifact pqlArtifact = (PQLArtifact)data;
                    if ( !pqlArtifact.isPrimitive() ) {
                        PQLValueHolder vlh = (PQLValueHolder)pqlArtifact.getValue();
                        Matcher matcher = pattern.matcher(vlh.getName());
                        if ( matcher.matches() ) {
                            Artifact artifact;
                            artifact = new ArtifactImpl((PQLArtifact)data, parent);
                            artifacts.add(artifact);
                        }
                    }
                }
            }
        }
        return artifacts;
    }

    public String getType() {
        String type;

        if ( isEmpty() )
            return "";

        Object rsObj = result.values().iterator().next();
        if ( rsObj instanceof PQLResultSet ) {
            PQLResultSet rs = (PQLResultSet)rsObj;
            type = rs.getMetaData().getType();
        }
        else {
            type = "string";
        }
        return type;
    }

    protected boolean isEmpty() {
        return result.values().size() == 0;
    }

    public String toString() {
        return "ResultArtifacts{" +
                "result=" + result +
                "}";
    }
}
