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
package org.acre.lang;

import org.acre.lang.analysis.ReversedDepthFirstAdapter;
import org.acre.lang.node.EOF;
import org.acre.lang.node.Node;
import org.acre.lang.node.Start;
import org.acre.lang.node.Token;
import org.acre.lang.tool.Version;

import java.io.FileReader;

/**
 * Computes line/column numbers of a given AST node
 *
 * @author Yury Kamen
 */
public class LineMapper extends ReversedDepthFirstAdapter {

    private Token firstToken = null;
    private Token lastToken = null;

    public static void main(String[] arguments) {
        System.out.println(Version.banner());
        String fname = null;
        if (arguments.length == 1) {
            fname = arguments[0];
        } else {
            System.out.println("usage:");
            System.out.println("  java LineMapper filename");
            System.exit(1);
        }

        try {
            java.io.LineNumberReader lineReader = new java.io.LineNumberReader((new FileReader(fname)));
            String line = null;
            System.out.println("====================== Input File: " + fname);
            System.out.println("|\t|1234567890123456789012345678901234567890123456789012345678901234567890");
            System.out.println("|\t|1--------2---------3---------4---------5---------6---------7---------8");
            int i = 1;
            do {
                line = lineReader.readLine();
                if (null != line) System.out.println("|" + i + "\t|" + line);
                i += 1;
            } while (line != null);
            System.out.println("====================== Line mapper results ===============");

            Node node = TreeBuilder.getNode(new FileReader(fname), true);//simple tree
            LineMapper mapper = new LineMapper(node);
            System.out.println("First Line     = " + mapper.getFirstToken().getLine());
            System.out.println("First Column   = " + mapper.getFirstToken().getPos());
            System.out.println("First Terminal = " + mapper.getFirstToken().getText());
            System.out.println("Last Line      = " + mapper.getLastToken().getLine());
            System.out.println("Last Column    = " + mapper.getLastToken().getPos());
            System.out.println("Last Terminal  = " + mapper.getLastToken().getText());

            System.out.println("==========================================================");
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
    }

    public LineMapper(Node node) {
        node.apply(this);
    }

    public Token getFirstToken() {
        return firstToken;
    }

    public Token getLastToken() {
        return lastToken;
    }

    /*
     * Visit terminal
     */
    public void defaultCase(Node node) {
        if (null == lastToken && !(node instanceof EOF)) {
            lastToken = (Token) node;
        }
        firstToken = (Token) node;
    }

//
//    /*
//     * After we visited all nodes
//     */
//    public void outStart(Start node) {
//        // Nothing to do
//    }
//
//
//    /*
//     * Visit each non-terminal node. The next node we visit will always be the last
//     * child of this node.
//     */
//    public void defaultIn(Node node) {
//        // Nothing to do
//    }
//
//    /*
//     * Leave a non-terminal node
//     */
//    public void defaultOut(Node node) {
//        // Nothing to do
//    }
//
//    /**
//     * Visit end of file
//     *
//     * @param node
//     */
//    public void caseEOF(EOF node) {
//        // Nothing to do
//    }
}








