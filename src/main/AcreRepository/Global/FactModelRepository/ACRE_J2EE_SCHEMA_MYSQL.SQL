-- ACE-GENERATED FILE: REMOVE THIS LINE TO KEEP YOUR CHANGES
DROP TABLE IF EXISTS J2EE_Application ;
DROP TABLE IF EXISTS War_Module ;
DROP TABLE IF EXISTS Context_Param ;
DROP TABLE IF EXISTS Ear_Module ;
DROP TABLE IF EXISTS Filter ;
DROP TABLE IF EXISTS Servlet ;
DROP TABLE IF EXISTS Servlet_Mapping ;
DROP TABLE IF EXISTS Security_Constraint ;
DROP TABLE IF EXISTS Web_Resource ;
DROP TABLE IF EXISTS Auth_Constraint ;
DROP TABLE IF EXISTS User_Data_Constraint ;
DROP TABLE IF EXISTS HTTP_Method ;
DROP TABLE IF EXISTS Web_Role_Name ;
DROP TABLE IF EXISTS Web_Security_Role ;
DROP TABLE IF EXISTS Web_Env_Entry ;
DROP TABLE IF EXISTS Servlet_Init_Param ;
DROP TABLE IF EXISTS Tag_Lib ;
DROP TABLE IF EXISTS URL_Pattern ;
DROP TABLE IF EXISTS Filter_Init_Param ;
DROP TABLE IF EXISTS Web_Resource_Ref ;
DROP TABLE IF EXISTS Web_Resource_Env_Ref ;
DROP TABLE IF EXISTS Web_EJB_Ref ;
DROP TABLE IF EXISTS Web_EJB_Local_Ref ;
DROP TABLE IF EXISTS Mime_Mapping ;
DROP TABLE IF EXISTS Welcome_File ;
DROP TABLE IF EXISTS Error_Page ;
DROP TABLE IF EXISTS Web_Security_Role_Ref ;
DROP TABLE IF EXISTS Entity_Bean ;
DROP TABLE IF EXISTS Session_Bean ;
DROP TABLE IF EXISTS Message_Driven_Bean ;
DROP TABLE IF EXISTS CMP_Field ;
DROP TABLE IF EXISTS Query ;
DROP TABLE IF EXISTS EJB_Method ;
DROP TABLE IF EXISTS EJB_Method_Parameter ;
DROP TABLE IF EXISTS Assembly_Descriptor ;
DROP TABLE IF EXISTS EJB_Resource_Ref ;
DROP TABLE IF EXISTS EJB_Env_Entry ;
DROP TABLE IF EXISTS EJB_Resource_Env_Ref ;
DROP TABLE IF EXISTS EJB_Ref ;
DROP TABLE IF EXISTS EJB_Local_Ref ;
DROP TABLE IF EXISTS Query_Method ;
DROP TABLE IF EXISTS EJB_Security_Role ;
DROP TABLE IF EXISTS EJB_Method_Permission ;
DROP TABLE IF EXISTS EJB_Role_Name ;
DROP TABLE IF EXISTS Container_Transaction ;
DROP TABLE IF EXISTS EJB_Relationship ;
DROP TABLE IF EXISTS EJB_Relationship_Role ;

CREATE TABLE J2EE_Application(
        id INTEGER AUTO_INCREMENT, 
        display_name VARCHAR(255), 
        description VARCHAR(255), 
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

COMMIT;

CREATE TABLE War_Module(
        id INTEGER AUTO_INCREMENT, 
        display_name VARCHAR(255), 
        description VARCHAR(255), 
        path VARCHAR(255), 
        small_icon VARCHAR(255), 
        large_icon VARCHAR(255), 
        distributable BOOLEAN, 
        session_timeout VARCHAR(255), 
        login_auth_method VARCHAR(255), 
        login_realm_name VARCHAR(255), 
        form_login_page VARCHAR(255), 
        form_login_name VARCHAR(255), 
        context_root VARCHAR(255), 
        name VARCHAR(255), 
        parent INTEGER, 
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

COMMIT;

CREATE TABLE Context_Param(
        id INTEGER AUTO_INCREMENT, 
        name VARCHAR(255), 
        value_ VARCHAR(255), 
        description VARCHAR(255), 
        parent INTEGER, 
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

COMMIT;

CREATE TABLE Ear_Module(
        id INTEGER AUTO_INCREMENT, 
        name VARCHAR(255), 
        display_name VARCHAR(255), 
        description VARCHAR(255), 
        small_icon VARCHAR(255), 
        large_icon VARCHAR(255), 
        client_jar_uri VARCHAR(255), 
        path VARCHAR(255), 
        wsdl_dir VARCHAR(255), 
        relationships_description VARCHAR(255), 
        parent INTEGER, 
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

COMMIT;

CREATE TABLE Filter(
        id INTEGER AUTO_INCREMENT, 
        name VARCHAR(255), 
        display_name VARCHAR(255), 
        class_name VARCHAR(255), 
        icon VARCHAR(255), 
        description VARCHAR(255), 
        parent INTEGER, 
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

COMMIT;

CREATE TABLE Servlet(
        id INTEGER AUTO_INCREMENT, 
        name VARCHAR(255), 
        description VARCHAR(255), 
        class_name VARCHAR(255), 
        icon VARCHAR(255), 
        display_name VARCHAR(255), 
        jsp_file VARCHAR(255), 
        load_on_startup INTEGER, 
        run_as VARCHAR(255), 
        parent INTEGER, 
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

COMMIT;

CREATE TABLE Servlet_Mapping(
        id INTEGER AUTO_INCREMENT, 
        url_pattern VARCHAR(255), 
        parent INTEGER, 
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

COMMIT;

CREATE TABLE Security_Constraint(
        id INTEGER AUTO_INCREMENT, 
        display_name VARCHAR(255), 
        description VARCHAR(255), 
        parent INTEGER, 
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

COMMIT;

CREATE TABLE Web_Resource(
        id INTEGER AUTO_INCREMENT, 
        description VARCHAR(255), 
        name VARCHAR(255), 
        parent INTEGER, 
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

COMMIT;

CREATE TABLE Auth_Constraint(
        id INTEGER AUTO_INCREMENT, 
        description VARCHAR(255), 
        parent INTEGER, 
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

COMMIT;

CREATE TABLE User_Data_Constraint(
        id INTEGER AUTO_INCREMENT, 
        description VARCHAR(255), 
        transport_guarantee VARCHAR(255), 
        parent INTEGER, 
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

COMMIT;

CREATE TABLE HTTP_Method(
        id INTEGER AUTO_INCREMENT, 
        name VARCHAR(255), 
        parent INTEGER, 
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

COMMIT;

CREATE TABLE Web_Role_Name(
        id INTEGER AUTO_INCREMENT, 
        name VARCHAR(255), 
        parent INTEGER, 
        security_role INTEGER, 
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

COMMIT;

CREATE TABLE Web_Security_Role(
        id INTEGER AUTO_INCREMENT, 
        name VARCHAR(255), 
        description VARCHAR(255), 
        parent INTEGER, 
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

COMMIT;

CREATE TABLE Web_Env_Entry(
        id INTEGER AUTO_INCREMENT, 
        name VARCHAR(255), 
        type VARCHAR(255), 
        description VARCHAR(255), 
        value_ VARCHAR(255), 
        parent INTEGER, 
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

COMMIT;

CREATE TABLE Servlet_Init_Param(
        id INTEGER AUTO_INCREMENT, 
        name VARCHAR(255), 
        value_ VARCHAR(255), 
        description VARCHAR(255), 
        parent INTEGER, 
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

COMMIT;

CREATE TABLE Tag_Lib(
        id INTEGER AUTO_INCREMENT, 
        uri VARCHAR(255), 
        location VARCHAR(255), 
        parent INTEGER, 
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

COMMIT;

CREATE TABLE URL_Pattern(
        id INTEGER AUTO_INCREMENT, 
        pattern VARCHAR(255), 
        parent INTEGER, 
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

COMMIT;

CREATE TABLE Filter_Init_Param(
        id INTEGER AUTO_INCREMENT, 
        name VARCHAR(255), 
        value_ VARCHAR(255), 
        description VARCHAR(255), 
        parent INTEGER, 
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

COMMIT;

CREATE TABLE Web_Resource_Ref(
        id INTEGER AUTO_INCREMENT, 
        name VARCHAR(255), 
        type VARCHAR(255), 
        auth VARCHAR(255), 
        description VARCHAR(255), 
        value_ VARCHAR(255), 
        parent INTEGER, 
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

COMMIT;

CREATE TABLE Web_Resource_Env_Ref(
        id INTEGER AUTO_INCREMENT, 
        name VARCHAR(255), 
        type VARCHAR(255), 
        auth VARCHAR(255), 
        description VARCHAR(255), 
        value_ VARCHAR(255), 
        parent INTEGER, 
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

COMMIT;

CREATE TABLE Web_EJB_Ref(
        id INTEGER AUTO_INCREMENT, 
        name VARCHAR(255), 
        type VARCHAR(255), 
        description VARCHAR(255), 
        home VARCHAR(255), 
        remote VARCHAR(255), 
        ejb_link VARCHAR(255), 
        parent INTEGER, 
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

COMMIT;

CREATE TABLE Web_EJB_Local_Ref(
        id INTEGER AUTO_INCREMENT, 
        name VARCHAR(255), 
        type VARCHAR(255), 
        description VARCHAR(255), 
        local_home VARCHAR(255), 
        local_ VARCHAR(255), 
        ejb_link VARCHAR(255), 
        parent INTEGER, 
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

COMMIT;

CREATE TABLE Mime_Mapping(
        id INTEGER AUTO_INCREMENT, 
        extension VARCHAR(255), 
        type VARCHAR(255), 
        parent INTEGER, 
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

COMMIT;

CREATE TABLE Welcome_File(
        id INTEGER AUTO_INCREMENT, 
        name VARCHAR(255), 
        parent INTEGER, 
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

COMMIT;

CREATE TABLE Error_Page(
        id INTEGER AUTO_INCREMENT, 
        error_code VARCHAR(255), 
        exception_type VARCHAR(255), 
        location VARCHAR(255), 
        parent INTEGER, 
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

COMMIT;

CREATE TABLE Web_Security_Role_Ref(
        id INTEGER AUTO_INCREMENT, 
        name VARCHAR(255), 
        description VARCHAR(255), 
        parent INTEGER, 
        security_role INTEGER, 
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

COMMIT;

CREATE TABLE Entity_Bean(
        id INTEGER AUTO_INCREMENT, 
        ejb_name VARCHAR(255), 
        description VARCHAR(255), 
        display_name VARCHAR(255), 
        small_icon VARCHAR(255), 
        large_icon VARCHAR(255), 
        ejb_class VARCHAR(255), 
        home VARCHAR(255), 
        remote VARCHAR(255), 
        local_home VARCHAR(255), 
        local_ VARCHAR(255), 
        persistence_type VARCHAR(255), 
        prim_key_class VARCHAR(255), 
        reentrant BOOLEAN, 
        cmp_version INTEGER, 
        abstract_schema_name VARCHAR(255), 
        prim_key_field VARCHAR(255), 
        security_identity_description VARCHAR(255), 
        security_use_caller_identity BOOLEAN, 
        security_run_as_description VARCHAR(255), 
        security_run_as_role_name VARCHAR(255), 
        parent INTEGER, 
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

COMMIT;

CREATE TABLE Session_Bean(
        id INTEGER AUTO_INCREMENT, 
        description VARCHAR(255), 
        display_name VARCHAR(255), 
        small_icon VARCHAR(255), 
        large_icon VARCHAR(255), 
        ejb_name VARCHAR(255), 
        home VARCHAR(255), 
        remote VARCHAR(255), 
        local_home VARCHAR(255), 
        local_ VARCHAR(255), 
        ejb_class VARCHAR(255), 
        session_type VARCHAR(255), 
        transaction_type VARCHAR(255), 
        timeout INTEGER, 
        security_identity_description VARCHAR(255), 
        security_use_caller_identity BOOLEAN, 
        security_run_as_description VARCHAR(255), 
        security_run_as_role_name VARCHAR(255), 
        parent INTEGER, 
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

COMMIT;

CREATE TABLE Message_Driven_Bean(
        id INTEGER AUTO_INCREMENT, 
        description VARCHAR(255), 
        display_name VARCHAR(255), 
        small_icon VARCHAR(255), 
        large_icon VARCHAR(255), 
        ejb_name VARCHAR(255), 
        ejb_class VARCHAR(255), 
        transaction_type VARCHAR(255), 
        message_selector VARCHAR(255), 
        acknowlede_mode VARCHAR(255), 
        destination_type VARCHAR(255), 
        subscription_durability VARCHAR(255), 
        security_identity_description VARCHAR(255), 
        security_use_caller_identity BOOLEAN, 
        security_run_as_description VARCHAR(255), 
        security_run_as_role_name VARCHAR(255), 
        parent INTEGER, 
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

COMMIT;

CREATE TABLE CMP_Field(
        id INTEGER AUTO_INCREMENT, 
        name VARCHAR(255), 
        description VARCHAR(255), 
        parent INTEGER, 
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

COMMIT;

CREATE TABLE Query(
        id INTEGER AUTO_INCREMENT, 
        description VARCHAR(255), 
        result_type_mapping VARCHAR(255), 
        ejb_ql VARCHAR(255), 
        parent INTEGER, 
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

COMMIT;

CREATE TABLE EJB_Method(
        id INTEGER AUTO_INCREMENT, 
        name VARCHAR(255), 
        ejb_name VARCHAR(255), 
        method_inf VARCHAR(255), 
        description VARCHAR(255), 
        container_transaction INTEGER, 
        method_permission INTEGER, 
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

COMMIT;

CREATE TABLE EJB_Method_Parameter(
        id INTEGER AUTO_INCREMENT, 
        type VARCHAR(255), 
        param_index INTEGER, 
        ejb_method INTEGER, 
        query_method INTEGER, 
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

COMMIT;

CREATE TABLE Assembly_Descriptor(
        id INTEGER AUTO_INCREMENT, 
        parent INTEGER, 
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

COMMIT;

CREATE TABLE EJB_Resource_Ref(
        id INTEGER AUTO_INCREMENT, 
        name VARCHAR(255), 
        type VARCHAR(255), 
        auth VARCHAR(255), 
        sharing_scope VARCHAR(255), 
        description VARCHAR(255), 
        entity_bean_parent INTEGER, 
        session_bean_parent INTEGER, 
        message_bean_parent INTEGER, 
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

COMMIT;

CREATE TABLE EJB_Env_Entry(
        id INTEGER AUTO_INCREMENT, 
        name VARCHAR(255), 
        type VARCHAR(255), 
        description VARCHAR(255), 
        value_ VARCHAR(255), 
        entity_bean_parent INTEGER, 
        session_bean_parent INTEGER, 
        message_bean_parent INTEGER, 
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

COMMIT;

CREATE TABLE EJB_Resource_Env_Ref(
        id INTEGER AUTO_INCREMENT, 
        name VARCHAR(255), 
        type VARCHAR(255), 
        description VARCHAR(255), 
        entity_bean_parent INTEGER, 
        session_bean_parent INTEGER, 
        message_bean_parent INTEGER, 
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

COMMIT;

CREATE TABLE EJB_Ref(
        id INTEGER AUTO_INCREMENT, 
        name VARCHAR(255), 
        type VARCHAR(255), 
        description VARCHAR(255), 
        home VARCHAR(255), 
        remote VARCHAR(255), 
        ejb_link VARCHAR(255), 
        entity_bean_parent INTEGER, 
        session_bean_parent INTEGER, 
        message_bean_parent INTEGER, 
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

COMMIT;

CREATE TABLE EJB_Local_Ref(
        id INTEGER AUTO_INCREMENT, 
        name VARCHAR(255), 
        type VARCHAR(255), 
        description VARCHAR(255), 
        local_home VARCHAR(255), 
        local_ VARCHAR(255), 
        ejb_link VARCHAR(255), 
        entity_bean_parent INTEGER, 
        session_bean_parent INTEGER, 
        message_bean_parent INTEGER, 
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

COMMIT;

CREATE TABLE Query_Method(
        id INTEGER AUTO_INCREMENT, 
        name VARCHAR(255), 
        parent INTEGER, 
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

COMMIT;

CREATE TABLE EJB_Security_Role(
        id INTEGER AUTO_INCREMENT, 
        name VARCHAR(255), 
        description VARCHAR(255), 
        parent INTEGER, 
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

COMMIT;

CREATE TABLE EJB_Method_Permission(
        id INTEGER AUTO_INCREMENT, 
        description VARCHAR(255), 
        parent INTEGER, 
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

COMMIT;

CREATE TABLE EJB_Role_Name(
        id INTEGER AUTO_INCREMENT, 
        name VARCHAR(255), 
        parent INTEGER, 
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

COMMIT;

CREATE TABLE Container_Transaction(
        id INTEGER AUTO_INCREMENT, 
        description VARCHAR(255), 
        transaction_attribute VARCHAR(255), 
        parent INTEGER, 
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

COMMIT;

CREATE TABLE EJB_Relationship(
        id INTEGER AUTO_INCREMENT, 
        name VARCHAR(255), 
        description VARCHAR(255), 
        parent INTEGER, 
        source INTEGER, 
        sink INTEGER, 
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

COMMIT;

CREATE TABLE EJB_Relationship_Role(
        id INTEGER AUTO_INCREMENT, 
        description VARCHAR(255), 
        multiplicity BOOLEAN, 
        cascade_delete BOOLEAN, 
        role_source_description VARCHAR(255), 
        role_source_ejb_name VARCHAR(255), 
        cmr_field_name VARCHAR(255), 
        cmr_field_type VARCHAR(255), 
        cmr_field_description VARCHAR(255), 
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

COMMIT;

ALTER TABLE War_Module ADD CONSTRAINT War_Module_parent FOREIGN KEY (parent) REFERENCES J2EE_Application(id);
ALTER TABLE Context_Param ADD CONSTRAINT Context_Param_parent FOREIGN KEY (parent) REFERENCES War_Module(id);
ALTER TABLE Ear_Module ADD CONSTRAINT Ear_Module_parent FOREIGN KEY (parent) REFERENCES J2EE_Application(id);
ALTER TABLE Filter ADD CONSTRAINT Filter_parent FOREIGN KEY (parent) REFERENCES War_Module(id);
ALTER TABLE Servlet ADD CONSTRAINT Servlet_parent FOREIGN KEY (parent) REFERENCES War_Module(id);
ALTER TABLE Servlet_Mapping ADD CONSTRAINT Servlet_Mapping_parent FOREIGN KEY (parent) REFERENCES Servlet(id);
ALTER TABLE Security_Constraint ADD CONSTRAINT Security_Constraint_parent FOREIGN KEY (parent) REFERENCES War_Module(id);
ALTER TABLE Web_Resource ADD CONSTRAINT Web_Resource_parent FOREIGN KEY (parent) REFERENCES Security_Constraint(id);
ALTER TABLE Auth_Constraint ADD CONSTRAINT Auth_Constraint_parent FOREIGN KEY (parent) REFERENCES Security_Constraint(id);
ALTER TABLE User_Data_Constraint ADD CONSTRAINT User_Data_Constraint_parent FOREIGN KEY (parent) REFERENCES Security_Constraint(id);
ALTER TABLE HTTP_Method ADD CONSTRAINT HTTP_Method_parent FOREIGN KEY (parent) REFERENCES Web_Resource(id);
ALTER TABLE Web_Role_Name ADD CONSTRAINT Web_Role_Name_parent FOREIGN KEY (parent) REFERENCES Auth_Constraint(id);
ALTER TABLE Web_Role_Name ADD CONSTRAINT Web_Role_Name_security_role FOREIGN KEY (security_role) REFERENCES Web_Security_Role(id);
ALTER TABLE Web_Security_Role ADD CONSTRAINT Web_Security_Role_parent FOREIGN KEY (parent) REFERENCES War_Module(id);
ALTER TABLE Web_Env_Entry ADD CONSTRAINT Web_Env_Entry_parent FOREIGN KEY (parent) REFERENCES War_Module(id);
ALTER TABLE Servlet_Init_Param ADD CONSTRAINT Servlet_Init_Param_parent FOREIGN KEY (parent) REFERENCES Servlet(id);
ALTER TABLE Tag_Lib ADD CONSTRAINT Tag_Lib_parent FOREIGN KEY (parent) REFERENCES War_Module(id);
ALTER TABLE URL_Pattern ADD CONSTRAINT URL_Pattern_parent FOREIGN KEY (parent) REFERENCES Web_Resource(id);
ALTER TABLE Filter_Init_Param ADD CONSTRAINT Filter_Init_Param_parent FOREIGN KEY (parent) REFERENCES Filter(id);
ALTER TABLE Web_Resource_Ref ADD CONSTRAINT Web_Resource_Ref_parent FOREIGN KEY (parent) REFERENCES War_Module(id);
ALTER TABLE Web_Resource_Env_Ref ADD CONSTRAINT Web_Resource_Env_Ref_parent FOREIGN KEY (parent) REFERENCES War_Module(id);
ALTER TABLE Web_EJB_Ref ADD CONSTRAINT Web_EJB_Ref_parent FOREIGN KEY (parent) REFERENCES War_Module(id);
ALTER TABLE Web_EJB_Local_Ref ADD CONSTRAINT Web_EJB_Local_Ref_parent FOREIGN KEY (parent) REFERENCES War_Module(id);
ALTER TABLE Mime_Mapping ADD CONSTRAINT Mime_Mapping_parent FOREIGN KEY (parent) REFERENCES War_Module(id);
ALTER TABLE Welcome_File ADD CONSTRAINT Welcome_File_parent FOREIGN KEY (parent) REFERENCES War_Module(id);
ALTER TABLE Error_Page ADD CONSTRAINT Error_Page_parent FOREIGN KEY (parent) REFERENCES War_Module(id);
ALTER TABLE Web_Security_Role_Ref ADD CONSTRAINT Web_Security_Role_Ref_parent FOREIGN KEY (parent) REFERENCES Servlet(id);
ALTER TABLE Web_Security_Role_Ref ADD CONSTRAINT Web_Security_Role_Ref_security_role FOREIGN KEY (security_role) REFERENCES Web_Security_Role(id);
ALTER TABLE Entity_Bean ADD CONSTRAINT Entity_Bean_parent FOREIGN KEY (parent) REFERENCES Ear_Module(id);
ALTER TABLE Session_Bean ADD CONSTRAINT Session_Bean_parent FOREIGN KEY (parent) REFERENCES Ear_Module(id);
ALTER TABLE Message_Driven_Bean ADD CONSTRAINT Message_Driven_Bean_parent FOREIGN KEY (parent) REFERENCES Ear_Module(id);
ALTER TABLE CMP_Field ADD CONSTRAINT CMP_Field_parent FOREIGN KEY (parent) REFERENCES Entity_Bean(id);
ALTER TABLE Query ADD CONSTRAINT Query_parent FOREIGN KEY (parent) REFERENCES Entity_Bean(id);
ALTER TABLE EJB_Method ADD CONSTRAINT EJB_Method_container_transaction FOREIGN KEY (container_transaction) REFERENCES Container_Transaction(id);
ALTER TABLE EJB_Method ADD CONSTRAINT EJB_Method_method_permission FOREIGN KEY (method_permission) REFERENCES EJB_Method_Permission(id);
ALTER TABLE EJB_Method_Parameter ADD CONSTRAINT EJB_Method_Parameter_ejb_method FOREIGN KEY (ejb_method) REFERENCES EJB_Method(id);
ALTER TABLE EJB_Method_Parameter ADD CONSTRAINT EJB_Method_Parameter_query_method FOREIGN KEY (query_method) REFERENCES Query_Method(id);
ALTER TABLE Assembly_Descriptor ADD CONSTRAINT Assembly_Descriptor_parent FOREIGN KEY (parent) REFERENCES Ear_Module(id);
ALTER TABLE EJB_Resource_Ref ADD CONSTRAINT EJB_Resource_Ref_entity_bean_parent FOREIGN KEY (entity_bean_parent) REFERENCES Entity_Bean(id);
ALTER TABLE EJB_Resource_Ref ADD CONSTRAINT EJB_Resource_Ref_session_bean_parent FOREIGN KEY (session_bean_parent) REFERENCES Session_Bean(id);
ALTER TABLE EJB_Resource_Ref ADD CONSTRAINT EJB_Resource_Ref_message_bean_parent FOREIGN KEY (message_bean_parent) REFERENCES Message_Driven_Bean(id);
ALTER TABLE EJB_Env_Entry ADD CONSTRAINT EJB_Env_Entry_entity_bean_parent FOREIGN KEY (entity_bean_parent) REFERENCES Entity_Bean(id);
ALTER TABLE EJB_Env_Entry ADD CONSTRAINT EJB_Env_Entry_session_bean_parent FOREIGN KEY (session_bean_parent) REFERENCES Session_Bean(id);
ALTER TABLE EJB_Env_Entry ADD CONSTRAINT EJB_Env_Entry_message_bean_parent FOREIGN KEY (message_bean_parent) REFERENCES Message_Driven_Bean(id);
ALTER TABLE EJB_Resource_Env_Ref ADD CONSTRAINT EJB_Resource_Env_Ref_entity_bean_parent FOREIGN KEY (entity_bean_parent) REFERENCES Entity_Bean(id);
ALTER TABLE EJB_Resource_Env_Ref ADD CONSTRAINT EJB_Resource_Env_Ref_session_bean_parent FOREIGN KEY (session_bean_parent) REFERENCES Session_Bean(id);
ALTER TABLE EJB_Resource_Env_Ref ADD CONSTRAINT EJB_Resource_Env_Ref_message_bean_parent FOREIGN KEY (message_bean_parent) REFERENCES Message_Driven_Bean(id);
ALTER TABLE EJB_Ref ADD CONSTRAINT EJB_Ref_entity_bean_parent FOREIGN KEY (entity_bean_parent) REFERENCES Entity_Bean(id);
ALTER TABLE EJB_Ref ADD CONSTRAINT EJB_Ref_session_bean_parent FOREIGN KEY (session_bean_parent) REFERENCES Session_Bean(id);
ALTER TABLE EJB_Ref ADD CONSTRAINT EJB_Ref_message_bean_parent FOREIGN KEY (message_bean_parent) REFERENCES Message_Driven_Bean(id);
ALTER TABLE EJB_Local_Ref ADD CONSTRAINT EJB_Local_Ref_entity_bean_parent FOREIGN KEY (entity_bean_parent) REFERENCES Entity_Bean(id);
ALTER TABLE EJB_Local_Ref ADD CONSTRAINT EJB_Local_Ref_session_bean_parent FOREIGN KEY (session_bean_parent) REFERENCES Session_Bean(id);
ALTER TABLE EJB_Local_Ref ADD CONSTRAINT EJB_Local_Ref_message_bean_parent FOREIGN KEY (message_bean_parent) REFERENCES Message_Driven_Bean(id);
ALTER TABLE Query_Method ADD CONSTRAINT Query_Method_parent FOREIGN KEY (parent) REFERENCES Query(id);
ALTER TABLE EJB_Security_Role ADD CONSTRAINT EJB_Security_Role_parent FOREIGN KEY (parent) REFERENCES Assembly_Descriptor(id);
ALTER TABLE EJB_Method_Permission ADD CONSTRAINT EJB_Method_Permission_parent FOREIGN KEY (parent) REFERENCES Assembly_Descriptor(id);
ALTER TABLE EJB_Role_Name ADD CONSTRAINT EJB_Role_Name_parent FOREIGN KEY (parent) REFERENCES EJB_Method_Permission(id);
ALTER TABLE Container_Transaction ADD CONSTRAINT Container_Transaction_parent FOREIGN KEY (parent) REFERENCES Assembly_Descriptor(id);
ALTER TABLE EJB_Relationship ADD CONSTRAINT EJB_Relationship_parent FOREIGN KEY (parent) REFERENCES Ear_Module(id);
ALTER TABLE EJB_Relationship ADD CONSTRAINT EJB_Relationship_source FOREIGN KEY (source) REFERENCES EJB_Relationship_Role(id);
ALTER TABLE EJB_Relationship ADD CONSTRAINT EJB_Relationship_sink FOREIGN KEY (sink) REFERENCES EJB_Relationship_Role(id);

