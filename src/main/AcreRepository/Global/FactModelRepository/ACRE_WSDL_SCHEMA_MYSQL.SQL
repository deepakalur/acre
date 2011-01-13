DROP TABLE IF EXISTS Definition ;
DROP TABLE IF EXISTS ExternalImport ;
DROP TABLE IF EXISTS Message ;
DROP TABLE IF EXISTS PortType ;
DROP TABLE IF EXISTS Binding ;
DROP TABLE IF EXISTS Service ;
DROP TABLE IF EXISTS Part ;
DROP TABLE IF EXISTS Operation ;
DROP TABLE IF EXISTS Parameter ;
DROP TABLE IF EXISTS Fault ;
DROP TABLE IF EXISTS Body ;

CREATE TABLE Definition(
    
        name VARCHAR(255), 
        targetNamespace VARCHAR(255), 
        id DECIMAL(20,0),
        PRIMARY KEY ( id )
    );


CREATE UNIQUE INDEX Definition_PRIMARY_KEY ON Definition(id);
COMMIT;

CREATE TABLE ExternalImport(
    
        namespace VARCHAR(255), 
        location VARCHAR(255), 
        id DECIMAL(20,0), 
        definition DECIMAL(20,0),
        PRIMARY KEY ( id )
    );


CREATE UNIQUE INDEX ExternalImport_PRIMARY_KEY ON ExternalImport(id);
COMMIT;

CREATE TABLE Message(
    
        name VARCHAR(255), 
        id DECIMAL(20,0), 
        definition DECIMAL(20,0),
        PRIMARY KEY ( id )
    );


CREATE UNIQUE INDEX Message_PRIMARY_KEY ON Message(id);
COMMIT;

CREATE TABLE PortType(
    
        name VARCHAR(255), 
        location VARCHAR(255), 
        id DECIMAL(20,0), 
        definition DECIMAL(20,0), 
        service DECIMAL(20,0),
        PRIMARY KEY ( id )
    );

CREATE UNIQUE INDEX PortType_PRIMARY_KEY ON PortType(id);
COMMIT;

CREATE TABLE Binding(
    
        name VARCHAR(255), 
        type VARCHAR(255), 
        protocol VARCHAR(255), 
        transport VARCHAR(255), 
        style VARCHAR(255), 
        id DECIMAL(20,0), 
        definition DECIMAL(20,0),
        PRIMARY KEY ( id )
    );


CREATE UNIQUE INDEX Binding_PRIMARY_KEY ON Binding(id);
COMMIT;

CREATE TABLE Service(
    
        name VARCHAR(255), 
        id DECIMAL(20,0), 
        definition DECIMAL(20,0),
        PRIMARY KEY ( id )
    );


CREATE UNIQUE INDEX Service_PRIMARY_KEY ON Service(id);
COMMIT;

CREATE TABLE Part(
    
        name VARCHAR(255), 
        element VARCHAR(255), 
        type VARCHAR(255), 
        id DECIMAL(20,0), 
        message DECIMAL(20,0),
        PRIMARY KEY ( id )
    );


CREATE UNIQUE INDEX Part_PRIMARY_KEY ON Part(id);
COMMIT;

CREATE TABLE Operation(
    
        name VARCHAR(255), 
        parameterOrder VARCHAR(255), 
        type VARCHAR(255), 
        protocol VARCHAR(255), 
        soapAction VARCHAR(255), 
        style VARCHAR(255), 
        id DECIMAL(20,0), 
        portType DECIMAL(20,0), 
        binding DECIMAL(20,0),
        PRIMARY KEY ( id )
    );


CREATE UNIQUE INDEX Operation_PRIMARY_KEY ON Operation(id);
COMMIT;

CREATE TABLE Parameter(
    
        name VARCHAR(255), 
        message VARCHAR(255), 
        id DECIMAL(20,0), 
        inOperation DECIMAL(20,0), 
        outOperation DECIMAL(20,0),
        PRIMARY KEY ( id )
    );


CREATE UNIQUE INDEX Parameter_PRIMARY_KEY ON Parameter(id);
COMMIT;

CREATE TABLE Fault(
    
        name VARCHAR(255), 
        message VARCHAR(255), 
        id DECIMAL(20,0), 
        operation DECIMAL(20,0),
        PRIMARY KEY ( id )
    );


CREATE UNIQUE INDEX Fault_PRIMARY_KEY ON Fault(id);
COMMIT;

CREATE TABLE Body(
    
        namespace VARCHAR(255), 
        use1 VARCHAR(255), 
        id DECIMAL(20,0), 
        parameter DECIMAL(20,0),
        PRIMARY KEY ( id )
    );


CREATE UNIQUE INDEX Body_PRIMARY_KEY ON Body(id);
COMMIT;

ALTER TABLE ExternalImport ADD CONSTRAINT ExternalImport_definition FOREIGN KEY (definition) REFERENCES Definition(id);
ALTER TABLE Message ADD CONSTRAINT Message_definition FOREIGN KEY (definition) REFERENCES Definition(id);
ALTER TABLE PortType ADD CONSTRAINT PortType_definition FOREIGN KEY (definition) REFERENCES Definition(id);
ALTER TABLE PortType ADD CONSTRAINT PortType_service FOREIGN KEY (service) REFERENCES Service(id);
ALTER TABLE Binding ADD CONSTRAINT Binding_definition FOREIGN KEY (definition) REFERENCES Definition(id);
ALTER TABLE Service ADD CONSTRAINT Service_definition FOREIGN KEY (definition) REFERENCES Definition(id);
ALTER TABLE Part ADD CONSTRAINT Part_message FOREIGN KEY (message) REFERENCES Message(id);
ALTER TABLE Operation ADD CONSTRAINT Operation_portType FOREIGN KEY (portType) REFERENCES PortType(id);
ALTER TABLE Operation ADD CONSTRAINT Operation_binding FOREIGN KEY (binding) REFERENCES Binding(id);
ALTER TABLE Parameter ADD CONSTRAINT Parameter_inOperation FOREIGN KEY (inOperation) REFERENCES Operation(id);
ALTER TABLE Parameter ADD CONSTRAINT Parameter_outOperation FOREIGN KEY (outOperation) REFERENCES Operation(id);
ALTER TABLE Fault ADD CONSTRAINT Fault_operation FOREIGN KEY (operation) REFERENCES Operation(id);
ALTER TABLE Body ADD CONSTRAINT Body_parameter FOREIGN KEY (parameter) REFERENCES Parameter(id);

