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
import org.acre.lang.pql.pdbc.PQLResultSetImpl;
import org.codehaus.groovy.runtime.InvokerHelper;

import java.beans.IntrospectionException;
import java.util.List;

public class PQLResultSetImplMetaClass extends org.acre.lang.runtime.groovy.PQLMetaClass {
    public Object getProperty(Object object, String property) {
        try {
            return super.getProperty(object, property);
        } catch (MissingPropertyException e) {
            PQLResultSetImpl self = (PQLResultSetImpl) object;
            Object value = self.getMetaData();
            return InvokerHelper.getMetaClass(value).getProperty(value, property);
        }
    }

    public static class Holder {
        PQLResultSetImpl self;
        int row;

        public Holder(PQLResultSetImpl self, int row) {
            this.self = self;
            this.row = row;
        }

        public Object getAt(int col) {
            return self.getValue(row, col);
        }
    }

    public Object invokeMethod(Object object, String methodName, Object[] arguments) {
        PQLResultSetImpl self = (PQLResultSetImpl) object;
        if ("getAt".equals(methodName)) {
            if (arguments.length == 1) {
                if (arguments[0] instanceof List) {
                    List list = (List) arguments[0];
                    if (list.size() == 2
                        && ((list.get(0) instanceof Integer) || (list.get(0) instanceof Long))
                        && ((list.get(1) instanceof Integer) || (list.get(1) instanceof Long))
                    ) {
                        return self.getValue(((Number) list.get(0)).intValue(), ((Number) list.get(1)).intValue());
                    }
                }
                if ((arguments[0] instanceof Integer) || (arguments[0] instanceof Long)) {
                    return new Holder(self, ((Number) arguments[0]).intValue());
                }
            }
            throw new IllegalArgumentException("No method getAt(...) found: arguments=" + arguments);
        }
        return super.invokeMethod(object, methodName, arguments);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public PQLResultSetImplMetaClass(MetaClassRegistry registry, Class theClass) throws IntrospectionException {
        super(registry, theClass);
    }
}
