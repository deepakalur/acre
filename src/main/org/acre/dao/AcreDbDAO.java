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
package org.acre.dao;

import org.acre.common.AcreStringUtil;
import org.acre.config.ConfigData;
import org.acre.config.ConfigService;
import org.acre.pdmengine.SearchContext;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author rajmohan@Sun.com
 */
public class AcreDbDAO {
    /**
     * Get Characteristics extraction timestamp between the given date ranges(start & end inclusive)
     * @param startDate
     * @param endDate
     * @return timestamps for all char-extraction runs in given date range
     */
    public Date[] getCharExtractTimeStamps(java.util.Date startDate, java.util.Date endDate) {

        SimpleDateFormat sdf = new SimpleDateFormat("''yyyy-MM-dd HH:mm:ss''");

        String query = "select extraction_time from salsaproject where extraction_time between " +
                sdf.format(startDate) + " and  " + sdf.format(endDate);

        return executeExtractTimeQuery(query);
    }



    /**
     * Get system's version for a given timestamp
     * @param system
     * @param extractTime if null, get version of the latest timestamp
     * @return version associated with the given extraction time
     */
    public String findVersion(String system, java.util.Date extractTime) {

        SimpleDateFormat sdf = new SimpleDateFormat("''yyyy-MM-dd HH:mm:ss''");

        String query;

        if ( extractTime != null )
            query = "select version from salsaproject where system = \"" + system + "\" and extraction_time = " +
                    sdf.format(extractTime) ;
        else
            query = "select version from salsaproject where system = \"" + system + "\" order by extraction_time desc ";

        Connection conn = null;
        try {
            conn = getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            String version = "";
            if ( rs.next() ) {
                version = rs.getString(1);
            }
            return version;
        } catch (SQLException e) {
            throw new RuntimeException("Internal Error in database :" + e.getMessage());
        } finally {
            try {
                if (conn!= null)
                    conn.close();
            } catch (SQLException e) {
                throw new RuntimeException("Internal Error in database :" + e.getMessage());
            }
        }

    }



    /**
     * Get extraction timestamp for the given system & version
     * @param system
     * @param version
     * @return timestamp associated with the given extraction time
     */
    public Date findExtractionTime(String system, String version) {

        if ( AcreStringUtil.isEmpty(system) || AcreStringUtil.isEmpty(version) )
            return null;

        String query;

        query = "select extraction_time from salsaproject where system = \"" + system + "\" and " +
                " version  = '" + version + "'";

        Connection conn = null;
        try {
            conn = getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            Date extractTime = null;
            if ( rs.next() ) {
                Timestamp ts = rs.getTimestamp(1);
                if (ts == null)
                    throw new RuntimeException("Could not fetch Extraction Timestamp from DB");

                extractTime = new Date(ts.getTime());
            }
            return extractTime;
        } catch (SQLException e) {
            throw new RuntimeException("Internal Error in database :" + e.getMessage());
        } finally {
            try {
                if (conn!= null)
                    conn.close();
            } catch (SQLException e) {
                throw new RuntimeException("Internal Error in database :" + e.getMessage());
            }
        }
    }






    private Date[] executeExtractTimeQuery(String timeQuery) {
        Connection conn = null;
        try {
            conn = getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(timeQuery);
            List dates = new ArrayList();
            while ( rs.next() ) {
                Timestamp ts = rs.getTimestamp(1);
                if (ts == null)
                    throw new RuntimeException("Could not fetch Extraction Timestamp from DB");

                dates.add(new Date(ts.getTime()));
            }
            Date [] result = new Date[dates.size()];
            dates.toArray(result);
            return result;
        } catch (SQLException e) {
            throw new RuntimeException("Internal Error in database :" + e.getMessage());
        } finally {
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                throw new RuntimeException("Internal Error in database :" + e.getMessage());
            }
        }
    }


    private Connection getConnection() {
        ConfigData configData = ConfigService.getInstance().getConfigData();
        // TODO : Use Apache DBCP to enable connection pooling
        try {
            Connection conn;
            Class.forName(configData.getRdbmsJDBCDriver());
            conn = DriverManager.getConnection(configData.getRdbmsURL(), configData.getRdbmsUserId(),
                    configData.getRdbmsUserPassword());
            return conn;
        } catch (Exception e) {
            throw new RuntimeException("Unable to connect : " + e.getMessage());
        }
    }

    // method to return list of available systems
    public String[] findAllSystems() {
        return findSystemsForVersion(null);
    }

    public String[] findSystemsForVersion(String version) {
            Connection conn = null;
            try {
                conn = getConnection();
                Statement stmt = conn.createStatement();
                String query =
                        "select distinct system from SalsaProject where 1=1 ";
                if ((version != null) && (version.trim().length() != 0)) {
                    query += "and version = \"" + version + "\"";
                }

                ResultSet rs = stmt.executeQuery(query);
                List systems = new ArrayList();
                systems.add(SearchContext.GLOBAL_SYSTEM);
                while ( rs.next() ) {
                    String system = rs.getString(1);
                    if (system == null)
                        throw new RuntimeException("Could not fetch System from DB");
                    systems.add(new String(system));
                }
                String [] result = new String [systems.size()];
                systems.toArray(result);
                return result;
            } catch (SQLException e) {
                throw new RuntimeException("Internal Error in database :" + e.getMessage());
            } finally {
                try {
                    if (conn!= null)
                        conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException("Internal Error in database :" + e.getMessage());
                }
            }
        }

    public String[] findVersionsForSystem(String system) {
                Connection conn = null;
                try {
                    conn = getConnection();
                    Statement stmt = conn.createStatement();
                    String query =
                            "select distinct version from SalsaProject where 1=1 ";
                    if ((system!= null) && (system.trim().length() != 0)) {
                        query += "and system = \"" + system + "\"";
                    }

                    ResultSet rs = stmt.executeQuery(query);
                    List versions = new ArrayList();
                    versions.add(SearchContext.LATEST_VERSION);

                    while ( rs.next() ) {
                        String version = rs.getString(1);
                        if (version == null)
                            throw new RuntimeException("Could not fetch Version from DB");

                        versions.add(new String(version));
                    }
                    String [] result = new String [versions.size()];
                    versions.toArray(result);
                    return result;
                } catch (SQLException e) {
                    throw new RuntimeException("Internal Error in database :" + e.getMessage());
                } finally {
                    try {
                        if (conn != null)
                            conn.close();
                    } catch (SQLException e) {
                        throw new RuntimeException("Internal Error in database :" + e.getMessage());
                    }
                }
            }

}
