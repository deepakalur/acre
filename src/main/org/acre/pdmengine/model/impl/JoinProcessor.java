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

import org.acre.pdmengine.model.Artifact;

import java.util.Arrays;
import java.util.List;

/**
 * @author rajmohan@Sun.com
 *
 * This class is responsible for joining and filtering links 'across relationships' to
 * be in conformance with Pattern definition.
 * This currently cannot be done in SQL due to performance issues as it would
 * require too many multi-table joins. Hence, the "join simulation" is done in-memory.
 */
class JoinProcessor {

    /**
     * The method is responsible for intersecting call links from relationship1 to relationship2
     * For example,
     *  relationship1 { (a1, b1) (a2, b2) (a3, b3) }
     *  relationship2 { (b1, c1) (b2, c2) (b4, c4) }
     *  intersection(relationship1, relationship2) would yield
     *     { (a1, b1) (a2, b2) }
     *     { (b1, c1) (b2, c2) }
     *
     * The intersected(filtered) links are placed in relationship1 & relationship2 respectively.
     * @param rel1
     * @param rel2
     */
    public void joinRelationships(RelationshipResultImpl rel1, RelationshipResultImpl rel2) {

        if ( rel1 == rel2 )
            return;

        if ( rel1.getJoinType() == RelationshipResultImpl.JOIN_TYPE_ANY ||
             rel2.getJoinType() == RelationshipResultImpl.JOIN_TYPE_ANY)
            return;
        
        FilteredLinks rel1Links = rel1.getFilteredLinks();

        FilteredLinks rel2Links = rel2.getFilteredLinks();

        // compute intersecting columns
        if ( rel1.getFromRole().getName().equalsIgnoreCase(rel2.getFromRole().getName())) {
            rel1Links.intersectSources(rel2Links.getSources());
            rel2Links.intersectSources(rel1Links.getSources());
        }
        else if ( rel1.getFromRole().getName().equalsIgnoreCase(rel2.getToRole().getName())) {
            rel1Links.intersectSources(rel2Links.getTargets());
            rel2Links.intersectTargets(rel1Links.getSources());
        }
        else if (rel1.getToRole().getName().equalsIgnoreCase(rel2.getFromRole().getName())) {
            rel1Links.intersectTargets(rel2Links.getSources());
            rel2Links.intersectSources(rel1Links.getTargets());
        }
        else if (rel1.getToRole().getName().equalsIgnoreCase(rel2.getToRole().getName())) {
            rel1Links.intersectTargets(rel2Links.getTargets());
            rel2Links.intersectTargets(rel1Links.getTargets());
        }
    }

    public void filterRelationshipRoles(RelationshipResultImpl relResult) {
        pruneQueryArtifacts((QueryResultImpl)relResult.getFromRole().getRoleResult(),
                relResult.getFilteredLinks().getSources());

        pruneQueryArtifacts((QueryResultImpl)relResult.getToRole().getRoleResult(),
                relResult.getFilteredLinks().getTargets());
    }

    private void pruneQueryArtifacts(QueryResultImpl queryResult, Artifact[] artifacts) {
        List l = Arrays.asList(artifacts);
        queryResult.setFixedArtifacts(l);
    }
}

