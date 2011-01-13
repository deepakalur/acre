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
package org.acre.visualizer.ui.pdmcomposer;

import org.acre.pdm.PDMType;
import org.acre.pdm.RelationshipType;
import org.acre.pdm.RoleType;
import org.acre.dao.PDMXMLConstants;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.util.List;


/**
 * @author Syed Ali
 */
public class PDMTreeModel implements TreeModel {

    //todo - make this Global and ProjectSpecific
    
    List  pdmList;

    public PDMTreeModel(List facade) {
        this.pdmList = facade;
    }

    public Object getRoot() {
        // TODO Auto-generated method stub
        return pdmList;
    }

    public int getChildCount(Object parent) {
        if (false) {
            return 0;
        } else if (parent instanceof List) {
            List node = (List) parent;
            return node.size();
        } else if (parent instanceof PDMType) {
            PDMType node = (PDMType) parent;
            return node.getRoles().getRole().size() + node.getRelationships().getRelationship().size(); 
        } else if (parent instanceof RoleType) {
            RoleType node = (RoleType) parent;
            if (PDMXMLConstants.ROLE_TYPE_PDM.equals(node.getType())) {
                //PDMType pdmRole = pdmFacade.getGlobalPatternModel(node.getTypeReferenceName());
                PDMType pdmRole = getPDMFromList(node.getTypeReferenceName());

                if (pdmRole == null) {
                    // todo - how to handle this error ? unknown PDMType referenced
                    return 0;
                    //throw new IllegalArgumentException(node.getTypeReferenceName() + " PDMType not found");
                } else {
                    return pdmRole.getRoles().getRole().size() + pdmRole.getRelationships().getRelationship().size();
                }
            } else {
                return 0;
            }
        } else if (parent instanceof RelationshipType) {
            return 2;
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
        } else if (parent instanceof PDMType) {
            return false;
        } else if (parent instanceof RoleType) {
            RoleType node = (RoleType) parent;
            if (PDMXMLConstants.ROLE_TYPE_QUERY.equals(node.getType())) {
                return true;
            } else {
                return false;
            }
        } else if (parent instanceof RelationshipType) {
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
        } else if (parent instanceof PDMType) {
            PDMType node = (PDMType) parent;
            List roles = node.getRoles().getRole();
            int rolesSize = roles.size();
            if (index < rolesSize) {
                return roles.get(index);
            } else {
                return node.getRelationships().getRelationship().get(index - rolesSize);
            }
        } else if (parent instanceof RoleType) {
            RoleType node = (RoleType) parent;
            if (PDMXMLConstants.ROLE_TYPE_QUERY.equals(node.getType())) {
                throw new IllegalArgumentException(node + " can not have children");
            } else {
                //PDMType pdmRole = pdmFacade.getGlobalPatternModel(node.getTypeReferenceName());
                PDMType pdmRole = getPDMFromList(node.getTypeReferenceName());
                List roles = pdmRole.getRoles().getRole();
                int rolesSize = roles.size();
                if (index < rolesSize) {
                    return roles.get(index);
                } else {
                    return pdmRole.getRelationships().getRelationship().get(index - rolesSize);
                }
            }
        } else if (parent instanceof RelationshipType) {
            RelationshipType node = (RelationshipType) parent;
            if (index == 0) {
                return node.getFromRole();
            } else {
                return node.getToRole();
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
        } else if (parent instanceof PDMType) {
            PDMType node = (PDMType) parent;
            List roles = node.getRoles().getRole();
            if (child instanceof RoleType) {
                for (int i = 0; i < node.getRoles().getRole().size(); i++) {
                    RoleType r = (RoleType) node.getRoles().getRole().get(i);
                    if (child.equals(r)) {
                        return i;
                    }
                }
            } else if (child instanceof RelationshipType) {
                for (int i = 0; i < node.getRelationships().getRelationship().size(); i++) {
                    RelationshipType r = (RelationshipType) node.getRelationships().getRelationship().get(i);
                    if (child.equals(r)) {
                        return node.getRoles().getRole().size() - 1 + i;
                    }
                }
            }
        } else if (parent instanceof RoleType) {
            RoleType role = (RoleType) parent;
            if ("Query".equals(role.getType())) {
                throw new IllegalArgumentException(role + " can not have children");
            } else {
                //PDMType node = pdmFacade.getGlobalPatternModel(role.getTypeReferenceName());
                PDMType node = getPDMFromList(role.getTypeReferenceName());
                List roles = node.getRoles().getRole();
                if (child instanceof RoleType) {
                    for (int i = 0; i < node.getRoles().getRole().size(); i++) {
                        RoleType r = (RoleType) node.getRoles().getRole().get(i);
                        if (child.equals(r)) {
                            return i;
                        }
                    }
                } else if (child instanceof RelationshipType) {
                    for (int i = 0; i < node.getRelationships().getRelationship().size(); i++) {
                        RelationshipType r = (RelationshipType) node.getRelationships().getRelationship().get(i);
                        if (child.equals(r)) {
                            return node.getRoles().getRole().size() - 1 + i;
                        }
                    }
                }
            }
        } else if (parent instanceof RelationshipType) {
            RelationshipType node = (RelationshipType) parent;
            if (child.equals(node.getFromRole())) {
                return 0;
            } else if (child.equals(node.getToRole())) {
                return 1;
            }
        }
        throw new IllegalArgumentException("Unknow parent & child: " + parent + " & " + child);
    }

    private PDMType getPDMFromList(String typeReferenceName) {
        for (int i=0; i < this.pdmList.size(); i++) {
            PDMType pdm = (PDMType) pdmList.get(i);
            if (pdm.getName().equals(typeReferenceName))
                return pdm;
        }

        return null;
    }

    public void valueForPathChanged(TreePath path, Object newValue) {
    }
    
}
