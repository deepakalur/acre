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
package org.acre.pdmengine.pqe;

import org.acre.lang.pql.pdbc.PQLArtifact;
import org.acre.lang.pql.pdbc.PQLResultSet;
import org.acre.pdmengine.model.Artifact;
import org.acre.pdmengine.model.impl.ArtifactImpl;

/**
 * @author rajmohan@Sun.com
 */
public class ArtifactAdapterFactory {

    public static Artifact[][] transform(PQLResultSet pqlRS) {
        int nRows;
        nRows = pqlRS.getMetaData().getRowCount();
        Artifact [][]links = new Artifact[nRows][2];

        for ( int iRow=0; iRow < nRows; iRow++ ) {
                PQLArtifact cols[];
                cols = pqlRS.getRow(iRow);
                links[iRow][0] = new ArtifactImpl(cols[0], null);
                links[iRow][1] = new ArtifactImpl(cols[1],null);
        }
        return links;
    }

    public static Artifact[][] transform(PQLArtifact[][] pqlArtifacts) {
        Artifact [][]links = new Artifact[pqlArtifacts.length][2];
        for ( int row=0; row < pqlArtifacts.length; row++ ) {
                links[row][0] = new ArtifactImpl(pqlArtifacts[row][0], null);
                links[row][1] = new ArtifactImpl(pqlArtifacts[row][1],null);
        }
        return links;
    }
}
