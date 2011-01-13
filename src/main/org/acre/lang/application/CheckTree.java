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
import org.acre.lang.node.Node;
import org.acre.lang.tool.Version;

import java.io.FileReader;

/**
 * Created by IntelliJ IDEA.
 * User: yakov
 * Date: Apr 15, 2004
 * Time: 9:28:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class CheckTree {

    public static void main(String[] arguments) {

        System.out.println(Version.banner());

        if (arguments.length == 0) {
            System.out.println("usage:   java CheckTree filename");
            System.exit(1);
        }

        System.out.println("-------------------- Started  Regression Testing -------------------------");
        for (int i = 0; i < arguments.length; i++) {
         System.out.println("+++++++++++++++++++ Successfully parsed PQL file: " + arguments[i]);
             try {
                Node ast = TreeBuilder.getNode(new FileReader(arguments[i]), true);//simple tree
            } catch (Exception ex) {
                 System.out.println("------------------ Failed to parse PQL file: " + arguments[i]);
                 ex.printStackTrace();
            }
        }
        System.out.println("-------------------- Finished Regression Testing -------------------------");
    }

}
