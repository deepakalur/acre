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
package org.acre.visualizer.ui;

import org.acre.lang.pql.pdbc.PQLArtifact;
import org.acre.visualizer.ui.components.Editor;
import org.acre.visualizer.ui.config.ConfigurationPanel;

/**
 * Abstracts SALSA MDI Frame interface
 *
 * @author Yury Kamen
 */
public interface MainMDIFrameInterface {

    public void openFile(String name);

    public void setSettingsTitle(String title);

    public String getSettingsTitle();

    public void showSettings();

    public void showLogViewer();

    public void showError(String message, String title);

    public boolean confirmOperation(String message, String title);

    public void showInfo(String message, String title);

    public void showBusyProgressBar(String message);

    public void hideBusyProgressBar();

    public ConfigurationPanel getConfigPanel();

     public void showStatus(final String message, boolean wait);

    public void showStatus(final String message);

    public void runGC();

    void openArtifact(PQLArtifact pqlData);

    Editor getQueryEditor();

    public void showPQLEditor();
}
