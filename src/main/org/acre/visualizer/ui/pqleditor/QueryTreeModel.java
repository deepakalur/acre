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
package org.acre.visualizer.ui.pqleditor;

import org.acre.pdmqueries.ArgumentType;
import org.acre.pdmqueries.QueryType;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.util.List;

/**
 * @author Deepak.Alur@Sun.com
 *         Date: Jun 22, 2004
 *         Time: 6:06:19 PM
 */
public class QueryTreeModel implements TreeModel {
    List dataList;
    private static final int NUM_QUERY_FIELDS = 4;
    private static final int NUM_ARG_FIELDS = 5;

    public QueryTreeModel(List dataList) {
        this.dataList = dataList;
    }

    //todo - make this Global and ProjectSpecific

    public Object getRoot() {
        // TODO Auto-generated method stub
        return dataList;
    }

    public int getChildCount(Object parent) {
        if (false) {
            return 0;
        } else if (parent instanceof List) {
            List node = (List) parent;
            return node.size();
        } else if (parent instanceof QueryType) {
            QueryType node = (QueryType) parent;
            return node.getArgument().size() + NUM_QUERY_FIELDS;
        } else if (parent instanceof ArgumentType) {
            return NUM_ARG_FIELDS;
        } else {
            unknownParent(parent);
        }
        return 0;

    }

    private void unknownParent(Object parent) {
        throw new IllegalArgumentException("Unknow parent: " +  parent.getClass()+ " " + parent);
    }

    public boolean isLeaf(Object parent) {
        if (false) {
            return false;
        } else if (parent instanceof List) {
            return false;
        } else if (parent instanceof QueryType) {
            return false;
        } else if (parent instanceof ArgumentType) {
            return false;
        } else if (parent instanceof String) {
            return true;
        } else {
            unknownParent(parent);
        }
        return true;
    }

    public void addTreeModelListener(TreeModelListener l) {
    }

    public void removeTreeModelListener(TreeModelListener l) {
    }

    public Object getChild(Object parent, int index) {
        if (false) {
            return null;
        } else if (parent instanceof List) {
            List node = (List) parent;
            return node.get(index);
        } else if (parent instanceof QueryType) {
            QueryType node = (QueryType) parent;
            List arguments = node.getArgument();

            if (index == 0) {
                return "Type = " + node.getType();
            } else if (index == 1) {
                return "Language = " + node.getLanguage();
            } else if (index == 2) {
                return "Description = " + node.getDescription();
            } else if (index == 3) {
                return "File Path = " + node.getRelativeFilePath();
            } else {
                // return arguments, if any
                int size = arguments.size();// + NUM_QUERY_FIELDS;
                int argIndex = index - NUM_QUERY_FIELDS;

                if (argIndex < arguments.size()) {
                    return arguments.get(argIndex);
                } else {
                    return arguments.get(index - size);
                }
            }
        } else if (parent instanceof ArgumentType) {
            ArgumentType node = (ArgumentType) parent;
            if (index == 0) {
                return "Sequence# = " + node.getSequence();
            } else if (index == 1) {
                return "Description = " + node.getDescription();
            } else if (index == 2) {
                return "Type = " + node.getType();
            } else if (index == 3) {
                return "Value = " + node.getValue();
            } else if (index == 4) {
                return "Related Query Name = " + node.getRelatedQueryName();
            }
        } else {
            unknownParent(parent);
        }
        return null;
    }

    public int getIndexOfChild(Object parent, Object child) {
        if (parent == null || child == null) {
            throw new IllegalArgumentException("Unknow parent & child: " + parent + " & " + child);
        }
        if (false) {
            return 0;
        } else if (parent instanceof List) {
            List node = (List) parent;
            return node.indexOf(child);
        } else if (parent instanceof QueryType) {
            QueryType node = (QueryType) parent;
            if (child instanceof ArgumentType) {
                for (int i = 0; i < node.getArgument().size(); i++) {
                    ArgumentType arg  = (ArgumentType) node.getArgument().get(i);
                    if (child.equals(arg)) {
                        return i;
                    }
                }
            }
        } else if (parent instanceof ArgumentType) {
            ArgumentType node = (ArgumentType) parent;
        }
        throw new IllegalArgumentException("Unknow parent & child: " + parent + " & " + child);
    }

    public void valueForPathChanged(TreePath path, Object newValue) {
    }


}
