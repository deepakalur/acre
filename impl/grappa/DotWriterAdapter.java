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
package org.acre.visualizer.grappa;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: johncrupi
 * Date: Oct 10, 2004
 * Time: 11:35:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class DotWriterAdapter {
    public static int SB = 1;
    public static int FILE = 2;
    int type;

    FileWriter file;
    StringBuffer sb;

    public DotWriterAdapter(String fileName) throws IOException {
        file = new FileWriter(new File(fileName));
        type = FILE;
    }

    public DotWriterAdapter() {
        sb = new StringBuffer();
        type = SB;
    }

    public void close() throws IOException {
        if (type == FILE) {
            file.close();
        }
    }

    public void flush() throws IOException {
        if (type == FILE) {
            file.flush();
        }
    }

    public void write(String msg) throws IOException {
        if (type == FILE) {
            file.write(msg);
        } else {
            sb.append(msg);
        }
    }

    public String getString() {
        if (type == FILE) {
            return file.toString();
        } else {
            return sb.toString();
        }
    }
}
