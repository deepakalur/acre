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
import org.acre.extractor.jaxb.soap.SoapElemBinding;
import org.acre.extractor.jaxb.soap.SoapElemOperation;
import org.acre.extractor.jaxb.soap.SoapElemFault;

import java.io.*;
import java.util.*;
import javax.xml.bind.*;


public class Main {
  public static void main(String[] args) throws FileNotFoundException,
      FileNotFoundException, JAXBException {
    if (args.length > 0) {
      Object o = unmarshal(new FileInputStream(args[0]), "org.acre.extractor.jaxb.wsdl");
      print((WsdlElemDefinitions) o);
      //System.out.println("o: " + o);
    }
    System.out.println("DONE");
  }
  public static void print(WsdlElemDefinitions def) {
    System.out.println("def: " + def.getName() + "=" + def.getTargetNamespace());
    printAnyList("  ", def.getAny());
    System.out.println("# Top level options: " + def.getAnyTopLevelOptionalElement().size());
//    for (int i = 0; i < def.getAnyTopLevelOptionalElement().size(); i++) {
//      System.out.println(i+": "+def.getAnyTopLevelOptionalElement().get(i));
//    }
    for (Iterator iter = def.getAnyTopLevelOptionalElement().iterator(); iter.hasNext(); ) {
      Object item = (Object)iter.next();
      if (false) {
      } else if (item instanceof AnyTopLevelOptionalElementImport) {
        AnyTopLevelOptionalElementImport a = (AnyTopLevelOptionalElementImport) item;
        System.out.println("    Import: " + a.getNamespace() + "=" + a.getLocation());
      } else if (item instanceof AnyTopLevelOptionalElementTypes) {
        AnyTopLevelOptionalElementTypes a = (AnyTopLevelOptionalElementTypes) item;
        System.out.println("    Type: " + a);
        System.out.println("    Type: " + a.getDocumentation());
        printAnyList("      ", a.getAny());
      } else if (item instanceof AnyTopLevelOptionalElementMessage) {
        AnyTopLevelOptionalElementMessage a = (AnyTopLevelOptionalElementMessage) item;
        System.out.println("    Message: " + a.getName());
        printAnyList("      ", a.getAny());
      } else if (item instanceof AnyTopLevelOptionalElementPortType) {
        AnyTopLevelOptionalElementPortType a = (AnyTopLevelOptionalElementPortType) item;
        System.out.println("    Port: " + a.getName());
        for (Iterator innerIter = a.getOperation().iterator(); innerIter.hasNext(); ) {
          WsdlTypeTOperation b = (WsdlTypeTOperation)innerIter.next();
          printOperation(b);
        }
      } else if (item instanceof AnyTopLevelOptionalElementBinding) {
        AnyTopLevelOptionalElementBinding a = (AnyTopLevelOptionalElementBinding) item;
        System.out.println("    Binding: " + a.getName());
        printAnyList("      ", a.getAny());
        for (Iterator innerIter = a.getOperation().iterator(); innerIter.hasNext(); ) {
          WsdlTypeTBindingOperation b = (WsdlTypeTBindingOperation) innerIter.next();
          printOperation(b);
        }
      } else if (item instanceof AnyTopLevelOptionalElementService) {
        AnyTopLevelOptionalElementService a = (AnyTopLevelOptionalElementService) item;
        System.out.println("    Service: " + a.getName());
        printAnyList("      ", a.getAny());
      } else {
        System.out.println("    " + item);
      }

    }

  }

  private static void printAnyList(String indent, List any) {
    printAnyList(indent, "Any", any);
  }

  private static void printAnyList(String indent, String name, List any) {
    if (any.size() == 0)
      return;
    System.out.println(indent + name + ": " + any.size());
    for (Iterator innerIter = any.iterator(); innerIter.hasNext(); ) {
      Object b = innerIter.next();
      if (false) {
      } else if (b instanceof SoapElemBinding) {
         SoapElemBinding c = (SoapElemBinding) b;
         System.out.println(indent + "  SoapBinding: " + c.getStyle());
         System.out.println(indent + "  SoapBinding: " + c.getTransport());
       } else if (b instanceof SoapElemOperation) {
          SoapElemOperation c = (SoapElemOperation) b;
          System.out.println(indent + "  SoapOperation: " + c.getSoapAction());
          System.out.println(indent + "  SoapOperation: " + c.getStyle());
        } else if (b instanceof SoapElemFault) {
           SoapElemFault c = (SoapElemFault) b;
           System.out.println(indent + "  SoapFault: " + c.getName());
           System.out.println(indent + "  SoapFault: " + c.getNamespace());
           System.out.println(indent + "  SoapFault: " + c.getNamespace());
      } else {
        System.out.println(indent + "Any: " + b);
      }
    }
  }

  private static void printOperation(WsdlTypeTOperation b) {
    System.out.println("      Operation: " + b.getName());
    System.out.println("        Input: " + b.getInput().getMessage());
    System.out.println("        Output: " + b.getOutput().getMessage());
    printAnyList("        ", b.getAny());
    for (Iterator iter3 = b.getFault().iterator(); iter3.hasNext(); ) {
      WsdlTypeTFault c = (WsdlTypeTFault)iter3.next();
      System.out.println("        Fault: " + c.getMessage());
    }
  }

  private static void printOperation(WsdlTypeTBindingOperation b) {
    System.out.println("      Operation: " + b.getName());
    System.out.println("        Input: " + b.getInput().getName());
    System.out.println("        Output: " + b.getOutput().getName());
    printAnyList("        ", b.getAny());
    for (Iterator iter3 = b.getFault().iterator(); iter3.hasNext(); ) {
      WsdlTypeTBindingOperationFault c = (WsdlTypeTBindingOperationFault)iter3.next();
      System.out.println("        Fault: " + c.getName());
    }
  }


  public static Object unmarshal(InputStream is, String packageName) throws JAXBException {
    // Unmarshaller to unmarshal xml to java object content tree
    JAXBContext jaxbContext = JAXBContext.newInstance(packageName);
    Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
    return unmarshaller.unmarshal(is);
  }

}
