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
package org.acre.visualizer.grappa;

import att.grappa.*;
/**
 * Created by IntelliJ IDEA.
 * User: johncrupi
 * Date: Oct 2, 2004
 * Time: 3:14:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class GrappaAdapter extends GrappaAdapter {
    public void grappaClicked(Subgraph subg, Element elem, GrappaPoint pt, int modifiers, int clickCount, GrappaPanel panel) {
        super.grappaClicked(subg, elem, pt, modifiers, clickCount, panel);

//        if (elem != null)
//            System.out.println("Got Grappa Click...: " + elem.getName() + " " + elem.getIdKey() + "\n");
    }

    public void grappaReleased(Subgraph subg, Element elem, GrappaPoint pt, int modifiers, Element pressedElem, GrappaPoint pressedPt, int pressedModifiers, GrappaBox outline, GrappaPanel panel) {
        super.grappaReleased(subg, elem, pt, modifiers, pressedElem, pressedPt, pressedModifiers, outline, panel);

//        if (elem != null)
//            System.out.println("Got Grappa Release...: " + elem.getName() + " " + elem.getIdKey());
    }
}