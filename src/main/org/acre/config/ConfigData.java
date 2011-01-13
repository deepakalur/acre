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
package org.acre.config;


import org.acre.pdmengine.SearchContext;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * @author Deepak.Alur@Sun.com
 *         Date: Oct 24, 2004
 *         Time: 3:44:57 PM
 */
public class ConfigData {

    String acreHomeDirectory;
    String acreRepositoryDirectory;
    String acreDBType;
    String acreDefaultSystem;
    String acreDefaultVersion;

    String tdbFilePath;
    String rdbmsURL;
    String rdbmsJDBCDriver;
    String rdbmsUserId;
    String rdbmsUserPassword;

    String visualizer3DOn;
    String collaboration3DOn;

    String debugOn;
    String debugLevel;
    String logFilePath;

    Color pdmColor;
    Color roleColor;
    Color artifactColor;

    String javaEditorPath;
    String tigerHomePath;
    String grappaDOTExecutablePath;

    private List configListeners = new ArrayList();

    private static final String ACRE_REPOSITORY_DIRNAME="AcreRepository";

    private static final String ACRE_HOME_DIR_KEY = "AcreRootDirectory";//key
    private static final String DEFAULT_HOME_DIRECTORY = "."; // value

    private static final String ACRE_REPOSITORY_DIR_KEY = "AcreRepositoryDirectory"; // key

    private static final String ACRE_DB_TYPE_KEY = "AcreDBType"; // key
    private static final String ACRE_DB_TYPE_TDB_VALUE = "TDB File"; // valid value
    private static final String ACRE_DB_TYPE_RDB_VALUE = "RDBMS";// valid value

    private static final String ACRE_TDB_FILEPATH_KEY = "AcreTDBFilePath"; // key

    private static final String RDBMS_URL_KEY = "RDBMSUrl"; // key
    private static final String RDBMS_JDBCDRIVER_KEY = "RDBMSJDBCDriver"; // key
    private static final String RDBMS_USERID_KEY_KEY = "RDBMSUserId"; // key
    private static final String RDBMS_USERPASSWORD_KEY = "RDBMSUserPassword"; // key

    private static final String VISUALIZER_3D_ON_KEY = "Visualizer3d"; //key
    private static final String VISUALIZER_3D_ON_VALUE = "true"; // value
    private static final String VISUALIZER_3D_OFF_VALUE = "false"; // value

    private static final String COLLABORATION_3D_ON_KEY = "Collaboration3d"; //key
    private static final String COLLABORATION_3D_ON_VALUE = "true"; // value
    private static final String COLLABORATION_3D_OFF_VALUE = "false"; // value
    
    private static final String DEBUG_ON_KEY = "DebugOn"; // key
    private static final String DEBUG_ON_TRUE_VALUE = "true"; // value
    private static final String DEBUG_ON_FALSE_VALUE = "false"; // value

    private static final String DEBUG_LEVEL_KEY = "DebugLevel";// key
    private static final String DEBUG_LEVEL_INFO_VALUE=ConfigConstants.DEBUG_LEVEL_INFO; // valid value
    private static final String DEBUG_LEVEL_SEVERE_VALUE=ConfigConstants.DEBUG_LEVEL_SEVERE;// valid value
    private static final String DEBUG_LEVEL_WARNING_VALUE = ConfigConstants.DEBUG_LEVEL_WARNING;// valid value
    private static final String DEBUG_LEVEL_ALL_VALUE = ConfigConstants.DEBUG_LEVEL_ALL; // valid value

    private static final String DEBUG_LOG_FILE_NAME_KEY = "DebugLogFile";//key

    public static Color BLUE =new Color(0x00, 0xCC, 0xFF); // blue
    public static Color LIGHT_ORANGE = new Color(0xFF, 0xCC, 0x00); // light orange
    public static Color  LIGHT_BLUE = new Color (0xCC, 0xE6, 0xFF); //  - lightblue
    public static Color  LIGHT_YELLOW_ORANGE = new Color(0xFF, 0xCC, 0x66); // light yellow orange
    public static Color PURPLE_BLUE = new Color(0x63, 0x30, 0xff); // sun purple
    public static Color LIGHT_BRIGHT_YELLOW = new Color(0xFF, 0xFF, 0x66); // light brighter yellow
    public static Color LIGHTER_ORANGE = new Color (0xff, 0x99, 0x33); // lighter orange
    public static Color LIGHT_YELLOW = new Color(0xFF, 0xFF, 0x99); // light yellow
    public static Color LIGHT_GREEN = new Color(0xCC, 0xFF, 0x00); // light green

    private Preferences myPreferences=null;
    private static final String DEFAULT_RDBMS_DRIVER = "org.gjt.mm.mysql.Driver";
    private static final String DEFAULT_RDBMS_URL = "jdbc:mysql://localhost:3306/salsa";
    private static final String DEFAULT_RDBMS_USERID = "salsa";
    private static final String DEFAULT_RDBMS_PASSWORD = "salsa";

    private static final int RDB_UPDATE_EVENT = 0; // future
    private static final int TDB_UPDATE_EVENT = 1;  // future
    private static final int DEBUG_UPDATE_EVENT = 2;  // future
    private static final int REPOSITORY_UPDATE_EVENT = 3;  // future
    private static final int ALL_UPDATE_EVENT=4; // default
    private static final String DEFAULT_REPOSITORY = "src/main/" + File.separator + ACRE_REPOSITORY_DIRNAME;
    public static final String DEFAULT_LOG_FILE_NAME = "AcreLog.log";
    private static final String PDM_COLOR_KEY = "PDMColor";
    private static final String ROLE_COLOR_KEY = "RoleColor";
    private static final String ARTIFACT_COLOR_KEY = "ArtifactColor";
    private static final String JAVA_FILE_EDITOR_KEY = "JavaFileEditor";
    private static final String TIGER_HOME_PATH = "TigerHomePath";
    private static final String GRAPPA_DOT_EXEC_FILE_PATH_KEY = "GrappaDOTExecPath";
    private static final String DEFAULT_DOT_EXECUTABLE_WINDOWS = "c:/Program Files/ATT/Graphviz/bin/dot.exe";;
    private static final String DEFAULT_MACOSX_EXECUTABLE_WINDOWS = "/Applications/Graphviz.app/Contents/MacOS/dot";
    private static final String ACRE_DEFAULT_SYSTEM_KEY = "AcreDefaultSystem";

    private static final String ACRE_DEFAULT_VERSION_KEY = "AcreDefaultVersion";

    protected ConfigData() {
    }

    public ConfigData(ConfigData origConfigData) {
        super();
        this.setData(origConfigData);
    }

    public String getAcreRepositoryDirectory() {
        return acreRepositoryDirectory;
    }

    public void setAcreRepositoryDirectory(String acreRepositoryDirectory) {
        this.acreRepositoryDirectory = acreRepositoryDirectory;
        setDefaultLogFile();
    }

    private void setDefaultLogFile() {
        setLogFilePath(getAcreRepositoryDirectory() +
                    ConfigService.FILE_SEPARATOR +
                    DEFAULT_LOG_FILE_NAME);
    }

    public String getAcreDBType() {
        return acreDBType;
    }

    public void setAcreDBType(String acreDBType) {
        this.acreDBType = acreDBType;
    }

    public String getTdbFilePath() {
        return tdbFilePath;
    }

    public void setTdbFilePath(String tdbFilePath) {
        this.tdbFilePath = tdbFilePath;
    }

    public String getRdbmsURL() {
        return rdbmsURL;
    }

    public void setRdbmsURL(String rdbmsURL) {
        this.rdbmsURL = rdbmsURL;
    }

    public String getRdbmsJDBCDriver() {
        return rdbmsJDBCDriver;
    }

    public void setRdbmsJDBCDriver(String rdbmsJDBCDriver) {
        this.rdbmsJDBCDriver = rdbmsJDBCDriver;
    }

    public String getRdbmsUserId() {
        return rdbmsUserId;
    }

    public void setRdbmsUserId(String rdbmsUserId) {
        this.rdbmsUserId = rdbmsUserId;
    }

    public String getRdbmsUserPassword() {
        return rdbmsUserPassword;
    }

    public void setRdbmsUserPassword(String rdbmsUserPassword) {
        this.rdbmsUserPassword = rdbmsUserPassword;
    }

    public String getAcreDefaultSystem() {
        return acreDefaultSystem;
    }

    public void setAcreDefaultSystem(String acreDefaultSystem) {
        this.acreDefaultSystem = acreDefaultSystem;
    }

    public String getAcreDefaultVersion() {
        return acreDefaultVersion;
    }

    public void setAcreDefaultVersion(String acreDefaultVersion) {
        this.acreDefaultVersion = acreDefaultVersion;
    }

    public String getDebugLevel() {
        return debugLevel;
    }

    public void setDebugLevel(String debugLevel) {
        this.debugLevel = debugLevel;
    }

    public String getDebugOn() {
        return debugOn;
    }

    public void setDebugOn(String debugOn) {
        this.debugOn = debugOn;
    }

    public Level getLogLevel() {

        if (DEBUG_LEVEL_INFO_VALUE.equalsIgnoreCase(getDebugLevel()))
            return Level.INFO;
        if (DEBUG_LEVEL_WARNING_VALUE.equalsIgnoreCase(getDebugLevel()))
            return Level.WARNING;
        if (DEBUG_LEVEL_SEVERE_VALUE.equalsIgnoreCase(getDebugLevel()))
            return Level.SEVERE;
        if (DEBUG_LEVEL_ALL_VALUE.equalsIgnoreCase(getDebugLevel()))
            return Level.ALL;

        return Level.ALL;
    }

    protected void setDefaultPreferences() {
        if (myPreferences == null) {
            loadPreferences();
        } else {
            //myPreferences.put(ACRE_REPOSITORY_DIR_KEY, null);
            myPreferences.put(GRAPPA_DOT_EXEC_FILE_PATH_KEY, getDefaultDOTExecutableFilePath());

            myPreferences.put(ACRE_DB_TYPE_KEY, ACRE_DB_TYPE_RDB_VALUE);
            myPreferences.put(RDBMS_JDBCDRIVER_KEY, DEFAULT_RDBMS_DRIVER);
            myPreferences.put(RDBMS_URL_KEY, DEFAULT_RDBMS_URL);
            myPreferences.put(RDBMS_USERID_KEY_KEY, DEFAULT_RDBMS_USERID);
            myPreferences.put(RDBMS_USERPASSWORD_KEY, DEFAULT_RDBMS_PASSWORD);
            myPreferences.put(ACRE_DEFAULT_SYSTEM_KEY, null);
            myPreferences.put(ACRE_DEFAULT_VERSION_KEY, null);
            myPreferences.put(DEBUG_ON_KEY, DEBUG_ON_TRUE_VALUE);
            myPreferences.put(VISUALIZER_3D_ON_KEY, VISUALIZER_3D_ON_VALUE);
            myPreferences.put(COLLABORATION_3D_ON_KEY, COLLABORATION_3D_OFF_VALUE);
            myPreferences.put(DEBUG_LEVEL_KEY, DEBUG_LEVEL_INFO_VALUE);
        }
    }

    protected void loadPreferences() {
        myPreferences = Preferences.userNodeForPackage(this.getClass());
        this.acreHomeDirectory = myPreferences.get(ACRE_HOME_DIR_KEY, DEFAULT_HOME_DIRECTORY);
        this.acreRepositoryDirectory = myPreferences.get(ACRE_REPOSITORY_DIR_KEY, DEFAULT_REPOSITORY);
        this.acreDBType = myPreferences.get(ACRE_DB_TYPE_KEY, ACRE_DB_TYPE_RDB_VALUE);
        this.rdbmsURL = myPreferences.get(RDBMS_URL_KEY, DEFAULT_RDBMS_URL);
        this.rdbmsJDBCDriver = myPreferences.get(RDBMS_JDBCDRIVER_KEY, DEFAULT_RDBMS_DRIVER);
        this.rdbmsUserId = myPreferences.get(RDBMS_USERID_KEY_KEY, DEFAULT_RDBMS_USERID);
        this.rdbmsUserPassword = myPreferences.get(RDBMS_USERPASSWORD_KEY, DEFAULT_RDBMS_PASSWORD);
        this.debugOn = myPreferences.get(DEBUG_ON_KEY, DEBUG_ON_TRUE_VALUE);
        this.visualizer3DOn = myPreferences.get(VISUALIZER_3D_ON_KEY, VISUALIZER_3D_ON_VALUE);
        this.collaboration3DOn = myPreferences.get(COLLABORATION_3D_ON_KEY, COLLABORATION_3D_OFF_VALUE);
        this.debugLevel = myPreferences.get(DEBUG_LEVEL_KEY, DEBUG_LEVEL_WARNING_VALUE);
//        this.tdbFilePath = myPreferences.get(ACRE_TDB_FILEPATH_KEY, null);
        this.logFilePath = myPreferences.get(DEBUG_LOG_FILE_NAME_KEY, null);
        this.acreDefaultSystem = myPreferences.get(ACRE_DEFAULT_SYSTEM_KEY, SearchContext.GLOBAL_SYSTEM);
        this.acreDefaultVersion = myPreferences.get(ACRE_DEFAULT_VERSION_KEY, SearchContext.LATEST_VERSION);

        String color;
        color = myPreferences.get(PDM_COLOR_KEY, Integer.toString(LIGHT_ORANGE.getRGB()));
        setPDMColor(Color.decode(color));
        color = myPreferences.get(ROLE_COLOR_KEY, Integer.toString(LIGHT_GREEN.getRGB()));
        setRoleColor(Color.decode(color));
        color = myPreferences.get(ARTIFACT_COLOR_KEY, Integer.toString(LIGHT_BLUE.getRGB()));
        setArtifactColor(Color.decode(color));

        if (this.logFilePath == null) {
            setDefaultLogFile();
        }

        this.javaEditorPath = myPreferences.get(JAVA_FILE_EDITOR_KEY, null);
        this.tigerHomePath = myPreferences.get(TIGER_HOME_PATH, null);

        this.grappaDOTExecutablePath = myPreferences.get(GRAPPA_DOT_EXEC_FILE_PATH_KEY,
                getDefaultDOTExecutableFilePath());

        if ((grappaDOTExecutablePath == null) ||
            (grappaDOTExecutablePath.trim().length() == 0))

            grappaDOTExecutablePath = getDefaultDOTExecutableFilePath();

    }

    protected void savePreferences() {
        ConfigError errors = validate();
        if (! errors.isEmpty()) {
            throw new AcreConfigException(errors);
        }
        if (myPreferences == null) {
            return;
        }

        try {
            myPreferences.put(ACRE_HOME_DIR_KEY, getAcreHomeDirectory());
            myPreferences.put(ACRE_REPOSITORY_DIR_KEY, getAcreRepositoryDirectory());
            myPreferences.put(ACRE_DB_TYPE_KEY, getAcreDBType());
//            myPreferences.put(ACRE_TDB_FILEPATH_KEY, getTdbFilePath());

            myPreferences.put(RDBMS_JDBCDRIVER_KEY, getRdbmsJDBCDriver());
            myPreferences.put(RDBMS_URL_KEY, getRdbmsURL());
            myPreferences.put(RDBMS_USERID_KEY_KEY, getRdbmsUserId());
            myPreferences.put(RDBMS_USERPASSWORD_KEY, getRdbmsUserPassword());

            myPreferences.put(ACRE_DEFAULT_SYSTEM_KEY, getAcreDefaultSystem());
            myPreferences.put(ACRE_DEFAULT_VERSION_KEY, getAcreDefaultVersion());

//            myPreferences.put(VISUALIZER_3D_ON_KEY, getVisualizer3DOn());
//            myPreferences.put(COLLABORATION_3D_ON_KEY, getCollaboration3DOn());

            myPreferences.put(DEBUG_ON_KEY, getDebugOn());
            myPreferences.put(DEBUG_LEVEL_KEY, getDebugLevel());
            myPreferences.put(DEBUG_LOG_FILE_NAME_KEY, getLogFilePath());

//            myPreferences.put(PDM_COLOR_KEY, Integer.toString(getPdmColor().getRGB()));
//            myPreferences.put(ROLE_COLOR_KEY, Integer.toString(getRoleColor().getRGB()));
//            myPreferences.put(ARTIFACT_COLOR_KEY, Integer.toString(getArtifactColor().getRGB()));

//            myPreferences.put(JAVA_FILE_EDITOR_KEY, getJavaEditorPath());
            myPreferences.put(TIGER_HOME_PATH, getTigerHomePath());
//            myPreferences.put(GRAPPA_DOT_EXEC_FILE_PATH_KEY, getGrappaDOTExecutablePath());
            myPreferences.flush();

        } catch (BackingStoreException e) {
            throw new AcreConfigException("Failed to Save Config Data Preferences into '"
                    + myPreferences.absolutePath() + "'",
                    e);
        }
    }

    public boolean isEmpty(String str) {
        if (str == null) return true;
        if (str.trim().length() == 0) return true;

        return false;
    }

    public boolean isValidDBType(String dbType) {
        if ( !(ACRE_DB_TYPE_RDB_VALUE.equals(acreDBType)) &&
                !(ACRE_DB_TYPE_TDB_VALUE.equals(acreDBType))) {
            return false;
        }

        return true;
    }

    public boolean isValidDebugLevel(String debugLevel) {
        if ( !(DEBUG_LEVEL_SEVERE_VALUE.equals(debugLevel)) &&
            !(DEBUG_LEVEL_INFO_VALUE.equals(debugLevel)) &&
            !(DEBUG_LEVEL_WARNING_VALUE.equals(debugLevel)) &&
            !(DEBUG_LEVEL_ALL_VALUE.equals(debugLevel))
        ) {
            return false;
        }

        return true;
    }

    public ConfigError validate() {

        ConfigError errors = new ConfigError();

        checkAcreHomeDirectory(errors);

        checkAcreRepository(errors);

        checkAcreDBType(errors);

        checkDebug(errors);

        checkFilePaths(errors);

        return errors;
    }

    private void checkFilePaths(ConfigError errors) {
        // todo
        // todo check for DOT executable
        // todo check for Java Editor executable
    }

    public void checkDebug(ConfigError errors) {
        if (isEmpty(getDebugOn())) {
            errors.addError(ConfigError.ACRE_DEBUGON_EMPTY_ERROR,
                    "Cannot set Debug On to null or empty value");
        } else {
            if (isEmpty(getDebugLevel())) {
                errors.addError(ConfigError.ACRE_DEBUGLEVEL_EMPTY_ERROR,
                "Cannot set Debug Level to null or empty value");
            }

            if (!isValidDebugLevel(getDebugLevel())) {
                errors.addError(ConfigError.ACRE_DEBUGLEVEL_INVALID_ERROR,
                    "Invalid value provided for DB Type, can be " +
                    DEBUG_LEVEL_SEVERE_VALUE + " or " + DEBUG_LEVEL_INFO_VALUE
                    + " or " + DEBUG_LEVEL_WARNING_VALUE );
            }
        }
    }

    public void checkAcreDBType(ConfigError errors) {
        if (isEmpty(getAcreDBType())) {
            errors.addError(ConfigError.ACRE_DBTYPE_EMPTY_ERROR,
                    "Cannot set DB Type to null or empty value");
        }
        if (! isValidDBType(getAcreDBType())) {
            errors.addError(ConfigError.ACRE_DBTYPE_INVALID_ERROR,
                "Invalid value provided for DB Type, can be " +
                ACRE_DB_TYPE_RDB_VALUE + " or " + ACRE_DB_TYPE_TDB_VALUE);
        }
        if (ACRE_DB_TYPE_TDB_VALUE.equals(getAcreDBType())) {
            if (isEmpty(tdbFilePath)) {
                errors.addError(ConfigError.ACRE_TDBFILEPATH_EMPTY_ERROR,
                    "DB Type is TDB, but TDB File Path is not set");
            }
            String tdbFilePath = getTdbFilePath();
            File f = new File(tdbFilePath);
            if (!f.exists()) {
                errors.addError(ConfigError.ACRE_TDBFILEPATH_NOTEXIST_ERROR,
                    "TDB File Path does not exist, Path = " +
                        f.getAbsolutePath());
            }
            if (!f.canRead()) {
                errors.addError(ConfigError.ACRE_TDBFILEPATH_PERMISSION_ERROR,
                        "TDB File does not have read Permission, Path = " +
                        f.getAbsolutePath());
            }
        } else if (ACRE_DB_TYPE_RDB_VALUE.equals(getAcreDBType())) {

            if (isEmpty(getRdbmsURL())) {
                errors.addError(
                    ConfigError.ACRE_RDBMSURL_EMPTY_ERROR,
                    "DB Type is RDBMS, but RDBMS URL is not set. \n");
            }
            if (isEmpty(getRdbmsJDBCDriver())) {
                errors.addError(
                    ConfigError.ACRE_RDBMSDBNAME_EMPTY_ERROR,
                    "DB Type is RDBMS, but RDBMS DB Name is not set. \n");
            }
            if (isEmpty(getRdbmsUserId())) {
                errors.addError(
                    ConfigError.ACRE_RDBMSUSERID_EMPTY_ERROR,
                    "DB Type is RDBMS, but RDBMS User Id is not set. \n");
            }
            if (isEmpty(getRdbmsUserPassword())) {
                errors.addError(
                    ConfigError.ACRE_RDBMSUSERPASSWORD_EMPTY_ERROR,
                    "DB Type is RDBMS, but RDBMS User Password is not set. \n");
            }
        }
        else {
            errors.addError(
                ConfigError.ACRE_DBTYPE_UNKNOWN_ERROR,
                "Unknown DBType. DBType can be either '"
                    + ACRE_DB_TYPE_RDB_VALUE
                    + "' or '"
                    + ACRE_DB_TYPE_TDB_VALUE
                    + "'");
        }
    }

    public void checkAcreHomeDirectory(ConfigError errors) {
        ConfigError tmpErrors = new ConfigError();
        boolean success = checkRepositoryFile(getAcreHomeDirectory(), tmpErrors);
        if(success) {
            success = checkRepositoryFile(getAcreRepositoryDirectory(), tmpErrors);
        }
        if(!success) {
            String defaultRepository = System.getProperty("acre.home");
            if(null != defaultRepository) {
                defaultRepository = defaultRepository + File.separator + ACRE_REPOSITORY_DIRNAME;
            }
            
            success = checkRepositoryFile(defaultRepository, tmpErrors);
            if(!success) {
                errors.putAll(tmpErrors);
            } else {
                setAcreHomeDirectory(System.getProperty("acre.home"));
                setAcreRepositoryDirectory(defaultRepository);
            }
        }
    }

    private boolean checkRepositoryFile(String filename, ConfigError errors) {
        if (isEmpty(filename)) {
            errors.addError(ConfigError.ACRE_HOMEDIR_EMPTY_ERROR, "Home Directory is not set");
            return false;
        }
        File file = new File(filename);
        if (!file.exists()) {
            // Directory does not exist, return error
            errors.addError(ConfigError.ACRE_HOMEDIR_NOTEXIST_ERROR,
                    "Home Directory "
                    + getAcreHomeDirectory()
                    + " does not exist. Path = "
                    + file.getAbsolutePath());
            return false;
        }
        if (!file.isDirectory()) {
            // error - file exists, but is not a directory
            errors.addError(ConfigError.ACRE_HOMEDIR_NOTDIRECTORY_ERROR,
                    "Home directory path '"
                    + getAcreHomeDirectory()
                    + "' exists, but is not a directory. Path = "
                    + file.getAbsolutePath());
            return false;
        }
        return true;
    }

    public void checkAcreRepository(ConfigError errors) {

        if (isEmpty(getAcreRepositoryDirectory())) {
            errors.addError(ConfigError.ACRE_REPOSITORYDIR_EMPTY_ERROR,
                    "Repository Root is not set");
        } else {
            File file = new File (getAcreRepositoryDirectory());
            if (! file.exists()) {

                // Try to find default repository based on acre.home

                // Directory does not exist, return error
                errors.addError(ConfigError.ACRE_REPOSITORYDIR_NOTEXIST_ERROR,
                "Repository Root "
                        + getAcreRepositoryDirectory()
                        + " does not exist. Path = "
                        + file.getAbsolutePath()
                        );
            } else {
                // directory exists, do additional checks
                if (!file.canWrite()) {
                    // error - no write permission for dir
                    errors.addError(ConfigError.ACRE_REPOSITORY_PERMISSION_ERROR,
                        "No Write Permission for Root '"
                            + getAcreRepositoryDirectory()
                            + "'. Path = "
                            + file.getAbsolutePath());
                }
                if (!file.isDirectory()) {
                    // error - file exists, but is not a directory
                    errors.addError(ConfigError.ACRE_REPOSITORY_NOTDIRECTORY_ERROR,
                        "Root '"
                            + getAcreRepositoryDirectory()
                            + "' exists, but is not a directory. Path = "
                            + file.getAbsolutePath());
                }
            }
        }
    }

    public void setData(ConfigData origConfigData) {
        this.setDebugLevel(origConfigData.getDebugLevel());
        this.setVisualizer3DOn(origConfigData.getVisualizer3DOn());
        this.setCollaboration3DOn(origConfigData.getCollaboration3DOn());
        this.setDebugOn(origConfigData.getDebugOn());
        this.setRdbmsJDBCDriver(origConfigData.getRdbmsJDBCDriver());
        this.setRdbmsURL(origConfigData.getRdbmsURL());
        this.setRdbmsUserId(origConfigData.getRdbmsUserId());
        this.setRdbmsUserPassword(origConfigData.getRdbmsUserPassword());
        this.setAcreDBType(origConfigData.getAcreDBType());
        this.setAcreRepositoryDirectory(origConfigData.getAcreRepositoryDirectory());
        this.setTdbFilePath(origConfigData.getTdbFilePath());
        this.setPDMColor(origConfigData.getPdmColor());
        this.setRoleColor(origConfigData.getRoleColor());
        this.setArtifactColor(origConfigData.getArtifactColor());
        this.setJavaEditorPath(origConfigData.getJavaEditorPath());
        this.setTigerHomePath(origConfigData.getTigerHomePath());
        this.setGrappaDOTExecutablePath(origConfigData.getGrappaDOTExecutablePath());
        this.setAcreHomeDirectory(origConfigData.getAcreHomeDirectory());
        this.setAcreDefaultSystem(origConfigData.getAcreDefaultSystem());
        this.setAcreDefaultVersion(origConfigData.getAcreDefaultVersion());
    }

    public String toString() {
        return "ConfigData{" +
                "acreRepositoryDirectory='" + acreRepositoryDirectory + "'" +
                ", acreDBType='" + acreDBType + "'" +
                ", tdbFilePath='" + tdbFilePath + "'" +
                ", rdbmsURL='" + rdbmsURL + "'" +
                ", rdbmsJDBCDriver='" + rdbmsJDBCDriver + "'" +
                ", rdbmsUserId='" + rdbmsUserId + "'" +
                ", rdbmsUserPassword='" + rdbmsUserPassword + "'" +
                ", acreDefaultSystem=" + acreDefaultSystem + "'" +
                ", acreDefaultVersion=" + acreDefaultVersion + "'" +
                ", visualizer3dOn='" + visualizer3DOn + "'" +
                ", collaboration3DOn='" + collaboration3DOn + "'" +
                ", debugOn='" + debugOn + "'" +
                ", debugLevel='" + debugLevel + "'" +
                ", logFilePath='" + logFilePath + "'" +
                ", pdmColor=" + pdmColor +
                ", roleColor=" + roleColor +
                ", artifactColor=" + artifactColor +
                ", javaEditorPath='" + javaEditorPath + "'" +
                ", tigerHomePath='" + tigerHomePath + "'" +
                ", grappaDOTExecutablePath='" + grappaDOTExecutablePath + "'" +
                ", configListeners=" + configListeners +
                ", myPreferences=" + myPreferences +
                "}";
    }

    public static void main(String args[]) {
        ConfigData data;
        data = new ConfigData();
        System.out.println("New Config Data = " + data);
        data.loadPreferences();
        System.out.println("Loaded Config Data = " + data);
        ConfigError errors;
        errors = data.validate();
        System.out.println("Errors before default = " + errors);
        data.setDefaultPreferences();
        errors = data.validate();
        System.out.println("Errors after default = " + errors);
        System.out.println("Config Data after default = " + data);
    }

    public boolean isDBTypeRDBMS() {
        if (ACRE_DB_TYPE_RDB_VALUE.equals(getAcreDBType()))
            return true;

        return false;
    }

    public void setDBTypeToRDBMS() {
        setAcreDBType(ACRE_DB_TYPE_RDB_VALUE);
    }

    public boolean isDBTypeTDB() {
        if (ACRE_DB_TYPE_TDB_VALUE.equals(getAcreDBType()))
            return true;

        return false;
    }

    public void setDBTypeToTDB() {
        setAcreDBType(ACRE_DB_TYPE_TDB_VALUE);
    }


    public boolean isVisualizer3DOn() {
        if (VISUALIZER_3D_ON_VALUE.equals(getVisualizer3DOn()))
            return true;

        return false;
    }

    public String getVisualizer3DOn() {
        return visualizer3DOn;
    }

    public void setVisualizer3DOn(String val) {
        visualizer3DOn = val;
    }

    public void setVisualizer3DOn(boolean on) {
        if (on)
            setVisualizer3DOn(DEBUG_ON_TRUE_VALUE);
        else
            setVisualizer3DOn(DEBUG_ON_FALSE_VALUE);
    }


    public boolean isDebugOn() {
        if (DEBUG_ON_TRUE_VALUE.equals(getDebugOn()))
            return true;

        return false;
    }

    public void setDebugOn(boolean on) {
        if (on)
            setDebugOn(DEBUG_ON_TRUE_VALUE);
        else
            setDebugOn(DEBUG_ON_FALSE_VALUE);
    }

    public boolean isDebugLevelSevere() {
        if (DEBUG_LEVEL_SEVERE_VALUE.equals(getDebugLevel()))
            return true;

        return false;
    }

    public void setDebugLevelSevere() {
        setDebugLevel(DEBUG_LEVEL_SEVERE_VALUE);
    }

    public boolean isDebugLevelWarning() {
        if (DEBUG_LEVEL_WARNING_VALUE.equals(getDebugLevel()))
            return true;

        return false;
    }

    public void setDebugLevelWarning() {
        setDebugLevel(DEBUG_LEVEL_WARNING_VALUE);
    }

    public boolean isDebugLevelInfo() {
        if (DEBUG_LEVEL_INFO_VALUE.equals(getDebugLevel()))
            return true;

        return false;
    }

    public void setDebugLevelInfo() {
        setDebugLevel(DEBUG_LEVEL_INFO_VALUE);
    }

    public void addConfigListener(ConfigListener listener) {
        this.configListeners.add(listener);
    }

    public void removeConfigListner(ConfigListener listener) {
        if (this.configListeners.contains(listener)) {
            this.configListeners.remove(listener);
        }
    }

    private void notifyListeners(int eventType) {
        for (int num=0; num < configListeners.size(); num++) {
            ConfigListener l = (ConfigListener) configListeners.get(num);
            switch (eventType) {
                case RDB_UPDATE_EVENT:
                    l.rdbUpdated();
                    break;
                case TDB_UPDATE_EVENT:
                    l.tdbUpdated();
                    break;
                case DEBUG_UPDATE_EVENT:
                    l.debugUpdated();
                    break;
                case REPOSITORY_UPDATE_EVENT:
                    l.repositoryUpdated();
                    break;
                case ALL_UPDATE_EVENT:
                    l.rdbUpdated();
                    l.tdbUpdated();
                    l.debugUpdated();
                    l.repositoryUpdated();
                    break;
                default:
                    break;
            }
        }
    }

    public String getLogFilePath() {
        return logFilePath;
    }

    public void setLogFilePath(String logFilePath) {
        this.logFilePath = logFilePath;
    }

    public void setPDMColor(Color color) {
        pdmColor = color;
    }

    public void setRoleColor(Color color) {
        roleColor = color;
    }

    public void setArtifactColor(Color color) {
        artifactColor = color;
    }

    public Color getPdmColor() {
        return pdmColor;
    }

    public Color getRoleColor() {
        return roleColor;
    }

    public Color getArtifactColor() {
        return artifactColor;
    }

    public void setJavaEditorPath(String path) {
        javaEditorPath = path;
    }

    public String getJavaEditorPath() {
        return javaEditorPath;
    }

    public String getTigerHomePath() {
        return tigerHomePath;
    }

    public void setTigerHomePath(String tigerHomePath) {
        this.tigerHomePath = tigerHomePath;
    }
    public String getGrappaDOTExecutablePath() {
        return grappaDOTExecutablePath;
    }

    public void setGrappaDOTExecutablePath(String grappaDOTExecutablePath) {
        this.grappaDOTExecutablePath = grappaDOTExecutablePath;
    }

    public String getAcreHomeDirectory() {
        return acreHomeDirectory;
    }

    public void setAcreHomeDirectory(String acreHomeDirectory) {
        this.acreHomeDirectory = acreHomeDirectory;
    }


    private String getDefaultDOTExecutableFilePath() {
        String os = System.getProperties().getProperty("os.name");
        String dotexecutable = System.getProperty("dotexecutable");
        if (null == dotexecutable) {
            if (os.startsWith("Windows")) { // Windows-specific
                dotexecutable = DEFAULT_DOT_EXECUTABLE_WINDOWS;
            } else {                        // Mac OSX-specific (TODO: add default location for Linux and Solaris)
                dotexecutable = DEFAULT_MACOSX_EXECUTABLE_WINDOWS;
            }
        }
        return dotexecutable;
    }


    public boolean isCollaboration3DOn() {
        if (COLLABORATION_3D_ON_VALUE.equals(getCollaboration3DOn()))
            return true;

        return false;
    }

    private String getCollaboration3DOn() {
        return collaboration3DOn;
    }

    public void setCollaboration3DOn(String val) {
        collaboration3DOn = val;
    }

    public void setCollaboration3DOn(boolean on) {
        if (on)
            setCollaboration3DOn(DEBUG_ON_TRUE_VALUE);
        else
            setCollaboration3DOn(DEBUG_ON_FALSE_VALUE);
    }

}
