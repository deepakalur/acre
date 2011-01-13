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

import org.acre.common.AcreErrors;
import org.acre.common.AcreStringUtil;
import org.acre.config.ConfigService;
import org.acre.pdm.*;
import org.acre.pdmqueries.QueryType;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.math.BigInteger;
import java.util.*;
import java.util.logging.Logger;

class PatternRepositoryImpl implements PatternRepository {
    private static PatternRepositoryImpl ourInstance;
    private PDMList globalPDMList;
    private PDMValidator validator = PDMValidator.getInstance();

    // create a PDM object
    private ObjectFactory factory = new ObjectFactory();
    private static final String COREJ2EEPATTERNS = "CoreJ2EEPatterns";

    private String FILE_PREPEND_STRING =
        "<PDMList xmlns=\"http://java.sun.com/xml/ns/acre/\"" +
        "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
        "xsi:schemaLocation=\"http://java.sun.com/xml/ns/acre/\n" +
        "AcrePDMSchema.xsd\">";

    private static final String PDM_SCHEMA_LOCATION =
            "http://java.sun.com/xml/ns/acre/\n"+
            ConfigService.PDMSCHEMAFILENAME
            ;


    private HashMap projectPDMListMap=new HashMap();
    private static final String PDM_PACKAGE_NAME = "org.acre.pdm";
    private Logger log;

    public static PatternRepositoryImpl getInstance() {
        if (ourInstance == null) {
            ourInstance = new PatternRepositoryImpl();
        }
        return ourInstance;
    }

    private PatternRepositoryImpl() {
        initialize();
    }

    private void initialize() {
        if (projectPDMListMap == null)
            projectPDMListMap = new HashMap();

        if (log == null)
            log = ConfigService.getInstance().getLogger(this);

        // load global PDM file
        load();

        // load all projects query list
        Set projects = projectPDMListMap.keySet();
        Iterator projIter = projects.iterator();
        while (projIter.hasNext()) {
            String projectName = (String) projIter.next();

            // load project
            PDMList list = load(ConfigService
                                    .getInstance()
                                    .getProjectPDMXMLFilePath(projectName));
            projectPDMListMap.put(projectName, list);
        }
    }

    public void load() {
        if (globalPDMList == null) {
            globalPDMList = load(ConfigService
                                .getInstance()
                                .getGlobalPDMXMLFilePath());
        }
    }


    public void setGlobalPatternModel(InputStream is) {
        globalPDMList = load(is);
    }

    private PDMList load(String pdmFilePath) {
        FileInputStream is = null;
        try {
            log.info("Loading PDM File:" + pdmFilePath);
            is = new FileInputStream(pdmFilePath);
            return load(is);
        } catch (FileNotFoundException e) {
            throw new PDMException(e);
        }
    }

    private PDMList load(InputStream is) {

        PDMList list=null;
        // Create JAXBContext object to handle generated classes in "com.sun.salsa.syscatalog" package
        JAXBContext jaxbCtx = null;
        try {
            jaxbCtx = JAXBContext.newInstance(PDM_PACKAGE_NAME);

            // Unmarshaller to unmarshal xml to java object content tree
            Unmarshaller unmarshaller = jaxbCtx.createUnmarshaller();

            list = (PDMList)unmarshaller.unmarshal(is);
            is.close();

        } catch (JAXBException e) {
            throw new PDMException(e);
        } catch (IOException ioe) {
            throw new PDMException(ioe);
        }

        return list;
    }

    public void refresh() {
        globalPDMList = load(ConfigService.getInstance().getGlobalPDMXMLFilePath());
    }

    public void refresh(String projectName) {
        PDMList projectList = load(ConfigService
                .getInstance()
                .getProjectPDMXMLFilePath(projectName));
        projectPDMListMap.put(projectName, projectList);
    }

    public void save(String projectName) {
        save(ConfigService.getInstance().getProjectPDMXMLFilePath(projectName),
                (PDMList) projectPDMListMap.get(projectName));
//        refresh (projectName);
    }

    public void save() {
        save(ConfigService.getInstance().getGlobalPDMXMLFilePath(), globalPDMList);
//        refresh();
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

    public List getProjects() {
        ArrayList projects = new ArrayList();

        if (projectPDMListMap.size() != 0)
            projects.addAll(projectPDMListMap.keySet());

        return projects;
    }

    private void save(String filepath, PDMList list) {

        // Create JAXBContext object to handle generated classes in "com.sun.salsa.syscatalog" package
        JAXBContext jaxbCtx = null;

        try {
            jaxbCtx = JAXBContext.newInstance(PDM_PACKAGE_NAME);

            // Unmarshaller to unmarshal xml to java object content tree
            Marshaller marshaller = jaxbCtx.createMarshaller();

            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
                    Boolean.TRUE);
            marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION,
                        PDM_SCHEMA_LOCATION
                    );
//            marshaller.setProperty(
//                    Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION,
//                    ConfigService.getInstance().getPDMSchemaPath());

            FileOutputStream os = null;

            validateBeforeSave(marshaller, list);

            // if it works for the temp file, it works for the real file
            File file = new File(filepath);

            if (! file.exists()) {
                file.createNewFile();
            }

            log.info("Got PDM File:" + file);
            os = new FileOutputStream(file);
            marshaller.marshal(list, os);
            os.flush();
            os.close();

            log.info("Saved PDM File:" + file);

        } catch (JAXBException e) {
            throw new PDMException(e);
        } catch (IOException ioe) {
            throw new PDMException(ioe);
        }
        finally {
            log.info("Successfully Saved PDM File");
        }
    }

    private void validateBeforeSave(Marshaller marshaller, PDMList list) {
        File tmpfile = null;

        try {
            tmpfile = createTempPDMFile(ConfigService.getInstance().getGlobalPDMXMLFilePath());
            log.info("Created Temp File:" + tmpfile);
            FileOutputStream tmpos = new FileOutputStream(tmpfile);
            marshaller.marshal(list, tmpos );
            tmpos .flush();
            tmpos .close();
            log.info("Successfully closed Temp File:" + tmpfile);
        } catch (JAXBException e) {
            throw new PDMException("PDM Save Validation Failed", e);
        }catch (IOException ioe) {
            throw new PDMException("Failed to create Temp file:" + tmpfile, ioe);
        } finally {
            // delete Temp file
            log.info("Deleting temp file:" + tmpfile);
            tmpfile.delete();
        }
    }

    public List getProjectPatternModels(String projectName) {
        if (projectPDMListMap.containsKey(projectName)) {
            PDMList list = (PDMList) projectPDMListMap.get(projectName);
            return list.getPDM();
        }

        // does not exist. load and create
        PDMList projectList = load(
                ConfigService.getInstance().getProjectPDMXMLFilePath(projectName));

        if (projectList != null) {
            projectPDMListMap.put(projectName, projectList);
            return projectList.getPDM();
        }

        return null;

    }

    public PDMType getProjectPatternModel(String projectName, String pdmName) {
        PDMList list = (PDMList) projectPDMListMap.get(projectName);
        if (list == null)
            throw new PDMException("No such project to get PDM:" + projectName);

//        for (Object pdmobj: list.getPDM()) {
        for (Iterator iter = list.getPDM().iterator(); iter.hasNext();) {
            Object pdmobj = iter.next();
            PDMType pdm = (PDMType) pdmobj;
            if ( pdmName.equalsIgnoreCase(pdm.getName()) )
                return pdm;
        }

        return null;

    }

    public List getProjectPatternModelRoles(String project, String pdmName) {
        return getProjectPatternModel(project, pdmName).getRoles().getRole();
    }

    public List getProjectPatternModelRelationships(String project, String pdmName) {
        return getProjectPatternModel(project, pdmName).getRelationships().getRelationship();
    }

    public List getGlobalPatternModels() {
        return globalPDMList.getPDM();
    }

    public PDMType getGlobalPatternModel(String pdmName) {
        if (pdmName == null)
            return null;

//        for (Object pdmobj: globalPDMList.getPDM()) {
        for (Iterator iter = globalPDMList.getPDM().iterator(); iter.hasNext();) {
            Object pdmobj = iter.next();
            PDMType pdm = (PDMType) pdmobj;
            if (pdmName.equalsIgnoreCase(pdm.getName()))
                return pdm;
        }

        return null;
    }

    public List getGlobalPatternModelRoles(String pdmName) {
        return getGlobalPatternModel(pdmName).getRoles().getRole();
    }

    public List getGlobalPatternModelRelationships(String pdmName) {
        return getGlobalPatternModel(pdmName).getRelationships().getRelationship();
    }

    public RelationshipType createRelationship(String name, String type, String fromRole, String toRole) {
        try {
//            if (!validator.isValidRelationshipType(type)) {
//                throw new PDMException("Attempting to create Relationship with Invalid Relationship Type: type =" + type);
//            }

            RelationshipType r = factory.createRelationship();
            r.setName(name);
            r.setType(type);
            r.setToRole(toRole);
            r.setFromRole(fromRole);

            log.info("Created Relationship:" + r);

            return r;
        } catch (JAXBException e) {
            throw new PDMException(e);
        }
    }

    public RelationshipsType createRelationships() {
        try {
            return factory.createRelationshipsType();
        } catch (JAXBException e) {
            throw new PDMException (e);
        }
    }

    public RolesType createRoles() {
        try {
            return factory.createRolesType();
        } catch (JAXBException e) {
            throw new PDMException(e);
        }
    }



    public RoleType createRole(
            String name, 
            String type, 
            String sequence,
            String typeReferenceName, 
            String queryName,
            String returnVariableName,
            List arguments
            ) {
        try {
//            if (!validator.isValidRoleType(type)) {
//                throw new PDMException("Attempting to create Role with Invalid Role Type: type =" + type);
//            }

            RoleType r = factory.createRoleType();
            r.setName(name);
            r.setType(type);
            r.setSequence(new BigInteger(sequence));
            r.setTypeReferenceName(typeReferenceName);
            r.setQueryName(queryName);
            r.setReturnVariableName(returnVariableName);

            if (arguments != null) {
                r.getArgument().clear();
                r.getArgument().addAll(arguments);
            }

            log.info("Created Role:" + r);
            return r;
        } catch (JAXBException e) {
            throw new PDMException(e);
        }
    }


    public RoleType createAnnotatedRole(
            String name,
            String sequence,
            String repository,
            List arguments
            ) {
        try {
//            if (!validator.isValidRoleType(type)) {
//                throw new PDMException("Attempting to create Role with Invalid Role Type: type =" + type);
//            }

            RoleType r = factory.createRoleType();
            r.setName(name);
            r.setType(PDMXMLConstants.ROLE_TYPE_ANNOTATED);
            r.setSequence(new BigInteger(sequence));

            if (arguments != null) {
                r.getArgument().clear();
                r.getArgument().addAll(arguments);
            }
            r.setRepository(repository);

            log.info("Created Role:" + r);
            return r;
        } catch (JAXBException e) {
            throw new PDMException(e);
        }
    }



    public void insertGlobalPatternModel(
                String name,
                String description,
                String category,
                String tier,
                String type,
                String attributes,
                String scriptedPDMPath,
                String factModelType,
                List roles,
                List relationships) {
        insertPatternModel(globalPDMList, name, description, category, tier, type, attributes,
                scriptedPDMPath, factModelType, roles, relationships);
    }

    public void insertProjectPatternModel(String projectName,
                String name,
                String description,
                String category,
                String tier,
                String type,
                String attributes,
                String scriptedPDMPath,
                String factModelType,
                List roles,
                List relationships) {

            PDMList projectList = (PDMList) projectPDMListMap.get(projectName);
            if (projectList == null) {
                // list was not loaded, try to load it
                List projectPDMList = getProjectPatternModels(projectName);
                if (projectPDMList == null) {
                    throw new PDMException("No such project found to insert PDM: " + projectName);
                }

                // getProjectPatternModels created and inserted the PDM List. get it and add
                projectList = (PDMList) projectPDMListMap.get(projectName);
            }

            insertPatternModel(projectList,
                    name,
                    description,
                    category,
                    tier,
                    type,
                    attributes,
                    scriptedPDMPath,
                    factModelType,
                    roles,
                    relationships);

    }

    public void insertPatternModel(PDMList list,
                String name,
                String description,
                String category,
                String tier,
                String type,
                String attributes,
                String scriptedPDMPath,
                String factModelType,
                List roles,
                List relationships) {


        PDMType pdm = createPDMType(
                name,
                description,
                category,
                tier,
                type,
                attributes,
                scriptedPDMPath,
                factModelType,
                roles,
                relationships);

        AcreErrors errors = validator.validatePDM(pdm);
        if (! errors.isEmpty() ) {
            throw new PDMException(errors, "Failed to insert PDM");
        }

        // check if PDM already exists
        checkDuplicateGlobalPDM(name);

        // check ok. add and return
        list.getPDM().add(pdm);

    }

    private void checkDuplicateGlobalPDM(String pdmName) {
        if (pdmName==null)
            throw new PDMException("Cannot check duplicate for PDMName=null");

//        for (Object pdmobj: globalPDMList.getPDM()) {
        for (Iterator iter = globalPDMList.getPDM().iterator(); iter.hasNext();) {
            Object pdmobj = iter.next();
            PDMType pdm = (PDMType) pdmobj;
            if (pdm.getName().equalsIgnoreCase(pdmName)) {
                throw new PDMException("Attempting to create duplicate PDM: " + pdmName);
            }
        }
    }

    private PDMType createPDMType(
                String name,
                String description,
                String category,
                String tier,
                String type,
                String attributes,
                String scriptedPDMPath,
                String factModelType,
                List roles,
                List relationships
                                  ) {

        try {
            PDMType newPDM = factory.createPDM();

            RelationshipsType relsType = factory.createRelationships();
            RolesType rolesType = factory.createRoles();

            if (relationships != null)
                relsType.getRelationship().addAll(relationships);

            if (roles != null)
                rolesType.getRole().addAll(roles);

            newPDM.setCategory(category);
            newPDM.setDescription(description);
            newPDM.setName(name);
            newPDM.setRelationships(relsType);
            newPDM.setRoles(rolesType);
            newPDM.setTier(tier);
            newPDM.setType(type);
            newPDM.setAttributes(attributes);
            newPDM.setScriptedPDMPath(scriptedPDMPath);
            newPDM.setFactModelType(factModelType);
            return newPDM;
        } catch (JAXBException e) {
            throw new PDMException(e);
        }

    }

    public void refreshCache() {
        initialize();
    }


    public static void main(String args[]) {

        PatternRepository facade = PatternRepositoryImpl.getInstance();
        System.out.println(facade);

        facade.printPatternModels();

        // add a new PDM to the list

        List roles = new ArrayList();
        List rels = new ArrayList();

        RoleType role = facade.createRole(
                "BusinessDelegate",
                PDMXMLConstants.ROLE_TYPE_QUERY,
                "1",
                null,
                "BusinessDelegate",
                "BusinessDelegate",
                null
        );

        roles.add(role);

        facade.insertGlobalPatternModel(
                "BusinessDelegate",
                "Identifies all Business Delegates",
                COREJ2EEPATTERNS,
                PDMXMLConstants.PDM_TIER_TYPE_BUSINESS,
                PDMXMLConstants.PDM_TYPE_MODEL_PDM,
                "attributes",
                PDMXMLConstants.DEFAULT_PDM_SCRIPTED_PDM_PATH,
                PDMXMLConstants.DEFAULT_PDM_FACT_MODEL_TYPE,
                roles,
                null);

        roles.clear();

        roles.add(facade.createRole(
                "SessionFacadeHome", PDMXMLConstants.ROLE_TYPE_QUERY,
                    "1", null, "SessionFacadeHome", "SessionFacadeHome", null));
        roles.add(facade.createRole("SessionFacadeInterface", PDMXMLConstants.ROLE_TYPE_QUERY,
                    "2", null, "SessionFacadeInterface", "SessionFacadeInterface", null));
        roles.add(facade.createRole("SessionFacadeBean", PDMXMLConstants.ROLE_TYPE_QUERY,
                    "3", null, "SessionFacadeBean", "SessionFacadeBean", null));


        rels.add(facade.createRelationship("Creates",
                PDMXMLConstants.RELATIONSHIP_TYPE_CREATES,
                        "SessionFacadeHome", "SessionFacadeBean"));
        rels.add(facade.createRelationship("Creates",
                PDMXMLConstants.RELATIONSHIP_TYPE_CREATES,
                "SessionFacadeHome", "SessionFacadeBean"));

        facade.insertGlobalPatternModel(
                "SessionFacade",
                "Identifies Session Facades",
                COREJ2EEPATTERNS,
                PDMXMLConstants.PDM_TIER_TYPE_BUSINESS,
                PDMXMLConstants.PDM_TYPE_MODEL_PDM,
                "attributes",
                PDMXMLConstants.DEFAULT_PDM_SCRIPTED_PDM_PATH,
                PDMXMLConstants.DEFAULT_PDM_FACT_MODEL_TYPE,
                roles,
                null);

        facade.printPatternModels();

        facade.save();

        facade.printPatternModels();

    }

    public void printPatternModels() {
//        for (Object pdmobj: getGlobalPatternModels()) {
        for (Iterator iter = getGlobalPatternModels().iterator(); iter.hasNext();) {
            Object pdmobj = iter.next();
            PDMType pdm = (PDMType) pdmobj;
            log.info("PDM = " + pdm.getName() + "; " +
                    pdm.getRoles());
        }
    }

    public String toString() {
        return "PatternRepository {" +
               "globalPDMList=" + globalPDMList +
               "}";
    }

    private File createTempPDMFile(String fileName) throws IOException {
        File tmpFile = File.createTempFile("pdm", ".pdm");
        tmpFile.deleteOnExit();

        Writer writer = new FileWriter(tmpFile);
        writer.write("");
        writer.flush();
        return tmpFile;
    }

    public PDMType createNewPatternModel() {

        PDMType pdm = createPDMType(
                PDMXMLConstants.NEW_PDM_NAME,
                PDMXMLConstants.DEFAULT_DESCRIPTION,
                PDMXMLConstants.DEFAULT_PDM_CATEGORY_TYPE,
                PDMXMLConstants.DEFAULT_PDM_TIER_TYPE,
                PDMXMLConstants.DEFAULT_PDM_TYPE,
                PDMXMLConstants.DEFAULT_PDM_ATTRIBUTES,
                PDMXMLConstants.DEFAULT_PDM_SCRIPTED_PDM_PATH,
                PDMXMLConstants.DEFAULT_PDM_FACT_MODEL_TYPE,
                null,
                null);

        return pdm;
    }

    public boolean deleteGlobalPatternModel(PDMType pdmType) {
        PDMType del = getGlobalPatternModel(pdmType.getName());
        boolean deleted = globalPDMList.getPDM().remove(del);

        save();

        if (!deleted) {
            throw new PDMException("PDM '"
                    + pdmType.getName()
                    + "' could not be deleted from Repository");
        }

        return true;

    }

    public RoleType createNewRole() {
        RoleType role = null;
        return createRole(
                "",
                PDMXMLConstants.DEFAULT_PDM_ROLE_TYPE,
                "0",
                "",
                "",
                "",
                null);
    }

    public RelationshipType createNewRelationship() {
        return createRelationship(
                "",
                "",
                "",
                "");
    }

    public void insertGlobalPatternModel(PDMType newPDM) {
        insertGlobalPatternModel(
                newPDM.getName(),
                newPDM.getDescription(),
                newPDM.getCategory(),
                newPDM.getTier(),
                newPDM.getType(),
                newPDM.getAttributes(),
                newPDM.getScriptedPDMPath(),
                newPDM.getFactModelType(),
                (newPDM.getRoles()!=null)?newPDM.getRoles().getRole():null,
                (newPDM.getRelationships() != null)?newPDM.getRelationships().getRelationship():null);
    }

    public PDMType clonePatternModel(PDMType pdm) {
        PDMType newPDM = createNewPatternModel();
        newPDM.setName(pdm.getName());
        newPDM.setDescription(pdm.getDescription());
        newPDM.setCategory(pdm.getCategory());
        newPDM.setTier(pdm.getTier());
        newPDM.setAttributes(pdm.getAttributes());
        newPDM.setScriptedPDMPath(pdm.getScriptedPDMPath());
        newPDM.setFactModelType(pdm.getFactModelType());

        RolesType roles = createRoles();
        if ( pdm.getRoles() != null ) {
            roles.setType(pdm.getRoles().getType());
            roles.setQueryName(pdm.getRoles().getQueryName());
            roles.setReturnVariableName(pdm.getRoles().getReturnVariableName());
        }
        newPDM.setRoles(roles);

        RelationshipsType rels = createRelationships();
        newPDM.setRelationships(rels);

        if (pdm.getRoles() != null) {
            if (pdm.getRoles().getRole() != null) {
                for (int i=0; i < pdm.getRoles().getRole().size(); i++) {
                    RoleType role = (RoleType) pdm.getRoles().getRole().get(i);
                    RoleType newRole = cloneRole(role);
                    newPDM.getRoles().getRole().add(newRole);
                }
            }
        }

        if (pdm.getRelationships() != null) {
            if (pdm.getRelationships().getRelationship() != null) {
                for (int i=0; i < pdm.getRelationships().getRelationship().size(); i++) {
                    RelationshipType rel = (RelationshipType) pdm.getRelationships().getRelationship().get(i);
                    RelationshipType newRel = cloneRelationship(rel);
                    newPDM.getRelationships().getRelationship().add(newRel);
                }
            }
        }

        return newPDM;
    }

    public RelationshipType cloneRelationship(RelationshipType rel) {
        RelationshipType newRel = null;
        newRel = createRelationship(rel.getName(), rel.getType(), rel.getFromRole(), rel.getToRole());

        return newRel;
    }

    public RoleType cloneRole (RoleType role) {
        RoleType newRole = createRole(
                role.getName(),
                role.getType(),
                (role.getSequence()==null)?null:role.getSequence().toString(),
                role.getTypeReferenceName(),
                role.getQueryName(),
                role.getReturnVariableName(),
                null);

        List args = cloneArgumentList(role.getArgument());

        List currArgs = newRole.getArgument();
        currArgs.addAll((List)args);

        return newRole;

    }

    public List cloneArgumentList(List argument) {

        List list = new ArrayList();

        if ((argument == null) || (argument.size() == 0)) {
            return list;
        }

        for (int i=0; i < argument.size(); i++ ) {
            ArgumentType arg = (ArgumentType) argument.get(i);
            ArgumentType newArg = cloneArgument(arg);
            list.add(newArg);
        }

        return list;
    }

    public ArgumentType cloneArgument(ArgumentType arg) {
        ArgumentType newArg = createArgumentType();
        newArg.setName(arg.getName());
        newArg.setSequence(arg.getSequence());
        newArg.setType(arg.getType());
        newArg.setValue(arg.getValue());

        return newArg;

    }

    private ArgumentType createArgumentType() {
        try {
            return factory.createArgumentType();
        } catch (JAXBException e) {
            throw new PDMException(e);
        }
    }

    public void updatePatternModel(PDMType originalPDM, PDMType currentPDM) {
        originalPDM.setName(currentPDM.getName());
        originalPDM.setAttributes(currentPDM.getAttributes());
        originalPDM.setCategory(currentPDM.getCategory());
        originalPDM.setDescription(currentPDM.getDescription());
        originalPDM.setFactModelType(currentPDM.getFactModelType());
        originalPDM.setTier(currentPDM.getTier());
        originalPDM.setType(currentPDM.getType());
        originalPDM.setScriptedPDMPath(currentPDM.getScriptedPDMPath());

        originalPDM.setRoles(currentPDM.getRoles());
        originalPDM.setRelationships(currentPDM.getRelationships());

        return;
    }

    public List getGlobalPatternModelNames() {
        ArrayList names = new ArrayList();
        for (int i = 0; i < globalPDMList.getPDM().size(); i++) {
            PDMType p = (PDMType) globalPDMList.getPDM().get(i);
            names.add(p.getName());
        }
        return names;
    }

    public List getGlobalPatternRoleNames(String pdmName) {
        List roles = getGlobalPatternModelRoles(pdmName);

        ArrayList names = new ArrayList();
        for (int i = 0; i < roles.size(); i++) {
            RoleType r = (RoleType) roles.get(i);
            names.add(r.getName());
        }
        return names;
    }

    public ArgumentType createRoleArgument(
            String sequence,
            String argumentName,
            String type,
            String value) {

        ArgumentType arg = createArgumentType();
        if (! AcreStringUtil.isEmpty(sequence)) {
            arg.setSequence(new java.math.BigInteger(sequence));
        } else {
            arg.setSequence(new java.math.BigInteger("0"));
        }
        arg.setName(argumentName);
        arg.setType(type);
        arg.setValue(value);

        return arg;
    }

    public void createPatternModelFromQueries(String pdmName, QueryType[] selectedQueries) {
        PDMType pdm = createNewPatternModel();
        pdm.setName(pdmName);
        pdm.setType(PDMXMLConstants.PDM_TYPE_MODEL_PQLPDM);
        pdm.setDescription("PDM created with a multiple queries as roles");

        for (int i =0; i < selectedQueries.length; i++) {
            QueryType query = selectedQueries[i];

            RoleType role = getInstance().createNewRole();
            role.setName(query.getName());
            role.setType(PDMXMLConstants.ROLE_TYPE_QUERY);
            role.setQueryName(query.getName());
            role.setSequence(new BigInteger("" + i));
            role.setTypeReferenceName("");
            pdm.getRoles().getRole().add(role);
        }
        getGlobalPatternModels().add(pdm);
        save();
    }

    public void createPatternModelFromQuery(String pdmName, String queryName) {
        PDMType pdm = getInstance().createNewPatternModel();
        pdm.setName(pdmName);
        pdm.setType(PDMXMLConstants.PDM_TYPE_MODEL_PQLPDM);
        pdm.setDescription("PDM created with a single query '"+
                pdmName
                + "' as a role");
        RoleType role = getInstance().createNewRole();
        role.setName("QueryRole");
        role.setType(PDMXMLConstants.ROLE_TYPE_QUERY);
        role.setQueryName(queryName);
        role.setSequence(new BigInteger("0"));
        role.setTypeReferenceName("");
        pdm.getRoles().getRole().add(role);
        getGlobalPatternModels().add(pdm);
        save();
    }
}
