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
package org.acre.model.metamodel;

public class RelationshipInfo {
    private String forwardName;
    private MetaType forwardType;
    private MetaRelationship forwardRel;

    private String reverseName;
    private MetaType reverseType;
    private MetaRelationship reverseRel;

    public RelationshipInfo(String typeName, String relName) {
        forwardName = relName;
        forwardType = FactMetaModel.getInstance().lookupMetaType(typeName);
        if (forwardType == null) {
            throw new IllegalArgumentException("Cannot find MetaType: " + typeName);
        }
        forwardRel = forwardType.lookupInMetaRelationships(relName);
        if (forwardRel == null) {
            throw new IllegalArgumentException("Cannot find MetaRelationship: " + relName + " for type " + typeName);
        }

        reverseType = forwardRel.getPointerMetaType();
        reverseName = forwardRel.getInverseName();
        reverseRel = reverseType.lookupInMetaRelationships(reverseName);
        if (reverseRel == null) {
            throw new IllegalArgumentException("Cannot find MetaRelationship: " + forwardRel.getInverseName() + " for type " + reverseName);
        }
    }

    public String getReverseName() {
        return reverseName;
    }
    public MetaType getReverseType() {
        return reverseType;
    }
    public String getForwardName() {
        return forwardName;
    }
    public MetaType getForwardType() {
        return forwardType;
    }
    public MetaRelationship getForwardRel() {
        return forwardRel;
    }
    public MetaRelationship getReverseRel() {
        return reverseRel;
    }
    public boolean isOneToOne() {
        return !forwardRel.getCollection() && !reverseRel.getCollection();
    }
    public boolean isOneToMany() {
        return !forwardRel.getCollection() && reverseRel.getCollection();
    }
    public boolean isManyToOne() {
        return forwardRel.getCollection() && !reverseRel.getCollection();
    }
    public boolean isManyToMany() {
        return forwardRel.getCollection() && reverseRel.getCollection();
    }
    public String getJoinTableName() {
        if (!isManyToMany())
            throw new IllegalArgumentException("Cannot have join table for MetaRelationship that is not m:n: " + forwardRel.getInverseName() + " for type " + reverseName);
        if (forwardRel.isPrimarySide()) {
            return forwardType.getName() + '_' + forwardName;
        } else {
            return reverseType.getName() + '_' + reverseName;
        }
    }
    public String getTransitiveJoinTableName() {
        String result;
        if (forwardRel.isPrimarySide()) {
            result = forwardType.getName() + '_' + forwardName;
        } else {
            result = reverseType.getName() + '_' + reverseName;
        }
        return result + "Transitive";
    }
}