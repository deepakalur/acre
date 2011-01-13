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

import org.acre.config.ConfigService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Deepak.Alur@Sun.com
 *         Date: Jun 14, 2004
 *         Time: 2:37:40 PM
 */
public class PDMProjectUtil {

    public static void createProject(String projectName,
                                     String description) throws IOException {

        // todo
        // the ConfigService must be initilaized to set the SalsaRoot directory prefix path
        // from the GUI or installation property file
        String dir= ConfigService.getInstance().getFactDBPath(projectName);
        createDirectory(dir);
        String srcDir = ConfigService.getInstance().getSourceCodeSrcPath(projectName);
        createDirectory(srcDir);
        dir = ConfigService.getInstance().getSourceCodeLibPath(projectName);
        createDirectory(dir);
        dir = ConfigService.getInstance().getProjectPath(projectName);
        createDirectory(dir);

        String projectPDMfile = ConfigService.getInstance().getProjectPDMXMLFilePath(projectName);
        String projectPDMQMfile = ConfigService.getInstance().getProjectPDMQueryMappingsFilePath(projectName);

        String file = ConfigService.getInstance().getProjectPropertiesFilePath(projectName);
        File propsFile = createFile(file);
        FileWriter writer = new FileWriter(propsFile);
        writer.write("name=" + projectName);
        writer.write('\n');
        writer.write("description=" + description );
        writer.write('\n');
        writer.write("sourceCodeDirectory=" + srcDir );
        writer.write('\n');
        writer.write("pdmFile=" + projectPDMfile );
        writer.write('\n');
        writer.write("pdmQueryMappings=" + projectPDMQMfile);
        writer.write('\n');
        writer.flush();
        writer.close();

        String pdmRepo = ConfigService.getInstance().getProjectPDMRepositoryPath(projectName);
        createDirectory(pdmRepo);
        file = ConfigService.getInstance().getProjectPDMXMLFilePath(projectName);
        createFile(file);
        file = ConfigService.getInstance().getProjectPDMQueryMappingsFilePath(projectName);
        createFile(file);

/*    - Create/Delete/Rename a Project "PSA":
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
    }

    public static File createDirectory(String dirPath) {
        File file = new File(dirPath);

        if (file.exists())
            new PDMException("Directory already exists: " + dirPath) ;

        file.mkdirs();

        return file;
    }

    public static File createFile(String filePath) {
        File file = new File(filePath);

        if (file.exists())
            new PDMException("File already exists: " + filePath) ;

        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new PDMException("Failed to create file: " + filePath, e);
        }

        return file;
    }


    /*
    - Create/Delete/Rename a Project "PSA":
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

    public static void deleteProject(String projectName) throws IOException {

        // todo
        // the ConfigService must be initilaized to set the SalsaRoot directory prefix path
        // from the GUI or installation property file

        String file = ConfigService.getInstance().getProjectPropertiesFilePath(projectName);
        delete(file);
        file = ConfigService.getInstance().getProjectPDMXMLFilePath(projectName);
        delete(file);
        file = ConfigService.getInstance().getProjectPDMQueryMappingsFilePath(projectName);
        delete(file);
        String pdmRepo = ConfigService.getInstance().getProjectPDMRepositoryPath(projectName);
        delete(pdmRepo);

        String dir= ConfigService.getInstance().getFactDBPath(projectName);
        delete(dir);
        dir = ConfigService.getInstance().getSourceCodeLibPath(projectName);
        delete(dir);
        dir = ConfigService.getInstance().getSourceCodeSrcPath(projectName);
        delete(dir);
        dir = ConfigService.getInstance().getSourceCodePath(projectName);
        delete(dir);
        dir = ConfigService.getInstance().getProjectPath(projectName);
        delete(dir);

        String projectPDMfile = ConfigService.getInstance().getProjectPDMXMLFilePath(projectName);
        String projectPDMQMfile = ConfigService.getInstance().getProjectPDMQueryMappingsFilePath(projectName);

    }

    private static void delete(String dir) {
        File file = new File(dir);

        if (file.exists())
            file.delete();
    }


    public static void main(String args[]) {
        try {
            ConfigService.getInstance().setAcreRepositoryDirectory("/acre/src/main");
            PDMProjectUtil.createProject("Hello", "Hello Project");
            PDMProjectUtil.deleteProject("Hello");
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}