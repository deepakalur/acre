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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Links input and output streams so that data taken from input
 * stream is transfered to the output stream.
 */
public class Pipe {

    /**
     * Default pipe buffer size
     */
    static public int defaultBufferSize = 4096;

    /**
     * Establish connection between input and output streams with specified
     * size of buffer used for data transfer.
     *
     * @param in         input stream
     * @param out        output stream
     * @param bufferSize size of buffer used to transfer data from the input
     *                   stream to the output stream
     */
    static public PipeThread between(InputStream in, OutputStream out, int bufferSize) {
        PipeThread t = new PipeThread(in, out, bufferSize);
        t.start();
        return t;
    }

    /**
     * Establish connection between input and output streams with default
     * buffer size.
     *
     * @param in  input stream
     * @param out output stream
     */

    static public PipeThread between(InputStream in, OutputStream out) {
        PipeThread t = new PipeThread(in, out, defaultBufferSize);
        t.start();
        return t;
    }
        static class PipeThread extends Thread {
        InputStream in;
        OutputStream out;
        byte[] buffer;

        PipeThread(InputStream in, OutputStream out, int bufferSize) {
            this.in = in;
            this.out = out;
            buffer = new byte[bufferSize];
        }

        public void run() {
            try {
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                    out.flush();
                }
                out.flush();
//                in.close();
                out.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
