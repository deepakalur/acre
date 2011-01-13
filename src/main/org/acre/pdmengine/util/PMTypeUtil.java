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
package org.acre.pdmengine.util;

import org.acre.pdm.PDMType;
import org.acre.pdm.Role;
import org.acre.pdm.RoleType;
import org.acre.common.AcreStringUtil;
import org.acre.dao.DAOFactory;
import org.acre.dao.PatternRepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author rajmohan@Sun.com
 */
public class PMTypeUtil {
    static PatternRepository patternRepository = DAOFactory.getPatternRepository();

    public static RoleType getRoleType(PDMType pdm, String roleName) {
        Iterator rolesItr = pdm.getRoles().getRole().iterator();
        while (rolesItr.hasNext() ) {
            RoleType roleType = (RoleType)rolesItr.next();
            if ( roleType.getName().equalsIgnoreCase(roleName))
                return roleType;
        }
        return null;
    }

    public static boolean isPatternName(String name) {
        return patternRepository.getGlobalPatternModel(name) != null;
    }

    public static String[] getPatternRoleNames(String patternName) {
        PDMType pdm = patternRepository.getGlobalPatternModel(patternName);

        if ( pdm == null)
            return new String[0];

        List roleNames = new ArrayList();

        Iterator roles = pdm.getRoles().getRole().iterator();
        while (roles.hasNext()) {
            RoleType role = (RoleType)roles.next();
            roleNames.add(role.getName());
        }

        String [] result = new String[roleNames.size()];
        roleNames.toArray(result);
        return result;

    }

    public static String getReferedPatternName(RoleType roleType) {
        String referedPattern;
        referedPattern = roleType.getTypeReferenceName();
        if ( AcreStringUtil.isEmpty(referedPattern) )
            referedPattern = roleType.getName();
        return referedPattern;
    }

}
