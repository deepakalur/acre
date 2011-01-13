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
package org.acre.pdmengine.model;

import org.acre.pdm.PDMType;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author rajmohan@Sun.com
 * @version Nov 22, 2004 10:23:48 PM
 */
public interface PatternResult extends Result {
    PDMType getPdm();

    void setPdm(PDMType pdm);

    Collection getRoles();

    RoleResult getRoleReference(String roleReference);

    void setRoles(Collection roles);

    Collection getRelationships();

    void setRelationships(Collection relationships);

    Artifact findArtifact(String artifactName);

    List matchArtifacts(String pattern);

    List matchArtifacts(Pattern pattern);

    String getSystem();

    String getVersion();

    Date getTimestamp();

    boolean isRefined();

    boolean isCoarse();

}
