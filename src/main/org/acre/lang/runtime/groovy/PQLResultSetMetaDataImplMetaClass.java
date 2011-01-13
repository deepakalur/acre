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
import org.acre.lang.pql.pdbc.PQLResultSetMetaDataImpl;

import java.beans.IntrospectionException;

public class PQLResultSetMetaDataImplMetaClass extends PQLMetaClass {
    public static class ColumnHeader {
        private final PQLResultSetMetaDataImpl metadata;
        private int index;

        public ColumnHeader(PQLResultSetMetaDataImpl metadata, int index) {
            this.metadata = metadata;
            this.index = index;
        }

        public final String getName() {
            return metadata.getColumnName(index);
        }

        public final String getLabel() {
            return metadata.getColumnLabel(index);
        }

        public final String getType() {
            return metadata.getColumnType(index);
        }
    }

    public Object getProperty(Object object, String property) {
        PQLResultSetMetaDataImpl self = (PQLResultSetMetaDataImpl) object;
        if ("columnHeaders".equals(property)) {
            ColumnHeader[] c = new ColumnHeader[self.getColumnCount()];
            for (int i = 0; i < c.length; i++) {
                c[i] = new ColumnHeader(self, i);
            }
            return c;
        }
        return super.getProperty(object, property);
    }

    public PQLResultSetMetaDataImplMetaClass(MetaClassRegistry registry, Class theClass) throws IntrospectionException {
        super(registry, theClass);
    }
}
