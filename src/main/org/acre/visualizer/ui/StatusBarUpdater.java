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

import org.acre.config.ConfigService;

import java.io.File;

/**
 * @author Deepak.Alur@Sun.com
 * @version Nov 13, 2004 4:24:23 PM
 */
public class StatusBarUpdater implements Runnable {

    long logLastModified=-1;
    private long logLastLength=-1;
    private boolean keepRunning=true;
    private boolean logViewed=false;

    private StatusPanel statusPanel;

    public StatusBarUpdater(StatusPanel statusPanel) {
        this.statusPanel = statusPanel;
    }

    public boolean isLogViewed() {
        return logViewed;
    }

    public void setLogViewed(boolean logViewed) {
        this.logViewed = logViewed;
    }

    public boolean isKeepRunning() {
        return keepRunning;
    }

    public void setKeepRunning(boolean keepRunning) {
        this.keepRunning = keepRunning;
    }

    private boolean isLogFileUpdated() {
        File f;
        f = new File(ConfigService.getInstance().getConfigData().getLogFilePath());
        if (f==null) return false;

        //System.out.println(f.lastModified() + " old = " + logLastModified);
        long currentModified = f.lastModified();
        if ((currentModified > logLastModified) ||
            (f.length() > logLastLength)) {
                logLastModified = currentModified;
                logLastLength = f.length();
                setLogViewed(false);
                return true;
        }

        return false;
    }

    private void initLastModifiedTime() {
        File f;
        f = new File(ConfigService.getInstance().getConfigData().getLogFilePath());
        logLastModified = f.lastModified();
        logLastLength = f.length();
        return;
    }

    public void run() {
        while (keepRunning) {
            if (logLastModified ==-1) {
                initLastModifiedTime();
            } else
            if (isLogFileUpdated()) {
                initLastModifiedTime();
                ConfigService.getInstance().setLogHasNewMessages(true);

                if (statusPanel != null) {
                    if (!isLogViewed()) {
                        statusPanel.setLogUpdated();
                    }
                }
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String args[]) {
        StatusBarUpdater u = new StatusBarUpdater(null);
        Thread t = new Thread(u);
        t.start();
    }

}
