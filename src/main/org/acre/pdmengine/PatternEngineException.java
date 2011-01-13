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
package org.acre.pdmengine;

/**
 * @author Deepak.Alur@Sun.com
 *         Date: Aug 3, 2004
 *         Time: 11:13:29 AM
 */
public class PatternEngineException extends RuntimeException {
    public PatternEngineException() {
    }

    public PatternEngineException(String message) {
        super(message);
    }

    public PatternEngineException(String message, Throwable cause) {
        super(message, cause);
    }

    public PatternEngineException(Throwable cause) {
        super(cause);
    }
}
