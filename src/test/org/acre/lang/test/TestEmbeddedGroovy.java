package org.acre.lang.test;

import groovy.lang.GroovyClassLoader;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.LinkedHashMap;

import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.syntax.SyntaxException;

//import org.codehaus.groovy.control.CompilationFailedException;
import org.acre.lang.runtime.lib.PDMComponent;
import org.acre.lang.runtime.lib.PDMComponentFactory;

public class TestEmbeddedGroovy {

    public static void main(String[] args) throws IllegalAccessException, InstantiationException, SyntaxException, CompilationFailedException, IOException {
        if(args.length == 0) {
            System.out.println("Usage: TestEmbeddedGroovy groovyscript");
        }

        String fileName = args[0];
//        ClassLoader parent = TestEmbeddedGroovy.class.getClassLoader();
//        GroovyClassLoader gcl = new GroovyClassLoader(parent);
//        Class clazz = gcl.parseClass(new File(fileName));
//        Object aScript = clazz.newInstance();
//        PDMComponent component = (PDMComponent) aScript;

        PDMComponent component = PDMComponentFactory.getPDMComponent(new File(fileName));

        System.out.println("============= Print   PDM component in script " + fileName + "==========");
        System.out.println("    name        = " + component.getName());
        System.out.println("    description = " + component.getDescription());
        System.out.println("    metadata    = " + component.getMetadata());
        System.out.println("    inputNames  = " + component.getInputNames());
        System.out.println("    outputNames = " + component.getOutputNames());
        System.out.println("============= Execute PDM component in script " + fileName + "==========");
        Map map = new LinkedHashMap();
        map.put("input1", new Integer(1));
        map.put("input2", new Integer(2));
        map.put("input3", new Integer(3));
        Object o = component.execute(map);
        System.out.println("++++++ Execution output of PDM component in script " + fileName + "+++++");
        System.out.println(o);
    }
}

