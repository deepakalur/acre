/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000 - 2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "Ant" and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */
package org.apache.tools.ant.gui.acs;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.File;

import java.net.URL;

import java.util.Enumeration;
import java.util.Properties;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.tools.ant.IntrospectionHelper;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.taskdefs.Property;

import org.apache.tools.ant.gui.xml.DOMDocument;
import org.apache.tools.ant.gui.xml.DOMNodeManager;

import org.xml.sax.SAXException;

/**
 * Factory for loading Ant Construction set elements.
 * 
 * @version $Revision: 1.1.1.1 $ 
 * @author Simeon Fitch, Christoph Wilhelms<a href="mailto:christoph.wilhelms@t-online.de">christoph.wilhelms@t-online.de</a>, Craig Campbell
 */
public class ACSFactory {
    /** Singleton instance of the factory. */
    private static ACSFactory _instance = null;

    /** Element maping. */
    private Properties _elementMap = new Properties();                        //(name -> ACSClassName)        Probably deprecated soon...

    private Properties _taskClassMap = new Properties();                      // (name -> source className)
    private Properties _elementClassMap = new Properties();                   // (name -> source className)
    private Map _classInfoMap = Collections.synchronizedMap (new HashMap ()); // (className -> attributes/subElements) 

    
    /**
     * The Main introspection happens here! Ofcourse there is a lot of "cleaning up" to do... 
     * Additionally: handling more default properties, custom Tasks, etc.
     */
    private void init() {
        try {
            String antProjectName = Project.class.getName();
            String antTargetName = Target.class.getName();
            String antPropertyName = Property.class.getName();
            analyzeClass(antProjectName);
            analyzeClass(antTargetName);

            // First we bootstrap our knowledge of the Ant tasks by reading
            // in the taskdef definitions and assigning them the default
            // task element class.
            _taskClassMap.load(org.apache.tools.ant.taskdefs.Ant.class.
                          getResourceAsStream("defaults.properties"));
            _taskClassMap.setProperty("property", antPropertyName);
            Enumeration enum = _taskClassMap.propertyNames();
            while(enum.hasMoreElements()) {
                String name = (String) enum.nextElement();

                // Directly collect Introspection Info for nown types
                analyzeClass(_taskClassMap.getProperty(name));

                // XXX the name of the class needs to be stored externally.
                _elementMap.setProperty(name, "org.apache.tools.ant.gui.acs.ACSTaskElement");
            }
            _elementMap.setProperty("property", "org.apache.tools.ant.gui.acs.ACSPropertyElement");
            
            Properties tempElementClassMap = new Properties();
            tempElementClassMap.load(org.apache.tools.ant.types.FileSet.class.
                         getResourceAsStream("defaults.properties"));
            _elementClassMap.putAll(tempElementClassMap);
            ((ClassInfo)_classInfoMap.get(antTargetName))._subelements.putAll(tempElementClassMap);
            ((ClassInfo)_classInfoMap.get(antProjectName))._subelements.putAll(tempElementClassMap);
            ((ClassInfo)_classInfoMap.get(antTargetName))._subelements.put("property",antPropertyName);
            ((ClassInfo)_classInfoMap.get(antProjectName))._subelements.put("property",antPropertyName);
            enum = _elementClassMap.propertyNames();
            while(enum.hasMoreElements()) {
                String name = (String) enum.nextElement();

                // Directly collect Introspection Info for nown types
                analyzeClass(_elementClassMap.getProperty(name));
                _elementMap.setProperty(
                    name, "org.apache.tools.ant.gui.acs.ACSIntrospectedElement");
            }

            // Then we add/override the local definitions.
            _elementMap.load(ACSFactory.class.
                             getResourceAsStream("acs-element.properties"));

            _elementClassMap.setProperty("project", antProjectName);
            _elementClassMap.setProperty("target", antTargetName);

            // Finally we scan the entire classpath for additional classes and custom Tasks.
        }
        catch(Throwable ex) {
            // If something wrong happens here we can't do much more...
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public void analyzeClass(String qualifiedClassName) {
        // Check if info already has been introspected.
        if (_classInfoMap.containsKey(qualifiedClassName)) return;
        try {
            // Create ClassInfo and insert in classInfoMap
            ClassInfo info = new ClassInfo();
            _classInfoMap.put(qualifiedClassName, "");
            // Create and introspect Class
            Class clazz = Class.forName(qualifiedClassName);
            IntrospectionHelper helper = IntrospectionHelper.getHelper(clazz);
            info._isContainer = helper.supportsCharacters();
            // Lookup Attributes
            Enumeration e = helper.getAttributes();
            if (e.hasMoreElements()) {
                info._attributes = new HashMap();
                while (e.hasMoreElements()) {
                    String name = (String) e.nextElement();
                    String type = helper.getAttributeType(name).getName();
                    if ((org.apache.tools.ant.Task.class.isAssignableFrom(clazz)) &&
                        ((name.equals ("location") && type.equals ("org.apache.tools.ant.Location"))/* || 
                         (name.equals ("taskname") && type.equals ("java.lang.String")) || 
                         (name.equals ("description") && type.equals ("java.lang.String"))*/)) { 
                        continue;
                    }
                    info._attributes.put(name, type);  // type is now needed for associating attribute name with a custom editor
//                    info._attributes.put(name, "");
                }
            }
            else info._attributes = null;
            // Lookup Subelements
            e = helper.getNestedElements();
            info._subelements = new HashMap();
            while (e.hasMoreElements()) {
                try {
                    String name = (String) e.nextElement();
                    Class subClazz = helper.getElementType(name);
                    info._subelements.put(name, subClazz.getName());
                    _elementClassMap.setProperty(name, subClazz.getName());
                    // ... and analyze that class!
                    analyzeClass(subClazz.getName());
                } catch (RuntimeException re) {}
            }
            _classInfoMap.remove(qualifiedClassName);
            _classInfoMap.put(qualifiedClassName, info);
        }
        catch (Throwable e) {}
    }
        
    /** 
     * Default ctor.
     * 
     */
    private ACSFactory() {
        init();
    }
    
    public ClassInfo getClassInfo(String key, boolean forTarget) {
        if (!_elementMap.containsKey(key))
            return null; // We have no clue how to optain this information!
        String className = null;
        if (forTarget) className = (String)_taskClassMap.get(key);
        if (className == null) className = (String)_elementClassMap.get(key);
        if (className == null) className = (String)_taskClassMap.get(key);
        if (className == null) return null;
        return (ClassInfo)_classInfoMap.get(className);
    }
    
    public Map getTasks() {
        return _taskClassMap;
    }
    
    /** 
     * Get an instance of the factory.
     * 
     * @return Factory instance.
     */
    public static ACSFactory getInstance() {
        if(_instance == null) {
            _instance = new ACSFactory();
        }
        return _instance;
    }

    /** 
     * Load a project from the given XML file.
     * 
     * @param location Location of the file.
     * @return Loaded project.
     */
    public ACSProjectElement load(File location) throws IOException {
        return load(new URL("file", null, location.getPath()));
    }

    /** 
     * Load a project from the given XML file.
     * 
     * @param location Location of the file.
     * @return Loaded project.
     */
    public ACSProjectElement load(URL location) throws IOException {
        return load(location.openStream());
    }

    /** 
     * Load a project from the given XML file.
     * 
     * @param location Location of the file.
     * @return Loaded project.
     */
    public ACSProjectElement load(InputStream stream) throws IOException {
        
        ACSProjectElement project = null;
        try {
            DOMNodeManager manager =
                new DOMNodeManager(_elementMap);
            
            DOMDocument doc = manager.parse(stream);
            project = (ACSProjectElement) doc.getDocumentElement();
            
        } catch (SAXException se) {
            se.printStackTrace();
            throw new IOException(se.getMessage());
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
            throw new IOException(pce.getMessage());
        }            
        return project;
    }

    /** 
     * Create a new, empty project.
     * 
     * @return Empty project.
     */
    public ACSProjectElement createProject() {
        final String XMLDOC = 
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<project name=\"untitled\" />";
        try {
            return load(new ByteArrayInputStream(XMLDOC.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /** 
     * Create a new target.
     * 
     * @param project Project the target is assigned to.
     * @return New, unnamed target.
     */
    public ACSTargetElement createTarget(ACSProjectElement project) {
        ACSTargetElement retval = (ACSTargetElement) project.
            getOwnerDocument().createElement("target");
        project.appendChild(retval);
        return retval;
    }

    /** 
     * Create a new task.
     * 
     * @param target Target the task is assigned to.
     * @return New, unnamed task.
     */
    public ACSTaskElement createTask(ACSTargetElement target) {
        ACSTaskElement retval = (ACSTaskElement) target.
            getOwnerDocument().createElement("task");
        target.appendChild(retval);
        return retval;
    }

    /** 
     * Create a new property.
     * 
     * @param node the Node to assign the property to.
     * @return New, unnamed property.
     */
    public ACSPropertyElement createProperty(ACSElement node) {
        ACSPropertyElement retval = (ACSPropertyElement) node.
            getOwnerDocument().createElement("property");
        node.appendChild(retval);
        return retval;
    }

    /** 
     * Create a new element.
     * 
     * @param node the Node to assign the property to.
     * @param name the new elements type.
     * @return New, unnamed property.
     */
    public ACSElement createElement(ACSElement node, String name) {
        ACSElement retval = (ACSElement) node.
            getOwnerDocument().createElement(name);
        addRequiredAttributes(retval);
        node.appendChild(retval);
        return retval;
    }
    
    /** 
     * Add required attributes to the node.
     * 
     * @param node the Node to add the attrinutes to.
     */
    public void addRequiredAttributes(ACSElement node) {
        if (node instanceof ACSDtdDefinedElement) {
            ACSDtdDefinedElement dtdElement = 
                (ACSDtdDefinedElement) node;
            dtdElement.addRequiredAttributes();
        }
    }
    
    /** 
     * Test code
     * 
     * @param args  XML file to parse.
     */
    public static void main(String[] args) {
        try {
            ACSFactory f = ACSFactory.getInstance();

            System.out.println(f.load(new File(args[0])));
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public class ClassInfo {
        public Map _attributes; 
        public Map _subelements; 
        public boolean _isContainer;
        
        public String toString () {
            return "ClassInfo[attributes=" + _attributes + ",subs=" + _subelements + "]";
        }
    }
}
