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

import org.acre.pdmengine.model.PatternResult;
import org.acre.pdmengine.model.Result;
import org.acre.pdmengine.model.visitor.ResultVisitor;

/**
 * @author Deepak.Alur@Sun.com
 *         Date: Sep 10, 2004
 *         Time: 4:24:02 PM
 */
abstract public class ResultImpl implements Result, java.io.Serializable {
    static final long serialVersionUID = -5242744551900891857L;
    
    int level;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void accept(ResultVisitor visitor) {
        // no-op
    }

    public int getHitCount() {
        /**
         * if ( having relationship ) {
         *          return map[relationshipName, count]
         * } else {
         *      if ( roles.type==QueryResult)
         *          return roles.role[0].count;
         *      else if ( roles.type == PatternResult) {
         *              min(count(calls for all relationships))
         *      }
         * }
         */

        // if calls relationship { return calls.getHitCount(); }
        // if having 1 role { role.getQueryResult().artifactcount }

        //
        return -1;
    }

    abstract public PatternResult getPattern();
}

