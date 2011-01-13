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

import org.acre.pdmqueries.QueryType;
import org.acre.analytics.PatternAnalyticsEngine;
import org.acre.analytics.SnapshotQuery;
import org.acre.analytics.Snapshots;
import org.acre.common.AcreException;
import org.acre.dao.DAOFactory;
import org.acre.pdmengine.PatternEngine;
import org.acre.pdmengine.PatternEngineEventListener;
import org.acre.pdmengine.SearchContext;
import org.acre.pdmengine.model.PatternResult;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: johncrupi
 * Date: May 13, 2005
 * Time: 2:14:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class AcreDelegate {
    UserContextObject userContext;
    AcreServer       server;
    PatternModelService      patternModelService;
    PatternQueryModelService patternQueryModelService;

    // Need this as a Config and passed in
    String serverIP = "127.0.0.1";
    
    boolean USE_SERVER = false;
    static ServerSession session = new ServerSession();
                              
    PatternEngine getLocalPatternEngine( UserContextObject user ) {
        return session.getPatternEngine(user);
    }

    PatternAnalyticsEngine getLocalAnalyticsEngine( UserContextObject user ) {
        return session.getAnalyticsEngine(user);
    }

    public AcreDelegate( UserContextObject user ) {
        this.userContext = user;
        setupServer(serverIP);
    }

    public AcreDelegate( String user, String pass, String serverIP ) {
        userContext = new UserContextObject( user, pass );
        setupServer(serverIP);
    }

    synchronized private void setupServer(String serverIP) {
        if( USE_SERVER ) {
            setServer( serverIP );
        try {
	    System.out.print( "AcreDelegate getting PatternModelService...." );
            patternModelService = server.getPatternModelService( userContext );
	    System.out.println( " got it : " + patternModelService );

	    System.out.print( "AcreDelegate getting PatternQueryModelService...." );
            patternQueryModelService = server.getPatternQueryModelService( userContext );
	    System.out.println( " got it : " + patternQueryModelService );

        } catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        }
        else {
            System.out.println( "ACRE is not set to use Server.");
        }
    }

    public AcreDelegate( String user, String pass ) {
        userContext = new UserContextObject( user, pass );
        setupServer( serverIP );
    }
    public void userServer( boolean useServer ) {
        this.USE_SERVER = useServer;
    }
    private void setServer( String serverIP ) {
        try {
            System.out.println( "AcreDelegate: getting server.");
            server = (AcreServer) Naming.lookup("//" + serverIP + "/AcreServerImpl");
            System.out.println( "AcreDelegate: getting server. Done.");
        } catch (NotBoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private boolean useServer() throws AcreException {
        if( USE_SERVER ) {
            if( server == null ) {
                throw new AcreException( "ACRE Server is not available.");
            }
            else {
                return true;
            }
        }
        else {
            return false;
        }
    }
      
    public PatternResult executePattern( String pattern ) throws AcreException {
        PatternResult patternResult = null;
        try {
            if( useServer()) {
                patternResult = server.execute( userContext, pattern );
            }
            else {
                patternResult = getLocalPatternEngine( userContext ).execute(pattern);
            }
        } catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new AcreException( "Unable to execute Pattern");
        }
        return patternResult;
    }

    public void loadAllPatterns( ) throws AcreException {
        try {
            if( useServer() ) {
                server.loadAllPatterns( userContext, true, null );
            }
            else {
                getLocalPatternEngine( userContext ).loadAllPatterns(true, null);
            }
        }
        catch (RemoteException e) {
            e.printStackTrace();
            throw new AcreException( "Unable to load all patterns");
        }
    }

    public List getGlobalPatternModels() throws AcreException {
        List patternModels = null;
        try {
            if( useServer() ) {
                System.out.println( "Making remote PatternModelService call to getGlobalPatternModels...");
                patternModels = patternModelService.getGlobalPatternModels();
            }
           else {
                patternModels = DAOFactory.getPatternRepository().getGlobalPatternModels();
            }
        }
        catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new AcreException( "Unable to get Global Pattern Models");
        }
        return patternModels;
    }

    public List getGlobalPatternQueryModels() throws AcreException {
        List patternQueryModels = null;
        try {
            if( useServer() ) {
                System.out.println( "Making remote PatternQueryModelService call to getGlobalPatternQueryModels...");
                patternQueryModels = patternQueryModelService.getGlobalPatternQueryModels();
            }
           else {
                patternQueryModels = DAOFactory.getPatternQueryRepository().getGlobalQueryList();
            }
        }
        catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new AcreException( "Unable to get Global Pattern Query Models");
        }
        return patternQueryModels;
    }

     public Snapshots getSnapshots(Date start, Date end ) throws AcreException {
         Snapshots snapshots = null;
         try {
             if( useServer() ) {
                 snapshots = server.getSnapshots(userContext, start, end);
             }
             else {
                 snapshots = getLocalAnalyticsEngine(userContext).getSnapshots(start, end);
             }
         }
         catch( RemoteException e ) {
             e.printStackTrace();
             throw new AcreException( "Unable to get Analytics Snapshots");
         }
         return snapshots;
     }

    public Snapshots getQuerySnapshots(SnapshotQuery query) throws AcreException {
         Snapshots snapshots = null;
         try {
             if( useServer() ) {
                 snapshots = server.getQuerySnapshots(userContext, query);
             }
             else {
                 snapshots = getLocalAnalyticsEngine(userContext).getSnapshots(query);
             }
         }
         catch( RemoteException e ) {
             e.printStackTrace();
             throw new AcreException( "Unable to get Analytics Snapshots");
         }
         return snapshots;
     }

    public QueryType getGlobalQuery( String queryName ) throws AcreException {
        QueryType query = null;
        try {
            if( useServer() ) {
                query = patternQueryModelService.getGlobalQuery( queryName );
            }
            else {
                query = DAOFactory.getPatternQueryRepository().getGlobalQuery(queryName);
            }
        }
        catch( RemoteException e ){
            e.printStackTrace();
            throw new AcreException( "Unable to get GlobalQuery");
        }
        return query;
    }

    public void createPatternModelFromQuery(String patternName, String queryName) throws AcreException {
        try {
            if( useServer() ) {
                System.out.println( "Making remote createPatternModelFromQuery call...");
                patternModelService.createPatternModelFromQuery(patternName, queryName);
            }
           else {
                DAOFactory.getPatternRepository().createPatternModelFromQuery(patternName, queryName);
            }
        }
        catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new AcreException( "Unable to get create Pattern Model");
        }
    }

    public void createPatternModelFromQueries(String patternName, QueryType[] selectedQueries)
            throws AcreException {
      try {
            if( useServer() ) {
                System.out.println( "Making remote createPatternModelFromQueries call...");
                patternModelService.createPatternModelFromQueries(patternName, selectedQueries);
            }
           else {
                DAOFactory.getPatternRepository().createPatternModelFromQueries(patternName, selectedQueries);
            }
        }
        catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new AcreException( "Unable to get create Pattern Model Queries");
        }
    }

    public QueryType createNewQuery() throws AcreException {
        QueryType query = null;
        try {
            if( useServer() ) {
                System.out.println( "Making remote createNewQuery call...");
                query = patternQueryModelService.createNewQuery();
            }
            else {
                query = DAOFactory.getPatternQueryRepository().createNewQuery();
            }
        }
        catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new AcreException( "Unable to create new Pattern Query" );
        }
        return query;
    }

    public void saveGlobalScript(QueryType selectedQuery, String text) throws AcreException {

    }

    public String getGlobalQueryFile(String name) throws AcreException {
        String file = null;
        try {
            if( useServer() ) {
                System.out.println( "Making remote getGlobalQueryFile call" );
                file = patternQueryModelService.getGlobalQueryFile( name );
            }
            else {
                file = DAOFactory.getPatternQueryRepository().getGlobalQueryFile( name );
            }
        }
        catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new AcreException( "Unable to get Global Query File" );
        }
        return file;
    }

    public List getGlobalQueryList() throws AcreException {
      List list = null;
        try {
            if( useServer() ) {
                System.out.println( "Making remote getGlobalQueryList call" );
                list = patternQueryModelService.getGlobalQueryList();
            }
            else {
                list = DAOFactory.getPatternQueryRepository().getGlobalQueryList();
            }
        }
        catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new AcreException( "Unable to get Global Query List" );
        }
        return list;
    }

    public void loadGlobalQueries() throws AcreException {
      try {
            if( useServer() ) {
                System.out.println( "Making remote loadGlobalQueries call") ;
                patternQueryModelService.load();
            }
            else {
                DAOFactory.getPatternQueryRepository().load();
            }
        }
        catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new AcreException( "Unable to load Global Queries" );
        }

    }

    public boolean deleteGlobalQuery(QueryType queryType) throws AcreException {
        boolean success = false;
        try {
            if( useServer() ) {
                System.out.println( "Making remote deleteGlobalQuery call" );
                success = patternQueryModelService.deleteGlobalQuery(queryType);
            }
            else {
                success = DAOFactory.getPatternQueryRepository().deleteGlobalQuery(queryType);
            }
        }
        catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new AcreException( "Unable to load delete Global Query" );
        }
       return success;
    }


    // loads all patterns for global + latest version
    public void loadAllPatterns(boolean async, PatternEngineEventListener listener) throws AcreException {
        try {
            if( useServer() ) {
                server.loadAllPatterns(userContext, async, listener);
            }
            else {
                getLocalPatternEngine(userContext).loadAllPatterns(async, listener);
            }
        }
        catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new AcreException( "Unable to load delete Global Query" );
        }
       return ;

    }

    // loads all patterns for specific search context
    public void loadAllPatterns(SearchContext context, boolean async, PatternEngineEventListener listener) throws AcreException {
        try {
            if( useServer() ) {
                server.loadAllPatterns(userContext, context, async, listener);
            }
            else {
                getLocalPatternEngine(userContext).loadAllPatterns(async, listener);
            }
        }
        catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new AcreException( "Unable to load delete Global Query" );
        }
       return ;


    }

    public void shutdown() throws AcreException {
        try {
            if( useServer() ) {
                server.shutdown(userContext);
            }
            else {
                getLocalPatternEngine(userContext).shutdown();
            }
        }
        catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new AcreException( "Unable to load delete Global Query" );
        }
       return ;


    }


}