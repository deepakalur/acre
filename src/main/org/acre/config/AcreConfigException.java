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
package org.acre.config;

/**
 * @author Deepak.Alur@Sun.com
 *         Date: Oct 22, 2004
 *         Time: 1:58:19 PM
 */
public class AcreConfigException extends RuntimeException {
    ConfigError error;

    public AcreConfigException(ConfigError error) {
        super();
        this.error = error;        
    }

    public AcreConfigException() {
    }

    public AcreConfigException(String message) {
        super(message);
    }

    public String getMessage() {
        if (error == null) {
            return super.getMessage();
        } else {
            return error.toString();
        }
    }

    public AcreConfigException(Throwable cause) {
        super(cause);
    }

    public AcreConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigError getError() {
        return error;
    }

    public void setError(ConfigError error) {
        this.error = error;
    }

}
