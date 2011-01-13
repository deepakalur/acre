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
package org.acre.pdmengine.util;

import java.util.Random;

/**
 * @author rajmohan@Sun.com
 * @version Dec 19, 2004 6:50:49 PM
 */
public class PatternEngineUtil {
    static int counter = -1;
    public static String generateTempName() {
        if ( counter == -1)
            counter = new Random().nextInt() & 0xffff;
        counter++;
        return Integer.toString(counter);
    }

    public static RoleReference getRoleReference(String roleReference) {
        RoleReference roleref = new RoleReference();
        int sepIdx = roleReference.indexOf('.');
        if ( sepIdx >= 0 ) {
            roleref.pattern = roleReference.substring(0, sepIdx);
            roleref.role = roleReference.substring(sepIdx+1);
        }
        else {
            roleref.role = roleReference;
        }
        return roleref;
    }

    public static class RoleReference {
        public String pattern;
        public String role;
    }
}
