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
package org.acre.visualizer.graphimpl.jgraph.pdmresult;

import org.acre.visualizer.graphimpl.jgraph.AcreGraphImpl;
import org.jgraph.graph.GraphModel;

import javax.swing.ToolTipManager;
import java.awt.Dimension;

/**
 * @author Deepak.Alur@Sun.com
 * @version Dec 21, 2004 3:45:23 PM
 */
public class PDMResultGraphImpl extends AcreGraphImpl {

    public PDMResultGraphImpl() {
        super();
        initialize();
    }

    public PDMResultGraphImpl(PDMResultGraphModelImpl model) {
        super((GraphModel) model);
    }

    public void setModel(PDMResultGraphModelImpl model) {
        this.setModel((GraphModel)model);
    }

//    public void setModel(GraphModel model) {
//        if (model == null) {
//            super.setModel(model);
//            return;
//        }
//
//        if (model instanceof SalsaPDMResultJGraphModel)
//            super.setModel(model);
//        else
//            throw new AcreGraphException(
//                    "Invalid Graph Model type '"+
//                    model.getClass().getName() +
//                    "'Supplied to Graph :" +
//                    this);
//    }


    protected void initialize() {

        // set Marquee Handler

        // set Default Layout
        setLayoutType(SUGIYAMA_LAYOUT);

        // set Default Properties
        setSizeable(true);
        setEditable(false);
        setPortsVisible(false);
        // JGraph 5.2 setSelectClonedCells(true);
        setSelectionEnabled(true); // JGraph 5.3
        getGraphLayoutCache().setSelectsAllInsertedCells(true);        setMoveable(true);
        setGridSize(10.0d);
        setGridVisible(true);
        setEditClickCount(2);
        setMinimumSize(new Dimension(750, 750));

        // register cell view factory
        // getGraphLayoutCache().setFactory(new SalsaCellViewFactory());

        // add model listener
        //getModel().addGraphModelListener(new ModelListener());

        // add a view observer to the view
        // getGraphLayoutCache().addObserver(new SalsaViewObserver());

        // register tool tip manager
        ToolTipManager.sharedInstance().registerComponent(this);
    }
}
