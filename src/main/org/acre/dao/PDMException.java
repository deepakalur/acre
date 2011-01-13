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
package org.acre.dao;

import org.acre.common.AcreErrors;
import org.acre.common.AcreRuntimeException;


/**
 * @author Deepak.Alur@Sun.com
 *         Date: Jun 10, 2004
 *         Time: 3:37:48 PM
 */
public class PDMException extends AcreRuntimeException {
    public PDMException(AcreErrors errors) {
        super(errors);
    }

    public PDMException(AcreErrors errors, String message) {
        super(errors, message);
    }

    public PDMException() {
        super();
    }

    public PDMException(String message) {
        super(message);
    }

    public PDMException(String message, Throwable cause) {
        super(message, cause);
    }

    public PDMException(Throwable cause) {
        super(cause);
    }
}
