-- ACE-GENERATED FILE: REMOVE THIS LINE TO KEEP YOUR CHANGES
-- if EXISTS(JFactDatabase) 
DROP TABLE JFactDatabase CASCADE;
-- if EXISTS(JPackage) 
DROP TABLE JPackage CASCADE;
-- if EXISTS(JClass) 
DROP TABLE JClass CASCADE;
-- if EXISTS(JClass_ImplementsInterfaces) 
DROP TABLE JClass_ImplementsInterfaces CASCADE;
-- if EXISTS(JMethod_ThrowsExceptions) 
DROP TABLE JMethod_ThrowsExceptions CASCADE;
-- if EXISTS(JMethod_CatchesExceptions) 
DROP TABLE JMethod_CatchesExceptions CASCADE;
-- if EXISTS(JMethod) 
DROP TABLE JMethod CASCADE;
-- if EXISTS(JMethod_Calls) 
DROP TABLE JMethod_Calls CASCADE;
-- if EXISTS(JMethod_UsedFields) 
DROP TABLE JMethod_UsedFields CASCADE;
-- if EXISTS(JField) 
DROP TABLE JField CASCADE;
-- if EXISTS(JParameter) 
DROP TABLE JParameter CASCADE;
-- if EXISTS(JAnnotation) 
DROP TABLE JAnnotation CASCADE;
-- if EXISTS(JAnnotationArgument) 
DROP TABLE JAnnotationArgument CASCADE;
-- if EXISTS(WriteVariable) 
DROP TABLE WriteVariable CASCADE;
-- if EXISTS(ReadVariable) 
DROP TABLE ReadVariable CASCADE;

CREATE TABLE JFactDatabase(
    
        id DECIMAL(20,0), 
        version VARCHAR(255), 
        name VARCHAR(255), 
        sourceFile VARCHAR(255),
        PRIMARY KEY ( id )
    );

-- if EXISTS(JFactDatabase_PRIMARY_KEY) 
-- DROP INDEX JFactDatabase.JFactDatabase_PRIMARY_KEY;

CREATE UNIQUE INDEX JFactDatabase_PRIMARY_KEY ON JFactDatabase(id);
COMMIT;

CREATE TABLE JPackage(
    
        id DECIMAL(20,0), 
        version VARCHAR(255), 
        name VARCHAR(255), 
        shortName VARCHAR(255),
        parentPackage DECIMAL(20,0),
        factDatabase DECIMAL(20,0),
        PRIMARY KEY ( id )
    );

-- if EXISTS(JPackage_PRIMARY_KEY) 
-- DROP INDEX JPackage.JPackage_PRIMARY_KEY;

CREATE UNIQUE INDEX JPackage_PRIMARY_KEY ON JPackage(id);
COMMIT;

CREATE TABLE JClass(
    
        id DECIMAL(20,0), 
        version VARCHAR(255), 
        name VARCHAR(255), 
        shortName VARCHAR(255), 
        isStatic BOOLEAN, 
        isFinal BOOLEAN, 
        isInner BOOLEAN, 
        accessibility VARCHAR(255), 
        sourceFile VARCHAR(255), 
        filePosition INTEGER, 
        parentPackage DECIMAL(20,0), 
        extendsClass DECIMAL(20,0), 
        containingClass DECIMAL(20,0),
        PRIMARY KEY ( id )
    );

-- if EXISTS(JClass_PRIMARY_KEY) 
-- DROP INDEX JClass.JClass_PRIMARY_KEY;

CREATE UNIQUE INDEX JClass_PRIMARY_KEY ON JClass(id);
COMMIT;

CREATE TABLE JClass_ImplementsInterfaces(
    -- JOIN table for n:n relationship
    
        implementsInterfaces DECIMAL(20,0), 
        implementingClasses DECIMAL(20,0),
        PRIMARY KEY ( implementsInterfaces,implementingClasses )
    );


COMMIT;

CREATE TABLE JMethod_ThrowsExceptions(
    -- JOIN table for n:n relationship
    
        throwsExceptions DECIMAL(20,0), 
        thrownBy DECIMAL(20,0),
        PRIMARY KEY ( throwsExceptions,thrownBy )
    );


COMMIT;

CREATE TABLE JMethod_CatchesExceptions(
    -- JOIN table for n:n relationship
    
        catchesExceptions DECIMAL(20,0), 
        caughtBy DECIMAL(20,0),
        PRIMARY KEY ( catchesExceptions,caughtBy )
    );


COMMIT;

CREATE TABLE JMethod(
    
        id DECIMAL(20,0), 
        version VARCHAR(255), 
        name VARCHAR(255), 
        shortName VARCHAR(255), 
        isAbstract BOOLEAN, 
        isConstructor BOOLEAN, 
        isStatic BOOLEAN, 
        isFinal BOOLEAN, 
        numParams INTEGER, 
        accessibility VARCHAR(255), 
        filePosition INTEGER, 
        parentClass DECIMAL(20,0), 
        returnType DECIMAL(20,0),
        PRIMARY KEY ( id )
    );

-- if EXISTS(JMethod_PRIMARY_KEY) 
-- DROP INDEX JMethod.JMethod_PRIMARY_KEY;

CREATE UNIQUE INDEX JMethod_PRIMARY_KEY ON JMethod(id);
COMMIT;

CREATE TABLE JMethod_Calls(
    -- JOIN table for n:n relationship
    
        calls DECIMAL(20,0), 
        callers DECIMAL(20,0),
        PRIMARY KEY ( calls,callers )
    );


COMMIT;

CREATE TABLE JMethod_UsedFields(
    -- JOIN table for n:n relationship
    
        usedFields DECIMAL(20,0), 
        usedByMethods DECIMAL(20,0),
        PRIMARY KEY ( usedFields,usedByMethods )
    );


COMMIT;

CREATE TABLE JField(
    
        id DECIMAL(20,0), 
        version VARCHAR(255), 
        name VARCHAR(255), 
        shortName VARCHAR(255), 
        isStatic BOOLEAN, 
        isFinal BOOLEAN, 
        accessibility VARCHAR(255), 
        filePosition INTEGER, 
        parentClass DECIMAL(20,0), 
        type DECIMAL(20,0),
        PRIMARY KEY ( id )
    );

-- if EXISTS(JField_PRIMARY_KEY) 
-- DROP INDEX JField.JField_PRIMARY_KEY;

CREATE UNIQUE INDEX JField_PRIMARY_KEY ON JField(id);
COMMIT;

CREATE TABLE JParameter(
    
        id DECIMAL(20,0), 
        version VARCHAR(255), 
        name VARCHAR(255), 
        shortName VARCHAR(255), 
        index INTEGER, 
        method DECIMAL(20,0), 
        type DECIMAL(20,0),
        PRIMARY KEY ( id )
    );

-- if EXISTS(JParameter_PRIMARY_KEY) 
-- DROP INDEX JParameter.JParameter_PRIMARY_KEY;

CREATE UNIQUE INDEX JParameter_PRIMARY_KEY ON JParameter(id);
COMMIT;

CREATE TABLE JAnnotation(
    
        id DECIMAL(20,0), 
        version VARCHAR(255), 
        name VARCHAR(255), 
        shortName VARCHAR(255), 
        numArgs INTEGER, 
        parentClass DECIMAL(20,0), 
        parentMethod DECIMAL(20,0), 
        parentField DECIMAL(20,0), 
        parentParameter DECIMAL(20,0), 
        type DECIMAL(20,0),
        PRIMARY KEY ( id )
    );

-- if EXISTS(JAnnotation_PRIMARY_KEY) 
-- DROP INDEX JAnnotation.JAnnotation_PRIMARY_KEY;

CREATE UNIQUE INDEX JAnnotation_PRIMARY_KEY ON JAnnotation(id);
COMMIT;

CREATE TABLE JAnnotationArgument(
    
        id DECIMAL(20,0), 
        version VARCHAR(255), 
        name VARCHAR(255), 
        shortName VARCHAR(255), 
        stringValue VARCHAR(255), 
        annotation DECIMAL(20,0), 
        type DECIMAL(20,0),
        PRIMARY KEY ( id )
    );

-- if EXISTS(JAnnotationArgument_PRIMARY_KEY) 
-- DROP INDEX JAnnotationArgument.JAnnotationArgument_PRIMARY_KEY;

CREATE UNIQUE INDEX JAnnotationArgument_PRIMARY_KEY ON JAnnotationArgument(id);
COMMIT;

CREATE TABLE WriteVariable(
    
        cookie VARCHAR(255), 
        scope INTEGER, 
        name VARCHAR(255), 
        varIndex INTEGER, 
        nodeId DECIMAL(20,0), 
        nodeName VARCHAR(255)
    );

-- if EXISTS(WriteVariable_PRIMARY_KEY) 
-- DROP INDEX WriteVariable.WriteVariable_PRIMARY_KEY;

CREATE UNIQUE INDEX WriteVariable_PRIMARY_KEY ON WriteVariable(cookie,scope,name,varIndex);
COMMIT;

CREATE TABLE ReadVariable(
    
        cookie VARCHAR(255), 
        scope INTEGER, 
        name VARCHAR(255), 
        varIndex INTEGER, 
        nodeId DECIMAL(20,0), 
        nodeName VARCHAR(255)
    );

-- if EXISTS(ReadVariable_PRIMARY_KEY) 
-- DROP INDEX ReadVariable.ReadVariable_PRIMARY_KEY;

CREATE UNIQUE INDEX ReadVariable_PRIMARY_KEY ON ReadVariable(cookie,scope,name,varIndex);
COMMIT;

ALTER TABLE JPackage ADD CONSTRAINT JPackage_parentPackage FOREIGN KEY (parentPackage) REFERENCES JPackage(id);
ALTER TABLE JPackage ADD CONSTRAINT JPackage_factDatabase FOREIGN KEY (factDatabase) REFERENCES JFactDatabase(id);
ALTER TABLE JClass ADD CONSTRAINT JClass_parentPackage FOREIGN KEY (parentPackage) REFERENCES JPackage(id);
ALTER TABLE JClass ADD CONSTRAINT JClass_extendsClass FOREIGN KEY (extendsClass) REFERENCES JClass(id);
ALTER TABLE JClass ADD CONSTRAINT JClass_containingClass FOREIGN KEY (containingClass) REFERENCES JClass(id);
ALTER TABLE JClass_ImplementsInterfaces ADD CONSTRAINT JClass_ImplementsInterfaces_fk_for_implementsInterfaces FOREIGN KEY (implementsInterfaces) REFERENCES JClass(id);
ALTER TABLE JClass_ImplementsInterfaces ADD CONSTRAINT JClass_ImplementsInterfaces_fk_for_implementingClasses FOREIGN KEY (implementingClasses) REFERENCES JClass(id);
ALTER TABLE JMethod_ThrowsExceptions ADD CONSTRAINT JMethod_ThrowsExceptions_fk_for_throwsExceptions FOREIGN KEY (throwsExceptions) REFERENCES JMethod(id);
ALTER TABLE JMethod_ThrowsExceptions ADD CONSTRAINT JMethod_ThrowsExceptions_fk_for_thrownBy FOREIGN KEY (thrownBy) REFERENCES JClass(id);
ALTER TABLE JMethod_CatchesExceptions ADD CONSTRAINT JMethod_CatchesExceptions_fk_for_catchesExceptions FOREIGN KEY (catchesExceptions) REFERENCES JMethod(id);
ALTER TABLE JMethod_CatchesExceptions ADD CONSTRAINT JMethod_CatchesExceptions_fk_for_caughtBy FOREIGN KEY (caughtBy) REFERENCES JClass(id);
ALTER TABLE JMethod ADD CONSTRAINT JMethod_parentClass FOREIGN KEY (parentClass) REFERENCES JClass(id);
ALTER TABLE JMethod ADD CONSTRAINT JMethod_returnType FOREIGN KEY (returnType) REFERENCES JClass(id);
ALTER TABLE JMethod_Calls ADD CONSTRAINT JMethod_Calls_fk_for_calls FOREIGN KEY (calls) REFERENCES JMethod(id);
ALTER TABLE JMethod_Calls ADD CONSTRAINT JMethod_Calls_fk_for_callers FOREIGN KEY (callers) REFERENCES JMethod(id);
ALTER TABLE JMethod_UsedFields ADD CONSTRAINT JMethod_UsedFields_fk_for_usedFields FOREIGN KEY (usedFields) REFERENCES JMethod(id);
ALTER TABLE JMethod_UsedFields ADD CONSTRAINT JMethod_UsedFields_fk_for_usedByMethods FOREIGN KEY (usedByMethods) REFERENCES JField(id);
ALTER TABLE JField ADD CONSTRAINT JField_parentClass FOREIGN KEY (parentClass) REFERENCES JClass(id);
ALTER TABLE JField ADD CONSTRAINT JField_type FOREIGN KEY (type) REFERENCES JClass(id);
ALTER TABLE JParameter ADD CONSTRAINT JParameter_method FOREIGN KEY (method) REFERENCES JMethod(id);
ALTER TABLE JParameter ADD CONSTRAINT JParameter_type FOREIGN KEY (type) REFERENCES JClass(id);
ALTER TABLE JAnnotation ADD CONSTRAINT JAnnotation_parentClass FOREIGN KEY (parentClass) REFERENCES JClass(id);
ALTER TABLE JAnnotation ADD CONSTRAINT JAnnotation_parentMethod FOREIGN KEY (parentMethod) REFERENCES JMethod(id);
ALTER TABLE JAnnotation ADD CONSTRAINT JAnnotation_parentField FOREIGN KEY (parentField) REFERENCES JField(id);
ALTER TABLE JAnnotation ADD CONSTRAINT JAnnotation_parentParameter FOREIGN KEY (parentParameter) REFERENCES JParameter(id);
ALTER TABLE JAnnotation ADD CONSTRAINT JAnnotation_type FOREIGN KEY (type) REFERENCES JClass(id);
ALTER TABLE JAnnotationArgument ADD CONSTRAINT JAnnotationArgument_annotation FOREIGN KEY (annotation) REFERENCES JAnnotation(id);
ALTER TABLE JAnnotationArgument ADD CONSTRAINT JAnnotationArgument_type FOREIGN KEY (type) REFERENCES JClass(id);

