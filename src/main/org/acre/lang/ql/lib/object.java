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
package org.acre.lang.ql.lib;

import ca.uwaterloo.cs.ql.fb.*;
import ca.uwaterloo.cs.ql.interp.*;
import ca.uwaterloo.cs.ql.lib.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <pre>
 * Functions:
 *
 *      List object(set)
 *
 * </pre>
 */
public class object extends BuiltinFunction {
    private Env env;
    private static boolean registered = false;

    protected object() {
        name = "object";
        type = List.class;
        paramTypes = new Class[1];
        paramTypes[0] = NodeSet.class;
    }

    public static void register(FunctionLib fLib) {
        if (registered)
            return;
        registered = true;

        object f;
        f = new object();
        f.paramTypes[0] = NodeSet.class;
        fLib.register(f);

        f = new object();
        f.paramTypes[0] = TupleSet.class;
        fLib.register(f);

        Class[] params;
        f = new object();
        params = new Class[2];
        params[0] = TupleSet.class;
        params[1] = NodeSet.class;
        f.setParamTypes(params);
        fLib.register(f);
        
    }

    public Value invoke(Env env, Value[] vals) throws InvocationException {
        List result = new ArrayList();
        Object o = vals[0].objectValue();
        Scope scp;
        this.env = env;
        scp = env.peepScope();
        EdgeSet instances = null;
        try {
            Variable variable = scp.lookup("$INSTANCE");
            if (variable != null) {
                instances = (EdgeSet) (variable.getValue().objectValue());
            }
        } catch (LookupException e) {
            throw new IllegalAccessError("$INSTANCE set not intialized: " + e.getMessage());
        }
        if (o instanceof NodeSet) {
            Node[] nodes = ((NodeSet) o).getAllNodes();
            // wrap all
            for (int j = 0; j < nodes.length; j++) {
                result.add(Helper.createArtifact(nodes[j].getID(), scp, instances));
            }
        } else {
            Set columns = null;
            if (vals.length > 1) {
                Object columnSet = vals[1].objectValue();
                if (columnSet instanceof NodeSet) {
                    columns = new HashSet();
                    Node[] nodes = ((NodeSet) columnSet).getAllNodes();
                    for (int j = 0; j < nodes.length; j++) {
                        columns.add(nodes[j].get());
                    }
                }
            }
            Tuple[] tuples = ((TupleSet) o).getAllTuples();
            // wrap all
            for (int j = 0; j < tuples.length; j++) {
                List row = new ArrayList(tuples[j].size());
                for (int k = 0; k < tuples[j].size(); k++) {
                    if (columns == null || columns.contains(k+"")) {
                        row.add(Helper.createArtifact(tuples[j].get(k), scp, instances));
                    } else {
                        row.add(IDManager.get(tuples[j].get(k)));
                    }
                }
                result.add(row);
            }
        }
        return new Value(result);
    }
    
}
