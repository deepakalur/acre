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

package org.acre.lang.application;

import org.acre.lang.TreeBuilder;
import org.acre.lang.analysis.*;
import org.acre.lang.node.*;
import org.acre.lang.tool.Version;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;

public class ASTDisplay extends DepthFirstAdapter {

    private Stack parents = new Stack();

    public ASTDisplay() {
    }

    public void outStart(Start node) {
        JFrame frame = new JFrame("AST Displayer for " + Version.NAME + " ("
                + Version.FULLNAME + ")  Version " + Version.VERSION);
        JTree tree = new JTree((DefaultMutableTreeNode) parents.pop());
        JScrollPane pane = new JScrollPane(tree);

        expandAll(tree);

        /* window listener so the program will die */
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        frame.setSize(550, 800);
        frame.getContentPane().add(pane);
        frame.setVisible(true);
    }

    /*
     * As we come across non terminals, push them onto the stack
     */
    public void defaultIn(Node node) {
        DefaultMutableTreeNode thisNode = new DefaultMutableTreeNode
                (node.getClass().getName().substring(node.getClass().getName().lastIndexOf('.') + 1));
        parents.push(thisNode);
    }

    /*
     * As we leave a non terminal, it's parent is the node before it
     * on the stack, so we pop it off and add it to that node
     */
    public void defaultOut(Node node) {
        DefaultMutableTreeNode thisNode = (DefaultMutableTreeNode) parents.pop();
        ((DefaultMutableTreeNode) parents.peek()).add(thisNode);
    }

    /*
     * Terminals - our parent is always on the top of the stack, so we
     * add ourselves to it
     */
    public void defaultCase(Node node) {
        DefaultMutableTreeNode thisNode = new
                DefaultMutableTreeNode(((Token) node).getText());
        ((DefaultMutableTreeNode) parents.peek()).add(thisNode);
    }

    public void caseEOF(EOF node) {
    }

    /*
     * we want to see the whole tree. These functions expand it for
     * us, they are written by Christian Kaufhold and taken from the
     * comp.lang.jave.help newsgroup
     */
    public static void expandAll(JTree tree) {
        Object root = tree.getModel().getRoot();

        if (root != null)
            expandAll(tree, new TreePath(root));
    }


    public static void expandAll(JTree tree, TreePath path) {
        for (Iterator i = extremalPaths(tree.getModel(), path,
                new HashSet()).iterator();
             i.hasNext();)
            tree.expandPath((TreePath) i.next());
    }

    /**
     * The "extremal paths" of the tree model's subtree starting at
     * path. The extremal paths are those paths that a) are non-leaves
     * and b) have only leaf children, if any. It suffices to know
     * these to know all non-leaf paths in the subtree, and those are
     * the ones that matter for expansion (since the concept of expan-
     * sion only applies to non-leaves).
     * The extremal paths of a leaves is an empty collection.
     * This method uses the usual collection filling idiom, i.e.
     * clear and then fill the collection that it is given, and
     * for convenience return it. The extremal paths are stored
     * in the order in which they appear in pre-order in the
     * tree model.
     */
    public static Collection extremalPaths(TreeModel data,
                                           TreePath path,
                                           Collection result) {
        result.clear();

        if (data.isLeaf(path.getLastPathComponent())) {
            return result; // should really be forbidden (?)
        }

        extremalPathsImpl(data, path, result);

        return result;
    }

    private static void extremalPathsImpl(TreeModel data,
                                          TreePath path,
                                          Collection result) {
        Object node = path.getLastPathComponent();

        boolean hasNonLeafChildren = false;

        int count = data.getChildCount(node);

        for (int i = 0; i < count; i++)
            if (!data.isLeaf(data.getChild(node, i)))
                hasNonLeafChildren = true;

        if (!hasNonLeafChildren)
            result.add(path);
        else {
            for (int i = 0; i < count; i++) {
                Object child = data.getChild(node, i);

                if (!data.isLeaf(child))
                    extremalPathsImpl(data,
                            path.pathByAddingChild(child),
                            result);
            }
        }
    }

    public static void main(String[] arguments) {

        System.out.println(Version.banner());

        String fname = null;
        if (arguments.length == 1) {
            fname = arguments[0];
        } else {
            System.out.println("usage:");
            System.out.println("  java ASTDisplay filename");
            System.exit(1);
        }

        try {
            java.io.LineNumberReader lineReader = new java.io.LineNumberReader((new java.io.FileReader(fname)));
            String line = null;
            System.out.println("====================== Input File: =======================");
            do {
                line = lineReader.readLine();
                if (null != line) System.out.println(line);
            } while (line != null);
            System.out.println("==========================================================");

            Node ast = TreeBuilder.getNode(new FileReader(fname), true);//simple tree
            ASTDisplay display = new ASTDisplay();
            if (ast != null) {
                ast.apply(display);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
    }
}



