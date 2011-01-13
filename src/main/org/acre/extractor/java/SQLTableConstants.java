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
package org.acre.extractor.java;
public interface SQLTableConstants {
    public static final String JPackage_PackagesTransitive ="JPackage_PackagesTransitive";
    public static final String JClass_ImplementsInterfaces ="JClass_ImplementsInterfaces";
    public static final String JClass_ImplementsInterfacesTransitive ="JClass_ImplementsInterfacesTransitive";
    public static final String JClass_ExtendsClassTransitive ="JClass_ExtendsClassTransitive";
    public static final String JMethod_ThrowsExceptions ="JMethod_ThrowsExceptions";
    public static final String JMethod_CatchesExceptions ="JMethod_CatchesExceptions";
    public static final String JMethod_Calls ="JMethod_Calls";
    public static final String JMethod_CallsTransitive ="JMethod_CallsTransitive";
    public static final String JMethod_Instantiates ="JMethod_Instantiates";
    public static final String JMethod_UsedFields ="JMethod_UsedFields";
    public static final String JAnnotationArgument ="JAnnotationArgument";
    public static final String JAnnotation ="JAnnotation";
    public static final String JNonClassType ="JNonClassType";
    public static final String JClass_ContainsClassesTransitive ="JClass_ContainsClassesTransitive";
    public static final String JParameter ="JParameter";
    public static final String JField ="JField";
    public static final String JMethod ="JMethod";
    public static final String JClass ="JClass";
    public static final String JPackage ="JPackage";
    public static final String[] SQL_TABLES = {
        JPackage_PackagesTransitive,
        JClass_ImplementsInterfaces,
        JClass_ImplementsInterfacesTransitive,
        JClass_ExtendsClassTransitive,
        JMethod_ThrowsExceptions,
        JMethod_CatchesExceptions,
        JMethod_Calls,
        JMethod_CallsTransitive,
        JMethod_Instantiates,
        JMethod_UsedFields,
        JAnnotationArgument,
        JAnnotation,
        JNonClassType,
        JClass_ContainsClassesTransitive,
        JParameter,
        JField,
        JMethod,
        JClass,
        JPackage,
    };
}
