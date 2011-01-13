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
package org.acre.lang.runtime;

import org.acre.lang.pql.pdbc.PQLResultSet;
import org.acre.lang.pql.pdbc.PQLValueHolder;

import java.util.Iterator;
import java.util.Map;

/**
 * Sample code for retrieving ResultSets for PQL variables.
 *
 * @author Syed Ali
 *         TODO: add docs
 */
public class Sample {

public static void main(String[] args) {
    sampleResultSet(""
            + "return select c from classes c where c.shortName LIKE \".*Manager.*\";"
        );
    sampleResultSet(""
            + "return select c, c.methods from classes c where c.shortName LIKE \".*Manager.*\";"
        );
}
public static void sampleResultSet(String query) {
    PQL pql = getPQL();
    Map result = pql.executePQL(query);
    for (Iterator iter = result.entrySet().iterator(); iter.hasNext();) {
        Map.Entry element = (Map.Entry) iter.next();
        String rsName = (String) element.getKey();
        PQLResultSet prs = (PQLResultSet) element.getValue();
        System.out.println("@@@ Result Set '"+ rsName + "'");
        for (int i = 0; i < prs.getMetaData().getRowCount(); i++) {
            for (int j = 0; j < prs.getMetaData().getColumnCount(); j++) {
                PQLValueHolder data = (PQLValueHolder) prs.getValue(i, j).getValue();
                System.out.print(data.getName() + "\t");
                //fetch all the attributes and relationship for the PQLData
                pql.fetch(data);
                //Print PQLData fetch with all the attributes and relationship
                //System.out.println(data.toString(true, true));
            }
            System.out.println();
        }
    }
}
    public static void main2(String[] args) {
        PQL pql = getPQL();
        System.out.println("Executing queries ...");
        Map o1 = pql.executePQL(""
            + "define aaa as select c from classes c where c.shortName = \".*Manager.*\";"
            + "define w as select c from classes c where c.shortName = \"StaffResourceFB\";"
            + "return aaa, w;"
        );
        
        Map m;
        if (false) {
//        } else if (o1 instanceof PQLResultSet) {
//            printResultSet("OnlyResult", (PQLResultSet) o1);
        } else if (o1 instanceof Map) {
            m = (Map) o1;
            for (Iterator iter = m.entrySet().iterator(); iter.hasNext();) {
                Map.Entry element = (Map.Entry) iter.next();
                printResultSet((String) element.getKey(), (PQLResultSet) element.getValue());
            }
        }
        m = (Map) o1;
        for (Iterator iter = m.entrySet().iterator(); iter.hasNext();) {
            Map.Entry element = (Map.Entry) iter.next();
            pql.setProperty((String) element.getKey(), (PQLResultSet) element.getValue());
        }
        o1 = (Map) pql.executePQL(""
                + "define qqq as select c from aaa c where c.shortName = \".*Manager.*\";"
                + "define www as select c from w c where c.shortName = \"StaffResourceFB\";"
                + "return qqq, www;"
            );
            
        if (o1 instanceof PQLResultSet) {
            printResultSet("OnlyResult", (PQLResultSet) o1);
        } else if (o1 instanceof Map) {
            m = (Map) o1;
            for (Iterator iter = m.entrySet().iterator(); iter.hasNext();) {
                Map.Entry element = (Map.Entry) iter.next();
                printResultSet((String) element.getKey(), (PQLResultSet) element.getValue());
            }
        }
        System.out.println("Done");
    }
    /**
     * @return
     */
    private static PQL getPQL() {
        String database = "PSA/factDatabase.ta";
//      String database = "/Users/smali/Documents/workspaces/current/salsa/database/factdb/psa/psaFacts.ta";
//      String database = "c:/ali/work/current/salsa/database/factdb/psa/psaFacts.ta";
        System.out.println("Loading " + database + " ...");
        PQL pql = PQL.createQLPQL(database);
        return pql;
    }
    public static void main1(String[] args) {
        String database = "database/factdb/psa/psaFacts.ta";
//      String database = "/Users/smali/Documents/workspaces/current/salsa/database/factdb/psa/psaFacts.ta";
//      String database = "c:/ali/work/current/salsa/database/factdb/psa/psaFacts.ta";
        System.out.println("Loading " + database + " ...");
        PQL pql = PQL.createQLPQL(database);
        System.out.println("Executing queries ...");
        Map o1 = pql.executePQL(""
            + "define aaa as select c.name from classes c where c.shortName = \"fff\";"
            + "define w as select c from classes c where c.shortName = \"StaffResourceFB\";"
            + "return aaa, w;"
        );
        
        if (o1 instanceof PQLResultSet) {
            printResultSet("OnlyResult", (PQLResultSet) o1);
        } else if (o1 instanceof Map) {
            Map m = (Map) o1;
            for (Iterator iter = m.entrySet().iterator(); iter.hasNext();) {
                Map.Entry element = (Map.Entry) iter.next();
                printResultSet((String) element.getKey(), (PQLResultSet) element.getValue());
            }
        }
        System.out.println("Done");
    }
    public static void printResultSet(String name, PQLResultSet rs) {
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@ " + name + " @@@@@@@@@@@@@@@@@@@@@@@@");
        System.out.println(rs);
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@ END " + name + " @@@@@@@@@@@@@@@@@@@@@@@@");
//        while(rs.next()) {
//            for (int i = 0; i < rs.getColumnCount(); i++) {
//                if (o instanceof PQLData) {
//                    PQLData pd = (PQLData) o;
//                    System.out.println(pd);
//                    
//                    for (Iterator iter = pd.getRelationshipNames(); iter.hasNext();) {
//                        String rel = (String) iter.next();
//                        System.out.println("  " + rel + ":");
////                        PQLData.RelData[] relData = pd.getRelationship(rel);
////                        for (int i = 0; i < relData.length; i++) {
////                            System.out.println("    " + relData[i].nodeId + ":\t" + relData[i].name);
////                        }
//                        PQLData[] relData = pd.getManyRelationshipObject(pql, rel);
//                        for (int i = 0; i < relData.length; i++) {
//                            System.out.println("    " + relData[i]);
//                        }
//                        
//                    }
//                } else {
//                    System.out.println(o);
//                    
//                }
//            }
//        }
    }
}
