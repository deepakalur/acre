REPLACE INTO SalsaProject (id, project, version, source) values(1, 'p1' , 'v1.2.3' , NULL);
SELECT @project_id := id FROM SalsaProject WHERE project = 'p1'  AND version = 'v1.2.3' ;
REPLACE INTO JPackage(id, name, shortName, parentPackage, salsaProjectId) VALUES(2, 'my' , 'my' , NULL, @project_id);
REPLACE INTO JPackage(id, name, shortName, parentPackage, salsaProjectId) VALUES(3, 'my.salsa' , 'salsa' , 2, @project_id);
REPLACE INTO JPackage_PackagesTransitive(packages, parentPackage, salsaProjectId) VALUES(3, 2, @project_id);
REPLACE INTO JPackage(id, name, shortName, parentPackage, salsaProjectId) VALUES(4, 'my.salsa.test' , 'test' , 3, @project_id);
REPLACE INTO JPackage_PackagesTransitive(packages, parentPackage, salsaProjectId) VALUES(4, 3, @project_id);
REPLACE INTO JClass(id, name, shortName, isStatic, isFinal, isInner, isInterface, accessibility, sourceFile, filePosition, containingClass, parentPackage, salsaProjectId
    ) VALUES(5, 'my.salsa.test.A' , 'A' , false, false, false, false, '<package>' , 'test/my/salsa/test/Test4.java' , 5, NULL, 4, @project_id);
UPDATE JClass SET extendsClass = 6 WHERE id = 5;
REPLACE INTO JClass_ExtendsClassTransitive(extendsClass, extendingClasses) VALUES (6, 5);
REPLACE INTO JMethod_Calls(calls, callers, filePosition) VALUES (8, 7, 5);
REPLACE INTO JMethod_CallsTransitive(calls, callers, filePosition) VALUES (8, 7, 5);
REPLACE INTO JMethod(id, name, shortName,isAbstract, isConstructor, isStatic, isFinal, numParams, accessibility,filePosition, parentClass, returnType, signature, salsaProjectId) VALUES (7, 'my.salsa.test.A.A[]' , 'A' , false, true, false, false, 0, '<package>' , 5, 5, 9, 'void A[]' , @project_id);
REPLACE INTO JMethod(id, name, shortName,isAbstract, isConstructor, isStatic, isFinal, numParams, accessibility,filePosition, parentClass, returnType, signature, salsaProjectId) VALUES (10, 'my.salsa.test.A.methodA[]' , 'methodA' , false, false, false, false, 0, '<package>' , 6, 5, 9, 'void methodA[]' , @project_id);
REPLACE INTO JClass(id, name, shortName, isStatic, isFinal, isInner, isInterface, accessibility, sourceFile, filePosition, containingClass, parentPackage, salsaProjectId
    ) VALUES(11, 'my.salsa.test.B' , 'B' , false, false, false, false, '<package>' , 'test/my/salsa/test/Test4.java' , 10, NULL, 4, @project_id);
UPDATE JClass SET extendsClass = 5 WHERE id = 11;
REPLACE INTO JClass_ExtendsClassTransitive(extendsClass, extendingClasses) VALUES (5, 11);
REPLACE INTO JMethod_Calls(calls, callers, filePosition) VALUES (7, 12, 10);
REPLACE INTO JMethod_CallsTransitive(calls, callers, filePosition) VALUES (7, 12, 10);
REPLACE INTO JMethod(id, name, shortName,isAbstract, isConstructor, isStatic, isFinal, numParams, accessibility,filePosition, parentClass, returnType, signature, salsaProjectId) VALUES (12, 'my.salsa.test.B.B[]' , 'B' , false, true, false, false, 0, '<package>' , 10, 11, 9, 'void B[]' , @project_id);
REPLACE INTO JMethod_Calls(calls, callers, filePosition) VALUES (10, 13, 12);
REPLACE INTO JMethod_CallsTransitive(calls, callers, filePosition) VALUES (10, 13, 12);
REPLACE INTO JMethod_ThrowsExceptions(throwsExceptions, thrownBy, filePosition) VALUES (14, 13, 13);
REPLACE INTO JMethod_Instantiates(callingMethod, instantiatedClass, filePosition) VALUES (13, 14, 13);
REPLACE INTO JMethod(id, name, shortName,isAbstract, isConstructor, isStatic, isFinal, numParams, accessibility,filePosition, parentClass, returnType, signature, salsaProjectId) VALUES (13, 'my.salsa.test.B.methodB[]' , 'methodB' , false, false, false, false, 0, '<package>' , 11, 11, 9, 'void methodB[]' , @project_id);
REPLACE INTO JClass(id, name, shortName, isStatic, isFinal, isInner, isInterface, accessibility, sourceFile, filePosition, containingClass, parentPackage, salsaProjectId
    ) VALUES(15, 'my.salsa.test.C' , 'C' , false, false, false, false, '<package>' , 'test/my/salsa/test/Test4.java' , 17, NULL, 4, @project_id);
UPDATE JClass SET extendsClass = 11 WHERE id = 15;
REPLACE INTO JClass_ExtendsClassTransitive(extendsClass, extendingClasses) VALUES (11, 15);
REPLACE INTO JMethod_Calls(calls, callers, filePosition) VALUES (12, 16, 17);
REPLACE INTO JMethod_CallsTransitive(calls, callers, filePosition) VALUES (12, 16, 17);
REPLACE INTO JMethod(id, name, shortName,isAbstract, isConstructor, isStatic, isFinal, numParams, accessibility,filePosition, parentClass, returnType, signature, salsaProjectId) VALUES (16, 'my.salsa.test.C.C[]' , 'C' , false, true, false, false, 0, '<package>' , 17, 15, 9, 'void C[]' , @project_id);
REPLACE INTO JMethod_Calls(calls, callers, filePosition) VALUES (13, 17, 19);
REPLACE INTO JMethod_CallsTransitive(calls, callers, filePosition) VALUES (13, 17, 19);
REPLACE INTO JMethod(id, name, shortName,isAbstract, isConstructor, isStatic, isFinal, numParams, accessibility,filePosition, parentClass, returnType, signature, salsaProjectId) VALUES (17, 'my.salsa.test.C.methodC[]' , 'methodC' , false, false, false, false, 0, '<package>' , 18, 15, 9, 'void methodC[]' , @project_id);
REPLACE INTO JClass(id, name, shortName, isStatic, isFinal, isInner, isInterface, accessibility, sourceFile, filePosition, containingClass, parentPackage, salsaProjectId
    ) VALUES(14, 'my.salsa.test.Eee' , 'Eee' , false, false, false, false, '<package>' , 'test/my/salsa/test/Test4.java' , 22, NULL, 4, @project_id);
UPDATE JClass SET extendsClass = 18 WHERE id = 14;
REPLACE INTO JClass_ExtendsClassTransitive(extendsClass, extendingClasses) VALUES (18, 14);
REPLACE INTO JMethod_Calls(calls, callers, filePosition) VALUES (20, 19, 22);
REPLACE INTO JMethod_CallsTransitive(calls, callers, filePosition) VALUES (20, 19, 22);
REPLACE INTO JMethod(id, name, shortName,isAbstract, isConstructor, isStatic, isFinal, numParams, accessibility,filePosition, parentClass, returnType, signature, salsaProjectId) VALUES (19, 'my.salsa.test.Eee.Eee[]' , 'Eee' , false, true, false, false, 0, '<package>' , 22, 14, 9, 'void Eee[]' , @project_id);
REPLACE INTO JMethod(id, name, shortName,isAbstract, isConstructor, isStatic, isFinal, numParams, accessibility,filePosition, parentClass, returnType, signature, salsaProjectId) VALUES (20, 'java.io.IOException.IOException[]' , 'IOException' , false, true, false, false, 0, 'public' , 1, 18, 9, 'void IOException[]' , @project_id);
REPLACE INTO JClass(id, name, shortName, isStatic, isFinal, isInner, isInterface, accessibility, sourceFile, filePosition, containingClass, parentPackage, salsaProjectId
    ) VALUES(18, 'java.io.IOException' , 'IOException' , false, false, false, false, 'public' , 'IOException.java' , 1, NULL, 21, @project_id);
UPDATE JClass SET extendsClass = 22 WHERE id = 18;
REPLACE INTO JClass_ExtendsClassTransitive(extendsClass, extendingClasses) VALUES (22, 18);
REPLACE INTO JPackage(id, name, shortName, parentPackage, salsaProjectId) VALUES(21, 'java.io' , 'io' , 23, @project_id);
REPLACE INTO JPackage_PackagesTransitive(packages, parentPackage, salsaProjectId) VALUES(21, 23, @project_id);
REPLACE INTO JPackage(id, name, shortName, parentPackage, salsaProjectId) VALUES(23, 'java' , 'java' , NULL, @project_id);
REPLACE INTO JMethod(id, name, shortName,isAbstract, isConstructor, isStatic, isFinal, numParams, accessibility,filePosition, parentClass, returnType, signature, salsaProjectId) VALUES (8, 'java.lang.Object.Object[]' , 'Object' , false, true, false, false, 0, 'public' , 1, 6, 9, 'void Object[]' , @project_id);
REPLACE INTO JClass(id, name, shortName, isStatic, isFinal, isInner, isInterface, accessibility, sourceFile, filePosition, containingClass, parentPackage, salsaProjectId
    ) VALUES(6, 'java.lang.Object' , 'Object' , false, false, false, false, 'public' , 'Object.java' , 1, NULL, 24, @project_id);
REPLACE INTO JPackage(id, name, shortName, parentPackage, salsaProjectId) VALUES(24, 'java.lang' , 'lang' , 23, @project_id);
REPLACE INTO JPackage_PackagesTransitive(packages, parentPackage, salsaProjectId) VALUES(24, 23, @project_id);
REPLACE INTO JClass(id, name, shortName, isStatic, isFinal, isInner, isInterface, accessibility, sourceFile, filePosition, containingClass, parentPackage, salsaProjectId
    ) VALUES(22, 'java.lang.Exception' , 'Exception' , false, false, false, false, 'public' , 'Exception.java' , 1, NULL, 24, @project_id);
UPDATE JClass SET extendsClass = 25 WHERE id = 22;
REPLACE INTO JClass_ExtendsClassTransitive(extendsClass, extendingClasses) VALUES (25, 22);
REPLACE INTO JClass(id, name, shortName, isStatic, isFinal, isInner, isInterface, accessibility, sourceFile, filePosition, containingClass, parentPackage, salsaProjectId
    ) VALUES(25, 'java.lang.Throwable' , 'Throwable' , false, false, false, false, 'public' , 'Throwable.java' , 1, NULL, 24, @project_id);
UPDATE JClass SET extendsClass = 6 WHERE id = 25;
REPLACE INTO JClass_ExtendsClassTransitive(extendsClass, extendingClasses) VALUES (6, 25);
REPLACE INTO JClass_ImplementsInterfaces(implementsInterfaces, implementingClasses) VALUES(26, 25);
REPLACE INTO JClass_ImplementsInterfacesTransitive(implementsInterfaces, implementingClasses) VALUES(26, 25);
REPLACE INTO JClass(id, name, shortName, isStatic, isFinal, isInner, isInterface, accessibility, sourceFile, filePosition, containingClass, parentPackage, salsaProjectId
    ) VALUES(26, 'java.io.Serializable' , 'Serializable' , false, false, false, true, 'public' , 'Serializable.java' , 1, NULL, 21, @project_id);
REPLACE INTO JNonClassType (id, name, shortName, salsaProjectId) values(9, 'void' , 'void' , @project_id);
--  ============== Populate JClass_ContainsClassesTransitive: Start
SELECT @cntold := 0;
SELECT @cntnew := COUNT(*) FROM JClass_ContainsClassesTransitive;
--  ============== Populate JClass_ContainsClassesTransitive: iteration 1
REPLACE into JClass_ContainsClassesTransitive SELECT a.containsClasses, b.containingClass FROM JClass_ContainsClassesTransitive AS a, JClass_ContainsClassesTransitive AS b
WHERE @cntnew != @cntold AND a.containingClass = b.containsClasses;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ContainsClassesTransitive;
--  ============== Populate JClass_ContainsClassesTransitive: iteration 2
REPLACE into JClass_ContainsClassesTransitive SELECT a.containsClasses, b.containingClass FROM JClass_ContainsClassesTransitive AS a, JClass_ContainsClassesTransitive AS b
WHERE @cntnew != @cntold AND a.containingClass = b.containsClasses;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ContainsClassesTransitive;
--  ============== Populate JClass_ContainsClassesTransitive: iteration 3
REPLACE into JClass_ContainsClassesTransitive SELECT a.containsClasses, b.containingClass FROM JClass_ContainsClassesTransitive AS a, JClass_ContainsClassesTransitive AS b
WHERE @cntnew != @cntold AND a.containingClass = b.containsClasses;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ContainsClassesTransitive;
--  ============== Populate JClass_ContainsClassesTransitive: iteration 4
REPLACE into JClass_ContainsClassesTransitive SELECT a.containsClasses, b.containingClass FROM JClass_ContainsClassesTransitive AS a, JClass_ContainsClassesTransitive AS b
WHERE @cntnew != @cntold AND a.containingClass = b.containsClasses;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ContainsClassesTransitive;
--  ============== Populate JClass_ContainsClassesTransitive: iteration 5
REPLACE into JClass_ContainsClassesTransitive SELECT a.containsClasses, b.containingClass FROM JClass_ContainsClassesTransitive AS a, JClass_ContainsClassesTransitive AS b
WHERE @cntnew != @cntold AND a.containingClass = b.containsClasses;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ContainsClassesTransitive;
--  ============== Populate JClass_ContainsClassesTransitive: iteration 6
REPLACE into JClass_ContainsClassesTransitive SELECT a.containsClasses, b.containingClass FROM JClass_ContainsClassesTransitive AS a, JClass_ContainsClassesTransitive AS b
WHERE @cntnew != @cntold AND a.containingClass = b.containsClasses;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ContainsClassesTransitive;
--  ============== Populate JClass_ContainsClassesTransitive: iteration 7
REPLACE into JClass_ContainsClassesTransitive SELECT a.containsClasses, b.containingClass FROM JClass_ContainsClassesTransitive AS a, JClass_ContainsClassesTransitive AS b
WHERE @cntnew != @cntold AND a.containingClass = b.containsClasses;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ContainsClassesTransitive;
--  ============== Populate JClass_ContainsClassesTransitive: iteration 8
REPLACE into JClass_ContainsClassesTransitive SELECT a.containsClasses, b.containingClass FROM JClass_ContainsClassesTransitive AS a, JClass_ContainsClassesTransitive AS b
WHERE @cntnew != @cntold AND a.containingClass = b.containsClasses;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ContainsClassesTransitive;
--  ============== Populate JClass_ContainsClassesTransitive: iteration 9
REPLACE into JClass_ContainsClassesTransitive SELECT a.containsClasses, b.containingClass FROM JClass_ContainsClassesTransitive AS a, JClass_ContainsClassesTransitive AS b
WHERE @cntnew != @cntold AND a.containingClass = b.containsClasses;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ContainsClassesTransitive;
--  ============== Populate JClass_ContainsClassesTransitive: iteration 10
REPLACE into JClass_ContainsClassesTransitive SELECT a.containsClasses, b.containingClass FROM JClass_ContainsClassesTransitive AS a, JClass_ContainsClassesTransitive AS b
WHERE @cntnew != @cntold AND a.containingClass = b.containsClasses;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ContainsClassesTransitive;
--  ============== Populate JClass_ContainsClassesTransitive: iteration 11
REPLACE into JClass_ContainsClassesTransitive SELECT a.containsClasses, b.containingClass FROM JClass_ContainsClassesTransitive AS a, JClass_ContainsClassesTransitive AS b
WHERE @cntnew != @cntold AND a.containingClass = b.containsClasses;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ContainsClassesTransitive;
--  ============== Populate JClass_ContainsClassesTransitive: iteration 12
REPLACE into JClass_ContainsClassesTransitive SELECT a.containsClasses, b.containingClass FROM JClass_ContainsClassesTransitive AS a, JClass_ContainsClassesTransitive AS b
WHERE @cntnew != @cntold AND a.containingClass = b.containsClasses;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ContainsClassesTransitive;
--  ============== Populate JClass_ContainsClassesTransitive: iteration 13
REPLACE into JClass_ContainsClassesTransitive SELECT a.containsClasses, b.containingClass FROM JClass_ContainsClassesTransitive AS a, JClass_ContainsClassesTransitive AS b
WHERE @cntnew != @cntold AND a.containingClass = b.containsClasses;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ContainsClassesTransitive;
--  ============== Populate JClass_ContainsClassesTransitive: iteration 14
REPLACE into JClass_ContainsClassesTransitive SELECT a.containsClasses, b.containingClass FROM JClass_ContainsClassesTransitive AS a, JClass_ContainsClassesTransitive AS b
WHERE @cntnew != @cntold AND a.containingClass = b.containsClasses;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ContainsClassesTransitive;
--  ============== Populate JClass_ContainsClassesTransitive: iteration 15
REPLACE into JClass_ContainsClassesTransitive SELECT a.containsClasses, b.containingClass FROM JClass_ContainsClassesTransitive AS a, JClass_ContainsClassesTransitive AS b
WHERE @cntnew != @cntold AND a.containingClass = b.containsClasses;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ContainsClassesTransitive;
--  ============== Populate JClass_ContainsClassesTransitive: iteration 16
REPLACE into JClass_ContainsClassesTransitive SELECT a.containsClasses, b.containingClass FROM JClass_ContainsClassesTransitive AS a, JClass_ContainsClassesTransitive AS b
WHERE @cntnew != @cntold AND a.containingClass = b.containsClasses;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ContainsClassesTransitive;
--  ============== Populate JClass_ContainsClassesTransitive: iteration 17
REPLACE into JClass_ContainsClassesTransitive SELECT a.containsClasses, b.containingClass FROM JClass_ContainsClassesTransitive AS a, JClass_ContainsClassesTransitive AS b
WHERE @cntnew != @cntold AND a.containingClass = b.containsClasses;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ContainsClassesTransitive;
--  ============== Populate JClass_ContainsClassesTransitive: iteration 18
REPLACE into JClass_ContainsClassesTransitive SELECT a.containsClasses, b.containingClass FROM JClass_ContainsClassesTransitive AS a, JClass_ContainsClassesTransitive AS b
WHERE @cntnew != @cntold AND a.containingClass = b.containsClasses;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ContainsClassesTransitive;
--  ============== Populate JClass_ContainsClassesTransitive: iteration 19
REPLACE into JClass_ContainsClassesTransitive SELECT a.containsClasses, b.containingClass FROM JClass_ContainsClassesTransitive AS a, JClass_ContainsClassesTransitive AS b
WHERE @cntnew != @cntold AND a.containingClass = b.containsClasses;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ContainsClassesTransitive;
--  ============== Populate JClass_ContainsClassesTransitive: iteration 20
REPLACE into JClass_ContainsClassesTransitive SELECT a.containsClasses, b.containingClass FROM JClass_ContainsClassesTransitive AS a, JClass_ContainsClassesTransitive AS b
WHERE @cntnew != @cntold AND a.containingClass = b.containsClasses;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ContainsClassesTransitive;
--  ============== Populate JClass_ContainsClassesTransitive: iteration 21
REPLACE into JClass_ContainsClassesTransitive SELECT a.containsClasses, b.containingClass FROM JClass_ContainsClassesTransitive AS a, JClass_ContainsClassesTransitive AS b
WHERE @cntnew != @cntold AND a.containingClass = b.containsClasses;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ContainsClassesTransitive;
--  ============== Populate JClass_ContainsClassesTransitive: iteration 22
REPLACE into JClass_ContainsClassesTransitive SELECT a.containsClasses, b.containingClass FROM JClass_ContainsClassesTransitive AS a, JClass_ContainsClassesTransitive AS b
WHERE @cntnew != @cntold AND a.containingClass = b.containsClasses;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ContainsClassesTransitive;
--  ============== Populate JClass_ContainsClassesTransitive: iteration 23
REPLACE into JClass_ContainsClassesTransitive SELECT a.containsClasses, b.containingClass FROM JClass_ContainsClassesTransitive AS a, JClass_ContainsClassesTransitive AS b
WHERE @cntnew != @cntold AND a.containingClass = b.containsClasses;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ContainsClassesTransitive;
--  ============== Populate JClass_ContainsClassesTransitive: iteration 24
REPLACE into JClass_ContainsClassesTransitive SELECT a.containsClasses, b.containingClass FROM JClass_ContainsClassesTransitive AS a, JClass_ContainsClassesTransitive AS b
WHERE @cntnew != @cntold AND a.containingClass = b.containsClasses;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ContainsClassesTransitive;
--  ============== Populate JClass_ContainsClassesTransitive: iteration 25
REPLACE into JClass_ContainsClassesTransitive SELECT a.containsClasses, b.containingClass FROM JClass_ContainsClassesTransitive AS a, JClass_ContainsClassesTransitive AS b
WHERE @cntnew != @cntold AND a.containingClass = b.containsClasses;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ContainsClassesTransitive;
--  ============== Populate JClass_ContainsClassesTransitive: iteration 26
REPLACE into JClass_ContainsClassesTransitive SELECT a.containsClasses, b.containingClass FROM JClass_ContainsClassesTransitive AS a, JClass_ContainsClassesTransitive AS b
WHERE @cntnew != @cntold AND a.containingClass = b.containsClasses;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ContainsClassesTransitive;
--  ============== Populate JClass_ContainsClassesTransitive: iteration 27
REPLACE into JClass_ContainsClassesTransitive SELECT a.containsClasses, b.containingClass FROM JClass_ContainsClassesTransitive AS a, JClass_ContainsClassesTransitive AS b
WHERE @cntnew != @cntold AND a.containingClass = b.containsClasses;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ContainsClassesTransitive;
--  ============== Populate JClass_ContainsClassesTransitive: iteration 28
REPLACE into JClass_ContainsClassesTransitive SELECT a.containsClasses, b.containingClass FROM JClass_ContainsClassesTransitive AS a, JClass_ContainsClassesTransitive AS b
WHERE @cntnew != @cntold AND a.containingClass = b.containsClasses;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ContainsClassesTransitive;
--  ============== Populate JClass_ContainsClassesTransitive: iteration 29
REPLACE into JClass_ContainsClassesTransitive SELECT a.containsClasses, b.containingClass FROM JClass_ContainsClassesTransitive AS a, JClass_ContainsClassesTransitive AS b
WHERE @cntnew != @cntold AND a.containingClass = b.containsClasses;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ContainsClassesTransitive;
--  ============== Populate JClass_ContainsClassesTransitive: iteration 30
REPLACE into JClass_ContainsClassesTransitive SELECT a.containsClasses, b.containingClass FROM JClass_ContainsClassesTransitive AS a, JClass_ContainsClassesTransitive AS b
WHERE @cntnew != @cntold AND a.containingClass = b.containsClasses;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ContainsClassesTransitive;
--  ============== Populate JClass_ContainsClassesTransitive: iteration 31
REPLACE into JClass_ContainsClassesTransitive SELECT a.containsClasses, b.containingClass FROM JClass_ContainsClassesTransitive AS a, JClass_ContainsClassesTransitive AS b
WHERE @cntnew != @cntold AND a.containingClass = b.containsClasses;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ContainsClassesTransitive;
--  ============== Populate JClass_ContainsClassesTransitive: iteration 32
REPLACE into JClass_ContainsClassesTransitive SELECT a.containsClasses, b.containingClass FROM JClass_ContainsClassesTransitive AS a, JClass_ContainsClassesTransitive AS b
WHERE @cntnew != @cntold AND a.containingClass = b.containsClasses;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ContainsClassesTransitive;
--  ============== Populate JClass_ContainsClassesTransitive: iteration 33
REPLACE into JClass_ContainsClassesTransitive SELECT a.containsClasses, b.containingClass FROM JClass_ContainsClassesTransitive AS a, JClass_ContainsClassesTransitive AS b
WHERE @cntnew != @cntold AND a.containingClass = b.containsClasses;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ContainsClassesTransitive;
--  ============== Populate JClass_ContainsClassesTransitive: iteration 34
REPLACE into JClass_ContainsClassesTransitive SELECT a.containsClasses, b.containingClass FROM JClass_ContainsClassesTransitive AS a, JClass_ContainsClassesTransitive AS b
WHERE @cntnew != @cntold AND a.containingClass = b.containsClasses;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ContainsClassesTransitive;
--  ============== Populate JClass_ContainsClassesTransitive: iteration 35
REPLACE into JClass_ContainsClassesTransitive SELECT a.containsClasses, b.containingClass FROM JClass_ContainsClassesTransitive AS a, JClass_ContainsClassesTransitive AS b
WHERE @cntnew != @cntold AND a.containingClass = b.containsClasses;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ContainsClassesTransitive;
--  ============== Populate JClass_ContainsClassesTransitive: iteration 36
REPLACE into JClass_ContainsClassesTransitive SELECT a.containsClasses, b.containingClass FROM JClass_ContainsClassesTransitive AS a, JClass_ContainsClassesTransitive AS b
WHERE @cntnew != @cntold AND a.containingClass = b.containsClasses;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ContainsClassesTransitive;
--  ============== Populate JClass_ContainsClassesTransitive: iteration 37
REPLACE into JClass_ContainsClassesTransitive SELECT a.containsClasses, b.containingClass FROM JClass_ContainsClassesTransitive AS a, JClass_ContainsClassesTransitive AS b
WHERE @cntnew != @cntold AND a.containingClass = b.containsClasses;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ContainsClassesTransitive;
--  ============== Populate JClass_ContainsClassesTransitive: iteration 38
REPLACE into JClass_ContainsClassesTransitive SELECT a.containsClasses, b.containingClass FROM JClass_ContainsClassesTransitive AS a, JClass_ContainsClassesTransitive AS b
WHERE @cntnew != @cntold AND a.containingClass = b.containsClasses;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ContainsClassesTransitive;
--  ============== Populate JClass_ContainsClassesTransitive: iteration 39
REPLACE into JClass_ContainsClassesTransitive SELECT a.containsClasses, b.containingClass FROM JClass_ContainsClassesTransitive AS a, JClass_ContainsClassesTransitive AS b
WHERE @cntnew != @cntold AND a.containingClass = b.containsClasses;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ContainsClassesTransitive;
--  ============== Populate JClass_ContainsClassesTransitive: iteration 40
REPLACE into JClass_ContainsClassesTransitive SELECT a.containsClasses, b.containingClass FROM JClass_ContainsClassesTransitive AS a, JClass_ContainsClassesTransitive AS b
WHERE @cntnew != @cntold AND a.containingClass = b.containsClasses;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ContainsClassesTransitive;
--  ============== Populate JClass_ContainsClassesTransitive: iteration 41
REPLACE into JClass_ContainsClassesTransitive SELECT a.containsClasses, b.containingClass FROM JClass_ContainsClassesTransitive AS a, JClass_ContainsClassesTransitive AS b
WHERE @cntnew != @cntold AND a.containingClass = b.containsClasses;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ContainsClassesTransitive;
--  ============== Populate JClass_ContainsClassesTransitive: iteration 42
REPLACE into JClass_ContainsClassesTransitive SELECT a.containsClasses, b.containingClass FROM JClass_ContainsClassesTransitive AS a, JClass_ContainsClassesTransitive AS b
WHERE @cntnew != @cntold AND a.containingClass = b.containsClasses;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ContainsClassesTransitive;
--  ============== Populate JClass_ContainsClassesTransitive: iteration 43
REPLACE into JClass_ContainsClassesTransitive SELECT a.containsClasses, b.containingClass FROM JClass_ContainsClassesTransitive AS a, JClass_ContainsClassesTransitive AS b
WHERE @cntnew != @cntold AND a.containingClass = b.containsClasses;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ContainsClassesTransitive;
--  ============== Populate JClass_ContainsClassesTransitive: iteration 44
REPLACE into JClass_ContainsClassesTransitive SELECT a.containsClasses, b.containingClass FROM JClass_ContainsClassesTransitive AS a, JClass_ContainsClassesTransitive AS b
WHERE @cntnew != @cntold AND a.containingClass = b.containsClasses;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ContainsClassesTransitive;
--  ============== Populate JClass_ContainsClassesTransitive: iteration 45
REPLACE into JClass_ContainsClassesTransitive SELECT a.containsClasses, b.containingClass FROM JClass_ContainsClassesTransitive AS a, JClass_ContainsClassesTransitive AS b
WHERE @cntnew != @cntold AND a.containingClass = b.containsClasses;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ContainsClassesTransitive;
--  ============== Populate JClass_ContainsClassesTransitive: iteration 46
REPLACE into JClass_ContainsClassesTransitive SELECT a.containsClasses, b.containingClass FROM JClass_ContainsClassesTransitive AS a, JClass_ContainsClassesTransitive AS b
WHERE @cntnew != @cntold AND a.containingClass = b.containsClasses;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ContainsClassesTransitive;
--  ============== Populate JClass_ContainsClassesTransitive: iteration 47
REPLACE into JClass_ContainsClassesTransitive SELECT a.containsClasses, b.containingClass FROM JClass_ContainsClassesTransitive AS a, JClass_ContainsClassesTransitive AS b
WHERE @cntnew != @cntold AND a.containingClass = b.containsClasses;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ContainsClassesTransitive;
--  ============== Populate JClass_ContainsClassesTransitive: iteration 48
REPLACE into JClass_ContainsClassesTransitive SELECT a.containsClasses, b.containingClass FROM JClass_ContainsClassesTransitive AS a, JClass_ContainsClassesTransitive AS b
WHERE @cntnew != @cntold AND a.containingClass = b.containsClasses;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ContainsClassesTransitive;
--  ============== Populate JClass_ContainsClassesTransitive: iteration 49
REPLACE into JClass_ContainsClassesTransitive SELECT a.containsClasses, b.containingClass FROM JClass_ContainsClassesTransitive AS a, JClass_ContainsClassesTransitive AS b
WHERE @cntnew != @cntold AND a.containingClass = b.containsClasses;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ContainsClassesTransitive;
--  ============== Populate JClass_ContainsClassesTransitive: iteration 50
REPLACE into JClass_ContainsClassesTransitive SELECT a.containsClasses, b.containingClass FROM JClass_ContainsClassesTransitive AS a, JClass_ContainsClassesTransitive AS b
WHERE @cntnew != @cntold AND a.containingClass = b.containsClasses;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ContainsClassesTransitive;
--  ============== Populate JClass_ContainsClassesTransitive: End
--  ============== Populate JClass_ImplementsInterfacesTransitive: Start
SELECT @cntold := 0;
SELECT @cntnew := COUNT(*) FROM JClass_ImplementsInterfacesTransitive;
--  ============== Populate JClass_ImplementsInterfacesTransitive: iteration 1
REPLACE into JClass_ImplementsInterfacesTransitive SELECT a.implementsInterfaces, b.implementingClasses FROM JClass_ImplementsInterfacesTransitive AS a, JClass_ImplementsInterfacesTransitive AS b
WHERE @cntnew != @cntold AND a.implementingClasses = b.implementsInterfaces;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ImplementsInterfacesTransitive;
--  ============== Populate JClass_ImplementsInterfacesTransitive: iteration 2
REPLACE into JClass_ImplementsInterfacesTransitive SELECT a.implementsInterfaces, b.implementingClasses FROM JClass_ImplementsInterfacesTransitive AS a, JClass_ImplementsInterfacesTransitive AS b
WHERE @cntnew != @cntold AND a.implementingClasses = b.implementsInterfaces;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ImplementsInterfacesTransitive;
--  ============== Populate JClass_ImplementsInterfacesTransitive: iteration 3
REPLACE into JClass_ImplementsInterfacesTransitive SELECT a.implementsInterfaces, b.implementingClasses FROM JClass_ImplementsInterfacesTransitive AS a, JClass_ImplementsInterfacesTransitive AS b
WHERE @cntnew != @cntold AND a.implementingClasses = b.implementsInterfaces;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ImplementsInterfacesTransitive;
--  ============== Populate JClass_ImplementsInterfacesTransitive: iteration 4
REPLACE into JClass_ImplementsInterfacesTransitive SELECT a.implementsInterfaces, b.implementingClasses FROM JClass_ImplementsInterfacesTransitive AS a, JClass_ImplementsInterfacesTransitive AS b
WHERE @cntnew != @cntold AND a.implementingClasses = b.implementsInterfaces;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ImplementsInterfacesTransitive;
--  ============== Populate JClass_ImplementsInterfacesTransitive: iteration 5
REPLACE into JClass_ImplementsInterfacesTransitive SELECT a.implementsInterfaces, b.implementingClasses FROM JClass_ImplementsInterfacesTransitive AS a, JClass_ImplementsInterfacesTransitive AS b
WHERE @cntnew != @cntold AND a.implementingClasses = b.implementsInterfaces;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ImplementsInterfacesTransitive;
--  ============== Populate JClass_ImplementsInterfacesTransitive: iteration 6
REPLACE into JClass_ImplementsInterfacesTransitive SELECT a.implementsInterfaces, b.implementingClasses FROM JClass_ImplementsInterfacesTransitive AS a, JClass_ImplementsInterfacesTransitive AS b
WHERE @cntnew != @cntold AND a.implementingClasses = b.implementsInterfaces;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ImplementsInterfacesTransitive;
--  ============== Populate JClass_ImplementsInterfacesTransitive: iteration 7
REPLACE into JClass_ImplementsInterfacesTransitive SELECT a.implementsInterfaces, b.implementingClasses FROM JClass_ImplementsInterfacesTransitive AS a, JClass_ImplementsInterfacesTransitive AS b
WHERE @cntnew != @cntold AND a.implementingClasses = b.implementsInterfaces;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ImplementsInterfacesTransitive;
--  ============== Populate JClass_ImplementsInterfacesTransitive: iteration 8
REPLACE into JClass_ImplementsInterfacesTransitive SELECT a.implementsInterfaces, b.implementingClasses FROM JClass_ImplementsInterfacesTransitive AS a, JClass_ImplementsInterfacesTransitive AS b
WHERE @cntnew != @cntold AND a.implementingClasses = b.implementsInterfaces;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ImplementsInterfacesTransitive;
--  ============== Populate JClass_ImplementsInterfacesTransitive: iteration 9
REPLACE into JClass_ImplementsInterfacesTransitive SELECT a.implementsInterfaces, b.implementingClasses FROM JClass_ImplementsInterfacesTransitive AS a, JClass_ImplementsInterfacesTransitive AS b
WHERE @cntnew != @cntold AND a.implementingClasses = b.implementsInterfaces;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ImplementsInterfacesTransitive;
--  ============== Populate JClass_ImplementsInterfacesTransitive: iteration 10
REPLACE into JClass_ImplementsInterfacesTransitive SELECT a.implementsInterfaces, b.implementingClasses FROM JClass_ImplementsInterfacesTransitive AS a, JClass_ImplementsInterfacesTransitive AS b
WHERE @cntnew != @cntold AND a.implementingClasses = b.implementsInterfaces;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ImplementsInterfacesTransitive;
--  ============== Populate JClass_ImplementsInterfacesTransitive: iteration 11
REPLACE into JClass_ImplementsInterfacesTransitive SELECT a.implementsInterfaces, b.implementingClasses FROM JClass_ImplementsInterfacesTransitive AS a, JClass_ImplementsInterfacesTransitive AS b
WHERE @cntnew != @cntold AND a.implementingClasses = b.implementsInterfaces;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ImplementsInterfacesTransitive;
--  ============== Populate JClass_ImplementsInterfacesTransitive: iteration 12
REPLACE into JClass_ImplementsInterfacesTransitive SELECT a.implementsInterfaces, b.implementingClasses FROM JClass_ImplementsInterfacesTransitive AS a, JClass_ImplementsInterfacesTransitive AS b
WHERE @cntnew != @cntold AND a.implementingClasses = b.implementsInterfaces;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ImplementsInterfacesTransitive;
--  ============== Populate JClass_ImplementsInterfacesTransitive: iteration 13
REPLACE into JClass_ImplementsInterfacesTransitive SELECT a.implementsInterfaces, b.implementingClasses FROM JClass_ImplementsInterfacesTransitive AS a, JClass_ImplementsInterfacesTransitive AS b
WHERE @cntnew != @cntold AND a.implementingClasses = b.implementsInterfaces;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ImplementsInterfacesTransitive;
--  ============== Populate JClass_ImplementsInterfacesTransitive: iteration 14
REPLACE into JClass_ImplementsInterfacesTransitive SELECT a.implementsInterfaces, b.implementingClasses FROM JClass_ImplementsInterfacesTransitive AS a, JClass_ImplementsInterfacesTransitive AS b
WHERE @cntnew != @cntold AND a.implementingClasses = b.implementsInterfaces;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ImplementsInterfacesTransitive;
--  ============== Populate JClass_ImplementsInterfacesTransitive: iteration 15
REPLACE into JClass_ImplementsInterfacesTransitive SELECT a.implementsInterfaces, b.implementingClasses FROM JClass_ImplementsInterfacesTransitive AS a, JClass_ImplementsInterfacesTransitive AS b
WHERE @cntnew != @cntold AND a.implementingClasses = b.implementsInterfaces;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ImplementsInterfacesTransitive;
--  ============== Populate JClass_ImplementsInterfacesTransitive: iteration 16
REPLACE into JClass_ImplementsInterfacesTransitive SELECT a.implementsInterfaces, b.implementingClasses FROM JClass_ImplementsInterfacesTransitive AS a, JClass_ImplementsInterfacesTransitive AS b
WHERE @cntnew != @cntold AND a.implementingClasses = b.implementsInterfaces;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ImplementsInterfacesTransitive;
--  ============== Populate JClass_ImplementsInterfacesTransitive: iteration 17
REPLACE into JClass_ImplementsInterfacesTransitive SELECT a.implementsInterfaces, b.implementingClasses FROM JClass_ImplementsInterfacesTransitive AS a, JClass_ImplementsInterfacesTransitive AS b
WHERE @cntnew != @cntold AND a.implementingClasses = b.implementsInterfaces;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ImplementsInterfacesTransitive;
--  ============== Populate JClass_ImplementsInterfacesTransitive: iteration 18
REPLACE into JClass_ImplementsInterfacesTransitive SELECT a.implementsInterfaces, b.implementingClasses FROM JClass_ImplementsInterfacesTransitive AS a, JClass_ImplementsInterfacesTransitive AS b
WHERE @cntnew != @cntold AND a.implementingClasses = b.implementsInterfaces;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ImplementsInterfacesTransitive;
--  ============== Populate JClass_ImplementsInterfacesTransitive: iteration 19
REPLACE into JClass_ImplementsInterfacesTransitive SELECT a.implementsInterfaces, b.implementingClasses FROM JClass_ImplementsInterfacesTransitive AS a, JClass_ImplementsInterfacesTransitive AS b
WHERE @cntnew != @cntold AND a.implementingClasses = b.implementsInterfaces;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ImplementsInterfacesTransitive;
--  ============== Populate JClass_ImplementsInterfacesTransitive: iteration 20
REPLACE into JClass_ImplementsInterfacesTransitive SELECT a.implementsInterfaces, b.implementingClasses FROM JClass_ImplementsInterfacesTransitive AS a, JClass_ImplementsInterfacesTransitive AS b
WHERE @cntnew != @cntold AND a.implementingClasses = b.implementsInterfaces;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ImplementsInterfacesTransitive;
--  ============== Populate JClass_ImplementsInterfacesTransitive: iteration 21
REPLACE into JClass_ImplementsInterfacesTransitive SELECT a.implementsInterfaces, b.implementingClasses FROM JClass_ImplementsInterfacesTransitive AS a, JClass_ImplementsInterfacesTransitive AS b
WHERE @cntnew != @cntold AND a.implementingClasses = b.implementsInterfaces;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ImplementsInterfacesTransitive;
--  ============== Populate JClass_ImplementsInterfacesTransitive: iteration 22
REPLACE into JClass_ImplementsInterfacesTransitive SELECT a.implementsInterfaces, b.implementingClasses FROM JClass_ImplementsInterfacesTransitive AS a, JClass_ImplementsInterfacesTransitive AS b
WHERE @cntnew != @cntold AND a.implementingClasses = b.implementsInterfaces;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ImplementsInterfacesTransitive;
--  ============== Populate JClass_ImplementsInterfacesTransitive: iteration 23
REPLACE into JClass_ImplementsInterfacesTransitive SELECT a.implementsInterfaces, b.implementingClasses FROM JClass_ImplementsInterfacesTransitive AS a, JClass_ImplementsInterfacesTransitive AS b
WHERE @cntnew != @cntold AND a.implementingClasses = b.implementsInterfaces;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ImplementsInterfacesTransitive;
--  ============== Populate JClass_ImplementsInterfacesTransitive: iteration 24
REPLACE into JClass_ImplementsInterfacesTransitive SELECT a.implementsInterfaces, b.implementingClasses FROM JClass_ImplementsInterfacesTransitive AS a, JClass_ImplementsInterfacesTransitive AS b
WHERE @cntnew != @cntold AND a.implementingClasses = b.implementsInterfaces;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ImplementsInterfacesTransitive;
--  ============== Populate JClass_ImplementsInterfacesTransitive: iteration 25
REPLACE into JClass_ImplementsInterfacesTransitive SELECT a.implementsInterfaces, b.implementingClasses FROM JClass_ImplementsInterfacesTransitive AS a, JClass_ImplementsInterfacesTransitive AS b
WHERE @cntnew != @cntold AND a.implementingClasses = b.implementsInterfaces;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ImplementsInterfacesTransitive;
--  ============== Populate JClass_ImplementsInterfacesTransitive: iteration 26
REPLACE into JClass_ImplementsInterfacesTransitive SELECT a.implementsInterfaces, b.implementingClasses FROM JClass_ImplementsInterfacesTransitive AS a, JClass_ImplementsInterfacesTransitive AS b
WHERE @cntnew != @cntold AND a.implementingClasses = b.implementsInterfaces;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ImplementsInterfacesTransitive;
--  ============== Populate JClass_ImplementsInterfacesTransitive: iteration 27
REPLACE into JClass_ImplementsInterfacesTransitive SELECT a.implementsInterfaces, b.implementingClasses FROM JClass_ImplementsInterfacesTransitive AS a, JClass_ImplementsInterfacesTransitive AS b
WHERE @cntnew != @cntold AND a.implementingClasses = b.implementsInterfaces;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ImplementsInterfacesTransitive;
--  ============== Populate JClass_ImplementsInterfacesTransitive: iteration 28
REPLACE into JClass_ImplementsInterfacesTransitive SELECT a.implementsInterfaces, b.implementingClasses FROM JClass_ImplementsInterfacesTransitive AS a, JClass_ImplementsInterfacesTransitive AS b
WHERE @cntnew != @cntold AND a.implementingClasses = b.implementsInterfaces;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ImplementsInterfacesTransitive;
--  ============== Populate JClass_ImplementsInterfacesTransitive: iteration 29
REPLACE into JClass_ImplementsInterfacesTransitive SELECT a.implementsInterfaces, b.implementingClasses FROM JClass_ImplementsInterfacesTransitive AS a, JClass_ImplementsInterfacesTransitive AS b
WHERE @cntnew != @cntold AND a.implementingClasses = b.implementsInterfaces;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ImplementsInterfacesTransitive;
--  ============== Populate JClass_ImplementsInterfacesTransitive: iteration 30
REPLACE into JClass_ImplementsInterfacesTransitive SELECT a.implementsInterfaces, b.implementingClasses FROM JClass_ImplementsInterfacesTransitive AS a, JClass_ImplementsInterfacesTransitive AS b
WHERE @cntnew != @cntold AND a.implementingClasses = b.implementsInterfaces;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ImplementsInterfacesTransitive;
--  ============== Populate JClass_ImplementsInterfacesTransitive: iteration 31
REPLACE into JClass_ImplementsInterfacesTransitive SELECT a.implementsInterfaces, b.implementingClasses FROM JClass_ImplementsInterfacesTransitive AS a, JClass_ImplementsInterfacesTransitive AS b
WHERE @cntnew != @cntold AND a.implementingClasses = b.implementsInterfaces;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ImplementsInterfacesTransitive;
--  ============== Populate JClass_ImplementsInterfacesTransitive: iteration 32
REPLACE into JClass_ImplementsInterfacesTransitive SELECT a.implementsInterfaces, b.implementingClasses FROM JClass_ImplementsInterfacesTransitive AS a, JClass_ImplementsInterfacesTransitive AS b
WHERE @cntnew != @cntold AND a.implementingClasses = b.implementsInterfaces;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ImplementsInterfacesTransitive;
--  ============== Populate JClass_ImplementsInterfacesTransitive: iteration 33
REPLACE into JClass_ImplementsInterfacesTransitive SELECT a.implementsInterfaces, b.implementingClasses FROM JClass_ImplementsInterfacesTransitive AS a, JClass_ImplementsInterfacesTransitive AS b
WHERE @cntnew != @cntold AND a.implementingClasses = b.implementsInterfaces;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ImplementsInterfacesTransitive;
--  ============== Populate JClass_ImplementsInterfacesTransitive: iteration 34
REPLACE into JClass_ImplementsInterfacesTransitive SELECT a.implementsInterfaces, b.implementingClasses FROM JClass_ImplementsInterfacesTransitive AS a, JClass_ImplementsInterfacesTransitive AS b
WHERE @cntnew != @cntold AND a.implementingClasses = b.implementsInterfaces;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ImplementsInterfacesTransitive;
--  ============== Populate JClass_ImplementsInterfacesTransitive: iteration 35
REPLACE into JClass_ImplementsInterfacesTransitive SELECT a.implementsInterfaces, b.implementingClasses FROM JClass_ImplementsInterfacesTransitive AS a, JClass_ImplementsInterfacesTransitive AS b
WHERE @cntnew != @cntold AND a.implementingClasses = b.implementsInterfaces;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ImplementsInterfacesTransitive;
--  ============== Populate JClass_ImplementsInterfacesTransitive: iteration 36
REPLACE into JClass_ImplementsInterfacesTransitive SELECT a.implementsInterfaces, b.implementingClasses FROM JClass_ImplementsInterfacesTransitive AS a, JClass_ImplementsInterfacesTransitive AS b
WHERE @cntnew != @cntold AND a.implementingClasses = b.implementsInterfaces;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ImplementsInterfacesTransitive;
--  ============== Populate JClass_ImplementsInterfacesTransitive: iteration 37
REPLACE into JClass_ImplementsInterfacesTransitive SELECT a.implementsInterfaces, b.implementingClasses FROM JClass_ImplementsInterfacesTransitive AS a, JClass_ImplementsInterfacesTransitive AS b
WHERE @cntnew != @cntold AND a.implementingClasses = b.implementsInterfaces;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ImplementsInterfacesTransitive;
--  ============== Populate JClass_ImplementsInterfacesTransitive: iteration 38
REPLACE into JClass_ImplementsInterfacesTransitive SELECT a.implementsInterfaces, b.implementingClasses FROM JClass_ImplementsInterfacesTransitive AS a, JClass_ImplementsInterfacesTransitive AS b
WHERE @cntnew != @cntold AND a.implementingClasses = b.implementsInterfaces;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ImplementsInterfacesTransitive;
--  ============== Populate JClass_ImplementsInterfacesTransitive: iteration 39
REPLACE into JClass_ImplementsInterfacesTransitive SELECT a.implementsInterfaces, b.implementingClasses FROM JClass_ImplementsInterfacesTransitive AS a, JClass_ImplementsInterfacesTransitive AS b
WHERE @cntnew != @cntold AND a.implementingClasses = b.implementsInterfaces;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ImplementsInterfacesTransitive;
--  ============== Populate JClass_ImplementsInterfacesTransitive: iteration 40
REPLACE into JClass_ImplementsInterfacesTransitive SELECT a.implementsInterfaces, b.implementingClasses FROM JClass_ImplementsInterfacesTransitive AS a, JClass_ImplementsInterfacesTransitive AS b
WHERE @cntnew != @cntold AND a.implementingClasses = b.implementsInterfaces;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ImplementsInterfacesTransitive;
--  ============== Populate JClass_ImplementsInterfacesTransitive: iteration 41
REPLACE into JClass_ImplementsInterfacesTransitive SELECT a.implementsInterfaces, b.implementingClasses FROM JClass_ImplementsInterfacesTransitive AS a, JClass_ImplementsInterfacesTransitive AS b
WHERE @cntnew != @cntold AND a.implementingClasses = b.implementsInterfaces;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ImplementsInterfacesTransitive;
--  ============== Populate JClass_ImplementsInterfacesTransitive: iteration 42
REPLACE into JClass_ImplementsInterfacesTransitive SELECT a.implementsInterfaces, b.implementingClasses FROM JClass_ImplementsInterfacesTransitive AS a, JClass_ImplementsInterfacesTransitive AS b
WHERE @cntnew != @cntold AND a.implementingClasses = b.implementsInterfaces;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ImplementsInterfacesTransitive;
--  ============== Populate JClass_ImplementsInterfacesTransitive: iteration 43
REPLACE into JClass_ImplementsInterfacesTransitive SELECT a.implementsInterfaces, b.implementingClasses FROM JClass_ImplementsInterfacesTransitive AS a, JClass_ImplementsInterfacesTransitive AS b
WHERE @cntnew != @cntold AND a.implementingClasses = b.implementsInterfaces;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ImplementsInterfacesTransitive;
--  ============== Populate JClass_ImplementsInterfacesTransitive: iteration 44
REPLACE into JClass_ImplementsInterfacesTransitive SELECT a.implementsInterfaces, b.implementingClasses FROM JClass_ImplementsInterfacesTransitive AS a, JClass_ImplementsInterfacesTransitive AS b
WHERE @cntnew != @cntold AND a.implementingClasses = b.implementsInterfaces;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ImplementsInterfacesTransitive;
--  ============== Populate JClass_ImplementsInterfacesTransitive: iteration 45
REPLACE into JClass_ImplementsInterfacesTransitive SELECT a.implementsInterfaces, b.implementingClasses FROM JClass_ImplementsInterfacesTransitive AS a, JClass_ImplementsInterfacesTransitive AS b
WHERE @cntnew != @cntold AND a.implementingClasses = b.implementsInterfaces;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ImplementsInterfacesTransitive;
--  ============== Populate JClass_ImplementsInterfacesTransitive: iteration 46
REPLACE into JClass_ImplementsInterfacesTransitive SELECT a.implementsInterfaces, b.implementingClasses FROM JClass_ImplementsInterfacesTransitive AS a, JClass_ImplementsInterfacesTransitive AS b
WHERE @cntnew != @cntold AND a.implementingClasses = b.implementsInterfaces;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ImplementsInterfacesTransitive;
--  ============== Populate JClass_ImplementsInterfacesTransitive: iteration 47
REPLACE into JClass_ImplementsInterfacesTransitive SELECT a.implementsInterfaces, b.implementingClasses FROM JClass_ImplementsInterfacesTransitive AS a, JClass_ImplementsInterfacesTransitive AS b
WHERE @cntnew != @cntold AND a.implementingClasses = b.implementsInterfaces;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ImplementsInterfacesTransitive;
--  ============== Populate JClass_ImplementsInterfacesTransitive: iteration 48
REPLACE into JClass_ImplementsInterfacesTransitive SELECT a.implementsInterfaces, b.implementingClasses FROM JClass_ImplementsInterfacesTransitive AS a, JClass_ImplementsInterfacesTransitive AS b
WHERE @cntnew != @cntold AND a.implementingClasses = b.implementsInterfaces;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ImplementsInterfacesTransitive;
--  ============== Populate JClass_ImplementsInterfacesTransitive: iteration 49
REPLACE into JClass_ImplementsInterfacesTransitive SELECT a.implementsInterfaces, b.implementingClasses FROM JClass_ImplementsInterfacesTransitive AS a, JClass_ImplementsInterfacesTransitive AS b
WHERE @cntnew != @cntold AND a.implementingClasses = b.implementsInterfaces;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ImplementsInterfacesTransitive;
--  ============== Populate JClass_ImplementsInterfacesTransitive: iteration 50
REPLACE into JClass_ImplementsInterfacesTransitive SELECT a.implementsInterfaces, b.implementingClasses FROM JClass_ImplementsInterfacesTransitive AS a, JClass_ImplementsInterfacesTransitive AS b
WHERE @cntnew != @cntold AND a.implementingClasses = b.implementsInterfaces;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ImplementsInterfacesTransitive;
--  ============== Populate JClass_ImplementsInterfacesTransitive: End
--  ============== Populate JClass_ExtendsClassTransitive: Start
SELECT @cntold := 0;
SELECT @cntnew := COUNT(*) FROM JClass_ExtendsClassTransitive;
--  ============== Populate JClass_ExtendsClassTransitive: iteration 1
REPLACE into JClass_ExtendsClassTransitive SELECT a.extendsClass, b.extendingClasses FROM JClass_ExtendsClassTransitive AS a, JClass_ExtendsClassTransitive AS b
WHERE @cntnew != @cntold AND a.extendingClasses = b.extendsClass;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ExtendsClassTransitive;
--  ============== Populate JClass_ExtendsClassTransitive: iteration 2
REPLACE into JClass_ExtendsClassTransitive SELECT a.extendsClass, b.extendingClasses FROM JClass_ExtendsClassTransitive AS a, JClass_ExtendsClassTransitive AS b
WHERE @cntnew != @cntold AND a.extendingClasses = b.extendsClass;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ExtendsClassTransitive;
--  ============== Populate JClass_ExtendsClassTransitive: iteration 3
REPLACE into JClass_ExtendsClassTransitive SELECT a.extendsClass, b.extendingClasses FROM JClass_ExtendsClassTransitive AS a, JClass_ExtendsClassTransitive AS b
WHERE @cntnew != @cntold AND a.extendingClasses = b.extendsClass;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ExtendsClassTransitive;
--  ============== Populate JClass_ExtendsClassTransitive: iteration 4
REPLACE into JClass_ExtendsClassTransitive SELECT a.extendsClass, b.extendingClasses FROM JClass_ExtendsClassTransitive AS a, JClass_ExtendsClassTransitive AS b
WHERE @cntnew != @cntold AND a.extendingClasses = b.extendsClass;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ExtendsClassTransitive;
--  ============== Populate JClass_ExtendsClassTransitive: iteration 5
REPLACE into JClass_ExtendsClassTransitive SELECT a.extendsClass, b.extendingClasses FROM JClass_ExtendsClassTransitive AS a, JClass_ExtendsClassTransitive AS b
WHERE @cntnew != @cntold AND a.extendingClasses = b.extendsClass;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ExtendsClassTransitive;
--  ============== Populate JClass_ExtendsClassTransitive: iteration 6
REPLACE into JClass_ExtendsClassTransitive SELECT a.extendsClass, b.extendingClasses FROM JClass_ExtendsClassTransitive AS a, JClass_ExtendsClassTransitive AS b
WHERE @cntnew != @cntold AND a.extendingClasses = b.extendsClass;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ExtendsClassTransitive;
--  ============== Populate JClass_ExtendsClassTransitive: iteration 7
REPLACE into JClass_ExtendsClassTransitive SELECT a.extendsClass, b.extendingClasses FROM JClass_ExtendsClassTransitive AS a, JClass_ExtendsClassTransitive AS b
WHERE @cntnew != @cntold AND a.extendingClasses = b.extendsClass;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ExtendsClassTransitive;
--  ============== Populate JClass_ExtendsClassTransitive: iteration 8
REPLACE into JClass_ExtendsClassTransitive SELECT a.extendsClass, b.extendingClasses FROM JClass_ExtendsClassTransitive AS a, JClass_ExtendsClassTransitive AS b
WHERE @cntnew != @cntold AND a.extendingClasses = b.extendsClass;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ExtendsClassTransitive;
--  ============== Populate JClass_ExtendsClassTransitive: iteration 9
REPLACE into JClass_ExtendsClassTransitive SELECT a.extendsClass, b.extendingClasses FROM JClass_ExtendsClassTransitive AS a, JClass_ExtendsClassTransitive AS b
WHERE @cntnew != @cntold AND a.extendingClasses = b.extendsClass;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ExtendsClassTransitive;
--  ============== Populate JClass_ExtendsClassTransitive: iteration 10
REPLACE into JClass_ExtendsClassTransitive SELECT a.extendsClass, b.extendingClasses FROM JClass_ExtendsClassTransitive AS a, JClass_ExtendsClassTransitive AS b
WHERE @cntnew != @cntold AND a.extendingClasses = b.extendsClass;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ExtendsClassTransitive;
--  ============== Populate JClass_ExtendsClassTransitive: iteration 11
REPLACE into JClass_ExtendsClassTransitive SELECT a.extendsClass, b.extendingClasses FROM JClass_ExtendsClassTransitive AS a, JClass_ExtendsClassTransitive AS b
WHERE @cntnew != @cntold AND a.extendingClasses = b.extendsClass;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ExtendsClassTransitive;
--  ============== Populate JClass_ExtendsClassTransitive: iteration 12
REPLACE into JClass_ExtendsClassTransitive SELECT a.extendsClass, b.extendingClasses FROM JClass_ExtendsClassTransitive AS a, JClass_ExtendsClassTransitive AS b
WHERE @cntnew != @cntold AND a.extendingClasses = b.extendsClass;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ExtendsClassTransitive;
--  ============== Populate JClass_ExtendsClassTransitive: iteration 13
REPLACE into JClass_ExtendsClassTransitive SELECT a.extendsClass, b.extendingClasses FROM JClass_ExtendsClassTransitive AS a, JClass_ExtendsClassTransitive AS b
WHERE @cntnew != @cntold AND a.extendingClasses = b.extendsClass;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ExtendsClassTransitive;
--  ============== Populate JClass_ExtendsClassTransitive: iteration 14
REPLACE into JClass_ExtendsClassTransitive SELECT a.extendsClass, b.extendingClasses FROM JClass_ExtendsClassTransitive AS a, JClass_ExtendsClassTransitive AS b
WHERE @cntnew != @cntold AND a.extendingClasses = b.extendsClass;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ExtendsClassTransitive;
--  ============== Populate JClass_ExtendsClassTransitive: iteration 15
REPLACE into JClass_ExtendsClassTransitive SELECT a.extendsClass, b.extendingClasses FROM JClass_ExtendsClassTransitive AS a, JClass_ExtendsClassTransitive AS b
WHERE @cntnew != @cntold AND a.extendingClasses = b.extendsClass;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ExtendsClassTransitive;
--  ============== Populate JClass_ExtendsClassTransitive: iteration 16
REPLACE into JClass_ExtendsClassTransitive SELECT a.extendsClass, b.extendingClasses FROM JClass_ExtendsClassTransitive AS a, JClass_ExtendsClassTransitive AS b
WHERE @cntnew != @cntold AND a.extendingClasses = b.extendsClass;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ExtendsClassTransitive;
--  ============== Populate JClass_ExtendsClassTransitive: iteration 17
REPLACE into JClass_ExtendsClassTransitive SELECT a.extendsClass, b.extendingClasses FROM JClass_ExtendsClassTransitive AS a, JClass_ExtendsClassTransitive AS b
WHERE @cntnew != @cntold AND a.extendingClasses = b.extendsClass;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ExtendsClassTransitive;
--  ============== Populate JClass_ExtendsClassTransitive: iteration 18
REPLACE into JClass_ExtendsClassTransitive SELECT a.extendsClass, b.extendingClasses FROM JClass_ExtendsClassTransitive AS a, JClass_ExtendsClassTransitive AS b
WHERE @cntnew != @cntold AND a.extendingClasses = b.extendsClass;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ExtendsClassTransitive;
--  ============== Populate JClass_ExtendsClassTransitive: iteration 19
REPLACE into JClass_ExtendsClassTransitive SELECT a.extendsClass, b.extendingClasses FROM JClass_ExtendsClassTransitive AS a, JClass_ExtendsClassTransitive AS b
WHERE @cntnew != @cntold AND a.extendingClasses = b.extendsClass;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ExtendsClassTransitive;
--  ============== Populate JClass_ExtendsClassTransitive: iteration 20
REPLACE into JClass_ExtendsClassTransitive SELECT a.extendsClass, b.extendingClasses FROM JClass_ExtendsClassTransitive AS a, JClass_ExtendsClassTransitive AS b
WHERE @cntnew != @cntold AND a.extendingClasses = b.extendsClass;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ExtendsClassTransitive;
--  ============== Populate JClass_ExtendsClassTransitive: iteration 21
REPLACE into JClass_ExtendsClassTransitive SELECT a.extendsClass, b.extendingClasses FROM JClass_ExtendsClassTransitive AS a, JClass_ExtendsClassTransitive AS b
WHERE @cntnew != @cntold AND a.extendingClasses = b.extendsClass;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ExtendsClassTransitive;
--  ============== Populate JClass_ExtendsClassTransitive: iteration 22
REPLACE into JClass_ExtendsClassTransitive SELECT a.extendsClass, b.extendingClasses FROM JClass_ExtendsClassTransitive AS a, JClass_ExtendsClassTransitive AS b
WHERE @cntnew != @cntold AND a.extendingClasses = b.extendsClass;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ExtendsClassTransitive;
--  ============== Populate JClass_ExtendsClassTransitive: iteration 23
REPLACE into JClass_ExtendsClassTransitive SELECT a.extendsClass, b.extendingClasses FROM JClass_ExtendsClassTransitive AS a, JClass_ExtendsClassTransitive AS b
WHERE @cntnew != @cntold AND a.extendingClasses = b.extendsClass;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ExtendsClassTransitive;
--  ============== Populate JClass_ExtendsClassTransitive: iteration 24
REPLACE into JClass_ExtendsClassTransitive SELECT a.extendsClass, b.extendingClasses FROM JClass_ExtendsClassTransitive AS a, JClass_ExtendsClassTransitive AS b
WHERE @cntnew != @cntold AND a.extendingClasses = b.extendsClass;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ExtendsClassTransitive;
--  ============== Populate JClass_ExtendsClassTransitive: iteration 25
REPLACE into JClass_ExtendsClassTransitive SELECT a.extendsClass, b.extendingClasses FROM JClass_ExtendsClassTransitive AS a, JClass_ExtendsClassTransitive AS b
WHERE @cntnew != @cntold AND a.extendingClasses = b.extendsClass;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ExtendsClassTransitive;
--  ============== Populate JClass_ExtendsClassTransitive: iteration 26
REPLACE into JClass_ExtendsClassTransitive SELECT a.extendsClass, b.extendingClasses FROM JClass_ExtendsClassTransitive AS a, JClass_ExtendsClassTransitive AS b
WHERE @cntnew != @cntold AND a.extendingClasses = b.extendsClass;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ExtendsClassTransitive;
--  ============== Populate JClass_ExtendsClassTransitive: iteration 27
REPLACE into JClass_ExtendsClassTransitive SELECT a.extendsClass, b.extendingClasses FROM JClass_ExtendsClassTransitive AS a, JClass_ExtendsClassTransitive AS b
WHERE @cntnew != @cntold AND a.extendingClasses = b.extendsClass;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ExtendsClassTransitive;
--  ============== Populate JClass_ExtendsClassTransitive: iteration 28
REPLACE into JClass_ExtendsClassTransitive SELECT a.extendsClass, b.extendingClasses FROM JClass_ExtendsClassTransitive AS a, JClass_ExtendsClassTransitive AS b
WHERE @cntnew != @cntold AND a.extendingClasses = b.extendsClass;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ExtendsClassTransitive;
--  ============== Populate JClass_ExtendsClassTransitive: iteration 29
REPLACE into JClass_ExtendsClassTransitive SELECT a.extendsClass, b.extendingClasses FROM JClass_ExtendsClassTransitive AS a, JClass_ExtendsClassTransitive AS b
WHERE @cntnew != @cntold AND a.extendingClasses = b.extendsClass;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ExtendsClassTransitive;
--  ============== Populate JClass_ExtendsClassTransitive: iteration 30
REPLACE into JClass_ExtendsClassTransitive SELECT a.extendsClass, b.extendingClasses FROM JClass_ExtendsClassTransitive AS a, JClass_ExtendsClassTransitive AS b
WHERE @cntnew != @cntold AND a.extendingClasses = b.extendsClass;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ExtendsClassTransitive;
--  ============== Populate JClass_ExtendsClassTransitive: iteration 31
REPLACE into JClass_ExtendsClassTransitive SELECT a.extendsClass, b.extendingClasses FROM JClass_ExtendsClassTransitive AS a, JClass_ExtendsClassTransitive AS b
WHERE @cntnew != @cntold AND a.extendingClasses = b.extendsClass;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ExtendsClassTransitive;
--  ============== Populate JClass_ExtendsClassTransitive: iteration 32
REPLACE into JClass_ExtendsClassTransitive SELECT a.extendsClass, b.extendingClasses FROM JClass_ExtendsClassTransitive AS a, JClass_ExtendsClassTransitive AS b
WHERE @cntnew != @cntold AND a.extendingClasses = b.extendsClass;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ExtendsClassTransitive;
--  ============== Populate JClass_ExtendsClassTransitive: iteration 33
REPLACE into JClass_ExtendsClassTransitive SELECT a.extendsClass, b.extendingClasses FROM JClass_ExtendsClassTransitive AS a, JClass_ExtendsClassTransitive AS b
WHERE @cntnew != @cntold AND a.extendingClasses = b.extendsClass;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ExtendsClassTransitive;
--  ============== Populate JClass_ExtendsClassTransitive: iteration 34
REPLACE into JClass_ExtendsClassTransitive SELECT a.extendsClass, b.extendingClasses FROM JClass_ExtendsClassTransitive AS a, JClass_ExtendsClassTransitive AS b
WHERE @cntnew != @cntold AND a.extendingClasses = b.extendsClass;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ExtendsClassTransitive;
--  ============== Populate JClass_ExtendsClassTransitive: iteration 35
REPLACE into JClass_ExtendsClassTransitive SELECT a.extendsClass, b.extendingClasses FROM JClass_ExtendsClassTransitive AS a, JClass_ExtendsClassTransitive AS b
WHERE @cntnew != @cntold AND a.extendingClasses = b.extendsClass;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ExtendsClassTransitive;
--  ============== Populate JClass_ExtendsClassTransitive: iteration 36
REPLACE into JClass_ExtendsClassTransitive SELECT a.extendsClass, b.extendingClasses FROM JClass_ExtendsClassTransitive AS a, JClass_ExtendsClassTransitive AS b
WHERE @cntnew != @cntold AND a.extendingClasses = b.extendsClass;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ExtendsClassTransitive;
--  ============== Populate JClass_ExtendsClassTransitive: iteration 37
REPLACE into JClass_ExtendsClassTransitive SELECT a.extendsClass, b.extendingClasses FROM JClass_ExtendsClassTransitive AS a, JClass_ExtendsClassTransitive AS b
WHERE @cntnew != @cntold AND a.extendingClasses = b.extendsClass;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ExtendsClassTransitive;
--  ============== Populate JClass_ExtendsClassTransitive: iteration 38
REPLACE into JClass_ExtendsClassTransitive SELECT a.extendsClass, b.extendingClasses FROM JClass_ExtendsClassTransitive AS a, JClass_ExtendsClassTransitive AS b
WHERE @cntnew != @cntold AND a.extendingClasses = b.extendsClass;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ExtendsClassTransitive;
--  ============== Populate JClass_ExtendsClassTransitive: iteration 39
REPLACE into JClass_ExtendsClassTransitive SELECT a.extendsClass, b.extendingClasses FROM JClass_ExtendsClassTransitive AS a, JClass_ExtendsClassTransitive AS b
WHERE @cntnew != @cntold AND a.extendingClasses = b.extendsClass;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ExtendsClassTransitive;
--  ============== Populate JClass_ExtendsClassTransitive: iteration 40
REPLACE into JClass_ExtendsClassTransitive SELECT a.extendsClass, b.extendingClasses FROM JClass_ExtendsClassTransitive AS a, JClass_ExtendsClassTransitive AS b
WHERE @cntnew != @cntold AND a.extendingClasses = b.extendsClass;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ExtendsClassTransitive;
--  ============== Populate JClass_ExtendsClassTransitive: iteration 41
REPLACE into JClass_ExtendsClassTransitive SELECT a.extendsClass, b.extendingClasses FROM JClass_ExtendsClassTransitive AS a, JClass_ExtendsClassTransitive AS b
WHERE @cntnew != @cntold AND a.extendingClasses = b.extendsClass;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ExtendsClassTransitive;
--  ============== Populate JClass_ExtendsClassTransitive: iteration 42
REPLACE into JClass_ExtendsClassTransitive SELECT a.extendsClass, b.extendingClasses FROM JClass_ExtendsClassTransitive AS a, JClass_ExtendsClassTransitive AS b
WHERE @cntnew != @cntold AND a.extendingClasses = b.extendsClass;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ExtendsClassTransitive;
--  ============== Populate JClass_ExtendsClassTransitive: iteration 43
REPLACE into JClass_ExtendsClassTransitive SELECT a.extendsClass, b.extendingClasses FROM JClass_ExtendsClassTransitive AS a, JClass_ExtendsClassTransitive AS b
WHERE @cntnew != @cntold AND a.extendingClasses = b.extendsClass;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ExtendsClassTransitive;
--  ============== Populate JClass_ExtendsClassTransitive: iteration 44
REPLACE into JClass_ExtendsClassTransitive SELECT a.extendsClass, b.extendingClasses FROM JClass_ExtendsClassTransitive AS a, JClass_ExtendsClassTransitive AS b
WHERE @cntnew != @cntold AND a.extendingClasses = b.extendsClass;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ExtendsClassTransitive;
--  ============== Populate JClass_ExtendsClassTransitive: iteration 45
REPLACE into JClass_ExtendsClassTransitive SELECT a.extendsClass, b.extendingClasses FROM JClass_ExtendsClassTransitive AS a, JClass_ExtendsClassTransitive AS b
WHERE @cntnew != @cntold AND a.extendingClasses = b.extendsClass;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ExtendsClassTransitive;
--  ============== Populate JClass_ExtendsClassTransitive: iteration 46
REPLACE into JClass_ExtendsClassTransitive SELECT a.extendsClass, b.extendingClasses FROM JClass_ExtendsClassTransitive AS a, JClass_ExtendsClassTransitive AS b
WHERE @cntnew != @cntold AND a.extendingClasses = b.extendsClass;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ExtendsClassTransitive;
--  ============== Populate JClass_ExtendsClassTransitive: iteration 47
REPLACE into JClass_ExtendsClassTransitive SELECT a.extendsClass, b.extendingClasses FROM JClass_ExtendsClassTransitive AS a, JClass_ExtendsClassTransitive AS b
WHERE @cntnew != @cntold AND a.extendingClasses = b.extendsClass;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ExtendsClassTransitive;
--  ============== Populate JClass_ExtendsClassTransitive: iteration 48
REPLACE into JClass_ExtendsClassTransitive SELECT a.extendsClass, b.extendingClasses FROM JClass_ExtendsClassTransitive AS a, JClass_ExtendsClassTransitive AS b
WHERE @cntnew != @cntold AND a.extendingClasses = b.extendsClass;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ExtendsClassTransitive;
--  ============== Populate JClass_ExtendsClassTransitive: iteration 49
REPLACE into JClass_ExtendsClassTransitive SELECT a.extendsClass, b.extendingClasses FROM JClass_ExtendsClassTransitive AS a, JClass_ExtendsClassTransitive AS b
WHERE @cntnew != @cntold AND a.extendingClasses = b.extendsClass;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ExtendsClassTransitive;
--  ============== Populate JClass_ExtendsClassTransitive: iteration 50
REPLACE into JClass_ExtendsClassTransitive SELECT a.extendsClass, b.extendingClasses FROM JClass_ExtendsClassTransitive AS a, JClass_ExtendsClassTransitive AS b
WHERE @cntnew != @cntold AND a.extendingClasses = b.extendsClass;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JClass_ExtendsClassTransitive;
--  ============== Populate JClass_ExtendsClassTransitive: End
--  ============== Populate JPackage_PackagesTransitive: Start
SELECT @cntold := 0;
SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;
--  ============== Populate JPackage_PackagesTransitive: iteration 1
REPLACE into JPackage_PackagesTransitive SELECT a.packages, b.parentPackage, a.salsaProjectId FROM JPackage_PackagesTransitive AS a, JPackage_PackagesTransitive AS b
WHERE @cntnew != @cntold AND a.parentPackage = b.packages;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;
--  ============== Populate JPackage_PackagesTransitive: iteration 2
REPLACE into JPackage_PackagesTransitive SELECT a.packages, b.parentPackage, a.salsaProjectId FROM JPackage_PackagesTransitive AS a, JPackage_PackagesTransitive AS b
WHERE @cntnew != @cntold AND a.parentPackage = b.packages;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;
--  ============== Populate JPackage_PackagesTransitive: iteration 3
REPLACE into JPackage_PackagesTransitive SELECT a.packages, b.parentPackage, a.salsaProjectId FROM JPackage_PackagesTransitive AS a, JPackage_PackagesTransitive AS b
WHERE @cntnew != @cntold AND a.parentPackage = b.packages;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;
--  ============== Populate JPackage_PackagesTransitive: iteration 4
REPLACE into JPackage_PackagesTransitive SELECT a.packages, b.parentPackage, a.salsaProjectId FROM JPackage_PackagesTransitive AS a, JPackage_PackagesTransitive AS b
WHERE @cntnew != @cntold AND a.parentPackage = b.packages;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;
--  ============== Populate JPackage_PackagesTransitive: iteration 5
REPLACE into JPackage_PackagesTransitive SELECT a.packages, b.parentPackage, a.salsaProjectId FROM JPackage_PackagesTransitive AS a, JPackage_PackagesTransitive AS b
WHERE @cntnew != @cntold AND a.parentPackage = b.packages;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;
--  ============== Populate JPackage_PackagesTransitive: iteration 6
REPLACE into JPackage_PackagesTransitive SELECT a.packages, b.parentPackage, a.salsaProjectId FROM JPackage_PackagesTransitive AS a, JPackage_PackagesTransitive AS b
WHERE @cntnew != @cntold AND a.parentPackage = b.packages;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;
--  ============== Populate JPackage_PackagesTransitive: iteration 7
REPLACE into JPackage_PackagesTransitive SELECT a.packages, b.parentPackage, a.salsaProjectId FROM JPackage_PackagesTransitive AS a, JPackage_PackagesTransitive AS b
WHERE @cntnew != @cntold AND a.parentPackage = b.packages;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;
--  ============== Populate JPackage_PackagesTransitive: iteration 8
REPLACE into JPackage_PackagesTransitive SELECT a.packages, b.parentPackage, a.salsaProjectId FROM JPackage_PackagesTransitive AS a, JPackage_PackagesTransitive AS b
WHERE @cntnew != @cntold AND a.parentPackage = b.packages;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;
--  ============== Populate JPackage_PackagesTransitive: iteration 9
REPLACE into JPackage_PackagesTransitive SELECT a.packages, b.parentPackage, a.salsaProjectId FROM JPackage_PackagesTransitive AS a, JPackage_PackagesTransitive AS b
WHERE @cntnew != @cntold AND a.parentPackage = b.packages;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;
--  ============== Populate JPackage_PackagesTransitive: iteration 10
REPLACE into JPackage_PackagesTransitive SELECT a.packages, b.parentPackage, a.salsaProjectId FROM JPackage_PackagesTransitive AS a, JPackage_PackagesTransitive AS b
WHERE @cntnew != @cntold AND a.parentPackage = b.packages;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;
--  ============== Populate JPackage_PackagesTransitive: iteration 11
REPLACE into JPackage_PackagesTransitive SELECT a.packages, b.parentPackage, a.salsaProjectId FROM JPackage_PackagesTransitive AS a, JPackage_PackagesTransitive AS b
WHERE @cntnew != @cntold AND a.parentPackage = b.packages;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;
--  ============== Populate JPackage_PackagesTransitive: iteration 12
REPLACE into JPackage_PackagesTransitive SELECT a.packages, b.parentPackage, a.salsaProjectId FROM JPackage_PackagesTransitive AS a, JPackage_PackagesTransitive AS b
WHERE @cntnew != @cntold AND a.parentPackage = b.packages;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;
--  ============== Populate JPackage_PackagesTransitive: iteration 13
REPLACE into JPackage_PackagesTransitive SELECT a.packages, b.parentPackage, a.salsaProjectId FROM JPackage_PackagesTransitive AS a, JPackage_PackagesTransitive AS b
WHERE @cntnew != @cntold AND a.parentPackage = b.packages;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;
--  ============== Populate JPackage_PackagesTransitive: iteration 14
REPLACE into JPackage_PackagesTransitive SELECT a.packages, b.parentPackage, a.salsaProjectId FROM JPackage_PackagesTransitive AS a, JPackage_PackagesTransitive AS b
WHERE @cntnew != @cntold AND a.parentPackage = b.packages;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;
--  ============== Populate JPackage_PackagesTransitive: iteration 15
REPLACE into JPackage_PackagesTransitive SELECT a.packages, b.parentPackage, a.salsaProjectId FROM JPackage_PackagesTransitive AS a, JPackage_PackagesTransitive AS b
WHERE @cntnew != @cntold AND a.parentPackage = b.packages;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;
--  ============== Populate JPackage_PackagesTransitive: iteration 16
REPLACE into JPackage_PackagesTransitive SELECT a.packages, b.parentPackage, a.salsaProjectId FROM JPackage_PackagesTransitive AS a, JPackage_PackagesTransitive AS b
WHERE @cntnew != @cntold AND a.parentPackage = b.packages;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;
--  ============== Populate JPackage_PackagesTransitive: iteration 17
REPLACE into JPackage_PackagesTransitive SELECT a.packages, b.parentPackage, a.salsaProjectId FROM JPackage_PackagesTransitive AS a, JPackage_PackagesTransitive AS b
WHERE @cntnew != @cntold AND a.parentPackage = b.packages;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;
--  ============== Populate JPackage_PackagesTransitive: iteration 18
REPLACE into JPackage_PackagesTransitive SELECT a.packages, b.parentPackage, a.salsaProjectId FROM JPackage_PackagesTransitive AS a, JPackage_PackagesTransitive AS b
WHERE @cntnew != @cntold AND a.parentPackage = b.packages;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;
--  ============== Populate JPackage_PackagesTransitive: iteration 19
REPLACE into JPackage_PackagesTransitive SELECT a.packages, b.parentPackage, a.salsaProjectId FROM JPackage_PackagesTransitive AS a, JPackage_PackagesTransitive AS b
WHERE @cntnew != @cntold AND a.parentPackage = b.packages;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;
--  ============== Populate JPackage_PackagesTransitive: iteration 20
REPLACE into JPackage_PackagesTransitive SELECT a.packages, b.parentPackage, a.salsaProjectId FROM JPackage_PackagesTransitive AS a, JPackage_PackagesTransitive AS b
WHERE @cntnew != @cntold AND a.parentPackage = b.packages;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;
--  ============== Populate JPackage_PackagesTransitive: iteration 21
REPLACE into JPackage_PackagesTransitive SELECT a.packages, b.parentPackage, a.salsaProjectId FROM JPackage_PackagesTransitive AS a, JPackage_PackagesTransitive AS b
WHERE @cntnew != @cntold AND a.parentPackage = b.packages;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;
--  ============== Populate JPackage_PackagesTransitive: iteration 22
REPLACE into JPackage_PackagesTransitive SELECT a.packages, b.parentPackage, a.salsaProjectId FROM JPackage_PackagesTransitive AS a, JPackage_PackagesTransitive AS b
WHERE @cntnew != @cntold AND a.parentPackage = b.packages;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;
--  ============== Populate JPackage_PackagesTransitive: iteration 23
REPLACE into JPackage_PackagesTransitive SELECT a.packages, b.parentPackage, a.salsaProjectId FROM JPackage_PackagesTransitive AS a, JPackage_PackagesTransitive AS b
WHERE @cntnew != @cntold AND a.parentPackage = b.packages;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;
--  ============== Populate JPackage_PackagesTransitive: iteration 24
REPLACE into JPackage_PackagesTransitive SELECT a.packages, b.parentPackage, a.salsaProjectId FROM JPackage_PackagesTransitive AS a, JPackage_PackagesTransitive AS b
WHERE @cntnew != @cntold AND a.parentPackage = b.packages;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;
--  ============== Populate JPackage_PackagesTransitive: iteration 25
REPLACE into JPackage_PackagesTransitive SELECT a.packages, b.parentPackage, a.salsaProjectId FROM JPackage_PackagesTransitive AS a, JPackage_PackagesTransitive AS b
WHERE @cntnew != @cntold AND a.parentPackage = b.packages;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;
--  ============== Populate JPackage_PackagesTransitive: iteration 26
REPLACE into JPackage_PackagesTransitive SELECT a.packages, b.parentPackage, a.salsaProjectId FROM JPackage_PackagesTransitive AS a, JPackage_PackagesTransitive AS b
WHERE @cntnew != @cntold AND a.parentPackage = b.packages;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;
--  ============== Populate JPackage_PackagesTransitive: iteration 27
REPLACE into JPackage_PackagesTransitive SELECT a.packages, b.parentPackage, a.salsaProjectId FROM JPackage_PackagesTransitive AS a, JPackage_PackagesTransitive AS b
WHERE @cntnew != @cntold AND a.parentPackage = b.packages;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;
--  ============== Populate JPackage_PackagesTransitive: iteration 28
REPLACE into JPackage_PackagesTransitive SELECT a.packages, b.parentPackage, a.salsaProjectId FROM JPackage_PackagesTransitive AS a, JPackage_PackagesTransitive AS b
WHERE @cntnew != @cntold AND a.parentPackage = b.packages;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;
--  ============== Populate JPackage_PackagesTransitive: iteration 29
REPLACE into JPackage_PackagesTransitive SELECT a.packages, b.parentPackage, a.salsaProjectId FROM JPackage_PackagesTransitive AS a, JPackage_PackagesTransitive AS b
WHERE @cntnew != @cntold AND a.parentPackage = b.packages;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;
--  ============== Populate JPackage_PackagesTransitive: iteration 30
REPLACE into JPackage_PackagesTransitive SELECT a.packages, b.parentPackage, a.salsaProjectId FROM JPackage_PackagesTransitive AS a, JPackage_PackagesTransitive AS b
WHERE @cntnew != @cntold AND a.parentPackage = b.packages;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;
--  ============== Populate JPackage_PackagesTransitive: iteration 31
REPLACE into JPackage_PackagesTransitive SELECT a.packages, b.parentPackage, a.salsaProjectId FROM JPackage_PackagesTransitive AS a, JPackage_PackagesTransitive AS b
WHERE @cntnew != @cntold AND a.parentPackage = b.packages;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;
--  ============== Populate JPackage_PackagesTransitive: iteration 32
REPLACE into JPackage_PackagesTransitive SELECT a.packages, b.parentPackage, a.salsaProjectId FROM JPackage_PackagesTransitive AS a, JPackage_PackagesTransitive AS b
WHERE @cntnew != @cntold AND a.parentPackage = b.packages;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;
--  ============== Populate JPackage_PackagesTransitive: iteration 33
REPLACE into JPackage_PackagesTransitive SELECT a.packages, b.parentPackage, a.salsaProjectId FROM JPackage_PackagesTransitive AS a, JPackage_PackagesTransitive AS b
WHERE @cntnew != @cntold AND a.parentPackage = b.packages;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;
--  ============== Populate JPackage_PackagesTransitive: iteration 34
REPLACE into JPackage_PackagesTransitive SELECT a.packages, b.parentPackage, a.salsaProjectId FROM JPackage_PackagesTransitive AS a, JPackage_PackagesTransitive AS b
WHERE @cntnew != @cntold AND a.parentPackage = b.packages;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;
--  ============== Populate JPackage_PackagesTransitive: iteration 35
REPLACE into JPackage_PackagesTransitive SELECT a.packages, b.parentPackage, a.salsaProjectId FROM JPackage_PackagesTransitive AS a, JPackage_PackagesTransitive AS b
WHERE @cntnew != @cntold AND a.parentPackage = b.packages;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;
--  ============== Populate JPackage_PackagesTransitive: iteration 36
REPLACE into JPackage_PackagesTransitive SELECT a.packages, b.parentPackage, a.salsaProjectId FROM JPackage_PackagesTransitive AS a, JPackage_PackagesTransitive AS b
WHERE @cntnew != @cntold AND a.parentPackage = b.packages;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;
--  ============== Populate JPackage_PackagesTransitive: iteration 37
REPLACE into JPackage_PackagesTransitive SELECT a.packages, b.parentPackage, a.salsaProjectId FROM JPackage_PackagesTransitive AS a, JPackage_PackagesTransitive AS b
WHERE @cntnew != @cntold AND a.parentPackage = b.packages;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;
--  ============== Populate JPackage_PackagesTransitive: iteration 38
REPLACE into JPackage_PackagesTransitive SELECT a.packages, b.parentPackage, a.salsaProjectId FROM JPackage_PackagesTransitive AS a, JPackage_PackagesTransitive AS b
WHERE @cntnew != @cntold AND a.parentPackage = b.packages;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;
--  ============== Populate JPackage_PackagesTransitive: iteration 39
REPLACE into JPackage_PackagesTransitive SELECT a.packages, b.parentPackage, a.salsaProjectId FROM JPackage_PackagesTransitive AS a, JPackage_PackagesTransitive AS b
WHERE @cntnew != @cntold AND a.parentPackage = b.packages;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;
--  ============== Populate JPackage_PackagesTransitive: iteration 40
REPLACE into JPackage_PackagesTransitive SELECT a.packages, b.parentPackage, a.salsaProjectId FROM JPackage_PackagesTransitive AS a, JPackage_PackagesTransitive AS b
WHERE @cntnew != @cntold AND a.parentPackage = b.packages;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;
--  ============== Populate JPackage_PackagesTransitive: iteration 41
REPLACE into JPackage_PackagesTransitive SELECT a.packages, b.parentPackage, a.salsaProjectId FROM JPackage_PackagesTransitive AS a, JPackage_PackagesTransitive AS b
WHERE @cntnew != @cntold AND a.parentPackage = b.packages;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;
--  ============== Populate JPackage_PackagesTransitive: iteration 42
REPLACE into JPackage_PackagesTransitive SELECT a.packages, b.parentPackage, a.salsaProjectId FROM JPackage_PackagesTransitive AS a, JPackage_PackagesTransitive AS b
WHERE @cntnew != @cntold AND a.parentPackage = b.packages;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;
--  ============== Populate JPackage_PackagesTransitive: iteration 43
REPLACE into JPackage_PackagesTransitive SELECT a.packages, b.parentPackage, a.salsaProjectId FROM JPackage_PackagesTransitive AS a, JPackage_PackagesTransitive AS b
WHERE @cntnew != @cntold AND a.parentPackage = b.packages;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;
--  ============== Populate JPackage_PackagesTransitive: iteration 44
REPLACE into JPackage_PackagesTransitive SELECT a.packages, b.parentPackage, a.salsaProjectId FROM JPackage_PackagesTransitive AS a, JPackage_PackagesTransitive AS b
WHERE @cntnew != @cntold AND a.parentPackage = b.packages;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;
--  ============== Populate JPackage_PackagesTransitive: iteration 45
REPLACE into JPackage_PackagesTransitive SELECT a.packages, b.parentPackage, a.salsaProjectId FROM JPackage_PackagesTransitive AS a, JPackage_PackagesTransitive AS b
WHERE @cntnew != @cntold AND a.parentPackage = b.packages;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;
--  ============== Populate JPackage_PackagesTransitive: iteration 46
REPLACE into JPackage_PackagesTransitive SELECT a.packages, b.parentPackage, a.salsaProjectId FROM JPackage_PackagesTransitive AS a, JPackage_PackagesTransitive AS b
WHERE @cntnew != @cntold AND a.parentPackage = b.packages;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;
--  ============== Populate JPackage_PackagesTransitive: iteration 47
REPLACE into JPackage_PackagesTransitive SELECT a.packages, b.parentPackage, a.salsaProjectId FROM JPackage_PackagesTransitive AS a, JPackage_PackagesTransitive AS b
WHERE @cntnew != @cntold AND a.parentPackage = b.packages;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;
--  ============== Populate JPackage_PackagesTransitive: iteration 48
REPLACE into JPackage_PackagesTransitive SELECT a.packages, b.parentPackage, a.salsaProjectId FROM JPackage_PackagesTransitive AS a, JPackage_PackagesTransitive AS b
WHERE @cntnew != @cntold AND a.parentPackage = b.packages;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;
--  ============== Populate JPackage_PackagesTransitive: iteration 49
REPLACE into JPackage_PackagesTransitive SELECT a.packages, b.parentPackage, a.salsaProjectId FROM JPackage_PackagesTransitive AS a, JPackage_PackagesTransitive AS b
WHERE @cntnew != @cntold AND a.parentPackage = b.packages;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;
--  ============== Populate JPackage_PackagesTransitive: iteration 50
REPLACE into JPackage_PackagesTransitive SELECT a.packages, b.parentPackage, a.salsaProjectId FROM JPackage_PackagesTransitive AS a, JPackage_PackagesTransitive AS b
WHERE @cntnew != @cntold AND a.parentPackage = b.packages;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JPackage_PackagesTransitive;
--  ============== Populate JPackage_PackagesTransitive: End
--  ============== Populate JMethod_CallsTransitive: Start
SELECT @cntold := 0;
SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;
--  ============== Populate JMethod_CallsTransitive: iteration 1
REPLACE into JMethod_CallsTransitive SELECT a.calls, b.callers, a.filePosition FROM JMethod_CallsTransitive AS a, JMethod_CallsTransitive AS b
WHERE @cntnew != @cntold AND a.callers = b.calls;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;
--  ============== Populate JMethod_CallsTransitive: iteration 2
REPLACE into JMethod_CallsTransitive SELECT a.calls, b.callers, a.filePosition FROM JMethod_CallsTransitive AS a, JMethod_CallsTransitive AS b
WHERE @cntnew != @cntold AND a.callers = b.calls;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;
--  ============== Populate JMethod_CallsTransitive: iteration 3
REPLACE into JMethod_CallsTransitive SELECT a.calls, b.callers, a.filePosition FROM JMethod_CallsTransitive AS a, JMethod_CallsTransitive AS b
WHERE @cntnew != @cntold AND a.callers = b.calls;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;
--  ============== Populate JMethod_CallsTransitive: iteration 4
REPLACE into JMethod_CallsTransitive SELECT a.calls, b.callers, a.filePosition FROM JMethod_CallsTransitive AS a, JMethod_CallsTransitive AS b
WHERE @cntnew != @cntold AND a.callers = b.calls;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;
--  ============== Populate JMethod_CallsTransitive: iteration 5
REPLACE into JMethod_CallsTransitive SELECT a.calls, b.callers, a.filePosition FROM JMethod_CallsTransitive AS a, JMethod_CallsTransitive AS b
WHERE @cntnew != @cntold AND a.callers = b.calls;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;
--  ============== Populate JMethod_CallsTransitive: iteration 6
REPLACE into JMethod_CallsTransitive SELECT a.calls, b.callers, a.filePosition FROM JMethod_CallsTransitive AS a, JMethod_CallsTransitive AS b
WHERE @cntnew != @cntold AND a.callers = b.calls;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;
--  ============== Populate JMethod_CallsTransitive: iteration 7
REPLACE into JMethod_CallsTransitive SELECT a.calls, b.callers, a.filePosition FROM JMethod_CallsTransitive AS a, JMethod_CallsTransitive AS b
WHERE @cntnew != @cntold AND a.callers = b.calls;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;
--  ============== Populate JMethod_CallsTransitive: iteration 8
REPLACE into JMethod_CallsTransitive SELECT a.calls, b.callers, a.filePosition FROM JMethod_CallsTransitive AS a, JMethod_CallsTransitive AS b
WHERE @cntnew != @cntold AND a.callers = b.calls;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;
--  ============== Populate JMethod_CallsTransitive: iteration 9
REPLACE into JMethod_CallsTransitive SELECT a.calls, b.callers, a.filePosition FROM JMethod_CallsTransitive AS a, JMethod_CallsTransitive AS b
WHERE @cntnew != @cntold AND a.callers = b.calls;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;
--  ============== Populate JMethod_CallsTransitive: iteration 10
REPLACE into JMethod_CallsTransitive SELECT a.calls, b.callers, a.filePosition FROM JMethod_CallsTransitive AS a, JMethod_CallsTransitive AS b
WHERE @cntnew != @cntold AND a.callers = b.calls;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;
--  ============== Populate JMethod_CallsTransitive: iteration 11
REPLACE into JMethod_CallsTransitive SELECT a.calls, b.callers, a.filePosition FROM JMethod_CallsTransitive AS a, JMethod_CallsTransitive AS b
WHERE @cntnew != @cntold AND a.callers = b.calls;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;
--  ============== Populate JMethod_CallsTransitive: iteration 12
REPLACE into JMethod_CallsTransitive SELECT a.calls, b.callers, a.filePosition FROM JMethod_CallsTransitive AS a, JMethod_CallsTransitive AS b
WHERE @cntnew != @cntold AND a.callers = b.calls;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;
--  ============== Populate JMethod_CallsTransitive: iteration 13
REPLACE into JMethod_CallsTransitive SELECT a.calls, b.callers, a.filePosition FROM JMethod_CallsTransitive AS a, JMethod_CallsTransitive AS b
WHERE @cntnew != @cntold AND a.callers = b.calls;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;
--  ============== Populate JMethod_CallsTransitive: iteration 14
REPLACE into JMethod_CallsTransitive SELECT a.calls, b.callers, a.filePosition FROM JMethod_CallsTransitive AS a, JMethod_CallsTransitive AS b
WHERE @cntnew != @cntold AND a.callers = b.calls;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;
--  ============== Populate JMethod_CallsTransitive: iteration 15
REPLACE into JMethod_CallsTransitive SELECT a.calls, b.callers, a.filePosition FROM JMethod_CallsTransitive AS a, JMethod_CallsTransitive AS b
WHERE @cntnew != @cntold AND a.callers = b.calls;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;
--  ============== Populate JMethod_CallsTransitive: iteration 16
REPLACE into JMethod_CallsTransitive SELECT a.calls, b.callers, a.filePosition FROM JMethod_CallsTransitive AS a, JMethod_CallsTransitive AS b
WHERE @cntnew != @cntold AND a.callers = b.calls;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;
--  ============== Populate JMethod_CallsTransitive: iteration 17
REPLACE into JMethod_CallsTransitive SELECT a.calls, b.callers, a.filePosition FROM JMethod_CallsTransitive AS a, JMethod_CallsTransitive AS b
WHERE @cntnew != @cntold AND a.callers = b.calls;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;
--  ============== Populate JMethod_CallsTransitive: iteration 18
REPLACE into JMethod_CallsTransitive SELECT a.calls, b.callers, a.filePosition FROM JMethod_CallsTransitive AS a, JMethod_CallsTransitive AS b
WHERE @cntnew != @cntold AND a.callers = b.calls;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;
--  ============== Populate JMethod_CallsTransitive: iteration 19
REPLACE into JMethod_CallsTransitive SELECT a.calls, b.callers, a.filePosition FROM JMethod_CallsTransitive AS a, JMethod_CallsTransitive AS b
WHERE @cntnew != @cntold AND a.callers = b.calls;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;
--  ============== Populate JMethod_CallsTransitive: iteration 20
REPLACE into JMethod_CallsTransitive SELECT a.calls, b.callers, a.filePosition FROM JMethod_CallsTransitive AS a, JMethod_CallsTransitive AS b
WHERE @cntnew != @cntold AND a.callers = b.calls;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;
--  ============== Populate JMethod_CallsTransitive: iteration 21
REPLACE into JMethod_CallsTransitive SELECT a.calls, b.callers, a.filePosition FROM JMethod_CallsTransitive AS a, JMethod_CallsTransitive AS b
WHERE @cntnew != @cntold AND a.callers = b.calls;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;
--  ============== Populate JMethod_CallsTransitive: iteration 22
REPLACE into JMethod_CallsTransitive SELECT a.calls, b.callers, a.filePosition FROM JMethod_CallsTransitive AS a, JMethod_CallsTransitive AS b
WHERE @cntnew != @cntold AND a.callers = b.calls;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;
--  ============== Populate JMethod_CallsTransitive: iteration 23
REPLACE into JMethod_CallsTransitive SELECT a.calls, b.callers, a.filePosition FROM JMethod_CallsTransitive AS a, JMethod_CallsTransitive AS b
WHERE @cntnew != @cntold AND a.callers = b.calls;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;
--  ============== Populate JMethod_CallsTransitive: iteration 24
REPLACE into JMethod_CallsTransitive SELECT a.calls, b.callers, a.filePosition FROM JMethod_CallsTransitive AS a, JMethod_CallsTransitive AS b
WHERE @cntnew != @cntold AND a.callers = b.calls;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;
--  ============== Populate JMethod_CallsTransitive: iteration 25
REPLACE into JMethod_CallsTransitive SELECT a.calls, b.callers, a.filePosition FROM JMethod_CallsTransitive AS a, JMethod_CallsTransitive AS b
WHERE @cntnew != @cntold AND a.callers = b.calls;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;
--  ============== Populate JMethod_CallsTransitive: iteration 26
REPLACE into JMethod_CallsTransitive SELECT a.calls, b.callers, a.filePosition FROM JMethod_CallsTransitive AS a, JMethod_CallsTransitive AS b
WHERE @cntnew != @cntold AND a.callers = b.calls;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;
--  ============== Populate JMethod_CallsTransitive: iteration 27
REPLACE into JMethod_CallsTransitive SELECT a.calls, b.callers, a.filePosition FROM JMethod_CallsTransitive AS a, JMethod_CallsTransitive AS b
WHERE @cntnew != @cntold AND a.callers = b.calls;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;
--  ============== Populate JMethod_CallsTransitive: iteration 28
REPLACE into JMethod_CallsTransitive SELECT a.calls, b.callers, a.filePosition FROM JMethod_CallsTransitive AS a, JMethod_CallsTransitive AS b
WHERE @cntnew != @cntold AND a.callers = b.calls;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;
--  ============== Populate JMethod_CallsTransitive: iteration 29
REPLACE into JMethod_CallsTransitive SELECT a.calls, b.callers, a.filePosition FROM JMethod_CallsTransitive AS a, JMethod_CallsTransitive AS b
WHERE @cntnew != @cntold AND a.callers = b.calls;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;
--  ============== Populate JMethod_CallsTransitive: iteration 30
REPLACE into JMethod_CallsTransitive SELECT a.calls, b.callers, a.filePosition FROM JMethod_CallsTransitive AS a, JMethod_CallsTransitive AS b
WHERE @cntnew != @cntold AND a.callers = b.calls;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;
--  ============== Populate JMethod_CallsTransitive: iteration 31
REPLACE into JMethod_CallsTransitive SELECT a.calls, b.callers, a.filePosition FROM JMethod_CallsTransitive AS a, JMethod_CallsTransitive AS b
WHERE @cntnew != @cntold AND a.callers = b.calls;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;
--  ============== Populate JMethod_CallsTransitive: iteration 32
REPLACE into JMethod_CallsTransitive SELECT a.calls, b.callers, a.filePosition FROM JMethod_CallsTransitive AS a, JMethod_CallsTransitive AS b
WHERE @cntnew != @cntold AND a.callers = b.calls;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;
--  ============== Populate JMethod_CallsTransitive: iteration 33
REPLACE into JMethod_CallsTransitive SELECT a.calls, b.callers, a.filePosition FROM JMethod_CallsTransitive AS a, JMethod_CallsTransitive AS b
WHERE @cntnew != @cntold AND a.callers = b.calls;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;
--  ============== Populate JMethod_CallsTransitive: iteration 34
REPLACE into JMethod_CallsTransitive SELECT a.calls, b.callers, a.filePosition FROM JMethod_CallsTransitive AS a, JMethod_CallsTransitive AS b
WHERE @cntnew != @cntold AND a.callers = b.calls;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;
--  ============== Populate JMethod_CallsTransitive: iteration 35
REPLACE into JMethod_CallsTransitive SELECT a.calls, b.callers, a.filePosition FROM JMethod_CallsTransitive AS a, JMethod_CallsTransitive AS b
WHERE @cntnew != @cntold AND a.callers = b.calls;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;
--  ============== Populate JMethod_CallsTransitive: iteration 36
REPLACE into JMethod_CallsTransitive SELECT a.calls, b.callers, a.filePosition FROM JMethod_CallsTransitive AS a, JMethod_CallsTransitive AS b
WHERE @cntnew != @cntold AND a.callers = b.calls;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;
--  ============== Populate JMethod_CallsTransitive: iteration 37
REPLACE into JMethod_CallsTransitive SELECT a.calls, b.callers, a.filePosition FROM JMethod_CallsTransitive AS a, JMethod_CallsTransitive AS b
WHERE @cntnew != @cntold AND a.callers = b.calls;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;
--  ============== Populate JMethod_CallsTransitive: iteration 38
REPLACE into JMethod_CallsTransitive SELECT a.calls, b.callers, a.filePosition FROM JMethod_CallsTransitive AS a, JMethod_CallsTransitive AS b
WHERE @cntnew != @cntold AND a.callers = b.calls;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;
--  ============== Populate JMethod_CallsTransitive: iteration 39
REPLACE into JMethod_CallsTransitive SELECT a.calls, b.callers, a.filePosition FROM JMethod_CallsTransitive AS a, JMethod_CallsTransitive AS b
WHERE @cntnew != @cntold AND a.callers = b.calls;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;
--  ============== Populate JMethod_CallsTransitive: iteration 40
REPLACE into JMethod_CallsTransitive SELECT a.calls, b.callers, a.filePosition FROM JMethod_CallsTransitive AS a, JMethod_CallsTransitive AS b
WHERE @cntnew != @cntold AND a.callers = b.calls;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;
--  ============== Populate JMethod_CallsTransitive: iteration 41
REPLACE into JMethod_CallsTransitive SELECT a.calls, b.callers, a.filePosition FROM JMethod_CallsTransitive AS a, JMethod_CallsTransitive AS b
WHERE @cntnew != @cntold AND a.callers = b.calls;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;
--  ============== Populate JMethod_CallsTransitive: iteration 42
REPLACE into JMethod_CallsTransitive SELECT a.calls, b.callers, a.filePosition FROM JMethod_CallsTransitive AS a, JMethod_CallsTransitive AS b
WHERE @cntnew != @cntold AND a.callers = b.calls;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;
--  ============== Populate JMethod_CallsTransitive: iteration 43
REPLACE into JMethod_CallsTransitive SELECT a.calls, b.callers, a.filePosition FROM JMethod_CallsTransitive AS a, JMethod_CallsTransitive AS b
WHERE @cntnew != @cntold AND a.callers = b.calls;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;
--  ============== Populate JMethod_CallsTransitive: iteration 44
REPLACE into JMethod_CallsTransitive SELECT a.calls, b.callers, a.filePosition FROM JMethod_CallsTransitive AS a, JMethod_CallsTransitive AS b
WHERE @cntnew != @cntold AND a.callers = b.calls;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;
--  ============== Populate JMethod_CallsTransitive: iteration 45
REPLACE into JMethod_CallsTransitive SELECT a.calls, b.callers, a.filePosition FROM JMethod_CallsTransitive AS a, JMethod_CallsTransitive AS b
WHERE @cntnew != @cntold AND a.callers = b.calls;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;
--  ============== Populate JMethod_CallsTransitive: iteration 46
REPLACE into JMethod_CallsTransitive SELECT a.calls, b.callers, a.filePosition FROM JMethod_CallsTransitive AS a, JMethod_CallsTransitive AS b
WHERE @cntnew != @cntold AND a.callers = b.calls;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;
--  ============== Populate JMethod_CallsTransitive: iteration 47
REPLACE into JMethod_CallsTransitive SELECT a.calls, b.callers, a.filePosition FROM JMethod_CallsTransitive AS a, JMethod_CallsTransitive AS b
WHERE @cntnew != @cntold AND a.callers = b.calls;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;
--  ============== Populate JMethod_CallsTransitive: iteration 48
REPLACE into JMethod_CallsTransitive SELECT a.calls, b.callers, a.filePosition FROM JMethod_CallsTransitive AS a, JMethod_CallsTransitive AS b
WHERE @cntnew != @cntold AND a.callers = b.calls;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;
--  ============== Populate JMethod_CallsTransitive: iteration 49
REPLACE into JMethod_CallsTransitive SELECT a.calls, b.callers, a.filePosition FROM JMethod_CallsTransitive AS a, JMethod_CallsTransitive AS b
WHERE @cntnew != @cntold AND a.callers = b.calls;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;
--  ============== Populate JMethod_CallsTransitive: iteration 50
REPLACE into JMethod_CallsTransitive SELECT a.calls, b.callers, a.filePosition FROM JMethod_CallsTransitive AS a, JMethod_CallsTransitive AS b
WHERE @cntnew != @cntold AND a.callers = b.calls;
SELECT @cntold := @cntnew; SELECT @cntnew := COUNT(*) FROM JMethod_CallsTransitive;
--  ============== Populate JMethod_CallsTransitive: End
