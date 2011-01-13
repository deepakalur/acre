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

import org.acre.lang.runtime.PQL;
import org.acre.model.metamodel.PopulateMetaModel;

import java.util.*;

/**
 * Holder Object that will represent ResultSet Row Object for holdingPQL queries
 *
 * @author Syed Ali
 */
public class PQLValueHolder implements java.io.Serializable {
	
    private final static boolean PRINT_ATTRS = false;
    private final static boolean PRINT_RELS = false;
    public final static boolean FETCH_INVERSE_RELS = true;
    private boolean complete = false;
    private long nodeId;
    private String name;
    private String type;
    private Map atrs = new HashMap();
    private Map rels = new HashMap();
    
    static final long serialVersionUID = 516807792930395899L;

    public PQLValueHolder(long id) {
        nodeId = id;
    }
    
    public void addAttribute(String name, String value) {
        if (atrs.get(name) == null) {
            atrs.put(name, value);
        } else {
            atrs.put(name, atrs.get(name) + "," + value);
        }
    }
    public void addRelationship(String name, List values) {
        String mappedName;
        Map typeBag = new HashMap();
        for (Iterator iter = values.iterator(); iter.hasNext();) {
            //PQLValueHolder element = (PQLValueHolder) iter.next();
            Object o = iter.next();
            PQLValueHolder element = null;
            if (o instanceof PQLArtifact)
                element = (PQLValueHolder) ((PQLArtifact) o).getValue();
            if (o instanceof PQLValueHolder)
                element = (PQLValueHolder) o;
            if (element == null )
                throw new IllegalArgumentException("Value cannot contain instances of " + o.getClass());
            List types = (List) typeBag.get(element.getType());
            if (types == null) {
                types = new ArrayList();
                typeBag.put(element.getType(), types);
            }
            types.add(element);
        }
        for (Iterator iter = typeBag.entrySet().iterator(); iter.hasNext();) {
            Map.Entry element = (Map.Entry) iter.next();
            String typeName = (String) element.getKey();
            List relations = (List) element.getValue();
            if (typeName == null) {
                mappedName = name;
            } else {
                mappedName = PopulateMetaModel.getReverseRelationName(type, typeName, name);
            }
            RelationShips s = (RelationShips) rels.get(mappedName);
            if (s == null) {
                s = new RelationShips();
            }
            s.add(relations);
            rels.put(mappedName, s);
        }
    }
    public void addRelationship1(String name, List values) {
        String mappedName = name;
//      String mappedName = PopulateMetaModel.getReverseRelationName(type, name);
        RelationShips s = (RelationShips) rels.get(mappedName);
        if (s == null) {
            s = new RelationShips();
        }
        s.add(values);
        rels.put(mappedName, s);
    }
    public String toString() {
        return toString(PRINT_ATTRS, PRINT_RELS);
    }
    
    public String toString(boolean printAttrs, boolean printRels) {
        StringBuffer b = new StringBuffer(name + " : " + type);
        if (printAttrs) {
            b.append(" { ");
            for (Iterator iter = atrs.entrySet().iterator(); iter.hasNext();) {
                Map.Entry element = (Map.Entry) iter.next();
                b.append(element.getKey() + "=" + element.getValue() + " ");
            }
            b.append(" }\n");
        }
        if (printRels) {
            for (Iterator iter = rels.entrySet().iterator(); iter.hasNext();) {
                Map.Entry element = (Map.Entry) iter.next();
                b.append("  " + element.getKey() + ":\n");
                RelationShips rs = (RelationShips) element.getValue();
                for (int i = 0; i < rs.relationships.length; i++) {
                    b.append("    " + rs.relationships[i].getName() + "\n");
                    //b.append("    " + rs.relationships[i].getNodeId() + " " + rs.relationships[i].getType() + " "+ rs.relationships[i].getName() + "\n");
                }
            }
        }
        return b.toString();
    }

    public long getNodeId() {
        return nodeId;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void setName(String string) {
        name = string;
    }

    public void setType(String string) {
        type = string;
    }

    public Iterator getAttributeNames() {
        return atrs.keySet().iterator();
    }

    public String getAttribute(String name) {
        String result = (String) atrs.get(name);
        if (result == null)
            throw new IllegalArgumentException("Attribute name " + name + " not found"); 
        return result;
    }

    public Iterator getRelationshipNames() {
        return rels.keySet().iterator();
    }

    public PQLValueHolder[] getRelationship(String name) {
        RelationShips rel = (RelationShips) rels.get(name);
        if (rel == null)
            throw new IllegalArgumentException("Relationship name " + name + " not found"); 
        return rel.relationships;
    }

    public PQLValueHolder getOneRelationshipObject(PQL engine, String name) {
        PQLValueHolder[] result = getRelationship(name);
        if (result == null || result.length == 0) {
            return null;
        }
        if (!result[0].isComplete()) 
            engine.fetch(result[0]);
        return result[0];
    }

    public PQLValueHolder[] getManyRelationshipObject(PQL engine, String name) {
        PQLValueHolder[] result = getRelationship(name);
        for (int i = 0; i < result.length; i++) {
            if (!result[i].isComplete()) 
                engine.fetch(result[i]);
        }
        return result;
    }

    public static class RelationShips {
        PQLValueHolder[] relationships;
        
        public void add(Collection values) {
            PQLValueHolder rel;
            List relList = new ArrayList();
            if (relationships != null) {
                relList.addAll(Arrays.asList(relationships));
            }
            for (Iterator iter = values.iterator(); iter.hasNext();) {
                PQLValueHolder element = (PQLValueHolder) iter.next();
//                rel = new PQLPointer(element.getNodeId());
//                rel.setName(element.getName());
//                rel.setType(element.getType());
                relList.add(element); 
            } 
            relationships = new PQLValueHolder[relList.size()];
            relList.toArray(relationships);
        }
    }

    public boolean isComplete() {
        return complete;
    }
    public void setComplete(boolean value) {
        complete = value;
    }

    public int hashCode() {
        return getName().hashCode();
    }
    
    public boolean equals(Object obj) {
        if ( obj == null )
            return false;
        
        if ( obj == this )
            return true;

        if ( obj instanceof PQLValueHolder) {
             PQLValueHolder that = (PQLValueHolder)obj;
             if ( (this.getNodeId() == that.getNodeId()) &&
                   this.getType().equals(that.getType()) )
                 return true;
        }
        return false;
    }
}