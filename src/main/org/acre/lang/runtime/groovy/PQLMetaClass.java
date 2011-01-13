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
package org.acre.lang.runtime.groovy;

import groovy.lang.Closure;
import groovy.lang.MetaClass;
import groovy.lang.MetaClassRegistry;
import groovy.lang.MetaMethod;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.runtime.Reflector;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public  class PQLMetaClass extends MetaClass {
    public PQLMetaClass(MetaClassRegistry registry, Class theClass) throws IntrospectionException {
        super(registry, theClass);
    }

    public List getMethods(String name) {
        return super.getMethods(name);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public List getStaticMethods(String name) {
        return super.getStaticMethods(name);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void addNewInstanceMethod(Method method) {
        super.addNewInstanceMethod(method);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void addNewInstanceMethod(MetaMethod method) {
        super.addNewInstanceMethod(method);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void addNewStaticMethod(Method method) {
        super.addNewStaticMethod(method);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void addNewStaticMethod(MetaMethod method) {
        super.addNewStaticMethod(method);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public Object invokeMethod(Object object, String methodName, Object arguments) {
        return super.invokeMethod(object, methodName, arguments);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public Object invokeMethod(Object object, String methodName, Object[] arguments) {
        return super.invokeMethod(object, methodName, arguments);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public MetaMethod retrieveMethod(Object owner, String methodName, Object[] arguments) {
        return super.retrieveMethod(owner, methodName, arguments);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public MetaMethod retrieveMethod(String methodName, Class[] arguments) {
        return super.retrieveMethod(methodName, arguments);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public Constructor retrieveConstructor(Class[] arguments) {
        return super.retrieveConstructor(arguments);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public MetaMethod retrieveStaticMethod(String methodName, Class[] arguments) {
        return super.retrieveStaticMethod(methodName, arguments);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public MetaMethod pickMethod(Object object, String methodName, Object[] arguments) {
        return super.pickMethod(object, methodName, arguments);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public MetaMethod pickMethod(String methodName, Class[] arguments) {
        return super.pickMethod(methodName, arguments);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public Object invokeStaticMethod(Object object, String methodName, Object[] arguments) {
        return super.invokeStaticMethod(object, methodName, arguments);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public MetaMethod pickStaticMethod(Object object, String methodName, Object[] arguments) {
        return super.pickStaticMethod(object, methodName, arguments);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public MetaMethod pickStaticMethod(String methodName, Class[] arguments) {
        return super.pickStaticMethod(methodName, arguments);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public Object invokeConstructor(Object[] arguments) {
        return super.invokeConstructor(arguments);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void setProperties(Object bean, Map map) {
        super.setProperties(bean, map);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public Object getProperty(Object object, String property) {
        return super.getProperty(object, property);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public List getProperties() {
        return super.getProperties();    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected void setupProperties(PropertyDescriptor[] propertyDescriptors) {
        super.setupProperties(propertyDescriptors);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void setProperty(Object object, String property, Object newValue) {
        super.setProperty(object, property, newValue);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public ClassNode getClassNode() {
        return super.getClassNode();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public String toString() {
        return super.toString();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public Object[] asArray(Object arguments) {
        return super.asArray(arguments);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public Object createListenerProxy(Class listenerType, String listenerMethodName, Closure closure) {
        return super.createListenerProxy(listenerType, listenerMethodName, closure);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void addMethods(Class theClass) {
        super.addMethods(theClass);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void addMethod(MetaMethod method) {
        super.addMethod(method);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public boolean containsMatchingMethod(List list, MetaMethod method) {
        return super.containsMatchingMethod(list, method);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected void addNewStaticMethodsFrom(Class theClass) {
        super.addNewStaticMethodsFrom(theClass);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public Object getStaticProperty(Class aClass, String property) {
        return super.getStaticProperty(aClass, property);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public MetaMethod findMethod(Method aMethod) {
        return super.findMethod(aMethod);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public MetaMethod findGetter(Object object, String name) {
        return super.findGetter(object, name);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public MetaMethod findStaticGetter(Class type, String name) {
        return super.findStaticGetter(type, name);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public Object doMethodInvoke(Object object, MetaMethod method, Object[] argumentArray) {
        return super.doMethodInvoke(object, method, argumentArray);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public Object doConstructorInvoke(Constructor constructor, Object[] argumentArray) {
        return super.doConstructorInvoke(constructor, argumentArray);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public Object chooseMethod(String methodName, List methods, Class[] arguments, boolean coerce) {
        return super.chooseMethod(methodName, methods, arguments, coerce);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public boolean isValidMethod(Object method, Class[] arguments, boolean includeCoerce) {
        return super.isValidMethod(method, arguments, includeCoerce);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public Object chooseMostSpecificParams(String name, List matchingMethods, Class[] arguments) {
        return super.chooseMostSpecificParams(name, matchingMethods, arguments);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void checkForInvalidOverloading(String name, Class[] baseTypes, Class[] derivedTypes) {
        super.checkForInvalidOverloading(name, baseTypes, derivedTypes);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public Class[] getParameterTypes(Object methodOrConstructor) {
        return super.getParameterTypes(methodOrConstructor);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public Object chooseMostGeneralMethodWith1Param(List methods) {
        return super.chooseMostGeneralMethodWith1Param(methods);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public Object chooseEmptyMethodParams(List methods) {
        return super.chooseEmptyMethodParams(methods);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public boolean isAssignableFrom(Class mostSpecificType, Class type) {
        return super.isAssignableFrom(mostSpecificType, type);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public boolean isGenericSetMethod(MetaMethod method) {
        return super.isGenericSetMethod(method);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public boolean isGenericGetMethod(MetaMethod method) {
        return super.isGenericGetMethod(method);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void registerStaticMethods() {
        super.registerStaticMethods();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void registerInstanceMethods() {
        super.registerInstanceMethods();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public String capitalize(String property) {
        return super.capitalize(property);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public synchronized void onMethodChange() {
        super.onMethodChange();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public synchronized void checkInitialised() {
        super.checkInitialised();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public MetaMethod createMetaMethod(Method method) {
        return super.createMetaMethod(method);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public boolean isValidReflectorMethod(MetaMethod method) {
        return super.isValidReflectorMethod(method);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void generateReflector() {
        super.generateReflector();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public Reflector loadReflector(List methods) {
        return super.loadReflector(methods);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public Class loadReflectorClass(String name, byte[] bytecode) throws ClassNotFoundException {
        return super.loadReflectorClass(name, bytecode);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public Class loadReflectorClass(String name) throws ClassNotFoundException {
        return super.loadReflectorClass(name);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public List getMethods() {
        return super.getMethods();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public List getMetaMethods() {
        return super.getMetaMethods();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public synchronized List getInterfaceMethods() {
        return super.getInterfaceMethods();    //To change body of overridden methods use File | Settings | File Templates.
    }
}
