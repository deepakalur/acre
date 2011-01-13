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

import org.acre.dao.DAOFactory;
import org.acre.dao.PatternRepository;

import java.io.Serializable;
import java.util.*;

/**
 * @author rajmohan@Sun.com
 */
public class Snapshots implements Serializable {
    static final long serialVersionUID = -2193049493271940641L;
    
    public Iterator getAllSnapshots() {
        return snapshotSet.iterator();
    }


    public synchronized Iterator getSortedByTime() {
        if ( ssByTime != null )
            return ssByTime.iterator();

        ssByTime = new TreeSet(new Comparator() {

            public int compare(Object o1, Object o2) {
                Snapshot s1, s2;
                s1 = (Snapshot)o1;
                s2 = (Snapshot)o2;
                return s1.getTimestamp().compareTo(s2.getTimestamp());
            }
        });

        ssByTime.addAll(snapshotSet);
        return ssByTime.iterator();
    }

    public synchronized Iterator getSortedByVersion() {
        if ( ssByVersion != null )
            return ssByVersion.iterator();

        ssByVersion = new TreeSet(new Comparator() {

            public int compare(Object o1, Object o2) {
                Snapshot s1, s2;
                s1 = (Snapshot)o1;
                s2 = (Snapshot)o2;
                return s1.getVersion().compareTo(s2.getVersion());
            }
        });
        ssByVersion.addAll(snapshotSet);
        return ssByVersion.iterator();
    }

    public synchronized  Iterator getSortedBySystem() {
        if ( ssBySystem != null )
            return ssBySystem.iterator();

        ssBySystem = new TreeSet(new Comparator() {

            public int compare(Object o1, Object o2) {
                Snapshot s1, s2;
                s1 = (Snapshot)o1;
                s2 = (Snapshot)o2;
                return s1.getSystem().compareTo(s2.getSystem());
            }
        });
        ssBySystem.addAll(snapshotSet);
        return ssBySystem.iterator();
    }

    public Iterator getPatternNames() {
        PatternRepository patternRepository = DAOFactory.getPatternRepository();
        return patternRepository.getGlobalPatternModelNames().iterator();
    }

    public synchronized void addSnapshot(Snapshot snapshot) {
        if ( !snapshotSet.contains(snapshot))
            snapshotSet.add(snapshot);
    }

    private Set snapshotSet = Collections.synchronizedSet(new HashSet());
    private Set ssByTime ;
    private Set ssByVersion;
    private Set ssBySystem;
}
