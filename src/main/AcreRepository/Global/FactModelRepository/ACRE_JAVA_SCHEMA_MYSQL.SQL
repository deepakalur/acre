-- ========================== Main tables ==============================================================
DROP TABLE IF EXISTS JPackage_PackagesTransitive ;
DROP TABLE IF EXISTS JClass_ImplementsInterfaces ;
DROP TABLE IF EXISTS JClass_ImplementsInterfacesTransitive ;
DROP TABLE IF EXISTS JClass_ExtendsClassTransitive ;
DROP TABLE IF EXISTS JMethod_ThrowsExceptions ;
DROP TABLE IF EXISTS JMethod_CatchesExceptions ;
DROP TABLE IF EXISTS JMethod_Calls ;
DROP TABLE IF EXISTS JMethod_CallsTransitive ;
DROP TABLE IF EXISTS JMethod_Instantiates ;
DROP TABLE IF EXISTS JMethod_UsedFields ;
DROP TABLE IF EXISTS JAnnotationArgument ;
DROP TABLE IF EXISTS JAnnotation ;
DROP TABLE IF EXISTS JNonClassType ;
DROP TABLE IF EXISTS JClass_ContainsClassesTransitive ;
DROP TABLE IF EXISTS JParameter ;
DROP TABLE IF EXISTS JField ;
DROP TABLE IF EXISTS JMethod ;
DROP TABLE IF EXISTS JClass ;
DROP TABLE IF EXISTS JPackage ;
DROP TABLE IF EXISTS JCallpath ;
DROP TABLE IF EXISTS JCallpath_Classes;

-- ========================== Main tables ==============================================================
CREATE TABLE JPackage(
        id INTEGER AUTO_INCREMENT,
        name VARCHAR(255),
        shortName VARCHAR(255),
        parentPackage INTEGER,
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

-- CREATE UNIQUE INDEX JPackage_PRIMARY_KEY ON JPackage(id);
CREATE UNIQUE INDEX JPackage_UNIQUE_KEY_UniqueName ON JPackage(name, salsaProjectId);
COMMIT;
CREATE TABLE JPackage_PackagesTransitive(
    -- JOIN table for n:n relationship
        packages INTEGER,
        parentPackage INTEGER,
        salsaProjectId INTEGER,
        PRIMARY KEY ( packages,parentPackage )
    ) type=MyISAM;

COMMIT;

CREATE TABLE JNonClassType(
        id INTEGER AUTO_INCREMENT,
        name VARCHAR(255),
        shortName VARCHAR(255),
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

-- CREATE UNIQUE INDEX JNonClassType_PRIMARY_KEY ON JNonClassType(id);
CREATE UNIQUE INDEX JNonClassType_name ON JNonClassType(name, salsaProjectId);
COMMIT;

CREATE TABLE JClass(
        id INTEGER AUTO_INCREMENT,
        name VARCHAR(255),
        shortName VARCHAR(255),
        isStatic BOOLEAN,
        isFinal BOOLEAN,
        isInner BOOLEAN,
        isInterface BOOLEAN,
        accessibility VARCHAR(255),
        sourceFile VARCHAR(255),
        filePosition INTEGER,

        numStatements INTEGER,
        numLocalVariables INTEGER,
        numTryClauses INTEGER,
        numCatchClauses INTEGER,
        numCalls INTEGER,
        numInstantiations INTEGER,
        numAnonClassInstantiations INTEGER,
        numLines INTEGER,
        numSynchronizedBlocks INTEGER,
        numReturnStatements INTEGER,
        numThrowStatements INTEGER,
        numAssertStatements INTEGER,
        numCasts INTEGER,
        numSkips INTEGER,
        numAssignStatements INTEGER,
        numInitializedLocalVariables INTEGER,
        numLoopStatements INTEGER,
        numIfStatements INTEGER,
        numSwitchStatements INTEGER,
        numCaseStatements INTEGER,
        numBranchStatements INTEGER,
        numLinearSegments INTEGER,
        numLocalAnnotations INTEGER,

        numFields INTEGER,
        numInitializedFields INTEGER,
        numMethods INTEGER,

        parentPackage INTEGER,
        extendsClass INTEGER,
        containingClass INTEGER,
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

-- CREATE UNIQUE INDEX JClass_PRIMARY_KEY ON JClass(id);
CREATE UNIQUE INDEX JClass_UNIQUE_KEY_UniqueName ON JClass(name, salsaProjectId);
COMMIT;

CREATE TABLE JClass_ImplementsInterfaces(    -- JOIN table for n:n relationship
        implementsInterfaces INTEGER,
        implementingClasses INTEGER,
        salsaProjectId INTEGER,
        PRIMARY KEY (implementsInterfaces,implementingClasses, salsaProjectId),
        KEY(implementsInterfaces, salsaProjectId), KEY(implementingClasses, salsaProjectId)
    ) type=MyISAM;

CREATE TABLE JClass_ImplementsInterfacesTransitive(    -- JOIN table for n:n relationship
        implementsInterfaces INTEGER,
        implementingClasses INTEGER,
        salsaProjectId INTEGER,
        PRIMARY KEY ( implementsInterfaces,implementingClasses, salsaProjectId),
        KEY(implementsInterfaces, salsaProjectId), KEY(implementingClasses, salsaProjectId)
    ) type=MyISAM;


COMMIT;

CREATE TABLE JClass_ContainsClassesTransitive(
        containsClasses   INTEGER,
        containingClass INTEGER,
        salsaProjectId INTEGER,
        PRIMARY KEY (containsClasses, containingClass, salsaProjectId),
        KEY(containsClasses, salsaProjectId), KEY(containingClass, salsaProjectId)
    ) type=MyISAM;

COMMIT;


CREATE TABLE JClass_ExtendsClassTransitive(    -- JOIN table for n:n relationship
        extendsClass     INTEGER,
        extendingClasses INTEGER,
        salsaProjectId INTEGER,
        PRIMARY KEY ( extendsClass,extendingClasses, salsaProjectId),
        KEY(extendsClass, salsaProjectId), KEY(extendingClasses, salsaProjectId)
    ) type=MyISAM;
COMMIT;

CREATE TABLE JMethod_Calls(
        calls INTEGER,
        callers INTEGER,
        filePosition INTEGER,
        salsaProjectId INTEGER,
        PRIMARY KEY (calls, callers, salsaProjectId),
        KEY(calls, salsaProjectId), KEY(callers, salsaProjectId)
    ) type=MyISAM;
CREATE TABLE JMethod_CallsTransitive(
        calls INTEGER,
        callers INTEGER,
        filePosition INTEGER,
        salsaProjectId INTEGER,
        PRIMARY KEY (calls, callers, salsaProjectId),
        KEY(calls, salsaProjectId), KEY(callers, salsaProjectId)
    ) type=MyISAM;
COMMIT;

CREATE TABLE JMethod_ThrowsExceptions(
        throwsExceptions INTEGER,
        thrownBy INTEGER,
        filePosition INTEGER,
        salsaProjectId INTEGER,
        PRIMARY KEY ( throwsExceptions,thrownBy, salsaProjectId ),
        KEY(throwsExceptions, salsaProjectId), KEY(thrownBy, salsaProjectId)
    ) type=MyISAM;
COMMIT;

CREATE TABLE JMethod_CatchesExceptions(
        catchesExceptions INTEGER,
        caughtBy INTEGER,
        filePosition INTEGER,
        salsaProjectId INTEGER,
        PRIMARY KEY ( catchesExceptions,caughtBy, salsaProjectId ),
        KEY(catchesExceptions, salsaProjectId), KEY(caughtBy, salsaProjectId)
    ) type=MyISAM;
COMMIT;
CREATE TABLE JMethod_Instantiates(
        instantiates   INTEGER,
        instantiatedBy INTEGER,
        filePosition INTEGER,
        salsaProjectId INTEGER,
        PRIMARY KEY ( instantiates,instantiatedBy, salsaProjectId),
        KEY(instantiates, salsaProjectId), KEY(instantiatedBy, salsaProjectId)
--        callingMethod     INTEGER,
--        instantiatedClass INTEGER,
--        PRIMARY KEY ( callingMethod,instantiatedClass ),
--        KEY(callingMethod), KEY(instantiatedClass)
    ) type=MyISAM;

COMMIT;

CREATE TABLE JMethod(
        id INTEGER AUTO_INCREMENT,
        name VARCHAR(255),
        shortName VARCHAR(255),
        isAbstract BOOLEAN,
        isConstructor BOOLEAN,
        isStatic BOOLEAN,
        isFinal BOOLEAN,
        numParams INTEGER,
        accessibility VARCHAR(255),
        filePosition INTEGER,

        signature VARCHAR(255),
        typeSignature VARCHAR(255),
        paramSignature VARCHAR(255),

        numStatements INTEGER,
        numLocalVariables INTEGER,
        numTryClauses INTEGER,
        numCatchClauses INTEGER,
        numCalls INTEGER,
        numInstantiations INTEGER,
        numAnonClassInstantiations INTEGER,
        numLines INTEGER,
        numSynchronizedBlocks INTEGER,
        numReturnStatements INTEGER,
        numThrowStatements INTEGER,
        numAssertStatements INTEGER,
        numCasts INTEGER,
        numSkips INTEGER,
        numAssignStatements INTEGER,
        numInitializedLocalVariables INTEGER,
        numLoopStatements INTEGER,
        numIfStatements INTEGER,
        numSwitchStatements INTEGER,
        numCaseStatements INTEGER,
        numBranchStatements INTEGER,
        numLinearSegments INTEGER,
        numLocalAnnotations INTEGER,

        parentClass INTEGER,
        returnType INTEGER,
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

-- CREATE UNIQUE INDEX JMethod_PRIMARY_KEY ON JMethod(id);
CREATE UNIQUE INDEX JMethod_UNIQUE_KEY_UniqueName ON JMethod(name, salsaProjectId);
CREATE INDEX JMethod_parentClass ON JMethod(parentClass, salsaProjectId);
COMMIT;

CREATE TABLE JMethod_UsedFields (
        usedFields INTEGER,
        usedByMethods INTEGER,
        filePosition INTEGER,
        salsaProjectId INTEGER,
        PRIMARY KEY ( usedFields,usedByMethods ),
        KEY(usedFields), KEY(usedByMethods)
    ) type=MyISAM;

COMMIT;

CREATE TABLE JField(
        id INTEGER AUTO_INCREMENT,
        name VARCHAR(255),
        shortName VARCHAR(255),
        isStatic BOOLEAN,
        isFinal BOOLEAN,
        accessibility VARCHAR(255),
        filePosition INTEGER,
        parentClass INTEGER,
        type INTEGER,
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

-- CREATE UNIQUE INDEX JField_PRIMARY_KEY ON JField(id);
CREATE UNIQUE INDEX JField_UNIQUE_KEY_UniqueName ON JField(name, salsaProjectId);
CREATE INDEX JField_parentClass ON JField(parentClass, salsaProjectId);

COMMIT;

CREATE TABLE JParameter(
        id INTEGER AUTO_INCREMENT,
        name VARCHAR(255),
        shortName VARCHAR(255),
        parameterIndex INTEGER,
        method INTEGER,
        type INTEGER,
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

-- CREATE UNIQUE INDEX JParameter_PRIMARY_KEY ON JParameter(id);
CREATE UNIQUE INDEX JParameter_UNIQUE_KEY_UniqueName ON JParameter(name, salsaProjectId);
COMMIT;

CREATE TABLE JAnnotation(
        id INTEGER AUTO_INCREMENT,
        name VARCHAR(255),
        shortName VARCHAR(255),
        numArgs INTEGER,
        parentClass INTEGER,
        parentMethod INTEGER,
        parentField INTEGER,
        parentParameter INTEGER,
        type INTEGER,
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

-- CREATE UNIQUE INDEX JAnnotation_PRIMARY_KEY ON JAnnotation(id);
CREATE UNIQUE INDEX JAnnotation_UNIQUE_KEY_UniqueName ON JAnnotation(name, salsaProjectId);
CREATE INDEX JAnnotation_parentClass ON JAnnotation(parentClass, salsaProjectId);

COMMIT;

CREATE TABLE JAnnotationArgument(
        id INTEGER AUTO_INCREMENT,
        name VARCHAR(255),
        shortName VARCHAR(255),
        stringValue VARCHAR(10000),
        annotation INTEGER,
        type INTEGER,
        salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

-- CREATE UNIQUE INDEX JAnnotationArgument_PRIMARY_KEY ON JAnnotationArgument(id);
CREATE UNIQUE INDEX JAnnotationArgument_UNIQUE_KEY_UniqueName ON JAnnotationArgument(name, salsaProjectId);
COMMIT;

CREATE TABLE JCallpath(
    
        id INTEGER AUTO_INCREMENT, 
        name VARCHAR(255), 
        parentMethod INTEGER,
	salsaProjectId INTEGER,
        PRIMARY KEY ( id )
    ) type=MyISAM;

CREATE UNIQUE INDEX JCallpath_PRIMARY_KEY ON JCallpath(id, salsaProjectId);
-- CREATE UNIQUE INDEX JCallpath_UNIQUE_KEY_UniqueName ON JCallpath(name, salsaProjectId);
COMMIT;


CREATE TABLE JCallpath_Classes(
    -- JOIN table for n:n relationship
    
        classes INTEGER, 
        callpaths INTEGER,
	salsaProjectId INTEGER,
        PRIMARY KEY ( classes,callpaths )
    ) type=MyISAM;


COMMIT;

ALTER TABLE JPackage ADD CONSTRAINT JPackage_parentPackage FOREIGN KEY (parentPackage) REFERENCES JPackage(id);
ALTER TABLE JPackage_PackagesTransitive ADD CONSTRAINT JPackage_PackagesTransitive_fk_for_packages FOREIGN KEY (packages) REFERENCES JPackage(id);
ALTER TABLE JPackage_PackagesTransitive ADD CONSTRAINT JPackage_PackagesTransitive_fk_for_parentPackage FOREIGN KEY (parentPackage) REFERENCES JPackage(id);
ALTER TABLE JClass ADD CONSTRAINT JClass_parentPackage FOREIGN KEY (parentPackage) REFERENCES JPackage(id);
ALTER TABLE JClass ADD CONSTRAINT JClass_extendsClass FOREIGN KEY (extendsClass) REFERENCES JClass(id);
ALTER TABLE JClass_ExtendsClassTransitive ADD CONSTRAINT JClass_ExtendsClassTransitive_fk_for_extendsClass FOREIGN KEY (extendsClass) REFERENCES JClass(id);
ALTER TABLE JClass_ExtendsClassTransitive ADD CONSTRAINT JClass_ExtendsClassTransitive_fk_for_extendingClasses FOREIGN KEY (extendingClasses) REFERENCES JClass(id);
ALTER TABLE JClass ADD CONSTRAINT JClass_containingClass FOREIGN KEY (containingClass) REFERENCES JClass(id);
ALTER TABLE JClass_ContainsClassesTransitive ADD CONSTRAINT JClass_ContainsClassesTransitive_fk_for_containsClasses FOREIGN KEY (containsClasses) REFERENCES JClass(id);
ALTER TABLE JClass_ContainsClassesTransitive ADD CONSTRAINT JClass_ContainsClassesTransitive_fk_for_containingClass FOREIGN KEY (containingClass) REFERENCES JClass(id);
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
ALTER TABLE JCallpath ADD CONSTRAINT JCallpath_parentMethod FOREIGN KEY (parentMethod) REFERENCES JMethod(id);
ALTER TABLE JCallpath_Classes ADD CONSTRAINT JCallpath_Classes_fk_for_classes FOREIGN KEY (classes) REFERENCES JCallpath(id);
ALTER TABLE JCallpath_Classes ADD CONSTRAINT JCallpath_Classes_fk_for_callpaths FOREIGN KEY (callpaths) REFERENCES JClass(id);
