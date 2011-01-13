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
package org.acre.common;

import org.acre.visualizer.graph.GraphFactoryConstants;

import java.util.StringTokenizer;

/**
 * @author Deepak.Alur@Sun.com
 * @version Nov 6, 2004 4:50:40 PM
 */
public class AcreStringUtil {
    public static boolean isEmpty(String str) {
        if (str == null)
            return true;

        if (str.trim().length() == 0)
            return true;

        return false;
    }

    public static boolean isEmpty(String str[]) {
        if (str == null)
            return true;

        if (str.length == 0)
            return true;

        return false;
    }

    public static String getRoleName(String fullyQualifiedRoleName) {
        if (isEmpty(fullyQualifiedRoleName)) {
            return null;
        }

        return getRoleNameEnding(fullyQualifiedRoleName);

    }

    public static String getPDMNameFromRole(String fullyQualifiedRoleName) {
        if (isEmpty(fullyQualifiedRoleName)) {
            return null;
        }
        int index = fullyQualifiedRoleName.indexOf(GraphFactoryConstants.SEPARATOR);
        if (index == -1)
            return null;

        return getRoleNameBeginning(fullyQualifiedRoleName);
    }

    public static String getRoleNameBeginning(String fullyQualifiedRoleName) {
        if (isEmpty(fullyQualifiedRoleName)) {
            return "";
        }
        StringTokenizer tokenizer = new StringTokenizer(
                fullyQualifiedRoleName,
                GraphFactoryConstants.SEPARATOR);
        String roleName = tokenizer.nextToken();
        return roleName;
    }

    public static String getRoleNameEnding(String fullyQualifiedRoleName) {
        if (isEmpty(fullyQualifiedRoleName)) {
            return "";
        }
        StringTokenizer tokenizer = new StringTokenizer(fullyQualifiedRoleName, GraphFactoryConstants.SEPARATOR);
        String roleName = null;
        while(tokenizer.hasMoreTokens()) {
            roleName = tokenizer.nextToken();
        }
        return roleName;
    }

    public static String concat(String str[], char delimiter) {

        if ( str == null || str.length ==0 )
            return null;

        int len = str.length;
        if ( len == 1)
            return str[0];

        StringBuffer sb = new StringBuffer(8096);

        for ( int i=0; i < len - 1; i++) {
            sb.append(str[i]).append(delimiter);
        }
        sb.append(str[len-1]);
        return sb.toString();
    }

     public static String getReturnVariable(String pqlScript) {
        String s = pqlScript.toLowerCase().trim();

        StringTokenizer tk = new StringTokenizer(s, ";");
         String lastToken = null;

        while (tk.hasMoreTokens()) {
             lastToken = tk.nextToken();
             System.out.println("Line -> " + lastToken);
        }

         String returnVariableName = null;
         lastToken = lastToken.trim();
         lastToken = lastToken.replaceAll("\n", "");
         lastToken = lastToken.replaceAll("\t", "");
         lastToken = lastToken.replaceAll("\r", "");

        if (lastToken.indexOf("return") != -1) {
            StringTokenizer ltk = new StringTokenizer(lastToken, " ");
            while (ltk.hasMoreTokens()) {
                returnVariableName = ltk.nextToken();
                System.out.println("R ->" + returnVariableName);
            }
        }

        return returnVariableName;
    }

    public static void main(String args[]) {
        String r = AcreStringUtil.getReturnVariable("include ServiceLocator;\n" +
                "\n" +
                "include SessionFacade;\n" +
                "\n" +
                "define classesCallingServiceLocators as\n" +
                "select c from classes c\n" +
                "where c.methods.calls.**.parentClass in EJBServiceLocators;\n" +
                "\n" +
                "define BusinessDelegates as\n" +
                "select c from classesCallingServiceLocators c\n" +
                "where  c.fields.type in SFInterfaces;\n" +
                "\n" +
                "\n\nreturn       BusinessDelegates \n ;\n\n   \n");

        System.out.println("Return Variable = " + r);
        System.out.println("PDM Name = " + getPDMNameFromRole("NoPDMRoleName"));
        System.out.println("PDM Name = " + getPDMNameFromRole("PDM.RoleName"));
        System.out.println("PDM Name = " + getRoleName("NoPDMRoleName"));
        System.out.println("PDM Name = " + getRoleName("PDM.RoleName"));

    }

}
