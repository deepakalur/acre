-- =========================== Auxiliary tables to calculate transitive closure =======================
DROP TABLE IF EXISTS stage1;
DROP TABLE IF EXISTS stage3;
DROP TABLE IF EXISTS stage2;
CREATE TABLE stage1(a CHAR(32) NOT NULL, b CHAR(32) NOT NULL, PRIMARY KEY(a,b), KEY(a), KEY(b));
CREATE TABLE stage2(a CHAR(32) NOT NULL, b CHAR(32) NOT NULL, PRIMARY KEY(a,b), KEY(a), KEY(b));
CREATE TABLE stage3(a CHAR(32) NOT NULL, b CHAR(32) NOT NULL, PRIMARY KEY(a,b), KEY(a), KEY(b));

-- ========================== Main tables ==============================================================
DROP TABLE IF EXISTS JFactDatabase ;
DROP TABLE IF EXISTS JPackage ;
DROP TABLE IF EXISTS JClass ;
DROP TABLE IF EXISTS JClass_ImplementsInterfaces ;
--DROP TABLE IF EXISTS JClass_ExtendsClasses ;
DROP TABLE IF EXISTS JClass_ImplementsInterfacesTransitive;
DROP TABLE IF EXISTS JClass_ExtendsClassTransitive;
DROP TABLE IF EXISTS JMethod_ThrowsExceptions ;
DROP TABLE IF EXISTS JMethod_CatchesExceptions ;
DROP TABLE IF EXISTS JMethod ;
DROP TABLE IF EXISTS JMethod_Calls ;
DROP TABLE IF EXISTS JMethod_CallsTransitive;
DROP TABLE IF EXISTS JMethod_Instantiates ;
DROP TABLE IF EXISTS JMethod_InstantiatesTransitive;
DROP TABLE IF EXISTS JMethod_UsedFields ;
DROP TABLE IF EXISTS JField ;
DROP TABLE IF EXISTS JParameter ;
DROP TABLE IF EXISTS JAnnotation ;
DROP TABLE IF EXISTS JAnnotationArgument ;
DROP TABLE IF EXISTS JNonClassType ;
DROP TABLE IF EXISTS WriteVariable ;
DROP TABLE IF EXISTS ReadVariable ;


CREATE TABLE JFactDatabase(
        id CHAR(32),
        version VARCHAR(255),
        name VARCHAR(255),
        sourceFile VARCHAR(255),
        PRIMARY KEY ( id )
    );

CREATE UNIQUE INDEX JFactDatabase_PRIMARY_KEY ON JFactDatabase(id);
CREATE UNIQUE INDEX JFactDatabase_name ON JFactDatabase(name);
COMMIT;

CREATE TABLE JPackage(
        id CHAR(32),
        version VARCHAR(255),
        name VARCHAR(255),
        shortName VARCHAR(255),
        factDatabase CHAR(32),
        PRIMARY KEY ( id )
    );

CREATE UNIQUE INDEX JPackage_PRIMARY_KEY ON JPackage(id);
CREATE UNIQUE INDEX JPackage_name ON JPackage(name);
COMMIT;

CREATE TABLE JNonClassType(
        id CHAR(32),
        version VARCHAR(255),
        name VARCHAR(255),
        shortName VARCHAR(255),
        PRIMARY KEY ( id )
    );

CREATE UNIQUE INDEX JNonClassType_PRIMARY_KEY ON JNonClassType(id);
CREATE UNIQUE INDEX JNonClassType_name ON JNonClassType(name);
COMMIT;

CREATE TABLE JClass(
        id CHAR(32),
        version VARCHAR(255),
        name VARCHAR(255),
        shortName VARCHAR(255),
        isStatic BOOLEAN,
        isFinal BOOLEAN,
        isInner BOOLEAN,
        accessibility VARCHAR(255),
        sourceFile VARCHAR(255),
        filePosition INTEGER,
        parentPackage CHAR(32),
        extendsClass CHAR(32),
        containingClass CHAR(32),
        PRIMARY KEY ( id )
    );

CREATE UNIQUE INDEX JClass_PRIMARY_KEY ON JClass(id);
CREATE UNIQUE INDEX JClass_name ON JClass(name);
COMMIT;

CREATE TABLE JClass_ImplementsInterfaces(    -- JOIN table for n:n relationship
        implementsInterfaces CHAR(32),
        implementingClasses CHAR(32),
        PRIMARY KEY ( implementsInterfaces,implementingClasses ),
        KEY(implementsInterfaces), KEY(implementingClasses)
    );

CREATE TABLE JClass_ImplementsInterfacesTransitive(    -- JOIN table for n:n relationship
        implementsInterfaces CHAR(32),
        implementingClasses CHAR(32),
        PRIMARY KEY ( implementsInterfaces,implementingClasses ),
        KEY(implementsInterfaces), KEY(implementingClasses)
    );


COMMIT;

--CREATE TABLE JClass_ExtendsClasses(    -- JOIN table for n:n relationship
--        extendsClasses CHAR(32),
--        extendingClasses CHAR(32),
--        PRIMARY KEY ( extendsClasses,extendingClasses ),
--        KEY(extendsClasses), KEY(extendingClasses)
--    );
CREATE TABLE JClass_ExtendsClassTransitive(    -- JOIN table for n:n relationship
        extendsClass CHAR(32),
        extendingClasses CHAR(32),
        PRIMARY KEY ( extendsClass,extendingClasses ),
        KEY(extendsClass), KEY(extendingClasses)
    );
COMMIT;

CREATE TABLE JMethod_Calls(
        calls CHAR(32),
        callers CHAR(32),
        filePosition INTEGER,
        PRIMARY KEY (calls,callers),
        KEY(calls), KEY(callers)
    );
CREATE TABLE JMethod_CallsTransitive(
        calls CHAR(32),
        callers CHAR(32),
        filePosition INTEGER,
        PRIMARY KEY (calls,callers),
        KEY(calls), KEY(callers)
    );
COMMIT;

CREATE TABLE JMethod_ThrowsExceptions(
        throwsExceptions CHAR(32),
        thrownBy CHAR(32),
        filePosition INTEGER,
        PRIMARY KEY ( throwsExceptions,thrownBy ),
        KEY(throwsExceptions), KEY(thrownBy)
    );
COMMIT;

CREATE TABLE JMethod_CatchesExceptions(
        catchesExceptions CHAR(32),
        caughtBy CHAR(32),
        filePosition INTEGER,
        PRIMARY KEY ( catchesExceptions,caughtBy ),
        KEY(catchesExceptions), KEY(caughtBy)
    );
COMMIT;
CREATE TABLE JMethod_Instantiates(
        callingMethod     CHAR(32),
        instantiatedClass CHAR(32),
        filePosition INTEGER,
        PRIMARY KEY ( callingMethod,instantiatedClass ),
        KEY(callingMethod), KEY(instantiatedClass)
    );

CREATE TABLE JMethod_InstantiatesTransitive(
        callingMethod     CHAR(32),
        instantiatedClass CHAR(32),
        filePosition INTEGER,
        PRIMARY KEY ( callingMethod,instantiatedClass ),
        KEY(callingMethod), KEY(instantiatedClass)
    );
COMMIT;


CREATE TABLE JMethod(
        id CHAR(32),
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
        parentClass CHAR(32),
        returnType CHAR(32),
        PRIMARY KEY ( id )
    );

CREATE UNIQUE INDEX JMethod_PRIMARY_KEY ON JMethod(id);
CREATE UNIQUE INDEX JMethod_name ON JMethod(name);
COMMIT;

CREATE TABLE JMethod_UsedFields(
        usedFields CHAR(32),
        usedByMethods CHAR(32),
        filePosition INTEGER,
        PRIMARY KEY ( usedFields,usedByMethods ),
        KEY(usedFields), KEY(usedByMethods)
    );
COMMIT;

CREATE TABLE JField(
        id CHAR(32),
        version VARCHAR(255),
        name VARCHAR(255),
        shortName VARCHAR(255),
        isStatic BOOLEAN,
        isFinal BOOLEAN,
        accessibility VARCHAR(255),
        filePosition INTEGER,
        parentClass CHAR(32),
        type CHAR(32),
        PRIMARY KEY ( id )
    );

CREATE UNIQUE INDEX JField_PRIMARY_KEY ON JField(id);
CREATE UNIQUE INDEX JField_name ON JField(name);
COMMIT;

CREATE TABLE JParameter(
        id CHAR(32) PRIMARY KEY,
        version VARCHAR(255),
        name VARCHAR(255),
        shortName VARCHAR(255),
        parameterIndex INTEGER,
        method CHAR(32),
        type CHAR(32)
    );

CREATE UNIQUE INDEX JParameter_PRIMARY_KEY ON JParameter(id);
CREATE UNIQUE INDEX JParameter_name ON JParameter(name);
COMMIT;

CREATE TABLE JAnnotation(
        id CHAR(32)PRIMARY KEY,
        version VARCHAR(255),
        name VARCHAR(255),
        shortName VARCHAR(255),
        numArgs INTEGER,
        parentClass CHAR(32),
        parentMethod CHAR(32),
        parentField CHAR(32),
        parentParameter CHAR(32),
        type CHAR(32)
    );

CREATE UNIQUE INDEX JAnnotation_PRIMARY_KEY ON JAnnotation(id);
CREATE UNIQUE INDEX JAnnotation_name ON JAnnotation(name);
COMMIT;

CREATE TABLE JAnnotationArgument(
        id CHAR(32) PRIMARY KEY,
        version VARCHAR(255),
        name VARCHAR(255),
        shortName VARCHAR(255),
        stringValue VARCHAR(10000),
        annotation CHAR(32),
        type CHAR(32)
    );

CREATE UNIQUE INDEX JAnnotationArgument_PRIMARY_KEY ON JAnnotationArgument(id);
CREATE UNIQUE INDEX JAnnotationArgument_name ON JAnnotationArgument(name);
COMMIT;

CREATE TABLE WriteVariable(

        cookie VARCHAR(255),
        scope INTEGER,
        name VARCHAR(255),
        varIndex INTEGER,
        nodeId DECIMAL(20,0),
        nodeName VARCHAR(255)
    );

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

CREATE UNIQUE INDEX ReadVariable_PRIMARY_KEY ON ReadVariable(cookie,scope,name,varIndex);
COMMIT;

ALTER TABLE JPackage ADD CONSTRAINT JPackage_factDatabase FOREIGN KEY (factDatabase) REFERENCES JFactDatabase(id);
ALTER TABLE JClass ADD CONSTRAINT JClass_parentPackage FOREIGN KEY (parentPackage) REFERENCES JPackage(id);
ALTER TABLE JClass ADD CONSTRAINT JClass_extendsClass FOREIGN KEY (extendsClass) REFERENCES JClass(id);
ALTER TABLE JClass ADD CONSTRAINT JClass_containingClass FOREIGN KEY (containingClass) REFERENCES JClass(id);
ALTER TABLE JClass_ImplementsInterfaces ADD CONSTRAINT JClass_ImplementsInterfaces_fk_for_implementsInterfaces FOREIGN KEY (implementsInterfaces) REFERENCES JClass(id);
ALTER TABLE JClass_ImplementsInterfaces ADD CONSTRAINT JClass_ImplementsInterfaces_fk_for_implementingClasses FOREIGN KEY (implementingClasses) REFERENCES JClass(id);
ALTER TABLE JClass_ExtendsClass ADD CONSTRAINT JClass_ExtendsClass_fk_for_extendsClass FOREIGN KEY (extendsClass) REFERENCES JClass(id);
ALTER TABLE JClass_ExtendsClass ADD CONSTRAINT JClass_ExtendsClass_fk_for_extendingClasses FOREIGN KEY (extendingClasses) REFERENCES JClass(id);
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

