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
package org.acre.visualizer.graph;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: Jun 8, 2004
 * Time: 10:41:05 AM
 * To change this template use File | Settings | File Templates.
 */
public class AcreGraphException extends RuntimeException {
    public AcreGraphException() {
    }

    public AcreGraphException(String message) {
        super(message);
    }

    public AcreGraphException(Throwable cause) {
        super(cause);
    }

    public AcreGraphException(String message, Throwable cause) {
        super(message, cause);
    }
}
