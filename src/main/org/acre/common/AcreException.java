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
package org.acre.common;

/**
 * @author Deepak.Alur@Sun.com
 * @version Nov 6, 2004 4:21:06 PM
 */
public class AcreException extends Exception {


    AcreErrors errors;

    public AcreException() {
    }

    public AcreException(AcreErrors errors) {
        super(errors.toString());
        this.errors = errors;
    }

    public AcreException(AcreErrors errors, String message) {
        super(message + "\n" + errors.toString());
        this.errors = errors;
    }

    public AcreException(String message) {
        super(message);
    }

    public AcreException(String message, Throwable cause) {
        super(message, cause);
    }

    public AcreException(Throwable cause) {
        super(cause);
    }

    public String getErrorMessages() {
        if (errors == null)
            return "No Errors Set";

        return errors.toString();
    }
}
