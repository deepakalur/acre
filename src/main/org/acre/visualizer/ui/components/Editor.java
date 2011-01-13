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
package org.acre.visualizer.ui.components;


/**
 * @author Deepak.Alur@Sun.com
 *         Date: Jun 22, 2004
 *         Time: 8:05:14 PM
 */
public interface Editor {

    // Make the Query Panel implement this interface and plug it into the QueryViewer
    public void viewObject(Object value);

    public void deleteObject(Object key);

    public void addObject(Object info);

    public void executeObject(Object key);

    public void editObject(Object value);

    public void clear();

    public void refreshList();
}
