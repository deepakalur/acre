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
package org.acre.analytics;

import java.util.Date;

/**
 * Analtyics Engine is responsible for analysing discovered pattern results and
 * produce "integrated information" including dashboard, scorecard & other
 * intelligent cross-dimensional reports that help users to access, analyse and act on
 * the analysed information.
 */
public interface PatternAnalyticsEngine {

/**
 *  Get all snapshots between startDate and endDate
  * @param startDate  if null, defaults to 1 year prior to endDate
 * @param endDate if null, defaults to current time
 * @return
 */
    Snapshots getSnapshots(Date startDate, Date endDate);

/**
 * Get patterns snapshot for the range specified in query specification object.
 * @param query
 * @return
 */
    Snapshots getSnapshots(SnapshotQuery query);
}
