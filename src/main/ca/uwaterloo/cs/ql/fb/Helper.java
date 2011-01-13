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
package ca.uwaterloo.cs.ql.fb;

import ca.uwaterloo.cs.ql.interp.Scope;
import ca.uwaterloo.cs.ql.interp.Variable;

import java.util.List;
import java.util.ArrayList;
import java.util.Enumeration;

import org.acre.lang.pql.pdbc.PQLArtifact;
import org.acre.lang.pql.pdbc.PQLValueHolder;
import org.acre.model.metamodel.PopulateMetaModel;

public class Helper {

    public static List getForwardRels(int nodeID, EdgeSet eSet, EdgeSet typeSet) {
        int ind;
        int prev;
        int count;
        TupleList domList; // dom sorted.
        TupleList rngList; // rng sorted.
        List result = new ArrayList();

//        rngList = eSet.shadow();
//        if ((eSet.sortLevel % 2) != 0) {
//            TupleList t_l = new TupleList(eSet.size());
//            RadixSorter.sort(rngList, 0, t_l);
//            eSet.data = t_l;
//            eSet.sortLevel = 2;
//        }

        domList = eSet.data;
        // Get ( rel -> node @ nodeID )
        ind = BinarySearch.search(domList, nodeID, 0);
        if (ind >= 0) {

            while (ind > 0) {
                prev = ind - 1;
                if (domList.get(prev).get(0) == nodeID)
                    ind = prev;
                else
                    break;
            }

            Tuple t;
            count = domList.size();
            while (ind < count) {
                t = domList.get(ind);
                if (t.get(0) != nodeID)
                    break;
                result.add(createArtifactShell(t.get(1), typeSet));
                ind++;
            }
        }
        return result;
    }

    public static List getReverseRels(int nodeID, EdgeSet eSet, EdgeSet typeSet) {
        int ind;
        int prev;
        int count;
        TupleList rngList; // rng sorted.
        List result = new ArrayList();

        rngList = eSet.shadow();
        if ((eSet.sortLevel % 2) != 0) {
            TupleList t_l = new TupleList(eSet.size());
            RadixSorter.sort(rngList, 0, t_l);
            eSet.data = t_l;
            eSet.sortLevel = 2;
        }

        // Get ( rel <- node @ nodeID )
        ind = BinarySearch.search(rngList, nodeID, 1);
        if (ind >= 0) {
            while (ind > 0) {
                prev = ind - 1;
                if (rngList.get(prev).get(1) == nodeID)
                    ind = prev;
                else
                    break;
            }

            char a = 'q';
            Tuple t;
            count = rngList.size();
            while (ind < count) {
                t = rngList.get(ind);
                if (t.get(1) != nodeID)
                    break;
                result.add(createArtifactShell(t.get(0), typeSet));

                ind++;
            }
        }
        return result;
    }
    /*
    public static class PointerStruct {
        private int node;
        private String name, type;

        private PointerStruct(int node) {
            this.node = node;
        }

        public String getName() {
            return name;
        }

        public int getNode() {
            return node;
        }

        public String getType() {
            return type;
        }


    }
    */
    public static PQLArtifact createArtifactShell(int node, EdgeSet typeSet) {
        return createArtifactShell(node, IDManager.get(node), Show.getAtt(node, typeSet));
    }

    public static PQLArtifact createArtifactShell(int node, String name, String type) {
        PQLValueHolder o = new PQLValueHolder(node);
        o.setName(name);
        o.setType(type);
        PQLArtifact value = new PQLArtifact(o);
        return value;
    }

    public static PQLArtifact createArtifact(long node, Scope scp, EdgeSet instances) {
        return createArtifact(node, scp, instances, false);
    }


    public static PQLArtifact createArtifact(long node, Scope scp, EdgeSet instances, boolean fetchAll) {
        PQLArtifact result = new PQLArtifact(createValueHolder(node, scp, instances, fetchAll));
        return result;
    }

    public static PQLValueHolder createValueHolder(long node, Scope scp, EdgeSet instances, boolean fetchAll) {
        PQLValueHolder value = new PQLValueHolder(node);
        populate(value, scp, instances, fetchAll);
        return value;
    }

    public static void populate(PQLValueHolder result, Scope scp, EdgeSet instances, boolean fetchAll) {
        String varName;
        Variable var;
        int node = (int) result.getNodeId();
        Enumeration enumerator = scp.allVisibleVariables();
        while (enumerator.hasMoreElements()) {
            var = (Variable) enumerator.nextElement();
            if (var.getType() == EdgeSet.class) {
                varName = var.getName();
                if (varName.equals("@name")) {
                    result.setName(Show.getAtt(node, (EdgeSet) var.getValue().objectValue()));
                } else if (varName.equals("$INSTANCE")) {
                    result.setType(PopulateMetaModel.getMappedName1(
                        Show.getAtt(node, (EdgeSet) var.getValue().objectValue())));
                } else if (fetchAll && varName.charAt(0) == '@') {
                    result.addAttribute(varName.substring(1),
                        Show.getAtt(node, (EdgeSet) var.getValue().objectValue()));
                } else if (fetchAll) {
                    EdgeSet relEdge = (EdgeSet) var.getValue().objectValue();
                    List rels = Helper.getForwardRels(node, relEdge, instances);
                    if (rels.size() > 0) {
                        result.addRelationship(varName, rels);
                    }
                    rels = Helper.getReverseRels(node, relEdge, instances);
                    if (rels.size() > 0) {
                        result.addRelationship("inv(" + varName + ")", rels);
                    }
                }
            }
        }
        if (result.getName() == null) {
            result.setName(IDManager.get(node));
        }
        if (fetchAll) {
            result.setComplete(true);
        }
    }


}

