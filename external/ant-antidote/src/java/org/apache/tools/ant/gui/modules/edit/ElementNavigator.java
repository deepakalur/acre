/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000 - 2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "Ant" and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */
package org.apache.tools.ant.gui.modules.edit;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.util.EventObject;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.apache.tools.ant.gui.acs.ACSElement;
import org.apache.tools.ant.gui.acs.ACSInfoProvider;
import org.apache.tools.ant.gui.acs.ACSProjectElement;
import org.apache.tools.ant.gui.acs.ACSTreeNodeElement;
import org.apache.tools.ant.gui.acs.ElementTreeModel;
import org.apache.tools.ant.gui.acs.ElementTreeSelectionModel;
import org.apache.tools.ant.gui.core.ActionManager;
import org.apache.tools.ant.gui.core.AntModule;
import org.apache.tools.ant.gui.core.AppContext;
import org.apache.tools.ant.gui.event.BusFilter;
import org.apache.tools.ant.gui.event.BusMember;
import org.apache.tools.ant.gui.event.DeleteElementEvent;
import org.apache.tools.ant.gui.event.ElementChangedEvent;
import org.apache.tools.ant.gui.event.ElementSelectionEvent;
import org.apache.tools.ant.gui.event.EventBus;
import org.apache.tools.ant.gui.event.NewElementEvent;
import org.apache.tools.ant.gui.event.NewProjectEvent;
import org.apache.tools.ant.gui.event.ProjectClosedEvent;
import org.apache.tools.ant.gui.event.ProjectSelectedEvent;
import org.apache.tools.ant.gui.event.RefreshDisplayEvent;

/**
 * Module for navigating build file elemenets.
 * 
 * @version $Revision: 1.1.1.1 $ 
 * @author Simeon Fitch, Christoph Wilhelms 
 */
public class ElementNavigator extends AntModule {

    /** Navigation via a tree widget. */
    private DragTree _tree = null;
    /** The selection model being used. */
    private ElementTreeSelectionModel _selections = null;

	/** 
	 * Default ctor.
	 * 
	 */
	public ElementNavigator() {
    }

	/** 
	 * Using the given AppContext, initialize the display.
	 * 
	 * @param context Application context.
	 */
    public void contextualize(AppContext context) {
        setContext(context);
        context.getEventBus().addMember(EventBus.MONITORING, new Handler());

        setLayout(new GridLayout(1,1));

        _tree = new DragTree();
        _tree.addDragTreeListener(new DragHandler());
        _tree.setModel(null);
        _tree.setCellRenderer(new ElementTreeCellRenderer());
        _tree.addMouseListener(new PopupHandler());
        _tree.putClientProperty("JTree.lineStyle", "Angled");
        _tree.setShowsRootHandles(true);
        JScrollPane scroller = new JScrollPane(_tree);
        add(scroller);

        setPreferredSize(new Dimension(250, 100));
        setMinimumSize(new Dimension(200, 100));

	}

    /** Class for handling project events. */
    private class Handler implements BusMember {
        private final Filter _filter = new Filter();

        /** 
         * Get the filter to that is used to determine if an event should
         * to to the member.
         * 
         * @return Filter to use.
         */
        public BusFilter getBusFilter() {
            return _filter;
        }
        
        /** 
         * Called when an event is to be posed to the member.
         * 
         * @param event Event to post.
         * @return true if event should be propogated, false if
         * it should be cancelled.
         */
        public boolean eventPosted(EventObject event) {
            ElementTreeModel model = (ElementTreeModel)_tree.getModel();
            // XXX This crap needs cleaning up. Type switching is lazy...
            if(event instanceof PropertyChangeEvent) {
                // The project node has changed.
                model.fireNodeChanged((ACSElement)event.getSource());
            }
            else if(event instanceof RefreshDisplayEvent && model != null) {
                _tree.validate();
            }
            else if(event instanceof NewElementEvent && 
                    (event instanceof NewProjectEvent == false) &&
                    model != null) {
                ACSElement element = ((NewElementEvent)event).getNewElement();
                model.fireNodeAdded(element);
                TreePath path = new TreePath(model.getPathToRoot(element));
                _selections.setSelectionPath(path);
                _tree.scrollPathToVisible(path);
            }
            else if(event instanceof DeleteElementEvent && model != null) {
                ACSElement element = ((DeleteElementEvent)event).getDeletedElement();
                model.fireNodeDeleted(element);
                _tree.updateUI();
           }
            else {
                ACSProjectElement project = null;
                if(event instanceof ProjectSelectedEvent) {
                    ProjectSelectedEvent e = (ProjectSelectedEvent) event;
                    project = e.getSelectedProject();
                }
                
                if(project == null) {
                    // The project has been closed.
                    // XXX this needs to be tested against 
                    // different version of Swing...
                    _tree.setModel(null);
                    _tree.setSelectionModel(null);
                    // Send an empty selection event to notify others that
                    // nothing is selected.
                    ElementSelectionEvent.createEvent(getContext(), null);
                }
                else {
                    boolean updateModel = false;
                    TreeModel testModel = _tree.getModel();
                    
                    // Set the model if's not an ElementTreeModel
                    if (testModel instanceof ElementTreeModel) {
                        ElementTreeModel etm = (ElementTreeModel) testModel;
                        ACSProjectElement currentProject = 
                            (ACSProjectElement) etm.getRoot();
                        
                        // Set the model if the project is wrong
                        if (currentProject != project) {
                            updateModel = true;
                        }
                    } else {
                        updateModel = true;
                    }

                    // Should we update the tree model
                    if (updateModel) {
                        _tree.setModel(new ElementTreeModel(project));
                        _tree.getModel().addTreeModelListener(
                            new ChangeHandler());

                        _selections = new ElementTreeSelectionModel();
                        _selections.addTreeSelectionListener(
                            new SelectionForwarder());
                        _tree.setSelectionModel(_selections);
                    }
                }
            }
            return true;
        }
    }

    /** Forwards selection events to the event bus. */
    private class SelectionForwarder implements TreeSelectionListener {
        public void valueChanged(TreeSelectionEvent e) {
            getContext().getEventBus().postEvent(
                ElementSelectionEvent.createEvent(
                    getContext(), _selections.getSelectedElements()));
        }
    }

    /** Class providing filtering for project events. */
    private static class Filter implements BusFilter {
        /** 
         * Determines if the given event should be accepted.
         * 
         * @param event Event to test.
         * @return True if event should be given to BusMember, false otherwise.
         */
        public boolean accept(EventObject event) {
            return event instanceof ProjectSelectedEvent ||
                event instanceof ProjectClosedEvent ||
                event instanceof NewElementEvent ||
                event instanceof PropertyChangeEvent ||
                event instanceof DeleteElementEvent ||
                event instanceof RefreshDisplayEvent;
        }
    }

    /** Mouse listener for showing popup menu. */
    private class PopupHandler extends MouseAdapter {
        private void handle(MouseEvent e) {
            if(e.isPopupTrigger()) {
                Object source = e.getSource();
                String[] menuStr = null;
                JTree tree = (JTree) source;
                
                // Find the selected path.
                TreePath selPath = tree.getPathForLocation(
                    e.getX(), e.getY());
                if (selPath == null) {
                    return;
                }

                // Update the selection. 
                tree.setSelectionPath(selPath);

                // Find the selected object.
                Object selObj = selPath.getLastPathComponent();

                String defaultID = null;
                
                // Does the item provide its own menu?
                if (selObj instanceof ACSInfoProvider) {
                    ACSInfoProvider ip = (ACSInfoProvider) selObj;
                    menuStr = ip.getMenuString();
                    defaultID = ip.getDefaultActionID();
                } else {
                    // Get the menu from the prop file.
                    menuStr = getContext().getResources().getStringArray(
                        ElementNavigator.class, defaultID);
                }

                // Should we create a menu?
                if (menuStr != null && menuStr.length != 0) {
                    ActionManager mgr = getContext().getActions();
                    JPopupMenu menu = mgr.createPopup(menuStr, defaultID);
                    menu.show((JComponent)e.getSource(), e.getX(), e.getY());
                }
            }
        }

        public void mousePressed(MouseEvent e) {
            handle(e);
        }
        public void mouseReleased(MouseEvent e) {
            handle(e);
        }
        public void mouseClicked(MouseEvent e) {
            handle(e);
        }
    }
    
    /** Class for handling drag operation. */
    private class DragHandler implements DragTreeListener {
        
        /**
         * Append the object to the end of the parent's child list.
         */
        public Object appendChild(Object parent, Object newChild) {
            
            ACSTreeNodeElement parentNode =
                (ACSTreeNodeElement) parent;
            ACSTreeNodeElement newChildNode =
                (ACSTreeNodeElement) newChild;
            ACSTreeNodeElement clone =
                (ACSTreeNodeElement) newChildNode.cloneNode(true);
            parentNode.appendChild(clone);
            
            ElementTreeModel model = (ElementTreeModel)_tree.getModel();
            model.fireNodeChanged(parentNode);
            
            return clone;
        }

        /**
         * Append the object to the end of the parent's child list.
         */
        public Object insertBefore(Object parent, Object index, Object newChild) {
            
            ACSTreeNodeElement parentNode =
                (ACSTreeNodeElement) parent;
            ACSTreeNodeElement indexNode =
                (ACSTreeNodeElement) index;
            ACSTreeNodeElement newChildNode =
                (ACSTreeNodeElement) newChild;
            ACSTreeNodeElement clone =
                (ACSTreeNodeElement) newChildNode.cloneNode(true);
            parentNode.insertBefore(clone, indexNode);
            
            ElementTreeModel model = (ElementTreeModel)_tree.getModel();
            model.fireNodeChanged(parentNode);
            
            return clone;
        }
    
        /**
         * Append the object to the end of the parent's child list.
         */
        public void removeChild(Object child) {
            
            ACSTreeNodeElement childNode =
                (ACSTreeNodeElement) child;
            ACSTreeNodeElement parentNode =
                (ACSTreeNodeElement) childNode.getParentNode();
            parentNode.removeChild(childNode);
            
            ElementTreeModel model = (ElementTreeModel)_tree.getModel();
            model.fireNodeChanged(childNode);
        }
    }
   
    /** class which handles sending events when the tree changes */
    private class ChangeHandler implements TreeModelListener {

        public void treeNodesInserted(TreeModelEvent e) {
            getContext().getEventBus().postEvent(
                ElementChangedEvent.createEvent(getContext(), null));
        }
        
        public void treeNodesChanged(TreeModelEvent e) {
            getContext().getEventBus().postEvent(
                ElementChangedEvent.createEvent(getContext(), null));
        }
        
        public void treeStructureChanged(TreeModelEvent e) {
            getContext().getEventBus().postEvent(
                ElementChangedEvent.createEvent(getContext(), null));
        }
        
        public void treeNodesRemoved(TreeModelEvent e) {
            getContext().getEventBus().postEvent(
                ElementChangedEvent.createEvent(getContext(), null));
        }
    }
}
