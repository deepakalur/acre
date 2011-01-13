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

import org.acre.pdmqueries.QueryType;
import org.acre.lang.pql.pdbc.PQLArtifact;
import org.acre.lang.pql.pdbc.PQLValueHolder;
import org.acre.pdmengine.model.Artifact;
import org.acre.pdmengine.model.PatternResult;
import org.acre.pdmengine.model.QueryResult;
import org.acre.pdmengine.model.RoleResult;
import org.acre.pdmengine.model.visitor.ResultVisitor;

import java.util.*;
import java.util.regex.Pattern;

public class QueryResultImpl extends ResultImpl implements QueryResult, java.io.Serializable {
    private QueryType query;
    private String queryStatement;
    private AbstractArtifactsHolder currentArtifactsHolder;

    // contains ALL Artifacts that belong to this Pattern Query (including artifacts that do & don't
    // participate in any relationships )
    private ArtifactsHolder resultArtifactsHolder;

    // filtered artifacts - used for storing conformant artifacts
    private FilteredArtifactsHolder filteredArtifactsHolder;

    static final long serialVersionUID = -6338277919665258339L;
    private RoleResult parent;

    public QueryResultImpl(Map result) {
        this(result, null);
    }

    public QueryResultImpl(Map result, RoleResult parent) {
        resultArtifactsHolder = new ArtifactsHolder(result, parent);
        setCurrentArtifactsHolder(resultArtifactsHolder);
        setParent(parent);
    }

    // Copy Constructor
    public QueryResultImpl(QueryResultImpl q, RoleResult parent) {
        setQuery(q.getQuery());
        this.setQueryStatement(q.getQueryStatement());

        resultArtifactsHolder = q.resultArtifactsHolder;

        if ( q.filteredArtifactsHolder == null ) {
            // cloning from coarse artifacts holder
            filteredArtifactsHolder = createFilterFromResult(q.resultArtifactsHolder);
        }
        else {
            // cloning from filtered artifacts holder
            filteredArtifactsHolder = new FilteredArtifactsHolder(q.filteredArtifactsHolder);
        }
        setCurrentArtifactsHolder(filteredArtifactsHolder);
        setParent(parent);
    }


    public String getName() {
        return getQuery().getName();
    }

    public QueryType getQuery() {
        return query;
    }

    public void setQuery(QueryType query) {
        this.query = query;
    }

    public String getQueryStatement() {
        return queryStatement;
    }

    public void setQueryStatement(String queryStatement) {
        this.queryStatement = queryStatement;
    }

    public String[] getVariableKeys() {
        return currentArtifactsHolder.getVariableKeys();
    }

    public String[] getArtifactNames(Object key) {
        return currentArtifactsHolder.getArtifactNames(key);
    }

    public Artifact[] getArtifacts() {
        return currentArtifactsHolder.getArtifacts(0);
    }

    public Artifact[] getArtifacts(int columnNumber) {
        return currentArtifactsHolder.getArtifacts(columnNumber);
    }

    public String getArtifactType() {
        return currentArtifactsHolder.getType();
    }

    public Artifact findArtifact(String artifactName) {
          return resultArtifactsHolder.findArtifact(0, artifactName,
                  new Comparator() {
                      public int compare(Object o1, Object o2) {
                          String val1 = getValue(o1);
                          String val2 = getValue(o2);
                          if ( val1.equalsIgnoreCase(val2) )
                              return 0;
                          return 1;
                      }

                      String getValue(Object o) {
                            if ( o instanceof PQLArtifact) {
                                PQLArtifact p = (PQLArtifact)o;
                                return ((PQLValueHolder)p.getValue()).getName();
                            }
                            return (String)o;
                      }
                  });
    }

    public List matchArtifacts(Pattern pattern) {
        return resultArtifactsHolder.matchArtifacts(0, pattern);
    }

    public Artifact findArtifact(PQLArtifact artifact) {
          return resultArtifactsHolder.findArtifact(0, artifact,
                  new Comparator() {
                      public int compare(Object o1, Object o2) {
                          if ( o1.equals(o2) )
                              return 0;
                          return 1;
                      }
                  });
    }

    public void setFixedArtifacts(Collection artifacts) {
        filteredArtifactsHolder.addArtifacts(artifacts);

        // set artifactHolder to filteredArtifactsHolder
        setCurrentArtifactsHolder(filteredArtifactsHolder);

    }

    private void setCurrentArtifactsHolder(AbstractArtifactsHolder artifactsHolder) {
        currentArtifactsHolder = artifactsHolder;
    }

    public void setParent(RoleResult roleResult) {
        this.parent = roleResult;
        currentArtifactsHolder.setParent(roleResult);
    }

    public RoleResult getParent() {
        return parent;
    }

    public void accept(ResultVisitor visitor) {
        visitor.visitQueryResult(this);
    }

    public int getHitCount() {
        return getArtifacts().length;
    }

    public PatternResult getPattern() {
        return getParent().getParent();
    }

    private FilteredArtifactsHolder createFilterFromResult(ArtifactsHolder artifactsHolder) {

        // initialize FilteredArtifactsHolder to artifact name & type
        String artifactName = artifactsHolder.getVariableKeys()[0];
        String artifactType = artifactsHolder.getType();

        FilteredArtifactsHolder filteredArtifactsHolder =
                new FilteredArtifactsHolder(artifactName, artifactType);

        List l = Arrays.asList(artifactsHolder.getArtifacts(0));
        filteredArtifactsHolder.addArtifacts(l);

        return filteredArtifactsHolder;
    }

    public String toString() {
        return "QueryResult{" +
                "query=" + PDMModelUtil.QueryToString(query) +
                ", queryStatement='" + queryStatement + "'" +
                ", \n artifacts=" + currentArtifactsHolder +
                "}";
    }
}
