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
package org.acre.lang.runtime.groovy;

import groovy.lang.MetaClassRegistry;
import groovy.lang.MissingPropertyException;
import org.acre.lang.pql.pdbc.PQLArtifact;
import org.codehaus.groovy.runtime.InvokerHelper;

import java.beans.IntrospectionException;

public class PQLArtifactMetaClass extends PQLMetaClass {
    public Object getProperty(Object object, String property) {
        try {
            return super.getProperty(object, property);
        } catch (MissingPropertyException e) {
            PQLArtifact self =   (PQLArtifact)object;
            Object value = self.getValue();
            return InvokerHelper.getMetaClass(value).getProperty(value, property);
        }
    }

    public PQLArtifactMetaClass(MetaClassRegistry registry, Class theClass) throws IntrospectionException {
        super(registry, theClass);
    }
}
