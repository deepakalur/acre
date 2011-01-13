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
package org.acre.visualizer.graph;

import org.acre.visualizer.graph.pdmmodel.PDMModelGraphModel;
import org.acre.visualizer.graph.pdmresult.PDMResultGraphModel;


/**
 * @author Deepak.Alur@Sun.com
 * @version Dec 22, 2004 12:40:44 AM
 */
public class GraphModelFactory {

    public static PDMResultGraphModel createPDMResultGraphModel(int graphType) {
        switch(graphType) {
            case GraphFactoryConstants.JGRAPH:
                try {
                    Class c = Class.forName("org.acre.visualizer.graphimpl.jgraph.pdmresult.PDMResultGraphModelImpl");
                    return (PDMResultGraphModel) c.newInstance();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (IllegalAccessException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (InstantiationException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

            case GraphFactoryConstants.JUNG:
                try {
                    Class c = Class.forName("org.acre.visualizer.graphimpl.jung.pdmresult.PDMResultGraphModelImpl");
                    return (PDMResultGraphModel) c.newInstance();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (IllegalAccessException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (InstantiationException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }


            default:
                break;
        }

        return null;
    }

    public static PDMModelGraphModel createPDMModelGraph(int graphType) {
        switch(graphType) {
            case GraphFactoryConstants.JGRAPH:
                try {
                    Class c = Class.forName("org.acre.visualizer.graphimpl.jgraph.pdmmodel.PDMModelGraphModelImpl");
                    return (PDMModelGraphModel) c.newInstance();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (IllegalAccessException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (InstantiationException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

            case GraphFactoryConstants.JUNG:
                try {
                    Class c = Class.forName("org.acre.visualizer.graphimpl.jung.pdmmodel.PDMModelGraphModelImpl");
                    return (PDMModelGraphModel) c.newInstance();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (IllegalAccessException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (InstantiationException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

            default:
                break;
        }

        return null;
    }
}
