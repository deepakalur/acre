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
package org.acre.pdmengine.model.visitor;

import org.acre.pdmengine.model.PatternResult;
import org.acre.pdmengine.model.QueryResult;
import org.acre.pdmengine.model.RelationshipResult;
import org.acre.pdmengine.model.RoleResult;

/**
 * @author rajmohan@Sun.com
 */
public class ResultVisitor {
    public void visitPatternResult(PatternResult patternResult) {}
    public void visitPatternRoleResult(RoleResult roleResult) {}
    public void visitRelationshipResult(RelationshipResult relationshipResult) {}
    public void visitQueryResult(QueryResult queryResult) {}
    public void visitPatternComplete(PatternResult patternResult){}
    public boolean isVisitCompleted() { return  visitComplete; }
    protected void setVisitCompleted(boolean visitComplete) {
               this.visitComplete = visitComplete;
    }
    private boolean visitComplete = false;
}
