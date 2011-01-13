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
package org.acre.lang.pql.pdbc;

/**
 * @author Deepak.Alur@Sun.com
 *         Date: Oct 14, 2004
 *         Time: 2:49:20 PM
 */
public class PQLParameterException extends PQLException {
    public PQLParameterException() {
    }

    public PQLParameterException(PQLErrors errors, String message) {
        super(errors, message);
    }

    public PQLParameterException(PQLErrors errors) {
        super(errors);
    }

    public PQLParameterException(String message) {
        super(message);
    }

    public PQLParameterException(String message, Throwable cause) {
        super(message, cause);
    }

    public PQLParameterException(Throwable cause) {
        super(cause);
    }
}
