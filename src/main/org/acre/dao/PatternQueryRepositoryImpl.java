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

import org.acre.pdmqueries.*;
import org.acre.common.AcreErrors;
import org.acre.config.ConfigService;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.math.BigInteger;
import java.util.*;
import java.util.logging.Logger;


/**
 * @author Deepak.Alur@Sun.com
 *         Date: Jun 18, 2004
 *         Time: 3:41:18 PM
 */
class PatternQueryRepositoryImpl implements PatternQueryRepository {
    private static PatternQueryRepository ourInstance;

    private PDMQueryList globalQueryList;
    private HashMap projectQueryListMap;

    private PDMQueryValidator validator = PDMQueryValidator.getInstance();

    // create a PDM object
    private ObjectFactory factory = new ObjectFactory();

    private String FILE_PREPEND_STRING =
        "<PDMQueryList xmlns=\"http://java.sun.com/xml/ns/acre/\"" +
            "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
            "xsi:schemaLocation=\"http://java.sun.com/xml/ns/acre/\n" +
            "AcrePDMQueryMappings.xsd\">\n";

    private static final String PACKAGE_NAME = "org.acre.pdmqueries";
    private static final String QUERY_MAPPINGS_SCHEMA_LOCATION =
            "http://java.sun.com/xml/ns/acre/\n"+
            ConfigService.PDM_QUERYMAPPINGS_SCHEMA_FILENAME
            ;
    private Logger logger=null;

    public static PatternQueryRepository getInstance() {
        if (ourInstance == null)
            ourInstance = new PatternQueryRepositoryImpl();

        return ourInstance;
    }

    private PatternQueryRepositoryImpl() {
        initialize();
    }

    private void initialize() {

        if (logger == null)
            logger = ConfigService.getInstance().getLogger(this);

        // todo synchronization - thread safety
        if (projectQueryListMap == null)
            projectQueryListMap = new HashMap();

        // load/reload Global Query List
        load();

        // reload all projects query list
        Set projects = projectQueryListMap.keySet();
        Iterator projIter = projects.iterator();
        while (projIter.hasNext()) {
            String projectName = (String) projIter.next();
            // load project
            PDMQueryList list = load(
                    ConfigService
                    .getInstance()
                    .getProjectPDMQueryMappingsFilePath(projectName));
            projectQueryListMap.put(projectName, list);
        }
    }

    public List getProjectQueryList(String project) {
        if (projectQueryListMap.containsKey(project)) {
            PDMQueryList queryList = (PDMQueryList) projectQueryListMap.get(project);
            return queryList.getQuery();
        }

        // does not exist. load and create
        PDMQueryList projectList = load(
                ConfigService
                .getInstance()
                .getProjectPDMQueryMappingsFilePath(project));

        if (projectList != null) {
            projectQueryListMap.put(project, projectList);
            return projectList.getQuery();
        }

        return null;
    }


    public void load() {
        globalQueryList = load(
                    ConfigService
                    .getInstance()
                    .getGlobalPDMQueryMappingsFilePath());
    }

    private PDMQueryList load(String filepath) {
        // Create JAXBContext object to handle generated classes in "com.sun.salsa.syscatalog" package
        PDMQueryList list = null;
        JAXBContext jaxbCtx = null;
        try {
            jaxbCtx = JAXBContext.newInstance(PACKAGE_NAME);

            // Unmarshaller to unmarshal xml to java object content tree
            Unmarshaller unmarshaller = jaxbCtx.createUnmarshaller();

            logger.info("Loading QueryMappings file:" +
                    ConfigService
                    .getInstance()
                    .getGlobalPDMQueryMappingsFilePath());

            FileInputStream is = new FileInputStream(filepath);
                    //config.getGlobalPDMQueryMappingsFilePath());
            list = (PDMQueryList) unmarshaller.unmarshal(is);
            is.close();

        } catch (JAXBException e) {
            throw new PDMException(e);
        } catch (IOException ioe) {
            throw new PDMException(ioe);
        }

        return list;
    }

    public void saveAll() {
        // save global list
        save();

        // save projects list
        Iterator iter = getProjects().iterator();
        while (iter.hasNext()) {
            String projectName = (String) iter.next();
            save(projectName);
        }
    }

    public void save(String projectName) {
        save(ConfigService
                .getInstance()
                .getProjectPDMQueryMappingsFilePath(projectName),
                (PDMQueryList) projectQueryListMap.get(projectName));

    }

    public void save() {
         save(ConfigService
                .getInstance()
                .getGlobalPDMQueryMappingsFilePath(),
                 globalQueryList);
    }

    private void save(String filepath, PDMQueryList queryList) {

        if ((filepath == null) || (filepath.trim().length() ==0))
            throw new PDMException("Attempting to save to illegal file path:"+  filepath);

        if (queryList == null)
            return;

        // Create JAXBContext object to handle generated classes in "com.sun.salsa.syscatalog" package
        JAXBContext jaxbCtx = null;

        try {
            jaxbCtx = JAXBContext.newInstance(PACKAGE_NAME);

            // Unmarshaller to unmarshal xml to java object content tree
            Marshaller marshaller = jaxbCtx.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
                    Boolean.TRUE);
            marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION,
                        QUERY_MAPPINGS_SCHEMA_LOCATION
                    );
//            marshaller.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION,
//                    ConfigService
//                    .getInstance()
//                    .getPDMQueryMappingsSchemaPath());

            FileOutputStream os = null;

            validateBeforeSave(marshaller, queryList);

            // if it works for the temp file, it works for the real file
            File file = new File(filepath);//config.getGlobalPDMQueryMappingsFilePath());

            if (! file.exists()) {
                file.createNewFile();
            }

            logger.info("Got Query Mappings File:" + file);
            os = new FileOutputStream(file);
            marshaller.marshal(queryList, os);
            os.flush();
            os.close();

            logger.info("Saved Query Mappings File:" + file);

        } catch (JAXBException e) {
            throw new PDMException(e);
        } catch (IOException ioe) {
            throw new PDMException(ioe);
        }
        finally {
            logger.info("Successfully Saved Query Mappings File");
        }
    }

    private void validateBeforeSave(Marshaller marshaller, PDMQueryList list) {
        File tmpfile = null;

        try {
            tmpfile = createTempPDMFile(
                    ConfigService
                    .getInstance()
                    .getGlobalPDMQueryMappingsFilePath());
            logger.info("Created Temp File:" + tmpfile);
            FileOutputStream tmpos = new FileOutputStream(tmpfile);
            marshaller.marshal(list, tmpos );
            tmpos .flush();
            tmpos .close();
            logger.info("Successfully closed Temp File:" + tmpfile);
        } catch (JAXBException e) {
            throw new PDMException("PDM Query Mappings Save Validation Failed", e);
        }catch (IOException ioe) {
            throw new PDMException("Failed to create Temp file:" + tmpfile, ioe);
        } finally {
            // delete Temp file
            logger.info("Deleting temp file:" + tmpfile);
            tmpfile.delete();
        }
    }

    /**
     * returns a list containing QueryType instances
     * @return
     */
    public List getGlobalQueryList() {
        return globalQueryList.getQuery();
    }

    public List getGlobalQueryNamesList() {
        ArrayList names = new ArrayList();
        for (int i = 0; i < globalQueryList.getQuery().size(); i++) {
            QueryType q = (QueryType) globalQueryList.getQuery().get(i);
            names.add(q.getName());
        }
        return names;
    }

    /**
     * returns a single QueryType instance for the given queryName from the global
     * queries list
     * @param queryName
     * @return
     */
    public QueryType getGlobalQuery(String queryName) {
        if (queryName == null)
            return null;
        
//        for (Object qobj: globalQueryList.getQuery()) {
        for (Iterator iter = globalQueryList.getQuery().iterator(); iter.hasNext();) {
            Object qobj = iter.next();
            QueryType query = (QueryType) qobj;
            if ( queryName.equalsIgnoreCase(query.getName()) )
                return query;
        }

        return null;
    }

    public boolean globalQueryFileExists(String queryName) {
        QueryType q = getGlobalQuery(queryName);
        if (q == null) {
            throw new PDMException("Global Query not found to load the script: Query Name = '"
                + queryName
                + "'"
            );
        }

        return globalQueryFileExists(q);
    }

    public boolean globalQueryFileExists(QueryType query) {
        if (query == null)
            throw new PDMException("Cannot check query file for null query");

        if (query.getRelativeFilePath() == null)
            throw new PDMException("Relative File Path not set for Query '"+
                    query.getName()
                    +"'");

        String file =
                ConfigService
                .getInstance()
                .getGlobalPDMQueryDetailsFilePath(
                            query.getRelativeFilePath());

        File f = new File(file);
        if (!f.exists()) {
            return false;
        }
        else {
            return true;
        }
    }

    public String getGlobalQueryFileName(String queryName) {
        QueryType q = getGlobalQuery(queryName);
        if (q == null) {
            throw new PDMException("Global Query not found to load the script: Query Name = '"
                + queryName
                + "'"
            );
        }

        if (! globalQueryFileExists(q)) {
            throw new PDMException("File does not exist: '" +
                    ConfigService
                        .getInstance()
                        .getGlobalPDMQueryDetailsFilePath(
                            q.getRelativeFilePath())
                    + "' for query '" +
                    q.getName() + "'"
            );
        }

        String file =
                ConfigService
                .getInstance()
                .getGlobalPDMQueryDetailsFilePath(
                            q.getRelativeFilePath());

        return file;
    }

    public String getGlobalQueryFile(String queryName) {

        String file = getGlobalQueryFileName(queryName);

        File f = new File(file);

        StringBuffer sb = new StringBuffer();
        try {
            BufferedReader r = new BufferedReader(new FileReader(f));
            String str=null;
            while ((str= r.readLine()) != null) {
                sb.append(str);
                sb.append('\n');
            }

        } catch (FileNotFoundException e) {
            throw new PDMException("File Not Found: " + file, e);
        } catch (IOException e) {
            throw new PDMException("Failed to read file: " + file, e);
        }

        return sb.toString();
    }

    public QueryType getProjectQuery(String projectName, String queryName) {
        PDMQueryList list = (PDMQueryList) projectQueryListMap.get(projectName);
        if (list == null)
            throw new PDMException("No such project to get Query:" + projectName);

//      for (Object qobj: list.getQuery()) {
        for (Iterator iter = list.getQuery().iterator(); iter.hasNext();) {
            Object qobj = iter.next();
            QueryType query = (QueryType) qobj;
            if ( queryName.equalsIgnoreCase(query.getName()) )
                return query;
        }

        return null;

    }

    public QueryType createNewQuery() {
        QueryType q = createQuery(
                PDMXMLConstants.NEW_QUERY_NAME,
                PDMXMLConstants.QUERY_RETURN_VARIABLE_TYPE_ANY,
                PDMXMLConstants.QUERY_LANGUAGE_PQL,
                "Modify before saving",
                PDMXMLConstants.NEW_QUERY_NAME + PDMXMLConstants.PQL_FILE_EXTENSION,
                null, null
                );

        return q;
    }

    public QueryType createNewAndInsertGlobalQuery() {
        QueryType q = createNewQuery();

        this.getGlobalQueryList().add(q);

        return q;
    }

    public QueryType createQuery(
            String name,
            String type,
            String language,
            String description,
            String relativeFilePath,
            List arguments,
            List returnVariables
            ) {

        try {

            QueryType q = factory.createQuery();
            q.setName(name);
            q.setType(type);
            q.setDescription(description);
            q.setRelativeFilePath(relativeFilePath);
            q.setLanguage(language);

            // add arguments
            if ((arguments != null) && (!arguments.isEmpty()))
                q.getArgument().addAll(arguments);

            // add return variables
            if ((returnVariables != null) && (!returnVariables.isEmpty()))
                q.getReturnVariable().addAll(returnVariables);

            logger.info("Created Query: " + q);

            return q;
        } catch (JAXBException e) {
            throw new PDMException(e);
        }
    }

    public ArgumentType createArgument(
            String seq, String name,
            String type, Serializable value, String description,
            String relatedQueryName,
            String relatedQueryVariableName
            ) {

        try {
            validator.validateQueryArgument(seq, name,
                    type, value, description, relatedQueryName, relatedQueryVariableName);

            ArgumentType a = factory.createArgument();
            a.setSequence(new BigInteger(seq));
            a.setName(name);
            a.setType(type);
            a.setValue(value.toString()); // todo how to serialize value?
            a.setDescription(description);
            a.setRelatedQueryName(relatedQueryName);
            a.setRelatedQueryReturnVariableName(relatedQueryVariableName);

            logger.info("Created Argument:" + a);

            return a;
        } catch (JAXBException e) {
            throw new PDMException(e);
        }
    }

    public void insertProjectQuery(
            String projectName,
            String name,
            String type,
            String language,
            String description,
            String relativeFilePath,
            List arguments,
            List returnVariables, 
            String queryScript
            ) {

        try {
            // check if Query already exists
            if (checkDuplicateProjectQuery(projectName, name) ) {
                throw new PDMException("Query Already Exists in Project Repository: " +
                        name);

            }

            QueryType query =
                    createQuery(name, type, language, description,
                            relativeFilePath, arguments, returnVariables);

            PDMQueryList projectList = (PDMQueryList) projectQueryListMap.get(projectName);

            if (projectList == null) {
                // project list was not loaded, try and load
                List projectQueryList = getProjectQueryList(projectName);
                if (projectQueryList == null)
                    throw new PDMException("No such project to insert Query:" + projectName);

                // getProject inserted the query list into the map, get it and add
                projectList = (PDMQueryList) projectQueryListMap.get(projectName);
            }

            if (projectList != null) {
                // save the script to the file
                saveProjectScript(projectName, query, queryScript);

                projectList.getQuery().add(query);
            }

        } catch (Exception e) {
            throw new PDMException(e);
        }
    }

    public void insertGlobalQuery(
            String name,
            String type,
            String language,
            String description,
            String relativeFilePath,
            List arguments,
            List returnVariables
            ) {

        try {
            // check if Query already exists
            if (checkDuplicateProjectQuery(null, name) ) {
                throw new PDMException("Query '"+ name + "'Already Exists in Global Repository.\nDuplicates query names are not allowed.");
            }

            QueryType query =
                    createQuery(name, type, language, description,
                            relativeFilePath, arguments, returnVariables);

//            // save the script to the file
//            saveGlobalScript(query, queryScript);

            globalQueryList.getQuery().add(query);
                        
        } catch (Exception e) {
            throw new PDMException(e);
        }
    }

    public void saveProjectScript(String projectName, QueryType query, String queryScript) {
        String file =
                ConfigService
                .getInstance()
                .getProjectQueryDetailsFilePath(
                            query.getRelativeFilePath());

        saveScriptToFile(file, queryScript);
    }

    public boolean deleteGlobalScript(QueryType query) {
        String file =
                ConfigService
                .getInstance()
                .getGlobalPDMQueryDetailsFilePath(
                            query.getRelativeFilePath());
        File f = new File(file);
        return f.delete();
    }

    public void saveGlobalScript(QueryType query, String queryScript) {
        String file =
                ConfigService
                .getInstance()
                .getGlobalPDMQueryDetailsFilePath(
                            query.getRelativeFilePath());

        saveScriptToFile(file, queryScript);
    }

    private void saveScriptToFile(String file, String queryScript) {
        File f = new File(file);

        if (f.exists()) {
            // delete file if exists
            f.delete();
        }

        try {
            f.createNewFile();
            FileWriter fw = new FileWriter(f);
            BufferedWriter w = new BufferedWriter(fw);
            w.write(queryScript);
            w.flush();
            w.close();
            fw.close();
        } catch (FileNotFoundException e) {
            throw new PDMException("File Not Found: " + file, e);
        } catch (IOException e) {
            throw new PDMException("Failed to write file: " + file, e);
        }
    }

    public boolean checkDuplicateGlobalQuery(String queryName) {
        return checkDuplicateProjectQuery(null, queryName);
    }

    public boolean checkDuplicateProjectQuery(String projectName, String queryName) {
        if (projectName == null) {
            // check if this is a duplicate in the global list
            List queries = globalQueryList.getQuery();
            return checkDuplicateInList(queries, queryName);
        }

        // get the project query list
        PDMQueryList projectList = (PDMQueryList) projectQueryListMap.get(projectName);
        if (projectList == null)
            return false;

        return checkDuplicateInList(projectList.getQuery(), queryName);
    }

    private boolean checkDuplicateInList(List queries, String queryName) {
        Iterator iter = queries.iterator();
        while (iter.hasNext()) {
            QueryType queryType = (QueryType) iter.next();
            if (queryType.getName().equalsIgnoreCase(queryName)) {
                return true;
//                throw new PDMException(
//                        "Attempting to create duplicate Query: " + queryName);
            }
        }

        return false;
    }

    public void refreshCache() {
        initialize();
    }


    public static void main(String args[]) {

        PatternQueryRepository patternQueryRepository = PatternQueryRepositoryImpl.getInstance();
                 //"c:\\dev\\salsadevcvs\\salsa\\src\\main"); 
        System.out.println(patternQueryRepository);

        patternQueryRepository.printGlobalQueryList();

        patternQueryRepository.getProjects();

        Iterator qlist = patternQueryRepository.getGlobalQueryList().iterator();

        /*while (qlist.hasNext()) {
            QueryType q = (QueryType) qlist.next();
            System.out.println("Statement=" + patternQueryRepository.getGlobalQueryFile(q.getName()));
        } */

        String pql = patternQueryRepository.getGlobalQueryFile("Servlets");
        System.out.println("query = '" + pql +"'");

        // add a new Query to the list

        patternQueryRepository.save();

        //patternQueryRepository.printQueryList();

    }

    public List getProjects() {
        ArrayList projects = new ArrayList();
        if (projectQueryListMap.size() != 0)
            projects.addAll(projectQueryListMap.keySet());

        return projects;
    }

    public void printAllProjectQueryLists() {
        Iterator projects = getProjects().iterator();
        while (projects.hasNext()) {
            printProjectQueryList((String)projects.next());
        }
    }

    public void printProjectQueryList(String projectName) {

        List queryList = getProjectQueryList(projectName);

        if (queryList == null) {
            logger.info("Query List Empty for project:" + projectName);
        }

//        System.out.println("Queries in project: "  + projectName);
//        for (Object qobj: queryList) {
        for (Iterator iter = queryList.iterator(); iter.hasNext();) {
            Object qobj = iter.next();
            QueryType query= (QueryType) qobj;
            logger.info("Project '"+ projectName + "' Query = " + query);
        }
    }


    public void printGlobalQueryList() {

        System.out.println("Global Queries");
//        for (Object qobj: getGlobalQueryList()) {
        for (Iterator iter = getGlobalQueryList().iterator(); iter.hasNext();) {
            Object qobj = iter.next();
            QueryType query= (QueryType) qobj;
            System.out.println("Query = " + query.getName());
        }
    }

    public String toString() {
        return "PatternQueryRepository {" +
               "globalQueryList=" + globalQueryList +
               "}";
    }

    private File createTempPDMFile(String fileName) throws IOException {
        File tmpFile = File.createTempFile(PDMXMLConstants.PQL_TEMP_FILE_PREFIX, PDMXMLConstants.PQL_FILE_EXTENSION);
        tmpFile.deleteOnExit();

        Writer writer = new FileWriter(tmpFile);
        writer.write("");
        writer.flush();
        return tmpFile;
    }

    public ReturnVariable createReturnVariable(String name, String type, String description) {
        try {
            validator.validateReturnVariable(name,
                    type, description);

            ReturnVariable a = factory.createReturnVariable();

            a.setName(name);
            a.setType(type);
            a.setDescription(description);

            logger.info("Created ReturnVariable:" + a);

            return a;
        } catch (JAXBException e) {
            throw new PDMException(e);
        }

    }

    public boolean deleteGlobalQuery(QueryType queryType) {

        QueryType del = getGlobalQuery(queryType.getName());
        boolean deletedQ = globalQueryList.getQuery().remove(del);

        boolean deletedS = deleteGlobalScript(queryType);

        // save the updated list
        save();

        StringBuffer sbuf = new StringBuffer();
        if (!deletedQ) {
            sbuf.append("Query '"
                    + queryType.getName()
                    + "' could not be deleted from Repository");
        }
        if (!deletedS) {
            sbuf.append("Query Script for query '"
                    + queryType.getName()
                    + "' could not be deleted from Repository.\nFile Path '"
                    + ConfigService.getInstance()
                        .getGlobalPDMQueryDetailsFilePath(
                            del.getRelativeFilePath())
                    + "'");
        }

        if (!deletedQ || !deletedS) {
            throw new PDMException(sbuf.toString());
        }

        return true;
    }

    public void insertGlobalQuery(QueryType query) {
        if (query == null) {
            throw new PDMException("Query cannot be null to insert into Repository");
        }
        if (checkDuplicateProjectQuery(null, query.getName())) {
            throw new PDMException("Query '"
                    + query.getName()
                    + "'already exists in Repository");
        }
        insertGlobalQuery(query.getName(), query.getType(),
                query.getLanguage(), query.getDescription(),
                query.getRelativeFilePath(),
                query.getArgument(), query.getReturnVariable()
                );
    }

    public QueryType cloneQuery(QueryType query) {
        QueryType newQuery = createNewQuery();

        newQuery.setName(query.getName());
        newQuery.setDescription(query.getDescription());
        newQuery.setLanguage(query.getLanguage());
        newQuery.setRelativeFilePath(query.getRelativeFilePath());
        newQuery.setType(query.getType());

        if (query.getArgument() != null) {
            for (int i=0; i < query.getArgument().size(); i++) {
                ArgumentType newArg = cloneArgument((ArgumentType) query.getArgument().get(i));
                newQuery.getArgument().add(newArg);
            }
        }

        if (query.getReturnVariable() != null) {
            for (int i=0; i < query.getReturnVariable().size(); i++) {
                ReturnVariableType newRet = cloneReturnVariableType((ReturnVariableType)
                        query.getReturnVariable().get(i));
                newQuery.getReturnVariable().add(newRet);
            }
        }

        return newQuery;
    }

    public ReturnVariableType cloneReturnVariableType(ReturnVariableType ret) {
        ReturnVariableType newRet = createReturnVariable(ret.getName(), ret.getType(), ret.getDescription());
        return newRet;
    }

    public ArgumentType cloneArgument(ArgumentType arg) {
        ArgumentType newArg = createNewArgument();
        newArg.setName(arg.getName());
        newArg.setDescription(arg.getDescription());
        newArg.setRelatedQueryName(arg.getRelatedQueryName());
        newArg.setRelatedQueryReturnVariableName(arg.getRelatedQueryReturnVariableName());
        newArg.setType(arg.getType());
        newArg.setValue(arg.getValue());

        return newArg;

    }

    private ArgumentType createNewArgument() {
        try {
            return factory.createArgumentType();
        } catch (JAXBException e) {
            throw new PDMException(e);
        }
    }

    public void updateGlobalQuery(QueryType originalQuery, QueryType currentQuery) {
        AcreErrors errors = PDMQueryValidator.getInstance().validateQuery(currentQuery);

        originalQuery.setName(currentQuery.getName());
        originalQuery.setDescription(currentQuery.getDescription());
        originalQuery.setLanguage(currentQuery.getLanguage());
        originalQuery.setRelativeFilePath(currentQuery.getRelativeFilePath());
        originalQuery.setType(currentQuery.getType());

        originalQuery.getArgument().clear();
        if (currentQuery.getArgument() != null)
            originalQuery.getArgument().addAll(currentQuery.getArgument());

        originalQuery.getReturnVariable().clear();

        if (originalQuery.getReturnVariable() != null)
            originalQuery.getReturnVariable().addAll(currentQuery.getReturnVariable());

        return;
    }
}
