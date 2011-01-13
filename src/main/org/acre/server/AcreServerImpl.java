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

import org.acre.pdm.PDMType;
import org.acre.analytics.SnapshotQuery;
import org.acre.analytics.Snapshots;
import org.acre.config.ConfigService;
import org.acre.dao.PatternQueryRepository;
import org.acre.dao.PatternRepository;
import org.acre.pdmengine.PatternEngine;
import org.acre.pdmengine.PatternEngineEventListener;
import org.acre.pdmengine.PatternEngineException;
import org.acre.pdmengine.SearchContext;
import org.acre.pdmengine.model.Artifact;
import org.acre.pdmengine.model.ArtifactLink;
import org.acre.pdmengine.model.PatternResult;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: johncrupi
 * Date: Jan 17, 2005
 * Time: 1:59:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class AcreServerImpl extends UnicastRemoteObject implements AcreServer {

    static final int SERVER_PORT = 19007;
    static Logger logger = ConfigService.getInstance().getLogger(AcreServerImpl.class);

    static ServerSession session = new ServerSession();

    public AcreServerImpl() throws RemoteException {
        super(SERVER_PORT);
        try {
            String hostname = "localhost";
            try {
                hostname = InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException e) {
                logger.info("Error getting hostname : " + e.getMessage());
            }
            Naming.rebind("//" + hostname + "/AcreServerImpl", this);

        } catch (MalformedURLException e) {
            String error = "Error starting ACRE Server : " + e.getMessage();
            logger.severe(error);
            throw new RemoteException(error, e);
        }
    }

    public static void main(String[] argv) {
	    try {
		    new AcreServerImpl();
		    logger.info("AcreServer started...");
	    } catch (Exception e) {
            logger.severe("Error starting AcreServer : " + e.getMessage());
	    }
    }

    private PatternEngine getPatternEngine(UserContextObject user) {
            return session.getPatternEngine(user);

    }
    public PatternResult execute(UserContextObject user, String pattern) {
        return getPatternEngine(user).execute(pattern);
    }

    public PatternResult execute(UserContextObject user, PDMType pdm) throws PatternEngineException {
        return getPatternEngine(user).execute(pdm);
    }

    public PatternResult[] execute(UserContextObject user, String patternName, Map params, SearchContext ctx) {
        return getPatternEngine(user).execute(patternName, params, ctx);
    }

    public PatternRepository getPatternRepository(UserContextObject user ) {
        return getPatternEngine(user).getPatternRepository();
    }

    public void setPatternRepository(UserContextObject user, PatternRepository patternRepository) {
        getPatternEngine(user).setPatternRepository(patternRepository );
    }

    public PatternQueryRepository getPatternQueryRepository(UserContextObject user ) {
        return getPatternEngine(user).getPatternQueryRepository();
    }

    public void setPatternQueryRepository(UserContextObject user, PatternQueryRepository patternQueryRepository) {
        getPatternEngine(user).setPatternQueryRepository(patternQueryRepository);
    }

    public void refresh(UserContextObject user, String patternName) {
        getPatternEngine(user).refresh( patternName );
    }

    public void refreshAll(UserContextObject user ) {
        getPatternEngine(user).refreshAll();
    }

    public ArtifactLink[] findRelcos(UserContextObject user, String patternName, String roleName, String relationship) {
        return getPatternEngine(user).findRelcos( patternName, roleName, relationship);
    }

    public Artifact[] findRelcos(UserContextObject user, String patternName, String roleName, String relationship, String artifactName) {
        return getPatternEngine(user).findRelcos(patternName, roleName, relationship, artifactName);
    }

    public ArtifactLink[] findInvRelcos(UserContextObject user, String patternName, String roleName, String relationship) {
        return getPatternEngine(user).findInvRelcos(patternName, roleName, relationship);
    }

    public Artifact[] findInvRelcos(UserContextObject user, String patternName, String roleName, String relationship, String artifactName) {
        return getPatternEngine(user).findInvRelcos(patternName, roleName, relationship, artifactName);
    }

    // find PDMs containing the given artifact
    public String[] findPatterns(UserContextObject user, String orphanArtifact) {
        return getPatternEngine(user).findPatterns(null, orphanArtifact);
    }

    // find PDMs, each containing all the given artifacts
    public String[] findPatterns(UserContextObject user, String[] orphanArtifacts) {
        return getPatternEngine(user).findPatterns(null, orphanArtifacts);
    }

    // List<RelationshipResult>
    public List discoverRelationships(UserContextObject user, String patterns[], String relationshiptypes[]) {
        return getPatternEngine(user).discoverRelationships(patterns, relationshiptypes);
    }

    public PatternResult discoverPDM(UserContextObject user, String dynamicPatternName, String pdms[], String relationshipTypes[]) {
        return getPatternEngine(user).discoverPattern(dynamicPatternName, pdms, relationshipTypes);
    }

    //List<RelationshipResult>
    public List discoverRoleRelationships(UserContextObject user, String pattern, String relationshipTypes[]) {
        return getPatternEngine(user).discoverRoleRelationships(pattern, relationshipTypes);
    }

    // loads all patterns for global + latest version
    public void loadAllPatterns(UserContextObject user, boolean async, PatternEngineEventListener listener) {
        loadAllPatterns(user, null, async, listener);
    }

    // loads all patterns for specific search context
    public void loadAllPatterns(UserContextObject user, SearchContext context, boolean async, PatternEngineEventListener listener) {
        getPatternEngine(user).loadAllPatterns(context, async, listener);
    }

    public void shutdown(UserContextObject user) {
        getPatternEngine(user).shutdown();
    }

    public PatternModelService getPatternModelService(UserContextObject user ) throws RemoteException{
        System.out.println( "AcreServer.getPatternRepositoryService called" );
        return new PatternModelServiceImpl();
    }

    public org.acre.server.PatternQueryModelService getPatternQueryModelService(UserContextObject user )throws RemoteException{
        System.out.println( "AcreServer.getPatternQueryModelService called" );
        return new PatternQueryModelServiceImpl();
    }

    public void setPatternQueryRepository(UserContextObject user, PatternQueryModelService patternQueryModelService) throws RemoteException {

        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void loadAllPatterns(UserContextObject user) {
        getPatternEngine(user).loadAllPatterns( true, null );
    }


    public Snapshots getSnapshots(UserContextObject user, Date start, Date end ){
        return session.getAnalyticsEngine(user).getSnapshots( start, end );
    }

    public Snapshots getQuerySnapshots(UserContextObject user, SnapshotQuery q) throws RemoteException {
        return session.getAnalyticsEngine(user).getSnapshots(q);
    }


}

