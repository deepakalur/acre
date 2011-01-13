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

/**
 * @author Deepak.Alur@Sun.com
 *         Date: Jun 23, 2004
 *         Time: 5:04:39 PM
 */
public class GlobalSettings {
    // todo - read from properties file
    private static int DIVIDER_SIZE = 7;
    private static final int VERTICAL_DIVIDER_LOCATION = 400;
    private static final int HORIZONTAL_DIVIDER_LOCATION = 200;

    public static int getDividerSize() {
        return DIVIDER_SIZE;
    }

    public static void setDividerSize(int dividerSize) {
        DIVIDER_SIZE= dividerSize;
    }

    public static int getVerticalDividerLocation() {
        return VERTICAL_DIVIDER_LOCATION;
    }

    public static int getHorizontalDividerLocation() {
        return HORIZONTAL_DIVIDER_LOCATION;
    }
}
