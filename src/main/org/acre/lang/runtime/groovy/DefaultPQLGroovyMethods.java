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

import org.acre.lang.pql.pdbc.PQLArtifact;
import org.acre.lang.pql.pdbc.PQLResultSetImpl;
import org.codehaus.groovy.runtime.InvokerHelper;

public class DefaultPQLGroovyMethods {

    public static void boo(PQLResultSetImpl self) {
        System.out.println("==========boo===============");
        System.out.println("==========boo===============");
        System.out.println("==========boo===============");
        System.out.println("==========boo===============");
        System.out.println("==========boo===============");
        System.out.println("==========boo===============");
    }


//// Groovy metaclass bug: The method below does not work
//    public static Iterator iterator(PQLResultSetImpl self) {
////        Arrays.asList((Object[]) object)
//        System.out.println("=================iterator()================");
//        System.out.println("=================iterator()================");
//        System.out.println("=================iterator()================");
//        return InvokerHelper.asIterator(self.getValues());
////        return Arrays.asList(self.getValues()).iterator();
//    }

//    public static Object getProperty(PQLResultSetImpl self, String property) {
//        Object res = InvokerHelper.getMetaClass(self).getProperty(self, property);
//        return res;
//    }

    public static Object getProperty(PQLArtifact self, String property) {
        Object res = InvokerHelper.getMetaClass(self).getProperty(self, property);
        if (null == res) {
            res = InvokerHelper.getMetaClass(self.getValue()).getProperty(self, property);
        }
        return res;
    }
//
//
//    public static Object getProperty(PQLResultSetImpl self, String property) {
//        System.out.println("=================getProperty()================");
//        return InvokerHelper.getMetaClass(self).getProperty(self, property);
//    }
//
//    public static void setProperty(PQLResultSetImpl self, String property, Object newValue) {
//        System.out.println("=================setProperty()================");
//         InvokerHelper.getMetaClass(self).setProperty(self, property, newValue);
//    }
//
//    public static Object invokeMethod(PQLResultSetImpl self, String name, Object args) {
//        System.out.println("=================invokeMethod()================");
//        return InvokerHelper.getMetaClass(self).invokeMethod(self, name, args);
//    }
//
////    public static Object invokeMethod(PQLResultSetImpl object, String method, Object arguments) {
////        return InvokerHelper.invokeMethod(object, method, arguments);
////    }
}
