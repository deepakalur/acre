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

package org.acre.lang.tool;

import org.acre.lang.node.*;

import javax.swing.JFrame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PushbackReader;


/**
 * Prints the tree of the given AST on standard output.
 * This is a toy class which demonstrates the use of PrintWalker adapter
 * for traversing the Abstract Syntax Tree and printing each node.
 *@author Mariusz Nowostawski
 */
public class PrintTree {

    public static void main(String[] arguments) {

        System.out.print(Version.banner());

        if(arguments.length < 1 || arguments[0].equals("-h") || arguments[0].equals("--help")) {
          System.out.println("Usage:");
          System.out.println("  java org.acre.lang.tool.PrintTree [-g|--gui] filename");
          System.exit(1);
        }

       String input;
       if(arguments.length == 2) input = arguments[1];
       else input = arguments[0];

        try {
            Node ast = TreeBuilder.getNode(new PushbackReader(
                new BufferedReader(
                new FileReader(input)), 1024));

            if(arguments[0].equals("--gui") || arguments[0].equals("-g")) {
               DisplayTree display = new DisplayTree();
               ast.apply(display);
               JFrame frame = display.getTreeFrame();
               frame.addWindowListener(new WindowAdapter(){
                 public void windowClosing(WindowEvent e){
                    System.exit(0);
                 }
               });
               frame.setVisible(true);
            } else {
              ast.apply(new PrintWalker());
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
