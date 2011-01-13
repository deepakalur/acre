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
package org.acre.visualizer.ui.config;

import org.acre.config.ConfigService;

import javax.swing.JFrame;

/**
 * @author Deepak.Alur@Sun.com
 * @version Feb 16, 2006 9:23:56 PM
 */
public class Main {
    public static void main(String args[]) {
        ConfigurationPanel form = new ConfigurationPanel ();
        ConfigService config = ConfigService.getInstance();
        form.setConfigService(config);
        JFrame frame;
        frame = new JFrame("Configuration Panel");
        frame.getContentPane().add(form);
        frame.pack();
        frame.setVisible(true);
    }
}
