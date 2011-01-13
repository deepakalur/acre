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
package org.acre.extractor.wsdl.wsdlimport;

import org.acre.extractor.jaxb.wsdl.*;

import java.io.*;
import java.util.*;
import javax.xml.bind.*;

public class WSDL_FactExtractor {

    private FileWriter factTupleWriter;
    private final String factTupleOutputFileName = "factTuples.ta";
    private FileWriter factAttributeWriter;
    private final String factAttributeOutputFileName = "factAttributes.ta";
    private FileWriter debugMesgWriter;
    private final String debugMesgFileName = "factDebugMesgs.txt";
    private final String inputFileName;

    public WSDL_FactExtractor (String inputFileName) {
	this.inputFileName = inputFileName;
	try {
	    factTupleWriter = new FileWriter (factTupleOutputFileName);
	    factTupleWriter.write("\nFACT TUPLE :\n\n");
	    factAttributeWriter = new FileWriter (factAttributeOutputFileName);
	    factAttributeWriter.write("\nFACT ATTRIBUTE :\n\n");
	} catch (FileNotFoundException e) {
	    System.err.println ("Couldn't find input file \"" 
		    + inputFileName + "\", aborting.");
	} catch (IOException e) {
	    System.err.println ("Couldn't create output file \"" 
		    + factTupleOutputFileName + "\" or \"" 
		    + factAttributeOutputFileName + "\", aborting.");
	}
    }

    private void writeFact (FileWriter writer, String s) {
	try {
	    if (writer == null) {
		System.out.println ("Sorry, this writer is null.");
	    } else {
		writer.write (s + "\n");
		// flush while still developing; nuke when stable
		writer.flush();
	    }
	} catch (IOException e) {
	    System.err.println(e.toString());
	}
    }
    
    private void extractFacts () throws FileNotFoundException, JAXBException{
	Object o = unmarshal(new FileInputStream(inputFileName),
		    "org.acre.extractor.jaxb.wsdl");
	extractFacts ((WsdlElemDefinitions) o);
	System.out.println("DONE");
    }

    private void extractFacts (WsdlElemDefinitions def) {
	final String defID = def.getTargetNamespace() + ":" + def.getName();
	writeFact (factAttributeWriter, defID + " { "
		+ "name=" + def.getName() 
		+ " targetNameSpace=" + def.getTargetNamespace()
		+ " }");
	for (Iterator iter = def.getAnyTopLevelOptionalElement().iterator(); 
		iter.hasNext(); ) { 
	    Object item = (Object)iter.next();
	    if (false) {
	    } else if (item instanceof AnyTopLevelOptionalElementImport) {
	    } else if (item instanceof AnyTopLevelOptionalElementTypes) {
	    } else if (item instanceof AnyTopLevelOptionalElementMessage) {
	    } else if (item instanceof AnyTopLevelOptionalElementPortType) {
		AnyTopLevelOptionalElementPortType a = 
		    (AnyTopLevelOptionalElementPortType) item;
		final String portTypeID = defID + ":" + a.getName();
		writeFact (factTupleWriter, "hasPortType " + defID + " " 
			+ portTypeID);
		writeFact (factAttributeWriter, portTypeID + " { "
			+ "name=" + a.getName()
			+ " }");
	    } else if (item instanceof AnyTopLevelOptionalElementBinding) {
		AnyTopLevelOptionalElementBinding a = 
		    (AnyTopLevelOptionalElementBinding) item;
		final String bindingID = defID + ":" + a.getName();
		writeFact (factTupleWriter, "hasBinding " + defID + " " 
			+ bindingID);
		writeFact (factAttributeWriter, bindingID + " { "
			+ "name=" + a.getName()
			+ "type=" + a.getType()
			+ " }");
	    } else if (item instanceof AnyTopLevelOptionalElementService) {
	    } else {
	    }
	}
    }

    public static void main(String[] args) {
	try {
	    if (args.length > 0) {
		WSDL_FactExtractor extractor = new WSDL_FactExtractor (args[0]);
		extractor.extractFacts();
		System.out.println("DONE");
	    } else {
		System.err.println("ERROR: Must provide WSDL file name");
	    }
	} catch (Exception e) {
	    System.err.println ("ERROR: " + e);
	}
    }

    public static Object unmarshal(InputStream is, String packageName) 
	    throws JAXBException {
	// Unmarshaller to unmarshal xml to java object content tree
	JAXBContext jaxbContext = JAXBContext.newInstance(packageName);
	Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
	return unmarshaller.unmarshal(is);
    }
}
