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
import org.acre.pdmengine.PatternEngineEventListener;
import org.acre.pdmengine.PatternEngineException;
import org.acre.pdmengine.SearchContext;
import org.acre.pdmengine.model.Artifact;
import org.acre.pdmengine.model.ArtifactLink;
import org.acre.pdmengine.model.PatternResult;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: johncrupi
 * Date: Jan 17, 2005
 * Time: 1:59:05 PM
 * To change this template use File | Settings | File Templates.
 */
public interface AcreServer extends Remote {
    public PatternResult execute(UserContextObject user, PDMType pdm) throws RemoteException, PatternEngineException ;

    public PatternResult execute(UserContextObject user, String patternName) throws RemoteException, PatternEngineException ;

    public PatternResult[] execute(UserContextObject user, String patternName, Map params, SearchContext ctx)
                throws RemoteException;

    public PatternModelService getPatternModelService(UserContextObject user ) throws RemoteException;

    public PatternQueryModelService getPatternQueryModelService(UserContextObject user ) throws RemoteException;

    public void setPatternQueryRepository(UserContextObject user, PatternQueryModelService patternQueryModelService) throws RemoteException;



    public void refresh(UserContextObject user, String patternName) throws RemoteException;

    public void refreshAll(UserContextObject user ) throws RemoteException;

    public ArtifactLink[] findRelcos(UserContextObject user, String patternName, String roleName, String relationship) throws RemoteException;

    public Artifact[] findRelcos(UserContextObject user, String patternName, String roleName, String relationship, String artifactName) throws RemoteException;

    public ArtifactLink[] findInvRelcos(UserContextObject user, String patternName, String roleName, String relationship) throws RemoteException;

    public Artifact[] findInvRelcos(UserContextObject user, String patternName, String roleName, String relationship, String artifactName) throws RemoteException;

    // find patterns containing the given artifact
    public String[] findPatterns(UserContextObject user, String orphanArtifact) throws RemoteException;

    // find patterns each containing all the given artifacts
    public String[] findPatterns(UserContextObject user, String[] orphanArtifacts) throws RemoteException;

    // List<RelationshipResult>
    public List discoverRelationships(UserContextObject user, String patterns[], String relationshiptypes[]) throws RemoteException;

    public PatternResult discoverPDM(UserContextObject user, String dynamicPatternName, String pdms[], String relationshipTypes[]) throws RemoteException;

    //List<RelationshipResult>
    public List discoverRoleRelationships(UserContextObject user, String pattern, String relationshipTypes[]) throws RemoteException;

    // loads all patterns for global + latest version
    void loadAllPatterns(UserContextObject user, boolean async, PatternEngineEventListener listener) throws RemoteException ;

    // loads all patterns for specific search context
    void loadAllPatterns(UserContextObject user, SearchContext context, boolean async, PatternEngineEventListener listener)
            throws RemoteException ;

    void shutdown(UserContextObject user) throws RemoteException ;


    public Snapshots getSnapshots(UserContextObject user, Date start, Date end ) throws RemoteException;

    public Snapshots getQuerySnapshots(UserContextObject user, SnapshotQuery q) throws RemoteException;


}

