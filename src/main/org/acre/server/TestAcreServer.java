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


import org.acre.analytics.Snapshots;
import org.acre.common.AcreException;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: johncrupi
 * Date: Jan 17, 2005
 * Time: 10:42:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestAcreServer {
    //AcreServer sas;
    String pdm;
    String serverIP;

    public TestAcreServer() {
        this.pdm = null;
    }

    public TestAcreServer(String serverIP, String pdm) {
        this.serverIP = serverIP;
        this.pdm = pdm;

        //sas = (AcreServer) Naming.lookup("//" + serverIP + "/AcreServerImpl");
        testServer("user1", "password1");
       // testServer("user2", "password2");
        //testServer("user3", "password3");
    }

    private void testServer(String username, String password) {
        String p = (pdm == null ? "BD_Calls_SF" : pdm);
        System.out.println("Running PDM : " + p);
        try {
            //PDMW3DWriter writer = new PDMW3DWriter();
            //writer.createW3DPDM( sas.getPDMResult( "PackageViewPDM"));

            AcreDelegate delegate = new AcreDelegate( username, password );

            java.util.List pmodels = delegate.getGlobalPatternModels();
            System.out.println( "PatternModels : " + pmodels );

            java.util.List pqmodels = delegate.getGlobalPatternQueryModels();
            System.out.println( "PatternQueryModels : " + pqmodels );

            Snapshots snapshots =
                    delegate.getSnapshots( new Date( System.currentTimeMillis() - 30000 ),
                            new Date( System.currentTimeMillis() ) );

            System.out.println( "PatternAnayticSnapshots : " + snapshots );
        }
        catch (AcreException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    static public void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Format TestAcreServer serverIP  pdmName");

            System.exit(0);
        }
        new TestAcreServer(args[0], args[1]);
    }
}
