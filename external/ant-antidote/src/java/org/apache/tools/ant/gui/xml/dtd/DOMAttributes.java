/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999-2002 The Apache Software Foundation.  All rights
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

package org.apache.tools.ant.gui.xml.dtd;

import java.io.IOException;
import java.util.Properties;

/**
 * Represents the attributes defined by the DTD.
 *
 * @version $Revision: 1.1.1.1 $
 * @author Nick Davis<a href="mailto:nick_home_account@yahoo.com">Nick Davis</a>
 */
public class DOMAttributes extends Properties {

    ANTDocumentType.DtdElement _element = null;

    /**
     * Default constructor
     */
    public DOMAttributes() {
        this(null);
    }

    /**
     * Constructor
     *
     * @param element provides information about possible attributes.
     */
    public DOMAttributes(ANTDocumentType.DtdElement element) {
        _element = element;
    }

    /**
     * Return the required attributes or null.
     */
    public String[] getRequiredAttributes() {
        if (_element == null) {
            return null;
        }
        return _element.getAttributes().getRequiredAttributes();
    }
    /**
     * Return the optional attributes or null.
     */
    public String[] getOptionalAttributes() {
        if (_element == null) {
            return null;
        }
        return _element.getAttributes().getOptionalAttributes();
    }
    /**
     * Retrun the class associated with the tag
     *
     * @return Class
     */
    public Class getTagClass()  {
        return lookupByName(_element.getName());
    }

    /**
     * Lookup the associated class name for dynamic editor creation.
     *
     * @param tag name
     * @return Class class of the tag
     */
    private Class lookupByName(String name) {
        Class retval = null;
        try {
            Properties tasks = new Properties();
            tasks.load(
                    org.apache.tools.ant.taskdefs.Ant.class.getResourceAsStream(
                    "defaults.properties"));

            retval = lookup(name, tasks);

            if(retval == null)  {

                Properties types = new Properties();
                types.load(
                        org.apache.tools.ant.types.DataType.class.getResourceAsStream(
                        "defaults.properties"));

                retval = lookup(name, types);
            }
        }
        catch(IOException exp)  {
            exp.printStackTrace();
        }

        return retval;
    }

    private Class lookup(String name, Properties defaults)  {

        Class retval = null;
        try {
            retval = Class.forName(defaults.getProperty(name));
        }
        catch(ClassNotFoundException exp)   {
        }
        catch(NoClassDefFoundError err)   {
        }

        return retval;
    }
}
