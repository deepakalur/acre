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
import java.util.*;
import java.util.logging.*;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;

/**
 * File and Jar file expansion utility routines.
 * @author Tom Ball
 * @version 1.2 08/15/03
 */
public final class SourceFileList {

    public static final String JAR_URL_PREFIX = "jar:/file:/";

    public static String[] findFiles(String [] args) {
	return findFiles(args, allFilter, null);
    }

    public static String[] findFiles(String [] args, Logger logger) {
	return findFiles(args, allFilter, logger);
    }

    public static String[] findFiles(String[] args, SourceFileFilter filter) {
	return findFiles(args, filter, null);
    }

    public static String[] findFiles(String [] args, SourceFileFilter filter,
				     Logger logger) {
	List<String> nargs = new ArrayList<String>();
	int limit = args.length;
	for(int i = 0; i<limit; i++) 
	    expand(null, args[i], filter, nargs);
	return nargs.toArray(new String[nargs.size()]);
    }

    private static void expand(File dir, String name, SourceFileFilter filter,
			       List nargs) {
	File f = dir != null ? new File(dir, name) : new File(name);
	if (filter.acceptFile(name)) {
	    nargs.add(f.getPath());
	    return;
	}
	if(name.endsWith(".jar") || name.endsWith(".zip")) {
	    expandZip(f.getPath(), filter, nargs);
	    return;
	}
	String[] subnames = f.list();
	if (subnames == null) 
	    return;
	int limit = subnames.length;
	for(int i = 0; i<limit; i++) {
	    String sn = subnames[i];
	    if (filter.acceptDirectory(sn))
		expand(f, sn, filter, nargs);
	}
    }

    private static void expandZip(String zipFileName, SourceFileFilter filter, 
				  List nargs) {
	try {
	    ZipFile zf = new ZipFile(zipFileName);
	    Enumeration files = zf.entries();
	    while (files.hasMoreElements()) {
		ZipEntry entry = (ZipEntry)files.nextElement();
		String name = entry.getName();
		if (filter.acceptFile(name))
		    nargs.add(JAR_URL_PREFIX + zipFileName + '!' + name);
	    }
	} catch (IOException e) {
	    System.err.println("failed reading zip file " + zipFileName + 
			       ": " + e);
	}
    }

    private static final SourceFileFilter allFilter = 
	new DefaultSourceFileFilter();
}
