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

import org.acre.common.AcreStringUtil;
import org.acre.lang.runtime.PQL;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * todo change this to load the properties from a file
 *
 * @author Deepak.Alur@Sun.com
 *         Date: Jun 10, 2004
 *         Time: 3:40:04 PM
 */
public class ConfigService {

    public static final String FILE_SEPARATOR = File.separator; // todo PQL() chokes on this - so setting to "/" System.getProperty("file.separator");

    private static ConfigService ourInstance = null;
    public static final String PDMSCHEMAFILENAME = "AcrePDMSchema.xsd";
    public static final String PDM_QUERYMAPPINGS_SCHEMA_FILENAME =     "AcrePDMQueryMappings.xsd";
    private static final String GLOBALPDMXMLFILENAME = "GlobalPDM.xml";
    private static final String GLOBALPDMQUERYMAPPINGS = "GlobalPDMQueryMappings.xml";
    private static final String PDMQUERYMAPPINGSXMLFILENAME = "PDMQueryMappings.xml";
    private static final String PDMXMLFILENAME = "PDM.xml";
    private static final String PROJECTSDIRNAME = "Projects";
    private static final String PDMREPOSITORYDIRNAME = "PDMRepository";
    private static final String SOURCECODEDIRNAME = "SourceCode";
    private static final String FACTDBNAME = "FactDB";
    private static final String APPLICATIONDATADIRNAME = "ApplicationData";
    private static final String GLOBARDIRNAME = "Global";
    private static final String QUERIES_DIR = "queries";
    private static final String ANNOTATIONS_DIR="annotations";
    private static final String LOGS_DIR = "logs";

    private ConfigData configData;
    private ArrayList debugLevels = new ArrayList();
    private FileHandler logFileHandler;
    private PQL pql = null;
    private boolean logHasNewMessages=false;

    public static ConfigService getInstance() {
        if (ourInstance == null) {
            ourInstance = new ConfigService();
            ourInstance.configData = new ConfigData();
            ourInstance.loadPreferences();
            ourInstance.checkConfig();
            ourInstance.initDebugLevels();
            ourInstance.initLogger();
        }
//        ourInstance.savePreferences(); // ??? Do we need it here ?

        return ourInstance;
    }

    private void initDebugLevels() {
        debugLevels = new ArrayList();
        debugLevels.add(ConfigConstants.DEBUG_LEVEL_ALL);
        debugLevels.add(ConfigConstants.DEBUG_LEVEL_INFO);
        debugLevels.add(ConfigConstants.DEBUG_LEVEL_WARNING);
        debugLevels.add(ConfigConstants.DEBUG_LEVEL_SEVERE);
    }

    private void initLogger() {
        try {
            if (checkLogDirectory()) {
//                String logFile = getConfigData().getLogFilePath();
//                if (AcreStringUtil.isEmpty(logFile)) {
                String logFile = getDefaultLogFilePath();
                getConfigData().setLogFilePath(logFile);
//                }
                logFileHandler = new FileHandler(logFile, 1000000, 1, true);
                Logger.getLogger("").addHandler(logFileHandler);
                logFileHandler.setFormatter(new SimpleFormatter());
                Logger.getLogger("").setLevel(getConfigData().getLogLevel());
                System.out.println("Log file is located at: '" + logFile + "'");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkLogDirectory() {
        if (getLogDirectory() != null) {
            File f = new File(getLogDirectory());
            if (f.exists()) {
                if (f.isDirectory())
                    return true;
                else
                    throw new AcreConfigException("Log Directory is not a directory, Path = "
                            + getLogDirectory());
            } else {
                return createLogDirectory(getLogDirectory());
            }
        }
        return false;
    }

    private boolean createLogDirectory(String dir) {
        File f = new File(dir);
        return f.mkdirs();
    }

    public String getLogDirectory() {
        if (getAcreRepositoryDirectory() != null) {
            return getAcreRepositoryDirectory() +
                    FILE_SEPARATOR +
                    LOGS_DIR;
        } else {
            return null;
        }
    }

    private String getDefaultLogFilePath() {
        return getLogDirectory() +
                FILE_SEPARATOR + ConfigData.DEFAULT_LOG_FILE_NAME;

    }

    public Logger getLogger(Object obj) {
        return getLogger(obj.getClass());
    }

    public Logger getLogger(Class cls) {
        return Logger.getLogger(cls.getPackage().getName().toString());
    }

    private ConfigService() {
    }

    public String getAcreRepositoryDirectory() {
        ConfigError errors = new ConfigError();
        configData.checkAcreRepository(errors);

        if (!errors.isEmpty())
            throw new AcreConfigException(errors);

        return configData.getAcreRepositoryDirectory();
    }

    public void setAcreRepositoryDirectory(String acreRootPrefix) throws AcreConfigException {
        ConfigError errors = new ConfigError();
        configData.setAcreRepositoryDirectory(acreRootPrefix);
        configData.checkAcreRepository(errors);

        if (!errors.isEmpty())
            throw new AcreConfigException(errors);

        configData.savePreferences();
    }

    public void setTemporaryAcreRepositoryDirectory(String salsaRootPrefix) throws AcreConfigException {
        ConfigError errors = new ConfigError();
        configData.setAcreRepositoryDirectory(salsaRootPrefix);
        configData.checkAcreRepository(errors);

        if (!errors.isEmpty())
            throw new AcreConfigException(errors);

    }

    public String getAcreRoot() {
        return getAcreRepositoryDirectory();// + FILE_SEPARATOR + ...

    }

    public String getGlobalPath() {
        return getAcreRoot() + FILE_SEPARATOR +
                getGlobal();
    }

    public String getApplicationDataPath() {
        return getGlobalPath() + FILE_SEPARATOR +
                getApplicationData();
    }

    public String getFactDBPath(String project) {
        return getApplicationDataPath() + FILE_SEPARATOR +
                getFactDB() + FILE_SEPARATOR +
                project;
    }

    public String getFactDBFilePath(String project) {
        return getApplicationDataPath() + FILE_SEPARATOR +
                getFactDB() + FILE_SEPARATOR +
                project + FILE_SEPARATOR +
                project + "Facts.ta";
    }

    public String getSourceCodePath(String project) {
        return getApplicationDataPath() + FILE_SEPARATOR +
                getSourceCode() + FILE_SEPARATOR +
                project;
    }

    public String getSourceCodeSrcPath(String project) {
        return getSourceCodePath(project) + FILE_SEPARATOR +
                "src";
    }

    public String getSourceCodeLibPath(String project) {
        return getSourceCodePath(project) + FILE_SEPARATOR +
                "lib";
    }

    public String getGlobalPDMRepositoryPath() {
        return getGlobalPath() + FILE_SEPARATOR +
                getPDMRepository();
    }

    public String getPDMSchemaPath() {
        return getGlobalPDMRepositoryPath() + FILE_SEPARATOR +
                PDMSCHEMAFILENAME; //"SalsaPDMSchema.xsd";
    }

    public String getGlobalPDMXMLFilePath() {
        return getGlobalPDMRepositoryPath() + FILE_SEPARATOR +
                GLOBALPDMXMLFILENAME; //"GlobalPDM.xml";
    }

    /**
     * This is the directory where we keep all *.pql files
     *
     * @return String - directory path name
     */
    public String getGlobalPDMQueryDetailsDirPath() {
        return getGlobalPDMRepositoryPath() + FILE_SEPARATOR +
                QUERIES_DIR;
    }

    public String getGlobalPDMQueryDetailsFilePath(String relativePath) {
        return getGlobalPDMQueryDetailsDirPath() + FILE_SEPARATOR +
                relativePath;

    }

    public String getPDMAnnotationsFilePath(String relativePath) {

        String pdmAnnotationsRepository =
               getGlobalPDMRepositoryPath() + FILE_SEPARATOR +   ANNOTATIONS_DIR;

        return pdmAnnotationsRepository + FILE_SEPARATOR + relativePath;
    }

    public String getGlobalPDMQueryMappingsFilePath() {
        return getGlobalPDMRepositoryPath() + FILE_SEPARATOR +
                GLOBALPDMQUERYMAPPINGS; //"GlobalPDMQueryMappings.xml";
    }

    public String getProjectPDMQueryMappingsFilePath(String project) {
        return getProjectPath(project) + FILE_SEPARATOR +
                project + PDMQUERYMAPPINGSXMLFILENAME; // "PDMQueryMappings.xml";
    }

    public String getProjectPDMXMLFilePath(String project) {
        return getProjectPDMRepositoryPath(project) + FILE_SEPARATOR +
                project + PDMXMLFILENAME; //"PDM.xml";
    }

    public String getProjectPropertiesFilePath(String project) {
        return getProjectPath(project) + FILE_SEPARATOR
                + project + ".properties";
    }

    public String getProjectPDMRepositoryPath(String project) {
        return getProjectPath(project) + FILE_SEPARATOR +
                getPDMRepository();
    }

    public String getProjectPath(String project) {
        return getAcreRoot() + FILE_SEPARATOR
                + getProjects() + FILE_SEPARATOR +
                project;
    }

    private String getProjects() {
        return PROJECTSDIRNAME; //"Projects";
    }

    private String getPDMRepository() {
        return PDMREPOSITORYDIRNAME; //"PDMRepository";
    }

    private String getSourceCode() {
        return SOURCECODEDIRNAME; //"SourceCode";
    }

    private String getFactDB() {
        return FACTDBNAME; //"FactDB";
    }

    private String getApplicationData() {
        return APPLICATIONDATADIRNAME; //"ApplicationData";
    }

    private String getGlobal() {
        return GLOBARDIRNAME; //"Global";
    }

    public String getPDMQueryMappingsSchemaPath() {
        return getGlobalPDMRepositoryPath() + FILE_SEPARATOR +
                PDMSCHEMAFILENAME; //"SalsaPDMSchema.xsd";
    }

/*
- Create $SALSA_REPOSITORY_ROOT/Global/ApplicationData/FactDB/PSA
- Create $SALSA_REPOSITORY_ROOT/Global/ApplicationData/SourceCode/PSA
- Create $SALSA_REPOSITORY_ROOT/Global/ApplicationData/SourceCode/PSA/lib
- Create $SALSA_REPOSITORY_ROOT/Global/ApplicationData/SourceCode/PSA/src
- Create $SALSA_REPOSITORY_ROOT/Projects/PSA
- Create $SALSA_REPOSITORY_ROOT/Projects/PSA/PSA.properties
- Create $SALSA_REPOSITORY_ROOT/Projects/PSA/PSAPDM.xml
- Create $SALSA_REPOSITORY_ROOT/Projects/PSA/PSAPDMQueryMappings.xml
- Set PSA.properties:
        name=PSA, description=PSA description sourceCodeDirectory=PSA factDatabaseDirectory=PSA
        pdmFile=PSAPDM.xml pdmQueryMappings=PSAPDMQueryMappings.xml
*/

    public static void main(String args[]) {
        ConfigService c=null;
        try {

            c = ConfigService.getInstance();

            Logger log = c.getLogger(c);
            log.setLevel(Level.ALL);
            log.info("This is an info");
            log.warning("This is a warning");
            log.severe("This is severe");
            log.config("This is a config message");
            log.fine("This is a fine");
            log.finer("This is finer");
            log.finest("This is finest");

            log = c.getLogger(AcreStringUtil.class);
            log.info("This is util log");
            log.info("This is util log");
            log.info("This is util log");

            //c.setSalsaRootPrefix("/");

            System.out.println("getAcreRepositoryDirectory() = " + c.getAcreRepositoryDirectory());
            System.out.println("getAcreRoot() = " + c.getAcreRoot());
            System.out.println("getGlobalPDMRepositoryPath() = " + c.getGlobalPDMRepositoryPath());
            System.out.println("getGlobalPath() = " + c.getGlobalPath());
            System.out.println("getPDMSchemaPath() = " + c.getPDMSchemaPath());
            System.out.println("getApplicationDataPath() = " + c.getApplicationDataPath());

            System.out.println("getGlobalPDMQueryMappingsFilePath() = " + c.getGlobalPDMQueryMappingsFilePath());
            System.out.println("getGlobalPDMXMLFilePath() = " + c.getGlobalPDMXMLFilePath());

            System.out.println("getProjectPDMQueryMappingsFilePath(\"PSA\") = " + c.getProjectPDMQueryMappingsFilePath("PSA"));
            System.out.println("getProjectPDMXMLFilePath(\"PSA\") = " + c.getProjectPDMXMLFilePath("PSA"));
            System.out.println("getProjectPropertiesFilePath(\"PSA\") = " + c.getProjectPropertiesFilePath("PSA"));
            System.out.println("getFactDBPath(\"PSA\") = " + c.getFactDBPath("PSA"));
            System.out.println("getSourceCodeLibPath(\"PSA\") = " + c.getSourceCodeLibPath("PSA"));
            System.out.println("getSourceCodePath(\"PSA\") = " + c.getSourceCodePath("PSA"));
            System.out.println("getSourceCodeSrcPath(\"PSA\") = " + c.getSourceCodeSrcPath("PSA"));
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            c.closeLog();            
        }
    }

    public void checkConfig() {
        ConfigError errors = configData.validate();
        if (!errors.isEmpty())
            throw new AcreConfigException(errors);
        checkAcrePDMSchema();
        checkGlobalPDMDefinitions();
        checkGlobalPDMQueryMappings();
    }

    public void checkAcrePDMSchema() {
        String fileName = getPDMSchemaPath();
        File file = new File(fileName);

        if (!file.exists()) {
            throw new AcreConfigException("PDM Schema file '" + fileName + "' does not exist. Path = " + file.getAbsolutePath());
        }
    }

    public void checkGlobalPDMDefinitions() {
        String fileName = getGlobalPDMXMLFilePath();

        File file = new File(fileName);

        if (!file.exists()) {
            throw new AcreConfigException("Global PDM Definitions XML file '" + fileName + "' does not exist. Path = " + file.getAbsolutePath());
        }
    }

    public void checkGlobalPDMQueryMappings() {

        String fileName = getGlobalPDMQueryMappingsFilePath();
        File file = new File(fileName);

        if (!file.exists()) {
            throw new AcreConfigException("Global PDM Query Mappings file '" + fileName + "' does not exist. Path = " + file.getAbsolutePath());
        }
    }

    public ConfigData getConfigData() {
        return configData;
    }

    public void setConfigData(ConfigData configData) {
        this.configData = configData;
    }

    public void savePreferences() {
        configData.savePreferences();
    }

    public void loadPreferences() {
        configData.loadPreferences();
    }

//    public PQL getPql() {
//        if (null == pql) {
//            pql = new org.acre.lang.runtime.groovy.GroovyPQL(configData.getRdbmsJDBCDriver(),
//                    configData.getRdbmsURL(),
//                    configData.getRdbmsUserId(),
//                    configData.getRdbmsUserPassword()).getPql();
//        }
////        configData.getAcreRepositoryDirectory();
//        return pql;  //To change body of created methods use File | Settings | File Templates.
//    }

    public void registerListener(ConfigListener l) {
        configData.addConfigListener(l);
    }

    public void removeListener(ConfigListener l) {
        configData.removeConfigListner(l);
    }

    public String getProjectQueryDetailsFilePath(String relativeFilePath) {
        return null;  //To change body of created methods use File | Settings | File Templates.
    }

    /**
     * This is the directory where we keep all *.pql files
     *
     * @return String - directory path name
     */
    public String getProjectPDMQueryDetailsDirPath(String projectName) {
        return getProjectPDMRepositoryPath(projectName) + FILE_SEPARATOR +
                QUERIES_DIR;
    }

    public String getProjectPDMQueryDetailsFilePath(String projectName,
                                                    String relativePath) {
        return getProjectPDMQueryDetailsDirPath(projectName) + FILE_SEPARATOR +
                relativePath;
    }

    public Object[] getDebugLevelValues() {
        return debugLevels.toArray();
    }

    public void eraseLogFile() {
        try {
            // remove handler and close the log handler
            Logger.getLogger("").removeHandler(logFileHandler);
            closeLog();

            // empty the file
            File f = new File(this.getConfigData().getLogFilePath());
            FileWriter fw = new FileWriter(f);
            fw.write("");
            fw.flush();
            fw.close();

            // re-initialize the logger
            initLogger();
        } catch (IOException e) {
            throw new AcreConfigException("Error encountered during log file erase", e);
        }

    }

    public void deleteAllLogs() {
        File f = new File(getLogDirectory());
        if (f.exists() && f.isDirectory()) {
            LogFileFilter filter = new LogFileFilter();
            File[] files = f.listFiles(filter);
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    files[i].delete();
                }
            }
        }
    }

    public void setLogHasNewMessages(boolean status) {
        logHasNewMessages = status;
    }

    public void clearLogHasNewMessages() {
        logHasNewMessages = false;
    }

    private static class LogFileFilter implements FileFilter {
        public boolean accept(File pathname) {
            if (pathname.getName().startsWith(ConfigData.DEFAULT_LOG_FILE_NAME))
                return true;
            else
                return false;
        }
    }

    public void closeLog() {
        logFileHandler.flush();
        logFileHandler.close();
    }
}
