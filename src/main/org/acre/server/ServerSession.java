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
package org.acre.server;

import org.acre.analytics.PatternAnalyticsEngine;
import org.acre.analytics.AcreContainer;
import org.acre.analytics.AcreContainerFactory;
import org.acre.config.ConfigService;
import org.acre.pdmengine.PatternEngine;

import java.util.*;
import java.util.logging.Logger;

/**
 * @author rajmohan@Sun.com
 */
class ServerSession {
    static Map<UserContextObject, UserSessionData> userSessions =
            Collections.synchronizedMap(new HashMap<UserContextObject, UserSessionData>());

    static Logger logger = ConfigService.getInstance().getLogger(ServerSession.class);

    static final int SESSION_TIMEOUT = 10 * 60 * 1000; // 10 minutes
    static final int POLLING_INTERVAL =5 * 60 * 1000; // 5 minutes

    public ServerSession() {
        Thread sessionMonitor = new Thread(new SessionMonitor(POLLING_INTERVAL));
        sessionMonitor.setDaemon(true);
        sessionMonitor.setPriority(Thread.MIN_PRIORITY);
        sessionMonitor.start();
    }


    public PatternEngine getPatternEngine( UserContextObject user ) {
        UserSessionData userSessionData = getUserSession(user);

        return userSessionData.getPatternEngine();
    }

    public PatternAnalyticsEngine getAnalyticsEngine( UserContextObject user ) {
        UserSessionData userSessionData = getUserSession(user);

        return userSessionData.getAnalyticsEngine();
    }


    public UserSessionData getUserSession(UserContextObject userContext) {
        logger.info("Accessing User Session : " + userContext);
        UserSessionData obj = userSessions.get(userContext);
        if ( obj == null ) {
            // create a User session for this User.
            AcreContainerFactory factory = AcreContainerFactory.getInstance();
            AcreContainer acreContainer = factory.getAcreContainer();
            obj = new UserSessionData(userContext, acreContainer);
            setUserSession(userContext, obj);
        }

        if ( obj != null )
            obj.touch();
        return obj;
    }

    public void setUserSession(UserContextObject userContext, UserSessionData userSessionData) {
        logger.info("Setting User Session : " + userContext);
        userSessions.put(userContext, userSessionData);
    }

    class SessionMonitor implements Runnable {
        int pollTime;
        public SessionMonitor(int pollTime) {
            this.pollTime = pollTime;
        }

        public void run() {
           while ( true ) {
               try {
                   if ( ! userSessions.isEmpty() ) {

                       synchronized(userSessions) {
                           logger.info("Session Monitor Thread: Checking for Stale sessions ");
                           List <UserContextObject>staleSessions = new ArrayList<UserContextObject>();

                           Iterator<Map.Entry<UserContextObject, UserSessionData>> entries =
                                    userSessions.entrySet().iterator();
                           while ( entries.hasNext() ) {
                               Map.Entry<UserContextObject, UserSessionData> entry = entries.next();
                               UserSessionData userSessionData = (UserSessionData)entry.getValue();
                               long current = System.currentTimeMillis();
                               if ( current - userSessionData.getLastaccessed() > SESSION_TIMEOUT ) {
                                    staleSessions.add(entry.getKey());
                               }
                           }

                           Iterator<UserContextObject> staleItr = staleSessions.iterator();
                           while (staleItr.hasNext()) {
                               UserContextObject userContext = staleItr.next();
                               logger.info("Removing Stale Session : " + userContext);
                               UserSessionData data = ServerSession.this.getUserSession(userContext);
                               data.cleanup();
                               userSessions.remove(userContext);
                           }
                       }
                   }
                   Thread.sleep(pollTime);
               } catch (InterruptedException e) {
                   logger.severe("Internal Error in Session Monitoring Thread : "
                        + e.getMessage());
               }
           }
        }
    }
}
