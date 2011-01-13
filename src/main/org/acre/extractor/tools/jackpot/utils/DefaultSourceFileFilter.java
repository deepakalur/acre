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

import java.io.*;

/**
 * An implementation of SourceFileFilter that rejects known source
 * control directories and backup file names.
 *
 * @author Tom Ball
 * @version 1.2 08/15/03
 * @see SourceFileFilter
 */
public class DefaultSourceFileFilter implements SourceFileFilter {
    public boolean acceptFile(String name) {
	return ! (name.startsWith(".") ||            // hidden file
		  name.startsWith("s.") ||           // SCCS
		  name.startsWith("p.") ||           // SCCS
		  name.endsWith("~") ||              // Emacs backup file
		  name.endsWith(".BAK"));
    }

    public boolean acceptDirectory(String name) {
	return ! (name.equals("SCCS") || 
		  name.equals("CVS") ||
		  name.equals("Codemgr_wsdata") ||   // TeamWare
		  name.equals("deleted_files"));     // TeamWare deleted files
    }
}
