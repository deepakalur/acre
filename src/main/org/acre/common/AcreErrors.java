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

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;

/**
 * @author Deepak.Alur@Sun.com
 * @version Nov 6, 2004 3:53:12 PM
 */
public class AcreErrors extends Properties {

    private Object getMessage(Object key) {
        return "Message Not Defined for key '"+ key +"'";
    }

    public void addError(Object key, Object value) {
        put(key, value);
    }

    public Object getError(Object key) {
        return get(key);
    }

    public Enumeration getErrorKeys() {
            return this.keys();
    }

    public Enumeration getErrorValues() {
            return this.elements();
    }


    public String toString() {
        Iterator errs = this.values().iterator();
        StringBuffer buf = new StringBuffer();

        while (errs.hasNext()) {
            buf.append(errs.next());
            buf.append("\n");
        }

        return buf.toString();
    }

    public void check() {
        if (! isEmpty()) {
            throw new AcreRuntimeException(this);
        }
    }

    public void addAcreErrors(AcreErrors errors) {
        this.putAll(errors);
    }
}
