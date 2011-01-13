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
package org.acre.extractor.tools.jackpot.utils;

/**
 * Filter for accepting files and directories.  Like 
 * java.io.FilenameFilter, but it includes separate tests for 
 * files and directories to improve performance.
 *
 * @author Tom Ball
 * @version 1.2 08/15/03
 * @see DefaultSourceFileFilter
 */
public interface SourceFileFilter {
    boolean acceptFile(String name);
    boolean acceptDirectory(String name);
}
