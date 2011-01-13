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
package org.acre.lang.analyser;

import org.acre.lang.LineMapper;
import org.acre.lang.node.Node;

/**
 * Exeception thrown while semantic analysis of queries
 * 
 * @author Syed Ali
 */
public class AnalyserException extends RuntimeException {

    private Node node = null;
    public AnalyserException(String message)
    {
        super(message);
    }

    public AnalyserException(String message, Node node)
    {
        super(message);
        this.node = node;
    }
    public String toString() {
        String s = super.toString();
        if(null == node) {
            return s;
        }
        LineMapper mapper = new LineMapper(node);
        return s + " [line=" + mapper.getFirstToken().getLine() + ", column=" + mapper.getFirstToken().getPos() + "]";
    }
    public Node getNode() {
        return node;
    }

    protected void setNode(Node value) {
        if (node == null) {
            this.node = value;
            
        }
    }
}
