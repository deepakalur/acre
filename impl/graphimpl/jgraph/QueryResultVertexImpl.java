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
package org.acre.visualizer.graphimpl.jgraph;

import org.acre.visualizer.graph.graph.pdmresult.QueryResultVertex;
import org.acre.visualizer.graph.graph.vertex.AcreVertexData;
import org.acre.pdmengine.model.PatternResult;
import org.acre.pdmengine.model.QueryResult;

/**
 * @author Deepak.Alur@Sun.com
 * @version Jan 25, 2005 4:27:06 PM
 */
public class QueryResultVertexImpl
        extends AcreVertexImpl
        implements QueryResultVertex {
    PatternResult patternResult;
    QueryResult queryResult;

    public QueryResultVertexImpl(PatternResult patternResult, QueryResult queryResult) {
        super(null);
        this.patternResult = patternResult;
        this.queryResult = queryResult;
        AcreVertexData data = new AcreVertexData(queryResult.getName(), queryResult);
        assignAttributes(data);
        setSalsaData(data);
    }

    public PatternResult getPDMResult() {
        return patternResult;
    }

    public QueryResult getQueryResult() {
        return queryResult;
    }

    // this method to be implemented by all inheriters
    // this method is invoked by setSalsaData
    protected void assignAttributes(AcreVertexData data) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getVertexName() {
        return queryResult.getName();
    }

    public String getToolTipText() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append(queryResult.toString());

//        sbuf.append(queryResult.getArtifacts());

        String str = sbuf.toString();

        if (str.length() > 120) {
            return str.substring(0, 120) + "..." ;
        } else return str;
    }

}
